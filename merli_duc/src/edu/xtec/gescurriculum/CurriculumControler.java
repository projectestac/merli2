package edu.xtec.gescurriculum;

import java.util.Hashtable;

import org.apache.struts.action.ActionServlet;

import edu.xtec.merli.semanticnet.DataSourceSNJDBC;
import edu.xtec.semanticnet.SemanticException;

public class CurriculumControler extends ActionServlet {
	
	public CurriculumControler(){
		super(); 
		System.out.println("\nInitializing DUC.\n");
		DataSourceSNJDBC ds = new DataSourceSNJDBC();	
		Hashtable propDoc = new Hashtable();
		//String folderProp = "../../../";
		String folderProp = "/";
		String pathProp = "database.properties";
		propDoc.put("folder",folderProp);
		propDoc.put("path",pathProp);
		ds.init(propDoc);
	}
	
}
