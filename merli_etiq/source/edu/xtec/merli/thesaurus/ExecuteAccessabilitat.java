package edu.xtec.merli.thesaurus;


import java.sql.SQLException;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.utils.Utility;
import edu.xtec.util.db.ConnectionBeanProvider;
import junit.framework.TestCase;

public class ExecuteAccessabilitat extends TestCase {

	public static void main(String[] args) throws Exception {
		//junit.swingui.TestRunner.run(ExecuteArcParse.class);
		System.out.println("lolo");
		executeArcParse();
	}

	public static void executeArcParse() throws Exception{
		
		try {
			AccessParse ap = new AccessParse();
			
			ap.parseDocument("C:\\eclipse-workspace\\XTEC\\data\\ETB-LRE_MEC-CCAA_V.1.0_ca.xml","ca");
			ap.parseDocument("C:\\eclipse-workspace\\XTEC\\data\\ETB-LRE_MEC-CCAA_V.1.0_es.xml","es");
			ap.parseDocument("C:\\eclipse-workspace\\XTEC\\data\\ETB-LRE_MEC-CCAA_V.1.0_en.xml","en");
			ap.importarXMLdocument("C:\\eclipse-workspace\\XTEC\\data\\ids_importar.xml");
			//ap.convertir();
			
		} catch (MerliDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
