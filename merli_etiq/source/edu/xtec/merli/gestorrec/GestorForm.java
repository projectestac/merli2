package edu.xtec.merli.gestorrec;

import java.util.ArrayList;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.basedades.RecursBD;
import edu.xtec.merli.segur.User;
import edu.xtec.merli.segur.operations.Operations;

import javax.servlet.http.HttpServletRequest;

public class GestorForm extends ActionForm {

	private int idRecurs;
	private String operation;
	private String llistat;
	private String user;
	
	String id_unitat="";
	String[] labUnitat={};
	String[] posUnitat={};
	
	public GestorForm(){
		initRecurs();
	}

	
	/**
	 * Dades inicials del formulari.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{

		llistat = request.getParameter("llistat");

		if (llistat == null || llistat.compareTo("")==0){
			llistat=String.valueOf(MerliBean.LLISTA_M_PROPIS);
		}
		
	}
	
	
	
	private void initRecurs() {
		RecursBD rbd = new RecursBD();
		try {
				ArrayList lAux = rbd.getUnitatsReals(RecursBD.IDENTIFIERS);
				lAux.add(0, "");
				labUnitat = createValueList(lAux);
				ArrayList lAuxValues = rbd.getUnitatsReals(RecursBD.LABELS);
				lAuxValues.add(0, "");
				posUnitat = createLabelList(lAux, lAuxValues);			
			} catch (MerliDBException e) {
				e.printStackTrace();
			}
			id_unitat="-1";
	}
	
	public String[] createLabelList(ArrayList values, ArrayList label) {  //*new* method
		String[] lab = new String[values.size()];
		for (int i = 0; i<values.size(); i++){
			if (label!= null && label.get(i)!=null)
				lab[i]= (String) label.get(i);
			else
				lab[i]= values.get(i).toString();
		}
		return lab;
	}
	public String[] createValueList(ArrayList values) {  //*new* method
		String[] pos = new String[values.size()];
		for (int i = 0; i<values.size(); i++){
			pos[i]= values.get(i).toString();
		}
		return pos;
	}
	
	
	
	
	/**
	 * Validació del contingut del formulari previ al tractament d'aquest.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors = new ActionErrors();

		if (operation != null){
			if(idRecurs < 1 && operation.equals("setRec")){
				errors.add("recurs", new ActionError("error.recurs.required"));
			}
			if(idRecurs < 1 && operation.equals("delRec")){
				errors.add("recurs", new ActionError("error.recurs.required"));
			}
			if(idRecurs < 1 && (operation.equals("veureRec") || operation.equals("tradRec")  || operation.equals("pubRec") || operation.equals("veureRec2"))){
				errors.add("recurs", new ActionError("error.recurs.required"));
			}
			if(idRecurs < 1 && operation.equals("sendAgregaRec")){
				errors.add("recurs", new ActionError("error.recurs.required"));
			}
			if(idRecurs < 1 && operation.equals("dispRec")){
				errors.add("recurs", new ActionError("error.recurs.required"));
			}

			/* comprovant permisos de l'usuari.*/
//			if(((User)request.getSession().getAttribute("user")).hasPermission(this.getIdOperacio())){
//			}else{
//				errors.add("user",new ActionError("error.user.privilegies"));
//				return errors;
//			}
		}

		return errors;
		
	}

	public int getIdOperacio() {
		if (operation.equals("addRec")) return Operations.MERLI.RECADD;
		if (operation.equals("setRec")) return Operations.MERLI.RECSET;
		if (operation.equals("valRec")) return Operations.MERLI.PUBLICAR;
		if (operation.equals("delRec")) return Operations.MERLI.RECDEL;
		if (operation.equals("selec")) return Operations.DUC.SELEC;
		if (operation.equals("addnode")) return Operations.DUC.ADD;
		if (operation.equals("addnodefill")) return Operations.DUC.ADD;
		if (operation.equals("setnode")) return Operations.DUC.SET;
		if (operation.equals("sendAgregaRec")) return Operations.MERLI.AGREGA;
		if (operation.equals("veureRec") || operation.equals("tradRec") || operation.equals("veureRec2")){
			if (Integer.parseInt(llistat) == MerliBean.LLISTA_M_PENDENTS)
				return Operations.MERLI.PENDVIEW;
			if (Integer.parseInt(llistat) == MerliBean.LLISTA_M_REALITZATS_VALIDAR)
				return Operations.MERLI.PUBLICAR;
			if (Integer.parseInt(llistat) == MerliBean.LLISTA_M_REALITZATS)
				return Operations.MERLI.RECSET;
			if (Integer.parseInt(llistat) == MerliBean.LLISTA_M_AGREGA_PEND)
				return Operations.MERLI.AGREGA;
			if (Integer.parseInt(llistat) == MerliBean.LLISTA_M_AGREGA_FET)
				return Operations.MERLI.AGREGA;
			return Operations.MERLI.RECADD;
		}
		return 0;
	}

	public int getIdRecurs() {
		return idRecurs;
	}
	public void setIdRecurs(int idRecurs) {
		this.idRecurs = idRecurs;
	}
	
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

	public String getLlistat() {
		if (llistat == null || llistat.equals(""))
			return "recurspropi";
		return llistat;
	}
	public void setLlistat(String llistat) {
		this.llistat = llistat;
	}


	public String getId_unitat() {
		return id_unitat;
	}


	public void setId_unitat(String id_unitat) {
		this.id_unitat = id_unitat;
	}


	public String[] getLabUnitat() {
		return labUnitat;
	}


	public void setLabUnitat(String[] labUnitat) {
		this.labUnitat = labUnitat;
	}


	public String[] getPosUnitat() {
		return posUnitat;
	}


	public void setPosUnitat(String[] posUnitat) {
		this.posUnitat = posUnitat;
	}

}
