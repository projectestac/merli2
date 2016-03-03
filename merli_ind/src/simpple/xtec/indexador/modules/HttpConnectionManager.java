package simpple.xtec.indexador.modules;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import simpple.xtec.indexador.util.UrlUtility;



/**
 * Manages http connections
 * 
 * @author descuer
 */

public class HttpConnectionManager {
	
	int codiHttp = 0;
	long sizeLimitText = 700000;
	long sizeLimitBinary = 1000000;	

	PostMethod post = null;
	GetMethod get = null;	
	//	Logger 	
	static Logger logger = Logger.getLogger(simpple.xtec.indexador.modules.HttpConnectionManager.class);
		
	String userAgent = "Cercador XTEC Crawler - Test / Mozilla/4.0 (compatible; MSIE 5.5; Windows 98; Win 9x 4.90)";
	
	InputStream inputStream = null;
    
	StringBuffer bodyPageBuffer = null;
	String bodyPage = "";
	boolean isBinary = false;
	boolean isImage = false;
	
	public InputStream getInputStream () {
		return inputStream;
	}
 
	public String getBodyPage () {
		try {
		  bodyPage = bodyPage.replaceAll("[^a-zA-Z0-9?¿!().,:;+-áéíóúèòïüçñÁÉÍÓÚÇÑÀÈÒ'\"&#%]", " ");
		  } catch (Exception e) {
		  logger.error(e);	  
		  }
		return bodyPage; 
	}
	
	
	// Constructor
	public HttpConnectionManager (boolean isBinary) {
		this.isBinary = isBinary;
		bodyPageBuffer = new StringBuffer();
	    }
	
	public HttpConnectionManager () {
		bodyPageBuffer = new StringBuffer();
	    }	
	
	

    /**
     * Checks if the content is in binary format
     * 
     * @param contentTypeHeader
     */	
	
	public void checkContentType (Header contentTypeHeader) {
		try {
			logger.debug("Checking content type..." + contentTypeHeader.getValue());
			if (contentTypeHeader != null) {
				if ((contentTypeHeader.getValue().toLowerCase().indexOf("application/") != -1) && (contentTypeHeader.getValue().toLowerCase().indexOf("javascript") == -1)) {
				   isBinary = true;
				   logger.debug("Binary");
				   }
				if (contentTypeHeader.getValue().toLowerCase().indexOf("multipart") != -1) {
				   isBinary = true;
				   logger.debug("Binary");				   
				   }
				if (contentTypeHeader.getValue().toLowerCase().indexOf("image") != -1) {
				   isBinary = true;
				   logger.debug("Binary");				   
				   isImage = true;
				   logger.debug("Image");				   
				   throw new Exception();
				   }
			}

		} catch (Exception e) {
		logger.error(e);
		}		
	 }

	/**
	 * Try to connect to URL with GET method 
	 * 
	 * @param client
	 * @param urlLink
	 * @return
	 */	
	
	public boolean executeGetMethod (HttpClient client, String urlLink, boolean isRedirection) {
		String redirectionURL = "";
		try {
			logger.debug("executeGetMethod -> in");
			logger.debug("Executing get method.. " + urlLink);
			get = new GetMethod (urlLink);
			get.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
			get.addRequestHeader("User-Agent", userAgent);
			codiHttp = client.executeMethod(get);
			logger.debug("Return code.. " + codiHttp);			
			checkContentType (get.getResponseHeader("Content-Type"));
			if ((codiHttp >= 200) && (codiHttp <= 206)) {
			   getContent (get, urlLink);
			   if ((codiHttp >= 300) && (codiHttp <= 306) && !isRedirection) {
				   redirectionURL = getRedirectionURL(get, urlLink);
				   executeGetMethod (client, redirectionURL, !isRedirection);
			       }			   
			   } else {
			   return false;	   
			   }
			logger.debug("executeGetMethod -> out");			
		} catch (Exception e) {
		logger.error(e);	
		}
	  return true;	
	  }

	/**
	 * Try to connect to URL with POST method 
	 * 
	 * @param client
	 * @param urlLink
	 * @return
	 */
	
