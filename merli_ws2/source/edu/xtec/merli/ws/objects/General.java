package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.ws.WSMerliBD;

public class General extends ObjectMerli {

	private Identifier identifier = new Identifier();
	private LangStringList title = new LangStringList();
	private ArrayList languageList = new ArrayList();
	private LangStringList description = new LangStringList();
	private ArrayList keywordList = new ArrayList();
	private SourceValue aggregationLevel = new SourceValue("aggregationLevel");
	private LangStringList coverage = new LangStringList();
	

	
	public General() {
		super();
		this.identifier = new Identifier();
		this.title = new LangStringList();
		this.languageList = new ArrayList();
		this.description = new LangStringList();
		this.keywordList = new ArrayList();
		this.aggregationLevel = new SourceValue("aggregationLevel");
		this.coverage = new LangStringList();
	}

	public General(Identifier identifier, LangStringList title, ArrayList languageList, LangStringList description) {
		this(identifier, title, languageList, description, new ArrayList(), new LangStringList());
	}
	
	public General(Identifier identifier, LangStringList title, ArrayList languageList, LangStringList description, ArrayList keywordList, LangStringList coverage) {
		this(identifier, title, languageList, description, keywordList, new SourceValue("aggregationLevel"), coverage);
	}	
	
	public General(Identifier identifier, LangStringList title, ArrayList languageList, LangStringList description, ArrayList keywordList, SourceValue aggregationLevel, LangStringList coverage) {
		super();
		this.identifier = identifier;
		this.title = title;
		this.languageList = languageList;
		this.description = description;
		this.keywordList = keywordList;
		this.aggregationLevel = aggregationLevel;
		this.coverage = new LangStringList();
	}	

	
	public LangStringList getTitle() {
		return title;
	}
	public LangString getTitle(String lang) {
		return title.getLangString(lang);
	}

