/*
 * ServletMain.java
 *
 * Created on 21 de junio de 2005, 17:56
 */

package edu.xtec.merli.ws;


import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.rmi.ServerException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.log4j.Logger;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.ws.objects.IdElement;
import edu.xtec.merli.ws.objects.IdResource;
import edu.xtec.merli.ws.objects.Identifier;
import edu.xtec.merli.ws.objects.Lom;
import edu.xtec.merli.ws.objects.ObjectMerli;
import edu.xtec.merli.ws.objects.Result;



/**
 * 
 * @author aleix
 *
 */
public abstract class ServletMain extends HttpServlet {
    
	protected static final Logger logger = Logger.getRootLogger();
    public static String versionControl ="v2.0.1";

    
    static
    {
      iniSoap();			
    }
    
    protected static SOAPFactory sf;
    

        
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {
        logger.warn("Get received\n" +req.toString());
        PrintWriter out = resp.getWriter();
        out.println("Only accepted POST request");
        out.println(versionControl);
        out.flush();        
        out.close();        
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
    {  		   
       try 
        {
		   //System.out.println("welcome to WS system..");
		   
            // Get all the headers from the HTTP request
            MimeHeaders headers = getHeaders(req);
            
            // Get the body of the HTTP request
			ServletInputStream sis = req.getInputStream();
            
            // Now internalize the contents of the HTTP request
            // and create a SOAPMessage         
            SOAPMessage msg = MessageFactory.newInstance().createMessage(headers, sis);
            
            //logger.info("Post Request to ServletMain from "+req.getRemoteAddr());
            
            
            SOAPMessage reply = null;
            
			//msg = MessageFactory.newInstance().createMessage();
			/*SOAPBody sbody = msg.getSOAPPart().getEnvelope().getBody();
			
			Result res = new Result();
			
			//Test de getResource
			res.setOperation("getResource");
			IdResource idResource = new IdResource();
			idResource.setIdentifier("466");
			idResource.setType("merli");
			res.setResultObject(idResource);
			
			sbody.addChildElement(res.toXml());
			
			*/
			
            reply = processMessage(msg);
            //reply = processRequest(msg);
            
            if (reply != null) {
                //
               // Need to call saveChanges because we're
                // going to use the MimeHeaders to set HTTP
                // response information. These MimeHeaders
                // are generated as part of the save.
                //
                if (reply.saveRequired()) {
                    reply.saveChanges();
                }
                
                resp.setStatus(HttpServletResponse.SC_OK);
                putHeaders(reply.getMimeHeaders(), resp);
                
                // Write out the message on the response stream
                
                
                OutputStream os = resp.getOutputStream();
                reply.writeTo(os);
                os.flush();
            } else {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        } 
        
        catch (SOAPException se) 
        {
            throw new ServletException("SAAJ POST failed: " + se.getMessage());
        }
    }
    
	
	
    static MimeHeaders getHeaders(HttpServletRequest req) 
    {
        Enumeration headerNames = req.getHeaderNames();
        MimeHeaders headers = new MimeHeaders();
        
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            String headerValue = req.getHeader(headerName);
            
            StringTokenizer values = new StringTokenizer(headerValue, ",");
            
            while (values.hasMoreTokens()) {
                headers.addHeader(headerName, values.nextToken().trim());
            }
        }
        
        return headers;
    }
    
	
	
	
	
    static void putHeaders(MimeHeaders headers, HttpServletResponse res) 
    {
        Iterator it = headers.getAllHeaders();
        
        while (it.hasNext()) {
            MimeHeader header = (MimeHeader) it.next();
            
            String[] values = headers.getHeader(header.getName());
            
            if (values.length == 1) {
                res.setHeader(header.getName(), header.getValue());
            } else {
                StringBuffer concat = new StringBuffer();
                int i = 0;
                
                while (i < values.length) {
                    if (i != 0) {
                        concat.append(',');
                    }
                    
                    concat.append(values[i++]);
                }
                
                res.setHeader(header.getName(), concat.toString());
            }
        }
    }
    
    
    public abstract SOAPMessage processMessage(SOAPMessage smRequest);
    
    public SOAPMessage processRequest(SOAPMessage smRequest)
    {
        SOAPMessage smResponse = null;
        
        try
        {
            //smRequest.getSOAPPart();//getSOAPBody();
        
            SOAPPart sbRequest = smRequest.getSOAPPart();
            SOAPBodyElement sbeRequest=null;
        
            Iterator it = sbRequest.getEnvelope().getBody().getChildElements();//.getChildElements(sbRequest.getEnvelope().createName("addResource"));
        
            if (it.hasNext()) sbeRequest = (SOAPBodyElement) it.next();

			
           smResponse = processMessage(smRequest);
           
        }
        catch (SOAPException se)
        {
            return internalError(se,"no trobada");
        }

        
        return smResponse;
    }
   
	
	
    public ObjectMerli getParam(SOAPBodyElement sbeRequest,String sParam) throws ServerException
    {
		ObjectMerli oMerliRes=null;
        
        SOAPElement seParam=null;
        Iterator iAux=null;
        
        try 
        {
            iAux=sbeRequest.getChildElements(sf.createName(sParam));
            if (iAux.hasNext()) 
            {
                seParam = (SOAPElement) iAux.next();
                if (sParam.equals("lom")) 
                {
                    oMerliRes= new Lom(seParam);
                } 
                else if (sParam.equals("result")) 
                {
                    oMerliRes= new Result(seParam);
                }
                else if (sParam.equals("idResource"))
                {
                    oMerliRes = new IdResource(seParam);
                }
                else if (sParam.equals("idElement"))
                {
					oMerliRes = new IdElement(seParam);
                }
                else if (sParam.equals("identifier"))
                {
					oMerliRes = new Identifier(seParam);
                }
            }
        } 
        catch (SOAPException se)
        {
            if (sParam.equals("owner")) return null;
            //Owner is an optional parameter (listCollection,openCollection,searchCollection,export,getStructure)
            else throw new ServerException("ERROR"+se);
        }
        
        return oMerliRes;
    }
    
   

	
    public SOAPMessage createResponse(String sResponse,ObjectMerli oMerliRes)
    {
        SOAPMessage smResponse=null;
        try
        {
            smResponse =  MessageFactory.newInstance().createMessage();
            SOAPBody sbResponse = smResponse.getSOAPPart().getEnvelope().getBody();
			
			SOAPElement seResp = sf.createElement(sf.createName(sResponse+"Response"));

			//Afageix l'element donat a un element Response.
			seResp.addChildElement(oMerliRes.toXml());
			
            sbResponse.addChildElement(seResp);
        
            smResponse.saveChanges();
        }
        catch(SOAPException se)
        {
        	se.printStackTrace();
            return internalError(se,sResponse);
        }
        return smResponse;
    }
    

    public SOAPMessage internalError(Exception e,String operacio)
    {
        SOAPMessage smRes=null;
        
        try
        {
            smRes =  MessageFactory.newInstance().createMessage();
            SOAPBody sbResponse = smRes.getSOAPPart().getEnvelope().getBody();
            SOAPFault sfResponse = sbResponse.addFault();
            
            Name nFault = sf.createName("wsxtec","",SOAPConstants.URI_NS_SOAP_ENVELOPE);
            sfResponse.setFaultCode(nFault.getLocalName());
            sfResponse.setFaultString("WS operacio: "+ operacio+" error:"+e.getMessage());
            
            String userId="";
            
            //if (uRequest!=null) userId=uRequest.getUserId();
            
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            
       }
        catch (SOAPException se)
        {
         se.printStackTrace();
        }
        return smRes;
    }
	

    public SOAPMessage internalError(MerliDBException e,String operacio)
    {
        SOAPMessage smRes=null;
        
        try
        {
            smRes =  MessageFactory.newInstance().createMessage();
            SOAPBody sbResponse = smRes.getSOAPPart().getEnvelope().getBody();
            SOAPFault sfResponse = sbResponse.addFault();
            
            Name nFault = sf.createName("wsxtec","",SOAPConstants.URI_NS_SOAP_ENVELOPE);
			sfResponse.setFaultString("WS operacio: "+ operacio+" error:"+e.getMessage());
	           
            String userId="";
            
            //if (uRequest!=null) userId=uRequest.getUserId();
            
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stacktrace = sw.toString();
            
       }
        catch (SOAPException se)
        {
         se.printStackTrace();
        }
        return smRes;
    }
    
    private static void iniSoap()
    {
        if (/*(mf==null) ||*/ (sf==null))
        {
            try
            {

				SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
				SOAPConnection conn = scf.createConnection();
				//mf = MessageFactory.newInstance();
                sf = SOAPFactory.newInstance();
            }
   
            catch (Exception e)
            {
                //logger.error();
                e.printStackTrace();
            }
        }
    }
	

	
   protected void printLog(String query, String host, String ws) {
	   logger.info(ws+": "+query+" host:"+host+" "+new Date(System.currentTimeMillis()));
	}
   
}
