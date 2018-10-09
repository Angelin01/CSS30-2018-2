package RMIrino.TicketsFX;

import RMIrino.InterfaceServ;
import Travel.PlaneTicket;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class TicketsController {
     private ObservableList<PlaneTicket> masterData = FXCollections.observableArrayList();
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
    public void initialize(){
        //String stringPreco = "R$" + preco/100 + "," + preco%100
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()));
        destinyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDestiny().toString()));
        seatsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNumSeats()));
        priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty("R$ " + cellData.getValue().getPrice()/100 + new DecimalFormat("#.00").format(cellData.getValue().getPrice()%100)));
        originColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrigin().toString()));
        departureColumn.setCellValueFactory(cellData -> new SimpleObjectProperty(cellData.getValue().getDepartureDate()));
        returnColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getReturnDate()));
        ticketsTable.setItems(masterData);
    }

    public void setServer(InterfaceServ server) throws RemoteException {
        List<PlaneTicket> planeTickets = server.getPlaneTickets();
        masterData.addAll(planeTickets);
    }


}
