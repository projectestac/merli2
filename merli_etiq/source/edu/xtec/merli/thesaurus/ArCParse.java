/**
 * 
 */
package edu.xtec.merli.thesaurus;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import edu.xtec.merli.basedades.AccesBD;
import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.utils.Utility;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticException;
import edu.xtec.semanticnet.SemanticNet;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

/**
 * @author aleix
 *
 */
public class ArCParse {
	protected static ConnectionBeanProvider broker;
	Connection con;
	private static final Logger logger = Logger.getRootLogger();
	/**
	 * @param args
	 * @throws SemanticException 
	 * @throws SQLException 
	 * @throws MerliDBException 
	 * @throws ClassNotFoundException 
	 */
	public void main(String[] args) throws SemanticException, MerliDBException, SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Carrega el thesaurus del fitxer passat com a parametre.
	 * @param file Path del document XML amb el thesaurus.
	 * @throws MerliDBException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public ArCParse(String file) throws MerliDBException, SQLException, ClassNotFoundException{
		try {
			loadXMLdocument(file);					 
			} catch (SemanticException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public ArCParse() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Carrega el thesaurus del fitxer passat com a parametre.
	 * @param xmlPath Path del document XML amb el thesaurus.
	 * @throws SemanticException
	 * @throws MerliDBException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void loadXMLdocument(String xmlPath) throws SemanticException, MerliDBException, SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		//SAXBuilder builder = new SAXBuilder();
		Iterator iter;
		Hashtable hProp;
        try {
			/** 
			 * Descriu el constructor del parser i la seva validació. 
			 */
			Document dDesc;
			SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser", false);
            builder.setFeature("http://xml.org/sax/features/validation", false);        
           
			/**
			 * Carrega l'xml desde una url o des del disc segons el path 'xmlPath' donat.
			 */
		
			if(xmlPath.toLowerCase().indexOf("http://")!=0 && xmlPath.toLowerCase().indexOf("ftp://")!=0){
				File f = new File(xmlPath);
				dDesc = builder.build(f);
			}else{
				URL u = new URL(xmlPath);
				dDesc = builder.build(xmlPath);
			}
	       
//			Element el = getRootElement().getChild("vdex");
			Iterator itr = dDesc.getRootElement().getChildren().iterator();
			int i = 0;
			while (itr.hasNext()) {				
	            Element e = (Element) itr.next();
				if (e.getName().compareTo("term") == 0)	
					parseTermsOf(e);
	        }
			System.out.println("Total paraulotes:");

		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			SemanticException seE;
			seE = new SemanticException("ErrorXMLDescriptor");
			seE.setCode(SemanticException.XMLERROR);
			con.rollback();
			con.close();
			seE.setStackTrace(e.getStackTrace());
			throw seE;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			con.rollback();
			con.close();
			SemanticException seE = new SemanticException("NotLoadedXML");
			seE.setCode(SemanticException.XMLNOTLOADED);
			seE.setStackTrace(e.getStackTrace());
			throw seE;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			con.rollback();
			con.close();
			e.printStackTrace();
		} 
	}

	private void parseTermsOf(Element e){
		String text="";
		String id="";
		Iterator itr = null;
		Element et;
		Element eaux;
		List list = e.getChildren();
		List list2;
		for (int i=0;i<list.size();i++){
			//StructureID de l'element.
			et= (Element) list.get(i);
			if(et.getName().equals("termIdentifier")){
				id=et.getValue();
			}
			if(et.getName().equals("caption")){
				list2 = et.getChildren();
				for (int j=0;j<list2.size();j++){
					eaux= (Element) list2.get(j);
					if(eaux.getName().equals("langstring")){
						text=eaux.getValue();	
						break;
					}
				}
			}
			if(et.getName().equals("term")){
				parseTermsOf(et);
			}
		}
		try {
			executeInsertOf(id,text);
		} catch (SQLException e1) {
			System.out.println("ERROR INS:"+id+","+text);
		} catch (MerliDBException e2) {
			System.out.println("ERROR INS:"+id+","+text);
		}
	}

	private void executeInsertOf(String id, String text) throws SQLException, MerliDBException {
//		List alis = new ArrayList();
//		alis.add(id);
//		alis.add(text);
//
//		AccesBD.executeInsert("CUR_ARC_TERMES", alis, con);
		System.out.println("INSERT INTO CUR_ARC_TERMES VALUES ('"+id+"','"+text.replaceAll("'", "''")+"');");
	}

	
}
