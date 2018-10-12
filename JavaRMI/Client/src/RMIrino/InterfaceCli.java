package RMIrino;

import Travel.Lodging;
import Travel.PlaneTicket;
import Travel.TravelPackage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceCli extends Remote {
    void echo(String str) throws RemoteException;
    //void notifyLodging(int id, Lodging lodging);
    //void notifyTravelPackage(int id, TravelPackage travelPackage);
    //void notifyPlaneTicket(int id, PlaneTicket planeTicket);
}
