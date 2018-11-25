package RMIrino;

import Travel.Location;
import Travel.Lodging;
import Travel.PlaneTicket;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface InterfacePlaneTicket extends Remote {
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
	 * Method for buying a Travel Package using Date objects
	 * @param travelPackageID the id for the desired travel package
	 * @param numPackets the number of rooms desired for the lodging
	 * @return true if the travel package was successfully bought, false if there was a problem
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	boolean buyTravelPackage(int travelPackageID, int numPackets) throws RemoteException;
}

