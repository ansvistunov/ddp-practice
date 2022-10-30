package primes;

import java.math.BigInteger;

public class NumberPublisherLocal {
    public static void main(String[] args) {

        BigInteger candidatToStart = new BigInteger("1047291047291127");
        BigInteger candidat = candidatToStart;
        SimpleRPM rpm = new SimpleRPM();
        while(true){
            candidat = candidat.add(BigInteger.ONE);
            long start = System.currentTimeMillis();
            System.out.println(BadPrimeCheck.primeCheck(candidat));
            long howLong = (System.currentTimeMillis() - start);
            System.out.println("milliseconds = "+howLong);
            rpm.addRequest();
            System.out.println("rpm.getRPM() = " + rpm.getRPM());
        }


        //long howLong = (System.currentTimeMillis() - start)/1000;
        //System.out.println("seconds="+howLong);
    }
}
