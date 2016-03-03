package edu.xtec.merli.semanticnet;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.xtec.merli.basedades.AccesBD;
import edu.xtec.merli.utils.Utility;
import edu.xtec.semanticnet.DataSource;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticException;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

public class DataSourceTHJDBC  implements DataSource{
		protected static ConnectionBeanProvider broker;	
		private Hashtable hProperties;
		
		private int num = 0;

		public void init(Hashtable propietats) {
			// TODO Auto-generated method stub
			this.hProperties = propietats;			
		}
	
		public Hashtable getNodes(Hashtable hnod) throws SemanticException{
			// TODO Auto-generated method stub
			List lRes = new ArrayList();
			Hashtable hRes = new Hashtable();
			String condicio;
			condicio = "id_paraula IN(";//+Utility.toParaula(lnod.toString())+")";
			if (hnod == null ||hnod.isEmpty() ) {return hRes;}
			Iterator it = hnod.values().iterator();
			
			while (it.hasNext()){
				List lnod = (List) it.next();
				for (int i=0;i<lnod.size();i++){
					condicio += ((List)lnod.get(i)).get(0);
					if(i+1<lnod.size()){
						condicio +=",";
					}else{
						condicio +=")";
					}
				}		
			}
			ConnectionBean cb = this.connectBD();		
			List l = new ArrayList();
			l.add("id_paraula");
			l.add("paraula");
			l.add("nota");
			l.add("categoria");
			l.add("historia");
			try {
				
				Map m =  AccesBD.getObjectList("PAR_CLAU_CAT",l,condicio,"nota",cb.getConnection());
				hRes.put("thesaurus",new ArrayList());
				for (int i = 0;i<((List)m.get("id_paraula")).size();i++){		
					Hashtable dto = new Hashtable();
					dto.put("idNode",new Integer((((List)m.get("id_paraula")).get(i)).toString()));//new Integer((String)((List)m.get("id_paraula")).get(0)));//Utility.toParaula(m.get("id_paraula").toString())));			
					if (m.containsKey("paraula")) 
						dto.put("term", ((List)m.get("paraula")).get(i));
					dto.put("nodeType","thesaurus"); 
					if (m.containsKey("categoria")) 
						if (((List)m.get("categoria")).get(i)!= null){
							dto.put("category", ((List)m.get("categoria")).get(i));
						}
					if (m.containsKey("nota")) 
						if (((List)m.get("nota")).get(i)!= null){
							dto.put("note", ((List)m.get("nota")).get(i));
						}
					if (m.containsKey("historia")) 
						if (((List)m.get("historia")).get(i)!= null){
							dto.put("history", ((List)m.get("historia")).get(i));
						}
					dto.put("description", "Terme extret de PAR_CLAU_CAT.");
		
					((List) hRes.get("thesaurus")).add(dto);
					lRes.add(dto);
				}
			} catch (Exception e) {
				SemanticException the = new SemanticException("NonExistentNode");
				the.setCode(SemanticException.NONEXISTENTNODE);
				the.setStackTrace(e.getStackTrace());
				e.printStackTrace();
			}
			this.disconnectBD(cb);
		
			
			return hRes;
		}

