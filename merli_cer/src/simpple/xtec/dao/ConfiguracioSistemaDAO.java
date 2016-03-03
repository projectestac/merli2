package simpple.xtec.dao;

import java.sql.SQLException;

/**
 * Interface for the Usuari model DAO
 * 
 * @author descuer
 *
 */

public interface ConfiguracioSistemaDAO {

	public void afegirDadaConfiguracio (String novaClau, String valor) throws SQLException, Exception;
	public void eliminarDadaConfiguracio (int idConfiguracio) throws SQLException, Exception;
	public void modificarDadaConfiguracio (int idConfiguracio, String novaClau, String valor) throws SQLException, Exception;	
}