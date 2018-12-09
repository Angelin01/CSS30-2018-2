package RMIrino;

import SimpleFileAccess.RecordsFile;
import SimpleFileAccess.RecordsFileException;
import Travel.Location;
import Travel.Lodging;

import java.io.BufferedReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Logger;

public class LodgingImpl extends UnicastRemoteObject implements InterfaceLodging {
	private static final int MILLIS_IN_DAY = 86400000;
	private final ArrayList<Lodging> listLodgings;
	private ArrayList<Lodging> tmpPlaneTickets;
	private final Logger logger;
	private final RecordsFile db;
	private final RecordsFile tmpDb;
	private final RecordsFile transactionLog;

	public LodgingImpl(RecordsFile db, RecordsFile tmpDb, RecordsFile transactionLog, Logger logger) throws IOException, RecordsFileException, ClassNotFoundException {
		this.logger = logger;
		this.db = db;
		this.tmpDb = tmpDb;
		this.transactionLog = transactionLog;

		listLodgings = new ArrayList<Lodging>();
		readLodgings();
	}

	protected void readLodgings() throws IOException, ClassNotFoundException {
		logger.info("Reading plane tickets from database...");
		Lodging planeTicket = null;
		synchronized (listLodgings) {
			listLodgings.clear();
		}

		synchronized (db) {
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
		tmpPlaneTickets =  new ArrayList<>(listLodgings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Lodging> getLodgings() throws RemoteException {
		return listLodgings;
	}

	/**
	 * {@inheritDoc}
	 */
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
	public boolean buyLodging(int lodgingID, int numRooms, boolean isPackage) throws RemoteException {
		for (Lodging lodging : listLodgings) {
			if (lodging.getId() == lodgingID) {
				synchronized (lodging) {
					if (lodging.getNumRooms() < numRooms) {
						return false;
					}

					System.out.println("Client just successfully bought:\n" +
							lodging + "\n" +
							"Number of rooms: " + numRooms);
					lodging.setNumRooms(lodging.getNumRooms() - numRooms);
					return true;
				}
			}
		}
		return false;
	}
}
