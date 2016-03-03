package edu.xtec.merli;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Unitat{

	private Integer identifier;
	private String name;
	

	public Unitat() {
		super();
		this.identifier = new Integer(0);
		this.name = "";
	}

	public Unitat(Integer identifier, String name) {
		super();
		this.identifier = identifier;
		this.name = name;
	}


	public String toString(){
		return "Unitat[id="+getIdentifier()+",type="+getName()+"]";
	}

	public Integer getIdentifier() {
		return identifier;
	}

	public void setIdentifier(Integer identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
