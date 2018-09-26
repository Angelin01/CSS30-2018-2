package Travel;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Lodging implements Serializable {
	private String location;
	private Date checkIn;
	private Date checkOut;
	private int price;
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Constructor for a Lodging object using Date objects
	 * @param location the String location
	 * @param price the price in CENTS
	 * @param checkIn the Date object for checking in
	 * @param checkOut the Date object for checking out
	 * @throws NullPointerException if location, checkIn or checkOut are null
	 * @throws IllegalArgumentException if price is negative
	 */
	public Lodging(String location, Date checkIn, Date checkOut, int price) {
		if (location == null) {
			throw new NullPointerException("Parameter location cannot be null");
		}
		// @todo verify if location is valid
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
	}
	
	/**
	 * Constructor for a Lodging object using Strings objects
	 * @param location the String location
	 * @param price the price in CENTS
	 * @param checkIn the String object for checking in
	 * @param checkOut the String object for checking out
	 * @throws NullPointerException if location is null
	 * @throws IllegalArgumentException if price is negative
	 * @throws java.text.ParseException if cannot parse date Strings
	 */
	public Lodging(String location, String checkIn, String checkOut, int price) throws java.text.ParseException {
		if (location == null) {
			throw new NullPointerException("Parameter location cannot be null");
		}
		// @todo verify if location is valid
		this.location = location;
		
		this.checkIn = format.parse(checkIn);
		this.checkOut = format.parse(checkOut);
		
		if (price < 0) {
			throw new IllegalArgumentException("Parameter price cannot be negative");
		}
		this.price = price;
	}
	
	/**
	 * Simple getter for location
	 * @return location String
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Simple setter for location
	 * @param location
	 * @throws NullPointerException if location is null
	 */
	public void setLocation(String location) {
		if (location == null) {
			throw new NullPointerException("Parameter location cannot be null");
		}
		// @todo verify if location is valid
		this.location = location;
	}
	
	/**
	 * Simple getter for checkIn
	 * @return the Date object for checkIn
	 */
	public Date getCheckIn() {
		return checkIn;
	}
	
	/**
	 * Simple setter for checkIn with Date object
	 * @param checkIn the Date object for checkIn
	 * @throws NullPointerException if checkIn is null
	 */
	public void setCheckIn(Date checkIn) {
		if (checkIn == null) {
			throw new NullPointerException("Parameter checkIn cannot be null");
		}
		this.checkIn = checkIn;
	}
	
	/**
	 * Simple setter for checkIn with String object
	 * @param checkIn the String object for checkIn
	 * @throws java.text.ParseException
	 */
	public void setCheckIn(String checkIn) throws java.text.ParseException {
		this.checkIn = format.parse(checkIn);
	}
	
	/**
	 * Simple getter for checkOut
	 * @return the Date object for checkOut
	 */
	public Date getCheckOut() {
		return checkOut;
	}
	
	/**
	 * Simple setter for checkOut with Date object
	 * @param checkOut the Date object for checkOut
	 * @throws NullPointerException if checkOut is null
	 */
	public void setCheckOut(Date checkOut) {
		if (checkOut == null) {
			throw new NullPointerException("Parameter checkOut cannot be null");
		}
		this.checkOut = checkOut;
	}
	
	/**
	 * Simple setter for checkOut with String object
	 * @param checkOut the String object for checkOut
	 * @throws java.text.ParseException
	 */
	public void setCheckOut(String checkOut) throws java.text.ParseException {
		this.checkOut = format.parse(checkOut);
	}
	
	/**
	 * Simple getter for price
	 * @return the price in CENTS
	 */
	public int getPrice() {
		return price;
	}
	
	/**
	 * Simple setter for price
	 * @param price
	 */
	public void setPrice(int price) {
		if (price < 0) {
			throw new IllegalArgumentException("Parameter price cannot be negative");
		}
		this.price = price;
	}
}
