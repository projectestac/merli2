package edu.xtec.merli.gestorrec;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.MerliContribution;
import edu.xtec.merli.RecursMerli;
import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.basedades.RecursBD;
import edu.xtec.merli.utils.Utility;

public class VeuRecursForm extends ActionForm {

	private static final String EN = "en";
	private static final String CA = "ca";
	private static final String ES = "es";
	private static final String OC = "oc";
	private String idRecurs;
	private String operation;
	private String llistat;
	private String usuari;
	private String subject;
	private String missatge;
	private Collection lAssign;
	
	private RecursMerli rm;

	//1.2 title
	private String title = "";
	//1.3 language
	private String language = "";
	//1.4 description
	private String description = "";
	//2.1 version
	private String version = "";
	//2.3 Contributions
	private ArrayList contribution = new ArrayList();
	//4.1 Format
	private String format = "";
	//4.3 Location
	private String url = "";
	//5.2 LearningResourceType
	private String resourceType = "";
	//5.5 IntendedEndUserRol
	private String endUserRol = "3"; //Valor per defecte. 'Alumne'. 
	//5.6 Context
	private String context = "";
	//5.7 TypicalAgeRange
	private String minAge = "0";
	private String maxAge = "99";
	//5.8 Difficulty
	private String difficulty = "";
	//5.9 TypicalLearningTime
	private String duraHora="";
	private String duraMin="";
	private String duraSeg="";
	//6.1 Cost.
	private String cost = "";
	//6.2 Copyright and other restirctions
	private String hasRights = "no";
	//6.3 Rights description
	private String rightsDesc = "";
	//6.4 License.
	private String license = "";
	//8. Annotation
	private ArrayList annotation = new ArrayList();
	//9.2 Taxon Entry
	private ArrayList taxon = new ArrayList();
	//9.2 Taxon Entry
	private String taxonTerm = "";
	//9.2 Taxon Entry-Curriculum
	private String curriculum = "";
	
	//extres
	private String ambit = "";
	private String responsable = "";
	private String autor = "";
	private String dateAutor = "";
	
	private MessageResources messages;
	private Locale locale;
	private String validador;
	private String dataValidador;
	private String etiquetador;
	private String dataEtiquetador;
	private String corrector;
	private String editor="";
	private String etiquetadorMail;
	

	private String titleEs = "";
	private String titleEn = "";
	private String titleOc = "";
	private String descriptionEs = "";
	private String descriptionEn = "";
	private String descriptionOc = "";
	private String rightsEs = "";
	private String rightsEn = "";
	private String rightsOc = "";

	private String vtradCa = "";
	private String vtradEs = "";
	private String vtradEn = "";
	private String vtradOc = "";

	private String sendAgrega;
	private String[] testAgrega;
	private ArrayList tipusRelSel;
	private ArrayList tipusIdFisicSel;
	private ArrayList tipusIdFisic;
	private ArrayList idFisic;
	private String caractRFisic = "";
	private boolean recFisic = false;
	private ArrayList recRel;
	private ArrayList descRel;
	private String context2 = "";
	private ArrayList tipusRel;

	private ArrayList unitats = new ArrayList();
	
	/**
	 * Dades inicials del formulari.
	 */	
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		idRecurs = request.getParameter("idRecurs");
		operation = request.getParameter("operation");
		llistat = request.getParameter("llistat");

