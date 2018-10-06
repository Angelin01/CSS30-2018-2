package TravelEvent;

import RMIrino.InterfaceCli;
import Travel.Location;
import Travel.PlaneTicket;
import Travel.TravelPackage;

import java.rmi.RemoteException;
import java.util.Date;

public class PlaneTicketEvent extends TravelEvent {
	private Location origin;
	private Date returnDate;

	/**
	 * Simple constructor for a PlaneTicketEvent notifier
	 * @param origin the origin Location. null if doesn't matter
	 * @param destiny the destiny Location, cannot be null
	 * @param departureDate the departure Date, cannot be null
	 * @param returnDate the Date object for the return date. null if doesn't matter
	 * @param maximumPrice the maximum price for the ticket. &lt;= 0 if doesn't matter
	 * @param clientReference the client reference, cannot be null
	 * @throws IllegalArgumentException if none of the filters matter
	 */
	public PlaneTicketEvent(Location origin, Location destiny, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) {
		super(destiny, departureDate, maximumPrice, clientReference);
		this.returnDate = returnDate;
		this.origin = origin;
	}

	/**
	 * Simple getter for the origin Location
	 * @return the origin Location
	 */
	public Location getOrigin() {
		return origin;
	}

	/**
	 * Simple getter for the return date
	 * @return the returnDate
	 */
	public Date getReturnDate() {
		return returnDate;
	}

	/**
	 * Notifies the client that the event happened
	 * @param planeTicket the PlaneTicket that matches the interest
	 * @throws RemoteException if there's any problems with the remote connection
	 */
	public void notifyClient(PlaneTicket planeTicket) throws RemoteException {
		this.clientReference.notifyPlaneTicket(this.getId(), planeTicket);
	}
}
