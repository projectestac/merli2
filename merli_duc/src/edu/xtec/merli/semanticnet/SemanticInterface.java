package edu.xtec.merli.semanticnet;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

import edu.xtec.gescurriculum.gestorcurriculum.DUCException;
import edu.xtec.merli.utils.Utility;
import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticException;
import edu.xtec.semanticnet.SemanticNet;

public class SemanticInterface {

    private static final Logger logger = Logger.getRootLogger();//("xtec.duc");

    public static SemanticNet snet;
    //public static SemanticNet thes;

    /**
     *
     */
    public SemanticInterface() {
        if (snet == null) {
            logger.info("Loading information of SemanticNet.");
            Hashtable hprops = new Hashtable();
            Properties pProp = new Properties();
            /*Carrega el properties amb l'adreça de l'XML de configuració del SemanticNet
             InputStream is = getClass().getResourceAsStream("../../../../semanticnet.properties");
             is = getClass().getResourceAsStream("../../../../semanticnet.properties");*/
            InputStream is = getClass().getResourceAsStream("/semanticnet.properties");
            //is = getClass().getResourceAsStream("/semanticnet.properties");
            pProp = new Properties();
            try {
                pProp.load(is);
                is.close();
            } catch (Exception e2) {
                logger.error("Can't load Properites 'semanticnet.properties'");
            }

            hprops.put("xml", (String) pProp.get("xml_definition"));
            hprops.put("esquema_validacio", (String) pProp.get("esquema_validacio"));
            logger.info("XML_DEFINITION: " + (String) pProp.get("xml_definition"));
            logger.info("esquema_validacio: " + (String) pProp.get("esquema_validacio"));
            try {
                snet = new SemanticNet(hprops);
            } catch (SemanticException e) {
                e.printStackTrace();
                logger.error("Can't create object 'snet' as SemanticNet object." + e.getMessage());
            }
        }
    }

    /**
     * Retrona el node amb 'idNode' com a id. i del típus 'sNType'.
     *
     * @param idNode Identificador del node.
     * @param sNType Típus del node.
     * @return
     */
    public Node getNode(int idNode, String sNType) {
        Node n = null;
        if (sNType.compareTo("thesaurus") == 0) {
            try {
                n = snet.getNode(idNode, sNType);
            } catch (SemanticException e) {
                logger.warn("Cann't load Thesaurus node");
            }
        } else {
            try {
                n = snet.getNode(idNode, sNType);
            } catch (SemanticException e) {
                logger.warn("Cann't load other node type:" + idNode + " type:" + sNType);
            }
        }
        return n;
    }

    public ArrayList getThesaurus(int idNode) {
        // TODO Auto-generated method stub
        ArrayList al = new ArrayList();
        Iterator iter;
        Node n;
        try {
            if (idNode == 0) {
                try {
                    iter = ((ArrayList) snet.getNodesRelated(idNode, "thesaurus", "OT", RelationType.SOURCE)).iterator();
                } catch (SemanticException e) {
                    // TODO Auto-generated catch block
                    iter = ((ArrayList) snet.getNodesRelated(idNode, "thesaurus", "OT", RelationType.DEST)).iterator();
                }
            } else {
                try {
                    ArrayList al2 = new ArrayList();
                    //iter = ((ArrayList) thes.getNodesRelated(idNode,"thesaurus","OT",RelationType.DEST)).iterator();
                    iter = ((ArrayList) snet.getListRelationTypes()).iterator();
                    while (iter.hasNext()) {
                        al2.addAll((ArrayList) snet.getNodesRelated(idNode, "thesaurus", (String) iter.next(), RelationType.SOURCE));
                    }
                    iter = al2.iterator();
                } catch (SemanticException e) {
                    // TODO Auto-generated catch block
                    iter = ((ArrayList) snet.getNodesRelated(0, "thesaurus", "OT", RelationType.DEST)).iterator();
                }
            }
            while (iter.hasNext()) {
                n = (Node) iter.next();
                al.add(n.toDTO());//h);		
            }
            al = (ArrayList) Utility.orderNodeListByNote(al);
        } catch (Exception e) {
            logger.warn("Cann't load Thesaurus information");
            Hashtable h = new Hashtable();
            h.put("term", "Problemes amb el servidor del Thesaure.");
            h.put("idNode", new Integer(-1));
            h.put("nodeType", "content");
            h.put("category", "etapa");
            al.add(h);
        }
        return al;
    }

