package project;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.jms.JMSException;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.awt.Font;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import java.awt.Color;


public class ClientView implements CellListener {
	private ClientController controller;
	private JFrame frame;
	private JPanel queueSpace;
	
    public Map<String, ClientCell> myTopics = new HashMap<>();
    public Map<String, ClientCell> myQueues = new HashMap<>();
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private JPanel queuePanel;
    private JScrollPane scrollQueue;
    private JButton addNewChat;
    private JLabel lblMinhasFilas;
    private JLabel clientName;
    private String userName;


	/**
	 * @wbp.parser.entryPoint
	 */
	public void startClient() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientView(String userName) throws RemoteException {
		this.userName = userName;
		controller = new ClientController();
		controller.start(userName);
   
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 587, 582);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		queuePanel = new JPanel();
		queuePanel.setBounds(0, 0, 554, 497);
		frame.getContentPane().add(queuePanel);
		queuePanel.setLayout(null);
		
		scrollQueue = new JScrollPane();
		scrollQueue.setBounds(21, 59, 511, 391);
		scrollQueue.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollQueue.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		queuePanel.add(scrollQueue);
		
		queueSpace = new JPanel();
		queueSpace.setLayout(new BoxLayout(queueSpace, BoxLayout.Y_AXIS));
		scrollQueue.setViewportView(queueSpace);
		
		addNewChat = new JButton("add contato");
		addNewChat.setBounds(21, 462, 183, 29);
		addNewChat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	showNewContactDialog();
            }
        });
		queuePanel.add(addNewChat);
		
		lblMinhasFilas = new JLabel("Contatos");
		lblMinhasFilas.setBounds(37, 23, 97, 25);
		lblMinhasFilas.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		queuePanel.add(lblMinhasFilas);
		
		JToggleButton statusToggleButton = new JToggleButton("Online");
		statusToggleButton.setSelected(false);
		
		statusToggleButton.setBackground(new Color(148, 255, 144));
		statusToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (statusToggleButton.isSelected()) {
					statusToggleButton.setText("offline");
					try {
						controller.disconnect();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
                } else {
                	statusToggleButton.setText("Online");
                	try {
						controller.connect();
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
                	
                }

			}
		});
		statusToggleButton.setBounds(399, 25, 85, 29);
		queuePanel.add(statusToggleButton);
		
		JLabel lblNewLabel_1 = new JLabel("status");
		lblNewLabel_1.setBounds(336, 31, 61, 16);
		queuePanel.add(lblNewLabel_1);
		
		clientName = new JLabel(controller.clientName);
		clientName.setHorizontalAlignment(SwingConstants.CENTER);
		clientName.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		clientName.setBounds(117, 6, 280, 25);
		queuePanel.add(clientName);
	}
	
	private void showNewContactDialog() {
		String inputName = JOptionPane.showInputDialog(
				frame,
		        "Digite o nome do contato:",
		        "Novo contato",
		         JOptionPane.QUESTION_MESSAGE);
	
		     if (inputName != null && !inputName.isEmpty()) {
		        try {
		            addConsumerQueueCard(inputName);
		    	} catch (JMSException e) {
		     	e.printStackTrace();
			}
		}
	}
	
	 private void addConsumerQueueCard(String queueName) throws JMSException {
		 ClientCell cell = new ClientCell(userName, queueName, this);
		 cell.isQueue = true;
		 getPendingMessages(cell, queueName);
		 
		 myQueues.put(queueName, cell);

	     queueSpace.add(cell);
	     startPeriodicCheckNewMessagesTask(queueName);
	     frame.revalidate();
	     frame.repaint();
	 }
	 
	 private void getPendingMessages(ClientCell cell, String queueName) throws JMSException {
			List<String> pendingMessages = controller.getMessages(queueName);
			

			pendingMessages.forEach(message -> {
				System.out.println("Valor na view: " + message);
				cell.didReceiveMessage(message);
				
			});
	 }

	@Override
	public void removeConsumer(String queueName, JPanel cell) throws JMSException {
		 System.out.println("Cartão excluído: " + queueName);
	     queueSpace.remove(cell);

	     frame.revalidate();
	     frame.repaint();	
		
	}

	@Override
	public void updateQueueMessage(String queueName, String message) {
		 ClientCell clientCell = myQueues.get(queueName);
		 System.out.println(message);
		 if (clientCell != null) {
	           clientCell.didReceiveMessage(message);
		 }
		
	}
	
	public void startPeriodicCheckNewMessagesTask(String queueName) {
	    executor.scheduleAtFixedRate(() -> {
	        try {
	            checkNewMessagesInBackground(queueName);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }, 0, 5, TimeUnit.SECONDS);
	}

	private void checkNewMessagesInBackground(String queueName) {
	    SwingUtilities.invokeLater(() -> {
	        try {
	            List<String> newMessages = controller.getMessages(queueName);
	            for (String message : newMessages) {
	            	updateQueueMessage(queueName, message);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    });
	}

	@Override
	public void sendMessage(String receiver, String message) throws RemoteException {
		if (controller.isOnline) {
			controller.sendMessage(receiver, message);		
		} else {
			controller.saveOfflineMessage(receiver, message);
		}
	}
}

