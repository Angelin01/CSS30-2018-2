package RMIrino;

import SimpleFileAccess.RecordsFile;
import SimpleFileAccess.RecordsFileException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
	public static void main(String[] args) throws IOException, RecordsFileException, ClassNotFoundException, NotBoundException {
		final int PORT = 1339;
		final int COORD_PORT = 1337;
		final String dbName = "lodging.db";
		final String tmpDbName = "tmp_lodging.db";
		final String transactionName = "lodging_transaction.db";

		Logger logger = Logger.getLogger("LodgingLog");
		FileHandler logHandler = new FileHandler("./lodging.log");
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

		RecordsFile lodgingDB;
		try {
			lodgingDB = new RecordsFile(dbName, 128);
		}
		catch (RecordsFileException e) {
			lodgingDB = new RecordsFile(dbName, "rw");
		}

		// Tmp DB

		RecordsFile lodgingTmpDB;
		try {
			lodgingTmpDB = new RecordsFile(tmpDbName, 128);
		}
		catch (RecordsFileException e) {
			lodgingTmpDB = new RecordsFile(tmpDbName, "rw");
		}

		// Transcation log

		RecordsFile lodgingTransaction;
		try {
			lodgingTransaction = new RecordsFile(transactionName, 128);
		}
		catch (RecordsFileException e) {
			lodgingTransaction = new RecordsFile(transactionName, "rw");
		}

		logger.info("Starting up Lodging system");
		Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
		Registry referenciaCoord = LocateRegistry.getRegistry(COORD_PORT);

		LodgingImpl lodgingService = new LodgingImpl(lodgingDB, lodgingTmpDB, lodgingTransaction,
		                                             (InterfaceCoord) referenciaCoord.lookup("coordenador"),logger);
		referenciaServicoNomes.rebind("lodging", lodgingService);
	}
}
