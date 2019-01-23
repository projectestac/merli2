package cat.xtec.merli.duc.client;

import java.util.logging.Logger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.EntryPoint;


/**
 * Main entry point of the DUC module.
 */
public class DucEntryPoint implements EntryPoint {

    /** Module logger instance */
    private static Logger logger;


    /**
     * {@inheritDoc}
     */
    public void onModuleLoad() {
        logger = Logger.getLogger(getLoggerName());

        GWT.setUncaughtExceptionHandler(e -> {
            logger.warning(e.getMessage());
        });
    }


    /**
     * Gets the logger name for this module.
     *
     * @return              Logger name
     */
    public static String getLoggerName() {
        return DucEntryPoint.class.getCanonicalName();
    }


    /**
     * Gets the logger for this module.
     *
     * @return              Logger instance
     */
    public static final Logger getLogger() {
        return logger;
    }

}
