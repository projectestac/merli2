package edu.xtec.merli.ws.objects;

import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

public class Entity extends ObjectMerli {

	private String username;
	private String email;
	private String org;
	private String vcard;
	
	

	public Entity() {
		super();
		// TODO Auto-generated constructor stub
		this.username = "";
		this.email = "";
		this.org = "";
		this.vcard = "";
	}
	public Entity(String username, String email, String org, String vcard) {
		super();
		// TODO Auto-generated constructor stub
		this.username = username;
		this.email = email;
		this.org = org;
		this.vcard = vcard;
	}




	public Entity(SOAPElement seEntity) {		
		vcard = seEntity.getValue();
	}
	
	public String getEmail() {
		return email;
	}
	



	public void setEmail(String email) {
		this.email = email;
	}
	



	public String getOrg() {
		return org;
	}
	



	public void setOrg(String org) {
		this.org = org;
	}
	
    public String getUsername() {
        if ("".equals(username) || username == null){
            try{
                int iStart = vcard.indexOf("FN:");
                int iEnd = vcard.indexOf ("EMAIL;");
                if (iEnd<0) iEnd = vcard.indexOf("END:");
                if (iStart<=iEnd && iStart>=0 && iEnd>=0) username = vcard.substring(iStart+3,iEnd).trim();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        
        return username;
    }



	public void setUsername(String username) {
		this.username = username;
	}
	



	public String getVcard() {
		return vcard;
	}
	
	public void setVcard(String vcard) {
		this.vcard = vcard;
	}
	
	public SOAPElement toXml() throws SOAPException {
		// TODO Auto-generated method stub
		SOAPElement seEntity;
		String vcardLocal;
		
		seEntity = soapFactory.createElement("entity");
		
		if (vcard.length() > 5)		
			vcardLocal = vcard;
		else{
			vcardLocal ="BEGIN:VCARD";
			vcardLocal +=" VERSION3.0";
			vcardLocal +=" FN:"+username;
			vcardLocal +=" EMAIL;TYPE=INTERNET:"+email;
			vcardLocal +=" ORG:"+org;
			vcardLocal += "END:VCARD";
		}

		if (vcardLocal == null) vcardLocal = "";
		seEntity.addTextNode(vcardLocal);
		
		return seEntity;
	}

}
