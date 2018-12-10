package RMIrino;

import SimpleFileAccess.RecordReader;
import SimpleFileAccess.RecordWriter;
import SimpleFileAccess.RecordsFile;
import SimpleFileAccess.RecordsFileException;
import Travel.Location;
import Travel.PlaneTicket;
import com.sun.prism.impl.Disposer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

public class PlaneTicketImpl extends UnicastRemoteObject implements InterfacePlaneTicket {
	private static final int KEY_ID = -1;
	private static final int KEY_NUM = -2;
	private static final int KEY_OBJECT = -3;
	private static final int KEY_STATUS = -4;
	private static final int MILLIS_IN_DAY = 86400000;

	private final ReentrantReadWriteLock rrwlMain;
	private final ReentrantReadWriteLock rrwlTmp;

	private final ArrayList<PlaneTicket> listPlaneTickets;
	private final Logger logger;
	private final RecordsFile db;
	private final RecordsFile tmpDb;
	private final RecordsFile transactionLog;

	private RecordWriter transactionWriter;

	public PlaneTicketImpl(RecordsFile db, RecordsFile tmpDb, RecordsFile transactionLog, Logger logger) throws IOException, RecordsFileException, ClassNotFoundException {
		this.logger = logger;
		this.db = db;
		this.tmpDb = tmpDb;
		this.transactionLog = transactionLog;

		rrwlMain = new ReentrantReadWriteLock();
		rrwlTmp = new ReentrantReadWriteLock();

		transactionWriter = null;

		listPlaneTickets = new ArrayList<PlaneTicket>();
		readPlaneTickets();
	}

	/**
	 * Reads all plane tickets from the database and adds them to the memory listPlaneTickets
	 * Will also check the ID on the PlaneTickets to avoid collisions
	 * Does NOT call locks on the database, must be done outside of this method if needed
	 */
	protected void readPlaneTickets() throws IOException, ClassNotFoundException {
		logger.info("Reading plane tickets from database...");
		PlaneTicket planeTicket = null;
		synchronized (listPlaneTickets) {
			listPlaneTickets.clear();

			for (Enumeration<Integer> e = db.enumerateKeys(); e.hasMoreElements(); ) {
				try {
					planeTicket = (PlaneTicket) db.readRecord(e.nextElement()).readObject();
				} catch (RecordsFileException e1) {
					logger.severe("Error reading from database. Something bad happened, oh no...");
					System.exit(1);
				}

				// Checks the ids to avoid duplicates
				// KINDA DUMB, but it works for the most part
				if (PlaneTicket.nextId <= planeTicket.getId()) {
					PlaneTicket.nextId = planeTicket.getId() + 1;
				}

				synchronized (listPlaneTickets) {
					listPlaneTickets.add(planeTicket);
				}
			}
		}
		logger.info("Successfully read database");
	}

