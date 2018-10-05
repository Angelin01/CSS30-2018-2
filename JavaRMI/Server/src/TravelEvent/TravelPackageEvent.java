package TravelEvent;

import RMIrino.InterfaceCli;
import Travel.Location;
import Travel.TravelPackage;

import java.rmi.RemoteException;
import java.util.Date;

public class TravelPackageEvent extends TravelEvent {
	private Location origin;
	private Date returnDate;

	/**
	 * Simple constructor for a TravelPackageEvent notifier
	 * @param origin the origin Location. null if doesn't matter
	 * @param destiny the destiny Location. null if doesn't matter
	 * @param departureDate the Date object for the departure date. null if doesn't matter
	 * @param returnDate the Date object for the return date. null if doesn't matter
	 * @param maximumPrice the maximum price for the ticket. <= 0 if doesn't matter
	 * @param clientReference the client reference, cannot be null
	 */
	public TravelPackageEvent(Location origin, Location destiny, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) {
		super(destiny, departureDate, maximumPrice, clientReference);
		this.origin = origin;
		this.returnDate = returnDate;
	}

	/**
	 * Notifies the client that the event happened
	 * @param travelPackage the TravelPackage that matches the interest
	 * @throws RemoteException if there's any problems with the remote connection
	 */
	public void notifyClient(TravelPackage travelPackage) throws RemoteException {
		this.clientReference.notifyTravelPackage(this.getId(), travelPackage);
	}
}