    /**
     * Retorna tots els nodes del tipus "level" relacionats amb el node donat.
     * Retorna els fills d'aquest, els seus germans, el seus antecessors i els
     * corresponents germans de cada un d'aquests.
     *
     * @param idNode Identificador del node amb quí han d'estar relacionats.
     * @return Llista de {infonode} on els infonode contenen la informació
     * necessaria d'un node.
     */
    public ArrayList getLevels(int idNode) {
        //int idNode = Integer.parseInt(sidNode);
        if (idNode <= 0) {
            idNode = 0;
        }
        ArrayList al = new ArrayList();
        ArrayList alf = new ArrayList();
        ArrayList lOnc = new ArrayList();
        ArrayList lRes = new ArrayList();
        Hashtable h, hs, h2;
        Node n, n2;
        int acid;
        Iterator iter;
        try {
            try {
                n = snet.getNode(idNode, "level");
            } catch (Exception e) {
                n = snet.getNode(0, "level");
            }
            //Ell mateix.
            h = n.toDTO();
            h.put("list", new ArrayList());
            acid = n.getIdNode();
            //Els fills.
            try {
                al = (ArrayList) snet.getNodesRelated(idNode, "level", "RLL", RelationType.DEST);
            } catch (Exception e) {
                acid = 0;
                al = (ArrayList) snet.getNodesRelated(0, "level", "RLL", RelationType.DEST);
            }
            iter = al.iterator();
            while (iter.hasNext()) {
                n2 = (Node) iter.next();
                ((ArrayList) h.get("list")).add(n2.toDTO());//hs);				
            }
            //La família
            if (n.getProperties() != null && n.getProperties().containsKey("relatedLevel")) {
                //Fills del Pare, germans.
                al = (ArrayList) snet.getNodesRelated(((Integer) n.getProperties().get("relatedLevel")).intValue(), "level", "RLL", RelationType.DEST);
                //Recupera el valor del node 'level' pare i en guarda els valors.
                n = snet.getNode(((Integer) n.getProperties().get("relatedLevel")).intValue(), "level");

                if (n.getIdNode() != 0) {
                    acid = n.getIdNode();
                    //Info del pare.
                    h2 = n.toDTO();// new Hashtable();
                    h2.put("list", new ArrayList());

                    //carrega els Germans
                    iter = al.iterator();
                    while (iter.hasNext()) {
                        n2 = (Node) iter.next();
                        if (n2.getIdNode() != idNode) {
                            ((ArrayList) h2.get("list")).add(n2.toDTO());//hs);
                        } else {
                            ((ArrayList) h2.get("list")).add(h);
                        }
                    }
                    //Si l'avi es tipus 0, no fer res.
                    if (n.getProperties() != null && n.getProperties().containsKey("relatedLevel") && ((Integer) n.getProperties().get("relatedLevel")).intValue() != 0) {
                        alf = (ArrayList) snet.getNodesRelated(((Integer) n.getProperties().get("relatedLevel")).intValue(), "level", "RLL", RelationType.DEST);

                        iter = alf.iterator();
                        while (iter.hasNext()) {
                            n2 = (Node) iter.next();
                            if (n2.getIdNode() != acid) {
                                lOnc.add(n2.toDTO());//h);
                            } else {
                                lOnc.add(h2);
                            }
                        }
                        acid = ((Integer) n.getProperties().get("relatedLevel")).intValue();
                    } else {
                        lOnc = (ArrayList) h2.get("list");
                    }
                } else {
                    lOnc = (ArrayList) h.get("list");
                }
            } else {
                //El node passat és el 0. Punt d'origen.
                if (((ArrayList) h.get("list")).isEmpty()) {
                    h = new Hashtable();
                    h.put("term", "Clica aquí per afegir un node Level.");
                    h.put("idNode", new Integer(-1));
                    h.put("nodeType", "level");
                    h.put("category", "etapa");

                    al.clear();
                    al.add(h);
                    return al;
                } else {
                    return (ArrayList) h.get("list");
                }
            }
            alf = (ArrayList) snet.getNodesRelated(0, "level", "RLL", RelationType.DEST);
            //Inicials
            iter = alf.iterator();
            while (iter.hasNext()) {
                n = (Node) iter.next();
                h = n.toDTO();//new Hashtable();
                if (n.getIdNode() == acid) {
                    h.put("list", lOnc);
                }
                lRes.add(h);
            }
            if (!lRes.isEmpty()) {
                return lRes;
            }

        } catch (SemanticException e) {
            // TODO Auto-generated catch block
            logger.warn("Error on load Level information.");
        } catch (Exception e) {
            logger.warn(e.getMessage() + " error on getting Levels.");
        }
        h = new Hashtable();
        h.put("term", "Clica aquí per afegir un node Level.");
        h.put("idNode", new Integer(-1));
        h.put("nodeType", "level");
        h.put("category", "etapa");

        al.clear();
        al.add(h);
        return al;
    }

    /**
     * Retorna l'identificador del node "etapa" del que penja el node "level"
     * donat.
     *
     * @param idLevel Identificador del node del que es vol coneixer l'"etapa" a
     * la que pertany.
     * @return Identificador de l'"etapa" a la que pertany el node donat.
     */
    public int getEtapaLevel(int idLevel) {
        Node n;
        if (idLevel <= 0) {
            return 0;
        }
        int acid = 0;
        try {
            n = snet.getNode(idLevel, "level");
            acid = ((Integer) n.getProperties().get("relatedLevel")).intValue();
            while (acid != 0) {
                n = snet.getNode(acid, "level");
                acid = ((Integer) n.getProperties().get("relatedLevel")).intValue();
            }
            acid = n.getIdNode();
        } catch (SemanticException e) {
            logger.warn("Cann't find information about 'etapa' related with level 'idLevel'.");
        } catch (Exception e) {
            logger.warn(e.getMessage() + " error on getting EtapaLevel.");
        }
        return acid;
    }

