package edu.xtec.merli.etiquetador.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.RecursMerli;
import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.basedades.RecursBD;
import edu.xtec.merli.mediateca.MediatecaBean;
import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.merli.utils.Utility;
import junit.framework.TestCase;

public class testDS  extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(testDS.class);
	}

	public final void testConnectarBD() throws ParseException{
//	SemanticInterface si = new SemanticInterface();
//		int valor = 55;
//	//	String result = Utility.createThesaurusList(((ArrayList)si.getThesaurus(valor)),"add");
//
//			// TODO Auto-generated constructor stub
//
//		SimpleDateFormat sDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
//		SimpleDateFormat sDateO = new SimpleDateFormat("dd/MMMM/yyyy");
//		Date d; 
//		d = sDate.parse("02-abr-2006 09:22:54 AM");
//System.out.println("dater:"+sDateO.format(d));
//		//Timestamp date = new Timestamp(sDate.getCalendar().getTimeInMillis());
//
//
//	
		
	}

	public final void testRecursBDadd() throws MerliDBException{
		/*RecursBD rb = new RecursBD();
		RecursMerli rm = new RecursMerli(3);
		rm.setTitle("jot");
		rm.setAmbit("2");
		rm.setDescription("description");
		rm.setUrl("http:77jarl");
		rm.setDifficulty("2");
		rb.addRecurs(rm);*/
	}
	
	public final void testRecursBDdel() throws MerliDBException{
		//RecursBD rb = new RecursBD();
//		rb.delRecurs(3);
	}
	
	
	
	
}
