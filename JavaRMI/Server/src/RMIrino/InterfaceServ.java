package RMIrino;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceServ extends Remote {
     void call(String str, InterfaceCli clientReference) throws RemoteException;
}
