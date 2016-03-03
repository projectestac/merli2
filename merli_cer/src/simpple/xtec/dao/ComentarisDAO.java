package simpple.xtec.dao;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface for the Comentaris model DAO
 * 
 * @author descuer
 *
 */

public interface ComentarisDAO {

	public void afegirComentari (String nomUsuari, String idRecurs, String titol, String comentari, int puntuacio) throws SQLException, Exception;
	public void editarComentari (String nomUsuari, String idRecurs, String titol, String comentari, int puntuacio, int idComentari) throws SQLException, Exception;
	public ArrayList doSearch (int nivell, String usuariCerca, String titolCerca , String recursCerca, String textCerca, String dataIniciCerca, String dataFinalCerca, int suspes);	
	public void eliminarComentari (int idComentari, String idRecurs) throws SQLException, Exception;	
	public void suspendreComentari (int idComentari, String idRecurs) throws SQLException, Exception;	
	public void publicarComentari (int idComentari, String idRecurs) throws SQLException, Exception;	
}