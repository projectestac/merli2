package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import edu.xtec.semanticnet.Node;

public class ElementDUC extends ObjectMerli {

	private IdElement idElement;
	private String term;
	private String description;
	private String category;
	private String references;
	private int position;
	private TaxonPath taxonList;
	private ArrayList DUCRelationList;
	private Hashtable DUCRelationHash;
	private LangStringList termLang;
	
	

	public ElementDUC() {
		super();
		// TODO Auto-generated constructor stub
		this.idElement = null;
		this.term = "";
		this.description = "";
		this.category = "";
		this.references = "";
		this.termLang = null;
		this.taxonList = null;
		this.DUCRelationList = null;
		this.position = APXTEC32.MAXPOSITION;
	}





	public ElementDUC(IdElement idElement, String term, String description, String category, String references, TaxonPath taxonList, ArrayList DUCRelationList) {
		super();
		// TODO Auto-generated constructor stub
		this.idElement = idElement;
		this.term = term;
		this.description = description;
		this.category = category;
		this.references = references;
		this.taxonList = taxonList;
		this.DUCRelationList = DUCRelationList;
		this.position = APXTEC32.MAXPOSITION;
		this.termLang = null;
	}

	public ElementDUC(IdElement idElement, String term, String description, String category, String references, TaxonPath taxonList, ArrayList DUCRelationList, int position) {
		super();
		// TODO Auto-generated constructor stub
		this.idElement = idElement;
		this.term = term;
		this.description = description;
		this.category = category;
		this.references = references;
		this.taxonList = taxonList;
		this.DUCRelationList = DUCRelationList;
		this.position = position;
		this.termLang = null;
	}





	public ElementDUC(SOAPElement seElementDUC) throws SOAPException {
		
		this.DUCRelationList = new ArrayList();
		
		Iterator it = seElementDUC.getChildElements(soapFactory.createName("idElement"));
		idElement = new IdElement((SOAPElement) it.next());
		
		it = seElementDUC.getChildElements(soapFactory.createName("term"));
		term = ((SOAPElement) it.next()).getValue();
		
		it = seElementDUC.getChildElements(soapFactory.createName("description"));
		description = ((SOAPElement) it.next()).getValue();
		
		it = seElementDUC.getChildElements(soapFactory.createName("category"));
		category = ((SOAPElement) it.next()).getValue();

		it = seElementDUC.getChildElements(soapFactory.createName("references"));
		references = ((SOAPElement) it.next()).getValue();

		it = seElementDUC.getChildElements(soapFactory.createName("position"));
		try{
			position = Integer.parseInt(((SOAPElement) it.next()).getValue());
		}catch(Exception e){
			position = APXTEC32.MAXPOSITION;
		}
	
		
		it = seElementDUC.getChildElements(soapFactory.createName("taxonPath"));
		taxonList = new TaxonPath((SOAPElement) it.next());
		
		it = seElementDUC.getChildElements(soapFactory.createName("DUCRelation"));
		while (it.hasNext())
			DUCRelationList.add(new DUCRelation((SOAPElement) it.next()));
	}





	public String getDescription() {
		return description;
	}
	




	public void setDescription(String description) {
		this.description = description;
	}
	




	public ArrayList getDUCRelationList() {
		return DUCRelationList;
	}
	




	public void setDUCRelationList(ArrayList relationList) {
		DUCRelationList = relationList;
	}
	

	public void setDUCRelationList(Hashtable relationList) {
		DUCRelationHash = relationList;
	}
	




	public IdElement getIdElement() {
		return idElement;
	}
	




	public void setIdElement(IdElement idElement) {
		this.idElement = idElement;
	}
	




	public String getCategory() {
		return category;
	}
	





	public void setCategory(String category) {
		this.category = category;
	}
	





	public String getReferences() {
		return references;
	}
	




	public void setReferences(String references) {
		this.references = references;
	}
	




	public TaxonPath getTaxonList() {
		return taxonList;
	}
	




	public void setTaxonList(TaxonPath taxonList) {
		this.taxonList = taxonList;
	}
	




	public String getTerm() {
		return term;
	}
	




	public void setTerm(String term) {
		this.term = term;
	}
	




	public LangStringList getTermLang() {
		return termLang;
	}





	public void setTermLang(LangStringList termLang) {
		this.termLang = termLang;
	}





