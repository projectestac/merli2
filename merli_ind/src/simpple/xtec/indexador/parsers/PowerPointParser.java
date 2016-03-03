package simpple.xtec.indexador.parsers;

/*
 * Created on 23/02/2005
 *
 *Pere: Estem a 24/02/05 i faig servir una clase creada per Nick Burch el 21/2/05
 *		NO espereu una estabilitat extrema, pero funciona.
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.DocumentInputStream;

// import simpple.sise.db.DocumentToIndex;



/**
 * @author pere
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PowerPointParser implements ParserInterface {

    private String text = null;
    private String url = null;
    private  String titol = null;
    private  String keywords= null;
    private  String data = null; // de fecha, no de dades!!!
    private  String autor= null;

    private boolean errors = false;
    private String errorString = "";    
    
    
    class MyPOIFSReaderListener implements POIFSReaderListener
    {
        public void processPOIFSReaderEvent(POIFSReaderEvent event)
        {
            SummaryInformation si = null;
            DocumentInputStream dis = null;
			dis = event.getStream();
            try {
                     si = (SummaryInformation)PropertySetFactory.create(event.getStream());
            titol = si.getTitle();
            keywords= si.getKeywords();
            Date d =si.getLastSaveDateTime();
            if (d != null) {
            	data = d.toString();
                } else { 
                data= null;
                }
            autor = si.getAuthor();
            } catch (Exception ex) {
                throw new RuntimeException
                    ("Property set stream \"" +
                     event.getPath() + event.getName() + "\": " + ex);
            } finally {
            try {
              if (dis != null) {
              	 dis.close();
                 }	
              } catch (Exception e) {
              logger.error(e);	
              }	
            }	
                        
        }
    }
    
	//	Logger 	
	static Logger logger = Logger.getLogger(simpple.xtec.indexador.parsers.PowerPointParser.class);

    public PowerPointParser (InputStream in, String url) {
    	try {
            POIFSReader r = new POIFSReader();
            r.registerListener(new MyPOIFSReaderListener(),"\005SummaryInformation");
            r.read(in);
            in.close();
       	
         } catch (Exception e) {
         logger.error("[Constructor] " + e);	
         }
       }
    //	Constructor
    public PowerPointParser (String nom, String url) {
        InputStream is = null;
        this.url=url;
        PowerPointExtractor ppe = null;
        try {
//            is = new XFileInputStream(nom);
            is = new FileInputStream(nom);        	
            
            POIFSReader r = new POIFSReader();
            r.registerListener(new MyPOIFSReaderListener(),"\005SummaryInformation");
            r.read(is);
        //    is.close();
            ppe = new PowerPointExtractor(nom);
        	text = ppe.getText(true,true);
            for (int i=1; i< 10 ; i++)text = text.replace((char)i,' ');
            for (int i=11; i< 31 ; i++)text = text.replace((char)i,' ');
            text = text.replace((char)8194,' ');
            text = text.replace((char)8217,'\'');
       // 	ppe.close();

            
        } catch ( IOException ex ) {
        	 errors = true;  
        	 errorString = "IOException";
        	logger.error("[Constructor(IO)] " + ex + "--->" + url);	
            ex.printStackTrace();
        } catch (Exception e ) {
        	 errors = true;  
        	 errorString = "Exception";
        	logger.error("[Constructor] " + e + "--->" + url);
        } finally {
        try {
           if (is != null) {
              is.close();
              }
           if (ppe != null) {
             	ppe.close();
             }
           } catch (Exception e) {
           	logger.error("[Constructor closing] " + "--->" + e + url);
           }		
        }
 }
    
    
    
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
        	d.url = url;
        	d.title = getTitle();
        	d.keywords = keywords;
        	d.text = getText();   
        	d.author = autor;
        	d.data = data;
        	d.fileType = 4; // fileType = POWERPOINT        	
        } catch (Exception e) {
        logger.error(e);	
        }
        return d;
    }
  */  
}    

