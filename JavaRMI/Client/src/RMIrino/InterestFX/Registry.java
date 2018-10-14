package RMIrino.InterestFX;

import Travel.Location;
import java.util.Date;

public class Registry {

    private int price;
    private int id;
    private String type;
    private Location destiny;
    private Location origin;
    private Date departureDate;
    private Date returnDate;

    Registry(int id, int price, String type, Location destiny, Location origin, Date departureDate, Date returnDate){
        if (price < 0) {
            throw new IllegalArgumentException("Parameter price cannot be negative");
        }
        this.price = price;
        this.type = type;
        this.destiny = destiny;
        this.origin = origin;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.id = id;
    }

    /**
     * Simple getter for the price
     * @return the price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Simple getter for the DepartureDate
     * @return the DepartureDate
     */
    public Date getDepartureDate() {
        return departureDate;
    }

    /**
     * Simple getter for the ReturnDate
     * @return the ReturnDate
     */
    public Date getReturnDate() {
        return returnDate;
    }

    /**
     * Simple getter for the Id
     * @return the Id
     */
    public int getId() {
        return id;
    }

    /**
     * Simple getter for the Destiny
     * @return the Destiny
     */
    public Location getDestiny() {
        return destiny;
    }

    /**
     * Simple getter for the Origin
     * @return the Origin
     */
    public Location getOrigin() {
        return origin;
    }

    /**
     * Simple getter for the Type
     * @return the Type
     */
    public String getType() {
        return type;
    }

}
