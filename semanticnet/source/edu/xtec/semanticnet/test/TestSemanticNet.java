package edu.xtec.semanticnet.test;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticNet;
import edu.xtec.semanticnet.SemanticException;
import junit.framework.Assert;
import junit.framework.TestCase;

public class TestSemanticNet extends TestCase {


	public static void main(String[] args) {
		junit.swingui.TestRunner.run(TestSemanticNet.class);
	}
	public final void testNodesRelated() throws SemanticException{

		Hashtable hPropietats = new Hashtable();
		hPropietats.put("xml", "c:/Documents and Settings/acanals5/Escritorio/MeRLí/SemanticNet/testSN.xml");
		//hPropietats.put("xml", "http://www.eda3s.com/merli/propThesaureJDBC.xml");
		SemanticNet tes = new SemanticNet(hPropietats);
		ArrayList l = (ArrayList) tes.getNodesRelated(1595,"T","RT",RelationType.ALL);
		Node n;
		Iterator it = l.iterator();
		while(it.hasNext()){
			n = (Node) it.next();
			System.out.println(n.getIdNode()+n.getTerm()+n.getDescription());
		}
	}

	public final void testCarregarNode(){
		try{
			Hashtable hPropietats = new Hashtable();
			hPropietats.put("xml", "c:/Documents and Settings/acanals5/Escritorio/MeRLí/SemanticNet/testSN.xml");
			//hPropietats.put("xml", "http://www.eda3s.com/merli/propThesaureJDBC.xml");
			SemanticNet tes = new SemanticNet(hPropietats);
			Node n;
			for (int i = 0; i < 2; i++){
				n = tes.getNode(2, "T");
			}
			Assert.assertEquals(tes.existsNode(0,"T"), false);
			Assert.assertEquals(tes.existsNode(1,"T"), false);
			Assert.assertEquals(tes.existsNode(2,"T"), true);
			for (int i = 0; i < 2; i++){
				n = tes.getNode(i,"T");
			}
			Assert.assertEquals(tes.existsNode(0,"T"), true);
			Assert.assertEquals(tes.existsNode(1,"T"), true);
			Assert.assertEquals(tes.existsNode(2,"T"), true);
		}catch (SemanticException e){
			System.out.println(e.getMessage()+e.getDescription());	
			e.printStackTrace();
			Assert.assertFalse(true);
		}		
	}
	
	public final void testCarregarNodes() throws Exception{
		try{
			Hashtable hPropietats = new Hashtable();
			hPropietats.put("xml", "c:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticNet/testSN.xml");
			SemanticNet snet = new SemanticNet(hPropietats);
			Node n;
			List al = new ArrayList();
			List lprov = new ArrayList();
			lprov.add(new Integer(2));
			lprov.add("T");
			al.add(lprov);
			lprov = new ArrayList();
			lprov.add(new Integer(8));
			lprov.add("T");
			al.add(lprov);
			lprov = new ArrayList();
			lprov.add(new Integer(5));
			lprov.add("T");
			al.add(lprov);
			lprov = new ArrayList();
			lprov.add(new Integer(3));
			lprov.add("T");
			al.add(lprov);
			Hashtable h = new Hashtable();
			h.put("T",al);
			al = snet.loadNodes(h);

			n = snet.getNode(3,"T");
			Assert.assertEquals(n.getIdNode(),3);
			
			Assert.assertEquals(snet.existsNode(0,"T"), false);		
			Assert.assertEquals(snet.existsNode(1,"T"), false);
			Assert.assertEquals(snet.existsNode(2,"T"), true);
			Assert.assertEquals(snet.existsNode(8,"T"), true);
			Assert.assertEquals(snet.existsNode(5,"T"), true);
			Assert.assertEquals(snet.existsNode(3,"T"), true);		
			
		}catch (SemanticException e){
			System.out.println(e.getMessage()+e.getDescription());	
			e.printStackTrace();
			Assert.assertFalse(true);
		}			
	}
	
	
	public final void testCarregarRelacio() throws Exception {
		try{
			Hashtable hPropietats = new Hashtable();
			hPropietats.put("class", "edu.xtec.semanticnet.test.RepositoriDadesJDBC");
			hPropietats.put("xml", "C:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSN.xml");
			SemanticNet snet = new SemanticNet(hPropietats);
			Hashtable h;
			int j;
			Node n;
			for (int i = 0; i < 3; i++){
				j = snet.loadRelations(i,"T",RelationType.SOURCE);
				Assert.assertEquals(5 == snet.getRelations(i,"T","RT",RelationType.SOURCE).size(), true);
			}
			for (int i = 0; i < 3; i++){
				j = snet.loadRelations(i,"T",RelationType.DEST);
				Assert.assertEquals(j >= snet.getRelations(i,"T","RT",RelationType.DEST).size(), true);
			}
			Hashtable l = snet.getRelations(1,"T",RelationType.SOURCE);
			Relation r = ((Relation)((List)l.get("RT")).get(3));

			Assert.assertEquals(snet.existsNode(0,"T"), true);
			Assert.assertEquals(snet.existsNode(1,"T"), true);
			Assert.assertEquals(snet.existsNode(2,"T"), true);
		}catch (SemanticException e){
			System.out.println(e.getMessage()+e.getCode()+e.getDescription());	
			e.printStackTrace();
			Assert.assertFalse(true);
		}
	}
	
