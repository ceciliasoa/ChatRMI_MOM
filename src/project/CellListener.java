package project;

import java.rmi.RemoteException;

import javax.jms.JMSException;
import javax.swing.JPanel;

public interface CellListener {
	void removeConsumer(String queueName, JPanel cell) throws JMSException ;
	void updateQueueMessage(String queueName,String message);
	void sendMessage(String receiver, String message) throws RemoteException ;
}

