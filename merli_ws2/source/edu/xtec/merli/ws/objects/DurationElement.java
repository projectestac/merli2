package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class DurationElement extends ObjectMerli {

	private Duration duration;
	
	
	

	public DurationElement() {
		super();
		this.duration = new Duration();
	}
	public DurationElement(Duration duration) {
		super();
		this.duration = duration;
	}





	public DurationElement(SOAPElement seDuration) throws SOAPException {
		
        Iterator It=seDuration.getChildElements(soapFactory.createName("duration"));
//		if (It.hasNext())
//			duration= ((SOAPElement)It.next()).getValue();

//		It=seDuration.getChildElements(soapFactory.createName("duration"));
		if (It.hasNext())
			duration = new Duration((SOAPElement)It.next());
	}
	public Duration getDuration() {
		return duration;
	}
	




	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	




	public SOAPElement toXml() throws SOAPException {
		return toXml(APXTEC32.DEFAULT_DURATION_NAME);
	}
	public SOAPElement toXml(String elementName) throws SOAPException {
//		SOAPElement seDuration;
//		
//		//Crea l'element duration		
//		seDuration = soapFactory.createElement(soapFactory.createName("duration"));
//		if (duration == null) duration = new Duration();
//		seDuration.addChildElement(duration.toXml());

		return duration.toXml();// seDuration;
	}
	public String toUrl(String prefix) {
		return duration.toUrl(prefix);
	}

}
