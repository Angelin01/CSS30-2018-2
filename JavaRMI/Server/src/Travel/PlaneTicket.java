package Travel;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PlaneTicket implements Serializable {
	private String destiny;
	private String origin;
	private Date departureDate;
	private Date returnDate;
	private int price;
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Constructor for a PlaneTicket object using Date objects
	 * @param destiny String with the name of the destiny location
	 * @param origin String with the name of the origin location
	 * @param departureDate Date object of departure
	 * @param returnDate Date object of return. If null, ticket is one way
	 * @param price price in CENTS for the ticket
	 * @throws NullPointerException if null is passed to destiny, origin or departure date
	 * @throws IllegalArgumentException if price is negative
	 */
	public PlaneTicket(String destiny, String origin, Date departureDate, Date returnDate, int price) {
		if(destiny == null) {
			throw new NullPointerException("Parameter destiny cannot be null");
		}
		this.destiny = destiny;

		if(origin == null) {
			throw new NullPointerException("Parameter origin cannot be null");
		}
		this.origin = origin;

		if(departureDate == null) {
			throw new NullPointerException("Parameter origin cannot be null");
		}
		this.departureDate = departureDate;

		this.returnDate = returnDate;
		
		if (price < 0) {
			throw new IllegalArgumentException("Price cannot be negative");
		}
		this.price = price;
	}
	
	/**
	 * Constructor for a PlaneTicket object using Strings for dates
	 * @param destiny String with the name of the destiny location
	 * @param origin String with the name of the origin location
	 * @param departureDate String date of departure
	 * @param returnDate String date of return. If null, ticket is one way
	 * @param price price in CENTS for the ticket
	 * @throws java.text.ParseException if the format for the date strings is incorrect
	 * @throws NullPointerException if null is passed to destiny, origin or departure date
	 * @throws IllegalArgumentException if price is negative
	 */
	public PlaneTicket(String destiny, String origin, String departureDate, String returnDate, int price)
			throws java.text.ParseException {
		if(destiny == null) {
			throw new NullPointerException("Parameter destiny cannot be null");
		}
		this.destiny = destiny;

		if(origin == null) {
			throw new NullPointerException("Parameter origin cannot be null");
		}
		this.origin = origin;

		if(departureDate == null) {
			throw new NullPointerException("Parameter departureDate cannot be null");
		}
		this.departureDate = format.parse(departureDate);
		
		if (returnDate == null) {
			this.returnDate = null;
		}
		else {
			this.returnDate = format.parse(returnDate);
		}
		
		if (price < 0) {
			throw new IllegalArgumentException("Price cannot be negative");
		}
		this.price = price;
	}
	
	/**
	 * Simple getter for destiny
	 * @return the destiny String
	 */
	public String getDestiny() {
		return destiny;
	}
	
	/**
	 * Setter for destiny
	 * @param destiny Sets destiny. Must be a valid location or will fail
	 * @throws NullPointerException if destiny is null
	 */
	public void setDestiny(String destiny) {
		if (destiny == null) {
			throw new NullPointerException("Parameter destiny cannot be null");
		}
		// @todo Check if location is valid
		this.destiny = destiny;
	}
	
	/**
	 * Simple getter for origin
	 * @return the origin String
	 */
	public String getOrigin() {
		return origin;
	}
	
	/**
	 * Setter for destiny
	 * @param origin Sets destiny. Must be a valid location or will fail
	 * @throws NullPointerException if origin is null
	 */
	public void setOrigin(String origin) {
		if (origin == null) {
			throw new NullPointerException("Parameter origin cannot be null");
		}
		// @todo Check if location is valid
		this.origin = origin;
	}
	
	/**
	 * Simple getter for origin
	 * @return the departure Date object
	 */
	public Date getDepartureDate() {
		return departureDate;
	}
	
	/**
	 * Setter for departureDate using Date objecs
	 * @param departureDate Sets date
	 * @throws NullPointerException if destiny is null
	 */
	public void setDepartureDate(Date departureDate) {
		if (departureDate == null) {
			throw new NullPointerException("Parameter departureDate cannot be null");
		}
		this.departureDate = departureDate;
	}
	
	/**
	 * Setter for departureDate using String objecs
	 * @param departureDate Sets date
	 * @throws java.text.ParseException if cannot parse the String object
	 */
	public void setDepartureDate(String departureDate) throws java.text.ParseException {
		this.departureDate = format.parse(departureDate);
	}
	
	/**
	 * Simple getter for return date
	 * @return the return Date object
	 */
	public Date getReturnDate() {
		return returnDate;
	}
	
	/**
	 * Setter for returnDate using Date objecs
	 * @param returnDate the return Date object
	 */
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	
	/**
	 * Setter for returnDate using String objecs
	 * @param returnDate the String
	 * @throws java.text.ParseException if cannot parse the String object
	 */
	public void setReturnDate(String returnDate) throws java.text.ParseException{
		if (returnDate == null) {
			this.returnDate = null;
		}
		this.returnDate = format.parse(returnDate);
	}
	
	/**
	 * Simple getter for the price
	 * @return the price in CENTS
	 */
	public int getPrice() {
		return price;
	}
	
	/**
	 * Simple setter for price
	 * @param price price in CENTS. Cannot be negative
	 */
	public void setPrice(int price) {
		if (price < 0) {
			throw new IllegalArgumentException("Price cannot be negative");
		}
		this.price = price;
	}
}
