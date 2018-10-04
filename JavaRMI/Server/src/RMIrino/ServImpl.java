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
	private static final int MILLIS_IN_DAY = 86400000;
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
	public List<Lodging> getLodgings(Location location, int maxPrice, Date checkIn, Date checkOut, int minimumRooms) throws RemoteException {
		List<Lodging> filteredLodgings = new ArrayList<Lodging>();
		
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
	public List<PlaneTicket> getPlaneTickets(Location origin, Location destiny, int maxPrice, Date departureDate, Date returnDate, int minimumSeats) throws RemoteException {
		List<PlaneTicket> filteredPlaneTickets = new ArrayList<PlaneTicket>();
		
		for (PlaneTicket planeTicket : listPlaneTickets) {
			// Check the origin filter
			if (origin != null && planeTicket.getOrigin() != origin) { continue; }
			
			// Check the destiny filter
			if (destiny != null && planeTicket.getDestiny() != destiny) { continue; }
			
			// Check the price filter
			if (maxPrice > 0 && planeTicket.getPrice() > maxPrice) { continue; }
			
			// Check the departure date filter
			// To check that it's the same day, calculates the Julian Day Number
			if (departureDate != null && planeTicket.getDepartureDate().getTime()/MILLIS_IN_DAY != departureDate.getTime()/MILLIS_IN_DAY) { continue; }
			
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
	public List<TravelPackage> getTravelPackages(Location origin, Location destiny, int maxPrice, Date departureDate, Date returnDate, int minimumAvailable) throws RemoteException {
		List<TravelPackage> filteredTravelPackages = new ArrayList<TravelPackage>();
		
		for (TravelPackage travelPackage : listTravelPackages) {
			// Check the origin filter
			if (origin != null && travelPackage.getPlaneTicket().getOrigin() != origin) { continue; }
			
			// Check the destiny filter
			if (destiny != null && travelPackage.getPlaneTicket().getDestiny() != destiny) { continue; }
			
			// Check the price filter
			if (maxPrice > 0 && travelPackage.getPrice() > maxPrice) { continue; }
			
			// Check the departure date filter
			// To check that it's the same day, calculates the Julian Day Number
			if (departureDate != null && travelPackage.getPlaneTicket().getDepartureDate().getTime()/MILLIS_IN_DAY != departureDate.getTime()/MILLIS_IN_DAY) { continue; }
			
			// Check the return date filter
			// To check that it's the same day, calculates the Julian Day Number
			if (returnDate != null && travelPackage.getPlaneTicket().getReturnDate().getTime()/MILLIS_IN_DAY != returnDate.getTime()/MILLIS_IN_DAY) { continue; }
			
			// Check the minimum available filter
			// Needs to check both the plane and the lodging
			if (minimumAvailable > 0 &&
			    (travelPackage.getPlaneTicket().getNumSeats() < minimumAvailable || travelPackage.getLodging().getNumRooms() < minimumAvailable)) { continue; }
			
			// Passed all filters, add to return list
			filteredTravelPackages.add(travelPackage);
		}
		
		return filteredTravelPackages;
	}
	
	/**
	 * @inheritDoc
	 */
	@Override
	public boolean buyPlaneTicket(int planeTicketID, int numTickets) throws RemoteException {
		for (PlaneTicket planeTicket : listPlaneTickets) {
			if (planeTicket.getId() == planeTicketID) {
				if (planeTicket.getNumSeats() < numTickets) {
					return false;
				}

				System.out.println("Client just successfully bought " + numTickets + " tickets:\n" +
								   planeTicket);
				planeTicket.setNumSeats(planeTicket.getNumSeats() - numTickets);
				return true;
			}
		}
		return false;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean buyLodging(int lodgingID, int numRooms) throws RemoteException {
		for (Lodging lodging : listLodgings) {
			if (lodging.getId() == lodgingID) {
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
