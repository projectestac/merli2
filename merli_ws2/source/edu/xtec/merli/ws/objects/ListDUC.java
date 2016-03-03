package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;



public class ListDUC extends ObjectMerli{

	private ArrayList listDUC;
	private SOAPElement seListDUC=null;
	

	public ListDUC() throws SOAPException {
		super();
		// TODO Auto-generated constructor stub
		this.listDUC = new ArrayList();
		
		//Crea l'element listDUC
		seListDUC = soapFactory.createElement("listDUC");
	}
	

	public ListDUC(ArrayList listDUC) {
		super();

		this.listDUC = listDUC;
	}

	



	public ListDUC(SOAPElement seListElement) throws SOAPException {
		this.listDUC = new ArrayList();
		Iterator it = seListElement.getChildElements(soapFactory.createName("elementDUC"));
		while (it.hasNext())
			listDUC.add(new ElementDUC((SOAPElement) it.next()));
	}


	
	/**
	 * @return Returns the listDUC.
	 */
	public ArrayList getListDUC() {
		return listDUC;
	}
	


	/**
	 * @param listDUC The listDUC to set.
	 */
	public void setListDUC(ArrayList listDUC) {
		this.listDUC = listDUC;
	}
	


	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		if (seListDUC == null || (listDUC != null && listDUC.size()>0)){
			//SOAPElement seListDUC=null;
		        
			//Crea l'element listDUC
			seListDUC = soapFactory.createElement("listDUC");
			
			if (listDUC == null) listDUC = new ArrayList();
			for(int i=0;i<listDUC.size();i++){
				//Afageix l'element ElementDUC a la llista.
				seListDUC.addChildElement(((ElementDUC)this.listDUC.get(i)).toXml());		
			}
		}
		return seListDUC;
	}
	
	
	public void addElement(ElementDUC eDUC) throws SOAPException{
		seListDUC.addChildElement(eDUC.toXml());
	}
	
	
	public String toString(){
		return "[listDuc="+listDUC.toString()+"]";
	}
	
	
	
}
