package RMIrino;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.util.ArrayList;

import Travel.*;

public class Main {
	public static void main (String[] args) throws RemoteException, ParseException {
		final int PORT = 1337;
		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		//ServImpl servico = new ServImpl();
		ServImpl servico = new ServImpl(defaultPlaneTickets(), defaultLodgings(), defaultTravelPackages());
		referenciaServicoNomes.rebind("servico", servico);
	}

	/**
	 * Returns a list of default PlaneTickets
	 * @return the list with 4 PlaneTickets
	 */
	private static final ArrayList<PlaneTicket> defaultPlaneTickets() {
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
	private static final ArrayList<Lodging> defaultLodgings() {
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
	private static final ArrayList<TravelPackage> defaultTravelPackages() {
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
}
