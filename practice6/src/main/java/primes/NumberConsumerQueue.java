package primes;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.math.BigInteger;

public class NumberConsumerQueue implements Runnable{

    public static final String queueRequestName = "PRIME-CHECK-CANDIDATE";
    public static final String queueResponseName = "PRIME-CHECK-RESULT";
    public static final String url = "tcp://localhost:61616";
    private static int counter = 0;

    private final int id;

    public  NumberConsumerQueue(){
        this.id = counter;
        counter++;
    }

    @Override
    public void run() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            // Create a Connection
            Connection connection = connectionFactory.createConnection();

            //PRODUCER
            Session producerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = producerSession.createQueue(queueResponseName);
            MessageProducer producer = producerSession.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);

            //CONSUMER
            Session consumerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = consumerSession.createQueue(queueRequestName);
            MessageConsumer consumer = consumerSession.createConsumer(destination);

            connection.start();
            while (true){
                Message message = consumer.receive();
                TextMessage textMessage = (TextMessage)message;
                BigInteger numberToCheck = new BigInteger(textMessage.getText());
                boolean primeCheck = BadPrimeCheck.primeCheck(numberToCheck);
                TextMessage resultMessage = producerSession.createTextMessage(String.valueOf(primeCheck));
                producer.send(resultMessage);
                System.out.println("solver: "+id+" checked: "+numberToCheck+" result: "+primeCheck);
            }


        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
