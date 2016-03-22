package simpple.xtec.indexador.util;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class DucBuilder {

    Connection myConnection = null;
    PreparedStatement insertStatement = null;
    PreparedStatement insertLangStatement = null;
    PreparedStatement deleteStatement = null;
    PreparedStatement dropStatement = null;
    PreparedStatement copyStatement = null;
    PreparedStatement checkStatement = null;
    PreparedStatement commitStatement = null;

    String insertSql = "";
    String insertLangSql = "";
    String deleteSql = "";
    String dropSql = "";
    String copySql = "";
    String checkSql = "";
    String commitSql = "";

    //	Logger 	
    static Logger logger = Logger.getLogger(simpple.xtec.indexador.util.DucBuilder.class);

    public DucBuilder() {
        try {
//		    myConnection = DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle, Configuracio.userBDOracle, Configuracio.passwordBDOracle);			  
            myConnection = Utils.getConnectionFromPool();
            insertSql = "INSERT INTO duc_info_tmp (id, term, relationType, parent_id) VALUES (?, ?, ?, ?)";
            insertStatement = myConnection.prepareStatement(insertSql);
            insertLangSql = "INSERT INTO duc_info_tmp (id, term, relationType, parent_id, term_es, term_en, term_oc) VALUES (?, ?, ?, ?, ?, ?, ?)";
            insertLangStatement = myConnection.prepareStatement(insertSql);
            deleteSql = "DELETE FROM duc_info_tmp";
            deleteStatement = myConnection.prepareStatement(deleteSql);
            dropSql = "DROP TABLE duc_info";
            dropStatement = myConnection.prepareStatement(dropSql);
            copySql = "CREATE table duc_info AS select * from duc_info_tmp";
            copyStatement = myConnection.prepareStatement(copySql);
            checkSql = "SELECT COUNT(*) from duc_info_tmp";
            checkStatement = myConnection.prepareStatement(checkSql);
            commitSql = "COMMIT";
            commitStatement = myConnection.prepareStatement(commitSql);
        } catch (Exception e) {
            logger.error(e);
        }

    }

    public void updateDUC() {
        String bodyXML = "";
        int numFiles = 0;
        try {
            logger.info("Deleting...");
            deleteStatement.executeUpdate();
            logger.info("Commit...");
            commitStatement.executeUpdate();
            logger.info("Doing call...");
            //SoapManager mySoapManager = new SoapManager();
            // bodyXML = mySoapManager.doCallDUC();

            String idNivells = doSOAPCallDUCNivells();
            ArrayList lev = parseNivellsDUC(idNivells);
            if (lev != null) {
                for (int i = 0; i < lev.size(); i++) {
                    try {
                        bodyXML = doSOAPCallDUC(((Integer) lev.get(i)).intValue());
                        logger.info("Parsing...");
                        parseDUC(bodyXML, ((Integer) lev.get(i)).intValue());
                    } catch (Exception e) {
                        logger.error("Parsing level:" + lev.get(i) + "..." + e.getMessage());
                    }
                }
            }

            logger.info("Getting num files...");
            numFiles = numFilesDUC();

            logger.info("Num files..." + numFiles);
            if (numFiles > 500) {
                logger.info("Updating db");

                updateDB();
                logger.info("Db updated");
            } else {
                logger.error(bodyXML);
            }

        } catch (Exception e) {
            logger.error(e);
            logger.error("Num files -> " + numFiles);
        } finally {
            try {
                deleteStatement.close();
                dropStatement.close();
                copyStatement.close();
                commitStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateDB() {
        try {
            logger.debug("Dropping...");
            dropStatement.executeUpdate();
            logger.debug("Commit...");

            commitStatement.executeUpdate();
            logger.debug("Copying...");

            copyStatement.executeUpdate();
            logger.debug("Commit...");

            commitStatement.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public int numFilesDUC() {
        ResultSet rs = null;
        int numResultats = -1;
        try {
            rs = checkStatement.executeQuery();
            rs.next();
            numResultats = rs.getInt(1);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return numResultats;
    }

    public void parseElementDUC(Element elementDUC, String separator, int parent_id, Element relationType) {
        int idInt = 0;
        Hashtable hlang = new Hashtable();
        String langTerm;
        if ("RCA".equals(relationType.getText())) {
            idInt = idInt + 1 - 1;
        }
        try {
            separator += "  ";
            Element term = elementDUC.getChild("term");
            logger.debug(separator + term.getText());
            Element idElement = elementDUC.getChild("idElement");
            Element identifier = idElement.getChild("identifier");
            Element typeElement = idElement.getChild("typeElement");
            List allTerms = elementDUC.getChildren("termLang");
            Iterator myIteratorLang = allTerms.iterator();
            while (myIteratorLang.hasNext()) {
                langTerm = "ca";
                Element elementRelation = (Element) myIteratorLang.next();
                Element termString = elementRelation.getChild("string");
                Attribute attrDUCchild = termString.getAttribute("lang");
                if (attrDUCchild != null) {
                    langTerm = attrDUCchild.getValue();
                }
                hlang.put(langTerm, termString.getText());
            }
            idInt = (new Integer(identifier.getText())).intValue();
            doInsert(idInt, parent_id, term.getText(), relationType.getText(), hlang);
            List allRelations = elementDUC.getChildren("DUCRelation");
            Iterator myIterator2 = allRelations.iterator();
            while (myIterator2.hasNext()) {
                Element elementRelation = (Element) myIterator2.next();
                Element ducRelationType = elementRelation.getChild("DUCRelationType");
                Element elementDUCchild = elementRelation.getChild("elementDUC");
                parseElementDUC(elementDUCchild, separator, idInt, ducRelationType);
            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

    public ArrayList parseNivellsDUC(String body) {
        SAXBuilder builder = null;

        ArrayList lev = new ArrayList();

        int idInt = -1;
        try {
            builder = new SAXBuilder();

            Document doc = builder.build(new StringReader(body));
            Element root = doc.getRootElement();

            Namespace myNamespace = Namespace.getNamespace("http://schemas.xmlsoap.org/soap/envelope/");

            Element mainElement = root.getChild("getLevelsResponse");
//	         Element mainElement = root.getChild("getDUCResponse");
            logger.debug("Main element: " + mainElement);
            Element mainElementList = mainElement.getChild("listDUC");
            logger.debug("Main element list: " + mainElementList);

            // List allChildren = mainElementList.getChildren("idElement", myNamespace);
            List allChildren = mainElementList.getChildren("elementDUC");
            Iterator myIterator = allChildren.iterator();
            String langTerm = "";
            Hashtable hlang;
            while (myIterator.hasNext()) {
                Element elementDUC = (Element) myIterator.next();
                Element term = elementDUC.getChild("term");
                List allTerms = elementDUC.getChildren("termLang");
                Iterator myIteratorLang = allTerms.iterator();
                hlang = new Hashtable();
                while (myIteratorLang.hasNext()) {
                    langTerm = "ca";
                    Element elementRelation = (Element) myIteratorLang.next();
                    Element termString = elementRelation.getChild("string");
                    Attribute attrDUCchild = termString.getAttribute("lang");
                    if (attrDUCchild != null) {
                        langTerm = attrDUCchild.getValue();
                    }
                    hlang.put(langTerm, termString.getText());
                }
                logger.debug("*****************************");
                logger.debug("Level: " + term.getText());
                Element idElement = elementDUC.getChild("idElement");
                Element identifier = idElement.getChild("identifier");
                Element typeElement = idElement.getChild("typeElement");
                idInt = (new Integer(identifier.getText())).intValue();
                logger.debug("do Insert 1");
                doInsert(idInt, -1, term.getText(), typeElement.getText(), hlang);
                logger.debug("do Insert 2");

                lev.add((new Integer(identifier.getText())));

                List allRelations = elementDUC.getChildren("DUCRelation");
                Iterator myIterator2 = allRelations.iterator();
                while (myIterator2.hasNext()) {
                    Element elementRelation = (Element) myIterator2.next();
                    Element relationType = elementRelation.getChild("DUCRelationType");
                    Element elementDUCchild = elementRelation.getChild("elementDUC");
                    parseElementDUC(elementDUCchild, "", (new Integer(identifier.getText())).intValue(), relationType);
                }

            }
        } catch (Exception e) {
            logger.error(e);
        }
        return lev;
    }

    public void parseDUC(String body, int parent_id) {
        SAXBuilder builder = null;
        int idInt = -1;
        try {
            builder = new SAXBuilder();

            Document doc = builder.build(new StringReader(body));
            Element root = doc.getRootElement();

            Namespace myNamespace = Namespace.getNamespace("http://schemas.xmlsoap.org/soap/envelope/");

//	         Element mainElement = root.getChild("getLevelsResponse", myNamespace);
            Element mainElement = root.getChild("getLevelElementsResponse");
            logger.debug("Main element: " + mainElement);
            Element mainElementList = mainElement.getChild("listDUC");
            logger.debug("Main element list: " + mainElementList);

            // List allChildren = mainElementList.getChildren("idElement", myNamespace);
            List allChildren = mainElementList.getChildren("elementDUC");
            Iterator myIterator = allChildren.iterator();
            String langTerm = "";
            Hashtable hlang;
            while (myIterator.hasNext()) {
                Element elementDUC = (Element) myIterator.next();
                Element term = elementDUC.getChild("term");
                List allTerms = elementDUC.getChildren("termLang");
                Iterator myIteratorLang = allTerms.iterator();
                hlang = new Hashtable();
                while (myIteratorLang.hasNext()) {
                    langTerm = "ca";
                    Element elementRelation = (Element) myIteratorLang.next();
                    Element termString = elementRelation.getChild("string");
                    Attribute attrDUCchild = termString.getAttribute("lang");
                    if (attrDUCchild != null) {
                        langTerm = attrDUCchild.getValue();
                    }
                    hlang.put(langTerm, termString.getText());
                }
                logger.debug("*****************************");
                logger.debug("Level: " + term.getText());
                List allRelations = elementDUC.getChildren("DUCRelation");
                Element idElement = elementDUC.getChild("idElement");
                Element identifier = idElement.getChild("identifier");
                Element typeElement = idElement.getChild("typeElement");
                idInt = (new Integer(identifier.getText())).intValue();
                logger.debug("do Insert 1");
//	             doInsert (idInt, parent_id, term.getText(), "RLL" ,hlang);
                logger.debug("do Insert 2");

                Iterator myIterator2 = allRelations.iterator();
                while (myIterator2.hasNext()) {
                    Element elementRelation = (Element) myIterator2.next();
                    Element relationType = elementRelation.getChild("DUCRelationType");
                    Element elementDUCchild = elementRelation.getChild("elementDUC");
                    parseElementDUC(elementDUCchild, "", idInt, relationType);
                }

            }
        } catch (Exception e) {
            logger.error(e);

        }

    }

    public void doInsert(int id, int parent_id, String term, String relationType, Hashtable hlang) {
        String sql = "";
        try {
            insertLangStatement = myConnection.prepareStatement(insertLangSql);

            insertLangStatement.setInt(1, id);
            insertLangStatement.setString(2, term);
            insertLangStatement.setString(3, relationType);
            insertLangStatement.setInt(4, parent_id);
            insertLangStatement.setString(5, (String) hlang.get("es"));
            insertLangStatement.setString(6, (String) hlang.get("en"));
            insertLangStatement.setString(7, (String) hlang.get("oc"));

            logger.debug(insertLangStatement);
            insertLangStatement.executeUpdate();

        } catch (Exception e) {

            logger.error(e);
            logger.debug("ID: " + id);
            logger.debug("PARENT_ID: " + parent_id);

        } finally {
            try {
                insertLangStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void doInsert(int id, int parent_id, String term, String relationType) {
        String sql = "";
        try {
            insertStatement = myConnection.prepareStatement(insertSql);

            insertStatement.setInt(1, id);
            insertStatement.setString(2, term);
            insertStatement.setString(3, relationType);
            insertStatement.setInt(4, parent_id);

            logger.debug(insertStatement);

            insertStatement.executeUpdate();
        } catch (Exception e) {

            logger.error(e);
            logger.debug("ID: " + id);
            logger.debug("PARENT_ID: " + parent_id);

        } finally {
            try {
                insertLangStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String doSOAPCallDUC(int level) {
        long freeMemory = Runtime.getRuntime().freeMemory();
        String responseXML = "";
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            String serverURL = "http://" + Configuracio.servidorWSmerli + ":" + Configuracio.portWSmerli + "/";
            //String serverURL = "http://localhost:8888/";
            String servicePath = Configuracio.nameLomWS.replaceAll("/merli", "/duc");
            //String servicePath = "e13_merli_ws/duc";

            SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();

            SOAPConnection soapConnection = factory.createConnection();

            MessageFactory msgFactory = MessageFactory.newInstance();

            SOAPMessage msg = msgFactory.createMessage();

            msg.setProperty("Content-type", "text/xml");

            // add headers - these are targets for the message
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();

            SOAPHeader myHeader = envelope.getHeader();

            myHeader.setAttribute("Content-Type", "text/xml");

            // envelope.setMimeHeader("Content-Type", "text/xml");
            // SOAPHeader headers = envelope.getHeader();
            SOAPHeaderElement target;

            SOAPBody body = envelope.getBody();

//	        SOAPElement message =  body.addBodyElement(envelope.createName("GetDUC"));
            SOAPElement message = body.addBodyElement(envelope.createName("GetLevelElements"));
            SOAPElement idElem = message.addChildElement("idElement");
            idElem.addChildElement("identifier").addTextNode(String.valueOf(level));
            idElem.addChildElement("typeElement").addTextNode("level");

            logger.info(msg.getSOAPPart().getEnvelope().getBody());

            // send the message
            String myUrl = serverURL + servicePath;
            logger.info("Sending message to " + myUrl + " ...");
            logger.info("Free Memory1: " + freeMemory);
            logger.info("Total memory usage1 = " + (Runtime.getRuntime().freeMemory() - freeMemory));

            logger.info("See the result at the server console.");

            logger.info("Free Memory2: " + freeMemory);
            logger.info("Total memory usage 2= " + (Runtime.getRuntime().freeMemory() - freeMemory));

            SOAPMessage response = soapConnection.call(msg, myUrl);

            SOAPEnvelope envelopeResponse = response.getSOAPPart().getEnvelope();

            SOAPBody bodyResponse = envelopeResponse.getBody();
            logger.info("Free Memory3: " + freeMemory);
            logger.info("Free Memory after response: " + Runtime.getRuntime().freeMemory());
            logger.info("Total memory usage 3= " + (Runtime.getRuntime().freeMemory() - freeMemory));
            /* PRUEBA CON TOSTRING*/
            DOMSource source = new DOMSource(bodyResponse);
            logger.debug("SOURCE: " + source);
            StringWriter stringResult = new StringWriter();
            logger.debug("stringResult 1: " + stringResult);
            TransformerFactory.newInstance().newTransformer().transform(source, new StreamResult(stringResult));
            logger.debug("stringResult 2: " + stringResult);
            responseXML = stringResult.toString();
            logger.debug("responseXML: " + responseXML);
            //responseXML = bodyResponse.toString();
            logger.info("Free Memory4: " + freeMemory);
            logger.info("Total memory usage 4= " + (Runtime.getRuntime().freeMemory() - freeMemory));
        } catch (Exception e) {
            logger.error(e);
        }
        return responseXML;
    }

    public String doSOAPCallDUCNivells() {
        String responseXML = "";
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            String serverURL = "http://" + Configuracio.servidorWSmerli;
            if ("80".equals(Configuracio.portWSmerli)) {
                serverURL += "/";
            } else {
                serverURL += ":" + Configuracio.portWSmerli + "/";
            }

            //String serverURL = "http://" + Configuracio.servidorWSmerli + "/";
            logger.debug("serverURL: " + serverURL);

            String servicePath = Configuracio.nameLomWS.replaceAll("/merli", "/duc");
            logger.debug("servicePath: " + servicePath);

            SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
            logger.debug("factory: " + factory);

            SOAPConnection soapConnection = factory.createConnection();
            logger.debug("soapConnection: " + soapConnection);

            MessageFactory msgFactory = MessageFactory.newInstance();
            logger.debug("msgFactory: " + msgFactory);

            SOAPMessage msg = msgFactory.createMessage();

            msg.setProperty("Content-type", "text/xml");

            // add headers - these are targets for the message
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
            logger.debug("envelope: " + envelope);

            SOAPHeader myHeader = envelope.getHeader();

            myHeader.setAttribute("Content-Type", "text/xml");
            logger.debug("myHeader: " + myHeader);

            // envelope.setMimeHeader("Content-Type", "text/xml");
            // SOAPHeader headers = envelope.getHeader();
            SOAPHeaderElement target;

            SOAPBody body = envelope.getBody();

            SOAPElement message = body.addBodyElement(envelope.createName("GetLevels"));

            logger.info(msg.getSOAPPart().getEnvelope().getBody());

            // send the message
            String myUrl = serverURL + servicePath;
            logger.debug("Sending message to " + myUrl + " ...");

            logger.info("See the result at the server console.");
            SOAPMessage response = soapConnection.call(msg, myUrl);
            logger.info("response: " + response);
            SOAPEnvelope envelopeResponse = response.getSOAPPart().getEnvelope();

            SOAPBody bodyResponse = envelopeResponse.getBody();

            responseXML = bodyResponse.toString();
	      //  logger.info(bodyResponse);

            //   parseNivells (bodyResponse.toString());
	/*  
             String lomRetrieved = (String)response.getSOAPPart().getEnvelope().getBody(); 

             */

            /*   PrintWriter myPrintWriter = new PrintWriter(new File("duc.xml"));
             myPrintWriter.println(bodyResponse);
             myPrintWriter.flush();
             myPrintWriter.close(); 
             */
        } catch (Exception e) {
            logger.error(e);
        }
        return responseXML;
    }

    public void finalize() {
        try {
            if (myConnection != null) {
                Utils.commit(myConnection);
                myConnection.close();
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