	public void setTitle(LangStringList title) {
		this.title = title;
	}
	public void setTitle(LangString title) {
		this.title.setLangString(title);
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
	
	public ArrayList getKeywordList() {
		return this.keywordList;
	}

	public void setKeywordList(ArrayList keywordList) {
		this.keywordList = keywordList;
	}
	
	public SourceValue getAggregationLevel() {
		return aggregationLevel;
	}

	public void setAggregationLevel(SourceValue aggregationLevel) {
		this.aggregationLevel = aggregationLevel;
	}
	
	public LangStringList getCoverage() {
		return coverage;
	}
	
	public void setCoverage(LangStringList coverage) {
		this.coverage = coverage;
	}

	public LangString getCoverage(String lang) {
		return coverage.getLangString(lang);
	}

	public void setCoverage(LangString coverage) {
		this.coverage.setLangString(coverage);
	}


	public General(SOAPElement seGeneral) throws SOAPException {
		languageList = new ArrayList();
		keywordList = new ArrayList();
		
        Iterator It=seGeneral.getChildElements(soapFactory.createName("identifier"));
		if (It.hasNext())
			identifier= new Identifier(((SOAPElement)It.next()));
//		else
//			identifier = new Identifier();
		
		It=seGeneral.getChildElements(soapFactory.createName("title"));
		if (It.hasNext())
			title= new LangStringList(((SOAPElement)It.next()));
//		else
//			title = new LangString();
		
		It=seGeneral.getChildElements(soapFactory.createName("language"));
        while (It.hasNext())
			languageList.add(((SOAPElement)It.next()).getValue());
		
		It=seGeneral.getChildElements(soapFactory.createName("description"));
		if (It.hasNext())
			description= new LangStringList(((SOAPElement)It.next()));
		
		It=seGeneral.getChildElements(soapFactory.createName("keyword"));
		while (It.hasNext()){
			SOAPElement se = (SOAPElement)It.next();
			try{
			keywordList.add(new LangStringList(se));//se.getFirstChild().getTextContent());
			}catch(Exception e){
				keywordList.add("error:"+e.getMessage());
			}
		}
		
//		else 
//			description = new LangString();
		It=seGeneral.getChildElements(soapFactory.createName("aggregationLevel"));
		if (It.hasNext())
			aggregationLevel = new SourceValue(((SOAPElement)It.next()));
		aggregationLevel.setName("aggregationLevel");
		
		It=seGeneral.getChildElements(soapFactory.createName("coverage"));
		if (It.hasNext())
			coverage= new LangStringList(((SOAPElement)It.next()));
		
	}

	public SOAPElement toXml() throws SOAPException {

		SOAPElement seGeneral=null;
		SOAPElement seLanguage;
		SOAPElement seTitle;
		SOAPElement seDescription;
		SOAPElement seCoverage;
	    
		//Crea l'element general
		seGeneral= soapFactory.createElement(soapFactory.createName("general"));
		
		//Afageix l'element identifier al general	
		if (identifier == null) identifier = new Identifier();
		seGeneral.addChildElement(this.identifier.toXml());

		//Afegeix l'element Title al general
		seTitle = soapFactory.createElement(soapFactory.createName("title"));
		if (title == null) title = new LangStringList();
		Enumeration e = title.getListString().elements();
		while(e.hasMoreElements()){
			seTitle.addChildElement(((LangString) e.nextElement()).toXml());
		}
		seGeneral.addChildElement(seTitle);
	
		
		//Afageix l'element Language al general. Si no nhia cap s'afageix el lang x defecte.
		if (languageList != null && languageList.size()>0){
			for (int i = 0; i < this.languageList.size(); i++){
				seLanguage= soapFactory.createElement(soapFactory.createName("language"));
				seLanguage.addTextNode((String) this.languageList.get(i));
				seGeneral.addChildElement(seLanguage);			
			}
		}else{
			seLanguage= soapFactory.createElement(soapFactory.createName("language"));
			seLanguage.addTextNode(APXTEC32.DEFAULT_LANG);
			seGeneral.addChildElement(seLanguage);
		}

		//Afageix l'element Description al general
		if(description != null && description.getLangString(APXTEC32.DEFAULT_LANG)!=null && description.getLangString(APXTEC32.DEFAULT_LANG).getString()!=null && !description.getLangString(APXTEC32.DEFAULT_LANG).getString().trim().equals(""))
		{
			seDescription = soapFactory.createElement(soapFactory.createName("description"));
		//	if (description == null) description = new LangStringList();
			Enumeration e1 = description.getListString().elements();
			while(e1.hasMoreElements()){
				seDescription.addChildElement(((LangString) e1.nextElement()).toXml());
			}
			seGeneral.addChildElement(seDescription);
		}

		//Afageix l'element keyword al general
		if (keywordList == null) keywordList = new ArrayList();
		for (int i = 0; i < this.keywordList.size(); i++){
			if(this.keywordList.get(i)!=null)
			{
				LangString aux;
				SOAPElement seKeyword = soapFactory.createElement(soapFactory.createName("keyword"));
				if(this.keywordList.get(i) instanceof String)
				{
					String sKeyword = (String) this.keywordList.get(i);
	//				seKeyword.addTextNode(sKeyword);
					aux = new LangString(sKeyword,"ca");
	//				seKeyword.addChildElement(aux.toXml());
	//				aux=new LangString(sKeyword,"es");
	//				seKeyword.addChildElement(aux.toXml());
	//				aux=new LangString(sKeyword,"en");
	//				seKeyword.addChildElement(aux.toXml());
	//				aux=new LangString(sKeyword,"oc");
	//				seKeyword.addChildElement(aux.toXml());
				}
				else
				{
					aux = ((LangStringList) this.keywordList.get(i)).getLangString();
				}
				seKeyword.addChildElement(aux.toXml());
				seGeneral.addChildElement(seKeyword);
			}
		}

		//Afageix l'element aggregationLevel
		if (aggregationLevel != null)
		{
			//if (aggregationLevel == null) aggregationLevel = new SourceValue("aggregationLevel");
			aggregationLevel.setName("aggregationLevel");
			seGeneral.addChildElement(aggregationLevel.toXml());
		}

//		Afageix l'element Coverage (context) al general
		if (coverage != null && coverage.getListString().size()>0)
		{
			seCoverage = soapFactory.createElement(soapFactory.createName("coverage"));
			if (coverage == null) coverage = new LangStringList();
			Enumeration e2 = coverage.getListString().elements();
			while(e2.hasMoreElements()){
				seCoverage.addChildElement(((LangString) e2.nextElement()).toXml());
			}
			seGeneral.addChildElement(seCoverage);
		}
		return seGeneral;
	}
	
	public String toUrl2(String prefix) {
		String params_general="";
		
		if (identifier == null) identifier = new Identifier();
		params_general+=this.identifier.toUrl(prefix+"id_");

		//Afegeix l'element Title al general
		if (title == null) title = new LangStringList();
		Enumeration e = title.getListString().elements();
		while(e.hasMoreElements())
			params_general+=((LangString) e.nextElement()).toUrl(prefix+"tit_");
		
		//Afageix l'element Language al general. Si no n'hi ha cap s'afageix el lang x defecte.
		if (languageList != null && languageList.size()>0)
			for (int i = 0; i < this.languageList.size(); i++)
				params_general+=(prefix+"l"+i+"="+(String) this.languageList.get(i)+"&");
		else
			params_general+=(prefix+"l0="+APXTEC32.DEFAULT_LANG+"&");

		//Afageix l'element Description al general
		if (description == null) description = new LangStringList();
		Enumeration e1 = description.getListString().elements();
		while(e1.hasMoreElements())
			params_general+=((LangString) e1.nextElement()).toUrl(prefix+"desc_");

		//Afageix l'element keyword al general
		if (keywordList == null) keywordList = new ArrayList();
		for (int i = 0; i < this.keywordList.size(); i++){
			LangStringList lKeyword = (LangStringList) this.keywordList.get(i);
			Enumeration eK = lKeyword.getListString().elements();
			while(eK.hasMoreElements())
				params_general+=((LangString) eK.nextElement()).toUrl(prefix+"k"+i+"_");		
		}

		//Afageix l'element aggregationLevel
		if (aggregationLevel == null) aggregationLevel = new SourceValue("aggregationLevel");	
		aggregationLevel.setName("aggregationLevel");
		params_general+=this.aggregationLevel.toUrl(prefix+"al_");

//		Afageix l'element Coverage (context) al general
		if (coverage == null) coverage = new LangStringList();
		Enumeration e2 = coverage.getListString().elements();
		while(e2.hasMoreElements())
			params_general+=((LangString) e2.nextElement()).toUrl(prefix+"cov_");
		return params_general;
	}
	
	public String toUrl(String prefix) {
		String params="";
		
		//Afegeix l'element Title al general
		if (title == null) title = new LangStringList();
		Enumeration e = title.getListString().elements();
		while(e.hasMoreElements())
			params+=((LangString) e.nextElement()).toUrl("titol");
		
		//Afageix l'element Language al general. Si no n'hi ha cap s'afageix el lang x defecte.
		if (languageList != null && languageList.size()>0)
			for (int i = 0; i < this.languageList.size(); i++)
				params+=("llengues="+(String) this.languageList.get(i)+"&");
		else
			params+=("llengues="+APXTEC32.DEFAULT_LANG+"&");

		//Afageix l'element Description al general
		if (description == null) description = new LangStringList();
		Enumeration e1 = description.getListString().elements();
		while(e1.hasMoreElements())
			params+=((LangString) e1.nextElement()).toUrl("descripcio");		

//		Afageix l'element Coverage (context) al general
		if (coverage == null) coverage = new LangStringList();
		Enumeration e2 = coverage.getListString().elements();
		while(e2.hasMoreElements())
			params+=((LangString) e2.nextElement()).toUrl("context2");
		
		String selecTerm="",selecLabel="";
		if (getKeywordList() == null) setKeywordList(new ArrayList());
		for (int i = 0; i < getKeywordList().size(); i++)
		{
			LangStringList lKeyword = (LangStringList) getKeywordList().get(i);
			Enumeration eK = lKeyword.getListString().elements();
			while(eK.hasMoreElements())
			{
				//selecTerm+="0;";
				WSMerliBD bd=new WSMerliBD();
				String id;
				String paraula=((LangString) eK.nextElement()).getString();
				try {
					id = bd.getIdParaulaByParaula(paraula);
				} catch (MerliDBException e3) {
					id="0";
				}
				selecTerm+=id+";";
				selecLabel+=paraula+";";
			}
		}
		if(selecTerm.length()>0) {selecTerm=selecTerm.substring(0, selecTerm.length()-1);selecLabel=selecLabel.substring(0, selecLabel.length()-1);}
		params+="selecParaulaId="+selecTerm+"&selecParaula="+selecLabel+"&";
		
		return params;
	}
}
