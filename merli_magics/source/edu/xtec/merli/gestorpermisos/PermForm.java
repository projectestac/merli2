package edu.xtec.merli.gestorpermisos;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.segur.User;
import edu.xtec.merli.segur.operations.Operations;

import javax.servlet.http.HttpServletRequest;

public class PermForm extends ActionForm {

	private String permission;
	private int idPermission;
	private String description;
	private String operation;
	private String listOperations;
	
	
	/**
	 * Dades inicials del formulari.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		idPermission = 0;
		permission = "";
		description = "";
		listOperations = "";
	}
	
	/**
	 * Validació del contingut del formulari previ al tractament d'aquest.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();

		/* Validació de l'existència i la longitud dels camps.*/
		if (operation != null && !operation.equals("delPerm"))
			if((permission == null)){
				errors.add("username", new ActionError("error.permission.required"));
			}else{
				if (permission.length() < 2) 
					errors.add("titol", new ActionError("error.etiq.permission.curt"));			
			}

		/* comprovant permisos de l'usuari.*/
		if(((User)request.getSession().getAttribute("user")).hasPermission(this.getIdOperacio())){
		}else{
			errors.add("user",new ActionError("error.user.privilegies"));
			return errors;
		}

		return errors;
		
	}


	private int getIdOperacio() {
		if (getOperation().equals("addPerm")) return Operations.MAGICS.PERMADD;
		if (getOperation().equals("delPerm")) return Operations.MAGICS.PERMDEL;
		return 0;
	}
	
	public String getDescription() {
		return description;
	}
	

	public void setDescription(String description) {
		this.description = description;
	}
	

	public int getIdPermission() {
		return idPermission;
	}
	

	public void setIdPermission(int idPermission) {
		this.idPermission = idPermission;
	}
	

	public String getOperation() {
		if (operation == null) return "";
		return operation;
	}
	

	public void setOperation(String operation) {
		this.operation = operation;
	}
	

	public String getPermission() {
		return permission;
	}
	

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getListOperations() {
		return listOperations;
	}
	

	public void setListOperations(String listOperations) {
		this.listOperations = listOperations;
	}
	


	
}
