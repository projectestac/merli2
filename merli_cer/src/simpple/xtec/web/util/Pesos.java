package simpple.xtec.web.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * Stores the current state of 'pesos' table, to be used in runtime.
 * 
 * @author descuer
 *
 */

public class Pesos {

	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.util.Pesos.class);
			
	static public float[] pes_title;
	static public float[] pes_description;
	static public float[] pes_text;
	static public float[] pes_keywords;
	

	
	public static void initInfo () {
		if (pes_title == null) {
			pes_title = new float[2];
		    }
		if (pes_description == null) {
			pes_description = new float[2];
		    }
		if (pes_text == null) {
			pes_text = new float[2];
		    }
		if (pes_keywords == null) {
			pes_keywords = new float[2];
		    }
		
	}

	/**
	 * Load the current values from the table
	 * 
	 * @param myConnection database connection to be used
	 */   
	
	public static void loadPesos (Connection myConnection, String tipusCercador) throws SQLException{
		Statement stmt = null;
		ResultSet rs = null;
		String sqlQuery = "";

		int indexConfiguracio = -1;
		try {
			initInfo ();
			logger.debug("Loading 'pesos'");
            if (tipusCercador.equals("edu365")) {
            	indexConfiguracio = 0;
                }
            if (tipusCercador.equals("xtec")) {
            	indexConfiguracio = 1;
                }
			
			stmt = myConnection.createStatement();
			sqlQuery = "select * from pesos where tipusCercador='" + tipusCercador + "'";
			logger.debug(sqlQuery);
			rs = stmt.executeQuery(sqlQuery);

			while (rs.next()){                            
				pes_title[indexConfiguracio] = rs.getFloat("pes_title");
				pes_description[indexConfiguracio] = rs.getFloat("pes_hottext");
				pes_text[indexConfiguracio] = rs.getFloat("pes_text");  
				pes_keywords[indexConfiguracio] = rs.getFloat("pes_keywords");
			}
			logger.debug("'pesos' loaded");			
		} catch (SQLException e) {
			logger.error(e);
		} catch (Exception e) {
			logger.error(e);
		} finally {
		     if (rs != null) {	
			rs.close();
		        }
		     if (stmt != null) {   
			stmt.close();
		        }
		}
	}

	
}