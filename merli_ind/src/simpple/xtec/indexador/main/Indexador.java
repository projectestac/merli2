package simpple.xtec.indexador.main;

import java.io.File;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import simpple.xtec.indexador.analisi.EducacioAnalyzer;
import simpple.xtec.indexador.objects.LuceneDocument;
import simpple.xtec.indexador.util.Configuracio;
import simpple.xtec.indexador.util.TipusFitxer;
import simpple.xtec.indexador.util.Utils;

/**
 * Class used to generate a Lucene index
 *
 * @author descuer
 *
 */
public class Indexador {

    //	Logger 	
    static Logger logger = Logger.getLogger(simpple.xtec.indexador.main.Indexador.class);

    IndexWriter indexWriter = null;

    String index1 = "";
    String index2 = "";
    String indexActual = "";

    /**
     * Select the current index
     *
     * @param index1
     * @param index2
     * @return
     */
    public String getIndexActual(String index1, String index2) {
        File dir1 = null;
        File dir2 = null;
        int numFiles1 = 0;
        int numFiles2 = 0;
        File segments1 = null;
        File segments2 = null;
        File writeLock1 = null;
        File writeLock2 = null;
        try {
            logger.debug("getIndexActual -> in");
            logger.debug("index1 -> " + index1);
            logger.debug("index2 -> " + index2);
            dir1 = new File(index1);
            dir2 = new File(index2);
            if (!dir1.isDirectory()) {
                return index2;
            }
            if (!dir2.isDirectory()) {
                return index2;
            }
            numFiles1 = dir1.list().length;
            logger.debug("numFiles1: " + numFiles1);
            numFiles2 = dir2.list().length;
            logger.debug("numFiles2: " + numFiles2);
            if (numFiles1 == 0) {
                return index1;
            }
            if (numFiles2 == 0) {
                return index2;
            }

            if (!indexIsOk(index1)) {
                return index1;
            }
            if (!indexIsOk(index2)) {
                return index2;
            }

            if (numFiles1 < 3) {
                return index1;
            }
            if (numFiles2 < 3) {
                return index2;
            }
            writeLock1 = new File(index1 + "/write.lock");
            if (writeLock1.exists()) {
                logger.debug("write lock -> " + index1 + "/write.lock");
                return index1;
            }
            writeLock2 = new File(index2 + "/write.lock");
            if (writeLock2.exists()) {
                logger.debug("write lock -> " + index2 + "/write.lock");
                return index2;
            }

            segments1 = new File(index1 + "/" + dir1.list()[0]);
            segments2 = new File(index2 + "/" + dir2.list()[0]);
            Date date1 = new Date(segments1.lastModified());
            logger.debug("Date 1: " + date1);
            Date date2 = new Date(segments2.lastModified());
            logger.debug("Date 2: " + date2);
            if (date2.after(date1)) {
                return index1;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return index2;
    }

    /**
     * Constructor
     */
    public Indexador() {

        try {
            logger.debug("Constructor -> in");
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            index1 = Configuracio.indexDir;
            logger.debug("Index1 -> " + index1);
            index2 = Configuracio.indexDir2;
            logger.debug("Index2 -> " + index2);
            String indexInUse = Utils.getIndexActualFromDB();
            if (index1.equals(indexInUse)) {
                indexActual = index2;
            }
            if (index2.equals(indexInUse)) {
                indexActual = index1;
            }

            //indexActual = getIndexActual (index1, index2);
            Utils.clearIndexDir(indexActual);
            logger.info("Create index at " + indexActual);

            indexWriter = new IndexWriter(indexActual, new EducacioAnalyzer(), true);
            logger.debug("Constructor -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Adds a document object to the index
     *
     * @param myDocument
     */
    public void createLuceneDocument(LuceneDocument myDocument) {
        Document document = null;
        try {
            logger.debug("createLuceneDocument -> in:" + myDocument.idRecurs);
            document = new Document();
            document.add(new Field("idRecurs", myDocument.idRecurs, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("autor", myDocument.autor, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("titol", myDocument.titol, Field.Store.YES, Field.Index.UN_TOKENIZED));
            document.add(new Field("descripcio", myDocument.descripcio, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("url", myDocument.urlDoc, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("numComentaris", "" + myDocument.numComentaris, Field.Store.YES, Field.Index.UN_TOKENIZED));
            document.add(new Field("puntuacio", "" + myDocument.puntuacio, Field.Store.YES, Field.Index.UN_TOKENIZED));
            document.add(new Field("numVisites", "" + myDocument.numVisites, Field.Store.YES, Field.Index.UN_TOKENIZED));
            document.add(new Field("comentaris", myDocument.comentaris, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("titolEs", myDocument.titolEs, Field.Store.YES, Field.Index.UN_TOKENIZED));
            document.add(new Field("descripcioEs", myDocument.descripcioEs, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("titolEn", myDocument.titolEn, Field.Store.YES, Field.Index.UN_TOKENIZED));
            document.add(new Field("descripcioEn", myDocument.descripcioEn, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("titolOc", myDocument.titolOc, Field.Store.YES, Field.Index.UN_TOKENIZED));
            document.add(new Field("descripcioOc", myDocument.descripcioOc, Field.Store.YES, Field.Index.TOKENIZED));

            document.add(new Field("keysEs", myDocument.keysEs, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("termsEs", myDocument.termsEs, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("keysEn", myDocument.keysEn, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("termsEn", myDocument.termsEn, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("keysOc", myDocument.keysOc, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("termsOc", myDocument.termsOc, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("keysCa", myDocument.keysCa, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("termsCa", myDocument.termsCa, Field.Store.YES, Field.Index.TOKENIZED));

            String duradaString = "";
            int durada = myDocument.duracio;
            if (durada < 10) {
                duradaString = "00000" + durada;
            }
            if ((durada >= 10) && (durada < 100)) {
                duradaString = "0000" + durada;
            }
            if ((durada > 100) && (durada < 1000)) {
                duradaString = "000" + durada;
            }
            if ((durada > 1000) && (durada < 10000)) {
                duradaString = "00" + durada;
            }
            if ((durada > 10000) && (durada < 100000)) {
                duradaString = "0" + durada;
            }

            document.add(new Field("duracio", duradaString, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("dataPublicacio", myDocument.dataPublicacio, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("dataCatalogacio", myDocument.dataCatalogacio, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("duc", "" + myDocument.duc, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("ambit", myDocument.ambit, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("bugSort", "1", Field.Store.YES, Field.Index.TOKENIZED));
            logger.info("Format recurs -> " + myDocument.formatRecurs);
       	 // String grupFormat = new String(TipusFitxer.getGrupFormat(myDocument.formatRecurs).getBytes(), "UTF-8");

            document.add(new Field("idfisics", myDocument.idfisics, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("unitatIds", myDocument.unitatIds, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("editor", myDocument.editor, Field.Store.YES, Field.Index.TOKENIZED));
            if (myDocument.unitatNoms != null && !myDocument.unitatNoms.equals("")) {
                document.add(new Field("catalogador", myDocument.unitatNoms, Field.Store.YES, Field.Index.TOKENIZED));
            } else {
                document.add(new Field("catalogador", myDocument.catalogador, Field.Store.YES, Field.Index.TOKENIZED));
            }
            document.add(new Field("unitatNoms", myDocument.unitatNoms, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("coverage", myDocument.coverage, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("carFisiques", myDocument.carFisiques, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("recurs", myDocument.recurs, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("disponibleIds", myDocument.disponibleIds, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("disponibleNoms", myDocument.disponibleNoms, Field.Store.YES, Field.Index.TOKENIZED));

            document.add(new Field("relacioIds", myDocument.relacioIds, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("relacioTipus", myDocument.relacioTipus, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("relacioDesc", myDocument.relacioDesc, Field.Store.YES, Field.Index.TOKENIZED));

            document.add(new Field("tipusRecurs", myDocument.tipusRecurs, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("context", myDocument.context, Field.Store.YES, Field.Index.TOKENIZED));

            document.add(new Field("llicDesc", myDocument.llicDesc, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("llicUrl", myDocument.llicUrl, Field.Store.YES, Field.Index.TOKENIZED));
            document.add(new Field("llicId", myDocument.llicId, Field.Store.YES, Field.Index.TOKENIZED));

            String grupsFormat = "";
            String documentFormats = myDocument.formatRecurs;

            StringTokenizer myTokenizer = new StringTokenizer(documentFormats);
            while (myTokenizer.hasMoreTokens()) {
                String nextToken = myTokenizer.nextToken();
                String grupFormat = TipusFitxer.getGrupFormat(nextToken);

                grupFormat = Utils.fromAcute(grupFormat);
                // 	   grupFormat = Utils.treureAccents(grupFormat);
                if (grupsFormat.indexOf(grupFormat) < 0) {
                    grupsFormat += grupFormat + " # ";
                }
            }

            logger.info("Format recurs transformat -> " + grupsFormat);

            document.add(new Field("format", grupsFormat, Field.Store.YES, Field.Index.TOKENIZED));

            Enumeration attNames = myDocument.attributeValues.keys();
            String attName = "";
            String attValue = "";
            while (attNames.hasMoreElements()) {
                attName = (String) attNames.nextElement();
                attValue = (String) myDocument.attributeValues.get(attName);
                document.add(new Field(attName, attValue, Field.Store.YES, Field.Index.TOKENIZED));
            }

            document.add(new Field("contingut", myDocument.contingut, Field.Store.YES, Field.Index.TOKENIZED));
            Enumeration elemNames = myDocument.elementValues.keys();
            String elemName = "";
            String elemValue = "";
            while (elemNames.hasMoreElements()) {
                elemName = (String) elemNames.nextElement();
                elemValue = (String) myDocument.elementValues.get(elemName);
                elemName = elemName.replace(':', '@');
                logger.debug("Elem name: " + elemName);

                document.add(new Field(elemName, elemValue, Field.Store.YES, Field.Index.TOKENIZED));
            }
            logger.debug("Adding document");
            indexWriter.addDocument(document);
            logger.debug("Document added");
            logger.debug("createLuceneDocument -> out");
        } catch (Exception e) {
            logger.error(myDocument);
            logger.error(e);
        }
    }

    /**
     * Checks if generated index is OK
     *
     * @param indexDir
     * @return
     */
    public boolean indexIsOk(String indexDir) {
        IndexSearcher searcher = null;
        FSDirectory indexDirectory = null;
        int maxDoc = 0;
        boolean isOk = false;
        try {
            logger.info("indexIsOk -> in");
            indexDirectory = FSDirectory.getDirectory(indexDir);
            logger.info("Checking... " + indexDir);
            if (IndexReader.isLocked(indexDirectory)) {
                logger.info("Index locked...." + indexDirectory);
                IndexReader.unlock(indexDirectory);
            }
            if (!IndexReader.indexExists(indexDirectory)) {
                logger.info("Index no existeix...." + indexDirectory);
                isOk = false;
            }
            logger.info("Creating searcher...");
            searcher = new IndexSearcher(indexDirectory);
            maxDoc = searcher.maxDoc();
            logger.info("Max doc..." + maxDoc);
            //     if (maxDoc <= 500) {
            if (maxDoc <= 5) {
                isOk = false;
            } else {
                isOk = true;
            }
            logger.info("indexIsOk -> out");
        } catch (Exception e) {
            logger.error(e);
            logger.error("Index -> " + indexDir);
            isOk = false;
        } finally {
            try {
                searcher.close();
            } catch (Exception e) {
            }
        }
        logger.info("Index ok -> " + isOk);
        return isOk;
    }

    /**
     * Optimizes and closes the index
     */
    public void closeIndex() {
        try {
            logger.debug("closeIndex -> in");
            logger.debug("Optimizing index...");
            indexWriter.optimize();
            logger.debug("Closing index...");
            indexWriter.close();
            logger.debug("closeIndex -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Returns true if index is OK
     *
     * @return
     */
    public boolean checkIndex() {
        boolean indexOk = false;
        try {
            logger.debug("checkIndex -> in");
            logger.debug("indexActual: " + indexActual);
            indexOk = indexIsOk(indexActual);
            logger.debug("indexOk: " + indexOk);
            logger.debug("checkIndex -> out");
        } catch (Exception e) {
            logger.error(e);
        }
        return indexOk;
    }

    /**
     * Cleans the directory index
     */
    public void cleanIndexDirectory() {
        try {
            logger.debug("cleanIndexDirectory -> in");
            logger.debug("indexActual: " + indexActual);
            Utils.clearIndexDir(indexActual);
            logger.debug("cleanIndexDirectory -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
