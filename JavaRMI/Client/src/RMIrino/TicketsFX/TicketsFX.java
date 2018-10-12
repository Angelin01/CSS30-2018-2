package RMIrino.TicketsFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TicketsFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TicketsFX.fxml"));
        primaryStage.setTitle("Janela Passagens");
        primaryStage.setScene(new Scene(root, 600,400));
        primaryStage.show();
    }
}