package edu.xtec.merli.harvesting.servlet;

/*
 * MerliHarvestingServlet.java
 *
 * Created on 2007/01/10
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;

import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;

import edu.xtec.merli.harvesting.MerliHarvestingException;
import edu.xtec.merli.harvesting.OAIElement;
import edu.xtec.merli.harvesting.OAIErrorElement;
import edu.xtec.merli.harvesting.OAIVerbElement;

public class MerliHarvestingServlet extends MerliServlet {
	
	protected static String RESPONSE_ENCODING = "UTF-8";
	protected static String XMLNS = "http://www.openarchives.org/OAI/2.0/";
	protected Namespace XSINS = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	protected Namespace OAINS = Namespace.getNamespace(XMLNS);
	
	
	protected static String P_VERB = "verb";
	
	protected static Map mArguments;
	

	protected void processRequest() throws ServletException, IOException {
		response.setContentType("text/xml; charset="+RESPONSE_ENCODING);
		PrintWriter out = response.getWriter();

		OAIElement eOAI = null;
		try{
			StringBuffer sb=new StringBuffer("VERB: "+request.getParameter("verb")+" ARGUMENTS: ");
			Enumeration enumParameters = request.getParameterNames();
			while (enumParameters.hasMoreElements()){
				String sKey = (String)enumParameters.nextElement();
				if (!sKey.equals("verb")) sb.append(sKey+"="+request.getParameter(sKey)+"; ");
			}
			logger.debug(sb);
			// Get operation
			eOAI = OAIVerbElement.getOAIVerbElement(request.getParameterMap(), getServletFullPath());

			// Check arguments
			eOAI.checkArguments();
			eOAI.generateResponse();
		}catch (MerliHarvestingException mhe){
			eOAI = new OAIErrorElement(mhe.getError(), mhe.getDescription());
			if (mhe.getOAIElement()!=null) eOAI.setRequest(mhe.getOAIElement().getRequest());
		}
		eOAI.getRequest().setText(getServletFullPath());
		
		// Generate and send response
		XMLOutputter xout = new XMLOutputter();
		Format oFormat = Format.getPrettyFormat();
		oFormat.setOmitDeclaration(false);
        oFormat.setEncoding(RESPONSE_ENCODING);
		oFormat.setTextMode(TextMode.TRIM);
		xout.setFormat(oFormat);
		out.println("<?xml version=\"1.0\" encoding=\""+RESPONSE_ENCODING+"\" ?>");
		xout.output(eOAI, out);
		//xout.output(eOAI, System.out);
	}
	
//	************************************	
//	* Utils
//	************************************

	protected String getServletFullPath(){
		//return request.getRequestURL().toString();
		return OAIElement.getProperty("harvesting.server");
		/*StringBuffer sbPath = new StringBuffer();
		sbPath.append(request.getScheme());
		sbPath.append("://");
		sbPath.append(request.getRemoteHost());
		if (request.getServerPort()!=80) sbPath.append(":"+request.getServerPort());
		sbPath.append(request.getRequestURI());
		return sbPath.toString();*/
	}
	
	
}
