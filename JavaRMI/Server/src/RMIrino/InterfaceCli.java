package RMIrino;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCli extends Remote {
    void echo(String str) throws RemoteException;
}
