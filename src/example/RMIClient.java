package example;

import java.rmi.Naming;

public class RMIClient {
    public static void main(String[] args) {
        try {
            // Obtenha a referência remota do servidor RMI
            MessageServer messageServer = (MessageServer) Naming.lookup("rmi://localhost/MessageServer");

            // Envie uma mensagem para o servidor RMI
            messageServer.storeMessage("Olá do Cliente RMI!");

            // Recupere uma mensagem do servidor RMI
            String retrievedMessage = messageServer.retrieveMessage();
            System.out.println("Mensagem recuperada do servidor RMI: " + retrievedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

