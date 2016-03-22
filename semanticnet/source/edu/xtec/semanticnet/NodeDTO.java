package edu.xtec.semanticnet;

import java.util.Hashtable;

public class NodeDTO {
	private int idNode;	
	private String term;
	private String nodeType;
	private String description;
	private String category;
	private String note;
	private String history;
	private Hashtable properties;	
	
	
	/*CONSTRUCTORS*/
	
	public NodeDTO(int idNode, String sNType) {
		this.idNode = idNode;
		this.nodeType = sNType;
		this.term = "";
		this.description = "";
		this.category = "";
		this.note = "";
		this.history = "";
		this.properties = new Hashtable();
	};	
	
	public NodeDTO(int idNode, String sNType, String term) {
		this.idNode = idNode;
		this.term = term;
		this.nodeType = sNType;
		this.description = "";
		this.category = "";
		this.note = "";		
		this.history = "";
		this.properties = new Hashtable();
	};
	
	/**
	 * Constructor with all class attributes.
	 * @param idNode Identifier
	 * @param term Term represented for the node.
	 * @param description 
	 * @param category 
	 * @param note 
	 * @param history 
	 */
	public NodeDTO (int idNode, String sNType, String term, String description, String category, String note, String history, Hashtable properties){
		this.idNode = idNode;
		this.term = term;
		this.nodeType = sNType;
		this.description = description;
		this.category = category;
		this.note = note;
		this.history = history;
		this.properties = properties;
	}
	
	/**
	 * Constructor with class attributes in a propierties list.
	 * @param properties Hastable;
	 */
	public NodeDTO(Hashtable properties){
		this.term = "";
		this.description = "";
		this.category = "";
		this.note = "";
		this.history = "";
		if (properties.containsKey("idNode")) this.idNode = ((Integer)properties.get("idNode")).intValue();
		if (properties.containsKey("term")) this.term = (String)properties.get("term");
		if (properties.containsKey("nodeType")) this.nodeType = (String)properties.get("nodeType");
		if (properties.containsKey("description")) this.description = (String)properties.get("description");
		if (properties.containsKey("category")) this.category = (String)properties.get("category");
		if (properties.containsKey("note")) this.note = (String)properties.get("note");
		if (properties.containsKey("history")) this.history = (String)properties.get("history");
		if (properties.containsKey("properties")) this.properties = (Hashtable)properties.get("properties");		
	}
	


	public Hashtable toDTO(){
		Hashtable dto = new Hashtable();
		

		dto.put("idNode", new Integer(this.getIdNode()));
		dto.put("nodeType", this.getNodeType());
		dto.put("term", this.getTerm());
		dto.put("category", this.getCategory());
		dto.put("note", this.getNote());
		dto.put("history", this.getHistory());
		dto.put("description", this.getDescription());
		dto.put("properties", this.getProperties());
		return dto;
	}
	
	
	/*OPERACIONS GET/SET*/
	

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
	

	public String getNote() {
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
	

	public String getNodeType() {
		return nodeType;
	}
	

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Hashtable getProperties() {
		return properties;
	}
	

	public void setProperties(Hashtable properties) {
		this.properties = properties;
	}
	
	
}
