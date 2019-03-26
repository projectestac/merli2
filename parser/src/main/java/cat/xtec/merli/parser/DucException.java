package cat.xtec.merli.parser;


/**
 * Umbrella exception for all the parser exceptions.
 */
public class DucException extends Exception {

    /**
     * {@inheritDoc}
     */
    public DucException() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    public DucException(String message) {
        super(message);
    }


    /**
     * {@inheritDoc}
     */
    public DucException(String message, Throwable cause) {
        super(message, cause);
    }


    /**
     * {@inheritDoc}
     */
    public DucException(Throwable cause) {
        super(cause);
    }

}
