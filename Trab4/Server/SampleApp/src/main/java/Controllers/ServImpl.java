package Controllers;

import Travel.Location;
import Travel.Lodging;
import Travel.PlaneTicket;
import Travel.TravelPackage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.util.*;


@Path("/agencia")
public class ServImpl implements InterfaceServ {
	private static final int MILLIS_IN_DAY = 86400000;
	private static ArrayList<PlaneTicket> listPlaneTickets;
	private static ArrayList<Lodging> listLodgings;
	private static ArrayList<TravelPackage> listTravelPackages;

    /**
     * Returns a list of default PlaneTickets
     * @return the list with 4 PlaneTickets
     */
    protected static final ArrayList<PlaneTicket> defaultPlaneTickets() {
        ArrayList<PlaneTicket> planeTickets = new ArrayList<PlaneTicket>();
        try {
            planeTickets.add(new PlaneTicket(Location.ARACAJU, Location.CURITIBA, "2018-11-15 08:30:00", "2018-11-25 16:00:00", 150000, 200));
            planeTickets.add(new PlaneTicket(Location.SAO_PAULO, Location.SALVADOR, "2018-10-14 12:30:00", null, 90000, 150));
            planeTickets.add(new PlaneTicket(Location.CURITIBA, Location.MANAUS, "2018-12-01 21:00:00", null, 100000, 150));
            planeTickets.add(new PlaneTicket(Location.CURITIBA, Location.MANAUS, "2018-12-01 21:00:00", "2018-12-05 10:00:00", 125000, 125));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return planeTickets;
    }

    /**
     * Returns a list of default Lodgings
     * @return the list with 4 Lodgings
     */
    protected static final ArrayList<Lodging> defaultLodgings() {
        ArrayList<Lodging> lodgings = new ArrayList<Lodging>();
        try {
            lodgings.add(new Lodging(Location.ARACAJU, "2018-11-15", "2018-11-25", 90000, 120));
            lodgings.add(new Lodging(Location.CURITIBA, "2018-12-02", "2018-12-05", 100000, 100));
            lodgings.add(new Lodging(Location.SAO_PAULO, "2018-10-20", "2018-10-22", 40000, 150));
            lodgings.add(new Lodging(Location.MANAUS, "2018-12-30", "2019-01-04", 125000, 80));
            lodgings.add(new Lodging(Location.FLORIANOPOLIS, "2018-11-25", "2018-11-30", 90000, 120));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lodgings;
    }

	/**
	 * Simple constructor for ServImpl with empty lists
	 */
	public ServImpl()  {
		listPlaneTickets = defaultPlaneTickets();
		listLodgings = defaultLodgings();
		listTravelPackages = new ArrayList<TravelPackage>();

	}

	/**
	 * Simple constructor for ServImpl with pre existing lists
	 * @param listPlaneTickets list of plane tickets
	 * @param listLodgings list of lodgings
	 * @param listTravelPackages list of travel packages
	 */
	public ServImpl(ArrayList<PlaneTicket> listPlaneTickets, ArrayList<Lodging> listLodgings, ArrayList<TravelPackage> listTravelPackages)  {
		if (listPlaneTickets == null) {
			throw new NullPointerException("Parameter listPlaneTickets cannot be null");
		}
		if (listLodgings == null) {
			throw new NullPointerException("Parameter listLodgings cannot be null");
		}
		if (listTravelPackages == null) {
			throw new NullPointerException("Parameter listTravelPackages cannot be null");
		}

		this.listPlaneTickets = defaultPlaneTickets();
		this.listLodgings = defaultLodgings();
		this.listTravelPackages = listTravelPackages;

	}

	/**
	 * Adds a new plane ticket to the system and notifies any clients that registered interest and matched
	 * @param planeTicket the PlaneTicket to add
	 */
	public void addPlaneTicket(PlaneTicket planeTicket) {
		synchronized (listPlaneTickets) {
			listPlaneTickets.add(planeTicket);
		}
		/*
		// First check if there is a key pair that matches the desired destiny and departureDate
		if (planeTicketEvents.containsKey(planeTicket.getDestiny()) &&
		    planeTicketEvents.get(planeTicket.getDestiny()).containsKey((int) (planeTicket.getDepartureDate().getTime()/MILLIS_IN_DAY))) {
			// Loop through the existing events and notify the ones that match
			for (PlaneTicketEvent event : planeTicketEvents.get(planeTicket.getDestiny()).get((int) (planeTicket.getDepartureDate().getTime()/MILLIS_IN_DAY))) {
				// destiny and departure date filters are checked
				// check maximumPrice
				if (event.getMaximumPrice() > 0 && event.getMaximumPrice() < planeTicket.getPrice()) { continue; }

				// check origin filter
				if (event.getOrigin() != null && event.getOrigin() != planeTicket.getOrigin()) { continue; }

				// check returnDate filter
				// To check that it's the same day, calculates the Julian Day Number
				if (event.getReturnDate() != null && planeTicket.getReturnDate().getTime()/MILLIS_IN_DAY != event.getReturnDate().getTime()/MILLIS_IN_DAY) { continue; }

				// Passed all checks, notify client
				try {
					event.notifyClient(planeTicket);
				} catch (RemoteException e) {
					// Remove from interest list?
				}
			}
		}
		*/
	}

	/**
	 * Adds a new lodging to the system and notifies any clients that registered interest and matched
	 * @param lodging the Lodging to add
	 */
	public void addLodging(Lodging lodging) {
		synchronized (listLodgings) {
			listLodgings.add(lodging);
		}

		/*
		// First check if there is a key pair that matches the desired location and checkIn
		if (lodgingEvents.containsKey(lodging.getLocation()) &&
		    lodgingEvents.get(lodging.getLocation()).containsKey((int) (lodging.getCheckIn().getTime()/MILLIS_IN_DAY))) {
			// Loop through the existing events and notify the ones that match
			for (LodgingEvent event : lodgingEvents.get(lodging.getLocation()).get((int) (lodging.getCheckIn().getTime()/MILLIS_IN_DAY))) {
				// location and checkIn filters are checked
				// check maximumPrice
				if (event.getMaximumPrice() > 0 && event.getMaximumPrice() < lodging.getPrice()) { continue; }

				// check the checkOut filter
				// To check that it's the same day, calculates the Julian Day Number
				if (event.getCheckOut() != null && lodging.getCheckOut().getTime()/MILLIS_IN_DAY != event.getCheckOut().getTime()/MILLIS_IN_DAY) { continue; }

				// Passed all checks, notify client
				try {
					event.notifyClient(lodging);
				} catch (RemoteException e) {
					// Remove from interest list?
				}
			}
		}
		*/
	}

	/**
	 * Adds a new travel package to the system and notifies any clients that registered interest and matched
	 * @param travelPackage the TravelPackage to add
	 */
	public void addTravelPackage(TravelPackage travelPackage) {
		synchronized (listTravelPackages) {
			listTravelPackages.add(travelPackage);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Path("/lodgings")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLodgings()  {
        return Response.status(Response.Status.OK).entity(listLodgings).build();
	}

    /**
     * {@inheritDoc}
     */
    @Path("/planetickets")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaneTickets()  {
        return Response.status(Response.Status.OK).entity(listPlaneTickets).build();
    }
	
	/**
	 * {@inheritDoc}
	 */
	public ArrayList<Lodging> getLodgings(Location location, int maxPrice, Date checkIn, Date checkOut, int minimumRooms)  {
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
	/*public ArrayList<PlaneTicket> getPlaneTickets()  {
		return listPlaneTickets;
	}*/
	
	/**
	 * {@inheritDoc}
	 */
	public ArrayList<PlaneTicket> getPlaneTickets(Location origin, Location destiny, int maxPrice, Date departureDate, Date returnDate, int minimumSeats)  {
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
	public ArrayList<TravelPackage> getTravelPackages()  {
		return listTravelPackages;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<TravelPackage> getTravelPackages(Location origin, Location destiny, int maxPrice, Date departureDate, Date returnDate, int minimumAvailable)  {
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
			if (departureDate != null && travelPackage.getPlaneTicket().getDepartureDate().getTime()/MILLIS_IN_DAY != (departureDate.getTime()/MILLIS_IN_DAY)) { continue; }
			
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
	public boolean buyPlaneTicket(int planeTicketID, int numTickets)  {
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
	public boolean buyLodging(int lodgingID, int numRooms)  {
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
	public boolean buyTravelPackage(int travelPackageID, int numPackages)  {
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

	/*
	/**
	 * {@inheritDoc}
	 *
	@Override
	public int interestPlaneTicket(Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) throws RemoteException {
		PlaneTicketEvent newInterest = new PlaneTicketEvent(origin, destiny, departureDate, returnDate, maximumPrice, clientReference);

		synchronized (planeTicketEvents) {
			// Check if there's already a key for this destiny, otherwise create one
			if (planeTicketEvents.containsKey(destiny)) {
				// Check if there's already a key for this date, otherwise create one
				if (planeTicketEvents.get(destiny).containsKey((int) (departureDate.getTime()/MILLIS_IN_DAY))) {
					// Just add the new interest to a pre-existing list
					planeTicketEvents.get(destiny).get((int) (departureDate.getTime()/MILLIS_IN_DAY)).add(newInterest);
				} else {
					// There was a destiny, but no date, add the date
					planeTicketEvents.get(destiny).put((int) (departureDate.getTime()/MILLIS_IN_DAY), new ArrayList<PlaneTicketEvent>());
					planeTicketEvents.get(destiny).get((int) (departureDate.getTime()/MILLIS_IN_DAY)).add(newInterest);
				}
			} else {
				// There's nothing related to that destiny, add everything
				planeTicketEvents.put(destiny, new HashMap<Integer, ArrayList<PlaneTicketEvent>>());
				planeTicketEvents.get(destiny).put((int) (departureDate.getTime()/MILLIS_IN_DAY), new ArrayList<PlaneTicketEvent>());
				planeTicketEvents.get(destiny).get((int) (departureDate.getTime()/MILLIS_IN_DAY)).add(newInterest);
			}
		}

		return newInterest.getId();
	}

	/**
	 * {@inheritDoc}
	 *
	@Override
	public int interestLodging(Location location, Date checkIn, Date checkOut, int maximumPrice, InterfaceCli clientReference) throws RemoteException {
		LodgingEvent newInterest = new LodgingEvent(location, checkIn, checkOut, maximumPrice, clientReference);

		synchronized (lodgingEvents) {
			// Check if there's already a key for this destiny, otherwise create one
			if (lodgingEvents.containsKey(location)) {
				// Check if there's already a key for this date, otherwise create one
				if (lodgingEvents.get(location).containsKey((int) checkIn.getTime()/MILLIS_IN_DAY)) {
					// Just add the new interest to a pre-existing list
					lodgingEvents.get(location).get((int) checkIn.getTime()/MILLIS_IN_DAY).add(newInterest);
				} else {
					// There was a destiny, but no date, add the date
					lodgingEvents.get(location).put((int) checkIn.getTime()/MILLIS_IN_DAY, new ArrayList<LodgingEvent>());
					lodgingEvents.get(location).get((int) checkIn.getTime()/MILLIS_IN_DAY).add(newInterest);
				}
			} else {
				// There's nothing related to that destiny, add everything
				lodgingEvents.put(location, new HashMap<Integer, ArrayList<LodgingEvent>>());
				lodgingEvents.get(location).put((int) checkIn.getTime()/MILLIS_IN_DAY, new ArrayList<LodgingEvent>());
				lodgingEvents.get(location).get((int) checkIn.getTime()/MILLIS_IN_DAY).add(newInterest);
			}
		}

		return newInterest.getId();
	}

	/**
	 * {@inheritDoc}
	 *
	@Override
	public int interestTravelPackage(Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) throws RemoteException {
		TravelPackageEvent newInterest = new TravelPackageEvent(origin, destiny, departureDate, returnDate, maximumPrice, clientReference);

		synchronized (travelPackageEvents) {
			// Check if there's already a key for this destiny, otherwise create one
			if (travelPackageEvents.containsKey(destiny)) {
				// Check if there's already a key for this date, otherwise create one
				if (travelPackageEvents.get(destiny).containsKey((int) (departureDate.getTime()/MILLIS_IN_DAY))) {
					// Just add the new interest to a pre-existing list
					travelPackageEvents.get(destiny).get((int) (departureDate.getTime()/MILLIS_IN_DAY)).add(newInterest);
				} else {
					// There was a destiny, but no date, add the date
					travelPackageEvents.get(destiny).put((int) (departureDate.getTime()/MILLIS_IN_DAY), new ArrayList<TravelPackageEvent>());
					travelPackageEvents.get(destiny).get((int) (departureDate.getTime()/MILLIS_IN_DAY)).add(newInterest);
				}
			} else {
				// There's nothing related to that destiny, add everything
				travelPackageEvents.put(destiny, new HashMap<Integer, ArrayList<TravelPackageEvent>>());
				travelPackageEvents.get(destiny).put((int) (departureDate.getTime()/MILLIS_IN_DAY), new ArrayList<TravelPackageEvent>());
				travelPackageEvents.get(destiny).get((int) (departureDate.getTime()/MILLIS_IN_DAY)).add(newInterest);
			}
		}

		return newInterest.getId();
	}

	/**
	 * {@inheritDoc}
	 *
	@Override
	public boolean removeInterestPlaneTicket(int id, Location destiny, Date departureDate) throws RemoteException {
		if (planeTicketEvents.containsKey(destiny) && planeTicketEvents.get(destiny).containsKey((int)(departureDate.getTime()/MILLIS_IN_DAY))) {
			synchronized (planeTicketEvents) {
				try {
					return planeTicketEvents.get(destiny).get((int) (departureDate.getTime()/MILLIS_IN_DAY)).removeIf(p -> p.getId() == id);
				}
				catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	@Override
	public boolean removeInterestLodging(int id, Location location, Date checkIn) throws RemoteException {
		if (lodgingEvents.containsKey(location) && lodgingEvents.get(location).containsKey((int)checkIn.getTime()/MILLIS_IN_DAY)) {
			synchronized (lodgingEvents) {
				return lodgingEvents.get(location).get((int) (checkIn.getTime()/MILLIS_IN_DAY)).removeIf(p -> p.getId() == id);
			}
		}
		return false;
	}

	/**
	* {@inheritDoc}
	*
	@Override
	public boolean removeInterestTravelPackage(int id, Location destiny, Date departureDate) throws RemoteException {
		if (travelPackageEvents.containsKey(destiny) && travelPackageEvents.get(destiny).containsKey((int)(departureDate.getTime()/MILLIS_IN_DAY))) {
			synchronized (travelPackageEvents) {
				return travelPackageEvents.get(destiny).get((int) (departureDate.getTime()/MILLIS_IN_DAY)).removeIf(p -> p.getId() == id);
			}
		}
		return false;
	}
	*/
}
