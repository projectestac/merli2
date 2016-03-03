package edu.xtec.merli.harvesting.util;

import java.util.Date;

/*
 * ResourceIdentifier.java
 *
 * Created on 2007/05/02
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

public class ResourceIdentifier {
	
	private String sIdentifier;
	private Date dDatestamp;
	
	public ResourceIdentifier(String sIdentifier, Date dDatestamp){
		this.sIdentifier=sIdentifier;
		this.dDatestamp=dDatestamp;
	}
	
	public String getIdentifier(){
		return this.sIdentifier;
	}
	public Date getDatestamp(){
		return this.dDatestamp;
	}	
	
	public String toString(){
		return "[id="+sIdentifier+", date="+dDatestamp+"]";
	}

}
