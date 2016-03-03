package simpple.xtec.web.cercador;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import simpple.xtec.web.util.Indexador;

 public class MyTimerTask extends TimerTask {



	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.cercador.MyTimerTask.class);
	
	
	public void run() {
		// TODO Auto-generated method stub
		logger.debug("*****************************");
		logger.debug(" TIMER ");
		logger.debug("*****************************");		

		try {
		  
		  Indexador.doProcess();
//		  ServletCerca.updateIndex();

		  } catch (Exception e) {
		  logger.error(e);	  
		  }
	    logger.debug("*****************************");
		logger.debug(" END TIMER ");
		logger.debug("*****************************");				  
	}
	
}