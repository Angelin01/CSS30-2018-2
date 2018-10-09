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
     * @throws RemoteException if there's any problem with the remote connection
     */
    List<Lodging> getLodgings() throws RemoteException;

    /**
     * Getter for Lodgings with filters
     * @param location the desired location. null if doesn't matter
     * @param maxPrice the maximum price. &lt;= 0 if doesn't matter
     * @param checkIn Date object for checkIn. null if doesn't matter
     * @param checkOut Date object for checkOut. null if doesn't matter
     * @param minimumRooms number of minimum rooms available. &lt;= 0 if doesn't matter
     * @return A list of Lodgings according to the filters
     * @throws RemoteException if there's any problem with the remote connection
     */
    List<Lodging> getLodgings(Location location, int maxPrice, Date checkIn, Date checkOut, int minimumRooms) throws RemoteException;

    /**
     * Simple getter for the list of available plane tickets
     * @return a List of ALL PlaneTickets objects
     * @throws RemoteException if there's any problem with the remote connection
     */
    List<PlaneTicket> getPlaneTickets() throws RemoteException;

    /**
     * Getter for PlaneTickets with filters
     * @param origin the origin location. null if doesn't matter
     * @param destiny the destiny location. null if doesn't matter
     * @param maxPrice the maximum price. &lt;= 0 if doesn't matter
     * @param departureDate the departure date. null if doesn't matter
     * @param returnDate the return date. null if doesn't matter. Note: having a return date implies a round-trip
     * @param minimumSeats the minimum number of available seats. &lt;= 0 if doesn't matter
     * @return A list of PlaneTickets according to the filters
     * @throws RemoteException if there's any problem with the remote connection
     */
    List<PlaneTicket> getPlaneTickets(Location origin, Location destiny, int maxPrice, Date departureDate,
                                      Date returnDate, int minimumSeats) throws RemoteException;

    /**
     * Simple getter for the list of available packages
     * @return a List of ALL TravelPackages objects
     * @throws RemoteException if there's any problem with the remote connection
     */
    List<TravelPackage> getTravelPackages() throws RemoteException;

    /**
     * Getter for TravelPackages with filters
     * @param origin the origin location. null if doesn't matter
     * @param destiny the destiny location. null if doesn't matter
     * @param maxPrice the maximum price. &lt;= 0 if doesn't matter
     * @param departureDate the departure date. null if doesn't matter
     * @param returnDate the return date. null if doesn't matter
     * @param minimumAvailable the minimum number of available packages. &lt;= 0 if doesn't matter
     * @return List of TravelPackages according to the filters
     * @throws RemoteException if there's any problem with the remote connection
     */
    List<TravelPackage> getTravelPackages(Location origin, Location destiny, int maxPrice, Date departureDate,
                                          Date returnDate, int minimumAvailable) throws RemoteException;

    /**
     * Method for buying a PlaneTicket
     * @param planeTicketID the id for the desired plane
     * @param numTickets the number of desired tickets
     * @return true if the ticket was successfully bought, false if there was a problem
     * @throws RemoteException if there's any problem with the remote connection
     */
    boolean buyPlaneTicket(int planeTicketID, int numTickets) throws RemoteException;

    /**
     * Method for buying a lodging using Date objects
     * @param lodgingID the id for the desired Lodging
     * @param numRooms the number of rooms desired

     * @return true if the lodging was successfully reserved, false if there was a problem
     * @throws RemoteException if there's any problem with the remote connection
     */
    boolean buyLodging(int lodgingID, int numRooms) throws RemoteException;

    /**
     * Method for buying a Travel Package using Date objects
     * @param travelPackageID the id for the desired travel package

     * @param numPackets the number of rooms desired for the lodging
     * @return true if the travel package was successfully bought, false if there was a problem
     * @throws RemoteException if there's any problem with the remote connection
     */
    boolean buyTravelPackage(int travelPackageID, int numPackets) throws RemoteException;

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
    int interestPlaneTicket(Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) throws RemoteException;

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
    int interestLodging(Location location, Date checkIn, Date checkOut, int maximumPrice, InterfaceCli clientReference) throws RemoteException;

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
    int interestTravelPackage(Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) throws RemoteException;

    /**
     * Method for removing an planeTicket Interest
     * @param id the id returned by the interest function
     * @param destiny the original destiny passed to the interest function
     * @param departureDate the original departureDate passed to the interest function
     * @return true if it was successfully removed, false otherwise
     * @throws RemoteException if there's any problem with the remote connection
     */
    boolean removeInterestPlaneTicket(int id, Location destiny, Date departureDate) throws RemoteException;

    /**
     * Method for removing an Lodging Interest
     * @param id the id returned by the interest function
     * @param location the original location passed to the interest function
     * @param checkIn the original checkIn date passed to the interest function
     * @return true if it was successfully removed, false otherwise
     * @throws RemoteException if there's any problem with the remote connection
     */
    boolean removeInterestLodging(int id, Location location, Date checkIn) throws RemoteException;

    /**
     * Method for removing an TravelPackage Interest
     * @param id the id returned by the interest function
     * @param destiny the original destiny passed to the interest function
     * @param departureDate the original departureDate passed to the interest function
     * @return true if it was successfully removed, false otherwise
     * @throws RemoteException if there's any problem with the remote connection
     */
    boolean removeInterestTravelPackage(int id, Location destiny, Date departureDate) throws RemoteException;
}
