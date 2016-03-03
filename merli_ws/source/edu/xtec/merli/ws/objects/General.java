package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import edu.xtec.merli.utils.Utility;

public class General extends ObjectMerli {
	
	private Identifier identifier;
	private LangString title;
	private ArrayList languageList;
	private LangString description;
	
	private static final int MAX_DESC =2000;
	private static final int MAX_TITLE = 1000;

	
	public General() {
		super();
		// TODO Auto-generated constructor stub
		this.identifier = new Identifier();
		this.title = new LangString();
		this.languageList = new ArrayList();
		this.description = new LangString();
	}
	
	public General(Identifier identifier, LangString title, ArrayList languageList, LangString description) {
		super();
		// TODO Auto-generated constructor stub
		this.identifier = identifier;
		this.title = title;
		this.title.setString(Utility.controlarString(title.getString(),General.MAX_TITLE));	
		this.languageList = languageList;
		this.description = description;
		this.description.setString(Utility.controlarString(description.getString(),General.MAX_DESC));
	}




	public LangString getDescription() {
		return description;
	}
	



	public void setDescription(LangString description) {
		this.description = description;
		this.description.setString(Utility.controlarString(description.getString(),General.MAX_DESC));
	}
	




	public Identifier getIdentifier() {
		return identifier;
	}
	



	public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
	}
	



	public ArrayList getLanguageList() {
		return languageList;
	}
	



	public void setLanguageList(ArrayList languageList) {
		this.languageList = languageList;
	}
	



	public LangString getTitle() {
		return title;
	}
	



	public void setTitle(LangString title) {
		this.title = title;
		this.title.setString(Utility.controlarString(title.getString(),General.MAX_TITLE));			
	}
	



	public General(SOAPElement seGeneral) throws SOAPException {
		// TODO Auto-generated constructor stub
		languageList = new ArrayList();
		
        Iterator It=seGeneral.getChildElements(soapFactory.createName("identifier"));
		if (It.hasNext())
			identifier= new Identifier(((SOAPElement)It.next()));
//		else
//			identifier = new Identifier();
		
		It=seGeneral.getChildElements(soapFactory.createName("title"));
		if (It.hasNext()){
			title= new LangString(((SOAPElement)It.next()));
			this.title.setString(Utility.controlarString(title.getString(),General.MAX_TITLE));	
		}
//		else
//			title = new LangString();
		
		It=seGeneral.getChildElements(soapFactory.createName("language"));
        while (It.hasNext())
			languageList.add(((SOAPElement)It.next()).getValue());
		
		It=seGeneral.getChildElements(soapFactory.createName("description"));
		if (It.hasNext()){
			description= new LangString(((SOAPElement)It.next()));
			this.description.setString(Utility.controlarString(description.getString(),General.MAX_DESC));
		}
//		else
//			description = new LangString();
	}



	public SOAPElement toXml() throws SOAPException {

		SOAPElement seGeneral=null;
		SOAPElement seLanguage;
		SOAPElement seTitle;
		SOAPElement seDescription;
	    
		//Crea l'element general
		seGeneral= soapFactory.createElement(soapFactory.createName("general"));
		
		//Afageix l'element identifier al general	
		if (identifier == null) identifier = new Identifier();
		seGeneral.addChildElement(this.identifier.toXml());

		//Afageix l'element Title al general
		if (title == null) title = new LangString();
		seTitle= soapFactory.createElement(soapFactory.createName("title"));
		seTitle.addChildElement(this.title.toXml());
		seGeneral.addChildElement(seTitle);

		//Afageix l'element Language al general
		if (languageList == null) languageList = new ArrayList();
		for (int i = 0; i < this.languageList.size(); i++){
			seLanguage= soapFactory.createElement(soapFactory.createName("language"));
			seLanguage.addTextNode((String) this.languageList.get(i));
			seGeneral.addChildElement(seLanguage);			
		}

		//Afageix l'element Description al general
		if (description == null) description = new LangString();
		seDescription = soapFactory.createElement(soapFactory.createName("description"));
		seDescription.addChildElement(this.description.toXml());
		seGeneral.addChildElement(seDescription);
		
		return seGeneral;
	}
//
//	public String controlarString(String text, int max) {
//		String curt=text;
//		if (curt != null){
//			if (curt.length()>max){
//				curt = curt.substring(0,max - 6);
//				curt +="[...]";				
//			}
//		}
//		return curt;
//	}
}
