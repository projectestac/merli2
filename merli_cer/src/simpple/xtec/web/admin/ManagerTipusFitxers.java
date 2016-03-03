package simpple.xtec.web.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import simpple.xtec.dao.OracleTipusFitxersDAO;
import simpple.xtec.dao.OracleUsuarisDAO;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.TipusFitxer;

/**
 * Manages the actions over Usuari model
 *
 * @author descuer
 *
 */
public class ManagerTipusFitxers extends HttpServlet {

    private static final long serialVersionUID = 1L;
    // logger
    static final Logger logger = Logger.getLogger(simpple.xtec.web.admin.ManagerTipusFitxers.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nouGrup = "";
        String mimeType = "";
        String idTipusFitxers = "";
        int idTipusFitxersInt = 0;
        String operacio = "";

        OracleTipusFitxersDAO tipusFitxersDAO = null;
        try {
            request.setCharacterEncoding("UTF-8");
            logger.debug("doPost -> in");
            operacio = request.getParameter("operacio");
            logger.debug("operacio: " + operacio);
            tipusFitxersDAO = new OracleTipusFitxersDAO();
            if (operacio.equals("afegir")) {
                nouGrup = request.getParameter("nouGrup");
                logger.debug(nouGrup);
                mimeType = request.getParameter("mimeType");
                logger.debug(mimeType);
                tipusFitxersDAO.afegirTipusFitxers(nouGrup, mimeType);
            }
            if (operacio.equals("eliminar")) {
                idTipusFitxers = request.getParameter("idTipusFitxers");
                idTipusFitxersInt = new Integer(idTipusFitxers).intValue();
                tipusFitxersDAO.eliminarTipusFitxers(idTipusFitxersInt);
            }
            if (operacio.equals("modificar")) {
                idTipusFitxers = request.getParameter("idTipusFitxers");
                idTipusFitxersInt = new Integer(idTipusFitxers).intValue();
                logger.debug(idTipusFitxers);
                nouGrup = request.getParameter("nouGrup");
                logger.debug(nouGrup);
                mimeType = request.getParameter("mimeType");
                logger.debug(mimeType);
                tipusFitxersDAO.modificarTipusFitxers(idTipusFitxersInt, nouGrup, mimeType);
            }

            TipusFitxer.carregaTipusFitxer();
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                tipusFitxersDAO.disconnect();
            } catch (Exception e) {
                logger.error(e);
            }
        }
        logger.debug("doPost -> out");
        response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/administracio/tipusFitxers.jsp");

    }

}
