package RMIrino;

import Travel.PlaneTicket;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class TicketsWindow {
    private InterfaceServ server;
    private JPanel panelTickets;
    private JTable tableTickets;
    private JButton button1;
    private JFrame frameTickets;


    public TicketsWindow(InterfaceServ server) throws RemoteException {
        int columnNumber = 8;
        this.server = server;
        this.frameTickets = new JFrame("Janela Passagens");
        this.frameTickets.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frameTickets.setContentPane(this.panelTickets);
        this.frameTickets.setSize(800, 600);
        this.frameTickets.setVisible(true);

        List<PlaneTicket> ticketList = server.getPlaneTickets();

        String[] columnNames = new String[] {
                "ID", "Origem", "Destino", "Ida", "Volta", "Preço (R$)", "Comprar", "Registrar"
        };

        Object[][] content = new Object[ticketList.size()][columnNumber];
        int i = 0;
        for(PlaneTicket planeTicket : ticketList) {
            content[i][0] = planeTicket.getId();
            content[i][1] = planeTicket.getOrigin();
            content[i][2] = planeTicket.getDestiny();
            content[i][3] = planeTicket.getDepartureDate();
            content[i][4] = planeTicket.getReturnDate();
            content[i][5] = planeTicket.getPrice();
            i++;
        }

        tableTickets.setModel(new DefaultTableModel(content,columnNames));

    }

}
