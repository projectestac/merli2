package cat.xtec.merli.crawler.jobs;

import java.net.URI;
import java.util.logging.Logger;
import java.util.concurrent.CancellationException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cat.xtec.merli.Application;
import cat.xtec.merli.client.LomClient;
import cat.xtec.merli.client.LomWorker;
import cat.xtec.merli.client.zonaclic.ZonaclicClient;
import cat.xtec.merli.crawler.Crawler;


/**
 * Queues a new crawling task for Zona Clic.
 */
public class ZonaclicJob implements Job {

    /** Application logger reference */
    private Logger logger = Crawler.getLogger();

    /** Crawler's worker instance */
    private LomWorker worker = Crawler.getLomWorker();


    /**
     * Queues a task for a new client and waits for the task to finish
     * before returning.
     */
    @Override
    public void execute(JobExecutionContext c) throws JobExecutionException {
        final String endpoint = getClientEndpoint();

        try (
            LomClient client = new ZonaclicClient(new URI(endpoint));
            ZonaclicConsumer consumer = new ZonaclicConsumer(client);
        ) {
            logger.info("Queuing task: " + endpoint);
            worker.queue(client, consumer).get();
            logger.info("Task finalized: " + endpoint);
        } catch (CancellationException e) {
            logger.warning("Task canceled: " + endpoint);
        } catch (Exception e) {
            logger.severe(String.valueOf(e));
            throw new JobExecutionException(e);
        }
    }


    /**
     * Returns the endpoint URL for the LOM client.
     *
     * @return              Endpoint URL
     */
    private static String getClientEndpoint() {
        return Application.getProperty("zonaclic.endpoint.url");
    }

}
