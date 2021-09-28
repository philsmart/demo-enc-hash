
package uk.ac.cardiff.nsa.demos.encrypt.rsa;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RSAKeySupport {
    
    /** the prime number e, nearly always this value. Must be less then carmicheal */
    //public static final BigInteger e = BigInteger.valueOf(65537);
    public static final int e = 11;

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
        
        //divides by 2 is never prime.
        if (n.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            System.out.println("Divides by 2");
            return false;
        }
        
        final BigInteger maxTeast = n.sqrt();
        
        Optional<BigInteger> exactDivisor = Stream.iterate(BigInteger.valueOf(3), num -> num.add(BigInteger.TWO))
               // .unordered()
                .parallel()
                .takeWhile(num -> maxTeast.compareTo(num) == 1)
                //.peek(p->{if (p.mod(BigInteger.valueOf(1)).equals(BigInteger.ZERO)){ System.out.println(p+" checking:"+p.bitLength());}})
                .filter(p->n.mod(p).equals(BigInteger.ZERO)).findAny();
        if (exactDivisor.isPresent()) {
            System.out.println("Divisor: "+exactDivisor.get());
        }
        return exactDivisor.isEmpty();

    }
    
    public static BigInteger getPrimeNumber(int length) {
        boolean foundPrime = false;
        BigInteger p = null;
        while(!foundPrime) {
            p = generateRandomNumber(length);
            if (isPrime(p)) {
                foundPrime=true;
            }            
        }
        return p;
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
                // Otherwise some numbers will have more than one prime number factor.
                // we could check the other factor is prime by seeing if its least factor of a 
                // pair is greater than the cube root of the number, then the greater factor is prime.
                // https://trans4mind.com/personal_development/mathematics/numberTheory/divisibilityBruteForce.htm
                // In other words, we should check these factors are themselves prime and we do not. If not
                // we need to break the factors down further to their primes.
                //System.out.println("Prime Factors "+bi+":"+otherFactor);
                p = bi;
                r = otherFactor;
                break;
            }
        }
        return new BigInteger[] {p,r};
    }

    /**
     * Generate a public number (a composite number) from the multiplcation of two secret primes p and q. 
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
     * A very slow alogrithm for finding the Carmichael's Totient Lowest Common Multiple.
     * 
     * @param p the first prime factor
     * @param q the second prime factor
     * @return the lowest common multiple - the private key.
     */
    public static BigInteger computeCarmichaelsTotientFromPublicFactors(final BigInteger p, final BigInteger q) {
        
        final BigInteger pMinus = p.subtract(BigInteger.valueOf(1));
        final BigInteger qMinus = q.subtract(BigInteger.valueOf(1));
        
        System.out.println("P-1: "+pMinus+" Q-1: "+qMinus);
        
        // find the smallest positive integer that is perfectly divisible by both pMinus and qMinus.
        // this gives part of the private key computation
        final BigInteger higher = pMinus.max(qMinus);
        final BigInteger lower = pMinus.min(qMinus);
        System.out.println("Higher:"+higher+" Lower:"+lower);
        
        BigInteger lcm = higher;
        while (!lcm.mod(lower).equals(BigInteger.ZERO)) {
            lcm = lcm.add(higher);
            //System.out.println(lcm);
        }
        System.out.println("carmichaelsTotient (Part of Private Key - slow): "+lcm);
        
        return lcm;
        
    }
    
    /**
     * A much faster alogrithm for finding the Carmichaels Totient from the Lowest Common Multiple using methods on the
     * BigInteger class.
     * 
     * @param p the first prime factor
     * @param q the second prime factor
     * @return the lowest common multiple - the private key.
     */
    public static BigInteger computeCarmichaelsTotientFromPublicFactorsFast(final BigInteger p, final BigInteger q) {
        
        final BigInteger pMinus = p.subtract(BigInteger.valueOf(1));
        final BigInteger qMinus = q.subtract(BigInteger.valueOf(1));
        
        BigInteger gcd = pMinus.gcd(qMinus);
        BigInteger absProduct = pMinus.multiply(qMinus).abs();
        BigInteger lcm = absProduct.divide(gcd);
        System.out.println("carmichaelsTotient (Part Of Private Key - fast):"+lcm);
        return lcm;
    }
    
    private static int computePrivateKey(final BigInteger carmichaelsTotient) {
        // compute the modular inverse of e and the carmichaelsTotient
        return BigInteger.valueOf(e).modInverse(carmichaelsTotient).intValue();
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
        
        //generate primes
//        BigInteger primeOne = BigInteger.probablePrime(24, new SecureRandom());
//        BigInteger primeTwo = BigInteger.probablePrime(24, new SecureRandom());
        
        final BigInteger primeOne = getPrimeNumber(10);
        final BigInteger primeTwo = getPrimeNumber(10);
       
        
        System.out.println("First Bin: "+primeOne.toString(2)+" Decimal: "+primeOne+" length: "+primeOne.bitLength());
        System.out.println("Second Bin: "+primeTwo.toString(2)+" Decimal: "+primeTwo+" length: "+primeTwo.bitLength());
     // The public key is a composite number e.g. two primes multiplied together.
        BigInteger compositeN = generatePublicComponentN(primeOne,primeTwo);        
        System.out.println("Composite Bin (Part of Public Key): "+compositeN.toString(2)+" Decimal: "+compositeN+" length: "+compositeN.bitLength());

        final boolean isPrime = isPrime(primeOne);
//        assert isPrime == true;
        System.out.println("Is Prime: "+isPrime);
//        System.out.println("------------------");
//        
//        System.out.println("Should not be prime (divides 257), is prime = "+isPrime(BigInteger.valueOf(608771)));
        
        //CHECK OVER
        
        final BigInteger[] factors = findFactors(compositeN);
        System.out.println("Prime Factors "+factors[0]+":"+factors[1]);
        
        //the Carmichael’s Totient which is part of the private key
        final BigInteger carmichaelsTotient = computeCarmichaelsTotientFromPublicFactors(primeOne,primeTwo);
        
        //this will be an int? otherwise wow. 
        final int privateKey = computePrivateKey(carmichaelsTotient);
        
        System.out.println("PrivateKey: "+privateKey);
        
        int messageAsInt = ByteBuffer.wrap("mess".getBytes(Charset.forName("UTF-8"))).getInt();
        //now compute encrypted message
        BigInteger message = BigInteger.valueOf(183536);
        System.out.println("MESSAGE: "+message);
        BigInteger cipherText = message.pow(e).mod(compositeN);
        System.out.println("CIPHER TEXT: "+cipherText);
        
        BigInteger messageFromCipher = cipherText.pow(privateKey).mod(compositeN);
        System.out.println("DECRYPTED TEXT: "+messageFromCipher);
        
        //No padding used to prevent padding attacks.
        
      
    }

}
