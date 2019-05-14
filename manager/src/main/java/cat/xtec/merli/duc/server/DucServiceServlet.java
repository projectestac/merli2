package cat.xtec.merli.duc.server;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
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
import cat.xtec.merli.duc.client.services.DucService;


/**
 * This is the main RPC service for the DUC module. The provided methods
 * on this service map OWL entities into domain objects.
 */
public class DucServiceServlet extends WebProtegeRemoteServiceServlet
    implements DucService {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Project manager instance */
    private ProjectManager manager;

    // TODO: WA: Create and destroy with project
    private Map<String, OWLStore> stores = new HashMap<>();

    /** Supported domain types */
    private Class<?>[] DOMAIN_TYPES = {
        Entity.class,
        Category.class,
        Resource.class,
        Term.class
    };


    /**
     * Servlet constructor.
     */
    @Inject
    public DucServiceServlet(ProjectManager manager) {
       super(new DefaultLogger());
       this.manager = manager;
    }


    /**
     * Returns the current HTTP session object.
     *
     * @return      Session object
     */
    private HttpSession getSession() {
        return getThreadLocalRequest().getSession();
    }


    /**
     * Returns the locale object for the current session.
     *
     * @return      Locale instance
     */
    private Locale getSessionLocale() {
        return (Locale) getSession().getAttribute("locale");
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
    public Category fetchCategory(String id, IRI iri) {
        return fetchObject(id, iri, Category.class);
    }


    /**
     * {@inheritDoc}
     */
    public Entity fetchEntity(String id, IRI iri) {
        return fetchObject(id, iri, Entity.class);
    }


    /**
     * {@inheritDoc}
     */
    public Resource fetchResource(String id, IRI iri) {
        return fetchObject(id, iri, Resource.class);
    }


    /**
     * {@inheritDoc}
     */
    public Term fetchTerm(String id, IRI iri) {
        return fetchObject(id, iri, Term.class);
    }


    /**
     * {@inheritDoc}
     */
    public List<Entity> fetchChildren(String id, IRI iri) {
        Project project = getProjectById(id);
        OWLOntology root = project.getRootOntology();
        OWLStore store = OWLStore.newInstance(root, DOMAIN_TYPES);

        System.out.println("FETCH CHILDREN FOR: " + iri);

        List<Entity> childs = store.children(iri)
            .map(i -> {
                Entity e = store.fetch(i, Entity.class);

                if (store.children(i).findAny().isPresent()) {
                    System.out.println("Has childs: " + i);
                    e.toggleFlag(EntityFlag.HAS_CHILDREN);
                } else  {
                    System.out.println("No childs for: " + i);
                }

                return e;
            })
            .collect(Collectors.toList());

        return childs;
    }


    /**
     * {@inheritDoc}
     */
    public void removeEntity(String id, IRI iri) {
        Project project = getProjectById(id);
        OWLOntology root = project.getRootOntology();
        OWLStore store = OWLStore.newInstance(root, DOMAIN_TYPES);

        store.remove(iri);
    }


    /**
     *
     */
    private <T> T fetchObject(String id, IRI iri, Class<T> type) {
        System.out.println("FETCH OBJECT REQUEST: " + iri);

        OWLStore store = null;

        // TODO: Keep a cache of stores

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

}
