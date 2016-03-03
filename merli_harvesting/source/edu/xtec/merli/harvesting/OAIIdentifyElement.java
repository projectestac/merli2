package edu.xtec.merli.harvesting;

/*
 * OAIIdentifyElement.java
 *
 * Created on 2007/01/10
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Element;

public class OAIIdentifyElement extends OAIVerbElement {
	
	protected static HashMap hArgs;
	protected String sBaseURL;
	
	public OAIIdentifyElement(Map oArgs){
		super(oArgs);
	}
	
	protected HashMap getArguments(){
		if (hArgs==null){
			hArgs=new HashMap();
			hArgs.put("R", new String[]{"verb"});
		}
		return hArgs;
	}

	public String getVerb() {
		return VERB_IDENTIFY;
	}
	
	public String getBaseURL(){
		if (sBaseURL!=null) return sBaseURL;
		return "";
	}
	public void setBaseURL(String sBaseURL){
		this.sBaseURL=sBaseURL;
	}
	
	public String getRepositoryName(){
		return getProperty("identify.repositoryName");
	}

	public String getProtocolVersion(){
		return getProperty("identify.protocolVersion");
	}

	public String getEarliestDatestamp(){
		return getProperty("identify.earliestDatestamp");
	}

	public String getDeletedRecord(){
		return getProperty("identify.deletedRecord");
	}
	
	public String getSampleIdentifier(){
		return internalIdToIdentifier("125");
	}

	public Vector getAdminEmails(){
		Vector vEmails = new Vector();
		StringTokenizer stEmails = new StringTokenizer(getProperty("identify.adminEmail"),";");
		while (stEmails.hasMoreTokens()){
			vEmails.addElement(stEmails.nextToken());
		}
		return vEmails;
	}

	public void generateResponse() throws MerliHarvestingException {
		Element eVerb = new Element(getVerb(), XMLNS);
		addContent(eVerb);
		
		Element eRepository = new Element("repositoryName", XMLNS);
		eRepository.setText(getRepositoryName());
		eVerb.addContent(eRepository);
		
		Element eBaseURL = new Element("baseURL", XMLNS);
		eBaseURL.setText(getBaseURL());
		eVerb.addContent(eBaseURL);
		
		Element eProtocol = new Element("protocolVersion", XMLNS);
		eProtocol.setText(getProtocolVersion());
		eVerb.addContent(eProtocol);
		
		Enumeration enumEmails = getAdminEmails().elements();
		while (enumEmails.hasMoreElements()){
			Element eEmail = new Element("adminEmail", XMLNS);
			eEmail.setText(enumEmails.nextElement().toString());
			eVerb.addContent(eEmail);			
		}

		Element eDatestamp = new Element("earliestDatestamp", XMLNS);
		eDatestamp.setText(getEarliestDatestamp());
		eVerb.addContent(eDatestamp);
		
		Element eDeleted = new Element("deletedRecord", XMLNS);
		eDeleted.setText(getDeletedRecord());
		eVerb.addContent(eDeleted);
		
		Element eGranularity = new Element("granularity", XMLNS);
		eGranularity.setText(getGranularity());
		eVerb.addContent(eGranularity);
		
		Element eDescription = new Element("description", XMLNS);
		Element eId = new Element("oai-identifier", OAIID_XMLNS);
		eId.addNamespaceDeclaration(XSINS);
		eId.setAttribute(new Attribute("schemaLocation", OAIID_XMLNS+" http://www.openarchives.org/OAI/2.0/oai-identifier.xsd", XSINS));
		Element eScheme = new Element("scheme", OAIID_XMLNS);
		eScheme.setText(getScheme());
		eId.addContent(eScheme);
		Element eRepositoryIdentifier = new Element("repositoryIdentifier", OAIID_XMLNS);
		eRepositoryIdentifier.setText(getRepositoryIdentifier());
		eId.addContent(eRepositoryIdentifier);
		Element eDelimiter = new Element("delimiter", OAIID_XMLNS);
		eDelimiter.setText(getDelimiter());
		eId.addContent(eDelimiter);
		Element eSample = new Element("sampleIdentifier", OAIID_XMLNS);
		eSample.setText(getSampleIdentifier());
		eId.addContent(eSample);
		
		eDescription.addContent(eId);
		eVerb.addContent(eDescription);
		
	}
	
}
