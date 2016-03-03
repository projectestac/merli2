package edu.xtec.merli;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.xtec.merli.mediateca.StringUtil;
import edu.xtec.merli.utils.Utility;

public class RecursMerli {

	private static final int MAX_DESCRIPTION = 2000;
	private static final int MAX_CAR_FISIQUES = 900;
	//1.1 Identifier
	private int idRecurs;
	//1.2 title
	private String title = "";
	private String titleEs = "";
	private String titleEn = "";
	private String titleOc = "";
	//1.3 language
	private ArrayList language = new ArrayList();
	//1.4 description
	private String description = "";
	private String descriptionEs = "";
	private String descriptionEn = "";
	private String descriptionOc = "";
	//1.5 Keywords
	private ArrayList paraules = new ArrayList();
	private ArrayList paraulesId = new ArrayList();
	//1.6 context
	private String context2 = "";
	//2.1 version
	private String version = "";
	//2.3 Contributions
	private ArrayList contribution = new ArrayList();
	//4.1 Format
	private ArrayList format = new ArrayList();
	//4.3 Location
	private String url = "";
	//5.2 LearningResourceType
	private ArrayList resourceType = new ArrayList();
	//5.5 IntendedEndUserRol
	//private String endUserRol = "3"; //Valor per defecte. 'Alumne'. 
	private ArrayList endUserRol = new ArrayList();
	//5.6 Context
	private ArrayList context = new ArrayList();
	//5.7 TypicalAgeRange
	private String minAge = "0";
	private String maxAge = "99";
	//5.8 Difficulty
	private String difficulty = "";
	//5.9 TypicalLearningTime
	private String learningTime = "";
	//6.1 Cost.
	private String cost = "";
	//6.2 Copyright and other restirctions
	private String hasRights = "no";
	//6.3 Rights description
	private String rightsDesc = "";
	private String rightsDescEs = "";
	private String rightsDescEn = "";
	private String rightsDescOc = "";
	//6.4 License.
	private String license = "";
	//7.1 Kind
	private ArrayList tipusRelacio = new ArrayList();
	//7.2.1 Identifier
	private ArrayList recursRelacionat = new ArrayList();
	//7.2.2 Description
	private ArrayList descripcioRelacio = new ArrayList();
	//8. Annotation
	private ArrayList annotation = new ArrayList();
	//9.2 Taxon Entry
	private ArrayList taxon = new ArrayList();
	//9.2 Taxon Entry
	private ArrayList taxonTerm = new ArrayList();
	//9.2 Taxon Entry-Curriculum
	private ArrayList curriculum = new ArrayList();
	
	private boolean esFisic=false;
	private String caractRFisic="";
	private ArrayList idFisic = new ArrayList();
	private ArrayList tipusIdFisic = new ArrayList();
	private String unitatCreadora="";
	//Format físic
	private ArrayList formatFisic = new ArrayList();
	private String nu="";
	
	//extres
	//private String ambit = "";
	private ArrayList ambit = new ArrayList();
	private String responsable = "";
	private String estat = "";
	//Unitats amb disponibilitat
	private ArrayList unitats = new ArrayList();

	private String agregaSend="";
	private Timestamp agregaDate;
	private String agregaId="";

	private boolean estatCa = false;
	private boolean estatEs = false;
	private boolean estatEn = false;
	private boolean estatOc = false;
	
	//Estats
	public final int BORRADOR = 0;
	public final int PERASSIGNAR = 1;
	public final int PERVALIDAR = 2;
	public final int PERCORRETGIR = 3;
	
	//Tipus d'identificador fisic
	public final int ISBN = 1;
	public final int ISSN = 2;
	public final int DIPOSITLEGAL = 3;
	public final static int NU = 5;
	public final int ALTRES = 0;
	
	//Tipus de relacions
	public final static int ES_PART_DE = 1;
	public final static int TE_PART = 2;
	public final static String S_ES_PART_DE = "és part de";
	public final static String S_TE_PART = "té part";

