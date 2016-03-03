package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Duration extends ObjectMerli {

	private String duration;
	
	
	

	public Duration() {
		super();
		// TODO Auto-generated constructor stub
		this.duration = "";
	}
	public Duration(String duration) {
		super();
		// TODO Auto-generated constructor stub
		this.duration = duration;
	}





	public Duration(SOAPElement seDuration) throws SOAPException {
		
        Iterator It=seDuration.getChildElements(soapFactory.createName("duration"));
		if (It.hasNext())
			duration= ((SOAPElement)It.next()).getValue();
	}
	public String getDuration() {
		return duration;
	}
	




	public void setDuration(String duration) {
		this.duration = duration;
	}
	




	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seDuration;
		
		seDuration = soapFactory.createElement(soapFactory.createName("duration"));
		if (duration == null) duration ="";
		seDuration.addTextNode(duration);

		return seDuration;
	}

}
