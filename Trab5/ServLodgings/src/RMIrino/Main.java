package RMIrino;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
	public static void main(String[] args) throws IOException {
		final int PORT = 1339;
		Logger logger = Logger.getLogger("LodgingLog");
		FileHandler logHandler = new FileHandler("./lodging.log");
		logger.addHandler(logHandler);
		logHandler.setFormatter(new SimpleFormatter());


		logger.info("Starting up Lodging system");
		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		// Implementar serviço
	}
}
