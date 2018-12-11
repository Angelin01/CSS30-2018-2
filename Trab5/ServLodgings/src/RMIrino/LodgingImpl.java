package RMIrino;

import SimpleFileAccess.RecordWriter;
import SimpleFileAccess.RecordsFile;
import SimpleFileAccess.RecordsFileException;
import Travel.Location;
import Travel.Lodging;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Logger;

public class LodgingImpl extends UnicastRemoteObject implements InterfaceLodging {
	private static final int KEY_ID = -1;
	private static final int KEY_NUM = -2;
	private static final int KEY_OBJECT = -3;
	private static final int KEY_STATUS = -4;
	private static final int MILLIS_IN_DAY = 86400000;

	private final ReentrantReadWriteLock rrwlMain;
	private final ReentrantReadWriteLock rrwlTmp;
	
	private final ArrayList<Lodging> listLodgings;
	private final Logger logger;
	private final RecordsFile db;
	private final RecordsFile tmpDb;
	private final RecordsFile transactionLog;

	private RecordWriter transactionWriter;

	public LodgingImpl(RecordsFile db, RecordsFile tmpDb, RecordsFile transactionLog, InterfaceCoord interfaceCoord, Logger logger) throws IOException, RecordsFileException, ClassNotFoundException {
		this.logger = logger;
		this.db = db;
		this.tmpDb = tmpDb;
		this.transactionLog = transactionLog;

		rrwlMain = new ReentrantReadWriteLock();
		rrwlTmp = new ReentrantReadWriteLock();

		// Check if there is a pending transaction
		if (transactionLog.readRecord(KEY_STATUS).readObject().equals("STARTING")) {
			rrwlTmp.writeLock().lock();
			rrwlMain.writeLock().lock();

			// The return from the coordinator will be a boolean, just pass it as an argument to commit
			commitTransaction(interfaceCoord.continueTransaction((int) transactionLog.readRecord(KEY_ID).readObject()));
		}

		transactionWriter = null;

		listLodgings = new ArrayList<Lodging>();
		readLodgings();
	}

	/**
	 * Reads all lodgings from the database and adds them to the memory listLodgings
	 * Will also check the ID on the Lodgings to avoid collisions
	 * Does NOT call locks on the database, must be done outside of this method if needed
	 */
	protected void readLodgings() throws IOException, ClassNotFoundException {
		logger.info("Reading lodgings from database...");
		Lodging planeTicket = null;
		synchronized (listLodgings) {
			listLodgings.clear();

			for (Enumeration<Integer> e = db.enumerateKeys(); e.hasMoreElements(); ) {
				try {
					planeTicket = (Lodging) db.readRecord(e.nextElement()).readObject();
				} catch (RecordsFileException e1) {
					logger.severe("Error reading from database. Something bad happened, oh no...");
					System.exit(1);
				}

				// Checks the ids to avoid duplicates
				// KINDA DUMB, but it works for the most part
				if (Lodging.nextId <= planeTicket.getId()) {
					Lodging.nextId = planeTicket.getId() + 1;
				}

				synchronized (listLodgings) {
					listLodgings.add(planeTicket);
				}
			}
		}
		logger.info("Successfully read database");
	}

