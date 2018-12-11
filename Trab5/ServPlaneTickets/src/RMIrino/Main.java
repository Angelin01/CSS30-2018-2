package RMIrino;

import SimpleFileAccess.RecordsFile;
import SimpleFileAccess.RecordsFileException;
import Travel.Location;
import Travel.PlaneTicket;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
	static final Scanner input = new Scanner(System.in);
	static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main (String[] args) throws IOException, RecordsFileException, ClassNotFoundException, NotBoundException {
		final int PORT = 1338;
		final int COORD_PORT = 1337;
		final String dbName = "planeticket.db";
		final String tmpDbName = "tmp_planeticket.db";
		final String transactionName = "planeticket_transaction.db";

		Logger logger = Logger.getLogger("PlaneTicketLog");
		FileHandler logHandler = new FileHandler("./plane-ticket.log");
		logHandler.setFormatter(new SimpleFormatter() {
			private static final String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";

			@Override
			public synchronized String format(LogRecord lr) {
				return String.format(format,
						new Date(lr.getMillis()),
						lr.getLevel(),
						lr.getMessage()
				);
			}
		});
		logger.addHandler(logHandler);

		// Main DB

		RecordsFile planeTicketDB;
		try {
			planeTicketDB = new RecordsFile(dbName, 128);
		}
		catch (RecordsFileException e) {
			planeTicketDB = new RecordsFile(dbName, "rw");
		}

		// Tmp DB

		RecordsFile planeTicketTmpDB;
		try {
			planeTicketTmpDB = new RecordsFile(tmpDbName, 128);
		}
		catch (RecordsFileException e) {
			planeTicketTmpDB = new RecordsFile(tmpDbName, "rw");
		}

		// Transcation log

		RecordsFile planeTicketTransaction;
		try {
			planeTicketTransaction = new RecordsFile(transactionName, 128);
		}
		catch (RecordsFileException e) {
			planeTicketTransaction = new RecordsFile(transactionName, "rw");
		}

		logger.info("Opened database " + dbName);
		logger.info("Starting up Plane Ticket system");

		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		Registry referenciaCoord = LocateRegistry.getRegistry(COORD_PORT);

		PlaneTicketImpl planeTicketService = new PlaneTicketImpl(planeTicketDB, planeTicketTmpDB, planeTicketTransaction,
		                                                         (InterfaceCoord) referenciaCoord.lookup("coordenador"), logger);
		referenciaServicoNomes.rebind("planeticket", planeTicketService);

		int choice;
		while (true) {
			choice = -1;
			System.out.println("RMI PlaneTicket management system thingy\n" +
					"If you'd like to perform an action, input an option:\n" +
					"1 - Input new PlaneTicket\n" +
					"0 - Shutdown service");
			while (choice < 0) {
				while (!input.hasNextInt()) {
					input.next();
					System.out.println("Invalid option! Try again:");
				}
				choice = input.nextInt();
				if (choice != 0 || choice != 1) {
					System.out.println("Invalid option! Try again:");
				}
			}

			switch(choice) {
				case 1:
					planeTicketService.addPlaneTicket(inputPlaneTicket());
					break;


				case 0:
					System.out.println("Are you sure you want to shutdown the system? (y/[n])");
					if (input.next().equals("y")) {
						System.out.println("Okay, good bye :(");
						referenciaServicoNomes.unbind("servico");
						UnicastRemoteObject.unexportObject(planeTicketService, true);
						System.exit(0);
					}
			}
		}
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

}
