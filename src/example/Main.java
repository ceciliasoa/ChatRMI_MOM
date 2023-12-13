package example;

import project.ClientView;

public class Main {
    public static void main(String[] args) {
        Cliente cliente1 = new Cliente("Cliente1", "Contato1");
        Cliente cliente2 = new Cliente("Cliente2", "Contato2");

        cliente1.adicionarAmigo(cliente2.getNomeContato());

        MessageProducerExample produtor = new MessageProducerExample();
        produtor.enviarMensagemParaAmigo(cliente1, "teste", cliente2.getNomeContato());

        MessageConsumerExample consumidor = new MessageConsumerExample();
        Thread consumerThread = new Thread(() -> consumidor.startReceivingMessages(cliente2));
        consumerThread.start();

        // Aguarde um tempo para a recepção da mensagem
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Pare de receber mensagens de forma controlada
        consumidor.stopReceivingMessages();
        
//        ClientView client = new ClientView(null);
//        client.startClient();
    }
}
