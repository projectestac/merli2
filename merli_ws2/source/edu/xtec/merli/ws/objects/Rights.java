package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import edu.xtec.merli.ws.WSMerliBD;

public class Rights extends ObjectMerli {

//	private String cost;
	private SourceValue cost = new SourceValue("cost");
	private SourceValue copyrightAndOtherRestrictions = new SourceValue("copyrightAndOtherRestrictions");
	private LangStringList description = new LangStringList();
	private boolean lomEs;


	
	

	public Rights() {
		super();
//		this.cost = "";
		this.cost = new SourceValue("cost");
		this.copyrightAndOtherRestrictions = new SourceValue("copyrightAndOtherRestrictions");
		this.description = new LangStringList();
	}
	public Rights(SourceValue cost, SourceValue copyRightAndOtherRestrictions, LangStringList description) {
		super();
		this.cost = cost;
		this.copyrightAndOtherRestrictions = copyRightAndOtherRestrictions;
		this.description = description;
	}






	public Rights(SOAPElement seRights) throws SOAPException {
		
		Iterator it = seRights.getChildElements(soapFactory.createName("cost"));
		if (it.hasNext())
			//cost = ((SOAPElement) it.next()).getValue();
			cost = new SourceValue((SOAPElement) it.next());
		
		it = seRights.getChildElements(soapFactory.createName("copyrightAndOtherRestrictions"));
		if (it.hasNext())
			copyrightAndOtherRestrictions = new SourceValue((SOAPElement) it.next());
		
		it = seRights.getChildElements(soapFactory.createName("description"));
		if (it.hasNext())
			description = new LangStringList((SOAPElement) it.next());
	}
	
	
	public SourceValue getCopyRightAndOtherRestrictions() {
		return copyrightAndOtherRestrictions;
	}
	





	public void setCopyRightAndOtherRestrictions(
			SourceValue copyRightAndOtherRestrictions) {
		this.copyrightAndOtherRestrictions = copyRightAndOtherRestrictions;
	}
	





	public SourceValue getCost() {
		return cost;
	}
	





	public void setCost(SourceValue cost) {
		this.cost = cost;
	}
	





	public LangStringList getDescription() {
		return description;
	}
	public LangString getDescription(String lang) {
		return description.getLangString(lang);
	}
	





	public void setDescription(LangStringList description) {
		this.description = description;
	}
	public void setDescription(LangString description) {
		this.description.setLangString(description);
	}
	





	public SOAPElement toXml() throws SOAPException {
		SOAPElement seRights;
		SOAPElement seCost;
		//SOAPElement seRightsCDR;
		//SOAPElement seRightsPermission;
		//SOAPElement seRightsAction;
		SOAPElement seDescription;
		SOAPElement seRightsAccess;
		SOAPElement seRightsAccessType;
		SOAPElement seRightsAccessDescription;
		SourceValue accessType;
		
		seRights = soapFactory.createElement(soapFactory.createName("rights"));
		
		if(cost!=null && !cost.getValue().equals(""))
		{
			//if (cost == null) cost = new SourceValue("cost");//"";
	//		seCost = soapFactory.createElement(soapFactory.createName("cost"));
	//		seCost.addTextNode(cost);
			cost.setName("cost");
			seRights.addChildElement(cost.toXml());
		}
		
		
		if (copyrightAndOtherRestrictions == null){
			copyrightAndOtherRestrictions = new SourceValue("copyrightAndOtherRestrictions");
			if (lomEs)
				copyrightAndOtherRestrictions.setSource(APXTEC32.LOMes);
			else
				copyrightAndOtherRestrictions.setSource(APXTEC32.LOM_VALUE);
		}else{
			if (lomEs)
				copyrightAndOtherRestrictions.setSource(APXTEC32.LOMes);
		}
		copyrightAndOtherRestrictions.setName("copyrightAndOtherRestrictions");
		seRights.addChildElement(copyrightAndOtherRestrictions.toXml());

//		if (description == null) description = new LangString();
		seDescription = soapFactory.createElement(soapFactory.createName("description"));
//		seDescription.addChildElement(description.toXml());
//		seRights.addChildElement(seDescription);
		
		if (description != null && description.getListString().size()>0 && description.getString().equals(""))
		{
			Enumeration e = description.getListString().elements();
			while(e.hasMoreElements()){
				seDescription.addChildElement(((LangString) e.nextElement()).toXml());
			}
			seRights.addChildElement(seDescription);
		}
		
		/*seRightsCDR = soapFactory.createElement(soapFactory.createName("cdr"));
		seRightsPermission = soapFactory.createElement(soapFactory.createName("permission"));
		seRightsAction = soapFactory.createElement(soapFactory.createName("action"));
		seRightsAction.addTextNode(ACTION);
		seRightsPermission.addChildElement(seRightsAction);
		seRightsCDR.addChildElement(seRightsPermission);
		seRights.addChildElement(seRightsCDR);*/

		if (isLomEs()){
			seRightsAccess = soapFactory.createElement(soapFactory.createName("access"));
			seRightsAccess.addChildElement((new SourceValue(APXTEC32.LOMes,APXTEC32.UNIVERSAL, "accessType")).toXml());

			seRightsAccessDescription = soapFactory.createElement(soapFactory.createName("description"));
			seRightsAccessDescription.addChildElement((new LangString(LOMES.RIGHTS_ACCES_DESCRIPTION, APXTEC32.DEFAULT_LANG)).toXml());
			seRightsAccess.addChildElement(seRightsAccessDescription);
			
			seRights.addChildElement(seRightsAccess);
		}
		
		
		return seRights;		
	}


	public boolean isLomEs() {
		return lomEs;
	}
	public void setLomEs(boolean lomEs) {
		this.lomEs = lomEs;
	}
	public String toUrl2(String prefix) {
		
		String params="";
		
		if (cost == null) cost = new SourceValue("cost");
		cost.setName("cost");
		params+=cost.toUrl(prefix+"cost_");
				
		if (copyrightAndOtherRestrictions == null){
			copyrightAndOtherRestrictions = new SourceValue("copyrightAndOtherRestrictions");
			copyrightAndOtherRestrictions.setSource(APXTEC32.LOM_VALUE);
		}
		
		copyrightAndOtherRestrictions.setName("copyrightAndOtherRestrictions");
		params+=copyrightAndOtherRestrictions.toUrl(prefix+"restr_");

		if (description == null) description = new LangStringList();
		Enumeration e = description.getListString().elements();
		while(e.hasMoreElements())
			params+=((LangString) e.nextElement()).toUrl(prefix+"desc_");

		return params;
	}
	public String toUrl(String prefix) {
		String params="";
		WSMerliBD wsmbd = new WSMerliBD();
		
		if (cost == null) cost = new SourceValue("cost");
		if(cost.getValue().equals("yes")) params+="cost=1&";
		else params+="cost=0&";
				
		if (copyrightAndOtherRestrictions == null) copyrightAndOtherRestrictions = new SourceValue("copyrightAndOtherRestrictions");
		String llic = wsmbd.getLlicencia(copyrightAndOtherRestrictions.getValue());
		params+="llicencia="+copyrightAndOtherRestrictions.getId()+"&";
		//params+="llicencia="+llic+"&";
		

		if (description == null) description = new LangStringList();
		Enumeration e = description.getListString().elements();
		while(e.hasMoreElements())
			params+=((LangString) e.nextElement()).toUrl("descDrets");

		return params;
	}
	
}
