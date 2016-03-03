package edu.xtec.gescurriculum.htmltag;

/**
*
* <p>Títol: HTMLTextareaTag</p>
* <p>Descripció: Component d'&agrave;rea de text, que genera codi HTML, per enriquir qualsevol text<p/>
* <p>Departament d'Educació - XTEC</p>
* @version 1.0
*
*
*/

import org.apache.struts.taglib.html.BaseHandlerTag;
import org.apache.struts.taglib.html.TextareaTag;


/**
* @jsp:tag name="AreaTextoHTML" body-content="JSP"
*/
public class HTMLTextareaTag extends TextareaTag implements Cloneable {

 protected String lang = "en";
 protected String base = "htmlarea/";
 protected String toolbar;


 public HTMLTextareaTag()  {

 }

 /* (non-Javadoc)
  * @see javax.servlet.jsp.tagext.Tag#doStartTag()
  */
 public int doStartTag() {
   return EVAL_BODY_BUFFERED;
 }

 /* (non-Javadoc)
  * @see javax.servlet.jsp.tagext.Tag#doEndTag()
  */
 public int doEndTag() {
   try {
     String html = buildHTML();
     pageContext.getOut().write(html);
   } catch(Exception e) {}
   return EVAL_PAGE;
 }

 /**
  * Genera el component en codi HTML.
  * @return cadena de text amb el codi HTML que es mostrar&agrave; a la p&agrave;gina
  */
 public String buildHTML() throws javax.servlet.jsp.JspException {
   String result = "<script language='javascript' src='"+base+"htmlarea.js'></script>\n";
   result += "<script language='javascript' src='"+base+"dialog.js'></script>\n";
   result += "<script language='javascript' src='"+base+"popupwin.js'></script>\n";
   result += "<script language='javascript' src='"+base+"lang/"+lang+".js'></script>\n";
   result += "<textarea id='"+getId()+"' name='";
   if (getName() != null) result +=getName();
   else result += getId();
   result += "' " + prepareEventHandlers() + prepareStyles();
   if(getRows() != null) result += " rows='"+getRows()+"'";
   if(getCols() != null) result += " cols='"+getCols()+"'";
   result += ">\n";
   if(getBodyContent() != null) result += getBodyContent().getString();
   result += "</textarea>\n";
   result += "<script language='javascript'>\n";
   result += "var "+getId()+"HTML = new HTMLArea.Config('"+base+"','"+lang+"');\n";
   if(toolbar != null) result += getId()+"HTML.toolbar="+toolbar+";\n";
   if(getStyle() != null) result += getId()+"HTML.pageStyle='body {"+getStyle()+"}';\n";
   if(getStyleClass() != null) result += getId()+"HTML.pageClass='"+getStyleClass()+"';\n";
   result += "var "+getId()+"Editor = HTMLArea.replace('"+getId()+"',"+getId()+"HTML);\n";
   result += "</script>\n";
   return result;
 }
 
 /**
  * @jsp:attribute name="lang" required="false" rtexprvalue="true" type="String"
  * 
  * Idioma amb el que es carregarà l'editor.
  * @param lang idioma (indicat amb el codi de 2 lletres ISO 639)
  */
 public void setLang(String lang) {
       this.lang = lang;
 }
 public String getLang() {
   return lang;
 }
 /**
  * @jsp:attribute name="base" required="false" rtexprvalue="false" type="String"
  * 
  * Localització del component HTML
  * @param base path del component HTML (relatiu o absolut)
  */
 public void setBase(String base) {
       this.base = base;
 }
 public String getBase() {
   return base;
 }

 /**
  * @jsp:attribute name="toolbar" required="false" rtexprvalue="false" type="String"
  * 
  * Configuració de la barra d'eines. 
  * Permet definir els botons que han d'apar&egrave;ixer en el component.
  * @param toolbar array d'arrays amb les barres que han d'apar&egrave;ixer 
  * i els botons que ha de contenir cadascuna.
  * 
  * <pre>
  * EXEMPLE:
  * [
  * ['fontname', 'space','fontsize', 'space','formatblock', 'space','bold', 'italic', 'underline']
  * ]
  * </pre>
  * Cada array defineix els botons d'una l&iacute;nia a la barra d'eines del component.
  * 
  * <pre>
  * EXEMPLE: Array amb totes les opciones possibles en una barra.
  * 
  * [
  * ["fontname", "space", "fontsize", "space", "formatblock", "space", "bold", "italic", "underline", "separator", "strikethrough", "subscript", "superscript", "separator", "copy", "cut", "paste", "space", "undo", "redo" ],
  * ["justifyleft", "justifycenter", "justifyright", "justifyfull", "separator", "insertorderedlist", "insertunorderedlist", "outdent", "indent", "separator", "forecolor", "hilitecolor", "textindicator", "separator", "inserthorizontalrule", "createlink", "insertimage", "inserttable", "htmlmode", "separator", "popupeditor", "separator", "showhelp", "about" ]
  * ]
  * </pre>
  * 
  * Les següents opcions no són funcions de contingut al textarea, sino opcions de disseny de la barra d'eines:
  * <ul>
  * <li>'space' inserta 5 pixels</li>
  * <li>'separator' inserta un separador vertical "|"</li>
  * <li>'linebreak' Crea una nueva linea en el toolbar</li>
  * </ul>
  * 
  * @param toolbar cadena de text amb l'array de configuració de la barra d'eines
  */
 public void setToolbar(String toolbar) {
       this.toolbar = toolbar;
 }
 public String getToolbar() {
       return toolbar;
 }
 
