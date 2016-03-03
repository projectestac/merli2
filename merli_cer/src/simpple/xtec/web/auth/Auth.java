/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simpple.xtec.web.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import simpple.xtec.dao.OracleTipusFitxersDAO;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.TipusFitxer;
import simpple.xtec.web.util.UtilsCercador;

/**
 *
 * @author NAseq
 */
public class Auth extends HttpServlet {

    private String username;
    private String password;

    public HttpSession sess;

    Connection myConnection = null;
    static final Logger logger = Logger.getLogger(simpple.xtec.web.auth.Auth.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        sess = request.getSession(true);
        response.setContentType("text/html");
        //boolean auth = request.authenticate(response);
        PrintWriter out = response.getWriter();

        username = request.getParameter("username");
        password = request.getParameter("password");
        logger.info(request.getHeader("Authorization"));

        if (validateUser(username, password)) {
            sess = request.getSession();
            sess.setAttribute("user", username);
            /*out.println("<HTML><HEAD><TITLE>Access Accepted</TITLE></HEAD>");
             out.println("<BODY>Your login and password are valid.<BR>");
             out.println("</BODY></HTML>");

             logger.error(request.getRemoteUser());*/
            response.sendRedirect("cerca/directoriInicial.jsp");
        } else {
            out.println("<HTML><HEAD><TITLE>Access Denied</TITLE></HEAD>");
            out.println("<BODY>Inici de sessió i / o la contrasenya és incorrecta o no té accés a aquesta opció. Si us plau, poseu-vos en contacte amb l'administrador.<BR>");
            out.println("Dins dels 5 segons aquesta pàgina redirigirà a la pàgina inicial.");
            out.println("</BODY></HTML>");
            response.setHeader("Refresh", "5;url=cerca/directoriInicial.jsp");
        }

//        response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/administracio/tipusFitxers.jsp");
    }

    public boolean validateUser(String username, String password) throws IOException {

        boolean valid = false;
        myConnection = UtilsCercador.getConnectionFromPool("jdbc/pool/MerliConnectionPoolDS");

        try {
            /*CallableStatement oFunction = myConnection.prepareCall("{ ? = call admxtec.FN_E13_MERLI_PASSWD_OK(?,?) }");
             logger.info("Connecting to: admxtec.FN_E13_MERLI_PASSWD_OK(?,?)");
             oFunction.registerOutParameter(1, Types.INTEGER);
             oFunction.setString(2, username);
             oFunction.setString(3, password);
             oFunction.execute();
             String sResult = oFunction.getString(1);
             logger.info(sResult);
             oFunction.close();*/

            CallableStatement oFunction = myConnection.prepareCall("{ call admxtec.PKG_XTEC.AUTENTICACIO(?,?,?) }");
            logger.info("Connecting to: admxtec.PKG_XTEC.AUTENTICACIO(?,?,?)");
            oFunction.setString(1, username);
            oFunction.setString(2, password);
            oFunction.registerOutParameter(3, Types.INTEGER);
            oFunction.execute();
            String sResult = oFunction.getString(3);
            logger.info("sResult: " + sResult);
            oFunction.close();
            if ("1".equals(sResult)) {
                valid = true;
            }
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            try {
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return valid;
    }

    public void logout() {

        sess.invalidate();
    }
}
