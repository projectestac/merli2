package simpple.xtec.web.util;

import java.sql.Connection;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;

public class SoapManager {


	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.util.SoapManager.class);
	
	
	String serverURL = "";
    String servicePath = "";
    String indexPrincipalDir = "";
	
	public SoapManager (String indexPrincipalDir) {
		if (Configuracio.isVoid()) {
			Configuracio.carregaConfiguracio();
		}
		serverURL = Configuracio.servidorOrganitzador;
		servicePath = Configuracio.serveiOrganitzador;	
		String indexDir1 = Configuracio.indexDir;
		String indexDir2 = Configuracio.indexDir2;
	//	indexPrincipalDir = UtilsCercador.getIndexActual(indexDir1, indexDir2);
	    this.indexPrincipalDir = indexPrincipalDir;
	     logger.debug("Index actual: "  + indexPrincipalDir); 
		
	 }
	
	
	  public boolean doCall (String nomUsuari, String id, String sheetId) {
		boolean okProcess = true;
		String titol = "";
		String descripcio = "";
		String linkFitxa = "";
		String linkRecurs = "";
		String mimeType = "";

		// IndexSearcher mySearcher = null;
		FitxaRecurs fitxaRecurs = null;
		Connection myConnection = null;
		try {  
		    myConnection = UtilsCercador.getConnectionFromPool();
		    logger.debug("indexPrincipalDir -> " + indexPrincipalDir);
	      //  mySearcher = new IndexSearcher(indexPrincipalDir);
	        fitxaRecurs = new FitxaRecurs(myConnection);
	        Document luceneDocument = fitxaRecurs.getDocument (indexPrincipalDir, id);
			titol = luceneDocument.get("titol");
			logger.info("Titol: " + titol);
			descripcio = luceneDocument.get("descripcio");
			logger.info("Descripcio: " + descripcio);			
			linkRecurs = luceneDocument.get("url");
			logger.info("linkRecurs: " + linkRecurs);			
			linkFitxa = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/";
			linkFitxa += "cerca/fitxaRecurs.jsp";
			linkFitxa += "?idRecurs=" + id;			
			logger.info("linkFitxa: " + linkFitxa);			
			mimeType = luceneDocument.get("lom@technical@format");
			logger.info("mimeType: " + mimeType);			
			
			
			
			
			
		  okProcess = doCall (nomUsuari, id, sheetId, titol, descripcio, linkFitxa, linkRecurs, mimeType);
	      } catch (Exception e) {
	      logger.error(e); 	  
	      } finally {
	      try {	  
	    /*	 if (mySearcher != null) { 
	           mySearcher.close();
	    	   }*/
	    	if (myConnection != null) { 
	           myConnection.close();
	    	   }
	        } catch (Exception e) {
	        }
	      }
	    return okProcess;
	  }
	  public boolean doCall (String nomUsuari, String id, String sheetId, String titol, String descripcio, String linkFitxa, String linkRecurs, String mimeType) {
		  boolean okProcess = true;
	      try {
            logger.debug("Creating factory...");
	        SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
	        logger.debug("Creating connection...");
	        SOAPConnection soapConnection = factory.createConnection(); 
	        logger.debug("Creating message factory...");
	        MessageFactory msgFactory = MessageFactory.newInstance();
	        logger.debug("Creating message...");	        
	        SOAPMessage msg = msgFactory.createMessage();
	        logger.debug(msg.CHARACTER_SET_ENCODING);
	        msg.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "utf-8");
	        logger.debug(msg.CHARACTER_SET_ENCODING);	        
	        logger.debug("Getting envelope....");
	        // add headers - these are targets for the message
	        SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
	        logger.debug("Getting header...");
	        SOAPHeader headers = envelope.getHeader();

	        SOAPHeaderElement target;
	        logger.debug("Getting body....");
/******/
	        SOAPBody body = envelope.getBody();
	        logger.debug("Creating infoOrganitzador...");	        

	        SOAPElement messageElem =  body.addBodyElement(envelope.createName("infoOrganitzador"));
	        logger.debug("Creating nomUsuari...");
	        
	        SOAPElement nomUsuariElem =  messageElem.addChildElement(envelope.createName("nomUsuari"));
	        logger.debug("Adding nomUsuari...." + nomUsuari);
	        nomUsuariElem.addTextNode(nomUsuari);
	        logger.debug("Checking sheetId...." + sheetId);	        
            
	        if ((sheetId != null) && !sheetId.equals("")) {
	          SOAPElement sheetIdElem =  messageElem.addChildElement(envelope.createName("sheetId"));
	          sheetIdElem.addTextNode(sheetId);
	          }
	        logger.debug("Creating infoRecurs...");	        
	        
	        
	        SOAPElement infoRecursElem =  messageElem.addChildElement(envelope.createName("infoRecurs"));
	        logger.debug("Creating id....");
	        SOAPElement idElem =  infoRecursElem.addChildElement(envelope.createName("id"));
	        logger.debug("Setting id..." + id);
	        idElem.addTextNode(id);
	        logger.debug("Creating titol...");
	        SOAPElement titolElem =  infoRecursElem.addChildElement(envelope.createName("titol"));
	        logger.debug("Setting titol... " + titol);
	        titolElem.addTextNode(titol);
	        logger.debug("Creating descripcio....");
	        SOAPElement descripcioElem =  infoRecursElem.addChildElement(envelope.createName("descripcio"));
	        logger.debug("Setting descripcio...." + descripcio);
	        descripcioElem.addTextNode(descripcio);
	        logger.debug("Creating linkFitxa....");
	        SOAPElement linkFitxaElem =  infoRecursElem.addChildElement(envelope.createName("linkFitxa"));
	        logger.debug("Setting linkFitxa...." + linkFitxa);
	        linkFitxaElem.addTextNode(linkFitxa);
	        logger.debug("Creating linkRecurs....");
	        SOAPElement linkRecursElem =  infoRecursElem.addChildElement(envelope.createName("linkRecurs"));
	        logger.debug("Setting linkRecurs...." + linkRecurs);
	        linkRecursElem.addTextNode(linkRecurs);
	        logger.debug("Creating mimeType....");	        
	        SOAPElement mimeTypeElem =  infoRecursElem.addChildElement(envelope.createName("mimeType"));
	        logger.debug("Setting mimeType...." + mimeType);        
	        mimeTypeElem.addTextNode(mimeType);
	                
	                

	        logger.debug(msg.toString());
	        
	        // send the message
	        String myUrl = serverURL + servicePath;
	        logger.info("Sending message to " + myUrl + " ...");
	      //  provider.send(msg, url);
	        //provider.send(msg);


	        SOAPMessage response = soapConnection.call(msg, myUrl); 
            logger.info("Message sent..");
	        SOAPEnvelope envelopeResponse = response.getSOAPPart().getEnvelope();

	        SOAPBody bodyResponse = envelopeResponse.getBody();
	        logger.debug(bodyResponse);
	       // logger.error(bodyResponse);
	      }	catch (Exception e) {
		  logger.error(e);	
	      okProcess = false;
	      }
	    return okProcess;  
	    }

	
}