package edu.xtec.merli.ws;

import java.sql.SQLException;
import java.util.Iterator;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.merli.ws.objects.ElementDUC;
import edu.xtec.merli.ws.objects.IdElement;
import edu.xtec.merli.ws.objects.ListDUC;
import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.SemanticException;

import org.apache.log4j.Logger;

/**
 * 
 * @author jlpatricio
 * @version 1.0.0
 */

public class ServletDuc extends ServletMain {

	private static final String GETELEMENTDUC = "GetElement";
	private static final String GETDUC = "GetDUC";
	private static final String GETNIVELLS = "GetLevels";
	private static final Object GETLEVELELEMENTSDUC = "GetLevelElements";
	
	static Logger logger = Logger.getLogger(ServletDuc.class);

	public SOAPMessage processMessage(SOAPMessage smRequest) {
		// TODO Auto-generated method stub
		// return processMessage(smRequest,1);
		logger.debug("processMessage");
		logger.debug("smRequest-> "+smRequest);

		SOAPMessage smResponse = null;
		String sQuery =null;
		try {
			SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();

			
			SOAPBodyElement sbeRequest = null;

			Iterator it = sbRequest.getChildElements();

			while (it.hasNext()) {
				try {
					sbeRequest = (SOAPBodyElement) it.next();
				} catch (Exception e) {
				}
			}

			sQuery = sbeRequest.getElementName().getLocalName();
			

			String[] j = smRequest.getMimeHeaders().getHeader("host");
			printLog(sQuery+"-INIT",j[0],"WSDUC");

			// getElementDUC
			if (GETELEMENTDUC.equals(sQuery)){
				logger.debug("IN>>>ServletDuc.processMessage-> action="+sQuery);
				//OLD -> Naseq [casting problem]
				smResponse = getElementDUC((SOAPElement)sbeRequest.getChildElements(sf.createName("idElement")).next());
				logger.debug("OUT>>>ServletDuc.processMessage-> action="+sQuery);
				//smResponse.writeTo(System.out);
			}
			// getLevelElementsDUC
			if (GETLEVELELEMENTSDUC.equals(sQuery)){
				logger.debug("IN>>>ServletDuc.processMessage-> action="+sQuery);
				logger.debug(sbeRequest.getChildElements(sf.createName("idElement")).next());
				//OLD -> Naseq [casting problem]
				smResponse = getLevelElementsDUC((SOAPElement)sbeRequest.getChildElements(sf.createName("idElement")).next());
				logger.debug("OUT>>>ServletDuc.processMessage-> action="+sQuery);
				//smResponse.writeTo(System.out);
			}
//			 getElementDUC
			if (GETDUC.equals(sQuery)){
				logger.debug("IN>>>ServletDuc.processMessage-> action="+sQuery);
				smResponse = getDUC();
				//smResponse.writeTo(System.out);
				logger.debug("OUT>>>ServletDuc.processMessage-> action="+sQuery);
			}
			// getElementDUC
			if (GETNIVELLS.equals(sQuery)){
				logger.debug("IN>>>ServletDuc.processMessage-> action="+sQuery);
				smResponse = getLevels();
				//smResponse.writeTo(System.out);
				logger.debug("OUT>>>ServletDuc.processMessage-> action="+sQuery);
			}
			printLog(sQuery+"-END","Server","WSDUC");
			// else createError(sbeRequest);
		}

		catch (SOAPException se) {
			logger.debug("SOAPException: "+se);
			se.printStackTrace();
			return internalError(se,sQuery);
		} catch (MerliDBException ioe) {
			logger.debug("MerliDBException: "+ioe);
			ioe.printStackTrace();
			return internalError(ioe,sQuery);		
		} catch (Exception ioe) {
			logger.debug("Exception: "+ioe);
			ioe.printStackTrace();
			return internalError(ioe,sQuery);
		}

		return smResponse;

	}

	
	//OLD -> Naseq [casting problem]
	private SOAPMessage getElementDUC(SOAPElement sbeRequest) throws SOAPException, MerliDBException, SQLException, SemanticException {
		logger.debug("getElementDUC");
		WSMerliBD wsdbd = new WSMerliBD();
		SOAPMessage smResponse = null;

		IdElement idElement = new IdElement(sbeRequest);
		logger.debug("idElement-> "+idElement);
		ElementDUC elementDUC = null;

		elementDUC = wsdbd.getElementDUC(idElement);
				
		smResponse = createResponse("getElement", elementDUC);

		return smResponse;
	}
	
	//OLD -> Naseq [casting problem]
	private SOAPMessage getLevelElementsDUC(SOAPElement sbeRequest) throws SOAPException, MerliDBException, SQLException, SemanticException {
		logger.debug("getLevelElementsDUC");
		WSMerliBD wsdbd = new WSMerliBD();
		SOAPMessage smResponse = null;

		IdElement idElement = new IdElement(sbeRequest);
		logger.debug("idElement-> "+idElement);
		ListDUC listDUC = null;

		listDUC =  wsdbd.getLevelElementsDUC(idElement);
				
		smResponse = createResponse("getLevelElements", listDUC);
		logger.debug("smResponse: "+smResponse);
		return smResponse;
	} 

	private SOAPMessage getDUC() throws SOAPException, MerliDBException, SQLException {
		logger.debug("getDUC");

		WSMerliBD wsdbd = new WSMerliBD();
		SOAPMessage smResponse = null;

		
		logger.debug("getDUC->> ");
		ListDUC listDUC = null;

		listDUC = wsdbd.getAllElementDUC(true);
				
		smResponse = createResponse("getDUC", listDUC);

		return smResponse;
	} 
	
/*WORKING FINE*/
	private SOAPMessage getLevels() throws SOAPException, MerliDBException, SQLException {
		logger.debug("getLevels");

		WSMerliBD wsdbd = new WSMerliBD();
		SOAPMessage smResponse = null;

		SemanticInterface si = new SemanticInterface();
		Node node =si.getNode(2231,"level");
		
		System.out.println("Test ws operacio getDUC"+node.toDTO().toString());
		
		
		logger.debug("getLevels->> ");
		ListDUC listDUC = null;

		listDUC = wsdbd.getAllElementDUC(false);
				
		smResponse = createResponse("getLevels", listDUC);

		return smResponse;
	} 
}
