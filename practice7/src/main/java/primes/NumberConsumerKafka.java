package primes;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Properties;


public class NumberConsumerKafka implements Runnable{
    public static final String topicRequestName = "PRIME-CHECK-CANDIDATE";
    public static final String topicResponseName = "PRIME-CHECK-RESULT";
    private static int counter = 0;
    private final int id;

    public  NumberConsumerKafka(){
        this.id = counter;
        counter++;
        new Thread(this).start();
    }

    String user = "admin";
    String password = "admin-secret";
    String broker = "localhost:8080";

    static Properties prepareProperties(String username, String password, String brokers) {
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
        return properties;
    }

    @Override
    public void run() {
        Properties properties = prepareProperties(user, password, broker);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList(topicRequestName));

        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        while (true){

            ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
            if (!records.isEmpty()){
                for(ConsumerRecord<String, String> record: records) {
                    try {
                        // Display record and count
                        BigInteger numberToCheck = new BigInteger(record.value());
                        boolean primeCheck = BadPrimeCheck.primeCheck(numberToCheck);
                        producer.send(new ProducerRecord<String, String>(topicResponseName, String.valueOf(primeCheck))).get();//blocking
                        System.out.println("solver: " + id + " checked: " + numberToCheck + " result: " + primeCheck);
                    }catch(Exception e){e.printStackTrace();}
                }
                consumer.commitSync();
            }

        }
    }
}
