package edu.xtec.merli.ws.test;


import java.net.URL;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;


import edu.xtec.merli.ws.ServletMerli;
import edu.xtec.merli.ws.objects.IdElement;
import edu.xtec.merli.ws.objects.IdResource;
import edu.xtec.merli.ws.objects.Lom;
import edu.xtec.merli.ws.objects.ObjectMerli;



/*
 * JAppletProva.java
 *
 * Created on 13 de abril de 2005, 13:55
 */

/**
 *
 * @author eda3s
 */
public class TestWS {      
	
    
    
	public static final int ADDRESOURCE = 0;
	public static final int SETRESOURCE = 1;
	public static final int DELRESOURCE = 2;
	public static final int UNPUBLISHRESOURCE = 7;
	public static final int GETRESOURCE = 3;  
	public static final int GETELEMENTDUC = 4;
	public static final int GETDUC = 5;
	public static final int GETNIVELLS = 6;

	public static final int LOCAL = 1;
	public static final int TEST = 2;
	public static final int PRODU = 3;
	public static final int ACCEPT = 4;
    
    SOAPConnectionFactory scf; 
    SOAPConnection con;
    MessageFactory mf;
    SOAPFactory sf;
    SOAPMessage smRequest;
    

    
    public TestWS() 
    {
        iniSoap();       
        
     }
    
    
     private void iniSoap()
     {
        try
        {
            scf= SOAPConnectionFactory.newInstance();
            con = scf.createConnection();
			
            mf = MessageFactory.newInstance();
            sf = SOAPFactory.newInstance();
        }
   
        catch (Exception e)
        {
            e.printStackTrace();
        
        }
        //System.out.println("INI SOAP OK!!!!!");
    }
   
     public SOAPElement processOperation(int idOperation, ObjectMerli objm){		 
		 return processOperation(idOperation, objm, LOCAL);
     }
	 
	 public SOAPElement processOperation(int idOperation, ObjectMerli objm, int server)
     {
		String operation = null;
		SOAPMessage sm;
		SOAPElement se =null;
        boolean merli = true;            
		
		switch (idOperation){
			case ADDRESOURCE:
				addResource((Lom) objm);
				operation = "addResourceResponse";
				break;

			case SETRESOURCE:
				setResource((Lom) objm);
				operation = "setResourceResponse";
				break;
				
			case DELRESOURCE:
				delResource((IdResource)objm);
				operation = "delResourceResponse";
				break;
			
			case UNPUBLISHRESOURCE:
				unpublishResource((IdResource)objm);
				operation = "unpublishResourceResponse";
				break;

			case GETRESOURCE:
				getResource((IdResource) objm);
				operation = "getResourceResponse";
				break;

			case GETELEMENTDUC:
				getElementDUC((IdElement) objm);
				operation = "getElementResponse";
				merli = false;  
				break;
			case GETDUC:
				getDUC();
				operation = "getDUCResponse";
				merli = false;  
				break;
			case GETNIVELLS:
				getLevels();
				operation = "getLevelsResponse";
				merli = false;  
				break;
				
        }
       
        sm =  sendMessage(merli,server);
		
		try {
			se =((Node)((SOAPElement) sm.getSOAPBody().getChildElements(sf.createName(operation)).next()).getFirstChild().getFirstChild()).getParentElement();
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return se;
     }
	

		public SOAPMessage sendMessage(boolean merli, int server)
	     {
			SOAPMessage smResponse = null;
	         try
	         {
	            URL url = null;
	            
        if (merli){
			switch (server){
				case LOCAL: url = new URL("http://localhost:8088/merli_ws/merli"); break;
				case TEST:	url = new URL("http://xtec-wc.educacio.intranet:7778/e13_merli_ws/merli");break;
				case ACCEPT: url = new URL("http://acc.xtec.cat/e13_merli_ws/merli");break;
				case PRODU: url = new URL("http://aplitic.xtec.cat/e13_merli_ws/merli");
				default: 	url = new URL("http://localhost:8088/WS/merli");			
			}
		}else{
			switch (server){
				case LOCAL: url = new URL("http://localhost:8088/merli_ws/duc"); break;
				case TEST:	url = new URL("http://xtec-wc.educacio.intranet:7778/e13_merli_ws/duc");break;
				case ACCEPT: url = new URL("http://acc.xtec.cat/e13_merli_ws/duc");break;
				case PRODU: url = new URL("http://aplitic.xtec.cat/e13_merli_ws/duc");break;
				default: 	url = new URL("http://localhost:8088/WS/duc");			
			}
		}

	            System.out.println("sending message to "+url+"...");		
	           
	            //jtaMessage.setText(Utils.soapMessageToString(smRequest));					
	            smResponse = con.call(smRequest, url);
							
				SOAPBody sbRequest = smResponse.getSOAPPart().getEnvelope().getBody();
				SOAPBodyElement sbeRequest = null;
	       
	            con.close();
	      
	            
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
		
		
     
	 public void addResource(Lom lom)
    {       
        try
                
        {
            smRequest = MessageFactory.newInstance().createMessage();
            
            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();
            /*MimeHeaders mimeh = smRequest.getMimeHeaders();
			mimeh= new MimeHeaders();
			mimeh.addHeader("SOAPAction","addResource");*/		
            //Propi de cada operacio            
            Name n = sf.createName("addResource"); 
                    
            SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);
         
            sbeRequest.addChildElement(lom.toXml());
            //sbeRequest.addChildElement(cRequest.toXml());
            //sbeRequest.addChildElement(rRequest.toXml());                       
            //smRequest.createAttachmentPart().setContent(lom,"lom");
			
            smRequest.saveChanges();

        }
        catch (Exception e)
        {
            System.out.println("Ha petat "+e.toString()+"*");        
        }
    }

	private void setResource(Lom lom) {

        try
        {
            smRequest = MessageFactory.newInstance().createMessage();
            
            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();
            MimeHeaders mimeh = smRequest.getMimeHeaders();
			
            //Propi de cada operacio            
            Name n = sf.createName("setResource"); 
                    
            SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);
         
            sbeRequest.addChildElement(lom.toXml());
			
            smRequest.saveChanges();

        }
        catch (Exception e)
        {
            System.out.println("Ha petat "+e.toString()+"*");
        }
	}
	
	
	private void unpublishResource(IdResource idResource){
		 try
	        {
	            smRequest = mf.createMessage();
		            
	            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();

	            //Propi de cada operacio            
	            Name n = sf.createName(ServletMerli.UNPUBLISHRESOURCE); 
	                    
	            SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);
	         
	            sbeRequest.addChildElement(idResource.toXml());				

	            smRequest.saveChanges();

	        }
	        catch (Exception e)
	        {
	            System.out.println("Ha petat "+e.toString()+"*");
	        
	        }
	}
	
