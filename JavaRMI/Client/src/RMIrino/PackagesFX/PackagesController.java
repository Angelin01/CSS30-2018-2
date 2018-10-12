package RMIrino.PackagesFX;

import RMIrino.CliImpl;
import RMIrino.InterfaceCli;
import RMIrino.InterfaceServ;
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

public class PackagesController {
    InterfaceServ server;
    InterfaceCli client;

    private List<Integer> interestIds;

    private ObservableList<TravelPackage> masterData = FXCollections.observableArrayList();

    @FXML
    private TextField originField;
    @FXML
    private TextField destinyField;
    @FXML
    private DatePicker departureField;
    @FXML
    private DatePicker returnField;
    @FXML
    private TextField seatField;
    @FXML
    private TableView<TravelPackage> packagesTable;
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

        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<TravelPackage> filteredData = new FilteredList<>(masterData, p -> true);

        // Set the filter Predicate whenever the filter changes.
        originField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ticket -> {
                // If filter text is empty, display all tickets.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare origin from table with filter.
                String lowerCaseFilter = newValue.toLowerCase();

                if (ticket.getPlaneTicket().getOrigin().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches origin.
                } else
                    return false; // Does not match.
            });
        });

        destinyField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ticket -> {
                // If filter text is empty, display all tickets.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare destiny from table with filter.
                String lowerCaseFilter = newValue.toLowerCase();

                if (ticket.getPlaneTicket().getDestiny().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches destiny.
                } else
                    return false; // Does not match.
            });
        });

        departureField.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ticket -> {
                // If filter text is empty, display all tickets.
                if (newValue == null) {
                    return true;
                }

                // Compare departure date from table with filter.
                Date lowerCaseFilter = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
                if (returnField.getValue() != null){
                    if (departureField.getValue().isAfter(returnField.getValue()))
                        returnField.setValue(newValue.plusDays(2));
                }

                if (lowerCaseFilter != null && ticket.getPlaneTicket().getDepartureDate() != null){
                    if (ticket.getPlaneTicket().getDepartureDate().getDate() == lowerCaseFilter.getDate() && ticket.getPlaneTicket().getDepartureDate().getMonth() == lowerCaseFilter.getMonth() && ticket.getPlaneTicket().getDepartureDate().getYear() == lowerCaseFilter.getYear()) {
                        return true; // Filter matches departure date.
                    } else
                        return false; // Does not match.
                }
                else
                    return false;
            });
        });

        returnField.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ticket -> {
                // If filter text is empty, display all tickets.
                if (newValue == null) {
                    return true;
                }

                // Compare return date from table with filter.
                Date lowerCaseFilter = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
                if (departureField.getValue() != null){
                    if(departureField.getValue().isAfter(returnField.getValue()))
                        departureField.setValue(newValue.minusDays(2));
                }

                if (lowerCaseFilter != null && ticket.getPlaneTicket().getReturnDate() != null){
                    if (ticket.getPlaneTicket().getReturnDate().getDate() == lowerCaseFilter.getDate() && ticket.getPlaneTicket().getReturnDate().getMonth() == lowerCaseFilter.getMonth() && ticket.getPlaneTicket().getReturnDate().getYear() == lowerCaseFilter.getYear()) {
                        return true; // Filter matches return date.
                    } else
                        return false; // Does not match.
                }
                else
                    return false;
            });
        });

        // Wrap the FilteredList in a SortedList.
        SortedList<TravelPackage> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(packagesTable.comparatorProperty());

        // Add sorted (and filtered) data to the table.
        packagesTable.setItems(sortedData);
    }

    public void setServer(InterfaceServ server, InterfaceCli client) throws RemoteException {
        this.server = server;
        this.client = this.client;
        List<TravelPackage> packages = null;
        packages = server.getTravelPackages();
        masterData.addAll(packages);
    }

    /**
     * This function calls the server buyTicket method with the inputs from the fields
     * @throws RemoteException
     */
    public void buyPackage() throws RemoteException {
        if (seatField.getText() == null || seatField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informação da pacote");
            alert.setContentText("Erro no processamento da compra, verifique o numero de passageiros.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
        else if (this.server.buyTravelPackage(packagesTable.getSelectionModel().getSelectedItem().getId(), Integer.valueOf(seatField.getText())) == true){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação do pacote");
            alert.setContentText("Passagem comprada com sucesso!");
            alert.setHeaderText(null);
            alert.showAndWait();

            List<TravelPackage> travelPackages = server.getTravelPackages();
            masterData.remove(0, masterData.size());
            masterData.addAll(travelPackages);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informação da pacote");
            alert.setContentText("Erro no processamento da compra, verifique o numero de passageiros.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }


}
