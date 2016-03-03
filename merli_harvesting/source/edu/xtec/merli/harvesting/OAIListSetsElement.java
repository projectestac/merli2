package edu.xtec.merli.harvesting;

/*
 * OAIIdentifyElement.java
 *
 * Created on 2007/05/03
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

import java.util.HashMap;
import java.util.Map;

import org.jdom.Element;

public class OAIListSetsElement extends OAIVerbElement {
	
	protected static HashMap hArgs;
	protected String sBaseURL;
	
	public OAIListSetsElement(Map oArgs){
		super(oArgs);
	}
	
	protected HashMap getArguments(){
		if (hArgs==null){
			hArgs=new HashMap();
			hArgs.put("R", new String[]{"verb"});
			hArgs.put("E", new String[]{"resumptionToken"});
		}
		return hArgs;
	}

	public String getVerb() {
		return VERB_LIST_SETS;
	}

	public boolean checkArguments() throws MerliHarvestingException{
		throw new MerliHarvestingException(MerliHarvestingException.ERROR_NO_SET_HIERARCHY, this);		
	}
	public void generateResponse() throws MerliHarvestingException {
		Element eVerb = new Element(getVerb(), XMLNS);
		addContent(eVerb);
	}
	
}
