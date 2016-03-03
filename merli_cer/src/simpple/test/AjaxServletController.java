package simpple.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*import es.mcu.pares.descripcion.database.descripcion.IndiceBoDao;
import es.mcu.pares.descripcion.form.descripcion.IndicedescForm;
import es.mcu.pares.descripcion.util.ConversorUtil;
import es.mcu.pares.descripcion.util.constantes.ConstantesWeb;
*/
public class AjaxServletController extends HttpServlet  {

	private static final String CONTENT_TYPE_XML = "text/xml";
	
    /**
     * This method is overriden from the base class to handle the
     * get request. 
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                   throws IOException
    {
        //set the content type
		response.setContentType(CONTENT_TYPE_XML);
        
    	//request.setHeader("Cache-Control", "no-cache");
        
        //get the PrintWriter object to write the html page
        PrintWriter writer = response.getWriter();
        
        // recogemos los parametros de la sesion
        HttpSession session = request.getSession(true);
  /*      IndicedescForm indicedescTipo = new IndicedescForm();        
        String campoIndice = (String)session.getAttribute(ConstantesWeb.PARAM_SESSION_CAMPO_INDICE);                               
        indicedescTipo.setCampoIndice(campoIndice);                 
        indicedescTipo.setIdIndicedesc(new Long(1)); //(Long)ConversorUtil.ponerNulo(Long.class, request.getParameter(ConstantesWeb.PARAM_REQ_BUSQUEDA_RAPIDA_SEL_TIPO)));                
        indicedescTipo.setIndicedesc(ConversorUtil.ponerNulo(request.getParameter(ConstantesWeb.PARAM_REQ_BUSQUEDA_RAPIDA_TXT_CAMPO)));
        List forms = null; 
        try {
        	forms = IndiceBoDao.findIndicesBusquedaRapida (indicedescTipo);
        } catch (Exception e) {
        	
        }
        
        if (forms != null && forms.size()>0) {
	        //get the author profile by quering the AuthorsBean by passing author name
	        writer.println("<indices>" + ((IndicedescForm)forms.get(0)).getIndicedesc() + "</indices>");
        } else {
	        writer.println("<indices>No hay resultados</indices>");
        }
        
        //close the write
        writer.close();
        */
    }  

}
