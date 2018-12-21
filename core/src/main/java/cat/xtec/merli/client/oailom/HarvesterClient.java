package cat.xtec.merli.client.oailom;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.lom.Resource;
import cat.xtec.merli.client.LomClient;
import cat.xtec.merli.client.oailom.domain.OAIResponse;
import cat.xtec.merli.client.oailom.domain.oai.Record;
import cat.xtec.merli.client.oailom.domain.oai.References;
import cat.xtec.merli.client.oailom.domain.voc.Verb;

import static java.util.concurrent.TimeUnit.MILLISECONDS;


/**
 * A client service for the Open Archives Initiative Protocol for
 * Metadata Harvesting with support for the XML LOM format.
 *
 * Only the verbs «ListIdentifiers» and «GetRecord» are supported.
 * Note also that for compatibility with the OAI-MPH version 1.1 the
 * date filters are encoded as an ISO 8601 complete date, and thus,
 * the time of the day will be ignored on the requests.
 */
public class HarvesterClient implements LomClient {

    /** Requested metadata format */
    private final String LOM_FORMAT = "oai_lom";

    /** Repository endpoint URL */
    private final URI ENDPOINT_URI;

    /** JAX-RS client instance */
    private final Client client;

    /** To format dates as ISO-8601 in UTC */
    private final DateFormat datetime;


    /**
     * Constructs a new harvester client.
     *
     * @param endpoint          Repository endpoint URL
     */
    public HarvesterClient(URI endpoint) {
        ENDPOINT_URI = endpoint;
        client = createClient();
        datetime = new SimpleDateFormat("yyyy-MM-dd");
        datetime.setTimeZone(TimeZone.getTimeZone("UTC"));
    }


    /**
     * {@inheritDoc}
     */
    public Iterator<UID> identifiers() {
        return new IdentifierIterator(this);
    }


    /**
     * {@inheritDoc}
     */
    public Iterator<UID> identifiers(Date from, Date until) {
        return new IdentifierIterator(this, from, until);
    }


    /**
     * {@inheritDoc}
     */
    public Resource resource(UID id) {
        return toResource(fetchRecord(id));
    }


    /**
     * Fetches the first page of record references from the server.
     * This method requests all the resources.
     *
     * @return              A references object with the results
     */
    protected References fetchReferences() {
        Form form = new Form();

        form.param("verb", Verb.LIST_IDENTIFIERS.value());
        form.param("metadataPrefix", LOM_FORMAT);

        return fetchResponse(form).getReferences();
    }


    /**
     * Fetches the first page of record references from the server
     * given a date range filtering. This method requests only the
     * resources updated on the given date range.
     *
     * @param from          Start date (included)
     * @param until         End date (included)
     *
     * @return              A references object with the results
     */
    protected References fetchReferences(Date from, Date until) {
        Form form = new Form();

        form.param("verb", Verb.LIST_IDENTIFIERS.value());
        form.param("from", datetime.format(from));
        form.param("until", datetime.format(until));
        form.param("metadataPrefix", LOM_FORMAT);

        return fetchResponse(form).getReferences();
    }


    /**
     * Fetches a page of record references from the server.
     *
     * The resumption token is used to specify the page of results to
     * retrieve. A token pointing to the next page of results can be
     * obtained from the {@code Pagination} object encapsulated on each
     * {@code References} instance.
     *
     * @param token         Resumption token of the page
     * @return              A references object with the results
     */
    protected References fetchReferences(String token) {
        Form form = new Form();

        form.param("verb", Verb.LIST_IDENTIFIERS.value());
        form.param("resumptionToken", token);
        form.param("metadataPrefix", LOM_FORMAT);

        return fetchResponse(form).getReferences();
    }


    /**
     * Fetches an individual record from the server given its globally
     * unique identifier.
     *
     * @see #fetchReferences    To obtain a list of identifiers
     * @param id                Unique identifier of the record
     * @return                  A new record instance
     */
    protected Record fetchRecord(UID id) {
        Form form = new Form();

        form.param("identifier", id.getString());
        form.param("metadataPrefix", LOM_FORMAT);
        form.param("verb", Verb.GET_RECORD.value());

        return fetchResponse(form).getRecords().get(0);
    }


    /**
     * Sends a request to the OAI-PMH repository server.
     *
     * @param form              Request parameters
     * @return                  A new response object
     */
    protected OAIResponse fetchResponse(Form form) {
        return client
            .target(ENDPOINT_URI)
            .request(MediaType.APPLICATION_XML)
            .header(HttpHeaders.USER_AGENT, null)
            .post(Entity.form(form), OAIResponse.class);
    }


    /**
     * Returns the LOM resource object contained on a record.
     *
     * @param record            OAI-MPH record instance
     *
     * @throws NullPointerException     If record is {@code null} or
     *      does not contain a resource object.
     */
    protected Resource toResource(Record record) {
        return record.getResources().get(0);
    }


    /**
     * {@inheritDoc}
     */
    public void close() {
        client.close();
    }


    /**
     * Return a configured JAX-RS client instance.
     *
     * @return          New client
     */
    protected Client createClient() {
        ClientBuilder builder = ClientBuilder.newBuilder();

        // builder.connectTimeout(CONNECT_TIMEOUT, MILLISECONDS);
        // builder.readTimeout(READ_TIMEOUT, MILLISECONDS);

        return builder.build();
    }

}
