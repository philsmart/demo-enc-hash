
package uk.ac.cardiff.nsa.demos.encrypt.simple;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Objects;

import javax.annotation.Nonnull;

import uk.ac.cardiff.nsa.demos.encrypt.Cipher;
import uk.ac.cardiff.nsa.demos.encrypt.DecryptingException;
import uk.ac.cardiff.nsa.demos.encrypt.Utils;

public class XORCipher implements Cipher {

    /** The XOR key. */
    @Nonnull private final byte[] key;

    /**
     * Create an XOR Cipher with the given key. Constructor.
     *
     * @param theKey the key
     */
    public XORCipher(@Nonnull final byte[] theKey) {
        key = Objects.requireNonNull(theKey);

    }

    public String encrypt(final String message) {

        final byte[] messageInBytes = message.getBytes();

        final byte[] paddedMessage = paddToKeySize(messageInBytes);

        final byte[] encrypted = new byte[paddedMessage.length];

        for (int i = 0; i < paddedMessage.length; i = i + key.length) {
            for (int j = 0; j < key.length; j++) {
                encrypted[i + j] = (byte) (paddedMessage[i + j] ^ key[j]);

                 System.out.println("---------------------");
                 System.out.println(Utils.byteToBinary(key[j]));
                 System.out.println(Utils.byteToBinary(paddedMessage[i + j]));
                 System.out.println("=");
                System.out.println(Utils.byteToBinary(encrypted[i + j]));

            }
        }

        return Base64.getEncoder().encodeToString(encrypted);

    }

    /**
     * Pad the input message so it is a multiple of the key length in bytes (our key is the block length in this case).
     * <p>
     * PKCS5 Padding is a common version of this for real world code block ciphers.
     * 
     * @param messageInBytes the message to pad.
     * @return a padded version of the <code>messageInBytes</code> argument.
     */
    private byte[] paddToKeySize(@Nonnull final byte[] messageInBytes) {
        // if the same size, no padding required.
        if (messageInBytes.length == key.length) {
            return messageInBytes;
        }
        // find the reminder to pad.
        final int bytesToAdd = messageInBytes.length % key.length;

        final byte[] paddedMessage = new byte[messageInBytes.length + bytesToAdd];
        for (int i = 0; i < paddedMessage.length; i++) {
            if (i < messageInBytes.length) {
                paddedMessage[i] = messageInBytes[i];
            } else {
                // add a simple byte
                paddedMessage[i] = Byte.parseByte("01111111", 2);
            }
        }
        return paddedMessage;
    }

    public String decrypt(final String base64Message) throws DecryptingException {

        final byte[] base64Decoded = Base64.getDecoder().decode(base64Message);

        if (base64Decoded.length % key.length != 0) {
            throw new DecryptingException(
                    "Message is not compatible with the key - could not have been produced by this key");

        }

        final byte[] decrypted = new byte[base64Decoded.length];

        for (int i = 0; i < base64Decoded.length; i = i + key.length) {
            for (int j = 0; j < key.length; j++) {
                decrypted[i + j] = (byte) (base64Decoded[i + j] ^ key[j]);

                // System.out.println("---------------------");
                // System.out.println(Utils.byteToBinary(key[j]));
                // System.out.println(Utils.byteToBinary(base64Decoded[i + j]));
                // System.out.println("=");
                // System.out.println(Utils.byteToBinary(decrypted[i + j]));

            }
        }
        try {
            return new String(decrypted, "US-ASCII");
        } catch (final UnsupportedEncodingException e) {
            throw new DecryptingException("Decrypted message can not be converted to ASCII");

        }

    }

}
