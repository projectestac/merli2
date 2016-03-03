package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Classification extends ObjectMerli {

	private SourceValue purpose;
	private ArrayList taxonPath;
	private LangStringList description;
	
	
	
	public Classification() {
		super();
		// TODO Auto-generated constructor stub
		this.purpose = null;
		this.taxonPath = null;
		this.description = null;
	}
	/**
	 * Constructor d'una classification amb un únic TaxonPath.
	 * @param purpose
	 * @param taxonPath Es crea una llista amb el TaxonPath donat com a únic element.
	 * @param description
	 */
	public Classification(SourceValue purpose, TaxonPath taxonPath, LangStringList description) {
		super();
		// TODO Auto-generated constructor stub
		this.purpose = purpose;
		this.taxonPath = new ArrayList();
		this.taxonPath.add(taxonPath);
		this.description = description;
	}

	
	public Classification(SourceValue purpose, ArrayList taxonPath, LangString description) {
		super();
		// TODO Auto-generated constructor stub
		this.purpose = purpose;
		this.taxonPath = taxonPath;
		this.description = new LangStringList();
		this.description.setLangString(description);
	}




	public Classification(SOAPElement seClassification) throws SOAPException {
		
		Iterator it = seClassification.getChildElements(soapFactory.createName("purpose"));
		if (it.hasNext())
			purpose = new SourceValue((SOAPElement) it.next());
		
		it = seClassification.getChildElements(soapFactory.createName("taxonPath"));
//		if (it.hasNext())
//			taxonPath = new TaxonPath((SOAPElement) it.next());
		taxonPath = new ArrayList();
		while (it.hasNext())
			taxonPath.add(new TaxonPath((SOAPElement) it.next()));
		
		it = seClassification.getChildElements(soapFactory.createName("description"));
		if (it.hasNext()){
			if (description==null) description=new LangStringList();
			description.setLangString(new LangString((SOAPElement)it.next()));
		}
	}
	
	
	
	public LangString getDescription() {
		return description.getLangString("ca");
	}
	



	public void setDescription(LangString description) {
		if (this.description==null) this.description=new LangStringList();
		this.description.setLangString(description);
	}
	



	public SourceValue getPurpose() {
		return purpose;
	}
	



	public void setPurpose(SourceValue purpose) {
		this.purpose = purpose;
	}
	



	public ArrayList getTaxonPath() {
		return taxonPath;
	}
	public TaxonPath getTaxonPath(int pos) {
		return (TaxonPath) taxonPath.get(pos);
	}
	



	public void addTaxonPath(TaxonPath taxonPath) {
		if (taxonPath != null){
			if (this.taxonPath == null)
				this.taxonPath = new ArrayList();
			this.taxonPath.add(taxonPath);
		}
	}
	public void setTaxonPath(ArrayList taxonPath) {
		this.taxonPath = taxonPath;
	}
	



	public SOAPElement toXml() throws SOAPException {
		if (taxonPath == null || 
				taxonPath.size()<=0) {
			return null;
		}else{
			SOAPElement seClassification = soapFactory.createElement(soapFactory.createName("classification"));
			
			if (purpose == null) purpose = new SourceValue("purpose");
			purpose.setName("purpose");
			seClassification.addChildElement(purpose.toXml());				
	
			for(int i=0;i<taxonPath.size();i++){
				seClassification.addChildElement(((TaxonPath)taxonPath.get(i)).toXml());
			}
	
			if (description == null) description = new LangStringList();
			seClassification.addChildElement(description.toXml("description"));
			
			return seClassification;
		}
	}
	
	public String toUrl2(String prefix) {
		String params="";
		if (taxonPath == null || taxonPath.size()<=0) {
			return null;
		}else{
			if (purpose == null) purpose = new SourceValue("purpose");
			purpose.setName("purpose");
			params+=purpose.toUrl(prefix+"p_");
				
			for(int i=0;i<taxonPath.size();i++)
				params+=((TaxonPath)taxonPath.get(i)).toUrl(prefix+"tP"+i+"_");
	
			if (description == null) description = new LangStringList();
			params+=description.toUrl(prefix+"desc_");
		}
		
		
		return params;
	}
	
	public String toUrl(String prefix) {
		String params="";
		if (taxonPath == null || taxonPath.size()<=0) {
			return null;
		}
		else
		{
			if(purpose.getSource().indexOf("DUC")>=0)
			{
				String selecTerm="",selecLabel="";
				for(int i=0;i<taxonPath.size();i++)
				{
					ArrayList taxonList=((TaxonPath)taxonPath.get(i)).getTaxonList();
					//nomes ens interessen els tres ultims elements
					int in=Math.max(0, taxonList.size()-3);
					for(int j=in;j<taxonList.size();j++)
					{
						selecTerm+=((Taxon)(taxonList.get(j))).getId()+";";
						selecLabel+=((Taxon)(taxonList.get(j))).getEntry().getString()+";";
					}
				}
				if(selecTerm.length()>0) {selecTerm=selecTerm.substring(0, selecTerm.length()-1);selecLabel=selecLabel.substring(0, selecLabel.length()-1);}
				params+="curriTemp="+selecLabel+"&";
				params+="curriTempId="+selecTerm+"&";
			}
			else if(purpose.getSource().indexOf("ETB")>=0)
			{	
				String selecTerm="",selecLabel="";
				if (getTaxonPath() != null && getTaxonPath().size()>0)
				{
					for(int tp=0;tp<taxonPath.size();tp++)
					{
//						nomes ens interessa l'ultim terme del taxonPath
						//Taxon t = (Taxon)((TaxonPath)taxonPath.get(tp)).getTaxonList().get(taxonPath.size()-1);
						TaxonPath tPath = (TaxonPath)taxonPath.get(tp);
						Taxon t = (Taxon)tPath.getTaxonList().get(tPath.getTaxonList().size()-1);
						selecTerm+= t.getId()+";";
						selecLabel+= t.getEntry().getString()+";";
					}
					if(selecTerm.length()>0) {selecTerm=selecTerm.substring(0, selecTerm.length()-1);selecLabel=selecLabel.substring(0, selecLabel.length()-1);}
					params+="selecTerm="+selecTerm+"&selecLabel="+selecLabel+"&";
				}
			}
		}		
		return params;
	}


}
