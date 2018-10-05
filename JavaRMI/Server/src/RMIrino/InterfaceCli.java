package RMIrino;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCli extends Remote {
    void notifyPlaneTicket(int id) throws RemoteException;
    void notifyLodging(int id) throws RemoteException;
    void notifyTravelPackage(int id) throws RemoteException;
}
