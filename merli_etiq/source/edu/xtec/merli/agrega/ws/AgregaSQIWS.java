package edu.xtec.merli.agrega.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.NoSuchElementException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import edu.xtec.merli.agrega.AgregaUtils;
import edu.xtec.merli.agrega.objects.AgregaPiF;
import edu.xtec.merli.agrega.objects.AgregaResource;
import edu.xtec.merli.agrega.objects.AgregaSession;
import edu.xtec.merli.agrega.objects.IdResource;
import edu.xtec.merli.agrega.objects.ObjectMerli;
import edu.xtec.merli.agrega.objects.SQIQuery;

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

	
	public static SOAPMessage processEstasActivo(AgregaResource resource,
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
		objm.setOperation(idOperation);

		switch (idOperation) {
		case SQIQUERY:
			context = AGREGA_SQI;
			smRequest = processAgregaSQIQuery((SQIQuery) objm, smRequest, mf, sf);
			response = "sqi";
			break;
		case SETQUERYLANGUAGE:
			context = AGREGA_SESSIONS;
			smRequest = processSetQueryLanguage((SQIQuery) objm,
					smRequest, mf, sf);
			response = "destroySessionResponse";
			break;
		case ESTASACTIVO:
			context = AGREGA_SQI;
			smRequest = processEstasActivo((AgregaResource) objm,
					smRequest, mf, sf);
			response = "estasActivoResponse";
			break;
		}

		try {
			con = scf.createConnection();
	        sm =  sendMessage(context,server);
	        con.close();
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


	public SOAPElement estasActivo(AgregaResource agResource, int servidor) {
		return processOperation(AgregaWS.ESTASACTIVO, agResource, servidor);
	}

	public SOAPElement agregaSQIQuery(SQIQuery sqiQuery, int servidor) {
		return processOperation(AgregaWS.SQIQUERY, sqiQuery, servidor);
	}
	
	public SOAPElement setQueryLanguage(SQIQuery sqiQuery, int servidor) {
		return processOperation(AgregaWS.SETQUERYLANGUAGE, sqiQuery, servidor);
	}
}
