package simpple.xtec.web.admin;

import java.io.IOException;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import simpple.xtec.dao.OracleNoticiesDAO;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.RSSGenerator;
import simpple.xtec.web.util.*;

/**
 * Manages the actions over Noticia model 
 * 
 * @author descuer
 *
 */

public class ManagerNoticies extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.admin.ManagerNoticies.class);

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
		String titol = "";
		String cosNoticia = "";
		String operacio = "";
		String idNoticia = "";
		String publicar = "";
		int noticiaPublica = 0;
		OracleNoticiesDAO noticiesDAO = null;
		RSSGenerator myRssGenerator = null;
		
		try {
			logger.debug("doPost -> in");
			String textEditor = request.getParameter("FCKeditor1");
			logger.debug("textEditor: " + textEditor);
			operacio = request.getParameter("operacio");
			logger.debug("operacio: " + operacio);
		   	titol = request.getParameter("titolNoticia");
			//titol = UtilsCercador.toAcute(titol);
			logger.debug("titolNoticia: " + titol);
		   	cosNoticia = textEditor;
		   	idNoticia = request.getParameter("idNoticia");
			logger.debug("idNoticia: " + idNoticia);
		   	publicar = request.getParameter("publicar");
		   	logger.debug("publicar: " + publicar);
		   	noticiesDAO = new OracleNoticiesDAO();
		   	
		   	if (operacio.equals("afegir")) {
		   	   if ((publicar != null) && publicar.equals("publicar")) {
				   noticiaPublica = 1;
				   }
		   	   noticiesDAO.afegirNoticia(titol, cosNoticia, noticiaPublica);
		   	   }
		   	if (operacio.equals("publicar")) {		   	   
		   	   noticiesDAO.publicarNoticia(idNoticia, 1);
		   	   }
		   	if (operacio.equals("despublicar")) {
		   	   noticiesDAO.publicarNoticia(idNoticia, 0);
			   }
		   	if (operacio.equals("eliminar")) {
		   	   noticiesDAO.eliminarNoticia(idNoticia);
			   }
		   	if (operacio.equals("editar")) {
		   	   noticiesDAO.editarNoticia(idNoticia, titol, cosNoticia);
			   }
		 	 myRssGenerator = new RSSGenerator();

		     logger.debug("Generating rss ..." + path + "rss/noticies.rss");		 	 
		 	 myRssGenerator.generateRssNoticies(path + "rss/noticies.rss");
             logger.debug("rss generated...");

		} catch (Exception e) {
		logger.error(e);	
		} finally {
			try {
				myRssGenerator.disconnect();				
				noticiesDAO.disconnect();
			} catch (Exception e) {
				logger.error(e);
			} 	
		}	
		
		logger.debug("doPost -> out");			
		if (operacio.equals("editar")) {
		   response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/administracio/editarNoticia.jsp?idNoticia=" + idNoticia);
		   } else {
		   response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/administracio/noticies.jsp");			   
		   }
		
	}
	
}	