		public Hashtable getNode(int idNode, String nodeType) throws SemanticException {
			// TODO Auto-generated method stub
			Hashtable dto = new Hashtable();
			ConnectionBean cb = this.connectBD();		
			List l = new ArrayList();
			l.add("id_paraula");
			l.add("paraula");
			l.add("nota");
			l.add("categoria");
			l.add("historia");
			
			try {
				Map m =  AccesBD.getObject("PAR_CLAU_CAT",l,"id_paraula = "+idNode,cb.getConnection());
				this.disconnectBD(cb);
				dto.put("idNode", new Integer(Utility.toParaula(m.get("id_paraula").toString())));
				if (m.containsKey("id_paraula")) 
					dto.put("term", Utility.toParaula(m.get("paraula").toString()));
				dto.put("nodeType", nodeType);
				if (m.containsKey("categoria")) 
					dto.put("category", Utility.toParaula(m.get("categoria").toString()));
				if (m.containsKey("nota")) 
					dto.put("note", Utility.toParaula(m.get("nota").toString()));
				if (m.containsKey("historia")) 
					dto.put("history", Utility.toParaula(m.get("historia").toString()));
				dto.put("description", "Terme extret de PAR_CLAU_CAT.");
				
			} catch (Exception e) {
				this.disconnectBD(cb);
				SemanticException the = new SemanticException("NonExistentNode");
				the.setCode(SemanticException.NONEXISTENTNODE);
				the.setStackTrace(e.getStackTrace());
			}


			return dto;
		}

		
		public List getRelations(int idNode,String nodeType , List types, int direction) throws SemanticException {
			// TODO Auto-generated method stub
			String condi;
			String relas;
			List l = new ArrayList();
			l.add("id_paraula");
			l.add("tipus_relacio");
			l.add("id_terme_rel");
			ConnectionBean cb= this.connectBD();
			
			switch (direction){
			case RelationType.SOURCE: condi = "id_paraula = "+idNode; break;
			case RelationType.DEST: condi = "id_terme_rel = "+idNode; break;
			default: condi = "(id_paraula = "+idNode+" OR id_terme_rel = "+idNode+")";
			}
			relas = " AND (";
			for (int i =0; i<types.size();i++){
				relas += "tipus_relacio ='"+types.get(i)+"'"; 
				if (i +1 < types.size()){
					relas += " OR ";
				}else{
					relas += ")";
				}
			}
			
			try {
				Map m = AccesBD.getObjectList("TERME_RELACIONAT",l,"("+condi+relas+")","", cb.getConnection());
				this.disconnectBD(cb);
				List l1, l2, l3;
				l1 = (List) m.get(l.get(0));
				l2 = (List) m.get(l.get(1));
				l3 = (List) m.get(l.get(2));
				l.clear();
				for (int i=0;i< l1.size();i++){
					Hashtable dto = new Hashtable();
					dto.put("idSource",new Integer(Utility.toParaula(l1.get(i).toString())));
					dto.put("sourceType",nodeType);
					dto.put("idDest",new Integer(Utility.toParaula(l3.get(i).toString())));
					dto.put("destType",nodeType);
					dto.put("relationType",Utility.toParaula(l2.get(i).toString()));
					l.add(dto);
				}
				
			} catch (SQLException e) {
				this.disconnectBD(cb);
				e.printStackTrace();
			}
			return l;//this.getRelacionsOrigen(idNode, types);
		}
		
		

		public Hashtable getAllNodes(List lNodeType) throws SemanticException {
			
			List lRes = new ArrayList();
			String condicio;
	
			ConnectionBean cb = this.connectBD();		
			List l = new ArrayList();
			l.add("id_paraula");
			l.add("paraula");
			l.add("nota");
			l.add("categoria");
			l.add("historia");
			try {
				
				Map m =  AccesBD.getObjectList("PAR_CLAU_CAT",l," "," id_paraula",cb.getConnection());

				for (int i = 0;i<((List)m.get("id_paraula")).size();i++){		
					Hashtable dto = new Hashtable();
					dto.put("idNode",new Integer((((List)m.get("id_paraula")).get(i)).toString()));//new Integer((String)((List)m.get("id_paraula")).get(0)));//Utility.toParaula(m.get("id_paraula").toString())));			
					if (m.containsKey("paraula")) 
						if (((List)m.get("paraula")).get(i)!= null){
							dto.put("term", ((List)m.get("paraula")).get(i));
						}
					dto.put("nodeType","thesaurus"); 
					if (m.containsKey("categoria")) 
						if (((List)m.get("categoria")).get(i)!= null){
							dto.put("category", ((List)m.get("categoria")).get(i));
						}
					if (m.containsKey("nota")) 
						if (((List)m.get("nota")).get(i)!= null){
							dto.put("note", ((List)m.get("nota")).get(i));
						}
					if (m.containsKey("historia")) 
						if (((List)m.get("historia")).get(i)!= null){
							dto.put("history", ((List)m.get("historia")).get(i));
						}
					dto.put("description", "Terme extret de PAR_CLAU_CAT.");
	
					lRes.add(dto);
				}
			} catch (Exception e) {
				SemanticException the = new SemanticException("NonExistentNode");
				the.setCode(SemanticException.NONEXISTENTNODE);
				the.setStackTrace(e.getStackTrace());
				e.printStackTrace();
			}
			this.disconnectBD(cb);
			Hashtable h = new Hashtable();
			h.put("thesaurus",lRes);
			return h;
		}

