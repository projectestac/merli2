package edu.xtec.merli.etiquetador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.MerliContribution;
import edu.xtec.merli.RecursMerli;
import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.basedades.RecursBD;
import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.merli.utils.Utility;
import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;

public class EtiqForm extends ActionForm {

	/*Desc Gen*/
	private String titol="";
	private String descripcio;
	private String titolEs="";
	private String descripcioEs;
	private String titolEn="";
	private String descripcioEn;
	private String url;
	private String versio="";
	private String responsable;
	private String dataResponsable;		//DATA DE CATALOGACIO DE LA FITXA

	/*Assign Term*/
	private String selecTerm;
	private String selecLabel;
	private String selecParaula;
	private String selecParaulaId;
	
	/*Desc EDU*/
	private String[] rolUser={};
	private String[] posRolUser;
	private String[] labRolUser;
	private String edatMin="";
	private String edatMax="";
	private String duraHora="";
	private String duraMin="";
	private String duraSeg="";
	private String[] dificultat={};
	private String[] posDificultat;
	private String[] labDificultat;
	private String[] context={};
	private String[] posNivellSc;
	private String[] labNivellSc;
	private String[] maxNivellSc;
	private String[] minNivellSc;
	private String[] posNivellSp;
	private String[] labNivellSp;
	private String[] maxNivellSp;
	private String[] minNivellSp;
	private String[] posNivellAd;
	private String[] labNivellAd;
	private String[] maxNivellAd;
	private String[] minNivellAd;
	
		
	/*Desc Tec*/
	private String[] posTipRec;
	private String[] tipRec = {};
	private String[] labTipRec;
	
	//private String format="";
	private String[] format = {};
	private String[] labFormat;
	private String[] posFormat;
	
	//private String llengues="";
	private String[] posLlengues;
	private String[] llengues = {};
	private String[] labLlengues;
	
	/*Desc Credits*/
	//private String drets;
	private String descDrets="";
	private String descDretsEs="";
	private String descDretsEn="";
	private String[] llicencia={};
	private String[] labLlicencia;
	private String[] posLlicencia;
	private String[] ambit={};
	private String[] labAmbit;
	private String[] posAmbit;
	private String autor="";
	private String validador="";
	private String dataValidador="";		//DATA DE PUBLICACIO DE LA FITXA
	private String corrector="";
	private String editor="";
	private String data="";					//DATA DE CREACIO/PUBLICACIO DEL RECURS
	private String cost="";
	
	/*Curriculum*/
	private String curLevel;
	private String curArea;
	private String curContent;
	private String curriculum;
	
	/*Argega*/
	private String agrega;
	private String agregaDate;
	
	/*Extres*/
	private String idRecurs="0";
	private String operacio="";
	//private String estat="";
	//private String[] estat={};
	private String[] estatSel;
	private Iterator contentCurriculum;
	private String llistat="";
	
	/*Traduccions*/
	private boolean estatCa;
	private boolean estatEn;
	private boolean estatEs;
	private boolean estatOc;

	private String context2="";
	private String[] tipusRel={};
	private String[] tipusRelSel;
	private String[] recRel={};
	private String[] descRel;
	private Boolean recFisic;
	private String caractRFisic="";
	private String[] idFisic={};
	private String[] tipusIdFisic={};
	private String[] tipusIdFisicSel={};
	
	private String nu = "";
	
	private String[] unitats={};
	private String[] labUnitats={};
	private String[] posUnitats={};
	
	private String usuariActual="";
	
	private String comprovaHidden="";
	
	private static final Logger logger = Logger.getRootLogger();
	
	public EtiqForm(){
		initRecurs();
	}

