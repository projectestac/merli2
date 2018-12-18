package cat.xtec.merli.client.oailom;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import cat.xtec.merli.domain.UID;
import cat.xtec.merli.client.oailom.domain.oai.Header;
import cat.xtec.merli.client.oailom.domain.oai.Pagination;
import cat.xtec.merli.client.oailom.domain.oai.References;


/**
 * A lazy iterator of OAI-MPH harvesting lists.
 *
 * This iterator fetches the pages of results (ListIdentifiers) only
 * when it is required and returns only the record identifiers.
 */
public class IdentifierIterator implements Iterator<UID> {

    /** Client used to obtain the listings */
    private HarvesterClient client;

    /** Current list of fetched headers */
    private List<Header> headers;

    /** Current resumption token */
    private String token = null;

    /** Return resources updated since this date */
    private Date from;

    /** Return resources updated until this date */
    private Date until;

    /** Whether the first page of results was fetched */
    private boolean initialized = false;

    /** Whether the list of results is filtered */
    private boolean filtered = false;

    /** Next element on the list to return */
    private int index = 0;


    /**
     * Construct a new identifier iterator.
     *
     * @param client        Harvester client instance
     */
    public IdentifierIterator(HarvesterClient client) {
        this.client = client;
        this.filtered = false;
    }


    /**
     * Construct a new identifier iterator.
     *
     * @param client        Harvester client instance
     */
    public IdentifierIterator(HarvesterClient client, Date from, Date until) {
        this.client = client;
        this.from = from;
        this.until = until;
        this.filtered = true;
    }


    /**
     * {@inheritDoc}
     */
    public boolean hasNext() {
        if (initialized == false) {
            refresh(fetchFirstPage());
        } else if (index >= headers.size()) {
            refresh(fetchNextPage());
        }

        return (headers != null) &&
               (index < headers.size());
    }


    /**
     * {@inheritDoc}
     */
    public UID next() {
        if (hasNext() == false) {
            throw new NoSuchElementException();
        }

        Header header = headers.get(index++);
        return header.getIdentifier();
    }


    /**
     * Updates this iterator values with a references object. If
     * the provided object is {@code null}, only the initialized
     * flag is set to true.
     *
     * @param references    References object
     */
    private void refresh(References references) {
        initialized = true;

        if (references instanceof References) {
            this.headers = references.getHeaders();
            this.token = parseToken(references);
            this.index = 0;
        }
    }


    /**
     * Fetches the first page of results applying the provided filters
     * to the request if there were any.
     *
     * @return          A new references object
     */
    private References fetchFirstPage() {
        return (filtered == false) ?
            client.fetchReferences() :
            client.fetchReferences(from, until);
    }


    /**
     * Fetches the next page of results from the server. Does nothing
     * if there aren't any more pages to fetch.
     *
     * @return          A new references object
     */
    private References fetchNextPage() {
        return (token instanceof String) ?
            client.fetchReferences(token) : null;
    }


    /**
     * Returns the resumption token for a page of results.
     *
     * This method returns {@code null} if no token exists on the
     * references object or if the token is a blank string; that is,
     * if the token is empty or contains only spaces.
     *
     * @param references    References object containing the token
     * @return              A resumption token or {@code null}
     */
    private String parseToken(References references) {
        Pagination pagination = references.getPagination();
        String token = null;

        if (pagination instanceof Pagination) {
            token = pagination.getToken();
        }

        if (token instanceof String) {
            if (token.trim().isEmpty()) {
                token = null;
            }
        }

        return token;
    }

}
