package edu.xtec.merli.harvesting;

import org.jdom.Element;

public class OAIErrorElement extends OAIElement {

	public OAIErrorElement(String sError, String sDescription){
		super();
		
		Element eError = new Element("error", XMLNS);
		eError.setAttribute("code", sError);
		eError.setText(sDescription);
		addContent(eError);
	}

	public void generateResponse() throws MerliHarvestingException {		
	}
}
