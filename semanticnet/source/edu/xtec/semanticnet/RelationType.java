package edu.xtec.semanticnet;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class RelationType {
	protected String type;
	protected String description;
	
	protected Hashtable hProperties;
	protected List lRelations;
	
	protected DataSource dSource;
	
	public static final int SOURCE = 1;
	public static final int DEST = 2;
	public static final int ALL = 3;
	
	/**
	 * Constructor. 
	 * @param type  Identifier type of the Relation.
	 */
	public RelationType(String type, DataSource ds){
		init();
		this.type = type;
		this.dSource = ds;
	}
	
	/**
	 * Constructor. 
	 * @param type  Identifier type of the Relation.
	 * @param properties  Needed propierties of the Relation Type.
	 */
	public RelationType(String type, Hashtable properties, DataSource ds){
		init();
		this.type = type;
		this.hProperties = properties;
		this.dSource = ds;
	}

	private void init(){
		this.hProperties = new Hashtable();
		lRelations = new ArrayList();
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

	/**
	 * 
	 * @return Tipus representat per l'objecte.
	 */
	public String getType() {
		return type;
	}
	
	
	/*FUNCIONALITATS DEL TipusRelacio*/
	/**
	 * Ask about the existens of the Relation 'rela'
	 * @return True if the Relation rela allready exists.
	 */
	public boolean existsRelation(Relation rela){
		boolean res = false;
		Relation rl2;
		Iterator iter = lRelations.iterator();
		while (iter.hasNext()){
			rl2 = (Relation) iter.next();
			if (rl2.equals(rela)){
				return true;
			}
		}
		return res;
	}
	
	/**
	 * Add a Relation with the node's given.
	 * @param idSource The identifier of the Source node of the relation.
	 * @param idDest The identifier of the destination node of the relation.
	 * @return the created relation.
	 */
	public Relation addRelation(int idSource, String sSType, int idDest, String sDType){
		Relation res = new Relation(this.type, idSource, sSType, idDest, sDType);
		//Comprova si la relació ja existeix en la llista de relations.
		if (!this.existsRelation(res)){
			if (lRelations.add(res)){
				return res;				
			}
		}
		return null;
	}
	
	/**
	 * Returns the relations related as 'direction' indicates with the given node.
	 * @param idNode Given node.
	 * @param sNType 
	 * @param iDirection 1-> node is Source; 2-> node is Destination; 3-> both options. 
	 * @return {Relation} Relation list.
	 */
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

	public Relation addRelation(Hashtable dto) {
		// TODO Auto-generated method stub
		int idS,idD;
		String relType,sSType,sDType;
		idS = ((Integer)dto.get("idSource")).intValue();
		sSType = (String)dto.get("sourceType");
		idD = ((Integer)dto.get("idDest")).intValue();
		sDType = (String)dto.get("destType");
		relType = (String)dto.get("relationType");
		
		Relation res = new Relation(relType, idS, sSType, idD, sDType);
		
		if (!this.existsRelation(res)){
			if (lRelations.add(res)){
				return res;				
			}
		}
		return res;
	}
	
	/**
	 * Adds new relation if it do not exists before.
	 * @param rNRel Relation to add to system.
	 * @return Inserted Relation or null.
	 * @throws SemanticException 
	 */
	public Relation addRelation(Relation rNRel) throws SemanticException {
		// TODO Auto-generated method stub
		
		if (!this.existsRelation(rNRel)){
			if (lRelations.add(rNRel)){
				try{
					Hashtable dto = rNRel.toDTO();			
					this.dSource.addNewRelation(dto);
				}catch(SemanticException se){
					lRelations.remove(rNRel);
					throw se;
				}
				catch(NullPointerException npe){
					SemanticException seE = new SemanticException("NonExistentNodeType");
					seE.setCode(SemanticException.NONEXISTENTNODETYPE);
					seE.setObject(rNRel.getRelationType());
					throw seE;
				}
				return rNRel;				
			}
		}
		return null;
	}
	
	

	public void clone(RelationType rtRelType) {
		// TODO Auto-generated method stub
		this.description = rtRelType.getDescription();
		this.hProperties = rtRelType.hProperties;
		this.lRelations = rtRelType.lRelations;
		this.type = rtRelType.type;		
		this.dSource = rtRelType.dSource;
	}

	
	/**
	 * Delete all relations related with the gived node.
	 * @param idNode Id of the node.
	 * @param sNType Type of the node.
	 * @return List with deleted nodes.
	 * @throws SemanticException
	 */
	public List delRelationsNode(int idNode, String sNType) throws SemanticException {
		// TODO Auto-generated method stub
		Relation rel;
		List ldel = new ArrayList();
		for (int i =0;i<lRelations.size();i++){
			rel = (Relation) lRelations.get(i);
			if (rel.getIdSource() == idNode && sNType.compareTo(rel.getSourceType())==0){
				this.lRelations.remove(i);
				this.dSource.delRelationsNode(idNode, sNType, this.getType());
				i--;
				ldel.add(rel);
			}else{
				if (rel.getIdDest() == idNode && sNType.compareTo(rel.getDestType())==0){
					this.lRelations.remove(i);
					this.dSource.delRelationsNode(idNode, sNType, this.getType());
					i--;
					ldel.add(rel);
				}
			}
		}		
		
		return ldel;
	}

	/**
	 * Adds new relation if it do not exists before.
	 * @param rNewRel
	 * @return Inserted Relation or null.
	 */
/*	public Relation addNewRelation(Relation rNewRel) {
		// TODO Auto-generated method stub
		if (!this.existsRelation(rNewRel)){
			if (lRelations.add(rNewRel)){
				return rNewRel;				
			}
		}		
		return null;
	}*/

	/**
	 * Search the original relation and change its values.
	 * @param rel original relation to change.
	 * @param dto Contents the parameters to change and its values.
	 * @return Returns modified Relation.
	 * @throws SemanticException 
	 */
	public Relation setRelation(Relation rel, Hashtable dto) throws SemanticException {
		// TODO Auto-generated method stub
		Relation rl2;
		Iterator iter = lRelations.iterator();
		while (iter.hasNext()){
			rl2 = (Relation) iter.next();
			if (rl2.equals(rel)){
				if (dto.containsKey("idSource")){
					rl2.setIdSource(((Integer)dto.get("idSource")).intValue());
				}
				if (dto.containsKey("sourceType")){
					rl2.setSourceType((String)dto.get("sourceType"));
				}
				if (dto.containsKey("idDest")){
					rl2.setIdSource(((Integer)dto.get("idDest")).intValue());
				}
				if (dto.containsKey("destType")){
					rl2.setSourceType((String)dto.get("destType"));
				}
				return rl2;
			}
		}

		try{
			this.dSource.setRelation(rel.toDTO(), dto);
		}catch(SemanticException se){
			try {
				this.addRelation(rel);
			} catch (SemanticException e) {
			}
			throw se;
		}
		return null;
	}

	
	/**
	 * Delete the gived relation.
	 * @param rn
	 * @throws SemanticException
	 */
	public void delRelation(Relation rn) throws SemanticException {
		// TODO Auto-generated method stub
		Relation rl2 = null;
		Iterator iter = lRelations.iterator();
		while (iter.hasNext()){
			rl2 = (Relation) iter.next();
			if (rl2.equals(rn)){
				break;
			}
		}
		Hashtable dto = rl2.toDTO();
		this.dSource.delRelation(dto);
		if(!lRelations.remove(rl2)){
			SemanticException seE = new SemanticException("NonDeletedRelation");
			seE.setCode(SemanticException.NONDELETEDRELATION);
			seE.setObject(rn);
			throw seE;
		}
	}

	public List loadRelations(int idNode, String sNType, int direction) throws SemanticException {
		// TODO Auto-generated method stub
		ArrayList lType = new ArrayList();
		ArrayList lRes;
		lType.add(this.getType());
		lRes = (ArrayList) dSource.getRelations(idNode, sNType, lType, direction);
		
		return lRes;
	}

	public List loadRelations() throws SemanticException {
		// TODO Auto-generated method stub
		ArrayList lType = new ArrayList();
		Hashtable hRes;
		lType.add(this.getType());
		hRes = (Hashtable) dSource.getAllRelations(lType);
		
		return (List) hRes.get(this.getType());
	}

	public void setDSource(DataSource source2) {
		// TODO Auto-generated method stub
		this.dSource = source2;
	}
	

	
}
