package edu.xtec.merli.etiquetador.ajax;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.merli.utils.Utility;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticException;


public class Responder extends Action
	{    
	private SemanticInterface si = new SemanticInterface();
	
	 public ActionForward execute( 	ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception{

			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("text/html; charset=UTF-8");
			//si = new SemanticInterface();
			int valor = ((ResponderForm)form).getId();
			String result;
			if (valor < 1){
				result = createMicroThesList(((ArrayList)si.getThesaurus(valor)));
			}else{
				result = createThesaurusList(((ArrayList)si.getThesaurus(valor)),"add",valor);
			}
			response.getWriter().write(""+result+"");
			//response.getWriter().write("<message><b>terme "+((ResponderForm)form).getId()+"</b></message>");
			
			ActionErrors errors = new ActionErrors();
			String accio,id,aux="";
			int res;			
		
			return (mapping.findForward(""));
			
		}

	private String createMicroThesList(ArrayList list) {
		Iterator it = list.iterator();
		Hashtable h;
		String temp, result="<ul>";
		String rt, bt;
		ArrayList lor;
		lor = Utility.orderListByInt(list,"idNode");
		
		int id=10000;
		//while (it.hasNext()){
		for (int i = lor.size()-1 ; i >=0; i--){
			//h = (Hashtable) it.next();
			h = (Hashtable) lor.get(i);
			id = ((Integer)h.get("idNode")).intValue();
			if (id % 10000 != 0){
				String temp2;
				temp = "";
				while (id % 10000 != 0 && i > 0){					
					temp2 = "<li>" + "<a href=\"#\" onclick=\"navigateTo("+h.get("idNode")+"); setMTKey(this, "+h.get("idNode")+");\" id=\""+h.get("idNode")+"\" ";
					temp2 += " title=\""+Utility.xmlEncode((String)h.get("description"))+"\"";
					temp2 += ">";
					
					temp2 += Utility.xmlEncode((String)h.get("term"));
					temp2 += "</a>";
					temp2 += "</li>";
					temp = temp2 + temp;
					i--;
					h = (Hashtable) lor.get(i);	
					id = ((Integer)h.get("idNode")).intValue();				
				}
				temp2 = "<ul>" + temp + "</ul>";
				
				temp = "<li>";
				/*+ "<a href=\"#\" onclick=\"navigateTo("+h.get("idNode")+"); setMTKey("+h.get("idNode")+");\" id=\""+h.get("idNode")+"\" ";
				temp += " title=\""+Utility.xmlEncode((String)h.get("description"))+"\"";
				temp += ">";
				*/
				
				temp += Utility.xmlEncode((String)h.get("term"));
				//temp += "</a>";
				temp += temp2;
				temp += "</li>";	
				
			}else{ 
				temp = "<li>" + "<a href=\"#\" onclick=\"navigateTo("+h.get("idNode")+"); setMTKey(this, "+h.get("idNode")+");\" id=\""+h.get("idNode")+"\" ";
				temp += " title=\""+Utility.xmlEncode((String)h.get("description"))+"\"";
				temp += ">";
				
				temp += Utility.xmlEncode((String)h.get("term"));
				temp += "</a>";
				temp += "</li>";			
			}
			result = temp + result;
		}	
		
		return result;
	}
	 
	private String createThesaurusList(ArrayList elems, String operation, int idNode) {
		// TODO Auto-generated method stub
		Iterator it = elems.iterator();
		Hashtable h;
		String temp, result="";
		String rt, bt;
		while (it.hasNext()){
			h = (Hashtable) it.next();
			temp = "<a href=\"#\" onclick=\"swapDisplay('fSormThesaure');"+operation+"Key("+h.get("idNode")+");\">";//,'"+h.get("term")+"');\">";
			temp += "<img src=\"web/images/mes.png\" title=\"Selecciona\" alt=\"Selecciona\" /></a>&nbsp;&nbsp;"; //<img src=\""+operation+"Key.png\"/>
			//Si té elements relacionats permet obrir-lo, sino dona la possibilitat d'accedir-hi
			try {
				if (1>((ArrayList)si.snet.getNodesRelated(((Integer) h.get("idNode")).intValue(),"thesaurus","RT",RelationType.SOURCE)).size()
					&& 1>((ArrayList)si.snet.getNodesRelated(((Integer) h.get("idNode")).intValue(),"thesaurus","UF",RelationType.SOURCE)).size()){
					temp += "<span title=\""+Utility.xmlEncode((String)h.get("description"))+"\" ";
					temp += " id=\""+h.get("nodeType")+h.get("idNode")+"\" >";								
					temp += Utility.xmlEncode((String)h.get("term"));					
					if (h.containsKey("relationType") && !((String)h.get("relationType")).equals("NT"))
						temp += " ("+Utility.xmlEncode((String)h.get("relationType"))+")";
					temp += "</span>";					
				}else{
					temp += "<a href=\"#\" title=\""+Utility.xmlEncode((String)h.get("description"))+"\" ";
					temp += " id=\""+h.get("nodeType")+h.get("idNode")+"\" ";			
					
					temp += " onclick=\"navigateTo("+h.get("idNode")+")\">"+Utility.xmlEncode((String)h.get("term"));
					
					if (h.containsKey("relationType") && !((String)h.get("relationType")).equals("NT"))
						temp += " ("+Utility.xmlEncode((String)h.get("relationType"))+")";
					temp += "</a>";
				}
			} catch (SemanticException e) {
				temp += "<a href=\"#\" title=\""+Utility.xmlEncode((String)h.get("description"))+"\" ";
				temp += " id=\""+h.get("nodeType")+h.get("idNode")+"\" ";			
				
				temp += " onclick=\"navigateTo("+h.get("idNode")+")\">"+Utility.xmlEncode((String)h.get("term"));
				
				if (h.containsKey("relationType") && !((String)h.get("relationType")).equals("NT"))
					temp += " ("+Utility.xmlEncode((String)h.get("relationType"))+")";
				temp += "</a>";
			}
			if (h.containsKey("relationType") && (((String)h.get("relationType")).equals("NT")||((String)h.get("relationType")).equals("NT")))
				result = temp +"<br/>"+ result;
			else
				result += "<br/>"+temp;			
		}	
		
		return result;
	}	
}
