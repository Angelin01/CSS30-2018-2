package RMIrino;

import Travel.Lodging;
import Travel.PlaneTicket;
import Travel.TravelPackage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCli extends Remote {
    void notifyPlaneTicket(int id, PlaneTicket planeTicket) throws RemoteException;
    void notifyLodging(int id, Lodging lodging) throws RemoteException;
    void notifyTravelPackage(int id, TravelPackage travelPackage) throws RemoteException;
}
