package cat.xtec.merli.crawler;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;


/**
 * Handles the servlet context events.
 */
@WebListener("Crawler context listener")
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
