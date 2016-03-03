package test.simpple.xtec.indexador;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import simpple.xtec.indexador.analisi.SharpAnalyzer;

public class TestAnalyzer extends TestCase {

	public final void testAnalyze() throws IOException {
		SharpAnalyzer sa = new SharpAnalyzer();

		printTokens(sa, "pre-SChool # hola  ");
	}

	private final Token[] analyzeTokens(Analyzer analyzer, String text) throws IOException {
		TokenStream stream = analyzer.tokenStream("contents", new StringReader(text));
		ArrayList tokenList = new ArrayList();
		while (true) {
			Token token = stream.next();
			if (token == null) {
				break;
			}
			tokenList.add(token);
		}
		return (Token[]) tokenList.toArray(new Token[0]);
	}

	public final void printTokens(Analyzer analyzer, String text) throws IOException {
		Token[] tokens = analyzeTokens(analyzer, text);
		for (int i = 0; i < tokens.length; i++) {
			Token token = tokens[i];
			System.out.println("[" + token.termText() + "] ");
		}
	}
}
