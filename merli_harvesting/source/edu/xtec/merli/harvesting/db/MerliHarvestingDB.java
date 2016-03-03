package edu.xtec.merli.harvesting.db;

/*
 * MerliHarvestingDB.java
 *
 * Created on 2007/01/10
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.xml.soap.SOAPElement;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.Namespace;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.harvesting.OAIElement;
import edu.xtec.merli.harvesting.util.ResourceIdentifier;
import edu.xtec.merli.ws.WSMerliBD;
import edu.xtec.merli.ws.objects.IdResource;
import edu.xtec.merli.ws.objects.Identifier;
import edu.xtec.merli.ws.objects.Lom;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

public class MerliHarvestingDB {

	public final static String DBCONF_PATH = "/";
	public final static String DBCONF_FILE = "database.properties";

	private static ConnectionBeanProvider broker;
	private ConnectionBean c;
	private static Properties pDB;
	
	private static final Logger logger = Logger.getRootLogger();
	private String sDBFormatGranularity = "yyyy-mm-dd";
	
//	************************************	
//	* Database access
//	************************************
	
	public Vector getListIdentifiers(String sFrom, String sUntil){
		Vector vList = new Vector();
		try{
			StringBuffer sQuery = new StringBuffer();
			sQuery.append("SELECT tc.* ");
			sQuery.append("FROM (SELECT id_rec, MAX(d_data) AS max_date FROM mer_contribucio mc GROUP BY id_rec) tc, mer_rec_info mri ");
			sQuery.append("WHERE mri.id_rec=tc.id_rec AND mri.id_estat=4 ");
			if (sFrom!=null && sFrom.length()>0) sQuery.append(" AND max_date >= TO_DATE(?, '"+getDBFormatGranularity()+"') ");
			if (sUntil!=null && sUntil.length()>0) sQuery.append(" AND max_date <= TO_DATE(?, '"+getDBFormatGranularity()+"') ");
			sQuery.append("ORDER BY max_date ");
			PreparedStatement pstmt = getConnection().getPreparedStatement(sQuery.toString());
			int iIndex=1;
			if (sFrom!=null && sFrom.length()>0) pstmt.setString(iIndex++, sFrom);
			if (sUntil!=null && sUntil.length()>0) pstmt.setString(iIndex++, sUntil);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()){
				ResourceIdentifier oResource = new ResourceIdentifier(OAIElement.internalIdToIdentifier(rs.getString("id_rec")), rs.getDate("max_date"));
				vList.add(oResource);
				//OAIHeaderElement eHeader = new OAIHeaderElement(OAIElement.internalIdToIdentifier(rs.getString("id_rec")), rs.getDate("max_date"));
				//vList.add(eHeader);
			}
			rs.close();
			getConnection().closeStatement(pstmt);				
		}
		catch (Exception e) {
			logger.error("MerliHarvestingDB EXCEPTION --> "+e);
			e.printStackTrace();
		}finally{
			freeConnection();				
		}		
		return vList;
	}
	
	public ResourceIdentifier getResourceIdentifier(String sId){
		ResourceIdentifier oResource = null;
		try{
			StringBuffer sQuery = new StringBuffer();
			sQuery.append("SELECT mc.id_rec, MAX(mc.d_data) AS max_date FROM mer_contribucio mc WHERE mc.id_rec=? GROUP BY id_rec");
			PreparedStatement pstmt = getConnection().getPreparedStatement(sQuery.toString());
			pstmt.setString(1, OAIElement.externalToInternalId(sId));
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				oResource = new ResourceIdentifier(OAIElement.internalIdToIdentifier(rs.getString("id_rec")), rs.getDate("max_date"));
			}
			rs.close();
			getConnection().closeStatement(pstmt);				
		}
		catch (Exception e) {
			logger.error("MerliHarvestingDB EXCEPTION --> "+e);
			e.printStackTrace();
		}finally{
			freeConnection();				
		}		
		return oResource;
	}
	
	
	/**
	 * 
	 * @param sId external id (sample: oai:merli.xtec.cat:merli/125)
	 * @return
	 */
	public Element getResourceMetadata(String sId){
		Element eResource = null;
		try{
			WSMerliBD oWSDB = new WSMerliBD();
			String sInternalId = OAIElement.externalToInternalId(sId);
			IdResource oId = new IdResource(sInternalId, "MERLI");
			Lom oLom = oWSDB.getResource(oId);
			if (oLom!=null){
				Identifier oNewId=new Identifier("MERLI", "merli/"+sInternalId);
				oLom.getGeneral().setIdentifier(oNewId);
				oLom.getMetaMetaData().setIdentifier(oNewId);
				SOAPElement seLOM = oLom.toXml();
				
				org.jdom.input.DOMBuilder db = new org.jdom.input.DOMBuilder();
				eResource = db.build(seLOM);
				Namespace eNS = org.jdom.Namespace.getNamespace(OAIElement.getProperty("harvesting.lom.xmlns"));
				eResource.setNamespace(eNS);
				Iterator itResource = eResource.getDescendants(new org.jdom.filter.ElementFilter());
				while (itResource.hasNext()){
					Element eTmp = (Element)itResource.next();
					eTmp.setNamespace(eNS);
				}
				eResource = (org.jdom.Element)eResource.detach();
				
				
				//seLOM.addNamespaceDeclaration("xmlns","http://ltsc.ieee.org/xsd/LOM");
				//seLOM.addNamespaceDeclaration("xsi","http://www.w3.org/2001/XMLSchema-instance");
				//seLOM.addNamespaceDeclaration("xmlns:oai_lom","http://www.openarchives.org/OAI/2.0/oai_lom/");
				//seLom.addNamespaceDeclaration("schemaLocation","http://ltsc.ieee.org/xsd/LOM http://fire.eun.org/xsd/lre/lre.xsd");
				
				/*Saaj2StringConverter oSaaj = new Saaj2StringConverter();
				String sLom = oSaaj.toString(seLOM);
				
				if (sLom.indexOf("<lom>")==0){
					String sXMLNS = OAIElement.getProperty("harvesting.lom.xmlns");
					if (sXMLNS==null) sXMLNS = "";
					sLom="<lom "+sXMLNS+">"+sLom.substring(5);
				}
				
				int iStart = sLom.indexOf("<location>");
				if (iStart>0){
					int iEnd = sLom.indexOf("</location>");
					String sLocation = sLom.substring(iStart+10, iEnd).trim();
					if (sLocation.indexOf("&")>=0){
						sLocation="<![CDATA["+sLocation+"]]>";
						sLom=sLom.substring(0, iStart)+"<location>"+sLocation+sLom.substring(iEnd);
						
					}
				}
				
				SAXBuilder sb=new SAXBuilder();
				sb.setExpandEntities(false);
				eResource = sb.build(new StringReader(sLom)).getRootElement();*/
				
				
				//eResource = (Element)eResource.detach();
				//eResource = (Element)eResource.clone();				
				
/*				SAXBuilder sb=new SAXBuilder(false);
				sb.setValidation(false);
				sb.setExpandEntities(false);
				sb.setIgnoringElementContentWhitespace(true);
				eResource = sb.build(new StringReader(sLom)).getRootElement();
				eResource = (Element)eResource.clone();
*/				
			}
		}catch (Exception e){
			logger.error("EXCEPTION getResource("+sId+") -> "+e);
			if (!(e instanceof MerliDBException)) e.printStackTrace();
		}
		return eResource;
	}
	
