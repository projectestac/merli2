package cat.xtec.ws.proxies.correu.utils;

import java.util.ResourceBundle;

public class ServiceLocator {

    public static final String ENTORN_INT = "int";

    public static final String ENTORN_PROD = "pro";
    private static final String BASE_PROP = "cat.xtec.ws.correu.url.";
    private static final String PROP_INT = "cat.xtec.ws.correu.url.int";

    private static final String PROP_PRO = "cat.xtec.ws.correu.url.pro";
    
    /* XTEC - NADIM - 30-09-2015*/
    public static final String ENTORN_PRE = "pre";
    private static final String PROP_PRE = "cat.xtec.ws.correu.url.pre";
    /*END*/
    
    
    private static ServiceLocator locator;
    private ResourceBundle bundle = null;

    private ServiceLocator() {
        this.bundle = ResourceBundle.getBundle("proxyCorreu");
    }

    public static ServiceLocator getInstance() {
        if (locator == null) {
            locator = new ServiceLocator();
        }
        return locator;
    }

    public String getUrl(String entorn) {
        String propertyName = "cat.xtec.ws.correu.url." + entorn;
        if (System.getProperty(propertyName) != null) {
            return System.getProperty(propertyName);
        }
        return this.bundle.getString(propertyName);
    }
}
