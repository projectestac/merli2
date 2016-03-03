package simpple.xtec.web.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import simpple.xtec.dao.OracleUsuarisDAO;
import simpple.xtec.web.util.Configuracio;

/**
 * Manages the actions over Usuari model 
 * 
 * @author descuer
 *
 */

public class ManagerUsuaris extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.admin.ManagerUsuaris.class);
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {	
		String nouUsuari = "";
		String idUsuari = "";		
		int idUsuariInt = 0;
		String operacio = "";		

		OracleUsuarisDAO usuarisDAO = null;
		try {
			logger.debug("doPost -> in");			
			operacio = request.getParameter("operacio");
			logger.debug("operacio: " + operacio);			
			usuarisDAO = new OracleUsuarisDAO();
		   	if (operacio.equals("afegir")) {
		   	   nouUsuari = request.getParameter("nouUsuari");
		   	   usuarisDAO.afegirUsuari(nouUsuari);
		   	   }
		   	if (operacio.equals("eliminar")) {
		   	   idUsuari = request.getParameter("idUsuari");
		   	   idUsuariInt = new Integer(idUsuari).intValue();
		   	   usuarisDAO.eliminarUsuari(idUsuariInt);
			   }

		} catch (Exception e) {
		logger.error(e);	
		} finally {
			try {	
				usuarisDAO.disconnect();
			} catch (Exception e) {
				logger.error(e);
			}
		}
	   logger.debug("doPost -> out");		
	   response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/administracio/usuaris.jsp");			   

	}
	
}	