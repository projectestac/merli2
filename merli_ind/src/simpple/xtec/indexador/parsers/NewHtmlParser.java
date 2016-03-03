package simpple.xtec.indexador.parsers;

import java.util.*;
import java.io.*;
import java.net.*;

import org.apache.log4j.Logger;

import simpple.xtec.indexador.util.UrlUtility;



/*import simpple.sise.crawler.CrawlerConfig;
import simpple.sise.crawler.UtilsCrawlerNew;
import simpple.sise.db.DocumentToIndex;
import simpple.sise.modules.IdentificadorIdioma;
import simpple.sise.modules.SimilarityManager;
import simpple.util.ColeccioObject;
import simpple.educacio.util.URLUtility;
*/
import au.id.jericho.lib.html.CharacterReference;
import au.id.jericho.lib.html.Element;
import au.id.jericho.lib.html.HTMLElementName;
import au.id.jericho.lib.html.Segment;
import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;



public class NewHtmlParser {
	
	static Logger logger = Logger.getLogger(simpple.xtec.indexador.parsers.NewHtmlParser.class);
	
	String url = "";
	String title = "";
	String description = "";
	String keywords = "";
	String text = "";
	int idioma = 0;
	String coleccions = "";
	String baseHref = "";
	public String textLinks = "";
	
	String nameSeed = "";
	
	Hashtable urlsInfo = null;
	Hashtable urlsInfoFrames = null;	
    Hashtable metaElementsInfo = null;	
    
    String textToParseJavascript = "";
	
	public NewHtmlParser() {
		urlsInfo = new Hashtable();
		urlsInfoFrames = new Hashtable();
		metaElementsInfo = new Hashtable();
	    }
	
	public Hashtable getUrlsInfo () {
	    return urlsInfo;
	    }

	public Hashtable getUrlsInfoFrames () {
	    return urlsInfoFrames;
	    }

