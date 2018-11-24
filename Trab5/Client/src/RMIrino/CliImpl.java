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

    /**
     * Print requested string
     * @param str - String to print
     * @throws RemoteException
     */
    @Override
    public void echo(String str) throws RemoteException {
        System.out.println(str);
    }

    /**
     * Pops-up a window warning the user that the interested lodging is now available
     * @param id - Unique identification of the interest
     * @param lodging - The complete lodging in which user is interested
     * @throws RemoteException
     */
    @Override
    public void notifyLodging(int id, Lodging lodging) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String lodgingDestiny = lodging.getLocation().toString();
                String lodgingCheckin = lodging.getCheckIn().toString();
                String lodgingCheckout = lodging.getCheckOut().toString();
                int lodgingRooms = lodging.getNumRooms();
                int lodgingPrice = lodging.getPrice();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informação de cadastro");
                alert.setHeaderText("Nova hospedagem com interesse cadastrada! Verifique a tela de hospedagens novamente.");
                alert.setContentText("Local: " + lodgingDestiny + "\n" +
                                    "Check-in: " + lodgingCheckin + "\n" +
                                    "Check-out: " + lodgingCheckout + "\n" +
                                    "Preço(R$): " + lodgingPrice/100 + "," + lodgingPrice%100 + "\n" +
                                    "Quartos vagos: " + lodgingRooms + "\n");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        });
    }

    /**
     * Pops-up a window warning the user that the interested package is now available
     * @param id - Unique identification of the interest
     * @param travelPackage - The complete package in which user is interested
     * @throws RemoteException
     */
    @Override
    public void notifyTravelPackage(int id, TravelPackage travelPackage) throws RemoteException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String lodgingDestiny = travelPackage.getLodging().getLocation().toString();
                String lodgingCheckin = travelPackage.getLodging().getCheckIn().toString();
                String lodgingCheckout = travelPackage.getLodging().getCheckOut().toString();

                String ticketDestiny = travelPackage.getPlaneTicket().getDestiny().toString();
                String ticketOrigin = travelPackage.getPlaneTicket().getOrigin().toString();
                String ticketCheckin = travelPackage.getPlaneTicket().getDepartureDate().toString();
                String ticketCheckout = (travelPackage.getPlaneTicket().getReturnDate() == null) ? "-" : travelPackage.getPlaneTicket().getReturnDate().toString();
                int packageAvailables = travelPackage.getAvailable();
                int packagePrice = travelPackage.getPrice();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informação de cadastro");
                alert.setHeaderText("Novo pacote com interesse cadastrado! Verifique a tela de pacotes novamente.");
                alert.setContentText("Local: " + lodgingDestiny + "\n" +
                        "Check-in: " + lodgingCheckin + "\n" +
                        "Check-out: " + lodgingCheckout + "\n" +
                        "Origem: " + ticketOrigin + "\n" +
                        "Destino: " + ticketDestiny + "\n" +
                        "Ida: " + ticketCheckin + "\n" +
                        "Volta: " + ticketCheckout + "\n" +
                        "Preço(R$): " + packagePrice/100 + "," + packagePrice%100 + "\n" +
                        "Quantidade disponível: " + packageAvailables + "\n");
                alert.showAndWait();
            }
        });
    }

    /**
     * Pops-up a window warning the user that the interested ticket is now available
     * @param id - Unique identification of the interest
     * @param planeTicket - The complete ticket in which user is interested
     * @throws RemoteException
     */
    @Override
    public void notifyPlaneTicket(int id, PlaneTicket planeTicket) throws RemoteException {
        Platform.runLater(new Runnable() {
            String ticketDestiny = planeTicket.getDestiny().toString();
            String ticketOrigin = planeTicket.getOrigin().toString();
            String ticketCheckin = planeTicket.getDepartureDate().toString();
            String ticketCheckout = (planeTicket.getReturnDate() == null) ? "-" : planeTicket.getReturnDate().toString();
            int ticketRooms = planeTicket.getNumSeats();
            int ticketPrice = planeTicket.getPrice();
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Informação de cadastro");
                alert.setHeaderText("Nova passagem com interesse cadastrada! Verifique a tela de passagens novamente.");
                alert.setContentText("Origem: " + ticketOrigin + "\n" +
                        "Destino: " + ticketDestiny + "\n" +
                        "Ida: " + ticketCheckin + "\n" +
                        "Volta: " + ticketCheckout + "\n" +
                        "Preço(R$): " + ticketPrice/100 + "," + ticketPrice%100 + "\n" +
                        "Assentos vagos: " + ticketRooms + "\n");
                alert.showAndWait();
            }
        });
    }
}
