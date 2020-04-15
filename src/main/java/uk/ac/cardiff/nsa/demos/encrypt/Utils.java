
package uk.ac.cardiff.nsa.demos.encrypt;

public class Utils {

    /**
     * Return a bit string representation of the byte argument.
     * 
     * @param b the byte to convert to a bit string.
     * @return the bit string representation.
     */
    public static String byteToBinary(final byte b) {

        return String.format("%8s", Integer.toBinaryString((b + 256) % 256)).replace(' ', '0');

    }

}
