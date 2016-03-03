package edu.xtec.merli.harvesting;

/*
 * OAIListIdentifiersElement.java
 *
 * Created on 2007/01/17
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

import java.util.HashMap;
import java.util.Map;

import org.jdom.Element;

import edu.xtec.merli.harvesting.util.ResourceIdentifier;

public class OAIGetRecordElement extends OAIVerbElement {
	
	protected static HashMap hArgs;
	
	public OAIGetRecordElement(Map oArgs){
		super(oArgs);
	}
	
	public String getMetadataPrefix(){
		return getArgument("metadataPrefix");
	}
	
	public String getIdentifier(){
		return getArgument("identifier");
	}
		
	protected HashMap getArguments(){
		if (hArgs==null){
			hArgs=new HashMap();
			hArgs.put("R", new String[]{"verb","metadataPrefix","identifier"});
		}
		return hArgs;
	}
	
	public boolean checkArguments() throws MerliHarvestingException{
		boolean bOk = super.checkArguments();
		if (bOk){
			bOk = externalToInternalId(getIdentifier())!=null;
			if (!bOk){
				throw new MerliHarvestingException(MerliHarvestingException.ERROR_ID_DOES_NOT_EXIST);				
			}
		}
		return bOk;
	}	
		
	public void generateResponse() throws MerliHarvestingException{
		Element eMetadata = getDB().getResourceMetadata(getIdentifier());
		if (eMetadata!=null){			
			Element eVerb = new Element(getVerb(), XMLNS);
			addContent(eVerb);
			ResourceIdentifier oResource = getDB().getResourceIdentifier(getIdentifier());
			Element eHeader = new OAIHeaderElement(oResource);
			Element eRecord = createRecordElement(eHeader, eMetadata);
			eVerb.addContent(eRecord);
		}else{
			throw new MerliHarvestingException(MerliHarvestingException.ERROR_ID_DOES_NOT_EXIST, this);
		}
	}

	public String getVerb() {
		return VERB_GET_RECORD;
	}
	
		
}
