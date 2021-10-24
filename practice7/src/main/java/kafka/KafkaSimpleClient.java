package kafka;

import car.CarServer;
import car.command.Command;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;

import static car.BasicCarServer.maxCarSecondsDonNotMove;

/**
 * @author : Alex
 * @created : 02.05.2021, воскресенье
 **/
public class KafkaSimpleClient implements Runnable{

    final KafkaProducer<Integer, String> producer;
    final KafkaConsumer<Integer, String> consumer;

    static final String user = "admin";
    static final String password = "admin-secret";
    static final String broker = "132.145.228.39:8080";

    private Properties prepareProperties(String username, String password, String brokers) {
        String jaasTemplate = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);
        String valueSerializer = StringSerializer.class.getName();
        String valueDeserializer = StringDeserializer.class.getName();

        String keySerializer = IntegerSerializer.class.getName();
        String keyDeserializer = IntegerDeserializer.class.getName();


        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokers);
        properties.put("group.id", username + "-consumer");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", keyDeserializer);
        properties.put("value.deserializer", valueDeserializer);
        properties.put("key.serializer", keySerializer);
        properties.put("value.serializer", valueSerializer);
        properties.put("security.protocol", "SASL_PLAINTEXT");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("sasl.jaas.config", jaasCfg);
        return properties;
    }

    public KafkaSimpleClient(){
        Properties properties = prepareProperties(user, password, broker);
        producer = new KafkaProducer<>(properties);
        consumer = new KafkaConsumer<>(properties);

    }

    public String sendCommand(int carIndex, String command){
        return "";
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(new KafkaSimpleClient()).start();
    }

    @Override
    public void run() {

    }
}
