package RMIrino.ClientWindow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ClientWindow extends Application {

    /**
     *
     * @param primaryStage The top level container for JavaFX application
     * @throws Exception if there is any error in showing up the window
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("ClientWindow.fxml"));
        primaryStage.setTitle("Janela Cliente");
        primaryStage.setScene(new Scene(root, 900,600));
        primaryStage.show();

    }
}