/**
 * @author : Alex
 * @created : 24.10.2021, воскресенье
 **/

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.KafkaProducer;

public class Main {
    KafkaProducer<Integer, String> producer;
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("");
        Channel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();
        System.out.println("\nHello world!\n");
    }
}
