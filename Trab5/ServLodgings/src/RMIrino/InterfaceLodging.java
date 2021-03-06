package RMIrino;

import Travel.Location;
import Travel.Lodging;

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
	List<Lodging> getLodgings(Location location, int maxPrice, Date checkIn, Date checkOut, int minimumRooms) throws RemoteException;

	/**
	 * Method for buying a lodging using Date objects
	 * @param lodgingID the id for the desired Lodging
	 * @param numRooms the number of rooms desired
	 * @param isPackage true if the buy order is part of a package and needs transactions
	 * @return true if the lodging was successfully reserved, false if there was a problem
	 * @throws RemoteException if there's any problem with the remote connection
	 */
	boolean buyLodging(int lodgingID, int numRooms, boolean isPackage) throws RemoteException;
}