	public final void testCarregarXML() throws SemanticException{
		try{
			Hashtable hPropietats = new Hashtable();
			hPropietats.put("class", "edu.xtec.semanticnet.test.RepositoriDadesJDBC");
			hPropietats.put("xml", "C:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSN.xml");
			Hashtable hprops = new Hashtable();
			hprops.put("propertiesDocument.path","database.properties");
			hprops.put("propertiesDocument.folder","/");
			hPropietats.put("class", "edu.xtec.semanticnet.test.RepositoriDadesJDBC");
			hPropietats.put("xml", "C:/Documents and Settings/acanals5/Escritorio/MeRLí/thesaure/descripJDBC.xml");
			hPropietats.put("properties",hprops);
			
			SemanticNet tes = new SemanticNet(hPropietats);
			//tes.loadXMLdocument("C:/Documents and Settings/acanals5/Escritorio/MeRLí/thesaure/descripJDBC.xml");
			//tes.loadXMLdocument("C:/Documents and Settings/acanals5/Escritorio/MeRLí/thesaure/propThesaureJDBCerror.xml");
			Assert.assertFalse(true);	
		}catch (SemanticException e){
			System.out.println(e.getCode()+": "+e.getMessage()+" "+e.getDescription());	
			Assert.assertFalse(false);	
		}
	}

	public final void testDinamicSource() throws SemanticException{
		try{
		int n1, n2;
		Hashtable hPropietats = new Hashtable();
		hPropietats.put("class", "edu.xtec.semanticnet.test.RepositoriDadesJDBC");
		//hPropietats.put("xml", "C:/Documents and Settings/acanals5/Escritorio/MeRLí/semanticnet/testSN.xml");
		SemanticNet snet = new SemanticNet(hPropietats);
		Node n;// = snet.getNode(2, "T");
		n = new Node(0, "T", "terme1", snet);
		n = snet.addNewNode(n);		
		n1 = n.getIdNode();
		n = new Node(0, "T", "terme2", snet);				
		try {
			n = snet.addNewNode(n);
			n2 = n.getIdNode();
			//System.out.println("nounodes:"+n1+", "+n2);
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			n2 = n.getIdNode();
			e.printStackTrace();
		}
		
		Relation r = new Relation("RBT", n1, "T", n2, "T");
		snet.addNewRelation(r);
		r = new Relation("BT", n2, "T", n1, "T");
		snet.addNewRelation(r);
		r = new Relation("MT", 4, "T", n1, "T");
		snet.addNewRelation(r);
		Relation r2 = new Relation("RBT", n1, "T", 3, "T");
		snet.addNewRelation(r2);
		List l = snet.getRelations(n1,"T","RBT",RelationType.SOURCE);
		for (int i = 0;i<l.size();i++){
			Relation r1 = (Relation) l.get(i);
		//	System.out.println(r1.getIdSource()+r1.getRelationType()+r1.getIdDest());
		}
		snet.delNode(n1,"T");
		r2 = new Relation("RBT", n2, "T", 3, "T");
		snet.addNewRelation(r2);
		r2 = new Relation("MT", 4, "T", n2, "T");
		snet.addNewRelation(r2);
		r2 = new Relation("RT", 3, "T", n2, "T");
		snet.addNewRelation(r2);
		Hashtable dto = new Hashtable();
		dto.put("relationType", "USE");
		dto.put("idSource", new Integer(4));
		snet.setRelation(r2,dto);
		r2 = new Relation("RT", 5, "T", n2, "T");
		snet.addNewRelation(r2);
		Hashtable hl = snet.getRelations(n2,"T",RelationType.ALL);
		
		Iterator it = (Iterator) hl.elements();
		while (it.hasNext()){
			l = (List) it.next();
			for (int i = 0;i<l.size();i++){
				Relation r1 = (Relation) l.get(i);
			//	System.out.println("rel2 "+r1.getIdSource()+r1.getRelationType()+r1.getIdDest());
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
