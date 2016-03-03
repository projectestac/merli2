package edu.xtec.merli.gestorpermisos;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import edu.xtec.merli.basedades.UserBD;
import edu.xtec.merli.segur.Permission;
import edu.xtec.merli.utils.Utility;

/**
 * Les operacions que es fan aqui sobre la BBDD amb l'obj. UserBD haurien de ser crides al MagicBean 
 * i aquest es qui hauria de executar l'accio.  
 * @author aleix
 *
 */
public class PermAction extends Action {

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
				if (((PermForm)form).getOperation() != null){
					UserBD ubd = new UserBD();
					Permission p = new Permission(((PermForm)form).getIdPermission(), ((PermForm)form).getPermission());
					p.setDescription(((PermForm)form).getDescription());
					p.setOperations(Utility.toList((String)((PermForm)form).getListOperations()));
					if (((PermForm)form).getOperation().compareTo("addPerm")==0){
						try{
							ubd.addPermission(p);
						}catch(Exception e){
							return mapping.findForward("error");
						}
					}
					if (((PermForm)form).getOperation().compareTo("setPerm")==0){
						try{
							ubd.setPermission(p);							
						}catch(Exception e){
							return mapping.findForward("error");
						}
					}

					if (((PermForm)form).getOperation().compareTo("delPerm")==0){
						try{
							ubd.delPermission(p.getIdPermission());
						}catch(Exception e){
							return mapping.findForward("error");
						}
					}
				}
		
		return (mapping.findForward("inserit"));
	}
}
