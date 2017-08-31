package simpple.xtec.web.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

/**
 * Stores the current state of 'configuracio' table, to be used in runtime.
 * 
 * @author descuer
 *
 */

public class ConfiguracioFragments {

	/**
	* Posició 0 -> Cercador edu365
	* Posició 1 -> Cercador xtec
	**/
	
/*	static public int[] highlightFragmentSizeInBytes;
	static public int[] maxNumFragmentsRequired;
	static public String[] fragmentSeparator;
	static public int[] maxSizeFragment;
	static public int[] resultatsPagina;
*/
	static public int[] numResultatsPerPagina;
	static public int[] longitudDescripcio;
	
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.util.ConfiguracioFragments.class);

	public static boolean isVoid () {
		if (numResultatsPerPagina == null) {
			return true;
		    }
		return false;
	}
	
	public static void initInfo () {
		if (numResultatsPerPagina == null) {
			numResultatsPerPagina = new int[2];
		    }
		if (longitudDescripcio == null) {
			longitudDescripcio = new int[2];
		    }
/*		if (fragmentSeparator == null) {
			fragmentSeparator = new String[2];
		    }
		if (maxSizeFragment == null) {
			maxSizeFragment = new int[2];
		    }
		if (resultatsPagina == null) {
			resultatsPagina = new int[2];
		    }
	*/	
	}
	
	
	/**
	 * Load the current values from the table
	 * 
	 * @param myConnection database connection to be used
	 */         
	
	public static void loadConfiguracio (Connection myConnection, String tipusCercador) throws SQLException{

		Statement stmt = null;
		ResultSet rs = null;
		String sql = "";
		int indexConfiguracio = -1;
		try {  			
			initInfo ();
			logger.debug("Loading 'configuracio'");
            if (tipusCercador.equals("edu365")) {
            	indexConfiguracio = 0;
            	sql = "select * from config_cerca where cercador_id=1";            	
                }
            if (tipusCercador.equals("xtec")) {
            	indexConfiguracio = 1;
            	sql = "select * from config_cerca where cercador_id=2";            	
                }

			if (tipusCercador.equals("altres")) {
				indexConfiguracio = 2;
				sql = "select * from config_cerca where cercador_id=3";
			}

            stmt = myConnection.createStatement();
            
            logger.debug(sql);
            rs = stmt.executeQuery(sql);
/*            if (!rs.next()) {
               sql = "insert into configuracio VALUES (10, 10, 10, 5, '...',10,10,10,10,100,5,'0')";
               logger.debug(sql);               
               stmt.executeUpdate(sql);
               sql = "select * from configuracio where tipusCercador='" + tipusCercador + "'";
               logger.debug(sql);               
               rs = stmt.executeQuery(sql);
               rs.next();
               }
*/
             
            rs.next();           
            numResultatsPerPagina [indexConfiguracio] = rs.getInt("max_resultats_pag");
            longitudDescripcio [indexConfiguracio] = rs.getInt("long_desc");            
	/*		highlightFragmentSizeInBytes[indexConfiguracio] = rs.getInt("highlightFragmentSizeInBytes");
			logger.debug("highlightFragmentSizeInBytes: " + highlightFragmentSizeInBytes);                  
			maxNumFragmentsRequired[indexConfiguracio] = rs.getInt("maxNumFragmentsRequired");
			logger.debug("maxNumFragmentsRequired: " + maxNumFragmentsRequired);
			fragmentSeparator[indexConfiguracio] = rs.getString("fragmentSeparator");
			logger.debug("fragmentSeparator: " + fragmentSeparator);
			maxSizeFragment[indexConfiguracio]  = rs.getInt("maxSizeFragment");
			logger.debug("maxSizeFragment: " + maxSizeFragment);				
			resultatsPagina[indexConfiguracio] = rs.getInt("resultatsPaginaPanell");
			logger.debug("resultatPaginaPanell: " + resultatsPagina);
*/
			logger.debug("'configuracio' loaded");			
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