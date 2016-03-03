package edu.xtec.merli.gestorusuaris;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import edu.xtec.merli.MagicBean;
import edu.xtec.merli.segur.User;
import edu.xtec.merli.segur.operations.Operations;

public class PermissionAction extends Action {

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

		
		String username = (String) ((DynaValidatorForm)form).get("username");
		String mail = (String) ((DynaValidatorForm)form).get("email");
		String result="Error en el servidor";
		User user = null;
		try{
		if (username.length() > 0){
			/* comprovant permisos de l'usuari.*/
			if(((User)request.getSession().getAttribute("user")).hasPermission(Operations.MAGICS.USERSET)){
			}else{
				response.getWriter().write("<message>L'usuari no disposa dels permisos necessaris</message>");
				return (mapping.findForward(""));
			}
			MagicBean mb=new MagicBean();
			user = mb.getUser(username);
			
			//Nom del permis i descripcio
			result = "<div><span>Correu electrònic:</span><br/>" +
					 "<INPUT type=\"text\" size=\"25\" maxlength=\"20\" name=\"mailset"+username+"\" value=\""+mail+"\"  onchange=\"operModified('"+username+"');\"/>" +
					 "</div><br>";


			result +="<div>";
			
			if (user.getUs_merli().booleanValue()){
				result +="	<input type=\"checkbox\" name=\"us_merli"+username+"\" onclick=\"javascript:activa_canv_pw(this.form,this.form.us_merli"+username+",this.form.canv_pw"+username+",this.form.pw"+username+",this.form.repw"+username+");operModified('"+username+"');\" checked >";
				result +=" 	<span>Usuari MeRLí:</span><br/>";
	
				result +="	<div id=\"userPassword"+username+"\">";
			
				result +="	<input type=\"checkbox\" name=\"canv_pw"+username+"\" onclick=\"javascript:activa_pw(this.form,this.form.canv_pw"+username+",this.form.pw"+username+",this.form.repw"+username+")\" defaultChecked=\"false\">";
				result +="	<span class=\"canv_pw\">";
				result +="		Canviar password";
				result +="	</span>";
			}
			else {
				result +="	<input type=\"checkbox\" name=\"us_merli"+username+"\" onclick=\"javascript:activa_pw(this.form,this.form.us_merli"+username+",this.form.pw"+username+",this.form.repw"+username+");operModified('"+username+"');\">";
				result +=" 	<span>Usuari MeRLí:</span><br/>";
				result +="	<div id=\"userPassword"+username+"\">";
			}
	
			result +="		<div id= \"setPassword"+username+"\">";
			result +="			<br/>";
			result +="			<span class=\"pw"+username+"\">Contrasenya:</br><input type=\"password\" name=\"pw"+username+"\" disabled=\"true\" onchange=\"operModified('"+username+"');\"></span>";
			result +="			<br>";
			result +="			<span class=\"repw"+username+"\">Repetir contrasenya:</br><input type=\"password\" name=\"repw"+username+"\" disabled=\"true\" onchange=\"operModified('"+username+"');\"></span>";
			result +=" 		</div>";

			result +="	</div>";
			result +="</div>";
			ArrayList l = mb.getElementList("unitat");

			result += "<div><span>Unitat:</span><br/>";
			result += "<select name=\"unitatset"+username+"\"  onchange=\"operModified('"+username+"');\"/>";
			result += "<option value=\"\">&nbsp;</option>";
			for(int i=0;i<l.size();i++)
			{
				//no es un user, son unitats
				//user=id
				//mail=nom
				User u = (User)l.get(i);
				result+= "<option ";
				if (u.getUser().equals(String.valueOf(user.getUnitat())))
					result += "selected=\"selected\" ";
				result+="value=\""+u.getUser()+"\">"+u.getMail()+"</option>";
			}
			result += "</select></div><br>";
			
			//Operacions del permis
			MagicBean mbn = new MagicBean();
			result += mbn.createPermissionList(mbn.getPermissionList(username),username);
			//Botons per modificar.
			result += "<div style=\"clear:both;text-align:center\"><input type=\"button\" class=\"buto\" value=\"modifica\" onclick=\"modificaAJAX('"+username+"');\"/>";
			result += "<input type=\"button\" class=\"buto\" value=\"cancela\" onclick=\"cancelModif('"+username+"')\"/></div>";
		}else{
			/* comprovant permisos de l'usuari.*/
			if(((User)request.getSession().getAttribute("user")).hasPermission(Operations.MAGICS.USERADD)){
			}else{
				response.getWriter().write("<message>L'usuari no disposa dels permisos necessaris</message>");
				return (mapping.findForward(""));
			}
//			Nom del permis i descripcio
			result = "<div><span>eMail:</span><br/><INPUT type=\"text\" size=\"25\" maxlength=\"20\" name=\"mailset"+username+"\" value=\""+mail+"\"  onchange=\"operModified('"+username+"');\"/></div>";
			//Operacions del permis
			MagicBean mbn = new MagicBean();
			result += mbn.createPermissionList(mbn.getPermissionList(username),username);
			//Botons per modificar.
			result += "<input type=\"button\" class=\"buto\" value=\"Inserta\" onclick=\"addNou();\"/>";
			result += "<input type=\"button\" class=\"buto\" value=\"Cancela\" onclick=\"cancelAdd();\"/>";
		}
			
		response.getWriter().write("<message>"+result+"</message>");
		//response.getWriter().write("<message><b>terme "+((ResponderForm)form).getId()+"</b></message>");
		
		ActionErrors errors = new ActionErrors();
		String accio,id,aux="";
		int res;			
		}catch(Exception e){
			result = "Error intern a l'obtenir la informació."+e.getMessage();
			response.getWriter().write("<message>"+result+"</message>");
			e.printStackTrace();
		}
		return (mapping.findForward(""));
	}
}
