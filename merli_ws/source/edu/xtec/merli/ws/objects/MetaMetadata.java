package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class MetaMetadata extends ObjectMerli {

	private Identifier identifier;
	private ArrayList contributeList;
	private String metadataSchema;
	private String language;
	private static final String DEFAULT_LANG = "ca";
	
	
	

	public MetaMetadata() {
		super();
		// TODO Auto-generated constructor stub
		this.identifier = new Identifier();
		this.contributeList = new ArrayList();
		this.metadataSchema = "";
		this.language = "";
	}
	public MetaMetadata(Identifier identifier, ArrayList contributeList, String metadataSchema, String language) {
		super();
		// TODO Auto-generated constructor stub
		this.identifier = identifier;
		this.contributeList = contributeList;
		this.metadataSchema = metadataSchema;
		this.language = language;
	}





	public MetaMetadata(SOAPElement seMetaMetaData) throws SOAPException {
		
		Iterator It=seMetaMetaData.getChildElements(soapFactory.createName("identifier"));
		if (It.hasNext())
			identifier = new Identifier((SOAPElement)It.next());
		
		contributeList = new ArrayList();
		It=seMetaMetaData.getChildElements(soapFactory.createName("contribute"));
		while (It.hasNext())
			contributeList.add(new Contribute((SOAPElement)It.next()));
		
		It=seMetaMetaData.getChildElements(soapFactory.createName("metadataSchema"));
        if (It.hasNext())
			metadataSchema = ((SOAPElement)It.next()).getValue();
		
		It=seMetaMetaData.getChildElements(soapFactory.createName("language"));
		if (It.hasNext())
			language = ((SOAPElement)It.next()).getValue();
		
	}
	
	
	
	public ArrayList getContributeList() {
		return contributeList;
	}
	




	public void setContributeList(ArrayList contributeList) {
		this.contributeList = contributeList;
	}
	




	public Identifier getIdentifier() {
		return identifier;
	}
	




	public void setIdentifier(Identifier identifier) {
		this.identifier = identifier;
	}
	




	public String getLanguage() {
		return language;
	}
	




	public void setLanguage(String language) {
		this.language = language;
	}
	




	public String getMetadataSchema() {
		return metadataSchema;
	}
	




	public void setMetadataSchema(String metadataSchema) {
		this.metadataSchema = metadataSchema;
	}
	




	public SOAPElement toXml() throws SOAPException {
		SOAPElement seMetaMetaData;
		SOAPElement seMetaDataSchema;
		SOAPElement seLanguage;
		
		//Crea l'element metaMetaData;
		seMetaMetaData = soapFactory.createElement(soapFactory.createName("metaMetadata"));
		
		//Afageix l'element Identifier		
		if (identifier == null) identifier = new Identifier();
		seMetaMetaData.addChildElement(identifier.toXml());

		//Afageix el llistat d'elements de contributeList
		if (contributeList == null) contributeList = new ArrayList();
		for (int i=0; i < this.contributeList.size();i++){			
			seMetaMetaData.addChildElement(((Contribute)this.contributeList.get(i)).toXml());
		}

		//Afageix l'element language
		if (language == null) language = "";
		seLanguage = soapFactory.createElement(soapFactory.createName("language"));
		if ("".equals(language))
			language = DEFAULT_LANG;		
		seLanguage.addTextNode(this.language);
		seMetaMetaData.addChildElement(seLanguage);
		
		//Afageix l'element metadataSchema
		//seMetaDataSchema = soapFactory.createElement("metadataSchema");
		//seMetaDataSchema.addTextNode(this.metadataSchema);
		//seMetaMetaData.addChildElement(seMetaDataSchema);
		
		return seMetaMetaData;
	}

}
