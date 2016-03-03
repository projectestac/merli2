package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.ws.WSAccesBD;
import edu.xtec.merli.ws.WSMerliBD;

public class Technical extends ObjectMerli {

	private ArrayList formatList = new ArrayList();
	private String location = "";
	private DurationElement duration = new DurationElement();	
	

	public Technical() {
		super();
		// TODO Auto-generated constructor stub
		this.formatList = new ArrayList();
		this.location = "";
		this.duration = new DurationElement();
	}
	public Technical(ArrayList format, String location, DurationElement duration) {
		super();
		// TODO Auto-generated constructor stub
		this.formatList = format;
		this.location = location;
		this.duration = duration;
	}




	public Technical(SOAPElement seTechnical) throws SOAPException {

		formatList = new ArrayList();
		Iterator It=seTechnical.getChildElements(soapFactory.createName("format"));
        while (It.hasNext())
			formatList.add(((SOAPElement)It.next()).getValue());
		
		It=seTechnical.getChildElements(soapFactory.createName("location"));
		if (It.hasNext())
			location = ((SOAPElement)It.next()).getValue();
		
		It=seTechnical.getChildElements(soapFactory.createName("duration"));
		if (It.hasNext())
			duration = new DurationElement((SOAPElement)It.next());
	}
	public DurationElement getDuration() {
		return duration;
	}
	



	public void setDuration(DurationElement duration) {
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
		this.location = location;
	}
	



	public SOAPElement toXml() throws SOAPException {
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
		if (location != null && !location.trim().equals(""))
		{
			//if (location == null) location = "";
			seLocation = soapFactory.createElement(soapFactory.createName("location"));
			seLocation.addTextNode(location);
			seTechnical.addChildElement(seLocation);
		}
		
		//Crea l'element duration
		if (duration == null) duration = new DurationElement();
		seTechnical.addChildElement(duration.toXml());
					
		return seTechnical;
	}
	public String toUrl2(String prefix) {
		String params="";
		
		if (formatList == null) formatList = new ArrayList();
		for (int i=0; i < this.formatList.size();i++){
			params+=prefix+"format"+i+"="+(String)formatList.get(i)+"&";
		}
		
		//Crea l'element Location
		if (location == null) location = "";
		params+=prefix+"loc="+location+"&";
		
		//Crea l'element duration
		if (duration == null) duration = new DurationElement();
		params+=duration.toUrl(prefix+"dur_");		
		
		return params;
	}
	
	public String toUrl(String prefix) {
		String params="";
		ArrayList camps=new ArrayList();
		WSMerliBD wsmbd = new WSMerliBD();
		
		if (formatList == null) formatList = new ArrayList();
		for (int i=0; i < this.formatList.size();i++){
			String idFormat = wsmbd.getFormat((String)formatList.get(i));
			params+="format="+idFormat+"&";
		}
		
		//Crea l'element Location
		if (location == null) location = "";
		params+="url="+location+"&";
		
		//Crea l'element duration
		if (duration == null) duration = new DurationElement();
		String dur=duration.getDuration().getDuration().toString();
		if(dur!=null &&!dur.equals("") && !dur.equals("P")){
			try
			{
				String duraHora="0",duraMin="0",duraSeg="0";
				dur=dur.substring(2);								//Format 2H1M02S
				if(dur.indexOf('H')>0)
				{
					duraHora=dur.substring(0,dur.indexOf('H'));
					dur=dur.substring(dur.indexOf('H')+1);			//Format 1M02S
				}
				
				if(dur.indexOf('M')>0)
				{
					duraMin=dur.substring(0,dur.indexOf('M'));
					dur=dur.substring(dur.indexOf('M')+1);			//Format 02S
				}
				if(dur.indexOf('S')>0)
					duraSeg=dur.substring(0,dur.indexOf('S'));
				params+="duraHora="+duraHora+"&duraMin="+duraMin+"&duraSeg="+duraSeg+"&";		
			}
			catch(Exception e){}
		}	
		
		return params;
	}

}
