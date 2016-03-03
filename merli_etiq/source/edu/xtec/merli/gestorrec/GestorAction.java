	 package edu.xtec.merli.gestorrec;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.agrega.AgregaInterface;
import edu.xtec.merli.basedades.RecursBD;
import edu.xtec.merli.mediateca.MediatecaBean;
import edu.xtec.merli.segur.User;

public class GestorAction extends Action {

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
		
			if (((GestorForm)form).getOperation() != null){
				if (((GestorForm)form).getOperation().compareTo("addRec")==0){					
					return mapping.findForward("editrec");
				}
				if (((GestorForm)form).getOperation().compareTo("setRec")==0){	
					request.setAttribute("idRecurs",new Integer(((GestorForm)form).getIdRecurs()));
					return mapping.findForward("editrec");
				}
				if (((GestorForm)form).getOperation().compareTo("valRec")==0){	
					request.setAttribute("idRecurs",new Integer(((GestorForm)form).getIdRecurs()));
					return mapping.findForward("editrec");
				}
				if (((GestorForm)form).getOperation().compareTo("delRec")==0){
					try{
						RecursBD rbd = new RecursBD();
						rbd.delRecurs(((GestorForm)form).getIdRecurs());
					}catch(Exception e){
						return mapping.findForward("error");
					}
				}
				if (((GestorForm)form).getOperation().compareTo("dispRec")==0){
					try{
						RecursBD rbd = new RecursBD();
						String sUsuari=((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).getUser();
						rbd.marcaDisponible(((GestorForm)form).getIdRecurs(),sUsuari);
					}catch(Exception e){
						return mapping.findForward("error");
					}
				}
				if (((GestorForm)form).getOperation().compareTo("noDispRec")==0){
					try{
						RecursBD rbd = new RecursBD();
						String sUsuari=((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).getUser();
						rbd.marcaNoDisponible(((GestorForm)form).getIdRecurs(),sUsuari);
					}catch(Exception e){
						return mapping.findForward("error");
					}
				}
				if (((GestorForm)form).getOperation().compareTo("sendAgregaRec")==0){
					try{
						request.setAttribute("llistat",new Integer(((GestorForm)form).getLlistat()));
						int idRec = ((GestorForm)form).getIdRecurs();
						MerliBean mb = new MerliBean();
						if (AgregaInterface.publicarRecurs(mb.getRecurs(idRec))){//, request.getRealPath(""))){
							if (form.getClass().equals(VeuRecursForm.class))
								mb.setSendAgrega(idRec,((VeuRecursForm)form).isSendAgrega(), ((User)request.getSession().getAttribute("user")).getUser(),true);			
							else
								mb.setSendAgrega(idRec, true, ((User)request.getSession().getAttribute("user")).getUser(),true);
							request.setAttribute("actionResult","ok");
						}else{
							request.setAttribute("actionResult","fail");
							request.setAttribute("actionMessage",MerliBean.getMessage(request.getLocale(),"merli.operacio.fail.agrega.send"));
						}
					}catch(Exception e){
						request.setAttribute("actionResult","fail");
						request.setAttribute("actionMessage",MerliBean.getMessage(request.getLocale(),"merli.operacio.fail.agrega.update"));
						return mapping.findForward("error");
					}
				}
				if (((GestorForm)form).getOperation().compareTo("veureRec")==0 || ((GestorForm)form).getOperation().compareTo("tradRec")==0 || ((GestorForm)form).getOperation().compareTo("pubRec")==0 || ((GestorForm)form).getOperation().compareTo("veureRec2")==0 ){	
					request.setAttribute("idRecurs",new Integer(((GestorForm)form).getIdRecurs()));
					request.setAttribute("llistat",new Integer(((GestorForm)form).getLlistat()));
					//request.setAttribute("operacio",new Integer(((GestorForm)form).getOperation()));
					return mapping.findForward("veurec");
				}
				if (((GestorForm)form).getOperation().compareTo("migracio")==0){	
					//request.setAttribute("operacio",new Integer(((GestorForm)form).getOperation()));
					MediatecaBean mdb = new MediatecaBean();
				if (mdb.getLock()) {
					mdb.executeMigracio();
					mdb.executeDisponibilitat();
					mdb.executeRelacions();
					mdb.unlockFile();
					request.setAttribute("actionResult","ok");
					request.setAttribute("actionMessage",MerliBean.getMessage(request.getLocale(),"merli.migracio.success"));
				} else {
					request.setAttribute("actionResult","ok");
					request.setAttribute("actionMessage",MerliBean.getMessage(request.getLocale(),"merli.migracio.inprogress"));					
				}
			}
			if (((GestorForm) form).getOperation().compareTo("releaseLock") == 0) {
				MediatecaBean mdb = new MediatecaBean();
				mdb.forceUnlockFile();
			}
			}
		
		return (mapping.findForward("inserit"));
	}
}
