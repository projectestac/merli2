package simpple.xtec.web.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import simpple.xtec.web.analisi.EducacioAnalyzer;

public class FitxaRecurs {

    // logger
    static final Logger logger = Logger.getLogger(simpple.xtec.web.util.FitxaRecurs.class);
    Connection myConnection = null;
    IndexSearcher mySearcher = null;

    public FitxaRecurs(Connection myConnection) {
        if (Configuracio.isVoid()) {
            Configuracio.carregaConfiguracio();
        }
        this.myConnection = myConnection;
    }

    public void closeSearcher() {
        try {
            if (mySearcher != null) {
                mySearcher.close();
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public Document getDocument(String indexActual, String idRecurs) throws SQLException {
        QueryParser myQueryParser = null;
        String textQuery = "";
        Query queryRecurs = null;
        Hits resultats = null;
        Document resultat = null;

        try {
            mySearcher = new IndexSearcher(indexActual);
            try {
                mySearcher.maxDoc();
            } catch (Exception e) {

                indexActual = indexActual.replaceAll("/tmp", "");

                if (UtilsCercador.indexIsOk(indexActual + "/tmp")) {

                    UtilsCercador.recoverFromTmp(indexActual);
                } else {

                    UtilsCercador.recoverFromBackup(indexActual);
                }
                mySearcher = new IndexSearcher(indexActual);
                UtilsCercador.setIndexActualDB(indexActual);
            }
            myQueryParser = new QueryParser("idRecurs", new EducacioAnalyzer());
            textQuery = "(idRecurs:\"" + idRecurs + "\")";
            logger.debug(textQuery);
            queryRecurs = myQueryParser.parse(textQuery);
            resultats = mySearcher.search(queryRecurs);
            resultat = (Document) resultats.doc(0);
        } catch (Exception e) {
            logger.error(e);
        } finally {

        }
        return resultat;
    }

    public float getPuntuacioMitja(String idRecurs) {
        float puntuacioMitja = (float) 0.0;
        String query = "";

        Statement stmt = null;
        ResultSet rs = null;
        try {

            stmt = myConnection.createStatement();

            query = "select avg(puntuacio) from comentaris where suspens=0 and recurs_id='" + idRecurs + "' and suspens=0";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                puntuacioMitja = (float) rs.getFloat(1);
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

            } catch (Exception e) {
                logger.error(e);
            }
        }
        return puntuacioMitja;
    }

    public int getNumVisites(String idRecurs) {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int numVisites = 0;
        try {
            logger.debug("getNumVisites -> in");
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
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return numVisites;
    }

    public int getNumComentaris(String idRecurs) {
        int numComentaris = 0;
        String query = "";

        Statement stmt = null;
        ResultSet rs = null;
        try {

            stmt = myConnection.createStatement();

            query = "select count(*) from comentaris where suspens=0 and recurs_id='" + idRecurs + "' and suspens=0";
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                numComentaris = rs.getInt(1);
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

            } catch (Exception e) {
                logger.error(e);
            }
        }
        return numComentaris;
    }

    public ComentariObject getComentariByUsuari(String nomUsuari, String idRecurs) {
        ComentariObject comentariObject = null;
        String query = "";

        Statement stmt = null;
        ResultSet rs = null;
        try {

            stmt = myConnection.createStatement();

            query = "SELECT * FROM comentaris WHERE xtec_username='" + nomUsuari + "' AND recurs_id='" + idRecurs + "'";
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                comentariObject = new ComentariObject();
                comentariObject.id = (long) rs.getLong("id");
                comentariObject.setComentari((String) rs.getString("comentari"), 100);
                comentariObject.titol = rs.getString("titol");
                comentariObject.puntuacio = rs.getInt("puntuacio");
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

            } catch (Exception e) {
                logger.error(e);
            }
        }
        return comentariObject;
    }

    public ArrayList getComentarisFromRecurs(String idRecurs) {
        String sqlQuery = "";

        Statement stmt = null;
        ResultSet rs = null;
        ArrayList allComentaris = null;
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            allComentaris = new ArrayList();

            stmt = myConnection.createStatement();
            sqlQuery = "SELECT * FROM comentaris WHERE recurs_id='" + idRecurs + "' AND suspens=0";
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                ComentariObject comentariObject = new ComentariObject();
                comentariObject.id = (long) rs.getLong("id");
                comentariObject.setComentari((String) rs.getString("comentari"), 100);
                comentariObject.titol = (String) rs.getString("titol");
                comentariObject.puntuacio = (int) rs.getInt("puntuacio");
                comentariObject.autor = (String) rs.getString("xtec_username");
                comentariObject.dataEdicio = (String) rs.getString("data_edicio");
                allComentaris.add(comentariObject);
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
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return allComentaris;
    }

    public ArrayList getRecomanacionsFromRecurs(String idRecurs) {
        String sqlQuery = "";

        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        ArrayList allRecursos = null;
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            allRecursos = new ArrayList();

            stmt = myConnection.createStatement();
            sqlQuery = "SELECT * FROM recomanacions WHERE recursid='" + idRecurs + "'";
            rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                //if (1 == 1) {
                String recursosRecomanats = rs.getString("recursosRecomanats");
                //String recursosRecomanats = "1812 2120 2000 1000";
                StringTokenizer myTokenizer = new StringTokenizer(recursosRecomanats);
                RecursObject recursObject = null;
                while (myTokenizer.hasMoreTokens()) {
                    String recomanat = (String) myTokenizer.nextToken();
                    recursObject = new RecursObject();
                    recursObject.id = recomanat;
                    //   stmt.close();
                    stmt2 = myConnection.createStatement();
                    logger.debug("SELECT avg(puntuacio) from comentaris where recurs_id=" + recomanat);
                    rs2 = stmt2.executeQuery("SELECT avg(puntuacio) from comentaris where recurs_id=" + recomanat);
                    if (rs2.next()) {
                        recursObject.puntuacio = rs2.getInt(1);
                    }
                    rs2.close();
                    logger.debug("SELECT titol from recursos where id=" + recomanat);
                    rs2 = stmt2.executeQuery("SELECT titol from recursos where id=" + recomanat);
                    if (rs2.next()) {
                        recursObject.titol = rs2.getString("titol");
                    }

                    allRecursos.add(recursObject);
                    stmt2.close();
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
    	// rs2 closed when stm2 closes
    /*	if (rs2 != null) {
                 logger.error("22 ->");    		
                 rs2.close();
                 }
                 */
                if (stmt != null) {
                    stmt.close();
                }
                /*	if (stmt2 != null) {
                 logger.error("23 ->");  		
                 stmt2.close();
                 }        	
                 */
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return allRecursos;
    }

    public Hashtable getNivellsAreesContents(String duc, String sLang) {
        String sqlQuery = "";
        String sqlQueryNivell = "";
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rsNivell = null;
        Hashtable results = null;
        String ducItem = "";
        String rel = "";
        String term;
        String lastParent = "";
        int parent_id = -1;
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            results = new Hashtable();

            stmt = myConnection.createStatement();

            StringTokenizer myTokenizer = new StringTokenizer(duc);

            results.put("content", new Hashtable());
            results.put("area", new Hashtable());
            results.put("level", new Hashtable());
            results.put("parent_level", new Hashtable());
            String lastlist = "";
            int pas = 0;
            while (myTokenizer.hasMoreTokens()) {
                ducItem = (String) myTokenizer.nextToken();
                ducItem = ducItem.trim();
                sqlQuery = "SELECT term, parent_id, relationType FROM duc_info WHERE id=" + ducItem;// + " AND relationType='RAL'";
                logger.debug(sqlQuery);
                rs = stmt.executeQuery(sqlQuery);
                while (rs.next()) {
                    lastParent = String.valueOf(rs.getInt("parent_id"));
                    rel = rs.getString("relationType");
                    term = rs.getString("term");

                    if (rel.indexOf("RC") >= 0) {
                        ((Hashtable) results.get("content")).put(ducItem, term);

                        if ("RCA".equals(rel)) {
                            ((Hashtable) results.get("area")).put(ducItem, lastParent);
                        }
                        if ("RCL".equals(rel)) {
                            if (results.containsKey(lastParent)) {
                                ((Hashtable) results.get("level")).put(ducItem, lastParent);
                            }
                        }
                        results.put(ducItem, term);
                    } else {
                        if ("level".equals(rel) || "RLL".equals(rel)) {
                            if (((Hashtable) results.get("level")).containsKey(lastParent)) {
                                results.put(ducItem, (String) results.get(lastParent) + " - " + term);
                            } else if (!"-1".equals(lastParent)) {
                                results.put(ducItem, UtilsCercador.getTermDuc(myConnection, lastParent, sLang) + " - " + term);
                            } else if ("-1".equals(lastParent)) {
                                if (!UtilsCercador.hasChildLevels(myConnection, ducItem)) {
                                    results.put(ducItem, term);
                                } else {
                                    ((Hashtable) results.get("parent_level")).put(ducItem, term);
                                }
                            }
                        } else {
                            results.put(ducItem, term);
							// ((Hashtable) results.get("area")).put(ducItem,
                            // ducItem);
                            ((Hashtable) results.get("level")).put(ducItem, lastParent);
							// ((Hashtable) results.get("content")).put(ducItem,
                            // term);
                        }
                    }

                }
//	    	 if (rs.next()) {
//	    		 rel = rs.getString("relationType");
//	    		 if (
//	    				(pas>1 && ("level".equals(rel) || rel.indexOf("RL")>=0)) ||
//	    				(pas==1 && rel.indexOf("RA")>=0) ||
//	    				(pas==2 && !"RAA".equals(rel)) ||
//	    				(pas==2 && rel.indexOf("RC")>=0) ||
//	    				(pas==3 && !"RCC".equals(rel))
//	    			){
//	    			if (!"".equals(lastlist)){
//	    				results.add(lastlist);
//	    			}
//	    			lastlist = "";
//	    		 }
//	    		 term = rs.getString("term");
//	    		 
//	    		 if (lastlist!= "") lastlist += " /";
//	    		 lastlist +="<span ";
//	    		 if ("level".equals(rel) || rel.indexOf("RL")>=0){		    		
//		    		 lastlist +="title=\"nivell"+ducItem+"\"";
//		    		 pas=1;
//	    		 }else if (rel.indexOf("RA")>=0){		    		
//		    		 lastlist +="title=\"area"+ducItem+"\"";
//		    		 pas = 2;
//		    	 }else if (rel.indexOf("RC")>=0){		    		
//		    		 lastlist += "title=\"content"+ducItem+"\" class=\"contentDUC\"";
//		    		 pas = 3;
//	    		 }	 
//	    		 lastlist +=">";
//	    		 lastlist +=term;	    		 
//	    		 lastlist +="&nbsp;</span>";
//    	     }
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
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return results;
    }

    public Hashtable getNivellsArees(String duc, String sLang) {
        String sqlQuery = "";
        String sqlQueryNivell = "";
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rsNivell = null;
        Hashtable results = null;
        String ducItem = "";
        int parent_id = -1;
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            results = new Hashtable();

            stmt = myConnection.createStatement();

            StringTokenizer myTokenizer = new StringTokenizer(duc);
            while (myTokenizer.hasMoreTokens()) {
                ducItem = (String) myTokenizer.nextToken();
                ducItem = ducItem.trim();
                sqlQuery = "SELECT parent_id FROM duc_info WHERE id=" + ducItem + " AND relationType='RAL'";
                logger.debug(sqlQuery);
                rs = stmt.executeQuery(sqlQuery);
                if (rs.next()) {
                    parent_id = rs.getInt("parent_id");
                    String nomArea = UtilsCercador.getTermDuc(myConnection, "" + ducItem, sLang);
                    String nomNivell = UtilsCercador.getTermDuc(myConnection, "" + parent_id, sLang);
	             // XTEC: Added this check to hide Competency area
                    //if (nomArea!=null && !nomArea.startsWith("Compet�ncies")){
                    results.put(nomNivell, nomArea);
                    //}
                }
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
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return results;
    }

    public int getComentarisSuspesos(String login) {
        int comentarisSuspesos = 0;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            stmt = myConnection.createStatement();
            sql = "SELECT count(*) FROM comentaris WHERE xtec_username='" + login + "' and suspens=1";
            logger.debug(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                comentarisSuspesos = rs.getInt(1);
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

            } catch (Exception e) {
            }
        }
        return comentarisSuspesos;
    }

    /*
     * DEPRECATED
     */
    public static String getImageFormat(String format) {
        String imgUrl = "";
        try {
            if (format.trim().equalsIgnoreCase("V�deo") || format.trim().equalsIgnoreCase("V?deo")) {
                imgUrl = "video.gif";
            }
            if (format.trim().equalsIgnoreCase("Interactiu Flash")) {
                imgUrl = "flash.gif";
            }
            if (format.trim().equalsIgnoreCase("P�gina web") || format.trim().equalsIgnoreCase("P?gina web")) {
                imgUrl = "web.gif";
            }
            if (format.trim().equalsIgnoreCase("Imatge")) {
                imgUrl = "imatges.gif";
            }
            if (format.trim().equalsIgnoreCase("PDF")) {
                imgUrl = "pdf.gif";
            }
            if (format.trim().equalsIgnoreCase("�udio") || format.trim().equalsIgnoreCase("?udio")) {
                imgUrl = "audio.gif";
            }
            if (format.trim().equalsIgnoreCase("Fitxer comprimit")) {
                imgUrl = "compressio.gif";
            }
            if (format.trim().equalsIgnoreCase("Full de c�lcul") || format.trim().equalsIgnoreCase("Full de c?lcul")) {
                imgUrl = "calcul.gif";
            }
            if (format.trim().equalsIgnoreCase("Presentaci�") || format.trim().equalsIgnoreCase("Presentaci?")) {
                imgUrl = "presentacion.gif";
            }
            if (format.trim().equalsIgnoreCase("Document de text")) {
                imgUrl = "text.gif";
            }

        } catch (Exception e) {
            logger.error(e);
        }
        return imgUrl;
    }

}
