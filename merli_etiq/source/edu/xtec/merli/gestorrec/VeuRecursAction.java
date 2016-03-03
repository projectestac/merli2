package edu.xtec.merli.gestorrec;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.MerliContribution;
import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.segur.User;

public class VeuRecursAction extends Action {
	
	private static final String EN = "en";
	private static final String ES = "es";
	private static final String CA = "ca";
	private static final String OC = "oc";
	private static final String MISSATGE_ACCIO = "actionMessage";
	private static final String RESULT_ACCIO = "actionResult";
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
			MerliBean mb = new MerliBean();
			mb.locale=request.getLocale();
			mb.messages = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);
			String peuMissatge = "\n\nMissatge enviat per l'usuari: "+((User)request.getSession().getAttribute("user")).getUser()+".\n";
			peuMissatge += "Si vol pot contactar amb l'usuari a l'adreça electrònica: "+((User)request.getSession().getAttribute("user")).getMail();
			String prefixAssumpte="[Merlí] ";
			logger.info("VeuRecForm - "+((VeuRecursForm)form).getOperation());
			if (((VeuRecursForm)form).getRm() == null){
				return (mapping.findForward("loading"));
			}
			//ASSIGN
			if (((VeuRecursForm)form).getOperation() != null && ((VeuRecursForm)form).getOperation().equals("assign")){
				autoAssignar(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()), ((VeuRecursForm)form).getUsuari(), mb);
				mb.setEstat(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()),MerliBean.ESTAT_M_EN_PROCES, ((VeuRecursForm)form).getUsuari());
			}
			//DENEGAR
			if (((VeuRecursForm)form).getOperation() != null && ((VeuRecursForm)form).getOperation().equals("denegar")){
				denegar(form, request, mb, prefixAssumpte, peuMissatge);
			}
			//RETORNAR
			if (((VeuRecursForm)form).getOperation() != null && ((VeuRecursForm)form).getOperation().equals("retornar")){
				retornar(form, request, mb, prefixAssumpte, peuMissatge);
			}
			//VALIDAR
			if (((VeuRecursForm)form).getOperation() != null && ((VeuRecursForm)form).getOperation().equals("validar")){
				mb.setEstat(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()),MerliBean.ESTAT_M_PUBLICAT, ((User)request.getSession().getAttribute("user")).getUser());				
				}
			//EDITAR
			if (((VeuRecursForm)form).getOperation() != null && ((VeuRecursForm)form).getOperation().equals("editar")){
				return (mapping.findForward("editar"));		
			}
			//CORREGIR
			if (((VeuRecursForm)form).getOperation() != null && ((VeuRecursForm)form).getOperation().equals("corregir")){
				corregir(form, request, mb);
			}
			//SALVAR
			if (((VeuRecursForm)form).getOperation() != null && ((VeuRecursForm)form).getOperation().equals("salvar")){
				salvar(form, request, mb);				
			}
			//TRADUIR
			if (((VeuRecursForm)form).getOperation() != null && ((VeuRecursForm)form).getOperation().equals("traduir")){
				traduir(form, request, mb);
			}
			//Integració amb AGREGA.
			if (((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).hasPermission(edu.xtec.merli.segur.operations.MerliOperations.AGREGA)){				
				try{
					if (!mb.calPublicarAgrega(((VeuRecursForm)form).isSendAgrega(),((VeuRecursForm)form).isValidaTradEs(),((VeuRecursForm)form).getRm().getAgregaDate())){
						if ((null==((VeuRecursForm)form).getRm().getAgregaDate()))
							mb.setSendAgrega(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()),((VeuRecursForm)form).isSendAgrega(), ((User)request.getSession().getAttribute("user")).getUser());
						request.setAttribute(RESULT_ACCIO,"ok");
					}else{
						if (mb.publicarRecursAgrega(((VeuRecursForm)form).getRm())){
							mb.setSendAgrega(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()),((VeuRecursForm)form).isSendAgrega(), ((User)request.getSession().getAttribute("user")).getUser(),true);			
							request.setAttribute(RESULT_ACCIO,"ok");
						}else{
							request.setAttribute(RESULT_ACCIO,"fail");
							request.setAttribute(MISSATGE_ACCIO,MerliBean.getMessage(request.getLocale(),"merli.operacio.fail.agrega.send"));
						}
					}
				}catch(Exception e){
					request.setAttribute(RESULT_ACCIO,"fail");
					request.setAttribute(MISSATGE_ACCIO,MerliBean.getMessage(request.getLocale(),"merli.operacio.fail.agrega.update"));
				}
			}
			request.setAttribute("llistat",((VeuRecursForm)form).getLlistat());			
			return (mapping.findForward(""));
	}



	private void autoAssignar(int idRec, String usuari, MerliBean mb) throws MerliDBException {
		mb.setContribucio(idRec,usuari,MerliContribution.ETIQUETADOR);
		mb.setEstat(idRec,MerliBean.ESTAT_M_EN_PROCES, usuari);
	}



	/**
	 * @param form
	 * @param request
	 * @param mb
	 * @param peuMissatge
	 * @throws MerliDBException
	 */
	private void denegar(ActionForm form, HttpServletRequest request, MerliBean mb, String prefixAssumpte, String peuMissatge) throws MerliDBException {
		mb.setEstat(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()),MerliBean.ESTAT_M_DENEGAT, ((User)request.getSession().getAttribute("user")).getUser());
		try{
			mb.setMessage(((VeuRecursForm)form).getRm(), prefixAssumpte+((VeuRecursForm)form).getSubject(), ((VeuRecursForm)form).getMissatge()+peuMissatge,((User)request.getSession().getAttribute("user")).getMail(),mb.getUserMail(((VeuRecursForm)form).getEtiquetador()));
		}catch(Exception e){
			
		}
	}



	/**
	 * @param form
	 * @param request
	 * @param mb
	 * @param peuMissatge
	 * @throws MerliDBException
	 */
	private void retornar(ActionForm form, HttpServletRequest request, MerliBean mb, String prefixAssumpte, String peuMissatge) throws MerliDBException {
		mb.setEstat(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()),MerliBean.ESTAT_M_RETORNAT, ((User)request.getSession().getAttribute("user")).getUser());
		try{
			mb.setMessage(((VeuRecursForm)form).getRm(), prefixAssumpte+((VeuRecursForm)form).getSubject(), ((VeuRecursForm)form).getMissatge()+peuMissatge,((User)request.getSession().getAttribute("user")).getMail(),mb.getUserMail(((VeuRecursForm)form).getEtiquetador()));
		}catch(Exception e){
			
		}
	}



	/**
	 * @param form
	 * @param request
	 * @param mb
	 * @throws MerliDBException
	 */
	private void corregir(ActionForm form, HttpServletRequest request, MerliBean mb) throws MerliDBException {
		mb.corregir(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()),
				((VeuRecursForm)form).getTitle(),
				((VeuRecursForm)form).getDescription(),
				((VeuRecursForm)form).getRightsDesc());
		mb.setEstat(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()),MerliBean.ESTAT_M_PUBLICAT, ((User)request.getSession().getAttribute("user")).getUser());
		try{
			//mb.setMessage(((VeuRecursForm)form).getRm(), ((VeuRecursForm)form).getSubject(), ((VeuRecursForm)form).getMissatge()+peuMissatge,((User)request.getSession().getAttribute("user")).getUser(),mb.getUserMail(((VeuRecursForm)form).getEtiquetador()));
		}catch(Exception e){
			
		}
	}



	/**
	 * @param form
	 * @param request
	 * @param mb
	 * @throws MerliDBException
	 */
	private void salvar(ActionForm form, HttpServletRequest request, MerliBean mb) throws MerliDBException {
		mb.corregir(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()),
				((VeuRecursForm)form).getTitle(),
				((VeuRecursForm)form).getDescription(),
				((VeuRecursForm)form).getRightsDesc());
		mb.setContribucio(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()),((User)request.getSession().getAttribute("user")).getUser(),MerliContribution.CORRECTOR);
	}
	
	
	
	/**
	 * @param form
	 * @param request
	 * @param mb
	 * @throws MerliDBException
	 * @throws SQLException
	 */
	private void traduir(ActionForm form, HttpServletRequest request, MerliBean mb) throws MerliDBException, SQLException {
		mb.salvarTextos(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()), CA,
				((VeuRecursForm)form).getTitle(), ((VeuRecursForm)form).getDescription(),((VeuRecursForm)form).getRightsDesc(),
				((VeuRecursForm)form).isValidaTradCa());
		mb.salvarTextos(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()), ES,
				((VeuRecursForm)form).getTitleEs(), ((VeuRecursForm)form).getDescriptionEs(),((VeuRecursForm)form).getRightsEs(),
				((VeuRecursForm)form).isValidaTradEs());
		mb.salvarTextos(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()), EN,
				((VeuRecursForm)form).getTitleEn(), ((VeuRecursForm)form).getDescriptionEn(),((VeuRecursForm)form).getRightsEn(),
				((VeuRecursForm)form).isValidaTradEn());
		mb.salvarTextos(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()), OC,
				((VeuRecursForm)form).getTitleOc(), ((VeuRecursForm)form).getDescriptionOc(),((VeuRecursForm)form).getRightsOc(),
				((VeuRecursForm)form).isValidaTradOc());
		mb.setContribucio(Integer.parseInt(((VeuRecursForm)form).getIdRecurs()),((User)request.getSession().getAttribute("user")).getUser(),MerliContribution.TRADUCTOR);
	}
}
