package RMIrino;

import Travel.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface InterfaceServ extends Remote {
	List<Lodging> getLodgings() throws RemoteException;
	List<PlaneTicket> getPlaneTickets() throws RemoteException;
	List<TravelPackage> getTravelPackages() throws RemoteException;

	boolean buyPlaneTicket(int planeTicketID) throws RemoteException;
	boolean buyLodging(int lodgingID, int numRooms, Date checkIn, Date checkOut) throws RemoteException;
	boolean buyLodging(int lodgingID, int numRooms, String scheckIn, String scheckOut) throws RemoteException, ParseException;
	boolean buyTravelPackage(int travelPackageID, int numRooms, Date checkIn, Date checkOut) throws RemoteException;
	boolean buyTravelPackage(int travelPackageID, int numRooms, String checkIn, String checkOut) throws RemoteException, ParseException;
}