 /** 
  * @jsp:attribute name="id" required="true" rtexprvalue="false" type="String"
  * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
  */
 public void setId(String arg0){
       super.setId(arg0);
 }
 
 /** 
  * @jsp:attribute name="name" required="false" rtexprvalue="false" type="String"
  * @see org.apache.struts.taglib.html.TextareaTag#setName(java.lang.String)
  */
 public void setName(String arg0){
       super.setName(arg0);
 }
 /** 
  * @jsp:attribute name="value" required="false" rtexprvalue="true" type="String"
  * @see 
  */
 public void setValue(String arg0){
       super.setValue(arg0);
 }
 /** 
  * @jsp:attribute name="style" required="false" rtexprvalue="false" type="String"
  * @see org.apache.struts.taglib.html.BaseHandlerTag#setStyle(java.lang.String)
  */
 public void setStyle(String arg0){
       super.setStyle(arg0);
 }
 /** 
  * @jsp:attribute name="styleClass" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setStyleClass(String arg0){
       super.setStyleClass(arg0);
 }
 /** 
  * @jsp:attribute name="styleId" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setStyleId(String arg0){
       super.setStyleId(arg0);
 }
 /** 
  * @jsp:attribute name="tabindex" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setTabindex(String arg0){
       super.setTabindex(arg0);
 }
 /** 
  * @jsp:attribute name="cols" required="false" rtexprvalue="false" type="String"
  * @see org.apache.struts.taglib.html.BaseInputTag#setCols(java.lang.String)
  */
 public void setCols(String arg0){
       super.setCols(arg0);
 }
 /** 
  * @jsp:attribute name="rows" required="false" rtexprvalue="false" type="String"
  * @see org.apache.struts.taglib.html.BaseInputTag#setRows(java.lang.String)
  */
 public void setRows(String arg0){
       super.setRows(arg0);
 }
 /** 
  * @jsp:attribute name="readonly" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setReadonly(boolean arg0){
       super.setReadonly(arg0);
 }
 /** 
  * @jsp:attribute name="disabled" required="false" rtexprvalue="false" type="Boolean"
  * @see org.apache.struts.taglib.html.BaseHandlerTag#setDisabled(boolean)
  */
 public void setDisabled(boolean arg0){
       super.setDisabled(arg0);
 }
 /** 
  * @jsp:attribute name="property" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setProperty(String arg0){
       super.setProperty(arg0);
 }
 /** 
  * @jsp:attribute name="accesskey" required="false" rtexprvalue="false" type="String"
  * @see org.apache.struts.taglib.html.BaseHandlerTag#setAccesskey(java.lang.String)
  */
 public void setAccesskey(String arg0){
       super.setAccesskey(arg0);
 }
 /** 
  * @jsp:attribute name="onblur" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnblur(String arg0){
       super.setOnblur(arg0);
 }
 /** 
  * @jsp:attribute name="onchange" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnchange(String arg0){
       super.setOnchange(arg0);
 }
 /** 
  * @jsp:attribute name="onclick" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnclick(String arg0){
       super.setOnclick(arg0);
 }
 

/** 
  * @jsp:attribute name="ondblclick" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOndblclick(String arg0){
       super.setOndblclick(arg0);
 }
 /** 
  * @jsp:attribute name="onfocus" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnfocus(String arg0){
       super.setOnfocus(arg0);
 }
 /** 
  * @jsp:attribute name="onkeydown" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnkeydown(String arg0){
       super.setOnkeydown(arg0);
 }
 /** 
  * @jsp:attribute name="onkeypress" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnkeypress(String arg0){
       super.setOnkeypress(arg0);
 }
 /** 
  * @jsp:attribute name="onkeyup" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnkeyup(String arg0){
       super.setOnkeyup(arg0);
 }
 /** 
  * @jsp:attribute name="onmousedown" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnmousedown(String arg0){
       super.setOnmousedown(arg0);
 }
 /** 
  * @jsp:attribute name="onmousemove" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnmousemove(String arg0){
       super.setOnmousemove(arg0);
 }
 /** 
  * @jsp:attribute name="onmouseout" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnmouseout(String arg0){
	super.setOnmouseout(arg0); 
 }
 /** 
  * @jsp:attribute name="onmouseover" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnmouseover(String arg0){
	super.setOnmouseover(arg0); 
 }
 /** 
  * @jsp:attribute name="onmouseup" required="false" rtexprvalue="false" type="String"
  * @see 
  */
 public void setOnmouseup(String arg0){
       super.setOnmouseup(arg0);
 }

}
