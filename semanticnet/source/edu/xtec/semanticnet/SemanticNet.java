package edu.xtec.semanticnet;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.text.Utilities;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


public class SemanticNet {
	
	/*ATRIBUTS*/
	private String sName;
	private boolean bDynamic = true;
	private Hashtable hProperties;

	private Hashtable hNodeTypes;
	private Hashtable hRelationTypes;
	private Hashtable hDSource;
	private DataSource dsDatSource;
	//private static String sEsquemaValidacio = "../../../semanticNetv3.xsd";
	//public static final String sEsqValidDefault = "../../../semanticNetv3.xsd";
	private static String sEsquemaValidacio = "../../../semanticNetv3.xsd";
	public static final String sEsqValidDefault = "../../../semanticNetv3.xsd";
	
	/*fi atributs*/
	
	/*CONSTRUCTORS*/
	/**
	 * Constructor without parameters.
	 * Put a SemanticNet name, and set the SemanticNet to dynamic.
	 * @throws SemanticException 
	 *
	 */
	public SemanticNet() throws SemanticException{
		this.sName = "nou thesaure";
		this.bDynamic = true;
		this.init(new Hashtable());
	}
	
	/**
	 * Constructor. SemanticNet without properties.
	 * @param sName
	 * @param bDynamic
	 * @throws SemanticException 
	 */
	public SemanticNet(String sName, boolean bDynamic) throws SemanticException{
		this.sName = sName;
		this.bDynamic = bDynamic;
		this.init(new Hashtable());
	}


	/**
	 * Constructor with a value por each attribute.
	 * @param sName
	 * @param bDynamic  boolean.
	 * @param hProperties Hashtable with SemanticNet properties like DataSource properties.
	 * @throws SemanticException 
	 */
	public SemanticNet(String sName, boolean bDynamic, Hashtable hProperties) throws SemanticException{
		this.sName = sName;
		this.bDynamic = bDynamic;
		this.init(hProperties);
	}

	/**
	 * Constructor. All properties where into porperties hashtable.
	 * @param hProperties All SemanticNet properties.
	 * @throws SemanticException 
	 */
	public SemanticNet(Hashtable hProperties) throws SemanticException{		
		this.init(hProperties);
		if (hProperties.containsKey("name")){
			this.sName = hProperties.get("name").toString();
		}else{
			this.sName = "nou XarxaSemantica";			
		}		
	}
	/*fi constructors*/

	/**
	 * Initialize SemanticNet attributes from a given XML source or given by parameters.
	 * @param properties 
	 * @throws SemanticException 
	 */
	private void init(Hashtable hProperties) throws SemanticException {
		// TODO Auto-generated method stub
		this.hProperties = hProperties;
		hNodeTypes = new Hashtable();
		hRelationTypes = new Hashtable();
		hDSource = new Hashtable();
		hDSource.put("relations",new Hashtable());
		hDSource.put("nodes",new Hashtable());
		/*Carrega la localització del XSD*/
		Properties pProp= new Properties();
		try {
			pProp.load(Utilities.class.getResourceAsStream("../../../semanticnet.properties"));	
			if (pProp.containsKey("esquema_validacio")){
				SemanticNet.sEsquemaValidacio = pProp.getProperty("esquema_validacio");
			}
		} catch (Exception e1) {
			SemanticNet.sEsquemaValidacio =SemanticNet.sEsqValidDefault;
		}	
		if (hProperties.containsKey("esquema_validacio")){
			SemanticNet.sEsquemaValidacio =(String) hProperties.get("esquema_validacio");
		}
		
		if (hProperties.containsKey("xml")){
			this.loadXMLdocument((String)hProperties.get("xml"));
		}else{
			if(pProp.containsKey("xml_definition")){
				this.loadXMLdocument(pProp.getProperty("xml_definition"));
			}
		}
		/**
		 * Assigna DataSources a cada element.
		 */
		Iterator iter=  this.getListRelationTypes().iterator();
		List lType = new ArrayList();
		String type;
		while (iter.hasNext()){
			type = (String)iter.next();
			((RelationType)this.hRelationTypes.get(type)).setDSource(((DataSource)((Hashtable)hDSource.get("relations")).get(type)));
			if (((Hashtable)hDSource.get("relations")).containsKey(type+"dynamic"))
				if (!((Boolean)((Hashtable)hDSource.get("relations")).get(type+"dynamic")).booleanValue())
					((RelationType)this.hRelationTypes.get(type)).loadRelations();
		}
		iter=  this.getListNodeTypes().iterator();
		lType = new ArrayList();
		while (iter.hasNext()){
			type = (String)iter.next();
			((NodeType)this.hNodeTypes.get(type)).setDSource(((DataSource)((Hashtable)hDSource.get("nodes")).get(type)));
			if(((Hashtable)hDSource.get("nodes")).containsKey(type+"dynamic")){
				if (!((Boolean)((Hashtable)hDSource.get("nodes")).get(type+"dynamic")).booleanValue()){
					((NodeType)this.hNodeTypes.get(type)).loadAllNodes();
				}
			}
		}	
	}

	
	
