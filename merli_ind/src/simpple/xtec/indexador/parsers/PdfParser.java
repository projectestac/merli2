package simpple.xtec.indexador.parsers;

/*
 * 28/02/04 Afegeixo Descripcio
 * 
 * Created on 21/02/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author pere
 *
 * Atencion! El obheto PDFTextStripper requiere un fichero de propierties
 * y lo busca en src/Resources/PDFTextStripper.properties, si se genera un error seguramente 
 * es que no encuentra este fichero, ya que por defecto esta fuera del directorio src!!!!
 */




import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;	
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;


import org.apache.log4j.Logger;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.PDDocumentInformation;
import org.pdfbox.util.PDFTextStripper;

// import simpple.sise.db.DocumentToIndex;
// import simpple.util.Base64;

import com.etymon.pj.Pdf;
import com.etymon.pj.object.PjArray;
import com.etymon.pj.object.PjObject;
import com.etymon.pj.object.PjPage;
import com.etymon.pj.object.PjStream;
/*
import com.sun.xfile.*;
*/
/**
 *  <at> author Pere
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments  */
public class PdfParser implements ParserInterface{
	//  private XFileInputStream In;
	private FileInputStream In;
	private PDDocument pdDoc;
	private PDDocumentInformation info;
	private String url = null;
	private String text = null;
	private File f = null;
	
	private boolean errors = false;
	private String errorString = "";    
	
	
	//	Logger 	
	static Logger logger = Logger.getLogger(simpple.xtec.indexador.parsers.PdfParser.class);
	
	
	public PdfParser(InputStream in, String url) {
		this.url=url;
		
	}
	
	public PdfParser(String nom, String url) {
		this.url=url;
		//          	f = new XFile(nom);
		f = new File(nom);
		try {

			//  extractText(f);
			//    System.err.println("Texto acabado");
			//                In = new XFileInputStream(f);
			In = new FileInputStream(f);


			extractInfo(In);


		} catch (FileNotFoundException e) {
			errors = true;  
			errorString = "Fitxer no trobat";

			e.printStackTrace();
		} catch (IOException e) {
			errors = true;  
			errorString = "IOException";
			System.err.println("Error en el constructor de PdfParser");
			e.printStackTrace();
		} catch (Exception e) {
			extractText(f);
			errors = true;  
			errorString = "Exception";
			System.err.println("Error en el constructor de PdfParser");
			e.printStackTrace();                
		} finally {
			try {	
				if (In != null) {
					In.close();
				}	
			} catch (Exception e) {

			}	   
		}	
		
	}
	
	
	

