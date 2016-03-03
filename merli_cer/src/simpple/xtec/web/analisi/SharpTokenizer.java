package simpple.xtec.web.analisi;

import java.io.Reader;

import org.apache.lucene.analysis.CharTokenizer;

/**
 * A SharpTokenizer is a tokenizer that divides text at #. Adjacent sequences of
 * # characters form tokens.
 */

public class SharpTokenizer extends CharTokenizer {
	/** Construct a new SharpTokenizer. */
	public SharpTokenizer(Reader in) {
		super(in);
	}

	/**
	 * Collects only characters which do not satisfy
	 * {@link Character#isWhitespace(char)}.
	 */
	protected boolean isTokenChar(char c) {
		return c != '#';
	}
}
