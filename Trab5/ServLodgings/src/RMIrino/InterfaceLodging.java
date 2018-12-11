package RMIrino;

import SimpleFileAccess.RecordsFileException;
import Travel.Location;
import Travel.Lodging;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public interface InterfaceLodging extends Remote {
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
	@Deprecated
	List<Lodging> getLodgings(Location location, int maxPrice, Date checkIn, Date checkOut, int minimumRooms) throws RemoteException;

	/**
	 * Method for buying a lodging using Date objects
	 * @param lodgingID the id for the desired Lodging
	 * @param numRooms the number of rooms desired
	 * @return true if the lodging was successfully reserved, false if there was a problem
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	boolean buyLodging(int lodgingID, int numRooms) throws RemoteException, RecordsFileException, ClassNotFoundException, IOException;

	/**
	 * Method for buying a Lodging in a package
	 * @param lodgingID the id for the desired plane
	 * @param numRooms the number of desired tickets
	 * @param idTransaction The ID for the transaction, only used on crashes. If it's not unique, there will be problems
	 * @return true if the lodging was successfully bought, false if there was a problem
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	boolean buyPackageLodging(int lodgingID, int numRooms, int idTransaction) throws RemoteException, RecordsFileException, ClassNotFoundException, IOException;

	/**
	 * Method for the coordenator to call to commit a transaction
	 * @param complete If true, will commit the transaction. If false, will abort and return to previous state
	 */
	void commitTransaction(boolean complete) throws RemoteException, IOException, ClassNotFoundException, RecordsFileException;
}
