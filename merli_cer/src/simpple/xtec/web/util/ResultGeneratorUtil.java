package simpple.xtec.web.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public class ResultGeneratorUtil {

    public static final int AGREGA = 1;
    public static final int MERLI = 0;

    public static final int SHOW_ALL = 0;
    public static final int SHOW_SEARCHRESULTS = 1;
    public static final int SHOW_SEARCH = 2;
    public static final int SHOW_ONLYRESULTS = 3;
    public static final int SHOW_EMBEDDED = 4;

    private static final char SALTLINIA = '\n';

    static final Logger logger = Logger.getLogger(simpple.xtec.web.util.ResultGeneratorUtil.class);

    public static void barraNivell(PrintWriter out, int opcio) {
        out.println("<div id=\"barra_nivell\">");
        out.println("	<div id=\"menu_nivell\" style=\"width:155px;\">");
        out.println("		<ul>");
        if (opcio == AGREGA) {
            out.println("		  <li><a href=\"#\" onclick=\"javascript:askToMerli();return false;\">Merl�</a></li>");
            out.println("		  <li><a style=\"background-color:#EFD393;\" onclick=\"return false;\" class=\"linial\" href=\"#\">Agrega</a></li></ul>");
        }
        if (opcio == MERLI) {
            out.println("		  <li><a style=\"background-color:#EFD393;\" href=\"#\" onclick=\"return false;\">Merl�</a></li>");
            out.println("		  <li><a onclick=\"javascript:askToAgrega();return false;\" class=\"linial\" href=\"#\">Agrega</a></li></ul>");
        }
        out.println("	</div>");
        out.println("</div>");
    }

    public static void htmlHeader(PrintWriter out, String baseUrl, String title, String urlRss, String lang) {
        out.println(htmlHeader(baseUrl, title, urlRss, lang));

        ResultGeneratorUtil.javascriptCercador(out);
    }

    public static String htmlHeader(String baseUrl, String title, String urlRss, String lang) {
        String html = "";
        baseUrl = baseUrl.trim();
        if ("/".equals(baseUrl.substring(baseUrl.length() - 1))) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        html += "<head>" + SALTLINIA;
        html += "<title>" + XMLCollection.getProperty(title) + "</title>" + SALTLINIA;
        html += "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>" + SALTLINIA;
        if (urlRss != null && !"".equals(urlRss)) {
            html += "<link rel=\"alternate\" type=\"application/rss+xml\" title=\"" + XMLCollection.getProperty("cerca.resultatsCerca.subscriureCerca", lang) + "\" href=\"" + urlRss + "\" />" + SALTLINIA;
        }
        html += "<link rel=\"shortcut icon\" href=\"" + baseUrl + "/imatges/merli.ico\" />" + SALTLINIA;
        html += "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + baseUrl + "/css/tooltip.css\"/>" + SALTLINIA;
        html += "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + baseUrl + "/css/merli.css\"/>" + SALTLINIA;
        html += "<link rel=\"stylesheet\" href=\"" + baseUrl + "/css/merli-print.css\" media=\"print\" type=\"text/css\" />" + SALTLINIA;
        html += "<link href=\"//maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css\" rel=\"stylesheet\" integrity=\"sha384-T8Gy5hrqNKT+hzMclPo118YTQO6cYprQmhrYwIiQ/3axmI1hQomh7Ud2hPOy8SP1\" crossorigin=\"anonymous\">"+ SALTLINIA;
        return html;
    }

    public static void javascriptCercador(PrintWriter out) {
        out.println("  <script type=\"text/javascript\">");
        out.println("    function copia_portapapeles (url) {");
        out.println("       var aux = document.createElement('input');");
        out.println("       aux.setAttribute('value', url);");
        out.println("       document.body.appendChild(aux);");
        out.println("       aux.select();");
        out.println("       document.execCommand(\"copy\");");
        out.println("       document.body.removeChild(aux);");
        out.println("      }");
        out.println("    function askToAgrega () {");
        out.println("      document.cerca.agrega.value = 1;");
        out.println("      document.cerca.submit();");
        out.println("      }");
        out.println("    function askToMerli () {");
        out.println("      document.cerca.agrega.value = 0;");
        out.println("      document.cerca.submit();");
        out.println("      }");
        out.println("    function goToNivell (nivell) {");
        out.println("      document.cerca.textCerca.value = '';");
        out.println("      document.cerca.nivell.value = nivell;");
        out.println("      document.cerca.submit();");
        out.println("      }");
        out.println("    function doSubmit (keep) {");


        out.println("var value = document.cerca.textCerca.value;");
        out.println("document.cerca.textCerca.value = value.toString();");

        out.println("      document.cerca.nivell.value = 0;");
        out.print("      if (!keep){ ");
        out.println("      	document.cerca.filtreRecurs.value = '';}");
        out.println("      document.cerca.tipus.value = 'simple';");
        out.println("      document.cerca.novaCerca.value = 'si';");
        out.println("      if(document.cerca.cicle) document.cerca.cicle.value=\"-1\";");
        out.println("      if(document.cerca.keywords) document.cerca.keywords.value=\"\";");
        out.println("      $(\"#ducContentIdentificador\").remove();");
        out.println("      if(document.cerca.autorCerca) document.cerca.autorCerca.value=\"\";");
        out.println("      if(document.cerca.editorialCerca) document.cerca.editorialCerca.value=\"\";");
        out.println("      if(document.cerca.idiomaCerca) document.cerca.idiomaCerca.value=\"\";");
        out.println("      if(document.cerca.recursOnline) document.cerca.recursOnline.value=\"\";");
        out.println("      if(document.cerca.recursFisic)  document.cerca.recursFisic.value=\"\";");
        out.println("      if(document.cerca.llicenciaCerca) document.cerca.llicenciaCerca.value=\"\";");
        out.println("      if(document.cerca.destinatariCerca) document.cerca.destinatariCerca.value=\"\";");
        out.println("      if(document.cerca.dataIniciPublicacio) document.cerca.dataIniciPublicacio.value=\"\";");
        out.println("      if(document.cerca.dataFinalPublicacio) document.cerca.dataFinalPublicacio.value=\"\";");
        out.println("      if(document.cerca.formatRecurs) document.cerca.formatRecurs.value=\"\";");
        out.println("      if(document.cerca.unitatCerca) document.cerca.unitatCerca.value=\"\";");
        out.println("      document.cerca.submit();");
        out.println("      }");
        out.println("    function sortBy (tipusOrdenacio, tipusDireccio) {");
        out.println("      document.cerca.nivell.value = 0;");
        out.println("      document.cerca.ordenacio.value = tipusOrdenacio;");
        out.println("      document.cerca.direccio.value = tipusDireccio;");
        out.println("      document.cerca.submit();");
        out.println("      }");
        out.println("    function filterBy (filtreRecurs) {");
        out.println("      document.cerca.filtreRecurs.value = filtreRecurs;");
        out.println("      document.cerca.submit();");
        out.println("      }");
        out.println("  function checkCR(evt) {");
        out.println("	    var evt  = (evt) ? evt : ((event) ? event : null);");
        out.println("	    var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);");
        out.println("	    if ((evt.keyCode == 13) && (node.type==\"text\")) {");
        out.println("		    doSubmit();");
        out.println("		    return false;");
        out.println("		    }");
        out.println("	  }");
        out.println("  function showFull (id) {");
        out.println("      document.getElementById(\"\" + id).innerHTML = document.getElementById(\"full_\" + id).innerHTML;");
        out.println("      }");
        out.println("  function showShort (id) {");
        out.println("      document.getElementById(\"\" + id).innerHTML = document.getElementById(\"short_\" + id).innerHTML;");
        out.println("      }");
        out.println("  document.onkeypress = checkCR;");
        out.println("  </script>");
        out.println("  <script type=\"text/javascript\" src=\"./scripts/ajax-visites.js\"></script>");
        out.println("  <script type=\"text/javascript\" src=\"./scripts/ietooltips.js\"></script>");
        out.println("  <script type=\"text/javascript\" src=\"./scripts/jquery-1.4.2.min.js\"></script>");

    }

    public static void getDucJSList(ArrayList allLevels, Hashtable allAreas, String lang, PrintWriter out) {
        out.print(getDucJSList(allLevels, allAreas, lang));
    }

    public static String getDucJSList(ArrayList allLevels, Hashtable allAreas, String lang) {
        int i;
        String html = "";
        i = 0;
        DucObject ducLevel = null;
        DucObject ducArea = null;
        ArrayList allAreasArray = null;
        while (i < allLevels.size()) {
            ducLevel = (DucObject) allLevels.get(i);
            html += ("       var areas_" + ducLevel.id + "=new Array(\"" + XMLCollection.getProperty("cerca.select.area", lang) + "\", \"-1\"") + SALTLINIA;
            allAreasArray = (ArrayList) allAreas.get(new Integer(ducLevel.id));
            if (allAreasArray != null) {
                logger.debug("(" + ducLevel.id + ") -> " + allAreasArray.size());
                int j = 0;
                while (j < allAreasArray.size()) {
                    ducArea = (DucObject) allAreasArray.get(j);
                    if (!ducArea.term.startsWith("Compet�ncies")) {
                        html += (",\"" + ducArea.term + "\",\"" + ducArea.id + "\"") + SALTLINIA;
                    }
                    j++;
                }
            }
            html += (");") + SALTLINIA;
            i++;
        }
        return html;
    }

    public static void functionJSchange_area(PrintWriter out, String lang) {
        out.print(functionJSchange_area(lang));
    }

    public static String functionJSchange_area(String lang) {
        String html = "";
        html += "  function change_area(){" + SALTLINIA;
        html += "      var nivell_educatiu" + SALTLINIA;
        html += "      nivell_educatiu = document.cerca.nivell_educatiu[document.cerca.nivell_educatiu.selectedIndex].value" + SALTLINIA;
        html += "      if (nivell_educatiu > 0) {" + SALTLINIA;
        html += "         mis_areas=eval(\"areas_\" + nivell_educatiu)" + SALTLINIA;
        html += "         num_areas = (mis_areas.length / 2)" + SALTLINIA;
        html += "         document.cerca.area_curricular.length = num_areas" + SALTLINIA;
        html += "         for(i=0;i<(num_areas * 2);i=i+2){" + SALTLINIA;
        html += "           document.cerca.area_curricular.options[i/2].text=mis_areas[i]" + SALTLINIA;
        html += "           document.cerca.area_curricular.options[i/2].value=mis_areas[i + 1]" + SALTLINIA;
        html += "           }" + SALTLINIA;
        html += "      }else{" + SALTLINIA;
        html += "       document.cerca.area_curricular.length = 1" + SALTLINIA;
        html += "       document.cerca.area_curricular.options[0].value = \"-1\"" + SALTLINIA;
        html += "       document.cerca.area_curricular.options[0].text = \"" + XMLCollection.getProperty("cerca.select.area", lang) + "\"" + SALTLINIA;
        html += "    }" + SALTLINIA;
        html += "    document.cerca.area_curricular.options[0].selected = true" + SALTLINIA;
        html += " }" + SALTLINIA;

        return html;
    }

    public static void functionJScheckLink(PrintWriter out) {
        out.print(functionJScheckLink());
    }

    public static String functionJScheckLink() {
        String html = "";
        html += "function checkLink(e) {" + SALTLINIA;
        html += "  if (!e) var e = window.event;" + SALTLINIA;
        html += "  var clickedObj = e.target ? e.target : e.srcElement;" + SALTLINIA;
        html += "  if(clickedObj.nodeName == 'A' ) {" + SALTLINIA;
        html += "        if (clickedObj.id != '') {" + SALTLINIA;
        html += "          addVisita('" + Configuracio.contextWebAplicacio + "',clickedObj.id);" + SALTLINIA;
        html += "         }" + SALTLINIA;
        html += "    }" + SALTLINIA;
        html += "}" + SALTLINIA;
        html += "document.onmousedown = checkLink;" + SALTLINIA;

        return html;
    }

    public static void htmlHeaderCabecera(PrintWriter out, String baseUrl, String alt, String lastUrl) {
        out.print(htmlHeaderCabecera(baseUrl, alt, lastUrl));
    }

    public static String htmlHeaderCabecera(String baseUrl, String alt, String lastUrl) {
        String html = "";
        String lastChar = "";
        if (lastUrl == null) {
            lastUrl = baseUrl;
        }
        if (lastUrl.indexOf("?") > 0) {
            if (lastUrl.indexOf("lang=") > 0) {
                lastUrl = lastUrl.substring(0, lastUrl.indexOf("lang=")) + lastUrl.substring(lastUrl.indexOf("lang=") + "lang=ca".length());
            }
            lastChar = "&";
        } else {
            lastChar = "?";
        }
//		if ("/".equals(lastUrl.substring(lastUrl.length()-1))){
//			lastUrl = lastUrl.substring(0, lastUrl.length()-1);
//		}
        lastUrl += lastChar;
        //Nadim 07/07/2015
        lastUrl = lastUrl.replaceAll("\"", "&#34;");
        //Nadim 07/07/2015 --> END
        html += "<div id=\"cabecera\">	" + SALTLINIA;
        html += "<a  href=\"" + baseUrl + "\" alt=\"" + alt + "\" ><img src=\"" + baseUrl + "/imatges/capca_logo_merli.png\" border=\"0\" usemap=\"#mapTop\" /></a>" + SALTLINIA;
        html += "<a href=\"http://www.gencat.cat/educacio/\" target=\"_blank\" alt=\"Generalitat de Catalunya. Departament d'Ensenyament\"><img style=\"float:right;\" src=\"" + baseUrl + "/imatges/capca_gene.png\" border=\"0\" usemap=\"#mapTop\" /></a>" + SALTLINIA;
//		html += "<img src=\""+ baseUrl +"/imatges/capcalera.png\" border=\"0\" usemap=\"#mapTop\" /><br />"+SALTLINIA;
//		html += " <map name=\"mapTop\" id=\"mapTop\"> "+SALTLINIA;
//		html += "  <area shape=\"rect\" coords=\"731,19,930,49\" href=\"http://www.gencat.cat/educacio/\" target=\"_blank\" alt=\"Generalitat de Catalunya. Departament d'Educaci&oacute;\" />"+SALTLINIA;
//		html += "  <area shape=\"rect\" coords=\"1,13,325,49\" href=\"" + baseUrl + "\" alt=\""+alt+"\" />"+SALTLINIA;
//		html += " </map> ";
        html += "<div id=\"languages\">";
        html += "	<a alt=\"Catal&agrave;\" href=\"" + lastUrl + "lang=ca\">Catal&agrave;</a>";
        html += " |	<a alt=\"Castellano\" href=\"" + lastUrl + "lang=es\">Castellano</a>";
        html += " |	<a alt=\"English\" href=\"" + lastUrl + "lang=en\">English</a> ";
        html += "</div>";

        html += "</div>" + SALTLINIA;

        return html;
    }

    public static void htmlHeaderMenu(String usuari, String usuariNomComplet, PrintWriter out, String baseUrl, int comentarisSuspesos, String lang) {
        out.println(htmlHeaderMenu(usuari, usuariNomComplet, baseUrl, comentarisSuspesos, lang));
    }

    public static String htmlHeaderMenu(String usuari, String usuariNomComplet, String baseUrl, int comentarisSuspesos, String lang) {
        String html = "";

        html += "<div id=\"menu\">" + SALTLINIA;
        html += " <div id=\"menu_left\">" + SALTLINIA;
        html += "  <ul><li><a href=\"" + baseUrl + "\">" + XMLCollection.getProperty("cerca.directoriInicial.inici", lang) + "</a></li>" + SALTLINIA;
//		html += "  <li><a target=\"_blank\" href=\"http://blocs.xtec.cat/merli\">"+XMLCollection.getProperty("cerca.directoriInicial.bloc",lang)+"</a></li>"+SALTLINIA;
//		html += "  <li><a target=\"_blank\" href=\"http://phobos.xtec.cat/forum/viewforum.php?f=46\">"+XMLCollection.getProperty("cerca.directoriInicial.forum",lang)+"</a></li>"+SALTLINIA;
        html += "  <li><a target=\"_blank\" href=\"" + XMLCollection.getProperty("url.ajuda", lang) + "\">" + XMLCollection.getProperty("cerca.directoriInicial.ajuda", lang) + "</a></li>" + SALTLINIA;
        html += "  <li class=\"ultimli\">" + SALTLINIA;
        if (usuari == null) {
            html += "  <a href=\"" + baseUrl + "/loginSSO.jsp?logOn=true\">" + XMLCollection.getProperty("cerca.directoriInicial.identificacio", lang) + "</a></li>" + SALTLINIA;
        }
        html += " </ul></div>" + SALTLINIA;
        html += "<div id=\"menu_right\">" + SALTLINIA;
        if (usuari != null) {
            html += usuariNomComplet + "&nbsp;";
            if (comentarisSuspesos == 1) {
                html += "<div id=\"comentarisSuspesos\">(<a class=\"info\" href=\"" + baseUrl + "/cerca/comentarisSuspesos.jsp\">" + comentarisSuspesos + " " + XMLCollection.getProperty("cerca.comentari", lang) + "</a> " + XMLCollection.getProperty("cerca.suspes", lang) + ")</div>" + SALTLINIA;
            }
            if (comentarisSuspesos > 1) {
                html += "<div id=\"comentarisSuspesos\">(<a class=\"info\" href=\"" + baseUrl + "/cerca/comentarisSuspesos.jsp\">" + comentarisSuspesos + " " + XMLCollection.getProperty("cerca.comentaris", lang) + "</a> " + XMLCollection.getProperty("cerca.suspesos", lang) + ")</div>" + SALTLINIA;
            }
            html += "<a href=\"" + baseUrl + "/administracio/logout.jsp\">" + XMLCollection.getProperty("cerca.directoriInicial.logout", lang) + "</a>" + SALTLINIA;

        }
        html += "</div>" + SALTLINIA;
        html += "</div>" + SALTLINIA;

        return html;
    }

    public static void htmlFooterCercador(PrintWriter out, String lang) {
        out.print(htmlFooterCercador(lang));
    }

    public static String htmlFooterCercador(String lang) {
        String html = "";

        html += "<div class=\"footer_cercador\">" + SALTLINIA;
        html += "  <div id=\"pie\">" + SALTLINIA;
        html += "<a href=\"http://www.xtec.cat/web/guest/avis\">" + XMLCollection.getProperty("cerca.directoriInicial.avisLegal", lang) + "</a> |" + SALTLINIA;
        html += "<a href=\"http://www.xtec.cat/web/guest/avis\">" + XMLCollection.getProperty("cerca.directoriInicial.privadesa", lang) + "</a> |" + SALTLINIA;
        html += "<a href=\"http://www.xtec.cat/web/guest/avis\">" + XMLCollection.getProperty("cerca.directoriInicial.condicionsUs", lang) + "</a> |" + SALTLINIA;
        html += "<a href=\"#\">Copyright � 2014, Generalitat de Catalunya</a> <br/>" + SALTLINIA;
        html += "<a href=\"http://www.xtec.cat/web/guest/avis\">" + XMLCollection.getProperty("cerca.directoriInicial.responsabilitat", lang) + "</a>" + SALTLINIA;
        html += "  </div>" + SALTLINIA;
        html += "</div>" + SALTLINIA;

        return html;
    }

    public static void htmlHeaderNoCapcalera(PrintWriter out) {
        out.print(htmlHeaderNoCapcalera());
    }

    public static String htmlHeaderNoCapcalera() {

        String html = "";
        html += "<style>" + SALTLINIA;
        html += "#header {" + SALTLINIA;
        html += "background-position:0px -105px;" + SALTLINIA;
        html += "background-repeat:repeat-x;" + SALTLINIA;
        html += "text-align:center;" + SALTLINIA;
        //html += "height:58px;"+SALTLINIA;
        html += "width:100%;" + SALTLINIA;
        html += "}" + SALTLINIA;
        html += "</style>" + SALTLINIA;
        html += "<div id=\"header\"> " + SALTLINIA;

        return html;
    }

    public static String htmlResumResultats(Hits hits, IndexSearcher indexPrincipal, Query totalQuery, String urlCerca,
                                            String lang, PrintWriter out) {
        String html = htmlResumResultats(hits, indexPrincipal, totalQuery, urlCerca, lang);
        out.println(html);
        return html;
    }

    public static String htmlResumResultats(Hits hits, IndexSearcher indexPrincipal, Query totalQuery, String urlCerca,
                                            String lang) {
        //Creaci� de la taula amb tipus de recursos i quantitat de cada un d'ells.
        String html = "";
        Hashtable hm = new Hashtable();

        String format;
        String filtre;

        int altresFisics = 0;
        int altresVirtuals = 0;
        int totalFisics = 0;
        int totalVirtuals = 0;
        int total = 0;

        QueryGenerator queryGenerator = null;
        Hits tmpHits = null;
        Query fisicsQuery = null, onlineQuery = null, grupsQuery = null, altresQuery = null;

        queryGenerator = new QueryGenerator();

        // 1. El total
        total = hits.length();
        logger.debug("TOTAL: " + total);
        // 2. Total f�sics
        try {
            fisicsQuery = (Query) totalQuery.clone();
            Query query = queryGenerator.getQueryRecursFisicOnline(TipusFitxer.FISIC);
            ((BooleanQuery) fisicsQuery).add(query, BooleanClause.Occur.MUST);

            tmpHits = indexPrincipal.search(fisicsQuery);
            totalFisics = tmpHits.length();
            logger.debug("totalFisics:" + totalFisics);
        } catch (Exception e) {
            logger.error(e);
        }

        // 3. Total online
        try {
            onlineQuery = (Query) totalQuery.clone();
            Query query = queryGenerator.getQueryRecursFisicOnline(TipusFitxer.ENLINIA);
            ((BooleanQuery) onlineQuery).add(query, BooleanClause.Occur.MUST);

            tmpHits = indexPrincipal.search(onlineQuery);
            totalVirtuals = tmpHits.length();
            logger.debug("totalVirtuals:" + totalVirtuals);
        } catch (Exception e) {
            logger.error(e);
        }

        // 4. Total per grup
        try {
            Hashtable grups = (Hashtable) TipusFitxer.allGrupRecursos.get(TipusFitxer.ID_GRUP);
            for (Enumeration it = grups.keys(); it.hasMoreElements();) {
                String idGrup = (String) it.nextElement();
                ArrayList lidTipus = (ArrayList) TipusFitxer.allGrupRecursos.get(idGrup);

                grupsQuery = (Query) totalQuery.clone();
                Query query = queryGenerator.getQueryTipusFitxers(lidTipus);
                ((BooleanQuery) grupsQuery).add(query, BooleanClause.Occur.MUST);

                tmpHits = indexPrincipal.search(grupsQuery);
                logger.debug("total grup " + idGrup + ":" + tmpHits.length());
                if (tmpHits.length() > 0) {
                    hm.put(idGrup, new Integer(tmpHits.length()));
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }

        // 5. Altres fisics
        try {
            altresQuery = (Query) fisicsQuery.clone();

            ArrayList lGrups = (ArrayList) TipusFitxer.allGrupRecursos.get(TipusFitxer.FISIC);
            ArrayList lTipus = new ArrayList();
            for (Iterator iterator = lGrups.iterator(); iterator.hasNext();) {
                String idGrup = (String) iterator.next();
                lTipus.addAll((ArrayList) TipusFitxer.allGrupRecursos.get(idGrup));
            }
            altresQuery = (Query) fisicsQuery.clone();
            Query query = queryGenerator.getQueryTipusFitxers(lTipus);
            ((BooleanQuery) altresQuery).add(query, BooleanClause.Occur.MUST_NOT);

            tmpHits = indexPrincipal.search(altresQuery);
            altresFisics = tmpHits.length();
            logger.debug("altres fisics:" + altresFisics);
        } catch (Exception e) {
            logger.error(e);
        }

        // 5. Altres virtuals
        try {
            altresQuery = (Query) onlineQuery.clone();

            ArrayList lGrups = (ArrayList) TipusFitxer.allGrupRecursos.get(TipusFitxer.ENLINIA);
            ArrayList lTipus = new ArrayList();
            for (Iterator iterator = lGrups.iterator(); iterator.hasNext();) {
                String idGrup = (String) iterator.next();
                lTipus.addAll((ArrayList) TipusFitxer.allGrupRecursos.get(idGrup));
            }
            altresQuery = (Query) onlineQuery.clone();
            Query query = queryGenerator.getQueryTipusFitxers(lTipus);
            ((BooleanQuery) altresQuery).add(query, BooleanClause.Occur.MUST_NOT);

            tmpHits = indexPrincipal.search(altresQuery);
            altresVirtuals = tmpHits.length();
            logger.debug("altres virtuals:" + altresVirtuals);
        } catch (Exception e) {
            logger.error(e);
        }

        //Prepara la urlCerca el camp Format.
        String formatAct = "";
        if (urlCerca.indexOf("&filtreRecurs=") > 0) {
            formatAct = urlCerca.substring(urlCerca.indexOf("&filtreRecurs=") + 14, (urlCerca + "&").indexOf("&", urlCerca.indexOf("filtreRecurs=")));
        } else {
            if (urlCerca.indexOf("?filtreRecurs=") > 0) {
                formatAct = urlCerca.substring(urlCerca.indexOf("?filtreRecurs=") + 14, (urlCerca + "&").indexOf("&", urlCerca.indexOf("filtreRecurs=")));
            }
        }

        //Maquetació de la taula de resum.
        html += "<div id=\"resumResultats\">" + SALTLINIA;

        String htv = "";
        String htf = "";

        String resum;
        String ico = "";
        Enumeration it = hm.keys();
        while (it.hasMoreElements()) {
            filtre = (String) it.nextElement();
            resum = "";
            if (TipusFitxer.allGrupIcones != null) {
                ico = (String) TipusFitxer.allGrupIcones.get(filtre);
            }
            if (!formatAct.equals(filtre)) {
                resum += "<li>";
                if (ico != null && !"".equals(ico)) {
                    resum += "<img src=\"" + ico.replaceAll(".gif", "_out.gif") + "\"/>";
                }
                resum += "<a href=\"#\" onclick=\"javascript:filterBy ('" + (filtre) + "');\">";
            } else {
                resum += "<li class=\"filtrat\">";
                if (ico != null && !"".equals(ico)) {
                    resum += "<img src=\"" + ico + "\"/>";
                }
            }
            resum += ((Hashtable) TipusFitxer.allGrupRecursos.get(TipusFitxer.ID_GRUP)).get(filtre) + " (";
            resum += hm.get(filtre);
            resum += ")";
            if (!formatAct.equals(filtre)) {
                resum += "</a>";
            }
            resum += "</li>";
            resum += "" + SALTLINIA;
            if (isInTheList(filtre, (ArrayList) TipusFitxer.allGrupRecursos.get(TipusFitxer.ENLINIA))) {
                htv += resum;
            } else {
                htf += resum;
            }
        }
        format = "<ul class=\"resumResultats\">" + SALTLINIA;
        if ("".equals(formatAct)) {
            format += "<li class=\"filtrat\">" + SALTLINIA
                    + XMLCollection.getProperty("cerca.resum.recursos.tot", lang)
                    + " (" + (total) + ")";
        } else {
            format += "<li>" + SALTLINIA
                    + "<a href=\"#\" onclick=\"javascript:doSubmit();\">"
                    + XMLCollection.getProperty("cerca.resum.recursos.tot", lang)
                    + " (" + (total) + ")"
                    + "</a>";
        }
        format += "</li>" + SALTLINIA
                + "</ul>" + SALTLINIA;
        html += format;
        if (totalVirtuals > 0) {
            String a = "", clas = "", enda = "";
            if (TipusFitxer.ENLINIA.equals(formatAct)) {
                clas = "class=\"filtrat\"";
            } else {
                a = "<a href=\"#\" onclick=\"javascript:filterBy ('" + (TipusFitxer.ENLINIA) + "');\">";
                enda = "</a>";
            }
            format = "<ul class=\"resumResultats\">" + SALTLINIA;
            format += "<li " + clas + ">" + SALTLINIA + a
                    + XMLCollection.getProperty("cerca.resum.recursos.linia", lang)
                    + " (" + totalVirtuals + ")" + enda;
            format += "</li>" + SALTLINIA;

            if (altresVirtuals > 0) {
                if (TipusFitxer.ALTRESENLINIA.equals(formatAct)) {
                    htv += "<li class=\"filtrat\">" + SALTLINIA
                            + "<img src=\"imatges/icones/altres.gif\"/>"
                            + XMLCollection.getProperty("cerca.resum.recursos.altres", lang)
                            + " (" + (altresVirtuals)
                            + ")";
                } else {
                    htv += "<li>" + SALTLINIA
                            + "<img src=\"imatges/icones/altres_out.gif\"/>"
                            + "<a href=\"#\" onclick=\"javascript:filterBy ('" + (TipusFitxer.ALTRESENLINIA) + "');\">"
                            + XMLCollection.getProperty("cerca.resum.recursos.altres", lang)
                            + " (" + (altresVirtuals)
                            + ")"
                            + "</a>";
                }
                format += "</li>" + SALTLINIA;
            }
            format += "<ul>" + SALTLINIA;
            format += htv
                    + "</ul>" + SALTLINIA
                    + "</ul>" + SALTLINIA;
            html += format;
        }
        if (totalFisics > 0) {
            String a = "", clas = "", enda = "";
            if (TipusFitxer.FISIC.equals(formatAct)) {
                clas = "class=\"filtrat\"";
            } else {
                a = "<a href=\"#\" onclick=\"javascript:filterBy ('" + (TipusFitxer.FISIC) + "');\">";
                enda = "</a>";
            }
            format = "<ul class=\"resumResultats\">" + SALTLINIA;
            format += "<li " + clas + ">" + SALTLINIA + a
                    + XMLCollection.getProperty("cerca.resum.recursos.fisics", lang)
                    + " (" + totalFisics + ")" + enda;
            format += "</li>" + SALTLINIA;

            if (altresFisics > 0) {
                if (TipusFitxer.ALTRESFISICS.equals(formatAct)) {
                    htf += "<li class=\"filtrat\">" + SALTLINIA
                            + "<img src=\"imatges/icones/altres.gif\"/>"
                            + XMLCollection.getProperty("cerca.resum.recursos.altres", lang)
                            + " (" + (altresFisics)
                            + ")";
                } else {
                    htf += "<li>" + SALTLINIA
                            + "<img src=\"imatges/icones/altres_out.gif\"/>"
                            + "<a href=\"#\" onclick=\"javascript:filterBy ('" + (TipusFitxer.ALTRESFISICS) + "');\">"
                            + XMLCollection.getProperty("cerca.resum.recursos.altres", lang)
                            + " (" + (altresFisics)
                            + ")"
                            + "</a>";
                }
                format += "</li>" + SALTLINIA;
            }
            format += "<ul>" + SALTLINIA;
            format += htf
                    + "</ul>" + SALTLINIA
                    + "</ul>" + SALTLINIA;
            html += format;

        }

        html += "</div>";

        return html;
    }

    public static String htmlBarraTotalResultats(Hits hits, String query, String imprimir, String lang, int docInicial, int docFinal) {
        String html = "";
        html += "<div id=\"barra_resultats\">" + SALTLINIA;

        if ((query != null) && !query.trim().equals("")) {
            html += "<div id=\"barra_resultats_left\">" + SALTLINIA;
            html += "	" + XMLCollection.getProperty("cerca.resultatsCerca.resultatsPer", lang) + " <b>" + query + "</b>" + SALTLINIA;
            if (imprimir.equals("no")) {
                //  html += "<a href=\"" + urlImprimir + "\"><img src=\"" + baseUrl + "/imatges/imprimir.gif\" style=\"border:0;padding-left:5px\" alt=\"" + XMLCollection.getProperty("cerca.resultatsCerca.versioImprimir", lang) + "\" title=\"" + XMLCollection.getProperty("cerca.resultatsCerca.versioImprimir", lang) + "\"/></a>"+SALTLINIA;
            }
            html += "</div>" + SALTLINIA;
        }
        html += "<div id=\"barra_resultats_right\">" + SALTLINIA;
        if (hits.length() > 1) {
            html += "" + (docInicial + 1) + "-" + docFinal + " " + XMLCollection.getProperty("cerca.de", lang) + " " + hits.length() + " " + XMLCollection.getProperty("cerca.resultats", lang) + SALTLINIA;
        }
        if (hits.length() == 1) {
            html += "1 " + XMLCollection.getProperty("cerca.resultat", lang) + SALTLINIA;
        }

        html += "</div>" + SALTLINIA;
        html += "</div>" + SALTLINIA;
        return html;
    }

    private static String[] getLlistatSenseRepeticions(String tots) {
        String[] list = tots.split(" #");
        String aux;
        Hashtable map = new Hashtable();
        for (int i = 0; i < list.length; i++) {
            if (TipusFitxer.allTipusIds.containsKey(list[i].trim())) {
                aux = (String) TipusFitxer.allTipusIds.get(list[i].trim());
            } else {
                aux = (String) TipusFitxer.allTipusIds.get(UtilsCercador.converteixString(list[i]));
            }
            if (aux != null) {
                map.put(list[i].trim(), list[i].trim());
            }
        }
        String[] returnList = new String[map.size()];
        Enumeration kcont = map.keys();
        int i = 0;
        while (kcont.hasMoreElements()) {
            String elem = (String) kcont.nextElement();
            returnList[i++] = elem;
        }
        return returnList;
    }

    private static boolean isInTheList(String format, ArrayList list) {

        return (list != null && list.contains(format));
    }

    public static void htmlPrintResults(Hits hits, String sheetId, String usuari, String userGeneric, PrintWriter out, String lang, int incrusXTEC, String fitxa, int longitud_descripcio, int docInicial, int docFinal, String filtreRecurs) throws IOException {
        out.println(htmlPrintResults(hits, sheetId, usuari, userGeneric, lang, incrusXTEC, fitxa, longitud_descripcio, docInicial, docFinal, filtreRecurs));
    }

    public static String htmlPrintResults(Hits hits, String sheetId, String usuari, String userGeneric, String lang, int incrusXTEC, String fitxa, int longitud_descripcio, int docInicial, int docFinal, String filtreRecurs) throws IOException {
        int i;
        String html = "";
        String aux = "";

        if (hits.length() == 0) {
            html += "<br/>" + XMLCollection.getProperty("cerca.resultatsCerca.noResultats", lang) + SALTLINIA;
        } else {
            for (i = docInicial; i < docFinal; i++) {
//		i = 0;
//		while(i<docTotals && i < hits.length()){
                Document currentDoc = (Document) hits.doc(i);
                String format = currentDoc.get("format");
                boolean isfisic = "fisic".equals(currentDoc.get("recurs"));
                //if (esVisualitzable(format, filtreRecurs)){
                String titol = "";
                if (!"ca".equals(lang.toLowerCase())) {
                    titol = currentDoc.get("titol" + lang.substring(0, 1).toUpperCase() + lang.substring(1).toLowerCase());
                }
                if ("".equals(titol) || titol == null) {
                    titol = currentDoc.get("titol");
                }
                String autor = currentDoc.get("autor");
                String editor = currentDoc.get("editor");

                String descripcio = "";
                if (!"ca".equals(lang)) {
                    descripcio = currentDoc.get("descripcio" + lang.substring(0, 1).toUpperCase() + lang.substring(1).toLowerCase());
                }
                if ("".equals(descripcio) || descripcio == null) {
                    descripcio = currentDoc.get("descripcio");
                }

                String docUrl = currentDoc.get("url");
                String idRecurs = currentDoc.get("idRecurs");
                // String data = currentDoc.get("lom@lifeCycle@contribute@date@euncl@dateTime"+SALTLINIA;
                String data = currentDoc.get("dataPublicacio");
                int numComentaris = (new Integer(currentDoc.get("numComentaris"))).intValue();
                int numVisites = (new Integer(currentDoc.get("numVisites"))).intValue();
                float puntuacioMitja = (new Float(currentDoc.get("puntuacio"))).floatValue();

                String imgUrls = getImgsUrl(format);

                String urlToShow = "";
                if ((usuari == null) && userGeneric.equals("edu365")) {
                    urlToShow = docUrl;
                } else {
                    urlToShow = fitxa + "?idRecurs=" + idRecurs + "&sheetId=" + sheetId + "&nomUsuari=" + usuari + "&inxtec=" + incrusXTEC;
                }

                String textComentaris = numComentaris + " " + XMLCollection.getProperty("cerca.resultatsCerca.comentaris", lang);
                if (numComentaris == 1) {
                    textComentaris = textComentaris.substring(0, textComentaris.length() - 1);
                } else if (numComentaris == 0) {
                    if (usuari == null) {
                        textComentaris = "<a href=\"/" + Configuracio.contextWebAplicacio + "/loginSSO.jsp?logOn=true&comment=true\" target=\"_blank\">" + XMLCollection.getProperty("cerca.resultatsCerca.comental", lang) + "</a>";
                    } else {
                        textComentaris = "<a href=\"" + urlToShow + "\">" + XMLCollection.getProperty("cerca.resultatsCerca.comental", lang) + "</a>";
                    }
                }

                String textVisites;
                if (numVisites == 0) {
                    textVisites = "<a href=\"" + urlToShow + "\">" + XMLCollection.getProperty("cerca.resultatsCerca.visital", lang) + "</a>";
                } else if (numVisites == 1) {
                    textVisites = "1 " + XMLCollection.getProperty("cerca.resultatsCerca.visita", lang);
                } else {
                    textVisites = numVisites + " " + XMLCollection.getProperty("cerca.resultatsCerca.visites", lang);
                }

                //data = UtilsCercador.girarDataDMYguio(data).replaceAll("01/01/", "");
                String[] aData = UtilsCercador.girarDataDMYguio(data).split("/");
                data = aData[aData.length - 1];

                html += "<div id=\"resultat\">" + SALTLINIA;
                //	html += "  <p>" + imgUrl + "</p>"+SALTLINIA;

                String titolToShow = titol;
                if (titolToShow.length() > 75) {
                    //titolToShow = titolToShow.substring(0, 74) + "..";
                    titolToShow = cutText(titolToShow, 69, ' ') + " [..]";
                    html += "  &nbsp;<a id=\"" + idRecurs + "\"href=\"" + urlToShow + "\"><span class=\"tooltip\"><dfn>" + titol + "</dfn>" + titolToShow + "</span></a>" + SALTLINIA;
                } else {
                    html += "  &nbsp;<a id=\"" + idRecurs + "\"href=\"" + urlToShow + "\">" + titolToShow + "</a>" + SALTLINIA;
                }

                html += "&nbsp;";
                int j = 0;
                while (j < puntuacioMitja) {
                    html += "<img src=\"./imatges/stars-full.gif\" alt=\"\"/>";
                    j++;
                }
                while (j < 5) {
                    html += "<img src=\"./imatges/stars-empty.gif\" alt=\"\"/>";
                    j++;
                }

                html += "  <h1>" + textComentaris + " - " + textVisites + "&nbsp;" + imgUrls + "</h1>" + SALTLINIA;
                html += "<br/>" + SALTLINIA;
                html += "  <h2>" + UtilsCercador.getFNfromVCard(autor) + "</h2>" + SALTLINIA;
                if (hits.length() == 0) {
                    html += "<h2>";
                    if (isfisic) {
                        aux = UtilsCercador.getFNfromVCard(editor);
                        if (aux.trim().lastIndexOf(',') < aux.trim().length() - 2) {
                            aux += ",";
                        }
                        html += aux + "&nsbp;";
                    }
                    html += XMLCollection.getProperty("cerca.resultatsCerca.noResultats", lang) + "</h2>" + SALTLINIA;
                }
                html += "  <h4>" + data + "</h4>" + SALTLINIA;
                html += "  <h3><div id=\"" + i + "\">" + SALTLINIA;
                String cutDescription = cutDescription(descripcio, longitud_descripcio);
                html += cutDescription;
               // html +="     <input type=\"inline\" name=\"resultaslength0\" value=\"" + longitud_descripcio + "\"/>";
               // html +="     <input type=\"inline\" name=\"resultaslength\" value=\"" + descripcio.length() + "\"/>";
               // html +="     <input type=\"inline\" name=\"resultaslength1\" value=\"" + cutDescription.length() + "\"/>";


                if (cutDescription.length() != descripcio.length()) {
                    html += "<a href=\"javascript:showFull('" + i + "')\">[+]</a></div>" + SALTLINIA;
                    html += "<div id=\"short_" + i + "\" style=\"visibility:hidden;display:none\">" + cutDescription + "<a href=\"javascript:showFull('" + i + "')\">[+]</a></div>" + SALTLINIA;
                    html += "<div id=\"full_" + i + "\" style=\"visibility:hidden;display:none\">" + descripcio + "<a href=\"javascript:showShort('" + i + "')\">[-]</a></div>" + SALTLINIA;
                } else {
                    html += "</div>" + SALTLINIA;
                }

                html += "  </h3>" + SALTLINIA;
                html += "</div>" + SALTLINIA;
            }
        }

        // }
        return html;
    }

    public static String cutDescription(String descripcio, int longitudDescripcio) {
        return cutText(descripcio, longitudDescripcio, '.');
    }

    public static String cutText(String descripcio, int longitudDescripcio, char fi) {
        int cutPoint = -1;
        String descripcioRetorn = "";
        char myChar;
        try {
            if (descripcio.length() <= longitudDescripcio) {
                return descripcio;
            }
            cutPoint = longitudDescripcio;
            myChar = descripcio.charAt(cutPoint);
            //logger.debug("Text cut myChar 3... " + myChar);
            while ((myChar != fi) && (descripcio.charAt(cutPoint + 1)  != ' ') && cutPoint < (descripcio.length() - 1)) {
                cutPoint++;
                myChar = descripcio.charAt(cutPoint);
             //   logger.debug("Text cut myChar 4... " + myChar);
            }
            if (myChar == fi) {
             //   logger.debug("Text cut myChar 5... " + myChar);
                cutPoint++;
            }
            descripcioRetorn = descripcio.substring(0, cutPoint);
           // logger.debug("Text cut longitudDescripcio 1... " + longitudDescripcio);
           // logger.debug("Text cut descripcio.length 2... " + descripcio.length());


            logger.debug("Text cut... " + cutPoint);
        } catch (Exception e) {
            logger.error(e);
        }
        return descripcioRetorn;
    }

    public static String getImgsUrl(String format) {
        String imgsUrl = "";
        try {
            if (format == null) {
                format = "";
            } else {
                format = format.trim();
                if (format.endsWith("#")) {
                    format = format.substring(0, (format.length() - 1));
                }
            }
            logger.info("Format to tokenize... " + format);
            StringTokenizer myTokenizer = new StringTokenizer(format, "#");
            while (myTokenizer.hasMoreTokens()) {
                String singleFormat = (String) myTokenizer.nextToken();
                singleFormat = singleFormat.trim();
                String imgUrl = "";
                logger.debug("Single format... " + singleFormat);
                if ((singleFormat != null) && !singleFormat.equals("")) {
                    imgUrl = UtilsCercador.getImageFormat(singleFormat);
                } else {
                    imgUrl = "imatges/icones/altres.gif";
                    singleFormat = "Altres";
                }
                if ((imgUrl == null) || imgUrl.equals("")) {
                    imgUrl = "imatges/icones/altres.gif";
                }
//CHANGES NADIM --> 20/10/2014
                String title = UtilsCercador.toAcute(singleFormat).trim();
//REMOVED NADIM --> 20/10/2014
                //String title = (String) TipusFitxer.allTipusIds.get(singleFormat);
                logger.debug("Single format no acute... " + singleFormat);

                imgsUrl += "<img src=\"" + imgUrl + "\" alt=\"" + title + "\" title=\"" + title + "\"/>";
            }
            if (imgsUrl.equals("")) {
                String imgUrl = "imatges/icones/altres.gif";
                String singleFormat = "Altres";
                imgsUrl += "<img src=\"" + imgUrl + "\" alt=\"" + singleFormat + "\" title=\"" + singleFormat + "\"/>";
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return imgsUrl;
    }

    public static void htmlOrderBy(String ordenacio, boolean direccio, String lang, PrintWriter out) {
        out.println(htmlOrderBy(ordenacio, direccio, lang, ""));
    }

    public static void htmlOrderBy(String ordenacio, boolean direccio, String lang, String recurs, PrintWriter out) {
        out.println(htmlOrderBy(ordenacio, direccio, lang, recurs));
    }

    public static String htmlOrderBy(String ordenacio, boolean direccio, String lang, String recurs) {
        String html = "";
        html += "<div id=\"resultats_right\">" + SALTLINIA;
        if (recurs != null && !"".equals(recurs)) {
            html += "<p style=\"float:left;\">Mostrant el recurs " + recurs + " - <a href=\"#\" onclick=\"doSubmit();\">mostrar-los tots</a></p>" + SALTLINIA;;
        }
        html += "  <p>" + XMLCollection.getProperty("cerca.resultatsCerca.ordenaPer", lang) + SALTLINIA;
        if (ordenacio.equals("") || ordenacio.equals("defecte")) {
            html += "  <a id=\"current\" href=\"javascript:sortBy('defecte', " + !direccio + ");\">" + XMLCollection.getProperty("cerca.resultatsCerca.defecte", lang) + "</a>" + SALTLINIA;
        } else {
            html += "  <a href=\"javascript:sortBy('defecte');\">" + XMLCollection.getProperty("cerca.resultatsCerca.defecte", lang) + "</a>" + SALTLINIA;
        }
        if (ordenacio.equals("data")) {
            html += "  <a id=\"current\" href=\"javascript:sortBy('data', " + !direccio + ");\">" + XMLCollection.getProperty("cerca.resultatsCerca.data", lang) + "</a>" + SALTLINIA;
        } else {
            html += "  <a href=\"javascript:sortBy('data');\">" + XMLCollection.getProperty("cerca.resultatsCerca.data", lang) + "</a>" + SALTLINIA;
        }
        if (ordenacio.equals("alfabeticament")) {
            html += "  <a id=\"current\" href=\"javascript:sortBy('alfabeticament', " + !direccio + ");\">"
                    + XMLCollection.getProperty("cerca.resultatsCerca.alfabeticament", lang) + "</a>" + SALTLINIA;
        } else {
            html += "  <a href=\"javascript:sortBy('alfabeticament');\">"
                    + XMLCollection.getProperty("cerca.resultatsCerca.alfabeticament", lang) + "</a>" + SALTLINIA;
        }
        if (ordenacio.equals("visites")) {
            html += "  <a id=\"current\" href=\"javascript:sortBy('visites', " + !direccio + ");\">" + XMLCollection.getProperty("cerca.resultatsCerca.visites", lang) + "</a>" + SALTLINIA;
        } else {
            html += "  <a href=\"javascript:sortBy('visites');\">" + XMLCollection.getProperty("cerca.resultatsCerca.visites", lang) + "</a>" + SALTLINIA;
        }
        if (ordenacio.equals("puntuacio")) {
            html += "  <a id=\"current\" href=\"javascript:sortBy('puntuacio', " + !direccio + ");\">" + XMLCollection.getProperty("cerca.resultatsCerca.puntuacio", lang) + "</a>" + SALTLINIA;
        } else {
            html += "  <a href=\"javascript:sortBy('puntuacio');\">" + XMLCollection.getProperty("cerca.resultatsCerca.puntuacio", lang) + "</a>" + SALTLINIA;
        }
        if (ordenacio.equals("comentaris")) {
            html += "  <a id=\"current\" href=\"javascript:sortBy('comentaris', " + !direccio + ");\">" + XMLCollection.getProperty("cerca.resultatsCerca.comentaris", lang) + "</a>" + SALTLINIA;
        } else {
            html += "  <a href=\"javascript:sortBy('comentaris');\">" + XMLCollection.getProperty("cerca.resultatsCerca.comentaris", lang) + "</a>" + SALTLINIA;
        }
        html += "</p>" + SALTLINIA;
        html += "</div>" + SALTLINIA;

        return html;
    }

}
