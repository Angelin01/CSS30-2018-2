package RMIrino;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TicketsWindow {
    private JPanel panelTickets;
    private JTable tableTickets;
    private JFrame frameTickets;


    private Action buy = new AbstractAction()
    {
        public void actionPerformed(ActionEvent e)
        {
            int modelRow = Integer.valueOf( e.getActionCommand() );
            ((DefaultTableModel)tableTickets.getModel()).removeRow(modelRow);
        }
    };

    public TicketsWindow(){
        int columnNumber = 3;
        this.frameTickets = new JFrame("Janela Passagens");
        this.frameTickets.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frameTickets.setContentPane(this.panelTickets);
        this.frameTickets.pack();
        this.frameTickets.setVisible(true);

        // Read CSV file and show data in table
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("subtest.csv")));
            List<String[]> elements = new ArrayList<String[]>();
            String line = null;
            while((line = br.readLine())!=null) {
                String[] splitted = line.split(",");
                elements.add(splitted);
            }
            br.close();

            String[] columnNames = new String[] {
                    "Ticket ID", "Price (R$)", "Buy"
            };

            Object[][] content = new Object[elements.size()][columnNumber];

            for(int i=0; i<elements.size(); i++) {
                content[i][0] = elements.get(i)[0];
                content[i][1] = elements.get(i)[1];
                content[i][2] = "Buy";
            }


            tableTickets.setModel(new DefaultTableModel(content,columnNames));

            ButtonColumn buyButtonColumn = new ButtonColumn(tableTickets, this.buy, 2);
            buyButtonColumn.setMnemonic(KeyEvent.VK_D);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
