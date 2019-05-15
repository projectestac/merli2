package cat.xtec.merli.duc.client.services;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import org.semanticweb.owlapi.model.IRI;


/**
 * Duc service RPC interface. This is the main entry point to manipulate
 * entities on a project from the client. This methods will be invoked
 * on the server service implementing this interface.
 */
public interface AbstractServiceIn<T> extends RemoteService {

    /**
     * Fetches a domain entity from a project.
     *
     * @param id        Project identifier
     * @param iri       Entity IRI identifier
     * @param type      Domain type to fetch
     *
     * @return          Entity instance
     */
    T fetch(String id, IRI iri);


    /**
     * Stores a domain entity into a project.
     *
     * @param id        Project identifier
     * @param iri       Entity IRI identifier
     * @param type      Domain type to persist
     */
    void persist(String id, IRI iri, T object);


    /**
     * Obtains a list of entities from a project which have a label that
     * matches the given search string. The project entities are traversed
     * from a root entity and returned in a breadth-first order.
     *
     * @param text      Text to search
     * @param id        Project identifier
     * @param iri       Root entity identifier
     * @param type      Domain type to fetch
     *
     * @return          A list of entities
     */
    List<T> search(String text, String id, IRI iri);


    /**
     * Obtains a list of entities from a project that are direct
     * descendants of an entity.
     *
     * @param id        Project identifier
     * @param iri       Entity IRI identifier
     * @param type      Domain type to fetch
     *
     * @return          A list of entities
     */
    List<T> children(String id, IRI iri);


    /**
     * Permanently removes an entity from a project.
     *
     * @param id        Project identifier
     * @param iri       Entity IRI identifier
     */
    void remove(String id, IRI iri);

}
