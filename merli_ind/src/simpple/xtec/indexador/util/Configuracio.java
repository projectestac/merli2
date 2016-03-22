package simpple.xtec.indexador.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.RandomAccessFile;

import org.apache.log4j.Logger;

import simpple.xtec.indexador.util.TipusFitxer;

/**
 * This class is used to load and store the configuration parameters for the
 * application. The valuea of all this parameters are extracted from a
 * configuration file.
 *
 * @author descuer
 *
 */
public class Configuracio {

    // logger
    static final Logger logger = Logger.getLogger(simpple.xtec.indexador.util.Configuracio.class);

    // config parameters
    public static String contextWebAplicacio = "";
    public static String indexDir = "";
    public static String indexDir2 = "";

    public static String cadenaConnexioBDOracle = "";
    public static String userBDOracle = "";
    public static String passwordBDOracle = "";
    public static String nomDriverBDOracle = "";
    public static String servidorWSmerli = "";
    public static String portWSmerli = "";
    public static String nameHarvestingWS = "";
    public static String nameLomWS = "";

    public static String servidorOrganitzador = "";
    public static String nameRecomanacionsWS = "";

    public static String servidorWeb = "";
    public static String portWeb = "";
//     public static String aplicacioCercador = "";    

    public static String parseUrl = "";
    public static String refreshDUC = "";

    //Added Nadim
    public static String versionControl="v2.0.1";

    /**
     * Check if the values are already loaded
     *
     * @return true if there are loaded
     */
    public static boolean isVoid() {
        if (servidorWeb.equals("")) {
            return true;
        }
        return false;
    }