    /**
     * Retorna tots els nodes del tipus "arees" relacionats amb el node
     * 'idLevel' donat.
     *
     * @param idArea Identificador del node "area" amb quí han d'estar
     * relacionats.
     * @param idLevel Identificador del node "level" amb quí han d'estar
     * relacionats.
     * @return Llista de {infonode} on els 'infonode' contenen la informació
     * necessaria d'un node.
     */
    public ArrayList getArees(int idArea, int idLevel) {

        ArrayList al = new ArrayList();
        ArrayList l = new ArrayList();
        Hashtable h = new Hashtable();
        Iterator iter;
        Node n;
        int acid;
        if (idLevel > 0) {
            try {
                n = snet.getNode(idLevel, "level");
                try {
                    acid = ((Integer) n.getProperties().get("relatedLevel")).intValue();
                } catch (Exception e) {
                    acid = 0;
                }
                while (acid != 0) {
                    n = snet.getNode(acid, "level");
                    acid = ((Integer) n.getProperties().get("relatedLevel")).intValue();
                }
                acid = n.getIdNode();

                al = (ArrayList) snet.getNodesRelated(acid, "level", "RAL", RelationType.DEST);
                iter = al.iterator();
                while (iter.hasNext()) {
                    n = (Node) iter.next();
                    l.add(n.toDTO());//h);		
                }
                if (!l.isEmpty()) {
                    return l;
                }

            } catch (SemanticException e) {
                logger.warn("Error on load Area information.");
            } catch (Exception e) {
                logger.warn(e.getMessage() + " error on getting Areas.");
            }
        }
        al.clear();
        h = new Hashtable();
        h.put("term", "Clica aquí per afegir un node Area.");
        h.put("idNode", new Integer(-1));
        h.put("nodeType", "area");
        h.put("description", "Clica aquí per agfegir un nou node al sistema.");

        al.add(h);

        return al;
    }

    public ArrayList getContentsRelated(int idContent, int idLevel, int idArea, String category) {
        Hashtable h = new Hashtable();
        Hashtable h2 = new Hashtable();
        Hashtable hPoss = new Hashtable();
        ArrayList lLevel, lArea = null, l2 = new ArrayList();
        Iterator iter;
        Iterator iter2;
        int idCont = idContent;
        boolean notFound;
        Node n, np, npater = null;

        try {
            npater = snet.getNode(idCont, "content");
        } catch (SemanticException se) {
            idCont = 0;
            try {
                npater = snet.getNode(idCont, "content");
            } catch (SemanticException e) {
                return l2;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage() + " error on getting ContentsRelated.");
        }

        h = npater.toDTO();
        ArrayList l;
        try {
            l = (ArrayList) snet.getNodesRelated(idCont, "content", "RCC", RelationType.DEST);
        } catch (SemanticException e3) {
            l = new ArrayList();
        } catch (Exception e) {
            l = new ArrayList();
            logger.warn(e.getMessage() + " error on getting ContentRelated.");
        }
        try {
            lLevel = (ArrayList) snet.getNodesRelated(idLevel, "level", "RCL", RelationType.DEST);
            //if (lLevel == null || lLevel.isEmpty()) lLevel = (ArrayList) snet.getNodesRelated(this.getEtapaLevel(idLevel),"level","RCL",RelationType.DEST);
        } catch (SemanticException e2) {
            lLevel = new ArrayList();
        } catch (Exception e) {
            lLevel = new ArrayList();
            logger.warn(e.getMessage() + " error on getting ContentRelated.");
        }
        try {
            lArea = (ArrayList) snet.getNodesRelated(idArea, "area", "RCA", RelationType.DEST);
        } catch (SemanticException e1) {
            lArea = new ArrayList();
        } catch (Exception e) {
            lArea = new ArrayList();
            logger.warn(e.getMessage() + " error on getting ContentRelated.");
        }

        try {
            iter = lLevel.iterator();
            while (iter.hasNext()) {
                np = (Node) iter.next();
                if (lArea != null && !lArea.isEmpty()) {
                    iter2 = lArea.iterator();
                    notFound = true;
                    while (iter2.hasNext() && notFound) {
                        n = (Node) iter2.next();
                        if (n.getIdNode() == np.getIdNode()) {
                            notFound = false;
                            hPoss.put(new Integer(n.getIdNode()), n.getCategory());
                        }
                    }
                } else {
                    if (idArea <= 0) {
                        hPoss.put(new Integer(np.getIdNode()), np.getCategory());
                    }
                }
            }
            iter = l.iterator();
            l2 = new ArrayList();
            while (iter.hasNext()) {
                n = (Node) iter.next();
                if (n.getCategory().compareTo(category) == 0 && hPoss.containsKey(new Integer(n.getIdNode()))) {
                    l2.add(n.toDTO());
                }
                //System.out.println("Fills"+n.getIdNode()+n.getCategory()+n.getProperties().get("relatedContent"));
            }
            while (npater.getIdNode() != 0) {
                l = (ArrayList) snet.getNodesRelated(((Integer) npater.getProperties().get("relatedContent")).intValue(), "content", "RCC", RelationType.DEST);
                iter = l.iterator();
                l = l2;
                l2 = new ArrayList();
                while (iter.hasNext()) {
                    n = (Node) iter.next();
                    if (n.getCategory().compareTo(category) == 0 && hPoss.containsKey(new Integer(n.getIdNode()))) {
                        if (npater.getIdNode() != n.getIdNode()) {
                            l2.add(n.toDTO());
                        } else {
                            h.put("list", l);
                            l2.add(h);
                        }
                    }
                    //System.out.println("Brothers"+i+n.getIdNode()+n.getCategory()+n.getProperties().get("relatedContent"));
                }
                npater = snet.getNode(((Integer) npater.getProperties().get("relatedContent")).intValue(), "content");
                h = npater.toDTO();
            }
        } catch (SemanticException e) {
            // TODO Auto-generated catch block
            logger.warn("Error on load Contents related with level,area,content given information.");
        } catch (Exception e) {
            logger.warn(e.getMessage() + " error on getting ContentsRelated.");
        }

        return l2;
    }

