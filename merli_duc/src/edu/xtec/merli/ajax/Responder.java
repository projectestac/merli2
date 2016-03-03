package edu.xtec.merli.ajax;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.merli.utils.Utility;


public class Responder extends Action
	{    
	 public ActionForward execute( 	ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception{

			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			SemanticInterface si = new SemanticInterface();
			int valor = ((ResponderForm)form).getId();
			String result = Utility.createThesaurusList(((ArrayList)si.getThesaurus(valor)), "add");
		
			response.getWriter().write("<message>"+result+"</message>");
			//response.getWriter().write("<message><b>terme "+((ResponderForm)form).getId()+"</b></message>");
			
			ActionErrors errors = new ActionErrors();
			String accio,id,aux="";
			int res;			
		
			return (mapping.findForward(""));
			
		}
	 
	}
