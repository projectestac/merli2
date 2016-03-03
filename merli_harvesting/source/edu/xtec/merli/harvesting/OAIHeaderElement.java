package edu.xtec.merli.harvesting;

import org.jdom.Element;

import edu.xtec.merli.harvesting.util.ResourceIdentifier;

public class OAIHeaderElement extends Element {
	
	public static String XMLNS = OAIElement.XMLNS;
	
	public OAIHeaderElement(ResourceIdentifier oResource){
		super("header", XMLNS);

		Element eIdentifier = new Element("identifier", XMLNS);
		eIdentifier.addContent(oResource.getIdentifier());
		addContent(eIdentifier);
		
		Element eDatestamp = new Element("datestamp", XMLNS);
		eDatestamp.addContent(OAIElement.dateToString(oResource.getDatestamp()));
		addContent(eDatestamp);
	}
	
	public String getIdentifier(){
		return getChildText("identifier", OAIElement.OAINS);
	}

}
