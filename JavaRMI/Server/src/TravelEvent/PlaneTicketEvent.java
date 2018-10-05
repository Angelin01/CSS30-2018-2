package TravelEvent;

import RMIrino.InterfaceCli;
import Travel.Location;
import Travel.PlaneTicket;

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
	 * @param maximumPrice the maximum price for the ticket. <= 0 if doesn't matter
	 * @param clientReference the client reference, cannot be null
	 * @throws IllegalArgumentException if none of the filters matter
	 */
	public PlaneTicketEvent(Location origin, Location destiny, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) {
		super(destiny, departureDate, maximumPrice, clientReference);
		this.returnDate = returnDate;
		this.origin = origin;
	}
}
