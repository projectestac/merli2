package edu.xtec.merli.ws.objects;

import java.sql.Timestamp;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Date extends ObjectMerli {

	private String dateTime;
	

	
	public Date() {
		super();
		this.dateTime = "";
	}
	
	public Date(String dateTime) {
		super();
		// TODO Auto-generated constructor stub
		this.dateTime = dateTime;
	}




	public Date(SOAPElement seDate) throws SOAPException {
		this.dateTime = "";
		
		Iterator It=seDate.getChildElements(soapFactory.createName("dateTime"));
		if (It.hasNext())
			dateTime = ((SOAPElement)It.next()).getValue();
	}

	public Date(Timestamp timestamp) {
		// TODO Auto-generated constructor stub
		dateTime = timestamp.toString();
	}

	public String getDateTime() {
		return dateTime;
	}
	



	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	



	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seDateTime = soapFactory.createElement("dateTime");
		if (dateTime == null) dateTime = "";
		seDateTime.addTextNode(dateTime);
				
		return seDateTime;
	}

}
