package RMIrino.InterestFX;

import RMIrino.InterfaceCli;
import RMIrino.InterfaceServ;
import Travel.Location;
import Travel.TravelPackage;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class InterestController {
    InterfaceServ server;
    InterfaceCli client;

    private ObservableList<Registry> masterData = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<Location> originField;
    @FXML
    private ChoiceBox<Location> destinyField;
    @FXML
    private DatePicker departureField;
    @FXML
    private DatePicker returnField;
    @FXML
    private TextField priceField;
    @FXML
    private Button btnBack;
    @FXML
    private ChoiceBox<String> optionChoiceBox;
    @FXML
    private TableView<Registry> registryTable;
    @FXML
    private TableColumn<Registry, Number> idColumn;
    @FXML
    private TableColumn<Registry, String> typeColumn;
    @FXML
    private TableColumn<Registry, String> priceColumn;
    @FXML
    private TableColumn<Registry, String> destinyColumn;
    @FXML
    private TableColumn<Registry, String> originColumn;
    @FXML
    private TableColumn<Registry, Date> departureColumn;
    @FXML
    private TableColumn<Registry, Date> returnColumn;

    @FXML
    public void initialize(){
        Location[] yourEnums = Location.values();
        optionChoiceBox.setItems(FXCollections.observableArrayList("Passagem", "Hospedagem", "Pacote"));
        originField.setItems(FXCollections.observableArrayList(yourEnums));
        destinyField.setItems(FXCollections.observableArrayList(yourEnums));

        // Initialize the columns.
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        destinyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDestiny().toString()));
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty("R$ " + cellData.getValue().getPrice() + new DecimalFormat("#.00").format(00)));
        originColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrigin().toString()));
        departureColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getDepartureDate()));
        returnColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getReturnDate()));

        // Add sorted (and filtered) data to the table.
        registryTable.setItems(masterData);

    }

    public void setServer(InterfaceServ server, InterfaceCli client) throws RemoteException {
        this.server = server;
        this.client = client;
    }

    public void btnInterestAction() throws RemoteException {
        int registry = 0;
        Registry reg = null;
        if (optionChoiceBox.getSelectionModel().getSelectedItem() == "Passagem"){
            // Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference
            registry = server.interestPlaneTicket(destinyField.getSelectionModel().getSelectedItem(), originField.getSelectionModel().getSelectedItem(), Date.from(departureField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(returnField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Integer.valueOf(priceField.getText()), client);
            System.out.println(registry);
            System.out.println(destinyField.getSelectionModel().getSelectedItem().toString());
            System.out.println(Date.from(departureField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()).toString());
            reg = new Registry(registry, Integer.valueOf(priceField.getText()), "Passagem",destinyField.getSelectionModel().getSelectedItem(), originField.getSelectionModel().getSelectedItem(), Date.from(departureField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(returnField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        } else if (optionChoiceBox.getSelectionModel().getSelectedItem() == "Hospedagem"){
            // Location location, Date checkIn, Date checkOut, int maximumPrice, InterfaceCli clientReference
            registry = server.interestLodging(destinyField.getSelectionModel().getSelectedItem(), Date.from(departureField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(returnField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Integer.valueOf(priceField.getText()), client);
            reg = new Registry(registry, Integer.valueOf(priceField.getText()), "Hospedagem", destinyField.getSelectionModel().getSelectedItem(), originField.getSelectionModel().getSelectedItem(), Date.from(departureField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(returnField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        } else {
            // Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference
            registry = server.interestTravelPackage(destinyField.getSelectionModel().getSelectedItem(), originField.getSelectionModel().getSelectedItem(), Date.from(departureField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(returnField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Integer.valueOf(priceField.getText()), client);
            reg = new Registry(registry, Integer.valueOf(priceField.getText()), "Pacote", destinyField.getSelectionModel().getSelectedItem(), originField.getSelectionModel().getSelectedItem(), Date.from(departureField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(returnField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (registry != -1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação de registro");
            alert.setContentText("Registro criado com sucesso!");
            alert.setHeaderText(null);
            alert.showAndWait();
            masterData.add(reg);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informação da registro");
            alert.setContentText("Erro na criação do  registro.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void btnRemoveInterestAction() throws RemoteException {
        Registry reg = registryTable.getSelectionModel().getSelectedItem();
        Boolean removed = false;
        if (reg.getType() == "Passagem") {
            removed = server.removeInterestPlaneTicket(reg.getId(), reg.getDestiny(), reg.getDepartureDate());
            System.out.println(reg.getId());
            System.out.println(reg.getDestiny().toString());
            System.out.println(reg.getDepartureDate().toString());
        } else if (reg.getType() == "Hospedagem") {
            removed = server.removeInterestLodging(reg.getId(), reg.getDestiny(), reg.getDepartureDate());
        } else if (reg.getType() == "Pacote") {
            removed = server.removeInterestTravelPackage(reg.getId(), reg.getDestiny(), reg.getDepartureDate());
        }

        if (removed == true) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação de registro");
            alert.setContentText("Registro removido com sucesso!");
            alert.setHeaderText(null);
            alert.showAndWait();
            masterData.remove(reg);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informação da registro");
            alert.setContentText("Erro na remoção do  registro.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }

    }
    public void btnBackAction(){
        btnBack.getScene().getWindow().hide();
    }

}
