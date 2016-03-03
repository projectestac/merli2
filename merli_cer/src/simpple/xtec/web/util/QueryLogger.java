package simpple.xtec.web.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;

import org.apache.log4j.Logger;

public class QueryLogger {

	 // logger
	 static final Logger logger = Logger.getLogger(simpple.xtec.web.util.QueryLogger.class);
	
	
	public static void saveQueryInfo (
	                Connection myConnection,			
			String textCerca,
			String tipus
	) throws SQLException, Exception {
				
		String insertStatement = "";
		PreparedStatement prepStmt = null;
		
		try {
			
			if (Configuracio.isVoid()) {
				Configuracio.carregaConfiguracio();
			    }
			    // myConnection = UtilsCercador.getConnectionFromPool();

			insertStatement = "INSERT INTO log_cerques (id, cerca, cercador_id, data_cerca) VALUES (s_log_cerques.nextVal, ?, ?, ?)";
			
			logger.debug("[actualitzaDades] " + insertStatement);
			
			
			prepStmt = myConnection.prepareStatement (insertStatement);
			logger.debug("[textCerca] " + textCerca);
			if (textCerca == null) {
				logger.debug("[textCerca null] ");
			    } else {
			    logger.debug("[textCerca not null] ");
			    }
			if (textCerca.trim().equals("")) {
			  textCerca = "  ";	
			  }
			prepStmt.setString(1, textCerca);
			if (tipus.equals("simple")) {
			   prepStmt.setInt(2, 1);
			   } else {
			   prepStmt.setInt(2, 2);	   
			   }

    		Calendar rightNow = Calendar.getInstance();
			String data_actual_string = UtilsCercador.calculaDataActual (rightNow);
			String hora_actual_string = UtilsCercador.calculaHoraActual (rightNow);

			prepStmt.setString(3, data_actual_string + " " + hora_actual_string);
			logger.debug("[saveQueryInfo] " + prepStmt.toString());			

			prepStmt.executeUpdate(); 
		} catch (SQLException e) {
			throw e;	
		} catch (Exception e) {
			throw e;	
		} finally {
			if (prepStmt != null) {
			  prepStmt.close();
			  }
		}	
	}		
	
	
}