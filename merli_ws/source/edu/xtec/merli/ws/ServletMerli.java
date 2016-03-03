package edu.xtec.merli.ws;

import java.io.IOException;
import java.io.PrintWriter;
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
import edu.xtec.merli.ws.objects.Contribute;
import edu.xtec.merli.ws.objects.IdResource;
import edu.xtec.merli.ws.objects.Lom;



/**
 * 
 * @author acanals5
 * @version 1.0.0
 */
public class ServletMerli extends ServletMain{

    public static final String GETRESOURCE = "getResource";
	public static final String DELRESOURCE = "delResource";
	public static final String UNPUBLISHRESOURCE = "unpublishResource";
	public static final String SETRESOURCE = "setResource";
	public static final String ADDRESOURCE = "addResource";
	

	/**
	 * Processa el SOAPMessage, identifica de quin típus és l'objecte enviat
	 * i segons el típus executa la operació adequada.
	 * Retorna el missatge retornat per la operació executada.
	 */
   public SOAPMessage processMessage(SOAPMessage smRequest)
    {
        SOAPMessage smResponse=null;
		String sQuery = null;
        try
        {
            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();
			
            SOAPBodyElement sbeRequest=null;

            Iterator it = sbRequest.getChildElements();        
           		
			while (it.hasNext()){
				try{
					sbeRequest = (SOAPBodyElement) it.next();
				}catch(Exception e){}
			}
			
            sQuery=sbeRequest.getElementName().getLocalName();
			
			String[] j = smRequest.getMimeHeaders().getHeader("host");
			printLog(sQuery+"-INIT",j[0],"WSMerli");
			
			
			//AddResource
			if (ADDRESOURCE.equals(sQuery))
					smResponse = addResource((SOAPBodyElement)sbeRequest.getChildElements(sf.createName("lom")).next());
					
			//SetResource
			if (SETRESOURCE.equals(sQuery))
					smResponse = setResource((SOAPBodyElement)sbeRequest.getChildElements(sf.createName("lom")).next());
			//unpublishResource
			if (UNPUBLISHRESOURCE.equals(sQuery))
					smResponse = unpublishResource((SOAPBodyElement)sbeRequest.getChildElements(sf.createName("idResource")).next());
			//DelResource
			if (DELRESOURCE.equals(sQuery))
					smResponse = delResource((SOAPBodyElement)sbeRequest.getChildElements(sf.createName("idResource")).next());
			
			//GetResource
			if (GETRESOURCE.equals(sQuery))
					smResponse = getResource((SOAPBodyElement)sbeRequest.getChildElements(sf.createName("idResource")).next());
			//else createError(sbeRequest);
			
			
			printLog(sQuery+"-END","Server","WSMerli");
        }
		
        catch(SOAPException se)
        {
            return internalError(se,sQuery);
        }
        catch(MerliDBException ioe)
        {   
            return internalError(ioe,sQuery);
        }
        catch(Exception ioe)
        {   
            return internalError(ioe,sQuery);
        }


        return smResponse;
    }
	


/**
     * Rep el SOAPBodyElement enviat pel client i el converteix a l'objecte 
	 * adequat dins el domini. En aquest cas un Lom
	 * 
	 * Executa la operació setResource passant com a parametre el Lom.
	 * 
	 * Retorna un Result indicant la operació i que l'execució ha estat correcte i 
	 * amb un objecte IdResource amb l'id del recurs modificat.
	 * 
	 * @param sbeRequest SOAPBodyElement amb un Lom
    * @return
 * @throws MerliDBException 
 * @throws SOAPException 
    */
	private SOAPMessage setResource(SOAPBodyElement sbeRequest) throws MerliDBException, SOAPException {
		WSMerliBD wsmbd = new WSMerliBD();

		Lom lom = new Lom(sbeRequest);
		
		SOAPMessage smResponse=null;
				
		IdResource idResource;			
		printLog("setResource-"+lom.getGeneral().getIdentifier().getIdEntry(),"SERVER","wsMerli");
		
		idResource = wsmbd.setRecurs(lom);								

		smResponse = createResponse(SETRESOURCE, idResource);
		
		return smResponse;
	}



