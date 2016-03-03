package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import edu.xtec.merli.ws.WSMerliBD;

public class LifeCycle extends ObjectMerli {

	private LangString version = new LangString();
	private SourceValue status = new SourceValue("status");
	private ArrayList contributeList = new ArrayList();
	
	

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
		
		//Crea l'element LifeCycle;
		seLifeCycle = soapFactory.createElement(soapFactory.createName("lifeCycle"));
		
		//Afegeix l'element Version
		if (version != null && version.getString()!=null) {
			SOAPElement seVersion = soapFactory.createElement(soapFactory.createName("version"));
			seVersion.addChildElement(version.toXml());
			seLifeCycle.addChildElement(seVersion);
		}
		
		//Afegeix l'element Status
		if (status == null) status = new SourceValue("status");
		status.setName("status");
		seLifeCycle.addChildElement(status.toXml());

		if (contributeList == null) contributeList = new ArrayList();
		for (int i=0; i < this.contributeList.size();i++){			
			seLifeCycle.addChildElement(((Contribute)this.contributeList.get(i)).toXml());
		}
	
		return seLifeCycle;
	}
	public String toUrl2(String prefix) {
		String params="";
		//Afegeix l'element Version
		if (version != null && version.getString()!=null) 
			params+=version.toUrl(prefix+"v_");
		
		//Afegeix l'element Status
		if (status == null) status = new SourceValue("status");
		status.setName("status");
		params+=status.toUrl(prefix+"s_");

		if (contributeList == null) contributeList = new ArrayList();
		for (int i=0; i < this.contributeList.size();i++){	
			params+=((Contribute)this.contributeList.get(i)).toUrl(prefix+"contr"+i+"_");
		}
		return params;		
	}
	
	public String toUrl(String prefix) {
		String params="";
		//Afegeix l'element Version
		if (version != null && version.getString()!=null) 
			params+=version.toUrl("versio");
		
		if (contributeList == null) contributeList = new ArrayList();
		for (int i=0; i < this.contributeList.size();i++){	
			String rol=((Contribute)this.contributeList.get(i)).getRole().getValue();
			String nom=((Contribute)this.contributeList.get(i)).getEntity().getUsername();
			//M'arriba la data en format AAAA-mm-dd, la passo a dd-mm-AAAA
			
			if(rol.equals("author")) 
			{
				params+="autor="+nom+"&";
				String[] data= ((String)((Contribute)this.contributeList.get(i)).getDateTime().getDateTime()).split("-");
				params+="data="+data[2]+"-"+data[1]+"-"+data[0]+"&";
			}
			else if(rol.equals("editor")) params+="editor="+nom+"&";
			else if(rol.equals("publisher")) 
			{
				WSMerliBD wsmbd = new WSMerliBD();
				String idAmbit = wsmbd.getAmbit(nom);
				params+="ambit="+idAmbit+"&";
			}
		}
		return params;		
	}
}
