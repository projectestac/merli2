package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import edu.xtec.merli.utils.Utility;

public class Technical extends ObjectMerli {

	private ArrayList formatList;
	private String location;
	private Duration duration;
	private static final int MAX_LOCATION = 1000;	
	

	public Technical() {
		super();
		// TODO Auto-generated constructor stub
		this.formatList = new ArrayList();
		this.location = "";
		this.duration = new Duration();
	}
	public Technical(ArrayList format, String location, Duration duration) {
		super();
		// TODO Auto-generated constructor stub
		this.formatList = format;
		this.location = Utility.controlarString(location,Technical.MAX_LOCATION);
		this.duration = duration;
	}




	public Technical(SOAPElement seTechnical) throws SOAPException {

		formatList = new ArrayList();
		Iterator It=seTechnical.getChildElements(soapFactory.createName("format"));
        while (It.hasNext())
			formatList.add(((SOAPElement)It.next()).getValue());
		
		It=seTechnical.getChildElements(soapFactory.createName("location"));
		if (It.hasNext()){
			location = ((SOAPElement)It.next()).getValue();
			location = Utility.controlarString(location,Technical.MAX_LOCATION);
		}
		It=seTechnical.getChildElements(soapFactory.createName("duration"));
		if (It.hasNext())
			duration = new Duration((SOAPElement)It.next());
	}
	public Duration getDuration() {
		return duration;
	}
	



	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	



	public ArrayList getFormat() {
		return formatList;
	}
	



	public void setFormat(ArrayList format) {
		this.formatList = format;
	}
	



	public String getLocation() {
		return location;
	}
	



	public void setLocation(String location) {
		this.location = Utility.controlarString(location,Technical.MAX_LOCATION);		
	}
	



	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		
		SOAPElement seTechnical;
		SOAPElement seFormat;
		SOAPElement seLocation;
		SOAPElement seDuration;
		
		//Crea l'element technical
		seTechnical = soapFactory.createElement(soapFactory.createName("technical"));
		
		//Crea l'element format
		if (formatList == null) formatList = new ArrayList();
		for (int i=0; i < this.formatList.size();i++){
			seFormat = soapFactory.createElement(soapFactory.createName("format"));
			seFormat.addTextNode((String)formatList.get(i));
			seTechnical.addChildElement(seFormat);
		}
		
		//Crea l'element Location
		if (location == null) location = "";
		seLocation = soapFactory.createElement(soapFactory.createName("location"));
		seLocation.addTextNode(location);
		seTechnical.addChildElement(seLocation);
		
		//Crea l'element duration
		if (duration == null) duration = new Duration();
		seTechnical.addChildElement(duration.toXml());
					
		return seTechnical;
	}

}
