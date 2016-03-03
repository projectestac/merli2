package edu.xtec.merli.ws.test;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import junit.framework.TestCase;
import edu.xtec.merli.ws.objects.ElementDUC;
import edu.xtec.merli.ws.objects.IdElement;
import edu.xtec.merli.ws.objects.ListDUC;

public class testWSDUC extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(testWSDUC.class);
	}

	public final void testGetElementDUC() throws SOAPException{
		
		TestWS tws = new TestWS();
		String sId = "8005";
		IdElement idElement = new IdElement(sId,"content");
		
		System.out.println("Test ws operacio get i id="+sId);
		SOAPElement se = tws.processOperation(TestWS.GETELEMENTDUC,idElement,TestWS.LOCAL);
		System.out.println("response="+se);
		
		ElementDUC elementDUC = new ElementDUC(se);
		
		System.out.println("\nFinal del test amb operacio getElementDUC "+elementDUC.getPosition());
		
//		if (elementDUC.getDescription() == null)
//			assertEquals(null,elementDUC.getDescription());
//		else
		//assertEquals("Llengua Oralc",elementDUC.getTerm());
		assertTrue(0 < elementDUC.getPosition());
	}
	

//	
//	public final void testGetDUC() throws SOAPException{
//		
//		TestWS tws = new TestWS();
//		
//		SOAPElement se = tws.processOperation(TestWS.GETDUC, null);
//		//System.out.println("response="+se);			
//		
//		System.out.println("\nFinal del test amb operacio getDUC ");//: "+elementDUC.toXml());
//		
//	}
//
	
	public final void testGetNivells() throws SOAPException{
		
		TestWS tws = new TestWS();
		
		System.out.println("Test ws operacio getNivells");
		SOAPElement se = tws.processOperation(TestWS.GETNIVELLS, null);
		ListDUC listDUC = new ListDUC(se);
		System.out.println("\nFinal del test amb operacio getNivells ");//: "+elementDUC.toXml());		
		}

	
}
