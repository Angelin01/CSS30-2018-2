package RMIrino;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCli extends Remote {
    void notify(int id) throws RemoteException;
}