//	************************************	
//	* Database connection
//	************************************
	
	protected ConnectionBeanProvider getConnectionBeanProvider(){
		try{
			if(broker == null) { // Only created by first servlet to call
				broker = ConnectionBeanProvider.getConnectionBeanProvider(true, getDBProperties());				
			}
		}catch (Exception e){
			logger.error("EXCEPTION getting ConnectionBeanProvider-> "+e);
			e.printStackTrace();
		}
		return broker;
	}
	
	public ConnectionBean getConnection(){
		if (c==null){
			try{
				c = getConnectionBeanProvider().getConnectionBean();
				c.getConnection().setAutoCommit(true);
			}catch (Exception e){
				logger.error("EXCEPTION getting connection -> "+e);				
			}
		}
		return c;
	}

	public void freeConnection() {
		if (c!=null && broker!=null) {
			broker.freeConnectionBean(c);
			c=null;
		}
	}
	
//	************************************	
//	* Properties
//	************************************
	
	protected static Properties getDBProperties() throws Exception{
		if (pDB==null){
			pDB = new Properties();
			try{
				pDB.load(MerliHarvestingDB.class.getResourceAsStream(DBCONF_PATH+DBCONF_FILE));
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return pDB;
	}
	
	private String getDBFormatGranularity(){
		return this.sDBFormatGranularity;
	}
	public void setDBFormatGranularity(String sFormat){
		this.sDBFormatGranularity=sFormat;
	}
		
}
