package edu.xtec.merli.ws.objects;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class LangStringList extends ObjectMerli {

	private Hashtable listString;
	private String sListName = "langlistname";
	

	public LangStringList() {
		super();
		this.listString = new Hashtable();
	}
	public LangStringList(Hashtable list) {
		super();
		this.listString = list;
	}




	public LangStringList(SOAPElement seLangStringList) throws SOAPException {
		// TODO Auto-generated constructor stub
//		SOAPElement seString;
//		
//		Iterator It=seLangString.getChildElements(soapFactory.createName("string"));
//		if (It.hasNext()){
//			seString = ((SOAPElement)It.next());
//			string= seString.getValue();		
//			lang = seString.getAttributeValue(soapFactory.createName("language"));
//		}
		SOAPElement seString;
		
		Iterator It=seLangStringList.getChildElements(soapFactory.createName("string"));
		listString = new Hashtable();
		LangString ls;
//		while (It.hasNext()){
//			ls = new LangString((SOAPElement)It.next());
//			listString.put(ls.getLang(),ls);
//		}
		while (It.hasNext()){
			seString = ((SOAPElement)It.next());
			ls = new LangString(seString.getValue(),seString.getAttributeValue(soapFactory.createName("language")));
			//listString.put(ls.getLang(),ls);
			this.setLangString(ls);
		}
	}

	public Hashtable getListString() {
		return listString;
	}
	
	public void setListString(Hashtable listString) {
		this.listString = listString;
	}
	
	public LangString getLangString(String lang){
		return (LangString) listString.get(lang);
	}
	public LangString getLangString(){
		return getLangString(APXTEC32.DEFAULT_LANG);
	}

	public void setLangString(LangString langString){
		if (langString != null && langString.getLang() !=null && langString.getString() != null)
			listString.put(langString.getLang(),langString);
	}

	public void setString(String langString){
		setString(langString,APXTEC32.DEFAULT_LANG);
	}
	public void setString(String langString,String lang){
		listString.put(lang,new LangString(langString,lang));
	}
	
	public String getString(){
		return getString(APXTEC32.DEFAULT_LANG);
	}
	public String getString(String lang){
		if (listString.contains(lang))
			return ((LangString)listString.get(lang)).getString();
		return "";
	}
	
	public String getLangStringListName(){
		return this.sListName;
	}
	
	public SOAPElement toXml() throws SOAPException {
		return toXml(getLangStringListName());
	}
	
	
	public SOAPElement toXml(String sName) throws SOAPException {
		SOAPElement seLangString = soapFactory.createElement(soapFactory.createName(sName));
		Enumeration enumList = getListString().keys();
		while (enumList.hasMoreElements()){
			String sLang = (String)enumList.nextElement();
			LangString oLangString = getLangString(sLang);
			seLangString.addChildElement(oLangString.toXml());			
		}
		return seLangString;
	}
	public String toUrl(String prefix) {
		String params="";
		
		Enumeration enumList = getListString().keys();
		while (enumList.hasMoreElements()){
			String sLang = (String)enumList.nextElement();
			LangString oLangString = getLangString(sLang);
			params+=oLangString.toUrl(prefix);
		}
		return params;
	}

	
	
//	public SOAPElement toXml() throws SOAPException {
//		SOAPElement langString;	
//		
//		//Crea l'element string
//		langString = soapFactory.createElement(soapFactory.createName("string"));
//		//Posa l'atribut lang
//		langString.addAttribute(soapFactory.createName("language"),lang);
//		//Afageix el text
//		if (string == null) string = "";
//		langString.addTextNode(string);
//		
//		
//		return langString;
//		
//	}

}
