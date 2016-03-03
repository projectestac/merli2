package edu.xtec.merli.harvesting.util;

import java.util.Date;
import java.util.Vector;

/*
 * ResumptionToken.java
 *
 * Created on 2007/05/02
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

public class ResumptionToken {
	
	private String sId;
	private Date dExpiration;
	private Vector vResources;
	private int iCursor;
	
	public ResumptionToken(Date dExpiration, Vector vResources){
		this.sId=generateIdentifier();
		this.dExpiration=dExpiration;
		this.vResources=vResources;
		this.iCursor=0;
	}
	
	public String getIdentifier(){
		return this.sId;
	}
	public Date getExpirationDate(){
		return dExpiration;
	}
	public void setExpirationDate(Date dExpiration){
		this.dExpiration=dExpiration;
	}
	
	public Vector getResources(){
		return vResources;
	}
	
	public int getCursor(){
		return this.iCursor;
	}
	public void setCursor(int iCursor){
		this.iCursor=iCursor;
	}
	
	public int getCompleteListSize(){
		return getResources()!=null?getResources().size():0;
	}
	
	private static String generateIdentifier(){
		return "merli"+"_"+Math.round(Math.random()*10000)+"_"+new Date().getTime();
	}

}
