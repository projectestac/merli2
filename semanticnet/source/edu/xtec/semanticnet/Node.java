package edu.xtec.semanticnet;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class Node {
	private int idNode;	
	private String term;
	private String nodeType;
	private String description;
	private String category;
	private String note;
	private String history;
	private Hashtable properties;
	
	private SemanticNet snSource;

	private Hashtable loaded = new Hashtable();
	
	
	/*CONSTRUCTORS*/
	
	public Node(int idNode, String sNType, SemanticNet snSource) {
		this.idNode = idNode;
		this.snSource = snSource;
		this.nodeType = sNType;
		this.term = "";
		this.description = "";
		this.category = "";
		this.note = "";
		this.history = "";
		this.properties = new Hashtable();
	};	
	
	public Node(int idNode, String sNType, String term, SemanticNet snSource) {
		this.idNode = idNode;
		this.term = term;
		this.nodeType = sNType;
		this.snSource = snSource;
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
	public Node (int idNode, String sNType, String term, String description, String category, String note, String history, Hashtable properties, SemanticNet snSource){
		this.idNode = idNode;
		this.term = term;
		this.nodeType = sNType;
		this.description = description;
		this.category = category;
		this.note = note;
		this.history = history;
		this.properties = properties;
		this.snSource = snSource;
	}
	
	/**
	 * Constructor with class attributes in a propierties list.
	 * @param properties Hastable;
	 */
	public Node(Hashtable properties, SemanticNet snSource){
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
		this.snSource = snSource;
	}

	
	/*FUNCIONALITATS DE NODE*/
	
	/**
	 * Obté el llistat de relacions del node on aquest apareix com a origen/destí de la relació. 
	 * En format de Llista de relacions
	 * @param iDirection El valor a 1 indica que el node ès l'origen de la relació,
	 * a 2 indica que el node ès el destí de la relació. a 3 que es vol tota possible relacio. 
	 * @return Hashtable format per ["tipusRelacio", Llista relacions]
	 * @throws ThesaurusException 
	 */
	public Hashtable getRelations(List rTypes, int iDirection) throws SemanticException{
		Iterator iter = rTypes.iterator();
		String rType;
		Hashtable hRes;
		while (iter.hasNext()){
			rType = (String) iter.next();
			if (!this.isLoaded(rType)) {
				this.loadRelations(rType);
			}			
		}
		return snSource.getRelations(this.idNode, this.nodeType, iDirection);
	}
	
	
	/**
	 * Returns a list with the relations of the type 'type' and with the node on the 'direction' side.
	 * 
	 * @param relationType Relation type desired. 
	 * @param iDirection 1-> the node is the Source; direction 2-> the node is the Destination; direction 3->both; 
	 * @return {Relation}
	 * @throws ThesaurusException 
	 */
	public List getRelations(String relationType, int iDirection) throws SemanticException{
		if(!this.isLoaded(relationType)){
			this.loadRelations(relationType);
		}
		return snSource.getRelations(this.idNode, this.nodeType, relationType, iDirection);
	}
	
	
	/**
	 * Load all node relations. Call the object 'SemanticNet' to do that.
	 * @param relationType 
	 * @return int. Number of relations loaded
	 * @throws ThesaurusException 
	 */
	
	protected int loadRelations(String relationType) throws SemanticException{
		
		int num = snSource.loadRelations(this.idNode, this.nodeType, relationType, RelationType.ALL);

		this.setLoaded(relationType,true);
		
		return num;
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
	

	public boolean isLoaded(String sNType) {
		if (loaded.containsKey(sNType))
			return ((Boolean)loaded.get(sNType)).booleanValue();
		return false;
	}
	

	public void setLoaded(String sNType, boolean loaded) {
		this.loaded.put(sNType, new Boolean(loaded));
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
	

	public SemanticNet getSemanticNet() {
		return snSource;
	}
	

	public void setSemanticNet(SemanticNet snSource) {
		this.snSource = snSource;
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
