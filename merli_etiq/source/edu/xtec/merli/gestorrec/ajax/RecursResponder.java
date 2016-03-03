package edu.xtec.merli.gestorrec.ajax;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.MerliContribution;
import edu.xtec.merli.RecursMerli;
import edu.xtec.merli.basedades.RecursBD;
import edu.xtec.merli.segur.User;


public class RecursResponder extends Action
{    
	 private Locale locale;
	 private MessageResources messages;
	
	 public ActionForward execute( 	ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception{

	 	response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("text/html; charset=UTF-8");
		
		int pag;
		ArrayList listPaginacio = new ArrayList();
		int op;
		int id;
		String cerca, id_unitat, id_catalogador;
		String result = "";
		int estat, fisic;	//0=tots, 1=online, 2=fisics
		int propis;			//0=tots, 1=pròpies, 2=unitat, 3=propies i de la unitat
		Date data_i, data_f;
		MerliBean mb = new MerliBean();
		
		
		/*
		 * Guardem els parametres de filtrat
		 */
		setAttribute(request, "estat");
		setAttribute(request, "despl");
		setAttribute(request, "id");
		setAttribute(request, "cerca");
		setAttribute(request, "fisic");
		setAttribute(request, "data_i");
		setAttribute(request, "data_f");
		setAttribute(request, "id_unitat");
		setAttribute(request, "id_catalogador");
		setAttribute(request, "descripcioC");
		setAttribute(request, "ord");
		setAttribute(request, "value");

		/***********  Recupero els valors   ***************/
		try{
			pag = Integer.parseInt((String)((DynaValidatorForm)form).get("value"));
		}catch(NumberFormatException nfe){
			pag = 0;
		}
		try{
			op = Integer.parseInt((String)((DynaValidatorForm)form).get("operation"));
		}catch(NumberFormatException nfe){
			op = 0;
		}
		try{
			if(((String)((DynaValidatorForm)form).get("id")).equals("")) id=0;
			else id = Integer.parseInt((String)((DynaValidatorForm)form).get("id"));
		}catch(NumberFormatException nfe){
			id = -1;
		}
		cerca = (String)((DynaValidatorForm)form).get("cerca");
		messages = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);
		locale = request.getLocale(); 
		if (pag <= 0)	pag = 1;
		String sEstat=(String)((DynaValidatorForm)form).get("estat");
		String sEstatsDisponibles=(String)((DynaValidatorForm)form).get("estatsDisponibles");
		String sData_i=(String)((DynaValidatorForm)form).get("data_i");
		String sData_f=(String)((DynaValidatorForm)form).get("data_f");
		String sFisic=(String)((DynaValidatorForm)form).get("fisic");
		String sId_unitat=(String)((DynaValidatorForm)form).get("id_unitat");
		String sId_catalogador=(String)((DynaValidatorForm)form).get("id_catalogador");
		String sPropis=(String)((DynaValidatorForm)form).get("despl");
		String sDescripcio=(String)((DynaValidatorForm)form).get("descripcioC");
		String sOrdenacio=(String)((DynaValidatorForm)form).get("ord");
		if(sEstat!= null && !sEstat.equals("")) estat= Integer.parseInt(sEstat);
		else estat=-1;
		if(sData_i.length()==10)
			sData_i=sData_i.substring(6,10)+"-"+sData_i.substring(3,5)+"-"+sData_i.substring(0,2);
		if(sData_f.length()==10)
			sData_f=sData_f.substring(6,10)+"-"+sData_f.substring(3,5)+"-"+sData_f.substring(0,2);
		try
		{
			if(sData_i!=null && !sData_i.equals("")) data_i=Date.valueOf(sData_i);
			else data_i=new Date(0);
			if(sData_f!=null && !sData_f.equals("")) data_f= Date.valueOf(sData_f);
			else data_f=new Date(0);
		}
		catch(Exception e)
		{
			data_i=new Date(0); data_f=new Date(0);
		}
		if(sFisic!= null && !sFisic.equals("")) fisic= Integer.parseInt(sFisic);
		else fisic=-1;
		if(sId_unitat!= null && !sId_unitat.equals("")) id_unitat=sId_unitat;
		else id_unitat="";
		if(sId_catalogador!= null && !sId_catalogador.equals("")) id_catalogador=sId_catalogador;
		else id_catalogador="";
		if(sPropis != null && !sPropis.equals("") && !sPropis.equals("undefined")) propis=Integer.parseInt(sPropis);
		else propis=-1;
		

		/***********  Obtinc la llista ***************/
		if(op==MerliBean.OPERACIO_EXPORTACIO)			//Obtinc el llistat i obro el fitxer de l'exportacio
		{
			pag=-1;			//pagina=-1, pq no volem paginacio
			listPaginacio = mb.getLlistatRecursosExportacio(op, ((User)request.getSession().getAttribute("user")),id,cerca,estat,sEstatsDisponibles,data_i,data_f,fisic,id_unitat,id_catalogador,propis,sDescripcio,sOrdenacio);
			response.setContentType("text/plain; charset=ISO-8859-1");
			response.setHeader("Content-Disposition", "attachment; filename=\"export.txt\"");
			exportacio(listPaginacio, response);
			response.flushBuffer();
		}
		else if(op==MerliBean.OPERACIO_COUNT)			//Retorno el nombre de recursos que s'exportaran del llistat
		{
			pag=-1;			//pagina=-1, pq no volem paginacio
			int numRec = mb.getNumRecursosAdmin(((User)request.getSession().getAttribute("user")),id,cerca,estat,sEstatsDisponibles,data_i,data_f,fisic,id_unitat,id_catalogador,propis,sDescripcio);
			response.getWriter().write(Integer.toString(numRec));
		}
		else											//Obtinc el llistat i pagino
		{
			int numRec;
			mb.messages=messages;
			mb.locale=locale;
			
			if(op==MerliBean.OPERACIO_CERCADOR)		//llistat resultat d'una cerca
			{
				listPaginacio = mb.getLlistatRecursosAdmin(op, ((User)request.getSession().getAttribute("user")),pag,id,cerca,estat,sEstatsDisponibles,data_i,data_f,fisic,id_unitat,id_catalogador,propis,sDescripcio,sOrdenacio);
				numRec = mb.getNumRecursosAdmin(((User)request.getSession().getAttribute("user")),id,cerca,estat,sEstatsDisponibles,data_i,data_f,fisic,id_unitat,id_catalogador,propis,sDescripcio);
			}
			else									//llistat inicial
			{
				listPaginacio = mb.getLlistatRecursosInicial(op, ((User)request.getSession().getAttribute("user")),pag,id,cerca);
				numRec = mb.getNumRecursosInicial(op,((User)request.getSession().getAttribute("user")),id,cerca);
			}
				
			result += mb.llistaRec2HTML(listPaginacio, ((User)request.getSession().getAttribute("user")),op, pag);			
			String pagina = mb.paginacioHTML(numRec,pag,op);
			
			response.getWriter().write("<paginacio>"+pagina+"</paginacio><message>"+result+"</message>");
			
			ActionErrors errors = new ActionErrors();
		}
	
		return (mapping.findForward(""));
		
	}