	 private void delResource(IdResource idResource) {
		 try
	        {
	            smRequest = mf.createMessage();
		            
	            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();

	            //Propi de cada operacio            
	            Name n = sf.createName("delResource"); 
	                    
	            SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);
	         
	            sbeRequest.addChildElement(idResource.toXml());				

	            smRequest.saveChanges();

	        }
	        catch (Exception e)
	        {
	            System.out.println("Ha petat "+e.toString()+"*");
	        
	        }
	}




	/**
	  * 
	  *
	  */
     private void getResource(IdResource idResource) {
		 	
	        
        try
        {
           // MessageFactory objt = MessageFactory.newInstance();
           // smRequest = objt.createMessage();

            smRequest = mf.createMessage();
//	            
            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();

//	            SOAPBody sbRequest = smRequest.getSOAPBody();
//				SOAPElement se = sf.createElement("getResource");
            //Propi de cada operacio            
            Name n = sf.createName("getResource"); 
                    
            SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);
         
            sbeRequest.addChildElement(idResource.toXml());
			

			sbRequest.addTextNode("text de prova per jdk1.4.2");       
            smRequest.saveChanges();

        }
        catch (Exception e)
        {
            System.out.println("Ha petat "+e.toString()+"*");
        
        }
	}


     private void getElementDUC(IdElement idElement) {
		 	
	        
        try
        {
            smRequest = mf.createMessage();
			
            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();
			
            //Propi de cada operacio            
            Name n = sf.createName("GetElement"); 
                    
            SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);
         
            sbeRequest.addChildElement(idElement.toXml());
			
            smRequest.saveChanges();

        }
        catch (Exception e)
        {
            System.out.println("Ha petat "+e.toString()+"*");
        
        }
	}


     private void getDUC() {
		 	
	        
        try
        {
            smRequest = mf.createMessage();
			
            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();
			
            //Propi de cada operacio       
			Name n = sf.createName("GetDUC"); 
				
                    
            SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);         
			
            smRequest.saveChanges();

        }
        catch (Exception e)
        {
            System.out.println("Ha petat "+e.toString()+"*");
        
        }
	}

     private void getLevels() {
		 	
	        
     try
     {
         smRequest = mf.createMessage();
			
         SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();
	
         //Propi de cada operacio       
		Name n = sf.createName("GetLevels"); 
				
                 
         SOAPBodyElement sbeRequest = sbRequest.addBodyElement(n);         
			
         smRequest.saveChanges();

     }
     catch (Exception e)
     {
         System.out.println("Ha petat "+e.toString()+"*");
     
     }
	}


  
    
   
    

}
