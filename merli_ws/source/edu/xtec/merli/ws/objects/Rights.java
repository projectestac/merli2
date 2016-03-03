package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import edu.xtec.merli.utils.Utility;

public class Rights extends ObjectMerli {

	private String cost;
	private SourceValue copyrightAndOtherRestrictions;
	private LangString description;
	private static final String ACTION = "remoteplay";
	private static final int MAX_DESC = 2000;


	
	

	public Rights() {
		super();
		// TODO Auto-generated constructor stub
		this.cost = "";
		this.copyrightAndOtherRestrictions = new SourceValue("copyrightAndOtherRestrictions");
		this.description = new LangString();
	}
	public Rights(String cost, SourceValue copyRightAndOtherRestrictions, LangString description) {
		super();
		// TODO Auto-generated constructor stub
		this.cost = cost;
		this.copyrightAndOtherRestrictions = copyRightAndOtherRestrictions;
		this.description = description;
		this.description.setString(Utility.controlarString(description.getString(),Rights.MAX_DESC));
		
	}






	public Rights(SOAPElement seRights) throws SOAPException {
		
		Iterator it = seRights.getChildElements(soapFactory.createName("cost"));
		if (it.hasNext())
			cost = ((SOAPElement) it.next()).getValue();
		
		it = seRights.getChildElements(soapFactory.createName("copyrightAndOtherRestrictions"));
		if (it.hasNext())
			copyrightAndOtherRestrictions = new SourceValue((SOAPElement) it.next());
		
		it = seRights.getChildElements(soapFactory.createName("description"));
		if (it.hasNext()){
			description = new LangString((SOAPElement) it.next());
			this.description.setString(Utility.controlarString(description.getString(),Rights.MAX_DESC));
		}
	}
	
	
	public SourceValue getCopyRightAndOtherRestrictions() {
		return copyrightAndOtherRestrictions;
	}
	





	public void setCopyRightAndOtherRestrictions(
			SourceValue copyRightAndOtherRestrictions) {
		this.copyrightAndOtherRestrictions = copyRightAndOtherRestrictions;
	}
	





	public String getCost() {
		return cost;
	}
	





	public void setCost(String cost) {
		this.cost = cost;
	}
	





	public LangString getDescription() {
		return description;
	}
	





	public void setDescription(LangString description) {
		this.description = description;
		this.description.setString(Utility.controlarString(description.getString(),Rights.MAX_DESC));
	}
	





	public SOAPElement toXml() throws SOAPException {
		SOAPElement seRights;
		SOAPElement seCost;
		SOAPElement seRightsCDR;
		SOAPElement seRightsPermission;
		SOAPElement seRightsAction;
		SOAPElement seDescription;
		
		seRights = soapFactory.createElement(soapFactory.createName("rights"));
		
		if (cost == null) cost = "";
		seCost = soapFactory.createElement(soapFactory.createName("cost"));
		seCost.addTextNode(cost);
		seRights.addChildElement(seCost);
		
		if (copyrightAndOtherRestrictions == null) copyrightAndOtherRestrictions = new SourceValue("copyrightAndOtherRestrictions");
		copyrightAndOtherRestrictions.setName("copyrightAndOtherRestrictions");
		seRights.addChildElement(copyrightAndOtherRestrictions.toXml());

		if (description == null) description = new LangString();
		seDescription = soapFactory.createElement(soapFactory.createName("description"));
		seDescription.addChildElement(description.toXml());
		seRights.addChildElement(seDescription);

		seRightsCDR = soapFactory.createElement(soapFactory.createName("cdr"));
		seRightsPermission = soapFactory.createElement(soapFactory.createName("permission"));
		seRightsAction = soapFactory.createElement(soapFactory.createName("action"));
		seRightsAction.addTextNode(this.ACTION);
		seRightsPermission.addChildElement(seRightsAction);
		seRightsCDR.addChildElement(seRightsPermission);
		
		seRights.addChildElement(seRightsCDR);
		
		return seRights;		
	}

}
