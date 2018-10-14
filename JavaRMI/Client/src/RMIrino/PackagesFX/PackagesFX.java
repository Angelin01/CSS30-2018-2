package RMIrino.PackagesFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PackagesFX extends Application {

    /**
     * Starts the window
     * @param primaryStage stage that will show up to the user
     * @throws Exception if there is any error in showing up the window
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("PackagesFX.fxml"));
        primaryStage.setTitle("Janela Pacotes");
        primaryStage.setScene(new Scene(root, 600,400));
        primaryStage.show();
    }
}
