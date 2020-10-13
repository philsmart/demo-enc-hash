
package uk.ac.cardiff.nsa.demos;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;

public class HashUtils {

    public static void main(final String args[]) {
        System.out.println("Running..."+1.5819613093806715e+55%64);

        System.out.println(constructHash("Great") + ": Original");
        System.out.println(constructHash("Greet") + ": Very similar to original, so patterns exist");
        System.out.println(constructHash(
                "This is a bigger hashwdqedf \n efwrfgwrgferger ewfewfewknfjwekhfejwfh jwechwegfwehgfwerf"));

        System.out.println(constructCryptographicHash("Greet"));

         findCollision(constructHash("Great"));

    }

    /**
     * Generate random words of the right length and hope we find a collision.
     * 
     * @param hashToCrack hexadecimal hash.
     */
    public static void findCollision(final String hashToCrack) {

        int noCollisions = 0;
        for (long i = 0; i < 100000000; i++) {
            final String generatedString = RandomStringUtils.randomAlphanumeric(5);
            // System.out.println(generatedString);
            final String hexHash = constructHash(generatedString);
            if (hashToCrack.equals(hexHash)) {
                System.out.println("Found collision: " + generatedString + " hash: " + hexHash);
                noCollisions++;
            } else {
                // System.out.println("NOT collision: " + generatedString + " hash: " + hexHash);
            }

        }
        System.out.println("No of collisions: " + noCollisions);
    }

    /**
     * Generate a simple (non-cryptographic) 32bit (int) hash for a string. Taken from {@link String#hashCode()}.
     * <p>
     * {@literal 0xff} guarantees an unsigned byte value (80 bits for each byte) of 256 max value.
     * <p>
     * Observations.
     * <ol>
     * <li>The hash is unlikely to be reversible because it produces a fixed sized output (it is effectively mod 2^32).
     * And keeps multiplying the previous result - look this up to be sure.</li>
     * <li>Patterns exist in the output.</li>
     * <li>Does not have a very big state space 2^32 (as int is 32 bit in java)</li>
     * </ol>
     * 
     * 
     * @param message the message to hash, never {@literal null}.
     * @return a Hexadecimal representation of the hash.
     */
    @Nonnull
    public static String constructHash(@Nonnull final String message) {
        Objects.requireNonNull(message, "Input message can not be null");

        // 31 is efficient (can be done using bit-shifting)
        // 31 is prime, gives a more uniform output.
        final int factor = 31;
        int h = 0;
        for (final byte v : message.getBytes()) {
            //0xff is 255 as an int, or all 1's in a byte. Makes all ints positive
            h = factor * h + (v & 0xff);
        }
        return Integer.toHexString(h);

    }

    /**
     * Generate a sha256 non-cryptographic hash for a string.
     * 
     * @param message the message to hash, never {@literal null}.
     * @return a Hexadecimal representation of the hash.
     */
    @Nullable
    public static String constructCryptographicHash(@Nonnull final String message) {
        Objects.requireNonNull(message, "Input message can not be null");

        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] encodedhash = digest.digest(message.getBytes(StandardCharsets.UTF_8));
            return Hex.encodeHexString(encodedhash);

        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }

}
