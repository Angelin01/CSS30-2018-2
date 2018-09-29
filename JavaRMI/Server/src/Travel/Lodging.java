package Travel;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Lodging implements Serializable {
	private Location location;
	private int price;
	private int id;
	private static int nextId = 0;
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	/**
	 * Constructor for a Lodging object
	 * @param location Location enum for the location
	 * @param price the price in CENTS per room per day
	 * @throws NullPointerException if location is null
	 * @throws IllegalArgumentException if price is negative
	 */
	public Lodging(Location location, int price) {
		if (location == null) {
			throw new NullPointerException("Parameter location cannot be null");
		}
		this.location = location;
		
		if (price < 0) {
			throw new IllegalArgumentException("Parameter price cannot be negative");
		}
		this.price = price;
		this.id = nextId++;
	}

	
	/**
	 * Simple getter for location
	 * @return location Location
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Simple setter for location
	 * @param location Location for location
	 * @throws NullPointerException if location is null
	 */
	public void setLocation(Location location) {
		if (location == null) {
			throw new NullPointerException("Parameter location cannot be null");
		}
		// @todo verify if location is valid
		this.location = location;
	}
	
	/**
	 * Simple getter for price per room per day
	 * @return the price in CENTS per room per day
	 */
	public int getPrice() {
		return price;
	}
	
	/**
	 * Simple setter for price
	 * @param price price for the stay in CENTS
	 */
	public void setPrice(int price) {
		if (price < 0) {
			throw new IllegalArgumentException("Parameter price cannot be negative");
		}
		this.price = price;
	}

	/**
	 * Simple getter for the id
	 * @return the id for the Lodging
	 */
	public int getId() {
		return id;
	}

	/**
	 * A simple to string method for using with print to visualize a Lodging object
	 * @return a string visualization of the Lodging
	 */
	@Override
	public String toString() {
		return("Lodging:\n" +
		       "Location: " + location + "\n" +
		       "Price per room per day: $" + price/100 + "." + price%100);
	}
}