	public boolean isEstatCa() {
		return estatCa;
	}
	public void setEstatCa(boolean estatCa) {
		this.estatCa = estatCa;
	}
	public boolean isEstatEn() {
		return estatEn;
	}
	public void setEstatEn(boolean estatEn) {
		this.estatEn = estatEn;
	}
	public boolean isEstatEs() {
		return estatEs;
	}
	public void setEstatEs(boolean estatEs) {
		this.estatEs = estatEs;
	}
	public boolean isEstatOc() {
		return estatOc;
	}
	public void setEstatOc(boolean estatOc) {
		this.estatOc = estatOc;
	}
	public String getEstat() {
		return estat;
	}
	public void setEstat(String estat) {
		this.estat = estat;
	}
	
	public RecursMerli(int idRecurs) {
		super();
		// TODO Auto-generated constructor stub
		this.idRecurs = idRecurs;
	}

	public ArrayList getAnnotation() {
		return annotation;
	}
	public void setAnnotation(ArrayList annotation) {
		this.annotation = annotation;
	}

	public ArrayList getContext() {
		return context;
	}
	public void setContext(ArrayList context) {
		this.context = context;
	}

	public ArrayList getContribution() {
		return contribution;
	}
	public MerliContribution getContribution(int idRol) {
		for (int i=0; i < contribution.size(); i++){			
			if (((MerliContribution)contribution.get(i)).getIdRol() == idRol){
				return (MerliContribution)contribution.get(i);
			}
		}
		return null;
	}
	public void setContribution(ArrayList contribution) {
		this.contribution = contribution;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = StringUtil.shorten(description, MAX_DESCRIPTION, "[...]");
	}
	
