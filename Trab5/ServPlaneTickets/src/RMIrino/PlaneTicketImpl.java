package RMIrino;

import SimpleFileAccess.RecordReader;
import SimpleFileAccess.RecordWriter;
import SimpleFileAccess.RecordsFile;
import SimpleFileAccess.RecordsFileException;
import Travel.Location;
import Travel.PlaneTicket;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Logger;

public class PlaneTicketImpl extends UnicastRemoteObject implements InterfacePlaneTicket {
	private static final int MILLIS_IN_DAY = 86400000;
	private final ArrayList<PlaneTicket> listPlaneTickets;
	private ArrayList<PlaneTicket> tmpPlaneTickets;
	private Logger logger;
	private RecordsFile db;
	private RecordsFile tmpDb;

	public PlaneTicketImpl(RecordsFile db, RecordsFile tmpDb, Logger logger) throws IOException, RecordsFileException, ClassNotFoundException {
		this.logger = logger;
		this.db = db;
		this.tmpDb = tmpDb;

		listPlaneTickets = new ArrayList<PlaneTicket>();
		readPlaneTickets();
	}

	/**
	 * Reads all plane tickets from the database and adds them to the memory listPlaneTickets
	 * Will also check the ID on the PlaneTickets to avoid collisions
	 */
	protected void readPlaneTickets() throws IOException, ClassNotFoundException {
		logger.info("Reading plane tickets from database...");
		PlaneTicket planeTicket = null;
		synchronized (listPlaneTickets) {
			listPlaneTickets.clear();
		}

		synchronized (db) {
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
		tmpPlaneTickets =  new ArrayList<>(listPlaneTickets);
	}

	/**
	 * Copies the contents from the temporary db to the main db
	 * Makes a re read on the database and updates memory
	 */
	protected void commitUpdates() throws IOException, ClassNotFoundException {
		logger.info("Commiting updates to main database");
		synchronized (db) {
			synchronized (tmpDb) {
				FileChannel src = new FileInputStream(tmpDb.getDbPath()).getChannel();
				FileChannel dest = new FileOutputStream(db.getDbPath()).getChannel();
				dest.transferFrom(src, 0, src.size());
			}
		}

		readPlaneTickets();
	}

	public void addPlaneTicket(PlaneTicket planeTicket) {
		synchronized (tmpPlaneTickets) {
			tmpPlaneTickets.add(planeTicket);
		}
		logger.info("New planeticket added: " + planeTicket);
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
		synchronized (db) {
			PlaneTicket planeTicket;
			try {
				planeTicket = (PlaneTicket) db.readRecord(planeTicketID).readObject();
			} catch (RecordsFileException e) {
				return false;
			}

			if (planeTicket.getNumSeats() >= numTickets) {
				RecordWriter rw = new RecordWriter(planeTicketID);
				planeTicket.setNumSeats(planeTicket.getNumSeats() - numTickets);
				rw.writeObject(planeTicket);
				db.insertRecord(rw);
				return true;
			}
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyPackagePlaneTicket(int planeTicketID, int numTickets) throws RemoteException {
		return false;
	}
}
