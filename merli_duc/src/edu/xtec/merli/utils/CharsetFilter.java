package edu.xtec.merli.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class CharsetFilter implements Filter
{
        /**
        * Logging output for this plug in instance.
        */
 
       // private Log myLog = LogFactory.getLog(Constants.Package);
 
        public void init(FilterConfig config) throws ServletException
        {
              //  myLog.info(this.getClass().getName() + ": Starting filter.");
        }
 
        public void destroy()
        {
         //       myLog.info(this.getClass().getName() + ": Destroying filter.");
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
