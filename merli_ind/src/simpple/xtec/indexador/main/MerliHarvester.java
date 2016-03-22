package simpple.xtec.indexador.main;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import simpple.xtec.indexador.util.Configuracio;
import simpple.xtec.indexador.util.DucBuilder;
import simpple.xtec.indexador.util.Recomanacions;
import simpple.xtec.indexador.util.SoapManager;
import simpple.xtec.indexador.util.Utils;

/**
 * Retrieves the data from Merli system
 *
 * @author descuer
 *
 */
public class MerliHarvester implements Job {

    //	Logger 	
    static Logger logger = Logger.getLogger(simpple.xtec.indexador.main.MerliHarvester.class);

    static String urlServletHarvesting = "";
    String urlServletCercador = "";

    TestCelebrate myTestCelebrate = null;
    static HttpClient clientWS = null;
    static String resumptionTokenString = null;

    /**
     * *
     * Constructor
     */
    public MerliHarvester() {
        try {
            logger.debug("MerliHarvester -> in");
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            //NADIM

            if ("80".equals(Configuracio.portWSmerli)) {
                urlServletHarvesting = "http://" + Configuracio.servidorWSmerli + "/" + Configuracio.nameHarvestingWS;
            } else {
                urlServletHarvesting = "http://" + Configuracio.servidorWSmerli + ":" + Configuracio.portWSmerli + "/" + Configuracio.nameHarvestingWS;
            }

            //urlServletHarvesting = "http://aplitic.xtec.cat/e13_merli_harvesting/MerliHarvestingServlet";
            //urlServletHarvesting ="http://10.49.24.34:7005/merli_harvesting/MerliHarvestingServlet";
            logger.debug("urlServletHarvesting -> " + urlServletHarvesting);
            if ("80".equals(Configuracio.portWSmerli)) {
                urlServletCercador = "http://" + Configuracio.servidorWeb + "/" + Configuracio.contextWebAplicacio + "/ServletCerca";
            } else {
                urlServletCercador = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/ServletCerca";
            }

            logger.debug("urlServletCercador -> " + urlServletCercador);
            logger.debug("MerliHarvester -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void clearFoundLogIndexacio() {
        Connection myConnection = null;
        Statement stmt = null;
        String sql = "";
        int numComentaris = 0;
        try {
            logger.debug("clearFoundLogIndexacio -> in");
//           myConnection = DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle, Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            sql = "update log_indexacio set found=0, isNew=0";
            logger.debug(sql);
            stmt = myConnection.createStatement();
            stmt.executeUpdate(sql);

            logger.debug("clearFoundLogIndexacio -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (myConnection != null) {
                    Utils.commit(myConnection);
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    public void clearFoundRecursos() {
        Connection myConnection = null;
        Statement stmt = null;
        String sql = "";
        int numComentaris = 0;
        try {
            logger.debug("clearFoundRecursos -> in");
//           myConnection = DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle, Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            sql = "update recursos set found=0";
            logger.debug(sql);
            stmt = myConnection.createStatement();
            stmt.executeUpdate(sql);

            logger.debug("clearFoundRecursos -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (myConnection != null) {
                    Utils.commit(myConnection);
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    public void syncFound() {
        Connection myConnection = null;
        Statement stmt = null;
        String sql = "";
        int numComentaris = 0;
        try {
            logger.debug("syncFound -> in");
//           myConnection = DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle, Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            sql = "update recursos r set found = (select found from log_indexacio l where r.id=l.recurs_id)";
            logger.debug(sql);
            stmt = myConnection.createStatement();
            stmt.executeUpdate(sql);

            logger.debug("syncFound -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                if (myConnection != null) {
                    Utils.commit(myConnection);
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    public String getContentBySOAP(String idContingut) {
        String bodyText = "";
        try {
            SoapManager soapManager = new SoapManager();
            bodyText = soapManager.doCall(idContingut);
        } catch (Exception e) {
            logger.error(e);
        }
        return bodyText;
    }

    /**
     * Do the call to retrieve the lom content
     */
    public String getContentBySOAP2(String idContingut) {
        String xmlRetorn = "";
        try {
            logger.debug("getContentBySOAP -> in");
            String serverURL = "http://" + Configuracio.servidorWSmerli + ":" + Configuracio.portWSmerli + "/";
            logger.debug("serverURL -> " + serverURL);
            String servicePath = Configuracio.nameLomWS;
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

            SOAPElement message = body.addBodyElement(envelope.createName("getResource"));
            SOAPElement idResource = message.addChildElement(envelope.createName("idResource"));
            SOAPElement identifier = idResource.addChildElement(envelope.createName("identifier"));
            identifier.addTextNode(idContingut);
            SOAPElement type = idResource.addChildElement(envelope.createName("type"));
            type.addTextNode("oai_lom");

            logger.debug(msg.getSOAPPart().getEnvelope().getBody());
            // send the message
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

    /**
     * Return all identifiers found doing the harvesting against Merli.
     *
     * @param myXml
     * @return
     */
    public static ArrayList parseResultsHarvesting(String myXml) {
        SAXBuilder builder = null;
        ArrayList allIds = null;
        try {
            logger.info("parseResultsHarvesting -> in");
            allIds = new ArrayList();
            logger.info("allIds: " + allIds);
            builder = new SAXBuilder();
            logger.info("myXml:" + myXml);
            Document doc = builder.build(new StringReader(myXml));
            Element root = doc.getRootElement();
            logger.info("doc:" + doc);
            Namespace myNamespace = Namespace.getNamespace("http://www.openarchives.org/OAI/2.0/");
            logger.debug("Root element: " + root);

            Element listIdentifiers = root.getChild("ListIdentifiers", myNamespace);
            logger.debug("listIdentifiers: " + listIdentifiers);
            List allHeaders = listIdentifiers.getChildren("header", myNamespace);
            logger.debug("size of headers:" + allHeaders.size());
            Iterator myIterator = allHeaders.iterator();

            while (myIterator.hasNext()) {
                Element oneHeader = (Element) myIterator.next();
                Element idElement = (Element) oneHeader.getChild("identifier", myNamespace);
                String id = idElement.getText();
                // ID RECURS
                id = id.replaceAll("oai:integracio.merli.xtec.cat:merli/", "");
                allIds.add(id);
            }

            Element resumptionToken = listIdentifiers.getChild("resumptionToken", myNamespace);
            if (resumptionToken != null) {
                resumptionTokenString = resumptionToken.getText();

            } else {
                resumptionTokenString = null;
            }

            Element responseDateElement = root.getChild("responseDate", myNamespace);
            logger.debug("responseDate element: " + responseDateElement);

            Element requestElement = root.getChild("request", myNamespace);
            logger.debug("request element: " + requestElement);

            logger.info("parseResultsHarvesting out");
        } catch (JDOMException e) { // indicates a well-formedness or other error
            logger.error(e.getMessage());
        } catch (IOException e) { // indicates a well-formedness or other error
            logger.error(e.getMessage());
        }
        return allIds;
    }

    /**
     * Retrieves one record from Merli, using the GetRecord method
     *
     * @param client
     * @param identifier
     */
    public void parseRecordFromMerli(TestCelebrate myTestCelebrate, String identifier) {
        try {
            logger.info("parseRecordFromMerli -> in");
            String contingutLom = getContentBySOAP(identifier);
            logger.info("Parsing..." + contingutLom);
            myTestCelebrate.parse(contingutLom, identifier);
            logger.info("parseRecordFromMerli -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static String getXmlHarvesting(String resumptionToken) {
        PostMethod post = null;
        String xmlResult = null;
        try {
            logger.info("URL: " + urlServletHarvesting);
            post = new PostMethod(urlServletHarvesting);
            post.addParameter("verb", "ListIdentifiers");
            post.addParameter("metadataPrefix", "oai_lom");
            if (!resumptionToken.equals("")) {
                post.addParameter("resumptionToken", resumptionToken);
            }
            logger.info("Executing method..." + urlServletHarvesting);
            logger.info("*Post: " + post.getParameters());
            int resultCode = clientWS.executeMethod(post);
            logger.info("Result code: " + resultCode);

            xmlResult = post.getResponseBodyAsString();
            logger.debug("XML: " + xmlResult);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                post.releaseConnection();
            } catch (Exception e) {
            }
        }
        return xmlResult;
    }

    /**
     * Main method, do the harvesting against Merli servlet
     *
     * @param client
     */
    public boolean doHarvesting(HttpClient clientWS, HttpClient client) {

        boolean processOk = false;
        ArrayList allIds = null;

        try {
            this.clientWS = clientWS;
            resumptionTokenString = null;
            logger.debug("doHarvesting -> in");
            myTestCelebrate = new TestCelebrate(client);

            allIds = parseResultsHarvesting(getXmlHarvesting(""));
            if (resumptionTokenString != null) {
                //NADIM
                while (!resumptionTokenString.equals("")) {
                //while (!resumptionTokenString.equals("") && allIds.size() <= 100) {
                    logger.debug("Resumption! -> " + resumptionTokenString);
                    allIds.addAll(parseResultsHarvesting(getXmlHarvesting(resumptionTokenString)));
                }
            }

// * Menys de 5 recursos no es crea el nou index        	
//        	allIds = new ArrayList();
//        	allIds.add("171723");
//        	allIds.add("171719");
//        	allIds.add("193648");
//        	allIds.add("193759");
//        	allIds.add("8936");
//        	allIds.add("4081");
//        	allIds.add("4281");
//        	allIds.add("193748");
//        	allIds.add("193752");
            int i = 0;
            logger.debug("SIZE -> " + allIds.size());

            while (i < allIds.size()) {
                parseRecordFromMerli(myTestCelebrate, (String) allIds.get(i));
                logger.debug("Inici sleep - " + i);
                Thread.sleep(1500);
                logger.debug("Final sleep - " + i);
                i++;
            }
            logger.info("Finalizing...");
            myTestCelebrate.finalize();
            logger.info("Checking index...");
            processOk = myTestCelebrate.checkIndex();

            logger.info("Process OK -> " + processOk);
            logger.debug("doHarvesting -> out");
        } catch (Exception e) {
            logger.error(e);

        }
        return processOk;
    }

    public void callSearcher(HttpClient client) {
        PostMethod post = null;
        String indexNew = "";
        try {
            logger.debug("callSearcher -> in");

            String indexInUse = Utils.getIndexActualFromDB();
            logger.debug("indexInUse -> " + indexInUse);
            logger.debug("Configuracio.indexDir -> " + Configuracio.indexDir);
            logger.debug("Configuracio.indexDir2 -> " + Configuracio.indexDir2);
            if (Configuracio.indexDir.equals(indexInUse)) {
                indexNew = Configuracio.indexDir2;
            }
            if (Configuracio.indexDir2.equals(indexInUse)) {
                indexNew = Configuracio.indexDir;
            }
            logger.debug("indexNew -> " + indexNew);
            Utils.setIndexActualDB(indexNew);
            //  client = new HttpClient(new MultiThreadedHttpConnectionManager());
            logger.debug("Calling servlet..." + urlServletCercador);
            post = new PostMethod(urlServletCercador);
            post.addParameter("tipus", "updateIndex");
            int resultCode = client.executeMethod(post);
            logger.debug("Result code..." + resultCode);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                post.releaseConnection();
            } catch (Exception e) {
            }
        }
    }

    public void cleanIndexDirectory() {
        try {
            myTestCelebrate.cleanIndexDirectory();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        HttpClient clientWS = null;
        HttpClient client = null;
        MerliHarvester merliHarvester = new MerliHarvester();
        Recomanacions recomanacions = null;
        boolean processOk = false;
        try {
            clientWS = Utils.createHttpClient(30000);
            client = Utils.createHttpClient(5000);
            //    merliHarvester = new MerliHarvester();
            merliHarvester.clearFoundLogIndexacio();
//THIS WILL TRY TO SEARCH IN MERLI_ALOMA or MERLI_ORG (both are same)            

            /*recomanacions = new Recomanacions();
             recomanacions.loadRecomanacions();
             recomanacions.finalize();*/
            if (Configuracio.refreshDUC.equals("si")) {
                DucBuilder ducBuilder = new DucBuilder();
                ducBuilder.updateDUC();
                ducBuilder.finalize();
            }
            processOk = merliHarvester.doHarvesting(clientWS, client);
            if (processOk) {
                merliHarvester.callSearcher(clientWS);
                merliHarvester.clearFoundRecursos();
                merliHarvester.syncFound();
            } else {
                merliHarvester.cleanIndexDirectory();
            }

        } catch (Exception e) {
            logger.error(e);
        } finally {
        }

    }

}
