package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Entity extends ObjectMerli {

	private String username;
	private String name;
	private String email;
	private String org;
	private String vcard;
	
	private int idOrg;
	

	public Entity() {
		super();
		this.username = "";
		this.name = "";
		this.email = "";
		this.org = "";
		this.vcard = "";
		idOrg = -1;
	}
	public Entity(String username, String email, String org, String vcard) {
		super();
		if (vcard != null && vcard.length() > 5){
			this.vcard = vcard;
			this.createVcard();
		}
		this.username = username;
		this.name = username;
		this.email = email;
		this.org = org;
		this.vcard = vcard;
		idOrg = -1;
		
		initEntity();
	}

	public Entity(String username, String name, String email, String org, String vcard) {
		super();
		if (vcard != null && vcard.length() > 5){
			this.vcard = vcard;
			this.createVcard();
		}
		this.username = username;
		this.name = name;
		this.email = email;
		this.org = org;
		this.vcard = vcard;
		idOrg = -1;
		
		initEntity();
	}




	public Entity(SOAPElement seEntity) {		
		vcard = seEntity.getValue();

		initEntity();
		idOrg = -1;
	}
	
	
	public int getIdOrg() {
		return idOrg;
	}

	public void setIdOrg(int idOrg) {
		this.idOrg = idOrg;
	}
	public void setIdOrg(String idOrg) {
		try{
			this.idOrg = Integer.parseInt(idOrg);
		}catch(Exception e){}
	}
	
	public String getEmail() {
		if ("".equals(email) || email == null){
	         putEmail();
	     }
		return email;
	}
	



	public void setEmail(String email) {
		this.email = email;
	}
	
	private void putEmail(){
		String ident = "INTERNET:";
		try{
			int iStart = vcard.indexOf(ident);
	        int iEnd = vcard.indexOf (APXTEC32.SALT_LINIA,iStart);
	        if (iEnd<0) iEnd = vcard.indexOf ("ORG:");
	        if (iEnd<0) iEnd = vcard.indexOf("END:");
	        if (iStart<=iEnd && iStart>=0 && iEnd>=0) email = vcard.substring(iStart+ident.length(),iEnd).trim();
		}catch (Exception e){
	        e.printStackTrace();
	    }
	}



	public String getOrg() {
		 if ("".equals(org) || org == null){
	         putOrg();
	     }
		 
		return org;
	}
	



	private void putOrg() {
		String ident = "ORG:";
		 try{
			int iStart = vcard.indexOf(ident);
	        int iEnd = vcard.indexOf (APXTEC32.SALT_LINIA,iStart);
	        if (iEnd<0) iEnd = vcard.indexOf("END:");
	        if (iStart<=iEnd && iStart>=0 && iEnd>=0) org = vcard.substring(iStart+ident.length(),iEnd).trim();
		}catch (Exception e){
	        e.printStackTrace();
	    }
	}
	
	
	public void setOrg(String org) {
		this.org = org;
	}
	
	public String getUsername() {
		
        if ("".equals(username) || username == null){
            putUsername();
        }
        
        return username;
    }
	
	private void putUsername(){
		String ident = "FN:";
		try{
			int iStart = vcard.indexOf(ident);
	        int iEnd = vcard.indexOf (APXTEC32.SALT_LINIA,iStart);
	        if (iEnd<0) iEnd = vcard.indexOf ("EMAIL;");
	        if (iEnd<0) iEnd = vcard.indexOf("END:");
	        if (iStart<=iEnd && iStart>=0 && iEnd>=0) username = vcard.substring(iStart+ident.length(),iEnd).trim();
		}catch (Exception e){
	        e.printStackTrace();
	    }
	}



	public void setUsername(String username) {
		this.username = username;
	}
	

	private void putName(){
		/**
		 * Falta fer.
		 */
	}

	public String getName() {
		if ("".equals(name) || name == null){
            putName();
        }
        
        return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getVcard() {
		return vcard;
	}
	
	public void setVcard(String vcard) {
		this.vcard = vcard;
		
		this.putEmail();
		this.putOrg();
		this.putUsername();
	
	}
	
	private void initEntity(){
		this.putEmail();
		this.putOrg();
		this.putUsername();
		this.createVcard();
	}
	
	
	public SOAPElement toXml() throws SOAPException {
		SOAPElement seEntity;
		String vcardLocal;
		
		seEntity = soapFactory.createElement("entity");
		
//		if (vcard.length() > 5)		
//			vcardLocal = vcard;
//		else{
			vcardLocal = createVcard();
//		}

		if (vcardLocal == null) vcardLocal = "";
		seEntity.addTextNode(vcardLocal);
		
		return seEntity;
	}
	
	
	private String createVcard() {
		String vcardLocal ="BEGIN:VCARD"+APXTEC32.SALT_LINIA;
		vcardLocal +="VERSION:3.0"+APXTEC32.SALT_LINIA;
		vcardLocal +="N: "+name+APXTEC32.SALT_LINIA;
		vcardLocal +="FN: "+username+APXTEC32.SALT_LINIA;
		vcardLocal +="EMAIL;TYPE=INTERNET:"+email+APXTEC32.SALT_LINIA;
		vcardLocal +="ORG:"+org+APXTEC32.SALT_LINIA;
		vcardLocal += "END:VCARD";
		
		vcard = vcardLocal;
		
		return vcardLocal;
	}
	public String toUrl(String prefix) {
		String params="";
		String vcardLocal = createVcard();
		
		if (vcardLocal == null) vcardLocal = "";
		params+=prefix+"="+vcardLocal+"&";
		
		return params;
	}

}