		public Hashtable getAllRelations(List types) throws SemanticException {
			String condi;
			String relas;
			List l = new ArrayList();
			l.add("id_paraula");
			l.add("tipus_relacio");
			l.add("id_terme_rel");
			ConnectionBean cb= this.connectBD();
			Hashtable hRes = new Hashtable();
			
			relas = "(";
			for (int i =0; i<types.size();i++){
				relas += "tipus_relacio ='"+types.get(i)+"'"; 
				if (i +1 < types.size()){
					relas += " OR ";
				}else{
					relas += ")";
				}
				hRes.put(types.get(i),new ArrayList());
			}
			
			try {
				Map m = AccesBD.getObjectList("TERME_RELACIONAT",l,relas,"", cb.getConnection());
				this.disconnectBD(cb);
				List l1, l2, l3;
				l1 = (List) m.get(l.get(0));
				l2 = (List) m.get(l.get(1));
				l3 = (List) m.get(l.get(2));
				l.clear();
				for (int i=0;i< l1.size();i++){
					Hashtable dto = new Hashtable();
					dto.put("idSource",new Integer(Utility.toParaula(l1.get(i).toString())));
					dto.put("sourceType","thesaurus");
					dto.put("idDest",new Integer(Utility.toParaula(l3.get(i).toString())));
					dto.put("destType","thesaurus");
					dto.put("relationType",Utility.toParaula(l2.get(i).toString()));
					//l.add(dto);
					((List) hRes.get(Utility.toParaula(l2.get(i).toString()))).add(dto);
				}
				
			} catch (SQLException e) {
				this.disconnectBD(cb);
				e.printStackTrace();
			}
			
			
			
			return hRes;//this.getRelacionsOrigen(idNode, types);
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
			}catch(Exception e){}//missError ="error.desconnexio.bd";}
		}

		public int addNewNode(Hashtable DTO) throws SemanticException {
			// TODO Auto-generated method stub
			return 0;
		}

		public void setNode(int idNode, String sNType, Hashtable dto) throws SemanticException {
			// TODO Auto-generated method stub
			
		}

		public void delNode(int idNode, String sNType) throws SemanticException {
			// TODO Auto-generated method stub
			
		}

		public void addNewRelation(Hashtable dto) throws SemanticException {
			// TODO Auto-generated method stub
			
		}

		public void setRelation(Hashtable dtoOldNode, Hashtable dtoChanges) throws SemanticException {
			// TODO Auto-generated method stub
			
		}

		public void delRelation(Hashtable dto) throws SemanticException {
			// TODO Auto-generated method stub
			
		}

		public void delRelationsNode(int idNode, String sNType, String sRType) throws SemanticException {
			// TODO Auto-generated method stub
			
		}

		public Hashtable searchNode(String query, List type) throws SemanticException {
			// TODO Auto-generated method stub
			Hashtable ht = new Hashtable();
			ht.put("thesaurus", new ArrayList());

			ConnectionBean cb = this.connectBD();		
			List l = new ArrayList();
			l.add("id_paraula");
			l.add("paraula");
			l.add("nota");
			l.add("categoria");
			l.add("historia");
			
			Map m;
			try {
				m = AccesBD.getObjectList("PAR_CLAU_CAT",l," par_pla LIKE '%"+Utility.aplanarText(query)+"%' "," id_paraula",cb.getConnection());

				for (int i = 0;i<((List)m.get("id_paraula")).size();i++){		
					((ArrayList) ht.get("thesaurus")).add(new Integer((((List)m.get("id_paraula")).get(i)).toString()));			
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.disconnectBD(cb);
			
			return ht;
		}
		
	

	}


