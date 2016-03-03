package edu.xtec.merli.harvesting;

/*
 * OAIListIdentifiersElement.java
 *
 * Created on 2007/04/30
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

import java.util.Map;

import org.jdom.Element;

public class OAIListRecordsElement extends OAIListIdentifiersElement {
	
	public OAIListRecordsElement(Map oArgs){
		super(oArgs);
	}
	
	public void generateResponse() throws MerliHarvestingException{		
		Element eVerb = getListElement(true);
		addContent(eVerb);
	}
	

	public String getVerb() {
		return VERB_LIST_RECORDS;
	}	
		
}
