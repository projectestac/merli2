package edu.xtec.merli.semanticnet.elements;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import edu.xtec.semanticnet.DataSource;
import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.NodeType;
import edu.xtec.semanticnet.SemanticException;
import edu.xtec.semanticnet.SemanticNet;

public class CurriculumNode extends NodeType {

	private static final Logger logger = Logger.getRootLogger();//("xtec.duc");
	
	public CurriculumNode(String type, DataSource dSource, SemanticNet snSource) {
		super(type, dSource, snSource);
		// TODO Auto-generated constructor stub
	}
	
	public CurriculumNode(){
		super("newNode",null,null);
	}

	public void clone(NodeType n){
		super.clone(n);
	}
	
	public List getNodes(){
		List l = super.getNodes();
		List lord = new ArrayList();
		int posElemI, posElemJ;
		try{
			boolean trobat = false;
			for (int i = 0; i<l.size(); i++){
				int j = 0;
				while (!trobat && j<lord.size()){
					posElemI = Integer.parseInt( (String) ((Node)l.get(i)).getProperties().get("position") );
					posElemJ = Integer.parseInt( (String) ((Node)lord.get(j)).getProperties().get("position") );
					if (posElemI < posElemJ){
						trobat = true;
					}else{	
						j++;
					}
				}
				trobat = false;
				lord.add(j, l.get(i));
			}
		}catch(Exception e){
			logger.warn("Error getting information of node :"+e.getMessage());
			return l;
		}
		
		return lord;
	}
	
	
	public Node setNode(int idNode, Hashtable dto) throws SemanticException {
		// TODO Auto-generated method stub
		Node nsn = this.getNode(idNode);
		Hashtable hprop;
		Enumeration ep = dto.keys();

		if (dto.containsKey("term")){
			nsn.setTerm((String)dto.get("term"));
		}
		if (dto.containsKey("category")){
			nsn.setCategory((String)dto.get("category"));
		}
		if (dto.containsKey("note")){
			nsn.setNote((String)dto.get("note"));
		}
		if (dto.containsKey("history")){
			nsn.setHistory((String)dto.get("history"));
		}
		if (dto.containsKey("description")){
			nsn.setDescription((String)dto.get("description"));
		}
		if (dto.containsKey("properties")){
			if (nsn.getProperties().containsKey("position")){
				if (((Hashtable)dto.get("properties")).containsKey("position")){
						try{
							nsn.getProperties().put("position",(Integer)((Hashtable) dto.get("properties")).get("position"));
						}catch(Exception e){}			
				}
			}
			if (nsn.getProperties().containsKey("references")){
				if (((Hashtable)dto.get("properties")).containsKey("references")){
					nsn.getProperties().put("references",(String)((Hashtable) dto.get("properties")).get("references"));
				}
			}
			if (nsn.getProperties().containsKey("type")){
				if (((Hashtable)dto.get("properties")).containsKey("type")){
					nsn.getProperties().put("type",(String)((Hashtable) dto.get("properties")).get("type"));
				}
			}
			if (((Hashtable)dto.get("properties")).containsKey("user")){
				nsn.getProperties().put("user",(String)((Hashtable) dto.get("properties")).get("user"));
			}
			
			if (nsn.getProperties().containsKey("date")){
				if (((Hashtable)dto.get("properties")).containsKey("date")){
					nsn.getProperties().put("date",((Hashtable) dto.get("properties")).get("date"));
				}
			}
			if (nsn.getProperties().containsKey("relatedLevel")){
				if (((Hashtable)dto.get("properties")).containsKey("relatedLevel")){
						try{
							nsn.getProperties().put("relatedLevel",(Integer)((Hashtable) dto.get("properties")).get("relatedLevel"));
						}catch(Exception e){}			
				}
			}
			if (nsn.getProperties().containsKey("relatedContent")){
				if (((Hashtable)dto.get("properties")).containsKey("relatedContent")){
						try{
							nsn.getProperties().put("relatedContent",(Integer)((Hashtable) dto.get("properties")).get("relatedContent"));
						}catch(Exception e){}			
				}
			}
			if (nsn.getProperties().containsKey("v_term_es")){
				if (((Hashtable)dto.get("properties")).containsKey("v_term_es")){
						try{
							nsn.getProperties().put("v_term_es",(String)((Hashtable) dto.get("properties")).get("v_term_es"));
						}catch(Exception e){}			
				}
			}
			if (nsn.getProperties().containsKey("v_term_en")){
				if (((Hashtable)dto.get("properties")).containsKey("v_term_en")){
						try{
							nsn.getProperties().put("v_term_en",(String)((Hashtable) dto.get("properties")).get("v_term_en"));
						}catch(Exception e){}			
				}
			}
			if (nsn.getProperties().containsKey("v_term_oc")){
				if (((Hashtable)dto.get("properties")).containsKey("v_term_oc")){
						try{
							nsn.getProperties().put("v_term_oc",(String)((Hashtable) dto.get("properties")).get("v_term_oc"));
						}catch(Exception e){}			
				}
			}
			//nsn.setProperties((Hashtable)dto.get("properties"));
		}
		try{
			super.getDSource().setNode(idNode, this.getType(), nsn.toDTO());				
		}catch(SemanticException se){
			logger.warn("Error modifying node :"+se.getMessage());
			throw se;
		}

		return nsn;
	}
	
	
	
}
