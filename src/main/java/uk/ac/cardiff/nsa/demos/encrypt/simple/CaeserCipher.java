
package uk.ac.cardiff.nsa.demos.encrypt.simple;

import uk.ac.cardiff.nsa.demos.encrypt.Cipher;
import uk.ac.cardiff.nsa.demos.encrypt.DecryptingException;

public class CaeserCipher implements Cipher {

    /** The shift amount . */
    private final int shift;

    public CaeserCipher(final int shiftAmount) {
        shift = shiftAmount;
    }

    public String encrypt(final String message) {

        final StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            final char newChar = (char) (((message.charAt(i) + shift) - 97) % 26 + 97);
            System.out.println("Old Char [" + message.charAt(i) + "], shifted [" + newChar + "]");
            encrypted.append(newChar);
        }
        return encrypted.toString();
    }
    
    public String decrypt(final String message) throws DecryptingException {

        final StringBuilder encrypted = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            final char newChar = (char) (((message.charAt(i) - shift) + 26 - 97) % 26 + 97);
            System.out.println("Old Char [" + message.charAt(i) + "," + (int) message.charAt(i) + "], shifted ["
                    + newChar + "," + (int) newChar + "]");
            encrypted.append(newChar);
        }
        return encrypted.toString();
    }

}
