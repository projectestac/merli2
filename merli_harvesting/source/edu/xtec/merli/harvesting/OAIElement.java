package edu.xtec.merli.harvesting;

/*
 * OAIElement.java
 *
 * Created on 2007/01/10
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import edu.xtec.merli.harvesting.db.MerliHarvestingDB;

public abstract class OAIElement extends Element {

	public static final String SETTINGS_PATH="/edu/xtec/resources/properties/";
	public static final String SETTINGS_FILE="merli_harvesting.properties";
	public static String XMLNS = "http://www.openarchives.org/OAI/2.0/";
	protected Namespace XSINS = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
	public static Namespace OAINS = Namespace.getNamespace(XMLNS);
	
	public static String UTC_SIMPLE = "yyyy-MM-dd";
	public static String UTC_COMPLETE = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	public static String OAIID_XMLNS = "http://www.openarchives.org/OAI/2.0/oai-identifier";
	

	protected static Logger logger = Logger.getRootLogger();
	private MerliHarvestingDB oDB;
	private static Properties settings;
	

	public OAIElement(){
		super("OAI-PMH", XMLNS);
		addNamespaceDeclaration(XSINS);
		setAttribute(new Attribute("schemaLocation", "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd", XSINS));
		
		Element eDate = new Element("responseDate", XMLNS);
		eDate.addContent(dateToString(new Date(), UTC_COMPLETE));
		addContent(eDate);
		
		Element eRequest = new Element("request", XMLNS);
		addContent(eRequest);
	}
	
	public Element getRequest(){
		return getChild("request", OAINS);
	}
	
	public void setRequest(Element eRequest){
		Iterator itAtts = eRequest.getAttributes().iterator();
		while (itAtts.hasNext()){
			Attribute oAtt = (Attribute)(((Attribute)itAtts.next()).clone());
			getRequest().setAttribute(oAtt);
		}
		getRequest().setText(eRequest.getText());
	}
	
	public boolean checkArguments() throws MerliHarvestingException{
		return true;
	}

	public abstract void generateResponse() throws MerliHarvestingException;
	
//	************************************	
//	* Common getters
//	************************************
	
	public static String getGranularity(){
		return getProperty("identify.granularity");
	}	
	
	public static String getScheme(){
		return getProperty("identify.scheme");
	}
	public static String getRepositoryIdentifier(){
		return getProperty("identify.repositoryIdentifier");
	}
	public static String getDelimiter(){
		return getProperty("identify.delimiter");
	}
	
	
//	************************************	
//	* Utils
//	************************************
	
	public static String dateToString(Date dDate){
		String sFormat = UTC_SIMPLE;
		if (getGranularity().length()>10) sFormat = UTC_COMPLETE;
		return dateToString(dDate, sFormat);
	}
	public static String dateToString(Date dDate, String sFormat){
		String sDate = null;
		if (dDate!=null){
	        SimpleDateFormat sdf = new SimpleDateFormat(sFormat);
	        sDate = sdf.format(dDate);
		}
		return sDate;
	}
	
	protected Date stringToDate(String sDate) throws ParseException{
		Date oDate = null;
		if (sDate!=null && sDate.length()>0){
			SimpleDateFormat sdf = new SimpleDateFormat(UTC_SIMPLE);
			if (sDate.trim().length()>10) sdf = new SimpleDateFormat(UTC_COMPLETE);
			oDate = sdf.parse(sDate);
		}
		return oDate;
	}
	
	public static String internalIdToIdentifier(String sInternalId){
		return getScheme()+getDelimiter()+getRepositoryIdentifier()+getDelimiter()+"merli/"+sInternalId;
	}
	
	public static String externalToInternalId(String sIdentifier){
		String sInternal = null;
		if (sIdentifier!=null){			
			String sBase = getScheme()+getDelimiter()+getRepositoryIdentifier()+getDelimiter()+"merli/";
			if (sIdentifier.indexOf(sBase)==0){
				int iLength = sBase.length();
				if (sIdentifier.length()>=iLength) sInternal = sIdentifier.substring(iLength);
			}
		}
		return sInternal;
	}
	
	public static boolean isNotNull(String s){
		return s!=null && s.trim().length()>0;
	}
	
	public static String getProperty(String sName){
		return getProperties().getProperty(sName);
	}
	
	public static int getIntProperty(String sName){
		int iProperty = -1;
		try{
			iProperty = Integer.parseInt(getProperty(sName).trim());
		}catch(Exception e){
			logger.error("ERROR getting INTEGER property '"+sName+"' -> "+e.toString());
		}
		return iProperty;
	}
	
	
	protected static Properties getProperties(){
		if (settings==null){
			settings=new Properties();
			try{
				settings.load(OAIElement.class.getResourceAsStream(SETTINGS_PATH+SETTINGS_FILE));
			} catch(Exception e){
				logger.fatal("ERROR! Can't read properties file "+SETTINGS_PATH+SETTINGS_FILE);
				e.printStackTrace();
			}			
		}
		return settings;
	}
	
	protected MerliHarvestingDB getDB(){
		if (oDB==null){
			oDB = new MerliHarvestingDB();
			//oDB.setDBFormatGranularity(getDBFormatGranularity());
		}
		return oDB;
	}
	
	

}
