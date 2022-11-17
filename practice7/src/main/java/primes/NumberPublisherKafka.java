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
import java.util.concurrent.atomic.AtomicInteger;



public class NumberPublisherKafka {
    public static final String topicRequestName = "PRIME-CHECK-CANDIDATE";
    public static final String topicResponseName = "PRIME-CHECK-RESULT";
    public static final int REQUEST_QUEUE_MAX_LENGTH = 100;
    final SimpleRPM rpm;
    final AtomicInteger inProgress;

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
    public NumberPublisherKafka() {
        rpm = new SimpleRPM();
        inProgress = new AtomicInteger(0);
        new Thread(new Publisher()).start();
        new Thread(new Consumer()).start();
    }


    class Publisher implements Runnable {
        @Override
        public void run() {
            try {
                Properties properties = prepareProperties(user, password, broker);
                KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

                BigInteger candidatToStart = new BigInteger("1047291047291127");
                BigInteger candidat = candidatToStart;
                while (true) {
                    String message = candidat.toString();
                    producer.send(new ProducerRecord<String, String>(topicRequestName, message)).get(); //blocking
                    int currentQueueLength = inProgress.incrementAndGet();
                    System.out.println("currentQueuelength = " + currentQueueLength);
                    candidat = candidat.add(BigInteger.ONE);
                    while (inProgress.get() > REQUEST_QUEUE_MAX_LENGTH) {
                        //System.out.println("request queue size = "+inProgress.get()+", sleep...");
                        Thread.sleep(100);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Consumer implements Runnable{
        @Override
        public void run() {
            Properties properties = prepareProperties(user, password, broker);
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Arrays.asList(topicResponseName));
            while(true) {
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                if (!records.isEmpty()){
                    for(ConsumerRecord<String, String> record: records) {
                        inProgress.decrementAndGet();
                        rpm.addRequest();
                        System.out.println("requests per minute: " + rpm.getRPM());
                    }
                    consumer.commitSync();
                }
            }
        }
    }
}
