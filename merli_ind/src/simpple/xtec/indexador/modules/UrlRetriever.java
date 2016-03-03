package simpple.xtec.indexador.modules;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import simpple.xtec.indexador.modules.HttpConnectionManager;
import simpple.xtec.indexador.parsers.NewHtmlParser;

/**
 * Retrieves one url
 * 
 * @author descuer
 *
 */

public class UrlRetriever {
	HttpClient client = null;
	
	static final Logger logger = Logger.getLogger("simpple.educacio.modules.UrlRetriever");	
	
	public UrlRetriever (HttpClient client) {
	   this.client = client;
	   }
	
	/**
	 * Process one url
	 */
	
	public int process (String urlLink){
	   String fileType = "";	
	   HttpConnectionManager cm = null;
	   int codiHttp = 0;
	   try {
		logger.debug("process -> in");   
		fileType = identifyType (urlLink);
		logger.debug("fileType -> " + fileType);		
		boolean isBinary = true;
		if (fileType.equals("HTML") || fileType.equals("EMPTY") || fileType.equals("TXT")) {
			isBinary = false;				
		    }
		if (!isBinary) {
			  cm = new HttpConnectionManager(isBinary);
			  logger.debug("[process] Executant peticio..." + fileType);				
			  try {

			    cm.executarPeticio (client, urlLink);				  
			    } catch (HttpException e) {
			  return 0;
			  }
			codiHttp = cm.getCodiHttp();
			
			logger.debug("[process] Petició executada...");					

			if (fileType.equals("TXT")) {
				String bodyPagina = cm.getBodyPage();
				// Stats.addBytes(bodyPagina.length(), System.currentTimeMillis());					
/*				doc = new DocumentToIndex();
				doc.text = bodyPagina;
				doc.url = link.getUrlLink();
			    doc.fileType = 8; */
			    }
			logger.debug("FileType ...[" + fileType + "]");
			if (fileType.equals("HTML") || fileType.equals("EMPTY") || fileType.equals("")) {			
				logger.debug("[process] Getting body...");

				 String bodyPagina = cm.getBodyPage();
				 bodyPagina = bodyPagina.replaceAll("&nbsp;", " ");
				 bodyPagina = bodyPagina.replace((char)0x92, '\''); // Treu apostrofs 
				 // Stats.addBytes(bodyPagina.length(), System.currentTimeMillis());					 
				
				// Cas especial tràmits CAT365

				
				logger.debug("[process] Length pagina " + bodyPagina.length());
			//	SaxHTMLParser saxHTML = new SaxHTMLParser();
				logger.debug("[process] Parsejant......");
				NewHtmlParser myParser = new NewHtmlParser();
			//	saxHTML.parse (bodyPagina, link.getUrlLink(), crawlerConfig.nameSeed, identificadorIdioma, checkSimilarity);
		//		logger.debug("[process] Parsejant 2......" + link.getUrlLink());
				myParser.parse(bodyPagina, urlLink);
				logger.debug("[process] Parsejat......");
 			//    doc = myParser.getDocument();
			    }
			} else {
          // Binary 
			}		
	    } catch (Exception e) {
	    logger.error(e);	   
	    }
	  return codiHttp;			
	}

	/**
	 * Retorna el tipus del link (pdf / word / excel...)
	 * 
	 * @param urlLink 
	 * @return
	 */
	
	public String identifyType (String urlLink) {
		String typeFile = "EMPTY";
		try {
			urlLink = urlLink.toLowerCase();
			urlLink = urlLink.trim();
			if (urlLink.endsWith(".pdf")) {
				typeFile = "PDF"; 
			}
			if (urlLink.endsWith(".doc")) {
				typeFile = "WORD"; 
			}
			
			if (urlLink.endsWith(".xls")) {
				typeFile = "EXCEL"; 
			}
			
			if (urlLink.endsWith(".ppt")) {
				typeFile = "POWERPOINT"; 
			}

			if (urlLink.endsWith(".sxi")) {
				typeFile = "OPENOFFICE_PRESENTATION";
			}

			if (urlLink.endsWith(".sxc")) {
				typeFile = "OPENOFFICE_SPREADSHEET";
			}
		
			if (urlLink.endsWith(".sxw")) {
				typeFile = "OPENOFFICE_TEXT";
			}
			
			if (urlLink.endsWith(".html") || urlLink.endsWith(".htm")) {
				typeFile = "HTML"; 
			}

			if (urlLink.endsWith(".txt")) {
				typeFile = "TXT"; 
			}
						
			if (urlLink.endsWith("/")) {
				typeFile = "EMPTY"; 
			}
			
			if (urlLink.lastIndexOf("/") < 8) {
				typeFile = "EMPTY";				
			}

		} catch (Exception e) {
			logger.error("[identifyType] " + e);	
		}
		return typeFile;
	}
	
	
	
}