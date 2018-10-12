package RMIrino;

import RMIrino.ClientWindow.ClientWindow;
import java.rmi.RemoteException;
import javafx.application.Application;

public class Main {

    public static void main (String[] args) throws RemoteException {
        // GUI
        Application.launch(ClientWindow.class, args);

    }
}
