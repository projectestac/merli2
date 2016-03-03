package simpple.xtec.web.admin;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.quartz.CronExpression;

import simpple.xtec.dao.OracleIndexacioDAO;
import simpple.xtec.web.util.Configuracio;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet used to update information from the 'configuracio' table
 *
 * @author descuer
 *
 */
public class ServletIndexacio extends HttpServlet {

    private static final long serialVersionUID = 1L;
    // logger
    static final Logger logger = Logger.getLogger(simpple.xtec.web.admin.ServletIndexacio.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OracleIndexacioDAO indexacioDAO = null;
        CronExpression myCronExpression = null;
        boolean errors = false;
        String operacio = "";
        try {
            logger.debug("doPost -> in");
            operacio = request.getParameter("operacio");
            logger.debug("operacio: " + operacio);
            indexacioDAO = new OracleIndexacioDAO();
            if (operacio.equals("modificar")) {
                logger.debug("modify");
                String programacioTemporal = request.getParameter("programacioTemporal");
                logger.debug("programacioTemporal: " + programacioTemporal);
                myCronExpression = new CronExpression(programacioTemporal);
                indexacioDAO.actualitzaDades(programacioTemporal);
            }
            if (operacio.equals("indexar")) {
                logger.debug("index new");
                indexacioDAO.indexarAra();
            }

        } catch (ParseException e) {
            logger.error(e);
            errors = true;

        } catch (SQLException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                indexacioDAO.disconnect();
            } catch (Exception e) {
                logger.error(e);
            }
        }
        logger.debug("doPost -> out");
        if (errors) {
            response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/administracio/errorsCron.jsp");
        } else {
            response.sendRedirect("/" + Configuracio.contextWebAplicacio + "/administracio/indexacio.jsp");
        }
    }

}
