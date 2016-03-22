package edu.xtec.semanticnet.thesaurus;

import edu.xtec.semanticnet.RelationType;

public class RelationThesaurus extends RelationType {

	public RelationThesaurus() {
		super("new",null);
		// TODO Auto-generated constructor stub
	}
/*
	public List getRelations(int idNode, String sNType, int iDirection) {
		List lRes = new ArrayList();
		Relation rel;
		Iterator iter = lRelations.iterator();
		while (iter.hasNext()){
			rel = (Relation) iter.next();
			switch (iDirection){
				case RelationType.SOURCE:
					if (rel.getIdSource() == idNode && rel.getSourceType().equals(sNType)){
						lRes.add(rel);
					}break;
				case RelationType.DEST:
					if (rel.getIdDest() == idNode && rel.getDestType().equals(sNType)){
						lRes.add(rel);
					}break;
				case RelationType.ALL:
					if ((rel.getIdDest() == idNode && rel.getDestType().equals(sNType)) || (rel.getIdSource() == idNode && rel.getSourceType().equals(sNType))){
						lRes.add(rel);
					}					
			}
		}
		return lRes;
	}
	*/
}


 