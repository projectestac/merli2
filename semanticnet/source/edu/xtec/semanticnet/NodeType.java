package edu.xtec.semanticnet;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class NodeType {
	private String type;
	private String description;
	
	private Hashtable hProperties;
	protected Hashtable hNodes;
	
	private SemanticNet snSource;
	private DataSource dSource;
	
	
	/**
	 * Constructor. 
	 * @param type  Identifier type of the Node.
	 */
	public NodeType(String type, DataSource ds, SemanticNet snSource){
		init();
		this.type = type;
		this.snSource = snSource;
		this.dSource = ds;
	}
	
	/**
	 * Constructor. 
	 * @param type  Identifier type of the Node.
	 * @param properties  Needed propierties of the Node Type.
	 */
	public NodeType(String type, Hashtable properties, DataSource ds, SemanticNet snSource){
		init();
		this.type = type;
		this.hProperties = properties;
		this.dSource = ds;
		this.snSource = snSource;
	}

	private void init(){
		this.hProperties = new Hashtable();
		hNodes = new Hashtable();
	}
	
	
	/*GETs I SETs*/
	
	
	
	public String getDescription() {
		return description;
	}
	

	public void setDescription(String description) {
		this.description = description;
	}
	

	public Hashtable getHProperties() {
		return hProperties;
	}
	

	public void setHProperties(Hashtable properties) {
		hProperties = properties;
	}
	
	public DataSource getDSource() {
		return dSource;
	}
	

	public void setDSource(DataSource source) {
		dSource = source;
	}
	

	/**
	 * 
	 * @return Tipus representat per l'objecte.
	 */
	public String getType() {
		return type;
	}
	
	
	/*FUNCIONALITATS DEL TipusRelacio*/
	/**
	 * Ask about the existens of the Node 'node'
	 * @return True if the Node node allready exists.
	 */
	public boolean existsNode(int idNode){
		boolean res = false;
		Node nIterated;
		/*
		Iterator iter = ((Collection) hNodes.values()).iterator();
		while (iter.hasNext()){
			nIterated = (Node) iter.next();
			if (nIterated.getIdNode() == idNode){
				return true;
			}
		}
		*/
		if (hNodes.containsKey(new Integer(idNode))){
			return true;
		}
		if (!res) {
			try {
				this.loadNode(idNode);
			} catch (SemanticException e) {return false;}
		}
		return res;
	}
	
	/**
	 * Add the node's given.
	 * @param nNewNode The new node to add.
	 * @return the created relation.
	 * @throws SemanticException 
	 */
	public Node addNode(Node nNewNode) throws SemanticException{
		//Comprova si el node ja existeix en la llista de nodes.
		//if (!this.existsNode(nNewNode.getIdNode())){
		Node n2 = (Node)hNodes.put(new Integer(nNewNode.getIdNode()), nNewNode);
		if ( n2 != null){
			if (!n2.getTerm().equalsIgnoreCase(nNewNode.getTerm())){
				SemanticException seE = new SemanticException("ExistentDifferentNode");
				seE.setCode(SemanticException.EXISTDIFERENTNODE);
				seE.setObject(n2);
				throw seE;
			}
			SemanticException seE = new SemanticException("ExistentNode");
			seE.setCode(SemanticException.EXISTENTNODE);
			seE.setObject(n2);
			throw seE;
		}	
		return nNewNode;	
	}
	
	public Node addNode(int idNode, String sNType, String term, String description, String category, String note, String history, Hashtable properties,SemanticNet snSource) throws SemanticException{
		Node nNewNode;
		nNewNode = new Node(idNode,sNType ,term,description,category,note, history,properties, snSource);
		
		return this.addNode(nNewNode);
	}
	
	public Node addNode(Hashtable dto) throws SemanticException{
		Node nNewNode;
		if (dto.containsKey("idNode")){	
			nNewNode = new Node(((Integer)dto.get("idNode")).intValue(),(String)dto.get("nodeType"), this.snSource);
			if (dto.containsKey("term")) nNewNode.setTerm((String)dto.get("term"));	
			if (dto.containsKey("category")) nNewNode.setCategory((String)dto.get("category"));	
			if (dto.containsKey("description")) nNewNode.setDescription((String)dto.get("description"));	
			if (dto.containsKey("note")) nNewNode.setNote((String)dto.get("note"));	
			if (dto.containsKey("history")) nNewNode.setHistory((String)dto.get("history"));
			if (dto.containsKey("properties")) nNewNode.setProperties((Hashtable)dto.get("properties"));			
			
			return this.addNode(nNewNode);		
			
		}else{
			/*En cas d'error llençar una excepció.*/
			SemanticException seE = new SemanticException("NonExistentNode");
			seE.setCode(SemanticException.NONEXISTENTNODE);
			throw seE;
		}
	}
	
	
	/**
	 * Returns the node with the given ID.
	 * @param idNode Given node.
	 * @return Node
	 * @throws SemanticException 
	 */
	public Node getNode(int idNode) throws SemanticException {
		try{
			Node n;
			if (this.existsNode(idNode)){
				n = (Node)hNodes.get(new Integer(idNode));
			}else{
				n = (Node)hNodes.get(new Integer(idNode));
			}
			if (n != null)
				return n;
			else{
				SemanticException seE = new SemanticException("NonExistentNode");
				seE.setCode(SemanticException.NONEXISTENTNODE);
				throw seE;
			}
		}catch(NullPointerException ne){
			SemanticException seE = new SemanticException("NonExistentNode");
			seE.setCode(SemanticException.NONEXISTENTNODE);
			throw seE;
		}
	}

	public List getNodes() {
		// TODO Auto-generated method stub
		List l = new ArrayList(this.hNodes.values());
		return l;
	}

	public void clone(NodeType ntNType) {
		// TODO Auto-generated method stub
		this.description = ntNType.getDescription();
		this.snSource = ntNType.snSource;
		this.setDSource(ntNType.getDSource());
		this.type = ntNType.getType();
		this.hProperties = ntNType.getHProperties();
		this.hNodes = new Hashtable();
	}

	
	/**
	 * Add new node. Adds a new node non existent on the DataSource to the System.
	 * @param nNewNode
	 * @return Returns inserted Node.
	 * @throws SemanticException
	 */
	public int addNewNode(Node nNewNode) throws SemanticException{
		int newIdNode;
		Hashtable dto = nNewNode.toDTO();
		newIdNode = this.dSource.addNewNode(dto);
		nNewNode.setIdNode(newIdNode);
		this.addNode(nNewNode);
		try{
			this.loadNode(newIdNode);
		}catch(SemanticException se){}
		
		return newIdNode;
	}
	
	
	/**
	 * Set node's parameters described into 'dto' with its values.
	 * @param idNode
	 * @param dto
	 * @return Returns modified node.
	 * @throws SemanticException 
	 */
	public Node setNode(int idNode, Hashtable dto) throws SemanticException {
		// TODO Auto-generated method stub
		Node nsn = this.getNode(idNode);
		Node nn = new Node(nsn.toDTO(), this.snSource);
		
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
			nsn.setProperties((Hashtable)dto.get("properties"));
		}
		
		try{
			this.dSource.setNode(idNode, this.getType(), dto);				
		}catch(SemanticException se){
			this.addNode(nn);
			se.setObject(nn);
			throw se;
		}
		
		return nsn;
	}

	/**
	 * Delete the node with the given ID.
	 * @param idNode
	 * @return Deleted node.
	 * @throws SemanticException 
	 */
	public Node delNode(int idNode) throws SemanticException {
		// TODO Auto-generated method stub
		Node dn;
		
		dn = this.getNode(idNode);
		this.dSource.delNode(idNode, this.getType());
		this.hNodes.remove(new Integer(idNode));
		return dn;
	}


	
	/**
	 * Accedeix al Repositori de dades i aconsegueix la informació necessaria per crear el nou node.
	 * @param idNode Identificador del node.
	 * @return Node carregat.
	 */
	public Node loadNode(int idNode) throws SemanticException{
		Node n = null;
		Hashtable dto;
		dto = dSource.getNode(idNode, this.getType());	

		n = this.addNode(dto);	
		
		return n;
	}

	
	/**
	 * Carrega tots els nodes amb id dins la llista donada.
	 * @param lNod Llistat de {idNode}
	 * @return Llistat de {idNode,SemanticError} amb els nodes que no s'han pogut carregar i l'error donat. Les relacions on aquest apareixien tampoc s'han carregat.
	 * @throws SemanticException
	 */
	public List loadNodes(List lNodes) throws SemanticException {
		// TODO Auto-generated method stub
		Hashtable hList = new Hashtable();
		Hashtable hDTO;
		Hashtable dto;
		Node n;
		ArrayList lError = new ArrayList();
		
		hList.put(this.getType(),lNodes);
		hDTO = dSource.getNodes(hList);
		
		Iterator itNodes = ((AbstractList) hDTO.get(this.getType())).iterator();
		while (itNodes.hasNext()){
			dto = (Hashtable) itNodes.next();
			try{
				n = this.addNode(dto);				
			}catch(Exception te){
				List l = new ArrayList();
				l.add((Integer)dto.get("idNode"));
				l.add(te);
				lError.add(l);
			}
		}
		return lError;
	}

	public int loadAllNodes() throws SemanticException {
		// TODO Auto-generated method stub
		ArrayList lType = new ArrayList();
		Hashtable hdto;
		Hashtable dto;
		Iterator iter;
		Enumeration en;
		Node nNewNode;
		int iQntt = 0;
		lType.add(this.getType());
		hdto = dSource.getAllNodes(lType);
		
		en = hdto.keys();	
		iter = ((List) hdto.get(this.getType())).iterator();
		while (iter.hasNext()){
			dto = (Hashtable) iter.next();
			//En cas d'error no cal q fem res.
			try{
				nNewNode = this.addNode(dto);				
				iQntt++;
			}catch(Exception te){
			}
		}
		
		return iQntt;
	}

	public Node refresh(int idNode) throws SemanticException {
		// TODO Auto-generated method stub
		this.loadNode(idNode);
		return this.getNode(idNode);
	}

	public List search(String query) throws SemanticException {
		// TODO Auto-generated method stub
		ArrayList lType = new ArrayList();
		ArrayList lRes = new ArrayList();
		int idNode;
		Iterator iter;
		
		lType.add(this.getType());
		Hashtable hdto = dSource.searchNode(query, lType);
		
		iter = ((List) hdto.get(this.getType())).iterator();
		while (iter.hasNext()){
			idNode = ((Integer)iter.next()).intValue();
			try{
				lRes.add(this.getNode(idNode));
			}catch(Exception te){
				//En cas d'error no cal q fem res.
			}
		}
	
		return lRes;
	}
	
	


	
}
