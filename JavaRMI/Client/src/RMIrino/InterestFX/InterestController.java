package RMIrino.InterestFX;

import RMIrino.InterfaceCli;
import RMIrino.InterfaceServ;
import Travel.Location;
import Travel.Lodging;
import Travel.TravelPackage;
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

    private ObservableList<TravelPackage> masterData = FXCollections.observableArrayList();

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
    private TableView<TravelPackage> registryTable;
    @FXML
    private TableColumn<TravelPackage, Number> idColumn;
    @FXML
    private TableColumn<TravelPackage, Number> seatsColumn;
    @FXML
    private TableColumn<TravelPackage, Number> roomColumn;
    @FXML
    private TableColumn<TravelPackage, String> priceColumn;
    @FXML
    private TableColumn<TravelPackage, String> destinyColumn;
    @FXML
    private TableColumn<TravelPackage, String> originColumn;
    @FXML
    private TableColumn<TravelPackage, Date> departureColumn;
    @FXML
    private TableColumn<TravelPackage, Date> returnColumn;

    @FXML
    public void initialize(){
        Location[] yourEnums = Location.values();
        optionChoiceBox.setItems(FXCollections.observableArrayList("Passagem", "Hospedagem", "Pacote"));
        originField.setItems(FXCollections.observableArrayList(yourEnums));
        destinyField.setItems(FXCollections.observableArrayList(yourEnums));

        // Initialize the columns.
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        destinyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlaneTicket().getDestiny().toString()));
        seatsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPlaneTicket().getNumSeats()));
        seatsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPlaneTicket().getNumSeats()));
        roomColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getLodging().getNumRooms()));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty("R$ " + cellData.getValue().getPrice()/100 + new DecimalFormat("#.00").format(cellData.getValue().getPrice()%100)));
        originColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlaneTicket().getOrigin().toString()));
        departureColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPlaneTicket().getDepartureDate()));
        returnColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getPlaneTicket().getReturnDate()));

        // Add sorted (and filtered) data to the table.
        registryTable.setItems(masterData);

    }

    public void setServer(InterfaceServ server, InterfaceCli client) throws RemoteException {
        this.server = server;
        this.client = client;
    }

    public void btnInterestAction() throws RemoteException {
        int registry = 0;
        if (optionChoiceBox.getSelectionModel().getSelectedItem() == "Passagem"){
            // Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference
            registry = server.interestPlaneTicket(destinyField.getSelectionModel().getSelectedItem(), originField.getSelectionModel().getSelectedItem(), Date.from(departureField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(returnField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Integer.valueOf(priceField.getText()), client);
            //masterData.add((TravelPackage) server.getTravelPackages(destinyField.getSelectionModel().getSelectedItem(), originField.getSelectionModel().getSelectedItem(), Integer.valueOf(priceField.getText()), Date.from(departureField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(returnField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), 0));
        } else if (optionChoiceBox.getSelectionModel().getSelectedItem() == "Hospedagem"){
            // Location location, Date checkIn, Date checkOut, int maximumPrice, InterfaceCli clientReference
            registry = server.interestLodging(destinyField.getSelectionModel().getSelectedItem(), Date.from(departureField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(returnField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Integer.valueOf(priceField.getText()), client);
        } else {
            // Location destiny, Location origin, Date departureDate, Date returnDate, int maximumPrice, InterfaceCli clientReference
            registry = server.interestTravelPackage(destinyField.getSelectionModel().getSelectedItem(), originField.getSelectionModel().getSelectedItem(), Date.from(departureField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Date.from(returnField.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()), Integer.valueOf(priceField.getText()), client);

        }
        if (registry != -1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação de registro");
            alert.setContentText("Registro criado com sucesso!");
            alert.setHeaderText(null);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informação da registro");
            alert.setContentText("Erro na criação do  registro.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void btnBackAction(){
        btnBack.getScene().getWindow().hide();
    }

}
