package edu.xtec.merli.segur;

import edu.xtec.merli.segur.login.LoginBean;
import edu.xtec.merli.segur.login.LoginForm;
import edu.xtec.util.db.ConnectionBean;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SSOUtilities {
    /*
     * The value returned by this function will be upper case
     */

    private static final Logger logger = Logger.getRootLogger();//("xtec.duc");

    public static String getSSOUser(HttpServletRequest request) {

        return request.getRemoteUser();
    }

    /*
     * Set Session level SSO attributes containing user and subscriber information 
     * for added security.
     */
    private static void setSessionLevelSSOData(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");

        //Sets values only if the current session values are null.
        if (user == null) {
            LoginBean lb = new LoginBean();
            request.getSession().setAttribute("user", lb.getUser(request.getRemoteUser()));
        }
    }

    /*
     * This function parses the the user DN and returns the value
     * of the username portion of cn=<username>
     */
    public static String getSSOUserCaseSensitive(HttpServletRequest request) {
        String user_dn = request.getHeader("Osso-User-Dn");
        //user DN = cn=<username>, .....
        return user_dn.substring(user_dn.indexOf("cn=") + 3, user_dn.indexOf(","));
    }

    public static boolean isProtectedPattern(HttpServletRequest request, String pattern) {
        boolean v_protected = false;

        if ((request.getRequestURL().toString()).indexOf(pattern) > 0) {
            v_protected = true;
        }

        return v_protected;
    }

    public static boolean userLoggedIn(HttpServletRequest request) {
        boolean vReturn = true;

        String ssoUser = request.getRemoteUser();
        String ssoSubscriber = request.getHeader("OSSO-SUBSCRIBER");
        User user = (User) request.getSession().getAttribute("user");
//     String  appSubscriber  = (String) request.getSession().getAttribute("APP_SSO_SUBSCRIBER");

        if (ssoUser == null && ssoSubscriber == null) {
            vReturn = false;

            //Set session level data
        } else {
            setSessionLevelSSOData(request);
        }

        //Second check to make sure session level data and mod_osso cookie data are same.
        if (user != null) {
            if (!ssoUser.equalsIgnoreCase(user.getUser())) {
                //Session level SSO values do not match values obtained from mod_osso cookie
                request.getSession().invalidate();
                vReturn = false;
            }
        }

        return vReturn;
    }

    public static ServletResponse initSSOLogin(HttpServletResponse response, String redirectURL, boolean force) throws Exception {
        //Reset response header
        response.reset();

        /*if (force){
         //Would not require user to re-authenticated if already authenticated.
         response.setHeader( "Osso-Paranoid", "true" );
         }else
         {
         //Force user to re-authenticate
         response.setHeader( "Osso-Paranoid", "false" );         
         }
        
         // Set return URL for Post login
         response.setHeader("Osso-Return-Url",redirectURL);

         //Send Dynamic Directive for login
         response.sendError(499, "Oracle SSO");*/
        //if (!SSOUtilities.checkUser()) {
        response.sendRedirect("login.jsp");
        //}

        return (ServletResponse) response;
    }

    public static ServletResponse initSSOLogout(HttpServletResponse response, HttpServletRequest request, String redirectURL) throws Exception {
        // Invalidate current session and all session objects and variable
        logger.debug("Session invalidating");
        request.getSession().invalidate();
        logger.debug("Session invalidated");
        // Set return URL for Post logout
      /*response.setHeader("Osso-Return-Url",redirectURL);
         // Send Dynamic Directive for logout
         response.sendError(470, "Oracle SSO");*/
        response.sendRedirect("login.jsp");

        return (ServletResponse) response;
    }

}
