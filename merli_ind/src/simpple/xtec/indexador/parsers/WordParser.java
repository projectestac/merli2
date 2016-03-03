package simpple.xtec.indexador.parsers;

/*
 * Created on 21/02/2005
 *
 * Fem servir les llibreries de TextMining.org per:
 * 		suport desde word6.0
 * 		facilitat de us
 * i a pesar de 
 * 		falta extrema de documentació 
 **/



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hpsf.MarkUnsupportedException;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.Section;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;

import org.textmining.text.extraction.WordExtractor;

// import simpple.sise.db.DocumentToIndex;
import org.apache.poi.util.HexDump;


/**
 * @author pere
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class WordParser implements ParserInterface {

    private String text = null;
    private String url = null;
    private  String titul = null;
    private  String keywords= null;
    private  String data = null; // de fecha, no de dades!!!
    private  String autor= null;
    
    private boolean errors = false;
    private String errorString = "";    

    
    static class MyPOIFSReaderListener2 implements POIFSReaderListener
    {
    	  static String hex(final byte[] bytes)
    	    {
    	        return HexDump.dump(bytes, 0L, 0);
    	    }

    	    	
        public void processPOIFSReaderEvent(final POIFSReaderEvent event)
        {
            PropertySet ps = null;
            try
            {
                ps = PropertySetFactory.create(event.getStream());
            }
            catch (NoPropertySetStreamException ex)
            {
                return;
            }
            catch (Exception ex)
            {
                throw new RuntimeException
                    ("Property set stream \"" +
                     event.getPath() + event.getName() + "\": " + ex);
            }




            /* Print the list of sections: */
            List sections = ps.getSections();

            for (Iterator i = sections.iterator(); i.hasNext();)
            {
                /* Print a single section: */
                Section sec = (Section) i.next();

                String s = hex(sec.getFormatID().getBytes());
                s = s.substring(0, s.length() - 1);



                /* Print the properties: */
            }
        }
    }    
    
    class MyPOIFSReaderListener implements POIFSReaderListener
    {
  	   String hex(final byte[] bytes)
	    {
	        return HexDump.dump(bytes, 0L, 0);
	    }
    	
        public void processPOIFSReaderEvent(POIFSReaderEvent event)
        {

            SummaryInformation si = null;
            try
            {

                     si = (SummaryInformation)PropertySetFactory.create(event.getStream());

                     
                     
                     List mySections =  si.getSections();
                     
                     int j = 0;
                     while (j < mySections.size()) {
                    	Section mySection =  (Section)mySections.get(j);

                    	 String s = hex(mySection.getFormatID().getBytes());
                    	 s = s.substring(0, s.length() - 1);


                    	 /* Print the properties: */
                    	j ++;  
                        }
                     titul = si.getTitle();

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
                  }
              } 
         }
    
	//	Logger 	
	static Logger logger = Logger.getLogger(simpple.xtec.indexador.parsers.WordParser.class);
    


    
    
    public WordParser (String nom, String url) {
        File f = new File(nom);
   //     XFile f = new XFile(nom);
        InputStream is = null;
        this.url = url;
        
        try {
//            is = new XFileInputStream(f);
        	
            is = new FileInputStream(f);        	

            text = new WordExtractor().extractText(is);

            //Les 4 seguents linees treuen caracters basura
            // no trec el caracter 10 pq es el salt de linea, pero es podria fer
            // no crec q per indexar importi
            for (int i=1; i< 10 ; i++) {
            	 text = text.replace((char)i,' ');
            	 }
            for (int i=11; i< 31 ; i++) {
            	text = text.replace((char)i,' ');
                }
            text = text.replace((char)8194,' ');
            text = text.replace((char)8217,'\'');

     logger.debug("POIReader");            
            // Llegim les propietats fent servir POI, es una mica complexe,
            // pero q es pot esperar de un objecte/format que es diu "Horrible Propierty Set Format"
            POIFSReader r = new POIFSReader();
            logger.debug("Listener");            
            r.registerListener(new MyPOIFSReaderListener(),"\005SummaryInformation");
//            r.registerListener(new MyPOIFSReaderListener());
            is.close();  // mirar si es pot fer sense tancar i obrir
//            is = new XFileInputStream(f);
            is = new FileInputStream(f);
            logger.debug("Reading");            
            r.read(is);
            logger.debug("Readed");
            if ((titul == null) || titul.trim().equals("")) {
                int lastIndex = url.lastIndexOf("/");
                titul = url.substring(lastIndex + 1, url.length());             
            }
        } catch (FileNotFoundException e) {
        	errorString = "No es pot trobar el fitxer de word "+ nom + "de la url "+ url;
            logger.error("No es pot trobar el fitxer de word "+ nom + "de la url "+ url);
            errors = true;
            e.printStackTrace();
        } catch (MarkUnsupportedException e) {
        	errorString = "No es pot extreure les metadades del fitxer de word "+ nom + "a la url "+ url;
            logger.error("No es pot extreure les metadades del fitxer de word "+ nom + "a la url "+ url);
            errors = true;
            e.printStackTrace();
        } catch (Exception e) {
        	logger.error(e);
        	errorString = "No es pot extreure la informacio del fitxer de word "+ nom + "a la url "+ url;
            logger.error("No es pot extreure la informacio del fitxer de word "+ nom + "a la url "+ url);
            errors = true;
            e.printStackTrace();
        } finally {
        try {
           if (is != null) {
              is.close();
              }
           } catch (Exception e) {

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
        if (titul != null) {
        	titul = titul.replaceAll("","'");
            }
    	return titul;
    }

    public String getAutor() {
            return autor;
    }

    public String getDate() {
        return data;
    }

    public String getKeywords() {
        return keywords;
    }

    public boolean hasErrors () {
    	return errors;    	
    }

    public String getStringErrors () {
    	return errorString;    	
    }
    
/*    
    public DocumentToIndex getDocument () {
    	DocumentToIndex d = null;
        try {
        	d = new DocumentToIndex();

        	d.text = getText();
        	d.url = url;
        	d.title = getTitle();
        	d.keywords = keywords;  
        	d.author = autor;
        	d.data = data;
        	d.fileType = 2; // fileType = WORD
        } catch (Exception e) {
        logger.error(e);	
        }
        return d;
    }
  */  
}
