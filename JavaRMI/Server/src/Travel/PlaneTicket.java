package Travel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PlaneTicket {
    private String destiny;
    private String origin;
    private Date departureDate;
    private Date returnDate;
    private boolean bothWays;

    /**
     * Constructor for a PlaneTicket object using Date objects
     * @param destiny String with the name of the destiny location
     * @param origin String with the name of the origin location
     * @param departureDate Date object of departure
     * @param returnDate Date object of return. If bothways is false, must be null
     * @param bothways boolean that's true if the ticket is a two way trip, false if it's a one way
     */
    public PlaneTicket(String destiny, String origin, Date departureDate, Date returnDate, boolean bothways) {
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

        if(bothways && returnDate == null) {
            throw new IllegalArgumentException("If bothways is true, you must provide a return date");
        }
        else if(!bothways && returnDate != null) {
            throw new IllegalArgumentException("If bothways is false, return date must be null");
        }

        this.returnDate = returnDate;
        this.bothWays = bothWays;
    }

    /**
     * Constructor for a PlaneTicket object using String objects for the dates
     * @param destiny String with the name of the destiny location
     * @param origin String with the name of the origin location
     * @param departureDate String date of departure
     * @param returnDate String date of return. If bothways is false, must be null
     * @param bothways boolean that's true if the ticket is a two way trip, false if it's a one way
     */
    public PlaneTicket(String destiny, String origin, String departureDate, String returnDate, boolean bothways) throws java.text.ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        this.departureDate = format.parse(departureDate);

        if(bothways && returnDate == null) {
            throw new IllegalArgumentException("If bothways is true, you must provide a return date");
        }
        else if(!bothways && returnDate != null) {
            throw new IllegalArgumentException("If bothways is false, return date must be null");
        }

        this.returnDate = format.parse(returnDate);
        this.bothWays = bothWays;
    }
}
