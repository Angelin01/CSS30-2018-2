package RMIrino;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TicketsWindow {
    private JPanel panelTickets;
    private JTable tableTickets;
    private JFrame frameTickets;

    public TicketsWindow(){
        this.frameTickets = new JFrame("Janela Passagens");
        this.frameTickets.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frameTickets.setContentPane(this.panelTickets);
        this.frameTickets.pack();
        this.frameTickets.setVisible(true);



        try {

            BufferedReader br = new BufferedReader(new FileReader(new File(".\\subtest.csv")));
            List<String[]> elements = new ArrayList<String[]>();
            String line = null;
            while((line = br.readLine())!=null) {
                String[] splitted = line.split(",");
                elements.add(splitted);
            }
            br.close();

            String[] columNames = new String[] {
                    "ID", "Production"
            };

            Object[][] content = new Object[elements.size()][2];

            for(int i=0; i<elements.size(); i++) {
                content[i][0] = elements.get(i)[0];
                content[i][1] = elements.get(i)[1];
            }

            tableTickets.setModel(new DefaultTableModel(content,columNames));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
