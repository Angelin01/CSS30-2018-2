package RMIrino.ClientWindow;

import RMIrino.TicketsFX.TicketsFX;
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

public class ClientController extends VBox {

    /*public ClientController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ClientWindow.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }*/

    public void btnTicketsAction(ActionEvent event) throws IOException {
        Parent tableViewParent = FXMLLoader.load(getClass().getResource("/RMIrino/TicketsFX/TicketsFX.fxml"));
        Stage window = new Stage();
        window.setTitle("Janela Passagens");
        window.setScene(new Scene(tableViewParent, 600,400));
        window.show();
        ((Node)(event.getSource())).getScene().getWindow().hide();
    }

}
