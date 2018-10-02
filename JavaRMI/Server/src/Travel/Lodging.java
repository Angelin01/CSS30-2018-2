package Travel;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A Lodging, with a Location Enum, price per room, a date and the number of available rooms for that date
 */
public class Lodging implements Serializable {
	private Location location;
	private int price;
	private int id;
	private Date date;
	private int numRooms;
	private static int nextId = 0;
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	
	/**
	 * Constructor for a Lodging object using a Date object
	 * @param location Location enum for the location
	 * @param price the price in CENTS per room per day
	 * @param date the date object for the date
	 * @throws NullPointerException if location is null
	 * @throws IllegalArgumentException if price is negative
	 */
	public Lodging(Location location, Date date, int price, int numRooms) {
		if (location == null) {
			throw new NullPointerException("Parameter location cannot be null");
		}
		this.location = location;

		if (date == null) {
			throw new NullPointerException("Parameter date cannot be null");
		}
		this.date = date;
		
		if (price < 0) {
			throw new IllegalArgumentException("Parameter price cannot be negative");
		}
		this.price = price;

		if (numRooms < 0) {
			throw new IllegalArgumentException("Parameter numRooms cannot be negative");
		}
		this.numRooms = numRooms;

		this.id = nextId++;
	}

	/**
	 * Constructor for a Lodging object using a String object
	 * @param location Location enum for the location
	 * @param price the price in CENTS per room per day
	 * @param date the String object for the date
	 * @throws NullPointerException if location is null
	 * @throws IllegalArgumentException if price is negative
	 */
	public Lodging(Location location, String date, int price, int numRooms) throws java.text.ParseException {
		if (location == null) {
			throw new NullPointerException("Parameter location cannot be null");
		}
		this.location = location;

		this.date = format.parse(date);

		if (price < 0) {
			throw new IllegalArgumentException("Parameter price cannot be negative");
		}
		this.price = price;

		if (numRooms < 0) {
			throw new IllegalArgumentException("Parameter numRooms cannot be negative");
		}
		this.numRooms = numRooms;

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
	 * Simple getter for the date object
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Simple setter for the date using a Date object
	 * @param date the date
	 */
	public void setDate(Date date) {
		if (date == null) {
			throw new NullPointerException("Parameter date cannot be null");
		}
		this.date = date;
	}

	/**
	 * Simple setter for the date using a String object
	 * @param date the date
	 */
	public void setDate(String date) throws java.text.ParseException {
		this.date = format.parse(date);
	}

	/**
	 * Simple getter for the number of rooms
	 * @return the number of available rooms
	 */
	public int getNumRooms() {
		return numRooms;
	}

	/**
	 * Simple setter for the number of rooms
	 * @param numRooms the number of available rooms
	 */
	public void setNumRooms(int numRooms) {
		if (numRooms < 0) {
			throw new IllegalArgumentException("Parameter numRooms cannot be negative");
		}
		this.numRooms = numRooms;
	}

	// @todo update toString
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
