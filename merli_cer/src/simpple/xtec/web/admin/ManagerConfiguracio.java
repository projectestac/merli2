package simpple.xtec.web.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import simpple.xtec.dao.OracleConfiguracioSistemaDAO;
import simpple.xtec.dao.OracleUsuarisDAO;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.TipusFitxer;

/**
 * Manages the actions over Usuari model 
 * 
 * @author descuer
 *
 */

public class ManagerConfiguracio extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.admin.ManagerConfiguracio.class);
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {	
		String novaClau = "";
		String valor = "";		
		String idConfiguracio = "";		
		int idConfiguracioInt = 0;
		String operacio = "";		

		OracleConfiguracioSistemaDAO configuracioDAO = null;
		try {

			logger.debug("doPost -> in");			
			operacio = request.getParameter("operacio");
			logger.debug("operacio: " + operacio);			
			configuracioDAO = new OracleConfiguracioSistemaDAO();
		   	if (operacio.equals("afegir")) {
		   	   novaClau = request.getParameter("novaClau");
			   logger.debug(novaClau);		   	   
		   	   valor = request.getParameter("valor");
			   logger.debug(valor);		   	   
		       configuracioDAO.afegirDadaConfiguracio(novaClau, valor);
		   	   }
		   	if (operacio.equals("eliminar")) {
		   	   idConfiguracio = request.getParameter("idConfiguracio");		   	   
		       idConfiguracioInt = new Integer(idConfiguracio).intValue();
		   	   configuracioDAO.eliminarDadaConfiguracio(idConfiguracioInt);
			   }
		   	if (operacio.equals("modificar")) {
		   	   idConfiguracio = request.getParameter("idConfiguracio");
		   	   idConfiguracioInt = new Integer(idConfiguracio).intValue();			   
			   logger.debug(idConfiguracio);			   
			   novaClau = request.getParameter("novaClau");
			   logger.debug(novaClau); 
			   valor = request.getParameter("valor");
			   logger.debug(valor);		   	   
			   configuracioDAO.modificarDadaConfiguracio(idConfiguracioInt, novaClau, valor);
		   	   }
		   	
        Configuracio.carregaConfiguracio();
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
	   if (Configuracio.contextWebAplicacio.equals("")) {
	      response.sendRedirect("administracio/configuracio.jsp");
	      } else {
		      response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/administracio/configuracio.jsp");
	      }

	}
	
}	