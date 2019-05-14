package cat.xtec.merli.duc.server;

import dagger.Component;
import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.DispatchServiceExecutor;
import edu.stanford.bmir.protege.web.server.inject.ApplicationComponent;
import edu.stanford.bmir.protege.web.server.project.ProjectManager;


/**
 * Main DUC component for the Dagger injection framework.
 */
@DucScope
@Component(
  modules = DucModule.class,
  dependencies = ApplicationComponent.class
)
public interface DucComponent {

    /* Component bindings */

    /** RPC service servlet */
    DucServiceServlet getServiceServlet();

    /* Inherited bindings */

    /** Application permissions manager */
    AccessManager getAccessManager();

    /** Application projects manager */
    ProjectManager getProjectManager();

    /** Actions executor */
    DispatchServiceExecutor getActionExecutor();

}
