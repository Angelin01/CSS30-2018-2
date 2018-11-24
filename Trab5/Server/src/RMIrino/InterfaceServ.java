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
}
