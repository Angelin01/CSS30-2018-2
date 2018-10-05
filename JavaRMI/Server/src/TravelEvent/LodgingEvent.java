package TravelEvent;

import RMIrino.InterfaceCli;
import Travel.Location;

import java.util.Date;

public class LodgingEvent extends TravelEvent {
	private Location location;
	private Date checkIn;
	private Date checkOut;

	/**
	 * Simple constructor for a LodgingEvent notifier
	 * At least one of the filters must matter
	 * @param location the location of the lodging. null if doesn't matter
	 * @param checkIn the checkIn Date object. null if doesn't matter
	 * @param checkOut the checkOut Date object. null if doesn't matter
	 * @param maximumPrice the maximum price for the lodging. <= if doesn't matter
	 * @param clientReference the client reference, cannot be null
	 * @throws IllegalArgumentException if none of the filters matter   
	 */
	public LodgingEvent(Location location, Date checkIn, Date checkOut, int maximumPrice, InterfaceCli clientReference) {
		super(maximumPrice, clientReference);
		if(location == null && checkIn == null && checkOut == null && maximumPrice <= 0) {
			throw new IllegalArgumentException("At least one filter must matter");
		}
		this.location = location;
		this.checkIn = checkIn;
		this.checkOut = checkOut;
	}
}
