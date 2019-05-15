package cat.xtec.merli.duc.server.servlets;

import javax.inject.Inject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import cat.xtec.merli.duc.client.services.ResourceService;
import cat.xtec.merli.domain.lom.Resource;


/**
 * This servlet implements an RPC service to manage resources.
 */
public class ResourceServlet
    extends AbstractServlet<Resource> implements ResourceService {

    /** This class version number */
    static final long serialVersionUID = 1L;


    /** Instantiates a new servlet */
    @Inject public ResourceServlet(ProjectManager manager) {
       super(Resource.class, manager);
    }

}
