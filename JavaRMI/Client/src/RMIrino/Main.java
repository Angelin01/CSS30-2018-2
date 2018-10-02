package RMIrino;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main (String[] args) throws RemoteException {
        final int PORT = 1337;
        Registry referenciaServicoNomes = LocateRegistry.getRegistry(PORT);

        InterfaceServ servidor = null;
        try {
            servidor = (InterfaceServ) referenciaServicoNomes.lookup("servico");
        } catch (NotBoundException e) {
            e.printStackTrace();
        }
        CliImpl client = new CliImpl(servidor);

        // GUI

        JanelaCliente clientWindow = new JanelaCliente(servidor);
    }
}
