package simpple.xtec.indexador.util;

import org.apache.log4j.Logger;

public class LogError {
	
	// logger
    static final Logger logger = Logger.getLogger(simpple.xtec.indexador.util.LogError.class);

	static String logError = "";
	
	public static void addLog (String logLine) {
	   try {
		  logError += logLine + "\n";
	     } catch (Exception e) {
	     logger.error(e);
	     }
	   }
	
}