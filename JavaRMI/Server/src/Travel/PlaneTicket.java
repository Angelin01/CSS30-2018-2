package Travel;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlaneTicket implements Serializable {
	private Location destiny;
	private Location origin;
	private Date departureDate;
	private Date returnDate;
	private int price;
	private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * Constructor for a PlaneTicket object using Date objects and Location enum
	 * @param destiny Location of destiny location
	 * @param origin Location of origin location
	 * @param departureDate Date object of departure
	 * @param returnDate Date object of return. If null, ticket is one way
	 * @param price price in CENTS for the ticket
	 * @throws NullPointerException if null is passed to destiny, origin or departure date
	 * @throws IllegalArgumentException if price is negative
	 */
	public PlaneTicket(Location destiny, Location origin, Date departureDate, Date returnDate, int price) {
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
	 * Constructor for a PlaneTicket object using Strings for dates and the Location enum
	 * @param destiny Location of destiny location
	 * @param origin Location of origin location
	 * @param departureDate String date of departure
	 * @param returnDate String date of return. If null, ticket is one way
	 * @param price price in CENTS for the ticket
	 * @throws java.text.ParseException if the format for the date strings is incorrect
	 * @throws NullPointerException if null is passed to destiny, origin or departure date
	 * @throws IllegalArgumentException if price is negative
	 */
	public PlaneTicket(Location destiny, Location origin, String departureDate, String returnDate, int price)
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
	 * @return the destiny Location
	 */
	public Location getDestiny() {
		return destiny;
	}
	
	/**
	 * Setter for destiny
	 * @param destiny Sets destiny. Must be a valid location or will fail
	 * @throws NullPointerException if destiny is null
	 */
	public void setDestiny(Location destiny) {
		if (destiny == null) {
			throw new NullPointerException("Parameter destiny cannot be null");
		}
		// @todo Check if location is valid
		this.destiny = destiny;
	}
	
	/**
	 * Simple getter for origin
	 * @return the origin Location
	 */
	public Location getOrigin() {
		return origin;
	}
	
	/**
	 * Setter for destiny
	 * @param origin Sets destiny. Must be a valid location or will fail
	 * @throws NullPointerException if origin is null
	 */
	public void setOrigin(Location origin) {
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
			return;
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

	/**
	 * A simple to string method for using with print to visualize a PlaneTicket object
	 * @return a string visualization of the PlaneTicket
	 */
	@Override
	public String toString() {
		return("Ticket:\n" +
		       "Type: " + (returnDate == null ? "One way" : "Round-trip") + "\n" +
		       "Destiny: " + destiny + "\n" +
		       "Origin: " + origin + "\n" +
		       "Departure Date: " + departureDate + "\n" +
		       (returnDate != null ? ("Return date: " + returnDate + "\n") : "") +
		       "Price: $" + price/100 + "." + price%100);
	}
}