	public void addLodging(Lodging lodging) throws IOException, RecordsFileException {
		RecordWriter rw = new RecordWriter(lodging.getId());
		rw.writeObject(lodging);
		logger.info("New planeticket added: " + lodging);

		rrwlMain.writeLock().lock();
		db.insertRecord(rw);
		rrwlMain.writeLock().unlock();

		synchronized (listLodgings) {
			listLodgings.add(lodging);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void commitTransaction(boolean complete) throws RemoteException, IOException, ClassNotFoundException, RecordsFileException {
		if (complete) {
			logger.info("Commiting updates to main database");

			// Copy from tmpDb to db
			FileChannel src = new FileInputStream(tmpDb.getDbPath()).getChannel();
			FileChannel dest = new FileOutputStream(db.getDbPath()).getChannel();
			dest.transferFrom(src, 0, src.size());

			readLodgings();
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
	public ArrayList<Lodging> getLodgings() throws RemoteException {
		return listLodgings; //
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
	@Override
	public ArrayList<Lodging> getLodgings(Location location, int maxPrice, Date checkIn, Date checkOut, int minimumRooms) throws RemoteException {
		ArrayList<Lodging> filteredLodgings = new ArrayList<Lodging>();

		for (Lodging lodging : listLodgings) {
			// Check the location filter
			if (location != null && lodging.getLocation() != location) { continue; }

			// Check the price filter
			if (maxPrice > 0 && lodging.getPrice() > maxPrice) { continue; }

			// Check the checkIn filter
			// To check that it's the same day, calculates the Julian Day Number
			if (checkIn != null && lodging.getCheckIn().getTime()/MILLIS_IN_DAY != checkIn.getTime()/MILLIS_IN_DAY) { continue; }

			// Check the checkOut filter
			// To check that it's the same day, calculates the Julian Day Number
			if (checkOut != null && lodging.getCheckOut().getTime()/MILLIS_IN_DAY != checkOut.getTime()/MILLIS_IN_DAY) { continue; }

			// Check the minimum available rooms filter
			if (minimumRooms > 0 && lodging.getNumRooms() < minimumRooms) { continue; }

			// Passed all filters, add to return list
			filteredLodgings.add(lodging);
		}

		return filteredLodgings;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyLodging(int lodgingID, int numRooms) throws RemoteException, RecordsFileException, ClassNotFoundException, IOException {
		logger.info("Request to buy plane ticket " + lodgingID + ", " + " tickets");
		Lodging planeTicket;
		rrwlMain.readLock().lock();
		try {
			planeTicket = (Lodging) db.readRecord(lodgingID).readObject();
		} catch (RecordsFileException e) {
			rrwlMain.readLock().unlock();
			return false;
		}

		if (planeTicket.getNumRooms() >= numRooms) {
			rrwlMain.writeLock().lock();

			RecordWriter rw = new RecordWriter(lodgingID);
			planeTicket.setNumRooms(planeTicket.getNumRooms() - numRooms);
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
	public boolean buyPackageLodging(int lodgingID, int numRooms, int idTransaction) throws RemoteException, RecordsFileException, IOException, ClassNotFoundException {
		logger.info("Received transaction request from coordenator");
		Lodging planeTicket;

		RecordWriter transactionIdWriter = new RecordWriter(KEY_ID); // Key -1 always stores the ID
		RecordWriter transactionNumTicketsWriter = new RecordWriter(KEY_NUM); //
		RecordWriter transactionObjectWriter = new RecordWriter(KEY_OBJECT);
		RecordWriter transactionStatusWriter = new RecordWriter(KEY_STATUS);

		rrwlMain.writeLock().lock();
		rrwlTmp.writeLock().lock();

		// Copy from db to tmpDb
		FileChannel src = new FileInputStream(db.getDbPath()).getChannel();
		FileChannel dest = new FileOutputStream(tmpDb.getDbPath()).getChannel();
		dest.transferFrom(src, 0, src.size());

		try {
			planeTicket = (Lodging) tmpDb.readRecord(lodgingID).readObject();
		} catch (RecordsFileException e) {
			logger.info("Invalid ID on transaction, aborting.");
			rrwlTmp.writeLock().unlock();
			rrwlMain.writeLock().unlock();
			return false;
		}

		logger.info("Found relevant plane ticket, starting");

		// Save transaction stuffs in case of failure
		transactionStatusWriter.writeObject("STARTING");
		transactionObjectWriter.writeObject(planeTicket);
		transactionIdWriter.writeObject(idTransaction);
		transactionNumTicketsWriter.writeObject(numRooms);

		transactionLog.insertRecord(transactionObjectWriter);
		transactionLog.insertRecord(transactionIdWriter);
		transactionLog.insertRecord(transactionNumTicketsWriter);
		transactionLog.insertRecord(transactionStatusWriter); // Write status LAST to be sure all relevant data was saved

		if (planeTicket.getNumRooms() >= numRooms) {
			RecordWriter rw = new RecordWriter(lodgingID);
			planeTicket.setNumRooms(planeTicket.getNumRooms() - numRooms);
			rw.writeObject(planeTicket);
			tmpDb.insertRecord(rw);

			this.transactionWriter = transactionStatusWriter;

			logger.info("Operations done, waiting on commit from coordenator");

			return true;
		}

		transactionStatusWriter.writeObject("FAILED"); // FAILED isn't bad, just means it was aborted
		transactionLog.insertRecord(transactionStatusWriter);
		return false;
	}
}
