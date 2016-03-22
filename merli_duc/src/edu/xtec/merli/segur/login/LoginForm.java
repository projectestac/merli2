package edu.xtec.merli.segur.login;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Mike Robinson
 *
 */
public class LoginForm extends ActionForm 
{
	private String userName;
	private String password;

	public static String versionControl ="v2.0.1";
	
	/**
	 * Resets data fields to initial values on loginform
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		password = "";
		userName = "";		
	}
	
	/**
	 * Performs validation of data on loginform
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();
		
		if((userName == null) || (userName.length() < 1))
			errors.add("userName", new ActionError("error.username.required"));
		if((password == null) || (password.length() < 1))
			errors.add("password", new ActionError("error.password.required"));
		
		return errors;
			
	}
	/**
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return String
	 */
	public String getUserName() {
		return userName;
	}
	
	

	/**
	 * @param string
	 */
	public void setPassword(String string) {
		password = string;
	}

	/**
	 * @param string
	 */
	public void setUserName(String string) {
		userName = string;
	}

	//Amendez 23/03/2016 https://trello.com/c/a5nm65LM
	public static String getVersionControl(){
		return versionControl;
	}
	
}
