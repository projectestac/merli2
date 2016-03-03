package edu.xtec.merli.thesaurus;


import java.sql.SQLException;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.utils.Utility;
import edu.xtec.util.db.ConnectionBeanProvider;
import junit.framework.TestCase;

public class ExecuteArcParse extends TestCase {

	public static void main(String[] args) throws Exception {
		//junit.swingui.TestRunner.run(ExecuteArcParse.class);
		System.out.println("lolo");
		executeArcParse();
	}

	public static void executeArcParse() throws Exception{
		
		try {
			ArCParse ap = new ArCParse();
			
			ap.loadXMLdocument("C:\\Documents and Settings\\jesus\\Escritorio\\MeRLí-AGREGA\\conversio APXTEC\\arc_ca.xml");
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
