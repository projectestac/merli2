package edu.xtec.merli.semanticnet.elements;

import java.util.Hashtable;

import edu.xtec.semanticnet.DataSource;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticException;

public class CurriculumRelation extends RelationType {

	public CurriculumRelation(String arg0, Hashtable arg1, DataSource ds) {
		super(arg0, arg1,ds);
		//System.out.println("Curriculumrelations initialized..");
	}
	public CurriculumRelation() {
		super("newRT", null);
		//System.out.println("Curriculumrelations initialized..");
	}
	

	public Relation addNewRelation(Relation rel) throws SemanticException{
		/*Cal comprovar tipus de nodes dins la relació.*/
		super.addRelation(rel);
		return rel;		
	}
}
