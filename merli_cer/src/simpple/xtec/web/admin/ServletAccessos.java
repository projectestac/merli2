package simpple.xtec.web.admin;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import simpple.xtec.dao.OracleAccessosDAO;
import simpple.xtec.web.util.AccessLogObject;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.UtilsCercador;


public class ServletAccessos extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.admin.ServletAccessos.class);
	
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

	
	/**
	 * Creates the csv file
	 */
	
	private String crearFitxer (ArrayList resultats, String tipusResultat) {
	   RandomAccessFile file = null;		
	   String filePath = path + "administracio/csv/";
	   File fileDir = null;
	   AccessLogObject accessLog = null;
	   String nomCercador = "";
	   try {
		  logger.debug("crearFitxer -> in"); 
		  logger.debug("tipusResultat: " + tipusResultat); 
		  logger.debug("resultats: " + resultats);		  
		  logger.debug("filePath: " + filePath);		  
		  fileDir = new File(filePath);
		  fileDir.mkdirs();
		  filePath += "csvExport.csv";
		  logger.debug("file: " + filePath);		  
		  fileDir = new File(filePath);
		  if (fileDir.exists()) {
			  fileDir.delete();
		      }
   		  file = new RandomAccessFile(filePath, "rw");

	 	  // ens situem al final del fitxer Index
		  file.seek(file.length());
		  int i = 1;
		  logger.debug("Writing file....");
		  while (i < resultats.size()) {
			accessLog = (AccessLogObject)resultats.get(i);
			if (accessLog.cercador == 1) {
				nomCercador = "edu365";
			    } else {
				nomCercador = "XTEC";
			    }
		    if (tipusResultat.equals("llistat")) {
		       file.writeBytes(accessLog.cerca + "," + accessLog.data + "," + nomCercador + "\n");
		       }
		    if (tipusResultat.equals("histograma")) {
		       file.writeBytes(accessLog.cerca + "," + accessLog.numCerques + "," + nomCercador + "\n");
		       }
		    i ++;
		    }

		  file.close();
		  logger.debug("File -> " +  file.getFD().toString() );

	   } catch (Exception e) {
	   logger.error(e);	   
	   }
	  logger.debug("crearFitxer -> out");  
	  return filePath; 
	  }
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		String filtreEdu365 = "";
		String filtreXtec = "";
		String tipusResultat = "";	
		String dataIniciCerca = "";
		String dataFinalCerca = "";		
		String nivell = "";
		int nivellInt = 0;
		String operacio = "";
		boolean xtec = false;
		boolean edu365 = false;
		
		RequestDispatcher disp = null;
		ArrayList resultats = null;
		OracleAccessosDAO accessosDAO = null;
		
		try {
		   logger.debug("doPost -> in");	
			
		   if (Configuracio.isVoid()) {
			   Configuracio.carregaConfiguracio();
		       }

		   accessosDAO = new OracleAccessosDAO();
		   operacio = request.getParameter("operacio");
	       filtreEdu365 = request.getParameter("filtreEdu365");
	       logger.debug("filtreEdu365 -> " + filtreEdu365);

	       dataIniciCerca = request.getParameter("dataIniciCerca");
	       dataFinalCerca = request.getParameter("dataFinalCerca");	       
	       nivell = request.getParameter("nivell");
	       nivellInt = (new Integer(nivell)).intValue(); 
	    	   
	       if ((filtreEdu365 != null) && filtreEdu365.equals("edu365")) {
	    	  edu365 = true;
	          }

	       filtreXtec = request.getParameter("filtreXtec");
	       if ((filtreXtec != null) && filtreXtec.equals("xtec")) {
	    	  xtec = true;
	          }
	       logger.debug("filtreXtec -> " + filtreXtec);

	       tipusResultat = request.getParameter("tipusResultat");
	       logger.debug("tipusResultat -> " + tipusResultat);
	       dataIniciCerca = UtilsCercador.girarData(request.getParameter("dataIniciCerca"));
	       logger.debug("dataIniciCerca: " + dataIniciCerca);
	       dataFinalCerca = UtilsCercador.girarData(request.getParameter("dataFinalCerca"));
	       logger.debug("dataFinalCerca: " + dataFinalCerca);	       

	       if (tipusResultat.equals("llistat")) { 
	         resultats = accessosDAO.doSearch (nivellInt, edu365, xtec, dataIniciCerca, dataFinalCerca, false);
	         } else {
	         resultats = accessosDAO.doSearchHistograma (nivellInt, edu365, xtec, dataIniciCerca, dataFinalCerca, false);	 
	         }
	      
	      logger.debug("Redirecting..."); 
		  if (operacio.equals("cercar")) {
  	         
	         disp = ctx.getRequestDispatcher("/administracio/accessos.jsp");
	         request.setAttribute("accessos.resultats", resultats);
	         request.setAttribute("accessos.tipusResultat", tipusResultat);
	         request.setAttribute("accessos.filtreXtec", filtreXtec);
	         request.setAttribute("accessos.filtreEdu365", filtreEdu365);
	         request.setAttribute("accessos.dataIniciCerca", request.getParameter("dataIniciCerca"));
	         request.setAttribute("accessos.dataFinalCerca", request.getParameter("dataFinalCerca"));
	         request.setAttribute("accessos.resultats.numResultats", "" + resultats.size());
	         request.setAttribute("accessos.resultats.nivell", nivell);
	         disp.forward(request, response);
	         }
		  if (operacio.equals("exportar")) {
		       if (tipusResultat.equals("llistat")) { 
		         resultats = accessosDAO.doSearch (nivellInt, edu365, xtec, dataIniciCerca, dataFinalCerca, true);
		         } else {
		         resultats = accessosDAO.doSearchHistograma (nivellInt, edu365, xtec, dataIniciCerca, dataFinalCerca, true);	 
		         }
			 crearFitxer(resultats, tipusResultat); 
			 //response.setContentType("application/vnd.ms-excel");
			 response.setContentType("text/csv");
			 // Provoca un download
			 response.setHeader("Content-disposition", "attachment; filename=accessos.csv");
			 disp = ctx.getRequestDispatcher("/administracio/csv/csvExport.csv");
			 disp.forward(request, response);
		     }
		 } catch (Exception e) {
		 logger.error(e);	
		 } finally {
			try {	
				accessosDAO.disconnect();
				} catch (Exception e) {
				logger.error(e);
				} 	
			}
		 logger.debug("doPost -> out");		 
	}
	
}	