package simpple.xtec.web.admin;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import simpple.xtec.dao.OracleComentarisDAO;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.RSSGenerator;
import simpple.xtec.web.util.UtilsCercador;


public class ServletComentaris extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.admin.ServletComentaris.class);
	
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
		String recursCerca =  "";
		String usuariCerca = "";
		String titolCerca = "";
		String textCerca = "";
	    String tipusCerca = "";
		String dataIniciCerca = "";
		String dataFinalCerca = "";		
		String nivell = "";
		String operacio = "";
        String suspes = "";
        int nivellInt = 0;
		
		RequestDispatcher disp = null;
		ArrayList resultats = null;
		
		OracleComentarisDAO comentarisDAO = null;
		RSSGenerator myRssGenerator = null;
		
		try {
		   logger.debug("doPost -> in");	
		   if (Configuracio.isVoid()) {
			   Configuracio.carregaConfiguracio();
		       }

		   comentarisDAO = new OracleComentarisDAO ();
		   recursCerca = request.getParameter("recursCerca");
		   logger.debug("recursCerca: " + recursCerca);
		   if (recursCerca == null) {
		      recursCerca = ""; 
		      }    
		   
		   usuariCerca = request.getParameter("usuariCerca");
		   logger.debug("usuariCerca: " + usuariCerca);		   
		   if (usuariCerca == null) {
		      usuariCerca = ""; 
		      }

		   textCerca = request.getParameter("textCerca");
		   logger.debug("textCerca: " + textCerca);		   
		   if (textCerca == null) {
		      textCerca = ""; 
		      }

		   titolCerca = request.getParameter("titolCerca");
		   logger.debug("titolCerca: " + titolCerca);		   
		   if (titolCerca == null) {
			   titolCerca = ""; 
		      }
		   		   
		   tipusCerca = request.getParameter("tipusCerca");
		   logger.debug("tipusCerca: " + tipusCerca);
		   if (tipusCerca == null) {
			   tipusCerca = ""; 
		      }

		   operacio = request.getParameter("operacio");
		   logger.debug("operacio: " + operacio);		   
	       dataIniciCerca = request.getParameter("dataIniciCerca");
		   logger.debug("dataIniciCerca: " + dataIniciCerca);	       
	       dataFinalCerca = request.getParameter("dataFinalCerca");
		   logger.debug("dataFinalCerca: " + dataFinalCerca);	       
	       nivell = request.getParameter("nivell");
		   logger.debug("nivell: " + nivell);	       
	       try {
	         nivellInt = new Integer(nivell).intValue();
	         } catch (Exception e) {
	         }

	       dataIniciCerca = UtilsCercador.girarData(request.getParameter("dataIniciCerca"));
	       logger.debug("Data inici: " + dataIniciCerca);
	       dataFinalCerca = UtilsCercador.girarData(request.getParameter("dataFinalCerca"));
	       logger.debug("Data final: " + dataFinalCerca);	       
	       suspes = request.getParameter("suspes");	
	       if (suspes == null) {
	    	   suspes = "";
	           } 
	       logger.debug("Suspes: " + suspes);	       
	       
	       disp = ctx.getRequestDispatcher("/administracio/comentaris.jsp");     
 		   
	       if (operacio.equals("cercar")) {
  	         logger.debug("doing search...");
  	         int suspesInt = 0;
  	         if (suspes.equals("on")) {
  	        	 suspesInt = 1;
  	             }
  	         resultats = comentarisDAO.doSearch (nivellInt, usuariCerca, titolCerca, recursCerca, textCerca, dataIniciCerca, dataFinalCerca, suspesInt);
  	         logger.debug("filling results...");	         
	         request.setAttribute("comentaris.resultats", resultats);
	         request.setAttribute("comentaris.dataIniciCerca", request.getParameter("dataIniciCerca"));
	         request.setAttribute("comentaris.dataFinalCerca", request.getParameter("dataFinalCerca"));
	         request.setAttribute("comentaris.recursCerca", request.getParameter("recursCerca"));
	         request.setAttribute("comentaris.usuariCerca", request.getParameter("usuariCerca"));
	         request.setAttribute("comentaris.textCerca", request.getParameter("textCerca"));
	         request.setAttribute("comentaris.titolCerca", request.getParameter("titolCerca"));	         
	         request.setAttribute("comentaris.tipusCerca", request.getParameter("tipusCerca"));	         
	         request.setAttribute("comentaris.suspes", "" + suspesInt);	         
	         request.setAttribute("comentaris.resultats.numResultats", "" + resultats.size());
	         request.setAttribute("comentaris.resultats.nivell", nivell);
	         }
 		   
 		   if (operacio.equals("eliminar")) {
 	  	     logger.debug("deleting..."); 			   
 			 String idComentari = request.getParameter("idComentari");
 			 String idRecurs = request.getParameter("idRecurs"); 			 
 			 int idComentariInt = new Integer(idComentari).intValue();
 			 comentarisDAO.eliminarComentari(idComentariInt, idRecurs); 
 	 		 myRssGenerator = new RSSGenerator();
 	  	     logger.debug("generating rss..." + path + "rss/comentaris.rss"); 	 		 
 	 		 myRssGenerator.generateRssComentaris(path + "rss/comentaris.rss");
 		     }

 		   if (operacio.equals("suspendre")) {
 	 	  	 logger.debug("suspendre..."); 			   
 			 String idComentari = request.getParameter("idComentari");
 			 String idRecurs = request.getParameter("idRecurs"); 			 
 			 int idComentariInt = new Integer(idComentari).intValue();
 			 comentarisDAO.suspendreComentari(idComentariInt, idRecurs);
	 		 myRssGenerator = new RSSGenerator();
 	  	     logger.debug("generating rss..." + path + "rss/comentaris.rss");
	 	 	 myRssGenerator.generateRssComentaris(path + "rss/comentaris.rss");
 		     }
 		   
 		   if (operacio.equals("publicar")) {
 	 	 	 logger.debug("publish..."); 			   
 	 		 String idComentari = request.getParameter("idComentari");
 			 String idRecurs = request.getParameter("idRecurs"); 	 		 
 	 		 int idComentariInt = new Integer(idComentari).intValue();
 	 		 comentarisDAO.publicarComentari(idComentariInt, idRecurs); 
 	 		 }
 		   
		 disp.forward(request, response);

		 } catch (Exception e) {
		 logger.error(e);	
		 } finally {
			try {	
				comentarisDAO.disconnect();
				if (myRssGenerator != null) {
				  myRssGenerator.disconnect();		
				  }
				} catch (Exception e) {
				logger.error(e);
				} 	
			}	
	}
	
}	