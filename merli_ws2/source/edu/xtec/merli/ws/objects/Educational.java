package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import edu.xtec.merli.ws.WSMerliBD;

public class Educational extends ObjectMerli {

	private ArrayList learningResourceTypeList = new ArrayList();
	private ArrayList intendedEndUserRoleList = new ArrayList();
	private ArrayList contextList = new ArrayList();
	//private String typicalAgeRange;
	private LangString typicalAgeRange = new LangString();;
	private SourceValue difficulty = new SourceValue("difficulty");
	private Duration typicalLearningTime = new Duration();
	private ArrayList languageList = new ArrayList();
	
	private boolean lomEs = false;

	public Educational() {
		super();
		// TODO Auto-generated constructor stub
		this.learningResourceTypeList = new ArrayList();
		this.intendedEndUserRoleList = new ArrayList();
		this.contextList = new ArrayList();
		this.typicalAgeRange = new LangString();//"";
		this.difficulty = new SourceValue("difficulty");
		this.typicalLearningTime = new Duration();
		this.languageList = new ArrayList();
	}
	public Educational(ArrayList learningResourceType, ArrayList intendedEndUserRole, ArrayList context, LangString typicalAgeRange, SourceValue difficulty, Duration typicalLearningType) {
		this(learningResourceType, intendedEndUserRole, context, typicalAgeRange, difficulty, typicalLearningType, new ArrayList());
	}
	public Educational(ArrayList learningResourceType, ArrayList intendedEndUserRole, ArrayList context, LangString typicalAgeRange, SourceValue difficulty, Duration typicalLearningType, ArrayList language) {
		super();

		this.learningResourceTypeList = learningResourceType;
		this.intendedEndUserRoleList = intendedEndUserRole;
		this.contextList = context;
		this.typicalAgeRange = typicalAgeRange;
		this.difficulty = difficulty;
		this.typicalLearningTime = typicalLearningType;
		this.languageList = language;
	}




	public Educational(SOAPElement seEducational) throws SOAPException {
		
		learningResourceTypeList = new ArrayList();
		Iterator it=seEducational.getChildElements(soapFactory.createName("learningResourceType"));
        while (it.hasNext())
			learningResourceTypeList.add(new SourceValue((SOAPElement)it.next()));
		
		intendedEndUserRoleList = new ArrayList();
		it=seEducational.getChildElements(soapFactory.createName("intendedEndUserRole"));
        while (it.hasNext())
			intendedEndUserRoleList.add(new SourceValue((SOAPElement)it.next()));
		
		contextList = new ArrayList();
		it=seEducational.getChildElements(soapFactory.createName("context"));
        while (it.hasNext())
			contextList.add(new SourceValue((SOAPElement)it.next()));
		
		it=seEducational.getChildElements(soapFactory.createName("typicalAgeRange"));
		if (it.hasNext())
			typicalAgeRange = new LangString(((SOAPElement)it.next()));//(((SOAPElement)it.next()).getValue());

		it=seEducational.getChildElements(soapFactory.createName("difficulty"));
		if (it.hasNext())
			difficulty = new SourceValue((SOAPElement)it.next());
		
		it=seEducational.getChildElements(soapFactory.createName("typicalLearningTime"));
		if (it.hasNext())
			typicalLearningTime = new Duration((SOAPElement)it.next());
	}
	


	public SourceValue getDifficulty() {
		return difficulty;
	}
	



	public void setDifficulty(SourceValue difficulty) {
		this.difficulty = difficulty;
	}
	




	public ArrayList getContextList() {
		return contextList;
	}
	
	public void setContextList(ArrayList contextList) {
		this.contextList = contextList;
	}
	
	public ArrayList getIntendedEndUserRoleList() {
		return intendedEndUserRoleList;
	}
	
	public void setIntendedEndUserRoleList(ArrayList intendedEndUserRoleList) {
		this.intendedEndUserRoleList = intendedEndUserRoleList;
	}
	
	public ArrayList getLearningResourceTypeList() {
		return learningResourceTypeList;
	}
	
	public void setLearningResourceTypeList(ArrayList learningResourceTypeList) {
		this.learningResourceTypeList = learningResourceTypeList;
	}
	
	public LangString getTypicalAgeRange() {
		return typicalAgeRange;
	}
	



