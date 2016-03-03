package edu.xtec.merli.ajax;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class ResponderForm extends ActionForm {

	private int id;
	
	
	/**
	 * Dades inicials del formulari.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{

	}
	
	/**
	 * Validació del contingut del formulari previ al tractament d'aquest.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();

		return errors;
		
	}

	public int getId() {
		return id;
	}
	

	public void setId(int id) {
		this.id = id;
	}
	

	
}
