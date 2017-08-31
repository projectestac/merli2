package simpple.xtec.web.util;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public class ResultGenerator {

    String contextWebAplicacio = "";
    String urlServidor = "";

    Connection myConnection = null;
    // logger
    static final Logger logger = Logger.getLogger(simpple.xtec.web.util.ResultGenerator.class);

    public ResultGenerator(Connection myConnection) {
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            contextWebAplicacio = Configuracio.contextWebAplicacio;
            urlServidor = "http://" + Configuracio.servidorWeb;
            if (!Configuracio.portWeb.equals("")) {
                urlServidor += ":" + Configuracio.portWeb;
            }
            this.myConnection = myConnection;
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public int getNumComentaris(Connection myConnection, int idRecurs) {
        // Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int numComentaris = 0;
        try {

            sql = "SELECT count(*) from comentaris where suspens=0 and recurs_id=" + idRecurs;
            stmt = myConnection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
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
                /*  if (myConnection != null) {
                 myConnection.close();
                 } */
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return numComentaris;
    }

    public float getPuntuacioMitja(Connection myConnection, String idRecurs) {
        // Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        float puntuacioMitja = (float) 0.0;

        try {

            sql = "select avg(puntuacio) from comentari where recurs_id=" + idRecurs;
            stmt = myConnection.createStatement();
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                puntuacioMitja = rs.getFloat(1);
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
                /*  if (myConnection != null) {
                 myConnection.close();
                 } */
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return puntuacioMitja;
    }

    public String sanitizeXML(String original) {
        String modificada = original;
        try {
            modificada = modificada.replaceAll("[^a-zA-Z0-9?¿!().,:;+-áéíóúèòïüçñÁÉÍÓÚÇÑÀÈÒ'\"]", " ");
        } catch (Exception e) {
            logger.error(e);
        }
        return modificada;
    }

    public String generateRSS(Connection myConnection, Hits hits, String query, String nivellEducatiu, String areaCurricular) {


        String lang = "";

        String outputRSS = "";
        int docInicial = 0;
        int docFinal = 99;
        int maxItemsRss = 15;
        try {
            try {
                maxItemsRss = new Integer(Configuracio.maxItemsRss).intValue();
            } catch (Exception e) {
            }

            outputRSS = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
            outputRSS += " <rss version=\"2.0\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n";
            outputRSS += "  <channel>\n";
            if ((query != null) && !query.trim().equals("")) {
                outputRSS += "    <title>Merlí - Resultats cerca - " + query + "</title>\n";
            } else {
                String nivellEducatiuString = UtilsCercador.getTermDuc(myConnection, nivellEducatiu, lang);
                String areaCurricularString = UtilsCercador.getTermDuc(myConnection, areaCurricular, lang);
                outputRSS += "    <title>Merlí - Resultats cerca - " + nivellEducatiuString + " / " + areaCurricularString + "</title>\n";
            }

            outputRSS += "    <link>http://www.xtec.cat/</link>\n";
            outputRSS += "    <description>RSS de cerca</description>\n";
            outputRSS += "    <language>ca</language>\n";

            if (hits.length() < docFinal) {
                docFinal = hits.length();
            }

            if (docFinal >= maxItemsRss) {
                docFinal = maxItemsRss;
            }

            for (int i = docInicial; i < docFinal; i++) {
                Document currentDoc = (Document) hits.doc(i);
                outputRSS += "      <item>\n";
                outputRSS += "        <title>" + sanitizeXML(currentDoc.get("titol")) + "</title>\n";
                outputRSS += "        <link>" + sanitizeXML(currentDoc.get("url")) + "</link>\n";
                outputRSS += "        <description>" + sanitizeXML(currentDoc.get("descripcio")) + "</description>\n";
                outputRSS += "        <dc:date>" + currentDoc.get("dataPublicacio") + "</dc:date>\n";
                outputRSS += "     </item>\n";
            }

            outputRSS += "  </channel>\n";
            outputRSS += " </rss>\n";
        } catch (Exception e) {
            logger.error(e);
            logger.error(outputRSS);
        }
        return outputRSS;
    }

    public String generateXML(Hits hits, String tipus, int nivell, String query) {
        String outputXML = "";
        String baseUrl = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio;

        baseUrl += "/cerca/fitxaRecurs.jsp";
        int num_resultats = 0;
        int longitud_descripcio = 0;
        boolean nextPage = true;
        boolean previousPage = false;
        int docInicial = 0;
        int docFinal = 0;

        try {
            // myConnection = DriverManager.getConnection(Configuracio.cadenaConnexioBD);

            outputXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
            outputXML += "<!DOCTYPE resultatsCerca SYSTEM \"" + urlServidor + "/" + contextWebAplicacio + "/resultats.dtd\">\n";
            // HTML
            outputXML += "<?xml-stylesheet type=\"text/xsl\" href=\"" + urlServidor + "/" + contextWebAplicacio + "/resultats.xsl\"?>\n";
            outputXML += "<resultatsCerca>\n";
            outputXML += "<form>\n";
            outputXML += "   <tipus>" + tipus + "</tipus>\n";
            outputXML += "   <query>" + query + "</query>\n";
            outputXML += "   <servletURL>/" + Configuracio.contextWebAplicacio + "/ServletCerca</servletURL>\n";
            outputXML += "</form>\n";
            outputXML += "<resultats numResultats=\"" + hits.length() + "\">\n";

            /* if (i >= nivell * num_resultats && i < (nivell + 1) * num_resultats) {
             Document currentDoc = (Document)hits.doc(i);
             */
            if (ConfiguracioFragments.isVoid()) {
                ConfiguracioFragments.loadConfiguracio(myConnection, "edu365");
                ConfiguracioFragments.loadConfiguracio(myConnection, "xtec");
                ConfiguracioFragments.loadConfiguracio(myConnection, "altres");
            }
            if (tipus.equals("simple")) {
                num_resultats = ConfiguracioFragments.numResultatsPerPagina[0];
                longitud_descripcio = ConfiguracioFragments.longitudDescripcio[0];
            }
            else if (tipus.equals("embedded")){
                num_resultats = 5;
                longitud_descripcio = 20;
                // num_resultats = ConfiguracioFragments.numResultatsPerPagina[2];
                //longitud_descripcio = ConfiguracioFragments.longitudDescripcio[2];
            }
            else {
                num_resultats = ConfiguracioFragments.numResultatsPerPagina[1];
                longitud_descripcio = ConfiguracioFragments.longitudDescripcio[1];
            }
            docInicial = nivell * num_resultats;
            if (nivell > 0) {
                previousPage = true;
            }
            docFinal = (nivell + 1) * num_resultats;
            if (docFinal > hits.length()) {
                docFinal = hits.length();
                nextPage = false;
            }
            logger.debug("Doc inicial --> " + docInicial);
            logger.debug("Doc final --> " + docFinal);
            // for (int i = 0; i <  hits.length(); i++) {
            for (int i = docInicial; i < docFinal; i++) {
                Document currentDoc = (Document) hits.doc(i);
                String titol = currentDoc.get("titol");
                String autor = currentDoc.get("autor");
                String descripcio = currentDoc.get("descripcio");
                String docUrl = currentDoc.get("url");
                String idRecurs = currentDoc.get("idRecurs");
                String data = currentDoc.get("euncl@lom@euncl@lifeCycle@euncl@contribute@euncl@date@euncl@dateTime");
                int numComentaris = (new Integer(currentDoc.get("numComentaris"))).intValue();
                float puntuacioMitja = (new Float(currentDoc.get("puntuacio"))).floatValue();
                //		int numComentaris = getNumComentaris(myConnection, idRecurs);
                //		float puntuacioMitja = getPuntuacioMitja(myConnection, idRecurs);
                outputXML += " <resultat num=\"" + (i + 1) + "\" numComentaris=\"" + numComentaris + "\" puntuacioMitja=\"" + puntuacioMitja + "\">\n";
                int j = 0;
                outputXML += "    <puntuacio>";
                while (j < puntuacioMitja) {
                    outputXML += "<star/>";
                    j++;
                }
                while (j < 5) {
                    outputXML += "<empty-star/>";
                    j++;
                }
                outputXML += "</puntuacio>";
                outputXML += "    <titol>" + titol + "</titol>\n";
                outputXML += "    <autor>" + autor + "</autor>\n";
                outputXML += "    <descripcio>" + ResultGeneratorUtil.cutDescription(descripcio, longitud_descripcio) + "</descripcio>\n";
                outputXML += "    <data>" + data + "</data>\n";
                if (tipus.equals("simple")) {
                    outputXML += "    <url>" + docUrl + "</url>\n";
                } else {
                    String urlFitxa = baseUrl + "?idRecurs=" + idRecurs;
                    outputXML += "    <url>" + urlFitxa + "</url>\n";
                }
                outputXML += " </resultat>\n";
            }
            if (previousPage) {
                outputXML += " <previousPage nivell=\"" + (nivell - 1) + "\"/>\n";
            }
            if (nextPage) {
                outputXML += " <nextPage nivell=\"" + (nivell + 1) + "\"/>\n";
            }
            outputXML += "</resultats>\n";
            outputXML += "</resultatsCerca>\n";
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return outputXML;
    }

    public String generateHTML(Hits hitsTotal, Hits hits, IndexSearcher indexPrincipal, Query totalQuery,
                               ArrayList allLevels, Hashtable allAreas, String tipus, int nivell, String query, String sheetId,
                               Hashtable parameters, String ordenacio, boolean direccio, String usuari, String userGeneric,
                               String usuariNomComplet, boolean isCataleg, String imprimir, String urlImprimir, PrintWriter out,
                               String lang, int incrusXTEC, String lastURL) {
        String outputHTML = "";
        String baseUrl = "";
        String fitxa = "";
        int num_resultats = 0;
        int longitud_descripcio = 0;
        boolean nextPage = true;
        boolean previousPage = false;
        int docInicial = 0;
        int docFinal = 0;
        int i = 0;
        String nivellEducatiu = "";
        String areaCurricular = "";
        String autorCerca = "";
        String idiomaCerca = "";
        String destinatariCerca = "";
        String tipusRecurs = "";
        String formatRecurs = "";
        String keywordsCerca = "";
        String editorialCerca = "";
        String llicenciaCerca = "";
        String dataIniciPublicacio = "";
        String dataFinalPublicacio = "";
        String filtreRecurs = "";
        String recursOnline = "";
        String recursFisic = "";
        String unitatCerca = "";

        String urlRss = "";
        String urlLink = "";
        String titolRss="";
        String titolurlLink="";
        String hiddenFields = "";
        String cicle = "";
        String[] ducContent = null;
        try {
            nivellEducatiu = (String) parameters.get("nivellEducatiu");
            areaCurricular = (String) parameters.get("areaCurricular");
            cicle = (String) parameters.get("cicle");
            ducContent = (String[]) parameters.get("ducContent");

            autorCerca = (String) parameters.get("autorCerca");
            idiomaCerca = (String) parameters.get("idiomaCerca");
            destinatariCerca = (String) parameters.get("destinatariCerca");
            tipusRecurs = (String) parameters.get("tipusRecurs");
            formatRecurs = (String) parameters.get("formatRecurs");
            keywordsCerca = (String) parameters.get("keywords");
            editorialCerca = (String) parameters.get("editorialCerca");
            llicenciaCerca = (String) parameters.get("llicenciaCerca");
            dataIniciPublicacio = (String) parameters.get("dataIniciPublicacio");
            dataFinalPublicacio = (String) parameters.get("dataFinalPublicacio");

            filtreRecurs = (String) parameters.get("filtreRecurs");
            recursOnline = (String) parameters.get("recursOnline");
            recursFisic = (String) parameters.get("recursFisic");
            unitatCerca = (String) parameters.get("unitatCerca");

            baseUrl = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/";

            urlRss = baseUrl + "CercaRSS?textCerca=" + query;
            urlLink = baseUrl + "ServletCerca?textCerca=" + query;
            if (nivellEducatiu != null) {
                urlRss += "&nivell_educatiu=" + nivellEducatiu;
                urlLink += "&nivell_educatiu=" + nivellEducatiu;
            }
            if (areaCurricular != null) {
                urlRss += "&area_curricular=" + areaCurricular;
                urlLink += "&area_curricular=" + areaCurricular;
            }
            if (filtreRecurs != null) {
                urlRss += "&filtreRecurs=" + filtreRecurs;
                urlLink += "&filtreRecurs=" + filtreRecurs;
            }

            if ((!tipus.equals("simple")) || (!tipus.equals("embedded"))) {
                if (cicle != null) {
                    urlRss += "&cicle=" + cicle;
                    urlLink += "&cicle=" + cicle;
                    hiddenFields += " <input type=\"hidden\" name=\"cicle\" value=\"" + cicle + "\"/>\n";
                }
                if (ducContent != null) {
                    int j = 0;
                    String value = "";
                    while (j < ducContent.length) {
                        value += ducContent[j];
                        j++;
                        if (j < ducContent.length) {
                            value += ",";
                        }
                    }
                    hiddenFields += "   <input type=\"hidden\" name=\"ducContent\" id=\"ducContentIdentificador\" value=\"" + value + "\" />\n";
                }
                if (autorCerca != null) {
                    urlRss += "&autorCerca=" + autorCerca;
                    urlLink += "&autorCerca=" + autorCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"autorCerca\" value=\"" + autorCerca + "\"/>\n";
                }
                if (idiomaCerca != null) {
                    urlRss += "&idiomaCerca=" + idiomaCerca;
                    urlLink += "&idiomaCerca=" + idiomaCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"idiomaCerca\" value=\"" + idiomaCerca + "\"/>\n";
                }
                if (destinatariCerca != null) {
                    urlRss += "&destinatariCerca=" + destinatariCerca;
                    urlLink += "&destinatariCerca=" + destinatariCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"destinatariCerca\" value=\"" + destinatariCerca + "\"/>\n";
                }
                if (tipusRecurs != null) {
                    urlRss += "&tipusRecurs=" + tipusRecurs;
                    urlLink += "&tipusRecurs=" + tipusRecurs;
                    hiddenFields += " <input type=\"hidden\" name=\"tipusRecurs\" value=\"" + tipusRecurs + "\"/>\n";
                }
                if (recursOnline != null) {
                    urlRss += "&recursOnline=" + recursOnline;
                    urlLink += "&recursOnline=" + recursOnline;
                    hiddenFields += " <input type=\"hidden\" name=\"recursOnline\" value=\"" + recursOnline + "\"/>\n";
                }
                if (recursFisic != null) {
                    urlRss += "&recursFisic=" + recursFisic;
                    urlLink += "&recursFisic=" + recursFisic;
                    hiddenFields += " <input type=\"hidden\" name=\"recursFisic\" value=\"" + recursFisic + "\"/>\n";
                }
                if (unitatCerca != null) {
                    urlRss += "&unitatCerca=" + unitatCerca;
                    urlLink += "&unitatCerca=" + unitatCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"unitatCerca\" value=\"" + unitatCerca + "\"/>\n";
                }
                if (keywordsCerca != null) {
                    urlRss += "&keywords=" + keywordsCerca;
                    urlLink += "&keywords=" + keywordsCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"keywords\" value=\"" + keywordsCerca + "\"/>\n";
                }
                if (editorialCerca != null) {
                    urlRss += "&editorialCerca=" + editorialCerca;
                    urlLink += "&editorialCerca=" + editorialCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"editorialCerca\" value=\"" + editorialCerca + "\"/>\n";
                }
                if (llicenciaCerca != null) {
                    urlRss += "&llicenciaCerca=" + llicenciaCerca;
                    urlLink += "&llicenciaCerca=" + llicenciaCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"llicenciaCerca\" value=\"" + llicenciaCerca + "\"/>\n";
                }
                if (dataIniciPublicacio != null) {
                    urlRss += "&dataIniciPublicacio=" + dataIniciPublicacio;
                    urlLink += "&dataIniciPublicacio=" + dataIniciPublicacio;
                    hiddenFields += " <input type=\"hidden\" name=\"dataIniciPublicacio\" value=\"" + dataIniciPublicacio + "\"/>\n";
                }
                if (dataFinalPublicacio != null) {
                    urlRss += "&dataFinalPublicacio=" + dataFinalPublicacio;
                    urlLink += "&dataFinalPublicacio=" + dataFinalPublicacio;
                    hiddenFields += " <input type=\"hidden\" name=\"dataFinalPublicacio\" value=\"" + dataFinalPublicacio + "\"/>\n";
                }
                if (formatRecurs != null) {
                    urlRss += "&formatRecurs=" + formatRecurs;
                    urlLink += "&formatRecurs=" + formatRecurs;
                    hiddenFields += " <input type=\"hidden\" name=\"formatRecurs\" value=\"" + formatRecurs + "\"/>\n";
                }
            }

            if (ConfiguracioFragments.isVoid()) {

                ConfiguracioFragments.loadConfiguracio(myConnection, "edu365");
                ConfiguracioFragments.loadConfiguracio(myConnection, "xtec");
                ConfiguracioFragments.loadConfiguracio(myConnection, "altres");
            }

            if (tipus.equals("simple")) {

                num_resultats = ConfiguracioFragments.numResultatsPerPagina[0];
                longitud_descripcio = ConfiguracioFragments.longitudDescripcio[0];
            }
              else if (tipus.equals("embedded")){
                num_resultats = 5;
                longitud_descripcio = 20;
                //num_resultats = ConfiguracioFragments.numResultatsPerPagina[2];
               //longitud_descripcio = ConfiguracioFragments.longitudDescripcio[2];

            }
            else {
                num_resultats = ConfiguracioFragments.numResultatsPerPagina[1];
                longitud_descripcio = ConfiguracioFragments.longitudDescripcio[1];
            }
            num_resultats = Math.max(1, num_resultats);
            docInicial = nivell * num_resultats;
            if (nivell > 0) {
                previousPage = true;
            }
            docFinal = (nivell + 1) * num_resultats;
            //Descomentat per permetre un única pàgina de resultats.
            if (docFinal > hits.length()) {
                docFinal = hits.length();
                nextPage = false;
            }

            fitxa = baseUrl + "cerca/fitxaRecurs.jsp";
            // Necessari per tooltip
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
            out.println("<html lang=\"ca\">");
            ResultGeneratorUtil.htmlHeader(out, baseUrl, "cerca.resultatsCerca.titol", urlRss, lang);

            out.println("  <script type=\"text/javascript\">");
            //	ArrayList allLevels = UtilsCercador.getAllLevels(myConnection);
            DucObject ducLevel;
            DucObject ducArea;
            ArrayList allAreasArray;
            ResultGeneratorUtil.getDucJSList(allLevels, allAreas, lang, out);

            ResultGeneratorUtil.functionJSchange_area(out, lang);
            ResultGeneratorUtil.functionJScheckLink(out);

            out.println(" </script>");

            out.println("<!--[if IE 6]> ");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + baseUrl + "css/ie6.css\"/>");
            out.println("<![endif]--> ");

            out.println("</head>");
            out.println("<body>");
            out.println("<div id=\"non-footer\">");

            if ((incrusXTEC != ResultGeneratorUtil.SHOW_ONLYRESULTS)  && (incrusXTEC != ResultGeneratorUtil.SHOW_EMBEDDED )) {

                if (imprimir.equals("no")) {
                    //Capçalera del cercador
                    if (incrusXTEC <= ResultGeneratorUtil.SHOW_ALL) {
                        out.println("<div id=\"header\"> ");
                        ResultGeneratorUtil.htmlHeaderCabecera(out, baseUrl, "Merlí", lastURL);
                        int comentarisSuspesos = UtilsCercador.getComentarisSuspesos(usuari);
                        ResultGeneratorUtil.htmlHeaderMenu(usuari, usuariNomComplet, out, baseUrl, comentarisSuspesos, lang);

                    } else {
                        ResultGeneratorUtil.htmlHeaderNoCapcalera(out);
                    }
                    //FI - Capçalera

                    //Cercador
                    out.println("<div id=\"barra_buscador\">");
                    out.println("<div id=\"cercadorOptions\">");
                    out.println("	<form class=\"cercador_xtec\" name=\"cerca\" action=\"/" + Configuracio.contextWebAplicacio + "/ServletCerca\" method=\"POST\">");
                    out.println("      <fieldset>");
                    out.println("     <input type=\"hidden\" name=\"numResultats\" value=\"" + num_resultats + "\"/>");
                    out.println("     <input type=\"hidden\" name=\"descLong\" value=\"" + longitud_descripcio + "\"/>");
                    out.println("     <input type=\"hidden\" name=\"agrega\" value=\"0\"/>");
                    out.println("     <input type=\"hidden\" name=\"sheetId\" value=\"" + sheetId + "\"/>");
                    out.println("     <input type=\"hidden\" name=\"tipus\" value=\"" + tipus + "\"/>");
                    out.println("     <input type=\"hidden\" name=\"nivell\" value=\"0\"/>");
                    out.println("     <input type=\"hidden\" name=\"ordenacio\" value=\"" + ordenacio + "\"/>");
                    out.println("     <input type=\"hidden\" name=\"direccio\" value=\"\"/>");
                    out.println("     <input type=\"hidden\" name=\"novaCerca\" value=\"no\"/>");
                    out.println("     <input type=\"hidden\" name=\"filtreRecurs\" value=\"" + filtreRecurs + "\"/>");
                    out.println("     <input type=\"hidden\" name=\"formatRecurs\" value=\"" + formatRecurs + "\"/>");
                    if (incrusXTEC != ResultGeneratorUtil.SHOW_SEARCH) {
                        out.println("     <input type=\"hidden\" name=\"inxtec\" value=\"" + incrusXTEC + "\"/>");
                    } else {
                        out.println("     <input type=\"hidden\" name=\"inxtec\" value=\"" + ResultGeneratorUtil.SHOW_SEARCHRESULTS + "\"/>");
                    }
                    if (isCataleg) {
                        out.println("     <input type=\"hidden\" name=\"cataleg\" value=\"si\"/>");
                    } else {
                        out.println("     <input type=\"hidden\" name=\"cataleg\" value=\"no\"/>");
                    }
                    out.println("     <input type=\"hidden\" name=\"userGeneric\" value=\"" + userGeneric + "\"/>");
                    out.println("     <input type=\"hidden\" name=\"nomUsuari\" value=\"" + usuari + "\"/>");
                    query = query.replaceAll("\"", "&quot;");
                    out.println("     <input type=\"hidden\" name=\"textCercaHidden\" value=\"" + query + "\"/>");
                    out.println(hiddenFields);

                    out.print("<label for=\"textCerca\" class=\"hide\">Introdu&iuml;u les paraules a cercar</label>");
                    out.print("<input type=\"text\" size=\"20\" name=\"textCerca\" id=\"textCerca\" value=\"" + query + "\"  tabindex=\"1\" alt=\"text cerca\" class=\"textCerca\"/>");
                    out.println("<label for=\"nivell_educatiu\" class=\"hide\">Seleccioneu el nivell educatiu</label>");
                    out.print("<select name=\"nivell_educatiu\" id=\"nivell_educatiu\" onchange=\"change_area()\" tabindex=\"2\" class=\"select\">");
                    out.print("			   <option value=\"-1\">" + XMLCollection.getProperty("cerca.select.nivell", lang) + "</option>");
                    i = 0;
                    while (i < allLevels.size()) {
                        ducLevel = (DucObject) allLevels.get(i);
                        // FIXME: Treure quan FP estigui disponible al DUC
                        //if(ducLevel.getTerm(lang).indexOf("FP")<0) {
                        if (nivellEducatiu.equals("" + ducLevel.id)) {
                            out.print("						<option value=\"" + ducLevel.id + "\" selected>" + ducLevel.getTerm(lang) + "</option>");
                        } else {
                            out.print("						<option value=\"" + ducLevel.id + "\">" + ducLevel.getTerm(lang) + "</option>");
                        }
                        //}
                        i++;
                    }

                    out.print("			</select>");
                    out.println(" <label for=\"area_curricular\"class=\"hide\">Seleccioneu l'&agrave;rea</label>");
                    out.print("					<select name=\"area_curricular\" id=\"area_curricular\"  tabindex=\"3\" class=\"select\" >");
                    out.print("				    	<option value=\"-1\">" + XMLCollection.getProperty("cerca.select.area", lang) + "</option>");

                    logger.debug("Nivell: " + nivellEducatiu);
                    logger.debug("Area: " + areaCurricular);
                    if ((nivellEducatiu != null) && !nivellEducatiu.equals("")) {
                        allAreasArray = (ArrayList) allAreas.get(new Integer(nivellEducatiu));
                        i = 0;
                        if (allAreasArray != null) {
                            while (i < allAreasArray.size()) {
                                ducArea = (DucObject) allAreasArray.get(i);
                                if ((areaCurricular != null) && areaCurricular.equals("" + ducArea.id)) {
                                    out.print("						<option value=\"" + ducArea.id + "\" selected>" + ducArea.getTerm(lang) + "</option>");
                                } else {
                                    out.print("						<option value=\"" + ducArea.id + "\">" + ducArea.getTerm(lang) + "</option>");
                                }
                                i++;
                            }
                        }
                    }

                    out.print("					</select>");
                    //out.println( "<div class=\"buto_cerca\"><a class=\"button\" onclick=\"doSubmit(true);\">"+XMLCollection.getProperty("cerca.directoriInicial.cerca", lang)+"</a></div>");
                    out.println("<button onclick=\"doSubmit();\" class=\"butoMerli small red\">" + XMLCollection.getProperty("cerca.directoriInicial.cerca", lang) + "</button>");

                    if (incrusXTEC > ResultGeneratorUtil.SHOW_ALL) {
                        out.println(" <a href=\"/" + Configuracio.contextWebAplicacio + "/cerca/cercaCompleta.jsp?inxtec=" + ResultGeneratorUtil.SHOW_SEARCHRESULTS + "\">" + XMLCollection.getProperty("cerca.directoriInicial.cercaCompleta", lang) + "</a>");
                    } else if(incrusXTEC == ResultGeneratorUtil.SHOW_EMBEDDED){
                        out.println(" <a href=\"/" + Configuracio.contextWebAplicacio + "/cerca/cercaCompleta.jsp?inxtec=" + ResultGeneratorUtil.SHOW_EMBEDDED + "\">" + XMLCollection.getProperty("cerca.directoriInicial.cercaCompleta", lang) + "</a>");
                    }
                    else {
                        out.println(" <a href=\"/" + Configuracio.contextWebAplicacio + "/cerca/cercaCompleta.jsp\">" + XMLCollection.getProperty("cerca.directoriInicial.cercaCompleta", lang) + "</a>");
                    }
                    out.println("&nbsp; <a href=\"" + XMLCollection.getProperty("url.ajuda", lang) + "\" target=\"_blank\"><img style=\"border:0;\" src=\"" + baseUrl + "imatges/ajuda.png\"/></a>");
                    out.println("   </fieldset>");
                    out.println("   </form>");

                    out.println("</div>");

                    out.println("</div>");
                    out.println("</div>");
                    out.println("");
                    out.println("");
                }
                //Fi-Cercador
            } else {

                out.println("	<form name=\"cerca\" action=\"/" + Configuracio.contextWebAplicacio + "/ServletCerca\" method=\"POST\">");
                out.println("     <input type=\"hidden\" name=\"numResultats\" value=\"" + num_resultats + "\"/>");
                out.println("     <input type=\"hidden\" name=\"descLong\" value=\"" + longitud_descripcio + "\"/>");
                out.println("     <input type=\"hidden\" name=\"agrega\" value=\"0\"/>");
                out.println("     <input type=\"hidden\" name=\"filtreRecurs\" value=\"" + filtreRecurs + "\"/>");
                out.println("     <input type=\"hidden\" name=\"sheetId\" value=\"" + sheetId + "\"/>");
                out.println("     <input type=\"hidden\" name=\"tipus\" value=\"" + tipus + "\"/>");
                out.println("     <input type=\"hidden\" name=\"nivell\" value=\"0\"/>");
                out.println("     <input type=\"hidden\" name=\"ordenacio\" value=\"" + ordenacio + "\"/>");
                out.println("     <input type=\"hidden\" name=\"direccio\" value=\"\"/>");
                out.println("     <input type=\"hidden\" name=\"novaCerca\" value=\"no\"/>");
                out.println("     <input type=\"hidden\" name=\"formatRecurs\" value=\"" + formatRecurs + "\"/>");
                if (incrusXTEC != ResultGeneratorUtil.SHOW_SEARCH) {
                    out.println("     <input type=\"hidden\" name=\"inxtec\" value=\"" + incrusXTEC + "\"/>");
                } else {
                    out.println("     <input type=\"hidden\" name=\"inxtec\" value=\"" + ResultGeneratorUtil.SHOW_SEARCHRESULTS + "\"/>");
                }

                if (isCataleg) {
                    out.println("     <input type=\"hidden\" name=\"cataleg\" value=\"si\"/>");
                } else {
                    out.println("     <input type=\"hidden\" name=\"cataleg\" value=\"no\"/>");
                }
                out.println("     <input type=\"hidden\" name=\"userGeneric\" value=\"" + userGeneric + "\"/>");
                out.println("     <input type=\"hidden\" name=\"nomUsuari\" value=\"" + usuari + "\"/>");
                query = query.replaceAll("\"", "&quot;");
                out.println("     <input type=\"hidden\" name=\"textCercaHidden\" value=\"" + query + "\"/>");
                out.println(hiddenFields);
                out.print("<input type=\"hidden\" name=\"textCerca\" id=\"textCerca\" value=\"" + query + "\"/>");
                out.print("<input type=\"hidden\" name=\"nivell_educatiu\" value=\"" + nivellEducatiu + "\">");
                out.print("<input type=\"hidden\" name=\"area_curricular\" value=\"" + areaCurricular + "\">");

                out.println("   </form>");
            }

            if (incrusXTEC != ResultGeneratorUtil.SHOW_SEARCH) {
                //CapçaleraResultats
                //	Resultats totals
                if (incrusXTEC != ResultGeneratorUtil.SHOW_EMBEDDED) {
                    out.println(ResultGeneratorUtil.htmlBarraTotalResultats(hits, query, imprimir, lang, docInicial, docFinal));

                //Camps d'ordenació de resultats
                if (hits.length() > 0) {
                    String aux = "";//outputHTML;
//		  if (aux.indexOf("<span class=\"filtrat\"")>0){
//			  aux  =aux.substring(aux.indexOf("<span class=\"filtrat\""), aux.indexOf("</span>",aux.indexOf("<span class=\"filtrat\""))+"</span>".length());
//			  if (aux.indexOf("title")>0){
//				  aux =
//					  "<span class=\"filtrat\">"+
//					  aux.substring(aux.indexOf("title=\"")+"title=\"".length(), aux.indexOf("\"",aux.indexOf("title=\"")+"title=\"".length()))+
//					  " "+
//					  aux.substring(aux.indexOf("("), aux.indexOf(")")+1)+
//					  "</span>";
//			  }
//		  }else{
//			  aux = "";
//		  }

                        ResultGeneratorUtil.htmlOrderBy(ordenacio, direccio, lang, aux, out);

                    //Resum de resultats

                      ResultGeneratorUtil.htmlResumResultats(hitsTotal, indexPrincipal, totalQuery, lastURL, lang, out);
                  }
                }
                //Fi camps ordenació resultats

                //Resultats
                out.println("<div id=\"resultats\">");

                if (docFinal > 0) {
                    out.println("  <div id=\"resultats_left\">");
                } else {
                    out.println("  <div id=\"resultats_left_empty\">");
                }

                ResultGeneratorUtil.htmlPrintResults(hits, sheetId, usuari, userGeneric, out, lang, incrusXTEC, fitxa, longitud_descripcio, docInicial, docFinal, filtreRecurs);

                out.println("</div>");

                if (imprimir.equals("no")) {
                    out.println("<div class=\"clear\"></div>");

                    out.println("</div>");

                    if (hits.length() > 0) {
                        out.println("<div id=\"bottom\">");
                        out.println("<div class=\"bottom_left_div\">");
                        out.println("</div>");
                        out.println("<div id=\"bottom_left\">");
                        int numPagines = hits.length() / num_resultats;
                        if ((hits.length() % num_resultats) != 0) {
                            numPagines++;
                        }
                        logger.debug("num pagines: " + numPagines);
                        int j = 0;
                        int top = 10;
                        if (numPagines > 1) {
                            out.println(XMLCollection.getProperty("cerca.resultatsCerca.paginaResultats", lang) + "    ");
                            j = nivell - 4;
                            if (j <= 0) {
                                j = 0;
                            } else {
                                out.println("...");
                            }
                            top = j + 10;
                            if (top > numPagines) {
                                top = numPagines;
                            }
                            while (j < top) {
                                if (j != nivell) {
                                    //   out.println( "<a href=\"javascript:goToNivell('" + j + "')\">" +  (j + 1) + "</a>&nbsp;";
                                    out.println("<a href=\"javascript:goToNivell('" + j + "')\">" + (j + 1) + "</a>&nbsp;&nbsp;");
                                } else {
                                    // out.println( "" + (j + 1) + "&nbsp;";
                                    out.println("<span>" + (j + 1) + "</span>&nbsp;&nbsp;");
                                }
                                j++;
                            }
                            if (j != numPagines) {
                                out.println("...");
                            }
                        }
                        titolRss = XMLCollection.getProperty("cerca.resultatsCerca.subscriureCerca", lang);
                        titolurlLink = XMLCollection.getProperty("cerca.resultatsCerca.subscriurePermalink", lang);
                        out.println("</div>");
                        if (incrusXTEC != ResultGeneratorUtil.SHOW_EMBEDDED) {
                            out.println("<div id=\"bottom_right\">");
                            out.println("<a  style=\"cursor: pointer;\" onclick=\"copia_portapapeles('" + urlLink + "')\" title=\"" + titolurlLink + "\"><i class=\"fa fa-clipboard fa-2x\" aria-hidden=\"true\"></i></a>");
                            out.println("<a href=\"" + urlRss + "\" title=\"" + titolRss + "\"><i class=\"fa fa-rss fa-2x\" aria-hidden=\"true\"></i></a>");
                            out.println("</div>");
                        }
                        out.println("<div class=\"clear\"></div>");
                        out.println("</div>");
                    }
                    out.println("</div>");
                    if (incrusXTEC <= ResultGeneratorUtil.SHOW_ALL) {
                        // FOOTER
                        ResultGeneratorUtil.htmlFooterCercador(out, lang);
                    }
                    //Fi Footer
                    out.println("<!-- GOOGLE ANALYTICS -->");
                    out.println("<script type=\"text/javascript\">");
                    out.println("var gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");");
                    out.println("document.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));");
                    out.println("</script>");
                    out.println("<script type=\"text/javascript\">");
                    out.println("try {");
                    out.println("var pageTracker = _gat._getTracker(\"UA-6935294-1\");");
                    out.println("pageTracker._trackPageview();");
                    out.println("} catch(err) {}</script>");
                    out.println("<!-- END -->");
                }
            }
//Fi - Resultats
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return "";
    }

    public String generateAgregaHTML(List hits, ArrayList allLevels, Hashtable allAreas, String tipus, int nivell, String query, String sheetId, Hashtable parameters, String ordenacio, String usuari, String userGeneric, String usuariNomComplet, boolean isCataleg, String imprimir, String urlImprimir, PrintWriter out, String lang) {
        String outputHTML = "";
        String baseUrl = "";
        String fitxa = "";
        int num_resultats = 0;
        int longitud_descripcio = 0;
        boolean nextPage = true;
        boolean previousPage = false;
        int docInicial = 0;
        int docFinal = 0;
        int i = 0;
        String nivellEducatiu = "";
        String areaCurricular = "";
        String autorCerca = "";
        String idiomaCerca = "";
        String dretsReproduccioCerca = "";
        String destinatariCerca = "";
        String tipusRecurs = "";
        String formatRecurs = "";
        String keywordsCerca = "";
        String llicenciaCerca = "";
        String editorialCerca = "";
        String dataIniciPublicacio = "";
        String dataFinalPublicacio = "";
        String dataIniciCatalogacio = "";
        String dataFinalCatalogacio = "";

        String ambit = "";
        String duradaMinima = "";
        String duradaMaxima = "";

        String urlRss = "";
        String hiddenFields = "";
        String cicle = "";
        String[] ducContent = null;

        String inxtec;
        try {
            nivellEducatiu = (String) parameters.get("nivellEducatiu");
            areaCurricular = (String) parameters.get("areaCurricular");
            cicle = (String) parameters.get("cicle");
            ducContent = (String[]) parameters.get("ducContent");

            autorCerca = (String) parameters.get("autorCerca");
            idiomaCerca = (String) parameters.get("idiomaCerca");
            dretsReproduccioCerca = (String) parameters.get("dretsReproduccioCerca");
            destinatariCerca = (String) parameters.get("destinatariCerca");
            tipusRecurs = (String) parameters.get("tipusRecurs");
            formatRecurs = (String) parameters.get("formatRecurs");
            keywordsCerca = (String) parameters.get("keywords");
            llicenciaCerca = (String) parameters.get("llicenciaCerca");
            editorialCerca = (String) parameters.get("editorialCerca");
            dataIniciPublicacio = (String) parameters.get("dataIniciPublicacio");
            dataFinalPublicacio = (String) parameters.get("dataFinalPublicacio");
            dataIniciCatalogacio = (String) parameters.get("dataIniciCatalogacio");
            dataFinalCatalogacio = (String) parameters.get("dataFinalCatalogacio");
            ambit = (String) parameters.get("ambit");
            duradaMinima = (String) parameters.get("duradaMinima");
            duradaMaxima = (String) parameters.get("duradaMaxima");

            inxtec = (String) parameters.get("inxtec");

            baseUrl = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/";

            urlRss = baseUrl + "CercaRSS?textCerca=" + query;
            if (nivellEducatiu != null) {
                urlRss += "&nivell_educatiu=" + nivellEducatiu;
            }
            if (areaCurricular != null) {
                urlRss += "&area_curricular=" + areaCurricular;
            }

            if (!tipus.equals("simple")) {

                if (ducContent != null) {
                    //	     urlRss += "&cicle=" + cicle;
                    int j = 0;
                    String value = "";
                    while (j < ducContent.length) {
                        value += ducContent[j];
                        //	  hiddenFields += "   <input type=\"checkbox\" style=\"visibility:hidden\" id=\"ducContent\" name=\"ducContent\" value=\"" + ducContent[j] + "\" />\n";
                        j++;
                        if (j < ducContent.length) {
                            value += ",";
                        }
                    }
                    hiddenFields += "   <input type=\"hidden\" name=\"ducContent\" id=\"ducContentIdentificador\" value=\"" + value + "\" />\n";
                }
                if (autorCerca != null) {
                    urlRss += "&autorCerca=" + autorCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"autorCerca\" value=\"" + autorCerca + "\"/>\n";
                }
                if (cicle != null) {
                    urlRss += "&cicle=" + cicle;
                    hiddenFields += " <input type=\"hidden\" name=\"cicle\" value=\"" + cicle + "\"/>\n";
                }
                if (idiomaCerca != null) {
                    urlRss += "&idiomaCerca=" + idiomaCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"idiomaCerca\" value=\"" + idiomaCerca + "\"/>\n";
                }
                if (dretsReproduccioCerca != null) {
                    urlRss += "&dretsReproduccioCerca=" + dretsReproduccioCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"dretsReproduccioCerca\" value=\"" + dretsReproduccioCerca + "\"/>\n";
                }
                if (destinatariCerca != null) {
                    urlRss += "&destinatariCerca=" + destinatariCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"destinatariCerca\" value=\"" + destinatariCerca + "\"/>\n";
                }
                if (tipusRecurs != null) {
                    urlRss += "&tipusRecurs=" + tipusRecurs;
                    hiddenFields += " <input type=\"hidden\" name=\"tipusRecurs\" value=\"" + tipusRecurs + "\"/>\n";
                }
                if (formatRecurs != null) {
                    urlRss += "&formatRecurs=" + formatRecurs;
                    hiddenFields += " <input type=\"hidden\" name=\"formatRecurs\" value=\"" + formatRecurs + "\"/>\n";
                }

                if (keywordsCerca != null) {
                    urlRss += "&keywordsCerca=" + keywordsCerca;
                }
                if (llicenciaCerca != null) {
                    urlRss += "&llicenciaCerca=" + llicenciaCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"llicenciaCerca\" value=\"" + llicenciaCerca + "\"/>\n";
                }
                if (ambit != null) {
                    urlRss += "&editorialCerca=" + editorialCerca;
                    hiddenFields += " <input type=\"hidden\" name=\"editorialCerca\" value=\"" + editorialCerca + "\"/>\n";
                }
                if (dataIniciPublicacio != null) {
                    urlRss += "&dataIniciPublicacio=" + dataIniciPublicacio;
                    hiddenFields += " <input type=\"hidden\" name=\"dataIniciPublicacio\" value=\"" + dataIniciPublicacio + "\"/>\n";
                }
                if (dataFinalPublicacio != null) {
                    urlRss += "&dataFinalPublicacio=" + dataFinalPublicacio;
                    hiddenFields += " <input type=\"hidden\" name=\"dataFinalPublicacio\" value=\"" + dataFinalPublicacio + "\"/>\n";
                }
                if (dataIniciCatalogacio != null) {
                    urlRss += "&dataIniciCatalogacio=" + dataIniciCatalogacio;
                    hiddenFields += " <input type=\"hidden\" name=\"dataIniciCatalogacio\" value=\"" + dataIniciCatalogacio + "\"/>\n";
                }
                if (dataFinalCatalogacio != null) {
                    urlRss += "&dataFinalCatalogacio=" + dataFinalCatalogacio;
                    hiddenFields += " <input type=\"hidden\" name=\"dataFinalCatalogacio\" value=\"" + dataFinalCatalogacio + "\"/>\n";
                }
                if (ambit != null) {
                    urlRss += "&ambit=" + ambit;
                    hiddenFields += " <input type=\"hidden\" name=\"ambit\" value=\"" + ambit + "\"/>\n";
                }
                if (duradaMinima != null) {
                    urlRss += "&duradaMinima=" + duradaMinima;
                    hiddenFields += " <input type=\"hidden\" name=\"durada_minima\" value=\"" + duradaMinima + "\"/>\n";
                }
                if (duradaMaxima != null) {
                    urlRss += "&duradaMaxima=" + duradaMaxima;
                    hiddenFields += " <input type=\"hidden\" name=\"durada_maxima\" value=\"" + duradaMaxima + "\"/>\n";
                }

                if (inxtec != null) {
                    urlRss += "&inxtec=" + duradaMaxima;
                    if (String.valueOf(ResultGeneratorUtil.SHOW_SEARCH).equals(inxtec)) {
                        hiddenFields += " <input type=\"hidden\" name=\"inxtec\" value=\"" + String.valueOf(ResultGeneratorUtil.SHOW_SEARCHRESULTS) + "\"/>\n";
                    } else {
                        hiddenFields += " <input type=\"hidden\" name=\"inxtec\" value=\"" + inxtec + "\"/>\n";
                    }
                }
            }

            if (ConfiguracioFragments.isVoid()) {

                ConfiguracioFragments.loadConfiguracio(myConnection, "edu365");
                ConfiguracioFragments.loadConfiguracio(myConnection, "xtec");
                ConfiguracioFragments.loadConfiguracio(myConnection, "altres");
            }

            if (tipus.equals("simple")) {
                num_resultats = ConfiguracioFragments.numResultatsPerPagina[0];
                longitud_descripcio = ConfiguracioFragments.longitudDescripcio[0];
            }
            else if (tipus.equals("embedded")){
                num_resultats = 5;
                longitud_descripcio = 20;
                // num_resultats = ConfiguracioFragments.numResultatsPerPagina[2];
                //longitud_descripcio = ConfiguracioFragments.longitudDescripcio[2];
            }
            else {
                num_resultats = ConfiguracioFragments.numResultatsPerPagina[1];
                longitud_descripcio = ConfiguracioFragments.longitudDescripcio[1];
            }
            docInicial = nivell * num_resultats;
            if (nivell > 0) {
                previousPage = true;
            }
            docFinal = (nivell + 1) * num_resultats;
//			if (docFinal > hits.length()) {
//				docFinal = hits.length();
//				nextPage = false;
//			    }

            fitxa = baseUrl + "cerca/fitxaRecurs.jsp";
            // Necessari per tooltip
            out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
            out.println("<html lang=\"ca\">");
            ResultGeneratorUtil.htmlHeader(out, baseUrl, "cerca.resultatsCerca.titol", urlRss, lang);

            //	ArrayList allLevels = UtilsCercador.getAllLevels(myConnection);
            i = 0;
            DucObject ducLevel = null;
            DucObject ducArea = null;
            ArrayList allAreasArray = null;
            while (i < allLevels.size()) {
                ducLevel = (DucObject) allLevels.get(i);
                out.println("       var areas_" + ducLevel.id + "=new Array(\"" + XMLCollection.getProperty("cerca.select.area", lang) + "\", \"-1\"");
                allAreasArray = (ArrayList) allAreas.get(new Integer(ducLevel.id));
                logger.debug("(" + ducLevel.id + ") -> " + allAreasArray.size());
                int j = 0;
                while (j < allAreasArray.size()) {
                    ducArea = (DucObject) allAreasArray.get(j);
                    out.println(",\"" + ducArea.getTerm(lang) + "\",\"" + ducArea.id + "\"");
                    j++;
                }
                out.println(");");

                i++;
            }

            out.println("  function change_area(){");
            out.println("      var nivell_educatiu");
            out.println("      nivell_educatiu = document.cerca.nivell_educatiu[document.cerca.nivell_educatiu.selectedIndex].value");
            out.println("      if (nivell_educatiu > 0) {");
            out.println("         mis_areas=eval(\"areas_\" + nivell_educatiu)");
            out.println("         num_areas = (mis_areas.length / 2)");
            out.println("         document.cerca.area_curricular.length = num_areas");
            out.println("         for(i=0;i<(num_areas * 2);i=i+2){");
            out.println("           document.cerca.area_curricular.options[i/2].text=mis_areas[i]");
            out.println("           document.cerca.area_curricular.options[i/2].value=mis_areas[i + 1]");
            out.println("           }");
            out.println("      }else{");
            out.println("       document.cerca.area_curricular.length = 1");
            out.println("       document.cerca.area_curricular.options[0].value = \"-1\"");
            out.println("       document.cerca.area_curricular.options[0].text = \"" + XMLCollection.getProperty("cerca.select.area", lang) + "\"");
            out.println("    }");
            out.println("    document.cerca.area_curricular.options[0].selected = true");
            out.println(" }");

            out.println("function checkLink(e) {");
            out.println("  if (!e) var e = window.event;");
            out.println("  var clickedObj = e.target ? e.target : e.srcElement;");
            out.println("  if(clickedObj.nodeName == 'A' ) {");
            out.println("        if (clickedObj.id != '') {");
            out.println("          addVisita('" + Configuracio.contextWebAplicacio + "',clickedObj.id);");
            out.println("         }");
            out.println("    }");
            out.println("}");
            out.println("document.onmousedown = checkLink;");

            out.println(" </script>");

            out.println("<!--[if IE 6]> ");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + baseUrl + "css/ie6.css\"/>");
            out.println("<![endif]--> ");

            out.println("</head>");
            out.println("<body>");
            out.println("<div id=\"non-footer\">");
            if (imprimir.equals("no")) {
                out.println("<div id=\"header\"> ");
                out.println("<div id=\"cabecera\">	");
                out.println("<img src=\"" + baseUrl + "/imatges/cabecera.png\" border=\"0\" usemap=\"#mapTop\" /><br />");
                out.println(" <map name=\"mapTop\" id=\"mapTop\"> ");
                out.println("  <area shape=\"rect\" coords=\"731,19,930,49\" href=\"http://www.gencat.cat/educacio/\" target=\"_blank\" alt=\"Generalitat de Catalunya. Departament d'Ensenyament\" />");
                out.println("  <area shape=\"rect\" coords=\"1,13,325,49\" href=\"" + baseUrl + "\" alt=\"Merlí\" />");
                out.println(" </map> </div>");
                out.println("<div id=\"menu\">");
                out.println(" <div id=\"menu_left\">");
                out.println("  <ul><li><a href=\"" + baseUrl + "\">" + XMLCollection.getProperty("cerca.directoriInicial.inici", lang) + "</a></li>");
//	out.println( "  <li><a target=\"_blank\" href=\"http://blocs.xtec.cat/merli\">bloc</a></li>");
//	out.println( "  <li><a target=\"_blank\" href=\"http://phobos.xtec.cat/forum/viewforum.php?f=46\">f&ograve;rum</a></li>");
                out.println("  <li><a target=\"_blank\" href=\"" + XMLCollection.getProperty("url.ajuda", lang) + "\">" + XMLCollection.getProperty("cerca.directoriInicial.ajuda", lang) + "</a></li><li>");
                if (usuari == null) {
                    out.println("  <a href=\"" + baseUrl + "/loginSSO.jsp?logOn=true\">identificaci&oacute;</a></li>");
                }
                out.println(" </ul></div>");
                out.println("<div id=\"menu_right\">");
                if (usuari != null) {
                    out.println(usuariNomComplet);
                    int comentarisSuspesos = UtilsCercador.getComentarisSuspesos(usuari);
                    if (comentarisSuspesos == 1) {
                        out.println("<div id=\"comentarisSuspesos\">(<a class=\"info\" href=\"" + baseUrl + "/cerca/comentarisSuspesos.jsp\">" + comentarisSuspesos + " " + XMLCollection.getProperty("cerca.comentari", lang) + "</a> " + XMLCollection.getProperty("cerca.suspes", lang) + ")</div>");
                    }
                    if (comentarisSuspesos > 1) {
                        out.println("<div id=\"comentarisSuspesos\">(<a class=\"info\" href=\"" + baseUrl + "/cerca/comentarisSuspesos.jsp\">" + comentarisSuspesos + " " + XMLCollection.getProperty("cerca.comentaris", lang) + "</a> " + XMLCollection.getProperty("cerca.suspesos", lang) + ")</div>");
                    }
                    out.println("<a href=\"" + baseUrl + "/administracio/logout.jsp\">" + XMLCollection.getProperty("cerca.directoriInicial.logout", lang) + "</a>");

                }
                out.println("</div></div>");
                out.println("<div id=\"barra_buscador\">");
                out.println("<div id=\"cercadorOptions\">");
                out.println("	<form class=\"cercador_xtec\" name=\"cerca\" action=\"/" + Configuracio.contextWebAplicacio + "/ServletCerca\" method=\"POST\">");
                out.println("      <fieldset>");
                out.println("     <input type=\"hidden\" name=\"agrega\" value=\"1\"/>");
                out.println("     <input type=\"hidden\" name=\"sheetId\" value=\"" + sheetId + "\"/>");
                out.println("     <input type=\"hidden\" name=\"tipus\" value=\"" + tipus + "\"/>");
                out.println("     <input type=\"hidden\" name=\"nivell\" value=\"0\"/>");
                out.println("     <input type=\"hidden\" name=\"ordenacio\" value=\"" + ordenacio + "\"/>");
                out.println("     <input type=\"hidden\" name=\"novaCerca\" value=\"no\"/>");
                if (isCataleg) {
                    out.println("     <input type=\"hidden\" name=\"cataleg\" value=\"si\"/>");
                } else {
                    out.println("     <input type=\"hidden\" name=\"cataleg\" value=\"no\"/>");
                }
                out.println("     <input type=\"hidden\" name=\"userGeneric\" value=\"" + userGeneric + "\"/>");
                out.println("     <input type=\"hidden\" name=\"nomUsuari\" value=\"" + usuari + "\"/>");
                query = query.replaceAll("\"", "&quot;");
                out.println("     <input type=\"hidden\" name=\"textCercaHidden\" value=\"" + query + "\"/>");
                out.println(hiddenFields);

                out.print("<label for=\"textCerca\" class=\"hide\">Introdu&iuml;u les paraules a cercar</label>");
                out.print("<input type=\"text\" size=\"20\" name=\"textCerca\" id=\"textCerca\" value=\"" + query + "\"  tabindex=\"1\" alt=\"text cerca\" class=\"textCerca\"/>");
                out.println("<label for=\"nivell_educatiu\" class=\"hide\">Seleccioneu el nivell educatiu</label>");
                out.print("<select name=\"nivell_educatiu\" id=\"nivell_educatiu\" onchange=\"change_area()\" tabindex=\"2\" class=\"select\">");
                out.print("			   <option value=\"-1\">" + XMLCollection.getProperty("cerca.select.nivell", lang) + "</option>");
                i = 0;
                while (i < allLevels.size()) {
                    ducLevel = (DucObject) allLevels.get(i);
                    if (nivellEducatiu.equals("" + ducLevel.id)) {
                        out.print("						<option value=\"" + ducLevel.id + "\" selected>" + ducLevel.getTerm(lang) + "</option>");
                    } else {
                        out.print("						<option value=\"" + ducLevel.id + "\">" + ducLevel.getTerm(lang) + "</option>");
                    }
                    i++;
                }

                out.print("			</select>");
                out.println(" <label for=\"area_curricular\"class=\"hide\">Seleccioneu l'&agrave;rea</label>");
                out.print("					<select name=\"area_curricular\" id=\"area_curricular\"  tabindex=\"3\" class=\"select\" >");
                out.print("				    	<option value=\"-1\">" + XMLCollection.getProperty("cerca.select.area", lang) + "</option>");

                logger.debug("Nivell: " + nivellEducatiu);
                logger.debug("Area: " + areaCurricular);
                if ((nivellEducatiu != null) && !nivellEducatiu.equals("")) {
                    allAreasArray = (ArrayList) allAreas.get(new Integer(nivellEducatiu));
                    i = 0;
                    if (allAreasArray != null) {
                        while (i < allAreasArray.size()) {
                            ducArea = (DucObject) allAreasArray.get(i);
                            if ((areaCurricular != null) && areaCurricular.equals("" + ducArea.id)) {
                                out.print("						<option value=\"" + ducArea.id + "\" selected>" + ducArea.getTerm(lang) + "</option>");
                            } else {
                                out.print("						<option value=\"" + ducArea.id + "\">" + ducArea.getTerm(lang) + "</option>");
                            }
                            i++;
                        }
                    }
                }

                out.print("					</select>");
                out.println("<input type=\"image\" class=\"button\" src=\"" + baseUrl + "/imatges/boton_cerca.gif\" value=\"" + XMLCollection.getProperty("cerca.directoriInicial.cerca", lang) + "\" tabindex=\"4\" />");
                //out.println( "     <input class=\"button\" type=\"button\" value=\"" + XMLCollection.getProperty("cerca.resultatsCerca.cerca", lang) + "\" onClick=\"javascript:doSubmit();\"/>");
                out.println("   </fieldset>");
                out.println("   </form>");

                out.println(" </div>");
                out.println("<div id=\"cercadorButton\"> ");
                out.println(" <a href=\"/" + Configuracio.contextWebAplicacio + "/cerca/cercaCompleta.jsp\"><img src=\"" + baseUrl + "/imatges/boton_cerca_avancada.gif\" alt=\"" + XMLCollection.getProperty("cerca.directoriInicial.cercaCompleta", lang) + "\" border=\"0\" /></a>");
                out.println("</div></div>");
                out.println("</div>");
                out.println("");
                out.println("");

                ResultGeneratorUtil.barraNivell(out, ResultGeneratorUtil.AGREGA);


                out.println("<div id=\"barra_resultats\">");


                if ((query != null) && !query.trim().equals("")) {
                    out.println("<div id=\"barra_resultats_left\">");
                    out.println("	" + XMLCollection.getProperty("cerca.resultatsCerca.resultatsPer", lang) + " <b>" + query + "</b>");
                    if (imprimir.equals("no")) {
                        //  out.println( "<a href=\"" + urlImprimir + "\"><img src=\"" + baseUrl + "/imatges/imprimir.gif\" style=\"border:0;padding-left:5px\" alt=\"" + XMLCollection.getProperty("cerca.resultatsCerca.versioImprimir") + "\" title=\"" + XMLCollection.getProperty("cerca.resultatsCerca.versioImprimir", lang) + "\"/></a>");
                    }
                    out.println("</div>");
                }
                out.println("<div id=\"barra_resultats_right\">");
                if (hits.size() > 1) {
                    out.println("" + (docInicial + 1) + "-" + docFinal + " " + XMLCollection.getProperty("cerca.de", lang) + " " + hits.size() + " " + XMLCollection.getProperty("cerca.resultats", lang));
                }
                if (hits.size() == 1) {
                    out.println("1 " + XMLCollection.getProperty("cerca.resultat", lang));
                }

                out.println("</div>");
                out.println("</div>");
            }

            out.println("<div id=\"resultats\">");
            if (docFinal > 0) {
                out.println("  <div id=\"resultats_left\">");
            } else {
                out.println("  <div id=\"resultats_left_empty\">");
            }

            for (i = docInicial; i < Math.min(hits.size(), docFinal); i++) {
                Hashtable currentDoc = (Hashtable) hits.get(i);
                String titol = (String) currentDoc.get("titol");
                String autor = (String) currentDoc.get("autor");

                String descripcio = (String) currentDoc.get("descripcio");
                String docUrl = (String) currentDoc.get("url");
                String idRecurs = (String) currentDoc.get("idRecurs");
                // String data = (String) currentDoc.get("lom@lifeCycle@contribute@date@euncl@dateTime");
                String data = (String) currentDoc.get("dataPublicacio");
                int numComentaris = 0;//(new Integer((String)currentDoc.get("numComentaris"))).intValue();
                int numVisites = 0;//(new Integer((String)currentDoc.get("numVisites"))).intValue();
                float puntuacioMitja = 0;//(new Float((String)currentDoc.get("puntuacio"))).floatValue();
                String format = (String) currentDoc.get("format");

                String imgUrl = "";
                String imgUrls = ResultGeneratorUtil.getImgsUrl(format);

                String textComentaris = "comentaris";
                if (numComentaris == 1) {
                    textComentaris = "comentari";
                }

                String textVisites = "visites";
                if (numVisites == 1) {
                    textVisites = "visita";
                }

                String urlToShow = "";
                if ((usuari == null) && userGeneric.equals("edu365")) {
                    urlToShow = docUrl;
                } else {
                    urlToShow = fitxa + "?idRecurs=" + idRecurs + "&sheetId="
                            + sheetId + "&nomUsuari=" + usuari + "&agrega=true"
                            + "&agregaId=" + i;
                }

                data = UtilsCercador.girarDataDMYguio(data);

                out.println("<div id=\"resultat\">");
                //	out.println( "  <p>" + imgUrl + "</p>");

                String titolToShow = titol;
                if (titolToShow.length() > 75) {
                    titolToShow = titolToShow.substring(0, 74) + "..";
                    out.println("  &nbsp;<a id=\"" + idRecurs + "\"href=\"" + urlToShow + "\"><span class=\"tooltip\"><dfn>" + titol + "</dfn>" + titolToShow + "</span></a>");
                } else {
                    out.println("  &nbsp;<a id=\"" + idRecurs + "\"href=\"" + urlToShow + "\">" + titolToShow + "</a>");
                }

                int j = 0;
                while (j < puntuacioMitja) {
                    out.println("<img src=\"./imatges/stars-full.gif\" alt=\"\"/>");
                    j++;
                }
                while (j < 5) {
                    out.println("<img src=\"./imatges/stars-empty.gif\" alt=\"\"/>");
                    j++;
                }

                out.println("  <h1>(" + numComentaris + " " + textComentaris + ") " + imgUrls + "</h1>");
                out.println("<br/>");
                out.println("  <h2>" + XMLCollection.getProperty("cerca.fitxaRecurs.autor", lang) + ": " + UtilsCercador.getFNfromVCard(autor) + "</h2>");
                out.println("  <h3><div id=\"" + i + "\">");
                String cutDescription = ResultGeneratorUtil.cutDescription(descripcio, longitud_descripcio);
                out.println(cutDescription);

                if (cutDescription.length() != descripcio.length()) {
                    out.println("<a href=\"javascript:showFull('" + i + "')\">[+]</a></div>");
                    out.println("<div id=\"short_" + i + "\" style=\"visibility:hidden;display:none\">" + cutDescription + "<a href=\"javascript:showFull('" + i + "')\">[+]</a></div>");
                    out.println("<div id=\"full_" + i + "\" style=\"visibility:hidden;display:none\">" + descripcio + "<a href=\"javascript:showShort('" + i + "')\">[-]</a></div>");
                } else {
                    out.println("</div>");
                }

                out.println("  </h3>");
                out.println("  <h4>" + XMLCollection.getProperty("cerca.fitxaRecurs.dataPublicacio", lang) + ": " + data + " - " + numVisites + " " + textVisites + "</h4>");
                out.println("</div>");
            }
            if (hits.size() == 0) {
                out.println("" + XMLCollection.getProperty("cerca.resultatsCerca.noResultats", lang) + "");
            }
            out.println("</div>");
            if (imprimir.equals("no")) {
                if (hits.size() > 0) {
                    out.println("<div id=\"resultats_right\">");
                    out.println("  <p>" + XMLCollection.getProperty("cerca.resultatsCerca.ordenaPer", lang) + "</p>");
                    if (ordenacio.equals("") || ordenacio.equals("defecte")) {
                        out.println("  <a id=\"current\" href=\"javascript:sortBy('defecte');\">" + XMLCollection.getProperty("cerca.resultatsCerca.defecte", lang) + "</a><br/>");
                    } else {
                        out.println("  <a href=\"javascript:sortBy('defecte');\">" + XMLCollection.getProperty("cerca.resultatsCerca.defecte", lang) + "</a><br/>");
                    }
                    if (ordenacio.equals("data")) {
                        out.println("  <a id=\"current\" href=\"javascript:sortBy('data');\">" + XMLCollection.getProperty("cerca.resultatsCerca.data", lang) + "</a><br/>");
                    } else {
                        out.println("  <a href=\"javascript:sortBy('data');\">" + XMLCollection.getProperty("cerca.resultatsCerca.data", lang) + "</a><br/>");
                    }
                    if (ordenacio.equals("visites")) {
                        out.println("  <a id=\"current\" href=\"javascript:sortBy('visites');\">" + XMLCollection.getProperty("cerca.resultatsCerca.visites", lang) + "</a><br/>");
                    } else {
                        out.println("  <a href=\"javascript:sortBy('visites');\">" + XMLCollection.getProperty("cerca.resultatsCerca.visites", lang) + "</a><br/>");
                    }
                    if (ordenacio.equals("puntuacio")) {
                        out.println("  <a id=\"current\" href=\"javascript:sortBy('puntuacio');\">" + XMLCollection.getProperty("cerca.resultatsCerca.puntuacio", lang) + "</a><br/>");
                    } else {
                        out.println("  <a href=\"javascript:sortBy('puntuacio');\">" + XMLCollection.getProperty("cerca.resultatsCerca.puntuacio", lang) + "</a><br/>");
                    }
                    if (ordenacio.equals("comentaris")) {
                        out.println("  <a id=\"current\" href=\"javascript:sortBy('comentaris');\">" + XMLCollection.getProperty("cerca.resultatsCerca.comentaris", lang) + "</a><br/>");
                    } else {
                        out.println("  <a href=\"javascript:sortBy('comentaris');\">" + XMLCollection.getProperty("cerca.resultatsCerca.comentaris", lang) + "</a><br/>");
                    }
                    out.println("</div>");
                }
                out.println("<div class=\"clear\"></div>");

                out.println("</div>");
            }
            if (imprimir.equals("no")) {
                if (hits.size() > 0) {
                    out.println("<div id=\"bottom\">");
                    out.println("<div class=\"bottom_left_div\">");
                    out.println("</div>");
                    out.println("<div id=\"bottom_left\">");
                    int numPagines = hits.size() / num_resultats;
                    if ((hits.size() % num_resultats) != 0) {
                        numPagines++;
                    }
                    logger.debug("num pagines: " + numPagines);
                    int j = 0;
                    int top = 10;
                    if (numPagines > 1) {
                        out.println(XMLCollection.getProperty("cerca.resultatsCerca.paginaResultats", lang) + "    ");
                        j = nivell - 4;
                        if (j <= 0) {
                            j = 0;
                        } else {
                            out.println("...");
                        }
                        top = j + 10;
                        if (top > numPagines) {
                            top = numPagines;
                        }
                        while (j < top) {
                            if (j != nivell) {
                                //   out.println( "<a href=\"javascriptgoToNivell:('" + j + "')\">" +  (j + 1) + "</a>&nbsp;";
                                out.println("<a href=\"javascript:goToNivell('" + j + "')\">" + (j + 1) + "</a>&nbsp;&nbsp;");
                            } else {
                                // out.println( "" + (j + 1) + "&nbsp;";
                                out.println("" + (j + 1) + "&nbsp;&nbsp;");
                            }
                            j++;
                        }
                        if (j != numPagines) {
                            out.println("...");
                        }
                    }
                    out.println("</div>");
                    out.println("<div id=\"bottom_right\">");
                    out.println("<p class=\"rss\"><a href=\"" + urlRss + "\">" + XMLCollection.getProperty("cerca.resultatsCerca.subscriureCerca", lang) + "</a></p>");
                    out.println("</div>");
                    out.println("<div class=\"clear\"></div>");
                    out.println("</div>");
                }
                out.println("</div>");

                out.println("<div class=\"footer_cercador\">");
                out.println("  <div id=\"pie\">");
                out.println("<a href=\"http://www.xtec.cat/web/guest/avis\">" + XMLCollection.getProperty("cerca.directoriInicial.avisLegal", lang) + "</a> |");
                out.println("<a href=\"http://www.xtec.cat/web/guest/avis\">" + XMLCollection.getProperty("cerca.directoriInicial.privadesa", lang) + "</a> |");
                out.println("<a href=\"http://www.xtec.cat/web/guest/avis\">" + XMLCollection.getProperty("cerca.directoriInicial.condicionsUs", lang) + "</a> |");
                out.println("<a href=\"#\">Copyright © 2014, Generalitat de Catalunya</a> <br/>");
                out.println("<a href=\"http://www.xtec.cat/web/guest/avis\">" + XMLCollection.getProperty("cerca.directoriInicial.responsabilitat", lang) + "</a>");
                out.println("  </div>");
                out.println("</div>");

                out.println("<!-- GOOGLE ANALYTICS -->");
                out.println("<script type=\"text/javascript\">");
                out.println("var gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");");
                out.println("document.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));");
                out.println("</script>");
                out.println("<script type=\"text/javascript\">");
                out.println("try {");
                out.println("var pageTracker = _gat._getTracker(\"UA-6935294-1\");");
                out.println("pageTracker._trackPageview();");
                out.println("} catch(err) {}</script>");
                out.println("<!-- END -->");

                /*
                 <!-- GOOGLE ANALYTICS -->
                 <script type="text/javascript">
                 var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
                 document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
                 </script>
                 <script type="text/javascript">
                 try {
                 var pageTracker = _gat._getTracker("UA-6935294-1");
                 pageTracker._trackPageview();
                 } catch(err) {}</script>
                 <!-- END -->

                 */
            }

            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return outputHTML;
    }

}