    /**
     * Load the info from the configuration file
     *

    public static void carregaConfiguracio() {

        RandomAccessFile propertiesFile = null;
        String nameFile;
        String line;

        try {
            logger.debug("[carregaConfiguracio] ");
            //	javax.naming.Context ctx = new javax.naming.InitialContext();

            nameFile = System.getProperty("indexacio.educacio");


            logger.debug("[Buscant configuracio] " + nameFile);

            propertiesFile = new RandomAccessFile(nameFile, "r");
            logger.debug("propertiesFile " + propertiesFile);

            if ((propertiesFile != null) && (propertiesFile.length() > 0)) {
                while ((line = propertiesFile.readLine()) != null) {

                    StringTokenizer myTokenizer = new StringTokenizer(line, "=");
                    String name = myTokenizer.nextToken();
                    String value = "";
                    if (myTokenizer.hasMoreElements()) {
                        value = myTokenizer.nextToken().trim();
                    }
                    if (name.equalsIgnoreCase("servidorWeb")) {
                        servidorWeb = value;
                        logger.debug("[Setting servidorWeb] " + value);
                    }
                    if (name.equalsIgnoreCase("portWeb")) {
                        portWeb = value;
                        logger.debug("[Setting portWeb] " + value);
                    }
                    if (name.equalsIgnoreCase("contextWebAplicacio")) {
                        contextWebAplicacio = value;
                        logger.debug("[Setting contextWebAplicacio] " + value);
                    }
                    if (name.equalsIgnoreCase("indexDir")) {
                        indexDir = value;
                        logger.debug("[Setting indexDir] " + value);
                    }
                    if (name.equalsIgnoreCase("indexDir2")) {
                        indexDir2 = value;
                        logger.debug("[Setting indexDir2] " + value);
                    }
                    if (name.equalsIgnoreCase("userBDOracle")) {
                        userBDOracle = value;
                        logger.debug("[Setting userBDOracle] " + value);
                    }
                    if (name.equalsIgnoreCase("passwordBDOracle")) {
                        passwordBDOracle = value;
                        logger.debug("[Setting passwordBDOracle] " + value);
                    }
                    if (name.equalsIgnoreCase("nomDriverBDOracle")) {
                        nomDriverBDOracle = value;
                        logger.debug("[Setting nomDriverBDOracle] " + value);
                    }
                    if (name.equalsIgnoreCase("cadenaConnexioBDOracle")) {
                        cadenaConnexioBDOracle = value;
                        logger.debug("[Setting cadenaConnexioBDOracle] " + value);
                    }
                    if (name.equalsIgnoreCase("servidorWSmerli")) {
                        servidorWSmerli = value;
                        logger.debug("[Setting servidorWSmerli] " + value);
                    }
                    if (name.equalsIgnoreCase("portWSmerli")) {
                        portWSmerli = value;
                        logger.debug("[Setting portWSmerli] " + value);
                    }
                    if (name.equalsIgnoreCase("nameHarvestingWS")) {
                        nameHarvestingWS = value;
                        logger.debug("[Setting nameHarvestingWS] " + value);
                    }
                    if (name.equalsIgnoreCase("nameLomWS")) {
                        nameLomWS = value;
                        logger.debug("[Setting nameLomWS] " + value);
                    }
                    if (name.equalsIgnoreCase("servidorOrganitzador")) {
                        servidorOrganitzador = value;
                        logger.debug("[Setting servidorOrganitzador] " + value);
                    }
                    if (name.equalsIgnoreCase("nameRecomanacionsWS")) {
                        nameRecomanacionsWS = value;
                        logger.debug("[Setting nameRecomanacionsWS] " + value);
                    }
                    if (name.equalsIgnoreCase("parseUrl")) {
                        parseUrl = value;
                        logger.debug("[Setting parseUrl] " + value);
                    }
                    if (name.equalsIgnoreCase("refreshDUC")) {
                        refreshDUC = value;
                        logger.debug("[Setting refreshDUC] " + value);
                    }

                }
                if (TipusFitxer.isVoid()) {
//        	 Class.forName(nomDriverBDOracle);
                    TipusFitxer.carregaTipusFitxer();
                }
            } else {
                carregaConfiguracioBD();
            }
        } catch (Exception e) {
            logger.error(e);
            carregaConfiguracioBD();
        } finally {
            try {
                propertiesFile.close();
            } catch (Exception e) {
            }
        }

    }
        */
    public static void carregaConfiguracio() {

        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            logger.debug("[carregaConfiguracio] -> init");
            myConnection = Utils.getConnectionFromPool();
            stmt = myConnection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM configuracio");
            logger.debug("SELECT * FROM configuracio");

            while (rs.next()) {

                String name = rs.getString("clau");
                String value = rs.getString("valor");

                if (name.equalsIgnoreCase("servidorWeb")) {
                    servidorWeb = value;
                    logger.debug("[Setting servidorWeb] " + value);
                }
                if (name.equalsIgnoreCase("portWeb")) {
                    portWeb = value;
                    logger.debug("[Setting portWeb] " + value);
                }
                if (name.equalsIgnoreCase("contextWebAplicacio")) {
                    contextWebAplicacio = value;
                    logger.debug("[Setting contextWebAplicacio] " + value);
                }
                if (name.equalsIgnoreCase("indexDir")) {
                    indexDir = value;
                    logger.debug("[Setting indexDir] " + value);
                }
                if (name.equalsIgnoreCase("indexDir2")) {
                    indexDir2 = value;
                    logger.debug("[Setting indexDir2] " + value);
                }
                if (name.equalsIgnoreCase("userBDOracle")) {
                    userBDOracle = value;
                    logger.debug("[Setting userBDOracle] " + value);
                }
                if (name.equalsIgnoreCase("passwordBDOracle")) {
                    passwordBDOracle = value;
                    logger.debug("[Setting passwordBDOracle] " + value);
                }
                if (name.equalsIgnoreCase("nomDriverBDOracle")) {
                    nomDriverBDOracle = value;
                    logger.debug("[Setting nomDriverBDOracle] " + value);
                }
                if (name.equalsIgnoreCase("cadenaConnexioBDOracle")) {
                    cadenaConnexioBDOracle = value;
                    logger.debug("[Setting cadenaConnexioBDOracle] " + value);
                }
                if (name.equalsIgnoreCase("servidorWSmerli")) {
                    servidorWSmerli = value;
                    logger.debug("[Setting servidorWSmerli] " + value);
                }
                if (name.equalsIgnoreCase("portWSmerli")) {
                    portWSmerli = value;
                    logger.debug("[Setting portWSmerli] " + value);
                }
                if (name.equalsIgnoreCase("nameHarvestingWS")) {
                    nameHarvestingWS = value;
                    logger.debug("[Setting nameHarvestingWS] " + value);
                }
                if (name.equalsIgnoreCase("nameLomWS")) {
                    nameLomWS = value;
                    logger.debug("[Setting nameLomWS] " + value);
                }
                if (name.equalsIgnoreCase("servidorOrganitzador")) {
                    servidorOrganitzador = value;
                    logger.debug("[Setting servidorOrganitzador] " + value);
                }
                if (name.equalsIgnoreCase("nameRecomanacionsWS")) {
                    nameRecomanacionsWS = value;
                    logger.debug("[Setting nameRecomanacionsWS] " + value);
                }
                if (name.equalsIgnoreCase("parseUrl")) {
                    parseUrl = value;
                    logger.debug("[Setting parseUrl] " + value);
                }
                if (name.equalsIgnoreCase("refreshDUC")) {
                    refreshDUC = value;
                    logger.debug("[Setting refreshDUC] " + value);
                }

            }

            if (TipusFitxer.isVoid()) {

                TipusFitxer.carregaTipusFitxer();


            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    Utils.commit(myConnection);
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

}
