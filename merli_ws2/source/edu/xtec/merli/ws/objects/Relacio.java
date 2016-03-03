package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Relacio extends ObjectMerli {

	private Integer idRecursRel;
	private String kind;
	private LangStringList description;
	
	public Relacio() {
		super();
		this.idRecursRel = new Integer(0);
		this.kind = "";
		this.description = new LangStringList();
	}
	
	public Relacio(Integer idRecurs, String kind) {
		this(idRecurs, kind, new LangStringList());
	}

	public Relacio(Integer idRecurs, String kind, LangStringList descripcio) {
		super();
		this.idRecursRel = idRecurs;
		this.kind = kind;
		this.description = descripcio;
	}	
	

	public Integer getIdRecursRel() {
		return idRecursRel;
	}

	public void setIdRecursRel(Integer idRecursRel) {
		this.idRecursRel = idRecursRel;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
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


	public Relacio(SOAPElement seRelacions) throws SOAPException {
		
        Iterator It=seRelacions.getChildElements(soapFactory.createName("resource"));
		if (It.hasNext())
			idRecursRel= new Integer(((SOAPElement)It.next()).getValue());
		
		It=seRelacions.getChildElements(soapFactory.createName("kind"));
		if (It.hasNext())
			kind= new String(((SOAPElement)It.next()).getValue());
		
		It=seRelacions.getChildElements(soapFactory.createName("description"));
		if (It.hasNext())
			description= new LangStringList(((SOAPElement)It.next()));
	}



	public SOAPElement toXml() throws SOAPException {

		SOAPElement seRelacions=null;
		SOAPElement seIdentifier;
		SOAPElement seKind;
		SOAPElement seDescription;
	    
		//Crea l'element relacions
		seRelacions= soapFactory.createElement(soapFactory.createName("relation"));
		
		//Afegeix l'element kind
		seKind = soapFactory.createElement(soapFactory.createName("kind"));
		if (kind == null) kind = "";
		seKind.addTextNode(kind);
		seRelacions.addChildElement(seKind);
		
		//Afageix l'element identificador del recurs relacionat	
		if (idRecursRel == null) idRecursRel = new Integer(0);
		seIdentifier = soapFactory.createElement("resource");
		seIdentifier.addTextNode(String.valueOf(this.idRecursRel));
		seRelacions.addChildElement(seIdentifier);

		//Afageix l'element Description de la relacio
		seDescription = soapFactory.createElement(soapFactory.createName("description"));
		if (description == null) description = new LangStringList();
		Enumeration e1 = description.getListString().elements();
		while(e1.hasMoreElements()){
			seDescription.addChildElement(((LangString) e1.nextElement()).toXml());
		}
		seRelacions.addChildElement(seDescription);

		return seRelacions;
	}

	public String toUrl(String prefix) {
		String params="";
		
		//Afegeix l'element kind
		if (kind == null) kind = "";
		params+="tipusRelSel="+kind+"&";
		
		//Afageix l'element identificador del recurs relacionat	
		if (idRecursRel == null) idRecursRel = new Integer(0);
		params+="recRel="+this.idRecursRel+"&";

		//Afageix l'element Description de la relacio
		if (description == null) description = new LangStringList();
		Enumeration e1 = description.getListString().elements();
		while(e1.hasMoreElements())
			params+=((LangString) e1.nextElement()).toUrl("descRel");
		
		return params;
	}


}
