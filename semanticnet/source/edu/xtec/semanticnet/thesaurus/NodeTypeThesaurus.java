package edu.xtec.semanticnet.thesaurus;

import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.NodeType;
import edu.xtec.semanticnet.SemanticException;

public class NodeTypeThesaurus extends NodeType {

	/**
	 * Constructor for NodeTypeThesaurus. It must be followed for an initialization givin 
	 * the correct data.
	 *
	 */
	public NodeTypeThesaurus() {
		super("T", null,null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Return desired node like NodeThesaurus class. 
	 * @throws SemanticException 
	 */
	public Node getNode(int idNode) throws SemanticException {
		return (NodeThesaurus)super.getNode(idNode);//.hNodes.get(new Integer(idNode));
	}

	/**
	 * Insert any node into the system like a NodeThesaurus class.
	 */
	public Node addNode(Node nNewNode) throws SemanticException{
		//Comprova si el node ja existeix en la llista de nodes.
		NodeThesaurus nNewNT = new NodeThesaurus(nNewNode);
		return super.addNode(nNewNT);
		/*if (!this.existsNode(nNewNT.getIdNode())){
			Node n2 = (NodeThesaurus)hNodes.put(new Integer(nNewNT.getIdNode()), nNewNT);
			if ( n2 != null){
				if (n2.getTerm().equalsIgnoreCase(nNewNT.getTerm())){
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
			return nNewNT;		
		}
		*///return null;
	}
}
