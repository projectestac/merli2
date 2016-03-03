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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.taglib.html.BaseHandlerTag;
import org.apache.struts.util.MessageResources;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.segur.User;
import edu.xtec.merli.utils.Utility;

/**
 * @jsp:tag name="ul" body-content="JSP"
 */
public class HTMLulTag extends BaseHandlerTag implements Cloneable{
	

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String lang = "en";
	  protected String name;
	  protected String property;
	  protected int idNode;

	  public HTMLulTag()  {

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
		  Object bean = pageContext.findAttribute(name);
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
		  Iterator it, iter, iter2;
		  ArrayList al = new ArrayList();
		  Hashtable h = new Hashtable();
		  String result ="", value, aux;
		  String temp="";
		  String[] prop = null;
		  Object bean = pageContext.findAttribute(id);
		  StringBuffer text = new StringBuffer();
		  try {
			prop = BeanUtils.getArrayProperty(bean, property);
		} catch (IllegalAccessException e1) {
		} catch (InvocationTargetException e1) {
		} catch (NoSuchMethodException e1) {
		}
		  
		  MerliBean mb = new MerliBean();//(MerliBean) pageContext.findAttribute(name);
		  aux = prop[0];
		  try{
		  al = mb.executeOperation(((User)pageContext.getSession().getAttribute("user")).getUser(),aux);
		  
		  al = Utility.orderListByPosition(al);
		  it = al.iterator();
		  MessageResources messages = (MessageResources)pageContext.getRequest().getAttribute(Globals.MESSAGES_KEY);
		  
		  while (it.hasNext()){
			  h = (Hashtable)it.next();
			  /**
			   * Paragraf per cada un dels recursos
			   */
			  text.append("<p id=\"").append(h.get("id")).append("\">");
			  /**
			   * Radiobutton per seleccionar el recurs i realitzar-hi operacions.
			   */
			  text.append("<input name=\"").append(getName());
			  text.append("\" value=\"").append(h.get("id")).append("\"");
			  text.append(" type=\"radio\" ");
			  text.append(" onclick=\"").append(getOnclick()).append("\"/>");
			  /**
			   * Nom del recurs amb la descripció com a 'title' i URL com a HREF
			   */
			  text.append("<span class=\"title\"");
			  if (h.containsKey("description")){
				  text.append("title=\"");
				  text.append(messages.getMessage(pageContext.getRequest().getLocale(),"merli.recurs"));
				  text.append(" ").append(h.get("id")).append(": ");
				  text.append(h.get("description"));
				  text.append("\"");
			  }
			  text.append(">");
				  /**
				   * link al recurs sobre el nom.
				   */
				  text.append("<a href=\"").append(h.get("url")).append("\">");
				  text.append(h.get("name"));
				  text.append("</a>");
			  text.append("</span>");
			 /**
			  * Atribut addicional si s'escau.
			  */
			 if (h.containsKey("attribute")){
				  temp += "<span class=\"attribute\">"; 
				  temp += h.get("attribute");
				  temp += "</span>";
			  }
			  /**
			   * URL del recurs
			   */
			  if (h.containsKey("url")){
				  text.append("<span class=\"url\">");
				  text.append(h.get("url"));
				  text.append("</span>");
			  }
			  /**
			   * informació de l'Estat en q es troba cada recurs
			   */
			  if (h.containsKey("estat")){
				  switch (Integer.parseInt((String) h.get("estat"))){
				  	case MerliBean.ESTAT_M_DENEGAT:
				  	case MerliBean.ESTAT_M_RETORNAT:
						text.append("<span class=\"alert\">");
						break;
					case MerliBean.ESTAT_M_EN_PROCES:
								text.append("<span class=\"message\">");
								break;
				  	case MerliBean.ESTAT_M_PENDENT:
				  	case MerliBean.ESTAT_M_REALITZAT:	
								text.append("<span class=\"notice\">");
								break;
				  	case MerliBean.ESTAT_M_PUBLICAT:		
								text.append("<span class=\"inform\">");
								break;
					default:							
				  }
				
				  text.append("<span class=\"estat\">");
				  try{
				  text.append(messages.getMessage(pageContext.getRequest().getLocale(),"merli.estat.user."+h.get("estat")));
				  }catch (Exception e){
					  e.printStackTrace();
				  }
				  text.append("</span>");
			  }
			  text.append("</p>");

		  }
		  result = text.toString();
		  }catch(Exception e){
			  text = new StringBuffer("<span class=\"alert\">");
			  text.append(messages.getMessage(pageContext.getRequest().getLocale(),"error.merli.llistat.recursos"));
			  text.append("</span>");
			  result = text.toString();
		  }
		  if (this.id.compareTo("NOuser") == 0){
			  try{
			  //result = String.valueOf(((UserForm)bean).getUsername());
			  }catch(Exception e){}
			  try{
			//	  al = mb.getElementList("users");
				  al = Utility.orderListByPosition(al);
				  it = al.iterator();
				  User u;
				  while (it.hasNext()){
					  u = (User) it.next();
					  temp += "<p id=\""+u.getUser()+"\">";
					  temp += "<input name=\""+this.getName()+"\" value=\""+u.getUser()+"\" type=\"radio\" onclick=\""+this.getOnclick()+"\"/>"; 
					  temp += "<span class=\"user\">";
					  temp += "<b>"+u.getUser()+"</b>"; 
					  temp += "</span>"; 
					  temp += "<span class=\"url\">"; 
					  temp += u.getMail(); 
					  temp += "</span>"; 
					  temp += "</p>";
				  }
				  result = temp;
			  }catch(Exception e){
				  result = result.replaceAll("<ol>","<ul>");
				  result = result.replaceAll("</ol>","</ul>");	  
			  }
		  }
		  return result;
	  }
  
	  
	  
