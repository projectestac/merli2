package simpple.xtec.agrega.ws;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.NoSuchElementException;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;

import simpple.xtec.agrega.AgregaUtils;
import simpple.xtec.agrega.objects.ObjectMerli;


/*
 * JAppletProva.java
 *
 * Created on 13 de abril de 2005, 13:55
 */

/**
 *
 * @author eda3s
 */
public abstract class AgregaWS {      
	
	static final Logger logger = Logger.getLogger(simpple.xtec.agrega.ws.AgregaWS.class);
	

	
	
	//Operacions
	public static final int GETRESOURCE = 		011;  
	public static final int SENDRESOURCE = 		012;
	
	public static final int PRESENTARALMACENAR =021;
	public static final int GENERARPAQUETEPIF = 022;
	
	public static final int CREARSESION = 		031;
	public static final int DESTROYSESSION = 	032;
	
	public static final int SQIQUERY = 			041;
	public static final int ESTASACTIVO = 		042;	
	public static final int SETQUERYLANGUAGE = 	043;

	
	
	//servidors
	public static final int LOCAL = 			101;
	public static final int TEST = 				102;
	public static final int PRODU = 			103;
	public static final int ACCEPT = 			104;

	//Context del servei
	public static final int MERLI = 			201;
	public static final int AGREGA = 			202;
	public static final int AGREGA_SQI = 		203;
	public static final int AGREGA_SESSIONS = 	204;
    
	
	
    SOAPConnectionFactory scf; 
    SOAPConnection con;
    MessageFactory mf;
    SOAPFactory sf;
    SOAPMessage smRequest;
    

    
    public AgregaWS() 
    {
        iniSoap();       
     }
    
    /**
     * inicialització del Soap.
     *
     */
     public void iniSoap()
     {
        try
        {
        	if (scf == null)
        		scf= SOAPConnectionFactory.newInstance();			
            if (mf == null)
        		mf = MessageFactory.newInstance();
            if (sf == null)
        		sf = SOAPFactory.newInstance();
        }
   
        catch (Exception e)
        {
            e.printStackTrace();
        
        }
    }
     
  	
	/**
	  * Processament d'una operació.
	  * @param idOperation identificador de la operació a realitzar.
	  * @param objm Objecte SOAP que serà enviat en l'execució de la operació.
	  * @param server Servidor al que s'accedeix. {LOCAL, TEST, ACCEPT, PRODU}
	  * @return
	  */
 	 public SOAPElement processOperation(int idOperation, ObjectMerli objm, int server)
     {
 		
		String response = null;
		SOAPMessage sm = null;
		SOAPElement se =null;

        int context = 0;
		
       
		try {
			con = scf.createConnection();
	        sm =  sendMessage(context,server);
	        con.close();
		     
			se =objm.getResponse(sm, response);
			//se = ((Node)((SOAPElement) sm.getSOAPBody().getChildElements(sf.createName(response)).next()).getFirstChild().getFirstChild()).getParentElement();

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
	

	public SOAPMessage sendMessage(int context, int server)
	     {
			SOAPMessage smResponse = null;
	         try
	         {
	            URL url = getUrl(context,server);
	            
	            System.out.println("sending message to "+url+"...");		
	       
	        AgregaUtils.printSOAPMessage(smRequest,"REQUEST:");	           			
	            smResponse = con.call(smRequest, url);
							
			      
	            AgregaUtils.printSOAPMessage(smResponse,"RESPONSE");
	            SOAPBody sb = smResponse.getSOAPPart().getEnvelope().getBody();            
	            if (sb.hasFault())
	                
	            {
	                SOAPFault sf = sb.getFault();
	                String fault = sf.getFaultString();
	                System.out.println("FAULT*"+fault+"*");
	            }          
	         }
	         
	         catch(Exception e)
	         {
	            e.printStackTrace();
	         }
			return smResponse;
	   
	     }

	private URL getUrl(int context, int server) {
		return AgregaUtils.getUrl(context,server);
	}

}
