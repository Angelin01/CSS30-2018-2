package RMIrino;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class JanelaCliente {
    private InterfaceServ server;
    private JButton btnTickets;
    private JButton btnRegistries;
    private JButton btnCombos;
    private JButton btnLodges;
    private JPanel panelClient;
    private JLabel labelClient;
    private JFrame frameClient;


    public JanelaCliente(InterfaceServ server) {
        this.server = server;
        this.frameClient = new JFrame("Janela Cliente");
        this.frameClient.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frameClient.setContentPane(this.getPanelClient());
        this.frameClient.pack();
        this.frameClient.setVisible(true);
        btnTickets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameClient.dispose();
                try {
                    new TicketsWindow(server);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    public JPanel getPanelClient() {
        return panelClient;
    }

    public JFrame getFrameClient() {
        return frameClient;
    }
}
