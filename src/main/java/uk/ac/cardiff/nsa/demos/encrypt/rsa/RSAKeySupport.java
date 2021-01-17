
package uk.ac.cardiff.nsa.demos.encrypt.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RSAKeySupport {

    public static BigInteger generatePrime() {
         for (int i=1000000000; i < 1445005645; i ++) {
             if (isPrime(i)) {
                 return BigInteger.valueOf(i);
             }
         }
         return BigInteger.valueOf(1);
    }

    // checks whether an int is prime or not.
    public static boolean isPrime(int n) {
        long current = System.currentTimeMillis();
        if (n%2==0) {
            return false;
        }
        OptionalInt prime = IntStream.rangeClosed(3, n-1).parallel()
                .peek(p -> {if (p%100000000==0) System.out.println("checking:"+p);})
                .filter(p -> n%p==0)
                .peek(p -> System.out.println("not prime:"+p))
                .findAny();
 
        long end = System.currentTimeMillis();
        System.out.println("Divides by: "+prime+" time: "+(end-current)/1000);
        return !prime.isPresent();
    }
    
    public static boolean isPrime(BigInteger n) {
        long baseTime = System.currentTimeMillis();
        
        //devides by 2 is never prime.
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            return false;
        }
        
        final BigInteger maxTeast = n.sqrt();
        
        return Stream.iterate(BigInteger.valueOf(3), num -> num.add(BigInteger.TWO))
                .unordered()
                .parallel()
                .takeWhile(num -> maxTeast.compareTo(num) == 1)
                .peek(p->{if (p.mod(BigInteger.valueOf(1000001)).equals(BigInteger.ZERO)){ System.out.println("checking:"+p);}})
                .filter(p->n.mod(p).equals(BigInteger.ZERO)).findAny().isPresent();
        //bigIntStream.forEach(p ->{if (p.mod(BigInteger.valueOf(1000001)).equals(BigInteger.ZERO)){ System.out.println("checking:"+p);}});
        

        
//        for (BigInteger i = BigInteger.valueOf(2); i.compareTo(n) < 0;i = i.add(BigInteger.ONE)) {
//            if (i.mod(BigInteger.valueOf(1000000)).equals(BigInteger.ZERO)) {
//                System.out.println((System.currentTimeMillis()-baseTime)+": "+i);
//            } 
//            if (n.mod(i).equals(BigInteger.ZERO))
//                return false;
//        }

    }

    public static BigInteger generatePublicComponentN(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }
    
    public static void main(String args[]) {
        BigInteger primeOne = BigInteger.probablePrime(1024, new SecureRandom());
        
       // IntStream.rangeClosed(1, 150).parallel().forEach(i->System.out.println(i));
        
       // System.out.println("isPrime: "+RSAKeySupport.isPrime(2147483643));
       System.out.println(primeOne.bitLength());
      
       
       System.out.println("Rnd prime: "+primeOne);
       System.out.println("Rnd prime sqrt: "+primeOne.sqrt());
       System.out.println("What bit length is this: "+BigInteger.valueOf(60).bitLength());
       System.out.println(RSAKeySupport.isPrime(primeOne));
        //System.out.println(RSAKeySupport.generatePrime());
    }

}
