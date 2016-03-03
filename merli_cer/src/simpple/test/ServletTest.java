package simpple.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ServletTest extends HttpServlet {

	// logger
	static final Logger logger = Logger.getLogger("simpple.test.ServletTest");
	
	// Carreguem el driver de la base de dades
	public void init(ServletConfig config) throws ServletException {
		logger.debug("ServletTest init");
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		logger.debug("ServletTest doGet");
		someMethod();
	}

	public void someMethod()
	{
	  Connection jdbcConnection = null;

	  try
	  {
	    Class.forName( "com.mysql.jdbc.Driver" ).newInstance();
	    jdbcConnection = DriverManager.getConnection(
	      "jdbc:mysql://localhost/CercaCAT365?user=CercaCAT365&password=CercaCAT365"
	    );

	    Statement stmt = jdbcConnection.createStatement();
	    ResultSet results = stmt.executeQuery(
	      "select * from tipusfitxer"
	    );

	    while ( results.next() )
	    {
	      logger.debug( "Employee name is "+results.getString("descTipusFitxer") );
	    }
	    results.close();

	  }
	  catch ( Exception e )
	  {
		logger.error(e);  

	  }	
	  finally
	  {
	    try
	    {
	      jdbcConnection.close();
	    }
	    catch ( SQLException sqle )
	    {
	      // ..
	    }
	  }

	}	
	
	
}