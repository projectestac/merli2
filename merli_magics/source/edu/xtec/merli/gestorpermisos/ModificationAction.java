package edu.xtec.merli.gestorpermisos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import edu.xtec.merli.basedades.UserBD;
import edu.xtec.merli.segur.Permission;
import edu.xtec.merli.segur.User;
import edu.xtec.merli.segur.operations.Operations;
import edu.xtec.merli.utils.Utility;

public class ModificationAction extends Action {

	/**
	 * Recull la sol·licitud de l'usuari per insertar un nou recurs.
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

		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/html; charset=UTF-8");

		/* comprovant permisos de l'usuari.*/
		if(((User)request.getSession().getAttribute("user")).hasPermission(Operations.MAGICS.PERMSET)){
		}else{
			response.getWriter().write("<message>L'usuari no disposa dels permisos necessaris</message>");
			return (mapping.findForward(""));
		}
		UserBD ubd = new UserBD();
		Permission p = new Permission(((Integer)((DynaValidatorForm)form).get("idPermission")).intValue(), (String)((DynaValidatorForm)form).get("permission"));
		p.setDescription((String)((DynaValidatorForm)form).get("description"));
		p.setOperations(Utility.toList((String)((DynaValidatorForm)form).get("listOperations")));
		
		String result;
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		try{
			ubd.setPermission(p);	
			result = "* Modificació realitzada correctament";
		}catch(Exception e){
			result = "* Modificació No realitzada";
			return mapping.findForward("error");
		}
		response.getWriter().write("<message>"+result+"</message>");
		
		ActionErrors errors = new ActionErrors();
		String accio,id,aux="";
		int res;			
	
		return (mapping.findForward(""));
	}
}