	public void setTypicalAgeRange(LangString typicalAgeRange) {
		this.typicalAgeRange = typicalAgeRange;
	}
	



	public Duration getTypicalLearningTime() {
		return typicalLearningTime;
	}
	



	public void setTypicalLearningTime(Duration typicalLearningTime) {
		this.typicalLearningTime = typicalLearningTime;
	}
	


	public ArrayList getLanguageList() {
		return languageList;
	}
	public void setLanguageList(ArrayList languageList) {
		this.languageList = languageList;
	}


	public SOAPElement toXml() throws SOAPException {
		SOAPElement seEducational;
		SOAPElement seTypicalAgeRange;
		SOAPElement seTypicalLearningTime;
		SOAPElement seLanguage;
		
		SourceValue aux;
		seEducational = soapFactory.createElement(soapFactory.createName("educational"));

		if (learningResourceTypeList != null && learningResourceTypeList.size()>0){
			for (int i=0; i<learningResourceTypeList.size();i++){
				aux=(SourceValue) learningResourceTypeList.get(i);
				aux.setName("learningResourceType");
				seEducational.addChildElement(aux.toXml());
			}
		}else{
			aux= new SourceValue("learningResourceType");
			if (lomEs){
				aux.setSource(APXTEC32.LOMes);
			}else{
				aux.setSource(APXTEC32.LRE3_VALUE);
			}
			aux.setValue(APXTEC32.DEFAULT_LEAR_RES_TYPE_VALUE);
			seEducational.addChildElement(aux.toXml());
		}

		//Interactivity Level. Mandatory.
		aux= new SourceValue("interactivityLevel");
		if (lomEs){
			aux.setSource(APXTEC32.LOMes);
		}else{
			aux.setSource(APXTEC32.LRE3_VALUE);
		}
		aux.setValue(APXTEC32.DEFAULT_INTERACTIVITY_LEVEL_VALUE);
		seEducational.addChildElement(aux.toXml());
		
		if (intendedEndUserRoleList == null) intendedEndUserRoleList = new ArrayList();
		for (int i=0; i<intendedEndUserRoleList.size();i++){
			aux=(SourceValue) intendedEndUserRoleList.get(i);
			aux.setName("intendedEndUserRole");
			seEducational.addChildElement(aux.toXml());
		}
		if (contextList == null) contextList = new ArrayList();
		for (int i=0; i<contextList.size();i++){
			aux=(SourceValue) contextList.get(i);
			aux.setName("context");
			seEducational.addChildElement(aux.toXml());
		}
		
		if (typicalAgeRange == null) typicalAgeRange = new LangString();
		seTypicalAgeRange= soapFactory.createElement(soapFactory.createName("typicalAgeRange"));
		seTypicalAgeRange.addChildElement(this.typicalAgeRange.toXml());
		seEducational.addChildElement(seTypicalAgeRange);
		
		if(difficulty!=null && !difficulty.getValue().equals(""))
		{
			//if (difficulty == null) difficulty = new SourceValue("difficulty");
			difficulty.setName("difficulty");
			seEducational.addChildElement(difficulty.toXml());
		}

		if (typicalLearningTime == null) typicalLearningTime = new Duration();
		//seTypicalLearningTime = soapFactory.createElement("typicalLearningTime");
		//seTypicalLearningTime.addChildElement(typicalLearningTime.toXml());
		//seEducational.addChildElement(seTypicalLearningTime);
		seEducational.addChildElement(typicalLearningTime.toXml("typicalLearningTime"));

		//Afageix l'element Language al Educational
		if (isLomEs()){
			if (languageList == null) languageList = new ArrayList();
			for (int i = 0; i < this.languageList.size(); i++){
				seLanguage= soapFactory.createElement(soapFactory.createName("language"));
				seLanguage.addTextNode((String) this.languageList.get(i));
				seEducational.addChildElement(seLanguage);			
			}
		}
		
		return seEducational;
	}
	
	
	
	public String getTypicalAgeRangeMin() {
		if (typicalAgeRange == null || typicalAgeRange.getString() == null || typicalAgeRange.getString().equals(""))
			return "0";
		return typicalAgeRange.getString().substring(0,typicalAgeRange.getString().indexOf('-'));
	}
	
	

	public String getTypicalAgeRangeMax() {
		if (typicalAgeRange == null || typicalAgeRange.getString() == null || typicalAgeRange.getString().equals(""))
			return "0";
		return typicalAgeRange.getString().substring(typicalAgeRange.getString().indexOf('-')+1);
	}
	
