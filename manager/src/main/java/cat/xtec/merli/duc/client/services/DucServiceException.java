package cat.xtec.merli.duc.client.services;

import java.io.Serializable;

/**
 * Exception thrown when an RPC service callback fails.
 */
public class DucServiceException extends Exception implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;


    /**
     * {@inheritDoc}
     */
    public DucServiceException() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    public DucServiceException(String message) {
        super(message);
    }

}
