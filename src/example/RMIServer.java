package example;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class RMIServer {
    public static void main(String[] args) {
        try {
            MessageServer messageServer = new MessageServerImpl();

            // Inicie o registro RMI
            LocateRegistry.createRegistry(1099);

            // Associe o objeto remoto ao registro RMI
            Naming.rebind("MessageServer", messageServer);

            System.out.println("Servidor RMI pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

