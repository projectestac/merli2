package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class LlistatGeneric extends ObjectMerli {

	private ArrayList objectList;
	private Class classe;
	private String etiquetaLlista;
	
	
	public LlistatGeneric(Class classe, String etiquetaLlista) {
		super();
		this.objectList = new ArrayList();
		this.classe=classe;
		this.etiquetaLlista=etiquetaLlista;
	}

	public LlistatGeneric(SOAPElement seObjectes, Class classe, String etiquetaObjecte) throws SOAPException {
		super();
		Iterator It=seObjectes.getChildElements(soapFactory.createName(etiquetaObjecte));
		objectList = new ArrayList();
		this.classe=classe;
		this.etiquetaLlista=seObjectes.getNodeName();
		while (It.hasNext())
		{
			if(classe.getName().indexOf("IdResource")>=0) 
		        objectList.add(new IdResource((SOAPElement)It.next()));
			else if(classe.getName().indexOf("Unitat")>=0) 
		        objectList.add(new Unitat((SOAPElement)It.next()));
		}
	} 
	
	public SOAPElement toXml() throws SOAPException 
	{
		SOAPElement seObjects=null;
		if(etiquetaLlista!=null && classe!=null)
		{
			//seObjects = soapFactory.createElement(etiquetaLlista);
			seObjects= soapFactory.createElement(soapFactory.createName(etiquetaLlista));
			
			if (objectList == null) objectList = new ArrayList();
			for (int i=0; i < this.objectList.size();i++)
			{	
					if(classe.getName().indexOf("IdResource")>=0) 
						seObjects.addChildElement(((IdResource)this.objectList.get(i)).toXml());
					else if(classe.getName().indexOf("Unitat")>=0) 
						seObjects.addChildElement(((Unitat)this.objectList.get(i)).toXml());
			}
		}
		return seObjects;
	}

	public Class getClasse() {
		return classe;
	}
	public void setClasse(Class classe) {
		this.classe = classe;
	}

	public String getEtiquetaLlista() {
		return etiquetaLlista;
	}
	public void setEtiquetaLlista(String etiquetaLlista) {
		this.etiquetaLlista = etiquetaLlista;
	}

	public ArrayList getObjectList() {
		return objectList;
	}
	public void setObjectList(ArrayList objectList) {
		this.objectList = objectList;
	}

	public String toUrl(String prefix) {
		String params="";
		
		if (objectList == null) objectList = new ArrayList();
		for (int i=0; i < this.objectList.size();i++)
		{	
			if(objectList.get(0).getClass().getName().indexOf("IdResource")>=0) 
				params+=((IdResource)this.objectList.get(i)).toUrl(prefix+"idF"+i+"_");
			else if(objectList.get(0).getClass().getName().indexOf("Unitat")>=0) 
				params+=((Unitat)this.objectList.get(i)).toUrl(prefix+"u"+i+"_");
		}
		
		return params;
	}
}
