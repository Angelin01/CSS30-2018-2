package RMIrino;

import SimpleFileAccess.RecordsFileException;
import Travel.Location;
import Travel.PlaneTicket;

import java.io.IOException;
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
	 * Method for buying a PlaneTicket
	 * @param planeTicketID the id for the desired plane
	 * @param numTickets the number of desired tickets
	 * @return true if the ticket was successfully bought, false if there was a problem
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	boolean buyPlaneTicket(int planeTicketID, int numTickets) throws RemoteException, ClassNotFoundException, RecordsFileException, IOException;

	/**
	 * Method for buying a PlaneTicket in a package
	 * @param planeTicketID the id for the desired plane
	 * @param numTickets the number of desired tickets
	 * @param idTransaction The ID for the transaction, only used on crashes. If it's not unique, there will be problems
	 * @return true if the ticket was successfully bought, false if there was a problem
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	boolean buyPackagePlaneTicket(int planeTicketID, int numTickets, int idTransaction) throws RemoteException, RecordsFileException, IOException, ClassNotFoundException;

	/**
	 * Method for the coordenator to call to commit a transaction
	 * @param complete If true, will commit the transaction. If false, will abort and return to previous state
	 */
	void commitTransaction(boolean complete) throws RemoteException, IOException, ClassNotFoundException, RecordsFileException;

}
