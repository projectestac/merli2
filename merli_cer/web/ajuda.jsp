<%@ page import="simpple.xtec.web.util.Configuracio, simpple.xtec.web.util.UtilsCercador, simpple.xtec.web.util.XMLCollection, org.apache.log4j.Logger" %>
<%@ page pageEncoding="UTF-8" %>
<%
   if (Configuracio.isVoid()) {
	  Configuracio.carregaConfiguracio(); 
      }
   Logger logger = Logger.getLogger("ajuda.jsp");      
   String usuari = (String)session.getAttribute("nomUsuari");
   String sLang = XMLCollection.getLang(request);
   String usuariNomComplet = "";
   int comentarisSuspesos = 0;
   if (usuari == null) {
	  usuari = (String)request.getRemoteUser();
      }

   logger.debug("Usuari 1: " + usuari);
   String userGeneric = (String)session.getAttribute("userGeneric");
   if (userGeneric == null){
	  userGeneric = ""; 
      }

   if (usuari != null) {         
     usuariNomComplet = (String)session.getAttribute("usuariNomComplet");
     if (usuariNomComplet == null) {      
        usuariNomComplet = UtilsCercador.getNomComplet(usuari);
        session.setAttribute("usuariNomComplet", usuariNomComplet);
        }      
     comentarisSuspesos = UtilsCercador.getComentarisSuspesos(usuari);        
     }
%>

<html>
<head>
	<title><%=XMLCollection.getProperty("ajuda.titol", sLang)%></title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
	<link rel="stylesheet" type="text/css" href="css/ajuda.css"/>
	<link rel="shortcut icon" href="./imatges/merli.ico" />
