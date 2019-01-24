package cat.xtec.merli.client.zonaclic;

import java.net.URI;
import javax.ws.rs.ext.ContextResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import cat.xtec.merli.client.LomClient;
import cat.xtec.merli.client.LomConverter;
import cat.xtec.merli.client.zonaclic.convert.ProjectConverter;
import cat.xtec.merli.client.zonaclic.domain.*;
import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.lom.Resource;

import static com.fasterxml.jackson.databind.DeserializationFeature.*;
import static java.util.concurrent.TimeUnit.MILLISECONDS;


/**
 * LOM service for Zona Clic.
 */
public class ZonaclicClient implements LomClient {

    /** Endpoint to fetch the projects */
    private final URI ENDPOINT_URI;

    /** URL where to fetch the list of projects */
    private final URI LISTING_URI;

    /** Project to resource converter */
    private final LomConverter<Project> converter;

    /** JAX-RS client instance */
    private final Client client;

    /** JSON mapper instance */
    private static final ObjectMapper mapper = createMapper();

    /** Generic type for a list of project references */
    private class ReferencesType extends GenericType<List<Reference>> {};


    /**
     * Creates a new LOM service instance.
     *
     * @param endpoint          Repository endpoint URL
     */
    public ZonaclicClient(URI endpoint) {
        LISTING_URI = endpoint.resolve("projects.json");
        ENDPOINT_URI = endpoint;

        converter = new ProjectConverter();
        client = createClient();
        client.register(mapperResolver);
    }


    /**
     * {@inheritDoc}
     */
    public Iterator<UID> identifiers() {
        List<Reference> references = fetchReferences();
        List<UID> identifiers = new ArrayList<>();

        for (Reference reference : references) {
            identifiers.add(reference.getUID());
        }

        return identifiers.iterator();
    }


    /**
     * {@inheritDoc}
     */
    public Iterator<UID> identifiers(Date from, Date until) {
        List<Reference> references = fetchReferences();
        List<UID> identifiers = new ArrayList<>();

        for (Reference reference : references) {
            Date updated = reference.getDate();
            UID id = reference.getUID();

            if (updated instanceof Date) {
                if (inDateRange(updated, from, until)) {
                    identifiers.add(id);
                }
            }
        }

        return identifiers.iterator();
    }


    /**
     * {@inheritDoc}
     */
    public Resource resource(UID id) {
        String path = Reference.getPath(id);
        UID location = Reference.getLocation(path);
        Project project = fetchProject(path);

        project.setUID(id);
        project.setLocation(location);

        return toResource(project);
    }


    /**
     * {@inheritDoc}
     */
    public void close() {
        client.close();
    }


    /**
     * Fetches the complete list of projects from the remote server.
     * Note that the full information for the project is not provided
     * on each instance found on the list.
     *
     * @see                     #fetchProject
     * @return                  A list of projects
     */
    protected List<Reference> fetchReferences() {
        return client
            .target(LISTING_URI)
            .request(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.USER_AGENT, null)
            .get(Response.class)
            .readEntity(new ReferencesType());
    }


    /**
     * Fetches the information of a project given its path.
     *
     * @param id                Project identifier
     * @return                  A project object
     */
    protected Project fetchProject(String id) {
        URI uri = ENDPOINT_URI
            .resolve(formatPath(id))
            .resolve("project.json");

        return client
            .target(uri)
            .request(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.USER_AGENT, null)
            .get(Project.class);
    }


    /**
     * Transforms a jClic project into a LOM resource insance. This
     * method maps the propeties of the project to a new rescource.
     *
     * @param project           Project to transform
     * @return                  A new resource instance
     */
    protected Resource toResource(Project project) {
        return converter.convert(project);
    }


    /**
     * Given an URL path fragment removes any hierarchical parts
     * from it and adds a trailing slash if it is missing.
     *
     * This method is used to make sure that we are querying the
     * correct project URL and not a malformed or malicious URL.
     *
     * @param path              URL path fragment
     * @return                  Formatted path
     */
    private String formatPath(String path) {
        String result = path;

        result = result.concat("/");
        result = result.replaceAll("\\.", "");
        result = result.replaceAll("//+", "/");
        result = result.replaceFirst("^[\\./]+", "");

        return result;
    }


    /**
     * Returns if the given date is included in the provided range.
     *
     * @param date              Date to check
     * @param from              Start date (included)
     * @param until             End date (included)
     *
     * @return                  If the date is in the range
     * @throws NullPointerException     If any date is {@code null}
     */
    private boolean inDateRange(Date date, Date from, Date until) {
        return date.compareTo(from) >= 0 && date.compareTo(until) <= 0;
    }


    /**
     * Return a configured JAX-RS client instance.
     *
     * @return          New client
     */
    protected Client createClient() {
        ClientBuilder builder = ClientBuilder.newBuilder();

        builder.connectTimeout(CONNECT_TIMEOUT, MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT, MILLISECONDS);

        return builder.build();
    }


    /**
     * Instantiates a new object mapper for the JAX-RS client.
     *
     * @return          Object mapper instance
     */
    protected static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.enable(READ_ENUMS_USING_TO_STRING);
        mapper.enable(READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        mapper.disable(FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper;
    }


    /**
     * Provides an ObjectMapper to the JAX-RS client.
     */
    protected static final ContextResolver mapperResolver =
        new ContextResolver<ObjectMapper>() {
        public ObjectMapper getContext(Class<?> type) {
            return ZonaclicClient.mapper;
        }
    };

}
