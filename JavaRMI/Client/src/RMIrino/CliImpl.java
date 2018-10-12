package RMIrino;

import Travel.Lodging;
import Travel.PlaneTicket;
import Travel.TravelPackage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli {
    private InterfaceServ serverReference = null;

    public CliImpl(InterfaceServ serverReference) throws RemoteException {
        this.serverReference = serverReference;
    }

    @Override
    public void echo(String str) throws RemoteException {
        System.out.println(str);
    }

    /*@Override
    public void notifyLodging(int id, Lodging lodging) {

    }

    @Override
    public void notifyTravelPackage(int id, TravelPackage travelPackage) {

    }

    @Override
    public void notifyPlaneTicket(int id, PlaneTicket planeTicket) {

    }*/
}
