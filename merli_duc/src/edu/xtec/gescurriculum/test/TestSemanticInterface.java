package edu.xtec.gescurriculum.test;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import edu.xtec.merli.utils.Utility;
import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticException;
import edu.xtec.semanticnet.thesaurus.NodeThesaurus;

public class TestSemanticInterface extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(TestSemanticInterface.class);
	}
	

	public static void testSlists(){
		String aux;
		aux = "32#43#645#65454#23#";
		String acID;
		while (aux.lastIndexOf("#")>0){
			acID = aux.substring(0,aux.indexOf("#"));
			aux = aux.substring(aux.indexOf("#")+1);
		}
	}
	
	public static void testSInterface2(){
		SemanticInterface si = new SemanticInterface();
		si.getNode(1,"area");
		si.getNode(2,"area");
		si.getNode(1,"level");
		System.out.println("initalized and provesd!");
	}
	/*
	public static void testSInodes(){
		SemanticInterface si = new SemanticInterface();
		
		System.out.println("--consulta nodes--");
		si.getNode(4,"area");
		si.getNode(3,"area");
		System.out.println("--consulta nodes--");
		si.getNode(2,"level");
		System.out.println("--history nodes--");
		si.getNode(1,"history");
	}
	
	public static void testRelation(){
		SemanticInterface si = new SemanticInterface();
	//	si.getNode(1,"objective");
	//	si.getNode(1,"level");
	//	si.getNode(2,"level");
		System.out.println(si.snet.getNodeTypes().toString());
		List l;
		try{
		l =si.snet.getRelations(1,"objective","ROL",RelationType.ALL);
		System.out.println("Relation:"+l.toString());
		l =si.snet.getNodes("level");
		System.out.println("level nodes:"+l.toString());
		l =si.snet.getNodes("objective");
		System.out.println("objective nodes:"+l.toString());
		}catch(Exception e){}
		try{
		System.out.println("relacions R(L|A)L R(H|N)");

		l =si.snet.getRelations(1,"level","RLL",RelationType.ALL);
		System.out.println("Relation:"+l.toString()+((Relation)l.get(0)).getSourceType()+((Relation)l.get(0)).getDestType()+((Relation)l.get(0)).getRelationType());
		l =si.snet.getNodes("level");
		System.out.println("level nodes:"+l.toString()+"\n");
		}catch(Exception e){}
		try{
		l =si.snet.getRelations(4,"area","RAL",RelationType.ALL);
		System.out.println("Relation:"+l.toString()+((Relation)l.get(0)).getSourceType()+((Relation)l.get(0)).getDestType()+((Relation)l.get(0)).getRelationType());
		l =si.snet.getNodes("area");
		System.out.println("area nodes:"+l.toString()+"\n");
		}catch(Exception e){}
		try{
		l =si.snet.getRelations(1,"notes","RN",RelationType.ALL);
		System.out.println("Relation:"+l.toString()+((Relation)l.get(0)).getSourceType()+((Relation)l.get(0)).getDestType()+((Relation)l.get(0)).getRelationType());
		l =si.snet.getNodes("notes");
		System.out.println("notes nodes:"+l.toString()+"\n");
		}catch(Exception e){}
		try{
		l =si.snet.getRelations(1,"history","RH",RelationType.ALL);
		System.out.println("Relation:"+l.toString()+((Relation)l.get(0)).getSourceType()+((Relation)l.get(0)).getDestType()+((Relation)l.get(0)).getRelationType());
		l =si.snet.getNodes("history");
		System.out.println("history nodes:"+l.toString()+"\n");
		}catch(Exception e){}
	
	}
	
	public static void testAllNodes(){
		SemanticInterface si = new SemanticInterface();
		try {
			si.snet.loadAllNodes();
			List l =si.snet.getNodes("level");
			System.out.println("level nodes:"+l.toString());
			l =si.snet.getNodes("objective");
			System.out.println("objective nodes:"+l.toString());
			l =si.snet.getNodes("area");
			System.out.println("area nodes:"+l.toString());
			l =si.snet.getNodes("content");
			System.out.println("content nodes:"+l.toString());
			l =si.snet.getNodes("history");
			System.out.println("history nodes:"+l.toString());
			l =si.snet.getNodes("notes");
			System.out.println("notes nodes:"+l.toString());
			Node n = (Node) l.get(0);

			System.out.println(n.getIdNode()+n.getDescription()+(String) n.getProperties().get("date"));
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(si.snet.getListNodeTypes());
		}
	}
	

	public final void testNodesRelated() throws SemanticException{

		SemanticInterface si = new SemanticInterface();
		ArrayList l = (ArrayList) si.snet.getNodesRelated(0,"level","RLL",RelationType.DEST);
		ArrayList l2 = new ArrayList();
		Node n;
		Iterator it = l.iterator();
		while(it.hasNext()){
			n = (Node) it.next();
			System.out.println(n.getIdNode()+n.getTerm()+n.getDescription());
			l2.addAll(si.snet.getNodesRelated(n.getIdNode(),n.getNodeType(),"RLL",RelationType.DEST));
		}

		it = l2.iterator();
		while(it.hasNext()){
			n = (Node) it.next();
			System.out.println(n.getIdNode()+n.getTerm()+n.getDescription());			
		}
	}
	

	
	public static void testAllRelation(){
		SemanticInterface si = new SemanticInterface();
		List lt = si.snet.getListRelationTypes();
		System.out.println("Tipus relacions:"+lt.toString());
		try {
			int i = si.snet.loadAllRelations();
			System.out.println("Loaded Relations:"+i);
			List l =si.snet.getNodes("level");
			System.out.println("level nodes:"+l.toString());
			l =si.snet.getNodes("objective");
			System.out.println("objective nodes:"+l.toString());
			l =si.snet.getNodes("area");
			System.out.println("area nodes:"+l.toString());
			l =si.snet.getNodes("content");
			System.out.println("content nodes:"+l.toString());
			l =si.snet.getNodes("history");
			System.out.println("history nodes:"+l.toString());
			l =si.snet.getNodes("notes");
			System.out.println("notes nodes:"+l.toString());
			
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public final void testNodesRelatedCC() throws SemanticException{
		SemanticInterface si = new SemanticInterface();
		
		ArrayList l = si.getContentsRelated(304,20,86,"cc");
		System.out.println(l);
	}
	
	public final void testGetPbjectives(){
		SemanticInterface si = new SemanticInterface();
		
		Hashtable h =si.getObjectives(4,240);
		
		System.out.println(h.toString());
	}
	
	public static void testListOrder(){
		SemanticInterface si = new SemanticInterface();
		ArrayList l = new ArrayList();
		ArrayList lord = new ArrayList();
	
		l = si.getLevels(3);
		lord = Utility.orderListByPosition(l);
		System.out.println(lord.toString());
	}
	
	
	public final void testCarregarNode(){
		SemanticInterface si = new SemanticInterface();

		Node n;
		n = si.getNode(944,"T");
		
		Assert.assertEquals(si.snet.existsNode(0,"T"), true);
		Assert.assertEquals(si.snet.existsNode(1,"T"), true);
		Assert.assertEquals(si.snet.existsNode(944,"T"), true);
		for (int i = 0; i < 89; i++){
			try{
			n = si.snet.getNode(i,"T");
			Assert.assertEquals(si.snet.existsNode(i,"T"), true);
			}catch(SemanticException te){
				Assert.assertEquals(si.snet.existsNode(i,"T"), false);
			}
		}
		
	}
	
	public final void testCarregarRelacio() throws Exception {
		int i=-1,j;
		Node n;
		try{
			SemanticInterface si= new SemanticInterface();
			
			System.out.println("temps:"+System.currentTimeMillis());
			List l2 =si.snet.getRelations(942,"T","MT",RelationType.ALL);
			for (int i2 = 0; i2 < l2.size();i2++){
				Relation r = (Relation) l2.get(i2);
			}
			System.out.println("temps:"+System.currentTimeMillis());
			n = si.snet.getNode(2,"T");
			Assert.assertEquals(si.snet.existsNode(2,"T"), true);
			
			Hashtable l = n.getRelations(si.snet.getListRelationTypes(),RelationType.ALL);
	
			Assert.assertEquals(si.snet.existsNode(22,"T"), true);
			Assert.assertEquals(si.snet.existsNode(23,"T"), true);
			Assert.assertEquals(si.snet.existsNode(138,"T"), true);
			Assert.assertEquals(si.snet.existsNode(266,"T"), true);
			Assert.assertEquals(si.snet.existsNode(273,"T"), true);
			Assert.assertEquals(si.snet.existsNode(381,"T"), true);
			Assert.assertEquals(si.snet.existsNode(1027,"T"), true);
			Assert.assertEquals(si.snet.existsNode(3188,"T"), true);			
				
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
			SemanticInterface si= new SemanticInterface();
			si.snet = null;
			si= new SemanticInterface();
			
			qt = si.snet.loadAllNodes();
			Assert.assertTrue(0<=qt);
			List l = si.snet.getNodes("T");
			Assert.assertEquals(1448,l.size());
			
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
			SemanticInterface si= new SemanticInterface();
			si.snet = null;
			si= new SemanticInterface();
			
			qt = si.snet.loadAllNodes();
			Assert.assertTrue(0<=qt);
			
			qt = si.snet.loadAllRelations();

			List l =si.snet.getRelations(1,"T","BT",RelationType.SOURCE);
			Assert.assertEquals(1,l.size());
			
			l.clear();
			l =si.snet.getRelations(503,"T","BT",RelationType.SOURCE);
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
			SemanticInterface si= new SemanticInterface();

			n = (NodeThesaurus) si.snet.getNode(723,"T");
			Hashtable ht = n.getRelations(si.snet.getListRelationTypes(),RelationType.ALL);
			
			n = (NodeThesaurus) si.snet.getNode(682,"T");
			Assert.assertEquals(si.snet.existsNode(682,"T"), true);
			
			List l = n.getRelations("BT",RelationType.ALL);
		
			Assert.assertEquals(si.snet.existsNode(250,"T"), true);
			Assert.assertEquals(si.snet.existsNode(251,"T"), true);
			Assert.assertEquals(si.snet.existsNode(391,"T"), true);
			Assert.assertEquals(si.snet.existsNode(369,"T"), true);
			Assert.assertEquals(si.snet.existsNode(273,"T"), true);
			Assert.assertEquals(si.snet.existsNode(723,"T"), true);
			Assert.assertEquals(si.snet.existsNode(888,"T"), true);
			Assert.assertEquals(si.snet.existsNode(1194,"T"), true);			
				
		}catch (SemanticException e){
			System.out.println("pos:"+i+" "+e.getMessage()+e.getCode()+e.getDescription());	
			Assert.assertFalse(true);
		}
	}

	public final void testNodesRelatedThes() throws SemanticException{
		Hashtable hPropietats = new Hashtable();
		SemanticInterface si= new SemanticInterface();
		ArrayList l = (ArrayList) si.snet.getNodesRelated(2,"T","RT",RelationType.ALL);
		Node n;
		Iterator it = l.iterator();
		while(it.hasNext()){
			n = (Node) it.next();
			System.out.println(n.getIdNode()+n.getTerm()+n.getDescription());
		}
	}

	public final void testGetThesaurusList() throws SemanticException{
		Hashtable hPropietats = new Hashtable();
		SemanticInterface si= new SemanticInterface();
		ArrayList lt = new ArrayList();
		lt = si.getThesaurus(0);	
		lt = (ArrayList) Utility.orderNodeListByNote(lt);
		Assert.assertTrue(lt.size()>2);
		lt = ((ArrayList)si.snet.getNodesRelated(0,"T","OT",RelationType.DEST));
		Assert.assertTrue(lt.size()>2);	
	}
	
	public final void testNodesSearch() throws SemanticException{
		Hashtable hPropietats = new Hashtable();
		SemanticInterface si= new SemanticInterface();
		ArrayList lt = new ArrayList();
		lt.add("T");
		Hashtable el = (Hashtable) si.snet.searchNode("ed‹cacio",lt);
		Node n;
		Iterator it = ((AbstractList) el.get("T")).iterator();
		while(it.hasNext()){
			n = (Node) it.next();
		}
		Assert.assertTrue(si.snet.existsNode(4,"T"));
		Assert.assertTrue(si.snet.existsNode(14,"T"));
		Assert.assertTrue(si.snet.existsNode(34,"T"));
		Assert.assertTrue(si.snet.existsNode(940,"T"));
		Assert.assertTrue(si.snet.existsNode(946,"T"));
		Assert.assertTrue(si.snet.existsNode(14,"T"));
		Assert.assertTrue(si.snet.existsNode(34,"T"));
		Assert.assertTrue(si.snet.existsNode(2448,"T"));
		Assert.assertTrue(si.snet.existsNode(2503,"T"));
	}
*/
}

