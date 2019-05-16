package cat.xtec.merli.duc.server.servlets;

import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import edu.stanford.bmir.protege.web.server.app.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.logging.DefaultLogger;
import edu.stanford.bmir.protege.web.server.project.Project;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import org.semanticweb.owlapi.model.*;

import cat.xtec.merli.domain.lom.Resource;
import cat.xtec.merli.domain.taxa.Category;
import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.EntityFlag;
import cat.xtec.merli.domain.taxa.Term;
import cat.xtec.merli.mapper.OWLStore;
import cat.xtec.merli.duc.client.services.DucServiceAbstract;
import cat.xtec.merli.duc.client.services.DucServiceException;


/**
 * This is the main RPC service for the DUC module. The provided methods
 * on this service map OWL entities into domain objects.
 * TODO: REFACTOR
 */
public abstract class AbstractServlet<T>
    extends WebProtegeRemoteServiceServlet implements DucServiceAbstract<T> {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Supported domain types */
    static final Class<?>[] DOMAIN_TYPES = {
        Entity.class,
        Category.class,
        Resource.class,
        Term.class
    };

    private Class<T> type;

    /** Project manager instance */
    private ProjectManager manager;

    // TODO: WA: Create and destroy with project
    // TODO: Keep a cache of stores per project
    private Map<String, OWLStore> stores = new HashMap<>();


    /**
     * Servlet constructor.
     */
    public AbstractServlet(Class<T> type, ProjectManager manager) {
       super(new DefaultLogger());
       this.manager = manager;
       this.type = type;
    }


    /**
     * Obtains the project with the given unique identifier.
     *
     * @param id    Unique project identifier
     */
    private Project getProjectById(String id) {
        return manager.getProject(ProjectId.get(id), getUserInSession());
    }


    /**
     * {@inheritDoc}
     */
    public T fetch(String id, IRI iri) throws DucServiceException {
        OWLStore store = null;

        if (stores.containsKey(id)) {
            store = stores.get(id);
            System.out.println("Reuse store: " + id);
        } else {
            Project project = getProjectById(id);
            OWLOntology root = project.getRootOntology();
            store = OWLStore.newInstance(root, DOMAIN_TYPES);
            stores.put(id, store);
            System.out.println("Create store: " + id);
        }

        return store.fetch(iri, type);
    }


    /**
     * {@inheritDoc}
     */
    public void persist(String id, IRI iri, T object) throws DucServiceException {
        // TODO: Implement
        // TODO: Send changes to DispatchServiceExecutor
        // TODO: Ensure a parent is set for the type

        System.out.println("WAS SAVED: " + iri);
        throw new DucServiceException("Woah!");
    }


    /**
     * {@inheritDoc}
     */
    public void remove(String id, IRI iri) throws DucServiceException {
        // TODO: Implement
        // TODO: Change parent of children before removal
        // TODO: Send changes to DispatchServiceExecutor

        Project project = getProjectById(id);
        OWLOntology root = project.getRootOntology();
        OWLStore store = OWLStore.newInstance(root, DOMAIN_TYPES);

        store.remove(iri);
        System.out.println("WAS REMOVED: " + iri);
    }


    /**
     * {@inheritDoc}
     */
    public List<T> search(String text, String id, IRI iri) throws DucServiceException {
        return Collections.emptyList(); // TODO: Implement
    }


    /**
     * {@inheritDoc}
     */
    public List<T> children(String id, IRI iri) throws DucServiceException {
        Project project = getProjectById(id);
        OWLOntology root = project.getRootOntology();
        OWLStore store = OWLStore.newInstance(root, DOMAIN_TYPES);

        List<T> childs = store.children(iri)
            .map(i -> {
                T e = store.fetch(i, type);

                if (store.children(i).findAny().isPresent()) {
                    Entity c = (Entity) e;
                    c.toggleFlag(EntityFlag.HAS_CHILDREN);
                }

                return e;
            })
            .collect(Collectors.toList());

        return childs;
    }

}
