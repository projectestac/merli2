package simpple.xtec.web.admin;

import java.io.IOException;
import java.sql.*;

import org.apache.log4j.Logger;

import simpple.xtec.dao.OracleConfiguracioDAO;
import simpple.xtec.web.util.Configuracio;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet used to update informaction from the 'configuracio' table
 * 
 * @author descuer
 *
 */

public class ServletConfiguracio extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.admin.ServletConfiguracio.class);
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		

		String tipusCercador = "";
		float pes_title = (float)0.0; 
		float pes_description = (float)0.0;		
		float pes_text = (float)0.0;			
		float pes_keywords = (float)0.0;		
		
		OracleConfiguracioDAO configuracioDAO = null;		
		try {
			logger.debug("doPost -> in");
			tipusCercador = request.getParameter("tipusCercador");
			logger.debug("tipusCercador: " + tipusCercador);
						
			String longitudDescripcio = request.getParameter("longitudDescripcio");
			logger.debug("longitudDescripcio: " + longitudDescripcio);
			Integer longitudDescripcioInt = new Integer(longitudDescripcio);

			String resultatsPagina = request.getParameter("resultatsPagina");
			logger.debug("resultatsPagina: " + resultatsPagina);
			Integer resultatsPaginaInt = new Integer(resultatsPagina);

			String nombreNovetats = request.getParameter("nombreNovetats");
			logger.debug("nombreNovetats: " + nombreNovetats);
			Integer nombreNovetatsInt = new Integer(nombreNovetats);
			
			String tempsVidaNovetat = request.getParameter("tempsVidaNovetat");
			logger.debug("tempsVidaNovetat: " + tempsVidaNovetat);
			Integer tempsVidaNovetatInt = new Integer(tempsVidaNovetat);

			configuracioDAO = new OracleConfiguracioDAO();
			configuracioDAO.actualitzaFragments (tipusCercador, longitudDescripcioInt.intValue(), resultatsPaginaInt.intValue(), nombreNovetatsInt.intValue(), tempsVidaNovetatInt.intValue() );
            // update info on runtime			
			
			String pes_titleString = request.getParameter("pes_titol");
			logger.debug("pes_titleString: " + pes_titleString);
			if (pes_titleString != null) {			
   			   pes_title = (new Float(pes_titleString)).floatValue();
			   }
			configuracioDAO.actualitzaPes (tipusCercador, "titol" ,pes_title);
			
			String pes_descriptionString = request.getParameter("pes_descripcio");
			logger.debug("pes_descriptionString: " + pes_descriptionString);
			if (pes_descriptionString != null) {			
			   pes_description = (new Float(pes_descriptionString)).floatValue();
			   }
			configuracioDAO.actualitzaPes (tipusCercador, "descripcio" ,pes_description);			
			
 	        String pes_textString = request.getParameter("pes_text");
			logger.debug("pes_textString: " + pes_textString);
			if (pes_textString != null) {			
			   pes_text = (new Float(pes_textString)).floatValue();
			   }
			configuracioDAO.actualitzaPes (tipusCercador, "text" ,pes_text);
			
			String pes_keywordsString = request.getParameter("pes_keywords");
			logger.debug("pes_keywordsString: " + pes_keywordsString);
			if (pes_keywordsString != null) {			
			   pes_keywords = (new Float(pes_keywordsString)).floatValue();
			   }
			configuracioDAO.actualitzaPes (tipusCercador, "keywords" ,pes_keywords);

		   // update info on runtime
			logger.debug("reloading....");
			configuracioDAO.reload(tipusCercador);

			
		} catch (SQLException e) {
			logger.error(e);             
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {	
				configuracioDAO.disconnect();
			} catch (Exception e) {
				logger.error(e);
			} 	
		}	
		logger.debug("doPost -> out");		
        if (tipusCercador.equals("edu365")) {
		    response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/administracio/cercadorEdu365.jsp");
		} else if(tipusCercador.equals("altres")){
			response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/administracio/cercadorAltres.jsp");
		} else {
            response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/administracio/cercadorXtec.jsp");            	
            }
	}
	
	
}
