
package uk.ac.cardiff.nsa.demos.encrypt.aes;

import uk.ac.cardiff.nsa.demos.encrypt.Cipher;
import uk.ac.cardiff.nsa.demos.encrypt.DecryptingException;

public class AESCipher implements Cipher {

    public String encrypt(final String message) {

        for (final byte b : message.getBytes()) {
            final byte sub = AESLookupTable.sboxMap(b);
        }

        return "";
    }

    public String decrypt(final String base64Message) throws DecryptingException {
        // TODO Auto-generated method stub
        return null;
    }

}