	private boolean comprovarCampsObligatoris(Lom lom) {
		try{
		if (lom.getGeneral().getTitle() == null  || "".equals(lom.getGeneral().getTitle().getString().trim()) ||
				lom.getGeneral().getDescription() == null  || "".equals(lom.getGeneral().getDescription().getString().trim())  ||
				lom.getTechnical().getLocation() == null || "".equals(lom.getTechnical().getLocation().trim()))
			return false;
		
		for (int i=0;i<lom.getLifeCycle().getContributeList().size();i++){
			if ("author".equals(((Contribute)lom.getLifeCycle().getContributeList().get(i)).getRole().getValue()) &&
				((Contribute)lom.getLifeCycle().getContributeList().get(i)).getEntity().getUsername().trim().length() > 0){
				return true;	
			}
		}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * Rep el SOAPBodyElement enviat pel client i el converteix a l'objecte 
	 * adequat dins el domini. En aquest cas un IdResource.
	 * 
	 * Executa la operació delResource passant com a parametre l'IdResource.
	 * 
	 * Retorna un Result indicant la operació i que l'execució ha estat correcte.
	 * 
	 * @param sbeRequest SOAPBodyElement amb un IdResource
	 * @return
	 * @throws SOAPException 
	 * @throws MerliDBException 
	 */
	public SOAPMessage delResource(SOAPBodyElement sbeRequest) throws SOAPException, MerliDBException {
				
		WSMerliBD wsmbd = new WSMerliBD();
		
		SOAPMessage smResponse=null;

		IdResource idResource = new IdResource(sbeRequest);
		printLog("delResource-"+idResource.getIdentifier(),"SERVER","wsMerli");
		wsmbd.delRecurs(idResource);						

		smResponse = createResponse(DELRESOURCE, idResource);

		return smResponse;
	}

	
	/**
	 * Rep el SOAPBodyElement enviat pel client i el converteix a l'objecte 
	 * adequat dins el domini. En aquest cas un IdResource.
	 * 
	 * Executa la operació unpublishRecurs passant com a parametre l'IdResource.
	 * 
	 * Retorna un Result indicant la operació i que l'execució ha estat correcte.
	 * 
	 * @param sbeRequest SOAPBodyElement amb un IdResource
	 * @return
	 * @throws SOAPException 
	 * @throws MerliDBException 
	 */
	public SOAPMessage unpublishResource(SOAPBodyElement sbeRequest) throws SOAPException, MerliDBException {
				
		WSMerliBD wsmbd = new WSMerliBD();
		
		SOAPMessage smResponse=null;

		IdResource idResource = new IdResource(sbeRequest);
		printLog("unpublishRecurs-"+idResource.getIdentifier(),"SERVER","wsMerli");
		wsmbd.unpublishRecurs(idResource);						

		smResponse = createResponse(UNPUBLISHRESOURCE, idResource);

		return smResponse;
	}
	
	
	
	/**
	 * Rep el SOAPBodyElement enviat pel client i el converteix a l'objecte 
	 * adequat dins el domini. En aquest cas un Lom
	 * 
	 * Executa la operació addResource passant com a parametre el Lom.
	 * 
	 * Retorna un Result indicant la operació i que l'execució ha estat correcte i 
	 * amb un objecte IdResource amb l'id del recurs creat.
	 * 
	 * @param sbeRequest SOAPBodyElement amb un Lom
	 * @return
	 * @throws SOAPException
	 * @throws MerliDBException 
	 */
	public SOAPMessage addResource(SOAPBodyElement sbeRequest) throws SOAPException, MerliDBException {

		WSMerliBD wsmbd = new WSMerliBD();

		Lom lom = new Lom(sbeRequest);
		
		SOAPMessage smResponse=null;
		
		IdResource idResource = new IdResource();	
		if (comprovarCampsObligatoris(lom)){
			idResource = wsmbd.addRecurs(lom);								
		}else
			throw new SOAPException("Camps obligatoris buits");
			
		smResponse = createResponse(ADDRESOURCE, idResource);
		printLog("addResource-creat:"+idResource.getIdentifier(),"SERVER","wsMerli");
		
		return smResponse;
	}

 
	/**
	 * Rep el SOAPBodyElement enviat pel client i el converteix a l'objecte 
	 * adequat dins el domini. En aquest cas un IdResource.
	 * 
	 * Executa la operació getResource passant com a parametre l'IdResource.
	 * 
	 * Retorna un Result indicant la operació i que l'execució ha estat correcte i 
	 * amb l'objecte sol·licitat en un Lom.
	 * 
	 * @param node SOAPBodyElement amb un IdResource
	 * @return
	 * @throws SOAPException
	 * @throws MerliDBException 
	 */
	public SOAPMessage getResource(SOAPBodyElement sbRequest) throws SOAPException, MerliDBException {

		WSMerliBD wsmbd = new WSMerliBD();

		IdResource idResource = new IdResource(sbRequest);			

		SOAPMessage smResponse=null;
		Lom lom=null;

		//printLog("getResource-"+idResource.getIdentifier(),"SERVER","wsMerli");
		lom = wsmbd.getResource(idResource);

		
		smResponse = createResponse(GETRESOURCE, lom);
		
		
		return smResponse;
	}

}

