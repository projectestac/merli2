package simpple.xtec.dao;

import java.sql.SQLException;

/**
 * Interface for the Usuari model DAO
 * 
 * @author descuer
 *
 */

public interface UsuarisDAO {

	public void afegirUsuari (String usuari) throws SQLException, Exception;
	public void eliminarUsuari (int idUsuari) throws SQLException, Exception;
	
}