package edu.xtec.merli.etiquetador.ajax;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.merli.utils.Utility;


public class CurriculumResponder extends Action
	{    
	 public ActionForward execute( 	ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception{

			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("text/html; charset=UTF-8");
			SemanticInterface si = new SemanticInterface();
			
			int level,area,content;
			String curriculum;

			try{
				level = Integer.parseInt((String)((DynaValidatorForm)form).get("level"));
			}catch(NumberFormatException nfe){
				level = 0;
			}
			try{
				area = Integer.parseInt((String)((DynaValidatorForm)form).get("area"));
			}catch(NumberFormatException nfe){
				area = 0;
			}
			try{
				content = Integer.parseInt((String)((DynaValidatorForm)form).get("content"));
			}catch(NumberFormatException nfe){
				content = 0;
			}
			try{
				curriculum =(String)((DynaValidatorForm)form).get("curriculum");
			}catch(NumberFormatException nfe){
				curriculum = "";
			}
			String type = (String)((DynaValidatorForm)form).get("type");
			
			String result = type+level;//Utility.createThesaurusList(((ArrayList)si.getThesaurus(valor)),"add");
			
			if (type.equals("level"))
				result = createLevelList(si, level, curriculum);
			if (type.equals("area"))
					result = createAreaList(si, area, level, curriculum);
			if (type.equals("content"))
				result = createContentList(si, level, area, content, curriculum);
			
			
			
			response.getWriter().write("<message>"+result+"</message>");
			
			ActionErrors errors = new ActionErrors();
		
			return (mapping.findForward(""));
			
		}

	private String createContentList(SemanticInterface si, int level, int area, int content, String curriculum) {
		  int contValue = content;
		  if (contValue < 0){contValue = 0;}
		  Hashtable h = new Hashtable();
		  ArrayList al;
		  Iterator it;
		  String result;
		  
		  try{
			  h = si.getContents(contValue,level,area);
			  //Node n = sn.getNode(((NodeForm)bean).getIdContent(),"content");			 
		  }catch(Exception e){
		  }
			  al = (ArrayList) h.get("cc");
			  al = Utility.orderListByPosition(al);
			  it = al.iterator();
			  result = createOLnew(it, contValue, curriculum,true);
			  if (((Integer) ((Hashtable)al.get(0)).get("idNode")).intValue() < 0){
				  result = result.replaceAll("<ol>","<ul>");
				  result = result.replaceAll("</ol>","</ul>");		
			  }

			  return result;
	}

	private String createAreaList(SemanticInterface si, int area, int level, String curriculum) {
		  String result = null;
		  ArrayList al;
		  Iterator it;
		  
		  try{
			  al = si.getArees(area,level);
			  al = Utility.orderListByPosition(al);
			  it = al.iterator();
			  result = createOLnew(it, area, curriculum,true);	
			  if (((Integer) ((Hashtable)al.get(0)).get("idNode")).intValue() < 0){
				  result = result.replaceAll("<ol>","<ul>");
				  result = result.replaceAll("</ol>","</ul>");		
			  }					  
		  }catch(Exception e){
			  result = "<ul><li>No hi ha elements.</li></ul>";//result.replaceAll("<ol>","<ul>");
			  //result = result.replaceAll("</ol>","</ul>");	  
		  }
		 return result;
	}

	private String createLevelList(SemanticInterface si, int level, String curriculum) {
		ArrayList al;
		Iterator it;
		String result = "";
		  try{
			  al = si.getLevels(level);	
			  al = Utility.orderListByPosition(al);
			  it = al.iterator();
			  
			  // TODO: delete "origen" node. Next line hides it
			  if (al.size()>1 && it.hasNext()) it.next();
			  result=createOLnew(it, level, curriculum,false);	
			  if (((Integer) ((Hashtable)al.get(0)).get("idNode")).intValue() < 0){
				  result = result.replaceAll("<ol>","<ul>");
				  result = result.replaceAll("</ol>","</ul>");		
			  }					  
		  }catch(Exception e){
			  result = "<ul><li>No hi ha elements.</li></ul>";//result.replaceAll("<ol>","<ul>");
			  //result = result.replaceAll("</ol>","</ul>");
		  }
		return result;
	}

	
	
	
	
	 
	public String createOLnew(Iterator it, int value, String checks, boolean radio){
		 String result;
		 String temp = "", aux;
		 String selec;
		 Hashtable h;
		 ArrayList al;
		 result = "";


	while (it.hasNext()){
		h = (Hashtable) it.next();
		if (((Integer) h.get("idNode")).intValue() != 0) {
			if (((Integer) h.get("idNode")).intValue() > 0){
				if (checks.indexOf((String)h.get("nodeType")+(Integer)h.get("idNode"))>=0)
					aux = "checked=\"checked\"";
				else
					aux = "";
				if (radio){
					temp = "<input type=\"checkbox\" name=\"curriculumBox\" "+aux+"  style=\"margin: 5px;height:12px;border:0px;\" onclick=\"changeCurriculum("+(Integer)h.get("idNode")+", this);\" value=\""+h.get("nodeType")+h.get("idNode")+"\"/>";
				}else{
					temp = "";
				}

				temp +="<a href=\"#\" title=\""+Utility.xmlEncode((String)h.get("description"))+"\" ";
				
				if(((Integer)h.get("idNode")).intValue() == value){
					temp += " class=\"selected\" ";
				}
				temp += " id=\""+h.get("nodeType")+h.get("idNode")+"\" ";
				temp += " onclick=\"getCurriculumList("+h.get("idNode")+",'"+h.get("nodeType")+"');\">"+Utility.xmlEncode((String)h.get("term"))+"</a>";

				al = (ArrayList) h.get("list");
				al = Utility.orderListByPosition(al);
				if(al != null){
					temp += createOLnew(al.iterator(), value, checks, radio);
				}			
				result += "<li"+">"+temp+"</li>";			
			}else{
				return "<ul><li>No hi ha elements.</li></ul>";
			}
		}
	}
	result = "<ol>"+result+"</ol>";		
	
	return result;
	 }
	
	}
