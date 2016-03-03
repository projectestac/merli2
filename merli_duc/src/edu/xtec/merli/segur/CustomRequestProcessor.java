package edu.xtec.merli.segur;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.RequestProcessor;

import edu.xtec.merli.segur.login.LoginBean;

public class CustomRequestProcessor extends RequestProcessor {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String sUserId;
    protected LoginBean lb;

    private static final Logger logger = Logger.getRootLogger();//("xtec.duc");

    protected boolean processPreprocess(HttpServletRequest request,
            HttpServletResponse response) {
        boolean continueProcessing = true; // assume success
        // Test if the request is a login request
        try {
            this.request = request;
            HttpSession session = null;
            // ensure that the user’s session has not timed out
            if (request.isRequestedSessionIdValid()) {
                session = request.getSession();
            } else {
                // user’s session has timed out, make them login again
                SSOUtilities.initSSOLogin(response, request.getRequestURL().toString(), true);
                logger.info("Session time out.");
            }

            // get the current request’s path
            String path = processPath(request, response);
            // don’t do any testing if user is logging on
            if (!path.equals((String) "/login")) {
                // get the user bean
                User user = (User) session.getAttribute("user");
                // ensure that user has logged on
                if (user == null) // else make them login first
                {
                    if (!SSOUtilities.userLoggedIn(request)) {
                        //redirigim a tots els usuaris que no estiguin logats al SSO.
                        SSOUtilities.initSSOLogin(response, request.getRequestURL().toString(), true);
                        logger.info("non validated user.");
                        try {
                            continueProcessing = false;
                        } catch (Exception ioe) {
                            logger.error("problem redirecting in processPreprocess - "
                                    + ioe.getMessage());
                            continueProcessing = false;
                        }
                    } else {
                        logger.info("validated user");
                    }
                }
            }
        } catch (Exception ioe) {
            logger.error("problem processing path - " + ioe.getMessage());
            continueProcessing = false;
        }
        return continueProcessing;
    }

    protected void redirectToValidation() {
        try {
            response.sendRedirect("validate.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void redirectToError() {
        try {
            response.sendRedirect("error.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
