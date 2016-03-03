package simpple.xtec.web.analisi;

import java.io.Reader;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * Analyzer used in the Lucene index
 * 
 * @author descuer
 *
 */

 public final class EducacioAnalyzer extends StandardAnalyzer {

	static final Logger logger = Logger.getLogger(simpple.xtec.web.analisi.EducacioAnalyzer.class);
	
	/**
	 * Constructor
	 */
	public EducacioAnalyzer() {		
	  }
	
	/**
	 * Apply the filter
	 */
	
	public final TokenStream tokenStream(String fieldName, Reader reader) {
		TokenStream result = null;
		
		try {
			result = new WhitespaceTokenizer(reader);
		   // Treu accents
 		   result = new FiltreAccents(result);
		   } catch(Exception e) {
		   logger.error(e);
		   }
		return result;
	}
	
	public static String filtra (String originalText) {
		//String modified = "";
		String s = null;
		try {
			s = originalText;

			StringBuffer sbuff = new StringBuffer();

			// Eliminem les Majscules
			s = s.toLowerCase();

			// Eliminem els accents
			for (int i = 0; i < s.length(); i++) {

				char c = s.charAt(i);

				switch (c) {

					case 'é' :
						sbuff.append('e');
						break;
					case 'è' :
						sbuff.append('e');
						break;
					case 'ë' :
						sbuff.append('e');
						break;						
					case 'á' :
						sbuff.append('a');
						break;
					case 'à' :
						sbuff.append('a');
						break;
					case 'ä' :
						sbuff.append('a');
						break;						
					case 'ï' :
						sbuff.append('i');
						break;
					case 'í' :
						sbuff.append('i');
						break;
					case 'ó' :
						sbuff.append('o');
						break;
					case 'ò' :
						sbuff.append('o');
						break;
					case 'ö' :
						sbuff.append('o');
						break;						
					case 'ú' :
						sbuff.append('u');
						break;
					case 'ü' :
						sbuff.append('u');
						break;
					case '·' :
						break;						
				case '.':
					break;
				case ',':
					break;
				case ';':
					break;
				case ':':
					break;
					default :
						sbuff.append(c);
				}
			}

			s = sbuff.toString().toLowerCase();

			// Eliminem els apstrofs
			if (s.indexOf("'") == 1) {
				s = s.substring(2, s.length());
			}
			
		  } catch (Exception e) {
		  logger.error(e);	
		  }
		return s;  
		
	}
	
}


