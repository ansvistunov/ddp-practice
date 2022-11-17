package primes;

public class MainKafka {
    public static void main(String[] args) {
        new NumberPublisherKafka();
        new NumberConsumerKafka();
        new NumberConsumerKafka();
        new NumberConsumerKafka();
    }
}
