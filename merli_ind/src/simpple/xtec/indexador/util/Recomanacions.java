package simpple.xtec.indexador.util;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
/*import org.w3c.dom.Document;
 import org.w3c.dom.NamedNodeMap;
 import org.w3c.dom.Node;
 import org.w3c.dom.NodeList;*/
import org.xml.sax.InputSource;

public class Recomanacions {

    // logger
    static final Logger logger = Logger.getLogger("simpple.xtec.indexador.util.Recomanacions");
    Connection myConnection = null;
    Hashtable sheetRecursos = null;
    ArrayList recursos = null;

    public Recomanacions() {
        try {
            //  myConnection = DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle, Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            sheetRecursos = new Hashtable();
            recursos = new ArrayList();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void parseRecomanacions(String xmlBody) {
        DOMParser parser = null;
        Document doc = null;
        int numReferences = 0;
        int i = 0;
        SAXBuilder builder = null;
        try {
            builder = new SAXBuilder();

            doc = builder.build(new StringReader(xmlBody));
            Element root = doc.getRootElement();

            Namespace myNamespace = Namespace.getNamespace("tns", "http://www.xtec.cat");
            logger.debug("Root element: " + root);
            Element infoOrganitzador = root.getChild("infoOrganitzador", myNamespace);
            Element getMerliReferences = infoOrganitzador.getChild("getMerliReferences", myNamespace);
            Element merliReferences = getMerliReferences.getChild("merliReferences", myNamespace);
            List allReferences = merliReferences.getChildren("reference", myNamespace);

            while (i < allReferences.size()) {
                Element myReference = (Element) allReferences.get(i);

                String merliId = (String) myReference.getAttributeValue("merliId");
                String userId = (String) myReference.getAttributeValue("userId");
                String sheetId = (String) myReference.getAttributeValue("sheetId");
                logger.debug(merliId);
                logger.debug(userId);
                logger.debug(sheetId);

                saveRecomanacioHashtable(merliId, userId, sheetId);
                i++;

            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void loadRecomanacions() {
        String xmlRetorn = "";
        try {
            xmlRetorn = callService();
            logger.debug(xmlRetorn);
            // if (xmlRetorn -> ok)

            parseRecomanacions(xmlRetorn);
            if (recursos.size() > 0) {
                cleanTables();
                hashtableToDb();
                calculateRelacions();
            }
        } catch (Exception e) {
            logger.error(e);
        }

    }

    public void addRelacionatsInfo(String recursId, String relacionats) {
        StringTokenizer myTokenizer = null;
        Hashtable allTerms = null;
        try {
            allTerms = new Hashtable();
            relacionats = relacionats.replaceAll(recursId, "");
            myTokenizer = new StringTokenizer(relacionats);

            logger.debug("relacionats: " + relacionats);
            while (myTokenizer.hasMoreTokens()) {
                String nextItem = (String) myTokenizer.nextToken();
                if (allTerms.containsKey(nextItem)) {
                    Integer repeticions = (Integer) allTerms.get(nextItem);
                    allTerms.remove(nextItem);
                    int newRepeticions = repeticions.intValue() + 1;
                    allTerms.put(nextItem, new Integer(newRepeticions));
                } else {
                    allTerms.put(nextItem, new Integer(1));
                }
            }
            logger.debug("A");
            RecomanacioObject[] allRecomanacions = new RecomanacioObject[allTerms.size()];
            Enumeration keys = allTerms.keys();
            int i = 0;
            while (keys.hasMoreElements()) {
                logger.debug("A2");
                String term = (String) keys.nextElement();
                Integer repeticions = (Integer) allTerms.get(term);
                RecomanacioObject recomanacioObject = new RecomanacioObject(term, repeticions.intValue());
                allRecomanacions[i] = recomanacioObject;
                i++;
            }
            logger.debug("A3");
            Arrays.sort(allRecomanacions, new RecomanacionsComparator());
            logger.debug("A4");
            i = 0;
            String stringToSave = "";
            while ((i < 5) && (i < allRecomanacions.length)) {
                logger.debug("Recomanacio: " + allRecomanacions[i].term + "/" + allRecomanacions[i].repeticions);
                stringToSave += allRecomanacions[i].term + " ";
                i++;
            }
            saveRelacionatsToDb(recursId, stringToSave);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void calculateRelacions() {
        int i = 0;
        String sql = "";
        Statement stmt = null;
        ResultSet rs = null;
        String recursId = "";
        String relacionats = "";
        try {

            while (i < recursos.size()) {
                stmt = myConnection.createStatement();
                recursId = (String) recursos.get(i);
                relacionats = "";
                sql = "SELECT recursosRelacionats from recomanacions_temp where recursosRelacionats LIKE '%" + recursId + "%'";
                logger.debug(sql);
                rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    relacionats += rs.getString("recursosRelacionats");
                }
                try {
                    rs.close();
                    stmt.close();
                } catch (Exception e) {
                    logger.error(e);
                }
                logger.debug("Recurs id: " + recursId);
                logger.debug("relacionats: " + relacionats);
                addRelacionatsInfo(recursId, relacionats);
                i++;
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void saveRelacionatsToDb(String recursId, String stringToSave) {
        String sql = "";
        PreparedStatement prepStmt = null;
        try {
            prepStmt = myConnection.prepareStatement("INSERT INTO recomanacions (recursId, recursosRecomanats) VALUES (?, ?)");
            prepStmt.setString(1, recursId);
            prepStmt.setString(2, stringToSave);
            prepStmt.executeUpdate();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                prepStmt.close();
            } catch (Exception e) {
            }
        }
    }

    public void hashtableToDb() {
        String sql = "";
        PreparedStatement prepStmt = null;
        try {
            prepStmt = myConnection.prepareStatement("INSERT INTO recomanacions_temp (sheetId, recursosRelacionats) VALUES (?, ?)");
            Enumeration allKeys = sheetRecursos.keys();
            while (allKeys.hasMoreElements()) {
                String key = (String) allKeys.nextElement();
                String value = (String) sheetRecursos.get(key);
                prepStmt.setString(1, key);
                prepStmt.setString(2, value);
                prepStmt.executeUpdate();
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                prepStmt.close();
            } catch (Exception e) {
            }
        }
    }

    public void cleanTables() {
        String deleteSql1 = "";
        String deleteSql2 = "";
        Statement stmt = null;
        try {
            deleteSql1 = "DELETE FROM recomanacions_temp";
            deleteSql2 = "DELETE FROM recomanacions";
            stmt = myConnection.createStatement();
            stmt.executeUpdate(deleteSql1);
            stmt.executeUpdate(deleteSql2);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
            }
        }
    }

    public String callService() {
        String xmlRetorn = "";
        try {
            logger.debug("getContentBySOAP -> in");
            String serverURL = Configuracio.servidorOrganitzador;
            logger.debug("serverURL -> " + serverURL);
            String servicePath = Configuracio.nameRecomanacionsWS;
            logger.debug("servicePath -> " + servicePath);

            SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
            logger.debug("Creating connection...");
            SOAPConnection soapConnection = factory.createConnection();

            logger.debug("Creating message...");
            MessageFactory msgFactory = MessageFactory.newInstance();
            SOAPMessage msg = msgFactory.createMessage();

            // add headers - these are targets for the message
            SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();

            SOAPBody body = envelope.getBody();
            SOAPElement message = body.addBodyElement(envelope.createName("getMerliReferences"));

            String myUrl = serverURL + servicePath;
            logger.debug("Sending message to " + myUrl + " ...");
            logger.debug("See the result at the server console.");
            SOAPMessage response = soapConnection.call(msg, myUrl);

            SOAPEnvelope envelopeResponse = response.getSOAPPart().getEnvelope();
            SOAPBody bodyResponse = envelopeResponse.getBody();

            xmlRetorn = bodyResponse.toString();

            // Bug d'apostrof
            xmlRetorn = xmlRetorn.replaceAll("&#x92;", "'");
            logger.debug("getContentBySOAP -> out");
        } catch (Exception e) {
            logger.error(e);
        }
        return xmlRetorn;
    }

    public void saveRecomanacioHashtable(String merliId, String userId, String sheetId) {
        try {
            if (!recursos.contains(merliId)) {
                recursos.add(merliId);
            }
            if (!sheetRecursos.containsKey(sheetId)) {
                sheetRecursos.put(sheetId, merliId);
            } else {
                String resources = (String) sheetRecursos.get(sheetId);
                resources += " " + merliId;
                sheetRecursos.remove(sheetId);
                sheetRecursos.put(sheetId, resources);
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void finalize() {
        try {
            Utils.commit(myConnection);
            myConnection.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
