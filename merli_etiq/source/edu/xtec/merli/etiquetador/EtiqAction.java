package edu.xtec.merli.etiquetador;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.basedades.RecursBD;
import edu.xtec.merli.segur.User;

public class EtiqAction extends Action {

	private static final Logger logger = Logger.getRootLogger();
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

		ActionErrors errors = new ActionErrors();
	//	ActionError error = new ActionError("error.etiq.titol.curt");
	//	errors.add("etiq",error);
	//	saveErrors(request,errors);

		if ("cancel".equals(((EtiqForm)form).getOperacio())){
			return mapping.findForward("cancel");
		}
		try{
			if (((EtiqForm)form).getOperacio().compareTo("modifyRec") == 0){
				try{
					RecursBD rbd = new RecursBD();
					//pretractarCheckbox(request, form);
					//pretractarSelects(request, form);
					rbd.setRecurs(((EtiqForm)form).getIdRecurs(), ((EtiqForm)form).getRecursMerli(), (edu.xtec.merli.segur.User)request.getSession().getAttribute("user"));
					return (mapping.findForward("inserit"));
				}catch(Exception e){
					logger.error("Error modifying resource ->"+e);
					return mapping.findForward("error");
				}
			}
			if (((EtiqForm)form).getOperacio().compareTo("validarRec") == 0){
				try{
					RecursBD rbd = new RecursBD();
					MerliBean mb = new MerliBean();
					rbd.setRecurs(((EtiqForm)form).getIdRecurs(), ((EtiqForm)form).getRecursMerli(),(edu.xtec.merli.segur.User)request.getSession().getAttribute("user"));
					if (((EtiqForm)form).getEstatSel()[0].equals(String.valueOf(MerliBean.ESTAT_M_PUBLICAT)))
						mb.setEstat(Integer.parseInt(((EtiqForm)form).getIdRecurs()),MerliBean.ESTAT_M_PUBLICAT, ((User)request.getSession().getAttribute("user")).getUser());
					return (mapping.findForward("inserit"));
				}catch(Exception e){
					logger.error("Error Validant resource ->"+e);
					return mapping.findForward("error");
				}
			}
			if (((EtiqForm)form).getOperacio().compareTo("newRec")==0 || ((EtiqForm)form).getOperacio().compareTo("newRecWS")==0){
				try{
					RecursBD rbd = new RecursBD();
					rbd.addRecurs(((EtiqForm)form).getRecursMerli());
					return (mapping.findForward("inserit"));
				}catch(Exception e){
					logger.error("Error adding a new resource ->"+e);
					e.printStackTrace();
					return mapping.findForward("error");
				}
			}
		}catch(Exception e){
			logger.error("Error in EtiqAction->"+e);
			return mapping.findForward("error");
		}
		return (mapping.findForward("nou"));
	}
	
	private void pretractarSelects(HttpServletRequest request, ActionForm form) {
		String[] buit={};
		if (request.getParameter("rolUser") == null)
			((EtiqForm)form).setRolUser(buit);
		if (request.getParameter("ambit") == null)
			((EtiqForm)form).setAmbit(buit);
		if (request.getParameter("llengues") == null)
			((EtiqForm)form).setLlengues(buit);
		if (request.getParameter("format") == null)
			((EtiqForm)form).setFormat(buit);
		if (request.getParameter("unitats") == null)
			((EtiqForm)form).setUnitats(buit);
	}
	
	private void pretractarCheckbox(HttpServletRequest request, ActionForm form) {
		String[] buit={};
		if (request.getParameter("context") == null)
			((EtiqForm)form).setContext(buit);
		if (request.getParameter("tipRec") == null)
			((EtiqForm)form).setTipRec(buit);
	}

	public ActionForward perform(ActionMapping mapping,
            ActionForm form,
            javax.servlet.http.HttpServletRequest request,
            javax.servlet.http.HttpServletResponse response)
     		throws java.io.IOException,
            javax.servlet.ServletException{
		ActionForward actionForward = super.perform(mapping, form, request, response);
		return actionForward;		
	}
	
}