	public boolean isLomEs() {
		return lomEs;
	}
	public void setLomEs(boolean lomEs) {
		this.lomEs = lomEs;
	}
	public String toUrl2(String prefix) {
		String params = "";
		
		SourceValue aux;
		if (learningResourceTypeList != null && learningResourceTypeList.size()>0){
			for (int i=0; i<learningResourceTypeList.size();i++){
				aux=(SourceValue) learningResourceTypeList.get(i);
				aux.setName("learningResourceType");
				params+=aux.toUrl(prefix+"lRT"+i+"_");
			}
		}
		else{
			aux= new SourceValue("learningResourceType");
			aux.setSource(APXTEC32.LRE3_VALUE);
			aux.setValue(APXTEC32.DEFAULT_LEAR_RES_TYPE_VALUE);
			params+=aux.toUrl(prefix+"lRT_");
		}
		
		//Interactivity Level. Mandatory.
		aux= new SourceValue("interactivityLevel");
		aux.setSource(APXTEC32.LRE3_VALUE);
		aux.setValue(APXTEC32.DEFAULT_INTERACTIVITY_LEVEL_VALUE);
		params+=aux.toUrl(prefix+"iL_");
		
		
		if (intendedEndUserRoleList == null) intendedEndUserRoleList = new ArrayList();
		for (int i=0; i<intendedEndUserRoleList.size();i++){
			aux=(SourceValue) intendedEndUserRoleList.get(i);
			aux.setName("intendedEndUserRole");
			params+=aux.toUrl(prefix+"iEUR"+i+"_");
		}
		if (contextList == null) contextList = new ArrayList();
		for (int i=0; i<contextList.size();i++){
			aux=(SourceValue) contextList.get(i);
			aux.setName("context");
			params+=aux.toUrl(prefix+"cont"+i+"_");
		}
		
		if (typicalAgeRange == null) typicalAgeRange = new LangString();
		params+=this.typicalAgeRange.toUrl(prefix+"tAge_");		
		
		if (difficulty == null) difficulty = new SourceValue("difficulty");
		difficulty.setName("difficulty");
		params+=this.difficulty.toUrl(prefix+"dif_");	

		if (typicalLearningTime == null) typicalLearningTime = new Duration();
		params+=this.typicalLearningTime.toUrl(prefix+"tLT_");	

		return params;
	}
	
	public String toUrl(String prefix) {
		String params = "";
		WSMerliBD wsmbd = new WSMerliBD();
		
		SourceValue aux;
		if (learningResourceTypeList != null && learningResourceTypeList.size()>0){
			for (int i=0; i<learningResourceTypeList.size();i++){
				aux=(SourceValue) learningResourceTypeList.get(i);
				String idTipusRec=wsmbd.getTipusRecurs(aux.getValue());
				params+="tipRec="+idTipusRec+"&";
			}
		}
		else params+="tipRec="+APXTEC32.DEFAULT_LEAR_RES_TYPE_VALUE+"&";	
			
		if (intendedEndUserRoleList == null) intendedEndUserRoleList = new ArrayList();
		for (int i=0; i<intendedEndUserRoleList.size();i++){
			aux=(SourceValue) intendedEndUserRoleList.get(i);
			String idRolUs = wsmbd.getRolUsuari(aux.getValue());		
			params+="rolUser="+idRolUs+"&";
		}
		
		if (contextList == null) contextList = new ArrayList();
		for (int i=0; i<contextList.size();i++){
			aux=(SourceValue) contextList.get(i);
			String idContext = wsmbd.getContext(aux.getValue());		
			params+="context="+idContext+"&";
		}
		
		if (typicalAgeRange == null) typicalAgeRange = new LangString();
		String rang=this.typicalAgeRange.getString();
		String edatMin=rang.substring(0, rang.indexOf('-'));
		String edatMax=rang.substring(rang.indexOf('-')+1);
		params+="edatMin="+edatMin+"&edatMax="+edatMax+"&";	
		
		if (difficulty == null || difficulty.getValue()==null) difficulty = new SourceValue("difficulty");
		String idDif = wsmbd.getDificultat(difficulty.getValue());		
		params+="dificultat="+idDif+"&";	

		return params;
	}

}
