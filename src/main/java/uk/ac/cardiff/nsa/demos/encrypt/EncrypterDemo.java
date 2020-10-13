
package uk.ac.cardiff.nsa.demos.encrypt;

import org.apache.commons.codec.binary.Base64;

import uk.ac.cardiff.nsa.demos.encrypt.aes.AESCipher;
import uk.ac.cardiff.nsa.demos.encrypt.simple.CaeserCipher;
import uk.ac.cardiff.nsa.demos.encrypt.simple.XORCipher;

public class EncrypterDemo {

    public static void main(final String args[]) throws DecryptingException {

         //doCaeserCipher();

         doXORCipher();

        //doAESCipher();
    }

    private static void doAESCipher() throws DecryptingException {

        final Cipher aesCipher = new AESCipher();
        aesCipher.encrypt("encrypt this");
    }

    private static void doCaeserCipher() throws DecryptingException {

        final Cipher caeserCipher = new CaeserCipher(6);

        final String encrypted = caeserCipher.encrypt("this is a cool input this");

        System.out.println("Encrypted [" + encrypted + "]");

        final String decrypted = caeserCipher.decrypt(encrypted);

        System.out.println("Decrypted [" + decrypted + "]");

    }

    private static void doXORCipher() throws DecryptingException {
        // create a simple key
        final byte[] key = new byte[] {Byte.parseByte("00111001", 2), Byte.parseByte("00000111", 2)};
        // create simple XOR cipher
        final Cipher xorCipher = new XORCipher(key);

        final String encrptyed = xorCipher.encrypt("Test Encryption");
        final String encrptyedTwo = xorCipher.encrypt("Test This");

        System.out.println("Encrypted [" + encrptyed + "], as ASCII ["
                + new String(Base64.decodeBase64(encrptyed.getBytes())) + "]");

        System.out.println("Encrypted [" + encrptyedTwo + "], as ASCII ["
                + new String(Base64.decodeBase64(encrptyedTwo.getBytes())) + "]");

        // now decrypt the same text.
        final String decrypted = xorCipher.decrypt(encrptyed);
        System.out.println("Decrypted [" + decrypted + "]");
    }

}
