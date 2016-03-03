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
import org.apache.struts.taglib.html.MultiboxTag;
import org.apache.struts.util.LabelValueBean;

import edu.xtec.merli.etiquetador.EtiqBean;

/**
 * @jsp:tag name="Select" body-content="JSP"
 */
public class HTMLMultiboxTag extends MultiboxTag implements Cloneable{
	
	protected String label;
	protected boolean breakLine;
	protected String possibleValues;
	protected String max;
	protected String min;
	  protected String lang = "en";


	  public HTMLMultiboxTag()  {
	  }

	  /* (non-Javadoc)
	   * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	   * /
	  public int doStartTag() {
		 // Object bean = pageContext.findAttribute(name);
		 try {
		  	match = BeanUtils.getArrayProperty(bean, property);			
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
	  public String buildHTML() throws javax.servlet.jsp.JspException {

		StringBuffer result = new StringBuffer();
		String value,aux = "";
		ArrayList subtemes;
		
		subtemes = new ArrayList();
		value = getValue();

		String[] prop = null;
		String[] lprop = null;
		String[] vprop = null;
		String[] mxprop = null;
		String[] mnprop = null;
		Object bean = pageContext.findAttribute(name);
		/* 
		 * DESCOMENTAR
		 */ 
		try {
			vprop = getValuesList();
			lprop = getLabelsList();
			prop = getPossibleValues();
			mxprop = getMaxList();
			mnprop = getMinList();
		} catch (IllegalAccessException e1) {
		} catch (InvocationTargetException e1) {
		} catch (NoSuchMethodException e1) {
		} catch (NullPointerException e1){}
		
		
		//EtiqBean eb = new EtiqBean();
		// aux = prop[0];
		// ArrayList al = eb.executeVaLaOperation(property);
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
							aux = "checked=\"checked\"";
						}
						j++;
					}
				} 													//("+prop[i]+")
				result.append("<input type=\"checkbox\" name=\""+property+"\"");
				
				if (max != null)
					result.append(" max=\"").append(mxprop[i]).append("\" ");
				if (min != null)
					result.append(" min=\"").append(mnprop[i]).append("\" ");
				
				result.append("value='").append(prop[i]).append("' ").append(aux);
		        result.append(prepareEventHandlers());
				result.append(" style=\"margin: 5px;height:12px;border:0px;\"/>");
				if (lprop != null)
					try{
						result.append(lprop[i]);
					}catch(Exception e){}
				if (breakLine)
					result.append("<br/>");
			}
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
	  
	  protected String[] getMaxList() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
			if (max != null){
				Object bean = pageContext.findAttribute(name);
				return BeanUtils.getArrayProperty(bean, max);
			}
			return new String[0];
	  }
	  protected String[] getMinList() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
			if (min != null){
				Object bean = pageContext.findAttribute(name);
				return BeanUtils.getArrayProperty(bean, min);
			}
			return new String[0];
	  }

	/** 
	 * @jsp:attribute name="max" required="false" rtexprvalue="true" type="String"
	 * @see 
	 */
	public void setMax(String arg0){
		max =arg0;
	}
	
	/** 
	 * @jsp:attribute name="min" required="false" rtexprvalue="true" type="String"
	 * @see 
	 */
	public void setMin(String arg0){
		min = arg0;
	}

	  /** 
	   * @jsp:attribute name="label" required="false" rtexprvalue="false" type="String"
	   * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
	   */
	  public void setLabel(String arg0){
	  	label = arg0;
	  }
	  

	  /** 
	   * @jsp:attribute name="breakline" required="false" rtexprvalue="false" type="boolean"
	   * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.Boolean)
	   */
	  public void setBreakLine(boolean arg0){
	  	breakLine = arg0;
	  }
	  

	  /** 
	   * @jsp:attribute name="possibleValues" required="false" rtexprvalue="false" type="String"
	   * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
	   */
	  public void setPossibleValues(String arg0){
		  possibleValues = arg0;
	  }
	  
	  
	  
	  /** 
	   * @jsp:attribute name="id" required="false" rtexprvalue="false" type="String"
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

