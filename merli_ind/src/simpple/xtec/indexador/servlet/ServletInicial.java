package simpple.xtec.indexador.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import simpple.xtec.indexador.main.IndexadorBot;
import simpple.xtec.indexador.util.Configuracio;

/**
 *
 * Servlet used to generate the 24-hour activity graph
 *
 * @author descuer
 *
 */
public class ServletInicial extends HttpServlet implements Runnable {

    private static final long serialVersionUID = 1L;

    // directori fisic on esta l'aplicacio dins del servidor
    String path = "";

    // logger
    static final Logger logger = Logger.getLogger(simpple.xtec.indexador.servlet.ServletInicial.class);

    ServletContext ctx = null;
    Thread myThread = null;

    /**
     * Gets configuration from properties file and loads the database driver
     */

    public void init(ServletConfig config) throws ServletException {

        try {
            logger.debug("********************");
            logger.debug("INITTTTTTTTTTTTTTTT");
            logger.debug("********************");
            if (myThread == null) {
                myThread = new Thread(this);
                myThread.setPriority(Thread.MIN_PRIORITY);
                myThread.start();
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void run() {
        try {
            logger.debug("********************");
            logger.debug("EXECUTINGGG");
            logger.debug("********************");
            logger.debug("Loading configuracio");
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            logger.debug("********************");
            IndexadorBot.iniciarProces(15);
            logger.debug("********************");
            logger.debug("EXECUTED");
            logger.debug("********************");

        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Mètode principal, es mostren les 5 darreres cerques i el gràfic generat 
	 *
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
