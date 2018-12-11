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
	private static final int PORT_TICKET = 1338;
	private static final int PORT_LODGING = 1339;

	public static void main (String[] args) throws RemoteException, NotBoundException {
		final int PORT = 1337;
		Registry nameServiceReferenceTicket = LocateRegistry.createRegistry(PORT_TICKET);
		Registry nameServiceReferenceLodging = LocateRegistry.createRegistry(PORT_LODGING);

		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		ServImpl servico = new ServImpl(referenciaServicoNomes, (InterfaceLodging) nameServiceReferenceLodging.lookup("lodging"));
		referenciaServicoNomes.rebind("coordenador", servico);
	}
}
