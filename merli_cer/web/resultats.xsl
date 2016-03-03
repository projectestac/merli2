<?xml version="1.0" encoding="utf-8" ?> 
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html"/>

<xsl:template match="/">
  <html>
    <head>
      <script language="Javascript">
        function goToNivell (nivell) {
          document.cerca.nivell.value = nivell;
          document.cerca.submit();
          }
        function doSubmit () {
          document.cerca.nivell.value = 0;
          document.cerca.textCercaHidden.value = '';   
          document.cerca.novaCerca.value = 'si';
          document.cerca.submit();
          }
        function sortBy (tipusOrdenacio) {
          document.cerca.nivell.value = 0;
          document.cerca.ordenacio.value = tipusOrdenacio;
          document.cerca.submit();
          }

      </script>
    </head>
    <body>
      <xsl:apply-templates select="resultatsCerca/form"/>
      <xsl:apply-templates select="resultatsCerca/resultats"/>
    </body>
  </html>    
</xsl:template>  


<xsl:template match="form">  
   <xsl:variable name="servletURL">
      <xsl:value-of select="servletURL"/>
   </xsl:variable>
   <xsl:variable name="query">
      <xsl:value-of select="query"/>
   </xsl:variable>
   <xsl:variable name="tipus">
      <xsl:value-of select="tipus"/>
   </xsl:variable>
   
   <form name="cerca" action="{$servletURL}" method="POST">
      <input type="hidden" name="tipus" value="{$tipus}"/>   
      <input type="hidden" name="ordenacio" value=""/>
      <input type="hidden" name="nivell" value="0"/>
      <input type="hidden" name="textCercaHidden" value="{$query}"/>
      <input type="hidden" name="novaCerca" value="no"/>
      <input type="text" name="textCerca" value="{$query}"/><br/>
      <br/>
      <input type="button" name="Cercar" value="Cercar" onClick="javascript:doSubmit();"/>
   </form>
   <a href="javascript:sortBy('puntuacio');">Ordenar per puntuació</a><br/>
   <a href="javascript:sortBy('comentaris');">Ordenar per comentaris</a><br/>
   <a href="javascript:sortBy('data');">Ordenar per data</a><br/>
   <a href="javascript:sortBy('titol');">Ordenar per títol</a><br/>   
</xsl:template>  

<xsl:template match="resultats">  
  <xsl:apply-templates/>
</xsl:template>  


<xsl:template match="nextPage">  
   <xsl:variable name="nivell">
      <xsl:value-of select="@nivell"/>
   </xsl:variable>
   <a href="javascript:goToNivell('{$nivell}')">Següent</a>
</xsl:template>  

<xsl:template match="previousPage">  
   <xsl:variable name="nivell">
      <xsl:value-of select="@nivell"/>
   </xsl:variable>
   <a href="javascript:goToNivell('{$nivell}')">Anterior</a>
</xsl:template>  


  
<xsl:template match="star">  
  <img src="./img/star_sm.gif"/>
</xsl:template>  

<xsl:template match="half-star">  
  <img src="./img/star_sm_half.gif"/>
</xsl:template>  

<xsl:template match="empty-star">  
  <img src="./img/star_sm_bg.gif"/>
</xsl:template>

  
<xsl:template match="resultat">  
  <xsl:variable name="url">
     <xsl:value-of select="url"/>
  </xsl:variable>
   <xsl:variable name="numComentaris">
      <xsl:value-of select="@numComentaris"/>
   </xsl:variable>
  <table border="1">
    <tr>
      <td>
        <xsl:apply-templates select="puntuacio/star"/>
        <xsl:apply-templates select="puntuacio/half-star"/>
        <xsl:apply-templates select="puntuacio/empty-star"/>
      </td>
    </tr>    
  
    <tr>
       <td><xsl:value-of select="titol"/>/<xsl:value-of select="autor"/>/(<xsl:value-of select="@numComentaris"/>)</td>
    </tr>
    <tr>
       <td><xsl:value-of select="descripcio"/></td>
    </tr>
    <tr>
       <td><xsl:value-of select="data"/></td>
    </tr>

    <tr>
       <td><a href="{$url}"><xsl:value-of select="url"/></a></td>
    </tr>
  </table>
</xsl:template>  
  
</xsl:stylesheet>