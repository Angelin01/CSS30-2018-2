package RMIrino.TicketsFX;

import RMIrino.CliImpl;
import RMIrino.InterfaceCli;
import RMIrino.InterfaceServ;
import Travel.Location;
import Travel.PlaneTicket;
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

public class TicketsController {
    InterfaceServ server;
    InterfaceCli client;

    private ObservableList<PlaneTicket> masterData = FXCollections.observableArrayList();

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
    private TableView<PlaneTicket> ticketsTable;
    @FXML
    private TableColumn<PlaneTicket, Number> idColumn;
    @FXML
    private TableColumn<PlaneTicket, Number> seatsColumn;
    @FXML
    private TableColumn<PlaneTicket, String> priceColumn;
    @FXML
    private TableColumn<PlaneTicket, String> destinyColumn;
    @FXML
    private TableColumn<PlaneTicket, String> originColumn;
    @FXML
    private TableColumn<PlaneTicket, Date> departureColumn;
    @FXML
    private TableColumn<PlaneTicket, Date> returnColumn;
    @FXML
    private Button btnBack;

    @FXML
    public void initialize(){
        // Initialize the columns.
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        destinyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDestiny().toString()));
        seatsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumSeats()));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty("R$ " + cellData.getValue().getPrice()/100 + new DecimalFormat("#.00").format(cellData.getValue().getPrice()%100)));
        originColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrigin().toString()));
        departureColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getDepartureDate()));
        returnColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getReturnDate()));

        // Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<PlaneTicket> filteredData = new FilteredList<>(masterData, p -> true);

        // Set the filter Predicate whenever the filter changes.
        originField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ticket -> {
                // If filter text is empty, display all tickets.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare origin from table with filter.
                String lowerCaseFilter = newValue.toLowerCase();

                if (ticket.getOrigin().toString().toLowerCase().contains(lowerCaseFilter)) {
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

                if (ticket.getDestiny().toString().toLowerCase().contains(lowerCaseFilter)) {
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

                if (lowerCaseFilter != null && ticket.getDepartureDate() != null){
                    if (ticket.getDepartureDate().getDate() == lowerCaseFilter.getDate() && ticket.getDepartureDate().getMonth() == lowerCaseFilter.getMonth() && ticket.getDepartureDate().getYear() == lowerCaseFilter.getYear()) {
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

                if (lowerCaseFilter != null && ticket.getReturnDate() != null){
                    if (ticket.getReturnDate().getDate() == lowerCaseFilter.getDate() && ticket.getReturnDate().getMonth() == lowerCaseFilter.getMonth() && ticket.getReturnDate().getYear() == lowerCaseFilter.getYear()) {
                        return true; // Filter matches return date.
                    } else
                        return false; // Does not match.
                }
                else
                    return false;
            });
        });

        // Wrap the FilteredList in a SortedList.
        SortedList<PlaneTicket> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(ticketsTable.comparatorProperty());

        // Add sorted (and filtered) data to the table.
        ticketsTable.setItems(sortedData);
    }

    public void setServer(InterfaceServ server, InterfaceCli client) throws RemoteException {
        this.server = server;
        this.client = this.client;
        List<PlaneTicket> planeTickets = null;
        planeTickets = server.getPlaneTickets();
        masterData.addAll(planeTickets);
    }

    /**
     * This function calls the server buyTicket method with the inputs from the fields
     * @throws RemoteException
     */
    public void buyTicket() throws RemoteException {
        if (seatField.getText() == null || seatField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informação da passagem");
            alert.setContentText("Erro no processamento da compra, verifique o numero de passageiros.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
        else if (this.server.buyPlaneTicket(ticketsTable.getSelectionModel().getSelectedItem().getId(), Integer.valueOf(seatField.getText())) == true){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informação da passagem");
            alert.setContentText("Passagem comprada com sucesso!");
            alert.setHeaderText(null);
            alert.showAndWait();

            List<PlaneTicket> planeTickets = server.getPlaneTickets();
            masterData.remove(0, masterData.size());
            masterData.addAll(planeTickets);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Informação da passagem");
            alert.setContentText("Erro no processamento da compra, verifique o numero de passageiros.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    public void btnBackAction(){
        btnBack.getScene().getWindow().hide();
    }

}
