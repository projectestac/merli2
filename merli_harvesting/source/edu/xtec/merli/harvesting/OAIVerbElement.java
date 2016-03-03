package edu.xtec.merli.harvesting;

/*
 * OAIVerbElement.java
 *
 * Created on 2007/01/10
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jdom.Element;

public abstract class OAIVerbElement extends OAIElement {
	
	protected static String VERB_IDENTIFY = "Identify";
	protected static String VERB_LIST_METADATA_FORMATS = "ListMetadataFormats";
	protected static String VERB_LIST_SETS = "ListSets";
	protected static String VERB_GET_RECORD = "GetRecord";
	protected static String VERB_LIST_IDENTIFIERS = "ListIdentifiers";
	protected static String VERB_LIST_RECORDS = "ListRecords";
	
	protected Map mReqArgs = new HashMap();
			
	public OAIVerbElement(){
		this(new HashMap());
	}
	
	public OAIVerbElement(Map oArgs){
		super();
		setRequestArguments(oArgs);
		
		String[] sAll = getAllArguments();
		for (int i=0;i<sAll.length;i++){
			if (getArgument(sAll[i])!=null) getRequest().setAttribute(sAll[i], getArgument(sAll[i]));			
		}
	}
	
	public abstract String getVerb();
	
	protected abstract HashMap getArguments();
	
	protected String[] getArguments(String sType){
		String[] sArgs = new String[]{};
		if (sType!=null){
			if (getArguments().containsKey(sType)) sArgs = (String[])getArguments().get(sType);
		}
		return sArgs;
	}
	
	public ArrayList getAllArgumentsList(){
		ArrayList alArgs = new ArrayList();
		Iterator itArgs = getArguments().values().iterator();
		while (itArgs.hasNext()){
			String[] sTmp = (String[])itArgs.next();
			for (int i=0;i<sTmp.length;i++){
				alArgs.add(sTmp[i]);					
			}
		}
		return alArgs;
	}	
	
	
	public String[] getAllArguments(){
		ArrayList alArgs = getAllArgumentsList();
		String[] sArgs = new String[alArgs.size()];
		System.arraycopy(alArgs.toArray(), 0, sArgs, 0, alArgs.size());
		return sArgs;
	}	
	
	public String[] getRequiredArguments(){
		return getArguments("R");
	}

	public String[] getOptionalArguments(){
		return getArguments("O");
	}

	public String[] getExclusiveArguments(){
		return getArguments("E");
	}
	
	/**
	 * Check if request arguments are valid.
	 * 
	 * @param mParams Request parameters
	 * @return true if arguments are valid 
	 * @throws MerliHarvestingException if there is some invalid argument  
	 */
	public boolean checkArguments() throws MerliHarvestingException{
		boolean bOk = false;
		// Check exclusive arguments
		String[] aExclusive = getExclusiveArguments();
		String sExclusive = null;
		for (int i=0;i<aExclusive.length;i++){
			if (getRequestArguments().containsKey(aExclusive[i])) {
				sExclusive=aExclusive[i];
				break;
			}
		}
		if (sExclusive!=null){
			if (getRequestArguments().size()!=2 && !getRequestArguments().containsKey("verb")){
				throw new MerliHarvestingException(MerliHarvestingException.ERROR_BAD_ARGUMENT, "Argument '"+sExclusive+"' must be the only argument (in addition to the verb argument)", this);				
			}
		}else{
			String[] sRequired = getRequiredArguments();
			if (sRequired!=null){
				if (getRequestArguments().size()>=sRequired.length){
					bOk = true;
					// Check mandatory arguments
					for (int i=0;i<sRequired.length;i++){
						if (!getRequestArguments().containsKey(sRequired[i])) throw new MerliHarvestingException(MerliHarvestingException.ERROR_BAD_ARGUMENT, "Missing required arguments");
					}
					// Check all request parameters are valid verb arguments
					Iterator itArgs = getRequestArguments().keySet().iterator();
					while (itArgs.hasNext()){
						String sArg = (String)itArgs.next();
						if (!getAllArgumentsList().contains(sArg)) throw new MerliHarvestingException(MerliHarvestingException.ERROR_BAD_ARGUMENT, "Illegal argument '"+sArg+"'", this);
						if ("set".equals(sArg)) throw new MerliHarvestingException(MerliHarvestingException.ERROR_NO_SET_HIERARCHY, this);
						if ("metadataPrefix".equals(sArg)) ckeckMetadataPrefixArgument(getArgument(sArg));
					}
				}else{
					throw new MerliHarvestingException(MerliHarvestingException.ERROR_BAD_ARGUMENT);				
				}
			}else{
				throw new MerliHarvestingException(MerliHarvestingException.ERROR_BAD_VERB);
			}
		}
		return bOk;
	}
	
	protected boolean ckeckMetadataPrefixArgument(String sValue) throws MerliHarvestingException{
		if (!OAIListMetadataFormatsElement.getMetadataPrefixs().contains(sValue)){
			throw new MerliHarvestingException(MerliHarvestingException.ERROR_CANNOT_DISSEMINATE_FORMAT, this);				
		}
		return true;
	}
	
	protected boolean checkDateArguments(String sFrom, String sUntil) throws MerliHarvestingException{
		boolean bOk = false;
		// from && until format
		Date dFrom, dUntil = null;
		try{
			dFrom = stringToDate(sFrom);
		}catch (ParseException pe){
			throw new MerliHarvestingException(MerliHarvestingException.ERROR_BAD_ARGUMENT, "Illegal from argument");
		}
		try{
			dUntil = stringToDate(sUntil);
		}catch (ParseException pe){
			throw new MerliHarvestingException(MerliHarvestingException.ERROR_BAD_ARGUMENT, "Illegal until argument");
		}
		
		// from < until?
		if (dFrom!=null && dUntil!=null){
			if (dFrom.compareTo(dUntil)>0) throw new MerliHarvestingException(MerliHarvestingException.ERROR_BAD_ARGUMENT, "Illegal from and until arguments: from must be lower than until");
		}
		bOk = true;
		return bOk;
	}
		
	
	public static OAIVerbElement getOAIVerbElement(Map oArgs, String sRequestURL) throws MerliHarvestingException{
		OAIVerbElement eVerb = null;
		String sVerb = getArgument(oArgs, "verb");
		if (VERB_GET_RECORD.equals(sVerb)){
			eVerb = new OAIGetRecordElement(oArgs);
		}else if (VERB_IDENTIFY.equals(sVerb)){
			eVerb = new OAIIdentifyElement(oArgs);
			((OAIIdentifyElement)eVerb).setBaseURL(sRequestURL);
		}else if (VERB_LIST_IDENTIFIERS.equals(sVerb)){
			eVerb = new OAIListIdentifiersElement(oArgs);
		}else if (VERB_LIST_RECORDS.equals(sVerb)){
			eVerb = new OAIListRecordsElement(oArgs);
		}else if (VERB_LIST_METADATA_FORMATS.equals(sVerb)){
			eVerb = new OAIListMetadataFormatsElement(oArgs);
		}else if (VERB_LIST_SETS.equals(sVerb)){
			eVerb = new OAIListSetsElement(oArgs);
		}else{
			throw new MerliHarvestingException(MerliHarvestingException.ERROR_BAD_VERB);
		}
		eVerb.setRequestArguments(oArgs);
		return eVerb;
	}
	
	public static Element createRecordElement(Element eHeader, Element eMetadataContent){
		Element eRecord = null;
		if (eHeader!=null && eMetadataContent!=null){
			eRecord = new Element("record", XMLNS);
			eRecord.addContent(eHeader);
			Element eMetadata = new Element("metadata", XMLNS);
			eMetadata.addContent(eMetadataContent);
			eRecord.addContent(eMetadata);
		}
		return eRecord;
	}
	
	public Map getRequestArguments(){
		return this.mReqArgs;
	}
	public void setRequestArguments(Map oArgs){
		this.mReqArgs = oArgs;
	}
	
	public String getArgument(String sName){
		return getArgument(getRequestArguments(), sName);
	}
	
	protected static String getArgument(Map oArgs, String sName){
		String sValue = null;
		try{
			if (oArgs.containsKey(sName)) sValue = ((String[])oArgs.get(sName))[0];
		}catch (Exception e){
			logger.error("EXCEPTION getting argument '"+sName+"'");
		}
		return sValue;
	}
	
}
