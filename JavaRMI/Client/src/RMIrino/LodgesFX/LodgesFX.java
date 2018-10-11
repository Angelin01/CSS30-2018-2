package RMIrino.LodgesFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LodgesFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("LodgesFX.fxml"));
        primaryStage.setTitle("Janela Quartos");
        primaryStage.setScene(new Scene(root, 600,400));
        primaryStage.show();
    }
}

