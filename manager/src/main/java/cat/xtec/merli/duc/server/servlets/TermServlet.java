package cat.xtec.merli.duc.server.servlets;

import javax.inject.Inject;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;
import cat.xtec.merli.duc.client.services.TermService;
import cat.xtec.merli.domain.taxa.Term;


/**
 * This servlet implements an RPC service to manage terms.
 */
public class TermServlet
    extends AbstractServlet<Term> implements TermService {

    /** This class version number */
    static final long serialVersionUID = 1L;


    /** Instantiates a new servlet */
    @Inject public TermServlet(ProjectManager manager) {
       super(Term.class, manager);
    }

}
