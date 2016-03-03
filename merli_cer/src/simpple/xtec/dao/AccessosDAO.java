package simpple.xtec.dao;

import java.util.ArrayList;

/**
 * Interface for the Accessos model DAO
 * 
 * @author descuer
 *
 */

public interface AccessosDAO {
	public ArrayList doSearch (int nivell, boolean filtreEdu365, boolean filtreXtec, String dataIniciCerca, String dataFinalCerca, boolean fullResults);
	public ArrayList doSearchHistograma (int nivell, boolean filtreEdu365, boolean filtreXtec, String dataIniciCerca, String dataFinalCerca, boolean fullResults);	
}