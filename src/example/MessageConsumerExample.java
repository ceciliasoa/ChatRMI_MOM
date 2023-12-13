package example;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class MessageConsumerExample {
    private volatile boolean receivingMessages = true;
    private Connection connection;

    public void startReceivingMessages(Cliente cliente) {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = session.createConsumer(session.createQueue(cliente.getNomeContato()));

            while (receivingMessages) {
                Message message = consumer.receive(1000); // Adicionado um tempo de espera de 1 segundo

                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    if (textMessage != null) {
                        System.out.println("Mensagem recebida por " + cliente.getNomeContato() + ": " + textMessage.getText());
                    }
                }
            }

            consumer.close();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopReceivingMessages() {
        receivingMessages = false;

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
