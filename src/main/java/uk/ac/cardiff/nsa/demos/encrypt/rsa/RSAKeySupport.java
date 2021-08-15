
package uk.ac.cardiff.nsa.demos.encrypt.rsa;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.annotation.concurrent.GuardedBy;

public class RSAKeySupport {
    


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
    
    /**
     * Check if the number input is prime by brute-force, but ignoring 2 and multiples of 2, and stopping
     * at sqrt(n) as factors (decomposition) after that they repeat (in reverse) e.g. 3*5 == 5*3.  
     * 
     * <p> probable prime with some additional assurances is good enough for RSA and openssl e.g.
     * use the Miller-Rabin test instead of this.</p>
     * 
     * @param n
     * @return
     */
    //TODO does it work e.g. 608771 , 729109?
    public static boolean isPrime(BigInteger n) {
        long baseTime = System.currentTimeMillis();
        
        //devides by 2 is never prime.
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            System.out.println("Divides by 2");
            return false;
        }
        
        final BigInteger maxTeast = n.sqrt();
        
        Optional<BigInteger> exactDivisor = Stream.iterate(BigInteger.valueOf(3), num -> num.add(BigInteger.TWO))
               // .unordered()
                .parallel()
                .takeWhile(num -> maxTeast.compareTo(num) == 1)
                .peek(p->{if (p.mod(BigInteger.valueOf(1)).equals(BigInteger.ZERO)){ System.out.println(p+" checking:"+p.bitLength());}})
                .filter(p->n.mod(p).equals(BigInteger.ZERO)).findAny();
        if (exactDivisor.isPresent()) {
            System.out.println("Divisor: "+exactDivisor.get());
        }
        return exactDivisor.isEmpty();

    }
    
    public static BigInteger[] findFactors(final BigInteger n) {
        
        BigInteger p = null;
        BigInteger r = null;
        
        //inclusive upper bound
        final BigInteger max = n.sqrt();
        
        for (BigInteger bi = BigInteger.valueOf(3);
                max.compareTo(bi) == 1;
                bi = bi.add(BigInteger.ONE)) {
            
            if (n.mod(bi).equals(BigInteger.ZERO)) {
                
                final BigInteger otherFactor = n.divide(bi);
                // This only works because we know RSA pub keys are a composite of only two primes.
                // Otherwise some numbers will have more than one prime number factors.
                // we could check the other factor is prime by seeing if it its least factor of a 
                // pair is greater than the cube root of the number, then the greater factor is prime.
                // https://trans4mind.com/personal_development/mathematics/numberTheory/divisibilityBruteForce.htm
                //System.out.println("Prime Factors "+bi+":"+otherFactor);
                p = bi;
                r = otherFactor;
                break;
            }
        }
        
        
        return new BigInteger[] {p,r};
    }

    /**
     * Generate a public number from the multiplcation of two secret primes p and q. 
     * finding the two primes that made this number (prime number factorisation) is hard if these
     * two numbers (p and q) are large - so less chance of finding the secret components. 
     * 
     * RSA uses probable primes - but very very likely! 
     * 
     * @param p
     * @param q
     * @return
     */
    public static BigInteger generatePublicComponentN(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }
    
    /**
     * Generate a random number of the given size in bits.
     * You need a 1 in the lengthBits position. So get the min and max 
     * numbers based on 2^lengthBits-1 and 2^lengthBits (exclusive), then 
     * generate a number between that range and add to the base (the min) to 
     * get a number which requires that many bits.
     * 
     * @param lengthBits
     * @return
     */
    //TODO stuck past 31 bits?
    public static BigInteger generateRandomNumber(int lengthBits) {
        int max= (int) (Math.pow(2,lengthBits)-1); //
        int min=(int) Math.pow(2,lengthBits-1);
        System.out.println("Max:"+max+" min:"+min);
        int range = max - min + 1;
        int rnd = (int) (Math.random() * range) + min;
        System.out.println("rnd: "+rnd);
        System.out.println("Rnd bits: "+BigInteger.valueOf(rnd).bitLength());
        return BigInteger.valueOf(rnd);
    }
    
   /**
    * At maxium, supports 24 bit primes. So 48 bit RSA. If we go bigger, we would
    * need faster ways to generate the primes, and faster methods e.g. probablistic
    * to check if they are prime.
    * 
    * 
    * @param args
    */
    public static void main(String args[]) {
        
        //check check things work
        BigInteger primeOne = BigInteger.probablePrime(24, new SecureRandom());
        BigInteger primeTwo = BigInteger.probablePrime(24, new SecureRandom());
        System.out.println("First Bin: "+primeOne.toString(2)+" Decimal: "+primeOne+" length: "+primeOne.bitLength());
        System.out.println("Second Bin: "+primeTwo.toString(2)+" Decimal: "+primeTwo+" length: "+primeTwo.bitLength());
     // The public key is a composite number e.g. two primes multiplied together.
        BigInteger compositeN = primeOne.multiply(primeTwo);        
        System.out.println("Composite Bin: "+compositeN.toString(2)+" Decimal: "+compositeN+" length: "+compositeN.bitLength());

//        final boolean isPrime = RSAKeySupport.isPrime(primeOne);
//        assert isPrime == true;
//        System.out.println("Is Prime: "+isPrime);
//        System.out.println("------------------");
//        
//        System.out.println("Should not be prime (divides 257), is prime = "+isPrime(BigInteger.valueOf(608771)));
        
        //CHECK OVER
        
        final BigInteger[] factors = findFactors(compositeN);
        System.out.println("Prime Factors "+factors[0]+":"+factors[1]);
        
        
        
        //now do it ourselves. Get random number of certain size, and check it
        //if not prime, repeat until you find one. assume 48bit RSA, so 2 24bit primes.
        
//        boolean foundPrime = false;
//        BigInteger p = null;
//        while(!foundPrime) {
//            p = generateRandomNumber(20);
//            if (isPrime(p)) {
//                foundPrime=true;
//            }            
//        }
//        System.out.println("Found prime P: "+p);
        
        

      
    }

}