</head>
<body id="body">

  <div class="headerMainTableFullPublic" style="height:2px">
	<div style="height:2px"></div>
  </div>
  <table border="0" cellpadding="0" cellspacing="0" class="maxWidth" id="mainSection" style="">

	<tr class="icePnlGrdRow1">
		<td class="titol1HeaderFullPublic">
			<label class="headerTitleFullPublic" style="">Ajuda del cercador de recursos</label>
		</td>
	</tr>
  </table>
  <div class="blueLine">
	<div></div>
  </div>

  <table width="85%" border="0" align="center" cellpadding="0" cellspacing="0">
   <tr>
	<td valign="top">&nbsp;</td>
   </tr>
   <tr>
	<td valign="top">&nbsp;</td>
   </tr>
   <tr> 
    <td valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">

		  <tr valign="bottom">
			<td valign="top"><a name="#_Toc0"><b><font color="#ff9900">Cercador de Recursos</font></b></a></td>
		  </tr>
          <tr>
            <td valign="top">&nbsp;</td>
          </tr>
          <tr> 
            <td valign="top">
				<img src="./imatges/spacer.gif" width="10" height="20" alt=""/>

				<img src="./imatges/bullet_orange.gif" alt=""/>
				<img src="./imatges/spacer.gif" width="1" height="20" alt=""/>
				<a href="#_Toc1">Funcionament bàsic del Cercador</a></td>
          </tr>
          <tr> 
            <td valign="top">
				<img src="./imatges/spacer.gif" width="10" height="20" alt=""/>

				<img src="./imatges/bullet_orange.gif" alt=""/>
				<img src="./imatges/spacer.gif" width="1" height="20" alt=""/>
				<a href="#_Toc2">Operadors booleans de cerca</a></td>
          </tr>
          <tr> 
            <td valign="top">
				<img src="./imatges/spacer.gif" width="10" height="20" alt=""/>
				<img src="./imatges/bullet_orange.gif" alt=""/>

				<img src="./imatges/spacer.gif" width="1" height="20" alt=""/>
				<a href="#_Toc3">Altres operadors de cerca avançada</a></td>
          </tr>
          <tr> 
            <td valign="top">
				<img src="./imatges/spacer.gif" width="10" height="20" alt=""/>
				<img src="./imatges/bullet_orange.gif" alt=""/>
				<img src="./imatges/spacer.gif" width="1" height="20" alt=""/>
				<a href="#_Toc4">Ordenacions alternatives del llistat de resultats</a></td>

          </tr>

          <tr>

            <td valign="top">&nbsp;</td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
          </tr>
          <tr>
            <td valign="top">&nbsp;</td>
          </tr>
        </table>

    </td>
   </tr>
 </table>

 <table width="85%" align="center" cellpadding="0" cellspacing="0">
  <tr valign="bottom">
    <td valign="top"><a name="_Toc1"><b><font color="#ff9900">Funcionament bàsic del Cercador</font></b></a></td>
  </tr>

  <tr bgcolor="#CCCCCC"> 
    <td><img src="./imatges/spacer.gif" width="20" height="1" alt=""/></td>
  </tr>
  <tr valign="bottom"> 
    <td><img src="./imatges/spacer.gif" width="20" height="10" alt=""/></td>
  </tr>
  <tr valign="bottom">
    <td valign="top" class="bodytext">
	  <p class="CuerpodeTexto"><span>El cercador és una servei de cerca dels recursos educatius etiquetats dins del 
	  Merlí, el repositori de recursos educatius del Departament d'Ensenyament de la Generalitat de Catalunya.</span>
	  </p>

      <p class="CuerpodeTexto"><span>El seu funcionament bàsic és molt senzill, només cal escriure una o més 
      paraules al formulari i el cercador retornarà tots aquells recursos en els que apareguin (en algun lloc) 
      algun d'aquests mots. Poden trobar-se en el titol, en la descripció o en la pàgina web del propi recurs entre 
      d'altres. Si apareixen en algun lloc, el Cercador les trobarà.</span>
      </p>
      
      <p class="CuerpodeTexto"><span>El criteri amb que es mostra la llista de resultats per defecte (també coneguts 
      com resultats orgànics) és purament estadístic: el Cercador disposa d'una seguit de criteris interns amb els que 
      quantifica el pes relatiu de cada resultat en funció de, per exemple, en quin lloc apareixen les paraules de la 
      consulta (al títol, a la descripció, etc.) o el nombre de vegades (freqüencia) d'aquestes en el text. Finalment, 
      el cercador ofereix un llistat de resultats on (idealment) els resultats més rellevants i pertinents apareixen 
      en les primeres posicions..</span>
      </p>

	  <p><a href="#">Tornar a l'&iacute;ndex</a></p>
	</td>
  </tr>
  <tr valign="bottom">
    <td>&nbsp;</td>
  </tr>

 </table>


 
 <table width="85%" align="center" cellpadding="0" cellspacing="0">
  <tr valign="bottom">
    <td valign="top"><a name="_Toc2"><b><font color="#ff9900">Operadors booleans de cerca</font></b></a></td>
  </tr>

  <tr bgcolor="#CCCCCC"> 
    <td><img src="./imatges/spacer.gif" width="20" height="1" alt=""/></td>
  </tr>
  <tr valign="bottom"> 
    <td><img src="./imatges/spacer.gif" width="20" height="10" alt=""/></td>
  </tr>
  <tr valign="bottom">
    <td valign="top" class="bodytext">
	  <p class="CuerpodeTexto"><span>			
	  
	     <p class="CuerpodeTexto">Per defecte, les cerques es fan en mode AND, &eacute;s a dir, si cerquem:</p>
	     <div id="code" class="CuerpodeTexto">progressió arimètica</div><p class="CuerpodeTexto">retorna resultats on apareixen el dos mots. Els
	     operadors disponibles s&oacute;n:</p>
	     
	     
	     <p><b>""</b></p>
		 <p class="CuerpodeTexto">L'operador "" es pot utilizar per indicar al Cercador que només es vol cercar 
		 aquells recursos en els que apareixen les paraules entre cometes com una única unitat (i per tant una darrera 
		 l'altre, en l'ordre indicat). Així per exemple: </p><div id="code" class="CuerpodeTexto">"progressió arimètica"</div> 
		 <p class="CuerpodeTexto">retorna resultats on apareixen el dos mots de forma consecutiva.</p>


		 <br/>
		 <p><b>-</b></p>
		 <p class="CuerpodeTexto">L'operador "-" exclou els resultats on apreix la paraula que s'escrigui després 
		 del símbol "-". Així, per exemple:</p><div id="code">progressió -artimètica</div><p class="CuerpodeTexto">retorna els resutats on 
		 apareix sempre el terme "progressió" i mai el terme "aritmètica".</p>


       </span>
	  </p>

	  <p><a href="#">Tornar a l'&iacute;ndex</a></p>
	</td>
  </tr>
  <tr valign="bottom">
    <td>&nbsp;</td>
  </tr>

 </table>
 
 
 

 
