package simpple.xtec.indexador.util;

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
    static final Logger logger = Logger.getLogger(simpple.xtec.indexador.util.SoapManager.class);

    String serverURL = "";
    String servicePath = "";

    public SoapManager() {
        if (Configuracio.isVoid()) {
            Configuracio.carregaConfiguracio();
        }
        //serverURL = "http://aplitic.xtec.cat/e13_";
        if("80".equals(Configuracio.portWSmerli)){
            serverURL = "http://" + Configuracio.servidorWSmerli + "/";
        }else{
            serverURL = "http://" + Configuracio.servidorWSmerli + ":" + Configuracio.portWSmerli + "/";
        }
        
        servicePath = Configuracio.nameLomWS;
    }

    public String doCall(String idContingut) {
        // boolean okProcess = true;
        String bodyText = "";
        try {
            logger.debug("doCall 1");
            SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
            logger.debug("doCall 2");
            SOAPConnection soapConnection = factory.createConnection();
            logger.debug("doCall 3");
            MessageFactory msgFactory = MessageFactory.newInstance();
            logger.debug("doCall 4");
            SOAPMessage msg = msgFactory.createMessage();

            logger.debug("doCall 5");
            // add headers - these are targets for the message
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();

            logger.debug("doCall 6");

            SOAPHeader headers = envelope.getHeader();
            logger.debug("doCall 7");
            SOAPHeaderElement target;
            logger.debug("doCall 8");
            SOAPBody body = envelope.getBody();

            logger.debug("doCall 9");
            SOAPElement message = body.addBodyElement(envelope.createName("getResource"));
            logger.debug("doCall 10");
            SOAPElement idResource = message.addChildElement(envelope.createName("idResource"));
            logger.debug("doCall 11 " + idContingut);
            SOAPElement identifier = idResource.addChildElement(envelope.createName("identifier"));
            logger.debug("doCall 12");
            identifier.addTextNode(idContingut);
            SOAPElement type = idResource.addChildElement(envelope.createName("type"));
            logger.debug("doCall 13");
            type.addTextNode("lom");

            // send the message
            String myUrl = serverURL + servicePath;
            logger.debug("Sending message to " + myUrl + " ...");
            logger.debug(msg.getSOAPPart().getEnvelope().getBody());
	        //  provider.send(msg, url);  
            //provider.send(msg);
            //logger.debug("See the result at the server console.");
            SOAPMessage response = soapConnection.call(msg, myUrl);
            logger.debug("doCall 14");
            SOAPEnvelope envelopeResponse = response.getSOAPPart().getEnvelope();
            logger.debug("doCall 15");
            SOAPBody bodyResponse = envelopeResponse.getBody();
            logger.debug(bodyResponse);
            bodyText = bodyResponse.toString();

        } catch (Exception e) {
            logger.error(e);
            // okProcess = false;
        }
        return bodyText;
    }

    public String doCallDUC() {
        // boolean okProcess = true;
        String bodyText = "";
        try {
            logger.info("doCall 1");
            SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
            logger.info("doCall 2");
            SOAPConnection soapConnection = factory.createConnection();
            logger.info("doCall 3");
            MessageFactory msgFactory = MessageFactory.newInstance();
            logger.info("doCall 4");
            SOAPMessage msg = msgFactory.createMessage();
            logger.info("doCall 5");
            // add headers - these are targets for the message
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            logger.info("doCall 6");
            SOAPHeader headers = envelope.getHeader();
            logger.info("doCall 7");
            SOAPHeaderElement target;
            logger.info("doCall 8");
            SOAPBody body = envelope.getBody();
            logger.info("doCall 9");
            SOAPElement message = body.addBodyElement(envelope.createName("getDUC"));
            logger.info("doCall 10");

            // send the message
            String myUrl = serverURL + "e13_merli_ws/duc";
            myUrl = serverURL + "merli_ws2/duc";
            logger.info("Sending message to " + myUrl + " ...");
            logger.info(msg.getSOAPPart().getEnvelope().getBody());
	        //  provider.send(msg, url);  
            //provider.send(msg);
            logger.debug("See the result at the server console.");
            SOAPMessage response = soapConnection.call(msg, myUrl);
            logger.info("doCall 11");
            SOAPEnvelope envelopeResponse = response.getSOAPPart().getEnvelope();
            logger.info("doCall 12");
            SOAPBody bodyResponse = envelopeResponse.getBody();
            logger.debug(bodyResponse);
            bodyText = bodyResponse.toString();

        } catch (Exception e) {
            logger.error(e);
            // okProcess = false;
        }
        return bodyText;
    }

}
