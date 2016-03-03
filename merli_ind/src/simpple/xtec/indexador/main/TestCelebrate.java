package simpple.xtec.indexador.main;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import simpple.xtec.indexador.modules.UrlRetriever;
import simpple.xtec.indexador.objects.LuceneDocument;
import simpple.xtec.indexador.util.Configuracio;
import simpple.xtec.indexador.util.Utils;

public class TestCelebrate {

    static final Logger logger = Logger.getLogger(simpple.xtec.indexador.main.TestCelebrate.class);

    Indexador myIndexador = null;
    String allContent = "";
    boolean isAuthor = false;
    boolean isCreator = false;
    boolean isPublisher = false;
    boolean isEditor = false;
    boolean isDUC = false;
    boolean isAmbit = false;
    boolean isETB = false;
    HttpClient client = null;
    String nivellEducatiu = "";
    String areaCurricular = "";

	// Necessari per executar Job
    // public TestCelebrate () {}
    /**
     * *
     * Constructor
     */
    public TestCelebrate(HttpClient client) {
        try {
            logger.debug("TestCelebrate -> in");
            myIndexador = new Indexador();
            this.client = client;
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            logger.debug("Loading driver -> " + Configuracio.nomDriverBDOracle);
            //Class.forName(Configuracio.nomDriverBDOracle).newInstance();
            logger.debug("TestCelebrate -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /*
     * public String getParentFromAncestors (String ancestors) { int lastBlank =
     * 0; String parent = ""; try { lastBlank = ancestors.lastIndexOf(" "); if
     * (lastBlank == -1) { return ancestors; } else { parent =
     * ancestors.substring(lastBlank + 1, ancestors.length()); } } catch
     * (Exception e) { logger.error(e); } return parent; }
     */
    /**
     * Returns true if the node is a leaf
     */
    public boolean isLeaf(Node myNode) {
        try {
            // logger.debug("isLeaf -> in");
            if ((myNode.getChildNodes().getLength() == 1) && (myNode.getChildNodes().item(0).getNodeType() == 3)
                    && !myNode.getChildNodes().item(0).getNodeValue().trim().equals("")) {
                return true;
            }
            // logger.debug("isLeaf -> out");
        } catch (Exception e) {
            logger.error(e);
        }
        return false;

    }

    /**
     * Returns the value of the leaf node
     */
    public String getLeafValue(Node myNode) {
        String leafValue = "";
        try {
            // logger.debug("getLeafValue -> in");
            leafValue = myNode.getChildNodes().item(0).getNodeValue().trim();
            // logger.debug("getLeafValue -> out");
        } catch (Exception e) {
            logger.error(e);
        }
        return leafValue;
    }

    /**
     * Get resource's number of comments
     */
    public int getNumComentaris(String idRecurs) {
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int numComentaris = 0;
        try {
            logger.debug("getNumComentaris -> in");
			// myConnection =
            // DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle,
            // Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            sql = "select count(*) from comentaris where recurs_id='" + idRecurs + "' and suspens=0";
            logger.debug(sql);
            stmt = myConnection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                numComentaris = rs.getInt(1);
            }
            logger.debug("getNumComentaris -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
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
        return numComentaris;
    }

    /**
     * Get resource's average rating
     */
    public float getPuntuacio(String idRecurs) {
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        float puntuacio = (float) 0.0;
        try {
            logger.debug("getPuntuacio -> out");
			// myConnection =
            // DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle,
            // Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            sql = "select avg(puntuacio) from comentaris where recurs_id='" + idRecurs + "' and suspens=0";
            logger.debug(sql);
            stmt = myConnection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                puntuacio = rs.getFloat(1);
            }
            logger.debug("getPuntuacio -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
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
        return puntuacio;
    }

    /**
     * Get resource's comments
     */
    public String getComentaris(String idRecurs) {
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String comentari = "";
        try {
            logger.debug("getComentaris -> in");
			// myConnection =
            // DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle,
            // Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            sql = "select comentari from comentaris where recurs_id='" + idRecurs + "' and suspens=0";
            logger.debug(sql);
            stmt = myConnection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                comentari += rs.getString("comentari");
            }
            logger.debug("getComentaris -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

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
        return comentari;
    }

    /**
     * Get resource's hits
     */
    public int getNumVisites(String idRecurs) {
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int numVisites = 0;
        try {
            logger.debug("getNumVisites -> in");
			// myConnection =
            // DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle,
            // Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            sql = "select sum(numVisites) from visites where idRecurs='" + idRecurs + "'";
            logger.debug(sql);
            stmt = myConnection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                numVisites = rs.getInt(1);
            }
            logger.debug("getNumVisites -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
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
        return numVisites;
    }

    /**
     * Add a resource to the database
     */
    public void afegirRecurs(String idRecurs, String titol) {
        Connection myConnection = null;
        String insertStatement = "";
        String logStatement = "";
        PreparedStatement prepStmt = null;
        PreparedStatement prepStmtLog = null;
        try {
            logger.debug("afegirRecurs -> in");
			// myConnection =
            // DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle,
            // Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();

            insertStatement = "INSERT INTO recursos (id, titol) VALUES (?, ?)";
            logger.debug(insertStatement);
            prepStmt = myConnection.prepareStatement(insertStatement);
            prepStmt.setString(1, idRecurs);
            prepStmt.setString(2, titol);

            logger.debug(prepStmt.toString());
            prepStmt.executeUpdate();
            logger.debug("Update executed...");
            myConnection.commit();
            logStatement = "INSERT INTO log_indexacio (recurs_id, data_primera_indexacio, found, isNew) VALUES (?, ?, 1, 1)";
            logger.debug(logStatement);
            prepStmtLog = myConnection.prepareStatement(logStatement);
            prepStmtLog.setString(1, idRecurs);
            Calendar rightNow = Calendar.getInstance();
            String data_actual_string = Utils.calculaDataActual(rightNow);
            String hora_actual_string = Utils.calculaHoraActual(rightNow);
            logger.info("Dia: " + data_actual_string + "/ " + hora_actual_string);
            prepStmtLog.setString(2, data_actual_string + " " + hora_actual_string);
            prepStmtLog.executeUpdate();
            myConnection.commit();
            logger.debug("afegirRecurs -> out");
        } catch (Exception e) {
            // logger.error(e);
        } finally {
            try {
                if (prepStmt != null) {
                    prepStmt.close();
                }
                if (prepStmtLog != null) {
                    prepStmtLog.close();
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

    /**
     * Updates the resource's number of hits
     */
    public void updateVisites(String idRecurs, String titol) {
        Connection myConnection = null;
        String insertStatement = "";
        PreparedStatement prepStmt = null;
        try {
            logger.debug("updateVisites -> in");
			// myConnection =
            // DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle,
            // Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            insertStatement = "UPDATE visites SET titol=? WHERE idRecurs=?";
            logger.debug(insertStatement);
            prepStmt = myConnection.prepareStatement(insertStatement);
            prepStmt.setString(1, titol);
            prepStmt.setString(2, idRecurs);
            logger.debug(prepStmt.toString());
            prepStmt.executeUpdate();
            logger.debug("Update executed...");
            myConnection.commit();
            logger.debug("updateVisites -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (prepStmt != null) {
                    prepStmt.close();
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

    /**
     * Updates the DUC info from a resource
     */
    public void updateDUC(String idRecurs, String ducInfo) {
        Connection myConnection = null;
        String insertStatement = "";
        PreparedStatement prepStmt = null;
        try {
            logger.info("updateDUC -> in");
			// myConnection =
            // DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle,
            // Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            insertStatement = "UPDATE recursos SET ducInfo=? WHERE id=?";
            logger.debug(insertStatement);
            prepStmt = myConnection.prepareStatement(insertStatement);
            prepStmt.setString(1, ducInfo);
            prepStmt.setString(2, idRecurs);

            logger.debug(prepStmt.toString());
            prepStmt.executeUpdate();
            logger.debug("Update executed...");
            myConnection.commit();
            logger.info("updateDUC -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (prepStmt != null) {
                    prepStmt.close();
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

    /**
     * Updates the resource's log
     */
    public void logIndexacio(String idRecurs, boolean xml_valid, boolean url_valid, String urlRecurs) {
        Connection myConnection = null;
        String insertStatement = "";
        PreparedStatement prepStmt = null;
        PreparedStatement prepStmtLog = null;
        try {
            logger.debug("logIndexacio -> in");
			// myConnection =
            // DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle,
            // Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            insertStatement = "UPDATE log_indexacio SET xml_valid=?, url_valid=?, data_log=?, found=1, url_recurs=? WHERE recurs_id=?";
            prepStmt = myConnection.prepareStatement(insertStatement);
            if (xml_valid) {
                prepStmt.setInt(1, 1);
            } else {
                prepStmt.setInt(1, 0);
            }

            if (url_valid) {
                prepStmt.setInt(2, 1);
            } else {
                prepStmt.setInt(2, 0);
            }
            logger.debug(prepStmt.toString());
            Calendar rightNow = Calendar.getInstance();
            String data_actual_string = Utils.calculaDataActual(rightNow);
            String hora_actual_string = Utils.calculaHoraActual(rightNow);
            logger.info("Dia: " + data_actual_string + "/ " + hora_actual_string);
            prepStmt.setString(3, data_actual_string + " " + hora_actual_string);
            prepStmt.setString(4, urlRecurs);
            prepStmt.setString(5, idRecurs);
            prepStmt.executeUpdate();
            myConnection.commit();
            logger.debug("logIndexacio -> out");
        } catch (Exception e) {
            logger.error("[logIndexacio] " + e);
        } finally {
            try {
                if (prepStmt != null) {
                    prepStmt.close();
                }
                if (prepStmtLog != null) {
                    prepStmtLog.close();
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

    /**
     * Updates the resource's log
     */
    public void logIdiomaRecurs(String idRecurs, String idioma) {
        Connection myConnection = null;
        String insertStatement = "";
        String deleteStatement = "";
        PreparedStatement prepStmt = null;
        PreparedStatement prepStmtDel = null;
        try {
            logger.debug("logIdiomaRecurs -> in");
            myConnection = Utils.getConnectionFromPool();

            deleteStatement = "DELETE FROM idioma_recurs WHERE recurs_id=?";
            prepStmtDel = myConnection.prepareStatement(deleteStatement);
            prepStmtDel.setString(1, idRecurs);
            prepStmtDel.executeUpdate();

            insertStatement = "INSERT INTO idioma_recurs (recurs_id, idioma) VALUES (?, ?)";
            prepStmt = myConnection.prepareStatement(insertStatement);
            prepStmt.setString(1, idRecurs);
            prepStmt.setString(2, idioma);
            logger.info("Recurs_id -> " + idRecurs);
            logger.debug("Idioma -> " + idioma);
            prepStmt.executeUpdate();

            myConnection.commit();
            logger.debug("logIdiomaRecurs -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (prepStmt != null) {
                    prepStmt.close();
                }
                if (prepStmtDel != null) {
                    prepStmtDel.close();
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

    /**
     * Returns duration (in minutes) The Celebrate format is P0D1H15M / PT15M
     */
    public int parseDuracio(String duracio) {
        int duracioInt = 0;
        int tIndex = 0;
        int hIndex = 0;
        int mIndex = 0;
        int dies = 0;
        int hores = 0;
        int minuts = 0;
        try {
            logger.debug("parseDuracio -> in");
            logger.debug("Duracio: " + duracio);
            tIndex = duracio.indexOf("T");
            logger.debug("dIndex: " + tIndex);
            hIndex = duracio.indexOf("H");
            logger.debug("hIndex: " + hIndex);
            mIndex = duracio.indexOf("M");
            logger.debug("mIndex: " + mIndex);
            try {
                dies = new Integer(duracio.substring(1, tIndex)).intValue();
            } catch (Exception e) {
            }
            logger.debug("Dies -> " + dies);
            try {
                hores = new Integer(duracio.substring(tIndex + 1, hIndex)).intValue();
            } catch (Exception e) {
            }
            logger.debug("Hores -> " + hores);
            try {
                minuts = new Integer(duracio.substring(hIndex + 1, mIndex)).intValue();
            } catch (Exception e) {
            }
            logger.debug("Minuts -> " + minuts);
            duracioInt = (dies * 1440) + (hores * 60) + minuts;
            logger.debug("parseDuracio -> out");
        } catch (Exception e) {
            logger.error(e);
        }
        return duracioInt; // en minuts
    }

    /**
     * Get the duc codes related to one duc id
     */
    public String getRelacionatsDUC(String ducId) {
        String relacionatsDUC = "";
        Connection myConnection = null;
        String selectStatement = "";
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        int parent_id = -1;
        boolean fi = false;
        try {
            relacionatsDUC += ducId + " ";
			// myConnection =
            // DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle,
            // Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            selectStatement = "SELECT parent_id FROM duc_info where id=" + ducId + " AND relationType='RCC'";
            logger.info(selectStatement);
            stmt = myConnection.createStatement();
            rs = stmt.executeQuery(selectStatement);
            if (rs.next()) {
                parent_id = rs.getInt("parent_id");
                while ((parent_id != -1) && !fi) {
                    relacionatsDUC += parent_id + " ";
                    selectStatement = "SELECT parent_id FROM duc_info where id=" + parent_id
                            + " AND relationType='RCC'";
                    logger.debug(selectStatement);
                    stmt2 = myConnection.createStatement();
                    rs = stmt2.executeQuery(selectStatement);
                    if (rs.next()) {
                        parent_id = rs.getInt("parent_id");
                    } else {
                        fi = true;
                    }
                }
            }
            rs.close();
            stmt.close();
            stmt = myConnection.createStatement();
            // Getting area
            selectStatement = "SELECT parent_id FROM duc_info where id=" + ducId + " AND relationType='RCA'";
            logger.debug(selectStatement);
            rs = stmt.executeQuery(selectStatement);
            while (rs.next()) {
                parent_id = rs.getInt("parent_id");
                relacionatsDUC += parent_id + " ";
            }
            // Getting level
            selectStatement = "SELECT parent_id FROM duc_info where id=" + ducId + " AND relationType='RCL'";
            logger.debug(selectStatement);
            rs.close();
            stmt.close();
            stmt = myConnection.createStatement();
            rs = stmt.executeQuery(selectStatement);
            while (rs.next()) {
                parent_id = rs.getInt("parent_id");
                relacionatsDUC += parent_id + " ";
            }

        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (stmt2 != null) {
                    stmt2.close();
                }

                if (myConnection != null) {
                    Utils.commit(myConnection);
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }

        }

        return relacionatsDUC;
    }

    // Recursiu
    public void parseNodes(Node myNode, String ancestors, String docid, LuceneDocument myDocument) throws Exception {
        NodeList childs = null;
        String attributenames = "";

        String content = "";
        int i = 0;
        int j = 0;
        int childnum = 0;
        int numAttributes = 0;
        try {

            childs = myNode.getChildNodes();

            while (i < childs.getLength()) {
                Node myChild = (Node) childs.item(i);
                int type = myChild.getNodeType();
                if (isLeaf(myChild)) {
                    content = getLeafValue(myChild);
                }

                if (type != 3) {
                    childnum++;
                    NamedNodeMap attributes = myChild.getAttributes();
                    numAttributes = attributes.getLength();
                    j = 0;
                    String lastAtributeValue = "";
                    while (j < numAttributes) {
                        Node myAttribute = attributes.item(j);
                        attributenames += myAttribute.getNodeName() + " ";
                        myDocument.addAttribute(ancestors + myChild.getNodeName() + "#" + myAttribute.getNodeName(),
                                myAttribute.getNodeValue());
                        lastAtributeValue = myAttribute.getNodeValue();
                        j++;
                    }

                    if (myChild.getNodeName().trim().equals("purpose")) {
                        isAmbit = false;
                        isDUC = false;
                        isETB = false;
                    }

                    if (!content.equals("")) {

                        if (content.equals("author")) {
                            isAuthor = true;
                        }
                        if (content.equals("editor")) {
                            isEditor = true;
                        }
                        if (content.equals("creator")) {
                            isCreator = true;
                        }
                        if (content.equals("publisher")) {
                            isPublisher = true;
                        }
                        if (content.equals("DUC")) {
                            isDUC = true;
                        }
                        if (content.equals("AMBIT")) {
                            isAmbit = true;
                        }
                        if (content.equals("ETB")) {
                            isETB = true;
                        }

                        // Get title
                        if (ancestors.trim().endsWith("title@")) {
                            if ("es".equals(lastAtributeValue)) {
                                myDocument.titolEs = content;
                            } else if ("en".equals(lastAtributeValue)) {
                                myDocument.titolEn = content;
                            } else if ("oc".equals(lastAtributeValue)) {
                                myDocument.titolOc = content;
                            } else {
                                myDocument.titol = content;
                            }
                        }
                        // Get description
                        if (ancestors.trim().endsWith("general@description@")) {

                            if ("es".equals(lastAtributeValue)) {
                                myDocument.descripcioEs = content;
                            } else if ("en".equals(lastAtributeValue)) {
                                myDocument.descripcioEn = content;
                            } else if ("oc".equals(lastAtributeValue)) {
                                myDocument.descripcioOc = content;
                            } else {
                                myDocument.descripcio = content;
                            }
                        }

                        // Get Termes thesaurus
                        if (isETB && ancestors.trim().endsWith("taxonPath@taxon@entry@")) {

                            if ("es".equals(lastAtributeValue)) {
                                myDocument.termsEs += content + " # ";
                            } else if ("en".equals(lastAtributeValue)) {
                                myDocument.termsEn += content + " # ";
                            } else if ("oc".equals(lastAtributeValue)) {
                                myDocument.termsOc += content + " # ";
                            } else {
                                myDocument.termsCa += content + " # ";
                            }
                        }
                        // Get Keys
                        if (ancestors.trim().endsWith("general@keyword@")) {

                            if ("es".equals(lastAtributeValue)) {
                                myDocument.keysEs += content + " # ";
                            } else if ("en".equals(lastAtributeValue)) {
                                myDocument.keysEn += content + " # ";
                            } else if ("oc".equals(lastAtributeValue)) {
                                myDocument.keysOc += content + " # ";
                            } else {
                                myDocument.keysCa += content + " # ";
                            }
                        }

                        // Get author
                        if (myChild.getNodeName().trim().equals("entity") && isAuthor) {
                            myDocument.autor = content;
                        }

                        // Get data publicacio
                        if (myChild.getNodeName().trim().equals("dateTime") && isAuthor) {
                            myDocument.dataPublicacio = content;
                            isAuthor = false;
                        }

                        // Get data catalogacio
                        if (myChild.getNodeName().trim().equals("dateTime") && (isCreator || isPublisher)) {
                            myDocument.dataCatalogacio = content;
                            isCreator = false;
                        }

                        // Get URL
                        if (myChild.getNodeName().trim().equals("location")) {
                            myDocument.urlDoc = content;
                        }

                        // Get id recurs
                        if (ancestors.trim().endsWith("general@identifier@")
                                && myChild.getNodeName().trim().equals("entry")) {
                            myDocument.idRecurs = content;
                        }

                        // Get format recurs
                        if (ancestors.trim().endsWith("lom@technical@")
                                && myChild.getNodeName().trim().equals("format")) {
                            myDocument.formatRecurs += " " + content;
                        }

                        // Get idioma
                        if (ancestors.trim().endsWith("lom@general@")
                                && myChild.getNodeName().trim().equals("language")) {
                            myDocument.idioma += " " + content;
                        }

                        // Get duracio
                        if (ancestors.trim().endsWith("typicalLearningTime@")
                                && myChild.getNodeName().trim().equals("duration")) {
                            myDocument.duracio = parseDuracio(content);
                        }

                        // Get DUC
                        if (ancestors.trim().endsWith("taxonPath@taxon@") && myChild.getNodeName().trim().equals("id")
                                && isDUC) {
							// myDocument.duc += getRelacionatsDUC(content) +
                            // " ";
                            myDocument.duc += content + " ";
                        }

                        // Get ambit
                        if (ancestors.trim().endsWith("taxonPath@taxon@entry@")
                                && myChild.getNodeName().trim().equals("string") && isAmbit) {
                            myDocument.ambit = content;
                        }

                        // Get format recurs
                        if (ancestors.trim().endsWith("lom@educational@learningResourceType@")
                                && myChild.getNodeName().trim().equals("value")) {
                            myDocument.tipusRecurs += content + " # ";
                        }
                        if (ancestors.trim().endsWith("lom@educational@context@")
                                && myChild.getNodeName().trim().equals("value")) {
                            myDocument.context += content + " # ";
                        }

						// Nous camps dels recursos físics
                        // Get Catalogador
                        if (myChild.getNodeName().trim().equals("entity") && isPublisher) {
                            myDocument.catalogador = content;
                            isPublisher = false;
                        }
                        // Get carcteristiques físiques
                        if (!"fisic".equals(myDocument.recurs) && ancestors.trim().endsWith("descripcioFisica@")) {
                            myDocument.recurs = "fisic";
                        }
                        // Get Editor
                        if (myChild.getNodeName().trim().equals("entity") && isEditor) {
                            myDocument.editor = content;
                            isEditor = false;
                        }
                        // Get carcteristiques físiques
                        if (ancestors.trim().endsWith("descripcioFisica@")
                                && myChild.getNodeName().trim().equals("caracteristiques")) {
                            myDocument.carFisiques = content;
                        }
                        // Get context
                        if (ancestors.trim().endsWith("general@coverage@")
                                && myChild.getNodeName().trim().equals("string")) {
                            myDocument.coverage = content;
                        }

                        // Get unitats
                        if (ancestors.trim().endsWith("descripcioFisica@unitat@")
                                && myChild.getNodeName().trim().equals("name")) {
                            myDocument.unitatNoms = content;
                        }
                        if (ancestors.trim().endsWith("descripcioFisica@unitat@")
                                && myChild.getNodeName().trim().equals("identifier")) {
                            myDocument.unitatIds = content;
                        }
                        // Get disponibilitats
                        if (ancestors.trim().endsWith("disponibleA@unitat@")
                                && myChild.getNodeName().trim().equals("name")) {
                            myDocument.disponibleNoms += content + ";";
                        }
                        if (ancestors.trim().endsWith("disponibleA@unitat@")
                                && myChild.getNodeName().trim().equals("identifier")) {
                            myDocument.disponibleIds += content + " # ";
                        }
                        // Get relacions
                        if (ancestors.trim().endsWith("relation@") && myChild.getNodeName().trim().equals("kind")) {
                            myDocument.relacioTipus += content + ";";
                        }
                        if (ancestors.trim().endsWith("relation@") && myChild.getNodeName().trim().equals("resource")) {
                            myDocument.relacioIds += content + ";";
                        }
                        if (ancestors.trim().endsWith("relation@description@")
                                && myChild.getNodeName().trim().equals("string")) {
                            myDocument.relacioDesc += content + " # ";
                        }
                        // Get format fisic recurs
                        if (ancestors.trim().endsWith("lom@descripcioFisica@")
                                && myChild.getNodeName().trim().equals("format")) {

                            myDocument.formatRecurs += " " + content.replaceAll(" ", "/");
                        }

                        // Get Llicencia
                        if (ancestors.trim().endsWith("rights@description@")) {
                            logger.info("righst-description-" + lastAtributeValue + ":" + content);
                            if ("ca".equals(lastAtributeValue)) {
                                myDocument.llicDesc = content;
                            } else if ("x-t-cc-url".equals(lastAtributeValue)) {
                                myDocument.llicUrl = content;
                            } else if ("x-t-cc".equals(lastAtributeValue)) {
                                myDocument.llicId = content;
                            } else {
                                if (content != null && content.indexOf("http") >= 0) {
                                    if (myDocument.llicUrl == null || "".equals(myDocument.llicUrl)) {
                                        myDocument.llicUrl = content;
                                    }
                                } else {
                                    if (myDocument.llicId == null || "".equals(myDocument.llicId)) {
                                        myDocument.llicId = content;
                                    }
                                }
                            }
                        }
                        // Get identificador
                        if (ancestors.trim().endsWith("descripcioFisica@identificadors@idResource@")
                                && myChild.getNodeName().trim().equals("identifier")) {
                            if (!"".equals(myDocument.idfisics)) {
                                myDocument.idfisics = "; " + myDocument.idfisics;
                            }
                            myDocument.idfisics = ":" + content + myDocument.idfisics;
                        }
                        if (ancestors.trim().endsWith("descripcioFisica@identificadors@idResource@")
                                && myChild.getNodeName().trim().equals("type")) {
                            myDocument.idfisics = content + myDocument.idfisics;
                        }

                        myDocument.addElement(ancestors + myChild.getNodeName(), content);
                    }

                    allContent += content + " ";
                    parseNodes(myChild, ancestors + myChild.getNodeName() + "@", docid, myDocument);
                }
                i++;
            }
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public void parse(String nameFile, String docid) {
        LuceneDocument myDocument = null;
        DOMParser parser = null;
        Document doc = null;
        UrlRetriever urlRetriever = null;
        int httpCode = 0;
        try {

            logger.info("parse -> in");
            allContent = "";
            logger.debug("Creating document");
            myDocument = new LuceneDocument();
            logger.debug("Creating parser");
            parser = new DOMParser();
            logger.debug("Parsing");
            InputSource IS = new InputSource(new StringReader(nameFile));            
            parser.parse(IS);
            logger.debug("Getting document");
            //NADIM (Transformaci� 20/06/2015) --> Default value return by "DOC" variable is [#document=null]. So don't get confused on seeing null.
            //The process is ok.
            doc = (org.w3c.dom.Document) parser.getDocument(); 
            NodeList rootCelebrate = doc.getElementsByTagName("lom");
            //rootCelebrate contains single item. Also if you see in debuger it'll show lom=null but that doesn't mean this item is null.
            logger.info("Root element: " + rootCelebrate.item(0));

            logger.debug("There are " + rootCelebrate.getLength() + "  elements.");
            boolean xmlOk = true;
            try {
                logger.info("IN");
                parseNodes(rootCelebrate.item(0), "lom@", docid, myDocument);
                logger.info("OUT");
            } catch (Exception e) {
                xmlOk = false;
                logger.error(e);
            }

            logger.info("Filling document -> " + myDocument.idRecurs);
            myDocument.contingut = allContent;
            myDocument.idRecurs = myDocument.idRecurs.replaceAll("MERLI", "");

            int numComentaris = getNumComentaris(myDocument.idRecurs);
            float puntuacio = getPuntuacio(myDocument.idRecurs);
            String comentaris = getComentaris(myDocument.idRecurs);
            int numVisites = getNumVisites(myDocument.idRecurs);
            logger.info("Num comentaris: " + numComentaris);
            myDocument.numComentaris = numComentaris;
            myDocument.puntuacio = puntuacio;
            myDocument.comentaris = comentaris;
            myDocument.numVisites = numVisites;
            myIndexador.createLuceneDocument(myDocument);
            afegirRecurs(myDocument.idRecurs, myDocument.titol);
            updateDUC(myDocument.idRecurs, myDocument.duc);
            logger.debug("Creating url retriever");
            urlRetriever = new UrlRetriever(client);

			// int httpCode = urlRetriever.process(myDocument.urlDoc);
            boolean urlOk = true;
            if (Configuracio.parseUrl.equals("si") && myDocument.urlDoc != null && myDocument.urlDoc.length() > 0) {
                urlOk = false;
                logger.info("Processing url.... " + myDocument.urlDoc);

                httpCode = urlRetriever.process(myDocument.urlDoc);
                if ((httpCode == 200) || myDocument.urlDoc.toLowerCase().endsWith(".pdf")
                        || myDocument.urlDoc.toLowerCase().endsWith(".doc")) {
                    urlOk = true;
                }
            }

            logger.info("Url processed.... " + myDocument.urlDoc);
            logger.debug("Http code: " + httpCode);
            logIndexacio(myDocument.idRecurs, xmlOk, urlOk, myDocument.urlDoc);

            logIdiomaRecurs(myDocument.idRecurs, myDocument.idioma);
            logger.info("parse -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Close the index
     */
    public void finalize() {
        try {
            myIndexador.closeIndex();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Check if index is OK
     */
    public boolean checkIndex() {
        boolean indexOk = false;
        try {
            logger.debug("checkIndex -> in");
            indexOk = myIndexador.checkIndex();
            logger.debug("checkIndex -> out");
        } catch (Exception e) {
            logger.error(e);
        }
        return indexOk;
    }

    /**
     * Cleans the index directory
     */
    public void cleanIndexDirectory() {
        try {
            logger.debug("cleanIndexDirectory -> in");
            myIndexador.cleanIndexDirectory();
            logger.debug("cleanIndexDirectory -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
