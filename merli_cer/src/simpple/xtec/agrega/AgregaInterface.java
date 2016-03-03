package simpple.xtec.agrega;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.soap.SOAPElement;

import org.apache.log4j.Logger;

import simpple.xtec.agrega.objects.AgregaSession;
import simpple.xtec.agrega.objects.SQIQuery;
import simpple.xtec.agrega.ws.AgregaSQIWS;
import simpple.xtec.agrega.ws.AgregaSessionsWS;
import simpple.xtec.agrega.ws.AgregaWS;
import simpple.xtec.web.util.Configuracio;


public class AgregaInterface {
	static final Logger logger = Logger.getLogger(simpple.xtec.agrega.AgregaInterface.class);
	
	private static final int SERVIDOR = AgregaWS.TEST;
	

	public static boolean estasActivo(){
		try{
		AgregaSQIWS aws = new AgregaSQIWS();

		return aws.estasActivo(SERVIDOR);
		
		}catch(Exception e){
			logger.error("Agrega is not responding..");
			
			return false;
		}
	}
	
	
	public static ArrayList askAgrega(String query){

		SQIQuery sqiQuery = new SQIQuery();
		
		//Crea la query a ser enviada
		sqiQuery.setQueryStatement(query);
		
		//Creem una nova sessió
		sqiQuery.setTargetSessionID(getSessionID(Configuracio.userAgrega, Configuracio.paswAgrega).getSessionID());
		
		
		AgregaSQIWS aws = new AgregaSQIWS();
		String xmlResponse = aws.agregaSQIQuery(sqiQuery , SERVIDOR);
		
		return AgregaUtils.parseResults(xmlResponse);
	}

	
	
	private static AgregaSession getSessionID(){
		return getSessionID(null,null);
	}
	
	private static AgregaSession getSessionID(String user, String password){
		String targetSessionID="";
		AgregaSessionsWS aws = new AgregaSessionsWS();

		AgregaSession aSession = new AgregaSession(user, password);
		aws.createSession(aSession , SERVIDOR);
	
		return aSession;
	}
	
	
}