	private void setAttribute(HttpServletRequest request, String attr) {
		if (request.getParameter(attr) != null && !"".equals(request.getParameter(attr))) {
			request.getSession().setAttribute(attr, request.getParameter(attr));
		} else {
			request.getSession().setAttribute(attr, null);
		}
	}
	 
	 /**
	  * envia la informacio sobre els recursos continguts a 'recursos' en el mateix format que utilitza la mediateca, a traves del response
	  * @param recursos
	  * @param response
	  */
	private void exportacio(ArrayList recursos, HttpServletResponse response)
	{
		
		StringBuffer resp = new StringBuffer("");
		RecursMerli rec;
		RecursBD rbd = new RecursBD();	
		Map mLleng = rbd.getLlenguesTotes();
		Map mFormats = rbd.getFormatFisicTots();
		Map mContextos = rbd.getContextTots();	//Nivell educatiu
		Map mRols = rbd.getRolUserTots();	//Ambit

		try {
			for(int i=0;i<recursos.size();i++)
			{
				rec=(RecursMerli) recursos.get(i);
				resp.append(":sinera\n");
				resp.append("-NU\n");
				if(!rec.getNu().equals("")) resp.append(rec.getNu()+"\n");	
				else resp.append(rec.getIdRecurs()+"\n");					
				resp.append("-CR\n");
				for(int j=0;j<rec.getFormatFisic().size();j++)
				{
					ArrayList totsIds=((ArrayList) mFormats.get("id_format_fisic"));
					String format=(String) rec.getFormatFisic().get(j);
					for(int l=0;l<totsIds.size();l++)
						if((((BigDecimal)totsIds.get(l)).toString()).equals(format))
							resp.append(((String)((ArrayList) mFormats.get("v_tipus_cat")).get(l))+"; ");
				}				
				resp.append("\n");
				resp.append("-TI\n");
				resp.append(rec.getTitle()+"\n");
				resp.append("-AU\n");
				MerliContribution autor= rec.getContribution(MerliContribution.AUTOR);
				if(autor!=null && autor.getEntity()!=null)
					resp.append(autor.getEntity()+"\n");
				else 
					resp.append("\n");
				resp.append("-ED\n");
				MerliContribution edicio= rec.getContribution(MerliContribution.EDITOR);
				if(edicio!=null && edicio.getEntity()!=null)
					resp.append(edicio.getEntity()+"\n");
				else 
					resp.append("\n");
				resp.append("-TR\n");
				resp.append("\n");
				resp.append("-DA\n");
				SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");
				if(autor!=null && autor.getDate()!=null)
					resp.append(sDate.format(autor.getDate())+"\n");
				else 
					resp.append("\n");
				resp.append("-LL\n");
				for(int j=0;j<rec.getLanguage().size();j++)
				{
					ArrayList totsIds=((ArrayList) mLleng.get("id_llengua"));
					String llengua=(String) rec.getLanguage().get(j);
					for(int l=0;l<totsIds.size();l++)
						if(((String)totsIds.get(l)).equals(llengua))
							resp.append(((String)((ArrayList) mLleng.get("llengua_cat")).get(l))+"; ");
				}
				//str=str.substring(0,str.length()-1);	//trec l'ultim ';'
				resp.append("\n");
				resp.append("-NS\n");
				for(int j=0;j<rec.getRecursRelacionat(RecursMerli.S_ES_PART_DE).size();j++)
					resp.append(rec.getRecursRelacionat(RecursMerli.S_ES_PART_DE).get(j)+"; ");
				resp.append("\n");
				resp.append("-NI\n");
				for(int j=0;j<rec.getRecursRelacionat(RecursMerli.S_TE_PART).size();j++)
					resp.append(rec.getRecursRelacionat(RecursMerli.S_TE_PART).get(j)+"; ");
				resp.append("\n");
				resp.append("-CA\n");
				if (rec.getLearningTime() != null && rec.getLearningTime().length() > 4)
				{
					String duraHora, duraMin, duraSeg, val ="";
					if(rec.getLearningTime().indexOf('H')>0)
						val = rec.getLearningTime().substring(2,rec.getLearningTime().indexOf('H'));
					duraHora = " "+val+" hores";
					try
					{
						if(Integer.valueOf(val).equals(new Integer(0))) duraHora="";
						if(Integer.valueOf(val).equals(new Integer(1))) duraHora=" 1 hora";
					}
					catch(NumberFormatException e)
					{
						duraHora="";
					}	
					val="";
					if(rec.getLearningTime().indexOf('H')>0 && rec.getLearningTime().indexOf('M')>0)
						val = rec.getLearningTime().substring(rec.getLearningTime().indexOf('H')+1,rec.getLearningTime().indexOf('M'));
					duraMin = " "+val+" minuts";
					try
					{
						if(Integer.valueOf(val).equals(new Integer(0))) duraMin="";
						if(Integer.valueOf(val).equals(new Integer(1))) duraMin=" 1 minut";
					}
					catch(NumberFormatException e)
					{
						duraMin="";
					}	
					val="";
					if(rec.getLearningTime().indexOf('M')>0 && rec.getLearningTime().indexOf('S')>0)
						val = rec.getLearningTime().substring(rec.getLearningTime().indexOf('M')+1,rec.getLearningTime().indexOf('S'));
					duraSeg = " "+val+" segons";
					try
					{
						if(Integer.valueOf(val).equals(new Integer(0))) duraSeg="";
						if(Integer.valueOf(val).equals(new Integer(1))) duraSeg=" 1 segon";
					}
					catch(NumberFormatException e)
					{
						duraSeg="";
					}	
					resp.append("Durada:"+duraHora+duraMin+duraSeg+". ");
				}
				resp.append((rec.getCaractRFisic() != null ? rec.getCaractRFisic() : "") + "\n");
				resp.append("-IA\n");
				for(int j=0;j<rec.getIdFisic().size();j++) resp.append(rec.getTipusIdFisic().get(j)+" "+rec.getIdFisic().get(j)+"; ");
				resp.append("\n");
				resp.append("-NR\n");
				resp.append("\n");
				resp.append("-SI\n");
				resp.append("\n");
				resp.append("-DE\n");
				resp.append("\n");
				resp.append("-DN\n");
				for(int j=0;j<rec.getContext().size();j++)
				{
					ArrayList totsIds=((ArrayList) mContextos.get("id_nivell_cat"));
					String context=(String) rec.getContext().get(j);
					for(int l=0;l<totsIds.size();l++)
						if((((BigDecimal)totsIds.get(l)).toString()).equals(context))
							resp.append(((String)((ArrayList) mContextos.get("nivell_cat")).get(l))+"; ");
				}				
				
				for(int j=0;j<rec.getEndUserRol().size();j++)
				{
					ArrayList totsIds=((ArrayList) mRols.get("id_rol_usuari"));
					String rol=(String) rec.getEndUserRol().get(j);
					for(int l=0;l<totsIds.size();l++)
						if((((BigDecimal)totsIds.get(l)).toString()).equals(rol))
							resp.append(((String)((ArrayList) mRols.get("rol_usuari_cat")).get(l))+"; ");
				}
				resp.append("\n");
				resp.append("-DC\n");
				resp.append("\n");//resp.append(rec.getContext2()+"\n");
				resp.append("-DT\n");
				for(int j=0;j<rec.getTaxonTerm().size();j++) resp.append(rec.getTaxonTerm().get(j)+"; ");
				resp.append("\n");
				resp.append("-ID\n");
				for(int j=0;j<rec.getParaules().size();j++) resp.append(rec.getParaules().get(j)+"; ");
				resp.append("\n");
				resp.append("-RE\n");
				resp.append(rec.getDescription()+"\n");
				resp.append("-DD\n");
				MerliContribution catalogador= rec.getContribution(MerliContribution.ETIQUETADOR);
				if(catalogador!=null && catalogador.getEntity()!=null)
					resp.append(catalogador.getEntity());
				resp.append("\n");
				resp.append("-LINKS\n");
				resp.append(rec.getUrl()+"\n\n");
			}
			String str = resp.toString();
			response.getWriter().write(str);
			response.setContentLength(str.length());
			response.getWriter().flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
}
