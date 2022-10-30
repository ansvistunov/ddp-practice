package primes;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

public class NumberPublisherQueue implements Runnable{
    public static final String queueRequestName = "PRIME-CHECK-CANDIDATE";
    public static final String queueResponseName = "PRIME-CHECK-RESULT";
    public static final String url = "tcp://localhost:61616";
    public static final int REQUEST_QUEUE_MAX_LENGTH = 100;
    final SimpleRPM rpm;
    final AtomicInteger inProgress;

    /*public static void main(String[] args) {
        new Thread(new NumberPublisherQueue()).start();
    }*/

    class ResposeListener implements MessageListener{

        @Override
        public void onMessage(Message message) {
            try {
                TextMessage textMessage = (TextMessage) message;
                String result = textMessage.getText();
                //System.out.println("result = "+result);
                inProgress.decrementAndGet();
                rpm.addRequest();
                System.out.println("requests per minute: " + rpm.getRPM());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public NumberPublisherQueue(){
        rpm = new SimpleRPM();
        inProgress = new AtomicInteger(0);
    }

    @Override
    public void run() {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            //PRODUCER
            Session producerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = producerSession.createQueue(queueRequestName);
            MessageProducer producer = producerSession.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //CONSUMER
            Session consumerSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = consumerSession.createQueue(queueResponseName);
            MessageConsumer consumer = consumerSession.createConsumer(destination);
            ResposeListener listener = new ResposeListener();
            consumer.setMessageListener(listener);

            connection.start();
            BigInteger candidatToStart = new BigInteger("1047291047291127");
            BigInteger candidat = candidatToStart;
            while(true) {
                TextMessage textMessage = producerSession.createTextMessage(candidat.toString());
                producer.send(textMessage);
                int currentQueueLength = inProgress.incrementAndGet();
                System.out.println("currentQueuelength = " + currentQueueLength);
                candidat = candidat.add(BigInteger.ONE);
                while(inProgress.get() > REQUEST_QUEUE_MAX_LENGTH){
                    //System.out.println("request queue size = "+inProgress.get()+", sleep...");
                    Thread.sleep(100);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
