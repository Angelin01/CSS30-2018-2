package RMIrino;

import SimpleFileAccess.RecordsFileException;
import Travel.Location;
import Travel.Lodging;
import Travel.PlaneTicket;
import Travel.TravelPackage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ {
	private static final int MILLIS_IN_DAY = 86400000;
	private InterfacePlaneTicket servTicket;
	private InterfaceLodging servLodging;

	/**
	 * Simple constructor for ServImpl with pre existing lists
	 *
	 */
	public ServImpl(InterfacePlaneTicket servTicket, InterfaceLodging servLodging) throws RemoteException {
		this.servTicket = servTicket;
		this.servLodging = servLodging;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Lodging> getLodgings() throws RemoteException {
		return servLodging.getLodgings();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Lodging> getLodgings(Location location, int maxPrice, Date checkIn, Date checkOut, int minimumRooms) throws RemoteException {
		ArrayList<Lodging> filteredLodgings = new ArrayList<Lodging>();

		for (Lodging lodging : this.getLodgings()) {
			// Check the location filter
			if (location != null && lodging.getLocation() != location) {
				continue;
			}

			// Check the price filter
			if (maxPrice > 0 && lodging.getPrice() > maxPrice) {
				continue;
			}

			// Check the checkIn filter
			// To check that it's the same day, calculates the Julian Day Number
			if (checkIn != null && lodging.getCheckIn().getTime() / MILLIS_IN_DAY != checkIn.getTime() / MILLIS_IN_DAY) {
				continue;
			}

			// Check the checkOut filter
			// To check that it's the same day, calculates the Julian Day Number
			if (checkOut != null && lodging.getCheckOut().getTime() / MILLIS_IN_DAY != checkOut.getTime() / MILLIS_IN_DAY) {
				continue;
			}

			// Check the minimum available rooms filter
			if (minimumRooms > 0 && lodging.getNumRooms() < minimumRooms) {
				continue;
			}

			// Passed all filters, add to return list
			filteredLodgings.add(lodging);
		}

		return filteredLodgings;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<PlaneTicket> getPlaneTickets() throws RemoteException {
		return (ArrayList<PlaneTicket>) servTicket.getPlaneTickets();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<PlaneTicket> getPlaneTickets(Location origin, Location destiny, int maxPrice, Date departureDate, Date returnDate, int minimumSeats) throws RemoteException {
		ArrayList<PlaneTicket> filteredPlaneTickets = new ArrayList<PlaneTicket>();

		for (PlaneTicket planeTicket : this.getPlaneTickets()) {
			// Check the origin filter
			if (origin != null && planeTicket.getOrigin() != origin) {
				continue;
			}

			// Check the destiny filter
			if (destiny != null && planeTicket.getDestiny() != destiny) {
				continue;
			}

			// Check the price filter
			if (maxPrice > 0 && planeTicket.getPrice() > maxPrice) {
				continue;
			}

			// Check the departure date filter
			// To check that it's the same day, calculates the Julian Day Number
			if (departureDate != null && planeTicket.getDepartureDate().getTime() / MILLIS_IN_DAY != (departureDate.getTime() / MILLIS_IN_DAY)) {
				continue;
			}

			// Check the return date filter
			// To check that it's the same day, calculates the Julian Day Number
			if (returnDate != null && planeTicket.getReturnDate().getTime() / MILLIS_IN_DAY != returnDate.getTime() / MILLIS_IN_DAY) {
				continue;
			}

			// Check the minimum available seats filter
			if (minimumSeats > 0 && planeTicket.getNumSeats() < minimumSeats) {
				continue;
			}

			// Passed all filters, add to return list
			filteredPlaneTickets.add(planeTicket);
		}

		return filteredPlaneTickets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<TravelPackage> getTravelPackages() throws RemoteException {
		ArrayList<TravelPackage> travelPackage = new ArrayList<>();
		for (PlaneTicket ticket: this.getPlaneTickets()) {
			for (Lodging lodging : this.getLodgings()) {
				if (lodging.getCheckIn().getTime() / MILLIS_IN_DAY == ticket.getDepartureDate().getTime() / MILLIS_IN_DAY &&
						lodging.getLocation() == ticket.getDestiny()) {
					travelPackage.add(new TravelPackage(ticket, lodging, ticket.getPrice() + lodging.getPrice()));
				}
			}
		}
		return travelPackage;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyPlaneTicket(int planeTicketID, int numTickets) throws ClassNotFoundException, IOException, RecordsFileException {
		return servTicket.buyPlaneTicket(planeTicketID, numTickets);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyLodging(int lodgingID, int numRooms) throws RemoteException {
		return servLodging.buyLodging(lodgingID, numRooms);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyTravelPackage(int travelPackageID, int numPackages) throws RemoteException {
		ArrayList<TravelPackage> travelPackage = null;
		travelPackage = getTravelPackages();

		for (TravelPackage pack : travelPackage){
			if (pack.getId() == travelPackageID){
				// Create log file
				//PrintWriter ticketLog = null;
				//PrintWriter lodgingLog = null;
				List<String> read = null;
				List<String> readTicket = null;
				List<String> readLodging = null;
				Path ticketLog = Paths.get(String.format("ticket_%s.txt", pack.getId()));
				Path lodgingLog = Paths.get(String.format("lodging_%s.txt", pack.getId()));
				try {
					read = Files.readAllLines(ticketLog, Charset.forName("UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!read.contains("OK")) {
					// Starting ticket buying
					if (servTicket.buyPackagePlaneTicket(pack.getPlaneTicket().getId(), numPackages)) {
						// Ticket buying complete
						//ticketLog.print("OK");
						List<String> lines = Arrays.asList("OK");
						try {
							Files.write(ticketLog, lines, Charset.forName("UTF-8"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						// Ticket buying failed
						// ticketLog.print("FAIL");
						List<String> lines = Arrays.asList("FAIL");
						try {
							Files.write(ticketLog, lines, Charset.forName("UTF-8"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				try {
					read = Files.readAllLines(lodgingLog, Charset.forName("UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!read.contains("OK")) {
					// Starting lodging buying
					if (servLodging.buyPackageLodging(pack.getLodging().getId(), numPackages)) {
						// Lodging buying complete
						//lodgingLog.print("OK");
						List<String> lines = Arrays.asList("OK");
						try {
							Files.write(lodgingLog, lines, Charset.forName("UTF-8"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						// Lodging buying failed
						//lodgingLog.print("FAIL");
						List<String> lines = Arrays.asList("FAIL");
						try {
							Files.write(lodgingLog, lines, Charset.forName("UTF-8"));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				try {
					readTicket = Files.readAllLines(ticketLog, Charset.forName("UTF-8"));
					readLodging = Files.readAllLines(lodgingLog, Charset.forName("UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (readTicket.contains("OK") && readLodging.contains("OK")){
					readTicket.clear();
					readLodging.clear();
				}

			}
		}
		return false;
	}


}
