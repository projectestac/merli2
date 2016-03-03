package edu.xtec.merli.gestorusuaris;

import java.util.List;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.segur.User;
import edu.xtec.merli.segur.operations.Operations;

import javax.servlet.http.HttpServletRequest;

public class UserForm extends ActionForm {

	private String username;
	private String oldusername;
	private String operation="";
	private String email;
	private String unitat;
	private Boolean us_merli=Boolean.FALSE;
	private String pw;
	private String repw;
	private List unitatsList;
	
	
	public List getUnitatsList() {
		return unitatsList;
	}

	public void setUnitatsList(List unitatsList) {
		this.unitatsList = unitatsList;
	}

	/**
	 * Dades inicials del formulari.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		username = "";
		email = "";
		unitat="";
		pw="";
		repw="";
	}
	
	/**
	 * Validació del contingut del formulari previ al tractament d'aquest.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		/* comprovant permisos de l'usuari.*/
		if(((User)request.getSession().getAttribute("user")).hasPermission(this.getIdOperacio())){
		}else{
			errors.add("user",new ActionError("error.user.privilegies"));
			return errors;
		}
		
		/* Validació de l'existència i la longitud dels camps.*/
		if((username == null)){
			errors.add("username", new ActionError("error.titol.required"));
		}else{
			if (username.length() > 0){
				if (username.length() < 2) 
					errors.add("username", new ActionError("error.username.curt"));
				if (username.length() > 8)
					errors.add("username", new ActionError("error.username.llarg"));
			}
			if ((email.length() > 0)&& operation!="delUser"){
				if (email.length() < 2) 
					errors.add("email", new ActionError("error.mail.curt"));
			}
			if(operation.equals("addUser"))
			{
				if(us_merli.booleanValue())
				{
					if(pw.length()<4)
						errors.add("contrasenya", new ActionError("error.contrasenya.curt"));
					if(pw.length()>12)
						errors.add("contrasenya", new ActionError("error.contrasenya.llarg"));
					if(!pw.equals(repw))
						errors.add("contrasenya", new ActionError("error.contrasenya.diferent"));
				}
			}
		}
		return errors;
	}

	private int getIdOperacio() {
		if (operation.equals("addUser")) return Operations.MAGICS.USERADD;
		if (operation.equals("delUser")) return Operations.MAGICS.USERDEL;
		return 0;
	}
	
	public String getUsername() {
		return username;
	}
	

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOperation() {
		return operation;
	}
	

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getEmail() {
		return email;
	}
	

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getUnitat() {
		return unitat;
	}

	public void setUnitat(String unitat) {
		this.unitat = unitat;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String contrasenya) {
		this.pw = contrasenya;
	}

	public String getRepw() {
		return repw;
	}

	public void setRepw(String repw) {
		this.repw = repw;
	}

	public Boolean getUs_merli() {
		return us_merli;
	}

	public void setUs_merli(Boolean us_merli) {
		this.us_merli = us_merli;
	}
	
}
