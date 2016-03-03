package edu.xtec.merli.ws;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.ws.objects.ElementDUC;
import edu.xtec.merli.ws.objects.IdElement;
import edu.xtec.merli.ws.objects.ListDUC;
import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.SemanticException;

/**
 * 
 * @author jlpatricio
 * @version 1.0.0
 */

public class ServletDuc extends ServletMain {

	private static final String GETELEMENTDUC = "GetElement";
	private static final String GETDUC = "GetDUC";
	private static final String GETNIVELLS = "GetLevels";


	public SOAPMessage processMessage(SOAPMessage smRequest) {
		// TODO Auto-generated method stub
		// return processMessage(smRequest,1);

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
			//logger.debug("ServletDuc.processMessage-> action="+sQuery);

			String[] j = smRequest.getMimeHeaders().getHeader("host");
			printLog(sQuery+"-INIT",j[0],"WSDUC");
			
			// getElementDUC
			if (GETELEMENTDUC.equals(sQuery)){
				smResponse = getElementDUC((SOAPBodyElement)sbeRequest.getChildElements(sf.createName("idElement")).next());
				//smResponse.writeTo(System.out);
			}
//			 getElementDUC
			if (GETDUC.equals(sQuery)){
				smResponse = getDUC();
				//smResponse.writeTo(System.out);
			}
			// getElementDUC
			if (GETNIVELLS.equals(sQuery)){
				smResponse = getLevels();
				//smResponse.writeTo(System.out);
			}
			printLog(sQuery+"-END","Server","WSDUC");
			// else createError(sbeRequest);
		}

		catch (SOAPException se) {
			se.printStackTrace();
			return internalError(se,sQuery);
		} catch (MerliDBException ioe) {
			ioe.printStackTrace();
			return internalError(ioe,sQuery);		
		} catch (Exception ioe) {
			ioe.printStackTrace();
			return internalError(ioe,sQuery);
		}

		return smResponse;

	}

	

	private SOAPMessage getElementDUC(SOAPBodyElement sbeRequest) throws SOAPException, MerliDBException, SQLException, SemanticException {

		WSMerliBD wsdbd = new WSMerliBD();
		SOAPMessage smResponse = null;

		IdElement idElement = new IdElement(sbeRequest);
		logger.debug("idElement-> "+idElement);
		ElementDUC elementDUC = null;

		elementDUC = wsdbd.getElementDUC(idElement);
				
		smResponse = createResponse("getElement", elementDUC);

		return smResponse;
	} 

	private SOAPMessage getDUC() throws SOAPException, MerliDBException, SQLException {

		WSMerliBD wsdbd = new WSMerliBD();
		SOAPMessage smResponse = null;

		
		logger.debug("getDUC->> ");
		ListDUC listDUC = null;

		listDUC = wsdbd.getAllElementDUC(true);
				
		smResponse = createResponse("getDUC", listDUC);

		return smResponse;
	} 
	

	private SOAPMessage getLevels() throws SOAPException, MerliDBException, SQLException {

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
