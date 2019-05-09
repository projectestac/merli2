package cat.xtec.merli.duc.client.services;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cat.xtec.merli.domain.lom.Resource;
import cat.xtec.merli.domain.taxa.Category;
import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.Term;


/**
 * Duc service RPC interface.
 */
@RemoteServiceRelativePath("duc-service")
public interface DucService extends RemoteService {

    /**
     * Obtains a {@code Category} representation of an OWL entity.
     *
     * @param id    Project identifier
     * @param iri   Entity IRI
     *
     * @return      Category instance
     */
    Category fetchCategory(String id, String iri);


    /**
     * Obtains an {@code Entity} representation of an OWL entity.
     *
     * @param id    Project identifier
     * @param iri   Entity IRI
     *
     * @return      Entity instance
     */
    Entity fetchEntity(String id, String iri);


    /**
     * Obtains a {@code Resource} representation of an OWL entity.
     *
     * @param id    Project identifier
     * @param iri   Entity IRI
     *
     * @return      Resource instance
     */
    Resource fetchResource(String id, String iri);


    /**
     * Obtains a {@code Term} representation of an OWL entity.
     *
     * @param id    Project identifier
     * @param iri   Entity IRI
     *
     * @return      Term instance
     */
    Term fetchTerm(String id, String iri);


    /**
     * Obtains the direct children of an OWL class as a list of
     * {@code Entity} intances.
     *
     * @param id    Project identifier
     * @param iri   Entity IRI
     *
     * @return      Entity nodes list
     */
    List<Entity> fetchChildren(String id, String iri);


    /**
     * Removes from a project all the entities that have the
     * given identifier.
     *
     * @param id    Project identifier
     * @param iri   Entity IRI
     */
    void removeEntity(String id, String iri);

}
