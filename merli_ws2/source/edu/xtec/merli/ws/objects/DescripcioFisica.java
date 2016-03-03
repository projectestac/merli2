package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class DescripcioFisica extends ObjectMerli {
	
	private String caracteristiques;	
	private Unitat unitatCreadora=new Unitat();
	private LlistatGeneric identificadorFisicList = new LlistatGeneric(IdResource.class,"identificadors");
	private LlistatGeneric disponibleA = new LlistatGeneric(Unitat.class,"disponibleA");	
	private ArrayList formatList = new ArrayList();
	
	public DescripcioFisica() {
		super();
		this.caracteristiques="";
		this.unitatCreadora=new Unitat();
		this.identificadorFisicList = new LlistatGeneric(IdResource.class,"identificadors");
		this.disponibleA = new LlistatGeneric(Unitat.class,"disponibleA");
		this.formatList = new ArrayList();
	}
	
	public DescripcioFisica(String caracteristiques, Unitat unitatCreadora, LlistatGeneric identificadorFisicList, LlistatGeneric disponibleA, ArrayList format) {
		super();
		this.caracteristiques=caracteristiques;
		this.unitatCreadora=unitatCreadora;
		if (identificadorFisicList==null) this.identificadorFisicList = new LlistatGeneric(IdResource.class,"identificadors");
		else this.identificadorFisicList = identificadorFisicList;
		if (disponibleA==null) this.disponibleA = new LlistatGeneric(Unitat.class,"disponibleA");
		else this.disponibleA = disponibleA;
		this.formatList = format;
	}
	
	public String getCaracteristiques() {
		return caracteristiques;
	}

	public void setCaracteristiques(String caracteristiques) {
		this.caracteristiques = caracteristiques;
	}

	public LlistatGeneric getIdentificadorFisicList() {
		return identificadorFisicList;
	}

	public void setIdentificadorFisicList(LlistatGeneric identificadorFisicList) {
		this.identificadorFisicList = identificadorFisicList;
	}

	public Unitat getUnitatCreadora() {
		return unitatCreadora;
	}

	public void setUnitatCreadora(Unitat unitatCreadora) {
		this.unitatCreadora = unitatCreadora;
	}

	public LlistatGeneric getDisponibleA() {
		return disponibleA;
	}
	public void setDisponibleA(LlistatGeneric disponibleA) {
		this.disponibleA = disponibleA;
	}
	
	public ArrayList getFormat() {
		return formatList;
	}
	public void setFormat(ArrayList format) {
		this.formatList = format;
	}
	
	public DescripcioFisica(SOAPElement seDescripcioF) throws SOAPException {
		Iterator It=seDescripcioF.getChildElements(soapFactory.createName("caracteristiques"));
		if (It.hasNext())
			caracteristiques=((SOAPElement)It.next()).getValue();
		
		It=seDescripcioF.getChildElements(soapFactory.createName("unitat"));
		if (It.hasNext())
			unitatCreadora=new Unitat((SOAPElement)It.next());
		
		identificadorFisicList = new LlistatGeneric(IdResource.class,"identificadors");
		It=seDescripcioF.getChildElements(soapFactory.createName("identificadors"));
		if (It.hasNext())
			identificadorFisicList=new LlistatGeneric((SOAPElement)It.next(),IdResource.class,"idResource");
		
		disponibleA = new LlistatGeneric(Unitat.class,"disponibleA");
		It=seDescripcioF.getChildElements(soapFactory.createName("disponibleA"));
		if (It.hasNext())
			disponibleA=new LlistatGeneric((SOAPElement)It.next(),Unitat.class, "unitat");
		
		formatList = new ArrayList();
		It=seDescripcioF.getChildElements(soapFactory.createName("format"));
        while (It.hasNext())
			formatList.add(((SOAPElement)It.next()).getValue());
	}

	public SOAPElement toXml() throws SOAPException {
		SOAPElement seDescripcioF=null;
		SOAPElement seCaracteristiques;
		SOAPElement seUnitat;
		SOAPElement seFormat;
	    		
		if(unitatCreadora!=null && unitatCreadora.getIdentifier()!=null && !unitatCreadora.getIdentifier().equals(new Integer(0)))
		{
			//Crea l'element descripcioFisica
			seDescripcioF= soapFactory.createElement(soapFactory.createName("descripcioFisica"));
	
			//Afegeix l'element caracteristiques
			if (caracteristiques != null && !caracteristiques.equals(""))
			{
				seCaracteristiques = soapFactory.createElement(soapFactory.createName("caracteristiques"));
				//if (caracteristiques == null) caracteristiques = "";
				seCaracteristiques.addTextNode(caracteristiques);
				seDescripcioF.addChildElement(seCaracteristiques);
			}
			
			//Afageix l'identificador de la unitat creadora
			seUnitat = soapFactory.createElement("unitatCreadora");
			seDescripcioF.addChildElement(seUnitat.addChildElement(unitatCreadora.toXml()));
		
			//Afegeix els identificadors fisics
			if (identificadorFisicList != null && identificadorFisicList.getObjectList().size()>0)
			{
				//if (identificadorFisicList == null) identificadorFisicList = new LlistatGeneric(IdResource.class,"identificadors");
				seDescripcioF.addChildElement(identificadorFisicList.toXml());
			}
			
			//Afegeix les unitats on el recurs esta disponible
			if (disponibleA != null&& disponibleA.getObjectList().size()>0)
			{
				//if (disponibleA == null) disponibleA = new LlistatGeneric(Unitat.class,"disponibleA");	
				seDescripcioF.addChildElement(disponibleA.toXml());
			}
			
			//Crea l'element format
			if (formatList == null) formatList = new ArrayList();
			for (int i=0; i < this.formatList.size();i++){
				seFormat = soapFactory.createElement(soapFactory.createName("format"));
				seFormat.addTextNode((String)formatList.get(i));
				seDescripcioF.addChildElement(seFormat);
			}
		}
		return seDescripcioF;
	}

	public String toUrl2(String prefix) {
		String params="";
		
		if(unitatCreadora!=null && unitatCreadora.getIdentifier()!=null && !unitatCreadora.getIdentifier().equals(new Integer(0)))
		{
			//Afegeix l'element caracteristiques
			if (caracteristiques == null) caracteristiques = "";
			params+=prefix+"cars="+caracteristiques+"&";
			
			//Afageix l'identificador de la unitat creadora
			params+=unitatCreadora.toUrl(prefix+"uni_");
		
			//Afegeix els identificadors fisics
			if (identificadorFisicList == null) identificadorFisicList = new LlistatGeneric(IdResource.class,"identificadors");
			params+=identificadorFisicList.toUrl(prefix+"idsF_");
			
			//Afegeix les unitats on el recurs esta disponible
			if (disponibleA == null) disponibleA = new LlistatGeneric(Unitat.class,"disponibleA");
			params+=disponibleA.toUrl(prefix+"disp_");
			
			//Crea l'element format
			if (formatList == null) formatList = new ArrayList();
			for (int i=0; i < this.formatList.size();i++)
				params+=prefix+"f"+i+"="+(String)formatList.get(i)+"&";
		}		
		
		return params;
	}
	
	public String toUrl(String prefix) {
		String params="";
		
		if(unitatCreadora!=null && unitatCreadora.getIdentifier()!=null && !unitatCreadora.getIdentifier().equals(new Integer(0)))
		{
			params+="recFisic=true&";
			
			//Afegeix l'element caracteristiques
			if (caracteristiques == null) caracteristiques = "";
			params+="caractRFisic="+caracteristiques+"&";
			
			//Afegeix els identificadors fisics
			if (identificadorFisicList == null) identificadorFisicList = new LlistatGeneric(IdResource.class,"identificadors");
			//params+=identificadorFisicList.toUrl(prefix+"idsF_");
			ArrayList objectList = identificadorFisicList.getObjectList();
			
			if (objectList == null) objectList = new ArrayList();
//			TODO: ull!! 
			for (int i=0; i < objectList.size();i++)
			{	
				params+="tipusIdFisicSel="+((IdResource)objectList.get(i)).getType()+"&";
				params+="idFisic="+((IdResource)objectList.get(i)).getIdentifier()+"&";
			}
		}
		else params+="recFisic=false&";
		
		return params;
	}

}