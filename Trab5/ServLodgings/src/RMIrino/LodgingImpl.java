package RMIrino;

import Travel.Location;
import Travel.Lodging;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LodgingImpl extends UnicastRemoteObject implements InterfaceLodging {
	private static final int MILLIS_IN_DAY = 86400000;

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
	public boolean buyLodging(int lodgingID, int numRooms) throws RemoteException {
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
