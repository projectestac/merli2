package simpple.xtec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;

import simpple.xtec.web.util.ComentariObject;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.UtilsCercador;

/**
 * Oracle implementation for the ComentarisDAO interface
 * 
 * @author descuer
 *
 */

public class OracleComentarisDAO implements ComentarisDAO {
	
	Connection myConnection = null;
	
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.dao.OracleComentarisDAO.class);
	
	/**
	 * Constructor
	 */
		
	public OracleComentarisDAO () {
		try {
		   logger.debug("constructor -> in");			
		   if (Configuracio.isVoid()) {
		      Configuracio.carregaConfiguracio();
		      }
	       // Get connection from pool		   
		   myConnection = UtilsCercador.getConnectionFromPool();
	       logger.debug("constructor -> out");			   
		} catch (Exception e) {
		logger.error(e);
		}
	}
	
	/**
	 * Add a comment to de db
	 */
	
	public void afegirComentari (			
			String nomUsuari,
			String idRecurs,
			String titol,
			String comentari,
			int puntuacio
	) throws SQLException, Exception {
		
		String insertStatement = "";
		PreparedStatement prepStmt = null;
		
		try {
			logger.debug("afegirComentari -> in");
			logger.debug("nomUsuari:" + nomUsuari);	
			logger.debug("idRecurs:" + idRecurs);	
			logger.debug("titol:" + titol);	
			logger.debug("comentari:" + comentari);	
			logger.debug("puntuacio:" + puntuacio);				
			
			insertStatement = "INSERT INTO comentaris (id, xtec_username, recurs_id, comentari, puntuacio, data_creacio, data_edicio, suspens, titol) VALUES (s_comentaris.nextVal, ?, ?, ?, ?, ?, ?, 0, ?)";
							
			prepStmt = myConnection.prepareStatement (insertStatement);
			prepStmt.setString(1, nomUsuari);
			prepStmt.setString(2, idRecurs);
			prepStmt.setString(3, comentari);
			prepStmt.setInt(4, puntuacio);
  		    Calendar rightNow = Calendar.getInstance();
			String data_actual_string = UtilsCercador.calculaDataActual (rightNow);
			String hora_actual_string = UtilsCercador.calculaHoraActual (rightNow);
			logger.debug("Dia: " + data_actual_string + "/ " + hora_actual_string);
			prepStmt.setString(5, data_actual_string + " " + hora_actual_string);
			prepStmt.setString(6, data_actual_string + " " + hora_actual_string);			
			prepStmt.setString(7, titol);
			logger.info("SQL: " + prepStmt);			

			prepStmt.executeUpdate(); 
			marcarRecursModificat(idRecurs);
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		} finally {
			prepStmt.close();	       
		}	
		logger.debug("afegirComentari -> out");		
	}		

	/**
	 * Edit a comment 
	 */
	
	public void editarComentari (
			String nomUsuari,
			String idRecurs,
			String titol,
			String comentari,
			int puntuacio,
			int idComentari
	) throws SQLException, Exception {
		
		String insertStatement = "";
		PreparedStatement prepStmt = null;
		
		try {
			logger.debug("editarComentari -> in");
			logger.debug("nomUsuari:" + nomUsuari);	
			logger.debug("idRecurs:" + idRecurs);	
			logger.debug("titol:" + titol);	
			logger.debug("comentari:" + comentari);
			logger.debug("puntuacio:" + puntuacio);
			logger.debug("idComentari:" + idComentari);			
			
			insertStatement = "UPDATE comentaris SET xtec_username=?, recurs_id=?, comentari=?, puntuacio=?, data_edicio=?, titol=? WHERE id=?";

			prepStmt = myConnection.prepareStatement (insertStatement);
			prepStmt.setString(1, nomUsuari);
			prepStmt.setString(2, idRecurs);
			prepStmt.setString(3, comentari);
			prepStmt.setInt(4, puntuacio);
  		    Calendar rightNow = Calendar.getInstance();
			String data_actual_string = UtilsCercador.calculaDataActual (rightNow);
			String hora_actual_string = UtilsCercador.calculaHoraActual (rightNow);
			logger.debug("Dia: " + data_actual_string + "/ " + hora_actual_string);
			prepStmt.setString(5, data_actual_string + " " + hora_actual_string);
			
			prepStmt.setString(6, titol);		
			prepStmt.setInt(7, idComentari);			
            logger.debug("SQL: " + prepStmt);
			prepStmt.executeUpdate(); 
			marcarRecursModificat(idRecurs);			
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		} finally {
			try {
			  prepStmt.close();
			  } catch (Exception e) {}
		}	
		logger.debug("editarComentari -> out");		
	}		
	
	/**
	 * Do the search, and return an arraylist filled with instances of ComentariObject class
	 */
		
	public ArrayList doSearch (int nivell, String usuariCerca, String titolCerca, String recursCerca, String textCerca, String dataIniciCerca, String dataFinalCerca, int suspens) {
		String selectStatement = "";
		String selectStatementCount = "";
		PreparedStatement prepStmt = null;
		PreparedStatement prepStmtCount = null;		
		ResultSet rs = null;
		ResultSet rsCount = null;
		ArrayList resultats =null;
		try {
			logger.debug("doSearch -> in");			
			resultats = new ArrayList();
            usuariCerca = usuariCerca.replaceAll("'", "''");
            titolCerca = titolCerca.replaceAll("'", "''");
            recursCerca = recursCerca.replaceAll("'", "''");
            textCerca = textCerca.replaceAll("'", "''");
            dataIniciCerca = dataIniciCerca.replaceAll("'", "''");
            dataFinalCerca = dataFinalCerca.replaceAll("'", "''");            
            
			selectStatement = "SELECT * FROM (select c.id, c.titol, puntuacio, recurs_id, xtec_username, data_edicio, suspens, comentari, ROW_NUMBER() OVER (ORDER BY data_edicio DESC) R from comentaris c,recursos r where c.recurs_id=r.id ";		
			selectStatementCount = "SELECT count(*) from comentaris c,recursos r where c.recurs_id=r.id ";

			if (!textCerca.equals("")) {
		       selectStatement += " and lower(c.comentari) like lower('%" + textCerca + "%')";        
		       selectStatementCount += " and lower(c.comentari) like lower('%" + textCerca + "%')";
		       }

		    if (!titolCerca.equals("")) {
		       selectStatement += " and lower(c.titol) like lower('%" + titolCerca + "%')";        
		       selectStatementCount += " and lower(c.titol) like lower('%" + titolCerca + "%')";
		       }

		    if (!usuariCerca.equals("")) {
		       selectStatement += " and lower(c.xtec_username) like lower('%" + usuariCerca + "%')";            
		       selectStatementCount += " and lower(c.xtec_username) like lower('%" + usuariCerca + "%')";
		       }       

		    if (!recursCerca.equals("")) {
		       selectStatement += " and (lower(r.id) like lower('%" + recursCerca + "%') OR lower(r.titol) like lower('%" + recursCerca + "%')";            
		       selectStatementCount += " and (lower(r.id) like lower('%" + recursCerca + "%') OR lower(r.titol) like lower('%" + recursCerca + "%')";
		       }			
					
			if (!dataIniciCerca.equals("")) {
			  selectStatement += " AND c.data_edicio>'" + dataIniciCerca + "' ";
			  selectStatementCount += " AND c.data_edicio>'" + dataIniciCerca + "' ";
			  }

			if (!dataFinalCerca.equals("")) {
			  selectStatement += " AND c.data_edicio<'" + dataFinalCerca + "' ";
			  selectStatementCount += " AND c.data_edicio<'" + dataFinalCerca + "' ";
			  }

			selectStatement += " AND suspens=" + suspens;
			selectStatementCount += " AND suspens=" + suspens;
		
			selectStatement += ") WHERE R>=" + nivell * Configuracio.numResultatsPagina + " AND R <=" + ((nivell + 1) * Configuracio.numResultatsPagina);

			logger.info("SQL: " + selectStatement);
			logger.info("SQL: " + selectStatementCount);
			
			prepStmt = myConnection.prepareStatement (selectStatement);

			prepStmtCount = myConnection.prepareStatement (selectStatementCount);
			rsCount = prepStmtCount.executeQuery();
			rsCount.next();
            
			// First position of array -> number of results
			resultats.add(new Integer(rsCount.getInt(1)));
			
			rs = prepStmt.executeQuery();
			
			while (rs.next()) {
				ComentariObject comentariObject = new ComentariObject();
				comentariObject.setComentari((String)rs.getString("comentari"), 135);
				logger.debug("Getting id..");
				comentariObject.id = (long)rs.getLong("id");			
				comentariObject.titol = rs.getString("titol");
				logger.debug("Getting puntuacio..");				
				comentariObject.puntuacio = rs.getInt("puntuacio");
				logger.debug("Getting recurs_id..");				
				comentariObject.idRecurs = rs.getString("recurs_id");
				comentariObject.nomUsuari = rs.getString("xtec_username");
				comentariObject.dataEdicio = rs.getString("data_edicio");			
				comentariObject.suspens = rs.getInt("suspens");
				resultats.add(comentariObject);
			}
            			
		  } catch (Exception e) {
		  logger.error(e);	
		  } finally {
		  try {	  
			if (rs != null) {
			  rs.close();
			  }
			if (rsCount != null) {
			  rsCount.close();
			  }			
			if (prepStmt != null) {
			  prepStmt.close();
			  }
			if (prepStmtCount != null) {
			  prepStmtCount.close();
			  }

		    } catch (Exception e ) {
		    	
		    }
		  }
    	logger.debug("numResultats:" + resultats.size());
		logger.debug("doSearch -> out");				  
		return resultats;
	}
	
    /**
     * Deletes a comment from the db
     */
	
	public void eliminarComentari (int idComentari, String idRecurs) throws SQLException{
        
        Statement stmt = null;
        String query = "";
        try {
    	  logger.debug("eliminarComentari -> in");
    	  stmt = myConnection.createStatement();
    	  query = "DELETE FROM comentaris WHERE id=" + idComentari;
    	  logger.info("SQL: " + query);
          stmt.executeUpdate (query);
          marcarRecursModificat(idRecurs);
        } catch (SQLException e) {
         logger.error(e);
        } catch (Exception e) {
        logger.error(e);
        } finally {
        if (stmt != null) {
           stmt.close();
           }
        }
  	  logger.debug("eliminarComentari -> out");        
     }
	
	/**
	 * Tags a comment as invalid
	 */
	
	public void suspendreComentari (int idComentari, String idRecurs) throws SQLException{
        
        Statement stmt = null;
        String query = "";
        try {
      	  logger.debug("suspendreComentari -> in");
    	  stmt = myConnection.createStatement();
    	  query = "UPDATE comentaris SET suspens=1 WHERE id=" + idComentari;
    	  logger.info("SQL: " + query);
          stmt.executeUpdate (query);        	
          marcarRecursModificat(idRecurs);
        } catch (SQLException e) {
        logger.error(e);
        } catch (Exception e) {
        logger.error(e);       
        } finally {
        if (stmt != null) {
           stmt.close();
           }
        }
    	logger.debug("suspendreComentari -> out");        
     }

	/**
	 * Publish a comment
	 */
	
	public void publicarComentari (int idComentari, String idRecurs) throws SQLException{
        
        Statement stmt = null;
        String query = "";
        try {
           logger.debug("publicarComentari -> in");
    	   stmt = myConnection.createStatement();
    	   query = "UPDATE comentaris SET suspens=0 WHERE id=" + idComentari;
     	   logger.info("SQL: " + query);
           stmt.executeUpdate (query);        	
           marcarRecursModificat(idRecurs);
        } catch (SQLException e) {
         logger.error(e);        
        } catch (Exception e) {
         logger.error(e);
        } finally {
        if (stmt != null) {
           stmt.close();
           }
        }
    	logger.debug("publicarComentari -> out");        
     }

	
	/**
	 * Set a resource as modified
	 */
	
	public void marcarRecursModificat (String idRecurs) throws SQLException{
        
        Statement stmt = null;
        String query = "";
        try {
           logger.debug("marcarRecursModificat -> in");
    	   stmt = myConnection.createStatement();
    	   query = "UPDATE recursos SET modificat=1 WHERE id='" + idRecurs + "'";
     	   logger.info("SQL: " + query);
           stmt.executeUpdate (query);

        } catch (SQLException e) {
         logger.error(e);
        } catch (Exception e) {
         logger.error(e);
        } finally {
        try {	
          if (stmt != null) {
            stmt.close();
            }
          } catch (Exception e) {}
        }
    	logger.debug("marcarRecursModificat -> out");
    	        
     }
	
	
	/*
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