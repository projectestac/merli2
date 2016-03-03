<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio,simpple.xtec.web.util.FitxaRecurs,simpple.xtec.web.util.ComentariObject" %>
<%@ page import="simpple.xtec.web.util.TipusFitxer,org.apache.lucene.analysis.standard.StandardAnalyzer,org.apache.lucene.queryParser.QueryParser,org.apache.lucene.search.*,org.apache.lucene.document.Document" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, java.util.ArrayList, java.util.StringTokenizer, simpple.xtec.web.util.Indexador ,simpple.xtec.web.util.UtilsCercador, simpple.xtec.web.util.RecursObject, simpple.xtec.web.util.XMLCollection, java.util.Hashtable, java.util.Enumeration, simpple.xtec.web.util.DucObject" %>
<%@ page pageEncoding="UTF-8" %>

<%
	Logger logger = Logger.getLogger("fitxaAgrega.jsp");  
	String idRecurs = request.getParameter("idRecurs");
	logger.error("idRecurs -> " + idRecurs);
	String sLang = XMLCollection.getLang(request);
	
	Hashtable luceneDocument = new Hashtable();
	String agregaId = request.getParameter("agregaId");
	if (session.getAttribute("agregaResults") != null && agregaId != null)
		luceneDocument = (Hashtable)((ArrayList)session.getAttribute("agregaResults")).get(Integer.parseInt(agregaId));
	
	Hashtable nivellsArees;
	double idComentariBD = -1;
	String comentariBD = ""; 
	String titolBD = "";
	int puntuacioBD = -1; 
	String operacio = "afegir";
	String textBoto = "Afegir comentari";
	float puntuacioMitja = (float)0.0;
	String usuari = (String)session.getAttribute("nomUsuari");
	int comentarisSuspesos = 0;    
	int numComentaris = 0;  
    String usuariNomComplet = "";
	if (usuari == null) {
	  //usuari = (String)request.getRemoteUser();
            usuari = (String)session.getAttribute("user");
    }  
	String imprimir = request.getParameter("imprimir");
	String urlImprimir = "javascript:window.print();";
	String sheetId = (String)session.getAttribute("sheetId");  
	if (sheetId == null) {
	  sheetId = "";
	  }
      
   logger.info("Usuari -> " + usuari);
  %>

