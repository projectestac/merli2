package simpple.xtec.dao;

import java.sql.SQLException;

/**
 * Interface for the Indexacio model DAO
 * 
 * @author descuer
 *
 */

public interface IndexacioDAO {
	
	public void actualitzaDades (String programacioTemporal) throws SQLException, Exception;
	public void indexarAra () throws SQLException, Exception;
	
}