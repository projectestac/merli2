package simpple.xtec.agrega.ws;

import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;

import simpple.xtec.agrega.objects.ObjectMerli;
import simpple.xtec.agrega.objects.SQIQuery;

/*
 * JAppletProva.java
 *
 * Created on 13 de abril de 2005, 13:55
 */

/**
 * 
 * @author eda3s
 */
public class AgregaSQIWS extends AgregaWS {



	/**
	 * Realitza una consulta SQI al node d'Agrega.
	 * 
	 * @param query
	 * @param smRequest
	 * @param mf
	 * @param sf
	 * @return 
	 */
	public static SOAPMessage processAgregaSQIQuery(SQIQuery query, SOAPMessage smRequest,
			MessageFactory mf, SOAPFactory sf) {

		try {
			smRequest = mf.createMessage();

			smRequest.getSOAPPart().getEnvelope().addNamespaceDeclaration(
					"xsd", "http://www.w3.org/2001/XMLSchema");
			smRequest.getSOAPPart().getEnvelope().addNamespaceDeclaration(
					"xsi", "http://www.w3.org/2001/XMLSchemainstance");

			SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope()
					.getBody();
			sbRequest.addChildElement(query.toXml());

			smRequest.saveChanges();
		} catch (Exception e) {
			System.out.println("Ha petat " + e.toString() + "*");

		}
		
		return smRequest;
	}

	
	public static SOAPMessage processEstasActivo(
			SOAPMessage smRequest, MessageFactory mf, SOAPFactory sf) {

		try {
			smRequest = mf.createMessage();

			smRequest.getSOAPPart().getEnvelope().addNamespaceDeclaration(
					"xsd", "http://www.w3.org/2001/XMLSchema");
			smRequest.getSOAPPart().getEnvelope().addNamespaceDeclaration(
					"xsi", "http://www.w3.org/2001/XMLSchemainstance");

			SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope()
					.getBody();
			// Propi de cada operacio
			Name n = sf.createName("estasActivo");

			SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);

			smRequest.saveChanges();
		} catch (Exception e) {
			System.out.println("Ha petat " + e.toString() + "*");

		}
		return smRequest;
	}

	

	public static SOAPMessage processSetQueryLanguage(SQIQuery sqiQuery, SOAPMessage smRequest, MessageFactory mf, SOAPFactory sf) {

        try
        {
            smRequest = mf.createMessage();
           
            smRequest.getSOAPPart().getEnvelope().addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
            smRequest.getSOAPPart().getEnvelope().addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchemainstance");

            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();
            //Propi de cada operacio            
            Name n = sf.createName("setQueryLanguage"); 
                    
            SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);
            sbeRequest.addAttribute(sf.createName("xmnls"), "http://Sesion.servicios.negocio.dri.pode.es");
            
            sbeRequest.addChildElement(sqiQuery.getSessionIDXml());
            sbeRequest.addChildElement(sqiQuery.getQueryLanguageXml());

           smRequest.saveChanges();
        }
        catch (Exception e)
        {
            System.out.println("Ha petat "+e.toString()+"*");
        
        }
        return smRequest;
	}

	/**
	 * Processament d'una operació.
	 * 
	 * @param idOperation
	 *            identificador de la operació a realitzar.
	 * @param objm
	 *            Objecte SOAP que serà enviat en l'execució de la operació.
	 * @param server
	 *            Servidor al que s'accedeix. {LOCAL, TEST, ACCEPT, PRODU}
	 * @return
	 */
	public SOAPElement processOperation(int idOperation, ObjectMerli objm,
			int server) {
		String response = null;
		SOAPMessage sm = null;
		SOAPElement se = null;

		int context = AGREGA;
		if (objm != null)
			objm.setOperation(idOperation);
        	
		switch (idOperation) {
		case SQIQUERY:
			context = AGREGA_SQI;
			smRequest = processAgregaSQIQuery((SQIQuery) objm, smRequest, mf, sf);
			response = "synchronousQueryResponse";
			break;
		case SETQUERYLANGUAGE:
			context = AGREGA_SESSIONS;
			smRequest = processSetQueryLanguage((SQIQuery) objm,
					smRequest, mf, sf);
			response = "setQueryLanguageResponse";
			break;
		case ESTASACTIVO:
			context = AGREGA_SQI;
			smRequest = processEstasActivo(smRequest, mf, sf);
			response = "estasActivoResponse";
			break;
		}

		try {
			con = scf.createConnection();
	        sm =  sendMessage(context,server);
	        con.close();
	        if (objm != null)
	        	se = objm.getResponse(sm, response);
		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			try {
				if (sm != null)
				se = (((SOAPElement) sm.getSOAPBody().getFault()));
			} catch (SOAPException e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return se;
	}


	public static URL getUrl(int context, int server){
	  	URL url = null;
		try{
			switch (server){
				case AgregaWS.LOCAL: url = new URL("http://agrega-int.educacio.intranet/dri-1/services/SrvDRIService"); break;
				case AgregaWS.TEST:	url = new URL("http://ccaa.agrega.indra.es/dri-1/services/SrvSQIService"); break;
				case AgregaWS.ACCEPT:
				case AgregaWS.PRODU:url = new URL("http://redes.agrega.indra.es/dri-1/services/SrvSQIService"); break;
				default: 	url = new URL("http://contenidos.proyectoagrega.es/dri-1/services/SrvSQIService");			
			}
		}catch(Exception e){}
		
		return url;
	}


	public boolean estasActivo(int servidor) {
		SOAPElement se = null;
		SQIQuery siq = new SQIQuery();
		try{
			se = processOperation(AgregaWS.ESTASACTIVO, siq, servidor);
			if ("true".equals(se.getFirstChild().getNodeValue())) 
				return true;
			else 
				return false;
		}catch(Exception e){
			logger.error("Error on estasActivo ->"+e.getMessage());
			if (se !=null)
				logger.equals("response: "+ se.toString());
			
			return false;
		}
	}

	public String agregaSQIQuery(SQIQuery sqiQuery, int servidor) {
		SOAPElement se = null;
		try{
			se = processOperation(AgregaWS.SQIQUERY, sqiQuery, servidor);
	
			return se.getFirstChild().toString();			
		}catch(Exception e){
			logger.error("Error on agregaSQIQuery ->"+e.getMessage());
			if (se !=null)
				logger.equals("response: "+ se.toString());
			
			return "";
		}
	}
	
	public SOAPElement setQueryLanguage(SQIQuery sqiQuery, int servidor) {
		return processOperation(AgregaWS.SETQUERYLANGUAGE, sqiQuery, servidor);
	}
}
