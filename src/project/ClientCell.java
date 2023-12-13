package project;

import javax.jms.JMSException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class ClientCell extends JPanel implements SubscribeListener {
	private static final long serialVersionUID = 1L;

	private DefaultListModel<String> listModel;
	private JList<String> list;
	Boolean isQueue = false;
	private JTextField textField;
	private String contactName;
    public ClientCell(String clientName, String texto, CellListener cellListener) {
    	contactName = texto;
    	setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setPreferredSize(new Dimension(500, 219));
        setLayout(null);

        JLabel label = new JLabel(texto);
        label.setBounds(6, 6, 113, 25);
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, 20)); 

        add(label);
        
        JButton removeTopic = new JButton("excluir");
        removeTopic.setBounds(381, 8, 113, 29);
        removeTopic.addActionListener(new ActionListener() {
            @Override
            	public void actionPerformed(ActionEvent e) {
            		try {
            			if (isQueue) {
            				cellListener.removeConsumer(label.getText(), ClientCell.this);
            			} 
					} catch (JMSException e1) {
						e1.printStackTrace();
					}
                }
           });
        add(removeTopic);
               
        listModel = new DefaultListModel<>();
        list = new JList<>(listModel);
        list.setBounds(18, 43, 464, 132);
        list.setVisibleRowCount(2);
        add(list);
        
        textField = new JTextField();
        textField.setBounds(16, 187, 351, 29);
        add(textField);
        textField.setColumns(10);
        
        JButton btnNewButton = new JButton("Enviar");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					cellListener.sendMessage(label.getText(), textField.getText());
					listModel.addElement("[" + clientName + "]: " + textField.getText());

					textField.setText("");
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
        btnNewButton.setBounds(365, 187, 117, 29);
        add(btnNewButton);
       
    }
    
	@Override
	public void didReceiveMessage(String message) {
		
		listModel.addElement("[" + contactName + "]: " + message);
		
	}
}

