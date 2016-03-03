package simpple.xtec.web.redirect;

import java.io.*; 

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.apache.log4j.Logger;



/**
 * This servlet handles the get author profile
 * ajax request. It gets the author name as parameter
 * queries the AuthorBean for the author profile and
 * returns the profile back.
 * 
 * @author Rahul Sapkal(rahul@javareference.com)
 */
public class ServletRedirectPortal extends HttpServlet 
{    

	 // logger
	 static final Logger logger = Logger.getLogger(simpple.xtec.web.redirect.ServletRedirectPortal.class);
	 
		/**
		 * Loads the database driver 
		 */
		
		public void init(ServletConfig config) throws ServletException {
			try {

			   } catch (Exception e) {
			   logger.error(e);
			   }
		}	  	 
		
		public void destroy() {
			try {
			} catch (Exception e) {
			logger.error(e);	
			}
			
		}
	/**
     * This method is overriden from the base class to handle the
     * get request. 
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
                   throws IOException {
    	try {
    	  response.sendRedirect("cerca/directoriInicial.jsp");	
    	 } catch (Exception e) {
    	 logger.error(e);	
    	 }
    }

	/**
     * This method is overriden from the base class to handle the
     * get request. 
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                   throws IOException {
    	try {
      	  response.sendRedirect("cerca/directoriInicial.jsp");    		
     	 } catch (Exception e) {
      	 logger.error(e);	
   	     }
    	
    }
    
    
}