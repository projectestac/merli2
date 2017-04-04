package simpple.xtec.web.util;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * This class is used to load and store the configuration parameters for the application.
 * The valuea of all this parameters are extracted from a configuration file.
 *
 * @author descuer
 *
 */

public class Configuracio {

	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.util.Configuracio.class);

	// config parameters

/*    public static String cadenaConnexioBDOracle = "";
    public static String userBDOracle = "";
    public static String passwordBDOracle = "";
    public static String nomDriverBDOracle = "";*/


	public static String servidorWeb = "";
	public static String portWeb = "";
	public static String contextWebAplicacio = "";
	public static String indexDir = "";
	public static String indexDir2 = "";
	public static int numResultatsPagina = 0;
	public static String idioma = "";

	public static String servidorOrganitzador = "";
	public static String serveiOrganitzador = "";

	public static String sso = "";
	public static String refreshIndex = "";
	public static String maxItemsRss = "";

	public static String userAgrega;
	public static String paswAgrega;

	public static int numNoticiesPortada = 4;
	public static String versionControl = "V 2.1.3";


	/**
	 * Check if the values are already loaded
	 *
	 * @return true if there are loaded
	 */

	public static boolean isVoid(){
		if (servidorWeb.equals("")){
			return true;
		}
		return false;
	}

	/**
	 * Load the info from the configuration file
	 *
	 */

	public static void carregaConfiguracio (){

		RandomAccessFile propertiesFile = null;
		String nameFile;
		String line;

		try {

			javax.naming.Context ctx = new javax.naming.InitialContext();
			nameFile = System.getProperty("config.educacio");
			if (nameFile == null) {
				nameFile = (String) ctx.lookup("java:comp/env/config.educacio");
			}

			logger.debug("[Buscant configuracio] " + nameFile);



			propertiesFile = new RandomAccessFile(nameFile, "r");
			if (propertiesFile.length() > 0) {
				logger.debug("[Getting from file]");
				while ((line = propertiesFile.readLine()) != null) {

					StringTokenizer myTokenizer = new StringTokenizer(line,"=");
					String name = myTokenizer.nextToken();
					String value = "";
					if (myTokenizer.hasMoreElements()){
						value = myTokenizer.nextToken().trim();
					}
					if (name.equalsIgnoreCase("servidorWeb")){
						servidorWeb = value;
						logger.debug("[Setting servidorWeb] " + value);
					}
					if (name.equalsIgnoreCase("portWeb")){
						portWeb = value;
						logger.debug("[Setting portWeb] " + value);
					}
					if (name.equalsIgnoreCase("contextWebAplicacio")){
						contextWebAplicacio = value;
						logger.debug("[Setting contextWebAplicacio] " + value);
					}
					if (name.equalsIgnoreCase("indexDir")){
						indexDir = value;
						logger.debug("[Setting indexDir] " + value);
					}
					if (name.equalsIgnoreCase("indexDir2")){
						indexDir2 = value;
						logger.debug("[Setting indexDir2] " + value);
					}
					if (name.equalsIgnoreCase("numResultatsPagina")){
						numResultatsPagina = new Integer(value).intValue();
						logger.debug("[Setting numResultatsPagina] " + value);
					}
					if (name.equalsIgnoreCase("idioma")){
						idioma = value;
						logger.debug("[Setting idioma] " + value);
					}
					if (name.equalsIgnoreCase("servidorOrganitzador")){
						servidorOrganitzador = value;
						logger.debug("[Setting servidorOrganitzador] " + value);
					}
					if (name.equalsIgnoreCase("serveiOrganitzador")){
						serveiOrganitzador = value;
						logger.debug("[Setting serveiOrganitzador] " + value);
					}
					if (name.equalsIgnoreCase("sso")){
						sso = value;
						logger.debug("[Setting sso] " + value);
					}
					if (name.equalsIgnoreCase("refreshIndex")){
						refreshIndex = value;
						logger.debug("[Setting refreshIndex] " + value);
					}
					if (name.equalsIgnoreCase("maxItemsRss")){
						maxItemsRss = value;
						logger.debug("[Setting maxItemsRss] " + value);
					}
					if (name.equalsIgnoreCase("numNoticiesPortada")){
						numNoticiesPortada = new Integer(value).intValue();
						logger.debug("[Setting numNoticiesPortada] " + value);
					}
					if (name.equalsIgnoreCase("agrega.user")){
						userAgrega =value;
						logger.debug("[Setting userAgrega] " + value);
					}
					if (name.equalsIgnoreCase("agrega.password")){
						paswAgrega =value;
						logger.debug("[Setting paswAgrega] " + value);
					}

				}

				if (TipusFitxer.isVoid()) {
					TipusFitxer.carregaTipusFitxer();
					//   XMLCollection.loadProperties(nameFile);
				}
			}           else {
				carregaConfiguracioBD();
			}
		} catch (Exception e) {
			logger.warn(e);
			carregaConfiguracioBD();
		} finally {
			try {
				propertiesFile.close();
			} catch (Exception e) {}
		}
	}




	public static void carregaConfiguracioBD (){


		Connection myConnection = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			logger.debug("[carregaConfiguracioBD] -> init");
			myConnection = UtilsCercador.getConnectionFromPool();
			stmt = myConnection.createStatement();
			rs = stmt.executeQuery("SELECT * FROM configuracio");
			logger.debug("SELECT * FROM configuracio");

			while (rs.next()) {
				String name = rs.getString("clau");
				String value = rs.getString("valor");

				if (name.equalsIgnoreCase("servidorWeb")){
					servidorWeb = value;
					logger.debug("[Setting servidorWeb] " + value);
				}
				if (name.equalsIgnoreCase("portWeb")){
					portWeb = value;
					logger.debug("[Setting portWeb] " + value);
				}
				if (name.equalsIgnoreCase("contextWebAplicacio")){
					contextWebAplicacio = value;
					logger.debug("[Setting contextWebAplicacio] " + value);
				}
				if (name.equalsIgnoreCase("indexDir")){
					indexDir = value;
					logger.debug("[Setting indexDir] " + value);
				}
				if (name.equalsIgnoreCase("indexDir2")){
					indexDir2 = value;
					logger.debug("[Setting indexDir2] " + value);
				}
				if (name.equalsIgnoreCase("numResultatsPagina")){
					numResultatsPagina = new Integer(value).intValue();
					logger.debug("[Setting numResultatsPagina] " + value);
				}
				if (name.equalsIgnoreCase("idioma")){
					idioma = value;
					logger.debug("[Setting idioma] " + value);
				}
				if (name.equalsIgnoreCase("servidorOrganitzador")){
					servidorOrganitzador = value;
					logger.debug("[Setting servidorOrganitzador] " + value);
				}
				if (name.equalsIgnoreCase("serveiOrganitzador")){
					serveiOrganitzador = value;
					logger.debug("[Setting serveiOrganitzador] " + value);
				}
				if (name.equalsIgnoreCase("sso")){
					sso = value;
					logger.debug("[Setting sso] " + value);
				}
				if (name.equalsIgnoreCase("refreshIndex")){
					refreshIndex = value;
					logger.debug("[Setting refreshIndex] " + value);
				}
				if (name.equalsIgnoreCase("maxItemsRss")){
					maxItemsRss = value;
					logger.debug("[Setting maxItemsRss] " + value);
				}
				if (name.equalsIgnoreCase("numNoticiesPortada")){
					numNoticiesPortada = new Integer(value).intValue();
					logger.debug("[Setting numNoticiesPortada] " + value);
				}

			}


			if (TipusFitxer.isVoid()) {
				TipusFitxer.carregaTipusFitxer();
				//  XMLCollection.loadProperties(nameFile);

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
					myConnection.close();
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
}