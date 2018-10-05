package TravelEvent;

import RMIrino.InterfaceCli;
import Travel.Location;

import java.util.Date;

public class LodgingEvent extends TravelEvent {
	private Date checkOut;

	/**
	 * Simple constructor for a LodgingEvent notifier
	 * @param location the location of the lodging
	 * @param checkIn the checkIn Date object. null if doesn't matter
	 * @param checkOut the checkOut Date object. null if doesn't matter
	 * @param maximumPrice the maximum price for the lodging. <= if doesn't matter
	 * @param clientReference the client reference, cannot be null
	 * @throws IllegalArgumentException if none of the filters matter
	 */
	public LodgingEvent(Location location, Date checkIn, Date checkOut, int maximumPrice, InterfaceCli clientReference) {
		super(location, checkIn, maximumPrice, clientReference);
		this.checkOut = checkOut;
	}
}