	private void extractText(File f) {


		try {

			text = getContents();
			// text = Base64.encodeBytes(text.getBytes());
			// text = new BASE64Encoder().encode(text.getBytes());

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
			// text = text.replace((char)8217,'\'');
			text = text.replace('\\',' ');
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	private String getContents( ) throws Exception {
		String aux = "";

		try {

			Pdf pdf = new Pdf(f.getPath());

			int pagecount = pdf.getPageCount();

			StringBuffer contents = new StringBuffer();
			for (int i = 1; i <= pagecount; i++) {
				contents.append(getContent(pdf, i) + "\n\n\b");
				try {
					//       Thread.sleep(10);
				} catch (Exception ex) {
					
				}
			}
			aux = contents.toString();
		} catch (Exception ex) {
			throw ex;
		}

		return aux;
	}
	
	
	private static String getContent(Pdf pdf, int pageNo) throws Exception {

		String content = null;
		PjStream stream = null;
		StringBuffer strbf = new StringBuffer();
		try {
			PjPage page = (PjPage) pdf.getObject(pdf.getPage(pageNo));
			PjObject pobj = (PjObject) pdf.resolve(page.getContents());
			if (pobj instanceof PjArray) {
				PjArray array = (PjArray) pobj;
				Vector vArray = array.getVector();
				int size = vArray.size();

				for (int j = 0; j < size; j++) {
					stream = (PjStream) pdf.resolve((PjObject) vArray.get(j));
					strbf.append(getStringFromPjStream(stream));
					try {
					   Thread.sleep(100);
					   } catch (Exception ex) {
					   }
				}
				content = strbf.toString();
			}
			else {
				stream = (PjStream) pobj;
				content = getStringFromPjStream(stream);
			}
		} catch (Exception ex) {
			throw ex;
		}

		return content;
	}
	
	
	private static String getStringFromPjStream(PjStream stream) throws Exception {
		StringBuffer strbf = new StringBuffer();

		try {
			int start = 0;
			int end = 1;
			stream = stream.flateDecompress();
			String longString = new String(stream.toString().getBytes(), "ISO-8859-1");

			int lastIndex = longString.lastIndexOf(')');

			while ( (lastIndex != -1) && ((end + 1) < lastIndex) && (end != start) && (start != -1)) {
				start = longString.indexOf('(', end);
				if (start != -1) {
					end = longString.indexOf(')', start);
					// String text = longString.substring(start + 1, end);
					String text = longString.substring(start + 1, end);
					strbf.append(text);
					try {
						//  Thread.sleep(10);
					} catch (Exception ex) {

					}
				}   
			}

		} catch (Exception ex) {
			throw ex;
		}
		return strbf.toString();
	}
	
	
	
	//private boolean extractInfo(InputStream  reader, BufferedWriter
	//writer) throws IOException{
	
	private boolean extractInfo(InputStream  reader) throws IOException{
		PDFParser parser = null;
		
		//PDFTextStripper stripper = null;
		try {
			parser = new PDFParser(reader);               
			parser.parse();
			
			pdDoc = parser.getPDDocument();
			
			PDFTextStripper stripper = new PDFTextStripper();

			text = stripper.getText(pdDoc);
			// text = Base64.encodeBytes(text.getBytes());
			// text = new BASE64Encoder().encode(text.getBytes());

			info = pdDoc.getDocumentInformation();

		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (pdDoc != null) {
				pdDoc.close();
			}
		}
		return true;
	}
	
	public String getText() {    
		if (text != null) {
			text = text.replaceAll("","'");
		}        	          
		return text;
		
	}
	
	public String getTitle() {
		String title = info.getTitle();
		if ((title != null) && !title.trim().equals("")){
			// title = Base64.encodeBytes(title.getBytes());  
			// title = new BASE64Encoder().encode(title.getBytes());
			title = title.replaceAll("","'");
		} else {
        int lastIndex = url.lastIndexOf("/");
        title = url.substring(lastIndex + 1, url.length());
		}
		return title;
	}
	
	public String getAutor() {
		String author = info.getAuthor();
		if (author != null) {
//			author = Base64.encodeBytes(author.getBytes());
		// 	author = new BASE64Encoder().encode(author.getBytes());			
		}
		return author;
	}
	
	public String getSubject() {
		String subject = info.getSubject();
		if (subject != null) {
	//		subject = Base64.encodeBytes(subject.getBytes());
		//	subject = new BASE64Encoder().encode(subject.getBytes());			
		}
		return subject;
	}
	
	public String getKeywords() {
		String keywords = info.getKeywords();
		if (keywords != null) {
		//	keywords = Base64.encodeBytes(keywords.getBytes());
		//	keywords = new BASE64Encoder().encode(keywords.getBytes());			
		}
		return keywords;
	}
	
	public String getCreator() {
		String creator = info.getCreator();
		if (creator != null) {
	//		creator = Base64.encodeBytes(creator.getBytes());
		//	creator = new BASE64Encoder().encode(creator.getBytes());			
		}
		return creator;
	}
	
	
	public String getProducer() {
		String producer = info.getProducer();
		if (producer != null) {
//			producer = Base64.encodeBytes(producer.getBytes());
		//	producer = new BASE64Encoder().encode(producer.getBytes());			
		}
		return producer;          	
	}
	
	public boolean hasErrors () {
		return errors;    	
	}
	
	public String getStringErrors () {
		return errorString;    	
	}
	
	public String getDate() {
		// Rebem un objecte Calendar,el pasem a string per ficarlo directament a 
		// un objecte de la classe simpple.sise.db.Document
		String s = null;
		GregorianCalendar c = null;
		c = (GregorianCalendar)info.getModificationDate();
		
		if (c != null) { //Mirem si hi ha la data de modificacio
			s = Integer.toString(c.get(Calendar.DAY_OF_MONTH))+
			Integer.toString(c.get(Calendar.MONTH))+
			Integer.toString(c.get(Calendar.YEAR));
		} else { // sino intentem ficar la data de creacio
			if ( c != null ) {
				c = (GregorianCalendar)info.getCreationDate();
				s = Integer.toString(c.get(Calendar.DAY_OF_MONTH))+
				Integer.toString(c.get(Calendar.MONTH))+
				Integer.toString(c.get(Calendar.YEAR));
			}
		}
		
		return s;
	}
	
	
	public String getUrl() {
		return url;
	}
	
	
	public String getDescription() {
		return info.getSubject(); // Es lo mes aproximat que he trobat, 2 pedres
	}
	
	
/*	public DocumentToIndex getDocument () {
		DocumentToIndex d = null;
		try {
			d = new DocumentToIndex();
			d.text = getText();
			d.url = url;
			d.title = getTitle();
			d.keywords = info.getKeywords();;  
			d.author = info.getAuthor();;
			d.data = getDate();
			d.fileType = 1;  // Document PDF
		} catch (Exception e) {
			logger.error(e);	
		}
		return d;
	}
*/	

	
}



