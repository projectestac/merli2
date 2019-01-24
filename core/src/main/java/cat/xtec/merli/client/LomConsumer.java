package cat.xtec.merli.client;

import cat.xtec.merli.domain.UID;


/**
 * An operation that accepts a single resource.
 */
public interface LomConsumer {

    /**
     * Performs an operation on the given resource.
     *
     * @param id            Resource identifier
     */
    void accept(UID id);

}
