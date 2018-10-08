package RMIrino.ClientWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


public class ClientWindow extends Application {

    /**
     *
     * @param primaryStage The top level container for JavaFX application
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //ClientController customControl = new ClientController();

        Parent root = FXMLLoader.load(getClass().getResource("ClientWindow.fxml"));
        primaryStage.setTitle("Janela Cliente");
        primaryStage.setScene(new Scene(root, 600,400));
        primaryStage.show();
    }


}