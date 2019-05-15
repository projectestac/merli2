package cat.xtec.merli.duc.server.servlets;

import javax.inject.Inject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import cat.xtec.merli.duc.client.services.CategoryService;
import cat.xtec.merli.domain.taxa.Category;


/**
 * This servlet implements an RPC service to manage categories.
 */
public class CategoryServlet
    extends AbstractServlet<Category> implements CategoryService {

    /** This class version number */
    static final long serialVersionUID = 1L;


    /** Instantiates a new servlet */
    @Inject public CategoryServlet(ProjectManager manager) {
       super(Category.class, manager);
    }

}
