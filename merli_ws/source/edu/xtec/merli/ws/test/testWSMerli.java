package edu.xtec.merli.ws.test;

import java.util.ArrayList;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import junit.framework.TestCase;
import edu.xtec.merli.utils.Utility;
import edu.xtec.merli.ws.objects.*;

public class testWSMerli extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(testWSMerli.class);
	}
	
private static final int SERVIDOR = TestWS.TEST;
/*
	public final void testAddWithIdentifier(){
		TestWS tws = new TestWS();
		Lom lom;

		//Crear un LOM buit amb les dades mínimes necessàries
		String sText = "testAddWithIdentifier";
		lom = new Lom();
		lom.getGeneral().setIdentifier(new Identifier("CELEBRATE", "MERLI789"));
		lom.getGeneral().getTitle().setString(sText);
		lom.getGeneral().getDescription().setString(sText);
		lom.getTechnical().setLocation(sText);
		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("", "publisher", "role"),new Entity("sarjona","","",""),new Date()));
		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("", "author", "role"),new Entity("mabad1","","",""),new Date()));
		lom.getRights().getCopyRightAndOtherRestrictions().setValue("no");
		lom.getRights().getCopyRightAndOtherRestrictions().setId("1");
		
		ArrayList alFormat = new ArrayList();
		//alFormat.add()
		lom.getTechnical().setFormat(alFormat);
		
		tws = new TestWS();
		System.out.println("\n####################");
		System.out.println("Test ws operacio add");
		SOAPElement se = tws.processOperation(TestWS.ADDRESOURCE,lom);
		IdResource idResource = null;
		
		try {
			idResource = new IdResource(se);
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		
		tws =  new TestWS();
		se = tws.processOperation(TestWS.GETRESOURCE, idResource);			
		lom = new Lom(se);
		assertEquals(sText, lom.getGeneral().getDescription().getString());
		
		System.out.println("\nFinal del test amb operacio add. Nou id:"+idResource.getIdentifier());
		
		//assertEquals(ts.getYear(),dt.getYear());
	}	
	
* / 
	public final void testAddResourceManual(){
		TestWS tws = new TestWS();
		Lom lom;

		//Crear un LOM buit amb les dades mínimes necessàries
		lom = new Lom();
		lom.getGeneral().getTitle().setString("titulo");
		lom.getGeneral().getDescription().setString("description");
		lom.getTechnical().setLocation("location");
		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("", "publisher", "role"),new Entity("jpanzano","","",""),new Date()));
		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("", "author", "role"),new Entity("jpanzano","","",""),new Date()));
		lom.getRights().getCopyRightAndOtherRestrictions().setValue("no");
		lom.getRights().getCopyRightAndOtherRestrictions().setId("1");
		
		ArrayList alFormat = new ArrayList();
		//alFormat.add()
		lom.getTechnical().setFormat(alFormat);
		
		tws = new TestWS();
		System.out.println("\n####################");
		System.out.println("Test ws operacio add");
		SOAPElement se = tws.processOperation(TestWS.ADDRESOURCE,lom,SERVIDOR);
		IdResource idResource = null;
		
		try {
			idResource = new IdResource(se);
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		
		tws =  new TestWS();
		se = tws.processOperation(TestWS.GETRESOURCE, idResource,SERVIDOR);			
		lom = new Lom(se);
		assertEquals("description", lom.getGeneral().getDescription().getString());
		
		System.out.println("\nFinal del test amb operacio add. Nou id:"+idResource.getIdentifier());
		
		//assertEquals(ts.getYear(),dt.getYear());
	}	

	
/*/

