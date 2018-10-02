package RMIrino;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;

import Travel.*;

public class Main {
	public static void main (String[] args) throws RemoteException, ParseException {
		final int PORT = 1337;
		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		ServImpl servico = new ServImpl();
		referenciaServicoNomes.rebind("servico", servico);

		PlaneTicket testP = new PlaneTicket(Location.ARACAJU, Location.CURITIBA, "2018-09-30 00:00:00", "2018-10-07 00:00:00", 75000);
		Lodging testL = new Lodging(Location.CURITIBA, 25000);
		TravelPackage testPkg = new TravelPackage(testP, testL, 80000);

		servico.listPlaneTickets.add(testP);

		System.out.println(testL);
		System.out.println(testP);
		System.out.println(testPkg);
	}
}
