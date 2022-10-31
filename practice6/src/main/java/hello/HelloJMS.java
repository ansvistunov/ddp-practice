package hello;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Scanner;

public class HelloJMS implements Runnable{
    public static final String url = "tcp://localhost:61616";
    public static String destinationName = "DEST";

    public static void main(String[] args) {
        new Thread(new HelloJMS()).start();
    }

    @Override
    public void run() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            // Create a Connection
            Connection connection = connectionFactory.createConnection();

            //PRODUCER
            Session producerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = producerSession.createQueue(destinationName);
            //Destination destination = producerSession.createTopic(destinationName);
            MessageProducer producer = producerSession.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            //CONSUMER
            Session consumerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageConsumer consumer = consumerSession.createConsumer(destination);
            consumer.setMessageListener(message -> {
                try {
                    TextMessage textMessage = (TextMessage)message;
                    System.out.println("recieved:"+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            });

            connection.start();

            Scanner scanner = new Scanner(System.in);
            String s;
            while ((s = scanner.nextLine()) != null ) {
                TextMessage resultMessage = producerSession.createTextMessage(s);
                producer.send(resultMessage);
                System.out.println("send:"+s);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
