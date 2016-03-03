package edu.xtec.merli.basedades;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import edu.xtec.merli.MerliBean;
import edu.xtec.merli.MerliContribution;
import edu.xtec.merli.RecursMerli;
import edu.xtec.merli.Unitat;
import edu.xtec.merli.segur.User;
import edu.xtec.merli.utils.Utility;
import edu.xtec.semanticnet.Node;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

public class RecursBD {
	protected static ConnectionBeanProvider broker;
	
	public static final int IDENTIFIERS = -1;
	public static final int LABELS = -2;	
	public static final int MAXIM = -3;
	public static final int MINIM = -4;	
	public static final int VEU_RECURS_LLICENCE = -5;

	public static final int ESCOLAR = 1;
	public static final int ESPECIAL = 2;
	public static final int ADMINISTRATIU = 3;
	
	private static final String APPLICATION_RESOURCES = "MediatecaResources";
	
	private static final Logger logger = Logger.getRootLogger();


	/**
	 * Sol·licita un ConnectionBean al ConnectionBeanProvider, si aquest no està innicialitzat, ho fa.
	 * @return ConnectionBean, servit pel ConnectionBeanProvider.
	 * @throws MerliDBException
	 */
	private ConnectionBean connectBD() throws MerliDBException{
		//Inicialitza el CBP si no ho està.
		ConnectionBean bd = null;

		try{
			if (broker == null){
				broker = ConnectionBeanProvider.getConnectionBeanProvider(true, Utility.loadProperties("/","database.properties"));
			}
			bd = broker.getConnectionBean();	
			if (bd == null)
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}catch(Exception e){
			logger.warn("Error on connection with DDBB->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);}
		//Retorna un ConnectionBean.
		return bd;
	}

	/**
	 * Es desconnecta de la BD.
	 * @throws MerliDBException
	 */
	private void disconnectBD(ConnectionBean cb) throws MerliDBException{
		//Allibera el connectionBean utilitzat.
		try{
		broker.freeConnectionBean(cb);
		}catch(Exception e){throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);}
	}

	/**
	 * @param user 
	 * @param tipus
	 * @return
	 * @throws MerliDBException 
	 * /
	public ArrayList getLlistatRecursos(String tipus) throws MerliDBException {
		Map m;
		RecursMerli r;
		ArrayList l1, l2, lelem= new ArrayList(),lres = new ArrayList();
		ConnectionBean cb = null;
		
		l1 = new ArrayList();
		l1.add("id_estat");
		l1.add("v_responsable");
		
		lelem.add("id_rec");
		lelem.add("titol");
		lelem.add("descripcio");
		lelem.add("url");
		lelem.add("versio");
		lelem.add("edat_min");
		lelem.add("edat_max");
		lelem.add("id_dificultat");
		lelem.add("duracio");
		lelem.add("drets");
		lelem.add("id_rol_usuari");
		lelem.add("confirmat");
		lelem.add("id_ambit"); 
		try {
			cb = connectBD();
			m =  AccesBD.getJoin(lelem,l1,"mer_recurs","mer_rec_info","id_rec","","","id_rec","","DESC",cb.getConnection());
			//m = AccesBD.getFullLlistat("mer_recurs",lelem,"id_rec DESC",cb.getConnection());
			for (int i = 0;i<((ArrayList)m.get("id_rec")).size();i++){
				r = new RecursMerli(((BigDecimal)((ArrayList)m.get("id_rec")).get(i)).intValue());
				r.setTitle(((ArrayList)m.get("titol")).get(i).toString());
				r.setDescription(((ArrayList)m.get("descripcio")).get(i).toString());
				r.setUrl(((ArrayList)m.get("url")).get(i).toString());
				r.setEstat(((ArrayList)m.get("id_estat")).get(i).toString());
				r.setResponsable(((ArrayList)m.get("v_responsable")).get(i).toString());
				lres.add(r);
			}
			disconnectBD(cb);
		} catch (NullPointerException ne){
			disconnectBD(cb);
			logger.warn("Can't create FullLlistat of Recursos.->"+ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Can't creating Recursos.->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}

		return lres;

	}
*/
	public boolean setRecurs(String idRecurs, RecursMerli recursMerli, User user) throws MerliDBException {
		ConnectionBean cb = connectBD();
		ArrayList lRec = new ArrayList();
		int idRec = new Integer(idRecurs).intValue();
		lRec.add(new Integer(idRecurs));
		try {
			/*UPDATE recurs a MER_RECURS*/
			AccesBD.executeUpdate("mer_recurs","id_rec=?",lRec,recursMerli.getSetQuery("mer_recurs"),recursMerli.getSetQueryFields("mer_recurs"),cb.getConnection());

			/* Desar camps de text en els diversos idiomes. No estaran mai validats.*/
			desarCampsTextuals(recursMerli, idRec, true, cb.getConnection());
			
			/*INSERT nous valors a MER_REC_LLENGUA*/
			AccesBD.executeDelete("mer_rec_llengua","id_rec = ?",lRec,cb.getConnection());
			insertLlengues(recursMerli, cb.getConnection());

			/*INSERT nous valors a MER_REC_NIVELL_EDUCATIU*/
			AccesBD.executeDelete("mer_rec_nivell_educatiu","id_rec = ?",lRec,cb.getConnection());
			insertNivellEducatiu(recursMerli, cb.getConnection());
			
			/*INSERT nous valors a MER_REC_TERMES*/
			AccesBD.executeDelete("mer_rec_termes","id_rec = ?",lRec,cb.getConnection());		
			insertTermes(recursMerli, cb.getConnection());
			
			/*INSERT valors a MER_REC_PARAULES*/
			AccesBD.executeDelete("mer_rec_paraules","id_rec = ?",lRec,cb.getConnection());
			insertParaules(recursMerli, cb.getConnection());
		
			/*INSERT nous valors a MER_REC_CURRICULUM*/
			AccesBD.executeDelete("mer_rec_curriculum","id_rec = ?",lRec,cb.getConnection());		
			insertCurriculum(recursMerli, cb.getConnection());

			/*INSERT nous valors a MER_REC_ROL_USUARI*/
			AccesBD.executeDelete("mer_rec_rol_usuari","id_rec = ?",lRec,cb.getConnection());
			insertDestinataris(recursMerli, cb.getConnection());
			
			/*INSERT nous valors a MER_REC_TIPUS_RECURS*/
			AccesBD.executeDelete("mer_rec_tipus_recurs","id_rec = ?",lRec,cb.getConnection());		
			insertTipusRecurs(recursMerli, cb.getConnection());
			
			/*INSERT nous valors a MER_REC_FORMAT i MER_REC_FORMAT_FISIC*/
			AccesBD.executeDelete("mer_rec_format","id_rec = ?",lRec,cb.getConnection());
			AccesBD.executeDelete("mer_rec_format_fisic","id_rec = ?",lRec,cb.getConnection());	
			insertFormats(recursMerli, cb.getConnection());
			
			/*INSERT nous valors a MER_REC_AMBITS*/
			AccesBD.executeDelete("mer_rec_ambits","id_rec = ?",lRec,cb.getConnection());
			insertAmbit(recursMerli, cb.getConnection());			

			/*UPDATE taula de MER_REC_INFO*/
			updateInfo(recursMerli, cb.getConnection());
			
			/*INSERT taula de MER_DRETS*/
			updateDrets(recursMerli, cb.getConnection());
			
			/*INSERT valors a MER_CONTRIBUCIO*/
			updateContribucions(recursMerli, cb.getConnection());

			/*INSERT taula de MER_RELACIO_RECURSOS*/
			AccesBD.executeDelete("mer_relacio_recursos","recurs1 = ?",lRec,cb.getConnection());	
			insertRelacions(recursMerli, cb.getConnection());

			/*INSERT taula de MER_RECURS_FISIC, MER_IDFISIC i MER_REC_DISP_UNI*/
			updateFisic(recursMerli, cb.getConnection(),user);
			
			publicacioAgrega(recursMerli, cb.getConnection());

		} catch (Exception e) {
			e.printStackTrace();
			try {
				cb.getConnection().rollback();
				logger.warn("Error on setRecurs ->"+e.getMessage());
				throw new MerliDBException(MerliDBException.DELETEERROR);
			} catch (SQLException e1) {
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}finally{
			disconnectBD(cb);
		}
		return true;
	}

	/**
	 * @param recursMerli
	 * @param idRec
	 * @throws MerliDBException
	 * @throws SQLException
	 */
	private void desarCampsTextuals(RecursMerli recursMerli, int idRec) throws MerliDBException, SQLException {
		desarCampsTextuals(recursMerli, idRec, false);
	}
	
	private void desarCampsTextuals(RecursMerli recursMerli, int idRec, boolean bOnlyCatalan) throws MerliDBException, SQLException {
		salvarTextos(idRec,"ca",recursMerli.getTitle(), recursMerli.getDescription(),recursMerli.getRightsDesc(),false);
		if (!bOnlyCatalan){
			salvarTextos(idRec,"es",recursMerli.getTitleEs(), recursMerli.getDescriptionEs(),recursMerli.getRightsDescEs(),false);
			salvarTextos(idRec,"en",recursMerli.getTitleEn(), recursMerli.getDescriptionEn(),recursMerli.getRightsDescEn(),false);					
		}
	}

	private void desarCampsTextuals(RecursMerli recursMerli, int idRec, boolean bOnlyCatalan, Connection c) throws MerliDBException, SQLException {
		salvarTextos(idRec,"ca",recursMerli.getTitle(), recursMerli.getDescription(),recursMerli.getRightsDesc(),false, c);
		if (!bOnlyCatalan){
			salvarTextos(idRec,"es",recursMerli.getTitleEs(), recursMerli.getDescriptionEs(),recursMerli.getRightsDescEs(),false, c);
			salvarTextos(idRec,"en",recursMerli.getTitleEn(), recursMerli.getDescriptionEn(),recursMerli.getRightsDescEn(),false, c);					
		}
	}

	public boolean addRecurs(RecursMerli recursMerli) throws MerliDBException {
		ConnectionBean cb = null;
		try{
			cb = connectBD();
			Connection c = cb.getConnection();
			return addRecurs(recursMerli, c);
		}finally{
				disconnectBD(cb);
			}
		}
	
	public boolean addRecurs(RecursMerli recursMerli, Connection c) throws MerliDBException {
		
		try {
			logger.info("recuperant sequencia...");
			int idRecurs = AccesBD.getNext("seq_merli", c);
			logger.info("end recuperant sequencia...");
			recursMerli.setIdRecurs(idRecurs);

			/*INSERT recurs a MER_RECURS*/
			logger.info("insertant mer_recurs...");
			AccesBD.executeInsert("mer_recurs",recursMerli.getAddQuery(),c);
			logger.info("end mer_recurs...");
			
			/* Desar camps de text en els diversos idiomes. No estaran mai validats.*/
			desarCampsTextuals(recursMerli, idRecurs, false, c);
			
			/*INSERT valors a MER_REC_LLENGUA*/
			logger.info("insertant llengues...");
			insertLlengues(recursMerli, c);
			logger.info("end insertant llengues...");
			
			/*INSERT valors a MER_REC_NIVELL_EDUCATIU*/
			logger.info("insertant nivell educatiu...");
			insertNivellEducatiu(recursMerli, c);
			logger.info("end nivell educatiu...");

			/*INSERT valors a MER_REC_TERMES*/
			logger.info("insertant termes...");
			insertTermes(recursMerli, c);
			logger.info("end insertant termes...");

			/*INSERT valors a MER_REC_PARAULES*/
			logger.info("insertant paraules...");
			insertParaules(recursMerli, c);
			logger.info("end insertant paraules...");

			/*INSERT valors a MER_REC_CURRICULUM*/
			logger.info("insertant curriculum...");
			insertCurriculum(recursMerli, c);
			logger.info("end insertant curriculum...");

			/*INSERT valors a MER_REC_ROL_USUARI*/
			logger.info("insertant rol usuari...");
			insertDestinataris(recursMerli, c);
			logger.info("end insertant rol usuari...");
			
			/*INSERT valors a MER_REC_TIPUS_RECURS*/
			logger.info("insertant tipus recurs...");
			insertTipusRecurs(recursMerli, c);
			logger.info("end insertant tipus recurs...");
			
			/*INSERT valors a MER_REC_FORMAT i MER_REC_FORMAT_FISIC*/
			logger.info("insertant formats...");
			insertFormats(recursMerli, c);
			logger.info("end insertant formats...");

			/*INSERT valors a MER_REC_AMBIT*/
			logger.info("insertant ambit...");
			insertAmbit(recursMerli, c);
			logger.info("end insertant ambit...");
			
			/*INSERT taula de MER_REC_INFO*/
			logger.info("insertant rec_info...");
			insertInfo(recursMerli, c);
			logger.info("end insertant rec_info...");
			
			/*INSERT valors a MER_CONTRIBUCIO*/
			logger.info("insertant contribucio...");
			insertContribucions(recursMerli, c);
			logger.info("end insertant contribucio...");
			
			/*INSERT taula de MER_DRETS*/
			logger.info("insertant drets...");
			insertDrets(recursMerli, c);
			logger.info("end insertant drets...");
			
			/*INSERT taula de MER_RELACIO_RECURSOS*/
			logger.info("insertant relacio recursos...");
			insertRelacions(recursMerli, c);
			logger.info("end insertant relacio recursos...");
			
			/*INSERT taula de MER_RECURS_FISIC, MER_IDFISIC i MER_REC_DISP_UNI*/
			logger.info("insertant fisic, id_fisic...");
			insertFisic(recursMerli, c);
			logger.info("end insertant fisic, id_fisic...");
			
			if (recursMerli.isAgregaSend())
				publicacioAgrega(recursMerli, c);
			
		} catch (Exception e) {
			logger.error("Error a l'afegir recurs ->"+e.toString());
			try {
				c.rollback();
				throw new MerliDBException(MerliDBException.DELETEERROR);
			} catch (SQLException e1) {
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}

		return true;
	}
	
	

	private void insertLlengues(RecursMerli recursMerli, Connection c)
	{
		ArrayList al, alcon;
		int idRecurs = recursMerli.getIdRecurs();
		al = recursMerli.getLanguage();
		if (!al.isEmpty())
		{
			for (int i = 0; i< al.size(); i++)
			{
				alcon = new ArrayList();
				alcon.add(new Integer(idRecurs));
				alcon.add(al.get(i));
				try{
					AccesBD.executeInsert("mer_rec_llengua",alcon,c);//idRecurs+",'"+al.get(i)+"'"
				}
				catch(Exception e){
					logger.warn("Llengua amb error:"+ al.get(i) + " ->"+e.getMessage());
				}
			}
		}
	}

	private void insertNivellEducatiu(RecursMerli recursMerli, Connection c)
	{
		ArrayList al, alcon;
		int idRecurs = recursMerli.getIdRecurs();
		al = recursMerli.getContext();
		if (!al.isEmpty())
		{
			for (int i = 0; i< al.size(); i++){
				alcon = new ArrayList();
				alcon.add(new Integer(idRecurs));
				alcon.add(al.get(i));
				try{
					AccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
				}catch(Exception e){
					logger.warn("Nivell educatiu amb error:"+ al.get(i) + " ->"+e.getMessage());
				}
			}
		}
	}
	
	private void insertTermes(RecursMerli recursMerli, Connection c)
	{
		ArrayList al, alcon;
		int idRecurs = recursMerli.getIdRecurs();
		al = recursMerli.getTaxon();
		for (int i = 0; i< al.size(); i++){
			alcon = new ArrayList();
			alcon.add(new Integer(idRecurs));
			alcon.add(al.get(i));
			alcon.add("ETB");
			try{
				AccesBD.executeInsert("mer_rec_termes",alcon,c);//idRecurs+","+al.get(i)+",'ETB'"
			}catch(Exception e){
				logger.warn("Termes amb error:"+ al.get(i) + " ->"+e.getMessage());
			}
		}
	}
	
	private void insertParaules(RecursMerli recursMerli, Connection c) throws MerliDBException
	{
		ArrayList al, alcon;
		int idRecurs = recursMerli.getIdRecurs();
		al = recursMerli.getParaulesId();	
		if (al!=null)
		for (int i = 0; i< al.size(); i++){
			String aux = (String) al.get(i);
			if (aux!=null && !"".equals(aux)){
				aux = aux.trim();
				//Sino és un identificador, 
				if (!aux.matches("[0-9]+")){
					aux = checkParaulaOberta(aux, recursMerli.getResponsable(), c);
				}
				if (aux!=null && !"".equals(aux) && aux.matches("[0-9]+")){
					alcon = new ArrayList();
					alcon.add(new Integer(idRecurs));
					
					alcon.add(aux);
					try{
						AccesBD.executeInsert("mer_rec_paraules",alcon,c);//idRecurs+","+al.get(i)+",'ETB'"
					}catch(Exception e){
						logger.warn("Paraula amb error:"+ al.get(i) + " ->"+e.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * Comprova si la paraula donada existeix. En cas de no existir la crea.
	 * Retorna l'identificador de la paraula.
	 * @param paraula Paraula a recuperar.
	 * @param responsable Usuari creador de la paraula sino existeix.
	 * @param c connexio.
	 * @return String identificador de la paraula.
	 * @throws MerliDBException
	 */
	private String checkParaulaOberta(String paraula, String responsable, Connection c) throws MerliDBException {
		String id;
		ArrayList alcon = new ArrayList();
		//comprovem la paraula.
		if (paraula == null || "".equals(paraula.trim())) return null;
		
		//comprova si la paraula existeix
		id = AccesBD.executeQuery("" +
				"SELECT  id_paraula FROM mer_paraules WHERE" +
				" v_paraula_pla LIKE UPPER('"+Utility.aplanarText(paraula).toUpperCase()+"')" +
				" GROUP BY id_paraula", "id_paraula", c);
		//Sino existeix la insereix a BBDD
		if (id==null || "0".equals(id)){
			alcon.clear();
			try{
				id = String.valueOf(AccesBD.getNext("SEQ_MERLI", c));
			}catch(Exception e){
				try{
				id = AccesBD.executeQuery("SELECT max(id_paraula) as maxim FROM mer_paraules",
							"maxim", c);
				id = String.valueOf(Integer.parseInt(id)+1);
				}catch(Exception e2){
					id = "1";
				}
			}
			alcon.add(id);
			if (paraula.length()>95){
				paraula = paraula.substring(0,89)+"[...]";
			}
			alcon.add(paraula);
			alcon.add(Utility.aplanarText(paraula).toUpperCase());
			alcon.add(responsable);
			Calendar c1=new GregorianCalendar();
			alcon.add(c1.getTime());
			AccesBD.executeInsert("mer_paraules", alcon, c);
		}
		return id;
	}

	
	private void publicacioAgrega(RecursMerli recursMerli, Connection c) throws MerliDBException {
		MerliBean mb = new MerliBean();
		boolean publica = false;
		try {			
			if (mb.calPublicarAgrega(recursMerli.isEstatEs(), recursMerli.isAgregaSend(), recursMerli.getAgregaDate()))
				publica=true;

			mb.setSendAgrega(recursMerli.getIdRecurs(), "1".equals(recursMerli.getAgregaSend()), recursMerli.getResponsable(),publica, c);
		} catch (MerliDBException e) {
			logger.error("Error a l'afegir publicacio AGREGA ->"+e.toString());
			throw e;
		}
	}

	public boolean delRecurs(int idRecurs) throws MerliDBException {
		ConnectionBean cb = connectBD();
		ArrayList l = new ArrayList();
		l.add(new Integer(idRecurs));

		Connection c = cb.getConnection();
		try {
			AccesBD.executeDelete("mer_rec_llengua","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_nivell_educatiu","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_format","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_ambits","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_termes","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_paraules","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_rol_usuari","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_tipus_recurs","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_mediateca","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_format_fisic","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_contribucio","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_info","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_drets","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_curriculum","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_rec_lang","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_idfisic", "id_rec = ?", l, c);
			AccesBD.executeDelete("mer_rec_disp_uni", "id_rec = ?", l, c);
			AccesBD.executeDelete("mer_recurs_fisic", "id_recurs = ?", l, c);	
			AccesBD.executeDelete("mer_relacio_recursos", "recurs1 = ?", l, c);	
			AccesBD.executeDelete("mer_relacio_recursos", "recurs2 = ?", l, c);	
			AccesBD.executeDelete("mer_rec_agrega","id_rec = ?",l,c);
			AccesBD.executeDelete("mer_recurs","id_rec = ?",l,c);
			} catch (MerliDBException e) {
			try {
				cb.getConnection().rollback();
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.DELETEERROR);
			} catch (SQLException e1) {
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}
		disconnectBD(cb);

		return true;
	}
	
	/**
	 * Marca com a disponible el recurs identificat per idRecurs a la unitat a la qual està associat l'usuari
	 * Això és, crea una entrada a la taula mer_rec_disp_uni amb el recurs i la unitat pertinents
	 * 
	 * @param idRecurs, recurs a marcar 
	 * @param usuari, s'assignarà el recurs a la unitat associada a l'usuari
	 * @return true
	 * @throws MerliDBException
	 */
	public boolean marcaDisponible(int idRecurs, String usuari) throws MerliDBException {
		ConnectionBean cb = connectBD();
		Connection c = cb.getConnection();
		ArrayList alcon = new ArrayList();
		alcon.add(usuari);
		try {
			String idUnitat=AccesBD.executeQuery("Select id_unitat from mer_users where v_user=?",alcon, "id_unitat", c);
			alcon.clear();
			alcon.add(String.valueOf(idRecurs));
			alcon.add(idUnitat);
			AccesBD.executeInsert("mer_rec_disp_uni",alcon,c);
		} catch (MerliDBException e) {
			try {
				cb.getConnection().rollback();
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.DELETEERROR);
			} catch (SQLException e1) {
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}
		disconnectBD(cb);
		return true;
	}

	/**
	 * Marca com a no disponible el recurs identificat per idRecurs a la unitat a la qual esta associat l'usuari
	 * Aixo es, elimina l'entrada de la taula mer_rec_disp_uni amb el recurs i la unitat pertinents
	 * En cas que la unitat sigui la creadora, es mostra un missatge de confirmacio
	 * 
	 * @param idRecurs, recurs a marcar 
	 * @param usuari, s'assignara el recurs a la unitat associada a l'usuari
	 * @return true
	 * @throws MerliDBException
	 */
	public boolean marcaNoDisponible(int idRecurs, String usuari) throws MerliDBException {
		ConnectionBean cb = connectBD();
		Connection c = cb.getConnection();
		ArrayList alcon = new ArrayList();
		boolean esborrar=true;
		Locale locale = null;
		alcon.add(usuari);
		try {
			String idUnitat=AccesBD.executeQuery("Select id_unitat from mer_users where v_user=?",alcon, "id_unitat", c);
			alcon.clear();
			alcon.add(String.valueOf(idRecurs));
			alcon.add(idUnitat);
			//if(AccesBD.executeExist("mer_recurs_fisic", "id_recurs=? and id_unitat_creadora=?", alcon, c)>0)
				//esborrar=JOptionPane.showConfirmDialog(null, MessageResources.getMessageResources("ApplicationResources").getMessage(locale,"etiq.op.disp"))==JOptionPane.OK_OPTION;
			//if(esborrar)
			AccesBD.executeDelete("mer_rec_disp_uni","id_rec=? and id_unitat=?",alcon,c);
		} catch (MerliDBException e) {
			try {
				cb.getConnection().rollback();
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.DELETEERROR);
			} catch (SQLException e1) {
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}
		disconnectBD(cb);
		return true;
	}
	
	public RecursMerli getRecurs(int idRecurs) throws MerliDBException {
		RecursMerli rm = new RecursMerli(idRecurs);
		ArrayList l = new ArrayList();
		ArrayList lRec = new ArrayList();
		ArrayList lAux = new ArrayList();
		lRec.add(new Integer(idRecurs));
		ConnectionBean cb = connectBD();
		Map m = null;
		Map m2 = null;
		try {
			m = AccesBD.getObjectList("mer_recurs",rm.getFieldsList("mer_recurs"),"id_rec=?",lRec,"id_rec",cb.getConnection());
			//m = AccesBD.getObjectList("mer_recurs",rm.getFieldsList("mer_recurs"),"id_rec = "+idRecurs,"id_rec",cb.getConnection());
		}catch(Exception e){
			logger.warn("Error getting resource data from DDBB.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		try{
			/*if (m.get("id_ambit") != null && 
				((ArrayList) m.get("id_ambit")).size() > 0 && 
				(((ArrayList) m.get("id_ambit")).get(0) != null))
					rm.setAmbit(((BigDecimal)((ArrayList) m.get("id_ambit")).get(0)).toString());	
			*/
			if (m.get("descripcio") != null && 
				((ArrayList) m.get("descripcio")).size() > 0 && 
				(((ArrayList) m.get("descripcio")).get(0) != null))
					rm.setDescription((String) ((ArrayList) m.get("descripcio")).get(0));

			if (m.get("id_dificultat") != null && 
				((ArrayList) m.get("id_dificultat")).size() > 0 && 
				(((ArrayList) m.get("id_dificultat")).get(0) != null))
					rm.setDifficulty(((BigDecimal)((ArrayList) m.get("id_dificultat")).get(0)).toString());
			
			//if (((ArrayList) m.get("id_rol_usuari")).size() > 0)
			//	rm.setEndUserRol(((BigDecimal)((ArrayList) m.get("id_rol_usuari")).get(0)).toString());

			if (m.get("drets") != null && 
				((ArrayList) m.get("drets")).size() > 0 && 
				(((ArrayList) m.get("drets")).get(0) != null))
					if (((BigDecimal)((ArrayList) m.get("drets")).get(0)).toString().compareTo("0") == 0 )
						rm.setHasRights("no");
					else
						rm.setHasRights("yes");
			
			if (m.get("duracio") != null && 
				((ArrayList) m.get("duracio")).size() > 0 && 
				(((ArrayList) m.get("duracio")).get(0) != null))
					rm.setLearningTime((String) ((ArrayList) m.get("duracio")).get(0));

			if (m.get("edat_max") != null && 
				((ArrayList) m.get("edat_max")).size() > 0 && 
				(((ArrayList) m.get("edat_max")).get(0) != null))
					rm.setMaxAge(((BigDecimal)((ArrayList) m.get("edat_max")).get(0)).toString());
		
			if (m.get("edat_min") != null && 
				((ArrayList) m.get("edat_min")).size() > 0 && 
				(((ArrayList) m.get("edat_min")).get(0) != null))
					rm.setMinAge(((BigDecimal)((ArrayList) m.get("edat_min")).get(0)).toString());

			if (m.get("titol") != null && 
				((ArrayList) m.get("titol")).size() > 0 && 
				(((ArrayList) m.get("titol")).get(0) != null))
					rm.setTitle((String) ((ArrayList) m.get("titol")).get(0));
			if (m.get("url") != null && 
				((ArrayList) m.get("url")).size() > 0 && 
				(((ArrayList) m.get("url")).get(0) != null))
					rm.setUrl((String) ((ArrayList) m.get("url")).get(0));
			
			if (m.get("versio") != null && 
				((ArrayList) m.get("versio")).size() > 0 && 
				(((ArrayList) m.get("versio")).get(0) != null))
					rm.setVersion((String) ((ArrayList) m.get("versio")).get(0));
			
			if (m.get("context") != null && 
					((ArrayList) m.get("context")).size() > 0 && 
					(((ArrayList) m.get("context")).get(0) != null))
						rm.setContext2((String) ((ArrayList) m.get("context")).get(0));


			/*Carrega Tipus de Destinatari endUserRol*/
			l = new ArrayList();
			m = AccesBD.getObjectList("mer_rec_rol_usuari",rm.getFieldsList("mer_rec_rol_usuari"),"id_rec = ?",lRec,"id_rec",cb.getConnection());
			if ((ArrayList)m.get("id_rol_usuari") != null){
				for (int i = 0; i< ((ArrayList)m.get("id_rol_usuari")).size();i++){
					if (((ArrayList)m.get("id_rol_usuari")).get(i) != null)
						l.add(((BigDecimal)((ArrayList) m.get("id_rol_usuari")).get(i)).toString());
				}
			}
			rm.setEndUserRol(l);
			
			/*Carrega dades del context.*/
			l = new ArrayList();
			m = AccesBD.getObjectList("mer_rec_nivell_educatiu",rm.getFieldsList("mer_rec_nivell_educatiu"),"id_rec = ?",lRec,"id_rec",cb.getConnection());
			if ((ArrayList)m.get("id_nivell") != null){
				for (int i = 0; i< ((ArrayList)m.get("id_nivell")).size();i++){
					if (((ArrayList)m.get("id_nivell")).get(i) != null)
						l.add(((BigDecimal)((ArrayList) m.get("id_nivell")).get(i)).toString());
				}
			}
			rm.setContext(l);
			
			
			/*Carrega Tipus de recurs.*/
			l = new ArrayList();
			m = AccesBD.getObjectList("mer_rec_tipus_recurs",rm.getFieldsList("mer_rec_tipus_recurs"),"id_rec = ?",lRec,"id_rec",cb.getConnection());
			if ((ArrayList)m.get("id_tipus_recurs") != null){
				for (int i = 0; i< ((ArrayList)m.get("id_tipus_recurs")).size();i++){
					if (((ArrayList)m.get("id_tipus_recurs")).get(i) != null)
						l.add(((BigDecimal)((ArrayList) m.get("id_tipus_recurs")).get(i)).toString());
				}
			}
			rm.setResourceType(l);

			/*Carrega llistat de curriculum.*/
			l = new ArrayList();
			m = AccesBD.getObject("mer_rec_curriculum",rm.getFieldsList("mer_rec_curriculum"),"id_rec = ?",lRec,cb.getConnection());
			if ((ArrayList)m.get("id_node") != null){
				for (int i = 0; i< ((ArrayList)m.get("id_node")).size();i++){
					if (((ArrayList)m.get("id_node")).get(i) != null && ((ArrayList)m.get("v_type")).get(i) != null)
						l.add((String)((ArrayList) m.get("v_type")).get(i)+((BigDecimal)((ArrayList) m.get("id_node")).get(i)).toString());
				}
			}
			rm.setCurriculum(l);

			/*Carrega llistat de paraules clau.* /
			l = new ArrayList();
			String query = " ";
			m = AccesBD.getObject("mer_rec_paraules_clau",rm.getFieldsList("mer_rec_paraules_clau"),"id_rec = ?",lRec,cb.getConnection());
			for (int i = 0; i< ((ArrayList)m.get("id_paraula")).size();i++){
				l.add(((BigDecimal)((ArrayList) m.get("id_paraula")).get(i)).toString());
				query += "id_paraula = ?";
				if (i +1 < ((ArrayList)m.get("id_paraula")).size())
					query += " OR ";
			}*/
//			l = new ArrayList();
//			String query = " ";
//			m = AccesBD.getObject("mer_rec_termes",rm.getFieldsList("mer_rec_termes"),"id_rec = ?",lRec,cb.getConnection());
//			if ((ArrayList)m.get("id_terme") != null){
//				for (int i = 0; i< ((ArrayList)m.get("id_terme")).size();i++){
//					if (((ArrayList)m.get("id_terme")).get(i) != null){
//						l.add(((BigDecimal)((ArrayList) m.get("id_terme")).get(i)).toString());
//						query += "id_terme = ?";
//						if (i +1 < ((ArrayList)m.get("id_terme")).size())
//							query += " OR ";
//					}
//				}
//			}
//			rm.setTaxon(l);
			
			/*Carrega llistat de termes de paraules clau.* /
			 * Versió antiga..
			//l = new ArrayList();
			if (l.size() > 0){
				m = AccesBD.getObject("par_clau_cat",rm.getFieldsList("par_clau_cat"),query,l,cb.getConnection());
				l = new ArrayList();
				for (int i = 0; i< ((ArrayList)m.get("paraula")).size();i++)
					l.add((String)((ArrayList) m.get("paraula")).get(i));
				rm.setTaxonTerm(l);
			}*/
//			if (l.size() > 0){
			
			
//			select m.id_terme, t.terme_ca
//			from the_termes t, mer_rec_termes m
//			where m.id_rec=4064 and m.id_terme = t.id_terme;
			
			
//			m = AccesBD.getObject("the_termes",rm.getFieldsList("the_termes"),query,l,cb.getConnection())
			m = AccesBD.getJoin(rm.getFieldsList("the_termes"),rm.getFieldsList("mer_rec_termes"),"the_termes","mer_rec_termes","id_terme","","id_rec="+rm.getIdRecurs(),"","","",cb.getConnection());
				l = new ArrayList();
				lAux = new ArrayList();
				if ((ArrayList)m.get("terme_ca") != null){
					for (int i = 0; i< ((ArrayList)m.get("terme_ca")).size();i++){
						if (((ArrayList)m.get("terme_ca")).get(i) != null){
							l.add((String)((ArrayList) m.get("terme_ca")).get(i));
							lAux.add(((BigDecimal)((ArrayList) m.get("id_terme")).get(i)).toString());							
						}
					}
				}
				rm.setTaxon(lAux);
				rm.setTaxonTerm(l);

			m = AccesBD.getJoin(rm.getFieldsList("mer_paraules"),rm.getFieldsList("mer_rec_paraules"),"mer_paraules","mer_rec_paraules","id_paraula","","id_rec="+rm.getIdRecurs(),"","","",cb.getConnection());
			l = new ArrayList();
			lAux = new ArrayList();
			if ((ArrayList)m.get("v_paraula") != null){
				for (int i = 0; i< ((ArrayList)m.get("v_paraula")).size();i++){
					if (((ArrayList)m.get("v_paraula")).get(i) != null){
						l.add((String)((ArrayList) m.get("v_paraula")).get(i));
						lAux.add(((BigDecimal)((ArrayList) m.get("id_paraula")).get(i)).toString());							
					}
				}
			}
			rm.setParaules(l);
			rm.setParaulesId(lAux);
//			}
			/*Carrega llistat de llengues.*/
			l = new ArrayList();
			m = AccesBD.getObject("mer_rec_llengua",rm.getFieldsList("mer_rec_llengua"),"id_rec = ?",lRec,cb.getConnection());		
			if ((ArrayList)m.get("id_llengua") != null){
				for (int i = 0; i< ((ArrayList)m.get("id_llengua")).size();i++){
					if (((ArrayList)m.get("id_llengua")).get(i) != null)
						l.add((String)((ArrayList) m.get("id_llengua")).get(i));
				}
			}
			rm.setLanguage(l);

			/*Carregar dades dels ambits.*/
			l = new ArrayList();
			m = AccesBD.getObjectList("mer_rec_ambits",rm.getFieldsList("mer_rec_ambits"),"id_rec = ?",lRec,"id_rec",cb.getConnection());
			if ((ArrayList)m.get("id_ambit") != null){
				for (int i = 0; i< ((ArrayList)m.get("id_ambit")).size();i++){
					if (((ArrayList)m.get("id_ambit")).get(i) != null)
						l.add(((BigDecimal)((ArrayList) m.get("id_ambit")).get(i)).toString());
				}
			}
			rm.setAmbit(l);
			
			/*Carregar dades de Contribucions.*/
			l = new ArrayList();
			m = AccesBD.getObjectList("mer_contribucio",rm.getFieldsList("mer_contribucio"),"id_rec = ?",lRec,"id_rec",cb.getConnection());
			MerliContribution mc;
			if ((ArrayList)m.get("id_rol_cont") != null){
				for (int i = 0; i< ((ArrayList)m.get("id_rol_cont")).size();i++){
					mc = new MerliContribution();
					if (((ArrayList)m.get("id_rol_cont")).get(i) != null){
						mc.setIdRol(((BigDecimal)(((ArrayList) m.get("id_rol_cont")).get(i))).intValue());
						if (((ArrayList)m.get("v_entitat")).get(i) != null)
							mc.setEntity((String) ((ArrayList) m.get("v_entitat")).get(i));
						if (((ArrayList)m.get("v_descripcio")).get(i) != null)
							mc.setDescription((String) ((ArrayList) m.get("v_descripcio")).get(i));
						if (((ArrayList)m.get("d_data")).get(i) != null){
							try{
								mc.setDate(((Timestamp) ((ArrayList) m.get("d_data")).get(i)));
							}catch (ClassCastException e){
								mc.setDate(new Timestamp(((Date) ((ArrayList) m.get("d_data")).get(i)).getTime()));					
							}
						}
						mc.setIdRecurs(String.valueOf(idRecurs));
						l.add(mc);
					}
				}
			}
			rm.setContribution(l);

			/* Carregar informació de drets: mer_drets*/
			try {
				m = AccesBD.getObject("mer_drets",rm.getFieldsList("mer_drets"),"id_rec = ?",lRec,cb.getConnection());				
				if ((((ArrayList) m.get("id_llicencia")) != null) && 
					((ArrayList) m.get("id_llicencia")).size() > 0&& 
					((ArrayList) m.get("id_llicencia")).get(0) !=null)
						rm.setLicense(((BigDecimal)((ArrayList) m.get("id_llicencia")).get(0)).toString());
				if ((((ArrayList) m.get("descripcio")) != null) && 
					((ArrayList) m.get("descripcio")).size() > 0&& 
					((ArrayList) m.get("descripcio")).get(0) !=null)
						rm.setRightsDesc((String) ((ArrayList) m.get("descripcio")).get(0));	
				if ((((ArrayList) m.get("cost")) != null) && 
					((ArrayList) m.get("cost")).size() > 0 && 
					((ArrayList) m.get("cost")).get(0) !=null)
						rm.setCost(((BigDecimal) ((ArrayList) m.get("cost")).get(0)).toString());	
				
			}catch(Exception e){
				logger.warn("Error getting Rights data from DDBB->"+e);
				e.printStackTrace();
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
			}
			

			carregaCampsText(rm,"ca",cb);
			carregaCampsText(rm,"es", cb);
			carregaCampsText(rm,"en", cb);
			carregaCampsText(rm,"oc", cb);

			/*Carrega dades de connexió amb AGREGA.*/
			m = AccesBD.getObject("mer_rec_agrega",rm.getFieldsList("mer_rec_agrega"),"id_rec = ?",lRec,cb.getConnection());
			if ((ArrayList)m.get("id_rec") != null && ((ArrayList)m.get("id_rec")).size()>0){
				try{
					if (((ArrayList)m.get("date_agrega")).get(0) != null)
						rm.setAgregaDate(((Timestamp) ((ArrayList) m.get("date_agrega")).get(0)));
					if (((ArrayList)m.get("id_agrega")).get(0) != null)
						rm.setAgregaId((String)((ArrayList) m.get("id_agrega")).get(0));
					if (((ArrayList)m.get("send")).get(0) != null)
						rm.setAgregaSend(((BigDecimal)((ArrayList) m.get("send")).get(0)).toString());
				}catch(Exception e){
					logger.warn("Error getting Agrega Integration data from DDBB->"+e);
				}
			}
			
//			carregar les unitats on el recurs esta disponible
			rm.setUnitats(carregarDisponibleA(rm.getIdRecurs(), rm.getFieldsList("mer_rec_disp_uni"), rm.getFieldsList("mer_unitats"), lRec, cb.getConnection()));

			
			/* Carregar informació d'estat del recurs: mer_rec_info*/
			try {
				m = AccesBD.getObject("mer_rec_info",rm.getFieldsList("mer_rec_info"),"id_rec = ?",lRec,cb.getConnection());				

				if ((((ArrayList) m.get("id_estat")) != null) && 
					((ArrayList) m.get("id_estat")).size() > 0 && 
					((ArrayList) m.get("id_estat")).get(0) !=null)
						rm.setEstat(((BigDecimal)((ArrayList) m.get("id_estat")).get(0)).toString());
				if ((((ArrayList) m.get("v_responsable")) != null) && 
					((ArrayList) m.get("v_responsable")).size() > 0 && 
					((ArrayList) m.get("v_responsable")).get(0) !=null)
						rm.setResponsable((String) ((ArrayList) m.get("v_responsable")).get(0));				
			}catch(Exception e){
				logger.warn("Error getting Information data from DDBB->"+e);
				e.printStackTrace();
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
			}
			
			/* Carregar informació de les relacions amb altres recursos: mer_relacio_recursos */
			try {
				m = AccesBD.getObject("mer_relacio_recursos",rm.getFieldsList("mer_relacio_recursos"),"recurs1 = ?",lRec,cb.getConnection());				
				for(int i=0; i<((ArrayList)m.get("recurs2")).size();i++)
				{
					String sId=((BigDecimal)((ArrayList) m.get("recurs2")).get(i)).toString();
					rm.getRecursRelacionat().add(Integer.valueOf(sId));
					rm.getTipusRelacio().add(((String)((ArrayList) m.get("tipus")).get(i)).toString());
//					rm.getDescripcioRelacio().add(((String) ((ArrayList) m.get("descripcio")).get(i)));
					String titolRecRel = AccesBD.executeQuery("select titol from mer_recurs where id_rec="+sId, "titol", cb.getConnection());
					rm.getDescripcioRelacio().add(titolRecRel);
				}
			}catch(Exception e){
				logger.warn("Error getting relation data from DDBB->"+e);
				e.printStackTrace();
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
			}

			/* Carregar informació del NU del recurs
			 * Atribut NOMES utilitzat en la migració de la mediateca-2010.
			 * No s'hi fan insercions ni canvis ni s'elimina, només es recupera.
			 * */
			try {
				m = AccesBD.getObject("mer_rec_mediateca",rm.getFieldsList("mer_rec_mediateca"),"id_rec = ?",lRec,cb.getConnection());				

				if (m!=null && (((ArrayList) m.get("nu")) != null) && 
					((ArrayList) m.get("nu")).size() > 0 && 
					((ArrayList) m.get("nu")).get(0) !=null)
						rm.setNu((String) ((ArrayList) m.get("nu")).get(0));				
			}catch(Exception e){
				logger.warn("Error getting Information data from DDBB->"+e);
				e.printStackTrace();
			}
			
			/* Carregar informació sobre recursos fisics: mer_recurs_fisic i mer_idfisic */
			try {
				m = AccesBD.getObject("mer_recurs_fisic",rm.getFieldsList("mer_recurs_fisic"),"id_recurs = ?",lRec,cb.getConnection());
				if ((((ArrayList) m.get("id_unitat_creadora")) != null) && 
					((ArrayList) m.get("id_unitat_creadora")).size()>0)
				{
					rm.setUnitatCreadora((((BigDecimal) ((ArrayList) m.get("id_unitat_creadora")).get(0))).toString());
					rm.setEsFisic(true);
					if ((((ArrayList) m.get("v_caracteristiques")) != null) && 
						((ArrayList) m.get("v_caracteristiques")).size() > 0 && 
						((ArrayList) m.get("v_caracteristiques")).get(0) !=null)
							rm.setCaractRFisic(((String) ((ArrayList) m.get("v_caracteristiques")).get(0)));
				}
			}catch(Exception e){
				logger.warn("Error getting physical data from DDBB->"+e);
				e.printStackTrace();
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
			}
			try {
				m = AccesBD.getObject("mer_idfisic",rm.getFieldsList("mer_idfisic"),"id_rec = ?",lRec,cb.getConnection());
				for(int i=0;i<((ArrayList)m.get("v_tipus")).size();i++)
				{
					rm.getTipusIdFisic().add(((String) ((ArrayList) m.get("v_tipus")).get(i)));	
					rm.getIdFisic().add(((String) ((ArrayList) m.get("v_valor")).get(i)));
				}
			}catch(Exception e){
				logger.warn("Error getting physical data from DDBB->"+e);
				e.printStackTrace();
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
			}
			
			
			/*Carregar dades del format (si el recurs es online) */
			if(!rm.getEsFisic())
			{
				l = new ArrayList();
				m = AccesBD.getObjectList("mer_rec_format",rm.getFieldsList("mer_rec_format"),"id_rec = ?",lRec,"id_rec",cb.getConnection());
				if ((ArrayList)m.get("id_format") != null){
					for (int i = 0; i< ((ArrayList)m.get("id_format")).size();i++){
						if (((ArrayList)m.get("id_format")).get(i) != null)
							l.add(((BigDecimal)((ArrayList) m.get("id_format")).get(i)).toString());
					}
				}
				rm.setFormat(l);
			}
			
			/*Carregar dades del format fisic (si el recurs es fisic)*/
			if(rm.getEsFisic())
			{
				l = new ArrayList();
				m = AccesBD.getObjectList("mer_rec_format_fisic",rm.getFieldsList("mer_rec_format_fisic"),"id_rec = ?",lRec,"id_rec",cb.getConnection());
				if ((ArrayList)m.get("id_format_fisic") != null){
					for (int i = 0; i< ((ArrayList)m.get("id_format_fisic")).size();i++){
						if (((ArrayList)m.get("id_format_fisic")).get(i) != null)
							l.add(((BigDecimal)((ArrayList) m.get("id_format_fisic")).get(i)).toString());
					}
				}
				rm.setFormatFisic(l);
			}
			
		} catch (Exception e) {
			logger.warn("Error while parsing data received from DDBB.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
		}
		disconnectBD(cb);

		return rm;
	}
	
	/**
	 * Obte el llistat de recursos físics que compleixen els criteris de busqueda de subQuery,
	 * obtenint-ne nomes els camps necessaris per a fer l'exportacio
	 * @param subQuery
	 * @return llistat recursos
	 * @throws MerliDBException
	 */
	public ArrayList getRecursosExportacio(String subQuery) throws MerliDBException {
		ArrayList lAux = new ArrayList();
		ArrayList lCamps = new ArrayList();
		ConnectionBean cb = connectBD();
		Map mResults = null;
		Map mRecursos = new HashMap();
		BigDecimal idRec;
		RecursMerli rm = new RecursMerli(1);
		try {
			mResults = AccesBD.getQuery("mer_recurs where id_rec in ("+subQuery+")",rm.getFieldsList("mer_recurs"),new ArrayList(),cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("id_rec")).size();i++)
			{
				idRec = ((BigDecimal)((ArrayList)mResults.get("id_rec")).get(i));
				rm = new RecursMerli(idRec.intValue());
				rm.setDescription((String)((ArrayList)mResults.get("descripcio")).get(i));
				rm.setTitle((String)((ArrayList)mResults.get("titol")).get(i));
				rm.setUrl((String) ((ArrayList) mResults.get("url")).get(i));
				rm.setLearningTime((String) ((ArrayList) mResults.get("duracio")).get(i));
				mRecursos.put(idRec, rm);
			}	
			
			lCamps = rm.getFieldsList("mer_rec_rol_usuari");	
			lCamps.add(0,"id_rec");
			mResults = AccesBD.getQuery("mer_rec_rol_usuari where id_rec in ("+subQuery+")",lCamps,new ArrayList(),cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("id_rec")).size();i++)
			{
				idRec = (BigDecimal)((ArrayList)mResults.get("id_rec")).get(i);
				rm = (RecursMerli) mRecursos.get(idRec);
				lAux = rm.getEndUserRol();
				lAux.add(((ArrayList)mResults.get("id_rol_usuari")).get(i).toString());
				rm.setEndUserRol(lAux);
			}	
			
			lCamps = rm.getFieldsList("mer_rec_nivell_educatiu");	
			lCamps.add(0,"id_rec");
			mResults = AccesBD.getQuery("mer_rec_nivell_educatiu where id_rec in ("+subQuery+")",lCamps,new ArrayList(),cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("id_rec")).size();i++)
			{
				idRec = (BigDecimal)((ArrayList)mResults.get("id_rec")).get(i);
				rm = (RecursMerli) mRecursos.get(idRec);
				lAux = rm.getContext();
				lAux.add(((ArrayList)mResults.get("id_nivell")).get(i).toString());
				rm.setContext(lAux);
			}	

			lCamps = rm.getFieldsList("the_termes");
			lCamps.add(0,"id_terme");
			ArrayList lCamps2 = new ArrayList();
			lCamps2.add("id_rec");
			mResults = AccesBD.getJoin(lCamps,lCamps2,"the_termes", "mer_rec_termes","id_terme","","id_rec in ("+subQuery+")","","","",cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("id_rec")).size();i++)
			{
				if(((ArrayList)mResults.get("terme_ca")).get(i)!=null)
				{
					idRec = (BigDecimal)((ArrayList)mResults.get("id_rec")).get(i);
					rm = (RecursMerli) mRecursos.get(idRec);
					lAux = rm.getTaxonTerm();
					lAux.add((String)((ArrayList)mResults.get("terme_ca")).get(i));
					rm.setTaxonTerm(lAux);
					lAux = rm.getTaxon();
					lAux.add(((BigDecimal)((ArrayList)mResults.get("id_terme")).get(i)).toString());
					rm.setTaxon(lAux);
				}
			}	
			lCamps = rm.getFieldsList("mer_paraules");	
			lCamps.add(0,"id_paraula");
			lCamps2 = new ArrayList();
			lCamps2.add("id_rec");
			mResults = AccesBD.getJoin(lCamps,lCamps2,"mer_paraules", "mer_rec_paraules","id_paraula","","id_rec in ("+subQuery+")","","","",cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("id_rec")).size();i++)
			{
				if(((ArrayList)mResults.get("v_paraula")).get(i)!=null)
				{
					idRec = (BigDecimal)((ArrayList)mResults.get("id_rec")).get(i);
					rm = (RecursMerli) mRecursos.get(idRec);
					lAux = rm.getParaules();
					lAux.add(((ArrayList)mResults.get("v_paraula")).get(i));
					rm.setParaules(lAux);
					lAux = rm.getParaulesId();
					lAux.add(((ArrayList)mResults.get("id_paraula")).get(i).toString());
					rm.setParaulesId(lAux);
				}
			}	
			
			lCamps = rm.getFieldsList("mer_rec_llengua");	
			lCamps.add(0,"id_rec");
			mResults = AccesBD.getQuery("mer_rec_llengua where id_rec in ("+subQuery+")",lCamps,new ArrayList(),cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("id_rec")).size();i++)
			{
				idRec = (BigDecimal)((ArrayList)mResults.get("id_rec")).get(i);
				rm = (RecursMerli) mRecursos.get(idRec);
				lAux = rm.getLanguage();
				lAux.add(((ArrayList)mResults.get("id_llengua")).get(i).toString());
				rm.setLanguage(lAux);
			}	

			lCamps = rm.getFieldsList("mer_contribucio");	
			lCamps.add(0,"id_rec");
			mResults = AccesBD.getQuery("mer_contribucio where id_rec in ("+subQuery+") and id_rol_cont in (1,3,8)",lCamps,new ArrayList(),cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("id_rec")).size();i++)
			{
				idRec = (BigDecimal)((ArrayList)mResults.get("id_rec")).get(i);
				rm = (RecursMerli) mRecursos.get(idRec);
				lAux = rm.getContribution();
				MerliContribution mc = new MerliContribution();
				if (((ArrayList)mResults.get("id_rol_cont")).get(i) != null){
					mc.setIdRol(((BigDecimal)(((ArrayList) mResults.get("id_rol_cont")).get(i))).intValue());
					if (((ArrayList)mResults.get("v_entitat")).get(i) != null)
						mc.setEntity((String) ((ArrayList) mResults.get("v_entitat")).get(i));
					if (((ArrayList)mResults.get("v_descripcio")).get(i) != null)
						mc.setDescription((String) ((ArrayList) mResults.get("v_descripcio")).get(i));
					if (((ArrayList)mResults.get("d_data")).get(i) != null){
						try{
							mc.setDate(((Timestamp) ((ArrayList) mResults.get("d_data")).get(i)));
						}catch (ClassCastException e){
							mc.setDate(new Timestamp(((Date) ((ArrayList) mResults.get("d_data")).get(i)).getTime()));					
						}
					}
					mc.setIdRecurs(String.valueOf(idRec));
				}
				lAux.add(mc);
				rm.setContribution(lAux);
			}
			
			lCamps = rm.getFieldsList("mer_relacio_recursos");	
			mResults = AccesBD.getQuery("mer_relacio_recursos where recurs1 in ("+subQuery+")",lCamps,new ArrayList(),cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("recurs1")).size();i++)
			{
				idRec = (BigDecimal)((ArrayList)mResults.get("recurs1")).get(i);
				rm = (RecursMerli) mRecursos.get(idRec);
				String sIdRec2 = ((BigDecimal)((ArrayList) mResults.get("recurs2")).get(i)).toString();
				lAux = rm.getRecursRelacionat();
				lAux.add(sIdRec2);
				rm.setRecursRelacionat(lAux);
				lAux = rm.getTipusRelacio();
				lAux.add(((String)((ArrayList) mResults.get("tipus")).get(i)));
				rm.setTipusRelacio(lAux);
				lAux = rm.getDescripcioRelacio();
				String titolRecRel = AccesBD.executeQuery("select titol from mer_recurs where id_rec="+sIdRec2, "titol", cb.getConnection());
				lAux.add(titolRecRel);
				rm.setDescripcioRelacio(lAux);
			}

			lCamps = rm.getFieldsList("mer_rec_mediateca");	
			lCamps.add(0,"id_rec");
			mResults = AccesBD.getQuery("mer_rec_mediateca where id_rec in ("+subQuery+")",lCamps,new ArrayList(),cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("id_rec")).size();i++)
			{
				idRec = (BigDecimal)((ArrayList)mResults.get("id_rec")).get(i);
				rm = (RecursMerli) mRecursos.get(idRec);
				rm.setNu((String)((ArrayList)mResults.get("nu")).get(i));
			}
			
			lCamps = rm.getFieldsList("mer_recurs_fisic");	
			mResults = AccesBD.getQuery("mer_recurs_fisic where id_recurs in ("+subQuery+")",lCamps,new ArrayList(),cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("id_recurs")).size();i++)
			{
				idRec = (BigDecimal)((ArrayList)mResults.get("id_recurs")).get(i);
				rm = (RecursMerli) mRecursos.get(idRec);
				rm.setUnitatCreadora((((BigDecimal) ((ArrayList) mResults.get("id_unitat_creadora")).get(i))).toString());
				rm.setEsFisic(true);
				rm.setCaractRFisic(((String) ((ArrayList) mResults.get("v_caracteristiques")).get(i)));
			}
			lCamps = rm.getFieldsList("mer_idfisic");	
			mResults = AccesBD.getQuery("mer_idfisic where id_rec in ("+subQuery+")",lCamps,new ArrayList(),cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("id_rec")).size();i++)
			{
				idRec = (BigDecimal)((ArrayList)mResults.get("id_rec")).get(i);
				rm = (RecursMerli) mRecursos.get(idRec);
				lAux = rm.getTipusIdFisic();
				lAux.add((String)((ArrayList)mResults.get("v_tipus")).get(i));
				rm.setTipusIdFisic(lAux);
				lAux = rm.getIdFisic();
				lAux.add((String)((ArrayList)mResults.get("v_valor")).get(i));
				rm.setIdFisic(lAux);
			}	
			lCamps = rm.getFieldsList("mer_rec_format_fisic");	
			lCamps.add(0,"id_rec");
			mResults = AccesBD.getQuery("mer_rec_format_fisic where id_rec in ("+subQuery+")",lCamps,new ArrayList(),cb.getConnection());
			for(int i=0;i<((ArrayList)mResults.get("id_rec")).size();i++)
			{
				idRec = (BigDecimal)((ArrayList)mResults.get("id_rec")).get(i);
				rm = (RecursMerli) mRecursos.get(idRec);
				lAux = rm.getFormatFisic();
				lAux.add(((BigDecimal)((ArrayList)mResults.get("id_format_fisic")).get(i)).toString());
				rm.setFormatFisic(lAux);
			}	
		}catch(Exception e){
			logger.warn("Error getting resource data from DDBB.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		
		disconnectBD(cb);
		
		return new ArrayList(mRecursos.values()); 
	}

	/**
	 * Carrega les unitats on esta disponible el recurs
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private ArrayList carregarDisponibleA(int idRec, ArrayList campsList, ArrayList campsList2, ArrayList rec, Connection c) {
		Unitat u;
		ArrayList aux = new ArrayList();
		Map m;
		try{
			m = AccesBD.getJoin(campsList, campsList2, "mer_rec_disp_uni", "mer_unitats", "id_unitat", "id_rec = "+idRec, "",  "", "id_unitat", "ASC", c);
			if (m!=null && m.get("v_nom")!=null){
			for (int i = 0; i< ((ArrayList)m.get("v_nom")).size();i++){
				u = new Unitat();
				int idUni=((BigDecimal) ((ArrayList) m.get("id_unitat")).get(i*2)).intValue();//com que en les dos taules el nom del camp coincideix (id_unitat), es guarden els valors duplicats (per aixo fem %2) 
				u.setIdentifier(new Integer(idUni));
				u.setName((String) ((ArrayList) m.get("v_nom")).get(i));
				aux.add(u);
			}
			}
		}catch(Exception e){
			logger.error("Error a l'obtenir les unitats->"+e);
			e.printStackTrace();
		}
		return aux;
	}

	/**
	 * @param rm
	 * @param rec 
	 * @param lRec
	 * @param cb
	 * @throws MerliDBException
	 */
	private void carregaCampsText(RecursMerli rm, String lang, ConnectionBean cb) throws MerliDBException {
		Map m;
		ArrayList lrec = new ArrayList();
		lrec.add(new Integer(rm.getIdRecurs()));		
		
		try {
			lrec.add(lang);
			m = AccesBD.getObject("mer_rec_lang",rm.getFieldsList("mer_rec_lang"),"id_rec = ? AND lang=?",lrec,cb.getConnection());	
			if ((((ArrayList) m.get("titol")) != null) && 
					((ArrayList) m.get("titol")).size() > 0&& 
					((ArrayList) m.get("titol")).get(0) !=null)
						rm.setTitle(lang,(String) ((ArrayList) m.get("titol")).get(0));	
			if ((((ArrayList) m.get("descripcio")) != null) && 
					((ArrayList) m.get("descripcio")).size() > 0&& 
					((ArrayList) m.get("descripcio")).get(0) !=null)
						rm.setDescription(lang,(String) ((ArrayList) m.get("descripcio")).get(0));	
			if ((((ArrayList) m.get("drets")) != null) && 
					((ArrayList) m.get("drets")).size() > 0&& 
					((ArrayList) m.get("drets")).get(0) !=null)
						rm.setRightsDesc(lang,(String) ((ArrayList) m.get("drets")).get(0));	
			if ((((ArrayList) m.get("estat")) != null) && 
				((ArrayList) m.get("estat")).size() > 0&& 
				((ArrayList) m.get("estat")).get(0) !=null)
						rm.setEstatLang(lang,((BigDecimal)((ArrayList) m.get("estat")).get(0)).toString());	
			
		}catch(Exception e){
			logger.warn("Error getting Rights data from DDBB->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
		}
	}

	public ArrayList getList(String elem, String table, String order, int way) throws MerliDBException {
		ArrayList res = new ArrayList();
		ArrayList lelem = new ArrayList();		
		Map m;
		int counter=0;
		int index = 0;
		String element = "";
		ConnectionBean cb = null;

		try {
			cb = connectBD();		
			switch (way){
			case RecursBD.IDENTIFIERS:	
				lelem.add(elem);
				m = AccesBD.getFullLlistat(table,lelem,order+" ASC",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}
				break;
			case RecursBD.LABELS:
				lelem.add(elem);
				m = AccesBD.getFullLlistat(table,lelem,order+" ASC",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(((String)(((ArrayList)m.get(lelem.get(0))).get(i))));
				}				
				break;			
//****************ADDED -- to get correctly the list of license from database
//****************  @naseq 2014.04.08			
			case RecursBD.VEU_RECURS_LLICENCE:
				lelem.add(elem);
				m = AccesBD.getFullLlistat(table,lelem,order+" ASC",cb.getConnection());
				
				if(elem.equals("llicencia")){
					lelem.add("id_llicencia");
					m = AccesBD.getFullLlistat(table,lelem,order+" ASC",cb.getConnection());
					//logger.info(m);
					int s = ((ArrayList)m.get(lelem.get(1))).size();
					counter = Integer.parseInt(((ArrayList)m.get("id_llicencia")).get(s-1).toString());
					for (int i = 0;i<=counter;i++){
						res.add(String.valueOf(i));
					}
				}
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					element = ((String)(((ArrayList)m.get(lelem.get(0))).get(i)));
					if(elem.equals("llicencia")){
						index = Integer.parseInt(((ArrayList)m.get(lelem.get(1))).get(i).toString());
						res.set(index, element);	
					/*	logger.info(m.get(lelem.get(1)));						 
						logger.info(String.valueOf(index));
						logger.info(element + " - "+index );*/
					}else{
						res.add(((String)(((ArrayList)m.get(lelem.get(0))).get(i))));
					}					
				}				
				break;
//****************  FI @naseq 2014.04.08				
			default:			
				lelem.add(elem);
				m = AccesBD.getObjectList(table,lelem," id_rec = "+way,order,cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}				
			}
			disconnectBD(cb);
		} catch (MerliDBException e) {
			logger.warn("Can't connect to DDBB to get "+table+"."+elem+" data.->"+e);
			disconnectBD(cb);
			throw e;
		} catch (SQLException e) {
			logger.warn("Can't connect to DDBB to get "+table+"."+elem+" data.->"+e);
			disconnectBD(cb);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		return res;
	}
	
	public ArrayList getListCond(String elem, String table, String condicio, String order, int way) throws MerliDBException {
		ArrayList res = new ArrayList();
		ArrayList lelem = new ArrayList();
		Map m;
		ConnectionBean cb = null;

		try {
			cb = connectBD();		
			switch (way){
			case RecursBD.IDENTIFIERS:	
				lelem.add(elem);
				m = AccesBD.getObjectList(table, lelem, condicio, order+" ASC", cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}
				break;
			case RecursBD.LABELS:
				lelem.add(elem);
				m = AccesBD.getObjectList(table, lelem, condicio, order+" ASC", cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(((String)(((ArrayList)m.get(lelem.get(0))).get(i))));
				}				
				break;
			default:			
				lelem.add(elem);
				m = AccesBD.getObjectList(table,lelem," id_rec = "+way,order,cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}				
			}
			disconnectBD(cb);
		} catch (MerliDBException e) {
			logger.warn("Can't connect to DDBB to get "+table+"."+elem+" data.->"+e);
			disconnectBD(cb);
			throw e;
		} catch (SQLException e) {
			logger.warn("Can't connect to DDBB to get "+table+"."+elem+" data.->"+e);
			disconnectBD(cb);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		return res;
	}

	public ArrayList getResourceType(int way) throws MerliDBException {
		switch(way){
		case RecursBD.IDENTIFIERS: return getList("id_tipus_recurs","mer_tipus_recurs","id_tipus_recurs",RecursBD.IDENTIFIERS);
		case RecursBD.LABELS: return getList("tipus_recurs_cat","mer_tipus_recurs","id_tipus_recurs",RecursBD.LABELS);
		default: return getList("id_tipus_recurs","mer_rec_tipus_recurs","",way);
		}
	}

	public ArrayList getAmbit(int way) throws MerliDBException {
		// TODO Auto-generated method stub
		switch(way){
		case RecursBD.IDENTIFIERS: return getList("id_ambit","mer_ambits","ordre",RecursBD.IDENTIFIERS);
		case RecursBD.LABELS: return getList("ambit","mer_ambits","ordre",RecursBD.LABELS);
		default: return getList("id_ambit","mer_recurs","ordre",way);
		}
	}

	public String getAmbitNom(int idAmbit) throws MerliDBException {

		ArrayList al = new ArrayList();
		al.add("ambit");
		ArrayList alcon = new ArrayList();
		alcon.add(new Integer(idAmbit));
		String ambit = null;
		Map m;
		ConnectionBean cb = null;

		try {
			cb = connectBD();		
			m = AccesBD.getObject("mer_ambits",al,"id_ambit = ?",alcon,cb.getConnection());	
			ambit = (String) ((ArrayList) m.get("ambit")).get(0);
		}catch(Exception e){
			logger.error("Error al recuperar l'ambit -> " + e.toString());
		}finally{
			disconnectBD(cb);
		}
		return ambit;
	}
		
	public ArrayList getDificultat(int way) throws MerliDBException {
		switch(way){
		case RecursBD.IDENTIFIERS: return getList("id_dificultat","mer_dificultat","id_dificultat",RecursBD.IDENTIFIERS);
		case RecursBD.LABELS: return getList("dificultat_cat","mer_dificultat","id_dificultat",RecursBD.LABELS);
		default: return getList("id_dificultat","mer_recurs","",way);
		}
	}
	
	public ArrayList getRolUser(int way) throws MerliDBException {
		switch(way){
		case RecursBD.IDENTIFIERS: return getList("id_rol_usuari","mer_rol_usuari","id_rol_usuari",RecursBD.IDENTIFIERS);
		case RecursBD.LABELS: return getList("rol_usuari_cat","mer_rol_usuari","id_rol_usuari",RecursBD.LABELS);
		default: return getList("id_rol_usuari","mer_recurs","",way);
		}
	}

	public ArrayList getUnitats(int way) throws MerliDBException {
		switch(way){
		case RecursBD.IDENTIFIERS: return getList("id_unitat","mer_unitats","upper(v_nom)",RecursBD.IDENTIFIERS);
		case RecursBD.LABELS: return getList("v_nom","mer_unitats","upper(v_nom)",RecursBD.LABELS);
		default: return getList("id_unitat","mer_unitats","",way);
		}
	}
	
	public ArrayList getUnitatsReals(int way) throws MerliDBException {
		String unitatDefecte = MessageResources.getMessageResources(APPLICATION_RESOURCES).getMessage("mediateca.unitat.creadora");

		switch(way){			
			case RecursBD.IDENTIFIERS: return getListCond("id_unitat","mer_unitats","id_unitat not in "+unitatDefecte ,"upper(v_nom)",RecursBD.IDENTIFIERS);
			case RecursBD.LABELS: return getListCond("v_nom","mer_unitats","id_unitat not in "+unitatDefecte, "upper(v_nom)",RecursBD.LABELS);
			default: return getList("id_unitat","mer_unitats","",way);
		}
	}
	
	public ArrayList getLlicencia(int way) throws MerliDBException {
		switch(way){
		case RecursBD.IDENTIFIERS: return getList("id_llicencia","mer_llicencia","id_llicencia",RecursBD.IDENTIFIERS);
		case RecursBD.LABELS: 			
			ArrayList res = getList("llicencia","mer_llicencia","id_llicencia",RecursBD.LABELS);
			res.add("");
			//logger.info(res);
			return res;
//****************ADDED -- New variable added to the RecursBD to get the list of license correctly.
//****************  @naseq 2014.04.08 			
		case RecursBD.VEU_RECURS_LLICENCE: 			
			ArrayList res2 = getList("llicencia","mer_llicencia","id_llicencia",RecursBD.VEU_RECURS_LLICENCE);
			res2.add("");
			//logger.info(res2);
			return res2;
//*************** FI (@naseq 2014.04.08)
		default: return getList("id_llicencia","mer_drets","",way);
		}
	}
	
	public ArrayList getTipusRelacio(int way) throws MerliDBException {
		switch(way){
			case RecursBD.IDENTIFIERS: return getList("id_tipusrel","mer_tipusrel","id_tipusrel",RecursBD.IDENTIFIERS);
			case RecursBD.LABELS: return getList("tipusrel","mer_tipusrel","id_tipusrel",RecursBD.LABELS);
			default: return getList("id_tipusrel","mer_tipusrel","",way);
		}
	}
	
	public ArrayList getTipusIdFisic(int way) throws MerliDBException {
		switch(way){
			case RecursBD.IDENTIFIERS: return getList("id_tipus","mer_tipus_idfisic","id_tipus",RecursBD.IDENTIFIERS);
			case RecursBD.LABELS: return getList("v_tipus","mer_tipus_idfisic","id_tipus",RecursBD.LABELS);
			default: return getList("v_tipus","mer_tipus_idfisic","",way);
		}
	}
	
	public ArrayList getLlengues(int way) throws MerliDBException {
		ArrayList res = new ArrayList();
		ArrayList lelem = new ArrayList();
		Map m;
		ConnectionBean cb = null;
		try {
			cb = connectBD();			
			switch (way){
			case RecursBD.IDENTIFIERS:	
				lelem.add("id_llengua");
				m = AccesBD.getFullLlistat("mer_llengua",lelem,"posicio, llengua_cat",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add((String)((ArrayList)m.get(lelem.get(0))).get(i));
				}
				break;
			case RecursBD.LABELS:
				lelem.add("llengua_cat");
				m = AccesBD.getFullLlistat("mer_llengua",lelem,"posicio, llengua_cat",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(((String)(((ArrayList)m.get(lelem.get(0))).get(i))));
				}				
				break;
			default:			
				lelem.add("id_llengua");
				m = AccesBD.getObjectList("mer_rec_llengua",lelem," id_rec = "+way,"",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add((String)((ArrayList)m.get(lelem.get(0))).get(i));
				}				
			}
			disconnectBD(cb);
		} catch (MerliDBException e) {
			logger.warn("Can't connect to DDBB to get mer_llengua-llengua data.->"+e);
			disconnectBD(cb);
			throw e;
		} catch (SQLException e) {
			logger.warn("Can't connect to DDBB to get mer_llengua-llengua data.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		} catch (NullPointerException e) {
			logger.warn("Can't connect to DDBB to get mer_llengua-llengua data.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		return res;
	}
	
	public ArrayList getUnitats() throws MerliDBException {
		ArrayList res = new ArrayList();
		ArrayList lelem = new ArrayList();
		Map m;
		ConnectionBean cb = null;
		try {
			cb = connectBD();			
			lelem.add("id_unitat");lelem.add("v_nom");
			m = AccesBD.getFullLlistat("mer_unitats",lelem,"v_nom",cb.getConnection());
			ArrayList ids=(ArrayList)m.get(lelem.get(0));
			ArrayList noms=(ArrayList)m.get(lelem.get(1));
			for (int i = 0;i<ids.size();i++)
			{
				Unitat uni=new Unitat();
				uni.setName((String)noms.get(i));
				int k=((BigDecimal)ids.get(i)).intValue();
				uni.setIdentifier(new Integer(k));
				res.add(uni);
			}				
			disconnectBD(cb);
		} catch (MerliDBException e) {
			logger.warn("Can't connect to DDBB to get mer_unitats data.->"+e);
			disconnectBD(cb);
			throw e;
		} catch (NullPointerException e) {
			logger.warn("Can't connect to DDBB to get mer_unitats data.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		return res;
	}
	
	public ArrayList getFormat(int way) throws MerliDBException {
		switch(way){
		case RecursBD.IDENTIFIERS: return getList("id_format","mer_formats","descriptor",RecursBD.IDENTIFIERS);
		case RecursBD.LABELS: return getList("descriptor","mer_formats","descriptor",RecursBD.LABELS);
		default: return getList("id_format","mer_rec_format","",way);
		}
	}	
	
	public ArrayList getFormatFisic(int way) throws MerliDBException {
		switch(way){
		case RecursBD.IDENTIFIERS: return getList("id_format_fisic","mer_formats_fisics","v_tipus_cat",RecursBD.IDENTIFIERS);
		case RecursBD.LABELS: return getList("v_tipus_cat","mer_formats_fisics","v_tipus_cat",RecursBD.LABELS);
		default: return getList("id_format_fisic","mer_rec_format_fisic","",way);
		}
	}	
	
	public ArrayList getFormatLimitat(int way) throws MerliDBException {
		return getFormatLimitat(way, -1);
	}
	public ArrayList getFormatLimitat(int way, int idFormat) throws MerliDBException {
		ArrayList res = new ArrayList();
		ArrayList lelem = new ArrayList();
		ArrayList l2 = new ArrayList();
		Map m;
		ConnectionBean cb = null;

		l2.add(" ");
		try {
			cb = connectBD();		
			switch (way){
			case RecursBD.IDENTIFIERS:	
				lelem.add("id_format");
				m = AccesBD.getObjectList("mer_formats",lelem," descriptor <> ?",l2,"descriptor",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}
				break;
			case RecursBD.LABELS:
				lelem.add("descriptor");
				if (idFormat > 0){
					l2.clear();
					l2.add(new Integer(idFormat));
					m = AccesBD.getObject("mer_formats",lelem," id_format = ?",l2,cb.getConnection());
					String resul = ((String)(((ArrayList)m.get(lelem.get(0))).get(0)));
					res.add(resul);
				}else{
					m = AccesBD.getObjectList("mer_formats",lelem," descriptor <> ?",l2,"descriptor",cb.getConnection());
					for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
						res.add(((String)(((ArrayList)m.get(lelem.get(0))).get(i))));
					}		
				}
				break;
			default:			
				lelem.add("id_format");
				m = AccesBD.getObjectList("mer_rec_format",lelem," id_rec = "+way,"",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}				
			}
			disconnectBD(cb);
		} catch (MerliDBException e) {
			logger.warn("Can't connect to DDBB to get mer_format data.->"+e);
			disconnectBD(cb);
			throw e;
		} catch (SQLException e) {
			logger.warn("Can't connect to DDBB to get mer_format data.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		} catch (Exception e) {
			logger.warn("Can't connect to DDBB to get mer_format data.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		return res;
	}
	
	public ArrayList getFormatFisicLimitat(int way) throws MerliDBException {
		return getFormatFisicLimitat(way, -1);
	}
	public ArrayList getFormatFisicLimitat(int way, int idFormat) throws MerliDBException {
		ArrayList res = new ArrayList();
		ArrayList lelem = new ArrayList();
		ArrayList l2 = new ArrayList();
		Map m;
		ConnectionBean cb = null;

		l2.add(" ");
		try {
			cb = connectBD();		
			switch (way){
			case RecursBD.IDENTIFIERS:	
				lelem.add("id_format_fisic");
				m = AccesBD.getObjectList("mer_formats_fisics",lelem," v_tipus_cat <> ?",l2,"v_tipus_cat",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}
				break;
			case RecursBD.LABELS:
				lelem.add("v_tipus_cat");
				if (idFormat > 0){
					l2.clear();
					l2.add(new Integer(idFormat));
					m = AccesBD.getObject("mer_formats_fisics",lelem," id_format_fisic = ?",l2,cb.getConnection());
					String resul = ((String)(((ArrayList)m.get(lelem.get(0))).get(0)));
					res.add(resul);
				}else{
					m = AccesBD.getObjectList("mer_formats_fisics",lelem," v_tipus_cat <> ?",l2,"v_tipus_cat",cb.getConnection());
					for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
						res.add(((String)(((ArrayList)m.get(lelem.get(0))).get(i))));
					}		
				}
				break;
			default:			
				lelem.add("id_format_fisic");
				m = AccesBD.getObjectList("mer_rec_format_fisic",lelem," id_rec = "+way,"",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}				
			}
			disconnectBD(cb);
		} catch (MerliDBException e) {
			logger.warn("Can't connect to DDBB to get mer_format_fisic data.->"+e);
			disconnectBD(cb);
			throw e;
		} catch (SQLException e) {
			logger.warn("Can't connect to DDBB to get mer_format_fisic data.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		} catch (Exception e) {
			logger.warn("Can't connect to DDBB to get mer_format data.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		return res;
	}

	public ArrayList getNivell(int type, int way) throws MerliDBException {
		ArrayList res = new ArrayList();
		ArrayList lelem = new ArrayList();
		ArrayList l2 = new ArrayList();
		Map m;
		ConnectionBean cb = null;

		l2.add(new Integer(type));
		try {
			cb = connectBD();		
			switch (way){
			case RecursBD.IDENTIFIERS:	
				lelem.add("id_nivell_cat");
				m = AccesBD.getObjectList("mer_nivell_edu_cat",lelem," grup_edu = ?",l2,"id_nivell_cat",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}
				break;
			case RecursBD.LABELS:
				lelem.add("nivell_cat");
				m = AccesBD.getObjectList("mer_nivell_edu_cat",lelem," grup_edu = ?",l2,"id_nivell_cat",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(((String)(((ArrayList)m.get(lelem.get(0))).get(i))));
				}				
				break;
			case RecursBD.MAXIM:
				lelem.add("edat_max");
				m = AccesBD.getObjectList("mer_nivell_edu_cat",lelem," grup_edu = ?",l2,"id_nivell_cat",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}				
				break;
			case RecursBD.MINIM:
				lelem.add("edat_min");
				m = AccesBD.getObjectList("mer_nivell_edu_cat",lelem," grup_edu = ?",l2,"id_nivell_cat",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}				
				break;
			default:			
				lelem.add("id_nivell");
				m = AccesBD.getObjectList("mer_rec_nivell_educatiu",lelem," id_rec = ? ",l2,"",cb.getConnection());
				for (int i = 0;i<((ArrayList)m.get(lelem.get(0))).size();i++){
					res.add(new Integer(((BigDecimal)(((ArrayList)m.get(lelem.get(0))).get(i))).intValue()));
				}				
			}
			disconnectBD(cb);
		} catch (MerliDBException e) {
			logger.warn("Can't connect to DDBB to get mer_nivell_edu_cat data.->"+e);
			disconnectBD(cb);
			throw e;
		} catch (SQLException e) {
			logger.warn("Can't connect to DDBB to get mer_nivell_edu_cat data.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		} catch (Exception e) {
			logger.warn("Can't connect to DDBB to get mer_nivell_edu_cat data.->"+e);
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		return res;
	}

	public boolean existsUrl(String url, String idRecurs) throws MerliDBException {
		ConnectionBean cb = null;
		int exs;
		try{
			cb = connectBD();		
			ArrayList l = new ArrayList();
			l.add(url);
			l.add(new Integer(idRecurs));
			//exs = AccesBD.executeExist("v_mer_list_etiq","url = ? AND id_estat = ?",l,cb.getConnection());
			exs = AccesBD.executeExist("v_mer_list_etiq","url = ? AND id_rec <> ?",l,cb.getConnection());
			//exs = AccesBD.executeExist("v_mer_list_etiq","url",url," id_estat ="+,cb.getConnection());
			//AccesBD.executeExist("mer_recurs","url = ?",l,cb.getConnection())
			disconnectBD(cb);
			if (0<exs){
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		return false;
	}

	public ArrayList getTermesCurriculum(String valor) throws MerliDBException {
		// TODO Auto-generated method stub
		ArrayList res = new ArrayList();
		ArrayList lelem = new ArrayList();
		ArrayList lParam = new ArrayList();
		Hashtable ht;
		Map m,m2;
		ConnectionBean cb = null;

		try {
			cb = connectBD();		
			lelem.add("id_terme");
			lParam.add(valor);
//			m = AccesBD.getObjectList("CUR_THESAURUS_ETB",lelem,"id_node=?",lParam,"",cb.getConnection());
			m = AccesBD.getObjectList("CUR_THESAURUS",lelem,"id_node=?",lParam,"",cb.getConnection());
			lelem.clear();
			lelem.add("terme_ca");
			for (int i = 0;i<((ArrayList)m.get("id_terme")).size();i++){
				ht = new Hashtable();
				ht.put("idTerme",new Integer(((BigDecimal)(((ArrayList)m.get("id_terme")).get(i))).intValue()));
				lParam.clear();
				lParam.add(ht.get("idTerme"));
				try{
 					m2 = AccesBD.getObject("the_termes",lelem,"id_terme =?",lParam,cb.getConnection());
					//AccesBD.executeQuery("Select terme_ca from the_termes where id_terme=7","terme_ca")
					ht.put("terme",(String)((ArrayList)m2.get(lelem.get(0))).get(0));
					res.add(ht);
				}catch (Exception e){
					logger.warn("Can not find element "+ht.get("id_terme"));
				}
			}				
			
			
		} catch (MerliDBException e) {
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		} finally{
			disconnectBD(cb);
		}
		return res;
	}
	
	public void salvarTextos(int idRecurs, String lang, String title, String description, String rightsDesc, boolean status) throws MerliDBException, SQLException {
		Connection c=null;
		ConnectionBean cb = null;
		try {
			cb = connectBD();
			c = cb.getConnection();
			salvarTextos(idRecurs, lang, title, description, rightsDesc, status, c);
		}catch (Exception e) {
			logger.warn("Error setting language correction:->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}finally{
			c.close();
			disconnectBD(cb);
		}
	}
	/**
	 * Desa els textos per l'idioma donat. SET->NO MODIFICA L'ESTAT DE LES TRADUCCIONS. 
	 * @param idRecurs
	 * @param lang
	 * @param title
	 * @param description
	 * @param rightsDesc
	 * @param status
	 * @throws MerliDBException
	 * @throws SQLException
	 */
	public void salvarTextos(int idRecurs, String lang, String title, String description, String rightsDesc, boolean status, Connection c) throws MerliDBException, SQLException {
		logger.info("salvarTextos " + lang);

		String query;
		ArrayList lParam = new ArrayList();
		ArrayList lCond = new ArrayList();
		lCond.add(new Integer(idRecurs));
		lCond.add(lang);
		try {
			query= "titol = ?,  descripcio = ?, drets =?";//, estat=?";
			
			lParam.add((title==null)?"":(title));
			lParam.add((description==null)?"":(description));
			lParam.add((rightsDesc==null)?"":(rightsDesc));

				
			if (AccesBD.executeExist("mer_rec_lang","id_rec=? AND lang=?",lCond,c)>0){					 
				AccesBD.executeUpdate("mer_rec_lang","id_rec = ? AND lang=?",lCond,query,lParam,c);
			}else{
				lParam.clear();
				
				lParam.add(new Integer(idRecurs));
				lParam.add(lang);					
				lParam.add((title==null)?"":(title));
				lParam.add((description==null)?"":(description));
				lParam.add((rightsDesc==null)?"":(rightsDesc));
	
				lParam.add("0");
				AccesBD.executeInsert("mer_rec_lang",lParam,c);
			}
			
		} catch (NullPointerException ne){
			logger.warn("Error setting language correction:>"+ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			logger.warn("Error setting language correction:->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}finally{
			logger.info("end salvarTextos " + lang);
//			c.close();
//			disconnectBD(cb);
		}
	}

	/**
	 * obte el llistat de totes les llengues de la taula mer_llengua de la BD en un hashmap<id_llengua, llengua_cat>
	 * @return
	 */
	public Map getLlenguesTotes() {
		ArrayList lParam = new ArrayList();
		Map m = new HashMap();
		ConnectionBean cb = null;

		try {
			cb = connectBD();
			lParam.add("id_llengua");
			lParam.add("llengua_cat");
			m = AccesBD.getFullLlistat("mer_llengua",lParam,"id_llengua",cb.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				disconnectBD(cb);
			} catch (MerliDBException e) {
				e.printStackTrace();
			}
		}
		return m;
	}
	
	public Map getFormatFisicTots() {
		ArrayList lParam = new ArrayList();
		Map m = new HashMap();
		ConnectionBean cb = null;

		try {
			cb = connectBD();
			lParam.add("id_format_fisic");
			lParam.add("v_tipus_cat");
			m = AccesBD.getFullLlistat("mer_formats_fisics",lParam,"id_format_fisic",cb.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				disconnectBD(cb);
			} catch (MerliDBException e) {
				e.printStackTrace();
			}
		}
		return m;
	}

	public Map getContextTots() {
		ArrayList lParam = new ArrayList();
		Map m = new HashMap();
		ConnectionBean cb = null;

		try {
			cb = connectBD();
			lParam.add("id_nivell_cat");
			lParam.add("nivell_cat");
			m = AccesBD.getFullLlistat("mer_nivell_edu_cat",lParam,"id_nivell_cat",cb.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				disconnectBD(cb);
			} catch (MerliDBException e) {
				e.printStackTrace();
			}
		}
		return m;
	}
	
	public Map getRolUserTots() {
		ArrayList lParam = new ArrayList();
		Map m = new HashMap();
		ConnectionBean cb = null;

		try {
			cb = connectBD();
			lParam.add("id_rol_usuari");
			lParam.add("rol_usuari_cat");
			m = AccesBD.getFullLlistat("mer_rol_usuari",lParam,"id_rol_usuari",cb.getConnection());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				disconnectBD(cb);
			} catch (MerliDBException e) {
				e.printStackTrace();
			}
		}
		return m;
	}
	
	/* Inserts i updates a les diferents taules */ 
	private void insertCurriculum(RecursMerli recursMerli, Connection c)
	{
		ArrayList al, alcon;
		int idRecurs = recursMerli.getIdRecurs();
		al = recursMerli.getCurriculum();
		String type = null;
		String idcur = null;
		for (int i = 0; i< al.size(); i++){
			String aux = (String) al.get(i);
			for (int l = 0; l < aux.length(); l++){
				if (aux.charAt(l) <= '9'){
					type = aux.substring(0,l);
					idcur = aux.substring(l);
					break;
				}
			}
			alcon = new ArrayList();
			alcon.add(new Integer(idRecurs));
			alcon.add(idcur);
			alcon.add(type);
			try{
				AccesBD.executeInsert("mer_rec_curriculum",alcon,c);//idRecurs+","+idcur+",'"+type+"'"
			}catch(Exception e){
				logger.warn("Curriculum amb error:"+ al.get(i) + " ->"+e.getMessage());
			}
		}
	}
	
	private void insertDestinataris(RecursMerli recursMerli, Connection c)
	{
		ArrayList al, alcon;
		int idRecurs = recursMerli.getIdRecurs();
		al = recursMerli.getEndUserRol();
		for (int i = 0; i< al.size(); i++){
			alcon = new ArrayList();
			alcon.add(new Integer(idRecurs));
			alcon.add(al.get(i));
			try{
				AccesBD.executeInsert("mer_rec_rol_usuari",alcon,c);//idRecurs+","+al.get(i)
			}catch(Exception e){
				logger.warn("Rol d'usuari amb error:"+ al.get(i) + " ->"+e.getMessage());
			}
		}
	}
	
	private void insertTipusRecurs(RecursMerli recursMerli, Connection c)
	{
		ArrayList al, alcon;
		int idRecurs = recursMerli.getIdRecurs();
		al = recursMerli.getResourceType();
		for (int i = 0; i< al.size(); i++){
			alcon = new ArrayList();
			alcon.add(new Integer(idRecurs));
			alcon.add(al.get(i));
			try{
				AccesBD.executeInsert("mer_rec_tipus_recurs",alcon,c);//idRecurs+","+al.get(i)
			}catch(Exception e){
				logger.warn("Tipus recurs amb error:"+ al.get(i) + " ->"+e.getMessage());
			}
		}
	}
	
	private void insertFormats(RecursMerli recursMerli, Connection c)
	{
		ArrayList al, alcon;
		int idRecurs = recursMerli.getIdRecurs();
		al = recursMerli.getFormat();
		for (int i = 0; i< al.size(); i++){
			alcon = new ArrayList();
			alcon.add(new Integer(idRecurs));
			alcon.add(al.get(i));
			try{
				AccesBD.executeInsert("mer_rec_format",alcon,c);//idRecurs+","+al.get(i)
			}catch(Exception e){
				logger.warn("Format amb error:"+ al.get(i) + " ->"+e.getMessage());
			}
		}
		al = recursMerli.getFormatFisic();
		for (int i = 0; i< al.size(); i++){
			alcon = new ArrayList();
			alcon.add(new Integer(idRecurs));
			alcon.add(al.get(i));
			try{
				AccesBD.executeInsert("mer_rec_format_fisic",alcon,c);//idRecurs+","+al.get(i)
			}catch(Exception e){
				logger.warn("Format Físic amb error:"+ al.get(i) + " ->"+e.getMessage());
			}
		}			
	}
	
	private void insertAmbit(RecursMerli recursMerli, Connection c)
	{
		ArrayList al, alcon;
		int idRecurs = recursMerli.getIdRecurs();
		al = recursMerli.getAmbit();
		for (int i = 0; i< al.size(); i++){
			alcon = new ArrayList();
			alcon.add(new Integer(idRecurs));
			alcon.add(al.get(i));
			try{
				AccesBD.executeInsert("mer_rec_ambits",alcon,c);//idRecurs+","+al.get(i)
			}catch(Exception e){
				logger.warn("Ambit amb error:"+ al.get(i) + " ->"+e.getMessage());
			}
		}
	}
	
	private void insertInfo(RecursMerli recursMerli, Connection c)
	{
		ArrayList alcon = new ArrayList();
		alcon.add(new Integer(recursMerli.getIdRecurs()));
		alcon.add(recursMerli.getEstat());
		alcon.add(recursMerli.getResponsable());
		try{
			AccesBD.executeInsert("mer_rec_info",alcon,c);//idRecurs+","+recursMerli.getEstat()+",'"+recursMerli.getResponsable()+"'"
		}catch(Exception e){
			logger.warn("Error al inserir la info ->"+e.getMessage());
		}
	}
	
	private void updateInfo(RecursMerli recursMerli, Connection c) throws MerliDBException
	{
		ArrayList al = new ArrayList();
		al.add(recursMerli.getEstat());
		ArrayList lRec = new ArrayList();
		int idRecurs = recursMerli.getIdRecurs();
		lRec.add(new Integer(idRecurs));
		if (recursMerli.getResponsable() == null || "".equals(recursMerli.getResponsable())){
			logger.error("No es pot inserir un responsable buit.");
			throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
		}
		al.add(recursMerli.getResponsable());
		AccesBD.executeUpdate("mer_rec_info","id_rec = ?",lRec," id_estat = ?, v_responsable = ?",al,c);		//(,idRecurs+",0,'"+recursMerli.getResponsable()+"'",cb.getConnection());
	}
	
	private void insertContribucions(RecursMerli recursMerli, Connection c)
	{
		ArrayList al, alcon;
		int idRecurs = recursMerli.getIdRecurs();
		al = recursMerli.getContribution();
		MerliContribution mc;
		String data;
		SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");
		for (int i = 0; i< al.size(); i++){
			mc = (MerliContribution) al.get(i);
			data = "'"+sDate.format(mc.getDate())+"'";
			if ((data.compareTo("''")==0)||data=="'null'"){
				data = "trunc(SYSDATE)";
			}
			alcon = new ArrayList();
			alcon.add(new Integer(mc.getIdRol()));
			alcon.add(mc.getEntity());
			alcon.add(mc.getDate());
			alcon.add(mc.getDescription());
			alcon.add(new Integer(idRecurs));
			try{
				//logger.info("InsertContribucio 1");
				AccesBD.executeInsert("mer_contribucio",alcon,c);//mc.getIdRol()+",'"+mc.getEntity()+"',"+data+",'"+mc.getDescription()+"',"+idRecurs
				
			}catch(Exception e){
				logger.warn("Contribucio amb error:"+ al.get(i) + " ->"+e.getMessage());
			}
		}
	}
	
	private void updateContribucions(RecursMerli recursMerli, Connection c) throws MerliDBException
	{
		ArrayList al, alcon;
		al = recursMerli.getContribution();
		MerliContribution mc;
		ArrayList lRec = new ArrayList();
		int idRecurs = recursMerli.getIdRecurs();
		lRec.add(new Integer(idRecurs));

		//esborro l'antiga contribucio 'editor', si es el cas, ja s'insertara de nou	
		try{
			AccesBD.executeDelete("mer_contribucio", "id_rec = ? AND id_rol_cont = 8",lRec,c);
		}catch(Exception e){
			logger.warn("No s'ha pogut esborrar l'editor ->"+e.getMessage());
		}
		
		try{
			//inserto les noves contribucions i edito les que ja existien
			for (int i = 0; i< al.size(); i++){
				mc = (MerliContribution) al.get(i);
				lRec.clear();
				lRec.add(new Integer(idRecurs));
				lRec.add(new Integer(mc.getIdRol()));
				alcon =  new ArrayList();
				alcon.add(mc.getEntity());
				alcon.add(mc.getDate());
				if(mc.getDescription()!=null) alcon.add(mc.getDescription());
				else alcon.add("");
				if (0 == AccesBD.executeExist("mer_contribucio","id_rec = ? AND id_rol_cont = ?",lRec,c))
				{
					alcon =  new ArrayList();
					alcon.add(new Integer(mc.getIdRol()));
					alcon.add(mc.getEntity());
					alcon.add(new Date(System.currentTimeMillis()));
					switch (mc.getIdRol()){
						case MerliContribution.AUTOR: alcon.add("Autor/a del recurs");
						break;
						case MerliContribution.CORRECTOR: alcon.add("Corrector/a del recurs");
						break;
						case MerliContribution.ETIQUETADOR: alcon.add("Etiquetador/a del recurs");
						break;
						case MerliContribution.VALIDADOR: alcon.add("Validador/a del recurs");
						break;
						case MerliContribution.EDITOR: alcon.add("Editor/a del recurs");
						break;
					}
					alcon.add(new Integer(idRecurs));
					AccesBD.executeInsert("mer_contribucio",alcon,c);
				}
				else
					AccesBD.executeUpdate("mer_contribucio","id_rec = ? AND id_rol_cont = ?",lRec," v_entitat = ?, d_data = ?, v_descripcio = ? ",alcon,c);
			}
		}catch(Exception e){
			logger.warn("No s'han pogut actualitzar les contribucions ->"+e.getMessage());
		}
	}
	
	private void insertDrets(RecursMerli recursMerli, Connection c)
	{
		ArrayList alcon = new ArrayList();
		int idRecurs = recursMerli.getIdRecurs();
		alcon.add(new Integer(idRecurs));
		alcon.add(recursMerli.getRightsDesc());
		alcon.add(new Integer(-1));
		alcon.add(recursMerli.getLicense());
		alcon.add(recursMerli.getCost());
		try{
			AccesBD.executeInsert("mer_drets",alcon,c);//idRecurs+",'"+Utility.toParaulaDB(recursMerli.getRightsDesc())+"',null,"+recursMerli.getLicense()+","+recursMerli.getCost()
		}catch(Exception e){
			logger.warn("Error inserint els drets ->"+e.getMessage());
		}
	}
	
	private void updateDrets(RecursMerli recursMerli, Connection c) throws NumberFormatException, MerliDBException
	{
		ArrayList al = new ArrayList();
		ArrayList alcon = new ArrayList();
		ArrayList lRec = new ArrayList();
		int idRecurs = recursMerli.getIdRecurs();
		lRec.add(new Integer(idRecurs));
		try{
			al.add(new Integer(recursMerli.getCost()));
		}catch(Exception e){
			al.add("");
		}
		al.add(recursMerli.getRightsDesc());
		al.add(recursMerli.getLicense());
		if (0 == AccesBD.executeExist("mer_drets","id_rec",((Integer) lRec.get(0)).toString(),"",c)){
			alcon.add(new Integer(idRecurs));
			alcon.add(recursMerli.getRightsDesc());	
			alcon.add(new Integer("-1"));
			alcon.add(recursMerli.getLicense());
			alcon.add(recursMerli.getCost());
			AccesBD.executeInsert("mer_drets",alcon,c);
			//AccesBD.executeInsert("mer_drets",idRecurs+",'"+
			//		recursMerli.getRightsDesc()+"',null,"+recursMerli.getLicense()+","+recursMerli.getCost(),cb.getConnection());
		}
		else{
			AccesBD.executeUpdate("mer_drets","id_rec = ?",lRec,"COST=?, DESCRIPCIO = ?, ID_LLICENCIA = ? ",al,c);
		}
	}
	
	private void insertRelacions(RecursMerli recursMerli, Connection c)
	{
		ArrayList alcon = new ArrayList();
		int idRecurs = recursMerli.getIdRecurs();
		ArrayList rr=recursMerli.getRecursRelacionat();
		if(rr!=null)	
		{
			for(int i=0;i<rr.size();i++)
			{
				if(recursMerli.getRecursRelacionat().get(i)!=null && recursMerli.getTipusRelacio().get(i)!=null)
				{
					alcon.clear();
					alcon.add(new Integer(idRecurs));
					alcon.add(recursMerli.getRecursRelacionat().get(i));
					alcon.add(recursMerli.getTipusRelacio().get(i));
					alcon.add(recursMerli.getDescripcioRelacio().get(i));
					try{
					AccesBD.executeInsert("mer_relacio_recursos",alcon,c);//idRecurs+",'"+Utility.toParaulaDB(recursMerli.getRightsDesc())+"',null,"+recursMerli.getLicense()+","+recursMerli.getCost()
					}catch(Exception e){
						logger.warn("Relacio amb recursos amb error:"+ recursMerli.getRecursRelacionat() + " ->"+e.getMessage());
					}
				}
			}
		}		
	}
	
	private void insertFisic(RecursMerli recursMerli, Connection c) throws MerliDBException
	{
		ArrayList alcon = new ArrayList();
		int idRecurs = recursMerli.getIdRecurs();
		if(recursMerli.getEsFisic())	
		{
			alcon.add(new Integer(idRecurs));
			alcon.add(recursMerli.getCaractRFisic());
			//recuperem la unitat creadora a partir de l'usuari
			// si el recurs no té ja una unitat creadora assignada.
			// (casos de migració o d'administrador)
			Integer idUnitat;
			if (recursMerli.getUnitatCreadora()==null || "".equals(recursMerli.getUnitatCreadora())){
				String query = "Select ID_UNITAT from mer_users where v_user = ?";
				ArrayList lCond = new ArrayList();
				lCond.add(recursMerli.getResponsable());				
				String sUnitat = AccesBD.executeQuery(query, lCond, "id_unitat" , c);
								
				if(sUnitat!=null && !sUnitat.equals(""))	//li assignem al recurs la unitat del responsable, si es que en te
					idUnitat = Integer.valueOf(sUnitat);
				else
				{
					String unitatDefecte = MessageResources.getMessageResources(APPLICATION_RESOURCES).getMessage("mediateca.unitat.creadora");
					idUnitat = new Integer(unitatDefecte);
				}
				
			}else{
				idUnitat = Integer.valueOf(recursMerli.getUnitatCreadora());
			}
			alcon.add(idUnitat);
			AccesBD.executeInsert("mer_recurs_fisic",alcon,c);//id_rec, v_caracteristiques, id_unitat_creadora
			
			for (int i=0; i<recursMerli.getIdFisic().size(); i++){
				if(recursMerli.getTipusIdFisic().get(i)!=null && !recursMerli.getTipusIdFisic().get(i).equals(""))
				{
					alcon.clear();
					alcon.add(new Integer(idRecurs));
					alcon.add(recursMerli.getTipusIdFisic().get(i));
					alcon.add(recursMerli.getIdFisic().get(i));
					AccesBD.executeInsert("mer_idfisic",alcon,c);//id_rec, id_tipus, v_valor
				}
			}
			
			ArrayList unis=recursMerli.getUnitats();
			for(int i=0;i<unis.size();i++)
			{
				alcon.clear();
				alcon.add(new Integer(idRecurs));
				alcon.add(unis.get(i));
				AccesBD.executeInsert("mer_rec_disp_uni",alcon,c);//id_rec, id_unitat
			}
		}
	}
	
	private void updateFisic(RecursMerli recursMerli, Connection c, User user) throws MerliDBException
	{
		ArrayList alcon = new ArrayList();
		int idRecurs = recursMerli.getIdRecurs();
		ArrayList lRec = new ArrayList();
		lRec.add(new Integer(idRecurs));
		if(recursMerli.getEsFisic())	
		{
			// recuperem la unitat creadora a partir de l'usuari
			// si el recurs no té ja una unitat creadora assignada.
			// (casos de migració o d'administrador)
			boolean existeixUnitat=true;
			Integer idUnitat=new Integer(0);
			if (recursMerli.getUnitatCreadora()==null || "".equals(recursMerli.getUnitatCreadora())){
				String query = "Select ID_UNITAT from mer_users where v_user = ?";
				ArrayList lCond = new ArrayList();
				lCond.add(recursMerli.getResponsable());				
				String sUnitat = AccesBD.executeQuery(query, lCond, "id_unitat" , c);
				//logger.info("1.0: Sunitat: "+sUnitat);
				//logger.info(lCond);
				if(sUnitat!=null && !sUnitat.equals("")){//li assignem al recurs la unitat del responsable, si es que en te
					idUnitat = Integer.valueOf(sUnitat);
					//logger.info("1.1: "+ idUnitat);
				}					
				else if(user.getUnitat()!=null)	{					//si no en te, li assignem la unitat de l'usuari
					idUnitat = user.getUnitat();
					//logger.info("1.2: "+ idUnitat);
				}					
				else existeixUnitat=false;					//si ni el recurs, ni el responsable, ni l'usuari tenen unitat assignada, no insertem
				//logger.info("1:" + existeixUnitat+", USUARIO: "+ query);
			}else{
				idUnitat = Integer.valueOf(recursMerli.getUnitatCreadora());
			}

				if (0 != AccesBD.executeExist("mer_recurs_fisic","id_recurs = ?",lRec,c)){
					alcon.clear();
					alcon.add(recursMerli.getCaractRFisic());
					AccesBD.executeUpdate("mer_recurs_fisic","id_recurs = ?",lRec, " v_caracteristiques = ? " , alcon,c);//id_rec, v_caracteristiques, id_unitat_creadora
					//logger.info("2");
				}
				else if(existeixUnitat)
				{
					alcon.clear();
					alcon.add(new Integer(idRecurs));
					alcon.add(recursMerli.getCaractRFisic());
					alcon.add(idUnitat);
					AccesBD.executeInsert("mer_recurs_fisic",alcon,c);//id_rec, v_caracteristiques, id_unitat_creadora
					//logger.info("3");
				}

				AccesBD.executeDelete("mer_idfisic","id_rec = ?",lRec,c);		
				ArrayList tipus = recursMerli.getTipusIdFisic();
				ArrayList ids=recursMerli.getIdFisic();
				for(int i=0; i<tipus.size()&&i<ids.size();i++)
				{
					if(tipus.get(i)!=null && !tipus.get(i).equals(""))
					{
						alcon.clear();
						alcon.add(new Integer(idRecurs));
						alcon.add(tipus.get(i));
						alcon.add(ids.get(i));
						try
						{
							AccesBD.executeInsert("mer_idfisic",alcon,c);//id_rec, id_tipus, v_valor
						}
						catch(MerliDBException e)
						{
							logger.info("Error mer_idfisic: "+e);
							//TODO: mostrar misssatge d'error, tipus repetit
						}
						//logger.info("4");
					}
				}
				
				AccesBD.executeDelete("mer_rec_disp_uni","id_rec = ?",lRec,c);
				ArrayList unis=recursMerli.getUnitats();
				for(int i=0;i<unis.size();i++)
				{
					alcon.clear();
					alcon.add(new Integer(idRecurs));
					alcon.add(unis.get(i));
					AccesBD.executeInsert("mer_rec_disp_uni",alcon,c);//id_rec, id_unitat
					//logger.info("5");
				}
		}
		else	//es un recurs online, esborro totes les entrades de recurs_fisic, per si era un recurs fisic
		{
			AccesBD.executeDelete("mer_idfisic", "id_rec = ?", lRec, c);
			AccesBD.executeDelete("mer_rec_disp_uni", "id_rec = ?", lRec, c);
			AccesBD.executeDelete("mer_recurs_fisic", "id_recurs = ?", lRec, c);
			//logger.info("6");
		}
	}

	public Map nombreOcurrencies(ArrayList nodes, boolean b, int max) {
		ArrayList camps=new ArrayList();
		Map m=new HashMap();
		Map h=new HashMap();
		String taula, ident;
		ConnectionBean cb = null;
		Connection c =null;
			
		if(b)
		{
			taula="mer_rec_termes";
			ident="id_terme";
		}
		else
		{
			taula="mer_rec_paraules";
			ident="id_paraula";
		}
		camps.add(ident);
		camps.add("count(*)");
		
		String cond=ident+" in(";
		for(int i=0;i<max;i++)
		{
			Node n = (Node) nodes.get(i);
			cond+=n.getIdNode();
			if(i<max-1) cond+=",";
		}
		cond+=") group by "+ident;
		try
		{
			cb = connectBD();
			c = cb.getConnection();
			m = AccesBD.getObject(taula, camps,cond, c);
			
			ArrayList ids = (ArrayList) m.get(ident);
			ArrayList counts = (ArrayList) m.get("count(*)");
			for (int i = 0; i < ids.size(); i++) {
				BigDecimal id = (BigDecimal)ids.get(i);
				BigDecimal count = (BigDecimal) counts.get(i);
				h.put(id, count);
			}
		}
		catch (Exception e) {
			logger.error("Error carregant el nombre d'ocurrencies del terme", e);
			try {
				c.rollback();
				logger.warn("Error carregant el nombre d'ocurrencies del terme ->"+e.getMessage());
			} catch (SQLException e1) {}
		}finally{
			try {
				disconnectBD(cb);
			} catch (MerliDBException e) {
				e.printStackTrace();
			}
		}
		return h;
	}
	
	public int nombreDisponibilitatsByIdRecurs(int idRec) {
		ConnectionBean cb = null;
		Connection c =null;
		int res=-1;
		
		try
		{
			cb = connectBD();
			c = cb.getConnection();
			res=AccesBD.executeCount("mer_rec_disp_uni", "id_rec=?", Utility.toList(String.valueOf(idRec)), c);
		}	catch (Exception e) {
			logger.error("Error comprovant última disponibilitat", e);
		}finally{
			try {
				disconnectBD(cb);
			} catch (MerliDBException e) {}
		}
		return res;
	}
}
