package edu.xtec.merli;

import java.io.*;
import javax.servlet.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.taglib.html.Constants;

public class CharsetFilter implements Filter
{
        /**
        * Logging output for this plug in instance.
        */
 
        private Log myLog = LogFactory.getLog(Constants.Package);
 
        public void init(FilterConfig config) throws ServletException
        {
                myLog.info(this.getClass().getName() + ": Starting filter.");
        }
 
        public void destroy()
        {
                myLog.info(this.getClass().getName() + ": Destroying filter.");
        }
		/**
		 * Filtra tots els atributs del sistema. Converteix del format UTF-8 al desitjat.
		 */
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException
        {
 
                request.setCharacterEncoding("UTF-8");
 
         /*       myLog.info(this.getClass().getName() + ": Setting request to " +
                                                        request.getCharacterEncoding());
         */       chain.doFilter(request,response);
        }
}