    /**
     * Retorna tots els nodes del típus "content" relacionats amb els nodes
     * donats.
     *
     * @param idContent Identificador del node "content" amb quí han d'estar
     * relacionats.
     * @param idLevel Identificador del node "area" amb quí han d'estar
     * relacionats.
     * @param idArea Identificador del node "level" amb quí han d'estar
     * relacionats.
     * @return Llista de {infonode} on els 'infonode' contenen la informació
     * necessaria d'un node.
     */
    public Hashtable getContents(int idContent, int idLevel, int idArea) {
        // TODO Auto-generated method stub
        Hashtable hRes = new Hashtable();
        Hashtable h;
        ArrayList lLevel = null;
        ArrayList lArea = null;
        Iterator itLevel, itArea;
        boolean notFound;
        Node nl, na;
        int idc = 0, idp = 0, ida = 0;

        try {
            Node n = snet.getNode(idContent, "content");
            if (n.getCategory().compareTo("cc") == 0) {
                idc = idContent;
            }
            if (n.getCategory().compareTo("ca") == 0) {
                ida = idContent;
            }
            if (n.getCategory().compareTo("cp") == 0) {
                idp = idContent;
            }
        } catch (SemanticException e) {
        }

        hRes.put("cp", this.getContentsRelated(idp, idLevel, idArea, "cp"));
        hRes.put("cc", this.getContentsRelated(idc, idLevel, idArea, "cc"));
        hRes.put("ca", this.getContentsRelated(ida, idLevel, idArea, "ca"));

        if (((ArrayList) hRes.get("cp")).isEmpty()) {
            h = new Hashtable();
            h.put("term", "Clica aquí per afegir un node Contingut.");
            h.put("idNode", new Integer(-1));
            h.put("nodeType", "content");
            h.put("category", "cp");
            h.put("description", "Clica aquí per agfegir un nou node al sistema.");
            ((ArrayList) hRes.get("cp")).add(h);
        }
        if (((ArrayList) hRes.get("cc")).isEmpty()) {
            h = new Hashtable();
            h.put("term", "Clica aquí per afegir un node Contingut.");
            h.put("idNode", new Integer(-1));
            h.put("nodeType", "content");
            h.put("category", "cc");
            h.put("description", "Clica aquí per agfegir un nou node al sistema.");
            ((ArrayList) hRes.get("cc")).add(h);
        }
        if (((ArrayList) hRes.get("ca")).isEmpty()) {
            h = new Hashtable();
            h.put("term", "Clica aquí per afegir un node Contingut.");
            h.put("idNode", new Integer(-1));
            h.put("nodeType", "content");
            h.put("category", "ca");
            h.put("description", "Clica aquí per agfegir un nou node al sistema.");
            ((ArrayList) hRes.get("ca")).add(h);
        }

        return hRes;
    }

