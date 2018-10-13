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
     * @return the price in CENTS
     */
    public int getPrice() {
        return price;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public int getId() {
        return id;
    }

    public Location getDestiny() {
        return destiny;
    }

    public Location getOrigin() {
        return origin;
    }

    public String getType() {
        return type;
    }

}
