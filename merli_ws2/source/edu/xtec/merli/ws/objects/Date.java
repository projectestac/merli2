package edu.xtec.merli.ws.objects;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Date extends ObjectMerli {

	private String dateTime;
	private LangString description;
	

	
	public Date() {
		super();
		this.dateTime = "";
		//this.description = new LangString();
	}

	public Date(String dateTime) {
		super();
		this.dateTime = dateTime;
		//this.description = new LangString();
	}
	public Date(String dateTime, LangString description) {
		super();
		this.dateTime = dateTime;
		this.description = description;
	}
	public Date(String dateTime, String description) {
		super();
		this.dateTime = dateTime;
		this.description = new LangString(description,APXTEC32.DEFAULT_LANG);
	}




	public Date(SOAPElement seDate) throws SOAPException {
		this.dateTime = "";
		//this.description = new LangString();

		Iterator It=seDate.getChildElements(soapFactory.createName("dateTime"));
		if (It.hasNext())
			dateTime = ((SOAPElement)It.next()).getValue();
		

		It=seDate.getChildElements(soapFactory.createName("description"));
		if (It.hasNext())
			description = new LangString(((SOAPElement)It.next()));
	}

	public Date(Timestamp timestamp) {
		// TODO Auto-generated constructor stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dateTime = sdf.format(new java.util.Date(timestamp.getTime()));
		
		//dateTime = timestamp.toString();
	}

	public String getDateTime() {
		return dateTime;
	}
	



	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	



	public LangString getDescription() {
		return description;
	}

	public void setDescription(LangString description) {
		this.description = description;
	}
	
	public void setDescription(String description) {
		if (this.description == null) this.description = new LangString(description, APXTEC32.DEFAULT_LANG);
		else
			this.description.setString(description);
	}

	
	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seDate = soapFactory.createElement("date");
		SOAPElement seDateTime = soapFactory.createElement("dateTime");
		if (dateTime == null) dateTime = "";
		seDateTime.addTextNode(dateTime);
		seDate.addChildElement(seDateTime);
		
//		description = new LangString("Date description is empty",APXTEC32.DEFAULT_LANG);

		if (description != null)
		{
			SOAPElement seDescription = soapFactory.createElement("description");
			seDescription.addChildElement(description.toXml());
			seDate.addChildElement(seDescription);
		}
				
		return seDate;
	}

	public String toUrl(String prefix) {
		String params="";
		
		if (dateTime == null) dateTime = "";
		params+=prefix+"d="+dateTime+"&";
		//if (description == null){
		//	description = new LangString("Date description is empty",APXTEC32.DEFAULT_LANG);
		//}
		if(description!=null)	params+=description.toUrl(prefix+"desc_");
		
		return params;
	}

}
