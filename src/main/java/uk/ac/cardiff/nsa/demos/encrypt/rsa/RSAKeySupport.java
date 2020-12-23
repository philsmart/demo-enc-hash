
package uk.ac.cardiff.nsa.demos.encrypt.rsa;

import java.math.BigInteger;

public class RSAKeySupport {

    public static BigInteger generatePrime() {
         
    }

    // checks whether an int is prime or not.
    public static boolean isPrime(int n) {
        for (int i = 2; 2 * i < n; i++) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    public static BigInteger generatePublicComponentN(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }

}
