package edu.xtec.merli.etiquetador.ajax;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;


import HTTPClient.HTTPClientModule;
import HTTPClient.HTTPClientSSLFactory;
import HTTPClient.HTTPConnection;
import HTTPClient.HttpURLConnection;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.basedades.RecursBD;
import edu.xtec.merli.etiquetador.EtiqBean;
import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.merli.utils.Utility;
import edu.xtec.semanticnet.Node;


public class ExtraResponder extends Action
	{    
	private static final Logger logger = Logger.getRootLogger();
	 public ActionForward execute( 	ActionMapping mapping,
				ActionForm form,
				HttpServletRequest request,
				HttpServletResponse response)
				throws Exception{

		 	MessageResources messages = (MessageResources)request.getAttribute(Globals.MESSAGES_KEY);
			String result = null;
			Map nums;
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("text/html; charset=UTF-8");

			String valor = (String)((DynaValidatorForm)form).get("value");
			String operation = (String)((DynaValidatorForm)form).get("operation");
			String misUrl="";

			int max = 0;
			Node n;
			StringBuffer resp = new StringBuffer("<table>");
			if (operation.equals("autocomplete")){	
				SemanticInterface si = new SemanticInterface();
				ArrayList al = si.searchWord(valor);

				if (al == null || al.isEmpty()){
					result="";
				}else{	
					nums = nombreOcurrencies(al,true,Math.min(al.size(), 15));
					max = Math.min(al.size(), 15);
					for (int i =0;i<max;i++){
						n = (Node) al.get(i);
						resp.append("<tr><td onclick=\"putValueTerm(");
						resp.append(n.getIdNode());
						resp.append(",this");
						//res.append(Utility.xmlEncode(n.getTerm()));
						resp.append(");\">");
						resp.append(Utility.xmlEncode(n.getTerm()));
						resp.append(" (");
						BigDecimal num = (BigDecimal)nums.get(new BigDecimal(new Integer(n.getIdNode()).toString()));
						resp.append(num != null ? num : new BigDecimal("0"));
						resp.append(")&nbsp; <img src=\"web/images/tesaure.png\" alt=\"Paraula existent a tesaurus\" title=\"Paraula existent a tesaurus\"/></td></tr>");	
					}
				}
				if (max < 15){
					al = loadParaulesObertesLike(valor);
					if(al!=null && !al.isEmpty())
					{
						nums = nombreOcurrencies(al,false,Math.min(al.size(), 15-max));
						max = Math.min(al.size(),15-max);
						for (int i =0;i<max;i++){
							n = (Node) al.get(i);
							resp.append("<tr class=\"paraulaoberta\"><td onclick=\"putValueParaula(");
							resp.append(n.getIdNode());
							resp.append(",this");
							//res.append(Utility.xmlEncode(n.getTerm()));
							resp.append(");\">");
							resp.append(Utility.xmlEncode(n.getTerm()));
							resp.append(" (");
							BigDecimal num = (BigDecimal)nums.get(new BigDecimal(new Integer(n.getIdNode()).toString()));
							resp.append(num != null ? num : new BigDecimal("0"));
							resp.append(")</td></tr>");	
						}	
					}
				}

				if (max >0){
					resp.append("</table>");
					if (max<al.size())
						resp.append("....");
					result = resp.toString();
				}
				
				response.getWriter().write(result);
			}
			
			if (operation.equals("validateUrl")){
				if(valor.length()>1)
				{
					String idRecurs = request.getParameter("idRecurs");
					result=validaUrl(valor, idRecurs);
					misUrl = messages.getMessage (request.getLocale(),result);
				}
				response.getWriter().write(misUrl);
			}
			if (operation.equals("comprova")){	
				MerliBean mb = new MerliBean();
				ArrayList listRecursos=new ArrayList();
				mb.messages=messages;
				String titol = (String)((DynaValidatorForm)form).get("cerca");
				String ids = (String)((DynaValidatorForm)form).get("fisic");
				boolean err=(titol.length()<3 && valor.length()<5 && ids.length()<1);
				String idRecurs = request.getParameter("idRecurs");
				if (!err) {
					listRecursos = mb.getLlistatRecursosSemblants(titol, valor, ids, idRecurs);
					if(valor.length()>1)
					{
						result = validaUrl(valor, idRecurs);
						misUrl=messages.getMessage(request.getLocale(), result);
					}
				}
				result = mb.llistaRec2HTMLComprova(listRecursos, err, misUrl);

//				for(int i=0;i<listRecursos.size();i++)
//				{
//					RecursMerli r=(RecursMerli)listRecursos.get(i);
//					System.out.println(i+". "+r.getTitle());
//				}
			//	System.out.println(result);
				response.getWriter().write(result);
			}	
			if (operation.equals("contentTerms")){
				EtiqBean eb = new EtiqBean();
				response.getWriter().write(eb.getTermesCurriculum(valor));
				//"{19,cal actualitzar}{1210,el thesaurus}{1316,del DUC}");
			}		
		
			return (mapping.findForward(""));
			
		}	 
	 
	 
	private Map nombreOcurrencies(ArrayList nodes, boolean b, int max) {
		RecursBD r=new RecursBD();
		return r.nombreOcurrencies(nodes,b, max);
	}


	private String validaUrl(String valor, String idRecurs) {
		EtiqBean eb = new EtiqBean();
		String result = eb.checkUrl(valor, idRecurs);
		if (result.equals("etiq.url.ok"))
		try {
            URL url = new URL(valor);

            HTTPConnection.setDefaultConnectionTimeout(10000);
            
            HttpURLConnection httpURLConnection= new HttpURLConnection(url);
            httpURLConnection.connect(); 

          } catch (IOException e) {
				result = "etiq.url.invalid";
          } catch (Exception e) {
				result = "error.etiq.url.validate";
	      }
		return result;
	}


	private ArrayList loadParaulesObertesLike(String valor) {
		MerliBean mb = new MerliBean();
		return mb.searchParaulaOberta(valor);	
	}
	
	
	 
	 
	 
}
