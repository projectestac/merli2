package edu.xtec.merli.segur.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.xtec.merli.segur.SSOUtilities;

/**
 * @author Mike Robinson
 */
public class LogOutAction extends Action {

    private static final Logger logger = Logger.getRootLogger();//("xtec.duc");
    protected static String USER_KEY = "usuari-edu365";

    /**
     * Handles user's request for login
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @throws Exception
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception {
        String addr = "login.jsp";//request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        logger.info("logout, redirigint a: " + addr);
        request.removeAttribute("estat");
        request.removeAttribute("despl");
        request.removeAttribute("id");
        request.removeAttribute("cerca");
        request.removeAttribute("fisic");
        request.removeAttribute("data_i");
        request.removeAttribute("data_f");
        request.removeAttribute("id_unitat");
        request.removeAttribute("id_catalogador");
        request.removeAttribute("ord");
        request.removeAttribute("value");
        SSOUtilities.initSSOLogout(response, request, addr);
        return (mapping.findForward(""));
    }

}
