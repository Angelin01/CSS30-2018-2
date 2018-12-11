package RMIrino;

import java.io.IOException;
import java.rmi.Remote;

public interface InterfaceCoord extends Remote {

    /**
     *
     * @param idTransaction the id for the current transaction
     * @return true if the transaction should continue or false if it should be aborted
     */
    Boolean continueTransaction(int idTransaction) throws IOException;
}
