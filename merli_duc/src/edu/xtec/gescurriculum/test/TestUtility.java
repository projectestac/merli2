package edu.xtec.gescurriculum.test;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.xtec.merli.utils.Utility;
import junit.framework.TestCase;

public class TestUtility extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(TestUtility.class);
	}

	public final void testCometes(){
		String upd = "casa, vavca, pallus,  ";
		upd = (String) upd.subSequence(0, upd.lastIndexOf(", "));
		System.out.println(upd);
	}
	
	public final void testLoadProperties() {
		//TODO Implement loadProperties().
		Properties p;
		String prop;
		try {
			prop="test.properties";
			p=Utility.loadProperties("/test/",prop);
			assertTrue(p.containsKey("name"));
			assertTrue(prop.equals(p.getProperty("name")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//assertTrue(false);
		}	
		
	}

	public final void testToList(){
		System.out.println(Utility.toList("casa,avet,pape, ras,  sa").toString());
	}
		
	
	public final void testToParaulaDB(){
		String aux = "l'esquena";
		String res = "l''esquena";
		
		aux = Utility.toParaulaDB(aux);
		assertEquals(aux,res);
	}
	
	public final void testToListDB(){
		ArrayList l = new ArrayList();
		l.add("id_tema");
		l.add("v_tema");
		l.add("3_tema");
		l.add("4_tema");
		l.add("5_tema");
		l.add("6_tema");

		String ls = Utility.toListDB(l,"a");			
	}

}
