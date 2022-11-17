package primes;

import java.math.BigInteger;

public class BadPrimeCheck {
    static boolean primeCheck(BigInteger num){
        boolean isNotPrime = false;
        for (BigInteger i = BigInteger.TWO; i.compareTo(num.sqrt()) < 0; i = i.add(BigInteger.ONE)){
            if (num.mod(i).compareTo(BigInteger.ZERO) == 0) isNotPrime = true;
        }
        return !isNotPrime;
    }
}
