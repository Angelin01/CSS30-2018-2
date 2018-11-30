package RMIrino;

import Travel.Location;
import Travel.PlaneTicket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

public class PlaneTicketImpl extends UnicastRemoteObject implements InterfacePlaneTicket {
	private static final int MILLIS_IN_DAY = 86400000;
	private ArrayList<PlaneTicket> listPlaneTickets;
	private ArrayList<PlaneTicket> tmpPlaneTickets;
	private Logger logger;
	private BufferedReader mainReader;
	private BufferedWriter mainWriter;
	private BufferedReader tmpReader;
	private BufferedWriter tmpWriter;

	public PlaneTicketImpl(BufferedReader mainReader, BufferedWriter mainWriter,
						   BufferedReader tmpReader, BufferedWriter tmpWriter,
						   Logger logger) throws IOException, ParseException {
		this.logger = logger;
		this.mainReader = mainReader;
		this.mainWriter = mainWriter;
		this.tmpReader = tmpReader;
		this.tmpWriter = tmpWriter;

		listPlaneTickets = new ArrayList<PlaneTicket>();
		tmpPlaneTickets = new ArrayList<PlaneTicket>();
		updatePlaneTickets();
	}

	protected void updatePlaneTickets() throws IOException, ParseException {
		String line = "";
		while ((line = mainReader.readLine()) != null) {
			listPlaneTickets.add(PlaneTicket.fromCsv(line));
		}
	}

	protected void commitUpdates() throws IOException, ParseException {
		String toSave = "";
		
		for (PlaneTicket planeTicket : tmpPlaneTickets) {
			toSave += planeTicket.toCsv() + "\n";
		}

		mainWriter.write(toSave);
		updatePlaneTickets();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<PlaneTicket> getPlaneTickets() throws RemoteException {
		return listPlaneTickets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<PlaneTicket> getPlaneTickets(Location origin, Location destiny, int maxPrice, Date departureDate, Date returnDate, int minimumSeats) throws RemoteException {
		ArrayList<PlaneTicket> filteredPlaneTickets = new ArrayList<PlaneTicket>();

		for (PlaneTicket planeTicket : listPlaneTickets) {
			// Check the origin filter
			if (origin != null && planeTicket.getOrigin() != origin) { continue; }

			// Check the destiny filter
			if (destiny != null && planeTicket.getDestiny() != destiny) { continue; }

			// Check the price filter
			if (maxPrice > 0 && planeTicket.getPrice() > maxPrice) { continue; }

			// Check the departure date filter
			// To check that it's the same day, calculates the Julian Day Number
			if (departureDate != null && planeTicket.getDepartureDate().getTime()/MILLIS_IN_DAY != (departureDate.getTime()/MILLIS_IN_DAY)) { continue; }

			// Check the return date filter
			// To check that it's the same day, calculates the Julian Day Number
			if (returnDate != null && planeTicket.getReturnDate().getTime()/MILLIS_IN_DAY != returnDate.getTime()/MILLIS_IN_DAY) { continue; }

			// Check the minimum available seats filter
			if (minimumSeats > 0 && planeTicket.getNumSeats() < minimumSeats) { continue; }

			// Passed all filters, add to return list
			filteredPlaneTickets.add(planeTicket);
		}

		return filteredPlaneTickets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean buyPlaneTicket(int planeTicketID, int numTickets, boolean isPackage) throws RemoteException {
		for (PlaneTicket planeTicket : listPlaneTickets) {
			if (planeTicket.getId() == planeTicketID) {
				synchronized (planeTicket) {
					if (planeTicket.getNumSeats() < numTickets) {
						return false;
					}

					System.out.println("Client just successfully bought " + numTickets + " tickets:\n" +
							planeTicket);
					planeTicket.setNumSeats(planeTicket.getNumSeats() - numTickets);
					return true;
				}
			}
		}
		return false;
	}
}
