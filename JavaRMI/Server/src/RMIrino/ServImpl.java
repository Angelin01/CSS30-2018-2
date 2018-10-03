package RMIrino;

import Travel.Location;
import Travel.Lodging;
import Travel.PlaneTicket;
import Travel.TravelPackage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ {
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public List<PlaneTicket> listPlaneTickets;
	public List<Lodging> listLodgings;
	public List<TravelPackage> listTravelPackages;

	protected ServImpl() throws RemoteException {
		listPlaneTickets = new ArrayList<PlaneTicket>();
		listLodgings = new ArrayList<Lodging>();
		listTravelPackages = new ArrayList<TravelPackage>();
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<Lodging> getLodgings() throws RemoteException {
		return listLodgings;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public List<Lodging> getLodgings(Location location, int maxPrice, Date date, int minimumRooms) throws RemoteException {
		final int MILLIS_IN_DAY = 86400000;
		List<Lodging> filteredLodgings = new ArrayList<Lodging>();
		
		for (Lodging lodging : listLodgings) {
			// Check the location filter
			if (location != null && lodging.getLocation() != location) { continue; }
			
			// Check the price filter
			if (maxPrice > 0 && lodging.getPrice() > maxPrice) { continue; }
			
			// Check the date filter
			// To check that it's the same day, calculates the Julian Day Number
			if (date != null && lodging.getDate().getTime()/MILLIS_IN_DAY != date.getTime()/MILLIS_IN_DAY) { continue; }
			
			// Check the minimum available rooms filter
			if (minimumRooms > 0 && lodging.getNumRooms() < minimumRooms) { continue; }
			
			// Passed all filters, add to return list
			filteredLodgings.add(lodging);
		}
		
		return filteredLodgings;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public List<PlaneTicket> getPlaneTickets() throws RemoteException {
		return listPlaneTickets;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public List<TravelPackage> getTravelPackages() throws RemoteException {
		return listTravelPackages;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean buyPlaneTicket(int planeTicketID) throws RemoteException {
		for (PlaneTicket planeTicket : listPlaneTickets) {
			if (planeTicket.getId() == planeTicketID) {
				System.out.println("Client just successfully bought:\n" +
								   planeTicket);
				return true;
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean buyLodging(int lodgingID, int numRooms, Date checkIn, Date checkOut) throws RemoteException {
		for (Lodging lodging : listLodgings) {
			if (lodging.getId() == lodgingID) {
				System.out.println("Client just successfully bought:\n" +
								   lodging + "\n" +
								   "Number of rooms: " + numRooms + "\n" +
								   "Check In: " + checkIn + "\n" +
								   "Check Out: " + checkOut);
				return true;
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean buyLodging(int lodgingID, int numRooms, String scheckIn, String scheckOut) throws RemoteException, ParseException {
		Date checkIn = format.parse(scheckIn);
		Date checkOut = format.parse(scheckIn);
		for (Lodging lodging : listLodgings) {
			if (lodging.getId() == lodgingID) {
				System.out.println("Client just successfully bought:\n" +
				                   lodging + "\n" +
				                   "Number of rooms: " + numRooms + "\n" +
				                   "Check In: " + checkIn + "\n" +
				                   "Check Out: " + checkOut);
				return true;
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean buyTravelPackage(int travelPackageID, int numPackages) throws RemoteException {
		for (TravelPackage travelPackage : listTravelPackages) {
			if (travelPackage.getId() == travelPackageID) {
				System.out.println("Client just successfully bought:\n" +
				                   travelPackage + "\n" +
				                   "Number of packages: " + numPackages;
				return true;
			}
		}
		return false;
	}


}
