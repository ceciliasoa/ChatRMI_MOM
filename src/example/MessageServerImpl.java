package example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class MessageServerImpl extends UnicastRemoteObject implements MessageServer {
    private List<String> messageList;

    protected MessageServerImpl() throws RemoteException {
        super();
        this.messageList = new ArrayList<>();
    }

    @Override
    public void storeMessage(String message) throws RemoteException {
        messageList.add(message);
        System.out.println("Mensagem armazenada: " + message);
    }

    @Override
    public String retrieveMessage() throws RemoteException {
        if (messageList.isEmpty()) {
            return "Nenhuma mensagem disponível.";
        } else {
            String message = messageList.remove(0);
            System.out.println("Mensagem recuperada: " + message);
            return message;
        }
    }
}
