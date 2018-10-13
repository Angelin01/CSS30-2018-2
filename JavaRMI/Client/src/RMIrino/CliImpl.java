package RMIrino;

import RMIrino.PackagesFX.PackagesController;
import Travel.Lodging;
import Travel.PlaneTicket;
import Travel.TravelPackage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
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

    @Override
    public void notifyLodging(int id, Lodging lodging) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informação de cadastro");
                alert.setContentText("Nova hospedagem com interesse cadastrada!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        });
    }

    @Override
    public void notifyTravelPackage(int id, TravelPackage travelPackage) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informação de cadastro");
                alert.setContentText("Novo pacote com interesse cadastrado!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        });
    }

    @Override
    public void notifyPlaneTicket(int id, PlaneTicket planeTicket) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informação de cadastro");
                alert.setContentText("Nova passagem com interesse cadastrada!");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        });
    }
}
