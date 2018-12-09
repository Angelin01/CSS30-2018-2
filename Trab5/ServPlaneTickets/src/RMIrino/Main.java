package RMIrino;

import SimpleFileAccess.RecordsFile;
import SimpleFileAccess.RecordsFileException;

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
	public static void main (String[] args) throws IOException, RecordsFileException, ClassNotFoundException {
		final int PORT = 1338;
		final String dbName = "planeticket.db";
		final String tmpDbName = "tmp_planeticket.db";
		final String transactionName = "planeticket_transaction.db";

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

		// Main DB

		RecordsFile planeTicketDB;
		try {
			planeTicketDB = new RecordsFile(dbName, 128);
		}
		catch (RecordsFileException e) {
			planeTicketDB = new RecordsFile(dbName, "rw");
		}

		// Tmp DB

		RecordsFile planeTicketTmpDB;
		try {
			planeTicketTmpDB = new RecordsFile(tmpDbName, 128);
		}
		catch (RecordsFileException e) {
			planeTicketTmpDB = new RecordsFile(tmpDbName, "rw");
		}

		// Transcation log

		RecordsFile planeTicketTransaction;
		try {
			planeTicketTransaction = new RecordsFile(transactionName, 128);
		}
		catch (RecordsFileException e) {
			planeTicketTransaction = new RecordsFile(transactionName, "rw");
		}

		logger.info("Opened database " + dbName);
		logger.info("Starting up Plane Ticket system");

		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		PlaneTicketImpl planeTicketService = new PlaneTicketImpl(planeTicketDB, planeTicketTmpDB, planeTicketTransaction, logger);
		referenciaServicoNomes.rebind("planeticket", planeTicketService);
	}
}
