package Travel;

import java.io.Serializable;

/**
 * Travel package, with a PlaneTicket and Lodging and a new price
 */
public class TravelPackage implements Serializable {
	private PlaneTicket planeTicket;
	private Lodging lodging;
	private int price;
	private int id;
	private static int nextId = 0;
	
	/**
	 * Simple constructor for a TravelPackage of a ticker and lodging
	 * @param planeTicket Ticket for the package
	 * @param lodging Lodging for the package
	 * @param price The price in CENTS for the package
	 * @throws NullPointerException if either planeTicket or lodging is null
	 * @throws IllegalArgumentException if price is negative
	 */
	public TravelPackage(PlaneTicket planeTicket, Lodging lodging, int price) {
		if (planeTicket == null) {
			throw new NullPointerException("Parameter planeTicket cannot be null");
		}
		this.planeTicket = planeTicket;
		
		if (lodging == null) {
			throw new NullPointerException("Parameter lodging cannot be null");
		}
		this.lodging = lodging;
		
		if (price < 0) {
			throw new IllegalArgumentException("Parameter price cannot be negative");
		}
		this.price = price;

		this.id = nextId++;
	}
	
	/**
	 * Simple getter for the planeTicket object
	 * @return the planeTicket object
	 */
	public PlaneTicket getPlaneTicket() {
		return planeTicket;
	}
	
	/**
	 * Simple setter for the planeTicket object
	 * @param planeTicket The planeTicket object
	 * @throws NullPointerException if planeTicket is null
	 */
	public void setPlaneTicket(PlaneTicket planeTicket) {
		if (planeTicket == null) {
			throw new NullPointerException("Parameter planeTicket cannot be null");
		}
		this.planeTicket = planeTicket;
	}
	
	/**
	 * Simple getter for the lodging object
	 * @return the lodging object
	 */
	public Lodging getLodging() {
		return lodging;
	}
	
	/**
	 * Simple setter for the lodging object
	 * @param lodging The lodging object
	 * @throws NullPointerException if lodging is null
	 */
	public void setLodging(Lodging lodging) {
		if (lodging == null) {
			throw new NullPointerException("Parameter lodging cannot be null");
		}
		this.lodging = this.lodging = lodging;
	}
	
	/**
	 * Simple getter for the price
	 * @return the price in CENTS
	 */
	public int getPrice() {
		return price;
	}
	
	/**
	 * Simple setter for the price
	 * @param price The price in CENTS
	 * @throws IllegalArgumentException if price is negative
	 */
	public void setPrice(int price) {
		if (price < 0) {
			throw new IllegalArgumentException("Parameter price cannot be negative");
		}
		this.price = price;
	}

	/**
	 * Simple getter for the id
	 * @return the id for the TravelPackage
	 */
	public int getId() {
		return id;
	}

	/**
	 * A simple to string method to visualize both the PlaneTicket and Lodging objects of the package
	 * @return a string visualization of the TravelPackage
	 */
	@Override
	public String toString() {
		return("TravelPackage:\n" +
		       planeTicket.toString() + "\n" +
		       lodging.toString() + "\n" +
		       "TravelPackage price: $" + price/100 + "." + price%100);
	}
}