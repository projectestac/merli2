<?xml version = "1.0" encoding = "UTF-8"?>

<!--
 * Removes the namespaces from an XML document.
 *
 * Note that is never a good idea to remove the namespaces from a document.
 * This transformation is only intended to be used for serialization on a
 * SOAP envelope that can be understood by the current LOM web service
 * implementation.
 * -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="no" omit-xml-declaration="yes"/>
  <xsl:strip-space elements="*"/>

  <!-- Elements -->
  <xsl:template match="*">
    <xsl:element name="{local-name()}">
      <xsl:apply-templates select="@*|node()"/>
    </xsl:element>
  </xsl:template>

  <!-- Attributes -->
  <xsl:template match="@*">
    <xsl:attribute name="{local-name()}">
      <xsl:value-of select="."/>
    </xsl:attribute>
  </xsl:template>

  <!-- Other nodes -->
  <xsl:template match="/|comment()|processing-instruction()">
    <xsl:copy>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

</xsl:stylesheet>
