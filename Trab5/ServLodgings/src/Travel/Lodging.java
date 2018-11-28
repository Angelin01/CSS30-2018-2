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
	private Date checkIn;
	private Date checkOut;
	private int numRooms;
	private static int nextId = 0;
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

	
	/**
	 * Constructor for a Lodging object using a Date object
	 * @param location Location enum for the location
	 * @param price the price in CENTS per room
	 * @param checkIn the date object for the checkIn
	 * @param checkOut the date object for the checkOut
	 * @param numRooms the number of available rooms
	 * @throws NullPointerException if location is null
	 * @throws IllegalArgumentException if price is negative
	 */
	public Lodging(Location location, Date checkIn, Date checkOut, int price, int numRooms) {
		if (location == null) {
			throw new NullPointerException("Parameter location cannot be null");
		}
		this.location = location;

		if (checkIn == null) {
			throw new NullPointerException("Parameter checkIn cannot be null");
		}
		this.checkIn = checkIn;

		if (checkOut == null) {
			throw new NullPointerException("Parameter checkOut cannot be null");
		}
		this.checkOut = checkOut;
		
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
	 * Uses the yyyy-MM-dd format
	 * @param location Location enum for the location
	 * @param price the price in CENTS per room
	 * @param checkIn the String object for the checkIn
	 * @param checkOut the String object for the checkOut
	 * @param numRooms the number of available rooms
	 * @throws NullPointerException if location is null
	 * @throws IllegalArgumentException if price is negative
	 * @throws java.text.ParseException if it cannot parse the dates
	 */
	public Lodging(Location location, String checkIn, String checkOut, int price, int numRooms) throws java.text.ParseException {
		if (location == null) {
			throw new NullPointerException("Parameter location cannot be null");
		}
		this.location = location;

		this.checkIn = format.parse(checkIn);

		this.checkOut = format.parse(checkOut);

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
		this.location = location;
	}
	
	/**
	 * Simple getter for price per room
	 * @return the price in CENTS per room
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
	 * Simple getter for the checkIn object
	 * @return the checkIn date
	 */
	public Date getCheckIn() {
		return checkIn;
	}

	/**
	 * Simple setter for the date using a Date object
	 * @param checkIn the date for checkIn
	 */
	public void setCheckIn(Date checkIn) {
		if (checkIn == null) {
			throw new NullPointerException("Parameter date cannot be null");
		}
		this.checkIn = checkIn;
	}

	/**
	 * Simple setter for the checkIn using a String object
	 * Uses the yyyy-MM-dd format
	 * @param checkIn the date for checkIn
	 * @throws java.text.ParseException if it cannot parse the date
	 */
	public void setCheckIn(String checkIn) throws java.text.ParseException {
		this.checkIn = format.parse(checkIn);
	}

	/**
	 * Simple getter for the checkOut object
	 * @return the checkOut date
	 */
	public Date getCheckOut() {
		return checkOut;
	}

	/**
	 * Simple setter for the date using a Date object
	 * @param checkOut the date for checkOut
	 */
	public void setcheckOut(Date checkOut) {
		if (checkOut == null) {
			throw new NullPointerException("Parameter date cannot be null");
		}
		this.checkOut = checkOut;
	}

	/**
	 * Simple setter for the checkOut using a String object
	 * Uses the yyyy-MM-dd format
	 * @param checkOut the date for checkOut
	 * @throws java.text.ParseException if it cannot parse the date
	 */
	public void setcheckOut(String checkOut) throws java.text.ParseException {
		this.checkOut = format.parse(checkOut);
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
	
	/**
	 * Simple method that outputs the Lodging object in a csv comma separated format
	 * @param separator the separator used between items, usually a comma
	 * @param stringMarker the string "isolator", usually double quotes
	 * @return A string in csv format: id,location,checkIn,checkOut,price,numRooms
	 */
	public String toCsv(char separator, char stringMarker) {
		return(id + separator +
		       stringMarker + location.name() + stringMarker + separator +
		       stringMarker + format.format(checkIn) + stringMarker + separator +
		       stringMarker + format.format(checkOut) + stringMarker + separator +
		       price + separator +
		       numRooms);
	}

	/**
	 * A simple to string method for using with print to visualize a Lodging object
	 * @return a string visualization of the Lodging
	 */
	@Override
	public String toString() {
		return("Lodging: " +
		       "Location=" + location + " " +
		       "CheckIn=" + checkIn + " " +
		       "CheckOut=" + checkOut + " " +
		       "Price per room=R$" + price/100 + "." + price%100);
	}

}
