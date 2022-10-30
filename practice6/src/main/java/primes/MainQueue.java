package primes;

public class MainQueue {
    public static void main(String[] args) {
        new Thread(new NumberConsumerQueue()).start();
        new Thread(new NumberConsumerQueue()).start();
        new Thread(new NumberConsumerQueue()).start();
        new Thread(new NumberPublisherQueue()).start();
    }
}
