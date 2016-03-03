package edu.xtec.merli.gestorpermisos;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import edu.xtec.merli.MagicBean;
import edu.xtec.merli.segur.User;
import edu.xtec.merli.segur.operations.Operations;

public class OperationsAction extends Action {

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

		Integer idPermis = (Integer) ((DynaValidatorForm)form).get("idPermission");
		String permis = (String) ((DynaValidatorForm)form).get("permission");
		String descrip = (String) ((DynaValidatorForm)form).get("description");
		String result="Error en el servidor";
		
		if (idPermis.intValue() > 0){
			/* comprovant permisos de l'usuari.*/
			if(((User)request.getSession().getAttribute("user")).hasPermission(Operations.MAGICS.PERMSET)){
			}else{
				response.getWriter().write("<message>L'usuari no disposa dels permisos necessaris</message>");
				return (mapping.findForward(""));
			}
			MagicBean mbn = new MagicBean();
			if (MagicBean.DEFAULT_PERM.equals(permis)){
				//Modifica el permís per defecte del sistema.
				if (mbn.setDefaultPermission(idPermis)){
					result = "<span class=\"inform\">Modificació correcte</span>";
				}else{
					result = "<span class=\"alert\">Error en l'operació</span>";
				}
			}else{
				//Nom del permis i descripcio
				result = "<div><span>Permís:</span><br/><INPUT type=\"text\" size=\"25\" maxlength=\"20\" name=\"permset"+idPermis+"\" value=\""+permis+"\"  onchange=\"operModified("+idPermis.intValue()+");\"/></div>";
				result += "<div><span>Descripció:</span><br/><INPUT type=\"text\" size=\"65\" maxlength=\"65\" name=\"mailset"+idPermis+"\" value=\""+descrip+"\"  onchange=\"operModified("+idPermis.intValue()+");\"/></div>";
				//Operacions del permis
				result += mbn.createOperationList(mbn.getOperationsList(idPermis.intValue()),idPermis.intValue());
				//Botons per modificar.
				result += "<input type=\"button\" class=\"buto\" value=\"modifica\" onclick=\"modificaAJAX("+idPermis.intValue()+");\"/>";
				result += "<input type=\"button\" class=\"buto\" value=\"cancela\" onclick=\"cancelModif("+idPermis.intValue()+")\"/>";
			}
		}
		if (idPermis.intValue()==0)
			result = "El superusuari no és editable.";
		if (idPermis.intValue() < 0){
			/* comprovant permisos de l'usuari.*/
			if(((User)request.getSession().getAttribute("user")).hasPermission(Operations.MAGICS.PERMADD)){
			}else{
				result ="L'usuari no disposa dels permisos necessaris<br/>";
				result += "<input type=\"button\" class=\"buto\" value=\"Cancela\" onclick=\"cancelAdd();\"/>";
				response.getWriter().write("<message>"+result+"</message>");
				return (mapping.findForward(""));
			}
			//Nom del permis i descripcio
			result = "<div><span>Permís:</span><br/><INPUT type=\"text\" size=\"25\" maxlength=\"20\" name=\"permnou\" value=\""+permis+"\"/></div>";
			result += "<div><span>Descripció:</span><br/><INPUT type=\"text\" size=\"65\" maxlength=\"65\" name=\"descnou\" value=\""+descrip+"\"/></div>";
			//Operacions del permis
			MagicBean mbn = new MagicBean();
			result += mbn.createOperationList(mbn.getOperationsList(idPermis.intValue()),idPermis.intValue());
			//Botons per modificar.
			result += "<input type=\"button\" class=\"buto\" value=\"Inserta\" onclick=\"addNou();\"/>";
			result += "<input type=\"button\" class=\"buto\" value=\"Cancela\" onclick=\"cancelAdd();\"/>";
		}
			
		response.getWriter().write("<message>"+result+"</message>");
		//response.getWriter().write("<message><b>terme "+((ResponderForm)form).getId()+"</b></message>");
		
		ActionErrors errors = new ActionErrors();
		String accio,id,aux="";
		int res;			
	
		return (mapping.findForward(""));
	}
}
