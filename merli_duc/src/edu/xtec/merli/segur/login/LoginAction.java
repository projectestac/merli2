package edu.xtec.merli.segur.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author Mike Robinson
 *
 */
public class LoginAction extends Action {

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
        // create a new LoginBean with valid users in it
        LoginBean lb = new LoginBean();

        // check to see if this user/password combination are valid
        if (lb.validateUser(((LoginForm) form).getUserName(), ((LoginForm) form).getPassword())) {
            try {
                // Clear old-session attributes
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
                // Clear old-session attributes

                request.getSession().setAttribute("user", lb.getUser(((LoginForm) form).getUserName()));
                //Amendez 23/03/2016 https://trello.com/c/zD0E2Ro8
                logger.info((((LoginForm) form).getUserName()) );
                logger.info("user validated.");
                return (mapping.findForward("success"));
            } catch (Exception e) {
                logger.warn("user cann't be validated.");
                return (mapping.findForward("failure"));
            }
        } else // username/password not validated
        {
            logger.info("user Non validated.");
            // create ActionError and save in the request
            ActionErrors errors = new ActionErrors();
            ActionError error = new ActionError("error.login.invalid");
            errors.add("login", error);
            saveErrors(request, errors);

            return (mapping.findForward("failure"));
        }
    }

}
