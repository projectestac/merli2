package simpple.xtec.web.admin;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import simpple.xtec.dao.OracleComentarisDAO;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.Indexador;
import simpple.xtec.web.util.RSSGenerator;

/**
 * Manages the add and edit actions 
 * 
 * @author descuer
 *
 */

public class ManagerComentaris extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.admin.ManagerComentaris.class);
	
	ServletContext ctx = null;
	String path = "";	
	
	/**
	 * Loads the database driver 
	 */
	
	public void init(ServletConfig config) throws ServletException {
		try {
		   ctx = config.getServletContext();
		   path = ctx.getRealPath("/");   	    		   
		   } catch (Exception e) {
		   logger.error(e);
		   }
	}	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String comentari = "";
		String titol = "";
		String nomUsuari = "";		
		String idRecurs = ""; 
		int puntuacioInt = 0;
		String puntuacio = "";
		String operacio = "";
		int idComentariInt = 0;
		String idComentari = "";

		
		OracleComentarisDAO comentarisDAO = null;
		RSSGenerator myRssGenerator = null;
		try {
			logger.debug("doPost -> in");
			comentari = request.getParameter("comentari");
			logger.debug("comentari: " + comentari);
			titol = request.getParameter("titol");
			logger.debug("titol: " + titol);			
			nomUsuari = request.getParameter("nomUsuari");
			logger.debug("nomUsuari: " + nomUsuari);			
			idRecurs = request.getParameter("idRecurs");
			logger.debug("idRecurs: " + idRecurs);			
			puntuacio = request.getParameter("puntuacio");
			logger.debug("puntuacio: " + puntuacio);			
			operacio = request.getParameter("operacio");
			logger.debug("operacio: " + operacio);			
			try {
			  puntuacioInt = new Float(puntuacio).intValue();	
			  } catch (Exception e) {
			  logger.error(e);
			  }
	    
			comentarisDAO = new OracleComentarisDAO();
	        if (operacio.equals("afegir")) {
	           comentarisDAO.afegirComentari(nomUsuari, idRecurs, titol, comentari, puntuacioInt);
	           }
	        if (operacio.equals("editar")) {
	           idComentari = request.getParameter("idComentari");
			   try {
			      idComentariInt = new Float(idComentari).intValue();	
				  } catch (Exception e) {
				  logger.error(e);	  			
				  }	           
			   comentarisDAO.editarComentari(nomUsuari, idRecurs, titol, comentari, puntuacioInt, idComentariInt);
	           }
	     logger.debug("Creating rss generator...");
	 	 myRssGenerator = new RSSGenerator();
	     logger.debug("Generating rss ..." + path + "rss/comentaris.rss");	 	 
	     myRssGenerator.generateRssComentaris(path + "rss/comentaris.rss");
	     logger.debug("Generating rss ..." + path + "rss/recurs_" + idRecurs + ".rss");	     
	 	 myRssGenerator.generateRssComentarisRecurs(path + "rss/recurs_" + idRecurs + ".rss", idRecurs);

		} catch (Exception e) {
		logger.error(e);	
		} finally {
			try {	
			   comentarisDAO.disconnect();
			   myRssGenerator.disconnect();			  
			  } catch (Exception e) {
			  logger.error(e);
			  } 	
		}	
     logger.debug("doPost -> out");
	 response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/cerca/fitxaRecurs.jsp?idRecurs=" + idRecurs + "&nomUsuari=" + nomUsuari);
	}
	
}