	public boolean executePostMethod (HttpClient client, String urlLink) {

		try {
			logger.debug("executePostMethod -> in");			
			logger.debug("Executing post method.. " + urlLink);			
			post = new PostMethod(urlLink);
			post.addRequestHeader("User-Agent", userAgent);				
			codiHttp = client.executeMethod(post);
			logger.debug("Return code.. " + codiHttp);
			if ((codiHttp >= 200) && (codiHttp <= 206)) {
				getContent (post, urlLink);
				}			
			logger.debug("executePostMethod -> out");
		} catch (Exception e) {
		logger.error(e);	
		}
	  return true;	
	  }
	
	/**
	 * Main method, first try a GET connection, and if it fails,
	 * try the POST connection
	 * 
	 * @param client
	 * @param urlLink
	 * @throws HttpException
	 */
	
	public void executarPeticio (HttpClient client, String urlLink) throws HttpException{
		try {
	      logger.debug("executarPeticio -> in");			
		  if (!executeGetMethod(client,urlLink, false)) {
			  executePostMethod(client,urlLink);
  		      }
	      logger.debug("executarPeticio -> out");		  
		 } catch (Exception e) {
		 logger.error(e);	
	  	 } finally {
			try {
			  if (!isBinary) {
			     releaseConnections();
			     MultiThreadedHttpConnectionManager mt = (MultiThreadedHttpConnectionManager)client.getHttpConnectionManager();
			     mt.closeIdleConnections(10000); 
			     mt.deleteClosedConnections();
			     }	
		      bodyPage = bodyPageBuffer.toString();
			  } catch (Exception e) {
			  logger.error(e);	
			  }
		}
		
	}
	
	/**
	 * Gets the binary or textual content
	 * 
	 * @param methodBase
	 */
	
    public void getContent (HttpMethodBase methodBase, String urlLink) {
	   BufferedReader reader = null;
	   String nextLine = "";
       try {
    	 logger.debug("getContent -> in"); 
	     if (isBinary) {
		   inputStream = methodBase.getResponseBodyAsStream();
		   } else {
		   reader = new BufferedReader(new InputStreamReader(methodBase.getResponseBodyAsStream(), methodBase.getResponseCharSet()));
		   nextLine = reader.readLine();;
		   while (nextLine != null) {
		   	   bodyPageBuffer.append(nextLine + "\n");
		   	   nextLine = reader.readLine();
		       }
		   logger.debug("Content length..." + bodyPageBuffer.length());
		   }
    	 logger.debug("getContent -> out");	     
        } catch (Exception e) {
        logger.error(e);
        }
	     
       }
    
  /**
   * Gets the redirect location from the headers
   * 
   * @param methodBase
   * @param urlLink
   * @return
   */
  
  public String getRedirectionURL (HttpMethodBase methodBase, String urlLink) {  
	String redirectLocation = "";
	Header locationHeader = null;
    try {
       logger.debug("getRedirectionURL -> in");	
       locationHeader = methodBase.getResponseHeader("location");
	   if (locationHeader != null) {
		 redirectLocation = locationHeader.getValue();	      	     
		 try {
			redirectLocation = new URL(UrlUtility.resolveBase(urlLink, redirectLocation)).toString();
			logger.debug("Redirect location.." + redirectLocation);						
		  } catch (Exception e) {
		  logger.error(e);
		  }	
	    }	
       logger.debug("getRedirectionURL -> out");	   
	  } catch (Exception e) {
      logger.error(e);		 
	  }
	return redirectLocation;
    }  

	
	/**
	 * Allibera les connexions http
	 */
	
	public void releaseConnections (){
		try {

			if (get != null) {
				get.releaseConnection();	
			}
			if (post != null) {
				post.releaseConnection();	
			}

		} catch (Exception e) {
			logger.error(e);	
		}		
	}
	
	/**
	 * Retorna cert si és codi de redirecció
	 **/
	
	public boolean isRedirectCode (int resultCode) {
		
		//301 Moved Permanently. HttpStatus.SC_MOVED_PERMANENTLY 
		//302 Moved Temporarily. HttpStatus.SC_MOVED_TEMPORARILY 
		//303 See Other. HttpStatus.SC_SEE_OTHER 
		//307 Temporary Redirect. HttpStatus.SC_TEMPORARY_REDIRECT 
		
		if ((resultCode == 301) ||
			(resultCode == 302) ||
			(resultCode == 303) ||
			(resultCode == 307)) {
			return true;
		}
		return false;
	}	
	
	
	// Getters
	
	public int getCodiHttp () {
		return codiHttp;	
	}	
	
	
}