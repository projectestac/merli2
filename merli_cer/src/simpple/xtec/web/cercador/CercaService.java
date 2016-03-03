package simpple.xtec.web.cercador;

import java.sql.Connection;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;

import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.QueryGenerator;
import simpple.xtec.web.util.ResultGenerator;
import simpple.xtec.web.util.UtilsCercador;

/**
 * Public interface for the search web services
 * 
 * @author descuer
 *
 */

public class CercaService {
	
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.cercador.CercaService.class);
	
	String indexPrincipalDir = "";	
	
	/**
	 * Reads the location for the index
	 */	
	
	public CercaService() {	
	   if (Configuracio.isVoid()) {
		   Configuracio.carregaConfiguracio(); 
		   }
	   indexPrincipalDir = Configuracio.indexDir;		
	   }
	

	public String doEdu365Search (String textCerca, String nivellEducatio, String areaCurricular, int nivell) {
		String resultatXML = "";
		BooleanQuery bq = null;
		QueryGenerator queryGenerator = null;
		ResultGenerator resultGenerator = null;
		Hits hits = null;		
		IndexSearcher indexPrincipal = null;	
		Connection myConnection = null;
		try {
			myConnection = UtilsCercador.getConnectionFromPool();			
			logger.debug("Index principal: " + indexPrincipalDir);
			indexPrincipal = new IndexSearcher(indexPrincipalDir);		
			queryGenerator = new QueryGenerator();
			resultGenerator = new ResultGenerator(myConnection);	
			
			Hashtable parameters = new Hashtable();
			
			bq = queryGenerator.getQuery(textCerca, parameters);
			
            logger.debug(bq.toString());
			hits = indexPrincipal.search(bq);
			resultatXML = resultGenerator.generateXML (hits, "simple", nivell, textCerca);			
		 } catch (Exception e) {
		 logger.error(e);	
		 } finally {
			try {	
			  indexPrincipal.close();
			  myConnection.close();
			  } catch (Exception e) {
			  logger.error(e);	  
			  }
		 }		  
	    return resultatXML;	 
	}

	
	public String doXtecSimpleSearch (String textCerca, String nivellEducatio, String areaCurricular, int nivell) {
		String resultatXML = "";
		BooleanQuery bq = null;
		QueryGenerator queryGenerator = null;
		ResultGenerator resultGenerator = null;
		Hits hits = null;		
		IndexSearcher indexPrincipal = null;
		Connection myConnection = null;
		try {
			myConnection = UtilsCercador.getConnectionFromPool();			
			logger.debug("Index principal: " + indexPrincipalDir);
			indexPrincipal = new IndexSearcher(indexPrincipalDir);		
			queryGenerator = new QueryGenerator();
			resultGenerator = new ResultGenerator(myConnection);
			
			Hashtable parameters = new Hashtable();
			
			bq = queryGenerator.getQuery(textCerca, parameters);
            logger.debug(bq.toString());
			hits = indexPrincipal.search(bq);
			resultatXML = resultGenerator.generateXML (hits, "simple", nivell, textCerca);			
		 } catch (Exception e) {
		 logger.error(e);	
		 } finally {
			try {	
			  myConnection.close();	
			  indexPrincipal.close();
			  } catch (Exception e) {
			  logger.error(e);
			  }
		 }		  
	    return resultatXML;	 
	}
	
	public String doXtecCompletaSearch (String textCerca, String nivellEducatiu, String areaCurricular, String idioma, String ambit, String dretsReproduccio, String autor, String llicencia, String editorial, String dataPublicacio, String durada, String dataCatalogacio, String altresRepositoris, String paraulesClau, String destinatari, String tipusRecurs, int nivell) {
		String resultatXML = "";
		BooleanQuery bq = null;
		QueryGenerator queryGenerator = null;
		ResultGenerator resultGenerator = null;
		Hits hits = null;		
		IndexSearcher indexPrincipal = null;
		Connection myConnection = null;
		try {
			myConnection = UtilsCercador.getConnectionFromPool();			
			logger.debug("Index principal: " + indexPrincipalDir);
			indexPrincipal = new IndexSearcher(indexPrincipalDir);		
			queryGenerator = new QueryGenerator();
			resultGenerator = new ResultGenerator(myConnection);		
			// public BooleanQuery getQuery (String cercaText, String cercaAutor, String cercaIdioma, String cercaDretsReproduccio, String cercaDestinatari, String cercaNivellEducatiu, String cercaTipusRecurs, String cercaKeywords) {
			
			Hashtable parameters = new Hashtable();

			parameters.put("autorCerca", autor);
			parameters.put("idiomaCerca", idioma);
			parameters.put("dretsReproduccioCerca", dretsReproduccio);
			parameters.put("destinatariCerca", destinatari);
			parameters.put("llicenciaCerca", llicencia);
			parameters.put("editorialCerca", editorial);
			parameters.put("nivellEducatiu", nivellEducatiu);
			parameters.put("tipusRecurs", tipusRecurs);
			parameters.put("keywords", paraulesClau);
			parameters.put("areaCurricular", areaCurricular);		
			
			bq = queryGenerator.getQuery(textCerca, parameters);
            logger.debug(bq.toString());
			hits = indexPrincipal.search(bq);
			resultatXML = resultGenerator.generateXML (hits, "completa", nivell, textCerca);			
		 } catch (Exception e) {
		 logger.error(e);	
		 } finally {
			try {	
			  indexPrincipal.close();
			  myConnection.close();
			  } catch (Exception e) {}
		 }		  
	    return resultatXML;
	}
	
	
}