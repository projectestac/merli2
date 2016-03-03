package edu.xtec.gescurriculum.gestorcurriculum;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.segur.User;
import edu.xtec.merli.segur.operations.Operations;
import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;

public class NodeForm extends ActionForm {

	private int idNode;	
	private String term;
	private String termEs;
	private String termEn;
	private String termOc;
	private String nodeType;
	private String description;
	private String category;
	private String note;
	private String history;
	private String references;
	
	private String user;
	private int profile;
	
	private int idLevel;
	private int idArea;
	private int idContent;
	private int idObjective;
	private String operacio; 
	private String entornOperacio; 
		
	private String lastNotes;
	private String contentcategory;
	private String objectivecategory;
	
	private int idKey;
	private String navPath;
	private String selecPath;
	
	
	/**
	 * Dades inicials del formulari. Inicialitza el formulari amb els valors adequats.
	 * Si cal carregar inforció d'algun node ho fa.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		idLevel = 0;
		idKey = 0;
		if (operacio == null){
			operacio = "";
		}
		if (navPath == null){
			navPath = "0#";
		}
		if (idNode == 0){
			idNode = 0;
		}
		term = "terme reseted";
		if(((User)request.getSession().getAttribute("user")).hasPermission(Operations.DUC.SELEC)){
			this.loadNodeInfo();
		}
		if (request.getSession() != null && request.getSession().getAttribute("user") != null){
			user = ((User)request.getSession().getAttribute("user")).getUser();
		}
	}
	
	
	/**
	 * Validació del contingut del formulari previ al tractament d'aquest.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		String aux;
		ActionErrors errors = new ActionErrors();
		//Recupera l'usuari de la sessió.
		if (request.getSession() != null && request.getSession().getAttribute("user") != null){
			user = ((User)request.getSession().getAttribute("user")).getUser();
			if (user == null){
				user =((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).getUser();				
			}
		}
		/* Validació de l'existència i la longitud dels camps.*/
		if((this.getTerm() == null)){
			errors.add("term", new ActionError("error.term.required"));
		}else{			
			if (this.getTerm().length() < 2) 
				errors.add("term", new ActionError("error.term.curt"));	
		}
		if((this.getNodeType() == null)){
			errors.add("nodeType", new ActionError("error.nodeType.required"));
		}else{			
			if (this.getNodeType().length() < 2) 
				errors.add("nodeType", new ActionError("error.nodeType.nonexistent"));	
			if((this.getCategory() == null) && (this.getNodeType().compareTo("area")!=0)){
				errors.add("category", new ActionError("error.category.required"));
			}else{			
				if ((this.getTerm().length() < 2)&& (this.getNodeType().compareTo("area")!=0)) 
					errors.add("category", new ActionError("error.category.curt"));	
			}
		}
		
		/* comprovant permisos de l'usuari.*/
		if(((User)request.getSession().getAttribute("user")).hasPermission(this.getIdOperacio())){
		}else{
			if (this.getIdOperacio()!=0)
				errors.add("user",new ActionError("error.user.privilegies"));
			return errors;
		}
		
		
		
		//Operacions de mobilitat. Canvi de posició.
		try{
			if(this.getOperacio().compareTo("swapup")==0){
				  SemanticInterface si = new SemanticInterface();
				  si.swapNodes(this.getIdNode(),this.getIdLevel(),this.getIdArea(),this.getNodeType(), true);
				  this.operacio = "";
				  errors.add("operation", new ActionError("error.swap.ok"));
			}
			
			if(this.operacio.compareTo("swapdown")==0){
				  SemanticInterface si = new SemanticInterface();
				  si.swapNodes(this.getIdNode(),this.getIdLevel(),this.getIdArea(),this.getNodeType(), false);
				  this.operacio = "";
				  errors.add("operation", new ActionError("error.swap.ok"));
			}
		}catch (DUCException de){
			errors.add("operation", new ActionError(de.getMessage()));
		}
		
