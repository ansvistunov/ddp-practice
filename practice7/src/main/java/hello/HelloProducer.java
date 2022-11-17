package hello;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import java.util.Properties;

public class HelloProducer {
    static int count = 0;
    public static void main(String[] args) throws Exception{
        String username = args[0];
        String password = args[1];
        String brokers = args[2];
        String topicName = args[3];
        String jaasTemplate = "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";";
        String jaasCfg = String.format(jaasTemplate, username, password);
        String serializer = StringSerializer.class.getName();
        String deserializer = StringDeserializer.class.getName();

        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokers);
        properties.put("group.id", username + "-consumer");
        properties.put("enable.auto.commit", "true");
        properties.put("auto.commit.interval.ms", "1000");
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer", deserializer);
        properties.put("value.deserializer", deserializer);
        properties.put("key.serializer", serializer);
        properties.put("value.serializer", serializer);
        properties.put("security.protocol", "SASL_PLAINTEXT");
        properties.put("sasl.mechanism", "PLAIN");
        properties.put("sasl.jaas.config", jaasCfg);

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        while (true){
            //synchro writes
            //producer.send(new ProducerRecord<String, String>(topicName, "Hello world "+count)).get();
            producer.send(new ProducerRecord<String, String>(topicName, "Hello world "+count),
                    (recordMetadata, e) -> System.out.println(String.format("message send. offset:%s i:%s",recordMetadata.offset(),count))
            );
            //System.out.println(String.format("Send message #%s",i));
            Thread.sleep(1000); count++;
        }
    }
}
