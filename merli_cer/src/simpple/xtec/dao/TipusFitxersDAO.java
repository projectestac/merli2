package simpple.xtec.dao;

import java.sql.SQLException;

/**
 * Interface for the Usuari model DAO
 * 
 * @author descuer
 *
 */

public interface TipusFitxersDAO {

	public void afegirTipusFitxers (String nomGrup, String mimeType) throws SQLException, Exception;
	public void eliminarTipusFitxers (int idTipusFitxers) throws SQLException, Exception;
	public void modificarTipusFitxers(int idTipusFitxers, String nouGrup, String mimeType) throws SQLException, Exception; 
}