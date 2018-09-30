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
	 * @return a List of Lodging objects
	 * @throws RemoteException @todo
	 */
	List<Lodging> getLodgings() throws RemoteException;

	/**
	 * Simple getter for the list of available plane tickets
	 * @return a List of PlaneTickets objects
	 * @throws RemoteException @todo
	 */
	List<PlaneTicket> getPlaneTickets() throws RemoteException;

	/**
	 * Simple getter for the list of available packages
	 * @return a List of TravelPackages objects
	 * @throws RemoteException @todo
	 */
	List<TravelPackage> getTravelPackages() throws RemoteException;

	/**
	 * Method for buying a PlaneTicket
	 * @param planeTicketID the id for the desired plane ticket
	 * @return true if the ticket was successfully bought, false if there was a problem
	 * @throws RemoteException @todo
	 */
	boolean buyPlaneTicket(int planeTicketID) throws RemoteException;

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
	 * @param numRooms the number of rooms desired for the lodging
	 * @param checkIn the Date object for the checkIn date for the lodging
	 * @param checkOut the Date object for the checkOut date
	 * @return true if the travel package was successfully bought, false if there was a problem
	 * @throws RemoteException @todo
	 */
	boolean buyTravelPackage(int travelPackageID, int numRooms, Date checkIn, Date checkOut) throws RemoteException;

	/**
	 * Method for buying a Travel Package using String objects
	 * @param travelPackageID the id for the desired travel package
	 * @param numRooms the number of rooms desired for the lodging
	 * @param scheckIn the String object for the checkIn date for the lodging
	 * @param scheckOut the String object for the checkOut date
	 * @return true if the travel package was successfully bought, false if there was a problem
	 * @throws RemoteException @todo
	 */
	boolean buyTravelPackage(int travelPackageID, int numRooms, String scheckIn, String scheckOut) throws RemoteException, ParseException;


}
