package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Enumeration;
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
	private ArrayList relacions;
	private DescripcioFisica descripcioFisica;

	public Lom() {
		super();
		this.general = new General();
		this.lifeCycle = new LifeCycle();
		this.metaMetaData = new MetaMetadata();
		this.technical = new Technical();
		this.educational = new Educational();
		this.rights = new Rights();
		this.classificationList = new ArrayList();
		this.relacions = new ArrayList();
		this.descripcioFisica = new DescripcioFisica();
	}

	public Lom(General general, LifeCycle lifeCycle, MetaMetadata metaMetaData, Technical technical, Educational educational, Rights rights, ArrayList classificationList, ArrayList relacions, DescripcioFisica descripcioFisica) {
		super();
		this.general = general;
		this.lifeCycle = lifeCycle;
		this.metaMetaData = metaMetaData;
		this.technical = technical;
		this.educational = educational;
		this.rights = rights;
		this.classificationList = classificationList;
		this.relacions = relacions;
		this.descripcioFisica = descripcioFisica;
	}

	public Lom(SOAPElement seLom){

		this.classificationList = new ArrayList();
		this.relacions = new ArrayList();
		
        Iterator It;
		try {
			It = seLom.getChildElements(soapFactory.createName("general"));		
			if (It.hasNext())
				general= new General(((SOAPElement)It.next()));
			else
				general = new General();
			
			It=seLom.getChildElements(soapFactory.createName("lifeCycle"));
			if (It.hasNext())
				lifeCycle = new LifeCycle(((SOAPElement)It.next()));
			else
				lifeCycle = new LifeCycle();
			
			It=seLom.getChildElements(soapFactory.createName("metaMetadata"));
			if (It.hasNext())
				metaMetaData= new MetaMetadata(((SOAPElement)It.next()));
			else
				metaMetaData= new MetaMetadata();
			
			It=seLom.getChildElements(soapFactory.createName("technical"));
			if (It.hasNext())
				technical= new Technical(((SOAPElement)It.next()));
			else
				technical= new Technical();
			
			It=seLom.getChildElements(soapFactory.createName("educational"));
			if (It.hasNext())
				educational= new Educational(((SOAPElement)It.next()));
			else
				educational= new Educational();
			
			It=seLom.getChildElements(soapFactory.createName("rights"));
			if (It.hasNext())
				rights= new Rights(((SOAPElement)It.next()));
			else
				rights= new Rights();
			
			It = seLom.getChildElements(soapFactory.createName("relation"));		
			while (It.hasNext())
				relacions.add(new Relacio(((SOAPElement)It.next())));
			
			It=seLom.getChildElements(soapFactory.createName("classification"));
			while (It.hasNext())
				classificationList.add(new Classification(((SOAPElement)It.next())));
			
			It=seLom.getChildElements(soapFactory.createName("descripcioFisica"));
			if (It.hasNext())
				descripcioFisica = new DescripcioFisica(((SOAPElement)It.next()));
			
		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}


	public SOAPElement toXml() throws SOAPException {
		return toXml(
				(this.getMetaMetaData() != null && this.getMetaMetaData().isLomEs()) ||
				(this.getRights() != null && this.getRights().isLomEs()) ||
				(this.getEducational() != null && this.getEducational().isLomEs())
				);
	}
	public SOAPElement toXml(boolean lomes) throws SOAPException {
		SOAPElement seLom = null;
		try{
		//Crea l'element taxon
			seLom = soapFactory.createElement("lom");
	
			//seLom.addNamespaceDeclaration("xmlns","http://ltsc.ieee.org/xsd/LOMv1p0");
			//seLom.addNamespaceDeclaration("euncl","http://celebrate.eun.org/xml/ns/celebrateLOM-0_2");
			//seLom.addNamespaceDeclaration("xsi","http://www.w3.org/2001/XMLSchema-instance");
			//seLom.addNamespaceDeclaration("rights","http://celebrate.eun.org/xml/ns/rights-0_1");
	//		if (lomes){
	//			seLom.addNamespaceDeclaration("xmlns","http://ltsc.ieee.org/xsd/LOM");
	////			seLom.addAttribute(soapFactory.createName("xmlns"),"http://ltsc.ieee.org/xsd/LOM");
	////			seLom.addAttribute(soapFactory.createName("xmnls:xsi"),"http://www.w3.org/2001/XMLSchema-instance");
	////			seLom.addAttribute(soapFactory.createName("xsi:schemaLocation"),"http://ltsc.ieee.org/xsd/LOM lomCustom.xsd");
	//		}
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
			
			if (relacions == null) relacions = new ArrayList();
			for (int i=0;i<relacions.size();i++){
				if (((Relacio)relacions.get(i)).getIdRecursRel() != null && !((Relacio)relacions.get(i)).getIdRecursRel().equals(new Integer(0)))
					seLom.addChildElement(((Relacio)relacions.get(i)).toXml());
			}
			
			if (classificationList == null) classificationList = new ArrayList();
			for (int i=0;i<classificationList.size();i++){
				if (((Classification)classificationList.get(i)).getTaxonPath() != null && ((Classification)classificationList.get(i)).getTaxonPath().size()>0)
					seLom.addChildElement(((Classification)classificationList.get(i)).toXml());
			}
			
			if (descripcioFisica == null) descripcioFisica = new DescripcioFisica();
			SOAPElement seDescripcioFisica=descripcioFisica.toXml();
			if(!lomes && seDescripcioFisica != null)seLom.addChildElement(seDescripcioFisica);
			
		}catch (Exception e){
			e.printStackTrace();
		}
		return seLom;
	
	}

	public String toUrl2() {
		String params_url="?";
		try{
				//Afageix tots els elements
				if (general == null) general = new General();
				params_url+=general.toUrl("g_");
				if (lifeCycle == null) lifeCycle = new LifeCycle();
				params_url+=lifeCycle.toUrl("lc_");
				if (metaMetaData == null) metaMetaData = new MetaMetadata();
				params_url+=metaMetaData.toUrl("m_");
				if (technical == null) technical = new Technical();
				params_url+=technical.toUrl("t_");
				if (educational == null) educational = new Educational();
				params_url+=educational.toUrl("e_");
				if (rights == null) rights = new Rights();
				params_url+=rights.toUrl("r_");
				
				if (relacions == null) relacions = new ArrayList();
				for (int i=0;i<relacions.size();i++){
					if (((Relacio)relacions.get(i)).getIdRecursRel() != null && !((Relacio)relacions.get(i)).getIdRecursRel().equals(new Integer(0)))
						params_url+=((Relacio)relacions.get(i)).toUrl("rel"+i+"_");
				}
				
				if (classificationList == null) classificationList = new ArrayList();
				for (int i=0;i<classificationList.size();i++){
					if (((Classification)classificationList.get(i)).getTaxonPath() != null && ((Classification)classificationList.get(i)).getTaxonPath().size()>0)
						params_url+=((Classification)classificationList.get(i)).toUrl("c"+i+"_");
				}
				
				if (descripcioFisica == null) descripcioFisica = new DescripcioFisica();
				params_url+=descripcioFisica.toUrl("dF_");
				
			}catch (Exception e){
				e.printStackTrace();
			}
			if(params_url.length()>0) params_url=params_url.substring(0, params_url.length()-1);	//trec l'últim &
			return params_url;
	}
	
	public String toUrl() {
		String params_url="";
		try{
				//Afageix tots els elements
				if (general == null) general = new General();
				params_url+=general.toUrl("");
				if (lifeCycle == null) lifeCycle = new LifeCycle();
				params_url+=lifeCycle.toUrl("");
				if (metaMetaData == null) metaMetaData = new MetaMetadata();
				params_url+=metaMetaData.toUrl("");
				if (technical == null) technical = new Technical();
				params_url+=technical.toUrl("");
				if (educational == null) educational = new Educational();
				params_url+=educational.toUrl("");
				if (rights == null) rights = new Rights();
				params_url+=rights.toUrl("");
				
				if (relacions == null) relacions = new ArrayList();
				for (int i=0;i<relacions.size();i++){
					if (((Relacio)relacions.get(i)).getIdRecursRel() != null && !((Relacio)relacions.get(i)).getIdRecursRel().equals(new Integer(0)))
						params_url+=((Relacio)relacions.get(i)).toUrl("");
				}
				
				if (classificationList == null) classificationList = new ArrayList();
				for (int i=0;i<classificationList.size();i++){
					if (((Classification)classificationList.get(i)).getTaxonPath() != null && ((Classification)classificationList.get(i)).getTaxonPath().size()>0)
						params_url+=((Classification)classificationList.get(i)).toUrl("");
				}
				
				if (descripcioFisica == null) descripcioFisica = new DescripcioFisica();
				params_url+=descripcioFisica.toUrl("");
//				
////				Afageix l'element keyword al general
//				String selecTerm="",selecLabel="";
//				if (general.getKeywordList() == null) general.setKeywordList(new ArrayList());
//				for (int i = 0; i < general.getKeywordList().size(); i++){
//					LangStringList lKeyword = (LangStringList) general.getKeywordList().get(i);
//					Enumeration eK = lKeyword.getListString().elements();
//					while(eK.hasMoreElements())
//					{
//						selecTerm+="0,";
//						selecLabel+=((LangString) eK.nextElement()).getString()+",";
//					}
//				}
//				for (int i=0;i<classificationList.size();i++){
//					if (((Classification)classificationList.get(i)).getTaxonPath() != null && ((Classification)classificationList.get(i)).getTaxonPath().size()>0)
//					{
//						ArrayList taxonPath = (((Classification)classificationList.get(i)).getTaxonPath());
//						for(int tp=0;tp<taxonPath.size();tp++)
//						{
//							ArrayList taxonList=((TaxonPath)taxonPath.get(tp)).getTaxonList();
//							for(int t=0;t<taxonList.size();t++)
//							{
//								selecTerm+=((Taxon)(taxonList.get(t))).getId()+",";
//								selecLabel+=((Taxon)(taxonList.get(t))).getEntry().getString()+",";
//							}
//						}
//					}
//				}
//				if(selecTerm.length()>0) {selecTerm=selecTerm.substring(0, selecTerm.length()-1);selecLabel=selecLabel.substring(0, selecLabel.length()-1);}
//				params_url+="selecTerm="+selecTerm+"&selecLabel="+selecLabel;
				if(params_url.length()>0) params_url=params_url.substring(0, params_url.length()-1);
				
				
			}catch (Exception e){
				e.printStackTrace();
			}
			return params_url;
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
	
	public ArrayList getRelacions() {
		return relacions;
	}
	public void setRelacions(ArrayList relacions) {
		this.relacions = relacions;
	}
	
	public DescripcioFisica getDescripcioFisica() {
		return descripcioFisica;
	}
	public void setDescripcioFisica(DescripcioFisica descripcioFisica) {
		this.descripcioFisica = descripcioFisica;
	}

	public ArrayList getAddQuery() throws MerliDBException {
		String query;
		ArrayList res = new ArrayList();
		
		res.add(new Integer(general.getIdentifier().getIdLom()));
		if (general.getTitle(APXTEC32.DEFAULT_LANG).getString() == null || general.getTitle(APXTEC32.DEFAULT_LANG).getString() == "")
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		res.add(general.getTitle(APXTEC32.DEFAULT_LANG).getString());
		res.add(Utility.aplanarText(general.getTitle(APXTEC32.DEFAULT_LANG).getString()));

		if (general.getDescription().getString(APXTEC32.DEFAULT_LANG) == null || general.getDescription(APXTEC32.DEFAULT_LANG).getString() == "")
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		res.add(general.getDescription(APXTEC32.DEFAULT_LANG).getString());
		res.add(Utility.aplanarText(general.getDescription(APXTEC32.DEFAULT_LANG).getString()));
		
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
			educational.getTypicalLearningTime().setDuration("P");
		res.add(educational.getTypicalLearningTime().getDuration());
		
		if (rights.getCost() != null && rights.getCost().getValue() != null && rights.getCost().getValue().compareTo("yes")==0)
			res.add(new Integer(1));
		else
			res.add(new Integer(0));
		//EndUserRol ID_ROL_USUARI, ja no es desa a la taula mer_recurs.
		res.add("");
		res.add( new Integer(0));
		/*Integer idAmbit = new Integer(0);
		for (int i=0; i < classificationList.size();i++){
			for (int j=0; j < ((Classification)classificationList.get(i)).getTaxonPath().size();j++){
				if ("AMBIT".equals(((Classification)classificationList.get(i)).getTaxonPath(j).getSource().getString())){
					idAmbit = new Integer(((Taxon)((Classification)classificationList.get(i)).getTaxonPath(j).getTaxonList().get(0)).getId());
				}	
			}
		}*/
		
		res.add("");//res.add(idAmbit);
		
		if (general.getCoverage(APXTEC32.DEFAULT_LANG)!= null)
			res.add(general.getCoverage(APXTEC32.DEFAULT_LANG).getString());//coverage
		else res.add("");
					
		return res;
	}
	
	public ArrayList getAddLang(String lang) throws MerliDBException 
	{
		String query;
		ArrayList res = new ArrayList();
		
		//Id
		if (general.getIdentifier().getIdLom()>0)
			res.add(new Integer(general.getIdentifier().getIdLom()));
		else
			res.add(new Integer(general.getIdentifier().getEntry()));
		
		//Lang
		res.add(lang);
		
		//Title
		if (general.getTitle(lang) == null || general.getTitle(lang).getString() == null || general.getTitle(lang).getString().equals(""))
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		res.add(general.getTitle(lang).getString());			

		//Descripcio
		if (general.getDescription(lang)==null || general.getDescription(lang).getString() == null || general.getDescription(lang).getString().equals(""))
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		res.add(general.getDescription(lang).getString());
		
		//Drets
		if (rights.getDescription(lang)==null || rights.getDescription(lang).getString() == null || rights.getDescription(lang).getString().equals("")){
			res.add("");
		}else
			res.add(rights.getDescription(lang).getString());

		//Estat
		res.add(new Integer(1));
					
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
				query += " context = ?";
//				query += " id_ambit  = ?";//"+Utility.toParaulaDB(this.getAmbit())+"";
		}
		else if (table.compareTo("mer_rec_lang")==0){
			query += " lang = ?,";//'"+Utility.toParaulaDB(this.getTitle())+"',";
			query += " titol = ?,";//'"+Utility.toParaulaDB(this.getTitle())+"',";
			query += " descripcio = ?,";//'"+Utility.toParaulaDB(this.getTitle())+"',";
			query += " drets = ?,";//'"+Utility.toParaulaDB(this.getTitle())+"',";
			query += " estat = ?";//'"+Utility.toParaulaDB(this.getTitle())+"',";
		}
		return query;
	}	
	
	
	public ArrayList getSetQueryFields(String table, String lang) throws MerliDBException {
		ArrayList res = new ArrayList();
		
		if (table.compareTo("mer_recurs")==0){
			if (general.getTitle(lang)==null || general.getTitle(lang).getString() == null || general.getTitle(lang).getString() == "")
				throw new MerliDBException(MerliDBException.CAMPS_OBLIGATORIS);
			res.add(general.getTitle(lang).getString());
			res.add(Utility.aplanarText(general.getTitle(lang).getString()));
			
			if (general.getDescription(lang)==null || general.getDescription(lang).getString() == null || general.getDescription(lang).getString() == "")
				throw new MerliDBException(MerliDBException.CAMPS_OBLIGATORIS);
			res.add((general.getDescription(lang).getString()));
			res.add(Utility.aplanarText(general.getDescription(lang).getString()));

			if (technical.getLocation() == null || technical.getLocation() == "")
				throw new MerliDBException(MerliDBException.CAMPS_OBLIGATORIS);
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
			
			if (rights.getCost() != null && rights.getCost().getValue() != null && rights.getCost().getValue().compareTo("yes")==0)
				res.add(new Integer(1));
			else
				res.add(new Integer(0));			

//			Integer idAmbit = new Integer(0);
//			for (int i=0; i < classificationList.size();i++){
//				if ("AMBIT".equals(((Classification)classificationList.get(i)).getTaxonPath().getSource().getString())){
//					idAmbit = new Integer(((Taxon)((Classification)classificationList.get(i)).getTaxonPath().getTaxonList().get(0)).getId());
//				}				
//			}
//			res.add(idAmbit);
			
			if (general.getCoverage(lang) == null) res.add("");
			else res.add(general.getCoverage(lang).getString());
		}
		else if(table.compareTo("mer_rec_lang")==0){
			//Lang
			res.add(lang);
			
			//Title
			if (general.getTitle(lang).getString() == null || general.getTitle(lang).getString() == "")
				throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
			res.add(general.getTitle(lang).getString());			

			//Descripcio
			if (general.getDescription(lang).getString() == null || general.getDescription(lang).getString() == "")
				throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
			res.add(general.getDescription(lang).getString());
			
			//Drets
			if (rights.getDescription(lang).getString() == null)
				 res.add("");
			else
				res.add(rights.getDescription(lang).getString());

			//Estat
			res.add(new Integer(0));
			
		}
		return res;
	}

	/**
	 * Pasa el valor LOMes als elements que ho necessitin.
	 * @param lomes
	 */
	public void setLomEs(boolean lomes) {
//		if (general != null) general.setLomEs(lomes);
//		if (lifeCycle != null) lifeCycle.setLomEs(lomes)
		if (metaMetaData != null) metaMetaData.setLomEs(lomes);
		if (educational != null) educational.setLomEs(lomes);
		if (rights != null) rights.setLomEs(lomes);
	}

}
