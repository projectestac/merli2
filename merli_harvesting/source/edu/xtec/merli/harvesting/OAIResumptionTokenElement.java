package edu.xtec.merli.harvesting;

/*
 * OAIResumptionTokenElement.java
 *
 * Created on 2007/05/02
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */
import java.util.Date;

import org.jdom.Element;
import org.jdom.Namespace;


public class OAIResumptionTokenElement extends Element {
	
	public static Namespace OAINS = OAIElement.OAINS;
	
	public OAIResumptionTokenElement(){
		super("resumptionToken", OAINS);
	}
	public OAIResumptionTokenElement (String sId, Date dExpiration, int iSize, int iCursor){
		this();
		setResumptionToken(sId);
		setExpirationDate(dExpiration);
		setCompleteListSize(String.valueOf(iSize));
		setCursor(String.valueOf(iCursor));
	}
	public OAIResumptionTokenElement (int iSize, int iCursor){
		this();
		setCompleteListSize(String.valueOf(iSize));
		setCursor(String.valueOf(iCursor));
	}
	
	
	public void setExpirationDate(Date d){
		setAttribute("expirationDate", OAIElement.dateToString(d, OAIElement.UTC_COMPLETE));
	}
	public void setCompleteListSize(String s){
		setAttribute("completeListSize", s);
	}
	public void setCursor(String s){
		setAttribute("cursor", s);
	}
	public void setResumptionToken(String s){
		setText(s);
	}	
}
