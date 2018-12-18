package cat.xtec.merli.client;

import java.util.Date;
import java.util.Iterator;

import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.lom.Resource;


/**
 * A client service that can be used to iterate LOM resources.
 *
 * Client implementations are encouraged to respect the defined connect
 * and read timeouts for the requests, although they may as well set
 * their own values.
 */
public interface LomClient extends AutoCloseable {

    /** Default connect timeout in milliseconds */
    public static final long CONNECT_TIMEOUT = 5000L;

    /** Default read timeout in milliseconds */
    public static final long READ_TIMEOUT = 180000L;


    /**
     * Returns an iterator of identifiers for all the resources.
     *
     * @return              A stream of unique identifiers
     */
    public Iterator<UID> identifiers();


    /**
     * Returns an iterator of identifiers for resources that have
     * been updated on the given date range.
     *
     * @param from          Start date (included)
     * @param until         End date (included)
     *
     * @return              A stream of unique identifiers
     */
    public Iterator<UID> identifiers(Date from, Date until);


    /**
     * Returns a LOM resource given a unique identifier.
     *
     * @param id            Unique resource identifier
     * @return              LOM resource object
     */
    public Resource resource(UID id);

}