	private String createThesaurusList(ArrayList elems, String operation) {
		// TODO Auto-generated method stub
		Iterator it = elems.iterator();
		Hashtable h;
		String temp, result="";
		while (it.hasNext()){
			h = (Hashtable) it.next();
			temp = "<a href=\"#\" onclick=\"swapDisplay('fSormThesaure');"+operation+"Key("+h.get("idNode")+");\">";//,'"+h.get("term")+"');\">";
			temp += "<img src=\"web/images/mes.png\" title=\"Selecciona\" alt=\"Selecciona\" /></a>"; //<img src=\""+operation+"Key.png\"/>
			temp += "<a href=\"#\" title=\""+Utility.xmlEncode((String)h.get("description"))+"\" ";
			temp += " id=\""+h.get("nodeType")+h.get("idNode")+"\" ";			
			
			temp += " onclick=\"navigateTo("+h.get("idNode")+")\">"+Utility.xmlEncode((String)h.get("term"))+"</a>";
						
			result += "<br/>"+temp;			
		}	
		
		return result;
	}

	public String createOLnew(Iterator it, int value){
			 String result;
			 String temp = null;
			 String selec;
			 Hashtable h;
			 ArrayList al;
			 result = "";


		while (it.hasNext()){
			h = (Hashtable) it.next();
			temp = "<a href=\"#\" title=\""+Utility.xmlEncode((String)h.get("description"))+"\" ";
			if(((Integer)h.get("idNode")).intValue() == value && ((Integer) h.get("idNode")).intValue() != 0){
				if(((Integer)h.get("idNode")).intValue() == idNode){
					temp += " class=\"selected\" ";
				}else{
					temp += " class=\"selected2\" ";
				}
			}
			temp += " id=\""+h.get("nodeType")+h.get("idNode")+"\" ";			//}
			if (((Integer) h.get("idNode")).intValue() > 0)
				temp += " onclick=\"selec('"+h.get("nodeType")+"','"+h.get("category")+"',"+h.get("idNode")+",this)\">"+Utility.xmlEncode((String)h.get("term"))+"</a>";
			else
				temp += " onclick=\"addNodeAlone(this,'"+(String)h.get("nodeType")+"','"+(String)h.get("category")+"')\">"+Utility.xmlEncode((String)h.get("term"))+"</a>";
			al = (ArrayList) h.get("list");
			al = Utility.orderListByPosition(al);
			if(al != null){
				temp += createOLnew(al.iterator(), value);
			}			
			result += "<li"+">"+temp+"</li>";			
		}
		result = "<ol>"+result+"</ol>";		
		
		return result;
		 }
	  
	  
	 public String createOL(Iterator it, int value){
		 String result;
		 String temp = null;
		 String selec;
		 Hashtable h;
		 ArrayList al;
		 result = "";


		while (it.hasNext()){
			h = (Hashtable) it.next();
			if(((Integer)h.get("id")).intValue() == value){
				temp = "<span class=\"selected\" ";
			}else{
				temp = "<span title=\""+Utility.xmlEncode((String)h.get("description"))+"\" ";
			}
			temp += "onclick=\"selec('"+h.get("type")+"',"+h.get("id")+",this)\">"+Utility.xmlEncode((String)h.get("node"))+"</span>";
			al = (ArrayList) h.get("list");
			al = Utility.orderListByPosition(al);
			if(al != null){
				temp += createOL(al.iterator(), value);
			}			
			result += "\n<li"+">"+temp+"</li>";			
			}
			result = "<ol>"+result+"</ol>\n";
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

