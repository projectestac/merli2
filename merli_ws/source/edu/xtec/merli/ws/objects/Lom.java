package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.utils.Utility;

public class Lom extends ObjectMerli {

	private General general;
	private LifeCycle lifeCycle;
	private MetaMetadata metaMetaData;
	private Technical technical;
	private Educational educational;
	private Rights rights;
	private ArrayList classificationList;
	
	
	
	
	

	public Lom() {
		super();
		// TODO Auto-generated constructor stub
		this.general = new General();
		this.lifeCycle = new LifeCycle();
		this.metaMetaData = new MetaMetadata();
		this.technical = new Technical();
		this.educational = new Educational();
		this.rights = new Rights();
		this.classificationList = new ArrayList();
	}

	public Lom(General general, LifeCycle lifeCycle, MetaMetadata metaMetaData, Technical technical, Educational educational, Rights rights, ArrayList classificationList) {
		super();
		// TODO Auto-generated constructor stub
		this.general = general;
		this.lifeCycle = lifeCycle;
		this.metaMetaData = metaMetaData;
		this.technical = technical;
		this.educational = educational;
		this.rights = rights;
		this.classificationList = classificationList;
	}







	public Lom(SOAPElement seLom){

		this.classificationList = new ArrayList();
		
        Iterator It;
		try {
			It = seLom.getChildElements(soapFactory.createName("general"));		
			if (It.hasNext())
				general= new General(((SOAPElement)It.next()));
			
			It=seLom.getChildElements(soapFactory.createName("lifeCycle"));
			if (It.hasNext())
				lifeCycle= new LifeCycle(((SOAPElement)It.next()));
			
			It=seLom.getChildElements(soapFactory.createName("metaMetadata"));
			if (It.hasNext())
				metaMetaData= new MetaMetadata(((SOAPElement)It.next()));
			
			It=seLom.getChildElements(soapFactory.createName("technical"));
			if (It.hasNext())
				technical= new Technical(((SOAPElement)It.next()));
			
			It=seLom.getChildElements(soapFactory.createName("educational"));
			if (It.hasNext())
				educational= new Educational(((SOAPElement)It.next()));
			
			It=seLom.getChildElements(soapFactory.createName("rights"));
			if (It.hasNext())
				rights= new Rights(((SOAPElement)It.next()));
			
			It=seLom.getChildElements(soapFactory.createName("classification"));
			while (It.hasNext())
				classificationList.add(new Classification(((SOAPElement)It.next())));
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public SOAPElement toXml() throws SOAPException {
		SOAPElement seLom = null;
		try{
		//Crea l'element taxon
		seLom = soapFactory.createElement("lom");
		seLom.addNamespaceDeclaration("euncl","http://celebrate.eun.org/xml/ns/celebrateLOM-0_2");
		seLom.addNamespaceDeclaration("xsi","http://www.w3.org/2001/XMLSchema-instance");
		seLom.addNamespaceDeclaration("rights","http://celebrate.eun.org/xml/ns/rights-0_1");
		seLom.addNamespaceDeclaration("schemaLocation","http://celebrate.eun.org/xml/ns/celebrateLOM-0_2 http://celebrate.eun.org/xml/schemas/celebrateLOM-0.2.xsd");
		//Afageix tots els elements
		if (general == null) general = new General();
		seLom.addChildElement(general.toXml());
		if (lifeCycle == null) lifeCycle = new LifeCycle();
		seLom.addChildElement(lifeCycle.toXml());
		if (metaMetaData == null) metaMetaData = new MetaMetadata();
		seLom.addChildElement(metaMetaData.toXml());
		if (technical == null) technical = new Technical();
		seLom.addChildElement(technical.toXml());
		if (educational == null) educational = new Educational();
		seLom.addChildElement(educational.toXml());
		if (rights == null) rights = new Rights();
		seLom.addChildElement(rights.toXml());
		
		if (classificationList == null) classificationList = new ArrayList();
		for (int i=0;i<classificationList.size();i++){
			seLom.addChildElement(((Classification)classificationList.get(i)).toXml());
		}
		}catch (Exception e){
			e.printStackTrace();
		}
		return seLom;
	
	}







	public ArrayList getClassificationList() {
		return classificationList;
	}
	







	public void setClassificationList(ArrayList classificationList) {
		this.classificationList = classificationList;
	}
	







	public Educational getEducational() {
		return educational;
	}
	







	public void setEducational(Educational educational) {
		this.educational = educational;
	}
	







	public General getGeneral() {
		return general;
	}
	







	public void setGeneral(General general) {
		this.general = general;
	}
	







	public LifeCycle getLifeCycle() {
		return lifeCycle;
	}
	







	public void setLifeCycle(LifeCycle lifeCycle) {
		this.lifeCycle = lifeCycle;
	}
	







	public MetaMetadata getMetaMetaData() {
		return metaMetaData;
	}
	







	public void setMetaMetaData(MetaMetadata metaMetaData) {
		this.metaMetaData = metaMetaData;
	}
	







	public Rights getRights() {
		return rights;
	}
	







	public void setRights(Rights rights) {
		this.rights = rights;
	}
	







	public Technical getTechnical() {
		return technical;
	}
	







	public void setTechnical(Technical technical) {
		this.technical = technical;
	}

	public ArrayList getAddQuery() throws MerliDBException {
		String query;
		ArrayList res = new ArrayList();
		
		res.add(new Integer(general.getIdentifier().getIdLom()));
		if (general.getTitle().getString() == null || general.getTitle().getString() == "")
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		res.add(general.getTitle().getString());
		res.add(Utility.aplanarText(general.getTitle().getString()));

		if (general.getDescription().getString() == null || general.getDescription().getString() == "")
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		res.add(general.getDescription().getString());
		res.add(Utility.aplanarText(general.getDescription().getString()));
		
		if (technical.getLocation() == null || technical.getLocation() == "")
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		res.add(technical.getLocation());
		
		if (lifeCycle.getVersion().getString() == null)
			lifeCycle.getVersion().setString("");
		res.add(lifeCycle.getVersion().getString());
		
		res.add(educational.getTypicalAgeRangeMin());
		res.add(educational.getTypicalAgeRangeMax());
		
		if (educational.getDifficulty().getId() == null || educational.getDifficulty().getId() == "0")
			educational.getDifficulty().setId("");
		res.add(educational.getDifficulty().getId());
		
		if (educational.getTypicalLearningTime().getDuration() == null)
			educational.getTypicalLearningTime().setDuration("");
		res.add(educational.getTypicalLearningTime().getDuration());
		
		if (rights.getCost() != null && rights.getCost().compareTo("yes")==0)
			res.add(new Integer(1));
		else
			res.add(new Integer(0));
		//EndUserRol ID_ROL_USUARI, ja no es desa a la taula mer_recurs.
		res.add("");
		res.add( new Integer(0));
		Integer idAmbit = new Integer(0);
		for (int i=0; i < classificationList.size();i++){
			if ("AMBIT".equals(((Classification)classificationList.get(i)).getTaxonPath().getSource().getString())){
				idAmbit = new Integer(((Taxon)((Classification)classificationList.get(i)).getTaxonPath().getTaxonList().get(0)).getId());
			}				
		}
		res.add(idAmbit);
					
		return res;
	}
	
	public ArrayList getAddLang() throws MerliDBException 
	{
		String query;
		ArrayList res = new ArrayList();
		
		//Id
		res.add(new Integer(general.getIdentifier().getIdLom()));
		
		//Lang
		res.add("ca");
		
		//Title
		if (general.getTitle().getString() == null || general.getTitle().getString() == "")
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		res.add(general.getTitle().getString());			

		//Descripcio
		if (general.getDescription().getString() == null || general.getDescription().getString() == "")
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		res.add(general.getDescription().getString());
		
		//Drets
		if (rights.getDescription().getString() == null || rights.getDescription().getString() == ""){
			res.add("");
		}else
			res.add(rights.getDescription().getString());

		//Estat
		res.add(new Integer(0));
					
		return res;
	}
	
	
	public String getSetQuery(String table){
		// TODO Auto-generated method stub
		String query = "";
		
		if (table.compareTo("mer_recurs")==0){
				query += " titol = ?,";//'"+Utility.toParaulaDB(this.getTitle())+"',";
				query += " titol_pla = ?,";//'"+Utility.aplanarText(this.getTitle())+"',";
				query += " descripcio  = ?,";//'"+Utility.toParaulaDB(this.getDescription())+"',";
				query += " desc_pla  = ?,";//'"+Utility.aplanarText(this.getDescription())+"',";
				query += " url  = ?,";//'"+Utility.toParaulaDB(this.getUrl())+"',";
				query += " versio  = ?,";//'"+Utility.toParaulaDB(this.getVersion())+"',";
				query += " edat_min  = ?,";//"+Utility.toParaulaDB(this.getMinAge())+",";
				query += " edat_max  = ?,";//"+Utility.toParaulaDB(this.getMaxAge())+",";
				query += " id_dificultat  = ?,";//"+Utility.toParaulaDB(this.getDifficulty())+",";
				query += " duracio  = ?,";//'"+Utility.toParaulaDB(this.getLearningTime())+"',";
				query += " drets  = ?,";
				query += " id_ambit  = ?";//"+Utility.toParaulaDB(this.getAmbit())+"";
		}
		else if (table.compareTo("mer_rec_lang")==0){
			//query += " lang = ?,";//'"+Utility.toParaulaDB(this.getTitle())+"',";
			query += " titol = ?,";//'"+Utility.toParaulaDB(this.getTitle())+"',";
			query += " descripcio = ?,";//'"+Utility.toParaulaDB(this.getTitle())+"',";
			query += " drets = ?,";//'"+Utility.toParaulaDB(this.getTitle())+"',";
			query += " estat = ?";//'"+Utility.toParaulaDB(this.getTitle())+"',";
		}
		return query;
	}	
	
	
	public ArrayList getSetQueryFields(String table) throws MerliDBException {
		// TODO Auto-generated method stub
		ArrayList res = new ArrayList();
		
		if (table.compareTo("mer_recurs")==0){
			if (general.getTitle().getString() == null || general.getTitle().getString() == "")
				throw new MerliDBException(MerliDBException.ERROR_SQL);
			res.add(general.getTitle().getString());
			res.add(Utility.aplanarText(general.getTitle().getString()));
			
			if (general.getDescription().getString() == null || general.getDescription().getString() == "")
				throw new MerliDBException(MerliDBException.ERROR_SQL);
			res.add((general.getDescription().getString()));
			res.add(Utility.aplanarText(general.getDescription().getString()));

			if (technical.getLocation() == null || technical.getLocation() == "")
				throw new MerliDBException(MerliDBException.ERROR_SQL);
			res.add((technical.getLocation()));
			
			if (lifeCycle.getVersion().getString() == null)
				lifeCycle.getVersion().setString("");
			res.add((lifeCycle.getVersion().getString()));
			
			res.add(educational.getTypicalAgeRangeMin());
			res.add(educational.getTypicalAgeRangeMax());
			
			if (educational.getDifficulty().getId() == null || educational.getDifficulty().getId() == "0")
				educational.getDifficulty().setId("");
			res.add((educational.getDifficulty().getId()));

			if (educational.getTypicalLearningTime().getDuration() == null)
				educational.getTypicalLearningTime().setDuration("");
			res.add((educational.getTypicalLearningTime().getDuration()));
			
			if (rights.getCost() != null && rights.getCost().compareTo("yes")==0)
				res.add(new Integer(1));
			else
				res.add(new Integer(0));			

			Integer idAmbit = new Integer(0);
//			for (int i=0; i < classificationList.size();i++){
//				if ("AMBIT".equals(((Classification)classificationList.get(i)).getTaxonPath().getSource().getString())){
//					idAmbit = new Integer(((Taxon)((Classification)classificationList.get(i)).getTaxonPath().getTaxonList().get(0)).getId());
//				}				
//			}
			res.add(idAmbit);
		}
		else if(table.compareTo("mer_rec_lang")==0){
			//Lang
			//res.add("ca");
			
			//Title
			if (general.getTitle().getString() == null || general.getTitle().getString() == "")
				throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
			res.add(general.getTitle().getString());			

			//Descripcio
			if (general.getDescription().getString() == null || general.getDescription().getString() == "")
				throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
			res.add(general.getDescription().getString());
			
			//Drets
			if (rights.getDescription().getString() == null)
				res.add("");
			else
				res.add(rights.getDescription().getString());

			//Estat
			res.add(new Integer(0));
			
		}
		return res;
	}

	

}
