package primes;

import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.IntStream;

public class SimpleRPM {
    final int[] requests;

    public  SimpleRPM(){
        requests = new int[60];
        TimerTask task = new MyTimerTask(this);
        Timer timer = new Timer("ClearRequestsTimer");
        long period = 1000L;
        timer.schedule(task, 0, period);
    }

    public synchronized void addRequest(){
        int currentSecondInMinute = (int)((System.currentTimeMillis()/1000) % 60);
        requests[currentSecondInMinute]++;
    }
    private synchronized void clearRequestsMinuteAgo(){
        int currentSecondInMinute = (int)((System.currentTimeMillis()/1000) % 60);
        int secondMinuteAgo = (currentSecondInMinute + 1) %60;
        requests[secondMinuteAgo] = 0;
    }
    public synchronized int getRPM(){
        return IntStream.of(requests).sum();
    }


    private class MyTimerTask extends TimerTask {
        private final SimpleRPM rpm;

        protected MyTimerTask(SimpleRPM rpm){
            this.rpm = rpm;
        }
        @Override
        public void run() {
            rpm.clearRequestsMinuteAgo();
        }
    }
}
