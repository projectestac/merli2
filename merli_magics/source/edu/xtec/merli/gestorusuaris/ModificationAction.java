package edu.xtec.merli.gestorusuaris;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

		String result = "* Modificació realitzada correctament";
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		try{
			/* comprovant permisos de l'usuari.*/
			if(((User)request.getSession().getAttribute("user")).hasPermission(Operations.MAGICS.USERSET)){
			}else{
				response.getWriter().write("<message>L'usuari no disposa dels permisos necessaris</message>");
				return (mapping.findForward(""));
			}
			UserBD ubd = new UserBD();
			
			//User u = new User((String)((DynaValidatorForm)form).get("username"), (String)((DynaValidatorForm)form).get("email"));
			User u=ubd.getUser((String)((DynaValidatorForm)form).get("username"));
			//TODO!! controlar que no sigui blanc?
			u.setMail((String)((DynaValidatorForm)form).get("email"));
			
			String unitat = (String)((DynaValidatorForm) form).get("unitat");
			if (!unitat.equals("") && unitat!=null)
				u.setUnitat(Integer.valueOf(unitat));
			else u.setUnitat(null);
			
			ArrayList al = Utility.toList((String)((DynaValidatorForm)form).get("listPermissions"));
			for (int i = 0; i < al.size();i++)
				u.setPermission(Integer.parseInt((String) al.get(i)));
			
			if (((DynaValidatorForm)form).get("us_merli")!=null){
				String us_merli = (String)((DynaValidatorForm) form).get("us_merli");
				String canv_pw = "false";
				if (((DynaValidatorForm)form).get("canv_pw")!=null)
					canv_pw = (String)((DynaValidatorForm) form).get("canv_pw");
			
				if(u.getContrasenya()== null || u.getContrasenya().equals(""))//si no te contrasenya no es usuari merli
				{
					if(us_merli.equalsIgnoreCase("true"))	//si esta marcada la casella --> comprovem la contrasenya i l'afegim
					{
						String pw=(String)((DynaValidatorForm)form).get("pw");
						String repw=(String)((DynaValidatorForm)form).get("repw");
						if(comprovar_pw(pw, repw))
						{
							try{
								pw=encriptar_pw(pw);
								u.setContrasenya(pw);
							}catch(Exception e){
								result = "CONTRASENYA INCORRECTA. Modificació no realitzada";
								response.getWriter().write("<message>"+result+"</message>");
								return mapping.findForward("error");
							}
						}
						else
						{
							result = "CONTRASENYA INCORRECTA. Modificació no realitzada";
							response.getWriter().write("<message>"+result+"</message>");
							return mapping.findForward("error");
						}
					}
				}
				else								// te contrasenya --> ja es usuari merli
				{
					if(!us_merli.equalsIgnoreCase("true"))		// si esta desmarcada la casella --> deixa de ser usuari merli
						u.setContrasenya("");
					else if(!((String)((DynaValidatorForm)form).get("pw")).equals(""))//if (canv_pw.equalsIgnoreCase("true"))	// si esta marcada la casella de canviar el pw --> comprovem i la canviem
					{
						String pw=(String)((DynaValidatorForm)form).get("pw");
						String repw=(String)((DynaValidatorForm)form).get("repw");
						if(comprovar_pw(pw, repw))	
						{
							try{
								pw=encriptar_pw(pw);
								u.setContrasenya(pw);
							}catch(Exception e){
								result = "CONTRASENYA INCORRECTA. Modificació no realitzada";
								response.getWriter().write("<message>"+result+"</message>");
								return mapping.findForward("error");
							}
						}
						else 
						{
							result = "CONTRASENYA INCORRECTA. Modificació no realitzada";
							response.getWriter().write("<message>"+result+"</message>");
							return mapping.findForward("error");
						}
					}
				}
			}
			
			ubd.setUser(u);
			
		}catch(Exception e){
			result = "* Modificació No realitzada";
			response.getWriter().write("<message>"+result+"</message>");
			return mapping.findForward("error");
		}
		response.getWriter().write("<message>"+result+"</message>");
		return (mapping.findForward(""));
	}

	private boolean comprovar_pw(String pw, String repw) {
		return (!pw.equals(repw) || pw.length()<2 || pw.length()>20)? false : true;
	}
	
	private String encriptar_pw(String pw) throws NoSuchAlgorithmException{
//		MessageDigest md;
//		try {
//			md = MessageDigest.getInstance("SHA-1");
//			byte[] msg = pw.getBytes();
//			md.update(msg);
//			byte[] aMessageDigest = md.digest();
//			result=new String(aMessageDigest);
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//			result="error";
//		}
		return Utility.getEncoded(pw);
	}
}