<table width="85%" align="center" cellpadding="0" cellspacing="0">
  <tr valign="bottom">
    <td valign="top"><a name="_Toc3"><b><font color="#ff9900">Altres operadors de cerca avançada</font></b></a></td>
  </tr>

  <tr bgcolor="#CCCCCC"> 
    <td><img src="./imatges/spacer.gif" width="20" height="1" alt=""/></td>
  </tr>
  <tr valign="bottom"> 
    <td><img src="./imatges/spacer.gif" width="20" height="10" alt=""/></td>
  </tr>
  <tr valign="bottom">
    <td valign="top" class="bodytext">
	  <p class="CuerpodeTexto"><span>			<p class="CuerpodeTexto"><b>Comodins</b></p>
			<p class="CuerpodeTexto">El Cercador també admet l'ús de comodins. Els símbols del comodins són "?" per al comodí d'un únic caràcter i "*" per al comodí de múltiple caràcters.</p>
			<p class="CuerpodeTexto">El comodí d'un sol caràcter "?" cerca els termes que conincidexen amb la paraula i on el comodí pot ser qualsevol lletra o número. Així, per exemple si volem cercar tant "text" com "test", podem posar al formulari: <div id="code">te?t</div>.
			<p class="CuerpodeTexto">El comodí de caris caràcters "*" cerca els termes que coincideixen amb la paraula i on on comodí pot ser quasevol combinació de lletres o números. D'aquesta manera, si per exemple volem cercar "programació", "programador" o "programa", podem introduir al formulari: <div id="code">programa*</div>.</p>
			<p class="CuerpodeTexto">Nota: no es pot fer servir el comodí com a primer caràcter d'una paraula de cerca.</p>
			<br/>
			<p class="CuerpodeTexto"><b>Cerques difuses</b></p>
			<p class="CuerpodeTexto">Una altra de les funcionalitats del Cercador és la possibilitat de fer cerques difoses. Per fer-les només cal afegir el símbol "~" al final d'una de les paraules i el Cercador retornarà resultats on apareixi el mot i d'altres mots que ell consideri similars (a nivell ortogràfic, no pas semàntic). Així, per exemple, cercant <div id="code">pèsol~</div> el Cercador ens pot retornar resultats on apareixen els mots "pèsol", "pèsols" o "fèsols".</p>
			<br/>
			<br/>
			<p class="CuerpodeTexto"><b>Cerques per proximitat</b></p>
			<p class="CuerpodeTexto">Una altra possibilitat és realitzar cerques per proximitat de paraules. És a dir per parelles de paraules que es troben com a mínim per sota d'un distància (en nombre de paraules). D'aquesta manera si introduim</p><div id="code">"teorema gauss"~10</div> 
			<p class="CuerpodeTexto">el Cercador retornarà els resultats on apareixen els mots "teorema" i "gauss" separats per un màxim de 10 paraules.</p>
</span></p>


	  <p><a href="#">Tornar a l'&iacute;ndex</a></p>
	</td>
  </tr>
  <tr valign="bottom">
    <td>&nbsp;</td>
  </tr>

</table> 
 
 


<table width="85%" align="center" cellpadding="0" cellspacing="0">
  <tr valign="bottom">
    <td valign="top"><a name="_Toc4"><b><font color="#ff9900">Ordenacions alternatives del llistat de resultats</font></b></a></td>
  </tr>

  <tr bgcolor="#CCCCCC"> 
    <td><img src="./imatges/spacer.gif" width="20" height="1" alt=""/></td>
  </tr>
  <tr valign="bottom"> 
    <td><img src="./imatges/spacer.gif" width="20" height="10" alt=""/></td>
  </tr>
  <tr valign="bottom">
    <td valign="top" class="bodytext">
	  <p class="CuerpodeTexto"><span>L'ordenació per defecte que el Cercador ofereix del llistat de resultats d'una 
	  cerca segueix un conjunt de criteris interns amb l'objectiu de situar en primer lloc els resultats més rellevants 
	  i pertinents. Ara bé, és possible modificar aquest criteri per d'altres en funció dels nostres interessos. En la 
	  vista del llistat de resultats apareix a la dreta un menú amb uns enllaços a les altres ordenacions alternatives 
	  (sempre però sobre conjunt de resultats fruit de la cerca original). En concret es pot ordenar per:</span></p>
			<ol>
				<li>Data: els recursos s'ordenaran per data de catalogació de manera que els que mes recentment s'ha catalogat al Merlí apareixeran en els primers llocs.</li>
				<li>Visites: els recursos més visitats apareixeran en els primers llocs.</li>
				<li>Valoració: els recursos amb una valoració promig més alta apareixeran en els primers llocs.</li>
				<li>Comentaris: els recursos amb un nombre més elevat de comentaris apareixeran en els primers llocs.</li>
			</ol>
	  <p><a href="#">Tornar a l'&iacute;ndex</a></p>
	</td>
  </tr>
  <tr valign="bottom">
    <td>&nbsp;</td>
  </tr>

</table>		
				
		
<table width="90%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr><td>
		<p align="center"><font size="-1"><br/><br/><font color="#999999"></font></font> </p></td>

	</tr>
</table>
<div id="footer">
	<div id="footer">
		<table border="0" cellpadding="0" cellspacing="0" class="footerMainTable" style="">
			<tbody>
				<tr class="icePnlGrdRow1">
				    <td><img style="float:left;margin-left:10px;margin-bottom:5px" src="./imatges/gencat_logo.png"/></td>
					<td class="maxWidth" ><img src="./imatges/spacer.gif" alt=""></img></td>
					<td class=""><a class="iceOutputLink" href="http://www.xtec.cat/" target="_blank" alt="XTEC" title="XTEC"><img src="./imatges/css-images/xtec_logo.png" alt=""></img></a></td>
					<td class=""><img src="./imatges/css-images/footer_spacer.png" alt=""></img></td>

					<td class=""><a class="iceOutputLink" href="http://www.edu365.cat/" target="_blank" alt="Edu365" title="Edu365">
						<img src="./imatges/css-images/edu_logo.png" alt=""></img></a>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>

</body>
</html>

