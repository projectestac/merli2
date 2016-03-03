package simpple.xtec.indexador.analisi;

import java.io.IOException;

import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

/** 
 * Remove acutes
 *
 */

public class FiltreAccents extends TokenFilter {

	public FiltreAccents(TokenStream in) {
		super(in);
	}

	/** Returns the next input Token, removing accents and going lowercase */
	public final Token next() throws IOException {

		Token token = input.next();

		if (token == null)
			return null;

		else {

			String s = token.termText();

			StringBuffer sbuff = new StringBuffer();

			// Eliminem les Majúscules
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
					case 'à' :
						sbuff.append('a');
						break;
					case 'á' :
						sbuff.append('a');
						break;
					case 'í' :
						sbuff.append('i');
						break;
					case 'ï' :
						sbuff.append('i');
						break;
					case 'ò' :
						sbuff.append('o');
						break;
					case 'ó' :
						sbuff.append('o');
						break;
					case 'ú' :
						sbuff.append('u');
						break;
					case 'ü' :
						sbuff.append('u');
						break;
				case '·':
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

			// Eliminem els apòstrofs
			if (s.indexOf("'") == 1) {
				s = s.substring(2, s.length());
			}

			// retornem el token
			if (!s.equals(token.termText())) {
				return new Token(s, token.startOffset(), token.endOffset(), token.type());
			}
			return token;

		}
	}

}