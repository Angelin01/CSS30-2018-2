package RMIrino.ClientWindow;

import RMIrino.CliImpl;
import RMIrino.InterfaceServ;
import RMIrino.LodgesFX.LodgesController;
import RMIrino.TicketsFX.TicketsController;
import RMIrino.TicketsFX.TicketsFX;
import Travel.PlaneTicket;
import com.sun.istack.internal.NotNull;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.lang.model.type.NullType;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class ClientController extends VBox {

    private final int PORT = 1337;
    private Registry nameServiceReference;
    private InterfaceServ server;
    private CliImpl client;

    public ClientController() throws RemoteException {

        this.nameServiceReference = LocateRegistry.getRegistry(PORT);

        try {
            this.server = (InterfaceServ) nameServiceReference.lookup("servico");
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
         this.client = new CliImpl(server);

    }

    public void btnTicketsAction(ActionEvent event) throws IOException, NotBoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RMIrino/TicketsFX/TicketsFX.fxml"));
        Parent tableViewParent = loader.load();
        TicketsController controller = loader.getController();
        controller.setServer(this.server);
        Stage window = new Stage();
        window.setTitle("Janela Passagens");
        window.setScene(new Scene(tableViewParent, 600,400));
        window.show();

        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

    public void btnLodgingAction(ActionEvent event) throws IOException, NotBoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RMIrino/LodgesFX/LodgesFX.fxml"));
        Parent tableViewParent = loader.load();
        LodgesController controller = loader.getController();
        controller.setServer(this.server);
        Stage window = new Stage();
        window.setTitle("Janela Hospedagem");
        window.setScene(new Scene(tableViewParent, 600,400));
        window.show();

        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

}
