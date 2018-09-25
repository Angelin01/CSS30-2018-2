package RMIrino;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main (String[] args) throws RemoteException {
        final int PORT = 1337;
        Registry referenciaServicoNomes = LocateRegistry.createRegistry(PORT);
        ServImpl servico = new ServImpl();
        referenciaServicoNomes.rebind("servico", servico);
    }
}
