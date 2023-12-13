package project;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientController implements RMIClientInterface {
	
	private static String SERVER_NAME = "ChatServer";

	private Registry registry = null;
	private Remote remoteObject = null;
	private RMIServerInterface server = null;
	String clientName = null;
	boolean isOnline = false;
	private Map<String, List<String>> arrivedMessages = new HashMap<>();
	private Map<String, List<String>> sentMessages = new HashMap<>();	
	public ClientController() {
        try {
			remoteObject = UnicastRemoteObject.exportObject(this, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public void start(String userName) throws RemoteException {
		this.clientName = userName;
		connect();
		
	}

	public void connect() throws RemoteException {
		System.out.println("Valor encontrado: " + 1);
		try {
			registry = LocateRegistry.getRegistry();
			System.out.println("Valor encontrado: " + 12);

			if (clientName != null) {
			    registry.rebind(clientName, remoteObject);
			} else {
			    System.out.println("clientName é nulo!");
			}
			System.out.println("Valor encontrado: " + 13);

			server = (RMIServerInterface) registry.lookup(SERVER_NAME);
			System.out.println("Valor encontrado: " + 14);

			server.connect(clientName);
			System.out.println("Valor encontrado: " + 15);

			this.isOnline = true;
			sentWaitingMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void disconnect() throws RemoteException {
		try {
			String userName = this.clientName;

			this.isOnline = false;
			registry.unbind(userName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String receiver, String message) throws RemoteException {
		if (!isOnline || clientName == null || clientName.equals("") || receiver == null || receiver.equals("")
				|| message == null || message.equals("")) {
			return;
		}
		server.sendMessage(clientName, receiver, message);
		
	}

	@Override
	public void deliveryMessage(String sender, String message) throws RemoteException {
		if (arrivedMessages == null) {
			arrivedMessages = new HashMap<>();
		}
		try {
			arrivedMessages.computeIfAbsent(sender, k -> new ArrayList<>()).add(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	 public List<String> getMessages(String sender) {
		 List<String> messages = arrivedMessages.getOrDefault(sender, new ArrayList<>());
	     arrivedMessages.remove(sender); // Remove as mensagens consumidas
	     return messages;
	  }
	 
	 public void saveOfflineMessage(String receiver, String message) {
		 sentMessages.computeIfAbsent(receiver, k -> new ArrayList<>()).add(message);
	 }
	 
	 private void sentWaitingMessages() {
		 if (sentMessages == null) {
				return;
			}
		 sentMessages.forEach((receiver, messages) -> {
	            messages.forEach(message -> {
					try {
						sendMessage(receiver, message);
						
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				});
	            messages.clear(); 
	        });
	 }

}
