package TravelEvent;

import RMIrino.InterfaceCli;
import Travel.Location;
import Travel.PlaneTicket;

public class PlaneTicketEvent extends TravelEvent {
	private Location origin;
	private Location destiny;

	/**
	 * Simple constructor for a PlaneTicketEvent notifier
	 * @param origin the origin Location. null if doesn't matter
	 * @param destiny the destiny Location. null if doesn't matter
	 * @param maximumPrice the maximum price for the ticket. <= 0 if doesn't matter
	 * @param clientReference the client reference
	 */
	public PlaneTicketEvent(Location origin, Location destiny, int maximumPrice, InterfaceCli clientReference) {
		super(maximumPrice, clientReference);
		if (origin == null && destiny == null && maximumPrice <= 0) {
			throw new IllegalArgumentException("At least one filter must matter");
		}
		this.origin = origin;
		this.destiny = destiny;
	}
}
