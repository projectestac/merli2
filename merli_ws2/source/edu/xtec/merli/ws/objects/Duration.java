package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Duration extends ObjectMerli {

	private String duration;
	
	
	

	public Duration() {
		super();
		// TODO Auto-generated constructor stub
		this.duration = "P";
	}
	public Duration(String duration) {
		super();
		// TODO Auto-generated constructor stub
		this.duration = duration;
		controlDurationFormat();
	}

	public void controlDurationFormat(){

		String dur ="P";
		
		boolean isT = false;
		String aux;		
		String time;
		if (duration == null){
			duration = dur;
			return;
		}
		try{
		if (duration.indexOf("T")>0){
			dur+="T";
		}
		
		if (duration.indexOf("H")>0){
			if ((duration.indexOf("H")-duration.indexOf("T"))==1){
				dur+="0";
			}
			dur+=duration.substring(duration.indexOf("T")+1, Math.min(duration.length()-1, duration.indexOf("H")+1));
		}
		if (duration.indexOf("M")>0){
			if ((duration.indexOf("M")-duration.indexOf("H"))==1){
				dur+="0";
			}
			dur+=duration.substring(duration.indexOf("H")+1, duration.indexOf("M")+1);
		}
		if (duration.indexOf("S")>0){
			if ((duration.indexOf("S")-duration.indexOf("M"))==1){
				dur+="0";
			}
			dur+=duration.substring(duration.indexOf("M")+1, duration.indexOf("S")+1);
		}

		}catch (Exception e){
			dur = "P";
		}
/*		
		time = duration.substring(duration.indexOf("P")+1);
		System.out.println("controlDurationFormal-> time="+time+" dur="+dur);
		if (time.indexOf("Y") > 0){
			aux = time.substring(0,time.indexOf("Y"));
			if (esNumero(aux))	
				dur += aux + "Y";
			
			time = time.substring(time.indexOf("Y")+1); 
		}
		System.out.println("controlDurationFormal-> time2="+time+" dur="+dur);
		if (time.indexOf("M") > 0){
			aux = time.substring(0,time.indexOf("M"));
			if (esNumero(aux))
				dur += aux + "M";
			
			time = time.substring(time.indexOf("M")+1); 
		}
		System.out.println("controlDurationFormal-> time3="+time+" dur="+dur);
		if (time.indexOf("D") > 0){
			aux = time.substring(0,time.indexOf("D"));
			if (esNumero(aux))
				dur += aux + "D";
			
			time = time.substring(time.indexOf("D")+1); 
		}
		
		System.out.println("controlDurationFormal-> time4="+time+" dur="+dur);
		time =  duration.substring(duration.indexOf("T")+1);
		dur += "T";
		if (time.indexOf("H") > 0){
			aux = time.substring(0,time.indexOf("H"));
			if (esNumero(aux)){	
				isT = true;
				dur += aux + "H";
			}
			time = time.substring(time.indexOf("H")+1); 
		}
		System.out.println("controlDurationFormal-> time5="+time+" dur="+dur);
		if (time.indexOf("M") > 0){
			aux = time.substring(0,time.indexOf("M"));
			if (esNumero(aux)){	
				isT = true;
				dur += aux + "M";
			}
			time = time.substring(time.indexOf("M")+1); 
		}
		if (time.indexOf("S") > 0){
			aux = time.substring(0,time.indexOf("S"));
			if (esFloat(aux)){	
				isT = true;
				dur += aux + "S";
			}
			time = time.substring(time.indexOf("S")+1); 
		}
		if (!isT)
			dur = dur.replaceAll("T","");
*/		
		duration = dur;
	}



	private boolean esFloat(String aux) {
		try{
			Float.valueOf(aux);
		}catch (Exception e){
			return false;
		}
		return true;
	}
	private boolean esNumero(String aux) {
		try{
			Integer.valueOf(aux);
		}catch (Exception e){
			return false;
		}
		return true;
	}
	public Duration(SOAPElement seDuration) throws SOAPException {
		
        Iterator It=seDuration.getChildElements(soapFactory.createName("duration"));
		if (It.hasNext())
			duration= ((SOAPElement)It.next()).getValue();
		else
			duration = seDuration.getValue();
		
		controlDurationFormat();
	}
	public String getDuration() {
		return duration;
	}
	




	public void setDuration(String duration) {
		this.duration = duration;
		controlDurationFormat();
	}
	



	public SOAPElement toXml() throws SOAPException {
		return toXml(APXTEC32.DEFAULT_DURATION_NAME);		
	}

	public SOAPElement toXml(String elementName) throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seDuration;
		SOAPElement seDurationDuration;
		
		//Crea l'element duration		
		seDuration = soapFactory.createElement(soapFactory.createName(elementName));	
		seDurationDuration = soapFactory.createElement(soapFactory.createName("duration"));
		seDurationDuration.addTextNode(duration);
		seDuration.addChildElement(seDurationDuration);
		
		//Descripció del Duration.
//		SOAPElement seDescription = soapFactory.createElement("description");
//		LangString description = new LangString(" - ",APXTEC32.DEFAULT_LANG);
//		seDescription.addChildElement(description.toXml());
//		seDuration.addChildElement(seDescription);

		return seDuration;
	}
	public String toUrl(String prefix) {
		String params="";
		
//		Crea l'element duration		
		params+=prefix+"dur="+duration+"&";
		
		//Descripció del Duration.
		LangString description = new LangString(" - ",APXTEC32.DEFAULT_LANG);
		params+=description.toUrl(prefix+"desc_");
		
		return params;
	}

}
