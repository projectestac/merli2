package simpple.xtec.indexador.main;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;

import simpple.xtec.indexador.util.Configuracio;
import simpple.xtec.indexador.util.DucBuilder;
import simpple.xtec.indexador.util.Recomanacions;
import simpple.xtec.indexador.util.Utils;

/**
 * Class called from the main script
 * 
 * @author descuer
 *NO SE USA!!!!!
 */

public class IndexadorTest {
	
	//	Logger 	
	static Logger logger = Logger.getLogger(simpple.xtec.indexador.main.IndexadorTest.class);
		
	// Main method
	public static void main (String[] args) {	    
	     HttpClient clientWS = null;
	     HttpClient client = null;	     
	     MerliHarvester merliHarvester = null;
	     boolean processOk = false;
	     try {	       
	    	logger.debug("main -> in"); 
	        clientWS = Utils.createHttpClient(30000);
	        client = Utils.createHttpClient(5000);

	        if (Configuracio.isVoid()) {
	        	Configuracio.carregaConfiguracio();
	        }
	        Recomanacions recomanacions = new Recomanacions(); 
	        //recomanacions.loadRecomanacions();
	        recomanacions.finalize();
//	        if (Configuracio.refreshDUC.equals("si")) {
//	          DucBuilder ducBuilder = new DucBuilder();
//	          ducBuilder.updateDUC();
//	          ducBuilder.finalize();
//	          }
	        merliHarvester = new MerliHarvester();
	        merliHarvester.clearFoundLogIndexacio();
	        logger.debug("doHarvesting");
	        processOk = merliHarvester.doHarvesting (clientWS, client);
	        
	        logger.debug("processOk -> " + processOk);
            if (processOk) {
               logger.debug("callSearcher..");	
	           merliHarvester.callSearcher(client);
	           merliHarvester.clearFoundRecursos();
	           merliHarvester.syncFound();
               } else {
               logger.debug("cleanIndexDirectory..");
              merliHarvester.cleanIndexDirectory();	   
               }
	        logger.debug("main -> out");           
		 } catch (Exception e) {
         logger.error(e);
		 } 
	}
	
}