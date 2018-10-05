package TravelEvent;

import RMIrino.InterfaceCli;
import Travel.Location;

import java.util.Date;

public abstract class TravelEvent {
	static int nextId = 0;

	private int id;
	protected Location destinyLocation;
	protected Date startDate;
	protected int maximumPrice;
	protected InterfaceCli clientReference;

	/**
	 * Simple constructor for the
	 * @param destinyLocation the location that you're interested in, cannot be null
	 * @param startDate the initial date of interest, probably departure date or check in date, null if doesn't matter
	 * @param maximumPrice the maximum price wanted. <= 0 if doesn't matter
	 * @param clientReference the client reference to call, cannot be null
	 * @throws NullPointerException if clientReference is null
	 */
	protected TravelEvent(Location destinyLocation, Date startDate, int maximumPrice, InterfaceCli clientReference) {
		if (destinyLocation == null) {
			throw new NullPointerException("Parameter destinyLocation cannot be null");
		}

		this.startDate = startDate;
		this.maximumPrice = maximumPrice;

		if (clientReference == null) {
			throw new NullPointerException("Parameter clientReference cannot be null");
		}

		this.clientReference = clientReference;
		this.id = nextId++;
	}

	/**
	 * Simple getter for the id
	 * @return the id
	 */
	public int getId() {
		return id;
	}
}
