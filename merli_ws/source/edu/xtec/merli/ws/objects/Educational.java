package edu.xtec.merli.ws.objects;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Educational extends ObjectMerli {

	private ArrayList learningResourceTypeList;
	private ArrayList intendedEndUserRoleList;
	private ArrayList contextList;
	private String typicalAgeRange;
	private SourceValue difficulty;
	private Duration typicalLearningTime;
	
	

	public Educational() {
		super();
		// TODO Auto-generated constructor stub
		this.learningResourceTypeList = new ArrayList();
		this.intendedEndUserRoleList = new ArrayList();
		this.contextList = new ArrayList();
		this.typicalAgeRange = "";
		this.difficulty = new SourceValue("difficulty");
		this.typicalLearningTime = new Duration();
	}
	public Educational(ArrayList learningResourceType, ArrayList intendedEndUserRole, ArrayList context, String typicalAgeRange, SourceValue difficulty, Duration typicalLearningType) {
		super();
		// TODO Auto-generated constructor stub
		this.learningResourceTypeList = learningResourceType;
		this.intendedEndUserRoleList = intendedEndUserRole;
		this.contextList = context;
		this.typicalAgeRange = typicalAgeRange;
		this.difficulty = difficulty;
		this.typicalLearningTime = typicalLearningType;
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
			typicalAgeRange = (((SOAPElement)it.next()).getValue());

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
	
	public String getTypicalAgeRange() {
		return typicalAgeRange;
	}
	



	public void setTypicalAgeRange(String typicalAgeRange) {
		this.typicalAgeRange = typicalAgeRange;
	}
	



	public Duration getTypicalLearningTime() {
		return typicalLearningTime;
	}
	



	public void setTypicalLearningTime(Duration typicalLearningTime) {
		this.typicalLearningTime = typicalLearningTime;
	}
	



	public SOAPElement toXml() throws SOAPException {
		SOAPElement seEducational;
		SOAPElement seTypicalAgeRange;
		SOAPElement seTypicalLearningTime;
		
		SourceValue aux;
		seEducational = soapFactory.createElement(soapFactory.createName("educational"));

		if (learningResourceTypeList == null) learningResourceTypeList = new ArrayList();
		for (int i=0; i<learningResourceTypeList.size();i++){
			aux=(SourceValue) learningResourceTypeList.get(i);
			aux.setName("learningResourceType");
			seEducational.addChildElement(aux.toXml());
		}
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
		if (typicalAgeRange == null) typicalAgeRange = "";
		seTypicalAgeRange = soapFactory.createElement("typicalAgeRange");
		seTypicalAgeRange.addTextNode(typicalAgeRange);
		seEducational.addChildElement(seTypicalAgeRange);

		if (difficulty == null) difficulty = new SourceValue("difficulty");
		difficulty.setName("difficulty");
		seEducational.addChildElement(difficulty.toXml());

		if (typicalLearningTime == null) typicalLearningTime = new Duration();
		seTypicalLearningTime = soapFactory.createElement("typicalLearningTime");
		seTypicalLearningTime.addChildElement(typicalLearningTime.toXml());
		seEducational.addChildElement(seTypicalLearningTime);
				
		return seEducational;
	}
	
	
	
	public String getTypicalAgeRangeMin() {
		if (typicalAgeRange == null)
			return "0";
		return typicalAgeRange.substring(0,typicalAgeRange.indexOf('-'));
	}
	
	

	public String getTypicalAgeRangeMax() {
		if (typicalAgeRange == null)
			return "0";
		return typicalAgeRange.substring(typicalAgeRange.indexOf('-')+1);
	}

}
