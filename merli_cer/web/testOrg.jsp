<%@ page import="java.sql.*,simpple.xtec.web.util.Configuracio,simpple.xtec.web.util.FitxaRecurs,simpple.xtec.web.util.ComentariObject" %>
<%@ page import="org.apache.lucene.analysis.standard.StandardAnalyzer,org.apache.lucene.queryParser.QueryParser,org.apache.lucene.search.*,org.apache.lucene.document.Document" %>
<%@ page import="org.apache.log4j.Logger, java.util.Locale, java.util.ArrayList, simpple.xtec.web.util.UtilsCercador" %>
<%!

%>     
<%
  String idRecurs = request.getParameter("idRecurs");
  // String nomUsuari = request.getParameter("nomUsuari");
  String nomUsuari = (String)session.getAttribute("usuari");
  String sheetId = (String)request.getParameter("sheetId");
  
  if (Configuracio.isVoid()){
      Configuracio.carregaConfiguracio(); 
      }
      
  Logger logger = Logger.getLogger("fitxaRecurs.jsp");  
  String urlLocal = "http://" + Configuracio.servidorWeb + ":" + Configuracio.portWeb + "/" + Configuracio.contextWebAplicacio;               
  %>
<html>
<head>
<title>Fitxa de recurs MERL</title>

<link rel="stylesheet" type="text/css" href="<%=urlLocal%>/css/cercador_xtec.css"/>
<link rel="stylesheet" type="text/css" href="<%=urlLocal%>/css/tooltip.css"/>
<script type="text/javascript" src="./scripts/ajax-organitzador.js"></script>
<script language="Javascript">
   
   var firstStartFull = false;
     
   function fillStars (numStars) {
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
        for (i=2;i<=5;i++) {
          document.getElementById("star" + i).src = '../imatges/stars-empty.gif';
          }
        } else { 
        document.afegirComentari.puntuacio.value = numStars;
        for (i=1;i<=5;i++) {
          if (i <= numStars) {
           document.getElementById("star" + i).src = '../imatges/stars-full.gif';                
           } else {
           document.getElementById("star" + i).src = '../imatges/stars-full.gif';                           
           }
          }      
        }  
     }
     
  function mostrarOculta(){
    document.getElementById('afegir_comentari_ocult').style.display='block';
    document.getElementById('formulari_comentari_ocult').style.display='block';    
    document.getElementById('afegir_comentari').style.display='none';    
    }
  function tancarOculta(){
    document.getElementById('afegir_comentari').style.display='block';
    document.getElementById('afegir_comentari_ocult').style.display='none';    
    document.getElementById('formulari_comentari_ocult').style.display='none';    
    }     
</script>
<script type="text/javascript" src="<%=urlLocal%>/scripts/ietooltips.js"></script>
</head>
<body>

		<div id="afegirOrganitzador">
		   <a href="javascript:addToOrganitzador('uuu', '111', '111')">Afegir recurs a l'organitzador</a>
		</div>
		<div class="clear"></div>
	

</body>
</html>

<!-- <a href="../rss/recurs_<%=idRecurs%>.rss">RSS</a> -->
