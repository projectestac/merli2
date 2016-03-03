package edu.xtec.merli.agrega.ws;


import java.net.URL;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;

import edu.xtec.merli.agrega.objects.AgregaResource;
import edu.xtec.merli.agrega.objects.IdResource;


/*
 * JAppletProva.java
 *
 * Created on 13 de abril de 2005, 13:55
 */

/**
 *
 * @author eda3s
 */
public class AgregaMerliWS extends AgregaWS{    
	
	/**
	 * Prepara el missatge a ser enviat. 
	 * 
	 * @param idResource id del recurs a recuperar.
	 * @return 
	 */   
	protected static SOAPMessage processGetResource(IdResource idResource, SOAPMessage smRequest, MessageFactory mf, SOAPFactory sf) {

         try
         {
             smRequest = mf.createMessage();
            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();

             //Propi de cada operacio            
             Name n = sf.createName("getResource"); 
                     
             SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);
             sbeRequest.addChildElement(idResource.toXml());
             

            smRequest.saveChanges();
         }
         catch (Exception e)
         {
             System.out.println("Ha petat "+e.toString()+"*");
         
         }
         return smRequest;
	}    

	
	

	private URL getUrl(int context, int server) {
		URL url = null;
		try {
			switch (server){
				case AgregaWS.LOCAL: url = new URL("http://localhost:8090/merli_ws_melt/merli"); break;
				case AgregaWS.INTEGRACIO: url = new URL("http://localhost:8090/merli_ws/merli"); break;
				case AgregaWS.TEST:	url = new URL("http://xtec-wc.educacio.intranet:7778/e13_merli_ws2/merli");break;
				case AgregaWS.ACCEPT: url = new URL("http://acc.xtec.cat/e13_merli_ws2/merli");break;
				case AgregaWS.PRODU: url = new URL("http://aplitic.xtec.cat/e13_merli_ws2/merli");break;
				default: url = new URL("http://localhost:8090/merli_ws/merli");			
			}
			return url;
		} catch (Exception e) {
			return null;
		}
	}
	
	

	public SOAPElement getResource(IdResource idResource, int servidor) {
		return processOperation(AgregaWS.GETRESOURCE, idResource, servidor);
	}
	

}
