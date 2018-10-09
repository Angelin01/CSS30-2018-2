package RMIrino;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli {
    private InterfaceServ serverReference = null;

    public CliImpl(InterfaceServ serverReference) throws RemoteException {
        this.serverReference = serverReference;
    }

    @Override
    public void echo(String str) throws RemoteException {
        System.out.println(str);
    }
}
