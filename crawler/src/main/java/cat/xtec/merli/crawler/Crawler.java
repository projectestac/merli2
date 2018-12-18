package cat.xtec.merli.crawler;

import java.util.logging.Logger;
import cat.xtec.merli.Application;
import cat.xtec.merli.client.LomWorker;


/**
 * Represents this crawler's global state.
 */
public final class Crawler {

    /** Application logger reference */
    private static Logger logger = Application.getLogger();

    /** Singleton worker instance */
    private static final LomWorker worker = new LomWorker();

    /** Prevent the instantiation of this class */
    private Crawler() {}

    /** Initialize this class */
    static { attachShutdownHook(); }


    /**
     * Obtain this crawler's LOM worker instance.
     *
     * @return              This crawler's worker instance
     */
    public static LomWorker getLomWorker() {
        return worker;
    }


    /**
     * Returns a reference to the application logger.
     *
     * @return              Logger instance
     */
    public static Logger getLogger() {
        return logger;
    }


    /**
     * Returns the configured default delay between requests.
     *
     * @return              Default delay in milliseconds
     */
    public static long getDefaultDelay() {
        return Long.parseLong(Application.getProperty(
            "crawler.delay", "1000L"));
    }


    /**
     * Shuts down this crawler. This method cancels all the queued tasks
     * and destoys the LOM worker's thread.
     */
    public static void shutdown() {
        worker.shutdown();
    }


    /**
     * Registers a shutdown hook to clean up this crawler.
     */
    private static void attachShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() { shutdown(); }
        });
    }

}
