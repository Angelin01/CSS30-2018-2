package RMIrino.LodgesFX;

import RMIrino.InterfaceCli;
import RMIrino.InterfaceServ;
import Travel.Lodging;
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

public class LodgesController {
    InterfaceServ server;
    InterfaceCli client;

    private List<Integer> interestIds;
    private ObservableList<Lodging> masterData = FXCollections.observableArrayList();

    @FXML
    private TextField originField;
    @FXML
    private DatePicker departureField;
    @FXML
    private DatePicker returnField;
    @FXML
    private TextField seatField;
    @FXML
    private TableView<Lodging> lodgesTable;
    @FXML
    private TableColumn<Lodging, Number> idColumn;
    @FXML
    private TableColumn<Lodging, Number> roomColumn;
    @FXML
    private TableColumn<Lodging, String> priceColumn;
    @FXML
    private TableColumn<Lodging, String> locationColumn;
    @FXML
    private TableColumn<Lodging, Date> checkinColumn;
    @FXML
    private TableColumn<Lodging, Date> checkoutColumn;

    // Wrap the FilteredList in a SortedList.
    SortedList<Lodging> sortedData = null;

    @FXML
    public void initialize(){
        // Initialize the columns.
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        roomColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumRooms()));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty("R$ " + cellData.getValue().getPrice()/100 + new DecimalFormat("#.00").format(cellData.getValue().getPrice()%100)));
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation().toString()));
        checkinColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getCheckIn()));
        checkoutColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getCheckOut()));

        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Lodging> filteredData = new FilteredList<>(masterData, p -> true);

        // Set the filter Predicate whenever the filter changes.
        originField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(lodge -> {
                // If filter text is empty, display all lodges.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare origin from table with filter.
                String lowerCaseFilter = newValue.toLowerCase();

                if (lodge.getLocation().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches origin.
                } else
                    return false; // Does not match.
            });
        });

        departureField.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(lodge -> {
                // If filter text is empty, display all lodges.
                if (newValue == null) {
                    return true;
                }

                // Compare departure date from table with filter.
                Date lowerCaseFilter = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
                if (returnField.getValue() != null){
                    if (departureField.getValue().isAfter(returnField.getValue()))
                        returnField.setValue(newValue.plusDays(2));
                }

                if (lowerCaseFilter != null && lodge.getCheckIn() != null){
                    if (lodge.getCheckIn().getDate() == lowerCaseFilter.getDate() && lodge.getCheckIn().getMonth() == lowerCaseFilter.getMonth() && lodge.getCheckIn().getYear() == lowerCaseFilter.getYear()) {
                        return true; // Filter matches departure date.
                    } else
                        return false; // Does not match.
                }
                else
                    return false;
            });
        });

        returnField.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(lodge -> {
                // If filter text is empty, display all lodges.
                if (newValue == null) {
                    return true;
                }

                // Compare return date from table with filter.
                Date lowerCaseFilter = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
                if (departureField.getValue() != null){
                    if(departureField.getValue().isAfter(returnField.getValue()))
                        departureField.setValue(newValue.minusDays(2));
                }

                if (lowerCaseFilter != null && lodge.getCheckOut() != null){
                    if (lodge.getCheckOut().getDate() == lowerCaseFilter.getDate() && lodge.getCheckOut().getMonth() == lowerCaseFilter.getMonth() && lodge.getCheckOut().getYear() == lowerCaseFilter.getYear()) {
                        return true; // Filter matches return date.
                    } else
                        return false; // Does not match.
                }
                else
                    return false;
            });
        });

        // Wrap the FilteredList in a SortedList.
        sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(lodgesTable.comparatorProperty());

        // Add sorted (and filtered) data to the table.
        lodgesTable.setItems(sortedData);

    }

    public void setServer(InterfaceServ server, InterfaceCli client) throws RemoteException {
        this.server = server;
        this.client = client;
        List<Lodging> lodges = null;
        lodges = server.getLodgings();
        masterData.addAll(lodges);
    }

    /**
     * This function calls the server buyLodge method with the inputs from the fields
     * @throws RemoteException
     */
    public void buyLodge() throws RemoteException {
        if (seatField.getText() == null || seatField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informação da hospedagem");
            alert.setContentText("Erro no processamento da compra, verifique o número de quartos.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
        else if (this.server.buyLodging(lodgesTable.getSelectionModel().getSelectedItem().getId(), Integer.valueOf(seatField.getText())) == true){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação da hospedagem");
            alert.setContentText("Hospedagem comprada com sucesso!");
            alert.setHeaderText(null);
            alert.showAndWait();

            List<Lodging> lodges = server.getLodgings();
            masterData.remove(0, masterData.size());
            masterData.addAll(lodges);


        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informação da hospedagem");
            alert.setContentText("Erro no processamento da compra, verifique o número de quartos.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void interestLodge() throws RemoteException {
        // (Location location, Date checkIn, Date checkOut, int maximumPrice, InterfaceCli clientReference)
       if (server.interestLodging(lodgesTable.getSelectionModel().getSelectedItem().getLocation(), lodgesTable.getSelectionModel().getSelectedItem().getCheckIn(), lodgesTable.getSelectionModel().getSelectedItem().getCheckOut(), lodgesTable.getSelectionModel().getSelectedItem().getPrice(), this.client) != -1)
           this.interestIds.add(lodgesTable.getSelectionModel().getSelectedItem().getId());
    }


}
