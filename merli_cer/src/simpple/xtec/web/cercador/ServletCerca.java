package simpple.xtec.web.cercador;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.store.FSDirectory;

import simpple.xtec.agrega.AgregaInterface;
import simpple.xtec.web.analisi.EducacioAnalyzer;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.QueryGenerator;
import simpple.xtec.web.util.QueryLogger;
import simpple.xtec.web.util.ResultGenerator;
import simpple.xtec.web.util.UtilsCercador;
import simpple.xtec.web.util.XMLCollection;

/**
 * This servlet handles the search requests
 *
 * @author descuer
 *
 */
public class ServletCerca extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    // logger
    static final Logger logger = Logger.getLogger(simpple.xtec.web.cercador.ServletCerca.class);
    
    static String indexDir1 = "";
    static String indexDir2 = "";
    static IndexSearcher indexPrincipal = null;
    
    static ArrayList allLevels = null;
    static Hashtable allAreas = null;
    static Random myRandom = null;
    
    static String semafor = "";
    static String indexActual = "";
    Timer myTimer = null;
    static FSDirectory myFSdirectory = null;

    //myRAMdirectory = new RAMDirectory(Configuracio.lematizeIndex);
    public synchronized static void updateIndex() {
        
        try {
            //	synchronized (semafor) {
            if (indexPrincipal != null) {
//			 logger.error("Closing index..."); 
                try {
                    indexPrincipal.close();
                } catch (Exception e) {
                    logger.error(e);
                }
                try {
                    myFSdirectory.close();
                } catch (Exception e) {
                    logger.error(e);
                }
                
            }
            
            indexDir1 = Configuracio.indexDir;
            indexDir2 = Configuracio.indexDir2;
            logger.debug("getIndexActual...");
            indexActual = UtilsCercador.getIndexActual(indexDir1, indexDir2);
            if (indexActual.indexOf("/tmp") == -1) {
                File backupTest = new File(indexActual + "/tmpBackup");
                if (!backupTest.exists()) {
                    saveToBackup(indexActual);
                }
            }
            logger.debug("Index actual: " + indexActual);
            myFSdirectory = FSDirectory.getDirectory(indexActual);
            indexPrincipal = new IndexSearcher(myFSdirectory);

            //  }
        } catch (Exception e) {
            logger.error(e);
        }
    }
    
    private void loadDUCinfo(Connection myConnection) {
        try {
            allLevels = UtilsCercador.getAllLevels(myConnection);
            allAreas = UtilsCercador.getAllAreas(myConnection, allLevels);
//		   config.getServletContext().setAttribute("levels", allLevels);
//		   config.getServletContext().setAttribute("areas", allAreas);			
        } catch (Exception e) {
            logger.error(e);
        }
    }
    
    public static void saveToBackup(String indexDir) {
        // TODO: Comentar temporalment
        // String destDir = indexDir + "/tmpBackup";
        // try{
        // File destDirFile = new File(destDir);
        // if (!destDirFile.exists()) {
        // destDirFile.mkdirs();
        // }
        // logger.info("saveToBackup -> in");
        // logger.info("clearing Dir -> " + destDir);
        // UtilsCercador.clearDir (destDir);
        // logger.info("copying Dir From -> " + indexDir);
        // logger.info("copying Dir To -> " + destDir);
        // UtilsCercador.copyDir (indexDir, destDir);
        // logger.info("saveToBackup -> out");
        // } catch (Exception e) {
        // logger.error(e);
        // logger.error("copying Dir From -> " + indexDir);
        // logger.error("copying Dir To -> " + destDir);
        // }
    }

    /**
     * Do the initialization
     */
    public void init(ServletConfig config) throws ServletException {
        Connection myConnection = null;
        String indexActual = "";
        
        try {
            // ServletContext application = getServletConfig().getServletContext();
            ServletContext application = config.getServletContext();
            String sPropertiesFile = "missatges_ca.properties";
            String webRoot = application.getRealPath("/WEB-INF/" + sPropertiesFile);
            XMLCollection.loadProperties(webRoot);
            logger.debug("init -> in");
            allAreas = new Hashtable();
            myRandom = new Random();
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            
            indexDir1 = Configuracio.indexDir;
            indexDir2 = Configuracio.indexDir2;
            
            indexActual = UtilsCercador.getIndexActual(indexDir1, indexDir2);
            indexActual = indexActual.replaceAll("/tmp", "");
            
            if (UtilsCercador.indexIsOk(indexActual)) {
                saveToBackup(indexActual);
            } else {
                UtilsCercador.recoverFromBackup(indexActual);
            }

            /*  if (indexActual.indexOf("/tmp/tmp") != -1) {
             indexActual = "/export/home/e13_merli_cer/indexs/index1"; 
             }
             */
            UtilsCercador.setIndexActualDB(indexActual);
            
            logger.info("Index actual: " + indexActual);
            //  indexPrincipal = new IndexSearcher(indexActual);

            myConnection = UtilsCercador.getConnectionFromPool();
            logger.debug("Loading levels...");
            loadDUCinfo(myConnection);
            
            if (Configuracio.refreshIndex.equals("si")) {
                if (myTimer != null) {
                    myTimer.cancel();
                }
                myTimer = new Timer();

                //  Cada 15 minuts
                // System.out.println("Scheduliiing....");
                //   myTimer.schedule(new MyTimerTask(),60000, 60000);
                //  System.out.println("Scheduled....");
                myTimer.schedule(new MyTimerTask(), 100, 900000);
                //     myTimer.schedule(new MyTimerTask(),120000, 120000);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                myConnection.close();
            } catch (Exception e) {
                logger.error(e);
            }
        }
        logger.debug("init -> out");
    }
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
    
    public void destroy() {
        try {
            logger.debug("destroy -> in");
            //  indexPrincipal.close();
            logger.debug("destroy -> out");
        } catch (Exception e) {
            logger.error(e);
        }
        
    }

    /**
     * Fills a hashtable with all the parameters
     */
    public static Hashtable getParametersFromRequest(HttpServletRequest request) {
        String autorCerca = "";
        String idiomaCerca = "";
        String dretsReproduccioCerca = "";
        String destinatariCerca = "";
        String nivellEducatiu = "";
        String areaCurricular = "";
        String cicle = "";
        String tipusRecurs = "";
        String formatRecurs = "";
        String keywordsCerca = "";
        String editorialCerca = "";
        String llicenciaCerca = "";
        String dataIniciPublicacio = "";
        String dataFinalPublicacio = "";
        String dataIniciCatalogacio = "";
        String dataFinalCatalogacio = "";
        String ambit = "";
        String duradaMinima = "";
        String duradaMaxima = "";
        String filtreRecurs = "";
        // String recursFisicOnline = "";
        String recursOnline = "";
        String recursFisic = "";
        String unitatCerca = "";
        
        Hashtable parameters = null;
        
        try {
            request.setCharacterEncoding("UTF-8");
            logger.debug("getParametersFromRequest -> in");
            parameters = new Hashtable();
            autorCerca = request.getParameter("autorCerca");
            if (autorCerca != null) {
                autorCerca = new String(request.getParameter("autorCerca").getBytes(), "UTF-8");
                parameters.put("autorCerca", autorCerca);
            }
            logger.debug("autorCerca: " + autorCerca);
            
            idiomaCerca = request.getParameter("idiomaCerca");
            if (idiomaCerca != null) {
                parameters.put("idiomaCerca", idiomaCerca);
            }
            logger.debug("idiomaCerca: " + idiomaCerca);
            
            editorialCerca = request.getParameter("editorialCerca");
            if (editorialCerca != null) {
                editorialCerca = new String(request.getParameter("editorialCerca").getBytes(), "UTF-8");
                parameters.put("editorialCerca", editorialCerca);
            }
            logger.debug("editorialCerca: " + editorialCerca);
            
            dretsReproduccioCerca = request.getParameter("dretsReproduccioCerca");
            if (dretsReproduccioCerca != null) {
                parameters.put("dretsReproduccioCerca", dretsReproduccioCerca);
            }
            logger.debug("dretsReproduccioCerca: " + dretsReproduccioCerca);
            
            destinatariCerca = request.getParameter("destinatariCerca");
            if (destinatariCerca != null) {
                parameters.put("destinatariCerca", destinatariCerca);
            }
            logger.debug("destinatariCerca: " + destinatariCerca);
            
            nivellEducatiu = request.getParameter("nivell_educatiu");
            if (nivellEducatiu != null) {
                parameters.put("nivellEducatiu", nivellEducatiu);
            } else {
                parameters.put("nivellEducatiu", "-1");
            }
            logger.debug("nivellEducatiu: " + nivellEducatiu);
            
            areaCurricular = request.getParameter("area_curricular");
            if (areaCurricular != null) {
                parameters.put("areaCurricular", areaCurricular);
            } else {
                parameters.put("areaCurricular", "-1");
            }
            logger.debug("areaCurricular: " + areaCurricular);
            
            cicle = request.getParameter("cicle");
            if (cicle != null) {
                parameters.put("cicle", cicle);
            } else {
                parameters.put("cicle", "-1");
            }
            logger.debug("cicle: " + cicle);
            
            tipusRecurs = request.getParameter("tipusRecurs");
            if (tipusRecurs != null) {
                parameters.put("tipusRecurs", tipusRecurs);
            }
            logger.debug("tipusRecurs: " + tipusRecurs);
            
            formatRecurs = request.getParameter("formatRecurs");
            if (formatRecurs != null) {
                formatRecurs = new String(request.getParameter("formatRecurs").getBytes(), "UTF-8");
                parameters.put("formatRecurs", formatRecurs);
            }
            logger.debug("formatRecurs: " + formatRecurs);
            
            filtreRecurs = request.getParameter("filtreRecurs");
            if (filtreRecurs != null) {
                filtreRecurs = new String(request.getParameter("filtreRecurs").getBytes(), "UTF-8");
                parameters.put("filtreRecurs", filtreRecurs);
            }
            logger.debug("filtreRecurs: " + filtreRecurs);
            
            keywordsCerca = request.getParameter("keywords");
            if (keywordsCerca != null) {
                keywordsCerca = new String(request.getParameter("keywords").getBytes(), "UTF-8");
                parameters.put("keywords", keywordsCerca);
            }
            logger.debug("keywords: " + keywordsCerca);
            
            dataIniciPublicacio = request.getParameter("dataIniciPublicacio");
            if (dataIniciPublicacio != null) {
                parameters.put("dataIniciPublicacio", dataIniciPublicacio);
            }
            logger.debug("Data inici publicacio: " + dataIniciPublicacio);
            
            dataFinalPublicacio = request.getParameter("dataFinalPublicacio");
            if (dataFinalPublicacio != null) {
                parameters.put("dataFinalPublicacio", dataFinalPublicacio);
            }
            logger.debug("Data final publicacio: " + dataFinalPublicacio);
            
            dataIniciCatalogacio = request.getParameter("dataIniciCatalogacio");
            if (dataIniciCatalogacio != null) {
                parameters.put("dataIniciCatalogacio", dataIniciCatalogacio);
            }
            logger.debug("Data inici catalogacio: " + dataIniciCatalogacio);
            
            dataFinalCatalogacio = request.getParameter("dataFinalCatalogacio");
            if (dataFinalCatalogacio != null) {
                parameters.put("dataFinalCatalogacio", dataFinalCatalogacio);
            }
            logger.debug("Data final catalogacio: " + dataFinalCatalogacio);
            
            ambit = request.getParameter("ambit");
            if (ambit != null) {
                parameters.put("ambit", ambit);
            }
            logger.debug("ambit: " + ambit);
            
            llicenciaCerca = request.getParameter("llicenciaCerca");
            if (llicenciaCerca != null) {
                parameters.put("llicenciaCerca", llicenciaCerca);
            }
            logger.debug("llicenciaCerca: " + llicenciaCerca);
            
            duradaMinima = request.getParameter("durada_minima");
            if (duradaMinima != null) {
                parameters.put("duradaMinima", duradaMinima);
            }
            logger.debug("duradaMinima: " + duradaMinima);
            
            duradaMaxima = request.getParameter("durada_maxima");
            if (duradaMaxima != null) {
                parameters.put("duradaMaxima", duradaMaxima);
            }
            logger.debug("duradaMaxima: " + duradaMaxima);
            
            String[] ducContent = request.getParameterValues("ducContent");
            if (ducContent != null) {
                if (ducContent[0].indexOf(",") != -1) {
                    StringTokenizer myTokenizer = new StringTokenizer(ducContent[0], ",");
                    ArrayList values = new ArrayList();
                    while (myTokenizer.hasMoreTokens()) {
                        values.add(myTokenizer.nextToken());
                    }
                    
                    String[] ducContentArray = new String[values.size()];
                    int k = 0;
                    while (k < values.size()) {
                        ducContentArray[k] = (String) values.get(k);
                        k++;
                    }
                    
                    parameters.put("ducContent", ducContentArray);
                } else {
                    parameters.put("ducContent", ducContent);
                }
            } else {
                
            }

            //Nous camps afegits amb els recursos f�sics.
//		recursFisicOnline = request.getParameter ("recursFisicOnline");
//		if (recursFisicOnline != null) {
//		   parameters.put("recursFisicOnline", recursFisicOnline);
//		   }
//		logger.debug("recursFisicOnline: " + recursFisicOnline);
            recursOnline = request.getParameter("recursOnline");
            if (recursOnline != null) {
                parameters.put("recursOnline", recursOnline);
            }
            logger.debug("recursOnline: " + recursOnline);
            
            recursFisic = request.getParameter("recursFisic");
            if (recursFisic != null) {
                parameters.put("recursFisic", recursFisic);
            }
            logger.debug("recursFisic: " + recursFisic);
            
            unitatCerca = request.getParameter("unitatCerca");
            if (unitatCerca != null) {
                parameters.put("unitatCerca", unitatCerca);
            }
            logger.debug("unitatCerca: " + unitatCerca);
            
        } catch (Exception e) {
            logger.error(e);
        }
        logger.debug("getParametersFromRequest -> out");
        return parameters;
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sLang = XMLCollection.getLang(request);
        logger.debug("sLang:" + sLang);
        String textCerca = "";
        String textCercaHidden = "";
        String novaCerca = "";
        QueryGenerator queryGenerator = null;
        ResultGenerator resultGenerator = null;
        String tipus = "";
        int nivell = 0;
        String nivellString = "";
        String ordenacio = "";
        String direccio = "true";
        Query bq = null, totalBq = null;
        Connection myConnection = null;
        
        String inxtecString = "";
        int inxtec = 0;
        
        Hashtable parameters = null;
        String sheetId = "";
        String usuari = "";
        String usuariNomComplet = "";
        String cataleg = "";
        boolean isCataleg = false;
        String agregaString = "0";
        boolean isAgrega = false;
        String userGeneric = "";
        String imprimir = "";
        
        Hits hitsA = null;
        Hits hitsTotal = null;
        ArrayList hitsAgrega = null;
        //    IndexSearcher indexPrincipal = null;

        try {

            // PropertyConfigurator.configure("log4j-dettbug.properties");
            HttpSession session = request.getSession();
            request.setCharacterEncoding("UTF-8");

            /*		    indexActual = UtilsCercador.getIndexActual(indexDir1, indexDir2);
             logger.debug("Index actual: " + indexActual);
             indexPrincipal = new IndexSearcher(indexActual);		    
             */
            agregaString = (String) request.getParameter("agrega");
            if ("1".equals(agregaString)) {
                isAgrega = true;
                logger.debug("Cerca en mode AGREGA.");
            } else {
                logger.debug("Cerca en mode MERLI.");
            }
            
            if (indexPrincipal == null) {
                updateIndex();
                session.setAttribute("indexActual", indexPrincipal);
            } else {
                try {
                    if (indexPrincipal.maxDoc() < 10) {
                        updateIndex();
                        session.setAttribute("indexActual", indexPrincipal);
                    }
                } catch (Exception e) {
                    updateIndex();
                    session.setAttribute("indexActual", indexPrincipal);
                }
            }
            tipus = request.getParameter("tipus");
            if (tipus == null) {
                tipus = "";
            }
            logger.debug("tipus: " + tipus);
            if (tipus.equals("updateIndex")) {
                updateIndex();
                session.setAttribute("indexActual", indexPrincipal);
                return;
            }
            
            usuari = (String) session.getAttribute("nomUsuari");
            if (usuari == null) {
                // Amendez 23-03-2016 https://trello.com/c/KGRG9QBR
                usuari= (String) session.getAttribute("user");
            }
            
            userGeneric = (String) session.getAttribute("userGeneric");
            session.setAttribute("lastUrl", UtilsCercador.getLastUrl(request));
            imprimir = request.getParameter("imprimir");
            String urlImprimir = "";
            if (imprimir == null) {
                imprimir = "no";
            }
            if (imprimir.equals("no")) {
                urlImprimir = UtilsCercador.getLastUrl(request);
                urlImprimir = urlImprimir.replaceAll("&imprimir=no", "");
                urlImprimir = urlImprimir.replaceAll("&imprimir=si", "");
                if (urlImprimir.indexOf("imprimir") == -1) {
                    urlImprimir += "&imprimir=si";
                }
            }
            
            inxtecString = request.getParameter("inxtec");
            logger.debug("instruccio: " + inxtecString);
            try {
                inxtec = new Integer(inxtecString).intValue();
            } catch (Exception e) {
            }

//			userGeneric = (String)request.getParameter("userGeneric");
            if (userGeneric == null) {
                userGeneric = "";
            }
            cataleg = (String) request.getParameter("cataleg");
            if ((cataleg == null) || cataleg.equals("null") || cataleg.equals("no")) {
                isCataleg = false;
            } else {
                isCataleg = true;
            }


            /*	if ((usuari == null) && userGeneric.equals("") && Configuracio.sso.equals("si") && !isCataleg){
             logger.debug("Redirect SSO");
             response.setHeader( "Osso-Paranoid", "true" );
             response.sendError(499, "Oracle SSO");					   
             } else {*/
            if (usuari != null) {
                usuariNomComplet = (String) session.getAttribute("usuariNomComplet");
                if (usuariNomComplet == null) {
                    usuariNomComplet = UtilsCercador.getNomComplet(usuari);
                    session.setAttribute("usuariNomComplet", usuariNomComplet);
                }
            }
            logger.debug("doPost -> in");
            parameters = new Hashtable();
            // HttpSession session = request.getSession();
            // usuari = (String)session.getAttribute("usuari");

            logger.debug("usuari: " + usuari);
            
            myConnection = UtilsCercador.getConnectionFromPool();
            if (allLevels == null) {
                logger.debug("Reloading duc info...");
                allLevels = (ArrayList) session.getAttribute("levels");
                allAreas = (Hashtable) session.getAttribute("areas");
                if (allLevels == null) {
                    loadDUCinfo(myConnection);
                }
            }
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=utf-8");
            
            queryGenerator = new QueryGenerator();
            resultGenerator = new ResultGenerator(myConnection);
            
            ordenacio = request.getParameter("ordenacio");
            if (ordenacio == null) {
                ordenacio = "";
            }
            logger.debug("ordenacio: " + ordenacio);
            //TODO Permetre canviar l'ordre d'ordenaci�.
//			direccio = request.getParameter ("direccio");
//			if (!"false".equals(direccio))
            direccio = "true";
            
            novaCerca = request.getParameter("novaCerca");
            logger.debug("novaCerca: " + novaCerca);
            
            textCerca = request.getParameter("textCerca");

            if (textCerca != null && !"".equals(textCerca)) {
                // textCerca = new
                // String(request.getParameter("textCerca").getBytes(),
                // "UTF-8");
            } else {
                textCerca = "";
            }

            logger.debug("textCerca: " + textCerca);

            // If nobody clicked in the text field
            if (textCerca.equals(XMLCollection.getProperty("cerca.cercaSimpleTest.textInicial", sLang))) {
                textCerca = "";
            }
            session.setAttribute("textCerca", textCerca);

            // organizer sheet id
            sheetId = request.getParameter("sheetId");
            
            textCercaHidden = request.getParameter("textCercaHidden");
            logger.debug("textCercaHidden: " + textCercaHidden);
            
            if (novaCerca != null && novaCerca.equals("si")) {
                if ((textCerca != null) && (!textCerca.equals(""))) {
                    QueryLogger.saveQueryInfo(myConnection, textCerca, tipus);
                }
            } else {
                if ((textCercaHidden != null) && !textCercaHidden.equals("") && ((textCerca == null) || textCerca.equals(""))) {
                    textCerca = textCercaHidden;
                    // textCerca = new
                    // String(request.getParameter("textCerca").getBytes(),
                    // "UTF-8");
                }
            }
            logger.debug("Text cerca --> " + textCerca);
            
            parameters = getParametersFromRequest(request);
            
            nivellString = request.getParameter("nivell");
            logger.debug("nivellString: " + nivellString);
            try {
                nivell = new Integer(nivellString).intValue();
            } catch (Exception e) {
            }
            
            logger.debug("Generating query");
            
            PrintWriter out = response.getWriter();
            
            if (isAgrega) {
                logger.debug("...Asking to AGREGA...");
                if (AgregaInterface.estasActivo()) {
                    hitsAgrega = AgregaInterface.askAgrega(queryGenerator.getQueryAgrega(textCerca, parameters));
                } else {
                    logger.error("Agrega is not responding.");
                    hitsAgrega = new ArrayList();
                }
                
                resultGenerator.generateAgregaHTML(hitsAgrega, allLevels, allAreas, tipus, nivell, textCerca, sheetId, parameters, ordenacio, usuari, userGeneric, usuariNomComplet, isCataleg, imprimir, urlImprimir, out, sLang);
                session.setAttribute("agregaResults", hitsAgrega);
            } else {
                totalBq = queryGenerator.getQuery(textCerca, parameters);
                
                if (totalBq.toString().equals("")) {
                    logger.debug("match all docs...");
                    
                    QueryParser myQueryParser = new QueryParser("bugSort", new EducacioAnalyzer());
                    totalBq = myQueryParser.parse("(+bugSort:1)");
                }
                logger.info("Query: " + totalBq.toString());
                
                try {
                    indexPrincipal.maxDoc();
                } catch (Exception e) {
                    logger.error("[noMaxDoc] " + e);
                    indexActual = indexActual.replaceAll("/tmp", "");
                    if (UtilsCercador.indexIsOk(indexActual + "/tmp")) {
                        UtilsCercador.recoverFromTmp(indexActual);
                    } else {
                        UtilsCercador.recoverFromBackup(indexActual);
                    }
                    myFSdirectory = FSDirectory.getDirectory(indexActual);
                    indexPrincipal = new IndexSearcher(myFSdirectory);
                    UtilsCercador.setIndexActualDB(indexActual);
                }
                
                try {
                    hitsTotal = indexPrincipal.search(totalBq);
                } catch (Exception e) {
                    logger.error(e);
                    updateIndex();
                    hitsTotal = indexPrincipal.search(totalBq);
                }
                
                bq = (Query) totalBq.clone();
                
                logger.debug("Query -> Format recurs");
                String filtreRecurs = (String) parameters.get("filtreRecurs");
                if ((filtreRecurs != null) && !filtreRecurs.trim().equals("") && !filtreRecurs.trim().equals("null")) {
                    bq = queryGenerator.getQueryFiltreRecurs(filtreRecurs, bq);
                }
                
                logger.debug("Waiting semafor...");
                logger.info("Index actual -> " + indexActual);
                logger.debug("Semafor in...");
                if (ordenacio.equals("") || ordenacio.equals("defecte")) {
                    try {
                        hitsA = indexPrincipal.search(bq);
                    } catch (Exception e) {
                        logger.error(e);
                        updateIndex();
                        hitsA = indexPrincipal.search(bq);
                    }
                } else if (ordenacio.equals("puntuacio")) {
                    try {
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("puntuacio", SortField.FLOAT, "true".equals(direccio))));
                    } catch (Exception e) {
                        updateIndex();
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("puntuacio", SortField.FLOAT, "true".equals(direccio))));
                    }
                } else if (ordenacio.equals("comentaris")) {
                    try {
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("numComentaris", "true".equals(direccio))));
                    } catch (Exception e) {
                        updateIndex();
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("numComentaris", "true".equals(direccio))));
                    }
                } else if (ordenacio.equals("data")) {
                    try {
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("dataPublicacio", "true".equals(direccio))));
                    } catch (Exception e) {
                        updateIndex();
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("dataPublicacio", "true".equals(direccio))));
                    }
                    
                } else if (ordenacio.equals("titol")) {
                    try {
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("titol", !"true".equals(direccio))));
                    } catch (Exception e) {
                        updateIndex();
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("titol", !"true".equals(direccio))));
                    }
                    
                } else if (ordenacio.equals("visites")) {
                    try {
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("numVisites", "true".equals(direccio))));
                    } catch (Exception e) {
                        updateIndex();
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("numVisites", "true".equals(direccio))));
                    }
                    
                } else if (ordenacio.equals("alfabeticament")) {
                    try {
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("titol", !"true".equals(direccio))));
                    } catch (Exception e) {
                        updateIndex();
                        hitsA = indexPrincipal.search(bq, new Sort(new SortField("titol", !"true".equals(direccio))));
                    }
                    
                }
                logger.debug("Hits length..." + hitsA.length());
                logger.debug("Generating html..");
                
                resultGenerator.generateHTML(hitsTotal, hitsA, indexPrincipal, totalBq, allLevels, allAreas, tipus, nivell, textCerca,
                        sheetId, parameters, ordenacio, "true".equals(direccio), usuari, userGeneric, usuariNomComplet,
                        isCataleg, imprimir, urlImprimir, out, sLang, inxtec, UtilsCercador.getLastUrl(request));
                
            }
            // String xmlSortida = resultGenerator.generateXML (hitsA, tipus, nivell, textCerca);
            out.flush();
            out.close();
            
            logger.debug("Writing..");
            
        } catch (FileNotFoundException e) {
            logger.error(e);
            logger.error("Index -> " + indexActual);
            try {
                indexActual = indexActual.replaceAll("/tmp", "");
                if (UtilsCercador.indexIsOk(indexActual + "/tmp")) {
                    UtilsCercador.recoverFromTmp(indexActual);
                } else {
                    UtilsCercador.recoverFromBackup(indexActual);
                }
                UtilsCercador.setIndexActualDB(indexActual);
                /*if (indexActual.equals(Configuracio.indexDir)) {
                 UtilsCercador.setIndexActualDB(Configuracio.indexDir2);	
                 } else {
                 UtilsCercador.setIndexActualDB(Configuracio.indexDir);
                 } */
                updateIndex();
            } catch (Exception ex) {
            }
        } catch (Exception e) {
            logger.error(e);
            logger.error("Index -> " + indexActual);
        } finally {
            try {
                logger.debug("Free connection");
                if (!myConnection.isClosed()) {
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
            /*try {
             logger.debug("Free connection");
             // indexPrincipal.close();		  
             } catch (Exception e) {
             logger.error(e);
             }		  
             */
        }
        logger.debug("doPost -> out");
    }
    
}
