package simpple.xtec.web.cercador;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;

import simpple.xtec.web.analisi.EducacioAnalyzer;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.QueryGenerator;
import simpple.xtec.web.util.ResultGenerator;
import simpple.xtec.web.util.UtilsCercador;

/**
 * Implements the rss for the results page
 * 
 * @author descuer
 *
 */

public class CercaRSS extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.cercador.CercaRSS.class);
	
	String indexPrincipalDir = "";	
	
	/**
	 * Reads the location for the index
	 */
	
	public void init() {	
	   if (Configuracio.isVoid()) {
		   Configuracio.carregaConfiguracio(); 
		   }
	   indexPrincipalDir = UtilsCercador.getIndexActualFromDB();	
	   }
		
	/**
	 * 
	 */
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
	
		String resultatRSS = "";
		Query bq = null;
		QueryGenerator queryGenerator = null;
		ResultGenerator resultGenerator = null;
		Hits hits = null;		
		IndexSearcher indexPrincipal = null;
		Connection myConnection = null;
		
		String textCerca = "";
		String nivellEducatiu = "";		
		String areaCurricular = "";
		
		try {
			logger.debug("doGet -> in");
			indexPrincipalDir = UtilsCercador.getIndexActualFromDB();
			request.setCharacterEncoding("UTF-8");
		    response.setContentType("text/xml;charset=utf-8");
			// response.setContentType("application/rdf+xml;charset=utf-8");
			
			textCerca = request.getParameter("textCerca");	
			logger.debug("textCerca: " + textCerca);
			
			nivellEducatiu = request.getParameter ("nivell_educatiu");
			logger.debug("nivellEducatiu: " + nivellEducatiu);
			
			areaCurricular = request.getParameter ("area_curricular");
			logger.debug("areaCurricular: " + areaCurricular);

			myConnection = UtilsCercador.getConnectionFromPool();			
			logger.debug("Index principal: " + indexPrincipalDir);
			
			indexPrincipal = new IndexSearcher(indexPrincipalDir);		
			queryGenerator = new QueryGenerator();
			resultGenerator = new ResultGenerator(myConnection);		

			
			Hashtable parameters = ServletCerca.getParametersFromRequest(request);

			logger.debug("Generating query");
			bq = queryGenerator.getQuery(textCerca, parameters);

			if (bq.toString().equals("")) {
				logger.debug("match all docs...");
				QueryParser myQueryParser = new QueryParser("bugSort", new EducacioAnalyzer());
				bq = myQueryParser.parse("(+bugSort:1)");
			}
			
			String filtreRecurs = (String) parameters.get("filtreRecurs");
			if ((filtreRecurs != null) && !filtreRecurs.trim().equals("") && !filtreRecurs.trim().equals("null")) {
				bq = queryGenerator.getQueryFiltreRecurs(filtreRecurs, bq);
			}

            logger.info("Query: " + bq.toString());

			hits = indexPrincipal.search(bq, new Sort(new SortField("dataPublicacio", true)));

			logger.debug("Generating rss...");
			resultatRSS = resultGenerator.generateRSS (myConnection, hits, textCerca, nivellEducatiu, areaCurricular);
			PrintWriter out = response.getWriter();
				
			out.println(resultatRSS);
			out.flush();			
		 } catch (Exception e) {
		 logger.error(e);	
		 } finally {
			try {	
			  indexPrincipal.close();
			  myConnection.close();
			  } catch (Exception e) {}
		 }		  
	logger.debug("doGet -> out");
	}
	
	
}