	public SOAPElement toXml() throws SOAPException {
		SOAPElement seElementDUC=null;
		SOAPElement seTerm;
		SOAPElement seDescrpition;
		SOAPElement seCategory;
		SOAPElement seReferences;
		SOAPElement sePosition;    
		
		//Crea l'element ElementDUC
		seElementDUC = soapFactory.createElement("elementDUC");
		
		//Afageix l'element Identifier al idElement
		if (idElement == null) idElement = new IdElement();
		seElementDUC.addChildElement(this.idElement.toXml());

		//Crea l'element term
		if (term == null) term = "";
		seTerm = soapFactory.createElement("term");
		seTerm.addTextNode(this.term);
		//Afageix l'element term al ElementDUC
		seElementDUC.addChildElement(seTerm);


		//Crea l'element termLang
		if (termLang == null) termLang = new LangStringList();
		//Afageix l'element termLang al ElementDUC
		seElementDUC.addChildElement(this.termLang.toXml("termLang"));
		
		//Crea l'element description
		if (description == null) description = "";
		if (!description.equals("")){
			seDescrpition = soapFactory.createElement("description");
			seDescrpition.addTextNode(this.description);
			//Afageix l'element description al ElementDUC
			seElementDUC.addChildElement(seDescrpition);
		}
		//Crea l'element category
		if (category == null) category = "";
		if (!category.equals("")){
			seCategory= soapFactory.createElement("category");
			seCategory.addTextNode(this.category);
			//Afageix l'element category al ElementDUC
			seElementDUC.addChildElement(seCategory);
		}

		//Crea l'element Position
		if (this.position != APXTEC32.MAXPOSITION){
		sePosition= soapFactory.createElement("position");
		sePosition.addTextNode(String.valueOf(this.position));
		//Afageix l'element position al ElementDUC
		seElementDUC.addChildElement(sePosition);
		}
		
		//Crea l'element references
		if (references == null) references = "";
		if (!references.equals("")){
		seReferences = soapFactory.createElement("references");
		seReferences.addTextNode(this.references);
		//Afageix l'element references al ElementDUC
		seElementDUC.addChildElement(seReferences);
		}
		
		//Afageix l'element taxonList al ElementDUC
		if (taxonList == null) taxonList = new TaxonPath();
		else
		seElementDUC.addChildElement(this.taxonList.toXml());

		//Crea els elements DUCRelation
		if (DUCRelationList == null) DUCRelationList = new ArrayList();
		for(int i=0;i<DUCRelationList.size();i++){
			//Afageix l'element DUCRelation al ElementDUC
			seElementDUC.addChildElement(((DUCRelation)this.DUCRelationList.get(i)).toXml());		
		}
		if (DUCRelationHash != null && !DUCRelationHash.isEmpty()){
			Enumeration nume = DUCRelationHash.elements();
			//while (nume.hasMoreElements()){				
				//Enumeration en = ((Hashtable)nume.nextElement()).elements();
				while (nume.hasMoreElements()){				
					seElementDUC.addChildElement(((DUCRelation)nume.nextElement()).toXml());
				}
			//}
		}
		
		return seElementDUC;
	}




/*
	public ArrayList getFieldsList(String string) {
		ArrayList al = new ArrayList();

		if("id".equals(string)){
			al.add("id_node");
		}
		if("cur_content".equals(string)){
			al.add("id_node");
			al.add("v_term");
			al.add("i_position");
			al.add("v_description");
			al.add("v_category");
			al.add("v_references");
			al.add("id_nodecur_content");
			
		}

		if("cur_level".equals(string)){
			al.add("id_node");
			al.add("v_term");
			al.add("i_position");
			al.add("v_description");
			al.add("v_category");
			al.add("v_references");
			al.add("id_nodecur_level");
		}
		
		if("cur_area".equals(string)){
			al.add("id_node");
			al.add("v_term");
			al.add("i_position");
			al.add("v_description");
			al.add("v_references");
			al.add("id_nodecur_level");
		}
		
		if("cur_thesaurus_etb".equals(string)){
			al.add("terme_ca");
			al.add("id_terme");
			
		}
		if("cur_level_area_content".equals(string)){
			al.add("id_node");
			al.add("v_term");
			al.add("i_position");
			al.add("v_description");
			al.add("v_references");			
		}

		return al;
	}


*/


	public List getFieldsList(String string, String string2) {
		ArrayList al = new ArrayList();

		if("cur_thesaurus_etb".equals(string)){
			al.add(/*string2 +*/ "terme_ca");
			al.add(/*string2 +*/ "id_terme");
			
		}
		if("cur_area".equals(string)){
			al.add(string2 + ".id_node");
			al.add(string2 + ".v_term");
			al.add(string2 + ".i_position");
			al.add(string2 + ".v_description");
			al.add(string2 + ".v_references");			
		}

		return al;
	}





	public int getPosition() {
		return position;
	}
	





	public void setPosition(int position) {
		this.position = position;
	}





	public void copyFromSemanticNet(Node node) {
		
		this.setTerm(node.getTerm());
		if (this.termLang==null) this.termLang = new LangStringList();
		this.getTermLang().setLangString(new LangString(this.getTerm(),"ca"));
	
		this.setDescription(node.getDescription());
		this.setIdElement(new IdElement(String.valueOf(node.getIdNode()),node.getNodeType()));
		if (node.getProperties() != null){
			if (!"".equals(node.getProperties().get("position"))	&& 
				node.getProperties().get("position") != null)
				this.setPosition(((Integer)node.getProperties().get("position")).intValue());

			if (!"".equals(node.getProperties().get("references"))	&& 
				node.getProperties().get("references") != null)
				this.setReferences((String) node.getProperties().get("references"));

			if (!"".equals(node.getProperties().get("v_term_es"))	&& 
				node.getProperties().get("v_term_es") != null){
				this.getTermLang().setLangString(new LangString(((String) node.getProperties().get("v_term_es")),"es"));
			}
			if (!"".equals(node.getProperties().get("v_term_en"))	&& 
				node.getProperties().get("v_term_en") != null){
				this.getTermLang().setLangString(new LangString(((String) node.getProperties().get("v_term_en")),"en"));
			}
			if (!"".equals(node.getProperties().get("v_term_oc"))	&& 
				node.getProperties().get("v_term_oc") != null){
				this.getTermLang().setLangString(new LangString(((String) node.getProperties().get("v_term_oc")),"oc"));
			}
		}
		this.setCategory(node.getCategory());	
	}
	

}
