package simpple.xtec.indexador.util;

import java.io.File;
import java.security.Security;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.log4j.Logger;

/**
 * Miscellaneous methods
 *
 * @author descuer
 *
 */
public class Utils {

    // logger
    static final Logger logger = Logger.getLogger(simpple.xtec.indexador.util.Utils.class);

    /**
     * A partir d'un objecte Calendar retorna un String amb la data
     */
    public static String calculaDataActual(Calendar rightNow) {
        int dia_actual = 0;
        int mes_actual = 0;
        int any_actual = 0;
        // 09/03/07 12:49:33,717047000 +01:00 		
        String dia_actual_string = "";
        String mes_actual_string = "";
        String data_actual_string = "";
        try {
            dia_actual = rightNow.get(Calendar.DAY_OF_MONTH);
            dia_actual_string = "" + dia_actual;
            if (dia_actual < 10) {
                dia_actual_string = "0" + dia_actual_string;
            }
            logger.debug("Dia actual: " + dia_actual);
            logger.debug("Dia actual string: " + dia_actual_string);
            mes_actual = rightNow.get(Calendar.MONTH);
            mes_actual++;
            mes_actual_string = "" + mes_actual;
            if (mes_actual < 10) {
                mes_actual_string = "0" + mes_actual_string;
            }
            logger.debug("Mes actual: " + mes_actual);
            logger.debug("Mes actual string: " + mes_actual_string);

            any_actual = rightNow.get(Calendar.YEAR);
            // 09/03/07 12:49:33,717047000 +01:00 		
            data_actual_string = any_actual + "-" + mes_actual_string + "-" + dia_actual_string;
        } catch (Exception e) {
            logger.error(e);
        }
        //String data_actual_string = "01/03/07";
        return data_actual_string;
    }

    /**
     * A partir d'un objecte Calendar retorna un String amb l'hora
     */
    public static String calculaHoraActual(Calendar rightNow) {
        int hora_actual = 0;
        int minut_actual = 0;
        String minut_actual_string = "";
        int segon_actual = 0;
        String segon_actual_string = "";
        String hora_actual_string = "";
        String hora_actual_return = "";
        try {
            hora_actual = rightNow.get(Calendar.HOUR_OF_DAY);
            hora_actual_string = "" + hora_actual;
            if (hora_actual < 10) {
                hora_actual_string = "0" + hora_actual_string;
            }
            minut_actual = rightNow.get(Calendar.MINUTE);
            minut_actual_string = "" + minut_actual;
            if (minut_actual < 10) {
                minut_actual_string = "0" + minut_actual_string;
            }
            segon_actual = rightNow.get(Calendar.SECOND);
            segon_actual_string = "" + segon_actual;
            if (segon_actual < 10) {
                segon_actual_string = "0" + segon_actual_string;
            }
            hora_actual_return = hora_actual_string + ":" + minut_actual_string + ":" + segon_actual_string;
        } catch (Exception e) {
            logger.error(e);
        }
        return hora_actual_return;
    }

    /**
     * Crea l'objecte HttpClient
     *
     */
    public static HttpClient createHttpClient(int timeOutMillis) {
        MultiThreadedHttpConnectionManager myConnectionManager = null;
        HttpClient client = null;
        HttpClientParams clientParams = null;
        try {
            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");

            clientParams = new HttpClientParams();
            // Sets the timeout in milliseconds used when retrieving an HTTP connection from the HTTP connection manager.			

//			clientParams.setConnectionManagerTimeout(15000);
            clientParams.setConnectionManagerTimeout(timeOutMillis);

            // Sets the default socket timeout (SO_TIMEOUT) in milliseconds which is the timeout for waiting for data. A timeout value of zero is interpreted as an infinite timeout.
            // clientParams.setSoTimeout(30000);
            //	clientParams.setSoTimeout(15000);
            clientParams.setSoTimeout(timeOutMillis);

            HttpConnectionManagerParams connManagerParams = new HttpConnectionManagerParams();
            connManagerParams.setDefaultMaxConnectionsPerHost(50);
            connManagerParams.setMaxTotalConnections(100);
            myConnectionManager = new MultiThreadedHttpConnectionManager();
            myConnectionManager.setParams(connManagerParams);
            client = new HttpClient(clientParams, myConnectionManager);
            //		   client.setConnectionTimeout(15000);
            //	        client.setTimeout(15000);
            client.setConnectionTimeout(timeOutMillis);
            client.setTimeout(timeOutMillis);

            //	Protocol myhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
/*		    Protocol myhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
             Protocol.registerProtocol("https", new Protocol("https", new EasySSLProtocolSocketFactory(), 443));
             */
            Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", easyhttps);
            // Protocol.registerProtocol("https", new Protocol("https", new EasySSLProtocolSocketFactory(), 443));			

        } catch (Exception e) {

            logger.error("CreateHttpClient --> " + e);
        }
        return client;
    }

