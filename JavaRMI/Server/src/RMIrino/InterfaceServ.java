package RMIrino;

import Travel.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


public interface InterfaceServ extends Remote {
	/**
	 * Simple getter for the list of available lodgings
	 * @return a List of ALL Lodging objects
	 * @throws RemoteException @todo
	 */
	List<Lodging> getLodgings() throws RemoteException;

	/**
	 * Getter for Lodgings with filters
	 * @param location the desired location. null if doesn't matter
	 * @param maxPrice the maximum price. <= 0 if doesn't matter
	 * @param date Date object of interest
	 * @param minimumRooms number of minimum rooms available. <= 0 if doesn't matter
	 * @return A list of Lodgings according to the filters
	 * @throws RemoteException @todo
	 */
	List<Lodging> getLodgings(Location location, int maxPrice, Date date, int minimumRooms) throws RemoteException;

	/**
	 * Simple getter for the list of available plane tickets
	 * @return a List of ALL PlaneTickets objects
	 * @throws RemoteException @todo
	 */
	List<PlaneTicket> getPlaneTickets() throws RemoteException;

	/**
	 * Getter for PlaneTickets with filters
	 * @param origin the origin location. null if doesn't matter
	 * @param destiny the destiny location. null if doesn't matter
	 * @param maxPrice the maximum price. <= 0 if doesn't matter
	 * @param departureDate the departure date. null if doesn't matter
	 * @param returnDate the return date. null if doesn't matter. Note: having a return date implies a round-trip
	 * @param minimumSeats the minimum number of available seats. <= 0 if doesn't matter
	 * @return A list of PlaneTickets according to the filters
	 * @throws RemoteException
	 */
	List<PlaneTicket> getPlaneTickets(Location origin, Location destiny, int maxPrice, Date departureDate,
	                                  Date returnDate, int minimumSeats) throws RemoteException;

	/**
	 * Simple getter for the list of available packages
	 * @return a List of ALL TravelPackages objects
	 * @throws RemoteException @todo
	 */
	List<TravelPackage> getTravelPackages() throws RemoteException;

	/**
	 * Getter for TravelPackages with filters
	 * @param origin the origin location. null if doesn't matter
	 * @param destiny the destiny location. null if doesn't matter
	 * @param maxPrice the maximum price. <= 0 if doesn't matter
	 * @param departureDate the departure date. null if doesn't matter
	 * @param returnDate the return date. null if doesn't matter
	 * @return List of TravelPackages according to the filters
	 * @throws RemoteException @todo
	 */
	List<TravelPackage> getTravelPackages(Location origin, Location destiny, int maxPrice, Date departureDate,
	                                      Date returnDate) throws RemoteException;

	/**
	 * Method for buying a PlaneTicket
	 * @param planeTicketID the id for the desired plane
	 * @param numTickets the number of desired tickets
	 * @return true if the ticket was successfully bought, false if there was a problem
	 * @throws RemoteException @todo
	 */
	boolean buyPlaneTicket(int planeTicketID, int numTickets) throws RemoteException;

	/**
	 * Method for buying a lodging using Date objects
	 * @param lodgingID the id for the desired Lodging
	 * @param numRooms the number of rooms desired
	 * @param checkIn the Date object for the checkIn date
	 * @param checkOut the Date object for the checkOut date
	 * @return true if the lodging was successfully reserved, false if there was a problem
	 * @throws RemoteException @todo
	 */
	boolean buyLodging(int lodgingID, int numRooms, Date checkIn, Date checkOut) throws RemoteException;

	/**
	 * Method for buying a lodging using String Objects
	 * @param lodgingID the id for the desired Lodging
	 * @param numRooms the number of rooms desired
	 * @param scheckIn the String object for the checkIn date
	 * @param scheckOut the String object for the checkOut date
	 * @return true if the lodging was successfully reserved, false if there was a problem
	 * @throws RemoteException @todo
	 */
	boolean buyLodging(int lodgingID, int numRooms, String scheckIn, String scheckOut) throws RemoteException, ParseException;

	/**
	 * Method for buying a Travel Package using Date objects
	 * @param travelPackageID the id for the desired travel package
	 * @param numPackets the number of rooms desired for the lodging
	 * @return true if the travel package was successfully bought, false if there was a problem
	 * @throws RemoteException @todo
	 */
	boolean buyTravelPackage(int travelPackageID, int numPackets) throws RemoteException;


	/**
	 * Method for registering interest in new plane tickets
	 * @param destiny the destiny location for the plane ticket
	 * @param origin the origin location for the plane ticket
	 * @param maximumPrice the maximum price for the tickets in CENTS
	 * @param clientReference the reference on which to notify the client. This interface MUST implement the "notify(int id)" method
	 * @return the id for the successfully registered event. Returns -1 if there was a failure.
	 * @throws RemoteException @todo
	 */
	int interestPlaneTicket(Location destiny, Location origin, int maximumPrice, InterfaceCli clientReference) throws RemoteException;

	/**
	 * Method for registering interest in new lodgings
	 * @param location the location for the lodging
	 * @param maximumPrice the maximum price for the lodging in CENTS per day per room
	 * @param clientReference the reference on which to notify the client. This interface MUST implement the "notify(int id)" method
	 * @return the id for the successfully registered event. Returns -1 if there was a failure.
	 * @throws RemoteException @todo
	 */
	int interestLodging(Location location, int maximumPrice, InterfaceCli clientReference) throws RemoteException;

	/**
	 * Method for registering interest in new plane tickets
	 * @param destiny the destiny location for the travel package
	 * @param origin the origin location for the travel package
	 * @param maximumPrice the maximum price for the travel package in CENTS
	 * @param clientReference the reference on which to notify the client. This interface MUST implement the "notify(int id)" method
	 * @return the id for the successfully registered event. Returns -1 if there was a failure.
	 * @throws RemoteException @todo
	 */
	int interestTravelPackage(Location destiny, Location origin, int maximumPrice, InterfaceCli clientReference) throws RemoteException;

	/**
	 * Method for removing an interested added
	 * @param id the id returned by the interest function
	 * @return true if it was successfully removed, false otherwise
	 * @throws RemoteException @todo
	 */
	boolean removeInterest(int id) throws RemoteException;
}
