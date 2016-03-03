package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class LifeCycle extends ObjectMerli {

	private LangString version;
	private SourceValue status;
	private ArrayList contributeList;
	
	

	public LifeCycle() {
		super();
	
		this.version = new LangString();
		this.status = new SourceValue("status");
		this.contributeList = new ArrayList();
	}
	public LifeCycle(LangString version, SourceValue status, ArrayList contributeList) {
		super();
		
		this.version = version;
		this.status = status;
		this.contributeList = contributeList;
	}




	public LifeCycle(SOAPElement seLifeCycle) throws SOAPException {
		
        Iterator It=seLifeCycle.getChildElements(soapFactory.createName("version"));
		if (It.hasNext())
			 version= new LangString(((SOAPElement)It.next()));
		
		It=seLifeCycle.getChildElements(soapFactory.createName("status"));
		if (It.hasNext())
			status = new SourceValue(((SOAPElement)It.next()));
		status.setName("status");
		
		It=seLifeCycle.getChildElements(soapFactory.createName("contribute"));
		contributeList = new ArrayList();
		while (It.hasNext())
        	contributeList.add(new Contribute((SOAPElement)It.next()));
		
	}
	public ArrayList getContributeList() {
		return contributeList;
	}
	



	public void setContributeList(ArrayList contributeList) {
		this.contributeList = contributeList;
	}
	



	public SourceValue getStatus() {
		return status;
	}
	



	public void setStatus(SourceValue status) {
		this.status = status;
	}
	



	public LangString getVersion() {
		return version;
	}
	



	public void setVersion(LangString version) {
		this.version = version;
	}
	



	public SOAPElement toXml() throws SOAPException {
		
		SOAPElement seLifeCycle;
		SOAPElement seVersion;
		
		//Crea l'element LifeCycle;
		seLifeCycle = soapFactory.createElement(soapFactory.createName("lifeCycle"));
		
		//Afageix l'element Version
		if (version == null) version = new LangString();
		seVersion = soapFactory.createElement(soapFactory.createName("version"));
		seVersion.addChildElement(version.toXml());
		seLifeCycle.addChildElement(seVersion);
		
		//Afageix l'element Status
		if (status == null) status = new SourceValue("status");
		status.setName("status");
		seLifeCycle.addChildElement(status.toXml());

		if (contributeList == null) contributeList = new ArrayList();
		for (int i=0; i < this.contributeList.size();i++){			
			seLifeCycle.addChildElement(((Contribute)this.contributeList.get(i)).toXml());
		}
	
		return seLifeCycle;
	}
}
