package edu.xtec.merli.htmltag;

/**
 *
 * <p>Títol: HTMLRadioTag</p>
 * <p>Descripció: Component d'&agrave;rea de text, que genera codi HTML, per enriquir qualsevol text<p/>
 * <p>Departament d'Educació - XTEC</p>
 * @version 1.0
 *
 *
 */

import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.taglib.html.BaseHandlerTag;

import edu.xtec.merli.utils.Utility;

/**
 * @jsp:tag name="ul" body-content="JSP"
 */
public class HTMLThesaurusTag extends BaseHandlerTag implements Cloneable{
	

	  protected String lang = "en";
	  protected String name;
	  protected String property;
	  protected String label;

	  public HTMLThesaurusTag()  {

	  }

	  public String getName() {
		return name;
	}
	

	public String getProperty() {
		return property;
	}
	

	/* (non-Javadoc)
	   * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	   */
	  public int doStartTag() {
		 try {
			  	//BeanUtils.getArrayProperty(bean, property);			
			  	//super.setValue("String.valueOf(match[0])");
			 }catch (Exception e) {
			}
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
			  String prop;
			  String lab;
			  
			  ArrayList lProp, lLab;
			  
			  StringBuffer result = new StringBuffer();
			  Object bean = pageContext.findAttribute(name);
			  try {
				prop = BeanUtils.getProperty(bean, property);
				lProp = Utility.toList(prop,";");
				lab = BeanUtils.getProperty(bean, label);			  
				lLab = Utility.toList(lab,";");
			  } catch (Exception e) {
					lProp= new ArrayList();
					lLab= new ArrayList();
			  }

			  result.append("<div id=\"path\" class=\"thesaurus\">");
			  result.append("<span id=\"parClauActual\"><br></span>");
			  //result.append("<a href=\"#\" id=\"closeThesaurus\" onclick=\"swapDisplay('formThesaure');\">X </a>");
			  //result.append("<a href=\"#\" id=\"path0\" onclick=\"navigateTo(0);\">INICI/ </a>");
			  result.append("</div>");

			  result.append("<div id=\"thesaurus\" class=\"thesaurus\">");				  
			  result.append("</div>");
				  
			  result.append("<div id=\"keys\" class=\"thesaurus\">");
			//  result.append("<a href=\"https://sites.google.com/a/xtec.cat/merli/el-cataleg/tesaurus#TOC-Tesaurus:-Llenguatge-controlat\" title=\"Ajuda\"><img src=\"web/images/ajuda.png\" /></a>");
			  result.append(" Descriptors tesaurus:");

			  for (int i = 0; i< lProp.size(); i++){
				result.append("<span class=\"keyValue\"");
				result.append(" id=\"keyValue").append(lProp.get(i)).append("\">");
				 result.append("<a href=\"#\"");
				  result.append(" onclick=\"navigateTo("+lProp.get(i)+");\"");
				  result.append(" id=\"key"+lProp.get(i)+"\">");
				  if (lLab.get(i) != null)
					  result.append(Utility.xmlEncode((String) lLab.get(i)));
				  else
					  result.append(" term").append(lProp.get(i));
				  result.append("</a>");
				  result.append("<a href=\"#\"");
				  	result.append(" onclick=\"delKey("+lProp.get(i)+");\"");
				  	result.append(" id=\"delkey"+lProp.get(i)+"\">");
				  	result.append(" <img src=\"web/images/elimina.png\" title=\"Elimina el terme '"+Utility.xmlEncode((String) lLab.get(i))+"'\" alt=\"Elimina el terme "+Utility.xmlEncode((String) lLab.get(i))+"\" />");
				  result.append("</a>&nbsp;");
				result.append("</span>");
			  }
				  
			  result.append("</div>");
			  
			  return result.toString();
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
	   * @jsp:attribute name="label" required="false" rtexprvalue="true" type="String"
	   * 
	   * Llista de noms dels elements seleccionats.
	   * @param lebel llistat String[] amb els noms dels elements. En concordança amb el property.
	   */
	  public void setLabel(String lang) {
		this.label = lang;
	  }
	  public String getLabel() {
	    return label;
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
		name = arg0;
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
		property = arg0;
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
	   * @jsp:attribute name="onclick" required="false" rtexprvalue="true" type="String"
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

