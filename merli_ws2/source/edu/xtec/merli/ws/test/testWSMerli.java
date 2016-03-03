package edu.xtec.merli.ws.test;

import java.util.ArrayList;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import junit.framework.TestCase;
import edu.xtec.merli.ws.objects.Contribute;
import edu.xtec.merli.ws.objects.Date;
import edu.xtec.merli.ws.objects.DescripcioFisica;
import edu.xtec.merli.ws.objects.Duration;
import edu.xtec.merli.ws.objects.Entity;
import edu.xtec.merli.ws.objects.IdResource;
import edu.xtec.merli.ws.objects.LangString;
import edu.xtec.merli.ws.objects.LlistatGeneric;
import edu.xtec.merli.ws.objects.Lom;
import edu.xtec.merli.ws.objects.Relacio;
import edu.xtec.merli.ws.objects.SourceValue;
import edu.xtec.merli.ws.objects.Taxon;
import edu.xtec.merli.ws.objects.TaxonPath;
import edu.xtec.merli.ws.objects.Unitat;

public class testWSMerli extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(testWSMerli.class);
	}
	
	public static int SERVIDOR=TestWS.LOCAL;

	public final void testAddWithIdentifier(){
		
		Duration d = new Duration();
		d.setDuration("2Y3T2H23.7S123");
//		
//		TestWS tws = new TestWS();
//		Lom lom;
//
//		//Crear un LOM buit amb les dades mínimes necessàries
//		String sText = "testAddWithIdentifier";
//		lom = new Lom();
//		lom.getGeneral().setIdentifier(new Identifier("CELEBRATE", "MERLI789"));
//		lom.getGeneral().getTitle().setString(sText);
//		lom.getGeneral().getDescription().setString(sText);
//		lom.getTechnical().setLocation(sText);
//		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("", "publisher", "role"),new Entity("sarjona","","",""),new Date()));
//		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("", "author", "role"),new Entity("mabad1","","",""),new Date()));
//		lom.getRights().getCopyRightAndOtherRestrictions().setValue("no");
//		lom.getRights().getCopyRightAndOtherRestrictions().setId("1");
//		
//		ArrayList alFormat = new ArrayList();
//		//alFormat.add()
//		lom.getTechnical().setFormat(alFormat);
//		
//		tws = new TestWS();
//		System.out.println("\n####################");
//		System.out.println("Test ws operacio add");
//		SOAPElement se = tws.processOperation(TestWS.ADDRESOURCE,lom,SERVIDOR);
//		IdResource idResource = null;
//		
//		try {
//			idResource = new IdResource(se);
//		} catch (SOAPException e) {
//			e.printStackTrace();
//		}
//		
//		tws =  new TestWS();
//		se = tws.processOperation(TestWS.GETRESOURCE, idResource,SERVIDOR);			
//		lom = new Lom(se);
//		assertEquals(sText, lom.getGeneral().getDescription().getString());
//		
//		System.out.println("\nFinal del test amb operacio add. Nou id:"+idResource.getIdentifier());
//		
//		//assertEquals(ts.getYear(),dt.getYear());
	}	
	

	public final void testAddResourceManual(){
//		TestWS tws = new TestWS();
//		Lom lom;
//
//		//Crear un LOM buit amb les dades mínimes necessàries
//		lom = new Lom();
//		lom.getGeneral().getTitle().setString("titulo");
//		lom.getGeneral().getDescription().setString("description");
//		lom.getTechnical().setLocation("location");
//		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("", "publisher", "role"),new Entity("jlpatricio","edu365@lo.po","Edu365",""),new Date()));
//		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("", "publisher", "role"),new Entity("jlpatricio","edu365@lo.po","CRP",""),new Date()));
//		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("", "publisher", "role"),new Entity("sarjona","","",""),new Date()));
//		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("", "author", "role"),new Entity("acanals5","","",""),new Date()));
//		lom.getMetaMetaData().getContributeList().add(new Contribute(new SourceValue("","publisher",""),new Entity("jpanzano","","",""),new Date()));
//		lom.getMetaMetaData().getContributeList().add(new Contribute(new SourceValue("","creator",""),new Entity("acanals5","","",""),new Date()));
////		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("", "editor", "role"),new Entity("cramos","","",""),new Date()));
//		
//		lom.getRights().getCopyRightAndOtherRestrictions().setValue("no");
//		lom.getRights().getCopyRightAndOtherRestrictions().setId("1");
//
//		//lom.getRights().getCost().setValue("no");
//		//lom.getRights().getCost().setId("1");
//		
//		ArrayList alFormat = new ArrayList();
//		//alFormat.add()
//		lom.getTechnical().setFormat(alFormat);
//		
//		tws = new TestWS();
//		System.out.println("\n####################");
//		System.out.println("Test ws operacio add");
//		SOAPElement se = tws.processOperation(TestWS.ADDRESOURCE,lom,SERVIDOR);
//		IdResource idResource = null;
//		
//		try {
//			idResource = new IdResource(se);
//		} catch (SOAPException e) {
//			e.printStackTrace();
//		}
//		
//		tws =  new TestWS();
//		se = tws.processOperation(TestWS.GETRESOURCE, idResource,SERVIDOR);			
//		lom = new Lom(se);
//		assertEquals("description", lom.getGeneral().getDescription().getString());
//		
//		System.out.println("\nFinal del test amb operacio add. Nou id:"+idResource.getIdentifier());
		
		//assertEquals(ts.getYear(),dt.getYear());
	}	

	
	public final void testAddResource(){
		
//		TestWS tws = new TestWS();
//		Lom lom;
//
//		IdResource idResource = new IdResource(String.valueOf(8934),"MERLI");//new IdResource();
//		SOAPElement se = tws.processOperation(TestWS.GETRESOURCE,idResource,SERVIDOR);
//		lom = new Lom(se);
//		System.out.println("^^^^^^^^^^^^^^^^^^^ end get 1");
//		
//		lom.getGeneral().setDescription(new LangString("Nova descripció", "ca"));
//
//		tws = new TestWS();
//		
//		//modifiquem general , lifecycle, technical, metadata i rights
//		lom.getGeneral().getTitle().setString("Prova MeRLí add. ");
//		lom.getGeneral().getDescription().setString("Nova descripció més llarga. # Comproveu que l'adreça no tingui errors en escriure    ww.example.com en comptes de    www.example.com#   Si no podeu carregar cap pàgina comproveu la connexió a la xarxa del vostre    ordinador.#   Si el vostre ordinador o xarxa estan protegits per un tallafocs o un servidor intermediari,assegureu-vos que el Firefox té permisos per a accedir al W");
//		lom.getGeneral().getCoverage().setString("coverage de prova");
//		lom.getTechnical().setLocation("http://www.merli.cat");
//		lom.getMetaMetaData().getContributeList().add(new Contribute(new SourceValue("","publisher",""),new Entity("CRP","CRP","","CRP"),new Date()));
//		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("","author",""),new Entity("eamat3","","",""),new Date()));
//		lom.getRights().getCopyRightAndOtherRestrictions().setValue("Non commercial");
//		
//		//afegim relacions
//		Relacio rel = new Relacio(new Integer(60903),"requereix");
//		Relacio rel2 = new Relacio(new Integer(11080),"referencia");
//		lom.getRelacions().add(rel);
//		lom.getRelacions().add(rel2);
//		
//		//modifiquem descripcio fisica, afegim idFisics i unitats on el recurs esta disponible
//		Unitat uni = new Unitat(new Integer(25990103), " CRP de la Noguera (BALAGUER)");
//		DescripcioFisica descFisica = new DescripcioFisica("cars",uni,null,null,null);
//				
//		LlistatGeneric idFisics = new LlistatGeneric(IdResource.class,"identificadors");
//		ArrayList lIdFisics = new ArrayList();
//		IdResource idFisic = new IdResource("CDU","prova");
//		lIdFisics.add(idFisic);		
//		IdResource idFisic2 = new IdResource("ISBN","prova2");
//		lIdFisics.add(idFisic2);
//		idFisics.setObjectList(lIdFisics);
//		descFisica.setIdentificadorFisicList(idFisics);
//		
//		LlistatGeneric disps = new LlistatGeneric(Unitat.class,"disponibleA");
//		ArrayList lDisp = new ArrayList();
//		lDisp.add(uni);
//		Unitat uni2 = new Unitat(new Integer(43990109), " CRP de la Terra Alta (GANDESA)");
//		lDisp.add(uni2);
//		disps.setObjectList(lDisp);
//		descFisica.setDisponibleA(disps);
//		
//		lom.setDescripcioFisica(descFisica);
//		
//		//aixo crec q no fa res ara
//		TaxonPath tp = new TaxonPath();
//		tp.setSource(new LangString("AMBIT","ca"));
//		ArrayList al = new ArrayList();
//		al.add(new Taxon("",new LangString("CRP","ca")));
//		al.add(new Taxon("",new LangString("XTEC","ca")));
//
//		se = tws.processOperation(TestWS.ADDRESOURCE,lom,SERVIDOR);	
//		System.out.println("^^^^^^^^^^^^^^^^^^^ end add");
//		
//		try {
//			idResource = new IdResource(se);
//		} catch (SOAPException e) {
//			e.printStackTrace();
//		}
//		tws =  new TestWS();
//		se = tws.processOperation(TestWS.GETRESOURCE, idResource,SERVIDOR);	
//		lom = new Lom(se);
//		System.out.println("^^^^^^^^^^^^^^^^^^^ end get 2");
//		assertEquals("Nova descripció més llarga. # Comproveu que l'adreça no tingui errors en escriure    ww.example.com en comptes de    www.example.com#   Si no podeu carregar cap pàgina, comproveu la connexió a la xarxa del vostre    ordinador.#   Si el vostre ordinador o xarxa estan protegits per un tallafocs o un servidor intermediari,assegureu-vos que el Firefox té permisos per a accedir al W", lom.getGeneral().getDescription().getString());
//		
//		System.out.println("\nFinal del test amb operacio add. Nou id:"+idResource.getIdentifier());
//		
	}
	
	
	public final void testUnpublishResource(){
		
//		TestWS tws = new TestWS();
//
//		IdResource idResource = new IdResource("8935", "MERLI");		
//
//		System.out.println("Test ws operacio Unpublish");
//		SOAPElement se = tws.processOperation(TestWS.UNPUBLISHRESOURCE,idResource,SERVIDOR);		
//		
//		try {
//			idResource = new IdResource(se);
//		} catch (SOAPException e) {
//			e.printStackTrace();
//			System.out.println("EXCEPTION delete");
//		}
//		
//		try{
//			tws =  new TestWS();
//			se = tws.processOperation(TestWS.GETRESOURCE, idResource,SERVIDOR);			
//			Lom lom = new Lom(se);
//			assertTrue(true);
//		}catch (Exception e){
//			assertTrue(false);
//		}
//		
//		
//		System.out.println("\nFinal del test amb operacio del:"+idResource.getIdentifier());
//		
//		//assertEquals(ts.getYear(),dt.getYear());
	}


	public final void testDelResource(){
		
//		TestWS tws = new TestWS();
//
//		IdResource idResource = new IdResource("8935", "MERLI");		
//
//		System.out.println("Test ws operacio del");
//		SOAPElement se = tws.processOperation(TestWS.DELRESOURCE,idResource,SERVIDOR);		
//		
//		try {
//			idResource = new IdResource(se);
//		} catch (SOAPException e) {
//			e.printStackTrace();
//			System.out.println("EXCEPTION delete");
//		}
//		
//		try{
//			tws =  new TestWS();
//			se = tws.processOperation(TestWS.GETRESOURCE, idResource,SERVIDOR);			
//			Lom lom = new Lom(se);
//			assertTrue(false);
//		}catch (Exception e){
//			assertTrue(true);
//		}
//		
//		
//		System.out.println("\nFinal del test amb operacio del:"+idResource.getIdentifier());
//		
//		//assertEquals(ts.getYear(),dt.getYear());
	}

	
	public final void testSetResource() throws SOAPException{
//		TestWS tws = new TestWS();
//		Lom lom;
//		
//		String sId = "61760";
//
//		System.out.println("Inici test SET.");
//		IdResource idResource = new IdResource(sId, "MERLI");
//		SOAPElement se = tws.processOperation(TestWS.GETRESOURCE,idResource,SERVIDOR);
//		lom = new Lom(se);
//
//		//Modificar el Lom.
//		//TODO: enlloc de modificar-lo a ma, hauria d'obrir-se la finestra d'edicio
//		String sText = "JClic";
//		lom.getGeneral().getTitle().setString("Test set APXTEC32 WSu-> "+sText);
////		lom.getGeneral().getDescription().setString(sText);
////		lom.getTechnical().setLocation(sText);
//		((Contribute)lom.getLifeCycle().getContributeList().get(0)).getEntity().setOrg("Edu365");
//		((Contribute)lom.getLifeCycle().getContributeList().get(1)).getEntity().setOrg("CRP");
////		lom.getMetaMetaData().setContributeList(new ArrayList());
////		lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("","publisher",""),new Entity("","","Altres",""),new Date()));
////		lom.getMetaMetaData().getContributeList().add(new Contribute(new SourceValue("","author",""),new Entity("jpanzano","","",""),new Date()));
//		
//		int max = Integer.parseInt(lom.getEducational().getTypicalAgeRangeMax());
//		lom.getEducational().getTypicalAgeRange().setString((max-10)+"-"+(max+1));
//		lom.getRights().setDescription(new LangString("algu","ca"));
//		
////		System.out.println(((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(0)).getEntry().getString());
//
////		((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(0)).getEntry().setString(sText);
////		((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(0)).setId("");
////		sText = "XTEC";
////		((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(1)).getEntry().setString(sText);
////		((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(1)).setId("");
//		
//		
//		
//		tws = new TestWS();
//		System.out.println("Test ws operacio set");		
//		se = tws.processOperation(TestWS.SETRESOURCE,lom,SERVIDOR);	
//
////		 tws = new TestWS();
////		 idResource = new IdResource(sId,"MERLI");
////		
////		 System.out.println("Test ws operacio get id="+sId);
////		 se = tws.processOperation(TestWS.GETRESOURCE,idResource,SERVIDOR);
////		
////		 lom = new Lom(se);
////		 //fin feo
////		 
////		assertEquals(sText, lom.getGeneral().getTitle().getString());
////		assertEquals(sText, lom.getGeneral().getDescription().getString());
////		assertEquals(sText, lom.getTechnical().getLocation());
////		System.out.println("\nFinal del test amb operacio set:"+idResource.getIdentifier());	
		
	}


	public final void testGetResource() throws SOAPException{

//		TestWS tws = new TestWS();
//		String sId = "61043";//"9007";//"3690";
//		IdResource idResource = new IdResource(sId,"MERLI");
//		
//		System.out.println("");
//		System.out.println("####################");
//		System.out.println("Test ws operacio GetResource amb id="+sId);
//		SOAPElement se = tws.processOperation(TestWS.GETRESOURCE, idResource, true, SERVIDOR);
//		
//		Lom lom = new Lom(se);
//
//		tws = new TestWS();
//		se = tws.processOperation(TestWS.GETRESOURCE, idResource, false, SERVIDOR);		
//		lom = new Lom(se);
//
//		//String sText = "http://url.com";
//		//System.out.println("\nFinal del test amb operacio get: "+lom.getGeneral().getTitle().getString());
//		//assertEquals(ts.getYear(),dt.getYear());
//		//System.out.println(((Contribute)lom.getLifeCycle().getContributeList().get(0)).getEntity().getUsername());
//		//System.out.println(((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(0)).getEntry().getString());
////		assertEquals(sText, ((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(0)).getEntry().getString());
//		//sText = "CRP";
////		assertEquals(sText, ((Taxon)((Classification)lom.getClassificationList().get(0)).getTaxonPath().getTaxonList().get(1)).getEntry().getString());
//		//assertEquals("aleix", ((Contribute)lom.getMetaMetaData().getContributeList().get(0)).getEntity().getUsername());
//		assertTrue(64<Integer.parseInt(lom.getEducational().getTypicalAgeRangeMax()));
	
	}

	public final void testEditResource(){
		
		TestWS tws = new TestWS();
		Lom lom;

		IdResource idResource = new IdResource(String.valueOf(61734),"MERLI");//new IdResource();
		SOAPElement se = tws.processOperation(TestWS.GETRESOURCE,idResource,SERVIDOR);
		
		System.out.println("^^^^^^^^^^^^^^^^^^^ end get");
		
//		lom.getGeneral().setDescription(new LangString("Nova descripció", "ca"));

		tws = new TestWS();
		
		//modifiquem general , lifecycle, technical, metadata i rights
//		lom.getGeneral().getTitle().setString("Prova MeRLí edit");
//		lom.getGeneral().getDescription().setString("Estic provant de passar els parametres  a la url");
//		lom.getGeneral().getCoverage().setString("coverage de prova");
//		lom.getTechnical().setLocation("http://www.merli.cat");
		
		
		//lom.getMetaMetaData().getContributeList().add(new Contribute(new SourceValue("","publisher",""),new Entity("CRP","CRP","","CRP"),new Date()));
		//lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("","author",""),new Entity("eamat3","","",""),new Date()));
		//lom.getLifeCycle().getContributeList().add(new Contribute(new SourceValue("","editor",""),new Entity("cramos","","",""),new Date()));
//		lom.getRights().getCopyRightAndOtherRestrictions().setValue("Non commercial");
		
		//afegim relacions
//		Relacio rel = new Relacio(new Integer(60903),"requereix");
//		Relacio rel2 = new Relacio(new Integer(11080),"referencia");
//		lom.getRelacions().add(rel);
//		lom.getRelacions().add(rel2);
		
		//modifiquem descripcio fisica, afegim idFisics i unitats on el recurs esta disponible
//		Unitat uni = new Unitat(new Integer(25990103), " CRP de la Noguera (BALAGUER)");
//		DescripcioFisica descFisica = new DescripcioFisica("cars",uni,null,null,null);
//				
//		LlistatGeneric idFisics = new LlistatGeneric(IdResource.class,"identificadors");
//		ArrayList lIdFisics = new ArrayList();
//		IdResource idFisic = new IdResource("CDU","prova");
//		lIdFisics.add(idFisic);		
//		IdResource idFisic2 = new IdResource("ISBN","prova2");
//		lIdFisics.add(idFisic2);
//		idFisics.setObjectList(lIdFisics);
//		descFisica.setIdentificadorFisicList(idFisics);
//		
//		LlistatGeneric disps = new LlistatGeneric(Unitat.class,"disponibleA");
//		ArrayList lDisp = new ArrayList();
//		lDisp.add(uni);
//		Unitat uni2 = new Unitat(new Integer(43990109), " CRP de la Terra Alta (GANDESA)");
//		lDisp.add(uni2);
//		disps.setObjectList(lDisp);
//		descFisica.setDisponibleA(disps);
//		
//		lom.setDescripcioFisica(descFisica);
		
//		se = tws.processOperation(TestWS.ADDRESOURCE,lom,SERVIDOR);	
//		System.out.println("^^^^^^^^^^^^^^^^^^^ end add");
//		
//		tws = new TestWS();
		lom = new Lom(se);
		se = tws.processOperation(TestWS.EDITRESOURCE,lom,SERVIDOR);	//retorna la url
		String url = (String) se.getFirstChild().getFirstChild().getNodeValue();
		System.out.println("^^^^^^^^^^^^^^^^^^^ end edit, aqui tens la url:");System.out.println(url);
		
	}
	
}
