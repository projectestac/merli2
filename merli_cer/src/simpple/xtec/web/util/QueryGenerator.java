package simpple.xtec.web.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;

import simpple.xtec.agrega.AgregaUtils;
import simpple.xtec.web.analisi.EducacioAnalyzer;
import simpple.xtec.web.analisi.SharpAnalyzer;

public class QueryGenerator {
	private static final String OR = " or ";
	private static final String AND = " and ";
	// logger
	static final Logger logger = Logger.getLogger(simpple.xtec.web.util.QueryGenerator.class);

	public QueryGenerator() {
	}

	public Query getQueryFormatRecurs(String formatRecurs) {
		Query queryFormatRecurs = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		Hashtable allTipusFitxer = null;
		ArrayList allMimeTypes = null;
		if (formatRecurs == null || "".equals(formatRecurs.trim())) {
			return null;
		}
		try {

			myQueryParser = new QueryParser("format", new EducacioAnalyzer());
			textQuery = "(format:";

			// formatRecurs = formatRecurs.replace('?', '*');

			// Evitar que trobi "de"
			// formatRecurs = formatRecurs.replaceAll(" de ", " ");
			// formatRecurs = formatRecurs.replaceAll(" or ", " ");
			textQuery += formatRecurs;
			textQuery += ")";
			logger.debug(textQuery);
			queryFormatRecurs = myQueryParser.parse(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryFormatRecurs;
	}

	public Query getQueryTipusRecurs(String cercaTipusRecurs) {
		Query queryTipusRecurs = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {

			myQueryParser = new QueryParser("lom@educational@learningResourceType@value", new EducacioAnalyzer());
			textQuery = "(lom@educational@learningResourceType@value:\"" + cercaTipusRecurs + "\")";
			logger.debug(textQuery);
			queryTipusRecurs = myQueryParser.parse(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryTipusRecurs;
	}

	public Query getQueryNivellEducatiu(String cercaNivellEducatiu) {
		Query queryNivellEducatiu = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {

			myQueryParser = new QueryParser("lom@educational@context@value", new EducacioAnalyzer());
			textQuery = "(lom@educational@context@value:\"" + cercaNivellEducatiu + "\")";
			logger.debug(textQuery);
			queryNivellEducatiu = myQueryParser.parse(textQuery);

		} catch (Exception e) {
			logger.error(e);
		}
		return queryNivellEducatiu;
	}

	public Query getQueryDestinatari(String cercaDestinatari) {
		Query queryDestinatari = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {

			myQueryParser = new QueryParser("lom@educational@intendedEndUserRole@value", new EducacioAnalyzer());
			textQuery = "(lom@educational@intendedEndUserRole@value:" + cercaDestinatari + ")";
			logger.debug(textQuery);
			queryDestinatari = myQueryParser.parse(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryDestinatari;
	}

	public Query getQueryDretsReproduccio(String cercaDretsReproduccio) {
		Query queryDretsReproduccio = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {

			myQueryParser = new QueryParser("lom@rights@cost", new EducacioAnalyzer());
			textQuery = "lom@rights@cost:\"" + cercaDretsReproduccio + "\"";
			logger.debug(textQuery);
			queryDretsReproduccio = myQueryParser.parse(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryDretsReproduccio;
	}

	public Query getQueryIdioma(String cercaIdioma) {
		Query queryIdioma = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {
			myQueryParser = new QueryParser("lom@general@language", new EducacioAnalyzer());
			textQuery = "(lom@general@language:" + cercaIdioma + ")";
			logger.debug(textQuery);
			queryIdioma = myQueryParser.parse(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryIdioma;
	}

	public Query getQueryAutor(String cercaAutor) {
		Query queryText = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {
			myQueryParser = new QueryParser("autor", new EducacioAnalyzer());
			textQuery = "(autor:" + cercaAutor + ")";
			logger.debug(textQuery);
			queryText = myQueryParser.parse(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryText;
	}

	public Query getQueryDescriptors(String cercaDescriptors, BooleanQuery bq) {
		Query queryText = null;
		String textQuery = "";
		QueryParser myQueryParser = null;

		PerFieldAnalyzerWrapper analyzer = new PerFieldAnalyzerWrapper(new EducacioAnalyzer());
		SharpAnalyzer sa = new SharpAnalyzer();
		analyzer.addAnalyzer("termsCa", sa);

		try {
			myQueryParser = new QueryParser("termsCa", new EducacioAnalyzer());
			String[] ltermes = cercaDescriptors.split(" ");
			for (int i = 0; i < ltermes.length; i++) {
				String terme = ltermes[i];
				textQuery = "(termsCa:" + terme + " keysCa:" + terme + ")";
				queryText = myQueryParser.parse(textQuery);
				bq.add(queryText, BooleanClause.Occur.MUST);
			}
			logger.debug(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryText;
	}

	public Query getQueryAmbit(String cercaAmbit) {
		Query queryAmbit = null;
		String ambitQuery = "";
		QueryParser myQueryParser = null;
		try {
			myQueryParser = new QueryParser("autor", new EducacioAnalyzer());
			ambitQuery = "(ambit:" + cercaAmbit + ")";
			logger.debug(ambitQuery);
			queryAmbit = myQueryParser.parse(ambitQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryAmbit;
	}

	public Query getQueryLlicencia(String cercaLlicencia) {
		Query queryLlicencia = null;
		String llicenciaQuery = "";
		QueryParser myQueryParser = null;
		// int llic=0;
		// llic = new Integer(cercaLlicencia).intValue();
		// switch(llic)
		// {
		// case 1: cercaLlicencia="Attribution";break;
		// case 2: cercaLlicencia="Non commercial";break;
		// case 3: cercaLlicencia="No Derivate Works";break;
		// case 4: cercaLlicencia="Share alike";break;
		// }
		try {
			myQueryParser = new QueryParser("llicenciaCerca", new EducacioAnalyzer());
			llicenciaQuery = "(llicId:" + cercaLlicencia + ")";
			logger.debug(llicenciaQuery);
			queryLlicencia = myQueryParser.parse(llicenciaQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryLlicencia;
	}

	public Query getQueryEditorial(String cercaEditorial) {
		Query queryEditorial = null;
		String editorialQuery = "";
		QueryParser myQueryParser = null;
		try {
			myQueryParser = new QueryParser("editorialCerca", new EducacioAnalyzer());
			editorialQuery = "(editor:" + cercaEditorial + ")";
			logger.debug(editorialQuery);
			queryEditorial = myQueryParser.parse(editorialQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryEditorial;
	}

	public Query getQueryDUC(String nivellEducatiu, String areaCurricular, String cicle, String[] ducContent) {
		Query queryDUC = null;
		String ducQuery = "";
		QueryParser myQueryParser = null;
		try {
			// PerFieldAnalyzerWrapper analyzer = new
			// PerFieldAnalyzerWrapper(new EducacioAnalyzer());
			// SharpAnalyzer sa = new SharpAnalyzer();
			// analyzer.addAnalyzer("context", sa);

			myQueryParser = new QueryParser("duc", new EducacioAnalyzer());
			if ((nivellEducatiu != null) && nivellEducatiu.indexOf("-1") < 0) {
				if (ducQuery.length() == 0) {
					ducQuery += "(";
				}
				ducQuery += "+duc:" + nivellEducatiu + " ";
			}
			if ((areaCurricular != null) && areaCurricular.indexOf("-1") < 0) {
				if (ducQuery.length() == 0) {
					ducQuery += "(";
				}
				ducQuery += "+duc:" + areaCurricular + " ";
			}
			if ((cicle != null) && cicle.indexOf("-1") < 0) {
				if (ducQuery.length() == 0) {
					ducQuery += "(";
				}
				ducQuery += "+duc:" + cicle + " ";
			}

			if (ducContent != null) {
				if (ducQuery.length() == 0) {
					ducQuery += "(";
				}
				int i = 0;
				ducQuery += "+duc:";
				while (i < ducContent.length) {
					ducQuery += ducContent[i] + " ";
					i++;
				}
			}
			if (ducQuery.length() > 0) {
				ducQuery += ")";
			}

			// The contexts
			if ((nivellEducatiu != null) && nivellEducatiu.indexOf("-1") < 0 && ducContent == null
					&& areaCurricular.equals("-1")) {
				List contextMapping = UtilsCercador.getContextMapping(nivellEducatiu);
				if (contextMapping != null) {
					for (Iterator iterator = contextMapping.iterator(); iterator.hasNext();) {
						String context = (String) iterator.next();
						if (context != null && context.length() > 0) {
							ducQuery += " context:\"" + context + "\"";
						}
					}
				}
			}
			if ((cicle != null) && cicle.indexOf("-1") < 0 && ducContent == null && areaCurricular.equals("-1")) {
				List contextMapping = UtilsCercador.getContextMapping(cicle);
				if (contextMapping != null) {
					for (Iterator iterator = contextMapping.iterator(); iterator.hasNext();) {
						String context = (String) iterator.next();
						if (context != null && context.length() > 0) {
							ducQuery += " context:\"" + context + "\"";
						}
					}
				}
			}

			logger.debug(ducQuery);

			queryDUC = myQueryParser.parse(ducQuery);

		} catch (Exception e) {
			logger.error(e);
		}
		return queryDUC;
	}

	public Query getQueryText(String cercaText, String cercaKeywords, String lang) {
		// Query queryText = null;
		BooleanQuery bq = null;
		BooleanQuery bqTemp = null;
		WildcardQuery queryText = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {

			// cercaText = cercaText.replace('?', '�');
			// cercaText = cercaText.replaceAll("�", "\\\\?");

			myQueryParser = new QueryParser("contingut", new EducacioAnalyzer());
			// MLP: Keywords han de cercar sobre els descriptors tematics
			// if ((cercaKeywords != null) && !cercaKeywords.equals("")) {
			// cercaText += " " + cercaKeywords;
			// cercaText = cercaText.trim();
			// }
			// Camp de text buit
			if ((cercaText != null) && !cercaText.equals("")) {
				bq = new BooleanQuery();

				ArrayList quotedTokens = getQuotedTokens(cercaText);
				ArrayList restOfTokens = getRestOfTokens(cercaText, quotedTokens);
				ArrayList allTokens = new ArrayList();
				allTokens.addAll(quotedTokens);
				allTokens.addAll(restOfTokens);
				textQuery = "+(";

				boolean must = true;

				int i = 0;
				while (i < allTokens.size()) {
					String nextToken = (String) allTokens.get(i);
					// nextToken = nextToken.replaceAll("\"", " ");
					nextToken = nextToken.trim();
					logger.debug("Token -> " + nextToken);
					if (nextToken.startsWith("-")) {
						nextToken = nextToken.substring(1, nextToken.length());
						must = false;
					}
					if ((nextToken.indexOf("?") != -1) || (nextToken.indexOf("*") != -1)) {
						WildcardQuery wildcardQueryContingut = new WildcardQuery(new Term("contingut", nextToken));
						WildcardQuery wildcardQueryDescripcio = new WildcardQuery(new Term("descripcio", nextToken));
						bqTemp = new BooleanQuery();
						bqTemp.add(wildcardQueryContingut, BooleanClause.Occur.SHOULD);
						bqTemp.add(wildcardQueryDescripcio, BooleanClause.Occur.SHOULD);
					} else {
						textQuery = "(contingut:" + nextToken + ")";
						Query queryContingut = myQueryParser.parse(textQuery);
						textQuery = "(descripcio:" + nextToken + ")";
						Query queryDescripcio = myQueryParser.parse(textQuery);
						bqTemp = new BooleanQuery();
						bqTemp.add(queryContingut, BooleanClause.Occur.SHOULD);
						bqTemp.add(queryDescripcio, BooleanClause.Occur.SHOULD);
					}

					if (must) {
						bq.add(bqTemp, BooleanClause.Occur.MUST);
					} else {
						bq.add(bqTemp, BooleanClause.Occur.MUST_NOT);
					}

					i++;
				}
				textQuery += ")";

				logger.debug(textQuery);
			}

		} catch (Exception e) {
			logger.error(e);
		}
		// return queryText;
		return bq;
	}

	public ArrayList getRestOfTokens(String cercaText, ArrayList quotedStrings) {
		int i = 0;
		String cercaTextUpdated = "";
		ArrayList restOfTokens = null;
		StringTokenizer myTokenizer = null;

		try {
			restOfTokens = new ArrayList();
			cercaTextUpdated = cercaText;
			while (i < quotedStrings.size()) {

				String stringToRemove = (String) quotedStrings.get(i);
				cercaTextUpdated = cercaTextUpdated.replaceAll(stringToRemove, "");
				i++;
			}
			myTokenizer = new StringTokenizer(cercaTextUpdated);
			while (myTokenizer.hasMoreTokens()) {
				String nextToken = (String) myTokenizer.nextToken();
				nextToken = EducacioAnalyzer.filtra(nextToken);
				logger.debug("Rest of tokens: " + nextToken);
				restOfTokens.add(nextToken);
			}

		} catch (Exception e) {
			logger.error(e);
		}
		return restOfTokens;
	}

	public ArrayList getQuotedTokens(String cercaText) {
		int indexOfIni = 0;
		int indexOfFi = 0;
		boolean fi = false;
		ArrayList quotedTokens = null;
		try {
			quotedTokens = new ArrayList();
			indexOfIni = cercaText.indexOf("\"");
			logger.debug("cercaText: " + cercaText);
			logger.debug("indexOfIni: " + indexOfIni);
			if (indexOfIni != -1) {
				while (!fi) {
					indexOfFi = cercaText.indexOf("\"", indexOfIni + 1);
					logger.debug("indexOfFi: " + indexOfFi);
					if (indexOfFi != -1) {
						String token = cercaText.substring(indexOfIni + 1, indexOfFi);
						logger.debug("Token: " + token);
						token = "\"" + token + "\"";
						quotedTokens.add(token);
						indexOfIni = cercaText.indexOf("\"", indexOfFi + 1);
						logger.debug("indexOfIni: " + indexOfIni);
						if (indexOfIni == -1) {
							fi = true;
						}
					} else {
						fi = true;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
		return quotedTokens;
	}

	public Query getQueryDataPublicacio(String cercaDataPublicacio, boolean posterior) {
		Query queryDataPublicacio = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {
			logger.debug("Data publicacio: " + cercaDataPublicacio);
			myQueryParser = new QueryParser("dataPublicacio", new EducacioAnalyzer());
			logger.debug("Data publicacio: " + cercaDataPublicacio);
			int any;
			try {
				any = new Integer(cercaDataPublicacio).intValue();
			} catch (Exception e) // si el text no es un any, la cerca no
			// retorna res
			{
				if (posterior)
					any = 3000;
				else
					any = 1920;
			}

			// logger.debug("Data publicacio: " + cercaDataPublicacio);
			// cercaDataPublicacio =
			// UtilsCercador.girarData(cercaDataPublicacio);
			// logger.debug("Data publicacio: " + cercaDataPublicacio);
			// cercaDataPublicacio = cercaDataPublicacio.replace('/', '-');
			if (posterior) {
				textQuery = "(dataPublicacio:[" + any + "-12-31 TO 2999-01-01])";
			} else {
				textQuery = "(dataPublicacio:[1920-01-01 TO " + any + "-01-01])";
			}
			logger.debug(textQuery);
			queryDataPublicacio = myQueryParser.parse(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryDataPublicacio;
	}

	public Query getQueryDataCatalogacio(String cercaDataCatalogacio, boolean posterior) {
		Query queryDataCatalogacio = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {
			logger.debug("Data catalogacio: " + cercaDataCatalogacio);
			myQueryParser = new QueryParser("dataCatalogacio", new EducacioAnalyzer());
			logger.debug("Data catalogacio: " + cercaDataCatalogacio);

			// logger.debug("Data catalogacio: " + cercaDataCatalogacio);
			// cercaDataCatalogacio =
			// UtilsCercador.girarData(cercaDataCatalogacio);
			// logger.debug("Data catalogacio: " + cercaDataCatalogacio);
			// cercaDataCatalogacio = cercaDataCatalogacio.replace('/', '-');
			if (posterior) {
				textQuery = "(dataCatalogacio:[" + cercaDataCatalogacio + "-12-31 TO 2999-01-01])";
			} else {
				textQuery = "(dataCatalogacio:[1920-01-01 TO " + cercaDataCatalogacio + "-01-01])";
			}
			logger.debug(textQuery);
			queryDataCatalogacio = myQueryParser.parse(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryDataCatalogacio;
	}

	public Query getQueryDurada(String duracio, boolean posterior) {
		Query queryDurada = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {

			myQueryParser = new QueryParser("duracio", new EducacioAnalyzer());

			if (posterior) {
				String duradaString = "";
				int durada = new Integer(duracio).intValue();
				if (durada < 10) {
					duradaString = "00000" + durada;
				}
				if ((durada > 10) && (durada < 100)) {
					duradaString = "0000" + durada;
				}
				if ((durada > 100) && (durada < 1000)) {
					duradaString = "000" + durada;
				}
				if ((durada > 1000) && (durada < 10000)) {
					duradaString = "00" + durada;
				}
				if ((durada > 10000) && (durada < 100000)) {
					duradaString = "0" + durada;
				}

				textQuery = "(duracio:[" + duradaString + " TO 999999])";

			} else {
				String duradaString = "";
				int durada = new Integer(duracio).intValue();
				if (durada < 10) {
					duradaString = "00000" + durada;
				}
				if ((durada > 10) && (durada < 100)) {
					duradaString = "0000" + durada;
				}
				if ((durada > 100) && (durada < 1000)) {
					duradaString = "000" + durada;
				}
				if ((durada > 1000) && (durada < 10000)) {
					duradaString = "00" + durada;
				}
				if ((durada > 10000) && (durada < 100000)) {
					duradaString = "0" + durada;
				}
				textQuery = "(duracio:[000000 TO " + duradaString + "])";
			}
			logger.debug(textQuery);
			queryDurada = myQueryParser.parse(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryDurada;
	}

	public Query getQueryUnitat(String unitat) {
		Query queryText = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {
			myQueryParser = new QueryParser("disponibleIds", new EducacioAnalyzer());
			textQuery = "(disponibleIds:" + unitat + ")";
			logger.debug(textQuery);
			queryText = myQueryParser.parse(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryText;
	}

	public Query getQueryTipusFormatRecurs(String tipus) {
		Query queryText = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {
			myQueryParser = new QueryParser("recurs", new EducacioAnalyzer());
			if (TipusFitxer.ALTRESFISICS.equals(tipus) || TipusFitxer.FISIC.equals(tipus)) {
				tipus = TipusFitxer.FISIC;
			} else if (TipusFitxer.ALTRESENLINIA.equals(tipus) || TipusFitxer.ENLINIA.equals(tipus)) {
				tipus = TipusFitxer.ENLINIA;
			}
			textQuery = "(recurs:" + tipus + ")";
			logger.debug(textQuery);
			queryText = myQueryParser.parse(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryText;
	}

	public Query getQueryRecursFisicOnline(String recurs) {
		Query queryText = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {
			myQueryParser = new QueryParser("recurs", new EducacioAnalyzer());
			textQuery = "(recurs:" + recurs + ")";
			logger.debug("TEXT: "+textQuery);
			queryText = myQueryParser.parse(textQuery);                        
                        logger.debug("Query Text(QueryGenerator.java - 622): "+queryText);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryText;
	}

	public Query getQueryTipusFitxers(ArrayList list) {
		Query queryText = null;
		String textQuery = "";
		QueryParser myQueryParser = null;
		try {
			myQueryParser = new QueryParser("format", new EducacioAnalyzer());
			for (Iterator it = list.iterator(); it.hasNext();) {
				if (textQuery.equals("")) {
					textQuery = " (";
				}
				String idTipus = (String) it.next();
				textQuery += "format:" + idTipus + " ";
			}
			if (textQuery.length() > 0) {
				textQuery += ")";
			}
			queryText = myQueryParser.parse(textQuery);
			logger.debug(textQuery);
		} catch (Exception e) {
			logger.error(e);
		}
		return queryText;
	}

	// public BooleanQuery getQuery (String cercaText, String cercaAutor, String
	// cercaIdioma, String cercaDretsReproduccio, String cercaDestinatari,
	// String nivellEducatiu, String cercaTipusRecurs, String cercaKeywords,
	// String dataIniciPublicacio, String dataFinalPublicacio, String
	// areaCurricular, String[] ducContent, String ambit, String duradaMinima,
	// String duradaMaxima) {
	public String getQueryAgrega(String cercaText, Hashtable parameters) {
		StringBuffer query = new StringBuffer();
		String queryAux;

		String cercaKeywords = null;
		String cercaAutor = null;
		String cercaIdioma = null;
		String cercaDretsReproduccio = null;
		String cercaDestinatari = null;
		String nivellEducatiu = null;
		String cercaLlicencia = null;
		String cercaEditorial = null;
		String areaCurricular = null;
		String cicle = null;
		String cercaTipusRecurs = null;
		String formatRecurs = null;
		String[] ducContent = null;
		String dataIniciPublicacio = null;
		String dataFinalPublicacio = null;
		String dataIniciCatalogacio = null;
		String dataFinalCatalogacio = null;
		String ambit = null;
		String duradaMinima = null;
		String duradaMaxima = null;

		try {

			cercaKeywords = (String) parameters.get("keywords");
			cercaAutor = (String) parameters.get("autorCerca");
			cercaIdioma = (String) parameters.get("idiomaCerca");
			cercaDretsReproduccio = (String) parameters.get("dretsReproduccioCerca");
			cercaDestinatari = (String) parameters.get("destinatariCerca");
			nivellEducatiu = (String) parameters.get("nivellEducatiu");
			areaCurricular = (String) parameters.get("areaCurricular");
			cicle = (String) parameters.get("cicle");
			cercaTipusRecurs = (String) parameters.get("tipusRecurs");
			formatRecurs = (String) parameters.get("formatRecurs");
			ducContent = (String[]) parameters.get("ducContent");
			dataIniciPublicacio = (String) parameters.get("dataIniciPublicacio");
			dataFinalPublicacio = (String) parameters.get("dataFinalPublicacio");
			dataIniciCatalogacio = (String) parameters.get("dataIniciCatalogacio");
			dataFinalCatalogacio = (String) parameters.get("dataFinalCatalogacio");
			cercaLlicencia = (String) parameters.get("llicencia");
			cercaEditorial = (String) parameters.get("editorial");
			ambit = (String) parameters.get("ambit");
			duradaMinima = (String) parameters.get("duradaMinima");
			duradaMaxima = (String) parameters.get("duradaMaxima");

			logger.debug("Query -> Text");
			if (cercaText != null) {
				queryAux = "(lom.general.title = \"" + cercaText + "\")";
				queryAux += OR;
				queryAux += "(lom.general.description = \"" + cercaText + "\")";

				query.append("(").append(queryAux).append(")");
			}
			logger.debug("Query -> Autor");
			if ((cercaAutor != null) && !cercaAutor.trim().equals("")) {
				queryAux = "(lom.meta-metadata.contribute.role = \"author\")";
				queryAux += AND;
				queryAux += "(lom.meta-metadata.contribute.entity = \"" + cercaAutor.trim() + "\")";

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}
			logger.debug("Query -> Idioma");
			if ((cercaIdioma != null) && !cercaIdioma.trim().equals("")) {
				queryAux = "(lom.general.langugage = \"" + cercaIdioma.trim() + "\")";

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}
			logger.debug("Query -> DretsReproduccio");
			if ((cercaDretsReproduccio != null) && !cercaDretsReproduccio.trim().equals("")) {
				queryAux = "(lom.rights.cost = \"" + cercaDretsReproduccio.trim() + "\")";

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}
			logger.debug("Query -> Destinatari");
			if ((cercaDestinatari != null) && !cercaDestinatari.trim().equals("")) {
				queryAux = "(lom.educational.intendedEndUserRole.value = \"" + cercaDestinatari.trim() + "\")";

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}
			logger.debug("Query -> Nivell educatiu");
			if (nivellEducatiu.indexOf("-1") < 0 || areaCurricular.indexOf("-1") < 0) {
				Enumeration elements = (AgregaUtils.parseDUC(ducContent)).keys();
				queryAux = "(";
				// for (int i=0; i< elements.keys().size(); i++){
				while (elements.hasMoreElements()) {
					queryAux += "(lom.classification.taxonPath.taxon = \"";
					queryAux += elements.nextElement();
					queryAux += "\")";
					if (elements.hasMoreElements())
						queryAux += OR;
				}
				queryAux += ")";

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}
			/* NO implementada al cercador */
			// logger.debug("Query -> Tipus recurs");
			// if ((cercaTipusRecurs!= null) &&
			// !cercaTipusRecurs.trim().equals("")) {
			// queryAux +=
			// "(lom.educational.learningResourceType.value = \""+cercaTipusRecurs.trim()+"\")";
			//			    
			// if (query.length()>0)
			// query.append(AND);
			// query.append(queryAux);
			// }
			logger.debug("Query -> Format recurs");
			if ((formatRecurs != null) && !formatRecurs.trim().equals("")) {
				ArrayList mimes = UtilsCercador.parseFormatValues(formatRecurs.trim());
				queryAux = "";
				for (int i = 0; i < mimes.size(); i++) {
					if (i > 0)
						queryAux += OR;
					queryAux += "(lom.technical.format = \"" + mimes.get(i) + "\")";
				}

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}
			logger.debug("Query -> Data inici publicacio");
			if ((dataIniciPublicacio != null) && !dataIniciPublicacio.trim().equals("")) {
				queryAux = "(lom.meta-metadata.contribute.role = \"author\")";
				queryAux += AND;
				queryAux += "(lom.meta-metadata.contribute.dateTime >= \""
						+ UtilsCercador.girarData(dataIniciPublicacio.trim()) + "\")";

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}
			logger.debug("Query -> Data final publicacio");
			if ((dataFinalPublicacio != null) && !dataFinalPublicacio.trim().equals("")) {
				queryAux = "(lom.meta-metadata.contribute.role = \"author\")";
				queryAux += AND;
				queryAux += "(lom.meta-metadata.contribute.dateTime <= \""
						+ UtilsCercador.girarData(dataFinalPublicacio.trim()) + "\")";

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}
			logger.debug("Query -> Data inici catalogacio");
			if ((dataIniciCatalogacio != null) && !dataIniciCatalogacio.trim().equals("")) {
				queryAux = "(lom.meta-metadata.contribute.role = \"publisher\"";
				queryAux += OR;
				queryAux += "lom.meta-metadata.contribute.role = \"creator\")";

				queryAux += AND;
				queryAux += "(lom.meta-metadata.contribute.dateTime >= \""
						+ UtilsCercador.girarData(dataIniciCatalogacio.trim()) + "\")";

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}
			logger.debug("Query -> Data final catalogacio");
			if ((dataFinalCatalogacio != null) && !dataFinalCatalogacio.trim().equals("")) {
				queryAux = "(lom.meta-metadata.contribute.role = \"publisher\"";
				queryAux += OR;
				queryAux += "lom.meta-metadata.contribute.role = \"creator\")";

				queryAux += AND;
				queryAux += "(lom.meta-metadata.contribute.dateTime <= \""
						+ UtilsCercador.girarData(dataFinalCatalogacio.trim()) + "\")";

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}

			logger.debug("Query -> Durada minima");
			if ((duradaMinima != null) && !duradaMinima.trim().equals("")) {
				queryAux = "(lom.educational.typicalLearningTime >= \""
						+ UtilsCercador.parseDurationString(duradaMinima.trim()) + "\")";

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}

			logger.debug("Query -> Durada maxima");
			if ((duradaMaxima != null) && !duradaMaxima.trim().equals("")) {

				queryAux = "(lom.educational.typicalLearningTime <= \""
						+ UtilsCercador.parseDurationString(duradaMaxima.trim()) + "\")";

				if (query.length() > 0)
					query.append(AND);
				query.append("(").append(queryAux).append(")");
			}

			logger.debug("End query...");

		} catch (Exception e) {
			logger.error(e);
		}
		return query.toString();
	}

	// public BooleanQuery getQuery (String cercaText, String cercaAutor, String
	// cercaIdioma, String cercaDretsReproduccio, String cercaDestinatari,
	// String nivellEducatiu, String cercaTipusRecurs, String cercaKeywords,
	// String dataIniciPublicacio, String dataFinalPublicacio, String
	// areaCurricular, String[] ducContent, String ambit, String duradaMinima,
	// String duradaMaxima) {
	public BooleanQuery getQuery(String cercaText, Hashtable parameters) {
		BooleanQuery bq = null;
		Query queryText = null;
		Query queryAutor = null;
		Query queryDescriptors = null;
		Query queryIdioma = null;
		Query queryDretsReproduccio = null;
		Query queryDestinatari = null;
		Query queryTipusRecurs = null;
		Query queryFormatRecurs = null;
		Query queryLlicencia = null;
		Query queryEditorial = null;
		Query queryDataPublicacioPosterior = null;
		Query queryDataPublicacioAnterior = null;
		Query queryDataCatalogacioPosterior = null;
		Query queryDataCatalogacioAnterior = null;
		Query queryDUC = null;
		Query queryContext = null;
		Query queryAmbit = null;
		Query queryDuradaMinima = null;
		Query queryDuradaMaxima = null;
		// Camps afegits amb els recursos f�sics.
		// tipus fisic o enlinia
		Query queryRecursFisicOnline = null;
		// unitat amb disponibilitat del recurs.
		Query queryUnitat = null;

		String cercaKeywords = null;
		String cercaAutor = null;
		String cercaIdioma = null;
		String cercaDretsReproduccio = null;
		String cercaDestinatari = null;
		String nivellEducatiu = null;
		String areaCurricular = null;
		String cicle = null;
		String tipusRecurs = null;
		String formatRecurs = null;
		String[] ducContent = null;
		String cercaLlicencia = null;
		String cercaEditorial = null;
		String dataIniciPublicacio = null;
		String dataFinalPublicacio = null;
		String dataIniciCatalogacio = null;
		String dataFinalCatalogacio = null;
		String ambit = null;
		String duradaMinima = null;
		String duradaMaxima = null;
		String lang = null;
		// String recursFisicOnline = null;
		String recursOnline = null;
		String recursFisic = null;
		String unitat = null;

		try {
			lang = (String) parameters.get("lang");
			cercaKeywords = (String) parameters.get("keywords");
			cercaAutor = (String) parameters.get("autorCerca");
			cercaIdioma = (String) parameters.get("idiomaCerca");
			cercaDretsReproduccio = (String) parameters.get("dretsReproduccioCerca");
			cercaDestinatari = (String) parameters.get("destinatariCerca");
			nivellEducatiu = (String) parameters.get("nivellEducatiu");
			areaCurricular = (String) parameters.get("areaCurricular");
			cicle = (String) parameters.get("cicle");
			tipusRecurs = (String) parameters.get("tipusRecurs");
			formatRecurs = (String) parameters.get("formatRecurs");
			cercaLlicencia = (String) parameters.get("llicenciaCerca");
			cercaEditorial = (String) parameters.get("editorialCerca");
			ducContent = (String[]) parameters.get("ducContent");
			dataIniciPublicacio = (String) parameters.get("dataIniciPublicacio");
			dataFinalPublicacio = (String) parameters.get("dataFinalPublicacio");
			dataIniciCatalogacio = (String) parameters.get("dataIniciCatalogacio");
			dataFinalCatalogacio = (String) parameters.get("dataFinalCatalogacio");
			ambit = (String) parameters.get("ambit");
			duradaMinima = (String) parameters.get("duradaMinima");
			duradaMaxima = (String) parameters.get("duradaMaxima");
			// recursFisicOnline =(String)parameters.get("recursFisicOnline");
			recursOnline = (String) parameters.get("recursOnline");
			recursFisic = (String) parameters.get("recursFisic");
			unitat = (String) parameters.get("unitatCerca");

			bq = new BooleanQuery();
			queryText = getQueryText(cercaText, cercaKeywords, lang);

			logger.debug("Query -> Text");
			if (queryText != null) {
				bq.add(queryText, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Descriptors");
			if ((cercaKeywords != null) && !cercaKeywords.trim().equals("")) {
				getQueryDescriptors(cercaKeywords, bq);
			}
			logger.debug("Query -> Autor");
			if ((cercaAutor != null) && !cercaAutor.trim().equals("")) {
				queryAutor = getQueryAutor(cercaAutor);
				bq.add(queryAutor, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Idioma");
			if ((cercaIdioma != null) && !cercaIdioma.trim().equals("")) {
				queryIdioma = getQueryIdioma(cercaIdioma);
				bq.add(queryIdioma, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> DretsReproduccio");
			if ((cercaDretsReproduccio != null) && !cercaDretsReproduccio.trim().equals("")) {
				queryDretsReproduccio = getQueryDretsReproduccio(cercaDretsReproduccio);
				bq.add(queryDretsReproduccio, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Destinatari");
			if ((cercaDestinatari != null) && !cercaDestinatari.trim().equals("")) {
				queryDestinatari = getQueryDestinatari(cercaDestinatari);
				bq.add(queryDestinatari, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Nivell educatiu");
			if (nivellEducatiu.indexOf("-1") < 0 || areaCurricular.indexOf("-1") < 0) {
				queryDUC = getQueryDUC(nivellEducatiu, areaCurricular, cicle, ducContent);
				bq.add(queryDUC, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Tipus recurs");
			if ((tipusRecurs != null) && !tipusRecurs.trim().equals("")) {
				queryTipusRecurs = getQueryTipusRecurs(tipusRecurs);
				bq.add(queryTipusRecurs, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Format recurs");
			if ((formatRecurs != null) && !formatRecurs.trim().equals("") && !formatRecurs.trim().equals("null")) {
				queryFormatRecurs = getQueryFormatRecurs(formatRecurs);
				if (queryFormatRecurs != null) {
					bq.add(queryFormatRecurs, BooleanClause.Occur.MUST);
				}
			}
			logger.debug("Query -> Data inici publicacio");
			if ((dataIniciPublicacio != null) && !dataIniciPublicacio.trim().equals("")) {
				queryDataPublicacioPosterior = getQueryDataPublicacio(dataIniciPublicacio, true);
				bq.add(queryDataPublicacioPosterior, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Data final publicacio");
			if ((dataFinalPublicacio != null) && !dataFinalPublicacio.trim().equals("")) {
				queryDataPublicacioAnterior = getQueryDataPublicacio(dataFinalPublicacio, false);
				bq.add(queryDataPublicacioAnterior, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Data inici catalogacio");
			if ((dataIniciCatalogacio != null) && !dataIniciCatalogacio.trim().equals("")) {
				queryDataCatalogacioPosterior = getQueryDataCatalogacio(dataIniciCatalogacio, true);
				bq.add(queryDataCatalogacioPosterior, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Data final catalogacio");
			if ((dataFinalCatalogacio != null) && !dataFinalCatalogacio.trim().equals("")) {
				queryDataCatalogacioAnterior = getQueryDataCatalogacio(dataFinalCatalogacio, false);
				bq.add(queryDataCatalogacioAnterior, BooleanClause.Occur.MUST);
			}

			logger.debug("Query -> Ambit");
			if ((ambit != null) && !ambit.trim().equals("")) {
				queryAmbit = getQueryAmbit(ambit);
				bq.add(queryAmbit, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Durada minima");
			if ((duradaMinima != null) && !duradaMinima.trim().equals("")) {
				queryDuradaMinima = getQueryDurada(duradaMinima, true);
				bq.add(queryDuradaMinima, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Durada maxima");
			if ((duradaMaxima != null) && !duradaMaxima.trim().equals("")) {
				queryDuradaMaxima = getQueryDurada(duradaMaxima, false);
				bq.add(queryDuradaMaxima, BooleanClause.Occur.MUST);
			}
			logger.debug("Query -> Tipus recurs fisic/enlinia");
			// if ((recursFisicOnline!= null) &&
			// !recursFisicOnline.trim().equals("")) {
			// queryRecursFisicOnline = getQueryRecursFisicOnline
			// (recursFisicOnline);
			// bq.add(queryRecursFisicOnline, BooleanClause.Occur.MUST);
			// }

			// if ((recursFisicOnline!= null) &&
			// !recursFisicOnline.trim().equals("")) {
			// queryRecursFisicOnline = getQueryRecursFisicOnline
			// (recursFisicOnline);
			// bq.add(queryRecursFisicOnline, BooleanClause.Occur.MUST);
			// }

			// si un (i nomes un) dels dos checkbox esta seleccionat, afegeixo
			// la restriccio
			if (((recursOnline != null) && !recursOnline.trim().equals(""))
					^ ((recursFisic != null) && !recursFisic.trim().equals(""))) {
				String rec = "enlinia";
				if (((recursOnline == null) || recursOnline.trim().equals("")))
					rec = "fisic";
				queryRecursFisicOnline = getQueryRecursFisicOnline(rec);
				bq.add(queryRecursFisicOnline, BooleanClause.Occur.MUST);
			}

			logger.debug("Query -> Unitat amb disponibilitat");
			if ((unitat != null) && !unitat.trim().equals("")) {
				queryUnitat = getQueryUnitat(unitat);
				bq.add(queryUnitat, BooleanClause.Occur.MUST);
			}

			logger.debug("Query -> Llicencia");
			if ((cercaLlicencia != null) && !cercaLlicencia.trim().equals("")) {
				queryLlicencia = getQueryLlicencia(cercaLlicencia);
				bq.add(queryLlicencia, BooleanClause.Occur.MUST);
			}

			logger.debug("Query -> Editorial");
			if ((cercaEditorial != null) && !cercaEditorial.trim().equals("")) {
				queryEditorial = getQueryEditorial(cercaEditorial);
				bq.add(queryEditorial, BooleanClause.Occur.MUST);
			}

			logger.debug("End query...");

		} catch (Exception e) {
			logger.error(e);
		}
		return bq;
	}

	public Query getQueryFiltreRecurs(String filtreRecurs, Query bq) {
		String aux = filtreRecurs;
		filtreRecurs = TipusFitxer.getNomTipusOfGrup(filtreRecurs);
		Query queryFormatRecurs = getQueryFormatRecurs(filtreRecurs);
		if (queryFormatRecurs != null) {
			queryFormatRecurs.setBoost(0);
			if (TipusFitxer.ALTRESFISICS.equals(aux)) {
				((BooleanQuery) bq).add(queryFormatRecurs, BooleanClause.Occur.MUST_NOT);
				Query queryTipusFormatRecurs = getQueryTipusFormatRecurs(aux);
				((BooleanQuery) bq).add(queryTipusFormatRecurs, BooleanClause.Occur.MUST);
			} else if (TipusFitxer.ALTRESENLINIA.equals(aux)) {
				((BooleanQuery) bq).add(queryFormatRecurs, BooleanClause.Occur.MUST_NOT);
				Query queryTipusFormatRecurs = getQueryTipusFormatRecurs(TipusFitxer.FISIC);
				((BooleanQuery) bq).add(queryTipusFormatRecurs, BooleanClause.Occur.MUST_NOT);
			} else if (TipusFitxer.FISIC.equals(aux)) {
				Query queryTipusFormatRecurs = getQueryTipusFormatRecurs(aux);
				((BooleanQuery) bq).add(queryTipusFormatRecurs, BooleanClause.Occur.MUST);
			} else if (TipusFitxer.ENLINIA.equals(aux)) {
				Query queryTipusFormatRecurs = getQueryTipusFormatRecurs(TipusFitxer.FISIC);
				((BooleanQuery) bq).add(queryTipusFormatRecurs, BooleanClause.Occur.MUST_NOT);
			} else {
				((BooleanQuery) bq).add(queryFormatRecurs, BooleanClause.Occur.MUST);
			}
		}
		return bq;
	}
}