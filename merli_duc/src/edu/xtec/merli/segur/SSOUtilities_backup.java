package edu.xtec.merli.segur;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  
import javax.servlet.ServletResponse;

import edu.xtec.merli.segur.login.LoginBean;

public class SSOUtilities_backup 
{
  /*
   * The value returned by this function will be upper case
   */
  public static String getSSOUser(HttpServletRequest request)
  {
     
     return request.getRemoteUser();
  }

  /*
   * Set Session level SSO attributes containing user and subscriber information 
   * for added security.
   */
   
  private static void setSessionLevelSSOData(HttpServletRequest request)
  {
    User user = (User)request.getSession().getAttribute("user");

    //Sets values only if the current session values are null.
    if (user == null){
    	LoginBean lb = new LoginBean();
    	request.getSession().setAttribute("user",lb.getUser(request.getRemoteUser()));
    }
  }

  /*
   * This function parses the the user DN and returns the value
   * of the username portion of cn=<username>
   */

  public static String getSSOUserCaseSensitive(HttpServletRequest request)
  {
     String user_dn = request.getHeader("Osso-User-Dn");
     //user DN = cn=<username>, .....
     return user_dn.substring(user_dn.indexOf("cn=")+3,user_dn.indexOf(","));
  }
  public static boolean isProtectedPattern(HttpServletRequest request,String pattern)
  {
     boolean v_protected = false;

     if ((request.getRequestURL().toString()).indexOf(pattern) > 0)
     {
       v_protected = true;
     }

     return v_protected;
  }

  public static boolean userLoggedIn(HttpServletRequest request)
  {
     boolean vReturn = true;

     String  ssoUser        = request.getRemoteUser();
     String  ssoSubscriber  = request.getHeader("OSSO-SUBSCRIBER");
     User user        = (User) request.getSession().getAttribute("user");
//     String  appSubscriber  = (String) request.getSession().getAttribute("APP_SSO_SUBSCRIBER");
    
     if (ssoUser ==null && ssoSubscriber ==null)
     {
       vReturn = false;

     //Set session level data
     }else{ 
         setSessionLevelSSOData(request);
     }

    //Second check to make sure session level data and mod_osso cookie data are same.
     if (user != null ){
       if (!ssoUser.equalsIgnoreCase(user.getUser()))
       {
         //Session level SSO values do not match values obtained from mod_osso cookie
         request.getSession().invalidate();
         vReturn = false;       
       }
     }

     return vReturn;
  }

  public static ServletResponse initSSOLogin(HttpServletResponse response, String redirectURL,boolean force) throws Exception
   {
        //Reset response header
        response.reset();

        if (force){
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
        response.sendError(499, "Oracle SSO");

        return (ServletResponse)response;
   }

  public static ServletResponse initSSOLogout(HttpServletResponse response, HttpServletRequest request, String redirectURL) throws Exception
   {
      // Invalidate current session and all session objects and variable
      request.getSession().invalidate();
      // Set return URL for Post logout
      response.setHeader("Osso-Return-Url",redirectURL);
      // Send Dynamic Directive for logout
      response.sendError(470, "Oracle SSO");

      return (ServletResponse)response;
   }
}
