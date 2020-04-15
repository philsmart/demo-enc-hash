
package uk.ac.cardiff.nsa.demos.encrypt;

/**
 * Base interface for an encryption implemention.
 */
public interface Cipher {

    /**
     * Produce Base64 encoded cipher text from the input message.
     * 
     * @param message the ASCII message to encrypt.
     * @return base64 encoded cipher text
     */
    String encrypt(String message);

    /**
     * Decrypt the input base64 encoded message and output it as ASCII text.
     * 
     * @param base64Message the encrypted message base64-encoded.
     * @return Decrypted text in ASCII format.
     */
    String decrypt(String base64Message) throws DecryptingException;

}
