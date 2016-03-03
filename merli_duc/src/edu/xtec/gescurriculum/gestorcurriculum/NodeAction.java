package edu.xtec.gescurriculum.gestorcurriculum;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.segur.User;
import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.semanticnet.Node;


public class NodeAction extends Action {

	private static final Logger logger = Logger.getRootLogger();//("xtec.duc");
	
	/**
	 * Recull la sol·licitud de l'usuari per realitzar una determinada acció.
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 * @return ActionForward
	 */
	public ActionForward execute( 	ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws Exception{
			
		ActionErrors errors = new ActionErrors();
		int res = -1; 
		
		String user="";
		if (request.getSession() != null && request.getSession().getAttribute("user") != null){
			user = ((User)request.getSession().getAttribute("user")).getUser();
		}
		
		
	try{
		if(((NodeForm)form).getUser()==""){
			((NodeForm)form).setUser(user);
		}
		//Inserir un nou node.		
		if((((NodeForm)form).getOperacio().compareTo("addnode")==0) 
			||(((NodeForm)form).getOperacio().compareTo("addnodefill")==0)){
			SemanticInterface si = new SemanticInterface();
			try{
				if (((NodeForm)form).getIdNode() == 0){	
					if (((NodeForm)form).getOperacio().compareTo("addnodefill")!=0 && ((NodeForm)form).getNodeType().compareTo("content")==0	){
						Node n = si.getNode(((NodeForm)form).getIdContent(), "content");
						if (n.getProperties() != null && n.getProperties().containsKey("relatedContent"))
						((NodeForm)form).setIdContent(((Integer)n.getProperties().get("relatedContent")).intValue());
					}
					si.addNode(((NodeForm)form).toDTO());
					errors.add("addnode", new ActionError("error.addnode.ok"));
					res = 0;
					logger.info("Node added correctly.");
				}
			}catch(DUCException de){
				logger.warn("Error adding new node:"+de.getMessage());
				errors.add(((NodeForm)form).getOperacio(), new ActionError(de.getMessage()));
			}
		}		

		//Modificar un node existent.
		if(((NodeForm)form).getOperacio().compareTo("setnode")==0){
			SemanticInterface si = new SemanticInterface();
			try{
				if (((NodeForm)form).getIdNode() > 0){
					si.setNode(((NodeForm)form).toDTO());
					errors.add("setnode",new ActionError("error.setnode.ok"));
					res = 0;
					logger.info("Node modified correctly.");
				}
			}catch(DUCException de){
				logger.warn("Error modifying node:"+de.getMessage());
				errors.add(((NodeForm)form).getOperacio(), new ActionError(de.getMessage()));
			}
		}
		
		//Esborrar un node.
		if(((NodeForm)form).getOperacio().compareTo("delnode")==0){			
			  SemanticInterface si = new SemanticInterface();			  
			  try {
				si.delNode(((NodeForm)form).getIdNode(),((NodeForm)form).getNodeType());
				errors.add("delnode", new ActionError("error.delnode.ok"));
				res = 0;
				((NodeForm)form).cleanForm();
				logger.info("Node deleted correctly.");
			} catch (DUCException e) {
				logger.warn("Error deleting node:"+e.getMessage());
				errors.add("delnode", new ActionError(e.getMessage()));
				res = 0;
			}
		}

		if(((NodeForm)form).getOperacio().compareTo("")==0){
			res = 0;
		}
	}catch(Exception e){
		errors.add("delnode", new ActionError("error."+((NodeForm)form).getOperacio()+".ko"));
		e.printStackTrace();
	}
		saveErrors(request,errors);
		//Comrpova el resultat de la operació. Si ha anat bé retorna SUCCESS, FAILURE altrement
		if (res !=0){			
			return (mapping.findForward("failure"));
		}else{		
			return (mapping.findForward("success"));
		}
	}
}
