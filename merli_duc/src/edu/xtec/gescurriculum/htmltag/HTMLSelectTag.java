package edu.xtec.gescurriculum.htmltag;



/**
 *
 * <p>Títol: HTMLSelectTag</p>
 * <p>Descripció: Component d'&agrave;rea de text, que genera codi HTML, per enriquir qualsevol text<p/>
 * <p>Departament d'Educació - XTEC</p>
 * @version 1.0
 *
 *
 */

import java.util.ArrayList;

import org.apache.struts.taglib.html.SelectTag;

/**
 * @jsp:tag name="Select" body-content="JSP"
 */
public class HTMLSelectTag extends SelectTag implements Cloneable{
	

	  protected String lang = "en";


	  public HTMLSelectTag()  {
	  }

	  /* (non-Javadoc)
	   * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	   */
	  public int doStartTag() {
		  Object bean = pageContext.findAttribute(name);
		 try {
		  	//match = BeanUtils.getArrayProperty(bean, property);			
			if (match == null)
				match = new String[0];
		  	super.setValue(String.valueOf(match[0]));
		 }catch (Exception e) {
		}

	    return EVAL_BODY_BUFFERED;
	  }

	  /* (non-Javadoc)
	   * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	   */
	  public int doEndTag() {
	    try {
			String html;
			html = buildHTMLsubtema();
	      pageContext.getOut().write(html);
	    } catch(Exception e) {}
	    return EVAL_PAGE;
	  }

	  /**
	   * Genera el component en codi HTML.
	   * @return cadena de text amb el codi HTML que es mostrar&agrave; a la p&agrave;gina
	   */
	  public String buildHTMLsubtema() throws javax.servlet.jsp.JspException {

		String result ="", aux;
		String value;
		ArrayList subtemes;
		
		subtemes = new ArrayList();
		result ="<select name='"+getId()+"' size=";
		if(getSize() != null)	result += getSize();
		else result += "'1'";
		result += ">";
		value = getValue();
		
		for (int i = 0; i< 8; i++){		
			aux = "";
			result +="<option ";
			result +="value='"+i+"' "+aux+">";
			result +=" option "+i;
			result +="</option>\n";
		}
		result += "</select>";
	    return result;
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
	   * @jsp:attribute name="size" required="false" rtexprvalue="true" type="String"
	   * @see 
	   */
	  public void setSize(String arg0){
		super.setSize(arg0);
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

