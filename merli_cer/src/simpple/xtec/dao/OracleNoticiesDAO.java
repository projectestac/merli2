package simpple.xtec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.log4j.Logger;

import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.UtilsCercador;

/**
 * Oracle implementation for the NoticiesDAO interface
 * 
 * @author descuer
 *
 */

public class OracleNoticiesDAO implements NoticiesDAO {

	Connection myConnection = null;
	
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.dao.OracleNoticiesDAO.class);
	
	/**
	 * Constructor
	 */
	
	public OracleNoticiesDAO () {
		try {
			logger.debug("constructor -> in");	
			if (Configuracio.isVoid()) {
				Configuracio.carregaConfiguracio();
			    }
            // Get connection from pool			
			myConnection = UtilsCercador.getConnectionFromPool();
		} catch (Exception e) {
		logger.error(e);
		}
	   logger.debug("constructor -> out");		
	}
	
	/**
	 * Add a new to the db
	 */
	
	public void afegirNoticia (
			String titol,
			String cosNoticia,
			int publicar
	) throws SQLException, Exception {
		
		String insertStatement = "";
		PreparedStatement prepStmt = null;
		
		try {
			logger.debug("afegirNoticia -> in");			
			logger.debug("titol: " + titol);
			logger.debug("cosNoticia: " + cosNoticia);
			logger.debug("publicar: " + publicar);
			
			insertStatement = "INSERT INTO noticies (id, titol, cos, data_creacio, data_edicio, publicat) VALUES (s_noticies.nextVal, ?, ?, ?, ?, ?)";
			prepStmt = myConnection.prepareStatement (insertStatement);
			prepStmt.setString(1, titol);
			prepStmt.setString(2, cosNoticia);

  		    Calendar rightNow = Calendar.getInstance();
			String data_actual_string = UtilsCercador.calculaDataActual (rightNow);
			String hora_actual_string = UtilsCercador.calculaHoraActual (rightNow);
			prepStmt.setString(3, data_actual_string + " " + hora_actual_string);
			prepStmt.setString(4, data_actual_string + " " + hora_actual_string);			
			prepStmt.setInt(5, publicar);
		    
			logger.info("SQL: " + prepStmt);
			prepStmt.executeUpdate(); 
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		} finally {
			prepStmt.close();	       
		}	
	  logger.debug("afegirNoticia -> out");		
	}		

	/**
	 * Edit a new
	 */ 

	public void editarNoticia (
			String idNoticia,
			String titol,
			String cosNoticia 			
	) throws SQLException, Exception {
		
		String insertStatement = "";
		PreparedStatement prepStmt = null;
		
		try {
			logger.debug("editarNoticia -> in");			
			logger.debug("titol: " + titol);
			logger.debug("cosNoticia: " + cosNoticia);
			logger.debug("idNoticia: " + idNoticia);
			
			insertStatement = "UPDATE noticies SET titol=?, cos=?, data_edicio=? WHERE id=?";
			
			prepStmt = myConnection.prepareStatement (insertStatement);
			prepStmt.setString(1, titol);
			prepStmt.setString(2, cosNoticia);
  		    Calendar rightNow = Calendar.getInstance();
			String data_actual_string = UtilsCercador.calculaDataActual (rightNow);
			String hora_actual_string = UtilsCercador.calculaHoraActual (rightNow);
			prepStmt.setString(3, data_actual_string + " " + hora_actual_string);
			prepStmt.setString(4, idNoticia);			
			
		    logger.info("SQL: " + prepStmt);
			
			prepStmt.executeUpdate(); 
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		} finally {
			prepStmt.close();	       
		}
	logger.debug("editarNoticia -> out");
	}		
	
	/**
	 * Publish a new
	 */ 
		
	public void publicarNoticia (
			String idNoticia,
			int publicar
	) throws SQLException, Exception {
		
		String insertStatement = "";
		PreparedStatement prepStmt = null;
		
		try {
			logger.debug("publicarNoticia -> in");			
			logger.debug("idNoticia: " + idNoticia);
			logger.debug("publicar: " + publicar);
			
			insertStatement = "UPDATE noticies SET publicat=?, data_publicacio=? WHERE id=?";
			prepStmt = myConnection.prepareStatement (insertStatement);
			prepStmt.setInt(1, publicar);
  		    Calendar rightNow = Calendar.getInstance();
			String data_actual_string = UtilsCercador.calculaDataActual (rightNow);
			String hora_actual_string = UtilsCercador.calculaHoraActual (rightNow);
			prepStmt.setString(2, data_actual_string + " " + hora_actual_string);			
			prepStmt.setString(3, idNoticia);
		    logger.info("SQL: " + prepStmt);
			
			prepStmt.executeUpdate(); 
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		} finally {
			prepStmt.close();	       
		}	
	logger.debug("publicarNoticia -> out");
	}		
	

	/**
	 * Deletes a new
	 */
	
	public void eliminarNoticia (
			String idNoticia
	) throws SQLException, Exception {
		
		String insertStatement = "";
		PreparedStatement prepStmt = null;
		
		try {

			logger.debug("eliminarNoticia -> in");			
			logger.debug("idNoticia: " + idNoticia);
			
			insertStatement = "DELETE FROM noticies WHERE id=?";
			prepStmt = myConnection.prepareStatement (insertStatement);			
			prepStmt.setString(1, idNoticia);
			logger.info("SQL: " + prepStmt);
			
			prepStmt.executeUpdate(); 
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		} finally {
			prepStmt.close();	       
		}	
		logger.debug("eliminarNoticia -> out");		
	}		

	
	/**
     * Close the db connection
     */ 
	
	public void disconnect () {
       try {
     	   logger.debug("disconnect -> in");
		   myConnection.close();
	   	   logger.debug("disconnect -> out");		   
		  } catch (Exception e) {
		  logger.error(e); 	 
		  }
		}	
	
}