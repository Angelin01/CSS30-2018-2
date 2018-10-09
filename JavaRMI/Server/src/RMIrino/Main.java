package RMIrino;

import java.lang.reflect.Array;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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

	public static void main (String[] args) throws RemoteException, NotBoundException {
		final int PORT = 1337;
		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		//ServImpl servico = new ServImpl();
		ServImpl servico = new ServImpl(defaultPlaneTickets(), defaultLodgings(), new ArrayList<TravelPackage>());
		servico.addTravelPackage(new TravelPackage(servico.getPlaneTickets().get(0), servico.getLodgings().get(0), 200000));
		servico.addTravelPackage(new TravelPackage(servico.getPlaneTickets().get(3), servico.getLodgings().get(1), 190000));

		referenciaServicoNomes.rebind("servico", servico);

		int choice;
		while (true) {
			choice = -1;
			System.out.println("RMI Travel Agency management system thingy\n" +
			                   "If you'd like to perform an action, input an option:\n" +
			                   "1 - Input new Plane Ticket\n" +
			                   "2 - Input new Lodging\n" +
			                   "3 - Input new Travel Package\n" +
			                   "0 - Shutdown service");
			while (choice < 0) {
				while (!input.hasNextInt()) {
					input.next();
					System.out.println("Invalid option! Try again:");
				}
				choice = input.nextInt();
				if (choice < 0 || choice > 3) {
					System.out.println("Invalid option! Try again:");
				}
			}

			switch(choice) {
				case 1:
					servico.addPlaneTicket(inputPlaneTicket());
					break;

				case 2:
					servico.addLodging(inputLodging());
					break;

				case 3:
					servico.addTravelPackage(inputTravelPackage(servico));
					break;

				case 0:
					System.out.println("Are you sure you want to shutdown the system? (y/[n])");
					if (input.next().equals("y")) {
						System.out.println("Okay, good bye :(");
						referenciaServicoNomes.unbind("servico");
						UnicastRemoteObject.unexportObject(servico, true);
						System.exit(0);
					}
			}
		}
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
			lodgings.add(new Lodging(Location.ARACAJU, "2018-11-15", "2018-11-25", 90000, 120));
			lodgings.add(new Lodging(Location.CURITIBA, "2018-12-02", "2018-12-05", 100000, 100));
			lodgings.add(new Lodging(Location.SAO_PAULO, "2018-10-20", "2018-10-22", 40000, 150));
			lodgings.add(new Lodging(Location.MANAUS, "2018-12-30", "2019-01-04", 125000, 80));
			lodgings.add(new Lodging(Location.FLORIANOPOLIS, "2018-11-25", "2018-11-30", 90000, 120));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lodgings;
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
				System.out.println("Price cannot be negative! Try again:");
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
				System.out.println("Number of seats cannot be negative! Try again:");
			}
		}

		return new PlaneTicket(destiny, origin, departureDate, returnDate, price, numSeats);
	}

	/**
	 * Constructs a Lodging object reading input from the user
	 * @return the Lodging object
	 */
	protected static Lodging inputLodging() {
		System.out.println("Available locations:\n" + Location.printAll());

		// Input location
		Location location = null;
		while (location == null) {
			try {
				location = Location.valueOf(input.next().toUpperCase());
			}
			catch (IllegalArgumentException e) {
				System.out.println("Invalid location! Try again.\nAvailable locations:\n" + Location.printAll());
			}
		}

		// Read the junk \n from the previous input.next()
		input.nextLine();

		// Input checkIn
		System.out.println("Input a check in date, use the \"yyyy-MM-dd\" format:");
		Date checkIn = null;
		while (checkIn == null) {
			try {
				checkIn = dateFormat.parse(input.nextLine());
			}
			catch (ParseException e) {
				System.out.println("Invalid date format! Please try again, use the \"yyyy-MM-dd\" format:");
			}
		}

		// Input checkOut
		System.out.println("Input a check out date, use the \"yyyy-MM-dd\" format:");
		Date checkOut = null;
		while (checkOut == null) {
			try {
				checkOut = dateFormat.parse(input.nextLine());
			}
			catch (ParseException e) {
				System.out.println("Invalid date format! Please try again, use the \"yyyy-MM-dd\" format:");
			}
		}

		// Input price
		System.out.println("Input the price for the lodging in CENTS, must not be negative:");
		int price = -1;
		while (price < 0) {
			while (!input.hasNextInt()) {
				input.next();
				System.out.println("Invalid price! Try again:");
			}
			price = input.nextInt();
			if (price < 0) {
				System.out.println("Price cannot be negative! Try again:");
			}
		}

		// Input number of available rooms
		// As a reminder, we are a travel agency that specializes in 1 person rooms
		System.out.println("Input the amount of available rooms, must not be negative:");
		int numRooms = -1;
		while (numRooms < 0) {
			while (!input.hasNextInt()) {
				input.next();
				System.out.println("Invalid number of rooms! Try again:");
			}
			numRooms = input.nextInt();
			if (numRooms < 0) {
				System.out.println("Number of rooms cannot be negative! Try again:");
			}
		}

		return new Lodging(location, checkIn, checkOut, price, numRooms);
	}

	/**
	 * Constructs a TravelPackage object reading input from the user
	 * Requires already constructed PlaneTickets and Lodgings
	 * @param servico the already running RMI server
	 * @return the TravelPackage object
	 */
	protected static TravelPackage inputTravelPackage(ServImpl servico) throws RemoteException {
		if (servico.getPlaneTickets().isEmpty()) {
			System.out.println("No plane tickets available! Create one first!");
			servico.addPlaneTicket(inputPlaneTicket());
		}

		if (servico.getLodgings().isEmpty()) {
			System.out.println("No lodgings available! Create one first!");
			servico.addLodging(inputLodging());
		}

		System.out.println("Choose a plane ticket number to add to the travel package, the available tickets are\n");
		for (int i=0; i < servico.getPlaneTickets().size(); ++i) {
			System.out.println(i + " - " + servico.getPlaneTickets().get(i));
		}

		int planeTicketID = -1;
		while (planeTicketID < 0 || planeTicketID > servico.getPlaneTickets().size()) {
			while (!input.hasNextInt()) {
				input.next();
				System.out.println("Invalid ID! Try again:");
			}
			planeTicketID = input.nextInt();
			if (planeTicketID < 0 || planeTicketID > servico.getPlaneTickets().size()) {
				System.out.println("Invalid ID! Try again:");
			}
		}

		System.out.println("\nChoose a lodging number to add to the travel package, the available lodgings are\n");
		for (int i=0; i < servico.getLodgings().size(); ++i) {
			System.out.println(i + " - " + servico.getLodgings().get(i));
		}

		int lodgingID = -1;
		while (lodgingID < 0 || lodgingID > servico.getLodgings().size()) {
			while (!input.hasNextInt()) {
				input.next();
				System.out.println("Invalid ID! Try again:");
			}
			lodgingID = input.nextInt();
			if (lodgingID < 0 || lodgingID > servico.getLodgings().size()) {
				System.out.println("Invalid ID! Try again:");
			}
		}

		System.out.println("Please input the price for the PACKAGE, in CENTS:");
		int price = -1;
		while (price < 0) {
			while (!input.hasNextInt()) {
				input.next();
				System.out.println("Invalid price! Try again:");
			}
			price = input.nextInt();
			if (price < 0) {
				System.out.println("Price cannot be negative! Try again:");
			}
		}
		
		return new TravelPackage(servico.getPlaneTickets().get(planeTicketID), servico.getLodgings().get(lodgingID), price);
	}
}
