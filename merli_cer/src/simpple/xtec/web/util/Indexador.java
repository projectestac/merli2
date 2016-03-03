package simpple.xtec.web.util;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexModifier;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.FSDirectory;

import simpple.xtec.web.analisi.EducacioAnalyzer;
import simpple.xtec.web.cercador.ServletCerca;

public class Indexador {
    
    static int luceneId = 0;
    static Document myDocument = null;
    static Connection myConnection = null;
    static String indexDir = "";
    static String tmpDir = "";
    static IndexWriter indexWriter = null;
    static IndexSearcher mySearcher = null;
    // logger
    static final Logger logger = Logger.getLogger(simpple.xtec.web.util.Indexador.class);
    
    static boolean working = false;
    
    public static synchronized void doProcess() {
        try {
            if (!working) {
                logger.info("Working.............");
                working = true;
                logger.info("Updating.............");
                indexDir = UtilsCercador.getIndexActual(Configuracio.indexDir, Configuracio.indexDir2);
                if (indexDir.indexOf("/tmp") == -1) {
                    updateRecursosModificats();
                } else {
                    logger.info("**********************");
                    logger.info("SKIPPED!!!!!!!!!!!!!");
                    logger.info("**********************");
                }
                logger.info("Updated.............");
            } else {
                logger.info("**********************");
                logger.info("SKIPPED!!!!!!!!!!!!!");
                logger.info("**********************");
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            
            working = false;
        }
    }
    
    public static synchronized void updateRecursosModificats() {
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            
            logger.info("enter...");

            // Test if exists
            tmpDir = indexDir + "/tmp";
            File tmpDirFile = new File(tmpDir);
            if (!tmpDirFile.exists()) {
                tmpDirFile.mkdirs();
            }
            // Clear tmp
            UtilsCercador.clearDir(tmpDir);
            // Copy to tmp
            UtilsCercador.copyDir(indexDir, tmpDir);
            
            logger.info("Opening searcher... " + tmpDir);
            mySearcher = new IndexSearcher(tmpDir);
            logger.info("Opening searcher... " + tmpDir);
            indexWriter = new IndexWriter(tmpDir, new EducacioAnalyzer(), false);
            logger.info("Getting connection.... ");
            myConnection = UtilsCercador.getConnectionFromPool();
            stmt = myConnection.createStatement();
            sql = "SELECT id FROM recursos WHERE modificat=1";
            logger.info(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String id = rs.getString("id");
                logger.info("Modificant recurs -> " + id);
                modificar(id);
                marcarRecursNoModificat(id);
            }
            logger.info("unlocking...");
	      //  IndexReader.unlock(FSDirectory.getDirectory(new File(tmpDir)));

    //        }	
     // checkIntegrity
     // change index dir
            // UtilsCercador.setIndexActualDB(tmpDir);
            // copy to root
            // change index dir
            //  UtilsCercador.setIndexActualDB(indexDir);     
            logger.info("updateRecursosModificats -> fi");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            logger.info("Closing db");
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
            try {
                mySearcher.close();
            } catch (Exception e) {
                logger.error("MySearceher: " + e);
            }            
            try {
                logger.info("Flush indexWriter?");
                if (indexWriter != null) {
                    logger.info("Flushing indexWriter");
                    indexWriter.flush();
                    indexWriter.optimize();
                    indexWriter.close();
                    if (IndexReader.isLocked(indexDir)) {
                        IndexReader.unlock(FSDirectory.getDirectory(new File(indexDir)));
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
            
            try {
                logger.debug("Warning -> Changing dir -> " + tmpDir);                
                if (UtilsCercador.indexIsOk(tmpDir)) {
                    UtilsCercador.setIndexActualDB(tmpDir);
                    ServletCerca.updateIndex();
                    logger.debug("Warning -> Cleaning dir -> " + indexDir);                    
                    UtilsCercador.clearDir(indexDir);
                    logger.debug("Warning -> Copying dir -> " + indexDir);
                    Long bytes = UtilsCercador.copyDir(tmpDir, indexDir);
                    if (bytes != null) {
                        logger.debug("Warning -> Setting dir -> " + indexDir);                        
                        UtilsCercador.setIndexActualDB(indexDir);
                    }
                }
                
                ServletCerca.updateIndex();
            } catch (Exception e) {
                logger.error(e);                
            }
        }
        
    }
    
    public static void getDocument(String recursId) {
        
        Query bq = null;
        QueryParser myQueryParser = null;
        Hits hits = null;
        
        try {
            logger.info("Looking at -> " + tmpDir);
            
            myQueryParser = new QueryParser("idRecurs", new StandardAnalyzer());
            logger.debug("Looking at -> " + tmpDir);
            logger.debug("(idRecurs:\"" + recursId + "\")");
            bq = myQueryParser.parse("(idRecurs:\"" + recursId + "\")");
            hits = mySearcher.search(bq);

			// luceneId = hits.id(0);
            myDocument = (Document) hits.doc(0);
            
        } catch (Exception e) {
            logger.error("Looking at -> " + indexDir);
            logger.error("(idRecurs:\"" + recursId + "\")");
            logger.error("mySearcher -> " + mySearcher);
            logger.error("hits -> " + hits);
            logger.error(e);
        } finally {
            
        }
        
    }
    
    public static boolean updateDocumentFromDb(String recursId) {
        //  Connection myConnection = null;
        FitxaRecurs fitxaRecurs = null;
        boolean processOk = false;
        try {
            //	myConnection = UtilsCercador.getConnectionFromPool();

            fitxaRecurs = new FitxaRecurs(myConnection);
            logger.info("UPDATING visites .... " + fitxaRecurs.getNumVisites(recursId));
            logger.info("Removing numComentaris -> ");
            myDocument.removeField("numComentaris");
            logger.info("Removing numFields -> ");
            myDocument.removeField("numVisites");
            myDocument.removeField("puntuacio");
            myDocument.add(new Field("numComentaris", "" + fitxaRecurs.getNumComentaris(recursId), Field.Store.YES, Field.Index.UN_TOKENIZED));
            myDocument.add(new Field("puntuacio", "" + fitxaRecurs.getPuntuacioMitja(recursId), Field.Store.YES, Field.Index.UN_TOKENIZED));
            myDocument.add(new Field("numVisites", "" + fitxaRecurs.getNumVisites(recursId), Field.Store.YES, Field.Index.UN_TOKENIZED));
            processOk = true;
        } catch (Exception e) {
            logger.error(e);
            logger.error("My connection -> " + myConnection);
            logger.error("My document -> " + myDocument);
            logger.error("fitxaRecurs -> " + fitxaRecurs);
        } finally {
            /*        try {
             myConnection.close();	
             } catch (Exception e) {
             logger.error(e);
             } */
        }
        return processOk;
    }
    
    public static void modificar(String recursId) {
//		IndexModifier indexModifier = null;

        boolean processOk = false;
        try {
            getDocument(recursId);
            processOk = updateDocumentFromDb(recursId);
            if (processOk) {
//		     indexModifier = new IndexModifier(indexDir, new EducacioAnalyzer(), true);

                logger.debug("Modify... " + luceneId);
                indexWriter.updateDocument(new Term("idRecurs", "" + recursId), myDocument, new EducacioAnalyzer());
                
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                /*			if (indexModifier != null) {
                 indexModifier.optimize();				
                 indexModifier.flush();							
                 indexModifier.close();
                 }*/
                
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    /**
     * Set a resource as modified
     */
    public static void marcarRecursNoModificat(String idRecurs) throws SQLException {
        
        Statement stmt = null;
        String query = "";
        try {
            logger.debug("marcarRecursModificat -> in");
            stmt = myConnection.createStatement();
            query = "UPDATE recursos SET modificat=0 WHERE id='" + idRecurs + "'";
            logger.debug("SQL: " + query);
            stmt.executeUpdate(query);            
            
        } catch (SQLException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
        logger.debug("marcarRecursModificat -> out");
    }
    
}
