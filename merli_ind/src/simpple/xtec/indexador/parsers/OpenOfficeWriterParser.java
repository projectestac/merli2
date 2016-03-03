package simpple.xtec.indexador.parsers;

/*
 * Created on 24/02/2005
 *
 *  Requereix afegir xml-apis.jar, de Xerxes-j, (el XerxesImpl.jar ja hi era)
 * 
 * J'ha esta fet!!!! -> S'haura de modificar pq tregui ell heading, paragarf etc en l'ordre del document, ara agrupa tot
 *  els paragrafs junts i els treu, despres els headings, i despres els elems text:s (especials).
 * Es pot canviar a SAX (continuar les funcions de avall), o fer un recorregut amb DOM per tot el arbre.
 *
 * ATENCIO: La validació esta deshabilitada!!!
 * 
 *  Si es vol fer validacio es requereix els seguents arxius::
 * 			- office.dtd (Document Type Definition) per a coneixer l'estuctura del document
 * 			- chart.mod
 * 			- datastyl.mod
 * 			- defs.mod
 * 			- drawing.mod
 * 			- dtypes.mod 
 * 			- meta.mod
 * 			- nmspace.mod
 * 			- office.mod
 * 			- script.mod
 * 			- settings.mod
 * 			- style.mod
 * 			- table.mod
 * 			- text.mod
 * Es poden copiar de la instalació del openoffice, en el meu cas esta a 
 * 
 * /usr/lib/openoffice/share/dtd/officedocument/1_0/settings.mod
 * 
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.zip.ZipFile;

//import javax.swing.text.Document;

import org.apache.log4j.Logger;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.apache.xerces.dom.TextImpl;

// import simpple.sise.db.DocumentToIndex;
/**
 * @author pere
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OpenOfficeWriterParser implements ParserInterface {
    private String text = null;
    private String nom = null;
    private String url = null;
    private  String titol = null;
    private  String keywords= null;
    private  String data = null; // de fecha, no de dades!!!
    private  String autor= null;

    private boolean errors = false;
    private String errorString = "";    
    
    
    public class  NullContentException extends Exception {
  
		private static final long serialVersionUID = 1L;

		public NullContentException (String s){
            logger.error(s);
        }
    }
    
    public class  NoPropertiesException extends Exception {
  
		private static final long serialVersionUID = 1L;

		public NoPropertiesException (String s){
            logger.error(s);
        }
    }

	//	Logger 	
	static Logger logger = Logger.getLogger(simpple.xtec.indexador.parsers.OpenOfficeWriterParser.class);
    
    
    public OpenOfficeWriterParser (String nom, String url)  {
        ZipFile f =null;
        try {
            this.nom = nom;
            this.url = url;
            //Abrimos el fichero, todos los de openoffice se comprimen en zip
            logger.debug("Obrint fitxer.... " + url);

            f = new ZipFile(nom);
            logger.debug("Llegint contingut.... ");            

            readContent(f); // Lee el texto
            logger.debug("Llegint propietats.... ");            

            readProperties(f);
            logger.debug("Fi de parser.... ");

         } catch (IOException e) {
         	errors = true;
         	errorString = "IOException";
            System.err.println("Error al abrir como zip el fichero "+ nom);
            e.printStackTrace();
        } catch (NullContentException n) {
         	errors = true; 
         	errorString = "NullContentException";
            n.printStackTrace();            
        } catch (NoPropertiesException e) {
         	errors = true;
         	errorString = "NoPropertiesException";         	
            e.printStackTrace();
        } catch (Exception e) {
         	errors = true;
         	errorString = "Exception";         	
            e.printStackTrace();            
        } finally {
            try {
                f.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        
    }
 
    private void readProperties( ZipFile inZip ) throws NoPropertiesException {
        DOMParser parser = new org.apache.xerces.parsers.DOMParser();
        InputStream in = null;
  
        try {
            //La meta informacio esta a meta.xml
            if ( ( in = inZip.getInputStream(inZip.getEntry("meta.xml"))) == null) 
                throw new NoPropertiesException("No se encuentra el meta.xml, seguro que el fichero "
                        						+ nom + " es un documento de openoffice? ");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoPropertiesException e) {
              throw e;
        }
     
        parser.setEntityResolver(new ResolveOfficeDTD());
        
            try {
                parser.parse(new InputSource(in));
                Document doc = parser.getDocument();
                NodeList dataNL = doc.getElementsByTagName("dc:date");
                NodeList titleNL = doc.getElementsByTagName("dc:title");
                NodeList keywordsNL = doc.getElementsByTagName("meta:keyword");
                NodeList autorNL = doc.getElementsByTagName("dc:creator");
                
                if (dataNL.getLength() > 0) {
                    NodeList nl = dataNL.item(0).getChildNodes();
                    Node n = nl.item(0);

                    data=n.getNodeValue();
                }
                if (titleNL.getLength() > 0) {
                    NodeList nl = titleNL.item(0).getChildNodes();
                    Node n = nl.item(0);

                    titol=n.getNodeValue();
                }

                //Pot haberhi mes de un keyword, si ho separen per comes
                // o només un si ho separen per espais
                if( 0 < keywordsNL.getLength())keywords="";
                for (int i=0; i< keywordsNL.getLength(); i++) {
                    
                    NodeList nl=keywordsNL.item(i).getChildNodes();
                    Node n=nl.item(0);

                    keywords += n.getNodeValue()+" ";
                }
                
                if (autorNL.getLength() > 0) {
                    NodeList nl = autorNL.item(0).getChildNodes();
                    Node n = nl.item(0);

                    autor=n.getNodeValue();
                }

                
            } catch (SAXException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } finally {
            try {	
               if (in != null) {
            	  in.close();
                  }
               } catch (Exception e) {
               logger.error(e);	
               }
            }
    }
    
    private void readContent( ZipFile inZip ) throws NullContentException {
            DOMParser parser = new org.apache.xerces.parsers.DOMParser();
            InputStream in = null;

            try {
      //      	logger.debug("Read content 1 ...");            	
                //El texto/grafico/hoja de calculo... esta en el content.xml
                if ( ( in = inZip.getInputStream(inZip.getEntry("content.xml"))) == null) 
                    throw new NullContentException("No se encuentra el content.xml, seguro que el fichero "
                            						+ nom + " es un documento de openoffice? ");
        //    	logger.debug("Read content 2 ...");                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NullContentException e) {
                  throw e;
            }
            
        	// logger.debug("Setting entity resolver ...");
            parser.setEntityResolver(new ResolveOfficeDTD());
        	// logger.debug("Entity resolver set...");            
            
            
            try {
                parser.parse(new InputSource(in));
                Document doc = parser.getDocument();
                traverse(doc);
            } catch (SAXException e1) {
            	   errors = true;  
            	   errorString = "SAXException";
                 e1.printStackTrace();
            } catch (IOException e1) {
            	   errors = true;  
               	   errorString = "IOException";            	   
                 e1.printStackTrace();
            } catch (Exception e1) {
         	   errors = true;  
              errorString = "Exception";            	   
              e1.printStackTrace();
         } finally {
            try {	
                if (in != null) {
             	  in.close();
                   }
                } catch (Exception e) {          
                }        
          }
        
    }
    
    private class ResolveOfficeDTD implements EntityResolver {
        public InputSource resolveEntity (String publicId, String systemId)
        {
            if (systemId.endsWith(".dtd"))
            {
                StringReader stringInput = new StringReader(" ");
                return new InputSource(stringInput);
            }
            else
            {
                return null;    // default behavior
            }
        }
    }
    
    
    public void traverse(Node node) {

        // is there anything to do?
        if (node == null) {
            return;
        }

        int type = node.getNodeType();
      //  logger.debug("Traverse type..." + type);        
        switch (type) {
        // print document
        case Node.DOCUMENT_NODE: {
                traverse(((Document)node).getDocumentElement());
                break;
            }

            // print element with attributes
        case Node.ELEMENT_NODE: {
//                NamedNodeMap attrs = node.getAttributes();
  //              if (attrs != null) {
   //                 attributes += attrs.getLength();
 //               }
                NodeList children = node.getChildNodes();
                if (children != null) {
                    int len = children.getLength();
                    for (int i = 0; i < len; i++) {
                        traverse(children.item(i));
                    }
                }
                break;
            }

            // handle entity reference nodes
        case Node.ENTITY_REFERENCE_NODE: {
                NodeList children = node.getChildNodes();
                if (children != null) {
                    int len = children.getLength();
                    for (int i = 0; i < len; i++) {
                        traverse(children.item(i));
                    }
                }
                break;
            }

            // print text
        case Node.CDATA_SECTION_NODE: {
                //characters += node.getNodeValue();

            	text+=node.getNodeValue()+"\n";
                break;
            }
        case Node.TEXT_NODE: {
                if (node instanceof TextImpl) {
                    if (((TextImpl)node).isIgnorableWhitespace())
                       // ignorableWhitespace += node.getNodeValue().length();

                        text+=node.getNodeValue()+"\n";
                    else
                        //characters += node.getNodeValue().length();

                        text+=node.getNodeValue()+"\n";
                } else

                    //characters += node.getNodeValue().length();
                    text+=node.getNodeValue()+"\n";
                break;
            }
        }

    } // traverse(Node)
    
    
    /*
    private String getContent(ZipFile f) throws NullContentException {
        String ret= null;
        InputStream in =null;
        try {
            if ( (in = f.getInputStream(f.getEntry("content.xml"))) == null) 
                throw new NullContentException("No se encuentra el content.xml, seguro que el fichero "
                        						+ nom + " es un documento de openoffice? ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NullContentException e) {
              throw e;
        }
        try {  
            // Read the response XML document
            XMLReader parser = XMLReaderFactory.createXMLReader(
                    "org.apache.xerces.parsers.SAXParser"
            );
            
            parser.setFeature("http://xml.org/sax/features/validation", false);
            ContentHandler handler = new TextHandler();
    
            parser.setContentHandler(handler);
      
            InputSource source = new InputSource(in);
        
            parser.parse(source);
            in.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SAXException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return ret;
    }
    
    
    private class TextHandler extends DefaultHandler {

        private boolean inText = false;
        
        public void startElement(String namespaceURI, String localName,
         String qualifiedName, Attributes atts) throws SAXException {
          
          if (localName.equals("text:p")) inText = true;
          
        }

        public void endElement(String namespaceURI, String localName,
         String qualifiedName) throws SAXException {
          
          if (localName.equals("text:p")) inText = false;
          
        }

        public void characters(char[] ch, int start, int length)
        throws SAXException {

          if (inText) {
            for (int i = start; i < start+length; i++) {

            }
          }   
         
        }
        
      }
    */
    
    public String getText() {
    	if (text != null) {
        	text = text.replaceAll("","'");
            }        	    	
        return text;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
    	if (titol != null) {
    		titol = titol.replaceAll("","'");
            }        	    	
        return titol;
    }

    public String getAutor() {
        return autor;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getDate() {
        return data;
    }

    public boolean hasErrors () {
    	return errors;    	
    }

    public String getStringErrors () {
    	return errorString;    	
    }    
    
/*    public DocumentToIndex getDocument () {
    	DocumentToIndex d = null;
        try {
        	d = new DocumentToIndex();
        	d.text = getText();
        	d.url = url;
        	d.title = getTitle();
        	d.keywords = keywords;  
        	d.author = autor;
        	d.data = data;
        	d.fileType = 5; // fileType = OPENOFFICE
        } catch (Exception e) {
        logger.error(e);	
        }
        return d;
    }
*/

}
