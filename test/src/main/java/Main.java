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

public class Main {
    public static void main(String[] args) {
        System.out.println("\nHello world!\n");
    }
}
