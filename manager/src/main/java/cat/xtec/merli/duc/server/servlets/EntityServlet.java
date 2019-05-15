package cat.xtec.merli.duc.server.servlets;

import javax.inject.Inject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import cat.xtec.merli.duc.client.services.EntityService;
import cat.xtec.merli.domain.taxa.Entity;


/**
 * This servlet implements an RPC service to manage entities.
 */
public class EntityServlet
    extends AbstractServlet<Entity> implements EntityService {

    /** This class version number */
    static final long serialVersionUID = 1L;


    /** Instantiates a new servlet */
    @Inject public EntityServlet(ProjectManager manager) {
       super(Entity.class, manager);
    }

}