	/**
	 * Dades inicials del formulari.
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request)
	{
		usuariActual=((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).getUser();
		try{
			this.setOperacio(request.getParameter("operation"));
			idRecurs = request.getParameter("idRecurs");	
			responsable = ((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).getUser();
			if(operacio!=null && operacio.equals("editarWS2"))
			{
				Map params = request.getParameterMap();
				this.loadRecursFromUrl(params);
			}
			else if(!idRecurs.equals("0")){
				this.loadRecurs(idRecurs);
			}
		}catch(Exception e){
			logger.warn("Error reseting etiqform.->"+e);
			e.printStackTrace();
		}
	}
	
	
	private void initRecurs() {
		RecursBD rbd = new RecursBD();
		ArrayList lAux, lAuxValues;
		cost = "0";
		curriculum = "";
		agrega = "0";
		agregaDate="";
		comprovaHidden="0";
		
		//operacio = "addRec";
		recFisic=null;

		try {
			lAux = rbd.getLlengues(RecursBD.IDENTIFIERS);
			posLlengues = createValueList(lAux);
			labLlengues = createLabelList(lAux, rbd.getLlengues(RecursBD.LABELS));
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}
		
		try {
			lAux = rbd.getUnitatsReals(RecursBD.IDENTIFIERS);
			lAuxValues = rbd.getUnitatsReals(RecursBD.LABELS);
					
			posUnitats = createValueList(lAux);
			labUnitats = createLabelList(lAux, lAuxValues);
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}
		
		try {
			lAux = rbd.getLlicencia(RecursBD.IDENTIFIERS);
			lAux.add(0,new Integer(9));
			posLlicencia = createValueList(lAux);
			lAuxValues=rbd.getLlicencia(RecursBD.LABELS);
			lAuxValues.add(0,"");
			labLlicencia = createLabelList(lAux, lAuxValues);
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}
		
		//de moment omplo la combo amb els formats online, si es fisic, la modificare a loadRecurs
		try {
			lAux = rbd.getFormatLimitat(RecursBD.IDENTIFIERS);
			posFormat = createValueList(lAux);
			labFormat = createLabelList(lAux, rbd.getFormatLimitat(RecursBD.LABELS));			
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}
		
		try {
			lAux = rbd.getRolUser(RecursBD.IDENTIFIERS);
			posRolUser = createValueList(lAux);
			labRolUser = createLabelList(lAux, rbd.getRolUser(RecursBD.LABELS));
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}
		
		try {
			lAux = rbd.getDificultat(RecursBD.IDENTIFIERS);
			posDificultat = createValueList(lAux);
			labDificultat = createLabelList(lAux, rbd.getDificultat(RecursBD.LABELS));
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}
		
		try {
			lAux = rbd.getAmbit(RecursBD.IDENTIFIERS);
			posAmbit = createValueList(lAux);
			lAuxValues = rbd.getAmbit(RecursBD.LABELS);
			labAmbit = createLabelList(lAux, lAuxValues);			
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}
		
		try {
			lAux = rbd.getTipusRelacio(RecursBD.IDENTIFIERS);
			tipusRel = createValueList(lAux);
			lAuxValues = rbd.getTipusRelacio(RecursBD.LABELS);
			tipusRel = createLabelList(lAux, lAuxValues);			
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}
		
		//lAux = rbd.getEstat(RecursBD.IDENTIFIERS); //--> els estats NO estan a la BD
//		lAux = new ArrayList();
//		lAux.add("0");
//		lAux.add("Denegat");
//		lAux.add("Retornat");
//		lAux.add("Esborrany");
//		lAux.add("Per assignar");
//		lAux.add("En revisió");
//		lAux.add("En correcció");
//		lAux.add("Acceptat");
//		estat = createValueList(lAux);
		
		estatSel=new String[1];
		estatSel[0]="0";

		
		try {
			lAux = rbd.getTipusIdFisic(RecursBD.IDENTIFIERS);
			lAux.add(0, new Integer(-1));
			tipusIdFisic = createValueList(lAux);
			lAuxValues = rbd.getTipusIdFisic(RecursBD.LABELS);
			lAuxValues.add(0, "");
			tipusIdFisic = createLabelList(lAux, lAuxValues);			
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}

		try {
			lAux = rbd.getResourceType(RecursBD.IDENTIFIERS);
			posTipRec = createValueList(lAux);
			labTipRec = createLabelList(lAux, rbd.getResourceType(RecursBD.LABELS));
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}

		try {
			lAux = rbd.getNivell(RecursBD.ESCOLAR, RecursBD.IDENTIFIERS);
			posNivellSc = createValueList(lAux);
			labNivellSc = createLabelList(lAux, rbd.getNivell(RecursBD.ESCOLAR, RecursBD.LABELS));
			maxNivellSc = createValueList(rbd.getNivell(RecursBD.ESCOLAR, RecursBD.MAXIM));
			minNivellSc = createValueList(rbd.getNivell(RecursBD.ESCOLAR, RecursBD.MINIM));
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}
		try {
			lAux = rbd.getNivell(RecursBD.ESPECIAL, RecursBD.IDENTIFIERS);
			posNivellSp = createValueList(lAux);
			labNivellSp = createLabelList(lAux, rbd.getNivell(RecursBD.ESPECIAL, RecursBD.LABELS));
			maxNivellSp = createValueList(rbd.getNivell(RecursBD.ESPECIAL, RecursBD.MAXIM));
			minNivellSp = createValueList(rbd.getNivell(RecursBD.ESPECIAL, RecursBD.MINIM));
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}
		try {
			lAux = rbd.getNivell(RecursBD.ADMINISTRATIU, RecursBD.IDENTIFIERS);
			posNivellAd = createValueList(lAux);
			labNivellAd = createLabelList(lAux, rbd.getNivell(RecursBD.ADMINISTRATIU, RecursBD.LABELS));
			maxNivellAd = createValueList(rbd.getNivell(RecursBD.ADMINISTRATIU, RecursBD.MAXIM));
			minNivellAd = createValueList(rbd.getNivell(RecursBD.ADMINISTRATIU, RecursBD.MINIM));
		} catch (MerliDBException e) {
			logger.error("Error initalizing resource form. ->"+e);
			e.printStackTrace();
		}
		
		cost="2";			//cost indefinit
		SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");
		dataResponsable=sDate.format(new Date());
		if (Integer.parseInt(idRecurs) > 0){
			this.loadRecurs(idRecurs);
		}
	}

	public void createTipusRecurs(ArrayList values, ArrayList label) {  //*new* method
		posTipRec = new String[values.size()];
		labTipRec = new String[values.size()];
		for (int i = 0; i<values.size(); i++){
			if (label!= null && label.get(i)!=null)
				labTipRec[i]= (String) label.get(i);
			else
				labTipRec[i]= values.get(i).toString();
			posTipRec[i] = values.get(i).toString();
		}
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
		if (operacio.equals("cancel"))
			return errors;
		
		usuariActual=((edu.xtec.merli.segur.User)request.getSession().getAttribute("user")).getUser();
		
		if (operacio.equals("addRec") || operacio.equals("setRec") || operacio.equals("valRec") || operacio.equals("editar") || operacio.equals("editarWS2")){
			if (operacio.equals("addRec") || operacio.equals("editarWS2")){
				if (operacio.equals("addRec"))		setOperacio("newRec");
				if (operacio.equals("editarWS2"))	setOperacio("newRecWS");
				setIdRecurs("");
				errors.add("merli", new ActionError("message.etiquetar.nou"));
			}
			if (operacio.equals("setRec") || operacio.equals("editar")){
				setOperacio("modifyRec");
				errors.add("merli", new ActionError("message.etiquetar.modificar"));
			}
			if (operacio.equals("valRec")){
				setOperacio("validarRec");
				errors.add("merli", new ActionError("message.etiquetar.validar"));
			}
			return errors;
		}else{
			/* Passar a pendent de validar o publicat*/
			if (estatSel[0].equals(String.valueOf(MerliBean.ESTAT_M_REALITZAT)) || estatSel[0].equals(String.valueOf(MerliBean.ESTAT_M_PUBLICAT))){
				/* Validació de l'existència i la longitud dels camps.*/
				//***** TITOL *****//
				if((titol == null))
					errors.add("titol", new ActionError("error.etiq.titol.required"));
				else{
					if (titol.length() == 0) {titol="";errors.add("titol", new ActionError("error.etiq.titol.required"));}
					else if (titol.length() < 2) errors.add("titol", new ActionError("error.etiq.titol.curt"));
					else if (titol.length() > 1000) errors.add("titol", new ActionError("error.etiq.titol.llarg"));
				}
				//***** FISIC ONLINE *****//
				if (recFisic == null) 
					errors.add("recFisic", new ActionError("error.etiq.recfisic.required"));
				//***** URL *****//
				if(recFisic!=null && !recFisic.booleanValue() && (url==null || url.length()<2))
					errors.add("url", new ActionError("error.etiq.url.required"));
				else if (url.length() > 1000)
					errors.add("url", new ActionError("error.etiq.url.llarg"));
				//***** DESC CURRICULAR *****//
				//si l'ambit es alumnat (3), necessitem almenys un descriptor curricular
				boolean ambitAlumnat=false;
				for(int i=0;i<rolUser.length;i++)
					if(rolUser[i].equals("3")) ambitAlumnat=true;
				if (ambitAlumnat && (curriculum ==null || curriculum.indexOf(";")<0)) 
					errors.add("curriculum", new ActionError("error.etiq.curriculum.required"));
				//***** DESC TEMATICA *****//
				String[] aux;
				int numParaules=0;
				if(selecParaulaId!=null && selecParaulaId.length()>0)
				{
					aux = selecParaulaId.split(";");
					numParaules+=aux.length;
				}
				if(selecTerm!=null && selecTerm.length()>0)
				{
					aux = selecTerm.split(";");
					numParaules+=aux.length;
				}
				if(numParaules<4)
					errors.add("thesaurus", new ActionError("error.etiq.thesaurus.required"));
				//***** DESCRIPCIO/RESUM *****//	
				if((descripcio == null) || (descripcio.length() < 1)){
					errors.add("descripcio", new ActionError("error.etiq.descripcio.required"));
					descripcio="";descripcioEn="";descripcioEs="";
				}
				else if(descripcio.length() < 100)
					errors.add("descripcio", new ActionError("error.etiq.descripcio.curt"));
				else if (descripcio.length() > 2000)
					errors.add("descripcio", new ActionError("error.etiq.descripcio.llarg"));
				//***** CONTEXTUALITZACIO *****//
				if (context2.length() > 1000)
					errors.add("context2", new ActionError("error.etiq.context2.llarg"));
				//***** RELACIO *****//
				if(recRel!=null) comprovarIdRecurs(recRel, errors);
				if(tipusRelSel==null) {tipusRelSel = new String[1];tipusRelSel[0]="";}
				else comprovaTipusRelSel();
				if (descRel!=null)
					for(int i=0;i<descRel.length;i++)
						if(descRel[i].length() > 1000) errors.add("descRel", new ActionError("error.etiq.descRel.llarg"));				
				//***** IDIOMA *****//	
				if (llengues ==null || llengues.length==0) 
					errors.add("idioma", new ActionError("error.etiq.idioma.required"));
				//***** FORMAT *****//	
				if (format ==null || format.length==0) 
					errors.add("formats", new ActionError("error.etiq.format.required"));
				//***** CARACTERISTIQUES *****//	
				if (caractRFisic.length() > 1000)
					errors.add("caractRFisic", new ActionError("error.etiq.caractRFisic.llarg"));
				//***** AUTOR *****//
				if((autor == null) || (autor.length() < 1))
					{errors.add("autor", new ActionError("error.etiq.autor.required"));autor="";}
				else if (autor.length() > 1000) 	
					errors.add("autor", new ActionError("error.etiq.autor.llarg"));
				//***** EDITOR *****//
				if((editor == null) || (editor.length() < 1))
					{errors.add("editor", new ActionError("error.etiq.editor.required"));editor="";}
				else if (editor.length() > 1000) 	
					errors.add("editor", new ActionError("error.etiq.editor.llarg"));
				//***** DESCRIPCIO DRETS *****//
				if((descDrets != null) && descDrets.length() > 1000)
						errors.add("descDrets", new ActionError("error.etiq.drets.descripcio.llarg"));
				//***** ESTAT *****//
				if(estatSel==null || estatSel.length<1 || estatSel[0] == null) {estatSel = new String[1];estatSel[0]="";}
				//***** RESPONSABLE/CATALOGADOR *****//
				if((responsable == null) || (responsable.length() < 1))
					errors.add("responsable", new ActionError("error.etiq.responsable.required"));
				else if (responsable.length() > 100)
					errors.add("responsable", new ActionError("error.etiq.responsable.llarg"));
				else 
					comprovarUsuari("responsable",responsable,errors);

				edu.xtec.merli.segur.User u = ((edu.xtec.merli.segur.User) request.getSession().getAttribute("user"));
				if (u.isSuperuser() || 
						(u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.PUBLICAR) )){ 

					//Afageix l'usuari actual com a validador del recurs.
					if (Integer.parseInt(getEstatSel()[0]) >= (edu.xtec.merli.MerliBean.ESTAT_M_PUBLICAT)){
						if((validador == null) || (validador.length() < 1))
							validador = u.getUser();
						if (validador.length() > 100)
							errors.add("validador", new ActionError("error.etiq.validador.llarg"));
						else 
							comprovarUsuari("validador",validador,errors);
					}
				}else{
					validador = null;
					corrector = null;
				}
				
