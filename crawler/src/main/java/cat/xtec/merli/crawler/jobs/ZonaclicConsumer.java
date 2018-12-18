package cat.xtec.merli.crawler.jobs;

import java.net.URI;
import java.util.logging.Logger;

import cat.xtec.merli.Application;
import cat.xtec.merli.client.LomClient;
import cat.xtec.merli.client.LomConsumer;
import cat.xtec.merli.crawler.Crawler;
import cat.xtec.merli.crawler.service.MerliClient;
import cat.xtec.merli.crawler.storage.ResourceStore;
import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.lom.Resource;


/**
 * {@inheritDoc}
 */
public class ZonaclicConsumer implements LomConsumer, AutoCloseable {

    /** Application logger reference */
    private Logger logger = Crawler.getLogger();

    /** Delay between requests */
    private long delay = Crawler.getDefaultDelay();

    /** Current client instance */
    private LomClient client;

    /** SOAP client */
    private MerliClient service;

    /** Resource store */
    private ResourceStore store;


    /**
     * Creates a new Zona Clic consumer.
     *
     * @param client        ZonaClic client
     * @param service       Merli service client
     */
    public ZonaclicConsumer(LomClient client) throws Exception {
        final String endpoint = getServiceEndpoint();

        this.client = client;
        this.store = new ResourceStore();
        this.service = new MerliClient(new URI(endpoint));
    }


    /**
     * Returns the endpoint URL for the service.
     *
     * @return              Endpoint URL
     */
    private static String getServiceEndpoint() {
        return Application.getProperty("crawler.webservice.url");
    }


    /**
     * Consumes a resource given its unique identifier. This method,
     * which is called for each project on the remote server, retrives
     * the project data from Zona Clic and imports it to Merlí.
     *
     * @param id            Identifier of the project
     */
    public void accept(UID id) {
        Resource resource;

        try {
            if (store.contains(id) == false) {
                Thread.sleep(delay);
                resource = client.resource(id);
                service.post(resource);
                store.put(id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            logger.warning(String.valueOf(e));
        }
    }


    /**
     * {@inheritDoc}
     */
    public void close() {
        try {
            store.close();
            service.close();
        } catch (Exception e) {}
    }

}
