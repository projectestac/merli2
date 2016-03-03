package simpple.xtec.indexador.main;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
// import org.quartz.CronExpression;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;
import java.sql.*;
import simpple.xtec.indexador.util.Configuracio;
import simpple.xtec.indexador.util.Utils;

/**
 * Class used to add a cron interface to the application
 *
 * @author descuer
 *
 */
public class IndexadorBot {

    SchedulerFactory schedFact = null;
    JobDetail jobdetailCreaIndexCron = null;
    JobDetail jobdetailCreaIndex = null;
    CronTrigger crontriggerCreaIndex = null;

    // Scheduler
    static Scheduler sched;

    // Cron applied
    static String cronString = "";

    // Logger
    static final Logger logger = Logger.getLogger(simpple.xtec.indexador.main.IndexadorBot.class);

    // Empty constructor
    public IndexadorBot() {
    }

    /**
     * Get the current cron string from database
     *
     * @return
     */
    public String getCurrentCron() {
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String ordre_cron = "";
        try {
            logger.debug("getCurrentCron -> in");
            //	 logger.debug("Connection: " + Configuracio.cadenaConnexioBDOracle);
//         myConnection = DriverManager.getConnection(Configuracio.cadenaConnexioBDOracle, Configuracio.userBDOracle, Configuracio.passwordBDOracle);
            myConnection = Utils.getConnectionFromPool();
            stmt = myConnection.createStatement();
            sql = "SELECT ordre_cron FROM config_indexacio";
            logger.debug(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            ordre_cron = rs.getString("ordre_cron");
            logger.debug("Ordre cron: " + ordre_cron);
            logger.debug("getCurrentCron -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    Utils.commit(myConnection);
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return ordre_cron;
    }

    /**
     * Returns true if the index creation process is going to start right now
     *
     * @return
     */
    public boolean indexacioInmediata() {
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int indexacio_inmediata = 0;
        boolean isIndexacioInmediata = false;
        try {
            logger.debug("indexacioInmediata -> in");
            myConnection = Utils.getConnectionFromPool();
            stmt = myConnection.createStatement();
            sql = "SELECT indexacio_inmediata FROM config_indexacio";
//            logger.debug(sql);
            rs = stmt.executeQuery(sql);
            rs.next();
            indexacio_inmediata = rs.getInt("indexacio_inmediata");
  //          logger.debug("Indexacio inmediata: " + indexacio_inmediata);
            if (indexacio_inmediata == 1) {
                isIndexacioInmediata = true;
                sql = "UPDATE config_indexacio SET indexacio_inmediata=0";
    //            logger.debug(sql);
                stmt.executeUpdate(sql);
            }
            logger.debug("indexacioInmediata -> out");
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }

                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    Utils.commit(myConnection);
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return isIndexacioInmediata;
    }

    /**
     * Creates the main job
     *
     */
    public void crearJobCron() {
        try {
            logger.debug("crearJobCron -> in");
            jobdetailCreaIndexCron = new JobDetail("CreaIndexCron", Scheduler.DEFAULT_GROUP, simpple.xtec.indexador.main.MerliHarvester.class);
            logger.debug("crearJobCron -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void crearJob() {
        try {
            logger.debug("crearJob -> in");
            jobdetailCreaIndex = new JobDetail("CreaIndex", Scheduler.DEFAULT_GROUP, simpple.xtec.indexador.main.MerliHarvester.class);
            logger.debug("crearJob -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void doProcess(String cron0String) {
        try {
            logger.debug("doProcess -> in");
            schedFact = new StdSchedulerFactory();
            logger.debug("Creating scheduler...");
            sched = schedFact.getScheduler();
            logger.debug("Starting scheduler");
            sched.start();

            crearJobCron();
            crontriggerCreaIndex = new CronTrigger("CreaIndexCron", Scheduler.DEFAULT_GROUP, "CreaIndexCron", Scheduler.DEFAULT_GROUP, cron0String);

            logger.debug("Scheduling...");

            sched.scheduleJob(jobdetailCreaIndexCron, crontriggerCreaIndex);
            logger.debug("doProcess -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Starts the process immediately
     */
    public void indexarAra() {
        try {
            logger.debug("indexarAra -> in");
            long startTime = System.currentTimeMillis() + 10000L;
            crearJob();
            SimpleTrigger trigger = new SimpleTrigger("myTrigger",
                    null,
                    new Date(startTime),
                    null,
                    0,
                    0L);
            sched.scheduleJob(jobdetailCreaIndex, trigger);
            logger.debug("indexarAra -> out");
        } catch (Exception e) {
            logger.error(e);
        }

    }

    /**
     * Cleans the scheduler
     */
    public void cleanScheduler() {
        try {
            logger.debug("cleanScheduler -> in");
            sched.unscheduleJob("CreaIndexCron", Scheduler.DEFAULT_GROUP);
            sched.unscheduleJob("CreaIndex", Scheduler.DEFAULT_GROUP);
            logger.debug("cleanScheduler -> out");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * Main method
     *
     * @param args
     */
    public static void main(String[] args) {
        int periode = 0;
        try {
            logger.debug("main -> in");
            if (args.length < 1) {
                logger.debug("Use: IndexadorBot periode");
                System.exit(0);
            }
            periode = new Integer(args[0]).intValue();
            logger.debug("periode -> " + periode);
            iniciarProces(periode);
            logger.debug("main -> out");
        } catch (Exception e) {
            logger.error(e);
        }

    }
    private static final Object lock = new Object();

    public static void iniciarProces(int periode) {
        String newCron = "";
        IndexadorBot indexadorBot = null;
        boolean indexarAra = false;
        boolean final_ = false;
        int counter = 0;
        try {

            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }

            indexadorBot = new IndexadorBot();
            cronString = indexadorBot.getCurrentCron();

            logger.debug("Cron string -> " + cronString);

            indexadorBot.doProcess(cronString);
            // String semafor = "";

//            synchronized (semafor) {
            synchronized (lock) {
                while (1 == 1) {
                    logger.info("Sleeping...");
                    // Thread.sleep(periode * 10000);
                    /*Thread.currentThread();
                     Thread.sleep(periode * 1000);*/
                    Long time = (long) periode * 1000;
                    lock.wait(time);
                    newCron = indexadorBot.getCurrentCron();
                    indexarAra = indexadorBot.indexacioInmediata();
                    logger.info("Testing.... ");

                    if (!newCron.equals(cronString)) {
                        logger.info("Cleaning cron!");

                        indexadorBot.cleanScheduler();
                        logger.info("Setting new cron: " + newCron);

                        indexadorBot.doProcess(newCron);
                        cronString = newCron;
                    }
                    if (indexarAra) {
                        logger.info("Indexar ara..");
                        indexadorBot.indexarAra();
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
            logger.debug("main -> out");
        }
    }

}
