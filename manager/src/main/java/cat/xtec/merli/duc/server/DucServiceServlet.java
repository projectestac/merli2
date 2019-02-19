package cat.xtec.merli.duc.server;

import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import edu.stanford.bmir.protege.web.server.app.WebProtegeRemoteServiceServlet;
import edu.stanford.bmir.protege.web.server.logging.DefaultLogger;
import edu.stanford.bmir.protege.web.server.project.Project;

import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.lom.Resource;
import cat.xtec.merli.domain.taxa.Category;
import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.Term;
import cat.xtec.merli.domain.taxa.Vertex;
import cat.xtec.merli.duc.client.services.DucService;


/**
 * This is the main RPC service for the DUC module. The provided methods
 * on this service map OWL entities into domain objects.
 */
public class DucServiceServlet extends WebProtegeRemoteServiceServlet
    implements DucService {

    /** This class version number */
    static final long serialVersionUID = 1L;


    /**
     * Servlet constructor.
     */
    @Inject
    public DucServiceServlet() {
       super(new DefaultLogger());
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
     * {@inheritDoc}
     */
    public Category fetchCategory(String id, String iri) {
        return (Category) fetchObject(id, iri, Category.class);
    }


    /**
     * {@inheritDoc}
     */
    public Entity fetchEntity(String id, String iri) {
        return (Entity) fetchObject(id, iri, Entity.class);
    }


    /**
     * {@inheritDoc}
     */
    public Resource fetchResource(String id, String iri) {
        return (Resource) fetchObject(id, iri, Resource.class);
    }


    /**
     * {@inheritDoc}
     */
    public Term fetchTerm(String id, String iri) {
        return (Term) fetchObject(id, iri, Term.class);
    }


    /**
     * {@inheritDoc}
     */
    public List<Vertex> fetchChildren(String id, String iri) {
        List<Vertex> childs = new java.util.ArrayList<>();
        Entity entity = (Entity) fetchObject(id, iri, Entity.class);
        Vertex child = new Vertex();

        child.setEntity(entity);
        child.setChildCount(1);
        childs.add(child);

        return childs;
    }


    /**
     * {@inheritDoc}
     */
    public void removeEntity(String id, String iri) {
        // null
    }


    /**
     *
     */
    private Object fetchObject(String id, String iri, Class type) {
        Entity entity = null;

        try {
            entity = (Entity) type.newInstance();
            entity.setId(UID.valueOf(id));
            entity.setType(cat.xtec.merli.domain.taxa.EntityType.EDUCATION_PHASE);
        } catch (Exception e) {}

        return entity;
    }

}