	public void addPlaneTicket(PlaneTicket planeTicket) throws IOException, RecordsFileException {
		RecordWriter rw = new RecordWriter(planeTicket.getId());
		rw.writeObject(planeTicket);
		logger.info("New planeticket added: " + planeTicket);

		rrwlMain.writeLock().lock();
		db.insertRecord(rw);
		rrwlMain.writeLock().unlock();

		synchronized (listPlaneTickets) {
			listPlaneTickets.add(planeTicket);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commitTransaction(boolean complete) throws RemoteException, IOException, ClassNotFoundException, RecordsFileException {
		if (complete) {
			logger.info("Commiting updates to main database");

			FileChannel src = new FileInputStream(tmpDb.getDbPath()).getChannel();
			FileChannel dest = new FileOutputStream(db.getDbPath()).getChannel();
			dest.transferFrom(src, 0, src.size());

			readPlaneTickets();
			transactionWriter.writeObject("SUCCESS");
		}
		else {
			logger.info("Aborting updates");
			transactionWriter.writeObject("FAILED");

		}
		transactionLog.insertRecord(transactionWriter);
		rrwlTmp.writeLock().unlock();
		rrwlMain.writeLock().unlock();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<PlaneTicket> getPlaneTickets() throws RemoteException {
		return listPlaneTickets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public ArrayList<PlaneTicket> getPlaneTickets(Location origin, Location destiny, int maxPrice, Date departureDate, Date returnDate, int minimumSeats) throws RemoteException {
		ArrayList<PlaneTicket> filteredPlaneTickets = new ArrayList<PlaneTicket>();

		for (PlaneTicket planeTicket : listPlaneTickets) {
			// Check the origin filter
			if (origin != null && planeTicket.getOrigin() != origin) { continue; }

			// Check the destiny filter
			if (destiny != null && planeTicket.getDestiny() != destiny) { continue; }

			// Check the price filter
			if (maxPrice > 0 && planeTicket.getPrice() > maxPrice) { continue; }

			// Check the departure date filter
			// To check that it's the same day, calculates the Julian Day Number
			if (departureDate != null && planeTicket.getDepartureDate().getTime()/MILLIS_IN_DAY != (departureDate.getTime()/MILLIS_IN_DAY)) { continue; }

			// Check the return date filter
			// To check that it's the same day, calculates the Julian Day Number
			if (returnDate != null && planeTicket.getReturnDate().getTime()/MILLIS_IN_DAY != returnDate.getTime()/MILLIS_IN_DAY) { continue; }

			// Check the minimum available seats filter
			if (minimumSeats > 0 && planeTicket.getNumSeats() < minimumSeats) { continue; }

			// Passed all filters, add to return list
			filteredPlaneTickets.add(planeTicket);
		}

		return filteredPlaneTickets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyPlaneTicket(int planeTicketID, int numTickets) throws RemoteException, RecordsFileException, ClassNotFoundException, IOException {
		logger.info("Request to buy plane ticket " + planeTicketID + ", " + " tickets");
		PlaneTicket planeTicket;
		rrwlMain.readLock().lock();
		try {
			planeTicket = (PlaneTicket) db.readRecord(planeTicketID).readObject();
		} catch (RecordsFileException e) {
			rrwlMain.readLock().unlock();
			return false;
		}

		if (planeTicket.getNumSeats() >= numTickets) {
			rrwlMain.writeLock().lock();

			RecordWriter rw = new RecordWriter(planeTicketID);
			planeTicket.setNumSeats(planeTicket.getNumSeats() - numTickets);
			rw.writeObject(planeTicket);
			db.insertRecord(rw);

			rrwlMain.writeLock().unlock();
			rrwlMain.readLock().unlock();
			return true;
		}

		rrwlMain.readLock().unlock();

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyPackagePlaneTicket(int planeTicketID, int numTickets, int idTransaction) throws RemoteException, RecordsFileException, IOException, ClassNotFoundException {
		logger.info("Received transaction request from coordenator");
		PlaneTicket planeTicket;

		RecordWriter transactionIdWriter = new RecordWriter(KEY_ID); // Key -1 always stores the ID
		RecordWriter transactionNumTicketsWriter = new RecordWriter(KEY_NUM); //
		RecordWriter transactionObjectWriter = new RecordWriter(KEY_OBJECT);
		RecordWriter transactionStatusWriter = new RecordWriter(KEY_STATUS);

		rrwlMain.writeLock().lock();
		rrwlTmp.writeLock().lock();

		FileChannel src = new FileInputStream(db.getDbPath()).getChannel();
		FileChannel dest = new FileOutputStream(tmpDb.getDbPath()).getChannel();
		dest.transferFrom(src, 0, src.size());

		try {
			planeTicket = (PlaneTicket) tmpDb.readRecord(planeTicketID).readObject();
		} catch (RecordsFileException e) {
			logger.info("Invalid ID on transaction, aborting.");
			rrwlTmp.writeLock().unlock();
			rrwlMain.writeLock().unlock();
			return false;
		}

		logger.info("Found relevant plane ticket, starting");

		transactionStatusWriter.writeObject("STARTING");
		transactionObjectWriter.writeObject(planeTicket);
		transactionIdWriter.writeObject(idTransaction);
		transactionNumTicketsWriter.writeObject(numTickets);

		transactionLog.insertRecord(transactionStatusWriter);
		transactionLog.insertRecord(transactionObjectWriter);
		transactionLog.insertRecord(transactionIdWriter);
		transactionLog.insertRecord(transactionNumTicketsWriter);

		if (planeTicket.getNumSeats() >= numTickets) {

			RecordWriter rw = new RecordWriter(planeTicketID);
			planeTicket.setNumSeats(planeTicket.getNumSeats() - numTickets);
			rw.writeObject(planeTicket);
			tmpDb.insertRecord(rw);

			this.transactionWriter = transactionStatusWriter;

			logger.info("Operations done, waiting on commit from coordenator");

			return true;
		}

		transactionStatusWriter.writeObject("FAILED");
		transactionLog.insertRecord(transactionStatusWriter);
		return false;
	}
}
