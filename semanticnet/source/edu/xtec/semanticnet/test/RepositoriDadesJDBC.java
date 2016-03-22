package edu.xtec.semanticnet.test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import edu.xtec.semanticnet.DataSource;
import edu.xtec.semanticnet.SemanticException;	



public class RepositoriDadesJDBC implements DataSource{
		
		private Hashtable hProperties;
		
		private int num = 1;

		public void init(Hashtable propietats) {
			// TODO Auto-generated method stub
			this.hProperties = propietats;			
		}
	
		public Hashtable getNode(int idNode, String sNType) throws SemanticException {
			// TODO Auto-generated method stub
			Hashtable dto = new Hashtable();
				
			List l = new ArrayList();
			l.add("id_paraula");
			l.add("paraula");
			l.add("nota");
			l.add("categoria");
			l.add("historia");
		
			dto.put("idNode", new Integer(idNode));
			dto.put("nodeType", sNType);
			dto.put("term", "terme nº"+idNode);
			dto.put("category", "categoria:"+idNode);
			dto.put("note", "nota"+idNode);
			dto.put("history", "history"+idNode);
			dto.put("description", "Terme "+idNode+" extret de prova.");
		
			return dto;
		}

		
		public List getRelations(int idNode, String sNType, List types, int direction) throws SemanticException {
			// TODO Auto-generated method stub
			String condi;
			String relas;
			List l = new ArrayList();
			
			for (int i=0;i< 5;i++){
				Hashtable dto = new Hashtable();
				dto.put("idSource",new Integer(idNode));
				dto.put("sourceType","T");
				dto.put("relationType","RT");
				dto.put("idDest",new Integer(i));
				dto.put("destType","T");
				l.add(dto);
			}
		
			return l;//this.getRelacionsOrigen(idNode, types);
		}
		
		

		public Hashtable getNodes(Hashtable hnod) throws SemanticException{
			// TODO Auto-generated method stub
			Hashtable hRes = new Hashtable();
			String condicio;
			if (hnod == null ||hnod.isEmpty() ) {return hRes;}
			List l = new ArrayList();
			Iterator it = hnod.values().iterator();
			while (it.hasNext()){
				List lnod = (List) it.next();
				for (int i = 0;i<lnod.size();i++){		
					Hashtable dto = new Hashtable();
	
					dto.put("idNode",((List)lnod.get(i)).get(0));
					dto.put("nodeType", ((List)lnod.get(i)).get(1));
					dto.put("term", "terme nº"+((List)lnod.get(i)).get(0));
					dto.put("category", "categoria:"+((List)lnod.get(i)).get(0));
					dto.put("note", "nota"+((List)lnod.get(i)).get(0));
					dto.put("history", "history"+((List)lnod.get(i)).get(0));
					dto.put("description", "Terme "+((List)lnod.get(i)).get(0)+" extret de prova.");
					if (!hRes.containsKey(((List)lnod.get(i)).get(1))){
						hRes.put(((List)lnod.get(i)).get(1), new ArrayList());
					}
					((List) hRes.get(((List)lnod.get(i)).get(1))).add(dto);
				}
			}
			
			return hRes;
		}

		public Hashtable getAllNodes(List lNodeType) throws SemanticException {
			
			List lRes = new ArrayList();
			String condicio;
			
			List l = new ArrayList();

			for (int i = 0;i<10;i++){		
				Hashtable dto = new Hashtable();

				dto.put("idNode",new Integer(i));
				dto.put("nodeType", "T");
				dto.put("term", "terme nº"+((List)l.get(i)).get(0));
				dto.put("category", "categoria:"+((List)l.get(i)).get(0));
				dto.put("note", "nota"+((List)l.get(i)).get(0));
				dto.put("history", "history"+((List)l.get(i)).get(0));
				dto.put("description", "Terme "+((List)l.get(i)).get(0)+" extret de prova.");
				
				lRes.add(dto);
			}
			
			Hashtable hRes = new Hashtable();
			hRes.put("T",lRes);
			
			return hRes;
		}

		public Hashtable getAllRelations(List types) throws SemanticException {
			String condi;
			String relas;
			List l = new ArrayList();
			Hashtable h = new Hashtable();
			/*l.add("id_paraula");
			l.add("tipus_relacio");
			l.add("id_terme_rel");
			ConnectionBean cb= this.connectBD();
			
			
			relas = "(";
			for (int i =0; i<types.size();i++){
				relas += "tipus_relacio ='"+types.get(i)+"'"; 
				if (i +1 < types.size()){
					relas += " OR ";
				}else{
					relas += ")";
				}
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
					dto.put("type",Utility.toParaula(l2.get(i).toString()));
					dto.put("idDest",new Integer(Utility.toParaula(l3.get(i).toString())));
					l.add(dto);
				}
				
			} catch (SQLException e) {
				this.disconnectBD(cb);
				e.printStackTrace();
			}*/
			return h;//this.getRelacionsOrigen(idNode, types);
		}

		public int addNewNode(Hashtable dto) throws SemanticException {
			System.out.println("DS:"+dto);
			return num++;
		}

		public void setNode(int idNode, String sNType, Hashtable dto) throws SemanticException {
			// TODO Auto-generated method stub

			System.out.println("DS:"+idNode+sNType+dto);
			
		}

		public void delNode(int idNode, String sNType) throws SemanticException {
			// TODO Auto-generated method stub
			System.out.println("DS:"+idNode+sNType);
			
		}

		public void addNewRelation(Hashtable dto) throws SemanticException {
			// TODO Auto-generated method stub
			System.out.println("DS:"+dto);
			
		}

		public void setRelation(Hashtable dtoOldNode, Hashtable dtoChanges) throws SemanticException {
			// TODO Auto-generated method stub

			System.out.println("DS:"+dtoOldNode+""+dtoChanges);
		}

		public void delRelation(Hashtable dto) throws SemanticException {
			// TODO Auto-generated method stub
			System.out.println("DS:"+dto);
			
		}

		public void delRelationsNode(int idNode, String sNType, String sRType) throws SemanticException {
			// TODO Auto-generated method stub
			
		}

		public Hashtable searchNode(String query, List type) {
			// TODO Auto-generated method stub
			return null;
		}


	}


