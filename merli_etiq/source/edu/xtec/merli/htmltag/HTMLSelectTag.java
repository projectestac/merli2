package edu.xtec.merli.htmltag;



/**
 *
 * <p>Títol: HTMLSelectTag</p>
 * <p>Descripció: Component d'&agrave;rea de text, que genera codi HTML, per enriquir qualsevol text<p/>
 * <p>Departament d'Educació - XTEC</p>
 * @version 1.0
 *
 *
 */

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.util.LabelValueBean;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.etiquetador.EtiqBean;

/**
 * @jsp:tag name="Select" body-content="JSP"
 */
public class HTMLSelectTag extends SelectTag implements Cloneable{
	
	protected String label;
	protected String possibleValues;
	  protected String lang = "en";


	  public HTMLSelectTag()  {
	  }

	  /* (non-Javadoc)
	   * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	   */
	  public int doStartTag() {
		 // Object bean = pageContext.findAttribute(name);
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
			html = buildHTML();
	      pageContext.getOut().write(html);
	    } catch(Exception e) {}
	    return EVAL_PAGE;
	  }

	  /**
	   * Genera el component en codi HTML.
	   * @return cadena de text amb el codi HTML que es mostrar&agrave; a la p&agrave;gina
	   */
	  public String buildHTMLsubtema() throws javax.servlet.jsp.JspException {

		String result ="", aux = "";
		String value;
		ArrayList subtemes;
		
		subtemes = new ArrayList();
		result ="<select name='"+getId()+"' size=";
		if(getId() == "" || getId() == null)
			result ="<select name='"+getProperty()+"' size=";
		if(getSize() != null)	result += getSize();
		else result += "'15'";
		result += ">";
		value = getValue();

		String[] prop = null;
		Object bean = pageContext.findAttribute(name);
		/* 
		 * DESCOMENTAR
		 * 
		try {
			prop = BeanUtils.getArrayProperty(bean, property);
			aux = prop[0];
		} catch (IllegalAccessException e1) {
		} catch (InvocationTargetException e1) {
		} catch (NoSuchMethodException e1) {
		} catch (NullPointerException e1){}
		*/
		EtiqBean eb = new EtiqBean();
		 // aux = prop[0];
		  ArrayList al = eb.executeVaLaOperation(property);
		  ArrayList l;
		try{  
			for (int i = 0; i< al.size(); i++){	
				l = new ArrayList();
				l = (ArrayList) al.get(i);
				aux = "";
				if (l.get(0) == prop) aux = "selected";
				result +="<option ";
				result +="value='"+l.get(0)+"' "+aux+">";
				result +=l.get(1);
				result +="</option>";
			}
			result += "</select>";
		}catch(Exception e){
			result = "Problemes en el servidor.";
		}
	    return result;
	  }
	  
	  /**
	   * Genera el component en codi HTML.
	   * @return cadena de text amb el codi HTML que es mostrar&agrave; a la p&agrave;gina
	   */
	  public String buildHTML() throws javax.servlet.jsp.JspException {

		StringBuffer result = new StringBuffer();
		String value,aux = "";
		ArrayList subtemes;
		
		subtemes = new ArrayList();		
		value = getValue();

		String[] prop = null;
		String[] lprop = null;
		String[] vprop = null;
		Object bean = pageContext.findAttribute(name);
		/* 
		 * DESCOMENTAR
		 */ 
		try {
			vprop = getValuesList();
			lprop = getLabelsList();
			prop = getPossibleValues();
		} catch (IllegalAccessException e1) {
		} catch (InvocationTargetException e1) {
		} catch (NoSuchMethodException e1) {
		} catch (NullPointerException e1){}
		
		
		//EtiqBean eb = new EtiqBean();
		// aux = prop[0];
		// ArrayList al = eb.executeVaLaOperation(property);
		result.append("<select name=\"").append(property).append("\"");
		if (multiple!=null)
			result.append(" multiple=\"").append(multiple).append("\"");
		if (size!=null)
			result.append(" size=\"").append(size).append("\"");
        result.append(prepareEventHandlers());
		result.append(">");
		ArrayList l;
		try{  
			for (int i = 0; i< prop.length; i++){	
				aux = "";
				if (vprop != null){
					//Buscar si l'element actual es a la llista.
					int j = 0;
					boolean find = false;
					while (j<vprop.length && !find){
						if (vprop[j].equals(prop[i])){
							find = true;
							aux = "selected=\"selected\"";
						}
						j++;
					}
				} 													//("+prop[i]+")
				result.append("<option value=\"");//name=\""+property+"\"";
				result.append(prop[i]).append("\"" );
				result.append(aux).append(">");
				if (lprop != null){
					try{
						result.append(lprop[i]);
					}catch(Exception e){}
				}else
					result.append(prop[i]);
				result.append("</option>");
					
			}
		result.append("</select>");
		}catch(Exception e){			
			result = new StringBuffer("Problemes en el servidor.");
		}
	    return result.toString();
	  }
	  
	  protected String[] getValuesList() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
			Object bean = pageContext.findAttribute(name);
			return BeanUtils.getArrayProperty(bean, property);
	  }
	  
	  protected String[] getLabelsList() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
			if (label != null){
				Object bean = pageContext.findAttribute(name);
				return BeanUtils.getArrayProperty(bean, label);
			}
			return new String[0];
	  }
	  
	  protected String[] getPossibleValues() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{	
		  if (possibleValues != null){
				Object bean = pageContext.findAttribute(name);
				return BeanUtils.getArrayProperty(bean, possibleValues);
		  }
		  return new String[0];
	  }

	  /** 
	   * @jsp:attribute name="label" required="false" rtexprvalue="true" type="String"
	   * @see 
	   */
	  public void setLabel(String arg0){
		label = arg0;
	  }
	  /** 
	   * @jsp:attribute name="possibleValues" required="false" rtexprvalue="true" type="String"
	   * @see 
	   */
	  public void setPossibleValues(String arg0){
		possibleValues = arg0;
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

