
package uk.ac.cardiff.nsa.demos.encrypt;

public class DecryptingException extends Exception {

    /**
     * serial UID.
     */
    private static final long serialVersionUID = -7374908839279539044L;

    /**
     * Constructor.
     *
     */
    public DecryptingException() {
        super();

    }

    /**
     * Constructor.
     *
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public DecryptingException(final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);

    }

    /**
     * Constructor.
     *
     * @param message
     * @param cause
     */
    public DecryptingException(final String message, final Throwable cause) {
        super(message, cause);

    }

    /**
     * Constructor.
     *
     * @param message
     */
    public DecryptingException(final String message) {
        super(message);

    }

    /**
     * Constructor.
     *
     * @param cause
     */
    public DecryptingException(final Throwable cause) {
        super(cause);

    }

}
