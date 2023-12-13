package example;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;


public class MessageProducerExample {
    public void enviarMensagemParaAmigo(Cliente cliente, String mensagem, String nomeAmigo) {
        if (cliente.getAmigos().contains(nomeAmigo)) {
            try {
                ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
                Connection connection = connectionFactory.createConnection();
                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                MessageProducer producer = session.createProducer(session.createQueue(nomeAmigo));

                TextMessage message = session.createTextMessage(cliente.getNomeContato() + ": " + mensagem);
                producer.send(message);

                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Você não tem " + nomeAmigo + " como amigo.");
        }
    }
}