    public static String girarData(String dataOriginal) {
        String dataResultat = "";
        StringTokenizer myTokenizer = null;
        String any = "";
        String mes = "";
        String dia = "";
        try {
            myTokenizer = new StringTokenizer(dataOriginal, "-");
            dia = (String) myTokenizer.nextToken();
            mes = (String) myTokenizer.nextToken();
            any = (String) myTokenizer.nextToken();
            dataResultat = any + "-" + mes + "-" + dia;
        } catch (Exception e) {
            logger.error(e);
        }
        return dataResultat;
    }

    public static void clearIndexDir(String rootDir) {
        File f = null;
        try {
            f = new File(rootDir);
            String[] allFiles = f.list();
            int i = 0;
            while (i < allFiles.length) {
                String fileToDeleteString = rootDir + "/" + allFiles[i];
                File fileToDelete = new File(fileToDeleteString);
                fileToDelete.delete();
                i++;
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static String fromAcute(String original) {
        String modificada = "";
        try {
            modificada = original;
            modificada = modificada.replaceAll("&iacute;", "í");
            modificada = modificada.replaceAll("&oacute;", "ó");
            modificada = modificada.replaceAll("&aacute;", "á");
            modificada = modificada.replaceAll("&eacute;", "é");
            modificada = modificada.replaceAll("&uacute;", "ú");
            modificada = modificada.replaceAll("&Oacute;", "Ó");
            modificada = modificada.replaceAll("&Eacute;", "É");
            modificada = modificada.replaceAll("&Iacute;", "Í");
            modificada = modificada.replaceAll("&Aacute;", "Á");
            modificada = modificada.replaceAll("&Uacute;", "Ú");
            modificada = modificada.replaceAll("&ograve;", "ò");
            modificada = modificada.replaceAll("&agrave;", "à");
            modificada = modificada.replaceAll("&egrave;", "è");
            modificada = modificada.replaceAll("&Ograve;", "Ò");
            modificada = modificada.replaceAll("&Agrave;", "À");
            modificada = modificada.replaceAll("&Egrave;", "È");
            modificada = modificada.replaceAll("&ccedil;", "ç");
            modificada = modificada.replaceAll("&Ccedil;", "Ç");

        } catch (Exception e) {
            logger.error(e);
        }
        return modificada;
    }

    public static Connection getConnectionFromPool() {
        Connection myConnection = null;
        try {
            Context initContext = new InitialContext();
            DataSource ds = null;
            //logger.info("Getting connection....");
            try {
                Class.forName("oracle.jdbc.OracleDriver").newInstance();


                ds = (DataSource) initContext.lookup("jdbc/pool/CercadorConnectionPoolDS");
                myConnection = ds.getConnection();

            } catch (Exception e3) {
                logger.info(e3);
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
                myConnection = DriverManager.getConnection(
                        Configuracio.cadenaConnexioBDOracle, Configuracio.userBDOracle, Configuracio.passwordBDOracle
                );

            }
            //logger.info("Connection get....");
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
        return myConnection;
    }

    public static String treureAccents(String original) {
        String modificada = "";
        try {
            modificada = original.toLowerCase();
            modificada = modificada.replaceAll("à", "a");
            modificada = modificada.replaceAll("á", "a");
            modificada = modificada.replaceAll("í", "i");
            modificada = modificada.replaceAll("ú", "u");
            modificada = modificada.replaceAll("é", "e");
            modificada = modificada.replaceAll("è", "e");
            modificada = modificada.replaceAll("ó", "o");
            modificada = modificada.replaceAll("ò", "o");
            modificada = modificada.replaceAll("ï", "i");
            modificada = modificada.replaceAll("ü", "u");
        } catch (Exception e) {
            logger.error(e);
        }
        return modificada;
    }

    public static String getIndexActualFromDB() {
        String indexActualDB = "";
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            myConnection = getConnectionFromPool();
            stmt = myConnection.createStatement();
            sql = "SELECT current_index FROM config_indexacio";
            logger.debug(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                indexActualDB = rs.getString("current_index");
            }

            //  	indexActualDB = "/home/cercador/index";
            indexActualDB = indexActualDB.replaceAll("/tmp", "");
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
            }
        }
        return indexActualDB;
    }

    public static void setIndexActualDB(String indexActual) {

        Connection myConnection = null;
        PreparedStatement prepStmt = null;
        String sql = "";
        try {
            myConnection = getConnectionFromPool();

            sql = "UPDATE config_indexacio SET current_index = ?";
            prepStmt = myConnection.prepareStatement(sql);
            prepStmt.setString(1, indexActual);
            logger.debug(sql);
            prepStmt.executeUpdate();

        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (prepStmt != null) {
                    prepStmt.close();
                }
                if (myConnection != null) {
                    Utils.commit(myConnection);
                    myConnection.close();
                }

            } catch (Exception e) {
            }
        }

    }

    public static void commit(Connection myConnection) {
        try {
            if (myConnection != null) {
                myConnection.commit();
            }
        } catch (Exception e) {
            //  logger.error(e);	  
        }

    }

}
