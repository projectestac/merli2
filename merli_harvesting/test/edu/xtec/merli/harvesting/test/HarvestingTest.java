package edu.xtec.merli.harvesting.test;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;

import junit.framework.TestCase;

import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format.TextMode;

public class HarvestingTest extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(HarvestingTest.class);
	}
	
	public final void testListIdentifiers(){
		String sResumptionToken = null;
		org.jdom.Element eOAI = null;
		try{
			do {
				eOAI = callListIdentifiers(sResumptionToken);
				Iterator itResumption = eOAI.getDescendants(new ElementFilter("resumptionToken"));
				if (itResumption.hasNext()){
					org.jdom.Element eResumption = (org.jdom.Element)itResumption.next();
					sResumptionToken = eResumption.getText();
					System.out.println("resumptionToken="+sResumptionToken+":  "+eResumption.getAttributeValue("cursor")+"/"+eResumption.getAttributeValue("completeListSize"));
					Thread.sleep(500);
				}
			} while (sResumptionToken!=null);
		}catch (Exception e){
			System.out.println("EXCEPTION testListIdentifiers-> resumptionToken="+sResumptionToken);
			e.printStackTrace();
			try{
				if (eOAI!=null){
					XMLOutputter xout = new XMLOutputter();
					Format oFormat = Format.getPrettyFormat();
					oFormat.setOmitDeclaration(false);
			        oFormat.setEncoding("UTF-8");
					oFormat.setTextMode(TextMode.TRIM);
					xout.setFormat(oFormat);
					xout.output(eOAI, System.out);
				}
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public org.jdom.Element callListIdentifiers(String sResumptionToken) throws Exception{
		String sVerb = "ListIdentifiers";
		String sParams = "verb="+sVerb;
		if (sResumptionToken==null) {
			String sMetadataPrefix = "oai_lom";
			sParams+="&metadataPrefix="+sMetadataPrefix;
		}else{
			sParams+="&resumptionToken="+sResumptionToken;
		}
		//String sServer = "http://localhost:8080/merli_harvesting";
		String sServer = "http://aplitic.xtec.cat/e13_merli_harvesting";
		URL uHarvesting = new URL(sServer+"/MerliHarvestingServlet?"+sParams);
		
		SAXBuilder sb=new SAXBuilder();
		sb.setExpandEntities(false);
		org.jdom.Element eOAI = sb.build(new InputStreamReader(uHarvesting.openStream())).getRootElement();		
		return eOAI;
		
	}


}