		//Eliminar node, comprovacions prèvies.
		if(this.operacio.compareTo("delnode")==0){
			if (this.getNodeType().compareTo("level")==0) this.setIdNode(this.getIdLevel());
			if (this.getNodeType().compareTo("area")==0) this.setIdNode(this.getIdArea());
			if (this.getNodeType().compareTo("content")==0) this.setIdNode(this.getIdContent());
			if (this.getNodeType().compareTo("objective")==0) this.setIdNode(this.getIdObjective());
			
			if (this.getIdNode() <= 0)
				errors.add("operation", new ActionError("error.node.noselected"));
		}
		
		//Selecció d'un node sense realitzar-hi cap operació.
		if(getOperacio().compareTo("selec")==0){
			this.loadNodeInfo();
			errors.add("", new ActionError("error.selec.fet"));
		}
/*
		//Navegar pel thesaurus.
		if (getOperacio().compareTo("thesnav")==0){
			int ac;
			boolean trobat = false;
			if (navPath != null){
				aux = navPath;
				navPath = "";
				String acID;
				while (aux.lastIndexOf("#")>0 && !trobat){
					acID = aux.substring(0,aux.indexOf("#"));
					aux = aux.substring(aux.indexOf("#")+1);
					navPath += acID+"#";
					if (Integer.parseInt(acID)==this.idKey){
						trobat = true;
					}
				}
			}else{
				navPath = "0#";
			}
			if (!trobat){
				navPath+= this.idKey+"#";  			
			}		
			errors.add("", new ActionError("error.selec.fet"));
		}
		*/
		
		
		return errors;	
	}
	
	
	/**
	 * Carrega la informació del node que cal. Posa els valors adequats a cada camp del formulari.
	 *
	 */
	protected void loadNodeInfo() {
		if (this.getIdNode() > 0 && this.getNodeType() != null){
			SemanticInterface si = new SemanticInterface();
			Node n = si.getNode(this.getIdNode(),this.getNodeType());
			this.setCategory(n.getCategory());
			this.setDescription(n.getDescription());
			this.setNote("");
			this.setTerm(n.getTerm());
			this.setTermEs((String) n.getProperties().get("v_term_es"));
			this.setTermEn((String) n.getProperties().get("v_term_en"));
			this.setTermOc((String) n.getProperties().get("v_term_oc"));
			this.setReferences((String) n.getProperties().get("references"));			
			this.setLastNotes(n.getNote().toString());
			this.setSelecPath("");
			this.setNavPath("0#");
			Iterator iter =(si.getRelations(n.getIdNode(),n.getNodeType(),"RET",RelationType.SOURCE)).iterator();
			while (iter.hasNext()){
				selecPath += ((Relation)iter.next()).getIdDest()+"#";
			}
			if (this.getIdArea() <= 0){
				ArrayList l=null;
				try {
					if (this.getNodeType().compareTo("objective")==0)
						l = si.getRelations(n.getIdNode(),"objective","ROA",RelationType.SOURCE);
					if (this.getNodeType().compareTo("content")==0)
						l = si.getRelations(n.getIdNode(),"content","RCA",RelationType.SOURCE);//(ArrayList) n.getRelations("RCA",RelationType.DEST);
					this.setIdArea(((Relation)l.get(0)).getIdDest());
				} catch (Exception e) {}
			}
			if (this.getNodeType().compareTo("content")==0){
				this.setContentcategory(this.getCategory());				
			}else{
				n = si.getNode(this.getIdContent(),"content");
				if (n!=null)
					this.setContentcategory(n.getCategory());
			}			
			n = si.getNode(this.getIdObjective(),"objective");			
			if (n!=null)
				this.setObjectivecategory(n.getCategory());
			else
				if (this.getNodeType().compareTo("objective")==0)
					this.setObjectivecategory(this.getCategory());											
		}
	}



	/**
	 * Converteix el formulari a DTO.
	 * @return Hashtable en format de Node amb els camps del formulari.
	 */
	public Hashtable toDTO(){
		Hashtable dto = new Hashtable();
		dto.put("idNode",new Integer(this.getIdNode()));
		dto.put("term",this.getTerm());
		dto.put("nodeType",this.getNodeType());
		dto.put("description",this.getDescription());
		if (this.getNodeType().compareTo("area")!=0)
			dto.put("category",this.getCategory());
		if (this.getNodeType().length()>2) dto.put("note",this.getNote());
		try{
		dto.put("history",this.getHistory());
		}catch(Exception e){}
		

		Hashtable properties = new Hashtable();
		try{
		properties.put("references",this.getReferences());
		}catch(Exception e){}
		try{
			if (this.getNodeType().compareTo("level")!=0){		
				properties.put("relatedLevel",new Integer(this.getIdLevel()));
			}else{
				if (this.getOperacio().compareTo("addnode")==0||this.getOperacio().compareTo("addnodefill")==0)
					properties.put("relatedLevel",new Integer(this.getIdLevel()));
			}
		}catch(Exception e){}
		try{
			properties.put("idArea",new Integer(this.getIdArea()));
		}catch(Exception e){}
		try{
			properties.put("user",this.getUser());
		}catch(Exception e){}
		try{
		properties.put("idContent",new Integer(this.getIdContent()));
		if (this.getOperacio().compareTo("addnode")==0||this.getOperacio().compareTo("addnodefill")==0)
			properties.put("relatedContent",new Integer(this.getIdContent()));	
		}catch(Exception e){}
		try{
		properties.put("idObjective",new Integer(this.getIdObjective()));
		}catch(Exception e){}
		try{
			properties.put("v_term_es",this.getTermEs());
		}catch(Exception e){}
		try{
			properties.put("v_term_en",this.getTermEn());
		}catch(Exception e){}
		try{
			properties.put("v_term_oc",this.getTermOc());
		}catch(Exception e){}
		try{
			String aux = this.getSelecPath();
			String acID;
			ArrayList lKeys = new ArrayList();
			while (aux.lastIndexOf("#")>0){
				acID = aux.substring(0,aux.indexOf("#"));
				aux = aux.substring(aux.indexOf("#")+1);
				lKeys.add(new Integer(acID));
			}
			properties.put("thesaurusKeys",lKeys);
		}catch(Exception e){}

		if(this.operacio.equals("delnode"))
			this.idLevel = 0;
		
		dto.put("properties",properties);
		return dto;
	}
	
	
	
	
	public int getIdKey() {
		return idKey;
	}
	


	public void setIdKey(int idKey) {
		this.idKey = idKey;
	}
	


	public String getNavPath() {
		return navPath;
	}
	


	public void setNavPath(String path) {
		navPath = path;
	}
	


	public String getSelecPath() {
		return selecPath;
	}
	


	public void setSelecPath(String selecPath) {
		this.selecPath = selecPath;
	}
	


	public String getLastNotes() {
		return lastNotes;
	}
	

	public void setLastNotes(String lastNotes) {
		this.lastNotes = lastNotes;
	}
	

	public String getContentcategory() {
		if (this.contentcategory == null) this.contentcategory = "ca";
		if (this.getCategory()!=null && this.contentcategory.compareTo(this.getCategory()) != 0 && this.getNodeType() != null && this.getNodeType().compareTo("content")==0)
			this.contentcategory = this.getCategory();
		return contentcategory.toUpperCase();
	}
	
	public void setContentcategory(String contentcategory) {
		this.contentcategory = contentcategory; 
	}
	

	public String getObjectivecategory() {
		if (this.objectivecategory == null) this.objectivecategory = "ot";
		if (this.getCategory()!=null && this.objectivecategory.compareTo(this.getCategory()) != 0 && this.getNodeType() != null &&this.getNodeType().compareTo("objective")==0)
			this.objectivecategory = this.getCategory();
		return this.objectivecategory.toUpperCase();
	}
	
	public void setObjectivecategory(String objectivecategory) {
		this.objectivecategory = objectivecategory; 
	}

	public String getOperacio() {
		return operacio;
	}
	
	public int getIdOperacio() {
		if (operacio.equals("swapup")) return Operations.DUC.SWAP;
		if (operacio.equals("swapdown")) return Operations.DUC.SWAP;
		if (operacio.equals("delnode")) return Operations.DUC.DEL;
		if (operacio.equals("selec")) return Operations.DUC.SELEC;
		if (operacio.equals("addnode")) return Operations.DUC.ADD;
		if (operacio.equals("addnodefill")) return Operations.DUC.ADD;
		if (operacio.equals("setnode")) return Operations.DUC.SET;
		return 0;
	}
	



	public void setOperacio(String operacio) {
		this.operacio = operacio;
	}
	


	public int getIdContent() {
		return idContent;
	}
	

	public void setIdContent(int idContent) {
		this.idContent = idContent;
	}
	

	public int getIdObjective() {
		return idObjective;
	}
	
	
	public void setIdObjective(int idObjective) {
		this.idObjective = idObjective;
	}
	

	public int getIdArea() {
		return idArea;
	}
	


	public void setIdArea(int idArea) {
		this.idArea = idArea;
	}
	


	public int getIdLevel() {
		return idLevel;
	}
	

	public void setIdLevel(int idLevel) {
		this.idLevel = idLevel;
	}
	

	public String getReferences() {
		if (references == null) return "";
		return references;
	}
	

	public void setReferences(String references) {
		this.references = references;
	}
	

	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getHistory() {
		return history;
	}
	
	public void setHistory(String history) {
		this.history = history;
	}
	
	public int getIdNode() {
		return idNode;
	}
	
	public void setIdNode(int idNode) {
		this.idNode = idNode;
	}
	
	public String getNodeType() {
		return nodeType;
	}
	
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	
	public String getNote() {
		if (note == null) {return "";}
		return note;
	}
	
	public void setNote(String note) {
		this.note = note;
	}
	
	public String getTerm() {
		return term;
	}
	
	public void setTerm(String term) {
		this.term = term;
	}


	public String getEntornOperacio() {
		return entornOperacio;
	}
	


	public String getTermOc() {
		return termOc;
	}


	public void setTermOc(String termOc) {
		this.termOc = termOc;
	}


	public String getTermEn() {
		return termEn;
	}


	public void setTermEn(String termEn) {
		this.termEn = termEn;
	}


	public String getTermEs() {
		return termEs;
	}


	public void setTermEs(String termEs) {
		this.termEs = termEs;
	}


	public void setEntornOperacio(String entornOperacio) {
		this.entornOperacio = entornOperacio;
	}


	public int getProfile() {
		return profile;
	}
	


	public void setProfile(int profile) {
		this.profile = profile;
	}
	


	public String getUser() {
		if (user == null) {
			
			return "";}
		return user;
	}
	


	public void setUser(String user) {
		this.user = user;
	}


	/**
	 * Neteja el formulari. Deixa tots els valors del formulari nets.
	 *
	 */
	public void cleanForm() {
		// TODO Auto-generated method stub
		this.setCategory("");
		this.setContentcategory("");
		this.setDescription("");
		this.setEntornOperacio("");
		this.setHistory("");
		//CANVIAT PER REPARAR THESAURUS
		//this.setIdKey(0);
		this.setIdNode(0);
		this.setIdObjective(0);
		this.setLastNotes("");
		//CANVIAT PER REPARAR THESAURUS : 	this.setNavPath("");
		this.setNavPath("0#");
		this.setNodeType("");
		this.setNote("");
		this.setObjectivecategory("");
		this.setReferences("");
		this.setSelecPath("");
		this.setTerm("");
	}
	
	
	
	



}
