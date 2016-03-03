package simpple.xtec.indexador.parsers;

/*
 * Created on 23/02/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

// import simpple.sise.db.DocumentToIndex;

/**
 * @author pere 
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ExcelParser implements ParserInterface {
	
	private String text = null;
	private String url = null;
	private  String titol = null;
	private  String keywords= null;
	private  String data = null; // de fecha, no de dades!!!
	private  String autor= null;
	
	private int maxRows = 100000;
	private int maxCells = 100000;
	
	private boolean errors = false;
	private String errorString = "";    
	
	
	class MyPOIFSReaderListener implements POIFSReaderListener
	{
		public void processPOIFSReaderEvent(POIFSReaderEvent event)
		{
			SummaryInformation si = null;
			try
			{
				si = (SummaryInformation)PropertySetFactory.create(event.getStream());
			}
			catch (Exception ex)
			{
				throw new RuntimeException
				("Property set stream \"" +
						event.getPath() + event.getName() + "\": " + ex);
			}
			titol = si.getTitle();
			keywords= si.getKeywords();
			Date d =si.getLastSaveDateTime();
			if (d != null)data = d.toString();else data= null;
			autor = si.getAuthor();
			
			
		}
	}
	
	//	Logger 	
	static Logger logger = Logger.getLogger(simpple.xtec.indexador.parsers.ExcelParser.class);
	
	
	
	//	Constructor
	public ExcelParser (InputStream in, String url) {
		int numRows = 0;
		int numCells = 0;    	
		try {
			POIFSFileSystem fs = new POIFSFileSystem( in );
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);

			// Iterate over each row in the sheet
			Iterator rows = sheet.rowIterator();
			
			text="";
			while( rows.hasNext() && (numRows < maxRows)) {
				HSSFRow row = (HSSFRow) rows.next();               
				// Iterate over each cell in the row and print out the cell's content
				Iterator cells = row.cellIterator();
				
				while( cells.hasNext() && (numCells < maxCells)) {
					HSSFCell cell = (HSSFCell) cells.next();
					
					switch ( cell.getCellType() ) {
					// Tan sols guardem el texte, el numeros no tenen sentit per a buscar
					case HSSFCell.CELL_TYPE_STRING:
						text+=cell.getStringCellValue()+" ";
					
					break;
					default:
						
						break;
					
					} // end switch

					numCells ++;
				}
				numRows ++;
			} // acabem el bucle per recorrer files i columnes
			
			// Llegim les propietats fent servir POI, es una mica complexe,
			// pero q es pot esperar de un objecte/format que es diu "Horrible Propierty Set Format"
			POIFSReader r = new POIFSReader();
			r.registerListener(new MyPOIFSReaderListener(),"\005SummaryInformation");
			in.close();  // mirar si es pot fer sense tancar i obrir
			//          is = new XFileInputStream(f);
			r.read(in);
			
		} catch ( IOException ex ) {
			errors = true;  
			ex.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();	
				}		
			} catch (Exception e) {

			}	
		}	   
	}   
	
	
	//	Constructor
	public ExcelParser (String nom, String url) {
		File f = new File(nom);
		//        XFile f = new XFile(nom);
		InputStream is = null;
		this.url=url;
		int numRows = 0;
		int numCells = 0;    	
		
		try {
			//            is = new XFileInputStream(nom);
			is = new FileInputStream(nom);
			POIFSFileSystem fs = new POIFSFileSystem( is );
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			
			// Iterate over each row in the sheet
			Iterator rows = sheet.rowIterator();
			
			text="";
			while( rows.hasNext() && (numRows < maxRows)) {          
				HSSFRow row = (HSSFRow) rows.next();               
				// Iterate over each cell in the row and print out the cell's content
				Iterator cells = row.cellIterator();
				
				while( cells.hasNext() && (numCells < maxCells)) {
					HSSFCell cell = (HSSFCell) cells.next();
					
					switch ( cell.getCellType() ) {
					// Tan sols guardem el texte, el numeros no tenen sentit per a buscar
					case HSSFCell.CELL_TYPE_STRING:
						text+=cell.getStringCellValue()+" ";
					
					break;
					default:
						
						break;
					
					} // end switch

				numCells ++;
				}
			numRows ++;	
			} // acabem el bucle per recorrer files i columnes
			
			// Llegim les propietats fent servir POI, es una mica complexe,
			// pero q es pot esperar de un objecte/format que es diu "Horrible Propierty Set Format"
			POIFSReader r = new POIFSReader();
			r.registerListener(new MyPOIFSReaderListener(),"\005SummaryInformation");
			is.close();  // mirar si es pot fer sense tancar i obrir
			//            is = new XFileInputStream(f);
			is = new FileInputStream(f);
			r.read(is);
			
		} catch ( IOException ex ) {
			ex.printStackTrace();
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
	
/*	public DocumentToIndex getDocument () {
		DocumentToIndex d = null;
		try {
			d = new DocumentToIndex();        	
			d.text = getText();
			d.url = url;
			d.title = getTitle();
			d.keywords = keywords;   
			d.author = autor;
			d.data = data;
			d.fileType = 3; // fileType = EXCEL        	
		} catch (Exception e) {
			logger.error(e);	
		}
		return d;
	}
	*/
}
