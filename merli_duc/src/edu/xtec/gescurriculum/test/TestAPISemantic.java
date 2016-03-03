package edu.xtec.gescurriculum.test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticNet;
import edu.xtec.semanticnet.SemanticException;
import edu.xtec.semanticnet.thesaurus.NodeThesaurus;
import junit.framework.Assert;
import junit.framework.TestCase;

public class TestAPISemantic extends TestCase {


	public static void main(String[] args) {
		junit.swingui.TestRunner.run(TestAPISemantic.class);
	}

	public final void testCarregarProperties(){
		int qt;
		Node n;
		Hashtable hPropietats = new Hashtable();
		Hashtable hprops = new Hashtable();
		
		try{
			hprops.put("propertiesDocument.path","dbERRONI.properties");
			hprops.put("propertiesDocument.folder","/");
			hPropietats.put("properties",hprops);
			hPropietats.put("class", "edu.xtec.thesaure.DataSourceSNJDBC");
			hPropietats.put("xml", "C:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSNTv3.xml");
			try{
				SemanticNet tes = new SemanticNet(hPropietats);
				qt = tes.loadAllNodes();
				Assert.assertEquals(1447,qt);
				
				Assert.assertTrue(false);
			}catch(Exception e){
				System.out.println(e.getMessage());
				Assert.assertTrue(true);
			}
			hprops.put("propertiesDocument.path","database.properties");
			hprops.put("propertiesDocument.folder","/");
			SemanticNet tes = new SemanticNet(hPropietats);
			qt = tes.loadAllNodes();
		System.out.println("node 2:"+tes.getNode(2,"T").getTerm());
			Assert.assertEquals(1447,qt);
		}catch(Exception e){
			System.out.println(e.getMessage());
			Assert.assertTrue(false);
		}
	}
	

	public final void testCarregarXML() throws SemanticException{
		try{
			Hashtable hPropietats = new Hashtable();
			hPropietats.put("class", "edu.xtec.thesaure.DataSourceSNJDBC");
			hPropietats.put("xml", "c:/Documents and Settings/acanals5/Escritorio/MeRLí/SemanticNet/testSN.xml");
			
			SemanticNet tes = new SemanticNet(hPropietats);
			tes.loadXMLdocument("C:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSNTv3.xml");
			Assert.assertTrue(true);	
			hPropietats.put("xml", "http://www.eda3s.com/merli/testSN.xml");
			SemanticNet tes2 = new SemanticNet(hPropietats);
			Assert.assertTrue(true);
			try{
				tes.loadXMLdocument("C:/Documents and Settings/acanals5/Escritorio/MeRLí/thesaure/propThesaureJDBCerror.xml");
				Assert.assertTrue(false);
			}catch(SemanticException se){
				Assert.assertEquals(se.getCode(),8);
			}
		}catch (SemanticException e){	
			System.out.println("OK "+e.getMessage()+" "+e.getCode()+":"+e.getDescription());	
			Assert.assertTrue(false);	
		}
	}

	
	
	public final void testCarregarNode(){
		try{
			Hashtable hPropietats = new Hashtable();
			hPropietats.put("class", "edu.xtec.thesaure.DataSourceSNJDBC");
			hPropietats.put("xml", "c:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSNTv3.xml");
			SemanticNet tes = new SemanticNet(hPropietats);

			Node n;
			n = tes.getNode(944,"T");
			
			Assert.assertEquals(tes.existsNode(0,"T"), false);
			Assert.assertEquals(tes.existsNode(1,"T"), false);
			Assert.assertEquals(tes.existsNode(944,"T"), true);
			for (int i = 0; i < 89; i++){
				try{
				n = tes.getNode(i,"T");
				Assert.assertEquals(tes.existsNode(i,"T"), true);
				}catch(SemanticException te){
					Assert.assertEquals(tes.existsNode(i,"T"), false);
				}
			}			
		}catch (SemanticException e){
			System.out.println(e.getMessage()+e.getDescription());	
			Assert.assertFalse(true);
		}
		
	}
	