		messages = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);
		locale = request.getLocale();
		MerliBean mb = new MerliBean();
		try {
			rm = mb.getRecurs(Integer.parseInt(idRecurs));
			
			this.setDescription(rm.getDescription());
			this.setTitle(rm.getTitle());
			this.setRightsDesc(rm.getRightsDesc());
			
			this.setDescriptionEs(rm.getDescriptionEs());
			this.setTitleEs(rm.getTitleEs());
			this.setRightsEs(rm.getRightsDescEs());

			this.setDescriptionEn(rm.getDescriptionEn());
			this.setTitleEn(rm.getTitleEn());
			this.setRightsEn(rm.getRightsDescEn());
			
			this.setDescriptionOc(rm.getDescriptionOc());
			this.setTitleOc(rm.getTitleOc());
			this.setRightsOc(rm.getRightsDescOc());

			//this.setValidaTradCa(rm.isEstatCa());
			//this.setValidaTradEs(rm.isEstatEs());
			//this.setValidaTradEn(rm.isEstatEn());

			//this.sendAgrega=rm.isAgregaSend();

			this.setContext2(rm.getContext2());
			
//			for(int i=0;i<rm.getTipusRelacio().size();i++)
//			{
				this.setTipusRel(rm.getTipusRelacio());
				this.setRecRel(rm.getRecursRelacionat());
				this.setDescRel(rm.getDescripcioRelacio());
//			}
			
			if(rm.getTipusIdFisic()!=null){
//				I pot passar que el recurs sigui fisic pero no hi hagi cap id fisic, 
//				per tant s'ha de modificar la condicio
				this.setCaractRFisic(rm.getCaractRFisic());
				this.setIdFisic(rm.getIdFisic());
				this.setTipusIdFisicSel(rm.getTipusIdFisic());
				this.setRecFisic(true);
			}
			else this.setRecFisic(false);
			
			this.setUnitats(rm.getUnitats());
			
			this.setLAssign(mb.getUserList(MerliContribution.VALIDADOR));
		} catch (MerliDBException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Validació del contingut del formulari previ al tractament d'aquest.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		if (this.operation != null && this.operation.equals("assign")){
			if (usuari == null || usuari.equals("")){
				errors.add("usuari", new ActionError("error.inforec.usuari"));
			}
		}
		if ((this.operation != null && this.operation.equals("denegar"))||
				(this.operation != null && this.operation.equals("retornar"))){
//			if (subject == null || subject.equals("") || subject.length() < 2){
//				errors.add("subject", new ActionError("error.inforec.subject"));
//			}
//			if (missatge == null || missatge.equals("") || missatge.length() < 2){
//				errors.add("missatge", new ActionError("error.inforec.missatge"));
//			}
		}
		
		if (operation == null || 
				(!operation.equals("assign") && 
						!operation.equals("denegar") && 
						!operation.equals("retornar") && 
						!operation.equals("corregir") && 
						!operation.equals("traduir") && 
						!operation.equals("salvar") && 
						!operation.equals("editar") && 
						!operation.equals("agrega") && 
						!operation.equals("validar"))){
			errors.add("carrega", new ActionError("error.inforec.carrega"));
		}
		if (title != null)
			title = title.substring(0,Math.min(1000,title.length()));
		if (description != null)
			description = description.substring(0,Math.min(2000,description.length()));
		if (rightsDesc != null)
			rightsDesc = rightsDesc.substring(0,Math.min(1000,rightsDesc.length()));
		
		return errors;		
	}

	public String getIdRecurs() {
		return idRecurs;
	}
	public void setIdRecurs(String idRecurs) {
		this.idRecurs = idRecurs;
	}
	
	public String getLlistat() {
		return llistat;
	}
	public void setLlistat(String llistat) {
		this.llistat = llistat;
	}
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public RecursMerli getRm() {
		return rm;
	}
	public void setRm(RecursMerli rm) {
		this.rm = rm;
	}

	public String getAmbit() {

		RecursBD rbd = new RecursBD();
		try {
			//ambit = rbd.getAmbitNom(Integer.parseInt(rm.getAmbit()));
			ambit = "";
			for (int i=0; i<rm.getAmbit().size();i++){
				if (i>0)
					ambit +=", ";
				ambit += rbd.getAmbitNom((Integer.parseInt((String)rm.getAmbit().get(i))));				
			}
			//if (al != null && !al.isEmpty())
			//	ambit = (String) al.get(Integer.parseInt(rm.getAmbit())-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ambit = "Error";
		}
		if (ambit == null)
			ambit = "No pertany a cap ambit";
		
		return ambit;
	}
	public void setAmbit(String ambit) {
		this.ambit = ambit;
	}
	
	public ArrayList getAnnotation() {
		return annotation;
	}
	public void setAnnotation(ArrayList annotation) {
		this.annotation = annotation;
	}
	
	public String getAutor() {
		if(rm.getContribution(MerliContribution.AUTOR)!=null)
			autor = rm.getContribution(MerliContribution.AUTOR).getEntity();
		return autor;
	}		
	public String getEtiquetadorMail() {
		MerliBean mb = new MerliBean();
		etiquetadorMail = mb.getUserMail(this.getEtiquetador());
		return etiquetadorMail;
	}	
	public String getValidador() {
		if (rm.getContribution(MerliContribution.VALIDADOR) != null)
			validador = rm.getContribution(MerliContribution.VALIDADOR).getEntity();
		else validador = "";
		return validador;
	}
	public String getDataValidador() {
		SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");				  		
		if (rm.getContribution(MerliContribution.VALIDADOR) != null)
			dataValidador = sDate.format(rm.getContribution(MerliContribution.VALIDADOR).getDate());
		else dataValidador = "";
		return dataValidador;
	}
	public String getCorrector() {
		if (rm.getContribution(MerliContribution.CORRECTOR) != null)
			corrector = rm.getContribution(MerliContribution.CORRECTOR).getEntity();
		else corrector = "";
		return corrector;
	}
	public String getEtiquetador() {
		if (rm.getContribution(MerliContribution.ETIQUETADOR) != null)
			etiquetador = rm.getContribution(MerliContribution.ETIQUETADOR).getEntity();
		else etiquetador = "";
		return etiquetador;
	}
	public String getEtiquetadorConvertit() {
		String sUnitat;
		if (rm.getContribution(MerliContribution.ETIQUETADOR) != null)
		{
			etiquetador = rm.getContribution(MerliContribution.ETIQUETADOR).getEntity();
			try
			{
				String uni="";
				MerliBean mb = new MerliBean();
				Integer.parseInt(etiquetador.substring(1));
				if(etiquetador.charAt(0)=='a') uni=etiquetador.substring(1);
				else if(etiquetador.charAt(0)=='b') uni="1"+etiquetador.substring(1);
				else if(etiquetador.charAt(0)=='c') uni="2"+etiquetador.substring(1);
				else if(etiquetador.charAt(0)=='e') uni="4"+etiquetador.substring(1);
				
				sUnitat = mb.getUnitatById(uni);
			}
			catch(NumberFormatException e)
			{
				sUnitat=etiquetador;
			}
		}
		else 
		{
			etiquetador = "";
			sUnitat="";
		}
		return sUnitat;
	}
	
	
	public String getDataEtiquetador() {
		SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");			
		if (rm.getContribution(MerliContribution.ETIQUETADOR) != null)
			dataEtiquetador = sDate.format(rm.getContribution(MerliContribution.ETIQUETADOR).getDate());
		else dataEtiquetador = "";
		return dataEtiquetador;
	}
	public String getEditor() {
		if(rm.getContribution(MerliContribution.EDITOR)!=null)
			editor = rm.getContribution(MerliContribution.EDITOR).getEntity();
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	
	public String getContext() {

		RecursBD rbd = new RecursBD();
		ArrayList al;
		try {
			al = rbd.getNivell(RecursBD.ESCOLAR,RecursBD.LABELS);
			al.addAll(rbd.getNivell(RecursBD.ESPECIAL,RecursBD.LABELS));
			//al.add("");	//afegim un espai a la posicio 12, no hi ha cap nivell amb aquest id, per tant mai s accedira a ell
			al.addAll(rbd.getNivell(RecursBD.ADMINISTRATIU,RecursBD.LABELS));
			al.add(11, "");	//afegim un espai a la posicio 12, no hi ha cap nivell amb aquest id, per tant mai s accedira a ell
			ArrayList lrt = rm.getContext();
			context = "";
			for (int i = 0; i < lrt.size(); i++){
				context += al.get(Integer.parseInt((String)lrt.get(i))-1);
				if (i +1 < lrt.size())
					context += ", ";
			}
		} catch (Exception e) {
			e.printStackTrace();
			context = "Error";
		}
		return context;
	}	
	public void setContext(String context) {
		this.context = context;
	}
	
	public ArrayList getContribution() {
		return contribution;
	}
	public void setContribution(ArrayList contribution) {
		this.contribution = contribution;
	}
	
	public String getCost() {
		if ("0".equals(rm.getCost()))
			cost = messages.getMessage(locale,"application.no");
		else if("1".equals(rm.getCost()))
			cost = messages.getMessage(locale,"application.si");
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	
	public String getCurriculum() {
		MerliBean mb = new MerliBean();
		String title;
		StringBuffer curri = new StringBuffer();
		ArrayList al = mb.getCurriculumElements(rm.getCurriculum());
		int size=al.size();
		for (int i=0;i<size;i++){
			title = (String) ((Hashtable)al.get(i)).get("level");
			title +=" - ";
			title += ((Hashtable)al.get(i)).get("area");
			if(((Hashtable)al.get(i)).get("content")!=null && ((String)((Hashtable)al.get(i)).get("content")).length()>0)
			{
				title +=" - ";
				title += ((Hashtable)al.get(i)).get("content");
				curri.append(mb.printTitleElement((String) ((Hashtable)al.get(i)).get("content"),title));
			}
			else curri.append(mb.printTitleElement((String) ((Hashtable)al.get(i)).get("area"),title));

			
			if (i+1<size)
				curri.append(", ");
		}
		curriculum = curri.toString();
		return curriculum;
	}
	public void setCurriculum(String curriculum) {
		this.curriculum = curriculum;
	}
	
	public String getDateAutor() {
		SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");	
		if(rm.getContribution(MerliContribution.AUTOR)!=null)
			dateAutor = sDate.format(rm.getContribution(MerliContribution.AUTOR).getDate());

		return dateAutor;
	}
	public void setDateAutor(String dateAutor) {
		this.dateAutor = dateAutor;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDifficulty() {
		RecursBD rbd = new RecursBD();
		ArrayList al;
		try {
			al = rbd.getDificultat(RecursBD.LABELS);
			if (rm.getDifficulty()!=null && !"".equals(rm.getDifficulty())){
				difficulty+= al.get(Integer.parseInt(rm.getDifficulty())-1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			difficulty = "Error";
		}
		return difficulty;
	}
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
	}
	
	public String getEndUserRol() {
		RecursBD rbd = new RecursBD();
		ArrayList al;
		try {
			al = rbd.getRolUser(RecursBD.LABELS);
			ArrayList lrt = rm.getEndUserRol();
			endUserRol = "";
			for (int i = 0; i < lrt.size(); i++){
				endUserRol += al.get(Integer.parseInt((String)lrt.get(i))-1);		
				if (i +1 < lrt.size())
					endUserRol += ", ";
			}
			//endUserRol = (String) al.get(Integer.parseInt(rm.getEndUserRol())-1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			endUserRol = "Error";
		}
		return endUserRol;
	}
	public void setEndUserRol(String endUserRol) {
		this.endUserRol = endUserRol;
	}
	
	public String getFormat() {
		RecursBD rbd = new RecursBD();
		try {
			//al = rbd.getFormatLimitat(RecursBD.LABELS);
			if(rm.getEsFisic())
			{
				ArrayList lrt = rm.getFormatFisic();
				format = "";
				for (int i = 0; i < lrt.size(); i++){
					//format += al.get(Integer.parseInt((String)lrt.get(i))-1);
					format += rbd.getFormatFisicLimitat(RecursBD.LABELS, Integer.parseInt((String)lrt.get(i))).get(0);
					
					if (i +1 < lrt.size())
						format += ", ";
				}
			}
			else
			{
				ArrayList lrt = rm.getFormat();
				format = "";
				for (int i = 0; i < lrt.size(); i++){
					//format += al.get(Integer.parseInt((String)lrt.get(i))-1);
					format += rbd.getFormatLimitat(RecursBD.LABELS, Integer.parseInt((String)lrt.get(i))).get(0);
					
					if (i +1 < lrt.size())
						format += ", ";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			format = "Error";
		}
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	
	public String getHasRights() {
		hasRights = rm.getHasRights();
		return hasRights;
	}
	public void setHasRights(String hasRights) {
		this.hasRights = hasRights;
	}
	
	public String getLanguage() {
		language = Utility.toParaula(rm.getLanguage().toString());
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getLanguageNoms()
	{		
		RecursBD rbd=new RecursBD();
		ArrayList ids_aux=(ArrayList) rbd.getLlenguesTotes().get("id_llengua");
		ArrayList lab_aux=(ArrayList) rbd.getLlenguesTotes().get("llengua_cat");
		Map llengues = new HashMap();
		for(int i=0;i<ids_aux.size();i++)
			llengues.put(ids_aux.get(i), lab_aux.get(i));
		
		language="";
		for(int i=0;i<rm.getLanguage().size();i++)
		{
			ArrayList ids_lleng=(ArrayList)rm.getLanguage();
			String id_lleng=(String) ids_lleng.get(i);
			String l = (String) llengues.get(id_lleng);
			language+=l+", ";
		}
		if(language.length()>0) language=language.substring(0,language.length()-2);	//trec la ultima coma
		return language;
	}
	
	public String getDuraHora() {
		if (rm.getLearningTime() != null && rm.getLearningTime().length() > 4 && rm.getLearningTime().indexOf('H')>0)
			duraHora = rm.getLearningTime().substring(2,rm.getLearningTime().indexOf('H'));
		try
		{
			if(Integer.valueOf(duraHora).equals(new Integer(0))) duraHora="0";
		}
		catch(NumberFormatException e)
		{
			duraHora="0";
		}
		return duraHora;
	}
	public void setDuraHora(String duraHora) {
		this.duraHora = duraHora;
	}
	
	public String getDuraMin() {
		if (rm.getLearningTime() != null && rm.getLearningTime().length() > 4 && rm.getLearningTime().indexOf('H')>0 && rm.getLearningTime().indexOf('M')>0)
			duraMin = rm.getLearningTime().substring(rm.getLearningTime().indexOf('H')+1,rm.getLearningTime().indexOf('M'));
		try
		{
			if(Integer.valueOf(duraMin).equals(new Integer(0))) duraMin="0";
		}
		catch(NumberFormatException e)
		{
			duraMin="0";
		}
		return duraMin;
	}
	public void setDuraMin(String duraMin) {
		this.duraMin = duraMin;
	}
	public String getDuraSeg() {
		if (rm.getLearningTime() != null && rm.getLearningTime().length() > 4 && rm.getLearningTime().indexOf('M')>0 && rm.getLearningTime().indexOf('S')>0)
			duraSeg = rm.getLearningTime().substring(rm.getLearningTime().indexOf('M')+1,rm.getLearningTime().indexOf('S'));
		try
		{
			if(Integer.valueOf(duraSeg).equals(new Integer(0))) duraSeg="0";
		}
		catch(NumberFormatException e)
		{
			duraSeg="0";
		}
			
		return duraSeg;
	}
	public void setDuraSeg(String duraSeg) {
		this.duraSeg = duraSeg;
	}
	
	public String getLicense() {
		RecursBD rbd = new RecursBD();
		ArrayList al;
		try {
			int iLicense = Integer.parseInt(rm.getLicense());
			al = rbd.getLlicencia(RecursBD.LABELS);
			license = (String) al.get(iLicense);			
		} catch (NumberFormatException nfe){
			license = "";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			license = "Error";
		}
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}

	public String getMaxAge() {
		maxAge = rm.getMaxAge();
		return maxAge;
	}
	public void setMaxAge(String maxAge) {
		this.maxAge = maxAge;
	}
	
	public String getMinAge() {
		minAge = rm.getMinAge();
		return minAge;
	}
	public void setMinAge(String minAge) {
		this.minAge = minAge;
	}

	public String getResourceType() {
		RecursBD rbd = new RecursBD();
		
		ArrayList al;
		try {
			al = rbd.getResourceType(RecursBD.LABELS);
			ArrayList lrt = rm.getResourceType();
			resourceType = "";
			for (int i = 0; i < lrt.size(); i++){
				resourceType += al.get(Integer.parseInt((String)lrt.get(i))-1);
				if (i +1 < lrt.size())
					resourceType += ", ";
			}
		} catch (MerliDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resourceType = "Error";
		}
		
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	public String getResponsable() {
		responsable = rm.getResponsable();
		return responsable;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
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
	
	public String getTaxonTerm() {	
		taxonTerm = Utility.toParaula(rm.getTaxonTerm().toString());
		return taxonTerm;
	}
	public void setTaxonTerm(String taxonTerm) {
		this.taxonTerm = taxonTerm;
	}
	
	public String getTitle() {
		return title;//new MerliBean().printTitleElement(title,60);
	}
	public String getTitleShort() {
		return new MerliBean().printTitleElement(title,60);
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getUrl() {
		//url = new MerliBean().printTitleElement(rm.getUrl(),70);
		url = rm.getUrl();
		return url;
	}	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getVersion() {
		version = rm.getVersion();
		if (rm.getVersion()!=null) return version;
		return "";
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public String getMissatge() {
		return missatge;
	}
	public void setMissatge(String missatge) {
		this.missatge = missatge;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getUsuari() {
		return usuari;
	}
	public void setUsuari(String usuari) {
		this.usuari = usuari;
	}

	public Collection getLAssign() {
		return lAssign;
	}
	public void setLAssign(Collection assign) {
		lAssign = assign;
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

	public String getRightsEn() {
		return rightsEn;
	}
	public void setRightsEn(String rightsEn) {
		this.rightsEn = rightsEn;
	}
	public String getRightsEs() {
		return rightsEs;
	}
	public void setRightsEs(String rightsEs) {
		this.rightsEs = rightsEs;
	}	
	public String getRightsOc() {
		return rightsOc;
	}
	public void setRightsOc(String rightsOc) {
		this.rightsOc = rightsOc;
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

	public String getVtradCa() {
		return vtradCa;
	}
	public void setVtradCa(String vtradCa) {
		this.vtradCa = vtradCa;
	}
	public String getVtradEs() {
		return vtradEs;
	}
	public void setVtradEs(String vtradEs) {
		this.vtradEs = vtradEs;
	}
	public String getVtradOc() {
		return vtradOc;
	}
	public void setVtradOc(String vtradOc) {
		this.vtradOc = vtradOc;
	}

	public boolean isValidaTradCa() {
		return CA.equals(vtradCa);
	}
	public void setValidaTradCa(boolean validaTradCa) {
		if (validaTradCa)
			this.vtradCa = CA;
		else
			this.vtradCa ="";
	}
	public boolean isValidaTradEn() {
		return EN.equals(vtradEn);
	}
	public String getVtradEn() {
		return vtradEn;
	}
	public void setVtradEn(String vtradEn) {
		this.vtradEn = vtradEn;
	}
	public void setValidaTradEn(boolean valida) {
		if (valida)
			this.vtradEn = EN;
		else
			this.vtradEn ="";
	}
	public boolean isValidaTradEs() {
		return ES.equals(vtradEs);
	}
	public void setValidaTradEs(boolean valida) {
		if (valida)
			this.vtradEs = ES;
		else
			this.vtradEs ="";
	}	
	public boolean isValidaTradOc() {
		return OC.equals(vtradOc);
	}
	public void setValidaTradOc(boolean valida) {
		if (valida)
			this.vtradOc = OC;
		else
			this.vtradOc ="";
	}

	public String getSendAgrega() {
		return sendAgrega;
	}
	public void setSendAgrega(String sendAgrega) {
		this.sendAgrega = sendAgrega;
	}
	public void setSendAgrega(boolean sendAgrega) {
		this.sendAgrega = "0";
		if (sendAgrega)
			this.sendAgrega = "1";			
	}

	public boolean isSendAgrega() {
		return (sendAgrega != null && sendAgrega =="1");
	}
/*	
	public String[] getSendAgrega() {
		return sendAgrega;
	}

	public void setSendAgrega(String[] sendAgrega) {
		this.sendAgrega = sendAgrega;
	}
	
	*/
	
	public ArrayList getTipusRel() {
		return tipusRel;
	}
	public void setTipusRel(ArrayList tipusRel) {
		this.tipusRel = tipusRel;
	}

	public String getContext2() {
		return context2;
	}
	public void setContext2(String context2) {
		this.context2 = context2;
	}

	public ArrayList getDescRel() {
		return descRel;
	}
	public void setDescRel(ArrayList descRel) {
		this.descRel = descRel;
	}
//	private void setDescRel(ArrayList descripcioRelacio) {
//		for(int i=0;i<descripcioRelacio.size();i++)
//			descRel[i]=(String)descripcioRelacio.get(i);
//	}
	
	public ArrayList getRecRel() {
		return recRel;
	}	
//	public Integer getRecRelI(){
//		Integer res;
//		try {
//			res = Integer.valueOf(recRel);
//		}catch(Exception e)
//		{
//			if(recRel.equals(""))	res=null;
//			else					res = new Integer(-1);
//		}
//		return res;
//	}
	public void setRecRel(ArrayList recRel) {
		this.recRel = recRel;
	}
//	private void setRecRel(ArrayList list) {
//		for(int i=0;i<list.size();i++)
//			recRel[i]=(String)list.get(i);
//	}
	
	public boolean getRecFisic() {
		return recFisic;
	}
	public void setRecFisic(boolean recFisic) {
		this.recFisic = recFisic;
	}

	public String getCaractRFisic() {
		return caractRFisic;
	}
	public void setCaractRFisic(String caractRFisic) {
		this.caractRFisic = caractRFisic;
	}

	public ArrayList getTipusIdFisic() {
		return tipusIdFisic;
	}


	public void setTipusIdFisic(ArrayList tipusIdFisic) {
		this.tipusIdFisic = tipusIdFisic;
	}


	public ArrayList getTipusIdFisicSel() {
		return tipusIdFisicSel;
	}


	public void setTipusIdFisicSel(ArrayList tipusIdFisicSel) {
		this.tipusIdFisicSel = tipusIdFisicSel;
	}


	public ArrayList getTipusRelSel() {
		return tipusRelSel;
	}


	public void setTipusRelSel(ArrayList tipusRelSel) {
		this.tipusRelSel = tipusRelSel;
	}


	public ArrayList getIdFisic() {
		return idFisic;
	}


	public void setIdFisic(ArrayList idFisic) {
		this.idFisic = idFisic;
	}


	public ArrayList getUnitats() {
		return unitats;
	}

	public void setUnitats(ArrayList unitats) {
		this.unitats = unitats;
	}


	public void setDataEtiquetador(String dataEtiquetador) {
		this.dataEtiquetador = dataEtiquetador;
	}


	public void setDataValidador(String dataValidador) {
		this.dataValidador = dataValidador;
	}

	
	
	

}
