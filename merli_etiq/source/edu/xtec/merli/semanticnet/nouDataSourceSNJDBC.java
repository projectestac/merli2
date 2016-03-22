package edu.xtec.merli.semanticnet;


import edu.xtec.merli.basedades.AccesBD;
import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.utils.Utility;
import edu.xtec.semanticnet.DataSource;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticException;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.*;

public class nouDataSourceSNJDBC implements DataSource{

	private static final Logger logger = Logger.getRootLogger();//("xtec.duc");
	
	protected static ConnectionBeanProvider broker;	
	private Hashtable hProperties;
	
	private int num = 0;
		
	public void init(Hashtable propietats) {
		this.hProperties = propietats;		
		Hashtable h;
		ConnectionBean cb = null;
		try {
			cb = this.connectBD();
			try {
				if (AccesBD.executeExist("cur_level","id_node","0","",cb.getConnection())==0){
					AccesBD.executeInsert("cur_level","id_node,v_term,v_category,id_nodecur_level","0,'origen','origen',0",cb.getConnection());	
				}
				if (AccesBD.executeExist("cur_content","id_node","0","",cb.getConnection())==0){
					AccesBD.executeInsert("cur_content","id_node,v_term,v_category","0,'origen','origen'",cb.getConnection());	
				}
			} catch (MerliDBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		} catch (SemanticException e) {
			logger.error("Error crate a connection to DB: "+ e.getMessage());
			e.printStackTrace();
		}finally{
			this.disconnectBD(cb);
		}
	}
	
		/**
		 * 
		 */
		public Hashtable getNode(int idNode, String nodeType) throws SemanticException {
			// TODO Auto-generated method stub
			Hashtable dto = new Hashtable();
			Hashtable properties = new Hashtable();
			ConnectionBean cb = null;	
			List l, lorder = new ArrayList();
			l = this.getAttributesNodeType(nodeType,lorder);
			String order = Utility.toParaula(lorder.toString());
			String id_node = "id_node";
			if (nodeType.compareTo("history")==0) id_node = "id_history";
			if (nodeType.compareTo("notes")==0) id_node = "id_note";
			try {
				cb = this.connectBD();
				Map m =  AccesBD.getObject("cur_"+nodeType,l,id_node+" = "+idNode,cb.getConnection());
				dto = (Hashtable)mapToNodesDTO(m, nodeType).get(0);				
			} catch (Exception e) {
				logger.warn("The node "+idNode+nodeType+" doesn't exists.");
				SemanticException the = new SemanticException("NonExistentNode");
				the.setCode(SemanticException.NONEXISTENTNODE);
				the.setStackTrace(e.getStackTrace());
			}finally{
				this.disconnectBD(cb);
			}
			return dto;
		}
		
		private Hashtable getNotes(int idNode, String nodeType) throws SemanticException {
//			 TODO Auto-generated method stub
			Hashtable dto = new Hashtable();
			Hashtable note = new Hashtable();
			ConnectionBean cb = null;
			List l = new ArrayList(); 
			List lorder = new ArrayList();
			l = this.getAttributesNodeType("notes",lorder);
			
			try {
				cb = this.connectBD();
				Map m =  AccesBD.getObjectList("cur_notes",l,"id_node = "+idNode+" AND v_type='"+nodeType+"'"," d_contribution DESC ",cb.getConnection());
				dto.put("idNode",new Integer(idNode));
				dto.put("nodeType",nodeType);
				dto.put("list",new ArrayList());
				
				for (int i = 0;i<((List)m.get("id_note")).size();i++){
					note = new Hashtable();
					note.put("idNote", new Integer(Utility.toParaula(((List)m.get("id_note")).get(i).toString())));					
					if (m.containsKey("v_note")){
						if (((List)m.get("v_note")).get(i)!= null)
							note.put("note",((List)m.get("v_note")).get(i));
					}
					if (m.containsKey("v_user")){
						if (((List)m.get("v_user")).get(i)!= null)
							note.put("user",((List)m.get("v_user")).get(i));
					}
					if (m.containsKey("d_contribution")){
						if (((List)m.get("d_contribution")).get(i)!= null)
							note.put("date",((List)m.get("d_contribution")).get(i));
					}
					((ArrayList) dto.get("list")).add(note);
				}
			} catch (Exception e) {
				logger.warn("The note "+idNode+nodeType+" doesn't exists.");
				SemanticException the = new SemanticException("NonExistentNode");
				the.setCode(SemanticException.NONEXISTENTNODE);
				the.setStackTrace(e.getStackTrace());
			}finally{
				this.disconnectBD(cb);
			}
			return dto;
		}

		/**
		 * Retorna el llistat de nodes amb l'identificador a 'lnod' i del típus 'nodeType'.
		 * @param lnod Llistat de identificador dels nodes que es vol carregar.
		 * @param nodeType Típus del nodes que es volen carregar.
		 * @return {nodeDTO} on 'nodesDTO' és un hashtable amb la informació necessaria
		 * @throws SemanticException
		 */
		public List getNodes(List lnod, String nodeType) throws SemanticException {
			// TODO Auto-generated method stub
			Hashtable dto = new Hashtable();
			ConnectionBean cb = null;
			Hashtable properties = new Hashtable();
			Map m = null;
 			String condicio ="(";
			String order = "i_position";
			List lorder = new ArrayList();
			List l;
			List lDTO = new ArrayList();
			/*Camps de tots els nodes*/
			l = getAttributesNodeType(nodeType, lorder);
			
			if (!lnod.isEmpty()){
				for (int i=0;i<lnod.size();i++){
					condicio += ((List) lnod.get(i)).get(0);
					if(i+1<lnod.size()){
						condicio +=",";
					}else{
						condicio +=")";
					}
				}
				condicio = "id_node in "+condicio;
			}else{
				/*Recupera tots els nodes del típus donat.*/
				condicio = " ";
			}
			order = Utility.toParaula(lorder.toString());
			try {
				cb = this.connectBD();
				m =  AccesBD.getObjectList("cur_"+nodeType,l,condicio,order,cb.getConnection());
				lDTO = this.mapToNodesDTO(m, nodeType);
			} catch (Exception e) {
				logger.warn("Error loading list of nodes "+nodeType+".");
				SemanticException the = new SemanticException("NonExistentNode");
				the.setCode(SemanticException.NONEXISTENTNODE);
				the.setStackTrace(e.getStackTrace());
			}finally{
				this.disconnectBD(cb);
			}
			return lDTO;
		}
		
		

		/**
		 * 
		 */
		public Hashtable getNodes(Hashtable hnod) throws SemanticException{
			// TODO Auto-generated method stub
			Hashtable hRes = new Hashtable();
			ArrayList l;
			String type;
			Enumeration en = hnod.keys();
			
			while (en.hasMoreElements()){
				type = (String)en.nextElement();
				l = (ArrayList) this.getNodes((List) hnod.get(type), type);
				if (l!=null){
					hRes.put(type,l);
				}
			}
			
			return hRes;
		}

		/**
		 * 
		 */
		public Hashtable getAllNodes(List lNodeType) throws SemanticException {
			Hashtable hRes = new Hashtable();
			ArrayList l;
			String type;
			ListIterator it = lNodeType.listIterator();
			
			while (it.hasNext()){
				type = (String)it.next();
				l = (ArrayList) this.getNodes(new ArrayList(), type);
				if (l!=null){
					hRes.put(type,l);
				}
			}
			
			return hRes;
		}

		
		
		/**
		 * Retorna un llistat amb totes les relacions del tipus 'type' an la direccio 'direction'
		 * del node 'idnode' del típus 'nodeType'
		 * @param idNode Identificador del node.
		 * @param nodeType Típus del node 'idNode'.
		 * @param type Típus de la relació.
		 * @param direction Direcció de les relacions buscades.
		 * @return Retorna un llistat {dtonode} on 'dtonode' és un hashtable amb la informació 
		 * del node. 
		 * @throws SemanticException
		 */
		public List getRelations(int idNode, String nodeType , String type, int direction) throws SemanticException {
			String condi, sType = "", dType = "", sIdNode = "id_node",  idNodeDest = "";
			String table = null;
			List l = new ArrayList();
			List lRes = new ArrayList();
			
			Hashtable hConf = configRelationQuery(idNode, nodeType, type, direction);
			table = (String) hConf.get("table");
			condi = (String) hConf.get("condition");
			l = (List) hConf.get("elements");
			sType = (String) hConf.get("sourceType");
			dType = (String) hConf.get("destType");
			ConnectionBean cb = null;
			try {
				cb= this.connectBD();
				Map m = AccesBD.getObjectList(table,l,"("+condi+")","", cb.getConnection());
				this.disconnectBD(cb);
				List l1, l2, l3 = null;
				l1 = (List) m.get(l.get(0));
				l2 = (List) m.get(l.get(1));
				if (l.size() > 2) l3 = (List) m.get(l.get(2));
				for (int i=0;i< l1.size();i++){
					/*En cas que algun dels camps sigui 'null' es creu que la relació no existeix.
					 * 
					 */
					try{
						Hashtable dto = new Hashtable();
						dto.put("idSource",new Integer(Utility.toParaula(l1.get(i).toString())));
						dto.put("sourceType",sType);
						if (l3 != null){
							dto.put("destType",Utility.toParaula(l3.get(i).toString()));
							if (sType.compareTo("thesaurus")==0){
								dto.put("sourceType",Utility.toParaula(l3.get(i).toString()));
								dto.put("destType",sType);
							}
						}
						else dto.put("destType",dType);
						//logger.info("l2="+l2);
						String sDest = l2.get(i)!=null?l2.get(i).toString():"0";
						dto.put("idDest",new Integer(Utility.toParaula(sDest)));
						dto.put("relationType",type);
						lRes.add(dto);
					}catch(NullPointerException npe){
						logger.warn("Error creating relation -> "+npe);
						npe.printStackTrace();
					}
				}
				
			} catch (SQLException e) {
				logger.warn("Error loading relations of:"+idNode+nodeType+":"+e.getMessage());
			}finally{
				this.disconnectBD(cb);
			}

			return lRes;
		}
		/*
		public List getRelations(int idNode, String nodeType , String type, int direction) throws SemanticException {
			String condi, sType = "", dType = "", sIdNode = "id_node",  idNodeDest = "";
			String table = null;
			List l = new ArrayList();
			List lRes = new ArrayList();
			
			Hashtable hConf = configRelationQuery(idNode, nodeType, type, direction);
			table = (String) hConf.get("table");
			condi = (String) hConf.get("condition");
			l = (List) hConf.get("elements");
			sType = (String) hConf.get("sourceType");
			dType = (String) hConf.get("destType");
			
			try {
				ConnectionBean cb= this.connectBD();
				Map m = AccesBD.getObjectList(table,l,"("+condi+")","", cb.getConnection());
				this.disconnectBD(cb);
				List l1, l2, l3 = null;
				l1 = (List) m.get(l.get(0));
				l2 = (List) m.get(l.get(1));
				if (l.size() > 2) l3 = (List) m.get(l.get(2));
				for (int i=0;i< l1.size();i++){
					/*
					 * En cas que algun dels camps sigui 'null' es creu que la relació no existeix.
					 * /
					try{
						Hashtable dto = new Hashtable();
						dto.put("idSource",new Integer(Utility.toParaula(l1.get(i).toString())));
						dto.put("sourceType",sType);
						if (l3 != null){
							dto.put("destType",Utility.toParaula(l3.get(i).toString()));
							if (sType.compareTo("thesaurus")==0){
								dto.put("sourceType",Utility.toParaula(l3.get(i).toString()));
								dto.put("destType",sType);
							}
						}
						else dto.put("destType",dType);
						dto.put("idDest",new Integer(Utility.toParaula(l2.get(i).toString())));
						dto.put("relationType",type);
						lRes.add(dto);
					}catch(NullPointerException npe){
						logger.warn("Error creating relation:"+npe.getMessage());
					}
				}
				
			} catch (SQLException e) {
				logger.warn("Error loading relations of:"+idNode+nodeType+":"+e.getMessage());
			}

			return lRes;
		}*/
		
		
		/**
		 * 
		 */
		public List getRelations(int idNode,String nodeType , List types, int direction) throws SemanticException {
			// TODO Auto-generated method stub
			String condi;
			String relas;
			List l = new ArrayList();
			
			for (int i =0; i<types.size();i++){
				l.addAll(this.getRelations(idNode,nodeType,(String) types.get(i),direction));
			}
	
			return l;
		}
		
		
		/**
		 * 
		 */
		public Hashtable getAllRelations(List types) throws SemanticException {
			
			String condi;
			String relas;
			Hashtable hRel = new Hashtable();
			
			for (int i =0; i<types.size();i++){
				hRel.put(types.get(i), this.getRelations(-1,"",(String) types.get(i),0));
			}
			return hRel;
		}


		
		
		

		/**
		 * Sol·licita un ConnectionBean al ConnectionBeanProvider, si aquest no està innicialitzat, ho fa.
		 * @return ConnectionBean, servit pel ConnectionBeanProvider.
		 * @throws ThesaurusException 
		 */
		private ConnectionBean connectBD() throws SemanticException{
			//Inicialitza el CBP si no ho està.
			ConnectionBean bd = null;	
			Hashtable propDoc;
			//String folderProp = "../../../../";
			String folderProp = "/";
			String pathProp = "database.properties";
			if (hProperties.containsKey("properties")){
				propDoc = (Hashtable) hProperties.get("properties");
				if (propDoc.containsKey("propertiesDocument.folder")){
					folderProp = (String) propDoc.get("propertiesDocument.folder");
				}
				if (propDoc.containsKey("propertiesDocument.path")){
					pathProp = (String) propDoc.get("propertiesDocument.path");
				}
			}				
			try{
				if (broker == null){
					broker = ConnectionBeanProvider.getConnectionBeanProvider(true, Utility.loadProperties(folderProp,pathProp));		
				}
				bd = broker.getConnectionBean();
			}catch(Exception e)
				{
				logger.warn("Error creating connection:"+e.getMessage());
				e.printStackTrace();
				SemanticException the = new SemanticException("DataBaseConnectionError");
				the.setCode(SemanticException.DATABASECONNECTIONERROR);
				the.setStackTrace(e.getStackTrace());
				throw the;
				}//throw new Exception("Error en la connexió amb la base de dades.");}		
			//Retorna un ConnectionBean.
			return bd;
		}
 
		/**
		 * Es desconnecta de la BD.
		 */
		private void disconnectBD(ConnectionBean cb){
			//Allibera el connectionBean utilitzat.
			try{
			broker.freeConnectionBean(cb);
			}catch(Exception e){
				logger.warn("Error disconnecting connection."+e.getMessage());}//missError ="error.desconnexio.bd";}
		}

		
		/**
		 * converteix un dto en un String per executar una operació de 'insert' o de 'update'
		 * segons l'indicat a 'insert'.
		 * @param l Llista de camps possibles.
		 * @param dto Dades del node a insertar/modificar
		 * @param insert TRUE cadena per relaitzar un 'insert', FALSE per realitzar un 'update'.
		 * @return Caden a punt per formar part d'una instrucció SQL de 'insert' o de 'update'
		 * @throws Exception
		 */
		private String dto2string(List l, Hashtable dto, boolean insert) throws Exception{
			String qery = "";
			for (int i =0;i<l.size();i++ ){
				if (((String) l.get(i)).compareTo("id_node") == 0){
					if (!insert) qery += " id_node=";
					if (dto.containsKey("idNode")) qery+=dto.get("idNode")+", ";
					else throw new Exception("id_node");
				}
				if (((String) l.get(i)).compareTo("id_history") == 0){
					if (!insert) qery += " id_history=";
					if (dto.containsKey("idHistory")) qery+=dto.get("idHistory")+", ";
					else throw new Exception("id_history");
				}
				if (((String) l.get(i)).compareTo("v_term") == 0){
					if (!insert) qery += " v_term=";
					if (dto.containsKey("term")) qery+="'"+Utility.toParaulaDB((String) dto.get("term"))+"'"+", ";
					else qery += "'nou node', ";
				}
				if (((String) l.get(i)).compareTo("v_category") == 0){
					if (!insert) qery += " v_category=";
					if (dto.containsKey("category")) qery+="'"+dto.get("category")+"'"+", ";
					else qery += "'p', ";
				}
				if (((String) l.get(i)).compareTo("v_note") == 0){
					if (!insert) qery += " v_note=";
					if (dto.containsKey("description")) qery+="'"+Utility.toParaulaDB((String) dto.get("description"))+"'"+", ";
					else qery += "'', ";
				}
				if (((String) l.get(i)).compareTo("i_position") == 0){
					if (!insert) qery += " i_position=";
					if (((Hashtable) dto.get("properties")).containsKey("position")) qery+=((Hashtable) dto.get("properties")).get("position")+", ";
					else qery +=99+", ";
				}
				if (((String) l.get(i)).compareTo("v_description") == 0){
					if (!insert) qery += " v_description=";
					if (dto.containsKey("description")) qery+="'"+Utility.toParaulaDB((String) dto.get("description"))+"'"+", ";
					else qery += "'', ";
				}
				if (((String) l.get(i)).compareTo("v_references") == 0){
					if (!insert) qery += " v_references=";
					if (((Hashtable) dto.get("properties")).containsKey("references")) qery+="'"+Utility.toParaulaDB((String)((Hashtable) dto.get("properties")).get("references"))+"', ";
					else qery +="'', ";
				}
				if (((String) l.get(i)).compareTo("v_type") == 0){
					if (!insert) qery += " v_type=";
					if (dto.containsKey("nodeType")) qery+="'"+(String)dto.get("nodeType")+"', ";
					else qery +="'notype', ";
				}
				if (((String) l.get(i)).compareTo("v_user") == 0){
					if (!insert) qery += " v_user=";
					if (((Hashtable) dto.get("properties")).containsKey("user")) qery+="'"+(String)((Hashtable) dto.get("properties")).get("user")+"', ";
					else qery +="'', ";
				}
				if (((String) l.get(i)).compareTo("d_contribution") == 0){
					if (!insert) qery += " d_contribution=";
					if (((Hashtable) dto.get("properties")).containsKey("date")) qery+="'"+(String)((Hashtable) dto.get("properties")).get("date")+"', ";
					else qery +="SYSDATE, ";					
				}
				if (((String) l.get(i)).compareTo("d_node") == 0){
					if (!insert) qery += " d_node=";
					if (((Hashtable) dto.get("properties")).containsKey("date")) qery+="'"+(String)((Hashtable) dto.get("properties")).get("date")+"', ";
					else qery +="SYSDATE, ";					
				}
				if (((String) l.get(i)).compareTo("id_nodecur_level") == 0){
					if (!insert) qery += " id_nodecur_level=";
					if (((Hashtable) dto.get("properties")).containsKey("relatedLevel")) qery+=((Hashtable) dto.get("properties")).get("relatedLevel")+", ";
					else qery +=0+", ";
				}
				if (((String) l.get(i)).compareTo("id_nodecur_content") == 0){
					if (!insert) qery += " id_nodecur_content=";
					if (((Hashtable) dto.get("properties")).containsKey("relatedContent")) qery+=((Hashtable) dto.get("properties")).get("relatedContent")+", ";
					else qery +=0+", ";
				}
				if (((String) l.get(i)).compareTo("v_term_es") == 0){
					if (!insert) qery += " v_term_es=";
					if (((Hashtable) dto.get("properties")).containsKey("v_term_es")) qery+="'"+((Hashtable) dto.get("properties")).get("v_term_es")+"', ";
					else qery +="'', ";
				}
				if (((String) l.get(i)).compareTo("v_term_en") == 0){
					if (!insert) qery += " v_term_en=";
					if (((Hashtable) dto.get("properties")).containsKey("v_term_en")) qery+="'"+((Hashtable) dto.get("properties")).get("v_term_en")+"', ";
					else qery +="'', ";
				}
				if (((String) l.get(i)).compareTo("v_term_oc") == 0){
					if (!insert) qery += " v_term_oc=";
					if (((Hashtable) dto.get("properties")).containsKey("v_term_oc")) qery+="'"+((Hashtable) dto.get("properties")).get("v_term_oc")+"', ";
					else qery +="'', ";
				}
				if (i == l.size()-1){
					qery = (String) qery.subSequence(0, qery.lastIndexOf(", "));
				}
			}
			logger.info("String created correctly.");
			return qery;
		}
		
		/**
		 * 
		 */
		public int addNewNode(Hashtable DTO) throws SemanticException {
			// TODO Auto-generated method stub
			ConnectionBean cb = null;
			String sNType;
			String insert= "";
			String camps= "";
			List l, lorder = new ArrayList();
			int res, idNode;
			sNType = (String) DTO.get("nodeType");
			l = this.getAttributesNodeType(sNType,lorder);
			try{
				cb = this.connectBD();
				//Recupera el següent valor d'identificador per insertar a la taula.
				idNode = AccesBD.getNext("cur_nodes", cb.getConnection());
				if (((String)DTO.get("nodeType")).compareTo("history") == 0) 
					DTO.put("idHistory",new Integer(idNode));
				else DTO.put("idNode",new Integer(idNode));
				logger.info("New id gotten from DDBB.");
			}catch (SQLException e) {
				logger.warn("Error on getting new id from DDBB:"+e.getMessage());
				throw new SemanticException("");
			}finally{
				this.disconnectBD(cb);
			}

			try{
				insert = this.dto2string(l,DTO,true);
			}catch(Exception e){}
			camps = Utility.toParaulaDB((String)Utility.toParaula(l.toString()));
			try {
				cb = this.connectBD();
				//Inserta el node a la BBDD.
				res = AccesBD.executeInsert("cur_"+sNType,camps,insert,cb.getConnection());
				//Inserta un nou node a la taula de notes.
				if (DTO.containsKey("note")){
					String notenode;
					try {
						if (DTO.get("note") != null && ((String)DTO.get("note")).length() > 2){
							//Recupera el següent valor d'identificador per insertar a la taula notes.
							int idNote = AccesBD.getNext("seq_notes", cb.getConnection());
							notenode = idNote+","+DTO.get("idNode")+", '"+DTO.get("nodeType")+"', '";
							notenode += Utility.toParaulaDB((String)(String)DTO.get("note"))+"', ";
							//Usuari validat.
							notenode += "'"+((Hashtable) DTO.get("properties")).get("user")+"', ";
							notenode += "SYSDATE";							
							res = AccesBD.executeInsert("cur_notes",notenode,cb.getConnection());
							logger.info("New node added on DDBB as a:"+sNType);
						}
					} catch (Exception e) {
						logger.warn("Error while inserting a new node in DDBB:"+e.getMessage());
					}
				}
//				Inserta les noves relacions amb thesaurus keys.
			/*	if (((Hashtable) DTO.get("properties")).containsKey("thesaurusKeys")){
					String insThesaurus;
					ArrayList thes = (ArrayList) ((Hashtable) DTO.get("properties")).get("thesaurusKeys");
					try {						
						for (int j=0;j<thes.size();j++){
							//Recupera el següent valor d'identificador per insertar a la taula notes.
							insThesaurus = DTO.get("idNode")+", '"+DTO.get("nodeType")+"', "+thes.get(j);									
							res = AccesBD.executeInsert("cur_thesaurus",insThesaurus,cb.getConnection());
						}
					} catch (Exception e) {
						this.disconnectBD(cb);
					}
				}*/
				
			} catch (Exception e) {
				logger.warn("Error while adding a new node:"+e.getMessage());
				throw new SemanticException("");
			}
			finally{
				this.disconnectBD(cb);
			}
			return idNode;
		}

		
		
		/**
		 * 
		 */
		public void setNode(int idNode, String sNType, Hashtable dto) throws SemanticException  {
			// TODO Auto-generated method stub
			ConnectionBean cb = null;	
			String upd = "";
			Hashtable hConf;
			List l, lorder = new ArrayList();
			int res;
			l = this.getAttributesNodeType(sNType,lorder);
			for (int i =0;i<l.size();i++ ){
				if (((String) l.get(i)).compareTo("v_term") == 0){
					if (dto.containsKey("term"))
					upd += " v_term='"+Utility.toParaulaDB((String)dto.get("term"))+"'"+", ";
				}
				if (((String) l.get(i)).compareTo("v_category") == 0){
					if (dto.containsKey("category"))
					upd += " v_category='"+dto.get("category")+"'"+", ";
				}
				if (((String) l.get(i)).compareTo("v_note") == 0){
					if (dto.containsKey("description"))
					upd += " v_note='"+Utility.toParaulaDB((String)dto.get("description"))+"'"+", ";
				}
				if (((String) l.get(i)).compareTo("i_position") == 0){
					if (((Hashtable) dto.get("properties")).containsKey("position"))
						try{
							int ipos = ((Integer)((Hashtable) dto.get("properties")).get("position")).intValue();
							upd += " i_position="+((Hashtable) dto.get("properties")).get("position")+", ";
						}catch(Exception e){}			
				}
				if (((String) l.get(i)).compareTo("v_description") == 0){
					if (dto.containsKey("description"))
					upd += " v_description='"+Utility.toParaulaDB((String)dto.get("description"))+"'"+", ";
				}
				if (((String) l.get(i)).compareTo("v_references") == 0){
					if (((Hashtable) dto.get("properties")).containsKey("references"))
					upd += " v_references='"+Utility.toParaulaDB((String)((Hashtable) dto.get("properties")).get("references"))+"'"+", ";
				}
				if (((String) l.get(i)).compareTo("v_type") == 0){
					if (((Hashtable) dto.get("properties")).containsKey("type"))
					upd += " v_type='"+((Hashtable) dto.get("properties")).get("type")+"'"+", ";
				}
				if (((String) l.get(i)).compareTo("v_user") == 0){
					if (((Hashtable) dto.get("properties")).containsKey("user"))
					upd += " v_user='"+((Hashtable) dto.get("properties")).get("user")+"'"+", ";
				}
				if (((String) l.get(i)).compareTo("d_contribution") == 0){
					if (((Hashtable) dto.get("properties")).containsKey("date"))
					upd += " d_contribution='"+((Hashtable) dto.get("properties")).get("date")+"'"+", ";					
				}
				if (((String) l.get(i)).compareTo("d_date") == 0){
					if (((Hashtable) dto.get("properties")).containsKey("date"))
					upd += " d_date='"+((Hashtable) dto.get("properties")).get("date")+"'"+", ";					
				}
				if (((String) l.get(i)).compareTo("id_nodecur_level") == 0){
					if (((Hashtable) dto.get("properties")).containsKey("relatedLevel"))
						try{
							int ipos = ((Integer)((Hashtable) dto.get("properties")).get("relatedLevel")).intValue();
							upd += " id_nodecur_level="+((Hashtable) dto.get("properties")).get("relatedLevel")+", ";
						}catch(Exception e){}						
				}
				if (((String) l.get(i)).compareTo("id_nodecur_content") == 0){
					if (((Hashtable) dto.get("properties")).containsKey("relatedContent"))
						try{
							int ipos = ((Integer)((Hashtable) dto.get("properties")).get("relatedContent")).intValue();
							upd += " id_nodecur_content="+((Hashtable) dto.get("properties")).get("relatedContent")+", ";
						}catch(Exception e){}						
				}
				if (((String) l.get(i)).compareTo("v_term_es") == 0){
					if (((Hashtable) dto.get("properties")).containsKey("v_term_es"))
						try{
							upd += " v_term_es='"+((Hashtable) dto.get("properties")).get("v_term_es")+"', ";
						}catch(Exception e){}			
				}
				if (((String) l.get(i)).compareTo("v_term_en") == 0){
					if (((Hashtable) dto.get("properties")).containsKey("v_term_en"))
						try{
							upd += " v_term_en='"+((Hashtable) dto.get("properties")).get("v_term_en")+"', ";
						}catch(Exception e){}			
				}
				if (((String) l.get(i)).compareTo("v_term_oc") == 0){
					if (((Hashtable) dto.get("properties")).containsKey("v_term_oc"))
						try{
							upd += " v_term_oc='"+((Hashtable) dto.get("properties")).get("v_term_oc")+"', ";
						}catch(Exception e){}			
				}
				if (i == l.size()-1){
					upd = (String) upd.subSequence(0, upd.lastIndexOf(", "));
				}
			}
			
				try {
					cb = this.connectBD();
					res =  AccesBD.executeUpdate("cur_"+sNType,"id_node = "+idNode,upd,cb.getConnection());
					logger.info("Node modified correctly");
					if (res <0 ){
						SemanticException seE = new SemanticException("NodeNotUpdated");
						throw seE;
					}else{
						/*Guardant els valors anteriors a l'historial.*/
						hConf = this.getNode(idNode,sNType);
						hConf.put("nodeType","history");
						this.addNewNode(hConf);
						try {
							if (dto.get("note") != null && ((String)dto.get("note")).length() > 2){
								//Recupera el següent valor d'identificador per insertar a la taula notes.
								int idNote = AccesBD.getNext("seq_notes", cb.getConnection());
								String notenode = idNote+","+idNode+", '"+sNType+"', '";
								notenode += (String)dto.get("note")+"', ";
								//Usuari validat.
								notenode += "'"+((Hashtable) dto.get("properties")).get("user")+"', ";
								notenode += "SYSDATE";							
								res = AccesBD.executeInsert("cur_notes",notenode,cb.getConnection());
								logger.info("Old node added in history table.");
							}
						} catch (Exception e) {
							logger.warn("Error on add old node in history table"+e.getMessage());
						}
					}
				} catch (Exception e) {
					logger.warn("Error on modify a node:"+e.getMessage());
					// TODO Auto-generated catch block
				}finally{
					this.disconnectBD(cb);
				}
			
		}

		/**
		 * 
		 */
		public void delNode(int idNode, String sNType) throws SemanticException {
			// TODO Auto-generated method stub
			ConnectionBean cb = null;	
			int res;
			Hashtable hConf;
			try {
				cb = this.connectBD();
				hConf = this.getNode(idNode,sNType);
				hConf.put("nodeType","history");
				this.addNewNode(hConf);
				//Esborra el node de la BBDD.
				res = AccesBD.executeDelete("cur_"+sNType, "id_node = "+idNode,cb.getConnection());
				logger.info("Node deleted correctly");				
			}catch(Exception e){
				logger.warn("Error on delete a node"+e.getMessage());
			}finally{
				this.disconnectBD(cb);
			}
		}

		
		/**
		 * 
		 */
		public void addNewRelation(Hashtable dto) throws SemanticException {
			// TODO Auto-generated method stub

			ConnectionBean cb = null;	
			String insert;
			String table;
			int res;
			insert = dto.get("idSource")+" ,"+dto.get("idDest");
			table = "cur_"+(String)dto.get("sourceType")+"_"+(String)dto.get("destType");
			if (((String)dto.get("relationType")).compareTo("RET")==0){
				insert = dto.get("idSource")+",'"+dto.get("sourceType")+"',"+dto.get("idDest");
				table = "cur_thesaurus";
			}
			try {
				cb = this.connectBD();
				//Inserta el node a la BBDD.
				res = AccesBD.executeInsert(table,insert,cb.getConnection());	
				logger.info("New relation added correctly");
			}catch(Exception e){
				logger.warn("Error on add new relation"+e.getMessage());
			}finally{
				this.disconnectBD(cb);
			}
		}

		/**
		 * 
		 */
		public void setRelation(Hashtable dtoOldNode, Hashtable dtoChanges) throws SemanticException {
			// TODO Auto-generated method stub
			
		}

		/**
		 * 
		 */
		public void delRelation(Hashtable dto) throws SemanticException {
			// TODO Auto-generated method stub
			
			ConnectionBean cb = null;	
			int res;
			Hashtable hConf;
			hConf  = this.configRelationQuery(0,"","RET",RelationType.ALL);
			//String condi = "id_paraula="+dto.get("idDest")+" AND v_type='"+dto.get("sourceType")+"' AND id_node="+dto.get("idSource");
			String condi = "id_terme="+dto.get("idDest")+" AND v_type='"+dto.get("sourceType")+"' AND id_node="+dto.get("idSource");
			try{
				cb = this.connectBD();
				res = AccesBD.executeDelete((String) hConf.get("table"),condi,cb.getConnection());
				logger.info("Relation deleted correctly");
			}catch(Exception e){
				logger.warn("Error on delete a node:"+e.getMessage());
			}finally{
				this.disconnectBD(cb);
			}
		}

		

		/**
		 * 
		 */
		public void delRelationsNode(int idNode, String sNType, String sRType) throws SemanticException {
			// TODO Auto-generated method stub
			ConnectionBean cb = null;	
			int res;
			Hashtable hConf;
			hConf  = this.configRelationQuery(idNode,sNType,sRType,RelationType.ALL);
			if (sRType.compareTo("RLL")!=0 && sRType.compareTo("RAL")!=0 && sRType.compareTo("RN")!=0 && sRType.compareTo("RCC")!=0){
				try {
					cb = this.connectBD();
					//Inserta el node a la BBDD.
					res = AccesBD.executeDelete((String) hConf.get("table"),(String)hConf.get("condition"),cb.getConnection());
					logger.info("Relations deleted of node:"+idNode+sNType);
				}catch(Exception e){
					logger.warn("Error on delete relations of node:"+idNode+sNType+" :"+e.getMessage());
				}finally{
					this.disconnectBD(cb);
				}
			}			
			
			
		}


		/**
		 * Retorna un llistat amb tots els atributs d'un node del típus 'nodeType'.
		 * @param nodeType Típus de node.
		 * @param lorder Parametre de sortida. Conté els camps per realitzar una consulta
		 * ordenada
		 * @return Llistat de atributs del node.
		 */
		protected List getAttributesNodeType(String nodeType, List lorder){
			List l = new ArrayList();
			l.add("id_node");
			/*Camps exclusius de 'notes'*/
			if (nodeType.compareTo("notes") == 0){
				lorder.add("d_contribution");
				l.clear();
				l.add("id_note");
				l.add("id_node");
				l.add("v_type");
				l.add("v_note");
				l.add("v_user");
				l.add("d_contribution");
			}else{
				/*Camps generals*/
				l.add("v_term");
				l.add("v_term_es");
				l.add("v_term_en");
				l.add("v_term_oc");
				if (nodeType.compareTo("history") != 0) {
					l.add("i_position"); 
					lorder.add("i_position");
				}else{/*camps especifics de 'history'*/
					l.clear();
					l.add("id_history");
					l.add("d_node");
					l.add("id_node");
					l.add("v_type");
					l.add("v_term");
					lorder.add("d_node");
				}
				l.add("v_description");
				if (nodeType.compareTo("area") != 0) l.add("v_category");
				l.add("v_references");
				if (nodeType.compareTo("area") == 0 || nodeType.compareTo("level") == 0) l.add("id_nodecur_level");
				if (nodeType.compareTo("content") == 0) l.add("id_nodecur_content");
				
			}
			return l;
		}
	
		
		
		
		/**
		 * Converteix un map a dto representatiu d'un node del típus 'nodeType'.
		 * @param m Map resultant d'una consulta SQL al sistema.
		 * @param nodeType Típus de nodes esperats.
		 * @return Llistat de DTO's, un per cada node.
		 */
		protected List mapToNodesDTO(Map m, String nodeType){
			ArrayList lDTO = new ArrayList();
			Hashtable dto;
			Hashtable properties = new Hashtable();
			try{
				for (int i = 0;i<((List)m.get("id_node")).size();i++){
					dto = new Hashtable();
					properties = new Hashtable();
					if (nodeType.compareTo("notes") == 0){
						dto.put("idNode", new Integer(Utility.toParaula(((List)m.get("id_note")).get(i).toString())));
						if (m.containsKey("id_node")) 
							if (((List)m.get("id_node")).get(i)!= null)
							properties.put("idNode",new Integer( Utility.toParaula(((List) m.get("id_node")).get(i).toString())));
					}else{
						dto.put("idNode", new Integer(Utility.toParaula(((List)m.get("id_node")).get(i).toString())));
						try {
							properties.put("notes",this.getNotes(((Integer) dto.get("idNode")).intValue(), nodeType));
							dto.put("note", Utility.notes2note((Hashtable)properties.get("notes")));
						} catch (Exception e) {properties.put("notes",new Hashtable());}					
					}
					if (m.containsKey("id_node") && m.containsKey("v_term")) 
						if (((List)m.get("id_node")).get(i) != null && ((List)m.get("v_term")).get(i) != null)
							dto.put("term", Utility.toParaula(((List) m.get("v_term")).get(i).toString()));
						dto.put("nodeType", nodeType);
					if (m.containsKey("v_category")) 
						if (((List)m.get("v_category")).get(i)!= null)
							dto.put("category", Utility.toParaula(((List) m.get("v_category")).get(i).toString()));
					if (m.containsKey("v_description")) 
						if (((List)m.get("v_description")).get(i)!= null)
						dto.put("description", Utility.toParaula(((List) m.get("v_description")).get(i).toString()));
					if (m.containsKey("i_position")) 
						if (((List)m.get("i_position")).get(i)!= null)
						properties.put("position", new Integer(Utility.toParaula(((List) m.get("i_position")).get(i).toString())));
					if (m.containsKey("v_type")) 
						if (((List)m.get("v_type")).get(i)!= null)
						properties.put("nodeType", Utility.toParaula(((List) m.get("v_type")).get(i).toString()));
					if (m.containsKey("v_user")) 
						if (((List)m.get("v_user")).get(i)!= null)
						properties.put("user", Utility.toParaula(((List) m.get("v_user")).get(i).toString()));
					if (m.containsKey("v_note")) 
						if (((List)m.get("v_note")).get(i)!= null)
						dto.put("description", Utility.toParaula(((List) m.get("v_note")).get(i).toString()));	
					if (m.containsKey("v_references")) 
						if (((List)m.get("v_references")).get(i)!= null)
						properties.put("references", Utility.toParaula(((List) m.get("v_references")).get(i).toString()));
					if (m.containsKey("d_contribution")) 
						if (((List)m.get("d_contribution")).get(i)!= null)
						properties.put("date", Utility.toParaula(((List) m.get("d_contribution")).get(i).toString()));
					if (m.containsKey("d_node")) 
						if (((List)m.get("d_node")).get(i)!= null)
						properties.put("date", Utility.toParaula(((List) m.get("d_node")).get(i).toString()));		
					if (m.containsKey("id_nodecur_level")) 
						if (((List)m.get("id_nodecur_level")).get(i)!= null){
							properties.put("relatedLevel", new Integer(Utility.toParaula(((List) m.get("id_nodecur_level")).get(i).toString())));						
					}if (m.containsKey("id_nodecur_content")) 
						if (((List)m.get("id_nodecur_content")).get(i)!= null){
							properties.put("relatedContent", new Integer(Utility.toParaula(((List) m.get("id_nodecur_content")).get(i).toString())));						
					}if (m.containsKey("v_term_es")){
						if (((List)m.get("v_term_es")).get(i)!= null)
							properties.put("v_term_es", Utility.toParaula(((List) m.get("v_term_es")).get(i).toString()));						
						else
							properties.put("v_term_es", "");
					}if (m.containsKey("v_term_oc")){
						if (((List)m.get("v_term_oc")).get(i)!= null)
							properties.put("v_term_oc", Utility.toParaula(((List) m.get("v_term_oc")).get(i).toString()));
						else
							properties.put("v_term_oc", "");
					}if (m.containsKey("v_term_en")){
						if (((List)m.get("v_term_en")).get(i)!= null)
							properties.put("v_term_en", Utility.toParaula(((List) m.get("v_term_en")).get(i).toString()));						
						else
							properties.put("v_term_en", "");
					}
					dto.put("properties",properties);	
					lDTO.add(dto);
				}
				logger.info("DTO list created correctly");
			}catch(Exception e){
				logger.warn("Error on create new DTO:"+e.getMessage());
			}
			return lDTO;
		}
		
		
		/**
		 * Retorna una hashtable amb els valors adequats per poder realitzar una consulta al
		 * sistema.
		 * @param idNode Identificador del node que es vol consultar.
		 * @param nodeType Típus del node que es vol consultar.
		 * @param type Típus de relació que es busca.
		 * @param direction Direcció de la relació.
		 * @return Hashtable amb els valors necessaris per realitzar una consulta.
		 */
		protected Hashtable configRelationQuery(int idNode, String nodeType, String type, int direction) {
			// TODO Auto-generated method stub
			String table = null;
			List lElem = new ArrayList();
			Hashtable hRes = new Hashtable();
			String idNodeDest = "";
			String sType = "";
			String dType="";
			String sIdNode;
			sIdNode = "id_node";
			String condi = "";
			
			if (type.compareTo("RCL")==0){
				idNodeDest = "id_nodecur_level";
				table = "cur_content_level";
				sType = "content";
				dType = "level";
			}
			if (type.compareTo("RCA")==0){
				idNodeDest = "id_nodecur_area";
				table = "cur_content_area";
				sType = "content";
				dType = "area";
			}
			if (type.compareTo("ROL")==0){
				idNodeDest = "id_nodecur_level";
				table = "cur_objective_level";
				sType = "objective";
				dType = "level";
			}
			if (type.compareTo("ROA")==0){
				idNodeDest = "id_nodecur_area";
				table = "cur_objective_area";
				sType = "objective";
				dType = "area";
			}
			if (type.compareTo("RH")==0){
				sIdNode = "id_history";
				idNodeDest = "id_node";
				lElem.add("v_type");
				table = "cur_history";
				sType = "history";
			}
			if (type.compareTo("RN")==0){
				sIdNode = "id_note";
				idNodeDest = "id_node";
				lElem.add("v_type");
				table = "cur_notes";
				sType = "notes";
			}
			if (type.compareTo("RLL")==0){
				idNodeDest = "id_nodecur_level";
				table = "cur_level";
				sType = "level";
				dType = "level";
			}
			if (type.compareTo("RAL")==0){
				idNodeDest = "id_nodecur_level";
				table = "cur_area";
				sType = "area";
				dType = "level";
			}
			if (type.compareTo("RCC")==0){
				idNodeDest = "id_nodecur_content";
				table = "cur_content";
				sType = "content";
				dType = "content";
			}
			if (type.compareTo("RET")==0){
				idNodeDest = "id_paraula";
				sIdNode = "id_node";
				table = "cur_thesaurus";
				lElem.add("v_type");
				sType = "thesaurus";
			}
			lElem.add(0,idNodeDest);
			lElem.add(0,sIdNode);
			/*Si el idNode < 0 vol dir q no esta buscant cap relacio en concret sino totes.*/
			if (idNode > 0){
				switch (direction){
				case RelationType.SOURCE: condi = sIdNode+" = "+idNode; break;
				case RelationType.DEST: condi = idNodeDest+" = "+idNode; break;
				default: condi = "("+sIdNode+" = "+idNode+" OR "+idNodeDest+" = "+idNode+")";
				}

				if (type.compareTo("RH")==0 || type.compareTo("RN")==0){
					switch (direction){
					case RelationType.SOURCE: condi = sIdNode+" = "+idNode; break;
					case RelationType.DEST: condi = idNodeDest+" = "+idNode+" AND v_type='"+nodeType+"'"; break;
					default: condi = "("+sIdNode+" = "+idNode+" OR ("+idNodeDest+" = "+idNode+" AND v_type='"+nodeType+"'))";
					}
				}
				if (type.compareTo("RET")==0){
					switch (direction){
					case RelationType.SOURCE: condi = sIdNode+" = "+idNode; break;
					case RelationType.DEST: condi = idNodeDest+" = "+idNode+" AND v_type='"+nodeType+"'"; break;
					default: condi = "("+sIdNode+" = "+idNode+" AND v_type='"+nodeType+"')";
					}
				}
				
			}
			hRes.put("table",table);
			hRes.put("condition", condi);
			hRes.put("elements",lElem);
			hRes.put("sourceType",sType);
			hRes.put("destType",dType);
			
			return hRes;
		}

		/**
		 * 
		 */
		public Hashtable searchNode(String query, List type) throws SemanticException {
			// TODO Auto-generated method stub
			return null;
		}



	}