	public String getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}

	public ArrayList getEndUserRol() {
		return endUserRol;
	}
	
	public void setEndUserRol(ArrayList endUserRol) {
		this.endUserRol = endUserRol;
	}
	

	public ArrayList getFormat() {
		return format;
	}
	
	public void setFormat(ArrayList format) {
		this.format = format;
	}
	

	public String getHasRights() {
		return hasRights;
	}
	
	public void setHasRights(String hasRights) {
		this.hasRights = hasRights;
	}
	

	public int getIdRecurs() {
		return idRecurs;
	}
	
	public void setIdRecurs(int idRecurs) {
		this.idRecurs = idRecurs;
	}
	

	public ArrayList getLanguage() {
		return language;
	}
	
	public void setLanguage(ArrayList language) {
		if (!language.isEmpty())
			this.language = language;
	}
	

	public String getLearningTime() {
		return learningTime;
	}
	
	public void setLearningTime(String learningTime) {
		this.learningTime = learningTime;
	}

	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}

	public String getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(String maxAge) {
		try{
			if (Integer.parseInt(maxAge) >= 0 && Integer.parseInt(maxAge) < 100)
				this.maxAge = maxAge;
			else
				this.maxAge = "99";
		}catch(Exception e){
			this.maxAge = "99";
		}
	}
	public String getMinAge() {
		return minAge;
	}
	public void setMinAge(String minAge) {
		try{
			if (Integer.parseInt(minAge) >= 0)
				this.minAge = minAge;
			else
				this.minAge = "0";
		}catch(Exception e){
			this.minAge = "0";
		}
		
	}
	
	public ArrayList getResourceType() {
		return resourceType;
	}
	public void setResourceType(ArrayList resourceType) {
		this.resourceType = resourceType;
	}
	
	public String getRightsDesc() {
		return rightsDesc;
	}
	
	public void setRightsDesc(String rightsDesc) {
		this.rightsDesc = rightsDesc;
	}

	public ArrayList getTaxon() {
		return taxon;
	}
	public void setTaxon(ArrayList taxon) {
		this.taxon = taxon;
	}
	public ArrayList getTaxonTerm() {
		return taxonTerm;
	}
	public void setTaxonTerm(ArrayList taxonTerm) {
		this.taxonTerm = taxonTerm;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public ArrayList getAmbit() {
		return ambit;
	}
	public void setAmbit(ArrayList ambit) {
		this.ambit = ambit;
	}
	
	public String getResponsable() {
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public ArrayList getCurriculum() {
		return curriculum;
	}
	public void setCurriculum(ArrayList curriculum) {
		this.curriculum = curriculum;
	}
	
	public void addContribution(MerliContribution mc) {
		// TODO Auto-generated method stub
		if (this.contribution == null){
			this.contribution = new ArrayList();
		}
		this.contribution.add(mc);
	}
	
	public ArrayList getAddQuery() {
		ArrayList res = new ArrayList();
		
			res.add(new Integer(this.getIdRecurs()));
			res.add(this.getTitle());
			res.add(Utility.aplanarText(this.getTitle()));
			res.add(this.getDescriptionQuery());
			res.add(Utility.aplanarText(this.getDescriptionQuery()));
			res.add(this.getUrl());
			res.add(this.getVersion());
			res.add(this.getMinAge());
			res.add(this.getMaxAge());
			res.add(this.getDifficulty());
			res.add(this.getLearningTime());
			
			if (this.getHasRights() != null && this.getHasRights().compareTo("yes")==0)
				res.add(new Integer(1));
			else
				res.add(new Integer(0));
			//EndUserRol, ja no es desa a la taula mer_recurs.
			res.add("");
			res.add( new Integer(0));
			res.add( new Integer(0));
			res.add(this.getContext2());
			//res.add(this.getAmbit());
		
		return res;
	}

	private String getDescriptionQuery() {
		if (description !=null && !"".equals(description))
			return description;
		else
			return " ";
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}
	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}
	public String getDescriptionEs() {
		return descriptionEs;
	}
	public void setDescriptionEs(String descriptionEs) {
		this.descriptionEs = descriptionEs;
	}
	public String getDescriptionOc() {
		return descriptionOc;
	}
	public void setDescriptionOc(String descriptionOc) {
		this.descriptionOc = descriptionOc;
	}
	
	public String getRightsDescEn() {
		return rightsDescEn;
	}
	public void setRightsDescEn(String rightsDescEn) {
		this.rightsDescEn = rightsDescEn;
	}
	public String getRightsDescEs() {
		return rightsDescEs;
	}
	public void setRightsDescEs(String rightsDescEs) {
		this.rightsDescEs = rightsDescEs;
	}
	public String getRightsDescOc() {
		return rightsDescOc;
	}
	public void setRightsDescOc(String rightsDescOc) {
		this.rightsDescOc = rightsDescOc;
	}

	public String getTitleEn() {
		return titleEn;
	}
	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}
	public String getTitleEs() {
		return titleEs;
	}
	public void setTitleEs(String titleEs) {
		this.titleEs = titleEs;
	}
	public String getTitleOc() {
		return titleOc;
	}	
	public void setTitleOc(String titleOc) {
		this.titleOc = titleOc;
	}
	
	public ArrayList getFormatFisic() {
		return formatFisic;
	}
	public void setFormatFisic(ArrayList formatFisic) {
		this.formatFisic = formatFisic;
	}

	/**
	 * retorna els noms dels camps de la taula tab
	 * @param tab
	 * @return ArrayList que conte els noms dels camps de la taula tab
	 */
	public ArrayList getFieldsList(String tab) {
		// TODO Auto-generated method stub
		String table = tab.toLowerCase();
		ArrayList lcamps = new ArrayList();
		
		if (table.compareTo("mer_recurs")==0){
			lcamps.add("id_rec");
			lcamps.add("titol");
			lcamps.add("descripcio");
			lcamps.add("url");
			lcamps.add("versio");
			lcamps.add("edat_min");
			lcamps.add("edat_max");
			lcamps.add("id_dificultat");
			lcamps.add("duracio");
			lcamps.add("drets");
			lcamps.add("id_rol_usuari");
			lcamps.add("confirmat");
			lcamps.add("id_ambit");
			lcamps.add("context");
		}
		if ("mer_rec_rol_usuari".compareTo(table)==0)
			lcamps.add("id_rol_usuari");
		
		if (table.compareTo("mer_rec_nivell_educatiu")==0)
			lcamps.add("id_nivell");

		if (table.compareTo("mer_rec_format")==0)
			lcamps.add("id_format");
		
		if (table.compareTo("mer_rec_format_fisic")==0)
			lcamps.add("id_format_fisic");

		if (table.compareTo("mer_rec_ambits")==0)
			lcamps.add("id_ambit");

		if (table.compareTo("mer_contribucio")==0){
			lcamps.add("id_rol_cont");
			lcamps.add("v_entitat");
			lcamps.add("v_descripcio");
			lcamps.add("d_data");
		}
		if (table.compareTo("mer_rec_termes")==0){
			lcamps.add("id_terme");
		}
		if (table.compareTo("mer_rec_curriculum")==0){
			lcamps.add("id_node");
			lcamps.add("v_type");
		}
		if (table.compareTo("mer_rec_llengua")==0){
			lcamps.add("id_llengua");
		}
		if (table.compareTo("mer_rec_tipus_recurs")==0){
			lcamps.add("id_tipus_recurs");
		}
		if (table.compareTo("mer_rec_info")==0){
			lcamps.add("id_estat");
			lcamps.add("v_responsable");
		}
		if (table.compareTo("mer_rec_lang")==0){
			lcamps.add("titol");
			lcamps.add("descripcio");
			lcamps.add("drets");
			lcamps.add("estat");
		}
		if (table.compareTo("mer_drets")==0){
			lcamps.add("id_llicencia");
			lcamps.add("descripcio");
			lcamps.add("cost");
		}
		if (table.compareTo("mer_rec_agrega")==0){
			lcamps.add("id_rec");
			lcamps.add("id_agrega");
			lcamps.add("send");
			lcamps.add("date_agrega");
		}
		if (table.compareTo("the_termes")==0){
			lcamps.add("terme_ca");
		}
		if (table.compareTo("mer_relacio_recursos")==0){
			lcamps.add("recurs1");
			lcamps.add("recurs2");
			lcamps.add("tipus");
			lcamps.add("descripcio");
		}
		if (table.compareTo("mer_recurs_fisic")==0){
			lcamps.add("id_recurs");
			lcamps.add("v_caracteristiques");
			lcamps.add("id_unitat_creadora");
		}
		if (table.compareTo("mer_idfisic")==0){
			lcamps.add("id_rec");
			lcamps.add("v_tipus");
			lcamps.add("v_valor");
		}
		if (table.compareTo("mer_unitats")==0){
			lcamps.add("id_unitat");
			lcamps.add("v_nom");
			lcamps.add("v_url");
		}
		if (table.compareTo("mer_rec_disp_uni")==0){
			lcamps.add("id_rec");
			lcamps.add("id_unitat");
		}
		

		if (table.compareTo("mer_paraules")==0){
			lcamps.add("v_paraula");
		}
		if (table.compareTo("mer_rec_paraules")==0){
			lcamps.add("id_paraula");
		}
		if ("mer_rec_mediateca".compareTo(table)==0)
			lcamps.add("nu");
		
		return lcamps;
	}

	public String getSetQuery(String table) {
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
				query += " drets  = ?,";/*";
				if (this.getHasRights() != null && this.getHasRights().compareTo("yes")==0)
					query += "1,";
				else
					query += "0,";
					*/
				//query += " id_rol_usuari  = ?";//"+Utility.toParaulaDB(this.getEndUserRol())+",";
				query += " confirmat  = ?,";//0,";//+this.getEstat()+",";
				query += " id_ambit  = ?,";//"+Utility.toParaulaDB(this.getAmbit())+"";
				query += " context  = ?";//"+Utility.toParaulaDB(this.getAmbit())+"";
		}
		return query;
	}		
	public ArrayList getSetQueryFields(String table) {
		// TODO Auto-generated method stub
		ArrayList res = new ArrayList();
		
		if (table.compareTo("mer_recurs")==0){
			/*res.add(Utility.toParaulaDB(this.getTitle()));
			res.add(Utility.aplanarText(this.getTitle()));
			res.add(Utility.toParaulaDB(this.getDescription()));
			res.add(Utility.aplanarText(this.getDescription()));
			res.add(Utility.toParaulaDB(this.getUrl()));
			res.add(Utility.toParaulaDB(this.getVersion()));
			res.add(Utility.toParaulaDB(this.getMinAge()));
			res.add(Utility.toParaulaDB(this.getMaxAge()));
			res.add(Utility.toParaulaDB(this.getDifficulty()));
			res.add(Utility.toParaulaDB(this.getLearningTime()));*/
			res.add((this.getTitle()));
			res.add(Utility.aplanarText(this.getTitle()));
			res.add((this.getDescription()));
			res.add(Utility.aplanarText(this.getDescription()));
			res.add((this.getUrl()));
			res.add((this.getVersion()));
			res.add((this.getMinAge()));
			res.add((this.getMaxAge()));
			res.add((this.getDifficulty()));
			res.add((this.getLearningTime()));
			
			if (this.getHasRights() != null && this.getHasRights().compareTo("yes")==0)
				res.add(new Integer(1));
			else
				res.add(new Integer(0));
			//Canvia la forma de desar els EndUserRol.
			//res.add("");//Utility.toParaulaDB(this.getEndUserRol()));
			res.add(new Integer(0));//,";//+this.getEstat()+",";
			res.add(new Integer(0));//ambits
			res.add(this.getContext2());//context
		}
		return res;
	}
	
	public void setTitle(String lang, String txt) {
		if ("ca".equals(lang))
			this.setTitle(txt);
		if ("es".equals(lang))
			this.setTitleEs(txt);
		if ("en".equals(lang))
			this.setTitleEn(txt);
		if ("oc".equals(lang))
			this.setTitleOc(txt);
	}
	
	public void setDescription(String lang, String txt) {

		if ("ca".equals(lang))
			this.setDescription(txt);
		if ("es".equals(lang))
			this.setDescriptionEs(txt);
		if ("en".equals(lang))
			this.setDescriptionEn(txt);
		if ("oc".equals(lang))
			this.setDescriptionOc(txt);
	}

	public void setRightsDesc(String lang, String txt) {
		if ("ca".equals(lang))
			this.setRightsDesc(txt);
		if ("es".equals(lang))
			this.setRightsDescEs(txt);
		if ("en".equals(lang))
			this.setRightsDescEn(txt);
		if ("oc".equals(lang))
			this.setRightsDescOc(txt);
	}
	
	public void setEstatLang(String lang, String bol) {
		boolean est;
		try{			
			est = "1".equals(bol);
		}catch(Exception e){
			est = false;
		}
		setEstatLang(lang, est);
	}
	public void setEstatLang(String lang, boolean estat) {
			
		if ("ca".equals(lang))
			this.setEstatCa(estat);
		if ("es".equals(lang))
			this.setEstatEs(estat);
		if ("en".equals(lang))
			this.setEstatEn(estat);
		if ("oc".equals(lang))
			this.setEstatOc(estat);
	}

	public String getAgregaSend() {
		return agregaSend;
	}
	public boolean isAgregaSend() {
		return "1".equals(agregaSend);
	}
	public void setAgregaSend(String sendAgrega) {
		this.agregaSend = sendAgrega;
	}
	
	public Timestamp getAgregaDate() {
		return agregaDate;
	}
	public String getAgregaDate(String format) {
		if (agregaDate == null) return null;
		if (format == null || "".equals(format))
			format = "dd-MM-yyyy";
		SimpleDateFormat sDate = new SimpleDateFormat(format);
		
		return sDate.format(new Date(agregaDate.getTime()));			
	}
	public void setAgregaDate(Timestamp agregaDate) {
		this.agregaDate = agregaDate;
	}

	public String getAgregaId() {
		return agregaId;
	}
	public void setAgregaId(String agregaId) {
		this.agregaId = agregaId;
	}
	
	public String getContext2() {
		return context2;
	}
	public void setContext2(String context2) {
		this.context2 = context2;
	}

	public ArrayList getDescripcioRelacio() {
		return descripcioRelacio;
	}
	/**
	 * Retorna la descripcio de totes aquelles relacions que son del tipus especificat
	 * @param tipus
	 * @return
	 */
	public ArrayList getDescripcioRelacio(int tipus) {
		ArrayList res = new ArrayList();
		for(int i=0; i<tipusRelacio.size();i++)
			if (((Integer) (tipusRelacio.get(i))).intValue() == tipus)
				res.add(descripcioRelacio.get(i));
		return res;
	}
	public void setDescripcioRelacio(ArrayList descripcioRelacio) {
		this.descripcioRelacio = descripcioRelacio;
	}
	public void setDescripcioRelacio(String[] descRel) {
		descripcioRelacio=new ArrayList();
		if(descRel!=null)
			for(int i=0;i<descRel.length;i++)
				descripcioRelacio.add(descRel[i]);
	}
	
	public ArrayList getRecursRelacionat() {
		return recursRelacionat;
	}
	/**
	 * Retorna els ids dels recursos relacionats de totes aquelles relacions que son del tipus especificat
	 * @param tipus
	 * @return
	 */
	public ArrayList getRecursRelacionat(int tipus) {
		ArrayList res = new ArrayList();
		for(int i=0; i<tipusRelacio.size();i++)
			if (((Integer) (tipusRelacio.get(i))).intValue() == tipus)
				res.add(recursRelacionat.get(i));
		return res;
	}
	/**
	 * Retorna els recursos relacionats en el format nom_recurs(id_recurs) de totes aquelles relacions que son del tipus especificat
	 * @param tipus 
	 * @return
	 */
	public ArrayList getRecursRelacionat(String tipus) {
		ArrayList res = new ArrayList();
		for(int i=0; i<tipusRelacio.size();i++)
			if (((String) (tipusRelacio.get(i))).equals(tipus))
				res.add(descripcioRelacio.get(i)+"("+recursRelacionat.get(i)+")");
		return res;
	}
	public void setRecursRelacionat(ArrayList recursRelacionat) {
		this.recursRelacionat = recursRelacionat;
	}
	
	public ArrayList getTipusRelacio() {
		return tipusRelacio;
	}
	public void setTipusRelacio(ArrayList tipusRelacio) {
		this.tipusRelacio = tipusRelacio;
	}
	
	public boolean getEsFisic() {
		return esFisic;
	}
	public void setEsFisic(boolean esFisic) {
		this.esFisic = esFisic;
	}

	public String getCaractRFisic() {
		return caractRFisic;
	}
	public void setCaractRFisic(String caractRFisic) {
		this.caractRFisic = StringUtil.shorten(caractRFisic, MAX_CAR_FISIQUES, "[...]");
	}

	public ArrayList getIdFisic() {
		return idFisic;
	}
	public ArrayList getIdFisic(int tipus) {
		ArrayList res = new ArrayList();
		for(int i=0;i<idFisic.size();i++)
			if (((Integer) tipusIdFisic.get(i)).intValue() == tipus)
				res.add(idFisic.get(i));
		return idFisic;
	}
	public void setIdFisic(ArrayList idFisic) {
		this.idFisic = idFisic;
	}
	public void setIdFisic(String[] idFisic2) {
		idFisic=new ArrayList();
		for(int i=0;i<idFisic2.length;i++)
			idFisic.add(idFisic2[i]);
	}
	
	public ArrayList getTipusIdFisic() {
		return tipusIdFisic;
	}
	public void setTipusIdFisic(ArrayList tipusIdFisic) {
		this.tipusIdFisic = tipusIdFisic;
	}
	public void setTipusIdFisic(String[] tipusIdFisicSel) {
		tipusIdFisic=new ArrayList();
		for(int i=0;i<tipusIdFisicSel.length;i++)
			tipusIdFisic.add(tipusIdFisicSel[i]);
	}
	
	public ArrayList getParaules() {
		return paraules;
	}
	public void setParaules(ArrayList paraules) {
		this.paraules = paraules;
	}
	
	public ArrayList getParaulesId() {
		return paraulesId;
	}
	public void setParaulesId(ArrayList paraulesId) {
		this.paraulesId = paraulesId;
	}




	public String getUnitatCreadora() {
		return unitatCreadora;
	}
	public void setUnitatCreadora(String unitatCreadora) {
		this.unitatCreadora = unitatCreadora;
	}
	public void setRecursRelacionat(Integer[] recRelI) {
		recursRelacionat.clear();
		if(recRelI!= null)
			for(int i=0;i<recRelI.length;i++)
				recursRelacionat.add(recRelI[i]);
	}
	public void setTipusRelacio(String[] tipusRelSel) {
		tipusRelacio=new ArrayList();
		if(tipusRelSel!=null)
			for(int i=0;i<tipusRelSel.length;i++)
				tipusRelacio.add(tipusRelSel[i]);	
	}




	public ArrayList getUnitats() {
		return unitats;
	}
	/**
	 * retorna els ids de les unitats en un ArrayList d'strings
	 * @return
	 */
	public ArrayList getIdsUnitats() {
		ArrayList ids=new ArrayList();
		for(int i=0;i<unitats.size();i++)
			ids.add(Integer.toString(((Unitat)unitats.get(i)).getIdentifier().intValue()));
		return ids;
	}




	public void setUnitats(ArrayList unitats) {
		this.unitats = unitats;
	}
	
	public void addIdentificador(String id, String tipus) {
		if (this.getIdFisic() == null){
			this.setIdFisic(new ArrayList());
		}
		this.getIdFisic().add(id);		
		if (this.getTipusIdFisic() == null){
			this.setTipusIdFisic(new ArrayList());
		}
		this.getTipusIdFisic().add(tipus);		
	}
	


	public void setNu(String string) {
		this.nu = string;		
	}
	public String getNu() {
		return nu;
	}


	


	/*
	 * public String getSetQuery(String table, RecursMerli mOld) {
		String query = "";
		
		if (table.compareTo("mer_recurs")==0){
			if (this.getTitle().compareTo(mOld.getTitle())!=0){
				query += " titol = '"+Utility.toParaulaDB(this.getTitle())+"',";
				query += " titol_pla = '"+Utility.aplanarText(this.getTitle())+"',";
			}
			if(this.getDescription().compareTo(mOld.getDescription())!=0){
				query += " descripcio  = '"+Utility.toParaulaDB(this.getDescription())+"',";
				query += " desc_pla  = '"+Utility.aplanarText(this.getDescription())+"',";
			}
			if (this.getUrl().compareTo(mOld.getUrl())!=0)
				query += " descripcio  = '"+Utility.toParaulaDB(this.getUrl())+"',";
			if (this.getVersion().compareTo(mOld.getVersion())!=0)
				query += " versio  = '"+Utility.toParaulaDB(this.getVersion())+"',";
			if (this.getMinAge().compareTo(mOld.getMinAge())!=0)
				query += " edat_min  = "+Utility.toParaulaDB(this.getMinAge())+",";
			if (this.getMaxAge().compareTo(mOld.getMaxAge())!=0)
				query += " edat_max  = "+Utility.toParaulaDB(this.getMaxAge())+",";
			if (this.getDifficulty().compareTo(mOld.getDifficulty())!=0)
				query += " id_dificultat  = "+Utility.toParaulaDB(this.getDifficulty())+",";
			if (this.getLearningTime().compareTo(mOld.getLearningTime())!=0)
				query += " duracio  = "+Utility.toParaulaDB(this.getLearningTime())+",";
			if (this.getHasRights().compareTo(mOld.getHasRights())!=0){		
				query += " duracio  = ";
				if (this.getHasRights() != null && this.getHasRights().compareTo("yes")==0)
					query += "1,";
				else
					query += "0,";
				query += ",";
			}	
			if (this.getEndUserRol().compareTo(mOld.getEndUserRol())!=0)
				query += " id_rol_usuari  = "+Utility.toParaulaDB(this.getEndUserRol())+",";
			query += this.getEndUserRol()+",";
			query += " confirmat  = "+this.getEstat()+",";
			if (this.getAmbit().compareTo(mOld.getAmbit())!=0)
				query += " id_ambit  = "+Utility.toParaulaDB(this.getAmbit())+",";
		}
		return query;
	}*/
	
}