				try{
					if (edatMax.equals("") || Integer.parseInt(edatMax)<0) edatMax = "";
					if (edatMin.equals("") || Integer.parseInt(edatMin)<0) edatMin = "";
				}catch(Exception e){
					errors.add("edat", new ActionError("error.etiq.edat.format"));
				}
			}else{
				/**
				 * En cas de voler guardar un recurs sense finalitzar cal tenir en 
				 * compte tots els valors que necessiten un valor concret.
				 */
				MessageResources messages = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);
	
				if (titol == null || titol.length() < 2) 
					titol = messages.getMessage(request.getLocale(),"etiq.title.null");
				else if (titol.length() > 1000)
					errors.add("titol", new ActionError("error.etiq.titol.llarg"));
				
				if((descripcio == null) || (descripcio.length() < 1))
					descripcio =" ";
				else if (descripcio.length() > 2000)
						errors.add("descripcio", new ActionError("error.etiq.descripcio.llarg"));
				
				if (context2.length() > 1000)
						errors.add("context2", new ActionError("error.etiq.context2.llarg"));
				
				if (caractRFisic.length() > 1000)
					errors.add("caractRFisic", new ActionError("error.etiq.caractRFisic.llarg"));
				
				if((descDrets == null) || (descDrets.length() < 1))
					descDrets ="";
				else if (descDrets.length() > 1000)
						errors.add("descDrets", new ActionError("error.etiq.drets.descripcio.llarg"));
				
				if((url == null) || (url.length() < 1))
					url = " ";
				else if (url.length() > 1000)
						errors.add("url", new ActionError("error.etiq.url.llarg"));
				
				if(getEstatSel()[0].equals(String.valueOf(MerliBean.ESTAT_M_PENDENT)))	responsable=" ";
				else comprovarUsuari("responsable",responsable,errors);

				//Validació del recurs relacionat i llargada descripcions
				if(recRel!=null)	comprovarIdRecurs(recRel, errors);
				if(tipusRelSel==null) {tipusRelSel = new String[1];tipusRelSel[0]="";}
				else comprovaTipusRelSel();
				if (descRel!=null)
					for(int i=0;i<descRel.length;i++)
						if(descRel[i].length() > 1000) errors.add("descRel", new ActionError("error.etiq.descRel.llarg"));				
				
				if((autor == null) || (autor.length() < 1))
					autor =" ";
				else if (autor.length() > 1000)
						errors.add("autor", new ActionError("error.etiq.autor.llarg"));
				if (editor.length() > 1000)
					errors.add("editor", new ActionError("error.etiq.editor.llarg"));
				edu.xtec.merli.segur.User u = ((edu.xtec.merli.segur.User) request.getSession().getAttribute("user"));
				if (u.isSuperuser() || 
						(u.hasPermission(edu.xtec.merli.segur.operations.MerliOperations.PUBLICAR) )){ 

					if (Integer.parseInt(getEstatSel()[0]) == edu.xtec.merli.MerliBean.ESTAT_M_REALITZAT || Integer.parseInt(getEstatSel()[0]) == edu.xtec.merli.MerliBean.ESTAT_M_PUBLICAT){
						if((validador == null) || (validador.length() < 1)){
							errors.add("validador", new ActionError("error.etiq.validador.required"));
						}else{
							if (validador.length() > 1000)
								errors.add("validador", new ActionError("error.etiq.validador.llarg"));
							else comprovarUsuari("validador",validador,errors);
						}
					}
				}else{
					validador = null;
					corrector = null;
				}
			}
			
			// pretractament dels checkbox i els selects
			String[] buit={};
			if (request.getParameter("rolUser") == null)	rolUser = buit;
			if (request.getParameter("ambit") == null)		ambit = buit;
			if (request.getParameter("llengues") == null)	llengues = buit;
			if (request.getParameter("format") == null)		format = buit;
			if (request.getParameter("unitats") == null)	unitats = buit;
			if (request.getParameter("context") == null)	context = buit;
			if (request.getParameter("tipRec") == null)		tipRec = buit;
			
			return errors;
		}
	}

	private void comprovarUsuari(String tipus,String user, ActionErrors errors) {
		
		MerliBean mb = new MerliBean();
		if (!mb.getUser(user))
			errors.add(tipus, new ActionError("error.etiq."+tipus+".inexistent"));
		
	}

	/**
	 * comprova que els ids de recurs continguts a 'idrec' corresponen a recursos de la BD
	 * @param idrec
	 * @param errors
	 */
	private void comprovarIdRecurs(String[] idrec, ActionErrors errors) {
		MerliBean mb = new MerliBean();
		for(int i=0;i<idrec.length;i++)
		{
			if (idrec[i].equals(""))  tipusRelSel[i]="";
			if (!idrec[i].equals("") && !mb.existsRecurs(idrec[i]))
				errors.add("recRel", new ActionError("error.etiq.recurs.invalid"));
		}
	}
	
	/**
	 * elimina una relacio que si no te un tipus seleccionat
	 *
	 */
	private void comprovaTipusRelSel()
	{
		for(int i=0;i<tipusRelSel.length;i++)
			if (tipusRelSel[i]==null || tipusRelSel[i].equals(""))
			{
				String [] auxTipus = new String[tipusRelSel.length-1];
				String [] auxIdRec = new String[tipusRelSel.length-1];
				String [] auxDesc = new String[tipusRelSel.length-1];
				for(int j=0;j<auxTipus.length;j++)
				{
					if(j<i)
					{
						auxTipus[j]=tipusRelSel[j];
						auxIdRec[j]=recRel[j];
						auxDesc[j]=descRel[j];
					}
					else
					{
						auxTipus[j]=tipusRelSel[j+1];
						auxIdRec[j]=recRel[j+1];
						auxDesc[j]=descRel[j+1];
					}
				}
				tipusRelSel=auxTipus;
				recRel=auxIdRec;
				descRel=auxDesc;
				i--;
			}
	}

	public String[] toStringList(ArrayList list){
		String[] string;
		string = new String[list.size()];
		for (int i =0;i<list.size();i++){
			string[i] = (String) list.get(i);
		}

		return string;
	}
	public String[] toStringList(String list){
		String[] string;
		string = new String[1];

		string[0] = list;

		return string;
	}
	public String toString(ArrayList al){
		StringBuffer aux = new StringBuffer();
		
		for (int i =0;i<al.size();i++){
			aux.append((String) al.get(i)).append(";");
		}
		
		return aux.toString();
	}
	
	public RecursMerli getRecursMerli(){
		RecursMerli rm = new RecursMerli(Integer.parseInt(this.getIdRecurs()));
		
		//rm.setAmbit(this.getAmbit(0));		
		rm.setAmbit(Utility.toList(this.getAmbit()));
		rm.setContext(Utility.toList(this.context));
	
		MerliContribution mc = new MerliContribution(1, this.getAutor(), this.getData());
		rm.addContribution(mc);

				
		/**
		 * Si l'estat és 0 o 1 o -1 aleshores crea una nova contribució d'etiquetador
		 * Sino crea una contribució de Validador.
		 */
		if (Integer.parseInt(estatSel[0]) == MerliBean.ESTAT_M_PUBLICAT){ 
			mc = new MerliContribution(MerliContribution.ETIQUETADOR, this.getResponsable(), this.getDataResponsable());
			rm.addContribution(mc);
		}
		else
		{
			mc = new MerliContribution(MerliContribution.ETIQUETADOR, this.getResponsable(), "");
			rm.addContribution(mc);
		}
		
		if (Integer.parseInt(estatSel[0]) == MerliBean.ESTAT_M_PUBLICAT){ 
			if (this.getValidador() != null && !this.getValidador().equals("")){
				mc = new MerliContribution(MerliContribution.VALIDADOR, this.getValidador(), "");
				rm.addContribution(mc);
			}
			else{
				mc = new MerliContribution(MerliContribution.VALIDADOR, this.getResponsable(), "");
				rm.addContribution(mc);
			}
		}
//		if (this.getCorrector() != null && !this.getCorrector().equals("")){
//			mc = new MerliContribution(MerliContribution.CORRECTOR, this.getCorrector(), this.getData());
//			rm.addContribution(mc);
//		}
		if (this.getEditor() != null && !this.getEditor().equals("")){
			mc = new MerliContribution(MerliContribution.EDITOR, this.getEditor(), this.getData());
			rm.addContribution(mc);
		}
		
		rm.setDescription(this.getDescripcio());
		if(this.getDificultat()!=null && this.getDificultat().length>0)
			rm.setDifficulty(this.getDificultat(0));
		rm.setEndUserRol(Utility.toList(this.rolUser));//this.getRolUser(0));
		
		rm.setEstat(this.getEstatSel()[0]);
		
		//si no s'especifica si es recurs fisic o online, no es guarda el format
		if(this.getRecFisic()!=null && this.getRecFisic().booleanValue())
		{
			rm.setFormat(new ArrayList());
			rm.setFormatFisic(Utility.toList(this.format));
		}
		else if(this.getRecFisic()!=null)
		{
			rm.setFormatFisic(new ArrayList());
			rm.setFormat(Utility.toList(this.format));
		}
		
		if (this.getDescDrets().length() > 1 && this.getAutor().length() >0 )
			rm.setHasRights("yes");
		else
			rm.setHasRights("no");
		rm.setLanguage(Utility.toList(this.llengues));
		rm.setLearningTime("PT"+this.getDuraHora()+"H"+this.getDuraMin()+"M"+this.getDuraSeg()+"S");
		rm.setCost(this.getCost());
		rm.setLicense(this.getLlicencia(0));
		rm.setMaxAge(this.getEdatMax());
		rm.setMinAge(this.getEdatMin());
		rm.setResourceType(Utility.toList(this.getTipRec()));
		if (rm.getEstat().equals(String.valueOf(MerliBean.ESTAT_M_PUBLICAT)))
		   rm.setResponsable(this.getValidador());
		else
			rm.setResponsable(this.getResponsable());
		rm.setRightsDesc(this.getDescDrets());
		rm.setTaxon(toList(this.getSelecTerm()));
		rm.setTaxonTerm(toList(this.getSelecLabel()));
		rm.setParaulesId(toList(this.getSelecParaulaId()));

		rm.setTitle(this.getTitol());
		rm.setUrl(this.getUrl());
		rm.setVersion(this.getVersio());
		rm.setCurriculum(toList(curriculum));
		
		rm.setAgregaSend(this.getAgrega());
		
		rm.setEstatCa(this.isEstatCa());
		rm.setEstatEn(this.isEstatEn());
		rm.setEstatEs(this.isEstatEs()); 
		rm.setEstatOc(this.isEstatOc());
		
		rm.setContext2(this.getContext2());
		rm.setRecursRelacionat(this.getRecRelI());
		rm.setTipusRelacio(this.getTipusRelSel());
		rm.setDescripcioRelacio(this.getDescRel());
		
		if(this.getRecFisic()!=null)
			rm.setEsFisic(this.getRecFisic().booleanValue());
		rm.setTipusIdFisic(this.getTipusIdFisicSel());
		
		rm.setIdFisic(this.getIdFisic());
		rm.setCaractRFisic(this.getCaractRFisic());
		
		rm.setUnitats(Utility.toList(this.unitats));
		
		return rm;
	}

	private void loadRecurs(String idRecurs2) {
		RecursBD rb = new RecursBD();
		RecursMerli rm ;
		try {
			rm = rb.getRecurs(Integer.parseInt(idRecurs));

			//operacio = "modifyRec";
			this.setAmbit(toStringList(rm.getAmbit()));
			this.setContext(toStringList(rm.getContext()));
			this.setDescDrets(rm.getRightsDesc());
			this.setDescripcio(rm.getDescription());
			if (this.getDescripcio().equals(" "))
				this.setDescripcio("");
			this.setDificultat(toStringList(rm.getDifficulty()));
			if (rm.getLearningTime() != null && rm.getLearningTime().length() > 4){
				this.setDuraHora(rm.getLearningTime().substring(2,rm.getLearningTime().indexOf('H')));
				this.setDuraMin(rm.getLearningTime().substring(rm.getLearningTime().indexOf('H')+1,rm.getLearningTime().indexOf('M')));
				if (rm.getLearningTime().indexOf('S') > 1)
					this.setDuraSeg(rm.getLearningTime().substring(rm.getLearningTime().indexOf('M')+1,rm.getLearningTime().indexOf('S')));
			}
			this.setEdatMax(rm.getMaxAge());
			this.setEdatMin(rm.getMinAge());
			String[] est=new String[1];
			est[0]=rm.getEstat();
			this.setEstatSel(est);
			
			if(rm.getEsFisic())	//modifico els elements de la combo i selecciono els que toca
			{
				try {	
					RecursBD rbd = new RecursBD();
					ArrayList lAux = rbd.getFormatFisicLimitat(RecursBD.IDENTIFIERS);
					posFormat = createValueList(lAux);
					labFormat = createLabelList(lAux, rbd.getFormatFisicLimitat(RecursBD.LABELS));			
				} catch (MerliDBException e) {
					logger.error("Error initalizing resource form. ->"+e);
					e.printStackTrace();
				}
				
				if (rm.getFormatFisic() != null && !rm.getFormatFisic().isEmpty())
					this.setFormat(toStringList(rm.getFormatFisic()));
			}
			else
			{
				if (rm.getFormat() != null && !rm.getFormat().isEmpty())
					this.setFormat(toStringList(rm.getFormat()));
			}
			
			
			this.setLlengues(toStringList(rm.getLanguage()));
			this.setCost(rm.getCost());
			this.setLlicencia(toStringList(rm.getLicense()));
			this.setRolUser(toStringList(rm.getEndUserRol()));
			if (rm.getTaxon() != null)
				this.setSelecTerm(rm.getTaxon().toString());
			this.setTipRec(toStringList(rm.getResourceType()));
			this.setTitol(rm.getTitle());
			this.setUrl(rm.getUrl());
			this.setVersio(rm.getVersion());
			
			this.setDescDrets(rm.getRightsDesc());
			//this.setCurriculum(toString(rm.getCurriculum()));
			//Carregarem el valor del curriculum a mida que en comprovem cada node.
			this.setSelecTerm(toString(rm.getTaxon()));
			this.setSelecLabel(toString(rm.getTaxonTerm()));
			
			this.setSelecParaula(toString(rm.getParaules()));
			this.setSelecParaulaId(toString(rm.getParaulesId()));
			/*Contribucions*/
			ArrayList l = rm.getContribution();
			MerliContribution mc;
			SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");
			for (int i = 0; i < l.size();i++){
				mc = (MerliContribution) l.get(i);
				switch (mc.getIdRol()){
					case 3: 
							if(mc.getEntity().equals(" "))
								this.setResponsable(usuariActual);
							else
							{
								this.setResponsable(mc.getEntity());
								if (mc.getDate()!=null)	this.setDataResponsable(sDate.format(mc.getDate()));
							}
					break;
					case 2: this.setValidador(mc.getEntity());
							if (mc.getDate()!=null)	this.setDataValidador(sDate.format(mc.getDate()));
					break;
					case 4: this.setCorrector(mc.getEntity());
					break;
					case 1: this.setAutor(mc.getEntity());
							if (mc.getDate()!=null){
	  							this.setData(sDate.format(mc.getDate()));
	  							if (this.getAutor().equals(" "))
	  								this.setAutor("");
							}
							break;
					case 8: this.setEditor(mc.getEntity());
					break;
					default:break;
				}
			}
			
			
			this.setUnitats(toStringList(rm.getIdsUnitats()));
			
			Hashtable ht = new Hashtable();
			ArrayList alist = new ArrayList();
			ArrayList curri = rm.getCurriculum();
			Relation rel;

			int idCont;
			SemanticInterface sn = new SemanticInterface();
			String curriContents = "";
			
			for (int i = 0; i < curri.size();i++)
			{
				//Es controla un IndexOutOfBounds per si el getRelations retorna una llista buida
				//és a dir que no té cap relació d'aquell típus.
				//Es controla un NullPointer per si el l'element no existeix.
				String test = ((String) curri.get(i));
				if (test.indexOf("area")>=0){
					idCont = Integer.parseInt(((String) curri.get(i)).substring(4));
					try{
						rel = (Relation) sn.getRelations(idCont,"area","RAL",RelationType.SOURCE).get(0);
						ht = new Hashtable();
						try{		
							ht.put("level",sn.getNode(rel.getIdDest(),"level").getTerm());
							ht.put("idLevel",new Integer(rel.getIdDest()));
						}catch (Exception e){
							ht.put("level","");
							ht.put("idLevel",new Integer(0));
						}
					}catch (Exception e){
						logger.error("Error carregant relacions DUC-RCL del recurs:"+idCont);
						ht.put("level","");
						ht.put("idLevel",new Integer(0));
					}
					try{
						ht.put("area",sn.getNode(idCont,"area").getTerm());
						ht.put("idArea",new Integer(idCont));
					}catch (Exception e){
						ht.put("area","area"+idCont);
					}
					alist.add(ht);
					curriContents += "area"+idCont;
					if (i<curri.size())//no hauria de ser -1??
						curriContents +=";";
				}else{
					idCont = Integer.parseInt(((String) curri.get(i)).substring(7));
					try{
						// rel = (Relation)
						// sn.getRelations(idCont,"content","RCL",RelationType.SOURCE).get(0);
						Node nLevel = sn.getLevel(idCont);
						Node nCicle = sn.getCicle(idCont);
						ht = new Hashtable();
						try {
							if (nCicle != null) {
								ht.put("level", nLevel.getTerm() + " - " + nCicle.getTerm());
								ht.put("idLevel", new Integer(nCicle.getIdNode()));
							} else {
								ht.put("level", nLevel.getTerm());
								ht.put("idLevel", new Integer(nLevel.getIdNode()));
							}
						}catch (Exception e){
							ht.put("level","");
							ht.put("idLevel",new Integer(0));
						}
					}catch (Exception e){
						logger.error("Error carregant relacions DUC-RCL del recurs:"+idCont);
						ht.put("level","");
						ht.put("idLevel",new Integer(0));
					}
					try{
						rel = (Relation) sn.getRelations(idCont,"content","RCA",RelationType.SOURCE).get(0);
						try{
							ht.put("area",sn.getNode(rel.getIdDest(),"area").getTerm());
							ht.put("idArea",new Integer(rel.getIdDest()));
						}catch (Exception e){
							ht.put("area","");
							ht.put("idArea",new Integer(0));
						}
					}catch (Exception e){
						logger.error("Error carregant relacions DUC-RCA del recurs:"+idCont);
						ht.put("area","");
						ht.put("idArea",new Integer(0));
					}
					//En cas de no existir el contingut no es llistarà.
					try{
						ht.put("content",sn.getNode(idCont,"content").getTerm());
						ht.put("idContent",new Integer(idCont));
						alist.add(ht);
						curriContents += "content"+idCont;
						if (i<curri.size())//no hauria de ser -1??
							curriContents +=";";
					}catch (Exception e){
						ht.put("content","content"+idCont);
					}
				}
			}
			
			this.setContentCurriculum(alist.iterator());
			//Aqui sota dues opcions. La que carrega el curriContents
			//elimina les relacions amb nodes del curriculum inexistens.
			//L'altre no els mostra x pantalla però els manté seleccionats.
			//Mantenir-los seleccionats no té sentit, a no ser que l'error pugui ser degut
			//a un error en la connexió d'aquell moment. Si és degut a que ha estat
			//eliminat no val la pena.
			this.setCurriculum(curriContents);
			//this.setCurriculum(toString(rm.getCurriculum()));
			this.setEstatCa(rm.isEstatCa());
			this.setEstatEs(rm.isEstatEs());
			this.setEstatEn(rm.isEstatEn());
			this.setEstatOc(rm.isEstatOc());
			
			this.setAgrega(rm.getAgregaSend());
			if (rm.getAgregaDate()!=null)
				this.setAgregaDate(rm.getAgregaDate("dd-MM-yyyy"));
		
			this.setContext2(rm.getContext2());
			
			if(rm.getTipusRelacio()!=null && rm.getTipusRelacio().size()>0)
			{
				this.setTipusRelSel(rm.getTipusRelacio());
				this.setRecRel(rm.getRecursRelacionat());
				this.setDescRel(rm.getDescripcioRelacio());
		
			}
			
			this.nu = rm.getNu();
			
			if(rm.getUnitatCreadora()!=null && !rm.getUnitatCreadora().equals("")){
				this.setCaractRFisic(rm.getCaractRFisic());
				this.setRecFisic(new Boolean(true));
				if(rm.getTipusIdFisic()!=null && rm.getTipusIdFisic().size()>0)
				{
					this.setIdFisic(rm.getIdFisic());
					this.setTipusIdFisicSel(rm.getTipusIdFisic());
				}
			}
			else this.setRecFisic(new Boolean(false));
			
		} catch (MerliDBException e) {
			ActionErrors errors = new ActionErrors();
			logger.warn("Error loading data from DDBB.->"+e);
			e.printStackTrace();
			errors.add("recurs", new ActionError("error.carrega.recurs"));
		}
	}

	private void setTipusIdFisicSel(ArrayList tipusIdFisic2) {
		tipusIdFisicSel=new String[tipusIdFisic2.size()];
		if(tipusIdFisic2!=null)
			for(int i=0;i<tipusIdFisic2.size();i++)
				tipusIdFisicSel[i]=(String)tipusIdFisic2.get(i);
	}

	private void setIdFisic(ArrayList idFisic2) {
		idFisic=new String[idFisic2.size()];
		if(idFisic2!=null)
			for(int i=0;i<idFisic2.size();i++)
				idFisic[i]=(String)idFisic2.get(i);
	}

	private void setDescRel(ArrayList descripcioRelacio) {
		descRel=new String[descripcioRelacio.size()];
		if(descripcioRelacio!=null)
			for(int i=0;i<descripcioRelacio.size();i++)
				if((String)descripcioRelacio.get(i)!=null) descRel[i]=(String)descripcioRelacio.get(i);
				else descRel[i]="";
	}

	private void setRecRel(ArrayList recursRelacionat) {
		recRel=new String[recursRelacionat.size()];
		if(recursRelacionat!=null)
			for(int i=0;i<recursRelacionat.size();i++){
				if (recursRelacionat.get(i)!=null)
					recRel[i]=((Integer)recursRelacionat.get(i)).toString();
			}
	}

	/**
	 * carrega, des d'una URL, els valors del formulari que no es poden carregar automaticament (pq requereixen tractament previ)
	 * @param params
	 */
	private void loadRecursFromUrl(Map params) {
		String [] labels = (String []) params.get("curriTemp");
		String [] ids = (String []) params.get("curriTempId");
		ArrayList totes = new ArrayList();
		
		if(labels!=null)
		{
			for(int i=0;i<labels.length;i++)
			{
				String [] sLabels = ((String)labels[i]).split(";");
				String [] sIds = ((String)ids[i]).split(";");
				for(int j=0;j<sLabels.length;j=j+3)
				{
					Hashtable ht = new Hashtable();
					ht.put("level",sLabels[j]);
					ht.put("idLevel",sIds[j]);
					ht.put("area",sLabels[j+1]);
					ht.put("idArea",sIds[j+1]);
					ht.put("content",sLabels[j+2]);
					ht.put("idContent",sIds[j+2]);
					totes.add(ht);
				}		
			}
			contentCurriculum=totes.iterator();
		}		
		if(idFisic!=null || idFisic.length>0) recFisic=new Boolean(true);
		
		logger.info("Resource loaded from an URL");
	}
	
	/*Set's & Get's*/
	public void setTitol(String string){
		titol = string;
	}
	public void setDescripcio(String string){
		descripcio = string;
	}
	public void setUrl(String string){
		url = string;
	}
	public String getTitol(){
		return titol;
	}
	public String getDescripcio(){
//		if (descripcio!=null && descripcio.length()>2000){
//			descripcio=descripcio.substring(0, descripcio.lastIndexOf(" ", 1995))+"...";
//		}
		return descripcio;
	}
	public String getUrl(){
		return url;
	}
	public void setVersio (String string){
		versio = string;
	}
	public String getVersio(){
		return versio;
	}
	public void setData (String string){
		data = string;
	}
	public String getData(){
		return data;
	}
	public void setResponsable (String string){
		responsable = string;
	}
	public String getResponsable(){
		return responsable;
	}
	public String[] getDificultat() {
		return dificultat;
	}	
	public String getDificultat(int pos) {
		return dificultat[pos];
	}
	public void setDificultat(String[] dificultat) {
		this.dificultat = dificultat;
	}
	public String[] getLabDificultat() {
		return labDificultat;
	}
	public void setLabDificultat(String[] labDificultat) {
		this.labDificultat = labDificultat;
	}
	public String[] getPosDificultat() {
		return posDificultat;
	}
	public void setPosDificultat(String[] posDificultat) {
		this.posDificultat = posDificultat;
	}
	public void setEdatMin (String string){
		edatMin = string;
	}
	public String getEdatMin(){
		return edatMin;
	}
	public void setEdatMax (String string){
		edatMax = string;
	}
	public String getEdatMax(){
		return edatMax;
	}
	public void setContext (String[] string){
		context = string;
	}
	public String[] getContext(){
		return context;
	}
	public String[] getLabRolUser() {
		return labRolUser;
	}
	public void setLabRolUser(String[] labRolUser) {
		this.labRolUser = labRolUser;
	}
	public String[] getPosRolUser() {
		return posRolUser;
	}
	public void setPosRolUser(String[] posRolUser) {
		this.posRolUser = posRolUser;
	}
	public String[] getRolUser() {
		return rolUser;
	}
	public String getRolUser(int pos) {
		return rolUser[pos];
	}
	public void setRolUser(String[] rolUser) {
		this.rolUser = rolUser;
	}
	public String[] getLabNivellAd() {
		return labNivellAd;
	}
	public void setLabNivellAd(String[] labNivellAd) {
		this.labNivellAd = labNivellAd;
	}
	public String[] getLabNivellSc() {
		return labNivellSc;
	}
	public void setLabNivellSc(String[] labNivellSc) {
		this.labNivellSc = labNivellSc;
	}
	public String[] getLabNivellSp() {
		return labNivellSp;
	}
	public void setLabNivellSp(String[] labNivellSp) {
		this.labNivellSp = labNivellSp;
	}
	public String[] getPosNivellAd() {
		return posNivellAd;
	}
	public void setPosNivellAd(String[] posNivellAd) {
		this.posNivellAd = posNivellAd;
	}
	public String[] getPosNivellSc() {
		return posNivellSc;
	}
	public void setPosNivellSc(String[] posNivellSc) {
		this.posNivellSc = posNivellSc;
	}
	public String[] getPosNivellSp() {
		return posNivellSp;
	}
	public void setPosNivellSp(String[] posNivellSp) {
		this.posNivellSp = posNivellSp;
	}
	public String[] getPosTipRec() {
		return posTipRec;
	}	
	public void setPosTipRec(String[] rec) {
		posTipRec = rec;
	}	
	public String[] getTipRec() {
		return tipRec;
	}	
	public void setTipRec(String[] rec) {
		tipRec = rec;
	}
	public String[] getLabTipRec() {
		return labTipRec;
	}	
	public void setLabTipRec(String[] rec) {
		labTipRec = rec;
	}
	public String[] getFormat() {
		return format;
	}
	public void setFormat(String[] format) {
		this.format = format;
	}
	public String[] getLabFormat() {
		return labFormat;
	}
	public void setLabFormat(String[] labFormat) {
		this.labFormat = labFormat;
	}
	public String[] getLabLlengues() {
		return labLlengues;
	}
	public void setLabLlengues(String[] labLlengues) {
		this.labLlengues = labLlengues;
	}
	public String[] getLlengues() {
		return llengues;
	}
	public void setLlengues(String[] llengues) {
		this.llengues = llengues;
	}
	public String[] getPosFormat() {
		return posFormat;
	}
	public void setPosFormat(String[] posFormat) {
		this.posFormat = posFormat;
	}
	public String[] getPosLlengues() {
		return posLlengues;
	}
	public void setPosLlengues(String[] posLlengues) {
		this.posLlengues = posLlengues;
	}
	public void setDescDrets (String string){
		descDrets = string;
	}
	public String getDescDrets(){
		return descDrets;
	}
	public String[] getAmbit() {
		return ambit;
	}
	public String getAmbit(int pos) {
		return ambit[pos];
	}
	public void setAmbit(String[] ambit) {
		this.ambit = ambit;
	}
	public String[] getLabAmbit() {
		return labAmbit;
	}
	public void setLabAmbit(String[] labAmbit) {
		this.labAmbit = labAmbit;
	}
	public String[] getLabLlicencia() {
		return labLlicencia;
	}
	public void setLabLlicencia(String[] labLlicencia) {
		this.labLlicencia = labLlicencia;
	}
	public String[] getLlicencia() {
		return llicencia;
	}
	public String getLlicencia(int pos) {
		return llicencia[pos];
	}
	public void setLlicencia(String[] llicencia) {
		this.llicencia = llicencia;
	}	
	public String[] getPosAmbit() {
		return posAmbit;
	}
	public void setPosAmbit(String[] posAmbit) {
		this.posAmbit = posAmbit;
	}
	public String[] getPosLlicencia() {
		return posLlicencia;
	}
	public void setPosLlicencia(String[] posLlicencia) {
		this.posLlicencia = posLlicencia;
	}
	public void setIdRecurs (String string){
		idRecurs = string;		
	}
	public String getIdRecurs(){
		if (idRecurs == null || idRecurs == "")
			return "0";
		return idRecurs;
	}
	public void setOperacio (String string){
		operacio = string;
	}
	public String getOperacio(){
		return operacio;
	}
	public String getSelecLabel() {
		return selecLabel;
	}
	public void setSelecLabel(String selecLabel) {
		this.selecLabel = selecLabel;
	}
	public String getSelecTerm() {
		return selecTerm;
	}
	public void setSelecTerm(String selecTerm) {
		this.selecTerm = selecTerm;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getDuraHora() {
		return duraHora;
	}
	public void setDuraHora(String duraHora) {
		this.duraHora = duraHora;
	}
	public String getDuraMin() {
		return duraMin;
	}
	public void setDuraMin(String duraMin) {
		this.duraMin = duraMin;
	}	
	public String[] getMaxNivellAd() {
		return maxNivellAd;
	}
	public void setMaxNivellAd(String[] maxNivellAd) {
		this.maxNivellAd = maxNivellAd;
	}
	public String[] getMaxNivellSc() {
		return maxNivellSc;
	}
	public void setMaxNivellSc(String[] maxNivellSc) {
		this.maxNivellSc = maxNivellSc;
	}
	public String[] getMaxNivellSp() {
		return maxNivellSp;
	}
	public void setMaxNivellSp(String[] maxNivellSp) {
		this.maxNivellSp = maxNivellSp;
	}
	public String[] getMinNivellAd() {
		return minNivellAd;
	}
	public void setMinNivellAd(String[] minNivellAd) {
		this.minNivellAd = minNivellAd;
	}
	public String[] getMinNivellSc() {
		return minNivellSc;
	}
	public void setMinNivellSc(String[] minNivellSc) {
		this.minNivellSc = minNivellSc;
	}
	public String[] getMinNivellSp() {
		return minNivellSp;
	}
	public void setMinNivellSp(String[] minNivellSp) {
		this.minNivellSp = minNivellSp;
	}
	public String getCurArea() {
		return curArea;
	}
	public void setCurArea(String curArea) {
		this.curArea = curArea;
	}
	public String getCurContent() {
		return curContent;
	}
	public void setCurContent(String curContent) {
		this.curContent = curContent;
	}
	public String getCurLevel() {
		return curLevel;
	}
	public void setCurLevel(String curLevel) {
		this.curLevel = curLevel;
	}
	public String getCurriculum() {
		return curriculum;
	}
	public void setCurriculum(String curriculum) {
		this.curriculum = curriculum;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public Iterator getContentCurriculum() {
		return contentCurriculum;
	}
	public void setContentCurriculum(Iterator iterator) {
		contentCurriculum = iterator;
	}
	public String getDuraSeg() {
		return duraSeg;
	}
	public void setDuraSeg(String duraSeg) {
		this.duraSeg = duraSeg;
	}
	public String getCorrector() {
		return corrector;
	}
	public void setCorrector(String corrector) {
		this.corrector = corrector;
	}
	public String getValidador() {
		return validador;
	}
	public void setValidador(String validador) {
		this.validador = validador;
	}
	public String getLlistat() {
		return llistat;
	}
	public void setLlistat(String llistat) {
		this.llistat = llistat;
	}
	public String getAgrega() {
		return agrega;
	}
	public void setAgrega(String agrega) {
		this.agrega = agrega;
	}
	public String getAgregaDate() {
		return agregaDate;
	}
	public void setAgregaDate(String agregaDate) {
		this.agregaDate = agregaDate;
	}
	public boolean isEstatCa() {
		return estatCa;
	}
	public void setEstatCa(boolean estatCa) {
		this.estatCa = estatCa;
	}
	public boolean isEstatEn() {
		return estatEn;
	}
	public void setEstatEn(boolean estatEn) {
		this.estatEn = estatEn;
	}
	public boolean isEstatEs() {
		return estatEs;
	}
	public void setEstatEs(boolean estatEs) {
		this.estatEs = estatEs;
	}
	public boolean isEstatOc() {
		return estatOc;
	}
	public void setEstatOc(boolean estatOc) {
		this.estatOc = estatOc;
	}
	public String[] getTipusRel() {
		return tipusRel;
	}
	public void setTipusRel(String[] tipusRel) {
		this.tipusRel = tipusRel;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getContext2() {
		return context2;
	}
	public void setContext2(String context2) {
		this.context2 = context2;
	}
	public String[] getDescRel() {
		return descRel;
	}
	public void setDescRel(String[] descRel) {
		this.descRel = descRel;
	}
	public String[] getRecRel() {
		return recRel;
	}
	public Integer[] getRecRelI(){
		Integer[] res = new Integer[recRel.length];
		try {
			for(int i=0;i<recRel.length;i++)
				res[i] = Integer.valueOf(recRel[i]);
		}catch(Exception e)
		{
			res=null;
		}
		return res;
	}
	public void setRecRel(String[] recRel) {
		this.recRel = recRel;
	}
	public Boolean getRecFisic() {
		return recFisic;
	}
	public void setRecFisic(Boolean recFisic) {
		this.recFisic = recFisic;
	}
	public String getCaractRFisic() {
		return caractRFisic;
	}
	public void setCaractRFisic(String caractRFisic) {
		this.caractRFisic = caractRFisic;
	}
	public String[] getIdFisic() {
		return idFisic;
	}
	public void setIdFisic(String[] idFisic) {
		this.idFisic = idFisic;
	}
	public String[] getTipusIdFisic() {
		return tipusIdFisic;
	}
	public void setTipusIdFisic(String[] tipusIdFisic) {
		this.tipusIdFisic = tipusIdFisic;
	}
	public String[] getTipusIdFisicSel() {
		return tipusIdFisicSel;
	}
	public void setTipusIdFisicSel(String[] tipusIdFisicSel) {
		this.tipusIdFisicSel = tipusIdFisicSel;
	}
	public String[] getTipusRelSel() {
		return tipusRelSel;
	}
	public void setTipusRelSel(String[] tipusRelSel) {
		this.tipusRelSel = tipusRelSel;
	}
	private void setTipusRelSel(ArrayList tipusRelacio) {
		tipusRelSel = new String[tipusRelacio.size()];
		for(int i=0;i<tipusRelacio.size();i++)
			tipusRelSel[i]=(String)tipusRelacio.get(i);		
	}
	public String getDescDretsEn() {
		return descDretsEn;
	}
	public void setDescDretsEn(String descDretsEn) {
		this.descDretsEn = descDretsEn;
	}
	public String getDescDretsEs() {
		return descDretsEs;
	}
	public void setDescDretsEs(String descDretsEs) {
		this.descDretsEs = descDretsEs;
	}
	public String getDescripcioEn() {
		return descripcioEn;
	}
	public void setDescripcioEn(String descripcioEn) {
		this.descripcioEn = descripcioEn;
	}
	public String getDescripcioEs() {
		return descripcioEs;
	}
	public void setDescripcioEs(String descripcioEs) {
		this.descripcioEs = descripcioEs;
	}
	public String getTitolEn() {
		return titolEn;
	}
	public void setTitolEn(String titolEn) {
		this.titolEn = titolEn;
	}
	public String getTitolEs() {
		return titolEs;
	}
	public void setTitolEs(String titolEs) {
		this.titolEs = titolEs;
	}
	public String getSelecParaula() {
		return selecParaula;
	}
	public void setSelecParaula(String selecParaula) {
		this.selecParaula = selecParaula;
	}
	public String getSelecParaulaId() {
		return selecParaulaId;
	}
	public void setSelecParaulaId(String selecParaulaId) {
		this.selecParaulaId = selecParaulaId;
	}


	public String getNu() {
		return nu;
	}
	public void setNu(String nu) {
		this.nu = nu;
	}
	public String[] getEstatSel() {
		return estatSel;
	}
	public void setEstatSel(String[] estatSel) {
		this.estatSel = estatSel;
	}
	/**
	 * Retorna l'estat del recurs en un string
	 * @return
	 */
	public String getEstatSelI() {
		return estatSel[0];
	}
	public void setEstatSel(String estatSel) {
		this.estatSel[0] = estatSel;
	}

	public String getDataResponsable() {
		return dataResponsable;
	}

	public void setDataResponsable(String dataResponsable) {
		this.dataResponsable = dataResponsable;
	}

	public String getDataValidador() {
		return dataValidador;
	}

	public void setDataValidador(String dataValidador) {
		this.dataValidador = dataValidador;
	}
	
	/*    Utils     */
	public static ArrayList toList(String list) {
		ArrayList l = new ArrayList();
		String aux;
		aux = list;
		while (aux.indexOf(';')>=0){
			if (!"".equals(aux.substring(0,aux.indexOf(';')).trim())){
				l.add(aux.substring(0,aux.indexOf(';')).trim());
			}
			aux = aux.substring(aux.indexOf(';')+1);
		}
		if (aux.trim().compareTo("") != 0)
			l.add(aux.trim());
		return l;
	}

	public String[] getLabUnitats() {
		return labUnitats;
	}

	public void setLabUnitats(String[] labUnitats) {
		this.labUnitats = labUnitats;
	}

	public String[] getPosUnitats() {
		return posUnitats;
	}

	public void setPosUnitats(String[] posUnitats) {
		this.posUnitats = posUnitats;
	}
	public String[] getUnitats() {
		return unitats;
	}

	public void setUnitats(String[] unitats) {
		this.unitats = unitats;
	}

	public String getUsuariActual() {
		return usuariActual;
	}

	public void setUsuariActual(String usuariActual) {
		this.usuariActual = usuariActual;
	}

	public String getComprovaHidden() {
		return comprovaHidden;
	}

	public void setComprovaHidden(String comprovaHidden) {
		this.comprovaHidden = comprovaHidden;
	}



}
