package edu.xtec.merli.semanticnet.elements;

import java.util.Hashtable;

import edu.xtec.semanticnet.DataSource;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticException;

public class CurriculumRelation extends RelationType {

	public CurriculumRelation(String arg0, Hashtable arg1, DataSource ds) {
		super(arg0, arg1,ds);
		// TODO Auto-generated constructor stub
	}
	public CurriculumRelation() {
		super("newRT", null);
		// TODO Auto-generated constructor stub
		
	}
	

	public Relation addNewRelation(Relation rel) throws SemanticException{
		/*Cal comprovar tipus de nodes dins la relació.*/
		super.addRelation(rel);
		return rel;		
	}
}
