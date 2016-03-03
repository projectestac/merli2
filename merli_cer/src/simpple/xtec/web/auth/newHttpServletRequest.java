/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simpple.xtec.web.auth;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *
 * @author NAseq
 */
public class newHttpServletRequest extends HttpServletRequestWrapper
{
    private String user;

    public newHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    public newHttpServletRequest(HttpServletRequest aRequest,        
        String username)
    {
        super(aRequest);
        
        user = username;
    }

    /**
     * This method returns the Remote User name as user\@domain.com.
     */
    @Override
    public String getRemoteUser()
    {
        return user;
    }
    
}