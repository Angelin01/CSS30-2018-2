package RMIrino;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

public class ServImpl extends UnicastRemoteObject implements InterfaceServ {
    protected ServImpl() throws RemoteException {}

    @Override
    public void call(String str, InterfaceCli clientReference) throws RemoteException {
        clientReference.echo(str);
    }
}
