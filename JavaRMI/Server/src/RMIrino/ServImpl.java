package RMIrino;

import Travel.Location;
import Travel.Lodging;
import Travel.PlaneTicket;
import Travel.TravelPackage;
import TravelEvent.LodgingEvent;
import TravelEvent.PlaneTicketEvent;
import TravelEvent.TravelPackageEvent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ {
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final int MILLIS_IN_DAY = 86400000;
	private ArrayList<PlaneTicket> listPlaneTickets;
	private ArrayList<Lodging> listLodgings;
	private ArrayList<TravelPackage> listTravelPackages;

	Map<Location, Map<Date, ArrayList<PlaneTicketEvent>>> planeTicketEvents;
	Map<Location, Map<Date, ArrayList<LodgingEvent>>> lodgingEvents;
	Map<Location, Map<Date, ArrayList<TravelPackageEvent>>> travelPackageEvents;

	/**
	 * Instantiates the HashMaps for events
	 * Will be cannot on the constructors for ServImpl,
	 * there's no need to call it again unless you want to destroy the previous events
	 */
	private final void instantiateMaps() {
		this.planeTicketEvents = new HashMap<Location, Map<Date, ArrayList<PlaneTicketEvent>>>();
		this.lodgingEvents = new HashMap<Location, Map<Date, ArrayList<LodgingEvent>>>();
		this.travelPackageEvents = new HashMap<Location, Map<Date, ArrayList<TravelPackageEvent>>>();
	}

	/**
	 * Simple constructor for ServImpl with empty lists
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	public ServImpl() throws RemoteException {
		listPlaneTickets = new ArrayList<PlaneTicket>();
		listLodgings = new ArrayList<Lodging>();
		listTravelPackages = new ArrayList<TravelPackage>();

		instantiateMaps();
	}

	/**
	 * Simple constructor for ServImpl with pre existing lists
	 * @param listPlaneTickets list of plane tickets
	 * @param listLodgings list of lodgings
	 * @param listTravelPackages list of travel packages
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	public ServImpl(ArrayList<PlaneTicket> listPlaneTickets, ArrayList<Lodging> listLodgings, ArrayList<TravelPackage> listTravelPackages) throws RemoteException {
		if (listPlaneTickets == null) {
			throw new NullPointerException("Parameter listPlaneTickets cannot be null");
		}
		if (listLodgings == null) {
			throw new NullPointerException("Parameter listLodgings cannot be null");
		}
		if (listTravelPackages == null) {
			throw new NullPointerException("Parameter listTravelPackages cannot be null");
		}

		this.listPlaneTickets = listPlaneTickets;
		this.listLodgings = listLodgings;
		this.listTravelPackages = listTravelPackages;

		instantiateMaps();
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
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<TravelPackage> getTravelPackages() throws RemoteException {
		return listTravelPackages;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<TravelPackage> getTravelPackages(Location origin, Location destiny, int maxPrice, Date departureDate, Date returnDate, int minimumAvailable) throws RemoteException {
		ArrayList<TravelPackage> filteredTravelPackages = new ArrayList<TravelPackage>();
		
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
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyPlaneTicket(int planeTicketID, int numTickets) throws RemoteException {
		for (PlaneTicket planeTicket : listPlaneTickets) {
			if (planeTicket.getId() == planeTicketID) {
				synchronized (planeTicket) {
					if (planeTicket.getNumSeats() < numTickets) {
						return false;
					}
					
					System.out.println("Client just successfully bought " + numTickets + " tickets:\n" +
											   planeTicket);
					planeTicket.setNumSeats(planeTicket.getNumSeats() - numTickets);
					return true;
				}
			}
		}
		return false;
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyTravelPackage(int travelPackageID, int numPackages) throws RemoteException {
		for (TravelPackage travelPackage : listTravelPackages) {
			if (travelPackage.getId() == travelPackageID) {
				synchronized (travelPackage) {
					if (travelPackage.getAvailable() < numPackages) {
						return false;
					}
					
					System.out.println("Client just successfully bought " + numPackages + " travel packages:\n" +
											   travelPackage);
					travelPackage.decreaseAvailable(numPackages);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int interestPlaneTicket(Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) throws RemoteException {
		PlaneTicketEvent newInterest = new PlaneTicketEvent(origin, destiny, departureDate, returnDate, maximumPrice, clientReference);

		// Check if there's already a key for this destiny, otherwise create one
		if (planeTicketEvents.containsKey(destiny)) {
			// Check if there's already a key for this date, otherwise create one
			if (planeTicketEvents.get(destiny).containsKey(departureDate)) {
				// Just add the new interest to a pre-existing list
				planeTicketEvents.get(destiny).get(departureDate).add(newInterest);
			}
			else {
				// There was a destiny, but no date, add the date
				planeTicketEvents.get(destiny).put(departureDate, new ArrayList<PlaneTicketEvent>());
				planeTicketEvents.get(destiny).get(departureDate).add(newInterest);
			}
		}
		else {
			// There's nothing related to that destiny, add everything
			planeTicketEvents.put(destiny, new HashMap<Date, ArrayList<PlaneTicketEvent>>());
			planeTicketEvents.get(destiny).put(departureDate, new ArrayList<PlaneTicketEvent>());
			planeTicketEvents.get(destiny).get(departureDate).add(newInterest);
		}

		return newInterest.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int interestLodging(Location location, Date checkIn, Date checkOut, int maximumPrice, InterfaceCli clientReference) throws RemoteException {
		LodgingEvent newInterest = new LodgingEvent(location, checkIn, checkOut, maximumPrice, clientReference);

		// Check if there's already a key for this destiny, otherwise create one
		if (lodgingEvents.containsKey(location)) {
			// Check if there's already a key for this date, otherwise create one
			if (lodgingEvents.get(location).containsKey(checkIn)) {
				// Just add the new interest to a pre-existing list
				lodgingEvents.get(location).get(checkIn).add(newInterest);
			}
			else {
				// There was a destiny, but no date, add the date
				lodgingEvents.get(location).put(checkIn, new ArrayList<LodgingEvent>());
				lodgingEvents.get(location).get(checkIn).add(newInterest);
			}
		}
		else {
			// There's nothing related to that destiny, add everything
			lodgingEvents.put(location, new HashMap<Date, ArrayList<LodgingEvent>>());
			lodgingEvents.get(location).put(checkIn, new ArrayList<LodgingEvent>());
			lodgingEvents.get(location).get(checkIn).add(newInterest);
		}

		return newInterest.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int interestTravelPackage(Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) throws RemoteException {
		TravelPackageEvent newInterest = new TravelPackageEvent(origin, destiny, departureDate, returnDate, maximumPrice, clientReference);

		// Check if there's already a key for this destiny, otherwise create one
		if (travelPackageEvents.containsKey(destiny)) {
			// Check if there's already a key for this date, otherwise create one
			if (travelPackageEvents.get(destiny).containsKey(departureDate)) {
				// Just add the new interest to a pre-existing list
				travelPackageEvents.get(destiny).get(departureDate).add(newInterest);
			}
			else {
				// There was a destiny, but no date, add the date
				travelPackageEvents.get(destiny).put(departureDate, new ArrayList<TravelPackageEvent>());
				travelPackageEvents.get(destiny).get(departureDate).add(newInterest);
			}
		}
		else {
			// There's nothing related to that destiny, add everything
			travelPackageEvents.put(destiny, new HashMap<Date, ArrayList<TravelPackageEvent>>());
			travelPackageEvents.get(destiny).put(departureDate, new ArrayList<TravelPackageEvent>());
			travelPackageEvents.get(destiny).get(departureDate).add(newInterest);
		}

		return newInterest.getId();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeInterestPlaneTicket(int id, Location destiny, Date departureDate) throws RemoteException {
		if (planeTicketEvents.containsKey(destiny) && planeTicketEvents.get(destiny).containsKey(departureDate)) {
			Predicate<PlaneTicketEvent> planeTicketPredicate = p -> p.getId() == id;
			return planeTicketEvents.get(destiny).get(departureDate).removeIf(planeTicketPredicate);
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeInterestLodging(int id, Location location, Date checkIn) throws RemoteException {
		if (lodgingEvents.containsKey(location) && lodgingEvents.get(location).containsKey(checkIn)) {
			Predicate<LodgingEvent> lodgingPredicate = p -> p.getId() == id;
			return lodgingEvents.get(location).get(checkIn).removeIf(lodgingPredicate);
		}
		return false;
	}

	/**
	* {@inheritDoc}
	*/
	@Override
	public boolean removeInterestTravelPackage(int id, Location destiny, Date departureDate) throws RemoteException {
		if (travelPackageEvents.containsKey(destiny) && travelPackageEvents.get(destiny).containsKey(departureDate)) {
			Predicate<TravelPackageEvent> travelPackagePredicate = p -> p.getId() == id;
			return travelPackageEvents.get(destiny).get(departureDate).removeIf(travelPackagePredicate);
		}
		return false;
	}
}