    /**
     * Retorna tots els nodes del típus "objective" relacionats amb els nodes
     * donats.
     *
     * @param idLevel Identificador del node "area" amb quí han d'estar
     * relacionats.
     * @param idArea Identificador del node "level" amb quí han d'estar
     * relacionats.
     * @return Llista de {infonode} on els 'infonode' contenen la informació
     * necessaria d'un node.
     */
    public Hashtable getObjectives(int idLevel, int idArea) {
        // TODO Auto-generated method stub
        Hashtable hRes = new Hashtable();
        Hashtable h;
        ArrayList lLevel = null;
        ArrayList lArea = null;
        Iterator itLevel, itArea;
        boolean notFound;
        Node nl, na;
        hRes.put("op", new ArrayList());
        hRes.put("ot", new ArrayList());
        if (idLevel > 0 && idArea >= 0) {
            try {
                lLevel = (ArrayList) snet.getNodesRelated(idLevel, "level", "ROL", RelationType.DEST);
                if (lLevel == null || lLevel.isEmpty()) {
                    lLevel = (ArrayList) snet.getNodesRelated(this.getEtapaLevel(idLevel), "level", "ROL", RelationType.DEST);
                }
                lArea = (ArrayList) snet.getNodesRelated(idArea, "area", "ROA", RelationType.DEST);
            } catch (Exception e) {
                if (lLevel == null || lLevel.isEmpty()) {
                    lLevel = new ArrayList();
                }
            }
            itLevel = lLevel.iterator();
            while (itLevel.hasNext()) {
                nl = (Node) itLevel.next();
                if (lArea != null) {
                    itArea = lArea.iterator();
                    notFound = true;
                    while (itArea.hasNext() && notFound) {
                        na = (Node) itArea.next();
                        if (na.getIdNode() == nl.getIdNode()) {
                            notFound = false;
                            ((ArrayList) hRes.get(nl.getCategory())).add(nl.toDTO());
                        }
                    }
                } else {
                    try {
                        ((ArrayList) hRes.get(nl.getCategory())).add(nl.toDTO());
                    } catch (NullPointerException n) {
                    }
                }
            }
        }

        if (((ArrayList) hRes.get("op")).isEmpty()) {
            h = new Hashtable();
            h.put("term", "Clica aquí per afegir un node Objectiu.");
            h.put("idNode", new Integer(-1));
            h.put("nodeType", "objective");
            h.put("category", "op");
            h.put("description", "Clica aquí per agfegir un nou node al sistema.");
            ((ArrayList) hRes.get("op")).add(h);
        }
        if (((ArrayList) hRes.get("ot")).isEmpty()) {
            h = new Hashtable();
            h.put("term", "Clica aquí per afegir un node Objectiu.");
            h.put("idNode", new Integer(-1));
            h.put("nodeType", "objective");
            h.put("category", "ot");
            h.put("description", "Clica aquí per agfegir un nou node al sistema.");
            ((ArrayList) hRes.get("ot")).add(h);
        }
        return hRes;
    }

    /**
     * Retorna un llistat amb tots els nodes germans del node donat.
     *
     * @param idNode Node del que s'en busquen els germans.
     * @param idLevel Identificador del node "level" relacionat al node 'idNode'
     * @param idArea Identificador del node "area" relacionat al node 'idNode'
     * @param nodeType Típus del node donat.
     * @return Llista de {infonode} on els 'infonode' contenen la informació
     * necessaria d'un node.
     */
    protected ArrayList getNodesList(int idNode, int idLevel, int idArea, String nodeType) {
        ArrayList l = null;
        Iterator iter;
        try {
            if (nodeType.compareTo("area") == 0) {
                l = (ArrayList) snet.getNodesRelated(this.getEtapaLevel(idLevel), "level", "RAL", RelationType.DEST);
                iter = l.iterator();
                l = new ArrayList();
                while (iter.hasNext()) {
                    l.add(((Node) iter.next()).toDTO());
                }
            }
            if (nodeType.compareTo("level") == 0) {
                Node n = snet.getNode(idNode, "level");
                l = (ArrayList) snet.getNodesRelated(((Integer) n.getProperties().get("relatedLevel")).intValue(), "level", "RLL", RelationType.DEST);
                iter = l.iterator();
                l = new ArrayList();
                while (iter.hasNext()) {
                    l.add(((Node) iter.next()).toDTO());
                }
            }
            if (nodeType.compareTo("content") == 0) {
                l = (ArrayList) snet.getNodesRelated(idNode, "content", "RCC", RelationType.DEST);
                iter = l.iterator();
                l = new ArrayList();
                while (iter.hasNext()) {
                    l.add(((Node) iter.next()).toDTO());
                }
            }
            if (nodeType.compareTo("objective") == 0) {
                Node n = snet.getNode(idNode, "objective");
                Hashtable h = this.getObjectives(idLevel, idArea);
                l = (ArrayList) h.get(n.getCategory());
            }
        } catch (Exception e) {
            logger.warn("Error on load brother nodes information.");
        }
        return l;
    }

