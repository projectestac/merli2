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
public class ThesaurusLoad {
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
	public ThesaurusLoad(String file) throws MerliDBException, SQLException, ClassNotFoundException{
		try {
			loadXMLdocument(file);					 
			} catch (SemanticException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public ThesaurusLoad() {
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
			SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser", true);
            builder.setFeature("http://xml.org/sax/features/validation", true);        
			builder.setFeature("http://apache.org/xml/features/validation/schema", true);        
			builder.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);	
			
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
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			con = DriverManager.getConnection("jdbc:oracle:thin:@aleixportatil2:1521:XTEC", "acanals5","ac4263");
	        Iterator itr = dDesc.getRootElement().getChildren().iterator();
			int i = 0;
			while (itr.hasNext()) {				
	            Element e = (Element) itr.next();
				if (e.getName().compareTo("termEquivalence") == 0){
		            this.loadTerm(e.getChildren());
					i++;
				}
				if (e.getName().compareTo("structure") == 0){
					con = DriverManager.getConnection("jdbc:oracle:thin:@aleixportatil2:1521:XTEC", "acanals5","ac4263");
			        this.loadStructure(e);
					con.close();
				}
				
	        }
			System.out.println("Total paraulotes:"+i);
			con.commit();
			con.close();
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
	 * Carrega l'estructura (camp Structure del thesaurus) de microthesaurus passada 
	 * com a parametre e2. Carrega les relacions amb els termes i crea un nou terme
	 * a la BBDD amb el nom del microthesaurus.
	 * @param e2 Element XML amb l'estructura d'un microthesaurus.
	 * @throws Exception
	 */
	private void loadStructure(Element e2) throws Exception {
		// TODO Auto-generated method stub
		String structId = null;
		int stId;
		String name_en="";
		String name_ca="";
		String name_es="";
		
		ArrayList al = new ArrayList();
		List lAux,list;
		Element eAux, e = e2;//(Element)list.get(0);
		list = e2.getChildren();
		//StructureID de l'element.
		e= (Element) list.get(0);
		if(e.getName().equals("structureID")){
			structId = e.getValue();
			
			//Nom de l'element.
			e= (Element) list.get(1);
			if (e != null){
				lAux = e.getChildren();
				for (int i =0;i<lAux.size();i++){
					eAux = (Element) lAux.get(i);
					if (eAux.getAttribute("language") != null && eAux.getAttribute("language").getValue().equals("en")){
						name_en = eAux.getValue();
					}
					if (eAux.getAttribute("language") != null && eAux.getAttribute("language").getValue().equals("ca")){
						name_ca = eAux.getValue();
					}
					if (eAux.getAttribute("language") != null && eAux.getAttribute("language").getValue().equals("es")){
						name_es = eAux.getValue();
					}
				}
				logger.info("Crear Structure: "+structId+" "+name_ca);
				try{			
					stId = Integer.parseInt(structId)*1000;		
				}catch(Exception ex){
					stId = Integer.parseInt(structId.substring(0,structId.indexOf(".")));
					stId = stId*1000+Integer.parseInt(structId.substring(structId.indexOf(".")+1,structId.length()));
				}
				structId = String.valueOf(stId);
				al.add(new Integer(stId));		
				al.add(name_en);
				al.add("MT");
				al.add(Utility.aplanarText(name_en));
				al.add(new Integer(stId));	
				al.add(name_ca);	
				al.add(Utility.aplanarText(name_ca));
				al.add(name_es);	
				al.add(Utility.aplanarText(name_es));
				
				try {					
					AccesBD.executeInsert("THE_TERMES","id_terme,terme_en,categoria,terme_pla_en,id_elr,terme_ca,terme_pla_ca,terme_es,terme_pla_es",al, con);
					al.clear();
					al.add(new Integer(0));
					al.add(new Integer(stId));
					al.add("OT");
					AccesBD.executeInsert("THE_TERME_REL","id_terme,id_terme_rel,tipus_relacio",al, con);
				} catch (MerliDBException e1) {
					// TODO Auto-generated catch block
					con.rollback();
					e1.printStackTrace();
					throw e1;				
				}
			}else{
				logger.error("ERROR, forma no vàlida. Falta el name");
				con.rollback();
				throw new SQLException();
			}
		}else{
			logger.error("ERROR, forma no valida. Falta structureID.");
			con.rollback();
			throw new SQLException();
		}

		String sourceId = null;
		String typeRelation = null;
		for (int i = 2; i < list.size(); i++){
			e = (Element) list.get(i);
			if (e.getName().equals("structure")){
				loadStructure(e);
			}
			if (e.getName().equals("setOfRelation")){
				lAux = e.getChildren();
				//if (e.getChild("sourceTermID") != null){
				//eAux = sourceTermID child.
				eAux = (Element) lAux.get(0);
				if ( eAux.getName().equals("sourceTermID")){
					sourceId = eAux.getValue();
					logger.info("ADD relation MT between:"+structId+"-"+sourceId);
					/*Carregar el nom a la taula The_termes..*/
					al.clear();
					al.add(new Integer(structId));
					al.add(new Integer(sourceId));
					al.add("MT");
					try {
						AccesBD.executeInsert("THE_TERME_REL","id_terme,id_terme_rel,tipus_relacio",al, con);
					} catch (MerliDBException e1) {
						// TODO Auto-generated catch block
						con.rollback();
						e1.printStackTrace();
						throw e1;
					}					
				}else{
					logger.error("ERROR, forma no valida. Falta sourceTermID.");
					con.rollback();
					throw new SQLException();
				}
				//eAux = Relation child.
				eAux = (Element) lAux.get(1);
				if (eAux.getName().equals("relation")){
					//eAux = e.getChild("relation");
					lAux = eAux.getChildren();
					//eAux = typeOfRelation child.
					eAux =(Element) lAux.get(0);
					if (eAux.getName().equals("typeOfRelation")){
						typeRelation = eAux.getValue();
					}
					//lAux = eAux.getChildren();
					for (int j=1;j<lAux.size();j++){
						eAux = (Element) lAux.get(j);
						if (eAux.getName().equals("targetTermID")){
							logger.info("ADD relation "+typeRelation+" between:"+sourceId+"-"+eAux.getValue());
							al.clear();
							al.add(new Integer(sourceId));
							al.add(new Integer((String)eAux.getValue()));
							al.add(typeRelation);
							try {
								AccesBD.executeInsert("THE_TERME_REL","id_terme,id_terme_rel,tipus_relacio",al, con);
							} catch (MerliDBException e1) {
								// TODO Auto-generated catch block
								con.rollback();
								e1.printStackTrace();
								throw e1;
							}
						}
					}
				}else{
					logger.error("ERROR, forma no valida. Falta relation.");
					con.rollback();
					throw new SQLException();
				}
				
			}
		}
	}

	
	/**
	 * Carrega el terme donat com a parametre en els 3 idiomes del MeRLí, en,ca i es.
	 * @param list
	 * @throws Exception
	 */
	private void loadTerm(List list) throws Exception{
		// TODO Auto-generated method stub
		String termId = null;
		String termType = null;
		String name_en = "";
		String name_ca = "";
		String name_es = "";
		

		List lAux;
				
		Element eAux, e;
		// e = termID.
		e = (Element) list.get(0);
		if (e.getName().equals("termID"))
			termId = e.getValue();
		//e = typeOfTerm.
		e = (Element) list.get(1);
		ArrayList al = new ArrayList();
		termType = e.getValue();
		for (int i =0; i <list.size(); i++){
			e = (Element)list.get(i);
			if (e.getAttribute("language") != null && e.getAttribute("language").getValue().equals("en")){
				name_en = (String)((Element)e.getChildren().get(0)).getValue();	
			}		
			if (e.getAttribute("language") != null && e.getAttribute("language").getValue().equals("ca")){
				name_ca = (String)((Element)e.getChildren().get(0)).getValue();	
			}	
			if (e.getAttribute("language") != null && e.getAttribute("language").getValue().equals("es")){
				name_es = (String)((Element)e.getChildren().get(0)).getValue();	
			}
				
		}						
		logger.info("Insert new term id:"+termId+" "+name_ca);
		try{
			al.add(new Integer(termId));
			al.add(name_en);
			al.add(termType);
			al.add(Utility.aplanarText(name_en));
			al.add(new Integer(termId));
			al.add(name_ca);
			al.add(Utility.aplanarText(name_ca));
			al.add(name_es);
			al.add(Utility.aplanarText(name_es));
			
			try {					
				AccesBD.executeInsert("THE_TERMES","id_terme,terme_en,categoria,terme_pla_en,id_elr,terme_ca,terme_pla_ca,terme_es,terme_pla_es",al, con);
			} catch (MerliDBException e1) {
				// TODO Auto-generated catch block
				con.rollback();
				e1.printStackTrace();
				throw e1;			
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				con.rollback();
				e2.printStackTrace();
				throw e2;
			}
		}catch(NumberFormatException e1){
			e1.printStackTrace();			
		}
	}
	
	
	/**
	 * Executa la conversio del les relacions Recurs-paraules_clau a Recurs-termes
	 * @return Llistat del termes que no han trobat equivalència en el nou thesaurus.
	 * @throws SQLException
	 * @throws MerliDBException
	 */
	public ArrayList convertir() throws SQLException, MerliDBException{
		ArrayList al = new ArrayList();
		ArrayList alis = new ArrayList();
		ArrayList lCond = new ArrayList();
		
		int idPar, idRec, idTerme;
		String idTer, query;
		
		try {
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			con = DriverManager.getConnection("jdbc:oracle:thin:@aleixportatil2:1521:XTEC", "acanals5","ac4263");
			alis.add("id_rec");
			alis.add("id_paraula");
			
			Map m= AccesBD.getFullLlistat("MER_REC_PARAULES_CLAU",alis,"id_paraula",con);
			for (int i = 0;i<((ArrayList)m.get("id_rec")).size();i++){
				idPar = ((BigDecimal)((ArrayList)m.get("id_paraula")).get(i)).intValue(); 
				idRec = ((BigDecimal)((ArrayList)m.get("id_rec")).get(i)).intValue();
				query = "Select * from par_clau_cat pc, the_termes t  where pc.id_paraula = ? AND t.terme_ca LIKE pc.paraula";
				lCond.clear();
				lCond.add(new Integer(idPar));
				try{
					idTer = AccesBD.executeQuery(query,lCond,"id_terme",con);
					
					lCond.clear();
					lCond.add(new Integer(idRec));
					lCond.add(new Integer(idTer));
					lCond.add("elr");
					AccesBD.executeInsert("MER_REC_TERMES", lCond, con);
				}catch (Exception e){
					al.add(new Integer(idPar));
					logger.error("Error, paraula amb id:"+idPar+" no trobada.");
				}
				
			}
			
			con.commit();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (MerliDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
        
		
		return al;		
	}
	
	/**
	 * Executa la conversio del les relacions Elements curriculum-paraules_clau a 
	 * curriculum-termes
	 * @return Llistat del termes que no han trobat equivalència en el nou thesaurus.
	 * @throws SQLException
	 * @throws MerliDBException
	 */
	public ArrayList convertirRelCurriculum() throws SQLException, MerliDBException{
		ArrayList al = new ArrayList();
		ArrayList alis = new ArrayList();
		ArrayList lCond = new ArrayList();
		
		int idPar, idCont, idTerme;
		String idTer, query, type;
		Hashtable cacheWords = new Hashtable();
		
		try {
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			con = DriverManager.getConnection("jdbc:oracle:thin:@aleixportatil2:1521:XTEC", "acanals5","ac4263");
			alis.add("id_node");
			alis.add("v_type");
			alis.add("id_paraula");
			
			Map m= AccesBD.getFullLlistat("CUR_THESAURUS",alis,"id_paraula",con);
			for (int i = 0;i<((ArrayList)m.get("id_node")).size();i++){
				idPar = ((BigDecimal)((ArrayList)m.get("id_paraula")).get(i)).intValue(); 
				idCont = ((BigDecimal)((ArrayList)m.get("id_node")).get(i)).intValue();
				type = (String)((ArrayList)m.get("v_type")).get(i);
				try{
					if (cacheWords.containsKey(new Integer(idPar))){
						idTer = (String)cacheWords.get(new Integer(idPar));
					}else{
						query = "Select * from par_clau_cat pc, the_termes t  where pc.id_paraula = ? AND t.terme_ca LIKE pc.paraula";
						lCond.clear();
						lCond.add(new Integer(idPar));
						idTer = AccesBD.executeQuery(query,lCond,"id_terme",con);
						cacheWords.put(new Integer(idPar),idTer);
					}
					
					lCond.clear();
					lCond.add(new Integer(idCont));
					lCond.add(type);
					lCond.add(new Integer(idTer));
					AccesBD.executeInsert("CUR_THESAURUS_ETB", lCond, con);
					logger.info("Paraula migarda correctament. origen:"+idPar+"->"+idTer+"pel node: "+idCont);
				}catch (Exception e){
					al.add(new Integer(idPar));
					logger.error("Error, paraula amb id:"+idPar+" no trobada.");
				}			
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (MerliDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} finally{
			con.commit();
			con.close();			
		}
   		return al;		
	}

	
}
