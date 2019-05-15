package cat.xtec.merli.duc.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import edu.stanford.bmir.protege.web.server.inject.ApplicationComponent;


/**
 * This context listener initializes the DUC module dependencies graph
 * for the Dagger injection framework and registers the RPC service when
 * the web application is initialized.
 */
@WebListener
public class DucContextListener implements ServletContextListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        // Obtain the main application component from the context

        ApplicationComponent app = (ApplicationComponent) context
            .getAttribute(ApplicationComponent.class.getName());

        // Build the DUC module's component tree, which depends on the
        // main application component

        DucComponent duc = DaggerDucComponent.builder()
            .applicationComponent(app).build();

        // Register the module's RPC service servlets. Notice that we
        // must register them using the dagger component in order for
        // the servlets to get injected correcly.

        context.addServlet("EntityService", duc.getEntityServlet())
               .addMapping("/webprotege/duc/entities");

        context.addServlet("CategoryService", duc.getCategoryServlet())
               .addMapping("/webprotege/duc/categories");

        context.addServlet("ResourceService", duc.getResourceServlet())
               .addMapping("/webprotege/duc/resources");

        context.addServlet("TermService", duc.getTermServlet())
               .addMapping("/webprotege/duc/terms");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {}

}
