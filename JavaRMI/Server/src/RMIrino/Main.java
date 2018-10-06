package RMIrino;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import Travel.*;

public class Main {
	static final Scanner input = new Scanner(System.in);
	static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main (String[] args) throws RemoteException, ParseException {
		final int PORT = 1337;
		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		//ServImpl servico = new ServImpl();
		ServImpl servico = new ServImpl(defaultPlaneTickets(), defaultLodgings(), defaultTravelPackages());
		referenciaServicoNomes.rebind("servico", servico);

		System.out.println(inputPlaneTicket());
	}

	/**
	 * Returns a list of default PlaneTickets
	 * @return the list with 4 PlaneTickets
	 */
	protected static final ArrayList<PlaneTicket> defaultPlaneTickets() {
		ArrayList<PlaneTicket> planeTickets = new ArrayList<PlaneTicket>();
		try {
			planeTickets.add(new PlaneTicket(Location.ARACAJU, Location.CURITIBA, "2018-11-15 08:30:00", "2018-11-25 16:00:00", 150000, 200));
			planeTickets.add(new PlaneTicket(Location.SAO_PAULO, Location.SALVADOR, "2018-10-14 12:30:00", null, 90000, 150));
			planeTickets.add(new PlaneTicket(Location.CURITIBA, Location.MANAUS, "2018-12-01 21:00:00", null, 100000, 150));
			planeTickets.add(new PlaneTicket(Location.CURITIBA, Location.MANAUS, "2018-12-01 21:00:00", "2018-12-05 10:00:00", 125000, 125));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return planeTickets;
	}

	/**
	 * Returns a list of default Lodgings
	 * @return the list with 4 Lodgings
	 */
	protected static final ArrayList<Lodging> defaultLodgings() {
		ArrayList<Lodging> lodgings = new ArrayList<Lodging>();
		try {
			lodgings.add(new Lodging(Location.CURITIBA, "2018-11-15", "2018-11-25", 100000, 100));
			lodgings.add(new Lodging(Location.SAO_PAULO, "2018-10-20", "2018-10-22", 40000, 150));
			lodgings.add(new Lodging(Location.MANAUS, "2018-12-30", "2019-01-04", 125000, 80));
			lodgings.add(new Lodging(Location.FLORIANOPOLIS, "2018-11-25", "2018-11-30", 90000, 120));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lodgings;
	}

	/**
	 * Returns a list of default TravelPackages
	 * @return the list with 2 TravelPackages
	 */
	protected static final ArrayList<TravelPackage> defaultTravelPackages() {
		ArrayList<TravelPackage> travelPackages = new ArrayList<TravelPackage>();
		try {
			travelPackages.add(new TravelPackage(
					new PlaneTicket(Location.ARACAJU, Location.CURITIBA, "2018-11-15 08:30:00", "2018-11-25 16:00:00", 150000, 200),
					new Lodging(Location.ARACAJU, "2018-11-15", "2018-11-25", 100000, 100),
					200000
			));
			travelPackages.add(new TravelPackage(
					new PlaneTicket(Location.SALVADOR, Location.SAO_PAULO, "2018-12-01 10:30:00", "2018-12-05 16:00:00", 100000, 150),
					new Lodging(Location.SALVADOR, "2018-12-01", "2018-12-05", 60000, 80),
					120000
			));
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return travelPackages;
	}

	/**
	 * Constructs a PlaneTicket object reading input from the user
	 * @return the PlaneTicket object
	 */
	protected static PlaneTicket inputPlaneTicket() {
		System.out.println("Available locations:\n" + Location.printAll());

		// Input destination
		System.out.println("Enter a destination for the plane ticket: ");
		Location destiny = null;
		while (destiny == null) {
			try {
				destiny = Location.valueOf(input.next().toUpperCase());
			}
			catch (IllegalArgumentException e) {
				System.out.println("Invalid location! Try again.\nAvailable locations:\n" + Location.printAll());
			}
		}

		// Input origin
		System.out.println("Enter a origin for the plane ticket: ");
		Location origin = null;
		while (origin == null) {
			try {
				origin = Location.valueOf(input.next().toUpperCase());
			}
			catch (IllegalArgumentException e) {
				System.out.println("Invalid location! Try again.\nAvailable locations:\n" + Location.printAll());
			}
		}

		// Read the junk \n from the previous input.next()
		input.nextLine();

		// Input departureDate
		System.out.println("Input a departure date, use the \"yyyy-MM-dd HH:mm:ss\" format:");
		Date departureDate = null;
		while (departureDate == null) {
			try {
				departureDate = datetimeFormat.parse(input.nextLine());
			}
			catch (ParseException e) {
				System.out.println("Invalid date format! Please try again, use the \"yyyy-MM-dd HH:mm:ss\" format:");
			}
		}

		// Input returnDate
		System.out.println("Input a return date. Leave BLANK for a one way ticket. Use the \"yyyy-MM-dd HH:mm:ss\" format:");
		Date returnDate = null;
		String sDate;
		while (returnDate == null) {
			sDate = input.nextLine();
			if (sDate.trim().isEmpty()) {
				break;
			}
			try {
				returnDate = datetimeFormat.parse(sDate);
			} catch (ParseException e) {
				System.out.println("Invalid date format! Please try again, use the \"yyyy-MM-dd HH:mm:ss\" format:");
			}
		}

		// Input price
		System.out.println("Input the price for the ticket in CENTS, must not be negative:");
		int price = -1;
		while (price < 0) {
			while (!input.hasNextInt()) {
				input.next();
				System.out.println("Invalid price! Try again:");
			}
			price = input.nextInt();
			if (price < 0) {
				System.out.println("Price cannot be negative! Try again");
			}
		}

		// Input number of available seats
		System.out.println("Input the amount of available seats, must not be negative:");
		int numSeats = -1;
		while (numSeats < 0) {
			while (!input.hasNextInt()) {
				input.next();
				System.out.println("Invalid number of seats! Try again:");
			}
			numSeats = input.nextInt();
			if (numSeats < 0) {
				System.out.println("Number of seats cannot be negative! Try again");
			}
		}

		return new PlaneTicket(destiny, origin, departureDate, returnDate, price, numSeats);
	}
}