	public Hashtable getMetaElements () {
	    return metaElementsInfo;
	    }

	
/*	public DocumentToIndex getDocument () {
        DocumentToIndex d = null;	
        try {
        	 d = new DocumentToIndex();
             d.url = url;
        	 d.title = title;
        	 d.text = text;
        	 d.description = description;
        	 d.keywords = keywords;
        	 d.idioma = idioma;
        	 d.coleccions = coleccions;
          d.fileType = 0;  // Document HTML
          } catch (Exception e) {
          logger.error(e);	
          }
        return d;  
     }	
*/	
	public void parse (String htmlText, String url) throws Exception {
	    this.url = url;
	    try {
	    baseHref = getBaseHref(url);
		Source source=new Source(htmlText);
		// source.setLogWriter(new OutputStreamWriter(System.err)); // send log messages to stderr

		source.fullSequentialParse();

        if (url.toLowerCase().endsWith(".js")) {
        	logger.debug("Adding JS...." + htmlText);
        	 textToParseJavascript += htmlText;
             }
		title=getTitle(source);



		description=getMetaValue(source,"description");



		keywords=getMetaValue(source,"keywords");

	


		List scriptElements=source.findAllElements(HTMLElementName.SCRIPT);
		for (Iterator i=scriptElements.iterator(); i.hasNext();) {

			Element scriptElement=(Element)i.next();
		//	System.out.println(scriptElement.toString());
		    textToParseJavascript += scriptElement.toString();
		    }
		
		Element bodyElement=source.findNextElement(0,HTMLElementName.BODY);
		Segment contentSegment=(bodyElement==null) ? source : bodyElement.getContent();
				

		text = contentSegment.extractText(false);
		// Remove links text
        int indexIni = 0;

		List metaElements=source.findAllElements(HTMLElementName.META);        
		for (Iterator i=metaElements.iterator(); i.hasNext();) {
			Element metaElement=(Element)i.next();
			String httpequiv=metaElement.getAttributeValue("HTTP-EQUIV");
			if (httpequiv!=null) {		
				 if (httpequiv.equalsIgnoreCase("refresh")) {
				   String content=metaElement.getAttributeValue("content");
				   if (content != null) {
					  int indexOf = content.indexOf("=");
					  String urlRedirect = content.substring(indexOf + 1, content.length());
					  urlRedirect = tractaHref (urlRedirect, baseHref);
				      if (urlRedirect != null) {
						  urlsInfoFrames.put(urlRedirect, "");
					      }

				      }
				    } 
				   }

			String name=metaElement.getAttributeValue("name");
			if (name!=null) {			
			   String content=metaElement.getAttributeValue("content");
			   if (content != null) {
  			      metaElementsInfo.put(name, content);
			      }
			   }
		    }
		
		
		List linkElements=source.findAllElements(HTMLElementName.A);
		for (Iterator i=linkElements.iterator(); i.hasNext();) {
			Element linkElement=(Element)i.next();
			String href=linkElement.getAttributeValue("href");
			if (href==null) continue;
			// A element can contain other tag s so need to extract the text from it:
			String label=linkElement.getContent().extractText();

	       	 href = tractaHref (href, baseHref);

	       	 if (href != null) {
	       		textLinks += label + " ";
			    
			    if (href.toLowerCase().indexOf("javascript") != -1) {			    
			       textToParseJavascript += href + " ";
			       } else {
			       urlsInfo.put(href, label);	   
			       }
	       	    }
 
	       // System.out.println("Label --> " + label);
            if (!label.trim().equals("")) {
              indexIni = text.indexOf(label, indexIni);
              if (indexIni > -1) {
			     text = text.substring(0, indexIni) + text.substring( (indexIni + label.length()), text.length());
                 }
              }
		} 

		List areaElements=source.findAllElements(HTMLElementName.AREA);
		for (Iterator i=areaElements.iterator(); i.hasNext();) {
			Element areaElement=(Element)i.next();
			String href=areaElement.getAttributeValue("href");
			if (href==null) continue;
			// A element can contain other tag s so need to extract the text from it:
			String label=areaElement.getContent().extractText();

	       	 href = tractaHref (href, baseHref);

	       	 if (href != null) {
		       	textLinks += label + " ";	       		 

			    if (href.toLowerCase().indexOf("javascript") != -1) {
			        textToParseJavascript += href + " ";
			        } else {
					urlsInfo.put(href, label);			        	
			        }
	       	    }
 
	       // System.out.println("Label --> " + label);
            if (!label.trim().equals("")) {
              indexIni = text.indexOf(label, indexIni);
              if (indexIni > -1) {
			     text = text.substring(0, indexIni) + text.substring( (indexIni + label.length()), text.length());
                 }
              }
		} 
		
		
		List frameElements=source.findAllElements(HTMLElementName.FRAME);
		for (Iterator i=frameElements.iterator(); i.hasNext();) {
			Element linkElement=(Element)i.next();
			String href=linkElement.getAttributeValue("src");
			if (href==null) continue;
			// A element can contain other tag s so need to extract the text from it:
			String label=linkElement.getContent().extractText();

	       	 href = tractaHref (href, baseHref);

	       	 if (href != null) {
		       	textLinks += label + " ";	       		 
	       		urlsInfoFrames.put(href, label);
	       	    }

            if (!label.trim().equals("")) {
              indexIni = text.indexOf(label, indexIni);
              if (indexIni > -1) {
			     text = text.substring(0, indexIni) + text.substring( (indexIni + label.length()), text.length());
                 }
              }
		} 

		List iFrameElements=source.findAllElements(HTMLElementName.IFRAME);
		for (Iterator i=iFrameElements.iterator(); i.hasNext();) {
			Element linkElement=(Element)i.next();
			String href=linkElement.getAttributeValue("src");
			if (href==null) continue;
			// A element can contain other tag s so need to extract the text from it:
			String label=linkElement.getContent().extractText();

	       	 href = tractaHref (href, baseHref);

	       	 if (href != null) {
		       	textLinks += label + " ";	       		 
	       		urlsInfoFrames.put(href, label);
	       	    }

            if (!label.trim().equals("")) {
              indexIni = text.indexOf(label, indexIni);
              if (indexIni > -1) {
			     text = text.substring(0, indexIni) + text.substring( (indexIni + label.length()), text.length());
                 }
              }
		} 
		
		

	  //  System.out.println("js: " + textToParseJavascript);
	   
		// String[] links = textToParseJavascript.split("javascript:*('(f|h|/)*'*)");
/*		ArrayList linksJS = parseJS (textToParseJavascript); 
        int i = 0;
        while (i < linksJS.size()) {
        	urlsInfo.put((String)linksJS.get(i), "");
        	i ++;
            }
	*/	
	    


	    } catch (Exception e) {
	    logger.error(e);	
	    }
  }

	public boolean hasStopWordJS (String text) {
		boolean banned = false;
		try {
		  if (text.toLowerCase().indexOf("script") != -1) {
			 return true; 
		     }
		  if (text.toLowerCase().indexOf("function") != -1) {
			 return true; 
		     }
		  if (text.toLowerCase().indexOf("javascript") != -1) {
			 return true; 
		     }
		  if (text.toLowerCase().indexOf("document.") != -1) {
			 return true; 
		     }
		  if (text.toLowerCase().indexOf("window.") != -1) {
			 return true; 
		     }
		  if (text.toLowerCase().indexOf("style.") != -1) {
			 return true; 
		     }
	/*	  if (text.toLowerCase().indexOf(";") != -1) {
			 return true; 
		     }*/
		  if (text.toLowerCase().indexOf("<") != -1) {
			 return true; 
		     }
		  if (text.toLowerCase().indexOf(">") != -1) {
			 return true; 
		     }

		  } catch (Exception e) {
		  logger.error(e);	
		  }
		return banned; 
	}
	
