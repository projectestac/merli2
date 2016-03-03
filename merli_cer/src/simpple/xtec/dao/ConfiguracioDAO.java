package simpple.xtec.dao;

import java.sql.SQLException;

/**
 * Interface for the Configuracio model DAO
 * 
 * @author descuer
 *
 */

public interface ConfiguracioDAO {
	
	public void actualitzaPes (String tipusCercador, String nomPes, float valorPes) throws SQLException, Exception;	
	public void actualitzaFragments (String tipusCercador, int longitudDescripcio, int resultatsPagina, int nombreNovetats, int tempsVidaNovetat) throws SQLException, Exception;	
}