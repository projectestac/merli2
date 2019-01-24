package cat.xtec.merli;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Application wide static utility methods.
 *
 * This class represents the environment where an application is running
 * in the same way the built-in {@code System} class provides system wide
 * utility methods. Use it to bootstrap your application by providing a
 * properties file with your custom settings.
 */
public final class Application {

    /** Application identifier */
    public static final String NAME = "XTEC Merlí Application";

    /** Application properties path */
    public static final String CONFIG_PATH = "/application.properties";

    /** System property keys that can be set */
    public static final String[] SYSTEM_PROPERTIES = { "http.agent" };

    /** Application logger instance */
    private static Logger logger;

    /** Application configuration properties */
    private static Properties config;

    /** Prevent the instantiation of this class */
    private Application() {}

    /** Initialize this class */
    static { initialize(); }


    /**
     * Application initialization method.
     */
    public static void initialize() {
        initializeLogger();
        updateSystemProperties();
    }


    /**
     * Gets the logger name for this application.
     *
     * @return              Logger name
     */
    public static String getLoggerName() {
        return Application.class.getCanonicalName();
    }


    /**
     * Gets the logger for this application.
     *
     * The logger name is determined by a call to {@link #getLoggerName}
     * wich by default returns the canonical name of this class.
     *
     * @return              Logger instance
     */
    public static Logger getLogger() {
        return logger;
    }


    /**
     * Obtains a resource as an input stream.
     *
     * @param path         Path of the resource
     * @return             Input stream or null
     *
     * @throws NullPointerException  If path is null or invalid
     * @throws IOException  If the file could not be read
     */
    public static InputStream getResource(String path) throws IOException {
        return Application.class.getResourceAsStream(path);
    }


    /**
     * Returns the configuration properties of the application.
     *
     * The default properties are defined on the «application.properties»
     * file found on this package.
     *
     * @return              Application properties
     * @throws IOException  If
     */
    public static Properties getProperties() {
        if (config instanceof Properties) {
            return config;
        }

        try {
            config = readProperties(CONFIG_PATH);
        } catch (Exception e) {
            config = new Properties();
            logger.fine(e.getMessage());
        }

        return config;
    }


    /**
     * Returns the application property for the specified key.
     *
     * @param key           Property name
     * @return              Property value or null
     */
     public static String getProperty(String key) {
         return getProperties().getProperty(key);
     }


    /**
     * Returns the application property for the specified key. If no
     * property is defined with the given key, the provided default
     * value will be returned.
     *
     * @param key           Property name
     * @param value         Fallback value
     *
     * @return              Property value
     */
     public static String getProperty(String key, String value) {
         return getProperties().getProperty(key, value);
     }


     /**
      * Reads a configuration file into the application properties object.
      * This is used to initialize lazily the application settings.
      *
      * @param path         Properties file path
      * @return             New properties instance
      *
      * @throws NullPointerException  If path is null or invalid
      * @throws IOException  If the file could not be read
      * @throws IllegalArgumentException  If the file contents are malformed
      */
     private static Properties readProperties(String path) throws IOException {
        InputStream stream = getResource(path);
        Properties properties = new Properties();

        properties.load(stream);
        stream.close();

        return properties;
     }


     /**
      * Updates the {@link System} properties overwritting them with any
      * provided properties for the application.
      *
      * This method sets the system properties to the values configured on
      * this application properties file, unless they were already configured
      * (from the command line or otherwise). The properties that can be
      * set are defined on the {@link #SYSTEM_PROPERTIES} array.
      */
     private static void updateSystemProperties() {
         try {
             for (String key : SYSTEM_PROPERTIES) {
                 final String value;

                 if (System.getProperty(key) != null) {
                     continue;
                 }

                 if ((value = getProperty(key)) != null) {
                     System.setProperty(key, value);
                     logger.config(String.format("%s=%s", key, value));
                 }
             }
         } catch (SecurityException e) {
             logger.warning("Could not set system properties");
             logger.fine(e.getMessage());
         }
    }


    /**
     * Initialize the logger for this application.
     */
    private static void initializeLogger() {
        logger = Logger.getLogger(getLoggerName());

        try {
            if (getProperty("logger.level") != null) {
                String value = getProperty("logger.level");
                logger.setLevel(Level.parse(value));
            }

            if (getProperty("logger.useParentHandlers") != null) {
                String value = getProperty("logger.useParentHandlers");
                logger.setUseParentHandlers(Boolean.parseBoolean(value));
            }

            if (getProperty("logger.file.pattern") != null) {
                String value = getProperty("logger.file.pattern");
                logger.addHandler(new FileHandler(value));
            }
        } catch (Exception e) {
            System.err.println("Logger initialization failed");
            System.err.print(e.getMessage());
        }
    }

}