    public ArrayList parseJS (String textJS) {
    	ArrayList allLinks = null;
    	String[] links = null;
    	String testLink = "";
    	int i = 0;
    	
    	// script , function, javascript, document.
    	try {
    	   allLinks = new ArrayList();	
    	   links = textJS.split("'*'");
   	       while (i < links.length) {
    
                 testLink = links[i].toLowerCase().trim();
                 if ((testLink.length() < 320) && (testLink.length() > 5) && !hasStopWordJS(testLink) && (testLink.indexOf(".") != -1)) {
                	allLinks.add(tractaHref (links[i], baseHref));
                    }
	    	     i ++;
	             }
   	       links = textToParseJavascript.split("\"*\"");
   	       i = 0;
  	       while (i < links.length) {

  	    	   
  	    	   testLink = links[i].toLowerCase().trim();;
               if ((testLink.length() < 120) && (testLink.length() > 5) && !hasStopWordJS(testLink) && (testLink.indexOf(".") != -1)) {
               	 allLinks.add(tractaHref (links[i], baseHref)); 	    	     
                 }
 	    	 i ++;
 	         }        	         	       
   	       
    	} catch (Exception e) {
    	logger.error(e);	
    	}
    return allLinks;	
    }
	
	
	
	private static String getTitle(Source source) {
		Element titleElement=source.findNextElement(0,HTMLElementName.TITLE);
		if (titleElement==null) return null;
		// TITLE element never contains other tags so just decode it collapsing whitespace:
		return CharacterReference.decodeCollapseWhiteSpace(titleElement.getContent());
	}

	private static String getMetaValue(Source source, String key) {
		for (int pos=0; pos<source.length();) {
			StartTag startTag=source.findNextStartTag(pos,"name",key,false);
			if (startTag==null) return null;
			if (startTag.getName()==HTMLElementName.META)
				return startTag.getAttributeValue("content"); // Attribute values are automatically decoded
			pos=startTag.getEnd();
		}
		return null;
	}
	
	public String getBaseHref (String urlHref) {
		String baseHref = "";

		int lastIndex = 0;

		try {

			URL myUrl = new URL(urlHref);

			String fileName = myUrl.getFile();
			// logger.debug("Filename --> " + fileName);			
			if (fileName.indexOf("/") == -1) {
				int indexOf = urlHref.indexOf(fileName);
				if (indexOf != 0) {
					baseHref = urlHref.substring(0, indexOf);
				} else {
					baseHref = urlHref;
				}
			} else {
				baseHref = urlHref;	
			}
		} catch (Exception e) {
			logger.error("[getBaseHref] " + e);
			logger.error("  Base Href: " + baseHref);
			logger.error("  Url Href: " + urlHref);
			logger.error("  Last index: " + lastIndex);         
		}
		//logger.debug("Base href --> " + baseHref);
		return baseHref;
	}

	public String tractaHref (String urlHref, String baseHref) {
		String tempHref = "";
		try {
			tempHref = urlHref;
			tempHref = tempHref.trim();
/*			 if (urlHref.indexOf("obrepag") != -1) {
logger.debug("Base href: " + baseHref);
logger.debug("url href: " + urlHref);
			 }*/
			if ((tempHref.indexOf("OpenDocument") != -1) && (tempHref.indexOf("javascript") == -1)) {
			}
			if (tempHref.startsWith("./")) {

			   tempHref = tempHref.substring(2, tempHref.length());


			   }
			if (!tempHref.toLowerCase().startsWith("www") && !tempHref.toLowerCase().startsWith("http")) {             
				try {
				     tempHref = new URL(UrlUtility.resolveBase(baseHref, tempHref)).toString();
				     // Fix URL's like http://www.urv.net/../serveis_universitaris/borsa_de_treball/btreball_practiques/Pract%2060-06.html
				     
				     int indexOf = tempHref.indexOf("/../");
				     while (indexOf != -1) {
                  //      tempHref = tempHref.replace("/../", "/");
				        tempHref = tempHref.replaceAll("/../", "/");
                        indexOf = tempHref.indexOf("/../");
				        }
				     if ((tempHref.indexOf("obrepag") != -1) && (tempHref.indexOf("ordireglaments") != -1)) {
				    	 logger.debug("*****: " +tempHref);
				     }
				} catch (Exception e) {				
					logger.error("[tractaHref] " + e);
					logger.error("[tractaHref] urlHref: " + urlHref);
					logger.error("[tractaHref] baseHref: " + baseHref);					
					logger.error("[tractaHref] tempHref:" + tempHref);		
				}
				
			}   

		
		} catch (Exception e) {
			logger.error("[tractaHref] " + e);
			logger.error("[tractaHref] " + tempHref);			
		}
		if ((tempHref.indexOf("OpenDocument") != -1)&& (tempHref.indexOf("javascript") == -1)) {


			}

		return tempHref;
	}

	public boolean isAuthorizedHost (String nextUrl, ArrayList allHosts) {
		try {
			nextUrl = nextUrl.trim();

			if ( (allHosts == null) || (allHosts.size() == 0) ) {
				return true;
			}

	/*		if (!nextUrl.startsWith("http") && !nextUrl.startsWith("www")){
				return true;
			}*/
			int i = 0;

			while (i < allHosts.size()){
				String tempHost = (String)allHosts.get(i);
				tempHost = tempHost.toLowerCase();							
				if (nextUrl.indexOf(tempHost) != -1) {
					return true;
				}
				i ++;	
			}     	    		
		} catch (Exception e) {
			logger.error("[isAuthorizedHost] " + e);
		} 

		return false;
	}			
	
	
}