    /**
     * Intercanvia les posicions de dos nodes. La del node donat 'idNode' i la
     * del node germà situat en la posició anterior o posterior, depenent del
     * valor de 'amunt'.
     *
     * @param idNode Identificador del node que es vol canviar de posició.
     * @param idLevel Identificador del node "level" relacionat amb aquest. Útil
     * en cas de ser del típus "content" o "objective.
     * @param idArea Identificador del node "area" relacionat amb aquest. Útil
     * en cas de ser del típus "content" o "objective.
     * @param nodeType Típus del node 'idNode'
     * @param amunt TRUE si es vol canviar la posició amb el germà superior,
     * altrament FALSE.
     * @throws DUCException
     */
    public void swapNodes(int idNode, int idLevel, int idArea, String nodeType, boolean amunt) throws DUCException {

        Iterator iter;
        ArrayList l = null;
        int id = idNode;
        try {
            if (nodeType.compareTo("content") == 0) {
                Node n = snet.getNode(idNode, "content");
                id = ((Integer) n.getProperties().get("relatedContent")).intValue();
            }

            l = this.getNodesList(id, idLevel, idArea, nodeType);
            id = idNode;
            l = Utility.orderListByPosition(l);
            Hashtable h1, h2 = null;
            iter = l.iterator();
            h1 = (Hashtable) iter.next();
            Object position = null;

            while (iter.hasNext() && position == null) {
                h2 = (Hashtable) iter.next();
                if (amunt) {
                    if (((Integer) h2.get("idNode")).intValue() == id) {
                        position = ((Hashtable) h2.get("properties")).get("position");
                        ((Hashtable) h2.get("properties")).put("position", ((Hashtable) h1.get("properties")).get("position"));
                        ((Hashtable) h1.get("properties")).put("position", position);
                        snet.setNode(((Integer) h1.get("idNode")).intValue(), nodeType, h1);
                        snet.setNode(((Integer) h2.get("idNode")).intValue(), nodeType, h2);
                    } else {
                        h1 = h2;
                    }
                } else {
                    if (((Integer) h1.get("idNode")).intValue() == id) {
                        position = ((Hashtable) h2.get("properties")).get("position");
                        ((Hashtable) h2.get("properties")).put("position", ((Hashtable) h1.get("properties")).get("position"));
                        ((Hashtable) h1.get("properties")).put("position", position);
                        snet.setNode(((Integer) h1.get("idNode")).intValue(), nodeType, h1);
                        snet.setNode(((Integer) h2.get("idNode")).intValue(), nodeType, h2);
                    } else {
                        h1 = h2;
                    }
                }
            }

        } catch (Exception e) {
            logger.warn("Error on move node position.");
            throw new DUCException(DUCException.ERRORMOVENODE);
        }
    }

