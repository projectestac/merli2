package simpple.xtec.dao;

import java.sql.SQLException;

/**
 * Interface for the Noticia model DAO
 * 
 * @author descuer
 *
 */

public interface NoticiesDAO {

	public void afegirNoticia (String titol, String cosNoticia, int publicar) throws SQLException, Exception;	
	public void editarNoticia (String idNoticia, String titol, String cosNoticia) throws SQLException, Exception;
	public void publicarNoticia (String idNoticia, int publicar) throws SQLException, Exception;
	public void eliminarNoticia (String idNoticia) throws SQLException, Exception;	
}