/*
	public final void testAddResource(){
		
		TestWS tws = new TestWS();
		Lom lom;

		//Trampa
		IdResource idResource = new IdResource();// = new IdResource(String.valueOf(864),"MERLI");
		SOAPElement se ;//= tws.processOperation(TestWS.GETRESOURCE,idResource);
//		lom = new Lom(se);
		//lom.getGeneral().setDescription(new LangString("Nova descripció", "ca"));
		//Fin trampa
		//lom = new Lom();
		//Crear un Lom buit. Amb les dades mínimes necessàries.
		//for (int i=0; i <1000;i++){
			tws = new TestWS();
			lom = new Lom();
		
		lom.getGeneral().getTitle().setString("Prova MeRLí add. ");
		lom.getGeneral().getDescription().setString("Nova descripció més llarga. # Comproveu que l'adreça no tingui errors en escriure    ww.example.com en comptes de    www.example.com#   Si no podeu carregar cap pàgina, comproveu la connexió a la xarxa del vostre    ordinador.#   Si el vostre ordinador o xarxa estan protegits per un tallafocs o un servidor intermediari,assegureu-vos que el Firefox té permisos per a accedir al W");
		lom.getTechnical().setLocation("http://www.merli.cat");
		lom.getMetaMetaData().getContributeList().add(new Contribute(new SourceValue("","publisher",""),new Entity("jpanzano","","",""),new Date()));
		lom.getMetaMetaData().getContributeList().add(new Contribute(new SourceValue("","creator",""),new Entity("jpanzano","","",""),new Date()));
		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("","author",""),new Entity("jpanzano","","",""),new Date()));
		lom.getRights().getCopyRightAndOtherRestrictions().setValue("Non commercial");

		ArrayList cl = new ArrayList();
		cl.add(new SourceValue("","Pre-school",""));
		cl.add(new SourceValue("","Compulsory-education",""));
		cl.add(new SourceValue("","Compulsory education",""));
		lom.getEducational().setContextList(cl);
		
		lom.getEducational().setTypicalAgeRange("15-17");
		
		Classification c = new Classification();
		TaxonPath tp = new TaxonPath();
		tp.setSource(new LangString("AMBIT","ca"));
		ArrayList al = new ArrayList();
		al.add(new Taxon("",new LangString("QV","ca")));
		al.add(new Taxon("",new LangString("JClic","ca")));
		tp.setTaxonList(al);
		c.setTaxonPath(tp);
		c.setDescription(new LangString("ca","nocas"));
		c.setPurpose(new SourceValue("AMBIT","competency","purpose"));
		
		cl = new ArrayList();
		cl.add(c);
		lom.setClassificationList(cl);
		
		
		
		//((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(0)).getEntry().setString("CRP");
		
		
//		System.out.println("####################");
//		System.out.println("Test ws operacio add");
		se = tws.processOperation(TestWS.ADDRESOURCE,lom,SERVIDOR);	
		
		System.out.println("end add");
		//}
		try {
			idResource = new IdResource(se);
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		
//		tws =  new TestWS();
//		se = tws.processOperation(TestWS.GETRESOURCE, idResource);			
//		lom = new Lom(se);
//		assertEquals("Nova descripció", lom.getGeneral().getDescription().getString());
////		
		System.out.println("\nFinal del test amb operacio add. Nou id:"+idResource.getIdentifier());
		
		//assertEquals(ts.getYear(),dt.getYear());
	}
*/	
	
	/*
	public final void testUnpublishResource(){
		
		TestWS tws = new TestWS();

		IdResource idResource = new IdResource("2108", "MERLI");		

		System.out.println("Test ws operacio Unpublish");
		SOAPElement se = tws.processOperation(TestWS.UNPUBLISHRESOURCE,idResource);		
		
		try {
			idResource = new IdResource(se);
		} catch (SOAPException e) {
			e.printStackTrace();
			System.out.println("EXCEPTION delete");
		}
		
		try{
			tws =  new TestWS();
			se = tws.processOperation(TestWS.GETRESOURCE, idResource);			
			Lom lom = new Lom(se);
			assertTrue(true);
		}catch (Exception e){
			assertTrue(false);
		}
		
		
		System.out.println("\nFinal del test amb operacio del:"+idResource.getIdentifier());
		
		//assertEquals(ts.getYear(),dt.getYear());
	}


	public final void testDelResource(){
		
		TestWS tws = new TestWS();

		IdResource idResource = new IdResource("2108", "MERLI");		

		System.out.println("Test ws operacio del");
		SOAPElement se = tws.processOperation(TestWS.DELRESOURCE,idResource);		
		
		try {
			idResource = new IdResource(se);
		} catch (SOAPException e) {
			e.printStackTrace();
			System.out.println("EXCEPTION delete");
		}
		
		try{
			tws =  new TestWS();
			se = tws.processOperation(TestWS.GETRESOURCE, idResource);			
			Lom lom = new Lom(se);
			assertTrue(false);
		}catch (Exception e){
			assertTrue(true);
		}
		
		
		System.out.println("\nFinal del test amb operacio del:"+idResource.getIdentifier());
		
		//assertEquals(ts.getYear(),dt.getYear());
	}

	
	*/

