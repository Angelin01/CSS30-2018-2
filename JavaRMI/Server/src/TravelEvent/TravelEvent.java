package TravelEvent;

import RMIrino.InterfaceCli;

public abstract class TravelEvent {
	static int nextId = 0;

	private int id;
	private int maximumPrice;
	private InterfaceCli clientReference;

	/**
	 * Simple constructor for the TravelEvent
	 * @param maximumPrice the maximum price wanted. <= 0 if doesn't matter
	 * @param clientReference the client reference to call, cannot be null
	 */
	protected TravelEvent(int maximumPrice, InterfaceCli clientReference) {
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
