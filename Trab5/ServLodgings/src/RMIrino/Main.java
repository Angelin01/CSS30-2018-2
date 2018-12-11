package RMIrino;

import SimpleFileAccess.RecordsFile;
import SimpleFileAccess.RecordsFileException;
import Travel.Location;
import Travel.Lodging;

import java.io.IOException;
import java.rmi.NotBoundException;
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
	static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) throws IOException, RecordsFileException, ClassNotFoundException, NotBoundException {
		final int PORT = 1339;
		final int COORD_PORT = 1337;
		final String dbName = "lodging.db";
		final String tmpDbName = "tmp_lodging.db";
		final String transactionName = "lodging_transaction.db";

		Logger logger = Logger.getLogger("LodgingLog");
		FileHandler logHandler = new FileHandler("./lodging.log");
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

		RecordsFile lodgingDB;
		try {
			lodgingDB = new RecordsFile(dbName, 128);
		}
		catch (RecordsFileException e) {
			lodgingDB = new RecordsFile(dbName, "rw");
		}

		// Tmp DB

		RecordsFile lodgingTmpDB;
		try {
			lodgingTmpDB = new RecordsFile(tmpDbName, 128);
		}
		catch (RecordsFileException e) {
			lodgingTmpDB = new RecordsFile(tmpDbName, "rw");
		}

		// Transcation log

		RecordsFile lodgingTransaction;
		try {
			lodgingTransaction = new RecordsFile(transactionName, 128);
		}
		catch (RecordsFileException e) {
			lodgingTransaction = new RecordsFile(transactionName, "rw");
		}

		logger.info("Starting up Lodging system");
		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		Registry referenciaCoord = LocateRegistry.getRegistry(COORD_PORT);

		LodgingImpl lodgingService = new LodgingImpl(lodgingDB, lodgingTmpDB, lodgingTransaction,
		                                             (InterfaceCoord) referenciaCoord.lookup("coordenador"),logger);
		referenciaServicoNomes.rebind("lodging", lodgingService);

		int choice;
		while (true) {
			choice = -1;
			System.out.println("RMI Lodging management system thingy\n" +
					"If you'd like to perform an action, input an option:\n" +
					"1 - Input new Lodging\n" +
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
					lodgingService.addLodging(inputLodging());
					break;


				case 0:
					System.out.println("Are you sure you want to shutdown the system? (y/[n])");
					if (input.next().equals("y")) {
						System.out.println("Okay, good bye :(");
						referenciaServicoNomes.unbind("servico");
						UnicastRemoteObject.unexportObject(lodgingService, true);
						System.exit(0);
					}
			}
		}
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

}
