package simpple.xtec.indexador.analisi;

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
	
	static final Logger logger = Logger.getLogger(simpple.xtec.indexador.analisi.EducacioAnalyzer.class);	
	
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
	
}


