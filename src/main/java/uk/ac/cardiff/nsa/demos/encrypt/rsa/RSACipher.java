package uk.ac.cardiff.nsa.demos.encrypt.rsa;

import java.math.BigInteger;

import uk.ac.cardiff.nsa.demos.encrypt.Cipher;
import uk.ac.cardiff.nsa.demos.encrypt.DecryptingException;

public class RSACipher implements Cipher{
    
    final BigInteger p;
    
    final BigInteger q;
    
    public RSACipher(final BigInteger pIn, final BigInteger qIn) {
        p = pIn;
        q = qIn;
    }
    

    public String encrypt(String message) {
        // TODO Auto-generated method stub
        return null;
    }

    public String decrypt(String base64Message) throws DecryptingException {
        // TODO Auto-generated method stub
        return null;
    }

}
