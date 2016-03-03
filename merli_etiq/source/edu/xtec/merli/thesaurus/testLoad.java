package edu.xtec.merli.thesaurus;


import java.sql.SQLException;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.utils.Utility;
import edu.xtec.util.db.ConnectionBeanProvider;
import junit.framework.TestCase;

public class testLoad extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(testLoad.class);
	}

	public final void testload() throws Exception{
		ThesaurusLoad tl;
		/*
		 * CAL CANVIAR ELS PARAMETRES DE CONNEXIÓ A LA BBDD 
		 * EN LA CLASSE ThesaurusLoad.java.
		 */ 
		try {
			tl = new ThesaurusLoad();
			
			//tl.loadXMLdocument("c:\\Java\\Desenv\\Metiq\\web\\70.xml");
			//tl.convertir();
			tl.convertirRelCurriculum();
			
		} catch (MerliDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
