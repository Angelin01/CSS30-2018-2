package RMIrino;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class CliImpl extends UnicastRemoteObject implements InterfaceCli {
    private InterfaceServ serverReference = null;

    protected CliImpl(InterfaceServ serverReference) throws RemoteException {
        this.serverReference = serverReference;
    }

    void vaca(String str) {
        try {
            this.serverReference.call(str, this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void echo(String str) throws RemoteException {
        System.out.println(str);
    }
}