	public final void testCarregarNodes() throws Exception{
		/*
		try{
			Hashtable hPropietats = new Hashtable();
			hPropietats.put("class", "edu.xtec.thesaure.DataSourceSNJDBC");
			hPropietats.put("xml", "c:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSNT.xml");
			SemanticNet tes = new SemanticNet(hPropietats);
			Node n;
			List aErr, al = new ArrayList();
			List aPr = new ArrayList();
			aPr.add(new Integer(2));
			aPr.add("T");
			al.add(aPr);
			aPr = new ArrayList();
			aPr.add(new Integer(-8));
			aPr.add("T");
			al.add(aPr);
			aPr = new ArrayList();
			aPr.add(new Integer(3));
			aPr.add("T");
			al.add(aPr);
			aPr = new ArrayList();
			aPr.add(new Integer(5));
			aPr.add("T");
			al.add(aPr);
			aErr = tes.loadNodes((Hashtable) al);
			Assert.assertEquals(tes.existsNode(0,"T"), false);		
			Assert.assertEquals(tes.existsNode(1,"T"), false);
			Assert.assertEquals(tes.existsNode(2,"T"), true);
			Assert.assertEquals(tes.existsNode(8,"T"), false);
			Assert.assertEquals(tes.existsNode(5,"T"), true);
			Assert.assertEquals(tes.existsNode(3,"T"), true);		
			aErr = tes.loadNodes(al);
		}catch (SemanticException e){
			System.out.println(e.getMessage()+e.getDescription());	
			Assert.assertFalse(true);
		}			*/
	}
	
	
	public final void testCarregarRelacio() throws Exception {
		int i=-1,j;
		Node n;
		Hashtable hPropietats = new Hashtable();
		try{
			hPropietats.put("class", "edu.xtec.thesaure.DataSourceSNJDBC");
			hPropietats.put("xml", "c:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSNTv3.xml");
			SemanticNet tes = new SemanticNet(hPropietats);
			System.out.println("temps:"+System.currentTimeMillis());
			List l2 =tes.getRelations(942,"T","MT",RelationType.ALL);
			for (int i2 = 0; i2 < l2.size();i2++){
				Relation r = (Relation) l2.get(i2);
			}
			System.out.println("temps:"+System.currentTimeMillis());
			n = tes.getNode(2,"T");
			Assert.assertEquals(tes.existsNode(2,"T"), true);
			
			Hashtable l = n.getRelations(tes.getListRelationTypes(),RelationType.ALL);
	
			Assert.assertEquals(tes.existsNode(22,"T"), true);
			Assert.assertEquals(tes.existsNode(23,"T"), true);
			Assert.assertEquals(tes.existsNode(138,"T"), true);
			Assert.assertEquals(tes.existsNode(266,"T"), true);
			Assert.assertEquals(tes.existsNode(273,"T"), true);
			Assert.assertEquals(tes.existsNode(381,"T"), true);
			Assert.assertEquals(tes.existsNode(1027,"T"), true);
			Assert.assertEquals(tes.existsNode(3188,"T"), true);			
				
		}catch (SemanticException e){
			System.out.println("pos:"+i+" "+e.getMessage()+e.getCode()+e.getDescription());	
			Assert.assertFalse(true);
		}
	}
	
	
	public final void testCarregarAllNodes(){
		int qt;
		Node n;
		Hashtable hPropietats = new Hashtable();
		try{
			hPropietats.put("class", "edu.xtec.thesaure.DataSourceSNJDBC");
			hPropietats.put("xml", "c:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSNTv3.xml");
			SemanticNet tes = new SemanticNet(hPropietats);
			qt = tes.loadAllNodes();
			Assert.assertEquals(1447,qt);
			List l = tes.getNodes("T");
			Assert.assertEquals(1447,l.size());
			
		}catch(Exception e){
			System.out.println(e.getMessage());
			Assert.assertTrue(false);	
		}
	}
	
	public final void testCarregarAll(){
		int qt;
		Node n;
		Hashtable hPropietats = new Hashtable();
		Hashtable hprops = new Hashtable();
		try{
			hPropietats.put("class", "edu.xtec.thesaure.DataSourceSNJDBC");
			hPropietats.put("xml", "c:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSNTv3.xml");
			
			SemanticNet tes = new SemanticNet(hPropietats);
			
			qt = tes.loadAllNodes();
			Assert.assertEquals(1447,qt);
			
			qt = tes.loadAllRelations();

			List l =tes.getRelations(1,"T","BT",RelationType.SOURCE);
			Assert.assertEquals(1,l.size());
			
			l.clear();
			l =tes.getRelations(503,"T","BT",RelationType.SOURCE);
			Relation r = (Relation) l.get(0);
			Assert.assertEquals(549,r.getIdDest());
			
				
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	
	public final void testCarregarRelacioUSE() throws Exception {
		int i=-1,j;
		NodeThesaurus n;
		Hashtable hPropietats = new Hashtable();
		try{
			hPropietats.put("class", "edu.xtec.thesaure.DataSourceSNJDBC");
			hPropietats.put("xml", "c:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSNTv3.xml");
			SemanticNet tes = new SemanticNet(hPropietats);

			n = (NodeThesaurus) tes.getNode(723,"T");
			Hashtable ht = n.getRelations(tes.getListRelationTypes(),RelationType.DEST);

			
			n = (NodeThesaurus) tes.getNode(682,"T");
			Assert.assertEquals(tes.existsNode(682,"T"), true);
			
			List l = n.getRelations("BT",RelationType.ALL);
			
			
			Assert.assertEquals(tes.existsNode(250,"T"), true);
			Assert.assertEquals(tes.existsNode(251,"T"), true);
			Assert.assertEquals(tes.existsNode(391,"T"), true);
			Assert.assertEquals(tes.existsNode(369,"T"), true);
			Assert.assertEquals(tes.existsNode(273,"T"), true);
			Assert.assertEquals(tes.existsNode(723,"T"), true);
			Assert.assertEquals(tes.existsNode(888,"T"), true);
			Assert.assertEquals(tes.existsNode(1194,"T"), true);			
				
		}catch (SemanticException e){
			System.out.println("pos:"+i+" "+e.getMessage()+e.getCode()+e.getDescription());	
			Assert.assertFalse(true);
		}
	}
	
	public final void testNodesRelated() throws SemanticException{

		Hashtable hPropietats = new Hashtable();
		hPropietats.put("class", "edu.xtec.thesaure.DataSourceSNJDBC");
		hPropietats.put("xml", "c:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSNTv3.xml");
		SemanticNet tes = new SemanticNet(hPropietats);
		ArrayList l = (ArrayList) tes.getNodesRelated(2,"T","RT",RelationType.ALL);
		Node n;
		Iterator it = l.iterator();
		while(it.hasNext()){
			n = (Node) it.next();
			System.out.println(n.getIdNode()+n.getTerm()+n.getDescription());
		}
	}

}