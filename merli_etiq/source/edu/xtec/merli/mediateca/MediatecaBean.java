package edu.xtec.merli.mediateca;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.MerliContribution;
import edu.xtec.merli.RecursMerli;
import edu.xtec.merli.basedades.AccesBD;
import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.basedades.RecursBD;
import edu.xtec.merli.utils.Utility;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

public class MediatecaBean {
	private static final String DATE_MATCH = "[0123]*[0-9][-][01]*[0-9][-](20|19)*[0-9][0-9]";

	private static final String APPLICATION_RESOURCES = "MediatecaResources";

	protected static ConnectionBeanProvider broker;

	private static final Logger logger = Logger.getRootLogger();
	private static String[] mesos = { "gener", "febrer", "mar", "abril", "maig", "juny", "juliol", "agost", "setembre",
			"octubre", "novembre", "desembre" };

	public Locale locale;
	public MessageResources messages;
	private static MessageResources missatges;

	private File lockfile;
	private FileChannel channel;
	private FileLock lock;

	/**
	 * SolÂ·licita un ConnectionBean al ConnectionBeanProvider, si aquest no
	 * estÃ  innicialitzat, ho fa.
	 * 
	 * @return ConnectionBean, servit pel ConnectionBeanProvider.
	 * @throws MerliDBException
	 */
	private ConnectionBean connectBD() throws MerliDBException {
		// Inicialitza el CBP si no ho estÃ .
		ConnectionBean bd = null;

		try {
			if (broker == null) {
				// broker =
				// ConnectionBeanProvider.getConnectionBeanProvider(true,
				// Utility.loadProperties("../../../../","database.properties"));
				broker = ConnectionBeanProvider.getConnectionBeanProvider(true, Utility.loadProperties("/",
						"database.properties"));
			}
			bd = broker.getConnectionBean();
			logger.info("connection created");
		} catch (Exception e) {
			logger.warn("Error on connection with DDBB->" + e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		// Retorna un ConnectionBean.
		return bd;
	}

	/**
	 * Es desconnecta de la BD.
	 * 
	 * @throws MerliDBException
	 */
	private void disconnectBD(ConnectionBean cb) throws MerliDBException {
		// Allibera el connectionBean utilitzat.
		try {
			broker.freeConnectionBean(cb);
		} catch (Exception e) {
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
	}

	public static String getMessage(Locale locale, String missatge) {
		if (missatges == null)
			missatges = MessageResources.getMessageResources(APPLICATION_RESOURCES);
		return missatges.getMessage(locale, missatge);
	}

	public boolean getLock() {
		try {
			String tmpdir = System.getProperty("java.io.tmpdir");
			String filename = MediatecaBean.class.getName() + ".lock";
			lockfile = new File(tmpdir, filename);

			// Try to get the lock
			channel = new RandomAccessFile(lockfile, "rw").getChannel();
			lock = channel.tryLock();
			if (lock == null) {
				// File is lock by other application
				channel.close();
				logger.error("Només un migració pot estar executant-se.");
				return false;
			}
			// Add shutdown hook to release lock when application shutdown
			ShutdownHook shutdownHook = new ShutdownHook(this);
			Runtime.getRuntime().addShutdownHook(shutdownHook);
			return true;
		} catch (Exception e) {
			logger.error("No es pot aconseguir el lock");
			return false;
		}
	}

	public void unlockFile() {
		// release and delete file lock
		try {
			if (lock != null) {
				lock.release();
				channel.close();
				lockfile.delete();
			} else {
				lockfile.delete();
			}
		} catch (IOException e) {
			logger.error("No es pot alliberar el lock");
		}
	}

	public void forceUnlockFile() {
		String tmpdir = System.getProperty("java.io.tmpdir");
		String filename = MediatecaBean.class.getName() + ".lock";
		lockfile = new File(tmpdir, filename);
		// release and delete file lock
		lockfile.delete();
	}

	static class ShutdownHook extends Thread {

		private MediatecaBean mdb;

		ShutdownHook(MediatecaBean mdb) {
			this.mdb = mdb;
		}

		public void run() {
			mdb.unlockFile();
		}
	}

	public ArrayList executeMigracio() throws MerliDBException {
		Map m, m2;
		RecursMerli r;
		ArrayList lparm, l, lRec, lelem = new ArrayList(), lres = new ArrayList();
		ConnectionBean cbc = null;
		Connection c = null;
		MerliContribution mc;
		int aux;
		int idrec;
		String saux;

		try {
			RecursBD rb = new RecursBD();

			lelem.add("DATARECURS");
			lelem.add("NU");
			lelem.add("CR");
			lelem.add("TI");
			lelem.add("AU");
			lelem.add("ED");
			lelem.add("TR");
			lelem.add("DA");
			lelem.add("LL");
			lelem.add("NS");
			lelem.add("NI");
			lelem.add("CA");
			lelem.add("IA");
			lelem.add("NR");
			lelem.add("SI");
			lelem.add("DE");
			lelem.add("DN");
			lelem.add("DC");
			lelem.add("DT");
			lelem.add("ID");
			lelem.add("RE");
			lelem.add("DD");
			lelem.add("ESTAT");
			lelem.add("DISPONIBILITAT");
			lelem.add("DATA_FITXA");
			lelem.add("CODI_AUTOR");
			cbc = connectBD();
			c = cbc.getConnection();

			lparm = new ArrayList();
			lparm.add("NU");
			Map ml = AccesBD.getObjectList("mediateca_migracio", lparm, "migrada <= 0", " nu ASC", c);

			c.close();
			disconnectBD(cbc);
			logger.info("Total recursos:" + ((ArrayList) ml.get("NU")).size());
			lRec = new ArrayList();
			int i = 0;
			for (int j = 0; j < ((ArrayList) ml.get("NU")).size(); j++) {
				cbc = connectBD();
				c = cbc.getConnection();
				m = AccesBD.getObject("mediateca_migracio", lelem, " NU='" + getString(ml, j, "NU") + "' ", c);

				try {

					aux = parseEstat(m, i);

					if (MerliBean.ESTAT_M_DENEGAT < aux) {

						idrec = AccesBD.getNext("SEQ_MERLI", c);
						logger.info("IdRecurs " + (idrec + 1) + "  nu:" + getString(m, i, "NU"));
						r = new RecursMerli(idrec + 1);
						r.setAgregaSend("0");

						// TI
						if (StringUtil.length(getString(m, i, "TI")) > 950) {
							saveError(m, i, c, idrec + 1, "TI - Supera la longitud permesa");
						}
						r.setTitle(StringUtil.shorten(getString(m, i, "TI"), 950));
						// RE
						if (StringUtil.length(getString(m, i, "RE")) > 2000) {
							saveError(m, i, c, idrec + 1, "RE - Supera la longitud permesa");
						}
						r.setDescription(StringUtil.shorten(getString(m, i, "RE"), 2000));
						r.setUrl(null);
						r.setEstat(String.valueOf(parseEstat(m, i)));
						r.setResponsable(getMessage(locale, "mediateca.responsable"));
						l = new ArrayList();
						l.add(getMessage(locale, "mediateca.ambit"));
						r.setAmbit(l);
						logger.info("General, estat...");
						// DN
						l = new ArrayList();
						l.add("destinatari1");
						l.add("destinatari2");
						l.add("destinatari3");
						l.add("context1");
						l.add("context2");
						l.add("context3");

						lRec.clear();
						m2 = AccesBD
								.getQuery(
										" mediateca_conversio_dn where TRANSLATE(UPPER(dn), 'ÁÀÂÄÈÉÊËÍÌÎÏÒÓÔÖÚÙÛÜ','AAAAEEEEIIIIOOOOUUUU') like TRANSLATE(UPPER('"
												+ converteixStringStripAccents(getString(m, i, "DN"))
												+ "'), 'ÁÀÂÄÈÉÊËÍÌÎÏÒÓÔÖÚÙÛÜ','AAAAEEEEIIIIOOOOUUUU') ", l, lRec, c);

						try {
							logger.info("IdRecurs " + (idrec + 1) + "  nu:" + getString(m, i, "NU") + ": context1: "
									+ getString(m2, 0, "context1"));
							logger.info("IdRecurs " + (idrec + 1) + "  nu:" + getString(m, i, "NU") + ": context2: "
									+ getString(m2, 0, "context2"));
							logger.info("IdRecurs " + (idrec + 1) + "  nu:" + getString(m, i, "NU") + ": context3: "
									+ getString(m2, 0, "context3"));
							logger.info("IdRecurs " + (idrec + 1) + "  nu:" + getString(m, i, "NU")
									+ ": destinatari1: " + getString(m2, 0, "destinatari1"));
							logger.info("IdRecurs " + (idrec + 1) + "  nu:" + getString(m, i, "NU")
									+ ": destinatari2: " + getString(m2, 0, "destinatari2"));
							logger.info("IdRecurs " + (idrec + 1) + "  nu:" + getString(m, i, "NU")
									+ ": destinatari3: " + getString(m2, 0, "destinatari3"));
							l = new ArrayList();
							ifContextAddIdNivellCat("context1", m2, l, c);
							ifContextAddIdNivellCat("context2", m2, l, c);
							ifContextAddIdNivellCat("context3", m2, l, c);
							r.setContext(l);
							// r.setMinAge(recuperaEdatMinima(l, c));

						} catch (Exception e) {
							saveError(m, i, c, idrec + 1, "Context - " + getString(m, i, "DN"));
						}

						try {
							l = new ArrayList();
							ifDestinatariaAddIdRolUsuari("destinatari1", m2, l, c);
							ifDestinatariaAddIdRolUsuari("destinatari2", m2, l, c);
							ifDestinatariaAddIdRolUsuari("destinatari3", m2, l, c);

							r.setEndUserRol(l);

							logger.info("Context, Destinataris...");
						} catch (Exception e) {
							saveError(m, i, c, idrec + 1, "Destinatari - " + getString(m, i, "DN"));
						}
						// DC
						if (getString(m, i, "DC") != null) {
							if (StringUtil.length(getString(m, i, "DC")) > 1000) {
								saveError(m, i, c, idrec + 1, "DC - Supera la longitud permesa");
							}
							r.setContext2(StringUtil.shorten(getString(m, i, "DC"), 1000));
						}

						logger.info("CODI_AUTOR..");
						// Unitat creadora i característiques físiques.
						// DD
						// MLP 10/01/11: Ja no es fa servir el camp DD, s'agafa
						// del CODI_AUTOR
						// parseContribucioDD(m, i, r);
						// DC
						saux = parseCodiAutor(m, i, c);
						if (saux == null || "0".equals(saux)) {
							saveError(m, i, c, idrec + 1, "Autor - " + getString(m, i, "CODI_AUTOR"));
							String data = parseDataCreacio(m, i);
							r.addContribution(new MerliContribution(3, extractUserUnitat(getMessage(locale,
									"mediateca.unitat.creadora")), data));
						} else {
							String data = parseDataCreacio(m, i);
							r.addContribution(new MerliContribution(3, extractUserUnitat(saux), data));

							r.setUnitatCreadora(saux);
						}
						if (!esStringValid(r.getUnitatCreadora()))
							r.setUnitatCreadora(getMessage(locale, "mediateca.unitat.creadora"));
						if (StringUtil.length(getString(m, i, "CA")) > 1000) {
							saveError(m, i, c, idrec + 1, "CA - Supera la longitud permesa");
						}
						r.setCaractRFisic(StringUtil.shorten(getString(m, i, "CA"), 1000));
						if (r.getCaractRFisic() == null)
							r.setCaractRFisic("");
						r.setLearningTime(comprovarMinuts(getString(m, i, "CA")));

						// logger.info("Contribucions..");
						// //Unitat creadora i característiques físiques.
						// if (r.getContribution(3) != null){
						// saux = parseUnitatCreadora(r.getContribution(),c);
						// if (saux==null || "0".equals(saux)){
						// saveError(m,i,c,idrec+1,"Unitat creadora - "+getString(m,
						// i, "DD"));
						// }else{
						// r.setUnitatCreadora(saux);
						// }
						// r.setCaractRFisic(getString(m, i, "CA"));
						// if (r.getCaractRFisic() == null)
						// r.setCaractRFisic("");
						// r.setLearningTime(comprovarMinuts(getString(m, i,
						// "CA")));
						// }
						// if (!esStringValid(r.getUnitatCreadora()))
						// r.setUnitatCreadora(getMessage(locale,
						// "mediateca.unitat.creadora"));

						// AU, ED
						saux = getString(m, i, "DA");
						if (saux == null) {
							saux = getMessage(locale, "mediateca.data.creacio");
						}
						String sautor = getString(m, i, "AU");
						if (sautor == null) {
							sautor = getMessage(locale, "mediateca.autoria");
						}
						if (StringUtil.length(getString(m, i, "AU")) > 990) {
							saveError(m, i, c, idrec + 1, "AU - Supera la longitud permesa");
							sautor = StringUtil.shorten(getString(m, i, "AU"), 990);
						}

						mc = new MerliContribution(1, sautor, "1-1-" + saux.substring(0, 4));
						if (saux.length() > 4) {
							mc.setDescription(saux);
						}
						if (mc != null)
							r.addContribution(mc);

						if (getString(m, i, "ED") != null) {
							mc = new MerliContribution(8, getString(m, i, "ED"), "1-1-" + getString(m, i, "DA"));
							if (esStringValid(mc.getEntity()))
								r.addContribution(mc);
						}

						logger.info("Unitat, autors, editors..");
						// MLP: Per defecte es posa a NULL segons incidencia a
						// LaFarga
						// r.setCost(getMessage(locale, "mediateca.cost"));

						saux = "";
						if (getString(m, i, "NS") != null)
							saux = getString(m, i, "NS");
						if (getString(m, i, "NI") != null)
							saux += getString(m, i, "NI");

						// if (!"".equals(saux)){
						// r.setDescription(r.getDescription() +
						// "\n\n"+
						// saux);
						// //r.setDescripcioRelacio(saux);
						// saveError(m, i, c, idrec+1,
						// "Relacions-NI:  "+getString(m, i, "NI"));
						// }

						r.setHasRights(getMessage(locale, "mediateca.rights"));

						try {
							r.setLanguage(parseLanguages(getString(m, i, "LL"), c));
						} catch (Exception e) {
							saveError(m, i, c, idrec + 1, "languages - " + getString(m, i, "LL"));
						}

						logger.info("Relacions, Drets, idiomes, ..");
						// MLP: Per defecte es posa a NULL segons incidencia a
						// LaFarga
						// r.setLicense(getMessage(locale,
						// "mediateca.license"));
						r.setRightsDesc(getMessage(locale, "mediateca.rights.desc"));

						try {
							r.setFormatFisic(parseFormat(getString(m, i, "CR"), c));
						} catch (Exception e) {
							saveError(m, i, c, idrec + 1, "Format - " + getString(m, i, "CR"));
						}
						r.setVersion(getMessage(locale, "mediateca.version"));

						logger.info("Formats, versio, ..");
						try {
							r.setEsFisic(true);
							parseIdFisic(getString(m, i, "IA"), r, c);

							// r.addIdentificador(getString(m, i, "NU"),
							// getMessage(locale, "mediateca.tipus"));
						} catch (Exception e) {
							saveError(m, i, c, idrec + 1, "Id fisic - sense nous ids fisics.." + getString(m, i, "IA"));
							// throw new Exception();
						}
						logger.info("Identificadors ..");

						try {
							parseParaulesTermes(r, m, i, c);
						} catch (MerliDBException e) {
							saveError(m, i, c, idrec + 1, "Error procesant els descriptors del recurs: " + getString(m, i, "NU"));
						}
						logger.info("Termes ..");

						rb.addRecurs(r, c);

						logger.info("Recurs creat! ");
						// DESAR relacio idrec-nu
						// mer_Rec_mediateca
						// Desem el NU a BBDD.
						try {
							inserirConversio(getString(m, i, "NU"), r.getIdRecurs(), c);
						} catch (MerliDBException e) {
							saveError(m, i, c, idrec + 1, "Error desant el NU del recurs: " + getString(m, i, "NU"));
						}

						// marcar recurs com a migrat
						desarEstatMigracio(getString(m, i, "NU"), 1, c);

						logger.info("migrat OK");
					} else {
						saveError(m, i, c, 0, "Estat - no valid" + getString(m, i, "ESTAT"));
						desarEstatMigracio(getString(m, i, "NU"), 1, c);
					}
				} catch (SQLException e) {
					saveError(m, i, c, 0, "Sql - " + e.getMessage());
					desarEstatMigracio(getString(m, i, "NU"), -1, c);
				} catch (MerliDBException e) {
					saveError(m, i, c, 0, "general - " + e.getMessage());
					desarEstatMigracio(getString(m, i, "NU"), -1, c);
				} catch (Exception e) {
					logger.error("Error migrant mediateca->" + e);
					desarEstatMigracio(getString(m, i, "NU"), -2, c);
				} finally {
					c.commit();
					c.close();
					disconnectBD(cbc);
				}
			}

		} catch (NullPointerException ne) {
			logger.error("Can't create FullLlistat of Recursos->" + ne);
			throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
		} catch (Exception e) {
			logger.error("Can't create Recursos->" + e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		} finally {
			try {
				c.commit();
				c.close();
				disconnectBD(cbc);
			} catch (Exception e) {
			}
		}
		return lres;
	}

	private String parseCodiAutor(Map m, int i, Connection c) {
		String unitat = null;
		String autor = getString(m, i, "CODI_AUTOR");
		if (autor == null) {
			return null;
		}
		// Check if is a number
		try {
			Integer.parseInt(autor);
		} catch (NumberFormatException e) {
			return null;
		}
		try {
			unitat = AccesBD.executeQuery("SELECT  id_unitat FROM mer_unitats WHERE id_unitat = '" + autor + "'",
					"id_unitat", c);
			if (!esStringValid(unitat)) {
				unitat = null;
			}
		} catch (Exception e) {
			logger.error("parseAutor. CODI_AUTOR:" + getString(m, i, "CODI_AUTOR"), e);
		}
		return unitat;
	}

	public static boolean esStringValid(String r) {
		return !(r == null || "".equals(r.trim()) || "0".equals(r.trim()));
	}

	public String comprovarMinuts(String ca) {
		if (ca == null)
			return "";
		int ind;
		String temps = "PTH";
		String pt = "";
		if (ca.indexOf("minuts") > 1) {
			ind = ca.indexOf("minuts") - 1;
			ind = Math.max(0, ca.substring(0, ind).lastIndexOf(" "));
			pt = ca.substring(Math.max(0, ind), ca.indexOf("minuts"));

			if (pt.trim().matches("[0-9]*")) {
				temps += pt.trim();
			}

		}
		temps += "M";
		return temps;
	}

	private void inserirConversio(String nu, int idrec, Connection c) throws MerliDBException {
		ArrayList al = new ArrayList();
		al.add(String.valueOf(idrec));
		al.add(nu);
		AccesBD.executeInsert("mer_rec_mediateca", al, c);
	}

	private void desarEstatMigracio(String nu, int estat, Connection c) throws MerliDBException {
		AccesBD.executeUpdate("mediateca_migracio", "NU like '" + nu + "' ", "migrada=" + estat, c);
	}

	private void parseParaulesTermes(RecursMerli r, Map m, int i, Connection c) throws MerliDBException {
		String listtermes = "";
		if (esStringValid(getString(m, i, "DT"))) {
			listtermes = getString(m, i, "DT");
		}
		if (esStringValid(getString(m, i, "ID"))) {
			listtermes += ";" + getString(m, i, "ID");
		}
		String idterme;
		String[] termes = listtermes.split(";");
		ArrayList idtermes = new ArrayList();
		String idtersec = "";
		ArrayList paraules = new ArrayList();
		String parsec = "";

		logger.info("nu=" + getString(m, i, "NU") + " termes=" + termes.length);

		// recuperar paraules all in one
		Map tmpTermes, tmpParaules;
		ArrayList lca = null, len = null, les = null, lidtermes = null, lparaules = null, lidparaules = null;
		if (termes.length > 0) {
			tmpTermes = getAllTermes(termes, c);
			lca = (ArrayList) tmpTermes.get("UPPER(terme_pla_ca)");
			len = (ArrayList) tmpTermes.get("UPPER(terme_pla_en)");
			les = (ArrayList) tmpTermes.get("UPPER(terme_pla_es)");
			lidtermes = (ArrayList) tmpTermes.get("id_terme");

			tmpParaules = getAllParaules(termes, c);
			lparaules = (ArrayList) tmpParaules.get("UPPER(v_paraula_pla)");
			lidparaules = (ArrayList) tmpParaules.get("id_paraula");
		}

		for (int indx = 0; indx < termes.length; indx++) {
			if (termes[indx] != null && !"".equals(termes[indx])) {
				try {
					String aux = converteixStringStripAccents(converteixEspais(termes[indx]));

					// Busquem la paraula al hashmap del TESAURE
					idterme = null;
					aux = aux.toUpperCase();
					int idx = lca.indexOf(aux);
					if (idx >= 0) {
						idterme = ((BigDecimal) lidtermes.get(idx)).toString();
					}
					idx = les.indexOf(aux);
					if (idx >= 0) {
						idterme = ((BigDecimal) lidtermes.get(idx)).toString();
					}
					idx = len.indexOf(aux);
					if (idx >= 0) {
						idterme = ((BigDecimal) lidtermes.get(idx)).toString();
					}

					if (idterme != null) {
						if (idtersec.indexOf("#" + idterme + "#") < 0) {
							idtermes.add(idterme);
							idtersec += "#" + idterme + "#";
						}
					} else {
						aux = converteixEspais(termes[indx]);
						aux = aux.toUpperCase();

						// Busquem la paraula al hashmap de PARAULES OBERTES
						idterme = null;
						idx = lparaules.indexOf(aux);
						if (idx >= 0) {
							idterme = ((BigDecimal) lidparaules.get(idx)).toString();
						}

						if (idterme != null) {
							if (parsec.indexOf("#" + idterme + "#") < 0) {
								paraules.add(idterme);
								parsec += "#" + idterme + "#";
							}
						} else {
							if (termes[indx].length() > 100) {
								if (parsec.indexOf("#" + termes[indx] + "#") < 0) {
									paraules.add(termes[indx].substring(0, 90) + "[..]");
									parsec += "#" + termes[indx] + "#";
								}
								logger.warn("Paraula massa llarga, serà retallada:" + termes[indx]);
							} else {
								if (parsec.indexOf("#" + termes[indx] + "#") < 0) {
									paraules.add(termes[indx]);
									parsec += "#" + termes[indx] + "#";
								}
							}
						}
					}
				} catch (Exception e) {
					if (termes[indx].length() > 100) {
						if (parsec.indexOf("#" + termes[indx] + "#") < 0) {
							paraules.add(termes[indx].substring(0, 90) + "[..]");
							parsec += "#" + termes[indx] + "#";
						}
						logger.warn("Paraula massa llarga, serà retallada:" + termes[indx]);
					} else {
						if (parsec.indexOf("#" + termes[indx] + "#") < 0) {
							paraules.add(termes[indx]);
							parsec += "#" + termes[indx] + "#";
						}
					}
				}
			}
		}
		// Afegir la llista d'ids al Taxon.

		r.setTaxon(idtermes);
		r.setParaules(paraules);
		r.setParaulesId(paraules);
	}

	private Map getAllTermes(String[] termes, Connection c) throws MerliDBException {
		StringBuffer sb = new StringBuffer();

		sb.append(" 1 = 1 AND ");
		for (int i = 0; i < termes.length; i++) {
			String aux = converteixStringStripAccents(converteixEspais(termes[i]));

			if (i > 0) {
				sb.append(" OR ");
			}
			sb.append(" UPPER(terme_pla_ca) LIKE UPPER('" + aux + "')");
			sb.append(" OR UPPER(terme_pla_en) LIKE UPPER('" + aux + "')");
			sb.append(" OR UPPER(terme_pla_es) LIKE UPPER('" + aux + "')");
		}
		String cond = sb.toString();

		List camps = new ArrayList();
		camps.add("UPPER(terme_pla_ca)");
		camps.add("UPPER(terme_pla_es)");
		camps.add("UPPER(terme_pla_en)");
		camps.add("id_terme");

		Map m = AccesBD.getObject("the_termes", camps, cond, c);
		return m;
	}

	private Map getAllParaules(String[] termes, Connection c) throws MerliDBException {
		StringBuffer sb = new StringBuffer();

		sb.append(" 1 = 1 AND ");
		for (int i = 0; i < termes.length; i++) {
			String aux = converteixStringStripAccents(converteixEspais(termes[i]));

			if (i > 0) {
				sb.append(" OR ");
			}
			sb.append(" UPPER(v_paraula_pla) LIKE UPPER('" + aux + "')");
		}
		String cond = sb.toString();

		List camps = new ArrayList();
		camps.add("UPPER(v_paraula_pla)");
		camps.add("id_paraula");

		Map m = AccesBD.getObject("MER_PARAULES", camps, cond, c);
		return m;
	}

	private void saveError(Map m, int i, Connection c, int idrec, String text) {
		saveError(getString(m, i, "NU"), c, idrec, text);
	}

	private void saveError(String nu, Connection c, int idrec, String text) {

		String camps = "id_rec, nu, descripcio";
		ArrayList lParams = new ArrayList();
		lParams.add(String.valueOf(idrec));
		lParams.add(nu);
		lParams.add(text);

		try {
			AccesBD.executeInsert("mediateca_migracio_logs", camps, lParams, c);
		} catch (MerliDBException e) {
			logger.error("no es pot accedir al log.");
		}
	}

	private void parseIdFisic(String identificacio, RecursMerli r, Connection c) throws MerliDBException {
		if (identificacio == null)
			throw new MerliDBException(MerliDBException.CODI_INEXISTENT);
		String idfisic;
		String tipus;
		String idtipus;
		String[] ids = identificacio.split("[;]");
		String[] tips;
		boolean noResource = true;
		for (int i = 0; i < ids.length; i++) {
			if (ids[i] != null) {
				tipus = "";
				idfisic = "";
				tips = ids[i].split("[ :;]");
				for (int x = 0; x < tips.length; x++) {
					if (tips[x].equals("")) {
						continue;
					}
					if ("".equals(idfisic) && !tips[x].matches("[a-zA-Z]*[0-9?():/./-]+[a-zA-Z0-9?():/./-]*")) {
						tipus += tips[x] + " ";
					} else {
						idfisic += tips[x];
					}
				}
				if (tipus != null && !"".equals(tipus)) {
					// MLP: 9/01/11: Els CDU no s'han de migrar
					if ("CDU".equalsIgnoreCase(converteixString(tipus)))
						continue;
					idtipus = AccesBD.executeQuery("SELECT  v_tipus " + "FROM mer_tipus_idfisic "
							+ "WHERE UPPER(v_tipus) like UPPER('%" + converteixString(tipus) + "%')"
							+ "GROUP BY v_tipus", "v_tipus", c);

					if (idtipus == null || idtipus.length() < 1 || "0".equals(idtipus)) {
						// Fem un insert del nou format.
						saveError("nu:" + r.getIdRecurs(), c, r.getIdRecurs(), "Tipus id - " + tipus);
						idtipus = getMessage(locale, "mediateca.tipus");
					}
					if (idfisic != null && !"".equals(idfisic) && idtipus != null && !"".equals(idtipus)) {
						if (!r.getTipusIdFisic().contains(idtipus)) {
							r.addIdentificador(idfisic, idtipus);
							noResource = false;
						}
					}
				}
			}
		}
		if (noResource) {
			throw new MerliDBException(MerliDBException.CODI_INEXISTENT);
		}
	}

	private String parseUnitatCreadora(ArrayList contribs, Connection c) {
		MerliContribution mc;
		if (contribs == null)
			return null;
		String unitat = null;

		int i = 0;
		while (i < contribs.size() && (unitat == null || "0".equals(unitat))) {
			try {
				mc = (MerliContribution) contribs.get(i);
				if (mc != null && mc.getIdRol() == 3) {
					unitat = AccesBD.executeQuery("SELECT  id_unitat " + "FROM mer_unitats "
							+ "WHERE UPPER(' '||v_nom||' ') like UPPER('%" + parseCRP(mc.getEntity()) + "%') "
							+ "GROUP BY id_unitat", "id_unitat", c);
					if (esStringValid(unitat)) {
						mc.setEntity(extractUserUnitat(unitat));
					}
				}
			} catch (Exception e) {
			}
			i++;
		}
		return unitat;
	}

	public static String extractUserUnitat(String unit) {
		if (unit == null || "".equals(unit))
			return "";
		String unitat = unit.trim();
		if (unitat.length() == 7 && unitat.indexOf("8") == 0) {
			return "a" + unitat;
		} else if (unitat.indexOf("1") == 0) {
			return "b" + unitat.substring(1);
		} else if (unitat.indexOf("2") == 0) {
			return "c" + unitat.substring(1);
		} else if (unitat.indexOf("4") == 0) {
			return "e" + unitat.substring(1);
		}

		return unitat;
	}

	public String parseCRP(String entity) {
		String crp = "";
		String listPossibleNames = getMessage(locale, "mediateca.crp.conversio");
		String[] lpn = listPossibleNames.split(";");
		boolean found = false;
		if (entity == null || "".equals(entity))
			return "";
		crp = converteixString(entity).replaceAll("%de%", "%").replaceAll("%el%", "%").replaceAll("%l%", "%")
				.replaceAll("%d%", "%").replaceAll("%la%", "%").replaceAll("%a%", "%").replaceAll("(%)+", "%");
		int i = 0;
		while (i < lpn.length && !found) {
			if (crp.indexOf(converteixString(lpn[i])) >= 0) {
				crp = crp.replaceAll(converteixString(lpn[i]), "CRP");
				found = true;
			}
			i++;
		}

		return crp;
	}

	private ArrayList parseFormat(String format, Connection c) throws MerliDBException {
		ArrayList l = new ArrayList();
		if (format == null)
			return l;
		String form;
		String formatAct = "";
		String[] formats = format.split("[,;:]");
		for (int i = 0; i < formats.length; i++) {
			if (formats[i] != null) {
				formatAct = formats[i].trim();
				if (!"".equals(formatAct)) {
					form = AccesBD.executeQuery("SELECT  id_format_fisic " + "FROM mer_formats_fisics "
							+ "WHERE UPPER(tipus) like UPPER('" + converteixString(formatAct) + "') "
							+ "GROUP BY id_format_fisic", "id_format_fisic", c);

					if (form == null || form.length() < 1 || "0".equals(form)) {
						// Fem un insert del nou format.
						try {
							form = AccesBD.executeQuery("SELECT  max(id_format_fisic) as max "
									+ "FROM mer_formats_fisics ", "max", c);
							insertQuery("INSERT INTO MER_FORMATS_FISICS VALUES (" + (Integer.parseInt(form) + 1)
									+ ", '" + formatAct + "','')", c);
						} catch (MerliDBException e) {
							saveError(format, c, 0, "Error Insert format:" + formatAct);
						}
						saveError("format", c, 0, "Tipus id - " + formatAct);
					} else {
						l.add(form);
					}
				}
			}
		}
		return l;
	}

	private ArrayList parseLanguages(String language, Connection c) throws MerliDBException {
		ArrayList l = new ArrayList();
		if (language == null)
			return l;
		String lang;
		String[] langs = language.replaceAll(" i ", "&").split("[,;&]");
		for (int i = 0; i < langs.length; i++) {
			if (langs[i] != null && !"".equals(langs[i].trim())) {
				lang = AccesBD.executeQuery("SELECT  id_llengua " + "FROM mer_llengua "
						+ "WHERE UPPER(llengua_cat) like UPPER('" + converteixString(langs[i]) + "') "
						+ "GROUP BY id_llengua", "id_llengua", c);

				if (lang == null || lang.length() < 2) {
					lang = AccesBD.executeQuery("SELECT  id_llengua " + "FROM mer_llengua " + "WHERE id_llengua = '"
							+ converteixString(langs[i]).substring(0, Math.min(2, langs[i].length())) + "' "
							+ "GROUP BY id_llengua", "id_llengua", c);
				}
				if (lang != null && lang.length() > 1) {
					l.add(lang);
				}
			}
		}
		return l;
	}

	public String parseDataCreacio(Map m, int i) {
		String dd = getString(m, i, "DD");
		if (dd == null)
			return null;

		String[] spl = dd.split("[.,;]");
		String data = getMessage(locale, "mediateca.data.catalogacio");

		if (spl == null)
			return null;

		if (spl.length > 1) {
			data = spl[spl.length - 1].trim().replaceAll(" de ", "");
			data = parseMesosTextuals(data, m, i);
		}
		return data;
	}

	public void parseContribucioDD(Map m, int i, RecursMerli r) {
		String dd = getString(m, i, "DD");
		if (dd == null)
			return;

		String[] spl = dd.split("[.,;]");
		String centre = getMessage(locale, "mediateca.unitat.catalogacio");
		String data = getMessage(locale, "mediateca.data.catalogacio");

		if (spl == null)
			return;

		if (spl.length > 0) {
			centre = spl[0];
		}
		if (spl.length > 1) {
			data = spl[spl.length - 1].trim().replaceAll(" de ", "");
			data = parseMesosTextuals(data, m, i);
		}
		if (esStringValid(centre))
			r.addContribution(new MerliContribution(3, centre, data));

		if (spl.length > 2) {
			for (int j = 1; j < spl.length - 1; j++) {
				if (esStringValid(spl[j]))
					r.addContribution(new MerliContribution(3, spl[j].trim(), data));
			}
		}
	}

	private String parseMesosTextuals(String data, Map m, int i) {
		String datares = null;
		if (data == null)
			return "1-1-2010";
		try {
			datares = data.replaceAll("/", "-");
			int mes = 0;

			if (data.matches(DATE_MATCH)) {
				return data;
			}
			while (mes < mesos.length && datares == null) {
				if (converteixString(data).indexOf(converteixString(mesos[mes])) >= 0)
					datares = "1-" + (mes + 1) + "-" + data.substring(data.length() - 4);
				mes++;
			}
			if (datares == null) {
				return getString(m, i, "DATA_FITXA");
			} else {
				return datares;
			}
		} catch (Exception e) {
			return "1-1-2010";
		}
	}

	private void ifContextAddIdNivellCat(String camp, Map m2, ArrayList l, Connection c) throws MerliDBException {
		String aux;
		if (getString(m2, 0, camp) != null) {
			aux = AccesBD.executeQuery("SELECT  id_nivell_cat " + "FROM mer_nivell_edu_cat "
					+ "WHERE id_nivell_cat = '" + getString(m2, 0, camp) + "' " + "GROUP BY id_nivell_cat",
					"id_nivell_cat", c);

			if (aux == null || "0".equals(aux) || "".equals(aux)) {
				saveError(camp, c, 0, "context - " + getString(m2, 0, camp));
			} else {
				if (notIsInList(aux, l))
					l.add(aux);
			}
		}
	}

	private String recuperaEdatMinima(ArrayList l, Connection c) {
		String aux = "0";
		try {
			aux = AccesBD.executeQuery("SELECT min(edat_min) as min " + "FROM mer_nivell_edu_cat "
					+ "WHERE id_nivell_cat in (" + l.toString() + ")", "min", c);
		} catch (MerliDBException e) {
		}

		return aux;
	}

	private void ifDestinatariaAddIdRolUsuari(String camp, Map m2, ArrayList l, Connection c) throws MerliDBException {
		String aux;
		if (getString(m2, 0, camp) != null) {
			aux = AccesBD.executeQuery("SELECT  id_rol_usuari " + "FROM mer_rol_usuari "
					+ "WHERE UPPER(rol_usuari_cat) like UPPER('" + converteixString(getString(m2, 0, camp)) + "') "
					+ "GROUP BY id_rol_usuari", "id_rol_usuari", c);

			if (aux == null || "0".equals(aux) || "".equals(aux)) {
				saveError(camp, c, 0, "destinatari - " + getString(m2, 0, camp));
			} else {
				if (notIsInList(aux, l))
					l.add(aux);
			}
		}
	}

	private boolean notIsInList(Object aux, ArrayList l) {
		for (int i = 0; i < l.size(); i++) {
			if (aux.equals(l.get(i)))
				return false;
		}
		return true;
	}

	private String converteixString(String input) {
		if (input == null)
			return "";
		return input.toLowerCase().trim().replaceAll("[^a-zA-Z]", "%");
	}

	private String converteixEspais(String input) {
		if (input == null)
			return "";
		return input.trim().replaceAll(" ", "_");
	}

	private String converteixStringStripAccents(String input) {
		if (input == null)
			return "";
		input = input.trim();
		input = input.replaceAll("[àáä]", "a");
		input = input.replaceAll("[èéë]", "e");
		input = input.replaceAll("[ìíï]", "i");
		input = input.replaceAll("[òóö]", "o");
		input = input.replaceAll("[ùúü]", "u");
		input = input.replaceAll("'", "''");
		return input;
	}

	private String getString(Map m, int i, String name) {
		if (m != null && name != null && m.get(name) != null && ((ArrayList) m.get(name)).size() > i
				&& ((ArrayList) m.get(name)).get(i) != null)
			return ((ArrayList) m.get(name)).get(i).toString();
		else
			return null;
	}

	private int parseEstat(Map m, int i) {

		int idestat = MerliBean.ESTAT_M_PENDENT;
		String estat = "";
		try {
			estat = getString(m, i, "ESTAT");
			if ("0".equals(estat)) {
				idestat = MerliBean.ESTAT_M_EN_PROCES;
			} else if ("1".equals(estat)) {
				idestat = MerliBean.ESTAT_M_EN_PROCES;
			} else if ("2".equals(estat)) {
				idestat = MerliBean.ESTAT_M_PUBLICAT;
			} else if ("3".equals(estat)) {
				idestat = -3;
			}
		} catch (NullPointerException nep) {
		}

		return idestat;
	}

	public int getNumeroPendents() {
		return getNumeroRecursosMediateca(0);
	}

	public int getNumeroARevisar() {
		return getNumeroRecursosMediateca(-1);
	}

	public int getNumeroMigrats() {
		return getNumeroRecursosMediateca(1);
	}

	private int getNumeroRecursosMediateca(int migracio) {
		int res = 0;
		try {
			ConnectionBean cbc = connectBD();
			Connection c = cbc.getConnection();
			ArrayList lCond = new ArrayList();
			lCond.add(String.valueOf(migracio));
			res = AccesBD.executeCount("mediateca_migracio", "migrada = ?", lCond, c);
			disconnectBD(cbc);
		} catch (Exception e) {
			res = 0;
		}
		return res;
	}

	public void executeDisponibilitat() {
		try {
			ConnectionBean cbc = connectBD();
			Connection c = cbc.getConnection();

			String query = "INSERT INTO MER_REC_DISP_UNI SELECT m.ID_REC, TO_NUMBER(tc.CODI_CRP) FROM T_CRPNU tc, MER_REC_MEDIATECA m WHERE tc.NU = m.NU AND 0 = (SELECT COUNT(*) FROM MER_REC_DISP_UNI du WHERE du.id_Rec = m.id_rec AND du.ID_UNITAT = TO_NUMBER(tc.codi_crp))";
			insertQuery(query, c);

			c.commit();
			c.close();
			disconnectBD(cbc);
		} catch (Exception e) {
		}
	}

	private void insertQuery(String query, Connection c) throws MerliDBException {
		PreparedStatement pstInsert = null;
		try {
			pstInsert = c.prepareStatement(query);
			pstInsert.execute();
			pstInsert.close();
		} catch (SQLException e) {
			logger.warn("Error on insertQuery DDBB:" + e.getMessage());
			try {
				if (pstInsert != null)
					pstInsert.close();
			} catch (SQLException e1) {
				logger.warn("Error connecting to DDBB:" + e1.getMessage());
				e1.printStackTrace();
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}
	}

	public void executeRelacions() {
		ConnectionBean cbc = null;
		Connection c = null;

		// Connection cins = null;
		Statement stmt = null;
		ResultSet rs = null;
		Map m = new LinkedHashMap();
		String relacioNS = getMessage(locale, "mediateca.relacio.ns");
		String relacioNI = getMessage(locale, "mediateca.relacio.ni");
		int idrec;
		String ni, ns, nu;
		logger.info("Inici Relacions");
		try {
			cbc = connectBD();
			c = cbc.getConnection();
			// cins = cbc.getConnection();
			// Statement stmt=null;
			try {
				stmt = c.createStatement();
				rs = stmt.executeQuery("SELECT RM.id_rec as idrec, m.nu , m.ni, m.ns "
						+ "FROM MEDIATECA_MIGRACIO  m, MER_REC_MEDIATECA RM "
						+ "WHERE RM.NU=m.nu AND (m.NI LIKE '%[%]%' OR m.NS LIKE '%[%]%')");
				while (rs.next()) {
					idrec = Integer.parseInt(rs.getString("idrec"));
					nu = rs.getString("nu");
					ns = rs.getString("ns");
					ni = rs.getString("ni");
					logger.info("--> Inici recurs " + idrec);
					if (ns != null) {
						parseNus(idrec, ns, relacioNS, c);
					}
					if (ni != null) {
						parseNus(idrec, ni, relacioNI, c, relacioNS, nu);
					}
					logger.info("--> Fi recurs " + idrec);
					c.commit();
				}
				rs.close();
				stmt.close();
			} catch (SQLException s) {
				try {
					rs.close();
					stmt.close();
				} catch (SQLException e1) {
					logger.warn("Error connecting to DDBB:" + e1.getMessage());
					throw e1;
				}
				throw s;
			}
			// cins.commit();
			// cins.close();
			c.commit();
			c.close();
			disconnectBD(cbc);

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			logger.info("Fi Relacions");
		}
	}

	public ArrayList parseNus(int idrec, String nus, String relacio, Connection cins) {
		return parseNus(idrec, nus, relacio, cins, null, null);
	}

	public ArrayList parseNus(int idrec, String nus, String relacio, Connection cins, String relacioSup,
			String nuOriginal) {

		ArrayList res = new ArrayList();
		String[] ids;
		// String query;
		nus = nus.replaceAll("Número ", "").replaceAll("Num. ", "").replaceAll("Num ", "");
		ids = nus.split("[\\[[.*]\\]]+");

		for (int i = 0; i < ids.length; i++) {
			if (ids[i].matches("[A-Z]+[0-9]+")) {
				logger.info("-----> Relacio trobada. idrec=" + idrec + ", nu=" + ids[i] + ", relacio=" + relacio);
				insertNUsQuery(idrec, nus, relacio, cins, ids[i]);
				// if (relacioSup != null) {
				// insertNUsQueryInvers(idrec, nus, relacioSup, cins, ids[i],
				// nuOriginal);
				// }
			}
		}

		return res;
	}

	private void insertNUsQuery(int idrec, String nus, String relacio, Connection cins, String nu) {
		String query = "INSERT INTO MER_RELACIO_RECURSOS VALUES (" + idrec + ", "
				+ "(SELECT ID_REC FROM MER_REC_MEDIATECA WHERE NU=" + "'" + nu + "'), " + "'" + relacio
 + "', NULL)";

		try {
			insertQuery(query, cins);
			logger.info("-----> Relacio insertada. idrec=" + idrec + ", nu=" + nu + ", relacio=" + relacio);

		} catch (MerliDBException e) {
			saveError(nu, cins, idrec, "Relacions :" + nus);
		} catch (Exception e2) {
			saveError(nu, cins, idrec, "Relacions :" + nus);
		}
	}

	private void insertNUsQueryInvers(int idrec, String nus, String relacio, Connection cins, String nu,
			String nuOriginal) {
		String query = "INSERT INTO MER_RELACIO_RECURSOS VALUES (" + "(SELECT ID_REC FROM MER_REC_MEDIATECA WHERE NU="
				+ "'" + nu + "'), " + idrec + ", " + "'" + relacio + "', " + "'NU: ["
				+ nuOriginal.replaceAll("'", "''") + "]')";

		try {
			insertQuery(query, cins);
			logger.info("-----> Relacio inversa insertada. idrec=" + idrec + ", nu=" + nu + ", relacio=" + relacio);

		} catch (MerliDBException e) {
			saveError(nu, cins, idrec, "Relacions :" + nus);
		} catch (Exception e2) {
			saveError(nu, cins, idrec, "Relacions :" + nus);
		}
	}

}
