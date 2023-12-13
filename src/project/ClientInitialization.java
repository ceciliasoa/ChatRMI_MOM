package project;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

public class ClientInitialization {

    private static String clientName;

    public void start() {
        // Abre um diálogo para inserir o nome do cliente
        showInputDialog();

        // Após o diálogo, verifica se o nome do cliente foi fornecido
        if (clientName != null && !clientName.isEmpty()) {
            // Se o nome do cliente estiver disponível, exibe a ClientView
            new ClientInitialization().showClientView();
        }
    }

    private static void showInputDialog() {
        JFrame frame = new JFrame("Main Application");
        JDialog dialog = new JDialog(frame, "Enter Client Name", true);

        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter Client Name:");
        JTextField textField = new JTextField(20);
        JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtém o nome do cliente do campo de texto
                clientName = textField.getText();
                // Fecha o diálogo
                dialog.dispose();
            }
        });

        panel.add(label);
        panel.add(textField);
        panel.add(okButton);

        dialog.getContentPane().add(panel);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(frame);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private void showClientView() {
		try {
			ClientView client = new ClientView(clientName);
			client.startClient();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
