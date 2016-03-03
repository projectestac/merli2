package edu.xtec.merli.gestorusuaris;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.MagicBean;
import edu.xtec.merli.segur.User;

public class UnitatsAction extends Action {

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

		String rIds="";
		
		MagicBean mb=new MagicBean();
		ArrayList l = mb.getElementList("unitat");
		
		for(int i=0;i<l.size();i++)
		{
			//no es un user, son unitats
			//user=id
			//mail=nom
			User u = (User)l.get(i);
			rIds+=u.getUser()+"#"+u.getMail()+"#";
		}
		response.getWriter().write(rIds);
		return (mapping.findForward(""));
	}
}
