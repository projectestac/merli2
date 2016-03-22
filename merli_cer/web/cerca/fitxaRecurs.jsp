<%@ page import="java.sql.*,java.util.Map,java.util.HashMap,java.util.Set,simpple.xtec.web.util.Configuracio,simpple.xtec.web.util.FitxaRecurs,simpple.xtec.web.util.ComentariObject" %>
<%@ page import="simpple.xtec.web.util.TipusFitxer,org.apache.lucene.analysis.standard.StandardAnalyzer,org.apache.lucene.queryParser.QueryParser,org.apache.lucene.search.*,org.apache.lucene.document.Document" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, java.util.Iterator, java.util.Enumeration , java.util.ArrayList, java.util.StringTokenizer, simpple.xtec.web.util.Indexador ,simpple.xtec.web.util.UtilsCercador, simpple.xtec.web.util.RecursObject, simpple.xtec.web.util.XMLCollection, java.util.Hashtable, java.util.Enumeration, simpple.xtec.web.util.DucObject" %>
<%@ page import="simpple.xtec.web.util.ResultGeneratorUtil"%>
<%@ page import="java.util.Comparator"%>
<%@ page import="simpple.xtec.web.analisi.EducacioAnalyzer"%>
<%@ page pageEncoding="UTF-8" %>


