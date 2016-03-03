package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Contribute extends ObjectMerli {

	private SourceValue role;
	private Entity entity;
	private Date dateTime;
	
	
	
	

	public Contribute() {
		super();
		// TODO Auto-generated constructor stub
		this.role = new SourceValue("role");
		this.entity = new Entity();
		this.dateTime = new Date();
	}


	public Contribute(SourceValue role, Entity entity, Date dateTime) {
		super();
		// TODO Auto-generated constructor stub
		this.role = role;
		this.entity = entity;
		this.dateTime = dateTime;
	}






	public Contribute(SOAPElement seContribute) throws SOAPException {
		
		Iterator It=seContribute.getChildElements(soapFactory.createName("role"));
		if (It.hasNext()){
			role = new SourceValue((SOAPElement)It.next());
			role.setName("role");
		}
		
		It=seContribute.getChildElements(soapFactory.createName("entity"));
		if (It.hasNext())
			entity = new Entity((SOAPElement)It.next());
		
		It=seContribute.getChildElements(soapFactory.createName("date"));
		if (It.hasNext())
			dateTime = new Date((SOAPElement)It.next());
		
	}


	public Date getDateTime() {
		return dateTime;
	}
	





	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	





	public Entity getEntity() {
		return entity;
	}
	





	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	





	public SourceValue getRole() {
		return role;
	}
	





	public void setRole(SourceValue role) {
		this.role = role;
	}
	





	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seContribute;
		SOAPElement seDate;
		
		//Crea l'element contribute
		seContribute = soapFactory.createElement("contribute");
		
		//posa valor a l'element role i l'afageix a l'element contribute.

		if (role == null) role = new SourceValue("role");
		role.setName("role");
		seContribute.addChildElement(role.toXml());
		//Afageix l'element entity
		if (entity == null) entity = new Entity();
		seContribute.addChildElement(entity.toXml());
		//Afageix l'element dateTime
		if (dateTime == null) dateTime = new Date();
		seDate = soapFactory.createElement("date");
		seDate.addChildElement(dateTime.toXml());
		seContribute.addChildElement(seDate);		
		
		return seContribute;
	}

}