	/**
	 * Carrega totes les propietats descriptores del thesaure desde l'XML donat.
	 * L'XML donat ha de ser validat per l'esquema 'thesaure.xsd'. Les propietats són carregades a
	 * cada una de les hashtables corresponents i els TipusRelacio descrits són creats.
	 * @param xmlPath Path de l'XML.
	 * @throws SemanticException
	 */
	public void loadXMLdocument(String xmlPath) throws SemanticException {
		// TODO Auto-generated method stub
		//SAXBuilder builder = new SAXBuilder();
		Iterator iter;
		Hashtable hProp;
        try {
			/** 
			 * Descriu el constructor del parser i la seva validació. 
			 */
			Document dDesc;
			SAXBuilder builder = new SAXBuilder("org.apache.xerces.parsers.SAXParser", true);
            builder.setFeature("http://xml.org/sax/features/validation", true);        
			builder.setFeature("http://apache.org/xml/features/validation/schema", true);        
			builder.setFeature("http://apache.org/xml/features/continue-after-fatal-error", true);
			builder.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", SemanticNet.sEsquemaValidacio);	// "c:/Documents and Settings/acanals5/Escritorio/MeRLí/SemanticNet/semanticNet.xsd");//Thesaurus.esquema_validacio);//		
			
			/**
			 * Carrega l'xml desde una url o des del disc segons el path 'xmlPath' donat.
			 */
		
			if(xmlPath.toLowerCase().indexOf("http://")!=0 && xmlPath.toLowerCase().indexOf("ftp://")!=0){
				File f = new File(xmlPath);
				dDesc = builder.build(f);
			}else{
				URL u = new URL(xmlPath);
				dDesc = builder.build(xmlPath);
			}
			
	        Iterator itr = dDesc.getRootElement().getChildren().iterator();
			while (itr.hasNext()) {				
	            Element e = (Element) itr.next();
				if (e.getName().compareTo("relations") == 0){
		            this.loadRelationsXML(e.getChildren());
				}
				if (e.getName().compareTo("nodes") == 0){
		            this.loadNodesXML(e.getChildren());
				}
				if (e.getName().compareTo("name") == 0){
					this.hProperties.put("name", e.getValue());
				}	
				if (e.getName().compareTo("dataSource") == 0){
					//Inicialització del hashtable amb les propietats del DataSource tractant-se actualment.
					if (this.hProperties.containsKey("properties")){
						hProp = (Hashtable)this.hProperties.get("properties");
					}else{
						hProp = new Hashtable();
					}
					//Classe del DataSource. Del properties general o el descrit en l'XML
					if (!this.hProperties.containsKey("class"))
						//this.hProperties.put("class", e.getChild("class").getValue());								
						hProp.put("class", e.getChild("class").getValue());
					else
						hProp.put("class",this.hProperties.get("class"));
					//DataSource dinàmic o no. 
					if (e.getChild("dynamic")!= null){
						String sBool = e.getChild("dynamic").getValue();
						bDynamic =  new Boolean(sBool).booleanValue();
						if (this.hProperties.containsKey("dynamic"))						
							hProp.put("dynamic", this.hProperties.get("dynamic"));
						else
							hProp.put("dynamic",  new Boolean(sBool));
					}else{
						if (this.hProperties.containsKey("dynamic"))						
							hProp.put("dynamic", this.hProperties.get("dynamic"));
						else
							hProp.put("dynamic", new Boolean(true));
					}
					//Node inicial, en cas de ser necessari pel DataSource.
					try{
						if (!this.hProperties.containsKey("initialNode")){
							if (e.getChild("initialNode")!= null)
								hProp.put("initialNode", e.getChild("initialNode").getValue());
						}
					}catch(NullPointerException npe){
						//No hi ha cap problema. Es segueix amb la carrega del document.
					}
					//Propietats descrites en l'XML necessaries pel DataSource.
					try{
						Element e2;
						iter = e.getChild("properties").getChildren().iterator();
						while (iter.hasNext()){
							e2 = (Element) iter.next();
							Object obj = hProp.put(e2.getChild("name").getValue(), e2.getChild("value").getValue());
							if (obj != null){
								hProp.put(e2.getChild("name").getValue(), obj);
							}
						}
					}catch(NullPointerException npe){
						//No hi ha cap problema. Es segueix amb la carrega del document.
					}
					//Relacions que pot tractar el DataSource.
					try{
						Element e2;
						if (!hProp.containsKey("relations"))
							hProp.put("relations",new ArrayList());
						iter = e.getChild("relations").getChildren().iterator();
						while (iter.hasNext()){
							e2 = (Element) iter.next();
							((ArrayList) hProp.get("relations")).add(e2.getValue());
						}
					}catch(NullPointerException npe){
						//No hi ha cap problema. Es segueix amb la carrega del document.
					}
					//Nodes que pot tractar el DataSource.
					try{
						Element e2;
						if (!hProp.containsKey("nodes"))
							hProp.put("nodes",new ArrayList());
						iter = e.getChild("nodes").getChildren().iterator();
						while (iter.hasNext()){
							e2 = (Element) iter.next();
							((ArrayList) hProp.get("nodes")).add(e2.getValue());
						}
					}catch(NullPointerException npe){
						//No hi ha cap problema. Es segueix amb la carrega del document.
					}
					//this.hProperties.put("properties",hProp);
					//Construcció del DataSource.					
					this.loadDataSource(hProp);
				}
	        }			
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			SemanticException seE;
			if (e.getMessage().toLowerCase().indexOf("parser") > 0){
				// TODO Auto-generated catch block
				seE = new SemanticException("ErrorXMLParser");
				seE.setCode(SemanticException.XMLPARSERERROR);
			}else{
				seE = new SemanticException("ErrorXMLDescriptor");
				seE.setCode(SemanticException.XMLERROR);
			}
			seE.setStackTrace(e.getStackTrace());
			throw seE;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			SemanticException seE = new SemanticException("NotLoadedXML");
			seE.setCode(SemanticException.XMLNOTLOADED);
			seE.setStackTrace(e.getStackTrace());
			throw seE;
		} 
	}
	
	
	private void loadDataSource(Hashtable hProp) throws SemanticException{
		/**
		 * Carrega el Repositori de dades.
		 */
		DataSource dsDatSource;
		Boolean bDyn = new Boolean(true);
		if (hProp.containsKey("class")){
			try {
				Class clRepDades = Class.forName((String)hProp.get("class"));
				if (hProp.containsKey("dynamic"))
					bDyn = (Boolean) hProp.get("dynamic");
				try {
					dsDatSource = (DataSource) clRepDades.newInstance();
					dsDatSource.init(hProp);
					List lType = new ArrayList();
					String type;
					Iterator iter=  ((AbstractList) hProp.get("relations")).iterator();
					while (iter.hasNext()){
						type = (String)iter.next();
						((Hashtable)hDSource.get("relations")).put(type,dsDatSource);
						((Hashtable)hDSource.get("relations")).put(type+"dynamic", bDyn);						
					}
					iter = ((AbstractList) hProp.get("nodes")).iterator();
					lType = new ArrayList();
					while (iter.hasNext()){
						type = (String)iter.next();
						((Hashtable)hDSource.get("nodes")).put(type,dsDatSource);		
						((Hashtable)hDSource.get("nodes")).put(type+"dynamic",bDyn);
					}					
				} catch (InstantiationException e) {
					SemanticException seE = new SemanticException("DataSourceInstantiation");
					seE.setCode(SemanticException.DATASOURCEINSTANTIATION);
					seE.setStackTrace(e.getStackTrace());
					throw seE;
				} catch (IllegalAccessException e) {
					SemanticException seE = new SemanticException("DataSourceIllegalAcces");
					seE.setCode(SemanticException.DATASOURCEILLEGALACCES);
					seE.setStackTrace(e.getStackTrace());
					throw seE;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				SemanticException seE = new SemanticException("DataSourceNotFound");
				seE.setCode(SemanticException.DATASOURCENOTFOUND);
				seE.setStackTrace(e.getStackTrace());
				throw seE;
			}
		}
	}
	
	
	
	/**
	 * Carrega la llista de Tipus de Relacions del thesaure desde un llistat d'elements XML.
	 * @param list Llistat d'elements XML relacio descrits en l'esquema. {Element[relacio]}
	 */
	private void loadRelationsXML(List list) {
		Element e = (Element)list.get(0);
		if (!this.hRelationTypes.containsKey(e.getChild("type").getValue())){
			RelationType rtRelType = new RelationType(e.getChild("type").getValue(),this.dsDatSource);
			if(e.getChild("description") !=null){
				rtRelType.setDescription(e.getChild("description").getValue());
			}
			Hashtable hProp = new Hashtable();
			if(e.getChild("properties") !=null){
				Iterator iter = e.getChild("properties").getChildren().iterator();
				while (iter.hasNext()){
					e = (Element) iter.next();
					hProp.put(e.getChild("name").getValue(), e.getChild("value").getValue());
				}
				rtRelType.setHProperties(hProp);		
			}
			//Permet carregar una classe q implementa la classe RelationType creada per l'usuari.
			if(hProp.containsKey("class")){
				Class clRelType;
				try {
					clRelType = Class.forName((String)hProp.get("class"));
				RelationType rt2;
				rt2 = (RelationType) clRelType.newInstance();
				rt2.clone(rtRelType);
				this.hRelationTypes.put(rt2.getType(),rt2);
				} catch (Exception e1) {
					this.hRelationTypes.put(rtRelType.getType(), rtRelType);
				} 
			}else{
				this.hRelationTypes.put(rtRelType.getType(), rtRelType);
			}
		}
		list.remove(0);
		if(!list.isEmpty()){
			this.loadRelationsXML(list);
		}
	}

	/**
	 * Carrega la llista de Tipus de Nodes del SemanticNet desde un llistat d'elements XML.
	 * @param list Llistat d'elements XML relacio descrits en l'esquema. {Element[node]}
	 */
	private void loadNodesXML(List list) {
		Element e = (Element)list.get(0);
		if (!this.hNodeTypes.containsKey(e.getChild("type").getValue())){
			NodeType ntNType = new NodeType(e.getChild("type").getValue(), this.dsDatSource, this);
			if(e.getChild("description") !=null){
				ntNType.setDescription(e.getChild("description").getValue());
			}
			
			Hashtable hProp = new Hashtable();
			if(e.getChild("properties") !=null){
				Iterator iter = e.getChild("properties").getChildren().iterator();
				while (iter.hasNext()){
					e = (Element) iter.next();
					hProp.put(e.getChild("name").getValue(), e.getChild("value").getValue());
				}
				ntNType.setHProperties(hProp);			
			}
			if(hProp.containsKey("class")){
				Class clNodType;
				try {
					clNodType = Class.forName((String)hProp.get("class"));
				NodeType nt2;
				nt2 = (NodeType) clNodType.newInstance();
				nt2.clone(ntNType);
				this.hNodeTypes.put(nt2.getType(),nt2);
				} catch (Exception e1) {
					this.hNodeTypes.put(ntNType.getType(), ntNType);
				} 
			}else{
				this.hNodeTypes.put(ntNType.getType(), ntNType);
			}
			
		}
		list.remove(0);
		if(!list.isEmpty()){
			this.loadNodesXML(list);
		}
	}
	
	
	/*FUNCIONS GET/SET DELS ATRIBUTS DEL THESAURE*/
	/**
	 * Retorna el valor de l'atribut dynamic, que indica si el thesaure es carrega de forma estàtica o dinàmica. 
	 * @return boolean dynamic.
	 */
	public boolean isDynamic() {
		return bDynamic;
	}
	
	public void setDynamic(boolean esDynamic) {
		this.bDynamic = esDynamic;
	}
	
	public String getName() {
		return sName;
	}
	
	public void setName(String sName) {
		this.sName = sName;
	}
	
	/**
	 * Retorna Hashtable amb les propietats del SemanticNet.
	 * @return Hashtable hProperties.
	 */
	public Hashtable getProperties() {
		return hProperties;
	}
	
	/**
	 * Carrega la llista de propietats del SemanticNet.
	 * @param hProperties
	 */
	public void setProperties(Hashtable hProperties) {
		this.hProperties = hProperties;
	}
	
	
	public Hashtable getNodeTypes() {
		return hNodeTypes;
	}	
	
	public List getListNodeTypes(){
		Enumeration e = hNodeTypes.keys();
		List lType = new ArrayList();
		while (e.hasMoreElements()){
			lType.add((String)e.nextElement());
		}
		
		return lType;
	}

	public Hashtable getRelationTypes(){
		return this.hRelationTypes;
	}
	
	public List getListRelationTypes(){
		Enumeration e = hRelationTypes.keys();
		List lType = new ArrayList();
		while (e.hasMoreElements()){
			lType.add((String)e.nextElement());
		}
		return lType;
	}
	/*fi funcions get/set*/
	
	

	/*FUNCIONALITATS DEL THESAURE*/

	public boolean existsNode(int idNode, String sNType){
		if (hNodeTypes.containsKey(sNType)){
			return ((NodeType)hNodeTypes.get(sNType)).existsNode(idNode);
		}
		return false;
	}
	
	/**
	 * Retorna el node amb idNode del tipus donat. Si no està carregat el carrega. Si no existeix
	 * llançara una excepció.
	 * 
	 * Return the node of the given type and with the given ID. If it's not loaded it will be 
	 * loaded. In case of non exists a SemanticException were raised. 
	 * @param idNode Id del node
	 * @param sNType Tipus de node
	 * @return Node. 
	 */
	public Node getNode(int idNode, String sNType) throws SemanticException{
		Node n;
		NodeType nt =(NodeType)hNodeTypes.get(sNType); 
		n = nt.getNode(idNode);
		
		return n;
	}
	
	
	/**
	 * Return nodes with the given type.
	 * @param sNType
	 * @return
	 * @throws SemanticException
	 */
	public List getNodes(String sNType) throws SemanticException{
		List lNodes = new ArrayList();
		if (this.hNodeTypes.containsKey(sNType)){
			lNodes = ((NodeType)this.hNodeTypes.get(sNType)).getNodes();
		}else{
			SemanticException seE = new SemanticException("NonexistentNodeType");
			seE.setCode(SemanticException.NONEXISTENTNODETYPE);
			seE.setObject(sNType);
			throw seE;
		}
		return lNodes;
	}
	
	

	/**
	 * Retorna el llistat de relacions del node donat en la direccio indicada.
	 * @param idNode
	 * @param sNType
	 * @param iDirection
	 * @return Hashtable dto
	 * @throws SemanticException
	 */
	public Hashtable getRelations(int idNode, String sNType, int iDirection) throws SemanticException {
		Enumeration e = hRelationTypes.elements();
		RelationType rtRelType;
		Hashtable dto = new Hashtable();
		List lRelList;
		Node n;
		n = this.getNode(idNode, sNType);
		if (!n.isLoaded(sNType)){
			this.loadRelations(n.getIdNode(),sNType, RelationType.ALL);
			n.setLoaded(sNType, true);
		}
		
		while (e.hasMoreElements()){
			rtRelType = (RelationType) e.nextElement();			
			lRelList = rtRelType.getRelations(idNode, sNType, iDirection);			
			if (!lRelList.isEmpty()) dto.put(rtRelType.getType(), lRelList);
		}
				
		return dto;
	}
	
	/**
	 * Retorna les relacions que té un node d'un tipus donat i on el node ocupa una 
	 * direccio donada.
	 * @param idNode Identificador del node.
	 * @param sNodeType Tipus de node.
	 * @param sRelationType Tipus de relació buscada.
	 * @param direction El valor a 1 indica que el node és l'origen de la relació,
	 * a 2 indica que el node és el destí de la relació. a 3 que es vol tota possible relacio.
	 * @return {Relacio} Llistat de Relacio.
	 * @throws SemanticException 
	 */
	public List getRelations(int idNode, String sNodeType, String sRelationType, int direction) throws SemanticException{
		/*Carrega de la taula de tipus de relacions la relacio 'type'. D'aquesta
		 * n'obté les relacions del 'idNode' amb la 'direccio'.
		 */
		Node n;
		n = this.getNode(idNode, sNodeType);
		if (!n.isLoaded(sRelationType)){
			this.loadRelations(n.getIdNode(), sNodeType, sRelationType, RelationType.ALL);
			n.setLoaded(sRelationType,true);
		}
		return (List) ((RelationType)hRelationTypes.get(sRelationType)).getRelations(idNode, sNodeType, direction);		
	}



	/**
	 * Accedeix al Repositori de dades i aconsegueix la informació necessaria per crear el nou node.
	 * @param idNode Identificador del node.
	 * @return Node carregat.
	 * /
	private Node loadNode(int idNode, String sNType) throws SemanticException{
		Node n = null;
		Hashtable dto;
		dto = dsDatSource.getNode(idNode, sNType);	
		if (this.hNodeTypes.containsKey(sNType)){
			n = ((NodeType)this.hNodeTypes.get(sNType)).addNode(dto);	
		}else{
			SemanticException seE = new SemanticException("NonExistentNodeType");
			seE.setCode(SemanticException.NONEXISTENTNODETYPE);
			throw seE;
		}
		
		return n;
	}*/
	
	
	/**
	 * Carrega les relacions del tipus donat d'un node en la direcció indicada.
	 * @param idNode Node.
	 * @param sNType Node type.
	 * @param relationType RelationType to load.
	 * @param iDirection Direcció. 1-> node es l'origen; 2->node es el destí; 3->ambdues.
	 * @throws SemanticException 
	 */
	public int loadRelations(int idNode, String sNType, String relationType, int iDirection) throws SemanticException {
		// TODO Auto-generated method stub
		List lRes;
		Hashtable hnod = new Hashtable();
		List lType = new ArrayList();

		lType.add(relationType);
		
		Hashtable dto;
		Relation rRelation;
		String relType, sSType, sDType; 
		int idS, idD, nRel = 0;
		if (this.hRelationTypes.containsKey(relationType)){
			//Accedeix al repositori de dades sol·licitant les relacions d'origen o de destí.
			lRes = ((RelationType)this.hRelationTypes.get(relationType)).loadRelations(idNode, sNType, iDirection);
			//Recorrer els resultats obtinguts i crea cada un dels nodes necessaris.
			for (int j = 0; j < lRes.size(); j++){
				dto = (Hashtable) lRes.get(j);
				idS = ((Integer)dto.get("idSource")).intValue();
				sSType = (String)dto.get("sourceType");
				idD = ((Integer)dto.get("idDest")).intValue();
				sDType = (String)dto.get("destType");
				relType = (String)dto.get("relationType");
				
				if (!this.hRelationTypes.containsKey(relType)){
					SemanticException the = new SemanticException("NonexistentRelationType");
					the.setCode(SemanticException.NONEXISTENTRELATIONTYPE);
					the.setObject(relType);
					throw the;
				}
				if (!this.existsNode(idS, sSType)){
					List lNeededNode = new ArrayList();
					lNeededNode.add(new Integer(idS));
					lNeededNode.add(sSType);
					if (!hnod.containsKey(sSType)){
						hnod.put(sSType,new ArrayList());
					}
					((List)hnod.get(sSType)).add(lNeededNode);
				}
				if (!this.existsNode(idD, sDType)){
					List lNeededNode = new ArrayList();
					lNeededNode.add(new Integer(idD));
					lNeededNode.add(sDType);
					if (!hnod.containsKey(sDType)){
						hnod.put(sDType,new ArrayList());
					}
					((List)hnod.get(sDType)).add(lNeededNode);
				}
			}
			//Carrega tots els nodes d'un sol cop.
			if (!hnod.isEmpty()) loadNodes(hnod);
			//Crea les relacions entre tots els nodes carregats correctament. 
			//Retorna un llistat amb els nodes inexistents.
			for (int j = 0; j < lRes.size(); j++){
				dto = (Hashtable) lRes.get(j);
				idS = ((Integer)dto.get("idSource")).intValue();
				sSType = (String)dto.get("sourceType");
				idD = ((Integer)dto.get("idDest")).intValue();
				sDType = (String)dto.get("destType");
				relType = (String)dto.get("relationType");
				if (this.existsNode(idS, sSType) && this.existsNode(idD, sDType)){
					rRelation = ((RelationType)hRelationTypes.get(relType)).addRelation(dto);
					//rRelation = ((RelationType)hRelationTypes.get(relType)).addRelation(idS, sSType, idD, sDType);
					nRel++;
				}
			}
		}
		return nRel;
	}
	
	
	/**
	 * Carrega totes les relacions d'un node en la direcció indicada.
	 * @param idNode Node.
	 * @param sNType Node type.
	 * @param iDirection Direcció. 1-> node es l'origen; 2->node es el destí; 3->ambdues.
	 * @return num relacions carregades.
	 * @throws SemanticException 
	 */
	public int loadRelations(int idNode, String sNType, int iDirection) throws SemanticException {
		// TODO Auto-generated method stub
		List lRes = new ArrayList();
		List lnod = new ArrayList();
		Hashtable hnod = new Hashtable();
		Hashtable dto;
		Relation rRelation;
		String relType, sSType, sDType; 
		int idS, idD, nRel = 0;
		
		Iterator iter=  hRelationTypes.values().iterator();
		List lType = new ArrayList();
		String type;
		while (iter.hasNext()){
			lRes.addAll(((RelationType) iter.next()).loadRelations(idNode,sNType,iDirection));			
		}
		
		//Accedeix al repositori de dades sol·licitant les relacions d'origen o de destí.
		//lRes = dsDatSource.getRelations(idNode, sNType, lType, iDirection);//type);
		
		//Recorrer els resultats obtinguts i crea cada un dels nodes necessaris.
		for (int j = 0; j < lRes.size(); j++){
			dto = (Hashtable) lRes.get(j);
			idS = ((Integer)dto.get("idSource")).intValue();
			sSType = (String)dto.get("sourceType");
			idD = ((Integer)dto.get("idDest")).intValue();
			sDType = (String)dto.get("destType");
			relType = (String)dto.get("relationType");
			
			if (!this.hRelationTypes.containsKey(relType)){
				SemanticException the = new SemanticException("NonexistentRelationType");
				the.setCode(SemanticException.NONEXISTENTRELATIONTYPE);
				the.setObject(relType);
				throw the;
			}
			if (!this.existsNode(idS, sSType)){
				List lNeededNode = new ArrayList();
				lNeededNode.add(new Integer(idS));
				lNeededNode.add(sSType);
				if (!hnod.containsKey(sSType)){
					hnod.put(sSType,new ArrayList());
				}
				((List)hnod.get(sSType)).add(lNeededNode);
				//lnod.add(lNeededNode);
			}
			if (!this.existsNode(idD, sDType)){
				List lNeededNode = new ArrayList();
				lNeededNode.add(new Integer(idD));
				lNeededNode.add(sDType);
				if (!hnod.containsKey(sDType)){
					hnod.put(sDType,new ArrayList());
				}
				((List)hnod.get(sDType)).add(lNeededNode);
				//lnod.add(lNeededNode);
			}
		}
		//Carrega tots els nodes d'un sol cop.
		if (!hnod.isEmpty()) loadNodes(hnod);
		//Crea les relacions entre tots els nodes carregats correctament. 
		//Retorna un llistat amb els nodes inexistents.
		for (int j = 0; j < lRes.size(); j++){
			dto = (Hashtable) lRes.get(j);
			idS = ((Integer)dto.get("idSource")).intValue();
			sSType = (String)dto.get("sourceType");
			idD = ((Integer)dto.get("idDest")).intValue();
			sDType = (String)dto.get("destType");
			relType = (String)dto.get("relationType");
			if (this.existsNode(idS, sSType) && this.existsNode(idD, sDType)){
				rRelation = ((RelationType)hRelationTypes.get(relType)).addRelation(dto);
				//rRelation = ((RelationType)hRelationTypes.get(relType)).addRelation(idS, sSType, idD, sDType);
				nRel++;
			}
		}
		return nRel;		
	}

	/**
	 * Load all nodes into given hashtable.
	 * @param hNod Hash de {type=[id1,id2],type2=[id4,id98]}
	 * @return Llistat de {idNode,SemanticError} amb els nodes que no s'han pogut carregar i l'error donat. Les relacions on aquest apareixien tampoc s'han carregat.
	 * @throws SemanticException 
	 */
	public List loadNodes(Hashtable hNod) throws SemanticException {
		// TODO Auto-generated method stub
		Node n = null;
		List ldto, lError;
		Hashtable hDTO;
		lError = new ArrayList();
		Hashtable dto;
		Iterator iter;
		String type;
		Enumeration e = hNod.keys();
		while (e.hasMoreElements()){
			type = (String) e.nextElement();
			lError.addAll(((NodeType)this.hNodeTypes.get(type)).loadNodes((List)hNod.get(type)));
		}
		return lError;
	}

	
	
	public int loadAllNodes() throws SemanticException{
		Hashtable dto;
		int iQntt = 0;
		Node nNewNode;
		String nType;
		
		Enumeration e = this.hNodeTypes.keys();
		while (e.hasMoreElements()){
			nType = (String) e.nextElement();
			iQntt+=((NodeType)this.hNodeTypes.get(nType)).loadAllNodes();
		}
		return iQntt;		
	}
	
	
	public int loadAllRelations() throws SemanticException{
		List lRes = new ArrayList();
		Hashtable hRes = new Hashtable();
		List lNod = new ArrayList();
		Hashtable hnod = new Hashtable();
		
		Hashtable dto;
		Relation rRelation;
		String relType, sSType, sDType, rType; 
		int idS, idD, iQntt = 0;
		//Accedeix al repositori de dades sol·licitant les relacions d'origen o de destí.
		
		Iterator iter=  this.getListRelationTypes().iterator();
		List lType = new ArrayList();
		String type;
		while (iter.hasNext()){
			type = (String)iter.next();
			hRes.put(type,((RelationType) this.hRelationTypes.get(type)).loadRelations());
		}
		
		Enumeration en = hRes.keys();
		while (en.hasMoreElements()){	
			rType = (String) en.nextElement();
			iter = ((List) hRes.get(rType)).iterator();
			//Recorrer els resultats obtinguts i crea cada un dels nodes necessaris.
			//for (int j = 0; j < lRes.size(); j++){
			while (iter.hasNext()){
				//dto = (Hashtable) lRes.get(j);
				dto = (Hashtable) iter.next();
				idS = ((Integer)dto.get("idSource")).intValue();
				sSType = (String)dto.get("sourceType");
				idD = ((Integer)dto.get("idDest")).intValue();
				sDType = (String)dto.get("destType");
				relType = (String)dto.get("relationType");
				if (!this.hRelationTypes.containsKey(relType)){
					SemanticException seE = new SemanticException("NonexistentRelationType");
					seE.setCode(SemanticException.NONEXISTENTRELATIONTYPE);
					seE.setObject(relType);
					throw seE;
				}
				if (!this.existsNode(idS, sSType)){
					List lNeededNode = new ArrayList();
					lNeededNode.add(new Integer(idS));
					lNeededNode.add(sSType);
					if (!hnod.containsKey(sSType)){
						hnod.put(sSType,new ArrayList());
					}
					((List)hnod.get(sSType)).add(lNeededNode);
					//lNod.add(lNeededNode);
				}
				if (!this.existsNode(idD, sDType)){
					List lNeededNode = new ArrayList();
					lNeededNode.add(new Integer(idD));
					lNeededNode.add(sDType);
					if (!hnod.containsKey(sDType)){
						hnod.put(sDType,new ArrayList());
					}
					((List)hnod.get(sDType)).add(lNeededNode);
				}
			}
		}
		//Carrega tots els nodes d'un sol cop.
		if (!hnod.isEmpty()) loadNodes(hnod);
		//Crea les relacions entre tots els nodes carregats correctament. 
		//Retorna un llistat amb els nodes inexistents.
		en = hRes.keys();		
		while (en.hasMoreElements()){	
			rType = (String) en.nextElement();
			iter = ((List) hRes.get(rType)).iterator();
			//Recorrer els resultats obtinguts i crea cada un dels nodes necessaris.
			while (iter.hasNext()){
				dto = (Hashtable) iter.next();
				idS = ((Integer)dto.get("idSource")).intValue();
				sSType = (String)dto.get("sourceType");
				idD = ((Integer)dto.get("idDest")).intValue();
				sDType = (String)dto.get("destType");
				relType = (String)dto.get("relationType");
				if (this.existsNode(idS, sSType) && this.existsNode(idD, sDType)){
					rRelation = ((RelationType)hRelationTypes.get(relType)).addRelation(dto);//idS, sSType, idD, sDType);
					iQntt++;
				}
			}
		}
		return iQntt;	
	}

	
	/**
	 * Returns all nodes which have a relation typed 'relType' with node 'idNode'.
	 * Return a list with all them.
	 * @param idNode Node related to identifier.
	 * @param sNType Node related to type.
	 * @param relType Relations searched.
	 * @param direction Direction. SOURCE means the given node is the source of the relation.
	 * @return {nodes related to it.}
	 * @throws SemanticException
	 */
	public List getNodesRelated(int idNode, String sNType, String relType, int direction) throws SemanticException{
		ArrayList arn = new ArrayList();
		this.getNode(idNode,sNType);
		if (!this.existsNode(idNode,sNType)){
			return arn; 
		}
		arn = (ArrayList) this.getRelations(idNode,sNType, relType,direction);
		Iterator iter;
		Relation r;
		iter = arn.iterator();
		ArrayList lRes = new ArrayList();
		
		while (iter.hasNext()){
			r = (Relation) iter.next();
			switch (direction){
			case RelationType.SOURCE : 
						lRes.add(this.getNode(r.getIdDest(),r.getDestType()));
						break;
			case RelationType.DEST : 
						lRes.add(this.getNode(r.getIdSource(),r.getSourceType()));
						break;
			case RelationType.ALL : 
						if (r.getIdSource() == idNode){
							lRes.add(this.getNode(r.getIdDest(),r.getDestType()));
						}else{
							lRes.add(this.getNode(r.getIdSource(),r.getSourceType()));
						}
			}
		}
		
		return lRes;
	}
	
	
	

	/**
	 * Adds the node in to the system.
	 * @param nNewNode Node to add.
	 * @return New node created and inserted into the system
	 * @throws SemanticException
	 */
	public Node addNewNode(Node nNewNode) throws SemanticException{
		Node nn;
		int newIdNode;
		if (this.hNodeTypes.containsKey(nNewNode.getNodeType())){
			newIdNode = ((NodeType)this.hNodeTypes.get(nNewNode.getNodeType())).addNewNode(nNewNode);
			this.getRelations(newIdNode,nNewNode.getNodeType(),RelationType.ALL);
		}
		return nNewNode;
	}
	
	
	/**
	 * Change values of the node identified with 'idNode' and 'sNType' with the values into 'dto'.
	 * @param idNode Id of the node.
	 * @param sNType Type of the node.
	 * @param dto Values to change.
	 * @return the node with the changed values.
	 * @throws SemanticException
	 */
	public Node setNode(int idNode, String sNType, Hashtable dto) throws SemanticException{
		Node nn;
		try{
			nn = ((NodeType)this.hNodeTypes.get(sNType)).setNode(idNode, dto);			
		}catch(NullPointerException npe){
			SemanticException seE = new SemanticException("NonExistentNodeType");
			seE.setCode(SemanticException.NONEXISTENTNODETYPE);
			seE.setObject(sNType);
			throw seE;
		}
		return nn;
	}
	
	
	/**
	 * Delete the node with the given idNode and from type sNType and its relations.
	 * @param idNode Id of the node.
	 * @param sNType Type of the node.
	 * @return Deleted node.
	 * @throws SemanticException
	 */
	public Node delNode(int idNode, String sNType) throws SemanticException{
		Node nn;
		Enumeration eRT;
		RelationType rt;
		ArrayList ldel = new ArrayList();
		try{
			//Delete all relations related to node 'idNode'
			eRT = this.hRelationTypes.elements();
			while (eRT.hasMoreElements()){
				rt = ((RelationType)eRT.nextElement());
				ldel.addAll(rt.delRelationsNode(idNode, sNType));								
			}	
			try{
				//Delete node 'idNode'
				nn = ((NodeType)this.hNodeTypes.get(sNType)).delNode(idNode);
			}catch(SemanticException se){
				//If there is any error then add deleted relations to system.
				Iterator iter = ldel.iterator();
				while (iter.hasNext()){
					this.addNewRelation((Relation)iter.next());
				}				
				throw se;
			}				
		}catch(NullPointerException npe){
			SemanticException seE = new SemanticException("NonExistentNodeType");
			seE.setCode(SemanticException.NONEXISTENTNODETYPE);
			seE.setObject(sNType);
			throw seE;
		}
		return nn;
	}
	
	
	
	/**
	 * Adds new Relation on System if any of its nodes exists.
	 * @param rNewRel New Relation.
	 * @return Created and inserted relation into the system.
	 * @throws SemanticException
	 */
	public Relation addNewRelation(Relation rNewRel) throws SemanticException{
		Relation rn;
		
		//Comprovar que existeixen els nodes, sino existeixen no s'incerta la relació.
		if (!this.existsNode(rNewRel.getIdSource(), rNewRel.getSourceType())){
			this.getNode(rNewRel.getIdSource(), rNewRel.getSourceType());
		}
		if (!this.existsNode(rNewRel.getIdDest(), rNewRel.getDestType())){
			this.getNode(rNewRel.getIdDest(), rNewRel.getDestType());
		}		
		rn = ((RelationType)hRelationTypes.get(rNewRel.getRelationType())).addRelation(rNewRel);

		return rn;
	}
	
	
	/**
	 * Changes values of the relation rRel in the system with the values into dto.
	 * @param rRel Relation to change values.
	 * @param dto Values to change
	 * @return Relation with changed values 
	 * @throws SemanticException
	 */
	public Relation setRelation(Relation rRel, Hashtable dto) throws SemanticException{
		Relation rn;
		
		try{
			rn = ((RelationType)hRelationTypes.get(rRel.getRelationType())).setRelation(rRel, dto);
			if (dto.containsKey("relationType")){
				((RelationType)hRelationTypes.get(rRel.getRelationType())).delRelation(rn);
				rn.setRelationType((String)dto.get("relationType"));
				((RelationType)hRelationTypes.get(rn.getRelationType())).addRelation(rn);
			}
		}catch(NullPointerException npe){
			SemanticException seE = new SemanticException("NonExistentNodeType");
			seE.setCode(SemanticException.NONEXISTENTNODETYPE);
			seE.setObject(rRel.getRelationType());
			throw seE;
		}
		return rn;
	
	}
	
	/**
	 * Delete the Relation with the same values as rNewRel.
	 * @param rNewRel Relation with the same values as desired relation to delete.
	 * @throws SemanticException
	 */
	public void delRelation(Relation rNewRel) throws SemanticException{
		Relation rn;
		try{
			((RelationType)hRelationTypes.get(rNewRel.getRelationType())).delRelation(rNewRel);			
		}catch(NullPointerException npe){
			SemanticException seE = new SemanticException("NonExistentNodeType");
			seE.setCode(SemanticException.NONEXISTENTNODETYPE);
			seE.setObject(rNewRel.getRelationType());
			throw seE;
		}
	}
	
	/**
	 * Refresh the given node. Reload it from database system.
	 * @param idNode Id of the node.
	 * @param sNType Type of the node.
	 * @return Node reloaded.
	 * @throws SemanticException
	 */
	public Node refreshNode(int idNode, String sNType) throws SemanticException{
		Node nRef = null;
		try{
			if (this.hNodeTypes.containsKey(sNType))
				nRef = ((NodeType)this.hNodeTypes.get(sNType)).refresh(idNode);
		}catch (SemanticException se){
			if (se.getCode() != SemanticException.EXISTDIFERENTNODE && se.getCode() != SemanticException.EXISTENTNODE){
				throw se;
			}
		}
		return nRef;
	}
	


	/**
	 * Search 'query' string from all nodes which type were in 'lType' which have 'query' string in
	 * its 'description' or 'term'.
	 * @param query Query searched.
	 * @param lType List of nodeTypes to search in.
	 * @return Hashtable with all nodes that contains 'query' string. {type1=[n1,n2],type2=[n5,n9]}
	 * @throws SemanticException
	 */
	public Hashtable searchNode(String query, List lType) throws SemanticException{
		Hashtable hRes = new Hashtable();
		Hashtable dto;
		String nType;
		int idNode;
		Iterator itlis = lType.iterator();
		
		while(itlis.hasNext()){
			nType = (String)itlis.next();
			try{ 
				if (this.hNodeTypes.containsKey(nType))
					hRes.put(nType,((NodeType)this.hNodeTypes.get(nType)).search(query));
				}catch(Exception e){
					//Si hi ha algun error no cal tractar-lo. Es passa al seguent típus on buscar sens més.}
				}
		}
		
		return hRes;		
	}
	
	/*fi funcionailtats del thesaure*/
}
