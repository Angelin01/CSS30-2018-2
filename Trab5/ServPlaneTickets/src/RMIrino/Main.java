package RMIrino;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
	public static void main (String[] args) throws IOException {
		final int PORT = 1338;
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

		logger.info("Starting up Plane Ticket system");

		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		// Implementar serviço
	}
}
