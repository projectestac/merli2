package cat.xtec.merli.crawler;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;


/**
 * Handles the servlet context events.
 */
public class CrawlerListener implements ServletContextListener {

    /**
     * {@inheritDoc}
     */
    public void contextInitialized(ServletContextEvent sce) {
        Crawler.getLogger().info("Crawler initialized");
    }


    /**
     * {@inheritDoc}
     */
    public void contextDestroyed(ServletContextEvent sce) {
        Crawler.getLogger().info("Crawler shutting down");
        Crawler.shutdown();
    }

}
