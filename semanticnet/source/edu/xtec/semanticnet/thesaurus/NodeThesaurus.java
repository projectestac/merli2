package edu.xtec.semanticnet.thesaurus;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticException;
import edu.xtec.semanticnet.SemanticNet;

public class NodeThesaurus extends Node {

	public NodeThesaurus(Hashtable properties, SemanticNet snSource) {
		super(properties, snSource);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Constructor to create a NodeThesaurus with the parameters of an existent node.
	 * @param nOrig Node used to take any parameter needed.
	 */
	public NodeThesaurus(Node nOrig){
		super(nOrig.getIdNode(),nOrig.getNodeType(),nOrig.getTerm(),nOrig.getDescription(),nOrig.getCategory(),nOrig.getNote(),nOrig.getHistory(), nOrig.getProperties(), nOrig.getSemanticNet());		
	}

	/**
	 * Return the relations and if node has a relation USE returns the USE node related relations.
	 */
	public Hashtable getRelations(List rTypes, int iDirection) throws SemanticException{
		String sTipus;
		Iterator iter = rTypes.iterator();
		while (iter.hasNext()){
			if (!this.isLoaded((String)iter.next())) {
				this.loadRelations();
			}
		}
		Hashtable hPral= this.getSemanticNet().getRelations(this.getIdNode(), this.getNodeType(), iDirection);
		List lUSE = this.getSemanticNet().getRelations(this.getIdNode(),this.getNodeType(),"USE",RelationType.SOURCE);
		for (int i = 0;i< lUSE.size();i++){
			//exceptuar les relacions  amb desti XX i tipus USE
			Hashtable hTemp = this.getSemanticNet().getRelations(((Relation)lUSE.get(i)).getIdDest(), ((Relation)lUSE.get(i)).getDestType(), iDirection);
			Enumeration enRT =  hTemp.keys();
			while (enRT.hasMoreElements()){
				sTipus = (String) enRT.nextElement();
				//Els casos USE no hi tenen cap relació. 
				if (sTipus.compareTo("USE") != 0){
					List l = (List) hTemp.get(sTipus);
					if(!hPral.containsKey(sTipus)){
						hPral.put(sTipus,l);
					}else{
						((List)hPral.get(sTipus)).addAll(l);
					}
				}
			}
		}
		return hPral; 
	}
	
	
	/**
	 *  Return the relations and if node has a relation USE returns the USE node related relations.
	 */
	public List getRelations(String relationType, int iDirection) throws SemanticException{
		//return snSource.getRelations(this.idNode, this.nodeType, relationType, iDirection);
		
		String sTipus;
		if (!this.isLoaded(relationType)) {
			this.loadRelations();
		}
		List lPral= this.getSemanticNet().getRelations(this.getIdNode(), this.getNodeType(), relationType, iDirection);
		List lUSE = this.getSemanticNet().getRelations(this.getIdNode(),this.getNodeType(),"USE",RelationType.SOURCE);
		for (int i = 0;i< lUSE.size();i++){
			//exceptuar les relacions  amb desti XX i tipus USE
			List lTemp = this.getSemanticNet().getRelations(((Relation)lUSE.get(i)).getIdDest(), ((Relation)lUSE.get(i)).getDestType(), relationType, iDirection);
			lPral.addAll(lTemp);
		}	
		
		return lPral; 
	}
	
	
	
	protected int loadRelations() throws SemanticException{
		int num = 0;
		num = this.getSemanticNet().loadRelations(this.getIdNode(), this.getNodeType(), RelationType.ALL);

		this.setLoaded(this.getNodeType(), true);
		return num;
	}
	
}
