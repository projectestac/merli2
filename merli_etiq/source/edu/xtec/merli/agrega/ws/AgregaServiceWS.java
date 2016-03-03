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
public class AgregaServiceWS extends AgregaWS {



	/**
	 * Envia un objecte Agrega empaquetat al repositori.
	 * 
	 * @param resource
	 */
	protected static void processSendResource(AgregaResource resource,
			SOAPMessage smRequest, MessageFactory mf, SOAPFactory sf) {

		try {
			smRequest = mf.createMessage();
			SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope()
					.getBody();

			// Propi de cada operacio
			Name n = sf.createName("getResource");

			SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);
			sbeRequest.addChildElement(resource.toXml());

			smRequest.saveChanges();
		} catch (Exception e) {
			System.out.println("Ha petat " + e.toString() + "*");

		}
	}

	protected static SOAPMessage processPresentarAlmacenar(AgregaPiF pif,
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
			Name n = sf.createName("presentarAlmacenar");
			SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);

			//Afegim l'usuario i la clave.
			sbeRequest.addChildElement(pif.toUsuarioXml());
			sbeRequest.addChildElement(pif.toClaveXml());
			
			DataSource ds = new ByteArrayDataSource(new FileInputStream(pif.getFile()), null); 
			DataHandler dh = new DataHandler(ds);
			
			AttachmentPart atpart = smRequest.createAttachmentPart(dh);
			atpart.setMimeHeader("Content-Transfer-Encoding", "binary");
			atpart.setContentType("application/octet-stream");
			atpart.setContentId(pif.getIdentificador());

			smRequest.addAttachmentPart(atpart);
			//Associem l'atachment al PiF.
			SOAPElement sePif = sf.createElement(sf.createName("pif"));
			sePif.addAttribute(sf.createName("href"), "cid:"+atpart.getContentId());
			sbeRequest.addChildElement(sePif);
			
			smRequest.saveChanges();
		} catch (Exception e) {
			System.out.println("AGsWS!! sHa petat " + e.toString() + "*");

		}
		return smRequest;
	}
	

	protected static SOAPMessage processSolicitarEntregarSession(AgregaPiF pif, SOAPMessage smRequest, MessageFactory mf, SOAPFactory sf) {

		try {
			smRequest = mf.createMessage();

			smRequest.getSOAPPart().getEnvelope().addNamespaceDeclaration(
					"xsd", "http://www.w3.org/2001/XMLSchema");
			smRequest.getSOAPPart().getEnvelope().addNamespaceDeclaration(
					"xsi", "http://www.w3.org/2001/XMLSchemainstance");

			SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope()
					.getBody();
			// Propi de cada operacio
			Name n = sf.createName("solicitarEntregarSesion");
			SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);

			//Afegim l'usuario i la clave.
			sbeRequest.addChildElement(pif.getAgregaSession().getSessionIDXml());
			sbeRequest.addChildElement(pif.getMecXml());
			
			smRequest.saveChanges();
		} catch (Exception e) {
			System.out.println("AGsWS-processSolicitarEntregarSession sHa petat " + e.toString() + "*");

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
		case SENDRESOURCE:
			AgregaServiceWS.processSendResource((AgregaResource) objm, smRequest, mf,
					sf);
			response = "sendResourceResponse";
			break;
		case PRESENTARALMACENAR:
			smRequest = AgregaServiceWS.processPresentarAlmacenar((AgregaPiF) objm,
					smRequest, mf, sf);
			response = "presentarAlmacenarResponse";
			break;
		case SOLICITARENTREGARSESSION:
			smRequest = AgregaServiceWS.processSolicitarEntregarSession((AgregaPiF) objm,
					smRequest, mf, sf);
			response = "solicitarEntregarSesionResponse";
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

	

	private URL getUrl(int context, int server) {
		URL url = null;
		try {
			switch (server) {
			case AgregaWS.LOCAL: url = new URL("agrega-int.educacio.intranet/dri-1/services/SrvDRIService"); break;
			case AgregaWS.TEST:	 url = new URL("http://ccaa.agrega.indra.es/dri-1/services/SrvDRIService"); break;
			case AgregaWS.INTEGRACIO: url = new URL("agrega-int.educacio.intranet/dri-1/services/SrvDRIService"); break;
			case AgregaWS.ACCEPT:
			case AgregaWS.PRODU:
			default:
				url = new URL("http://contenidos.proyectoagrega.es/dri-1/services/SrvDRIService");
			}
			return url;
		} catch (Exception e) {
			return null;
		}
	}

	
	
	

	public SOAPElement sendResource(AgregaResource aResource, int servidor) {
		return processOperation(AgregaWS.SENDRESOURCE, aResource, servidor);
	}
	
	public boolean presentarAlmacenar(AgregaPiF aPif, int servidor) {
		try{
			SOAPElement se = processOperation(AgregaWS.PRESENTARALMACENAR, aPif, servidor);
			return (se==null) || !se.hasChildNodes();
		}catch (Exception e){
			return false;
		}
	}

	public boolean solicitarEntregarSesion(AgregaPiF aPif, int servidor) {
		try{
			SOAPElement se = processOperation(AgregaWS.SOLICITARENTREGARSESSION, aPif, servidor);
			return "true".equals(se.getFirstChild().getNodeValue());
		}catch (Exception e){
			return false;
		}
	}
}
