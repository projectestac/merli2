package edu.xtec.merli.gestorusuaris;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.basedades.UserBD;
import edu.xtec.merli.segur.User;

public class UserAction extends Action {

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
		UserBD ubd = new UserBD();
		Integer unitat;
		try{
			unitat = Integer.valueOf(((UserForm)form).getUnitat());
		}catch(Exception e)
		{
			unitat = null;
		}
		
		User u = new User(((UserForm)form).getUsername().toLowerCase(), ((UserForm)form).getEmail(),unitat,((UserForm)form).getPw(),((UserForm)form).getUs_merli());
		if (((UserForm)form).getOperation() != null){
			if (((UserForm)form).getOperation().compareTo("addUser")==0){
				try{
					ubd.addUser(u);
				}catch(Exception e){
					return mapping.findForward("error");
				}
			}
			if (((UserForm)form).getOperation().compareTo("setUser")==0){
				try{
					ubd.setUser(u);
				}catch(Exception e){
					return mapping.findForward("error");
				}
			}
			if (((UserForm)form).getOperation().compareTo("delUser")==0){
				try{
					ubd.delUser(u.getUser());
				}catch(Exception e){
					return mapping.findForward("error");
				}
			}
		}
		return (mapping.findForward("inserit"));
	}
}
