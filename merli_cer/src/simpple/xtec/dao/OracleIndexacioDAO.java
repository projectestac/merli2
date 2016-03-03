package simpple.xtec.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.UtilsCercador;

/**
 * Oracle implementation for the IndexacioDAO interface
 *
 * @author descuer
 *
 */
public class OracleIndexacioDAO implements IndexacioDAO {

    Connection myConnection = null;

    // logger
    static final Logger logger = Logger.getLogger(simpple.xtec.dao.OracleIndexacioDAO.class);

    /**
     * Constructor
     */
    public OracleIndexacioDAO() {
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
     * Updates the info of the 'configuracio' table
     */
    public void actualitzaDades(String programacioTemporal) throws SQLException, Exception {

        String updateStatement = "";
        PreparedStatement prepStmt = null;

        try {
            logger.debug("actualitzaDades -> in");
            updateStatement = "UPDATE config_indexacio SET ordre_cron=?";
            prepStmt = myConnection.prepareStatement(updateStatement);
            prepStmt.setString(1, programacioTemporal);
            logger.info("SQL: " + prepStmt);
            prepStmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            prepStmt.close();
        }
        logger.debug("actualitzaDades -> out");
    }

    /**
     * Force the creation of a new index
     */
    public void indexarAra() throws SQLException, Exception {

        String updateStatement = "";
        PreparedStatement prepStmt = null;

        try {
            logger.debug("indexarAra -> in");
            updateStatement = "UPDATE config_indexacio SET indexacio_inmediata=1";
            prepStmt = myConnection.prepareStatement(updateStatement);
            logger.info("SQL: " + prepStmt);
            prepStmt.executeUpdate();
            logger.debug("indexarAra -> out");
        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        } finally {
            prepStmt.close();
        }

    }

    /**
     * Close the db connection
     */
    public void disconnect() {
        try {
            logger.debug("disconnect -> in");
            myConnection.close();
            logger.debug("disconnect -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
