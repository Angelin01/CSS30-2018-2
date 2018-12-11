package RMIrino;

import SimpleFileAccess.RecordsFileException;
import Travel.Location;
import Travel.Lodging;
import Travel.PlaneTicket;
import Travel.TravelPackage;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;


public class ServImpl extends UnicastRemoteObject implements InterfaceServ {
	private static final int MILLIS_IN_DAY = 86400000;
	private Registry servTicketLookup;
	private Registry servLodgingLookup;
	private int idTransaction;
	private static int nextId = 0;
/*	private static final int PORT_TICKET = 1338;
	private static final int PORT_LODGING = 1339;*/

	/**
	 * Simple constructor for ServImpl with pre existing lists
	 *
	 */
	public ServImpl(Registry servTicketLookup, Registry servLodgingLookup) throws RemoteException {
		this.servTicketLookup = servTicketLookup;
		this.servLodgingLookup = servLodgingLookup;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Lodging> getLodgings() throws RemoteException {
		try {
			return ((InterfaceLodging) servLodgingLookup.lookup("lodging")).getLodgings();
		} catch (NotBoundException e) {
			return new ArrayList<Lodging>();
		}
	}

	/**
	 * Rebind
	 */
	/*public void rebind() throws RemoteException, NotBoundException {
		Registry nameServiceReferenceTicket = LocateRegistry.createRegistry(PORT_TICKET);
		Registry nameServiceReferenceLodging = LocateRegistry.createRegistry(PORT_LODGING);
		this.servTicket = (InterfacePlaneTicket) nameServiceReferenceTicket.lookup("planeticket");
		this.servLodging = (InterfaceLodging) nameServiceReferenceLodging.lookup("lodging");
	}*/

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
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
		try {
			return ((InterfacePlaneTicket) servTicketLookup.lookup("planeticket")).getPlaneTickets();
		} catch (NotBoundException e) {
			return new ArrayList<PlaneTicket>();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Deprecated
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
		try {
			return ((InterfacePlaneTicket) servTicketLookup.lookup("planeticket")).buyPlaneTicket(planeTicketID, numTickets);
		} catch (NotBoundException e) {
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyLodging(int lodgingID, int numRooms) throws RemoteException {
		try {
			return ((InterfaceLodging) servLodgingLookup.lookup("lodging")).buyLodging(lodgingID, numRooms);
		} catch (NotBoundException e) {
			return false;
		}
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
				this.idTransaction = nextId++;
				// Create log file
				List<String> read = null;
				List<String> readTicket = null;
				List<String> readLodging = null;
				Path ticketLog = Paths.get(String.format("%s_ticket.txt", idTransaction));
				Path lodgingLog = Paths.get(String.format("%s_lodging.txt", idTransaction));
				Path transaction = Paths.get(String.format("%s_transaction.txt", idTransaction));
				try {
					read = Files.readAllLines(ticketLog, Charset.forName("UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!read.contains("OK")) {
					// Starting ticket buying
					try {
						try {
							if (((InterfacePlaneTicket) servTicketLookup.lookup("planeticket")).buyPackagePlaneTicket(pack.getPlaneTicket().getId(), numPackages, idTransaction)) {
								// Ticket buying complete
								List<String> lines = Arrays.asList("OK");
								try {
									Files.write(ticketLog, lines, Charset.forName("UTF-8"));
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								// Ticket buying failed
								List<String> lines = Arrays.asList("FAIL");
								try {
									Files.write(ticketLog, lines, Charset.forName("UTF-8"));
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						} catch (NotBoundException e) {
							DO SOME STUFF HERE
						}
					} catch (RecordsFileException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}

				try {
					read = Files.readAllLines(lodgingLog, Charset.forName("UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (!read.contains("OK")) {
					// Starting lodging buying
					try {
						if (((InterfaceLodging) servLodgingLookup.lookup("lodging")).buyPackageLodging(pack.getLodging().getId(), numPackages)) {
							// Lodging buying complete
							List<String> lines = Arrays.asList("OK");
							try {
								Files.write(lodgingLog, lines, Charset.forName("UTF-8"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						} else {
							// Lodging buying failed
							List<String> lines = Arrays.asList("FAIL");
							try {
								Files.write(lodgingLog, lines, Charset.forName("UTF-8"));
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					} catch (NotBoundException e) {
						DO SOME STUFF HERE
					}
				}
				try {
					readTicket = Files.readAllLines(ticketLog, Charset.forName("UTF-8"));
					readLodging = Files.readAllLines(lodgingLog, Charset.forName("UTF-8"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (readTicket.contains("OK") && readLodging.contains("OK")){
					// Lodging and ticket buying completed
					List<String> lines = Arrays.asList("COMPLETED");
					try {
						Files.write(lodgingLog, lines, Charset.forName("UTF-8"));
						Files.write(ticketLog, lines, Charset.forName("UTF-8"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					lines = Arrays.asList("OK");
					try {
						Files.write(transaction, lines, Charset.forName("UTF-8"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						try {
							((InterfacePlaneTicket) servTicketLookup.lookup("planeticket")).commitTransaction(true);
						} catch (NotBoundException e) {
							DO SOME STUFF HERE
						}
						try {
							((InterfaceLodging) servLodgingLookup.lookup("lodging")).commitTransaction(true);
						} catch (NotBoundException e) {
							DO SOME STUFF HERE
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (RecordsFileException e) {
						e.printStackTrace();
					}
				}
				else{
					// Lodging and ticket buying uncompleted
					List<String> lines = Arrays.asList("UNCOMPLETED");
					try {
						Files.write(lodgingLog, lines, Charset.forName("UTF-8"));
						Files.write(ticketLog, lines, Charset.forName("UTF-8"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					lines = Arrays.asList("FAIL");
					try {
						Files.write(transaction, lines, Charset.forName("UTF-8"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					try {
						try {
							((InterfacePlaneTicket) servTicketLookup.lookup("planeticket")).commitTransaction(false);
						} catch (NotBoundException e) {
							DO SOME STUFF HERE
						}
						try {
							((InterfaceLodging) servLodgingLookup.lookup("lodging")).commitTransaction(false);
						} catch (NotBoundException e) {
							DO SOME STUFF HERE
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (RecordsFileException e) {
						e.printStackTrace();
					}
				}

			}
		}
		return false;
	}

	Boolean continueTransaction(int idTransaction) throws IOException {
		Path transaction = Paths.get(String.format("%s_transaction.txt", idTransaction));
		List<String> readTrans;
		readTrans = Files.readAllLines(transaction, Charset.forName("UTF-8"));

		if (readTrans.contains("OK"))
			return true;
		else
			return false;
	}
}
