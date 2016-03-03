/**
 * 
 */
package edu.xtec.merli.thesaurus;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
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

import edu.xtec.merli.MerliBean;
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
public class AccessParse {
	protected static ConnectionBeanProvider broker;
	Connection con;
	private static final Logger logger = Logger.getRootLogger();
	public static Hashtable ht = new Hashtable();
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
	public AccessParse(String file) throws MerliDBException, SQLException, ClassNotFoundException{
		try {
			parseDocument(file, "ca");					 
			} catch (SemanticException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public AccessParse() {
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
	public void parseDocument(String xmlPath, String lang) throws SemanticException, MerliDBException, SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub
		//SAXBuilder builder = new SAXBuilder();
		Iterator iter;
		Hashtable hProp;
        try {
			/** 
			 * Descriu el constructor del parser i la seva validació. 
			 */
			Document dDesc = loadXMLDocument(xmlPath);
	       
//			Element el = getRootElement().getChild("vdex");
			Iterator itr = dDesc.getRootElement().getChildren().iterator();
			int i = 0;
			while (itr.hasNext()) {				
	            Element e = (Element) itr.next();
				if (e.getName().compareTo("term") == 0)	
					parseTermsOf(e, lang);
				if (e.getName().compareTo("relationship") == 0)	
					parseRelationshipOf(e, lang);
				i++;
	        }
			System.out.println("Total paraulotes:"+i);

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


	/**
	 * Carrega els ids del fitxer passat com a parametre.
	 * @param xmlPath Path del document XML amb el thesaurus.
	 * @throws SemanticException
	 * @throws MerliDBException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void importarXMLdocument(String xmlPath) throws SemanticException, MerliDBException, SQLException, ClassNotFoundException {
		//SAXBuilder builder = new SAXBuilder();
		Iterator iter;
		Hashtable hProp;
        try {
			/** 
			 * Descriu el constructor del parser i la seva validació. 
			 */
			Document dDesc = loadXMLDocument(xmlPath);
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			con = DriverManager.getConnection("jdbc:oracle:thin:@ISOCO-VM-WINXP:1521:XE", "e13_merli","e13merli");
			ht.put("new", new Hashtable());
//			Element el = getRootElement().getChild("vdex");
			Iterator itr = dDesc.getRootElement().getChildren().iterator();
			int i = 0;
			String id;
			while (itr.hasNext()) {				
	            Element e = (Element) itr.next();
				if(e.getName().equals("idterm")){
					id=e.getValue();
					try{
					parseIds(id);
					}catch(Exception ed){
						System.out.println ("--Error a l'importar l'id: "+ id);
					}
				}
					
				i++;
	        }
			System.out.println("Total ids:"+i);

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
	
	
	private Document loadXMLDocument(String xmlPath) throws JDOMException, IOException, MalformedURLException {
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
		return dDesc;
	}

	private void parseTermsOf(Element e, String lang){
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
				parseTermsOf(et, lang);
			}
			if (e.getName().compareTo("relationship") == 0)	{
				parseRelationshipOf(e, lang);
			}
		}
		try {
			executeInsertOf(id,text);
			if (!ht.containsKey(id)){
				ht.put(id, new Hashtable());
			}
			((Hashtable)ht.get(id)).put(lang, text);
		} catch (SQLException e1) {
			System.out.println("ERROR INS:"+id+","+text);
		} catch (MerliDBException e2) {
			System.out.println("ERROR INS:"+id+","+text);
		}
	}
	

	private void parseRelationshipOf(Element e, String lang){
		String ids="";
		String idt="";
		Iterator itr = null;
		Element et;
		Element eaux;
		List list = e.getChildren();
		List list2;
		if (!ht.containsKey("rel")){
			ht.put("rel", new Hashtable());
		}
		for (int i=0;i<list.size();i++){
			//StructureID de l'element.
			et= (Element) list.get(i);
			if(et.getName().equals("sourceTerm")){
				ids=et.getValue();
			}
			if(et.getName().equals("targetTerm")){
				idt=et.getValue();
			}
			if(et.getName().equals("term")){
				parseTermsOf(et, lang);
			}
			if (et.getName().compareTo("relationship") == 0)	{
				parseRelationshipOf(e, lang);
			}
		}
		try {
			executeInsertOfRel(ids,idt);
			if (!((Hashtable)ht.get("rel")).containsKey(idt)){
				((Hashtable)ht.get("rel")).put(idt, ids);
			}
			
		} catch (SQLException e1) {
			System.out.println("ERROR INS:"+ids+","+idt);
		} catch (MerliDBException e2) {
			System.out.println("ERROR INS:"+ids+","+idt);
		}
	}

	private void parseIds(String id){
		boolean found = false;
		int pas = 0;
		String auxRel = "";
		String print = "";
		String rels = "";
		found = alreadyExists(id);
		if (!ht.containsKey(id) || found){
			return;
		}
		print = printNewTermSQL(id, found);
		if (ht.containsKey("rel"))
		while (!found && pas < 5){
			try{
				if (ht.containsKey(id)){
					auxRel = (String)((Hashtable) ht.get("rel")).get(id);
					found = alreadyExists(auxRel);
					if (!found){
						if (auxRel != null && auxRel.indexOf("M")>=0){
							found = true;
							auxRel = auxRel.replaceAll("M", "");
							if (auxRel.indexOf(".")>0){
								auxRel = auxRel.replaceAll(".", "0");
							}else{
								auxRel += "000";
							}
						}
						print += printNewTermSQL(auxRel, found);
					}
					rels += printNewRelSQL(id, found, auxRel);
					pas++;
					id = auxRel;
				}else{pas = 5;}
			}catch (Exception e){
				found = true;
			}
		}
		if (found){
			System.out.println(print);
			System.out.println(rels);
		}
	}


	private String printNewRelSQL(String id, boolean found, String auxRel) {
		String rels = "INSERT INTO THE_TERME_REL (ID_TERME, ID_TERME_REL, TIPUS_RELACIO) VALUES (";
		rels += id;
		rels += ",";
		rels += auxRel;
		if (!found){
			rels += ", 'RT'";
		}else{
			rels += ", 'MT'";
		}
		rels += ");\n";
		return rels;
	}
		

	private boolean alreadyExists(String id) {

		int numRec;
		if ("41".equals(id) || "321".equals(id)){
			System.out.println("-- wasap");
		}
		if (((Hashtable)ht.get("new")).containsKey(id)){
			return true;
		}else{
			((Hashtable)ht.get("new")).put(id, "true");
		}
		ArrayList lparam = new ArrayList();
		try {
			lparam.add(id);
			numRec = AccesBD.executeCount("the_termes","id_terme = ?",lparam,con);
			
		}catch(Exception e){
		numRec=0;	
		}finally{
		}
		return numRec>0;
	}

	private String printNewTermSQL(String id, boolean found) {
		String print = "INSERT INTO THE_TERMES (id_terme, terme_ca, terme_es, terme_en, " +
		"terme_pla_ca, terme_pla_es, terme_pla_en, " +
		"id_elr, categoria) VALUES (";
			print += id;
			print += ",'";
			print += ((String)((Hashtable)ht.get(id)).get("ca")).replaceAll("'", "''");
			print += "','";
			print += ((String)((Hashtable)ht.get(id)).get("es")).replaceAll("'", "''");
			print += "','";
			print += ((String)((Hashtable)ht.get(id)).get("en")).replaceAll("'", "''");
			print += "','";
			print += Utility.aplanarText((String)((Hashtable)ht.get(id)).get("ca"));
			print += "','";
			print += Utility.aplanarText((String)((Hashtable)ht.get(id)).get("es"));
			print += "','";
			print += Utility.aplanarText((String)((Hashtable)ht.get(id)).get("en"));

			print += "',"+id;
			if (!found){
				print += ", 'AC'";
			}else{
				print += ", 'MT'";
			}
			print += ");\n";
		return print;
	}
	

	
	private void executeInsertOf(String id, String text) throws SQLException, MerliDBException {
//		List alis = new ArrayList();
//		alis.add(id);
//		alis.add(text);
//
//		AccesBD.executeInsert("CUR_ARC_TERMES", alis, con);
		//System.out.println("INSERT INTO THE_ALL_TERMES VALUES ('"+id+"','"+text.replaceAll("'", "''")+"');");
	}

	private void executeInsertOfRel(String ids, String idt) throws SQLException, MerliDBException {
		//System.out.println("INSERT INTO THE_ALL_REL_TERMES VALUES ('"+ids+"','"+idt+"');");
	}

	
}
