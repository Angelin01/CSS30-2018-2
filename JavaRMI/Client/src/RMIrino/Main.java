package RMIrino;

import RMIrino.ClientWindow.ClientWindow;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import Travel.PlaneTicket;
import javafx.application.Application;

public class Main {

    public static void main (String[] args) throws RemoteException {
        // GUI
        Application.launch(ClientWindow.class, args);

    }
}
