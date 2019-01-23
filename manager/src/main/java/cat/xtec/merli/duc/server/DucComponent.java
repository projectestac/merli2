package cat.xtec.merli.duc.server;

import dagger.Component;
import edu.stanford.bmir.protege.web.server.inject.ApplicationComponent;


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

}
