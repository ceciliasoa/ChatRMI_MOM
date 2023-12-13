package example;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageServer extends Remote {
    void storeMessage(String message) throws RemoteException;
    String retrieveMessage() throws RemoteException;
}
