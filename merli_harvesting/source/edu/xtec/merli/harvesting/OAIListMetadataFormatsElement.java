package edu.xtec.merli.harvesting;

/*
 * OAIIdentifyElement.java
 *
 * Created on 2007/04/30
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jdom.Element;

public class OAIListMetadataFormatsElement extends OAIVerbElement {
	
	protected static HashMap hArgs;
	
	public OAIListMetadataFormatsElement(Map oArgs){
		super(oArgs);
	}
	
	protected HashMap getArguments(){
		if (hArgs==null){
			hArgs=new HashMap();
			hArgs.put("R", new String[]{"verb"});
			hArgs.put("O", new String[]{"identifier"});
		}
		return hArgs;
	}

	public String getVerb() {
		return VERB_LIST_METADATA_FORMATS;
	}
	
	public Vector getMetadataFormats(){
		Vector vFormats = new Vector();
		StringTokenizer stFormats = new StringTokenizer(getProperty("harvesting.metadataFormats"),";");
		while (stFormats.hasMoreTokens()){
			vFormats.addElement(stFormats.nextToken());
		}
		return vFormats;
	}
	
	public static Vector getMetadataPrefixs(){
		Vector vPrefixs = new Vector();
		StringTokenizer stFormats = new StringTokenizer(getProperty("harvesting.metadataFormats"),";");
		while (stFormats.hasMoreTokens()){
			String sFormat = stFormats.nextToken();
			StringTokenizer stFormat = new StringTokenizer(sFormat, "$$");
			if (stFormat.hasMoreTokens()){
				vPrefixs.addElement(stFormat.nextToken());
			}
		}
		return vPrefixs;
	}
	
	public String getIdentifier(){
		return getArgument("identifier");
	}
	
	public boolean checkArguments() throws MerliHarvestingException{
		boolean bOk = super.checkArguments();
		if (bOk && isNotNull(getIdentifier())){
			bOk = externalToInternalId(getIdentifier())!=null;
			if (!bOk){
				throw new MerliHarvestingException(MerliHarvestingException.ERROR_ID_DOES_NOT_EXIST);
			}else{
				Element eResource = getDB().getResourceMetadata(getIdentifier());
				if (eResource==null){
					throw new MerliHarvestingException(MerliHarvestingException.ERROR_ID_DOES_NOT_EXIST);
				}
			}
		}
		return bOk;
	}	

	public void generateResponse() throws MerliHarvestingException {
		Element eVerb = new Element(getVerb(), XMLNS);
		addContent(eVerb);
		
		// Metadata information is generated dinamically, so is independent of the identifier
		Enumeration enumFormats = getMetadataFormats().elements();
		while (enumFormats.hasMoreElements()){
			String sFormat = (String)enumFormats.nextElement();
			StringTokenizer stFormat = new StringTokenizer(sFormat,"$$");
			
			if (stFormat.hasMoreTokens()){
				Element eFormat = new Element("metadataFormat", XMLNS);
				eVerb.addContent(eFormat);
				
				Element ePrefix = new Element("metadataPrefix", XMLNS);
				ePrefix.setText(stFormat.nextToken());
				eFormat.addContent(ePrefix);
				Element eSchema = new Element("schema", XMLNS);
				eSchema.setText(stFormat.nextToken());
				eFormat.addContent(eSchema);
				Element eNamespace = new Element("metadataNamespace", XMLNS);
				eNamespace.setText(stFormat.nextToken());
				eFormat.addContent(eNamespace);				
			}
		}
	}
	
}