/*
	public final void testSetResource() throws SOAPException{
		
		TestWS tws = new TestWS();
		Lom lom;
		
		String sId = "3700";

		System.out.println("Inici test SET.");
		IdResource idResource = new IdResource(sId, "MERLI");
		SOAPElement se = tws.processOperation(TestWS.GETRESOURCE,idResource,SERVIDOR);
		lom = new Lom(se);

		//Modificar el Lom.
		String sText = "http://url.com";
		lom.getGeneral().getTitle().setString("Test WS 0605-> "+sText);
		//lom.getGeneral().getDescription().setString(lom.getGeneral().getDescription().getString()+"1");
		String dosmil="";
		for (int i=0; i < 200; i++)
			dosmil += ".sn10chrs.";
		lom.getGeneral().getDescription().setString(dosmil);
		
		lom.getTechnical().setLocation(sText);
//		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("","author",""),new Entity("algu","","",""),new Date()));
//		lom.getMetaMetaData().getContributeList().add(new Contribute(new SourceValue("","publisher",""),new Entity("abachill","","",""),new Date()));
		
		TaxonPath tp = new TaxonPath();
		tp.setSource(new LangString("AMBIT","ca"));
		ArrayList al = new ArrayList();
		al.add(new Taxon("",new LangString("QV","ca")));
		al.add(new Taxon("",new LangString("JClic","ca")));
		tp.setTaxonList(al);
		((Classification)lom.getClassificationList().get(0)).setTaxonPath(tp);
		
		System.out.println(((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(0)).getEntry().getString());

//		((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(0)).getEntry().setString(sText);
//		((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(0)).setId("");
//		sText = "XTEC";
//		((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(1)).getEntry().setString(sText);
//		((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(1)).setId("");
		
		
		ArrayList cl = new ArrayList();
		cl.add(new SourceValue("","pre-school",""));
		//cl.add(new SourceValue("","Compulsory-education",""));
		//cl.add(new SourceValue("","compulsory education",""));
		lom.getEducational().setContextList(cl);
		
		lom.getEducational().setTypicalAgeRange("0-9");
		
		tws = new TestWS();
		System.out.println("Test ws operacio set");		
		se = tws.processOperation(TestWS.SETRESOURCE,lom,SERVIDOR);	

//		 tws = new TestWS();
//		 idResource = new IdResource(sId,"MERLI");
//		
//		 System.out.println("Test ws operacio get id="+sId);
		// se = tws.processOperation(TestWS.GETRESOURCE,idResource,SERVIDOR);
		
		// lom = new Lom(se);
//		 //fin feo
//		 
		//assertEquals("Test WS 0605-> "+sText, lom.getGeneral().getTitle().getString());
		//assertEquals(sText, lom.getGeneral().getDescription().getString());
		//assertEquals(sText, lom.getTechnical().getLocation());
//		System.out.println("\nFinal del test amb operacio set:"+idResource.getIdentifier());	
//		
	}
	
*/	

/**/
	public final void testGetResource() throws SOAPException{

		TestWS tws = new TestWS();
		String sId = "3697";//3690";
		IdResource idResource = new IdResource(sId,"MERLI");
		System.out.println("Test ws operacio GetResource amb id="+sId);
		SOAPElement se = tws.processOperation(TestWS.GETRESOURCE, idResource,SERVIDOR);
		
		Lom lom = new Lom(se);
		

		String sText = "http://url.com";
		//System.out.println("\nFinal del test amb operacio get: "+lom.getGeneral().getTitle().getString());
		//assertEquals(ts.getYear(),dt.getYear());
		//System.out.println(((Contribute)lom.getLifeCycle().getContributeList().get(0)).getEntity().getUsername());
		//System.out.println(((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(0)).getEntry().getString());
//		assertEquals(sText, ((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(0)).getEntry().getString());
//		sText = "XTEC";
//		assertEquals(sText, ((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(1)).getEntry().getString());
		//assertEquals("aleix", ((Contribute)lom.getMetaMetaData().getContributeList().get(0)).getEntity().getUsername());
		assertEquals("Test WS 0605-> "+sText, lom.getGeneral().getTitle().getString());
		//assertEquals(sText, lom.getGeneral().getDescription().getString());
		//assertEquals(sText, lom.getTechnical().getLocation());
	}

	
}
