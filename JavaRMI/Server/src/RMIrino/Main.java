package RMIrino;

import java.rmi.RemoteException;

import Travel.Location;

public class Main {
	public static void main (String[] args) throws RemoteException {
		/*final int PORT = 1337;
		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		ServImpl servico = new ServImpl();
		referenciaServicoNomes.rebind("servico", servico);*/

		for (Location l : Location.values()) {
			System.out.println(l);
		}
	}
}
