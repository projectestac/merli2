package edu.xtec.semanticnet;

import java.util.Hashtable;

public class Relation {
	private String relationType;
	private int idSource;
	private String sourceType;
	private int idDest;	
	private String destType;

	
	/**
	 * Dona valor als atributs invariables de l'objecte Relation.
	 * Afageix la Relation al llistat de Relations del tipus de relació.
	 * @param tipus String
	 * @param idSource int
	 * @param sSType 
	 * @param idDest int
	 * @param sDType 
	 */
	public Relation(String tipus, int idSource, String sSType, int idDest, String sDType){
		this.relationType = tipus;	
		this.idDest = idDest;
		this.destType = sDType;
		this.idSource = idSource;
		this.sourceType = sSType;
	}


	public int getIdDest() {
		return idDest;
	}
	

	public void setIdDest(int idDest) {
		this.idDest = idDest;
	}
	

	public int getIdSource() {
		return idSource;
	}
	

	public void setIdSource(int idSource) {
		this.idSource = idSource;
	}
	


	public String getDestType() {
		return destType;
	}
	

	public void setDestType(String destType) {
		this.destType = destType;
	}
	

	public String getRelationType() {
		return relationType;
	}
	

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	

	public String getSourceType() {
		return sourceType;
	}
	

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	

	/**
	 * Implements the equivalence between two Relations, this one and the given relation.
	 * @return True if were the same relation.
	 */
	public boolean equals(Relation rela) {
		// TODO Auto-generated method stub
		if(this.idSource == rela.idSource){
			if (this.idDest == rela.idDest){
				if (this.relationType.equals(rela.getRelationType())
					&& this.destType.equals(rela.getDestType())
					&& this.sourceType.equals(rela.getSourceType())){
						return true;
				}
			}
		}
		return false;
	}
	


	public Hashtable toDTO(){
		Hashtable dto = new Hashtable();

		dto.put("idSource",new Integer(this.getIdSource()));
		dto.put("sourceType",this.getSourceType());
		dto.put("relationType",this.getRelationType());
		dto.put("idDest",new Integer(this.getIdDest()));
		dto.put("destType",this.getDestType());
		
		return dto;
	}
	
}