<%
    Logger logger = Logger.getLogger("fitxaRecurs.jsp");
    String idRecurs = request.getParameter("idRecurs");
    logger.debug("idRecurs -> " + idRecurs);
    String sLang = XMLCollection.getLang(request);

    String imprimir = request.getParameter("imprimir");
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
    urlImprimir = "javascript:window.print();";

    String sheetId = (String) session.getAttribute("sheetId");
    if (sheetId == null) {
        sheetId = "";
    }
    logger.info("sheetId -> " + sheetId);
    session.setAttribute("lastUrl", UtilsCercador.getLastUrl(request));
    if (Configuracio.isVoid()) {
        Configuracio.carregaConfiguracio();
    }
    Connection myConnection = UtilsCercador.getConnectionFromPool();
    String userGeneric = (String) session.getAttribute("userGeneric");
    if (userGeneric == null) {
        userGeneric = "";
    }
    logger.debug("Getting userGeneric -> " + userGeneric);
    String usuari = (String) session.getAttribute("nomUsuari");
    //usuari = "sarjona";

    int comentarisSuspesos = 0;
    String usuariNomComplet = "";
    if (usuari == null) {
        //usuari = (String)request.getRemoteUser();
        usuari = (String) session.getAttribute("user");
    }

    logger.info("Usuari -> " + usuari);

    usuariNomComplet = (String) session.getAttribute("usuariNomComplet");
    logger.info("usuariNomComplet -> " + usuariNomComplet);

    String inxtecString = (String) request.getParameter("inxtec");
    int inxtec = 0;
    if (inxtecString != null) {
        try {
            inxtec = Integer.parseInt(inxtecString);
        } catch (Exception e) {
        }
    }
    FitxaRecurs fitxaRecurs = new FitxaRecurs(myConnection);

    if (usuariNomComplet == null) {

        usuariNomComplet = UtilsCercador.getNomComplet(usuari);

        session.setAttribute("usuariNomComplet", usuariNomComplet);

    }

    comentarisSuspesos = fitxaRecurs.getComentarisSuspesos(usuari);

    String urlLocal = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio;

    String cssFilePrint = urlLocal + "/css/fitxaRecurs-print.css";

    String query = "";

    double idComentariBD = -1;
    String comentariBD = "";
    String titolBD = "";
    int puntuacioBD = -1;
    String operacio = "afegir";
    String textBoto = "Afegir comentari";
    float puntuacioMitja = (float) 0.0;
    int numComentaris = 0;
    // IndexSearcher mySearcher = null;
    Hashtable nivellsArees = null;
    Hashtable duc = null;

    try {
     //puntuacioMitja = (int)fitxaRecurs.getPuntuacioMitja(idRecurs);          

        //logger.debug("Puntuacio mitja -> " + puntuacioMitja);       
        //numComentaris = fitxaRecurs.getNumComentaris(idRecurs);
        //logger.debug("Puntuacio mitja -> " + puntuacioMitja);
        ComentariObject comentariObject = fitxaRecurs.getComentariByUsuari(usuari, idRecurs);

        if (comentariObject != null) {
            logger.debug("Checking comentariObject...");
            idComentariBD = comentariObject.id;

            logger.debug("idComentariBD -> " + idComentariBD);
            comentariBD = comentariObject.comentari;

            titolBD = comentariObject.titol;
            logger.debug("titolBD -> " + titolBD);

            puntuacioBD = comentariObject.puntuacio;
            logger.debug("puntuacioBD -> " + puntuacioBD);

            operacio = "editar";
            textBoto = "Editar comentari";
        }

        String indexActual = null;

        if ((indexActual == null) || indexActual.trim().equals("")) {

            indexActual = UtilsCercador.getIndexActual(Configuracio.indexDir, Configuracio.indexDir2);
        }

        logger.info("Index actual: " + indexActual);
//      mySearcher = new IndexSearcher(indexActual);

        logger.info("Id recurs: " + idRecurs);
        //  Document luceneDocument = fitxaRecurs.getDocument (mySearcher, idRecurs);
        Document luceneDocument = fitxaRecurs.getDocument(indexActual, idRecurs);

        ArrayList allLevels = (ArrayList) session.getAttribute("levels");
        Hashtable allCicles = (Hashtable) session.getAttribute("cicles");
        Hashtable allAreas = (Hashtable) session.getAttribute("areas");

        if (allLevels == null) {
            logger.debug("Loading DUC (Levels) ...");
            allLevels = UtilsCercador.getAllLevels(myConnection);
            logger.debug("Loading DUC (Cicles) ...");
            allCicles = UtilsCercador.getAllCicles(myConnection, allLevels);
            logger.debug("Loading DUC (Areas) ...");
            allAreas = UtilsCercador.getAllAreas(myConnection, allLevels);
            session.setAttribute("levels", allLevels);
            session.setAttribute("cicles", allCicles);
            session.setAttribute("areas", allAreas);
        } else {
            logger.debug("Cached!");
        }

        String contextWeb = Configuracio.contextWebAplicacio;
        String cercaCompleta = urlLocal + "/cerca/cercaCompleta.jsp";

        if (inxtec == 2) {
            inxtec = 3;
        }
        if (inxtec > 0) {
            cercaCompleta += "?inxtec=" + inxtec;
        }
%>  
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html lang="ca">
    <%= ResultGeneratorUtil.htmlHeader(urlLocal, "cerca.fitxaRecurs.fitxaRecurs", null, sLang)%>

<!-- <link rel="stylesheet" type="text/css" href="<%=urlLocal%>/css/cercador_xtec.css"/> -->
    <script type="text/javascript" src="<%=urlLocal%>/scripts/ajax-organitzador.js"></script>
    <script type="text/javascript">
        <%try {%>
        <%= ResultGeneratorUtil.getDucJSList(allLevels, allAreas, sLang)%>

        <%= ResultGeneratorUtil.functionJSchange_area(sLang)%>

        <%} catch (Exception e) {
            }%>


        function doSubmit() {
            var value = document.cerca.textCerca.value;
            document.cerca.textCerca.value = value.toString();

            document.cerca.nivell.value = 0;
            document.cerca.tipus.value = 'simple';
            document.cerca.novaCerca.value = 'si';
            document.cerca.submit();
        }

        function executaSubmit() {
            if (document.afegirComentari.titol.value == '') {
                alert('<%=UtilsCercador.fromAcute(XMLCollection.getProperty("cerca.fitxaRecurs.titolBuit", sLang))%>');
                return false;
            }
            if (document.afegirComentari.comentari.value == '') {
                alert('<%=UtilsCercador.fromAcute(XMLCollection.getProperty("cerca.fitxaRecurs.comentariBuit", sLang))%>');
                return false;
            }
            document.afegirComentari.submit();
        }

        var firstStartFull = false;

        function fillStars(numStars) {
            var i = 1;
            if (numStars == 1) {
                if (!firstStartFull) {
                    firstStartFull = true;
                    document.getElementById("star1").src = '../imatges/stars-full.gif';
                    document.afegirComentari.puntuacio.value = 1;
                } else {
                    firstStartFull = false;
                    document.getElementById("star1").src = '../imatges/stars-empty.gif';
                    document.afegirComentari.puntuacio.value = 0;
                }
                for (i = 2; i <= 5; i++) {
                    document.getElementById("star" + i).src = '../imatges/stars-empty.gif';
                }
            } else {

                document.afegirComentari.puntuacio.value = numStars;
                for (i = 1; i <= 5; i++) {
                    if (i <= numStars) {
                        document.getElementById("star" + i).src = '../imatges/stars-full.gif';
                    } else {
                        document.getElementById("star" + i).src = '../imatges/stars-empty.gif';
                    }
                }
            }
        }
        <%-- /*
           function change_area(){

            var nivell_educatiu
            nivell_educatiu = document.cerca.nivell_educatiu[document.cerca.nivell_educatiu.selectedIndex].value

            if (nivell_educatiu != 0) {


               mis_areas=eval("areas_" + nivell_educatiu)

               num_areas = (mis_areas.length / 2)


               document.cerca.area_curricular.length = num_areas

               for(i=0;i<(num_areas * 2);i=i+2){
                  document.cerca.area_curricular.options[i/2].text=mis_areas[i]
                  document.cerca.area_curricular.options[i/2].value=mis_areas[i + 1]	          
               }
            }else{

               document.cerca.area_curricular.length = 1

               document.cerca.area_curricular.options[0].value = "-1"
               document.cerca.area_curricular.options[0].text = "<%=XMLCollection.getProperty("cerca.select.nivell", sLang)%>"
            }

            document.cerca.area_curricular.options[0].selected = true
        } */ --%>

        function mostrarOculta() {
            document.getElementById('afegir_comentari_ocult').style.display = 'block';
            document.getElementById('formulari_comentari_ocult').style.display = 'block';
            document.getElementById('afegir_comentari').style.display = 'none';

        }
        function tancarOculta() {
            document.getElementById('afegir_comentari').style.display = 'block';
            document.getElementById('afegir_comentari_ocult').style.display = 'none';
            document.getElementById('formulari_comentari_ocult').style.display = 'none';
        }

        function mostraAmagaDisponibilitat(elem)
        {
            if (elem.innerHTML.indexOf("+") >= 0)
            {
                document.getElementById('mesDispos').style.display = 'inline';
                elem.innerHTML = '-&nbsp;informaci&oacute;';
            }
            else
            {
                document.getElementById('mesDispos').style.display = 'none';
                elem.innerHTML = '+&nbsp;informaci&oacute;';
            }
        }

    </script>
    <script type="text/javascript" src="<%=urlLocal%>/scripts/ietooltips.js"></script>

    <!--[if IE 6]>
    <link rel="stylesheet" href="<%=urlLocal%>/css/ie6.css" type="text/css" />
    <![endif]-->

</head>
<body>
    <div id="non-footer">
        <% if (imprimir.equals("no")) { %>
        <%if (inxtec <= ResultGeneratorUtil.SHOW_ALL) {%>
        <div id="header">		    
            <!--  empieza cabecera   -->    
            <%=ResultGeneratorUtil.htmlHeaderCabecera("/" + Configuracio.contextWebAplicacio, "edu365.cat", UtilsCercador.getLastUrl(request))%>                        

            <%=ResultGeneratorUtil.htmlHeaderMenu(usuari, usuariNomComplet, "/" + Configuracio.contextWebAplicacio, comentarisSuspesos, sLang)%>
            <%} else {%>
            <%=ResultGeneratorUtil.htmlHeaderNoCapcalera()%>
            <%}%>  
            <%if (inxtec < ResultGeneratorUtil.SHOW_ONLYRESULTS) {%>
            <div id="barra_buscador">		             			 		                      	                      
                <div id="cercadorOptions">
                    <form name="cerca" action="/<%=Configuracio.contextWebAplicacio%>/ServletCerca" method="get">
                        <fieldset>
                            <input type="hidden" name="tipus" value="simple"/>
                            <input type="hidden" name="nivell" value="0"/>
                            <input type="hidden" name="ordenacio" value=""/>
                            <input type="hidden" name="direccio" value=""/>
                            <input type="hidden" name="novaCerca" value="si"/>	
                            <input type="hidden" name="usuari" value="<%=usuari%>"/>
                            <input type="hidden" name="userGeneric" value="<%=userGeneric%>"/>
                            <input type="hidden" name="nomUsuari" value="<%=usuari%>"/>            
                            <input type="hidden" name="cataleg" value="si"/>            
                            <input type="hidden" name="inxtec" value="<%=inxtec%>"/> 
                            <label for="textCerca" class="hide">Introdu&iuml;u les paraules a cercar</label>
                            <input type="text" size="20" name="textCerca" id="textCerca" tabindex="1" alt="text cerca" class="textCerca" value="<%=(session.getAttribute("textCerca") != null) ? (String) session.getAttribute("textCerca") : ""%>"/>
                            <select id="nivell_educatiu" name="nivell_educatiu" onchange="change_area()" tabindex="2" class="select">		   
                                <option value="-1" selected ><%=XMLCollection.getProperty("cerca.select.nivell", sLang)%></option>
                                <%
                                    int i = 0;
                                    while (i < allLevels.size()) {
                                        DucObject ducObject = (DucObject) allLevels.get(i);
                                        // FIXME: Treure quan FP estigui disponible al DUC
                                        //if(ducObject.getTerm(sLang).indexOf("FP")<0){
%>   
                                <option value="<%=ducObject.id%>"><%=ducObject.getTerm(sLang)%></option>
                                <%
                                        //}
                                        i++;
                                    }
                                %>
                            </select>
                            <select id="area_curricular" name="area_curricular" tabindex="3" class="select">		   
                                <option value="-1" selected ><%=XMLCollection.getProperty("cerca.select.area", sLang)%></option>
                            </select><button class="butoMerli small red" onClick="javascript:doSubmit();
                                    return false;"><%=XMLCollection.getProperty("cerca.directoriInicial.cerca", sLang)%></button>

                            <a href="<%=cercaCompleta%>"><%=XMLCollection.getProperty("cerca.directoriInicial.cercaCompleta", sLang)%></a>

                            &nbsp; <a href="<%=XMLCollection.getProperty("url.ajuda", sLang)%>" target="_blank"><img style="border:0;" src="../imatges/ajuda.png"/></a>

                                         <!--input type="image" class="button" src="/<%=Configuracio.contextWebAplicacio%>/imatges/boton_cerca.gif" value="<%=XMLCollection.getProperty("cerca.directoriInicial.cerca", sLang)%>" tabindex="4"/>
                            -->
                        </fieldset>
                    </form>
                </div>		                                                                  
                <!--div id="cercadorButton">  	                                                              
                  <a href="<%=cercaCompleta%>">              
                    <img src="/<%=Configuracio.contextWebAplicacio%>/imatges/boton_cerca_avancada.gif" alt="<%=XMLCollection.getProperty("cerca.directoriInicial.cercaCompleta", sLang)%>" border="0" /></a>
                </div-->	  	                           		  	            	                       	                  
            </div>
            <%}%>
            <%--div id="barra_resultats">		  	            			
              <div id="barra_fitxa_left"><b><%=XMLCollection.getProperty("cerca.fitxaRecurs.fitxa", sLang)%></b></div>			
              <div id="barra_fitxa_right">	<!-- 		1-10 de 582 resultats  --></div>	    
            </div--%>
        </div>
        <!--  hasta aqui cabecera   -->    



        <% } %>

        <div id="container_fitxa">


            <% if (luceneDocument != null) {
                    boolean isfisic = "fisic".equals(luceneDocument.get("recurs"));
            %>
            <div id="fitxa">
                <div>
                    <%
                        String titol = "";
                        if (!"ca".equals(sLang.toLowerCase())) {
                            titol = luceneDocument.get("titol" + sLang.substring(0, 1).toUpperCase() + sLang.substring(1).toLowerCase());
                        }
                        if ("".equals(titol) || titol == null) {
                            titol = luceneDocument.get("titol");
                        }
                    %>	
                    <h1><% if (!luceneDocument.get("url").equals("")) {%><a class="" target="_blank" href="<%=luceneDocument.get("url")%>"><%=titol%></a><% } else {%><%=titol%><% } %></h1>
                    <div id="comentaris">
                        <% puntuacioMitja = (new Float(luceneDocument.get("puntuacio"))).floatValue(); %>
                        <% if (puntuacioMitja <= 0) { %>
                        <img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                        <% } %>
                        <% if (puntuacioMitja == 1) { %>
                        <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                        <% } %>
                        <% if (puntuacioMitja == 2) { %>
                        <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                        <% } %>
                        <% if (puntuacioMitja == 3) { %>
                        <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                        <% } %>
                        <% if (puntuacioMitja == 4) { %>
                        <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif"  alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                        <% } %>
                        <% if (puntuacioMitja == 5) { %>
                        <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/>
                        <% } %>
                        <%	     numComentaris = (new Integer(luceneDocument.get("numComentaris"))).intValue();
                            String textComentaris = numComentaris + " " + XMLCollection.getProperty("cerca.resultatsCerca.comentaris", sLang);
                            if (numComentaris == 1) {
                                textComentaris = textComentaris.substring(0, textComentaris.length() - 1);
                            } else if (numComentaris == 0) {

                                if (usuari == null) {
                                    textComentaris = "<a href=\"/" + Configuracio.contextWebAplicacio + "/loginSSO.jsp?logOn=true&comment=true\" target=\"_blank\">" + XMLCollection.getProperty("cerca.resultatsCerca.comental", sLang) + "</a>";
                                } else {
                                    textComentaris = "<a href=\"#comentaris\" onclick=\"javascript:mostrarOculta();\">" + XMLCollection.getProperty("cerca.resultatsCerca.comental", sLang) + "</a>";
                                }
                            }
                        %>
                        <%=textComentaris%>
                        <%
                            int numVisites = (new Integer(luceneDocument.get("numVisites"))).intValue();
                            String textVisites;
                            if (numVisites <= 1) {
                                textVisites = "1 " + XMLCollection.getProperty("cerca.resultatsCerca.visita", sLang);
                            } else {
                                textVisites = numVisites + " " + XMLCollection.getProperty("cerca.resultatsCerca.visites", sLang);
                            }

                        %> - <%=textVisites%>
                    </div>
                    <%

                        String format = luceneDocument.get("format");
                        logger.debug("Checking format... " + format);
                        if (format == null) {
                            format = "";
                        } else {
                            format = format.trim();
                            if (format.endsWith("#")) {
                                format = format.substring(0, (format.length() - 1));
                            }
                        }
                        if (format.equals("")) {
                    %>
                    <img src="../imatges/icones/altres.gif" alt="Altres" title="Altres"/>
                    <%
                        }
                        logger.debug("Format to tokenize... " + format);
                        StringTokenizer myTokenizer = new StringTokenizer(format, "#");
                        while (myTokenizer.hasMoreTokens()) {
                            String singleFormat = (String) myTokenizer.nextToken();
                            singleFormat = singleFormat.trim();
                            String imgUrl = "";
                            String title = "";
                            logger.debug("Single format... " + singleFormat);
                            if ((singleFormat != null) && !singleFormat.equals("")) {
                                imgUrl = UtilsCercador.getImageFormat(singleFormat);
                                title = (String) TipusFitxer.allTipusIds.get(singleFormat);
                            } else {
                                imgUrl = "imatges/icones/altres.gif";
                                singleFormat = "Altres";
                            }
                            if ((imgUrl == null) || imgUrl.equals("")) {
                                imgUrl = "imatges/icones/altres.gif";
                            }
                            singleFormat = UtilsCercador.toAcute(singleFormat).trim();
                            logger.debug("Single format no acute... " + singleFormat);
                    %>
                    <img src="../<%=imgUrl%>" alt="<%=title%>" title="<%=title%>"/>
                    <% } %>
                </div>
                <%-- if (imprimir.equals("no")) { %>
                       <div id="imprimir">
                       <a href="<%=urlImprimir%>"><%=XMLCollection.getProperty("cerca.fitxaRecurs.versioImprimir", sLang)%></a>
                       </div>
               <% } --%>
                <div id="cos_blau">
                    <dl>
                        <p>
                            <%if (luceneDocument.get("autor") != null && !"".equals(luceneDocument.get("autor"))) {
                                    String aux = UtilsCercador.getFNfromVCard(luceneDocument.get("autor")).trim();
                                    //if (aux.lastIndexOf(",") < aux.length()-2){aux += ",";}
                                    out.print(aux);
                                }%>
                        </p>
                        <p>
                            <%
                                if (isfisic && luceneDocument.get("editor") != null && !"".equals(luceneDocument.get("editor"))) {%>
                            <%=UtilsCercador.getFNfromVCard(luceneDocument.get("editor")).trim()%>,
                            <%
                                }
                                if (luceneDocument.get("dataPublicacio") != null && !"".equals(luceneDocument.get("dataPublicacio"))) {
                                    //UtilsCercador.girarDataDMYguio(luceneDocument.get("dataPublicacio")).replaceAll("1/1/","").replaceAll("01/01/","")
                                    String[] aData = UtilsCercador.girarDataDMYguio(luceneDocument.get("dataPublicacio")).split("/");
                                    out.print(aData[aData.length - 1]);
                                }
                            %>
                        </p>
                        <p>
                            <%String descripcio = "";
                                if (!"ca".equals(sLang)) {
                                    descripcio = luceneDocument.get("descripcio" + sLang.substring(0, 1).toUpperCase() + sLang.substring(1).toLowerCase());
                                }
                                if ("".equals(descripcio) || descripcio == null) {
                                    descripcio = luceneDocument.get("descripcio");
                                }
                                if (descripcio != null && !"".equals(descripcio)) {%>
                            <%=descripcio%>
                            <%} else { %>
                            &nbsp; 
                            <%} %>
                        </p>

                        <%  String coverage = "";
                            coverage = luceneDocument.get("coverage");

                            if (coverage != null && !"".equals(coverage)) {%>
                        <!-- 
                        <p>
                        <%=coverage%>
                </p>
                        -->
                        <%} %>


                        <!-- Relacions -->
                        <p>
                            <%
                                String ri = luceneDocument.get("relacioIds");
                                String rt = luceneDocument.get("relacioTipus");
                                String rd = luceneDocument.get("relacioDesc");
                                if (ri != null && rt != null && rd != null)
                                    if (ri.length() > 5) {
                                        String[] ril = ri.split(";");
                                        String[] rtl = rt.split(";");
                                        String[] rdl = rd.split("#");
                                        String aux, tipusRelacio;
                                        ArrayList alis;
                                        Hashtable vals = new Hashtable();
                                        for (int i = 0; i < ril.length; i++) {
                                            if (!vals.containsKey(rtl[i])) {
                                                vals.put(rtl[i], new ArrayList());
                                            }
                                            aux = "<a href=\"fitxaRecurs.jsp?idRecurs=/" + ril[i] + "\">" + rdl[i] + "</a>";
                                            ((ArrayList) vals.get(rtl[i])).add(aux);
                                        }
                                        Iterator itr = (Iterator) vals.keys();
                                        while (itr.hasNext()) {
                                            aux = (String) itr.next();
                                            boolean b = aux.indexOf("de") > 0;
                                            if (b) {
                                                tipusRelacio = "Forma part de";
                                            } else {
                                                tipusRelacio = "Inclou";
                                            }

                                            alis = (ArrayList) vals.get(aux);
                            %><p><%=tipusRelacio%>: <%
                                for (int i = 0; i < alis.size(); i++) {
                                    if (i > 0) {
                                %>; <%
                                    }
                                %><%=alis.get(i)%><%
                                    }
                                %></p><%
                                            }
                                        }
                                %>
                        </p>
                        <!-- URL -->
                        <%
                            String urlRecurs = luceneDocument.get("url");
                            logger.debug("Url recurs -> " + urlRecurs);
                            String urlTooltip = "";
                            int tooltipSize = 90;
                            int cutPoint = 0;
                            boolean cutString = false;
                            if (urlRecurs.length() > 90) {
                                cutString = true;
                                int initPoint = 0;
                                while (initPoint < urlRecurs.length()) {
                                    if (urlRecurs.length() < initPoint + tooltipSize) {
                                        cutPoint = urlRecurs.length();
                                    } else {
                                        cutPoint = initPoint + tooltipSize;
                                    }
                                    urlTooltip += urlRecurs.substring(initPoint, cutPoint) + " ";
                                    initPoint += tooltipSize;
                                }
                                urlRecurs = urlRecurs.substring(0, 90) + "...";
                            }
                        %>     
                        <p> 
                            <a target="_blank" href="<%=luceneDocument.get("url")%>">
                                <% // Treiem tooltip
                                    //      if (1 == 2) { 
                                    if (urlRecurs.length() > 90) {
                                %>
                                <span class="tooltip"><dfn><%=urlTooltip%></dfn>
                                        <%   }  %> 
                                        <% if (imprimir.equals("no")) {%>
                                        <%=urlRecurs%>
                                        <% } else {%>
                                        <%=urlTooltip%>
                                        <% } %>
                            </a>

                        </p>

                        <div class="espaiFitxa">&nbsp;</div>
                        <!-- Termes i paraules clau-->
                        <%
                            String trm = luceneDocument.get("terms" + sLang.substring(0, 1).toUpperCase() + sLang.substring(1));
                            if (trm != null) {
                                trm += luceneDocument.get("keys" + sLang.substring(0, 1).toUpperCase() + sLang.substring(1));

                                if (trm.length() > 2) {
                        %>
                        <p>
                            <dt>
                                <%=XMLCollection.getProperty("cerca.fitxaRecurs.paraules", sLang)%>:
                            </dt>
                            <dd>
                                <%
                                    String[] trl = trm.trim().split("#");
                                    String aux = "";

                                    for (int i = 0; i < trl.length; i++) {
                                        if (trl[i] != null) {
                                            if (i > 0) {
                                                aux += ", ";
                                            }
                                            aux += trl[i].trim();
                                        }
                                    }
                                %>
                                <%=aux%>
                            </dd>
                        </p>
                        <%
                                }
                            }
                        %>

                        <!-- Descriptors DUC -->
                        <%
                            nivellsArees = fitxaRecurs.getNivellsArees((String) luceneDocument.get("duc"), sLang);
                            duc = fitxaRecurs.getNivellsAreesContents((String) luceneDocument.get("duc"), sLang);
                            String html = "";
                            if (duc != null) {
                                Enumeration kcont = ((Hashtable) duc.get("content")).keys();
                                Enumeration kar;
                                Enumeration kle;
                                String cont = "";
                                String are = "";
                                String lev = "";
                                Hashtable resultat = new Hashtable();
                                while (kcont.hasMoreElements()) {
                                    cont = (String) kcont.nextElement();
                                    if (cont != null) {
                                        are = (String) ((Hashtable) duc.get("area")).get(cont);
                                        if (are != null) {
                                            are = (String) duc.get(are);
                                        } else {
                                            are = "";
                                        }
                                        lev = (String) ((Hashtable) duc.get("level")).get(cont);
                                        if (lev != null && !"".equals(lev)) {
                                            lev = (String) duc.get(lev);
                                            if (lev != null && !"".equals(lev)) {
                                                lev = lev + " - " + are;
                                            } else {
                                                lev = are;
                                            }
                                        } else {
                                            lev = are;
                                        }

                                        cont = (String) duc.get(cont);
                                        if (resultat.containsKey(lev)) {
                                            cont = (String) resultat.get(lev) + "<span class=\"contentDUC\">,&nbsp; " + cont + "</span>";
                                        } else {
                                            cont = "<span class=\"contentDUC\">: " + cont + "</span>";
                                        }
                                        resultat.put(lev, cont);
                                    }
                                }
                                // Tractament Arees
                                kcont = ((Hashtable) duc.get("level")).keys();
                                while (kcont.hasMoreElements()) {
                                    cont = (String) kcont.nextElement();
                                    if (cont != null) {
                                        if (((Hashtable) duc.get("area")).contains(cont) || ((Hashtable) duc.get("content")).containsKey(cont)) {
                                            // Es un contingut real, no fem res
                                        } else {
                                            // Es una area sense continguts
                                            are = (String) duc.get(cont);
                                            lev = (String) ((Hashtable) duc.get("level")).get(cont);
                                            if (lev != null && !"".equals(lev)) {
                                                lev = (String) duc.get(lev);
                                                if (lev != null && !"".equals(lev)) {
                                                    lev = lev + " - " + are;
                                                } else {
                                                    lev = (String) ((Hashtable) duc.get("level")).get(cont);
                                                    lev = (String) ((Hashtable) duc.get("parent_level")).get(lev);
                                                    if (lev != null && !"".equals(lev)) {
                                                        lev = lev + " - " + are;
                                                    } else {
                                                        lev = are;
                                                    }
                                                }
                                            } else {
                                                lev = are;
                                            }
                                            if (!resultat.contains(lev)) {
                                                resultat.put(lev, "");
                                            } else {
                                                resultat.put(lev + " ", "");
                                            }
                                        }
                                    }
                                }

                                kcont = resultat.keys();
                                int ccc = 0;
                                lev = "";
                                while (kcont.hasMoreElements()) {
                                    cont = (String) kcont.nextElement();
                                    if (ccc > 0) {
                                        lev += "; ";
                                    }
                                    lev += cont + "" + (String) resultat.get(cont);
                                    ccc++;
                                }
                                if (ccc > 0) {
                        %>
                        <script language="JavaScript">
                            function setVisibleByClass(classname, elem, mes, node) {
                                if (!node) {
                                    node = document.getElementsByTagName('body')[0];
                                }
                                if (mes)
                                {
                                    elem.innerHTML = "-&nbsp;informaci&oacute;";
                                    elem.onclick = function() {
                                        setVisibleByClass('contentDUC', this, false);
                                        return false;
                                    };
                                    disp = "inline";
                                }
                                else
                                {
                                    elem.innerHTML = "+&nbsp;informaci&oacute;";
                                    elem.onclick = function() {
                                        setVisibleByClass('contentDUC', this, true);
                                        return false;
                                    };
                                    disp = "none";
                                }
                                var a = [], re = new RegExp('\\b' + classname + '\\b');
                                els = node.getElementsByTagName('*');
                                for (var i = 0, j = els.length; i < j; i++) {
                                    if (re.test(els[i].className)) {
                                        els[i].style.display = disp;
                                    }
                                }

                                return a;
                            }
                        </script>
                        <style>
                            .contentDUC{display:none; color:#7e7e7e;}
                        </style>
                        <dt>
                            <%=XMLCollection.getProperty("cerca.fitxaRecurs.duc", sLang)%>:
                        </dt>
                        <dd>
                            <%=lev%>
                            <a href="#" onclick="setVisibleByClass('contentDUC', this, true);
                                    return false;"><%=XMLCollection.getProperty("cerca.fitxaRecurs.mesinfo", sLang)%></a>
                        </dd>
                        <%	}
                            }
                        %>	

                        <%-- Enumeration keys = nivellsArees.keys();
                           if (keys.hasMoreElements()){%>
                                <dt><%=XMLCollection.getProperty("cerca.fitxaRecurs.nivellEducatiu", sLang)%> / <%=XMLCollection.getProperty("cerca.fitxaRecurs.areaCurricular", sLang)%>:
                                </dt>
                                <dd>
                                        <% 
                                        int count = 0;
                                        String more = "";
                                         while (keys.hasMoreElements()) {
                                                String nomNivell = (String)keys.nextElement();
                                                String nomArea = (String)nivellsArees.get(nomNivell);	
                                                if (count < 2){
                                                        %><%=nomNivell%> / <%=nomArea%>, <%
                                                }else{
                                                        more += nomNivell+" / "+nomArea;
                                                }
                                                count++;
                                        }
                                        if (more.length()>1){
                                        %>
                                        <a href="#" onclick="document.getElementById('mesDucs').style.display='inline'; this.style.display='none';return false;"><%=XMLCollection.getProperty("cerca.fitxaRecurs.mesinfo", sLang)%></a>
                                        <span id="mesDucs" style="display:none;">
                                         <%=more%>
                                        </span>
                                        <%
                                        }
                                        %>
                                </dd>
                        <%}--%>

                        <div class="espaiFitxa">&nbsp;</div>

                        <!-- Ã€mbit -->
                        <%--if (luceneDocument.get("ambit") != null && !"".equals(luceneDocument.get("ambit"))){%>
                                <% 
                                String ambit = luceneDocument.get("ambit");
                                logger.debug("ambit... " + ambit);
                                if ((ambit == null) || ambit.equals("0")) {
                                   ambit = "-";	
                                   }
                                %>
                                <dt>
                                        <%=XMLCollection.getProperty("cerca.fitxaRecurs.ambit", sLang)%>:
                                </dt>
                                <dd>
                                        <%=luceneDocument.get("ambit")%>
                                </dd>
                        <%} --%>

                        <!-- Destinataris -->
                        <%
                            String destinataris = luceneDocument.get("lom@educational@intendedEndUserRole@value");
                            logger.debug("destinataris... " + destinataris);
                            if ((destinataris != null) && !destinataris.trim().equals("")) {
                                String destinatarisTemp = "";
                                myTokenizer = new StringTokenizer(destinataris);
                                while (myTokenizer.hasMoreTokens()) {
                                    String destinatari = (String) myTokenizer.nextToken();
                                    logger.debug("destinatari... " + destinatari);
                                    String destinatariTranslated = XMLCollection.getProperty("cerca.cercaCompletaTest." + destinatari.toLowerCase(), sLang);
                                    logger.debug("destinatariTranslated... " + destinatariTranslated);
                                    destinatarisTemp += destinatariTranslated;
                                    if (myTokenizer.hasMoreTokens()) {
                                        destinatarisTemp += ", ";
                                    }
                                }
                                destinataris = destinatarisTemp;
                                //destinataris = destinataris.replaceAll(" ", " / ");			  
                            } else {
                                destinataris = "";
                            }
                        %>
                        <%if (destinataris != null && !"".equals(destinataris)) {%>
                        <dt>
                            <%--=XMLCollection.getProperty("cerca.fitxaRecurs.destinatari", sLang)--%>
                            <%=XMLCollection.getProperty("cerca.fitxaRecurs.ambit", sLang)%>:
                        </dt>
                        <dd>
                            <%=destinataris%>
                        </dd>
                        <%} %>

                        <!-- Context -->
                        <%
                            String context = luceneDocument.get("context");
                            logger.debug("context... " + context);
                            if ((context != null) && !context.trim().equals("")) {
                                String[] ctxl = context.split("#");
                                String aux = "";
                                context = "";
                                for (int i = 0; i < ctxl.length; i++) {
                                    if (ctxl[i] != null && ctxl[i].trim().length() > 0) {
                                        if (context.length() > 0) {
                                            context += "; ";
                                        }
                                        aux = XMLCollection.getProperty("cerca.fitxaRecurs." + ctxl[i].trim().replaceAll(" / ", "-").replaceAll(" ", "-").trim().toLowerCase(), sLang);
                                        if (aux != null && !aux.trim().equals("-")) {
                                            context += aux;
                                        }
                                    }
                                }
                                if (context.length() > 0) {
                        %>
                        <dt>
                            <%=XMLCollection.getProperty("cerca.fitxaRecurs.context", sLang)%>:
                        </dt>
                        <dd>
                            <%=context%>
                        </dd>
                        <%
                                }
                            }
                        %>



                        <!-- Idiomes -->

                        <%if (luceneDocument.get("lom@general@language") != null && !"".equals(UtilsCercador.getLanguageName(luceneDocument.get("lom@general@language")))) {%>
                        <dt>
                            <%=XMLCollection.getProperty("cerca.fitxaRecurs.idioma", sLang)%>:
                        </dt>
                        <dd>
                            <%=UtilsCercador.getLanguageName(luceneDocument.get("lom@general@language"))%>
                        </dd>
                        <%} %>


                        <!-- Durada -->
                        <%
                            String duracio = luceneDocument.get("lom@educational@typicalLearningTime@duration");
                            String duracioString = "";
                            String sDura = "";
                            int iDura = 0;
                            int iSeg = 0;

                            logger.debug("duracio... " + duracio);
                            if (duracio != null && duracio.length() > 4 && duracio.indexOf('H') > 0) {
                                //la duracio ve en format PT0H0M0S. Ho transformem tot a segons
                                if (duracio != null && duracio.length() > 4 && duracio.indexOf('H') > 0) {
                                    sDura = duracio.substring(2, duracio.indexOf('H'));								//hores
                                    iDura = (new Integer(sDura)).intValue();
                                    iSeg = iDura * 60 * 60;

                                    if (duracio.indexOf('M') > 0) {
                                        sDura = duracio.substring(duracio.indexOf('H') + 1, duracio.indexOf('M'));		//minuts
                                        iDura = (new Integer(sDura)).intValue();
                                        iSeg += iDura * 60;

                                        if (duracio.indexOf('S') > 0) {
                                            sDura = duracio.substring(duracio.indexOf('M') + 1, duracio.indexOf('S'));	//segons
                                            iDura = (new Integer(sDura)).intValue();
                                            iSeg += iDura;
                                        }
                                    }
                                }
                                if (iSeg > 0) {
                                    //Convertim els segons en un string amb els dies, hores, minuts i segons
                                    try {
                                        //DIES
                                        if (iSeg >= (60 * 60 * 24)) {
                                            if (iSeg / (60 * 60 * 24) == 1) {
                                                duracioString = "1 " + XMLCollection.getProperty("cerca.fitxaRecurs.dia", sLang) + " ";
                                            } else {
                                                duracioString = iSeg / (60 * 60 * 24) + " " + XMLCollection.getProperty("cerca.fitxaRecurs.dies", sLang) + " ";
                                            }
                                            iSeg = iSeg % (60 * 60 * 24);
                                        }
                                        //HORES
                                        if (iSeg >= (60 * 60)) {
                                            if (iSeg / (60 * 60) == 1) {
                                                duracioString += "1 " + XMLCollection.getProperty("cerca.fitxaRecurs.hora", sLang) + " ";
                                            } else {
                                                duracioString += (iSeg / (60 * 60)) + " " + XMLCollection.getProperty("cerca.fitxaRecurs.hores", sLang) + " ";
                                            }
                                            iSeg = iSeg % (60 * 60);
                                        }
                                        //MINUTS
                                        if (iSeg >= 60) {
                                            if (iSeg / 60 == 1) {
                                                duracioString += "1 " + XMLCollection.getProperty("cerca.fitxaRecurs.minut", sLang) + " ";
                                            } else {
                                                duracioString += (iSeg / 60) + " " + XMLCollection.getProperty("cerca.fitxaRecurs.minuts", sLang) + " ";
                                            }
                                            iSeg = iSeg % 60;
                                        }
                                        //SEGONS
                                        if (iSeg == 1) {
                                            duracioString += "1 " + XMLCollection.getProperty("cerca.fitxaRecurs.segon", sLang);
                                        } else if (iSeg > 0) {
                                            duracioString += iSeg + " " + XMLCollection.getProperty("cerca.fitxaRecurs.segons", sLang);
                                        }

                                    } catch (Exception e) {
                                    }

                        %>
                        <dt>
                            <%=XMLCollection.getProperty("cerca.fitxaRecurs.durada", sLang)%>:
                        </dt>
                        <dd>
                            <%=duracioString%>
                        </dd>
                        <%
                                }
                            }
                        %>

                        <!-- Tipus recurs -->
                        <%
                            destinataris = luceneDocument.get("tipusRecurs");
                            logger.debug("destinataris... " + destinataris);
                            if ((destinataris != null) && !destinataris.trim().equals("")) {
                                String destinatarisTemp = "";
                                myTokenizer = new StringTokenizer(destinataris, "#");
                                while (myTokenizer.hasMoreTokens()) {
                                    String destinatari = (String) myTokenizer.nextToken();
                                    logger.debug("destinatari... " + destinatari);
                                    String destinatariTranslated = XMLCollection.getProperty("cerca.cercaCompletaTest." + destinatari.replaceAll(" ", "").toLowerCase(), sLang);
                                    if (destinatariTranslated.indexOf(".") < 0) {
                                        logger.debug("destinatariTranslated... " + destinatariTranslated);
                                        if (destinatarisTemp.length() > 0) {
                                            destinatarisTemp += ", ";
                                        }
                                        destinatarisTemp += destinatariTranslated;
                                    }
                                }
                                destinataris = destinatarisTemp;
                                //destinataris = destinataris.replaceAll(" ", " / ");			  
                            } else {
                                destinataris = "";
                            }
                        %>
                        <%if (destinataris != null && !"".equals(destinataris)) {%>
                        <dt>
                            <%=XMLCollection.getProperty("cerca.cercaCompletaTest.tipusRecurs", sLang)%>:
                        </dt>
                        <dd>
                            <%=destinataris%>
                        </dd>
                        <%} %>


                        <!--Descripcio fisica -->		
                        <%  String descripcioFisica = "";
                            descripcioFisica = luceneDocument.get("carFisiques");

                            if (descripcioFisica != null && !"".equals(descripcioFisica)) {%>
                        <dt>
                            <%=XMLCollection.getProperty("cerca.fitxaRecurs.caracteristiques", sLang)%>:
                        </dt>
                        <dd>
                            <%=descripcioFisica%>
                        </dd>
                        <%} %>


                        <!-- IDENTIFICADORS FISICS -->		
                        <%
                            String isbn = luceneDocument.get("idfisics");
                        %>
                        <%if (isbn != null && !"".equals(isbn)) {%>
                        <%
                            if (isbn.toLowerCase().trim().indexOf("isbn:") >= 0) {
                                isbn = isbn.substring(isbn.toLowerCase().trim().indexOf("isbn:") + 5);
                                if (isbn.indexOf(";") > 0) {
                                    isbn = isbn.substring(0, isbn.indexOf(";"));
                                }
                            }
                        %>
                        <dt>
                            <%=XMLCollection.getProperty("cerca.fitxaRecurs.identificadorfisic", sLang)%>:
                        </dt>
                        <dd>
                            <%=isbn%>
                        </dd>
                        <%} %>

                        <div class="espaiFitxa">&nbsp;</div>

                        <!-- Llicencia -->
                        <%
                            String lic = luceneDocument.get("llicDesc");
                            if ((lic != null) && !lic.trim().equals("") && !lic.trim().equals("-")) {%>
                        <dt>
                            <%=XMLCollection.getProperty("cerca.fitxaRecurs.llicencia", sLang)%>:
                        </dt>
                        <dd>
                            <a target="_blank" href="<%=luceneDocument.get("llicUrl")%>"><%=luceneDocument.get("llicId")%></a>
                            <span class="lletraPetita" style="font-size:0.8em;">
                                <%=lic%>
                            </span>
                        </dd>

                        <%
                            }
                        %>


                        <!-- Cost del recurs-->
                        <%--
                        String cost = luceneDocument.get("lom@rights@cost");
                        logger.debug("cost... " + cost);
                        if ((cost != null) && !cost.equals("")) {
                                if (cost.equals("yes") || cost.equals("si")) {
                                  cost = "Pagament";%>
                                <dt>
                                        <%=XMLCollection.getProperty("cerca.fitxaRecurs.cost", sLang)%>:
                                </dt>
                                <dd>
                                        <%=cost%>
                                </dd>
                                  
                                <%} 
                        }		
                        --%>



                        <!-- Disponibilitat -->
                        <%
                            final class UnitatsComparator implements Comparator {

                                public int compare(Object o1, Object o2) {
                                    String s1 = (String) ((Set) ((Map) o1).keySet()).toArray()[0];
                                    String s2 = (String) ((Set) ((Map) o2).keySet()).toArray()[0];

                                    s1 = EducacioAnalyzer.filtra(s1);
                                    s2 = EducacioAnalyzer.filtra(s2);

                                    return s1.compareTo(s2);
                                }
                            }

                            String dids = luceneDocument.get("disponibleIds");
                            String dins = luceneDocument.get("disponibleNoms");

                            if ((dins != null) && !dins.trim().equals("") && (dids != null) && !dids.trim().equals("")) {
                                String[] didl = dids.split("#");
                                String[] dinl = dins.split(";");
                                String more = "";
                                String aux = "";
                                ArrayList a = new ArrayList();
                                for (int i = 0; i < dinl.length; i++) {
                                    Map m = new HashMap();
                                    m.put(dinl[i], didl[i]);
                                    a.add(m);
                                }
                                java.util.Collections.sort(a, new UnitatsComparator());

                                for (int i = 0; i < dinl.length; i++) {
                                    if (dinl[i] != null) {
                                        String nomUnitat = (String) ((Set) ((Map) a.get(i)).keySet()).toArray()[0];
                                        String idUnitat = (String) ((Map) a.get(i)).get(nomUnitat);
                                        String br = "";
                                        if (i > 0) {
                                            br = "<br/>";
                                        }
                                        // Intentem recuperar la url
                                        String url = UtilsCercador.getUnitatUrl(idUnitat);
                                        if (url != null && url.length() > 0) {
                                            aux += br + "<a target='_blank' href='" + url + "'><span title=\"Unitat:" + idUnitat + "\">" + nomUnitat + "</span></a>";
                                        } else {
                                            aux += br + "<span title=\"Unitat:" + idUnitat + "\">" + nomUnitat + "</span>";
                                        }
                                        if (i < dinl.length - 1) {
                                            //aux += "<br/> ";
                                        }
                                        if (i < 2) {
                                            more += aux;
                                            aux = "";
                                        }
                                    }
                                }

                                if (aux.length() > 0 || more.length() > 0) {
                        %>
                        <div style="float:left; font-weight:bold; margin:7px 10px 0 0">
                            <%=XMLCollection.getProperty("cerca.fitxaRecurs.disponibilitat", sLang)%>:
                        </div>
                        <div style="float:left; margin:7px 10px 0 0; width:500px">
                            <%if (dinl.length > 2) {%>
                            <%=more%>
                            <span id="mesDispos" style="display:none;">
                                <%=aux%>
                            </span>
                            <a href="#" onclick="mostraAmagaDisponibilitat(this)"><%=XMLCollection.getProperty("cerca.fitxaRecurs.mesinfo", sLang)%></a>

                            <%} else {%>
                            <%=more%>
                            <%}%>
                        </div>
                        <%
                                }
                            }
                        %>






                        <%--if (luceneDocument.get("dataCatalogacio") != null && !"".equals(luceneDocument.get("dataCatalogacio"))){%>
                        <dt>
                                <%=XMLCollection.getProperty("cerca.fitxaRecurs.dataCatalogacio", sLang)%>:
                        </dt>
                        <dd>
                                <%=UtilsCercador.girarDataDMYguio(luceneDocument.get("dataCatalogacio"))%>
                        </dd>
                        <%} --%>		


                        <div class="espaiFitxa" style="clear:both" >&nbsp;</div>
                        <!-- AddThis Button BEGIN -->
                        <div class="addthis_toolbox addthis_default_style">
                            <a href="http://www.addthis.com/bookmark.php?v=250&amp;username=merli" class="addthis_button_compact">Comparteix-ho</a>
                            <span class="addthis_separator">|</span>
                            <a title="Envia a Facebook" class="addthis_button_facebook"></a>
                            <a title="Envia a Twitter" class="addthis_button_twitter"></a>
                            <a title="Envia a Delicious" class="addthis_button_delicious"></a>
                            <a title="Afegeix als favorits" class="addthis_button_favorites"></a>
                            <a title="Envia per correu mitjançant Gmail" class="addthis_button_gmail"></a>
                        </div>
                        <script type="text/javascript">var addthis_config = {"data_track_clickback": true};</script>
                        <script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#username=merli"></script>
                        <!-- AddThis Button END -->

                        <!-- ADDED false to next if to hide addAloma button -->
                        <% if (false && (usuari != null) && imprimir.equals("no")) {%>
                        <dd>
                            <div id="afegirOrganitzador">
                                <a href="javascript:addToOrganitzador('<%=Configuracio.contextWebAplicacio%>','<%=usuari%>', '<%=idRecurs%>', '<%=sheetId%>')"><%=XMLCollection.getProperty("cerca.fitxaRecurs.afegirRecurs", sLang)%></a>
                            </div>
                            <dd>
                                <%    }  %>		
                                </dl>
                                </div> <!-- cos_blau -->
                                </div>		<!-- fitxa -->
                                <% } %>

                                <%
                                    logger.info("Get suggeriments....");
                                    ArrayList suggeriments = fitxaRecurs.getRecomanacionsFromRecurs(idRecurs);
                                    logger.info("Size... " + suggeriments.size());
                                    if (suggeriments.size() > 0) {
                                %>

                                <h2>Materials relacionats</h2>
                                <div id="cos_blau">
                                    <ul>	
                                        <%
                                            int j = 0;
                                            while (j < suggeriments.size()) {
                                                RecursObject recursObject = (RecursObject) suggeriments.get(j);
                                                String urlRecurs = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio + "/cerca/fitxaRecurs.jsp?idRecurs=" + recursObject.id;
                                        %>
                                        <li>
                                            <% if (recursObject.puntuacio <= 0) { %>
                                            <img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                            <% } %>
                                            <% if (recursObject.puntuacio == 1) { %>
                                            <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                            <% } %>
                                            <% if (recursObject.puntuacio == 2) { %>
                                            <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                            <% } %>
                                            <% if (recursObject.puntuacio == 3) { %>
                                            <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                            <% } %>
                                            <% if (recursObject.puntuacio == 4) { %>
                                            <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif"  alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                            <% } %>
                                            <% if (recursObject.puntuacio == 5) { %>
                                            <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/>
                                            <% }%>		
                                            <a href="<%=urlRecurs%>"><%=recursObject.titol%></a></li>



                                        <%
                                                j++;
                                            }
                                        %>

                                    </ul>

                                </div>          <!-- cos_blau -->


                                <%
                                    }
                                %>


                                <%
                                    if ((usuari != null) && imprimir.equals("no")) {

                                %>
                                <div id="afegir_comentari">
                                    <h2><a href="javascript:mostrarOculta()">+<%=XMLCollection.getProperty("cerca.fitxaRecurs.comentaRecurs", sLang)%></a></h2>
                                </div>
                                <div id="afegir_comentari_ocult">
                                    <h2><a href="javascript:tancarOculta()">- <%=XMLCollection.getProperty("cerca.fitxaRecurs.comentaRecurs", sLang)%></a></h2>
                                </div>
                                <a name="comentaris"/>
                                <div id="formulari_comentari_ocult">
                                <!-- <h2><%=XMLCollection.getProperty("cerca.fitxaRecurs.comentaRecurs", sLang)%></h2> -->
                                    <!-- <div id="cos_blau"> -->
                                    <form name="afegirComentari" id="afegirComentari" method="post" action="/<%=Configuracio.contextWebAplicacio%>/ManagerComentaris">
                                        <input type="hidden" name="idRecurs" value="<%=idRecurs%>"/>
                                        <input type="hidden" name="nomUsuari" value="<%=usuari%>"/>
                                        <input type="hidden" name="operacio" value="<%=operacio%>"/>
                                        <input type="hidden" name="idComentari" value="<%=idComentariBD%>"/>
                                        <input type="hidden" name="puntuacio" value="<%=puntuacioBD%>"/>

                                        <p><label><%=XMLCollection.getProperty("cerca.fitxaRecurs.puntuacio", sLang)%>:</label>
                                            <% if (puntuacioBD <= 0) { %>
                                            <a href="javascript:fillStars(1)"><img src="../imatges/stars-empty.gif" id="star1" alt=""/></a><a href="javascript:fillStars(2)"><img src="../imatges/stars-empty.gif" id="star2" alt=""/></a><a href="javascript:fillStars(3)"><img src="../imatges/stars-empty.gif" id="star3" alt=""/></a><a href="javascript:fillStars(4)"><img src="../imatges/stars-empty.gif" id="star4" alt=""/></a><a href="javascript:fillStars(5)"><img src="../imatges/stars-empty.gif" id="star5" alt=""/></a>
                                                <% } %>
                                                <% if (puntuacioBD == 1) { %>
                                            <a href="javascript:fillStars(1)"><img src="../imatges/stars-full.gif" id="star1" alt=""/></a><a href="javascript:fillStars(2)"><img src="../imatges/stars-empty.gif" id="star2" alt=""/></a><a href="javascript:fillStars(3)"><img src="../imatges/stars-empty.gif" id="star3" alt=""/></a><a href="javascript:fillStars(4)"><img src="../imatges/stars-empty.gif" id="star4" alt=""/></a><a href="javascript:fillStars(5)"><img src="../imatges/stars-empty.gif" id="star5" alt=""/></a>
                                                <% } %>
                                                <% if (puntuacioBD == 2) { %>
                                            <a href="javascript:fillStars(1)"><img src="../imatges/stars-full.gif" id="star1" alt=""/></a><a href="javascript:fillStars(2)"><img src="../imatges/stars-full.gif" id="star2" alt=""/></a><a href="javascript:fillStars(3)"><img src="../imatges/stars-empty.gif" id="star3" alt=""/></a><a href="javascript:fillStars(4)"><img src="../imatges/stars-empty.gif" id="star4" alt=""/></a><a href="javascript:fillStars(5)"><img src="../imatges/stars-empty.gif" id="star5" alt=""/></a>
                                                <% } %>
                                                <% if (puntuacioBD == 3) { %>
                                            <a href="javascript:fillStars(1)"><img src="../imatges/stars-full.gif" id="star1" alt=""/></a><a href="javascript:fillStars(2)"><img src="../imatges/stars-full.gif" id="star2" alt=""/></a><a href="javascript:fillStars(3)"><img src="../imatges/stars-full.gif" id="star3" alt=""/></a><a href="javascript:fillStars(4)"><img src="../imatges/stars-empty.gif" id="star4" alt=""/></a><a href="javascript:fillStars(5)"><img src="../imatges/stars-empty.gif" id="star5" alt=""/></a>
                                                <% } %>
                                                <% if (puntuacioBD == 4) { %>
                                            <a href="javascript:fillStars(1)"><img src="../imatges/stars-full.gif" id="star1" alt=""/></a><a href="javascript:fillStars(2)"><img src="../imatges/stars-full.gif" id="star2" alt=""/></a><a href="javascript:fillStars(3)"><img src="../imatges/stars-full.gif" id="star3" alt=""/></a><a href="javascript:fillStars(4)"><img src="../imatges/stars-full.gif" id="star4" alt=""/></a><a href="javascript:fillStars(5)"><img src="../imatges/stars-empty.gif" id="star5" alt=""/></a>
                                                <% } %>
                                                <% if (puntuacioBD == 5) { %>
                                            <a href="javascript:fillStars(1)"><img src="../imatges/stars-full.gif" id="star1" alt=""/></a><a href="javascript:fillStars(2)"><img src="../imatges/stars-full.gif" id="star2" alt=""/></a><a href="javascript:fillStars(3)"><img src="../imatges/stars-full.gif" id="star3" alt=""/></a><a href="javascript:fillStars(4)"><img src="../imatges/stars-full.gif" id="star4" alt=""/></a><a href="javascript:fillStars(5)"><img src="../imatges/stars-full.gif" id="star5" alt=""/></a>
                                                <% }%>
                                        </p>
                                        <br/>

                                        <p><label for="titol"><%=XMLCollection.getProperty("cerca.fitxaRecurs.titol", sLang)%>:</label><input type="text" name="titol" id="titolcom" value="<%=titolBD%>"/></p>
                                        <br/>
                                        <p><label class="topalign" for="comentari"><%=XMLCollection.getProperty("cerca.fitxaRecurs.comentari", sLang)%>:</label><textarea name="comentari" id="comentari" rows="7" cols="50"><%=comentariBD%></textarea></p>
                                        <br/>


                                        <p><input type="button" class="button"  value="<%=XMLCollection.getProperty("cerca.fitxaRecurs.enviar", sLang)%>" onClick="javascript:executaSubmit();"/></p>
                                    </form>  
                                    <!-- </div>   -->
                                </div>  <!-- afegir comentari_ocult -->
                                <%   } else {%>
                                <div id="afegir_comentari">
                                    <h2><a href="/<%=Configuracio.contextWebAplicacio%>/loginSSO.jsp?logOn=true&comment=true">+<%=XMLCollection.getProperty("cerca.fitxaRecurs.comentaRecurs", sLang)%></a></h2>
                                </div>
                                <%   } %>

                                <%
                                    logger.debug("Getting comentaris from recurs -> " + idRecurs);
                                    ArrayList allComments = fitxaRecurs.getComentarisFromRecurs(idRecurs);
                                    int i = 0;
                                    logger.debug("Comentaris -> " + allComments.size());
                                    while (i < allComments.size()) {
                                        ComentariObject myComentari = (ComentariObject) allComments.get(i);
                                        long id = myComentari.id;
                                        logger.debug("Id -> " + id);
                                        String comentari = myComentari.comentari;
                                        logger.debug("Comentari -> " + comentari);
                                        String titol = myComentari.titol;
                                        logger.debug("Titol -> " + titol);
                                        int puntuacio = myComentari.puntuacio;
                                        logger.debug("Puntuacio -> " + puntuacio);
                                        String autor = myComentari.autor;
                                        logger.debug("Autor -> " + autor);

                                        String dataEdicio = myComentari.dataEdicio;
                                        logger.debug("dataEdicio -> " + dataEdicio);

                                        String dia = UtilsCercador.getDia(dataEdicio);
                                        String hora = UtilsCercador.getHora(dataEdicio);

                                        if ((i % 2) == 0) {
                                %>           
                                <div id="comentari_senar">
                                    <% } else { %>
                                    <div id="comentari_parell">           
                                        <% } %>
                                        <h3>
                                            <table border="0" width="100%" summary="">
                                                <tr>
                                                    <td align="left">
                                                        <% if (puntuacio <= 0) { %>
                                                        <img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                                        <% } %>
                                                        <% if (puntuacio == 1) { %>
                                                        <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                                        <% } %>
                                                        <% if (puntuacio == 2) { %>
                                                        <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                                        <% } %>
                                                        <% if (puntuacio == 3) { %>
                                                        <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/><img src="../imatges/stars-empty.gif" alt=""/>
                                                        <% } %>
                                                        <% if (puntuacio == 4) { %>
                                                        <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt="" /><img src="../imatges/stars-empty.gif" alt=""/>
                                                        <% } %>
                                                        <% if (puntuacio == 5) { %>
                                                        <img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/><img src="../imatges/stars-full.gif" alt=""/>
                                                        <% }%>		
                                                        <a name="<%=id%>"><b><%=titol%></b></a>
                                                        <span class="autor_comentari">
                                                            <%=autor%> el <%=dia%>
                                                        </span>
                                                    </td></tr>
                                            </table>
                                        </h3>
                                        <!-- <div class="clear"></div> -->
                                        <h4><%=comentari%></h4>
                                    </div> <!-- comentari -->



                                    <%
                                            i++;
                                        }
                                    %>

                                    <div class="espai">&nbsp;</div>

                                </div> 

                                <% if (imprimir.equals("no") && inxtec <= ResultGeneratorUtil.SHOW_ALL) {%>
                                <%=ResultGeneratorUtil.htmlFooterCercador(sLang)%>    	
                                <%   } %>

                                <% if ("true".equalsIgnoreCase(request.getParameter("comment"))) { %>
                                <script type="text/javascript">mostrarOculta();</script>
                                <%   } %>

                                <!-- GOOGLE ANALYTICS -->
                                <script type="text/javascript">
                                    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
                                    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
                                </script>
                                <script type="text/javascript">
                                    try {
                                        var pageTracker = _gat._getTracker("UA-6935294-1");
                                        pageTracker._trackPageview();
                                    } catch (err) {
                                    }</script>
                                <!-- END -->

                                </body>
                                </html>

                                <%
                                    } catch (Exception e) {
                                        logger.error(e);
                                    } finally {
                                        try {
                                            fitxaRecurs.closeSearcher();
                                        } catch (Exception e) {
                                            logger.error(e);
                                        }
                                        try {
                                            if (myConnection != null) {
                                                myConnection.close();
                                            }
                                        } catch (Exception e) {
                                            logger.error(e);
                                        }
                                    }
                                    // }
                                %> 