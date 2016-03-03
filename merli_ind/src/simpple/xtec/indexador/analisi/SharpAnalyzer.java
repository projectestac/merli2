package simpple.xtec.indexador.analisi;

import java.io.Reader;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * Analyzer used in the Lucene index
 * 
 */

public final class SharpAnalyzer extends Analyzer {

	static final Logger logger = Logger.getLogger(simpple.xtec.indexador.analisi.SharpAnalyzer.class);

	/**
	 * Constructor
	 */

	public SharpAnalyzer() {
	}

	/**
	 * Apply the filter
	 */

	public final TokenStream tokenStream(String fieldName, Reader reader) {
		return new TrimFilter(new LowerCaseFilter(new SharpTokenizer(reader)));
	}

}