<% 
try{ 
	if (luceneDocument != null) { 
        nivellsArees = (Hashtable)luceneDocument.get("duc");	
	%>
	<div id="fitxa">
	       <table width="100%" summary=""> 
		         <tr>
		           <td align="left">	
		<h1><%=(String) luceneDocument.get("titol")%></h1>
		    <div id="comentaris">
				<!-- Puntuaci� -->
                  <% if (puntuacioMitja <= 0)  { %>
                    <img src="../imatges/stars-empty.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/>
                  <% } %>
                  <% if (puntuacioMitja == 1)  { %>
                    <img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/>
                  <% } %>
                  <% if (puntuacioMitja == 2)  { %>
                    <img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/>
                  <% } %>
                  <% if (puntuacioMitja == 3)  { %>
                    <img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/><img src="../imatges/stars-empty.png" alt=""/>
                  <% } %>
                  <% if (puntuacioMitja == 4)  { %>
                    <img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-full.png"  alt=""/><img src="../imatges/stars-empty.png" alt=""/>
                  <% } %>
                  <% if (puntuacioMitja == 5)  { %>
                    <img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-full.png" alt=""/><img src="../imatges/stars-full.png" alt=""/>
                  <% } %>
				  
				<!-- Comentaris -->
				(<%=numComentaris%> 
				<%if (numComentaris == 1) {%>comentari<%} else {%>comentaris<%}%>)
	
				<!-- Impressi� -->
				<% if ("no".equals(imprimir)) { %>
					<div id="imprimir">
					<a href="<%=urlImprimir%>"><img src="../imatges/imprimir.gif" style="border:0;padding-left:5px;vertical-align:bottom" alt="<%=XMLCollection.getProperty("cerca.fitxaRecurs.versioImprimir", sLang)%>" title="<%=XMLCollection.getProperty("cerca.fitxaRecurs.versioImprimir", sLang)%>"/></a>
					</div>
				<% } %>
				
			</div></td><td align="right">
			<%		

			  String format = (String) luceneDocument.get("format");    
			  logger.debug("Checking format... " + format);    	  
			  if (format == null) {
				 format = "";	
				 } else {
				 format = format.trim();
				 if (format.endsWith("#")) {
					format = format.substring(0, (format.length() - 1));
					}
				 }
			  logger.debug("Format to tokenize... " + format);
			  StringTokenizer myTokenizer = new StringTokenizer(format, "#");
			  while (myTokenizer.hasMoreTokens()) {    
				  String singleFormat = (String)myTokenizer.nextToken();
				  singleFormat = singleFormat.trim();
				  String imgUrl = "";
				  String title = "";
				  logger.debug("Single format... " + singleFormat);
				  if ((singleFormat != null) && !singleFormat.equals("")){
					imgUrl = UtilsCercador.getImageFormat (singleFormat);
					title = (String) TipusFitxer.allTipusIds.get(singleFormat);
					} else{
					imgUrl = "altres.gif";
					singleFormat = "Altres";
					}
				  if ((imgUrl == null) || imgUrl.equals("")) {
					imgUrl = "altres.gif";  
					}  
				  singleFormat = UtilsCercador.toAcute(singleFormat).trim();
				  logger.debug("Single format no acute... " + singleFormat);
				 %>
				 <img src="../imatges/icones/icones_grans/<%=imgUrl%>" alt="<%=title%>" title="<%=title%>"/>
		     <% } %>
		</td></tr></table>
		<div id="cos_blau">
		<b><%=XMLCollection.getProperty("cerca.fitxaRecurs.autor", sLang)%>: </b><%=UtilsCercador.getFNfromVCard((String) luceneDocument.get("autor"))%>
		<br/><br/>
		<b><%=XMLCollection.getProperty("cerca.fitxaRecurs.dataPublicacio", sLang)%>: </b><%=UtilsCercador.girarDataDMYguio((String) luceneDocument.get("dataPublicacio"))%>
		<br/><br/>
		<b><%=XMLCollection.getProperty("cerca.fitxaRecurs.dataCatalogacio", sLang)%>: </b><%=UtilsCercador.girarDataDMYguio((String) luceneDocument.get("dataCatalogacio"))%>
		<br/><br/>
		<b><%=XMLCollection.getProperty("cerca.fitxaRecurs.idioma", sLang)%>: </b><%=UtilsCercador.getLanguageName((String) luceneDocument.get("lom@general@language"))%>
		<br/><br/>
		<%
		String duracio = (String) luceneDocument.get("duracio");
		logger.debug("duracio... " + duracio);
		String duracioString = "";
		if ((duracio == null) || duracio.equals("") || duracio.equals("0")) {
			duracioString = "-";
		    } else {
			try {
			  duracioString = "" + (new Integer(duracio)).intValue();
			  } catch (Exception e) {
			  }
			duracioString += " minuts";
		    }
		%>
		<b><%=XMLCollection.getProperty("cerca.fitxaRecurs.durada", sLang)%>: </b><%=duracioString%>
		<br/><br/>
		<% 
		String ambit = (String) luceneDocument.get("ambit");
		logger.debug("ambit... " + ambit);
		if ((ambit == null) || ambit.equals("0")) {
		   ambit = "-";	
		   }
		%>
		<b><%=XMLCollection.getProperty("cerca.fitxaRecurs.ambit", sLang)%>: </b>Agrega<%--=(String) luceneDocument.get("ambit")--%>
		<br/><br/>

		<%
		String cost = (String) luceneDocument.get("lom@rights@cost");
		logger.debug("cost... " + cost);		
		if ((cost == null) || cost.equals("")) {
			cost = "-";
		    } else {
			if (cost.equals("yes") || cost.equals("si")) {
			  cost = "Pagament";	
			  } else {
			  cost = "Gratu&iuml;t";
			  }
		    }		
		%>
		
		<b><%=XMLCollection.getProperty("cerca.fitxaRecurs.cost", sLang)%>: </b><%=cost%>
		<br/><br/>
						
		<%		
		  String destinataris = (String) luceneDocument.get("lom@educational@intendedEndUserRole@value");
		  logger.debug("destinataris... " + destinataris);
		  if ((destinataris != null) && !destinataris.trim().equals("")){
			 String destinatarisTemp = "";
			 myTokenizer = new StringTokenizer(destinataris);
			 while (myTokenizer.hasMoreTokens()) {
				 String destinatari = (String)myTokenizer.nextToken();
				 logger.debug("destinatari... " + destinatari);
				 String destinatariTranslated = XMLCollection.getProperty("cerca.cercaCompletaTest." + destinatari.toLowerCase());
				 logger.debug("destinatariTranslated... " + destinatariTranslated);				 
				 destinatarisTemp += destinatariTranslated;
				 if (myTokenizer.hasMoreTokens()) {
					destinatarisTemp += " / "; 
				    }
			     } 
			  destinataris = destinatarisTemp;   
			 //destinataris = destinataris.replaceAll(" ", " / ");			  
		     } else {
			 destinataris = "";    
		     }
		%>
		<b><%=XMLCollection.getProperty("cerca.fitxaRecurs.destinatari", sLang)%>: </b><%=destinataris%>
		<br/><br/>
		
		<b><%=XMLCollection.getProperty("cerca.fitxaRecurs.descripcio", sLang)%>: </b><%=(String) luceneDocument.get("descripcio")%>
		<br/><br/>		
		<b><%=XMLCollection.getProperty("cerca.fitxaRecurs.nivellEducatiu", sLang)%> / <%=XMLCollection.getProperty("cerca.fitxaRecurs.areaCurricular", sLang)%>: </b>
		<%
		if (nivellsArees!=null){
			Enumeration keys = nivellsArees.keys();
			   while (keys.hasMoreElements()) {
				   String nomNivell = (String)keys.nextElement();
				   String nomArea = (String)nivellsArees.get(nomNivell);			   
					%><%=nomNivell%> / <%=nomArea%><br/><%
		     }
			}
		%>

		<br/><br/>
		
		
		<%
		  String urlRecurs = (String)luceneDocument.get("url");
		  logger.debug("Url recurs -> " + urlRecurs);
		  String urlTooltip = "";
		  int tooltipSize = 90;
		  int cutPoint = 0;
		  boolean cutString = false;
		  if (urlRecurs!=null && urlRecurs.length() > 90){
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
		     urlRecurs = urlRecurs.substring(0,90) + "...";   
		     }
		%>     
		   <b>URL del recurs: </b><a target="_blank" href="<%=(String) luceneDocument.get("url")%>">
		   <% 
			if (urlRecurs!= null && urlRecurs.length() > 90){
			%>
		      <span class="tooltip"><dfn><%=urlTooltip%></dfn>
		   <%   }  %> 
		   <% if ("no".equals(imprimir)) { %>
		   <%=urlRecurs%>
		   <% } else { %>
		   <%=urlTooltip%>
		   <% } %>
		   </a>
   
		   
   
<!-- ADDED false to next if to hide addAloma button -->
     <% if (false && (usuari != null) && "no".equals(imprimir)){ %>
        <br/><br/>
		<div id="afegirOrganitzador">
		   <a href="javascript:addToOrganitzador('<%=Configuracio.contextWebAplicacio%>','<%=usuari%>', '<%=idRecurs%>', '<%=sheetId%>')"><%=XMLCollection.getProperty("cerca.fitxaRecurs.afegirRecurs", sLang)%></a>
		</div>
     <%    }  %>		

    </div> <!-- cos_blau -->
  </div><!-- fitxa -->
  
<%   }  else {%> 
recurs no visible.
<%}
   }catch (Exception e) {
        logger.error(e);
	} finally{
		//session.removeAttribute("agregaResults");
        logger.error("fitxaAgrega Finally");
	}		
%> 
