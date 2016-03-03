package simpple.xtec.web.analisi;

import java.io.IOException;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/**
 * Trim token
 * 
 */

public class TrimFilter extends TokenFilter {

	public TrimFilter(TokenStream in) {
		super(in);
	}

	public final Token next() throws IOException {

		Token token = input.next();

		if (token == null)
			return null;

		else {

			String s = token.termText();
			s = s.trim();

			// retornem el token
			if (!s.equals(token.termText())) {
				return new Token(s, token.startOffset(), token.endOffset(), token.type());
			}
			return token;
		}
	}

}