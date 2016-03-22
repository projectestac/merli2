package edu.xtec.semanticnet;

import java.util.Hashtable;
import java.util.List;

public interface DataSource {

	/**
	 * Data Source initialization. 'properties' contains all the needed information to initialize it.
	 * @param properties Set of properties.
	 */
	public void init(Hashtable properties) throws SemanticException;
	
	/**
	 * The DataSource returns the information needed to create the Node object with the identifier idNode.
	 * @param idNode Identifier of the node.
	 * @param sNType 
	 * @return Hashtable with the information. The information was in String format.
	 * @throws ThesaurusException
	 */
	public Hashtable getNode(int idNode, String sNType) throws SemanticException;
	
	/**
	 * Return information about the nodes related with the node 'idNode'.
	 * @param idNode Identifier of the node.
	 * @param sNType 
	 * @param types Type of relation searched.
	 * @param direction Direction of the relations search.
	 * @return List of DTO's with the Relation's data.
	 * @throws ThesaurusException
	 */
	public List getRelations(int idNode, String sNType, List types, int direction) throws SemanticException;

	/**
	 * DataSource return the data about the nodes wich identifier where in the 'lnod' list.
	 * @param hnod Hashtable of idNode's.
	 * @return List of DTO's with Nodes data.
	 * @throws ThesaurusException
	 */
	public Hashtable getNodes(Hashtable hnod) throws SemanticException;
	
	/**
	 * Load data of all nodes in the dataSource with the givin types.
	 * @param types Type of nodes to load.
	 * @return List of DTO's with Node's data.
	 * @throws ThesaurusException
	 */
	public Hashtable getAllNodes(List types) throws SemanticException;
	
	/**
	 * Load data of all relations in the dataSource.
	 * @param types
	 * @return List of DTO's with Realtion's data.
	 * @throws ThesaurusException
	 */
	public Hashtable getAllRelations(List types) throws SemanticException;

	/**
	 * Add a new node to system. This new node is described in the gived 'DTO'.
	 * @param DTO Hashtable describing new nodes data.
	 * @return Identifier of new node.
	 * @throws SemanticException
	 */
	public int addNewNode(Hashtable DTO) throws SemanticException;

	/**
	 * Update values of the gived node. New data are described in hashtable 'DTO'.
	 * @param idNode Node related to identifier.
	 * @param sNType Node related to type.
	 * @param dto Hashtable describing new nodes data.
	 * @throws SemanticException
	 */
	public void setNode(int idNode, String sNType, Hashtable dto) throws SemanticException;

	/**
	 * Delete given node from system.
	 * @param idNode Node related to identifier.
	 * @param sNType Node related to type.
	 * @throws SemanticException
	 */
	public void delNode(int idNode, String sNType) throws SemanticException;

	/**
	 * Add new relation to system. This new relation is described in the gived 'dto'.
	 * @param dto Hashtable describing the relation to add to the system.
	 * @throws SemanticException
	 */
	public void addNewRelation(Hashtable dto) throws SemanticException;

	/**
	 * Update data from relation. 'dtoOldNode' describe the original relation and 'dtoChanges'
	 * @param dtoOldNode Dto decribing the original relation.
	 * @param dtoChanges Dto containing new values.
	 * @throws SemanticException
	 */
	public void setRelation(Hashtable dtoOldNode, Hashtable dtoChanges) throws SemanticException;

	/**
	 * Delete the described relation in 'dto' from system.
	 * @param dto Hashtable describing the relation to delete.
	 * @throws SemanticException
	 */
	public void delRelation(Hashtable dto) throws SemanticException;

	/**
	 * Delete from system every 'sRType' type relation related to the given node. 
	 * @param idNode Node related to identifier.
	 * @param sNType Node related to type.
	 * @param sRType Relation type to delete.
	 * @throws SemanticException
	 */
	public void delRelationsNode(int idNode, String sNType, String sRType) throws SemanticException;

	/**
	 * Search nodes conataining the string 'query' in its 'term' or 'description'. Search only in nodes
	 * which type are in 'type' nodes types list.
	 * @param query The string searched.
	 * @param type Nodes types to search.
	 * @return Hashtable containing every identifier. {"nodeType1"=[id1,id2,id3],"nodeType2"=[id2,id4,id6]}
	 */
	public Hashtable searchNode(String query, List type) throws SemanticException;

}
