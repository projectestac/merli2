package simpple.xtec.web.util;

import java.util.Hashtable;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class XMLCollection {

	static final Logger logger = Logger.getLogger(simpple.xtec.web.util.XMLCollection.class);
	static Hashtable hAllProperties = new Hashtable();
	
	public static void loadProperties (String nameFile) {
/*		BufferedReader in = null;
        String line = "";
        StringTokenizer myTokenizer = null;
		try {
	      nameFile = nameFile.replaceAll("cercador_educacio", "missatges");
		  hash = new Hashtable ();	
	      in = new BufferedReader(new FileReader(nameFile));    	
	      logger.info("Seeking..." + nameFile);  
          // while ((line = propertiesFile.readLine()) != null) {
	       while ((line = in.readLine()) != null) {
	        //  line = new String(line.getBytes(), "UTF-8");
	          logger.debug(line);
	          if ((line.indexOf("#") == -1) && !line.trim().equals("")) {
	        	  myTokenizer = new StringTokenizer(line, "=");
	        	  String key = myTokenizer.nextToken();
	        	  String value = myTokenizer.nextToken();
	        	  hash.put(key, value);
	             }
	          }
	  	  } catch (Exception e) {
		  logger.error(e);
		  }
*/		  
	   }

	
	public static void loadProperties () {
/*		BufferedReader in = null;
        String line = "";
        StringTokenizer myTokenizer = null;
		try {
			                             
	     // InputStream is = this.getClass().getResourceAsStream("/missatges.properties");
		  hash = new Hashtable ();
		  String sPropertiesFile = "missatges_en.properties";
	      in = new BufferedReader(new FileReader(sPropertiesFile));    	
	     // logger.error("Seeking..." + nameFile);  
          // while ((line = propertiesFile.readLine()) != null) {
	       while ((line = in.readLine()) != null) {
	        //  line = new String(line.getBytes(), "UTF-8");
	          logger.debug(line);
	          if ((line.indexOf("#") == -1) && !line.trim().equals("")) {
	        	  myTokenizer = new StringTokenizer(line, "=");
	        	  String key = myTokenizer.nextToken();
	        	  String value = myTokenizer.nextToken();
	        	  hash.put(key, value);
	             }
	          }
	  	  } catch (Exception e) {
		  logger.error(e);
		  }
*/		  
	   }

	public static Properties getProperties (String sLang) {
        Properties oProperties = null;
		try {
			if (hAllProperties.containsKey(sLang)){
				oProperties = (Properties)hAllProperties.get(sLang);
			}else{
				// Load properties
				String sPropertiesFile = "missatges_"+sLang+".properties";				
				oProperties = new Properties();
				oProperties.load(XMLCollection.class.getResourceAsStream("/simpple/xtec/i18n/"+sPropertiesFile));				
			    hAllProperties.put(sLang, oProperties);
				
/*				System.out.println("Loading properties from "+sPropertiesFile);
			    BufferedReader oIn = new BufferedReader(XMLCollection.class.getResourceAsStream(sPropertiesFile));    	
		        String line = "";
			    while ((line = oIn.readLine()) != null) {
			    	//  line = new String(line.getBytes(), "UTF-8");
			    	logger.debug(line);
			    	System.out.println("line="+line);
			        if ((line.indexOf("#") == -1) && !line.trim().equals("")) {
			        	  StringTokenizer myTokenizer = new StringTokenizer(line, "=");
			        	  String key = myTokenizer.nextToken();
			        	  String value = myTokenizer.nextToken();
			        	  hProperties.put(key, value);
			        }
			    }
			    hAllProperties.put(sLang, hProperties);
*/			    
			}
	  	} catch (Exception e) {
	  		e.printStackTrace();
	  		logger.error(e);
		}
	  	return oProperties;
	}
	
	public static String getProperty (String nameProperty) {
		return getProperty(nameProperty, "ca");
	}
	
	public static String getProperty (String nameProperty, String sLang) {
		String sValue = "";
		try {
			Properties oProperties = (Properties)getProperties(sLang);
			if (oProperties.containsKey(nameProperty)) {
				sValue = oProperties.getProperty(nameProperty); 
			} else {
				sValue = nameProperty;	
			}
		 } catch (Exception e) {
			 logger.error(e);	
		 }
		return sValue; 
	   }
	
    public static String getLang(HttpServletRequest oRequest){
    	String sLang = "ca";
    	if (oRequest.getParameter("lang")!=null){
    		sLang = oRequest.getParameter("lang");
    		oRequest.getSession().setAttribute("lang", sLang);
    	}else if (oRequest.getSession().getAttribute("lang")!=null){
    		sLang = (String)oRequest.getSession().getAttribute("lang");
    	}
    	return sLang;
    }
	
}