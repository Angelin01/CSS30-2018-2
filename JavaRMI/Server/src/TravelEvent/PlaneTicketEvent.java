package TravelEvent;

import RMIrino.InterfaceCli;
import Travel.Location;
import Travel.PlaneTicket;

import java.util.Date;

public class PlaneTicketEvent extends TravelEvent {
	private Location origin;
	private Location destiny;
	private Date departureDate;
	private Date returnDate;

	/**
	 * Simple constructor for a PlaneTicketEvent notifier
	 * At least one of the filters must matter
	 * @param origin the origin Location. null if doesn't matter
	 * @param destiny the destiny Location. null if doesn't matter
	 * @param departureDate the Date object for the departure date. null if doesn't matter
	 * @param returnDate the Date object for the return date. null if doesn't matter
	 * @param maximumPrice the maximum price for the ticket. <= 0 if doesn't matter
	 * @param clientReference the client reference, cannot be null
	 * @throws IllegalArgumentException if none of the filters matter
	 */
	public PlaneTicketEvent(Location origin, Location destiny, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference) {
		super(maximumPrice, clientReference);
		if (origin == null && destiny == null && departureDate == null && returnDate == null && maximumPrice <= 0) {
			throw new IllegalArgumentException("At least one filter must matter");
		}
		this.departureDate = departureDate;
		this.returnDate = returnDate;
		this.origin = origin;
		this.destiny = destiny;
	}
}
