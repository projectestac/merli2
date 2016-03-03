package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class SourceValue extends ObjectMerli {

	private String source;
	private String value;
	private String id;
	
	private String name; 
	
	

	public SourceValue(String name) {
		super();
		// TODO Auto-generated constructor stub
		this.source = "";
		this.value = "";
		this.name = name;
	}

	public SourceValue(String source, String value, String name) {
		super();
		// TODO Auto-generated constructor stub
		this.source = source;
		this.value = value;
		this.name = name;
	}




	public SourceValue(SOAPElement seSourceValue) throws SOAPException {
		
		Iterator It=seSourceValue.getChildElements(soapFactory.createName("source"));
		if (It.hasNext())
			source= ((SOAPElement)It.next()).getValue();
		
		It=seSourceValue.getChildElements(soapFactory.createName("value"));
		if (It.hasNext())
			 value = ((SOAPElement)It.next()).getValue();
	}
	public String getName() {
		return name;
	}
	



	public void setName(String name) {
		this.name = name;
	}
	



	public String getSource() {
		return source;
	}
	



	public void setSource(String source) {
		this.source = source;
	}
	



	public String getValue() {
		return value;
	}
	



	public void setValue(String value) {
		this.value = value;
	}
	
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}


	public SOAPElement toXml() throws SOAPException {
		SOAPElement seSourceValue;
		SOAPElement seSource;
		SOAPElement seValue;
		
		seSourceValue= soapFactory.createElement(soapFactory.createName(this.name));
		
		if (source == null) source = "";
		seSource = soapFactory.createElement(soapFactory.createName("source"));
		seSource.addTextNode(source);
		seSourceValue.addChildElement(seSource);
		
		if (value == null) value = "";
		seValue = soapFactory.createElement(soapFactory.createName("value"));
		seValue.addTextNode(value);
		seSourceValue.addChildElement(seValue);
		
		return seSourceValue;		
	}
	

	

}
