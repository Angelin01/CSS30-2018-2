package RMIrino;

import javax.swing.*;
import java.rmi.RemoteException;

public class Main {
    public static void main (String[] args) throws RemoteException {
        /*final int PORT = 1337;
        Registry referenciaServicoNomes = LocateRegistry.getRegistry(PORT);

        InterfaceServ servidor = null;
        try {
            servidor = (InterfaceServ) referenciaServicoNomes.lookup("servico");
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        CliImpl client = new CliImpl(servidor);
        client.vaca("Batatas sao boas");*/

        // GUI

        JanelaCliente clientWindow = new JanelaCliente();
    }
}