    /**
     * Afegeix al sistema el node descrit en 'dtonode'.
     *
     * @param dtoNode Hashtable amb tota la informació necessaria del node.
     * @throws DUCException
     */
    public void addNode(Hashtable dtoNode) throws DUCException {
        Relation r = null, r2 = null, rc, re;
        ArrayList lNodes = new ArrayList();
        Hashtable orderNodes;
        Integer idPNodes = new Integer(0);
        String relType = "RL";
        String category;
        int position, res;
        try {
            //Trobar el node pare del que penja. 
            if (((String) dtoNode.get("nodeType")).compareTo("level") == 0) {
                idPNodes = (Integer) ((Hashtable) dtoNode.get("properties")).get("relatedLevel");
                //Posa el pare correcte al node.
                //try{
                Node nLevel = snet.getNode(idPNodes.intValue(), "level");
                Integer idPLevel = (Integer) nLevel.getProperties().get("relatedLevel");
                category = (String) dtoNode.get("category");
                if ((category.compareTo("curs") == 0 && nLevel.getCategory().compareTo("cicle") == 0)
                        || (category.compareTo("cicle") == 0 && nLevel.getCategory().compareTo("etapa") == 0)
                        || (category.compareTo("etapa") == 0 && nLevel.getCategory().compareTo("origen") == 0)) {
                    ((Hashtable) dtoNode.get("properties")).put("relatedLevel", new Integer(nLevel.getIdNode()));
                } else {
                    if (category.compareTo(nLevel.getCategory()) == 0) {
                        ((Hashtable) dtoNode.get("properties")).put("relatedLevel", idPLevel);
                    } else {
                        throw new DUCException(DUCException.ERRORADDNODE);
                    }
                }
                //	}catch(Exception e){throw new DUCException(DUCException.ERRORADDNODE);}
            }
            if (((String) dtoNode.get("nodeType")).compareTo("content") == 0) {
                idPNodes = (Integer) ((Hashtable) dtoNode.get("properties")).get("idContent");
                relType = "RC";
            }
//			if (((String)dtoNode.get("nodeType")).compareTo("objective") == 0){
//				idPNodes = (Integer) ((Hashtable) dtoNode.get("properties")).get("idObjective"); 
//				relType = "RO";
//			}
            if (((String) dtoNode.get("nodeType")).compareTo("area") == 0) {
                idPNodes = (Integer) ((Hashtable) dtoNode.get("properties")).get("relatedLevel");
                ((Hashtable) dtoNode.get("properties")).put("relatedLevel", new Integer(this.getEtapaLevel(idPNodes.intValue())));
                idPNodes = (Integer) ((Hashtable) dtoNode.get("properties")).get("idArea");
            }

            lNodes = this.getNodesList(idPNodes.intValue(), ((Integer) ((Hashtable) dtoNode.get("properties")).get("relatedLevel")).intValue(), ((Integer) ((Hashtable) dtoNode.get("properties")).get("idArea")).intValue(), (String) dtoNode.get("nodeType"));
            lNodes = Utility.orderListByPosition(lNodes);
            //Reordena tots els nodes germans i li dona la nova posició al nou node.
            int aux = 0;
            for (int i = 0; i < lNodes.size(); i++) {
                orderNodes = (Hashtable) lNodes.get(i);
                if (idPNodes.compareTo((Integer) orderNodes.get("idNode")) == 0) {
                    aux = 1;
                    ((Hashtable) dtoNode.get("properties")).put("position", new Integer(i));
                }
                if (!((Hashtable) orderNodes.get("properties")).containsKey("position")
                        || ((Integer) ((Hashtable) orderNodes.get("properties")).get("position")).intValue() != i + aux) {
                    ((Hashtable) orderNodes.get("properties")).put("position", new Integer(i + aux));
                    snet.setNode(((Integer) orderNodes.get("idNode")).intValue(), (String) dtoNode.get("nodeType"), orderNodes);
                }

            }

            Node n = new Node(dtoNode, snet);

            //Inserta el nou node al sistema.
            int inode;
            n = snet.addNewNode(n);
            snet.getNode(n.getIdNode(), n.getNodeType());
            res = n.getIdNode();

            //Afageix les relacions necessaries del nou node.
            if (((String) dtoNode.get("nodeType")).compareTo("objective") == 0 || ((String) dtoNode.get("nodeType")).compareTo("content") == 0) {
                inode = ((Integer) ((Hashtable) dtoNode.get("properties")).get("relatedLevel")).intValue();
                if (inode < 0) {
                    inode = 0;
                } else {
                    Node n2 = snet.getNode(inode, "level");
                    if (n2.getCategory().compareTo("curs") == 0) {
                        r = new Relation(relType + "L", n.getIdNode(), (String) dtoNode.get("nodeType"), n2.getIdNode(), "level");
                        n2 = snet.getNode(((Integer) n2.getProperties().get("relatedLevel")).intValue(), "level");
                        try {
                            snet.addNewRelation(r);
                        } catch (Exception e) {
                            snet.delRelation(r);
                        }
                    }
                    if (n2.getCategory().compareTo("cicle") == 0) {
                        rc = new Relation(relType + "L", n.getIdNode(), (String) dtoNode.get("nodeType"), n2.getIdNode(), "level");
                        n2 = snet.getNode(((Integer) n2.getProperties().get("relatedLevel")).intValue(), "level");
                        try {
                            snet.addNewRelation(rc);
                        } catch (Exception e) {
                            snet.delRelation(r);
                        }
                    }
                    if (n2.getCategory().compareTo("etapa") == 0) {
                        re = new Relation(relType + "L", n.getIdNode(), (String) dtoNode.get("nodeType"), n2.getIdNode(), "level");
                        try {
                            snet.addNewRelation(re);
                        } catch (Exception e) {
                            snet.delRelation(r);
                        }
                    }
                    if (((Hashtable) dtoNode.get("properties")).containsKey("idArea")) {
                        inode = ((Integer) ((Hashtable) dtoNode.get("properties")).get("idArea")).intValue();
                        r2 = new Relation(relType + "A", n.getIdNode(), (String) dtoNode.get("nodeType"), inode, "area");
                        try {
                            snet.addNewRelation(r2);
                        } catch (Exception e) {
                        }
                    }
                }
            }
            if (((Hashtable) dtoNode.get("properties")).containsKey("thesaurusKeys")) {
                String insThesaurus;
                ArrayList thes = (ArrayList) ((Hashtable) dtoNode.get("properties")).get("thesaurusKeys");
                for (int j = 0; j < thes.size(); j++) {
                    //Recupera el següent valor d'identificador per insertar a la taula cur_thesaurus.
                    r = new Relation("RET", n.getIdNode(), n.getNodeType(), ((Integer) thes.get(j)).intValue(), "thesaurus");
                    try {
                        snet.addNewRelation(r);
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception de) {
            de.printStackTrace();
            logger.error("Error on add new node");
            throw new DUCException(DUCException.ERRORADDNODE);
        }
    }

    /**
     * Modifica els valors presents en 'dtonode'. Els camps 'idNode' i
     * 'nodeType' hi són obligatoris.
     *
     * @param dtonode Hashtable amb la informació dels camps que es volen
     * modificar.
     * @throws DUCException
     */
    public void setNode(Hashtable dtonode) throws DUCException {
        try {
            snet.setNode(((Integer) dtonode.get("idNode")).intValue(), (String) dtonode.get("nodeType"), dtonode);
            if (((Hashtable) dtonode.get("properties")).containsKey("thesaurusKeys")) {
                ArrayList thes = (ArrayList) ((Hashtable) dtonode.get("properties")).get("thesaurusKeys");
                ArrayList retAct = (ArrayList) snet.getRelations(((Integer) dtonode.get("idNode")).intValue(), (String) dtonode.get("nodeType"), "RET", RelationType.ALL);
                Relation rel = null;
                int rel2;
                boolean exists;
                if (retAct.size() > 0) {
                    for (int j = 0; j < retAct.size(); j++) {
                        rel = (Relation) retAct.get(j);
                        exists = false;
                        for (int i = 0; i < thes.size() && !exists; i++) {
                            rel2 = ((Integer) thes.get(i)).intValue();
                            if (rel.getIdDest() == rel2) {
                                exists = true;
                                thes.remove(i);
                            }
                        }
                        if (!exists) {
                            snet.delRelation(rel);
                        }
                    }
                }
                for (int j = 0; j < thes.size(); j++) {
                    //Recupera el següent valor d'identificador per insertar a la taula notes.
                    rel = new Relation("RET", ((Integer) dtonode.get("idNode")).intValue(), (String) dtonode.get("nodeType"), ((Integer) thes.get(j)).intValue(), "thesaurus");
                    try {
                        snet.addNewRelation(rel);
                    } catch (Exception e) {
                    }
                }
            }
            snet.refreshNode(((Integer) dtonode.get("idNode")).intValue(), (String) dtonode.get("nodeType"));
        } catch (Exception e) {
            logger.warn("Error on modify node information.");
            throw new DUCException(DUCException.ERRORSETNODE);
        }
    }

    /**
     * Elimina del sistema el node 'idNode' del típus 'sNType'.
     *
     * @param idNode Identificador del node que es vol eliminar.
     * @param sNType Típus del node 'idNode'.
     */
    public void delNode(int idNode, String sNType) throws DUCException {
        int res = 0;
        try {
            if (sNType.compareTo("level") == 0) {
                if (snet.getRelations(idNode, sNType, "RAL", RelationType.ALL).size() > 0) {
                    throw new DUCException(DUCException.ERRORDELETENODE);
                }
//				if (snet.getRelations(idNode,sNType,"ROL",RelationType.ALL).size() > 0){
//					throw new DUCException(DUCException.ERRORDELETENODE);
//				}
                if (snet.getRelations(idNode, sNType, "RCL", RelationType.ALL).size() > 0) {
                    throw new DUCException(DUCException.ERRORDELETENODE);
                }
            }
            if (sNType.compareTo("area") == 0) {
//				if (snet.getRelations(idNode,sNType,"ROA",RelationType.ALL).size() > 0){
//					throw new DUCException(DUCException.ERRORDELETENODE);
//				}
                if (snet.getRelations(idNode, sNType, "RCA", RelationType.ALL).size() > 0) {
                    throw new DUCException(DUCException.ERRORDELETENODE);
                }
            }
            Node n = snet.delNode(idNode, sNType);
            if (n.getNodeType().compareTo("level") == 0) {
                res = ((Integer) n.getProperties().get("relatedLevel")).intValue();
            }
            //return res;
        } catch (Exception e) {
            logger.warn("Error on delete node.");
            throw new DUCException(DUCException.ERRORDELETENODE);
        }
    }

    /**
     * Retorna els valors del llistat de possibilitats d'un camp donat del
     * formulari.
     *
     * @param id Identificador del camp del formulari.
     * @return {LabelValueBean} amb el 'value' i el 'label' de cada una de les
     * opcions.
     */
    public ArrayList getRadioCollection(String id) {
        LabelValueBean lvb;
        ArrayList al = new ArrayList();
        if (id.compareTo("content") == 0) {
            lvb = new LabelValueBean("Procedimentals", "cp");
            al.add(lvb);
            lvb = new LabelValueBean("Conceptuals", "cc");
            al.add(lvb);
            lvb = new LabelValueBean("Actitudinals", "ca");
            al.add(lvb);
        }
        if (id.compareTo("objective") == 0) {
            lvb = new LabelValueBean("Principal", "op");
            al.add(lvb);
            lvb = new LabelValueBean("Terminals", "ot");
            al.add(lvb);
        }
        if (id.compareTo("level") == 0) {
            lvb = new LabelValueBean("Etapa", "etapa");
            al.add(lvb);
            lvb = new LabelValueBean("Cicle", "cicle");
            al.add(lvb);
            lvb = new LabelValueBean("Curs", "curs");
            al.add(lvb);
        }

        if (id.compareTo("nodeType") == 0) {
            lvb = new LabelValueBean("Nivell", "level");
            al.add(lvb);
            lvb = new LabelValueBean("Àrea", "area");
            al.add(lvb);
            lvb = new LabelValueBean("Contingut", "content");
            al.add(lvb);
            lvb = new LabelValueBean("Objectiu", "objective");
            al.add(lvb);
        }
        if (al.isEmpty()) {
            lvb = new LabelValueBean(id, "");
            al.add(lvb);
        }
        return al;
    }

    public ArrayList getRelations(int idNode, String sNType, String sRType, int dest) {
        // TODO Auto-generated method stub
        try {
            return (ArrayList) snet.getRelations(idNode, sNType, sRType, dest);
        } catch (SemanticException e) {
            return null;
        }

    }

}
