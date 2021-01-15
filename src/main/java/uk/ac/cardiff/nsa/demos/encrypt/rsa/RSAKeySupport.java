
package uk.ac.cardiff.nsa.demos.encrypt.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSAKeySupport {

    public static BigInteger generatePrime() {
         for (long i=1000000000; i < 1445005645; i ++) {
             if (isPrime(i)) {
                 return BigInteger.valueOf(i);
             }
         }
         return BigInteger.valueOf(1);
    }

    // checks whether an int is prime or not.
    public static boolean isPrime(long n) {
        for (long i = 2; 2 * i < n; i++) {
            if (n % i == 0)
                return false;
        }
        return true;
    }
    
    public static boolean isPrime(BigInteger n) {
        for (BigInteger i = BigInteger.valueOf(2); i.compareTo(n) < 0;i = i.add(BigInteger.ONE)) {
            //System.out.println(i);
            if (n.mod(i).equals(BigInteger.ZERO))
                return false;
        }
        return true;
    }

    public static BigInteger generatePublicComponentN(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }
    
    public static void main(String args[]) {
        BigInteger primeOne = BigInteger.probablePrime(1024, new SecureRandom());
       System.out.println(primeOne.bitLength());
      
       System.out.println("Rnd int: "+new SecureRandom().nextLong());
       System.out.println("What bit length is this: "+BigInteger.valueOf(60).bitLength());
       System.out.println(RSAKeySupport.isPrime(primeOne));
        //System.out.println(RSAKeySupport.generatePrime());
    }

}
