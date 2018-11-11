package Controllers;

import Travel.*;

import javax.ws.rs.core.Response;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;


public interface InterfaceServ {
	/**
	 * Simple getter for the list of available lodgings
	 * @return a List of ALL Lodging objects
	 */
	//Response getLodgings();

	/**
	 * Getter for Lodgings with filters
	 * @param location the desired location. null if doesn't matter
	 * @param maxPrice the maximum price. &lt;= 0 if doesn't matter
	 * @param scheckIn Date object for checkIn. null if doesn't matter
	 * @param scheckOut Date object for checkOut. null if doesn't matter
	 * @param minimumRooms number of minimum rooms available. &lt;= 0 if doesn't matter
	 * @return A list of Lodgings according to the filters
	 */
	Response getLodgings(Location location, int maxPrice, String scheckIn, String scheckOut, int minimumRooms);

    /**
     * Method for buying a lodging using Date objects
     * @param id the id for the desired Lodging
     * @param numRooms the number of rooms desired
     * @return true if the lodging was successfully reserved, false if there was a problem
     */
    Response buyLodging(int id, int numRooms);

	/**
	 * Simple getter for the list of available plane tickets
	 * @return a List of ALL PlaneTickets objects
	 */
    //Response getPlaneTickets();

	/**
	 * Getter for PlaneTickets with filters
	 * @param origin the origin location. null if doesn't matter
	 * @param destiny the destiny location. null if doesn't matter
	 * @param maxPrice the maximum price. &lt;= 0 if doesn't matter
	 * @param sdepartureDate the departure date. null if doesn't matter
	 * @param sreturnDate the return date. null if doesn't matter. Note: having a return date implies a round-trip
	 * @param minimumSeats the minimum number of available seats. &lt;= 0 if doesn't matter
	 * @return A list of PlaneTickets according to the filters
	 */
	Response getPlaneTickets(Location origin, Location destiny, int maxPrice, String sdepartureDate,
	                                  String sreturnDate, int minimumSeats);

    /**
     * Method for buying a PlaneTicket
     * @param id the id for the desired plane ticket
     * @param numTickets the number of desired tickets
     * @return true if the ticket was successfully bought, false if there was a problem
     */
    Response buyPlaneTicket(int id, int numTickets);


    /**
     * Simple getter for the list of available plane tickets
     * @return a List of ALL Travel Packages objects
     */
    //Response getTravelPackages();

	/**
	 * Getter for TravelPackages with filters
	 * @param origin the origin location. null if doesn't matter
	 * @param destiny the destiny location. null if doesn't matter
	 * @param maxPrice the maximum price. &lt;= 0 if doesn't matter
	 * @param sdepartureDate the departure date. null if doesn't matter
	 * @param sreturnDate the return date. null if doesn't matter
	 * @param minimumAvailable the minimum number of available packages. &lt;= 0 if doesn't matter
	 * @return List of TravelPackages according to the filters
	 */
	Response getTravelPackages(Location origin, Location destiny, int maxPrice, String sdepartureDate,
	                                      String sreturnDate, int minimumAvailable);

    /**
     *
     * @param id The id of the desired travel package
     * @param numPackages The number of rooms of the desired travel package
     * @return The travel package with the updated number packages available after the buy
     */
    Response buyTravelPackage(int id, int numPackages);

	/**
	 * Method for buying a PlaneTicket
	 * @param planeTicketID the id for the desired plane
	 * @param numTickets the number of desired tickets
	 * @return true if the ticket was successfully bought, false if there was a problem
	 */
	//boolean buyPlaneTicket(int planeTicketID, int numTickets);

	/**
	 * Method for buying a lodging using Date objects
	 * @param lodgingID the id for the desired Lodging
	 * @param numRooms the number of rooms desired
	 * @return true if the lodging was successfully reserved, false if there was a problem
	 */
	//boolean buyLodging(int lodgingID, int numRooms);

	/**
	 * Method for buying a Travel Package using Date objects
	 * @param travelPackageID the id for the desired travel package
	 * @param numPackets the number of rooms desired for the lodging
	 * @return true if the travel package was successfully bought, false if there was a problem
	 */
	//boolean buyTravelPackage(int travelPackageID, int numPackets);

	/**
	 * Method for registering interest in new plane tickets
	 * @param destiny the destiny location for the plane ticket. REQUIRED
	 * @param origin the origin location for the plane ticket. null if doesn't matter
	 * @param maximumPrice the maximum price for the tickets in CENTS. &lt;= 0 if doesn't matter
	 * @param departureDate the Date object for the departure date. REQUIRED
	 * @param returnDate the Date object for the return date. null if doesn't matter
	 * @param clientReference the reference on which to notify the client. This interface MUST implement the "notifyPlaneTicket(int id, PlaneTicket planeTicket)" method
	 * @return the id for the successfully registered event. Returns -1 if there was a failure.
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	//int interestPlaneTicket(Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) throws RemoteException;

	/**
	 * Method for registering interest in new lodgings
	 * @param location the location for the lodging. REQUIRED
	 * @param checkIn the Date object for the check in date. REQUIRED
	 * @param checkOut the Date object for the check out date.null if doesn't matter
	 * @param maximumPrice the maximum price for the lodging in CENTS per room. &lt;= 0 if doesn't matter
	 * @param clientReference the reference on which to notify the client. This interface MUST implement the "notifyLodging(int id, Lodging lodging)" method
	 * @return the id for the successfully registered event. Returns -1 if there was a failure.
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	//int interestLodging(Location location, Date checkIn, Date checkOut, int maximumPrice, InterfaceCli clientReference) throws RemoteException;

	/**
	 * Method for registering interest in new plane tickets
	 * @param destiny the destiny location for the travel package. REQUIRED
	 * @param origin the origin location for the travel package. null if doesn't matter
	 * @param departureDate the Date object for the departure date. null if doesn't matter. REQUIRED
	 * @param returnDate the Date object for the return date. null if doesn't matter
	 * @param maximumPrice the maximum price for the travel package in CENTS. &lt;= 0 if doesn't matter
	 * @param clientReference the reference on which to notify the client. This interface MUST implement the "notifyTravelPackage(int id, TravelPackage travelPackage)" method
	 * @return the id for the successfully registered event. Returns -1 if there was a failure.
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	//int interestTravelPackage(Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) throws RemoteException;

	/**
	 * Method for removing an planeTicket Interest
	 * @param id the id returned by the interest function
	 * @param destiny the original destiny passed to the interest function
	 * @param departureDate the original departureDate passed to the interest function
	 * @return true if it was successfully removed, false otherwise
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	//boolean removeInterestPlaneTicket(int id, Location destiny, Date departureDate) throws RemoteException;
	
	/**
	 * Method for removing an Lodging Interest
	 * @param id the id returned by the interest function
	 * @param location the original location passed to the interest function
	 * @param checkIn the original checkIn date passed to the interest function
	 * @return true if it was successfully removed, false otherwise
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	//boolean removeInterestLodging(int id, Location location, Date checkIn) throws RemoteException;
	
	/**
	 * Method for removing an TravelPackage Interest
	 * @param id the id returned by the interest function
	 * @param destiny the original destiny passed to the interest function
	 * @param departureDate the original departureDate passed to the interest function
	 * @return true if it was successfully removed, false otherwise
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	//boolean removeInterestTravelPackage(int id, Location destiny, Date departureDate) throws RemoteException;
}
