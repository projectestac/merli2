/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.xtec.merli.segur;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;


/**
 *
 * @author Nadim Aseq A Arman [ Itteria ]
 */
public class authUtilities {

    private static final Logger logger = Logger.getRootLogger();//("xtec.duc");

    public static ServletResponse initSSOLogin(HttpServletResponse response, String redirectURL, boolean force) throws Exception {
        //Reset response header
        response.reset();
        response.sendRedirect("login.jsp");
        
        return (ServletResponse) response;
    }

    public static ServletResponse initSSOLogout(HttpServletResponse response, HttpServletRequest request, String redirectURL) throws Exception {
        // Invalidate current session and all session objects and variable
        logger.debug("Session invalidating");
        request.getSession().invalidate();
        logger.debug("Session invalidated");     
        response.sendRedirect(redirectURL);

        return (ServletResponse) response;
    }

}
