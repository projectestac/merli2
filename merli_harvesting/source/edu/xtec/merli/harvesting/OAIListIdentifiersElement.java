package edu.xtec.merli.harvesting;

/*
 * OAIListIdentifiersElement.java
 *
 * Created on 2007/01/12
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.jdom.Element;

import edu.xtec.merli.harvesting.util.ResourceIdentifier;
import edu.xtec.merli.harvesting.util.ResumptionToken;

public class OAIListIdentifiersElement extends OAIVerbElement {
	
	protected static HashMap hArgs;
	protected static HashMap hTokens = new HashMap();
	
	public OAIListIdentifiersElement(Map oArgs){
		super(oArgs);
	}
	
	public String getMetadataPrefix(){
		return getArgument("metadataPrefix");
	}
	
	public String getResumptionToken(){
		return getArgument("resumptionToken");
	}
	public String getFrom(){
		return getArgument("from");
	}
	public Date getFromDate(){
		return getDate(getFrom());
	}
	
	public String getUntil(){
		return getArgument("until");
	}	
	public Date getUntilDate(){
		return getDate(getUntil());
	}
	
	
	protected HashMap getArguments(){
		if (hArgs==null){
			hArgs=new HashMap();
			hArgs.put("R", new String[]{"verb","metadataPrefix"});
			hArgs.put("O", new String[]{"from", "until", "set"});
			hArgs.put("E", new String[]{"resumptionToken"});
		}
		return hArgs;
	}
	
	public boolean checkArguments() throws MerliHarvestingException{
		boolean bOk = super.checkArguments();
		if (bOk){
			bOk = checkDateArguments(getFrom(), getUntil());
		}
		return bOk;
	}
	
	/**
	 * 
	 * @param bAll true if returns id+lom information; otherwise false
	 * @return
	 * @throws MerliHarvestingException
	 */
	protected Element getListElement(boolean bAll) throws MerliHarvestingException{
		Element eVerb = null;
		ResumptionToken oToken = null;
		clearExpiredResumptionTokens();
		// Get or create the resumptionToken
		if (getResumptionToken()!=null){
			if (hTokens.containsKey(getResumptionToken())){
				oToken = (ResumptionToken)hTokens.get(getResumptionToken());
			}else{
				throw new MerliHarvestingException(MerliHarvestingException.ERROR_BAD_RESUMPTION_TOKEN, this);
			}
		}else{
			if (hTokens.size()>getMaxTokenNumber()){
				String sText = "Too many resumptionTokens opened. Wait for a moment and try again";
				logger.debug(MerliHarvestingException.ERROR_BAD_RESUMPTION_TOKEN+" -> "+sText);
				throw new MerliHarvestingException(MerliHarvestingException.ERROR_BAD_RESUMPTION_TOKEN, sText);
			}
			Vector vIds = getDB().getListIdentifiers(getFrom(), getUntil());
			oToken = new ResumptionToken(getExpirationDate(), vIds);
			hTokens.put(oToken.getIdentifier(), oToken);
		}
		
		Vector vIds = oToken.getResources();		
		if (vIds.isEmpty()){
			throw new MerliHarvestingException(MerliHarvestingException.ERROR_NO_RECORDS_MATCH, this);
		}else{
			// Get resources list (from current cursor to max token size list)
			eVerb = new Element(getVerb(), XMLNS);
			int iCursor = oToken.getCursor();
			int iStop = getTokenSizeList()+iCursor;
			Iterator itIds = vIds.listIterator(iCursor);
			while (itIds.hasNext() && iCursor<iStop){
				ResourceIdentifier oResource = (ResourceIdentifier)itIds.next();
				OAIHeaderElement eHeader = new OAIHeaderElement(oResource);
				if (bAll){
					Element eMetadata = getDB().getResourceMetadata(eHeader.getIdentifier());
					Element eRecord = createRecordElement(eHeader, eMetadata);
					eVerb.addContent(eRecord);
				}else{
					eVerb.addContent(eHeader);
				}
				iCursor++;
			}
			if (oToken.getCompleteListSize()>getTokenSizeList()){
				OAIResumptionTokenElement eToken = null;
				if (iCursor<iStop){
					eToken = new OAIResumptionTokenElement(oToken.getCompleteListSize(), oToken.getCursor());
					// The list is now complete: token must be deleted from the hash
					hTokens.remove(getResumptionToken());
				}else{
					oToken.setExpirationDate(getExpirationDate());
					eToken = new OAIResumptionTokenElement(oToken.getIdentifier(), oToken.getExpirationDate(), oToken.getCompleteListSize(), oToken.getCursor());
					// Update the token
					oToken.setCursor(iCursor);
				}
				eVerb.addContent(eToken);
			}
		}
		return eVerb;
	}	
	
	public void generateResponse() throws MerliHarvestingException{		
		Element eVerb = getListElement(false);
		addContent(eVerb);
	}

	public String getVerb() {
		return VERB_LIST_IDENTIFIERS;
	}
	
//	************************************	
//	* Utils
//	************************************
	
	protected void clearExpiredResumptionTokens(){
		Date dCurrent = new Date();
		Vector vDelTokens = new Vector();
		Iterator itTokens = hTokens.values().iterator();
		while (itTokens.hasNext()){
			ResumptionToken oToken = (ResumptionToken)itTokens.next();
			if (oToken.getExpirationDate().before(dCurrent)) vDelTokens.addElement(oToken.getIdentifier());
			//if (oToken.getExpirationDate().before(dCurrent)) hTokens.remove(oToken.getIdentifier());
		}
		Iterator itDelTokens = vDelTokens.iterator();
		while (itDelTokens.hasNext()){
			hTokens.remove(itDelTokens.next());
		}
	}
	
	protected int getTokenSizeList(){
		return getIntProperty("harvesting.resumptionToken.tokenSizeList");
	}
	protected int getMaxTokenNumber(){
		return getIntProperty("harvesting.resumptionToken.maxTokenNumber");
	}
	protected Date getExpirationDate(){
		return new Date(new Date().getTime()+getIntProperty("harvesting.resumptionToken.expirationTime"));
	}
			
	private Date getDate(String sDate){
		Date oDate = null;
		try{
			oDate = stringToDate(sDate);
		}catch (Exception e){}
		return oDate;		
	}
	
	
		
}
