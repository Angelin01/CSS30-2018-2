package RMIrino;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JanelaCliente {
    private JButton btnTickets;
    private JButton btnRegistries;
    private JButton btnCombos;
    private JButton btnLodges;
    private JPanel panelClient;
    private JLabel labelClient;
    private JFrame frameClient;

    public JanelaCliente() {
        this.frameClient = new JFrame("Janela Cliente");
        this.frameClient.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frameClient.setContentPane(this.getPanelClient());
        this.frameClient.pack();
        this.frameClient.setVisible(true);
        btnTickets.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frameClient.dispose();
                new TicketsWindow();
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
