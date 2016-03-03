package edu.xtec.merli.ws;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.merli.utils.Utility;
import edu.xtec.merli.ws.objects.APXTEC32;
import edu.xtec.merli.ws.objects.Classification;
import edu.xtec.merli.ws.objects.Contribute;
import edu.xtec.merli.ws.objects.DUCRelation;
import edu.xtec.merli.ws.objects.DescripcioFisica;
import edu.xtec.merli.ws.objects.Duration;
import edu.xtec.merli.ws.objects.DurationElement;
import edu.xtec.merli.ws.objects.ElementDUC;
import edu.xtec.merli.ws.objects.Entity;
import edu.xtec.merli.ws.objects.IdElement;
import edu.xtec.merli.ws.objects.IdResource;
import edu.xtec.merli.ws.objects.Identifier;
import edu.xtec.merli.ws.objects.LOMES;
import edu.xtec.merli.ws.objects.LangString;
import edu.xtec.merli.ws.objects.LangStringList;
import edu.xtec.merli.ws.objects.ListDUC;
import edu.xtec.merli.ws.objects.LlistatGeneric;
import edu.xtec.merli.ws.objects.Lom;
import edu.xtec.merli.ws.objects.Relacio;
import edu.xtec.merli.ws.objects.Rights;
import edu.xtec.merli.ws.objects.SourceValue;
import edu.xtec.merli.ws.objects.Taxon;
import edu.xtec.merli.ws.objects.TaxonPath;
import edu.xtec.merli.ws.objects.Unitat;
import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticException;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;


public class WSMerliBD {
	protected static ConnectionBeanProvider broker;
	private static final Logger logger = Logger.getRootLogger();
	private boolean lomes = false;


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
		}catch(Exception e){
			logger.error("Error al crear la connexio->"+e);			
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);}
		//Retorna un ConnectionBean.
		return bd;
	}

	private void tancaConexio(Connection c, ConnectionBean cb) {
		if (c != null){
			try {
				c.close();
				disconnectBD(cb);
			} catch (SQLException e) {
				logger.error("Error al tancar la conexio ->"+e);
				e.printStackTrace();
			} catch (MerliDBException e) {
				logger.error("Error al tancar la conexio ->"+e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Es desconnecta de la BD.
	 * @throws MerliDBException
	 */
	private void disconnectBD(ConnectionBean cb) throws MerliDBException{
		//Allibera el connectionBean utilitzat.
		if (cb != null)
			try{
				broker.freeConnectionBean(cb);
			}catch(Exception e){
				logger.error("Error al desconnectar la connexio->"+e);			
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
	}


	public int getIdStatus(String status){
		if (APXTEC32.STX_CA_RETORNAT.equals(status))
			return APXTEC32.ST_ID_RETORNAT;
		if (APXTEC32.STX_CA_DENEGAT.equals(status))
			return APXTEC32.ST_ID_DENEGAT;
		if (APXTEC32.STX_CA_ESBORRANY.equals(status))
			return APXTEC32.ST_ID_ESBORRANY;
		if (APXTEC32.STX_CA_PENDENT.equals(status))
			return APXTEC32.ST_ID_PENDENT;
		if (APXTEC32.STX_CA_PER_REVISAR.equals(status))
			return APXTEC32.ST_ID_PER_REVISAR;
		if (APXTEC32.STX_CA_EN_REVISIO.equals(status))
			return APXTEC32.ST_ID_EN_REVISIO;
		if (APXTEC32.STX_CA_REVISANT.equals(status))
			return APXTEC32.ST_ID_REVISANT;
		if (APXTEC32.STX_CA_ACCEPTAT.equals(status))
			return APXTEC32.ST_ID_ACCEPTAT;
		
		if (APXTEC32.STX_ESBORRANY.equals(status))
			return APXTEC32.ST_ID_ESBORRANY ;
		if (APXTEC32.STX_PENDENT.equals(status))
			return APXTEC32.ST_ID_PENDENT;
		if (APXTEC32.STX_RETORNAT.equals(status))
			return APXTEC32.ST_ID_RETORNAT;
		if (APXTEC32.STX_DENEGAT.equals(status))
			return APXTEC32.ST_ID_DENEGAT;
		if (APXTEC32.STX_EN_REVISIO.equals(status))
			return APXTEC32.ST_ID_EN_REVISIO;
		if (APXTEC32.STX_REVISANT.equals(status))
			return APXTEC32.ST_ID_REVISANT;
		if (APXTEC32.STX_ACCEPTAT.equals(status))
			return APXTEC32.ST_ID_ACCEPTAT;
		return APXTEC32.ST_ID_ESBORRANY;
	}

	
	/**
	 * add recurs
	 * 
	 * @param lom
	 * @return
	 * @throws MerliDBException
	 */
	public IdResource addRecurs(Lom lom) throws MerliDBException {
			
			ConnectionBean cb = connectBD();
			Connection c = null;
			ArrayList lIdForm = new ArrayList();
			int idLom = 0;
			String idRecurs;
			
			try{
				//DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
				c = cb.getConnection();

				c.setAutoCommit(false);
				//Si no retorna un id acaba l'operacio.
				idLom = WSAccesBD.getNext("seq_merli", c);
				idRecurs = String.valueOf(idLom);
				boolean esFisic = (lom.getDescripcioFisica()!=null && lom.getDescripcioFisica().getUnitatCreadora()!=null && lom.getDescripcioFisica().getUnitatCreadora().getIdentifier().intValue()!=0);
				
			lom.getGeneral().getIdentifier().setEntry(APXTEC32.MERLI+"/"+idRecurs);
				try{
					carregarIdsLom(lom,c,lIdForm, esFisic);
				}catch(Exception e){
					logger.error("Error al carregar id's ->"+e);
					e.printStackTrace();
				}
				lom.getGeneral().getIdentifier().setIdLom(idLom);	
				
				/*INSERT recurs a MER_RECURS*/
				//Si no funciona l'insert del recurs no segueix amb la insercio.
				WSAccesBD.executeInsert("mer_recurs",lom.getAddQuery(),c);
				
				/*INSERT valors de les traduccions a MER_REC_LANG*/
				addTextualLangValues(lom, idRecurs, c);
				
				/*INSERT valors a MER_REC_LLENGUA*/
				addLanguageList(lom.getGeneral().getLanguageList(), idRecurs, c);
				
				/*INSERT valors a MER_REC_NIVELL_EDUCATIU*/
				addContextList(lom.getEducational().getContextList(), lom.getEducational().getTypicalAgeRangeMin(), lom.getEducational().getTypicalAgeRangeMax(), idRecurs, c);
				
				/*INSERT valors del Thesaurus ETB, del DUC i Competències i Ambit.*/				
				addClassification(lom.getClassificationList(), idRecurs, c);
				
				/*INSERT valors a MER_REC_PARAULES*/				
				addKeywords(lom.getGeneral().getKeywordList(), idRecurs, c);
								
				/*INSERT valors a MER_REC_ROL_USUARI*/		
				addIntendedEndUserRole(lom.getEducational().getIntendedEndUserRoleList(), idRecurs, c);
				
				/*INSERT valors a MER_REC_TIPUS_RECURS*/			
				addLearningResourceType(lom.getEducational().getLearningResourceTypeList(), idRecurs, c);
				
				/*INSERT valors a MER_REC_FORMAT*/
				addFormats(lIdForm, idRecurs, esFisic, c);
				
				/*INSERT taula de MER_REC_INFO*/
				//La inserció correcte d'aquest camp és obligatòria per la correcte
				//inserció del recurs.
				addStatusInfo(lom, c);
				
				/*INSERT taula MER_RELACIO_RECURSOS*/
				addRelationList(lom, idRecurs, c);
				
				/*INSERT valors a MER_CONTRIBUCIO*/
				addContributionList(lom, idRecurs, c);
				
				/*INSERT taula de MER_DRETS*/
				addRights(lom.getRights(), idRecurs, c);
				
				/*INSERT de les taules MER_RECURS_FISIC, MER_IDFISIC i MER_REC_DISP_UNI*/
				if(esFisic)	addDescripcioFisica(lom, idRecurs, c);
				
				c.commit();
			}catch(Exception e){
				try {
					c.rollback();
					logger.error("ws Error on addRecurs ->"+e.getMessage());
					e.printStackTrace();
					throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
				} catch (SQLException e1) {
					throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
				}
			}finally{
				tancaConexio(c,cb);
			}
			
			IdResource idResource = new IdResource();
			idResource.setIdentifier(String.valueOf(idLom));
			idResource.setType("MERLI");
			
			return idResource;
		}

	/**
	 * set recurs
	 * @param lom
	 * @return
	 * @throws MerliDBException
	 */
	public IdResource setRecurs(Lom lom) throws MerliDBException {
		
		//ConnectionBean cb = connectBD();
		ConnectionBean cb = null;
		ArrayList al;
		ArrayList alcon = new ArrayList();
		ArrayList lRec = new ArrayList();
		ArrayList lIdForm = new ArrayList();
		
		String idRecurs = lom.getGeneral().getIdentifier().getIdEntry();
		lRec.add(new Integer(idRecurs));
		cb = connectBD();		
		Connection c = null;
		
		boolean esFisic = (lom.getDescripcioFisica()!=null && lom.getDescripcioFisica().getUnitatCreadora()!=null && lom.getDescripcioFisica().getUnitatCreadora().getIdentifier().intValue()!=0);
		carregarIdsLom(lom,cb.getConnection(),lIdForm, esFisic);
		try {
			c = cb.getConnection();
			c.setAutoCommit(true);	
			al = new ArrayList();
			
			//UPDATE recurs a MER_RECURS//
			//obligatori que funcioni correctament.
			WSAccesBD.executeUpdate("mer_recurs","id_rec=?",lRec,lom.getSetQuery("mer_recurs"),lom.getSetQueryFields("mer_recurs", APXTEC32.DEFAULT_LANG),c);			
			
			//WSAccesBD.executeUpdate("mer_rec_lang","id_rec=?",lRec,lom.getSetQuery("mer_rec_lang"),lom.getSetQueryFields("mer_rec_lang"),c);
			delTextualLangValuesString(idRecurs,c);
			addTextualLangValues(lom, idRecurs, c);
			
			//INSERT nous valors a MER_REC_LLENGUA//			
			delLanguageList(idRecurs, c);
			addLanguageList(lom.getGeneral().getLanguageList(), idRecurs, c);
			
			/*INSERT nous valors a MER_REC_NIVELL_EDUCATIU*/
			delContextList(idRecurs, c);
			addContextList(lom.getEducational().getContextList(), lom.getEducational().getTypicalAgeRangeMin(), lom.getEducational().getTypicalAgeRangeMax(), idRecurs, c);
	
	
			/*INSERT valors del Thesaurus ETB, del DUC i Competències i Ambit.*/	
			delClassification(idRecurs, c);
			addClassification(lom.getClassificationList(), idRecurs, c);
			
			/*INSERT paraules clau thesaurus */	
			delKeyword(idRecurs, c);
			addKeywords(lom.getGeneral().getKeywordList(), idRecurs, c);
	
			/*INSERT nous valors a MER_REC_ROL_USUARI*/
			delIntentedEndUserRole(idRecurs, c);
			addIntendedEndUserRole(lom.getEducational().getIntendedEndUserRoleList(), idRecurs, c);
			
			/*INSERT nous valors a MER_REC_TIPS_RECURS*/
			delLearningResourceType(idRecurs, c);	
			addLearningResourceType(lom.getEducational().getLearningResourceTypeList(), idRecurs, c);
				
			
			/*INSERT nous valors a MER_REC_FORMAT*/
			delFormats(idRecurs, esFisic, c);	
			addFormats(lIdForm, idRecurs, esFisic, c);
	
			
			/*INSERT taula de MER_DRETS*/
			addRights(lom.getRights(), idRecurs, c);
			
			/*INSERT valors a MER_CONTRIBUCIO*/
			//Si no funciona no s'inserta el lom.
			al.clear();
			WSAccesBD.executeDelete("mer_rec_ambits","id_rec = ?",lRec,c);	
			WSAccesBD.executeDelete("mer_contribucio","id_rec = ?",lRec,c);			
			addContributionList(lom, idRecurs, c);

			c.commit();
		}catch (MerliDBException me){
			throw me;			
		}catch (Exception e) {
			try {
				c.rollback();
				logger.debug("dades array="+alcon);
				logger.error("ws Error on setRecurs ->"+e.getMessage());
				throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}finally{
			tancaConexio(c,cb);
		}
		
		IdResource idResource = new IdResource();
		idResource.setIdentifier(idRecurs);
		idResource.setType("MERLI");
		
		return idResource;
	}

	public IdResource unpublishRecurs(IdResource idResource) throws MerliDBException {
	
		//ConnectionBean cb = connectBD();
		
		ConnectionBean cb = null;
		Connection c = null;
		String query;
		ArrayList lParam = new ArrayList();
		ArrayList lCond = new ArrayList();
		lCond.add(new Integer(idResource.getIdentifier()));
		
		try {
			cb = connectBD();
			c = cb.getConnection();
			query = "id_estat = ?";
			lParam.add(new Integer(APXTEC32.ESTAT_DENEGAT));
			WSAccesBD.executeUpdate("mer_rec_info","id_rec = ?",lCond,query,lParam,c);
	
			
		} catch (MerliDBException e) { 
			try {
				c.rollback();
				logger.warn("ws Error on delRecurs ->"+e.getMessage());
				throw new MerliDBException(MerliDBException.DELETEERROR);
			} catch (SQLException e1) {
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}finally{
			tancaConexio(c,cb);
		}
	
		return idResource;
	}

	public IdResource delRecurs(IdResource idResource) throws MerliDBException {
	
		//ConnectionBean cb = connectBD();
		if(idResource.getIdentifier()==null || idResource.getIdentifier().length()<1) throw new MerliDBException(MerliDBException.CAMPS_OBLIGATORIS);
				
		ArrayList l = new ArrayList();
		l.add(new Integer(idResource.getIdentifier()));
		ConnectionBean cb = null;
		Connection c = null;
		
		int nbRecursosEsborrats=-1;
		
		try {
			cb = connectBD();
			c=cb.getConnection();
			
			WSAccesBD.executeDelete("mer_rec_llengua","id_rec = ?",l,c);
			delContextList(idResource.getIdentifier(), c);
			WSAccesBD.executeDelete("mer_rec_format","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_termes","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_paraules","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_ambits","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_rol_usuari","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_tipus_recurs","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_mediateca","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_format_fisic","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_contribucio","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_info","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_drets","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_curriculum","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_lang","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_idfisic", "id_rec = ?", l, c);
			WSAccesBD.executeDelete("mer_rec_disp_uni", "id_rec = ?", l, c);
			WSAccesBD.executeDelete("mer_recurs_fisic", "id_recurs = ?", l, c);	
			WSAccesBD.executeDelete("mer_relacio_recursos", "recurs1 = ?", l, c);	
			WSAccesBD.executeDelete("mer_relacio_recursos", "recurs2 = ?", l, c);	
			WSAccesBD.executeDelete("mer_rec_agrega","id_rec = ?",l,c);
			nbRecursosEsborrats = WSAccesBD.executeDelete("mer_recurs","id_rec = ?",l,c);
		} catch (MerliDBException e) { 
			try {
				c.rollback();
				logger.warn("ws Error on delRecurs ->"+e.getMessage());
				throw new MerliDBException(MerliDBException.DELETEERROR);
			} catch (SQLException e1) {
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}finally{
			tancaConexio(c,cb);
		}
		if(nbRecursosEsborrats==0) throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
	
		return idResource;
	}
	
	public Lom getResource(IdResource idResource) throws MerliDBException {	
	
		if(idResource.getIdentifier()==null || idResource.getIdentifier().length()<1) throw new MerliDBException(MerliDBException.CAMPS_OBLIGATORIS);
		
		Lom lom = new Lom(); 
	
		ArrayList lRec = new ArrayList();
		lRec.add(new Integer(idResource.getIdentifier()));

		Connection c = null;
		ConnectionBean cb = null;
		String aux;		

		try{
			cb = connectBD();
			c=cb.getConnection();	
	
			if (lomes){
				aux = getAgregaId(idResource.getIdentifier(), c);
				lom.getGeneral().setIdentifier(new Identifier(APXTEC32.PLATAFORMA_AGREGA,aux));
				lom.getMetaMetaData().setIdentifier(new Identifier(APXTEC32.PLATAFORMA_AGREGA,aux+"-meta"));
				lom.getGeneral().getIdentifier().setIdLom(Integer.parseInt(idResource.getIdentifier()));
				lom.getMetaMetaData().getIdentifier().setIdLom(Integer.parseInt(idResource.getIdentifier()));
			}else{
				lom.getGeneral().setIdentifier(new Identifier(APXTEC32.CELEBRATE,APXTEC32.MERLI+"/"+idResource.getIdentifier()));
				lom.getMetaMetaData().setIdentifier(new Identifier(APXTEC32.CELEBRATE,APXTEC32.MERLI+"/"+idResource.getIdentifier()));
			}
			//Valors estàtics de  aggregationLevel
			lom.getGeneral().getAggregationLevel().setSource(lomSource());
			lom.getGeneral().getAggregationLevel().setValue(APXTEC32.DEFAULT_AGGREGATION_VALUE);
			
			ArrayList lcamps=getFieldsList("mer_recurs",null);
			carregaGeneralMeRLi(lom, lcamps, lRec, c);
			
			//Carrega llistat de llengues.
			carregarTextosIdiomes(lom, getFieldsList("mer_rec_lang",null), lRec, c);
			
			//Carrega Tipus de Destinatari endUserRol
			carregarIntendedEndUserRole(lom, getFieldsList("mer_rec_rol_usuari",null), lRec, c);	
			
			//Carrega dades del context.
			carregarContext(lom, getFieldsList("mer_rec_nivell_educatiu",null), lRec, c);	
				
			//Carrega Tipus de recurs.
			carregarLearningResourceType(lom, getFieldsList("mer_rec_tipus_recurs",null), lRec, c);					
			
			/* Carregar AMBITS */
			carregarAmbitToLifeCycleContribute(lom, lRec, c);	
			
			/*Carrega llistat de DUC.*/
			carregarLlistatDUC(lom, lRec, c);							
	
			//Carrega llistat de paraules clau.
			carregarClassificationTermesThesaurus(lom, idResource.getIdentifier(), c);
			
			//Carrega llistat de paraules obertes.
			carregarKeywords(lom, idResource.getIdentifier(), c);
			
			//Carrega llistat de llengues.
			carregarLanguageList(lom, getFieldsList("mer_rec_llengua",null), lRec, c);	
				
			//Carregar dades de Contribucions.
			carregarContributions(lom, getFieldsList("mer_contribucio",null), lRec, c);	
	
			//Carregar informació de drets: mer_drets
			carregarRights(lom, getFieldsList("mer_drets",null), lRec, c);
				
			//Carregar informació d'estat del recurs: mer_rec_info
			carregarStatus(lom, getFieldsList("mer_rec_info",null), lRec, c);	
			
			//Carregar relacions entre recursos
			carregarRelacions(lom, getFieldsList("mer_relacio_recursos",null), lRec, c);	
			
			//Carregar la descripcio fisica d'un recurs fisic
			carregarDescripcioFisica(lom, getFieldsList("mer_recurs_fisic",null), getFieldsList("mer_unitats",null), lRec, c);
			
			//carregar els identificadors fisics d'un recurs fisic
			lcamps.clear();
			lcamps=getFieldsList("mer_idfisic",null);
			carregarIdsFisics(lom, lcamps, lRec, c);
			
//			carregar les unitats on el recurs esta disponible
			carregarDisponibleA(lom, getFieldsList("mer_rec_disp_uni",null), getFieldsList("mer_unitats",null), lRec, c);
			
			//Carregar dades del format.
			if(lom.getDescripcioFisica()!=null && lom.getDescripcioFisica().getUnitatCreadora()!=null && lom.getDescripcioFisica().getUnitatCreadora().getIdentifier().intValue()!=0)
				carregarFormatFisic(lom, getFieldsList("mer_rec_format_fisic",null), lRec, c);	
			else
				carregarFormat(lom, getFieldsList("mer_rec_format",null), lRec, c);	
			
		}catch (SQLException e){
			logger.error("Error on getResource ("+idResource+")"+" ->"+e.getMessage());
			throw new MerliDBException(MerliDBException.ERROR_SQL);
		}finally{
			tancaConexio(c,cb);
		}

		/* Se li passa el valor LomES per preparar la sortida.*/
		lom.setLomEs(lomes);
		
		return lom;
	}

	private void carregarIdsLom(Lom lom, Connection c, ArrayList lIdForm, boolean esFisic) throws MerliDBException {
			ArrayList al = new ArrayList();
			lom.getGeneral().getIdentifier().setIdLom(Integer.parseInt(lom.getGeneral().getIdentifier().getIdEntry()));
			//		Preparar valors amb id de la BBDD
			al .clear();
			if(lom.getEducational()!=null)
			{
				if (lom.getEducational().getDifficulty().getValue() == null)
					lom.getEducational().getDifficulty().setValue("");
				al.add(lom.getEducational().getDifficulty().getValue());
				lom.getEducational().getDifficulty().setId(WSAccesBD.executeQuery("select id_dificultat from mer_dificultat where dificultat = ?",al,"id_dificultat",c));
			
				for (int i=0;i<lom.getEducational().getContextList().size();i++){
					al.clear();
					al.add(((SourceValue)lom.getEducational().getContextList().get(i)).getValue());
					((SourceValue)lom.getEducational().getContextList().get(i)).setId(WSAccesBD.executeQuery("select id_nivell from mer_nivell_educatiu where nivell = ?",al,"id_nivell",c));
				}
				for (int i=0;i<lom.getEducational().getIntendedEndUserRoleList().size();i++){
					al.clear();
					al.add(((SourceValue)lom.getEducational().getIntendedEndUserRoleList().get(i)).getValue());
					((SourceValue)lom.getEducational().getIntendedEndUserRoleList().get(i)).setId(WSAccesBD.executeQuery("select id_rol_usuari from mer_rol_usuari where rol_usuari = ?",al,"id_rol_usuari",c));
				}
				for (int i=0;i<lom.getEducational().getLearningResourceTypeList().size();i++){
					al.clear();
					al.add(((SourceValue)lom.getEducational().getLearningResourceTypeList().get(i)).getValue());
					((SourceValue)lom.getEducational().getLearningResourceTypeList().get(i)).setId(WSAccesBD.executeQuery("select id_tipus_recurs from mer_tipus_recurs where tipus_recurs = ?",al,"id_tipus_recurs",c));
				}
			}
			if(esFisic)
			{
				for (int i=0;i<lom.getDescripcioFisica().getFormat().size();i++){
					al.clear();
					al.add((String)lom.getDescripcioFisica().getFormat().get(i));
					lIdForm.add(WSAccesBD.executeQuery("select id_format_fisic from mer_formats_fisics where tipus = ?",al,"id_format_fisic",c));
				}
			}
			else
			{
				for (int i=0;i<lom.getTechnical().getFormat().size();i++){
					al.clear();
					al.add((String)lom.getTechnical().getFormat().get(i));
					lIdForm.add(WSAccesBD.executeQuery("select id_format from mer_formats where mime = ?",al,"id_format",c));
				}
			}
			if(lom.getLifeCycle()!=null)
			{
				for (int i=0;i<lom.getLifeCycle().getContributeList().size();i++){
					al.clear();
					al.add(((Contribute)lom.getLifeCycle().getContributeList().get(i)).getRole().getValue());
					((Contribute)lom.getLifeCycle().getContributeList().get(i)).getRole().setId(WSAccesBD.executeQuery("select id_rol from mer_rol_contribucio r where r.ROL= ?",al,"id_rol",c));
				}
			}
			if(lom.getMetaMetaData()!=null)
			{
				for (int i=0;i<lom.getMetaMetaData().getContributeList().size();i++){
					al.clear();
					al.add(((Contribute)lom.getMetaMetaData().getContributeList().get(i)).getRole().getValue());
					((Contribute)lom.getMetaMetaData().getContributeList().get(i)).getRole().setId(WSAccesBD.executeQuery("select id_rol from mer_rol_contribucio r where r.ROL= ?",al,"id_rol",c));
				}
			}
			al.clear();
			if(lom.getRights()!=null)
			{
				if (((SourceValue)lom.getRights().getCopyRightAndOtherRestrictions()).getValue() == null)
					((SourceValue)lom.getRights().getCopyRightAndOtherRestrictions()).setValue("");
			}
			
			if(lom.getLifeCycle()!=null)
			{
				for (int i=0; i < lom.getLifeCycle().getContributeList().size();i++){
					if ("publisher".equals(((Contribute)lom.getLifeCycle().getContributeList().get(i)).getRole().getValue())){
						//idAmbit = new Integer(((Taxon)((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList().get(0)).getId());
							al.clear();
							al.add(((Contribute)lom.getLifeCycle().getContributeList().get(i)).getEntity().getOrg());
							((Contribute)lom.getLifeCycle().getContributeList().get(i)).getEntity().setIdOrg(WSAccesBD.executeQuery("select id_ambit from mer_ambits r where r.AMBIT= ?",al,"id_ambit",c));								
					}
				}
			}
			
	//		for (int i=0; i < lom.getClassificationList().size();i++){
	//			if ("AMBIT".equals(((Classification)lom.getClassificationList().get(i)).getTaxonPath().getSource().getString())){
	//				//idAmbit = new Integer(((Taxon)((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList().get(0)).getId());
	//				for (int j=0; j <((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList().size();j++){
	//					al.clear();
	//					al.add(((Taxon)((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList().get(j)).getEntry().getString());
	//					((Taxon)((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList().get(j)).setId(WSAccesBD.executeQuery("select id_ambit from mer_ambits r where r.AMBIT= ?",al,"id_ambit",c));			
	//				}				
	//			}
	//		}
		}

	private void addTextualLangValues(Lom lom, String idRecurs, Connection c) throws Exception {
		Enumeration listLS = lom.getGeneral().getTitle().getListString().elements();
		LangString ls = null;
		while (listLS.hasMoreElements()){
			ls = (LangString)listLS.nextElement();
			try{
			WSAccesBD.executeInsert("mer_rec_lang",lom.getAddLang(ls.getLang()),c);
			}catch(Exception e){
				if (APXTEC32.DEFAULT_LANG.equals(ls.getLang())){
					logger.error("Error a l'inserir valors textuals obligatoris en "+APXTEC32.DEFAULT_LANG+" ->"+e);
					throw e;
				}
			}
		}
	}

	private void delTextualLangValuesString(String idRecurs, Connection c) throws MerliDBException {
		ArrayList lRec = new ArrayList();		
		lRec.add(new Integer(idRecurs));
		try{
			WSAccesBD.executeDelete("mer_rec_lang","id_rec = ?",lRec,c);
		}catch (MerliDBException e){
			logger.error("error a l'eliminar llengues ->"+e);
			e.printStackTrace();
			throw e;
		}			
	}
	
	private void addContributionList(Lom lom, String idRecurs, Connection c) {
		ArrayList al = new ArrayList();
		ArrayList alcon = new ArrayList();
		boolean hasbeenpublished = false;
		boolean hasbeenauthored = false;
		boolean hasbeenedited = false;
		boolean hasbeencreated = false;
		for (int i = 0; i< lom.getLifeCycle().getContributeList().size(); i++){
			try{
				if (APXTEC32.ROL_PUBLISHER.equals(((Contribute)lom.getLifeCycle().getContributeList().get(i)).getRole().getValue())
					&& ((Contribute)lom.getLifeCycle().getContributeList().get(i)).getEntity().getIdOrg() > 0
					&& !("".equals(((Contribute)lom.getLifeCycle().getContributeList().get(i)).getEntity().getUsername())
							|| ((Contribute)lom.getLifeCycle().getContributeList().get(i)).getEntity().getUsername() == null )){					
					alcon.clear();
					alcon.add(idRecurs);
					alcon.add(new Integer(((Contribute)lom.getLifeCycle().getContributeList().get(i)).getEntity().getIdOrg()));
					WSAccesBD.executeInsert("mer_rec_ambits",alcon,c);
				}else
					al.add(lom.getLifeCycle().getContributeList().get(i));					
			}catch(Exception e){
			}
		}

		al.addAll(lom.getMetaMetaData().getContributeList());
		
		String data = null;
		SimpleDateFormat sDate = new SimpleDateFormat(APXTEC32.DATE_FORMAT);
		for (int i = 0; i< al.size(); i++){
			//Per evitar dupliacions de publishers en les metaMetadades, provocaria que el recurs es dupliques.
			if (APXTEC32.ROL_AUTHOR.equals(((Contribute)al.get(i)).getRole().getValue()))
				if (hasbeenauthored) continue;
				else hasbeenauthored = true;

			if (APXTEC32.ROL_PUBLISHER.equals(((Contribute)al.get(i)).getRole().getValue()))
				if (hasbeenpublished)	continue;
				else hasbeenpublished = true;
			
			if (APXTEC32.ROL_EDITOR.equals(((Contribute)al.get(i)).getRole().getValue()))
				if (hasbeenedited)	continue;
				else hasbeenedited = true;
			
			if (APXTEC32.ROL_CREATOR.equals(((Contribute)al.get(i)).getRole().getValue()))
				if (hasbeencreated)	continue;
				else hasbeencreated = true;
			
			try{
				Date dData = null;
				try{
					data = ((Contribute)al.get(i)).getDateTime().getDateTime();
					dData = sDate.parse(data);
				} catch (Exception e){
					dData = new Date(System.currentTimeMillis());
				}
				
//				if (data != null)
//					data = sDate.format(sDate.parse(data));
//				else data = sDate.format(new Timestamp(System.currentTimeMillis()));//"trunc(SYSDATE)";
//				if ((data.compareTo("''")==0)||data=="'null'")
//					data = sDate.format(new Timestamp(System.currentTimeMillis()));//"trunc(SYSDATE)";				
					
				alcon.clear();
				alcon.add(new Integer(((Contribute)al.get(i)).getRole().getId()));
				alcon.add(((Contribute)al.get(i)).getEntity().getUsername());
				//alcon.add(data);
				alcon.add(dData);
				alcon.add(((Contribute)al.get(i)).getRole().getValue());
				alcon.add(idRecurs);
				WSAccesBD.executeInsert("mer_contribucio",alcon,c);
			}
			catch (Exception e){
				logger.error("Error a l'inserir una contribució ->"+e);
			}
		}
		
		if(!hasbeencreated)
		{
			try{
				Date dData = null;
				dData =new Date(System.currentTimeMillis());

				alcon.clear();
				alcon.add(new Integer(APXTEC32.IROL_CREATOR));
				alcon.add(" ");
				alcon.add(dData);
				alcon.add(APXTEC32.ROL_CREATOR);
				alcon.add(idRecurs);
				WSAccesBD.executeInsert("mer_contribucio",alcon,c);
			}
			catch (Exception e){
				logger.error("Error a l'inserir el catalogador a mer_contribucio ->"+e);
			}
		}
	}
	
	private void addRelationList(Lom lom, String idRecurs, Connection c) {
		ArrayList al = new ArrayList();
		ArrayList alcon = new ArrayList();
		al.addAll(lom.getRelacions());

		for (int i = 0; i< al.size(); i++){
			try{
				alcon.clear();
				alcon.add(idRecurs);									//RECURS1
				alcon.add(((Relacio)al.get(i)).getIdRecursRel());		//RECURS2
				alcon.add(((Relacio)al.get(i)).getKind());				//TIPUS
				if(((Relacio)al.get(i)).getDescription("ca")!=null)		//DESCRIPCIO
					alcon.add(((Relacio)al.get(i)).getDescription("ca").getString());
				else
					alcon.add("");
				WSAccesBD.executeInsert("mer_relacio_recursos",alcon,c);
			}
			catch (Exception e){
				logger.error("Error a l'inserir una relació ->"+e);
				e.printStackTrace();
			}
		}
	}
	
	private void addDescripcioFisica(Lom lom, String idRecurs, Connection c) {
		ArrayList alcon = new ArrayList();
		ArrayList al = new ArrayList();
		DescripcioFisica df = lom.getDescripcioFisica();

		try{
			//inserim la descripcio general del recurs fisic
			alcon.add(idRecurs);									//ID_RECURS
			alcon.add(df.getCaracteristiques());					//V_CARACTERISTIQUES
			alcon.add(df.getUnitatCreadora().getIdentifier());		//ID_UNITAT_CREADORA	
			WSAccesBD.executeInsert("mer_recurs_fisic",alcon,c);
			
			//inserim els identificadors fisics
			al.addAll(df.getIdentificadorFisicList().getObjectList());
			for (int i = 0; i< al.size(); i++){
					alcon.clear();
					alcon.add(idRecurs);								//ID_REC
					alcon.add(((IdResource)al.get(i)).getType());		//V_VALOR
					alcon.add(((IdResource)al.get(i)).getIdentifier());	//V_TIPUS
					WSAccesBD.executeInsert("mer_idfisic",alcon,c);
			}
			
			//inserim les unitats on el recurs és disponible
			al.clear();
			al.addAll(df.getDisponibleA().getObjectList());
			for (int i = 0; i< al.size(); i++){
					alcon.clear();
					alcon.add(idRecurs);								//ID_REC
					alcon.add(((Unitat)al.get(i)).getIdentifier());		//ID_UNITAT
					WSAccesBD.executeInsert("mer_rec_disp_uni",alcon,c);
			}
			//falta inserir a mer_formats_fisics
		}
		catch (Exception e){
			logger.error("Error a l'inserir la descripció física ->"+e);
			e.printStackTrace();
		}
	}

	private void addStatusInfo(Lom lom, Connection c) throws MerliDBException {
		ArrayList alcon = new ArrayList();
		boolean hiaCreator= false;
		alcon.add(new Integer(lom.getGeneral().getIdentifier().getIdLom()));
		alcon.add(new Integer(getIdStatus(lom.getLifeCycle().getStatus().getValue())));
				
		for (int i=0;i<lom.getMetaMetaData().getContributeList().size();i++){
			if ("creator".equals(((Contribute)lom.getMetaMetaData().getContributeList().get(i)).getRole().getValue())){
				int tam = Math.min(((Contribute)lom.getMetaMetaData().getContributeList().get(i)).getEntity().getUsername().length(), 10);
				alcon.add(((Contribute)lom.getMetaMetaData().getContributeList().get(i)).getEntity().getUsername().substring(0,tam));
				hiaCreator= true;
			}
		}
		if (!hiaCreator)	//si no hi ha creador li afegeixo buit
		{
			alcon.add(" ");
			//throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		}
		try{
			WSAccesBD.executeInsert("mer_rec_info",alcon,c);
		}catch (MerliDBException e){
			logger.error("Error a l'afegir Status Info->"+e);
			e.printStackTrace();
			throw e;
		}
	}
	
	private void addRights(Rights rights, String idRecurs, Connection c) {
		ArrayList al = new ArrayList();
		ArrayList alcon = new ArrayList();
		ArrayList lRec = new ArrayList();
		lRec.add(idRecurs);
		
		try{
			if (0 == WSAccesBD.executeExist("mer_drets","id_rec",idRecurs,"",c)){
				alcon.clear();
				alcon.add(idRecurs);
				if (rights.getDescription().getString() == null )
					rights.getDescription().setString("");
				alcon.add(rights.getDescription().getString());	
				alcon.add(new Integer(-1));
				if (rights.getCopyRightAndOtherRestrictions().getId() == null || rights.getCopyRightAndOtherRestrictions().getId() == "0")
					alcon.add("");
				else
					alcon.add(rights.getCopyRightAndOtherRestrictions().getId());					
			
				if ("no".equals(rights.getCost().getValue()))
					alcon.add(new Integer(0));
				else 
					alcon.add(new Integer(1));
				WSAccesBD.executeInsert("mer_drets",alcon,c);
			}else{
				al.clear();			
				if ("no".equals(rights.getCost()))
					al.add(new Integer(0));
				else 
					al.add(new Integer(1));
				
				if (rights.getDescription().getString() == null )
					rights.getDescription().setString("");
				al.add(rights.getDescription().getString());
				
				if (rights.getCopyRightAndOtherRestrictions().getValue() == null )
					rights.getCopyRightAndOtherRestrictions().setValue("");
				
				
				if(rights.getCopyRightAndOtherRestrictions().getId()!=null)
				{
					al.add(rights.getCopyRightAndOtherRestrictions().getId());
					WSAccesBD.executeUpdate("mer_drets","id_rec = ?",lRec,"COST=?, DESCRIPCIO = ?, ID_LLICENCIA = ? ",al,c);
				}
				else WSAccesBD.executeUpdate("mer_drets","id_rec = ?",lRec,"COST=?, DESCRIPCIO = ?",al,c);
			}
		}catch(Exception e){
			logger.error("Error a l'inserir els drets ->"+e);
			e.printStackTrace();
		}
	}
	
	private void delFormats(String idRecurs, boolean esFisic, Connection c) throws MerliDBException {
		ArrayList lRec = new ArrayList();
		lRec.add(idRecurs);
		try{
			if(esFisic)	WSAccesBD.executeDelete("mer_rec_format_fisic","id_rec = ?",lRec,c);	
			else		WSAccesBD.executeDelete("mer_rec_format","id_rec = ?",lRec,c);
		}catch (MerliDBException e){
			logger.error("Error a l'eliminar Formats ->"+e);
			e.printStackTrace();
			throw e;
		}
	}

	private void addFormats( ArrayList lIdForm, String idRecurs, boolean esFisic, Connection c) {
		ArrayList alcon = new ArrayList();
		try{		
			if (!lIdForm.isEmpty()){
				for (int i = 0; i< lIdForm.size(); i++){
					alcon.clear();
					alcon.add(idRecurs);
					alcon.add(lIdForm.get(i));	
					if(esFisic)	WSAccesBD.executeInsert("mer_rec_format_fisic",alcon,c);
					else		WSAccesBD.executeInsert("mer_rec_format",alcon,c);
				}
			}
		}catch(Exception e){
			logger.error("Error a l'inserir el format ->"+e);
			e.printStackTrace();
		}
	}

	private void delLearningResourceType(String idRecurs, Connection c) throws MerliDBException {
		ArrayList lRec = new ArrayList();
		lRec.add(idRecurs);

		try{
			WSAccesBD.executeDelete("mer_rec_tipus_recurs","id_rec = ?",lRec,c);
		}catch (MerliDBException e){
			logger.error("Error a l'eliminar LearningResourceType ->"+e);
			e.printStackTrace();
			throw e;
		}
	}
	
	private void addLearningResourceType(ArrayList lrtList, String idRecurs, Connection c) {
		ArrayList alcon = new ArrayList();
		try{			
			if (!lrtList.isEmpty()){
				for (int i = 0; i< lrtList.size(); i++){
					if(!((SourceValue)lrtList.get(i)).getId().equals("0"))
					{
						alcon.clear();
						alcon.add(idRecurs);
						alcon.add(((SourceValue)lrtList.get(i)).getId());
						WSAccesBD.executeInsert("mer_rec_tipus_recurs",alcon,c);
					}
				}
			}
		}catch(Exception e){
			logger.error("Error a l'inserir tipus de recurs ->"+e);
			e.printStackTrace();
		}
	}

	private void delIntentedEndUserRole(String idRecurs, Connection c) throws MerliDBException {
		ArrayList lRec = new ArrayList();
		lRec.add(idRecurs);

		try{
			WSAccesBD.executeDelete("mer_rec_rol_usuari","id_rec = ?",lRec,c);
		}catch (MerliDBException e){
			logger.error("Error a l'eliminar IntendedEndUserRole ->"+e);
			e.printStackTrace();
			throw e;
		}
	}

	private void addIntendedEndUserRole(ArrayList ieurList, String idRecurs, Connection c) {
		ArrayList alcon = new ArrayList();
		
		for (int i = 0; i< ieurList.size(); i++){	
			try{
				alcon.clear();
				alcon.add(idRecurs);
				alcon.add(((SourceValue)ieurList.get(i)).getId());
				WSAccesBD.executeInsert("mer_rec_rol_usuari",alcon,c);
			}catch(Exception e){
				logger.error("Error a l'inserir rol d'usuari ->"+e);
				e.printStackTrace();
			}
		}
	}
	
	private void delClassification(String idRecurs, Connection c) throws MerliDBException {
		ArrayList lRec = new ArrayList();
		lRec.add(idRecurs);

		try{
			WSAccesBD.executeDelete("mer_rec_ambits","id_rec = ?",lRec,c);	
			WSAccesBD.executeDelete("mer_rec_termes","id_rec = ?",lRec,c);		
			WSAccesBD.executeDelete("mer_rec_curriculum","id_rec = ?",lRec,c);
		}catch (MerliDBException e){
			logger.error("Error a l'eliminar Classification ->"+e);
			e.printStackTrace();
			throw e;
		}
	}
	
	private void addClassification(ArrayList clasList, String idRecurs, Connection c) {
			ArrayList al = new ArrayList();
			
			for (int i=0;i<clasList.size();i++){
				try{
					if (APXTEC32.THESAURUS.equals(((Classification)clasList.get(i)).getPurpose().getSource())){
						for (int j=0; j<((Classification)clasList.get(i)).getTaxonPath().size();j++){
							al.clear();
							al.add(idRecurs);
							al.add(((Taxon)((Classification)clasList.get(i)).getTaxonPath(j).getTaxonList(APXTEC32.LAST)).getId());
							al.add(APXTEC32.THESAURUS);
							WSAccesBD.executeInsert("mer_rec_termes",al,c);
						}
					}
				}catch(Exception e){
					logger.error("Error a l'inserir termes thesaurus ->"+e);
					e.printStackTrace();
				}
				try{
					if (APXTEC32.DUC.equals(((Classification)clasList.get(i)).getPurpose().getSource()) ||
							APXTEC32.COMPETENCIES_DUC.equals(((Classification)clasList.get(i)).getPurpose().getSource())){
						for (int j=0; j<((Classification)clasList.get(i)).getTaxonPath().size();j++){
							al.clear();
							al.add(idRecurs);
							al.add(((Taxon)((TaxonPath)((Classification)clasList.get(i)).getTaxonPath(j)).getTaxonList(APXTEC32.LAST)).getId());
							al.add("content");//ull!! podria ser una area. Es pot comprovar a la bd per l'id_node
							WSAccesBD.executeInsert("mer_rec_curriculum",al,c);
						}
					}
				}catch(Exception e){
					logger.error("Error a l'inserir termes DUC ->"+e);
					e.printStackTrace();
				}					
			}
		}
	
	private void delKeyword(String idRecurs, Connection c) throws MerliDBException {
		ArrayList lRec = new ArrayList();
		lRec.add(idRecurs);
		try{
			WSAccesBD.executeDelete("mer_rec_paraules","id_rec = ?",lRec,c);	
		}catch (MerliDBException e){
			logger.error("Error a l'eliminar les paraules  ->"+e);
			throw e;
		}
	}
	
	private void addKeywords(ArrayList keywordList, String idRecurs, Connection c) {
		ArrayList al = new ArrayList();
		ArrayList camps = new ArrayList();
		Map m=new HashMap();
		camps.add("id_paraula");
		for (int i=0;i<keywordList.size();i++){
			try{
				al.clear();
				al.add(((LangStringList)keywordList.get(i)).getLangString("ca").getString());
				if(!al.isEmpty()) m = WSAccesBD.getObject("mer_paraules", camps, "v_paraula = ?", al , c);
				if(!m.isEmpty())
				{
					al.clear();
					al.add(idRecurs);
					al.add(((ArrayList)m.get("id_paraula")).get(0));
					WSAccesBD.executeInsert("mer_rec_paraules",al,c);
				}
			}
			catch(Exception e){
				logger.error("Error a l'inserir paraules obertes ->"+e);
				e.printStackTrace();
			}
		}
	}
	
	

	private void delContextList(String idRecurs, Connection c) throws MerliDBException {
		ArrayList lRec = new ArrayList();
		
		lRec.add(new Integer(idRecurs));
		try{
			WSAccesBD.executeDelete("mer_rec_nivell_educatiu","id_rec = ?",lRec,c);
		}catch (MerliDBException e){
			logger.error("Error a l'eliminar nivell educatiu ->"+e);
			throw e;
		}
	}

	private void addContextList(ArrayList contextList, String ageMin, String ageMax, String idRecurs, Connection c) {
	/*	ArrayList alcon = new ArrayList();
		try{
			if (!contextList.isEmpty()){
				for (int i = 0; i< contextList.size(); i++){	
					alcon.clear();
					alcon.add(idRecurs);
					alcon.add(((SourceValue)contextList.get(i)).getId());								
					WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);
				}
			}
		}catch (Exception e){
			logger.error("Error a l'inserir nivell educatiu ->"+e);
			e.printStackTrace();
		}
	*/
		String aux;
		ArrayList alcon = new ArrayList();
		ArrayList lRec = new ArrayList();
		
		lRec.add(new Integer(idRecurs));
		
		try{
			WSAccesBD.executeDelete("mer_rec_nivell_educatiu","id_rec = ?",lRec,c);
			if (!contextList.isEmpty())
				for (int i = 0; i< contextList.size(); i++){
					try{
						//En cas de Compulsory-education parsegem l'edat per saber si correspon
						//a Ed. Primaria o a Ed. Secundaria, a ambdos o a cap.
						if ("2".equals(((SourceValue)contextList.get(i)).getId())){
							if ( ageMin != null && !"".equals(ageMin) && 12>=Integer.parseInt(ageMin)){
								alcon.clear();
								alcon.add(idRecurs);
								alcon.add("3");
								WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
							}
							if (ageMax != null && !"".equals(ageMax) && 12<=Integer.parseInt(ageMax)){
								alcon.clear();
								alcon.add(idRecurs);
								alcon.add("4");
								WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
							}
						}else{
							//En cas de pre-school parsegem l'edat per saber si correspon
							//a Llar d'infants o a Parvulari, a ambdos o a cap.
							if ("1".equals(((SourceValue)contextList.get(i)).getId())){
								if ( ageMin != null && !"".equals(ageMin) && 3>=Integer.parseInt(ageMin)){
									alcon.clear();
									alcon.add(idRecurs);
									alcon.add("1");
									WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
								}
								if (ageMax != null && !"".equals(ageMax) && 3<=Integer.parseInt(ageMax)){
									alcon.clear();
									alcon.add(idRecurs);
									alcon.add("2");
									WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
								}
							}else{
								alcon.clear();
								alcon.add(((SourceValue)contextList.get(i)).getId());
								aux = WSAccesBD.executeQuery("select id_nivell_cat from mer_nivell_edu_cat where id_nivell = ?",alcon,"id_nivell_cat",c);
								alcon.clear();
								alcon.add(idRecurs);
								alcon.add(aux);
								WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
							}
						}
					}catch(Exception e){
						logger.error("Error al afegir contextos ->"+e);
						e.printStackTrace();
					}
				}
			
		}catch (Exception e){
			logger.error("Error a l'inserir nivell educatiu ->"+e);
			e.printStackTrace();
		}
	
	}
	
	private void delLanguageList(String idRecurs, Connection c) throws MerliDBException {
		ArrayList lRec = new ArrayList();		
		lRec.add(new Integer(idRecurs));
		try{
			WSAccesBD.executeDelete("mer_rec_llengua","id_rec = ?",lRec,c);
		}catch (MerliDBException e){
			logger.error("error a l'eliminar llengues ->"+e);
			e.printStackTrace();
			throw e;
		}			
	}

	private void addLanguageList(ArrayList listLang, String idRecurs, Connection c) {
		ArrayList alcon = new ArrayList();
		ArrayList lRec = new ArrayList();
		
		lRec.add(new Integer(idRecurs));
		try{
			if (!listLang.isEmpty())
				for (int i = 0; i< listLang.size(); i++){
					alcon.clear();
					alcon.add(idRecurs);
					alcon.add(listLang.get(i));
					WSAccesBD.executeInsert("mer_rec_llengua",alcon,c);
				}
		}catch (Exception e){
			logger.error("error a l'inserir Llengues ->"+e);
			e.printStackTrace();
		}
	}

	/**
	 * Carrega els camps del punt 6.Rights. Agafa els valors de mer_drets i mer_llicencia.
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarRights(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		Map m;
		ArrayList al = new ArrayList();
		try {				
			if (lomes){ 
		System.out.println("LOMES- mapejar RIGTHS");
				LOMES.mapejarRights(lom.getRights()); 
			}else{
				m = WSAccesBD.getObject("mer_drets",campsList,"id_rec = ?",lRec,c);				
				if (((ArrayList) m.get("id_llicencia")).size() > 0 && ((ArrayList) m.get("id_llicencia")).get(0) != null){
					al.clear();	
					BigDecimal ll=((BigDecimal)((ArrayList) m.get("id_llicencia")).get(0));
					al.add(ll.toString());
					if(ll.equals(new BigDecimal(9)))
					{
						lom.getRights().setCopyRightAndOtherRestrictions(new SourceValue(lomSource(),"no","copyRightAndOtherRestrictions"));//WSAccesBD.executeQuery("select llicencia from mer_llicencia where id_llicencia = ?",al,"llicencia",c),"copyRightAndOtherRestrictions"));
					}
					else
					{
						lom.getRights().setCopyRightAndOtherRestrictions(new SourceValue(lomSource(),"yes","copyRightAndOtherRestrictions"));//WSAccesBD.executeQuery("select llicencia from mer_llicencia where id_llicencia = ?",al,"llicencia",c),"copyRightAndOtherRestrictions"));
						lom.getRights().getCopyRightAndOtherRestrictions().setId(ll.toString());
					}

					if (((ArrayList) m.get("descripcio")).size() > 0
							&& ((ArrayList) m.get("descripcio")).get(0) != null) {
						try {
							lom.getRights().setDescription(new LangString((String) ((ArrayList) m.get("descripcio")).get(0),APXTEC32.DEFAULT_LANG));
							lom.getRights().setDescription(new LangString(WSAccesBD.executeQuery("select llicencia from mer_llicencia where id_llicencia = ?", al,"llicencia", c), "x-t-cc"));
							String sURL = WSAccesBD.executeQuery("select url from mer_llicencia where id_llicencia = ?", al, "url", c);
							if (sURL != null)
								lom.getRights().setDescription(new LangString(sURL, "x-t-cc-url"));
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					}
				}

				if (((ArrayList) m.get("cost")).size() > 0 && ((ArrayList) m.get("cost")).get(0) != null){
					if("0".equals(((BigDecimal) ((ArrayList) m.get("cost")).get(0)).toString()))
						lom.getRights().setCost(new SourceValue(lomSource(),"no","cost"));
					else if("1".equals(((BigDecimal) ((ArrayList) m.get("cost")).get(0)).toString()))
						lom.getRights().setCost(new SourceValue(lomSource(),"yes","cost"));
				}
			}
		}catch(Exception e){
			logger.error("Error a l'obtenir els drets ->"+e);
			e.printStackTrace();
		}
	}

	/**
	 * Carrega el camp 3.2 i 2.3 Contributions.
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarContributions(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		ArrayList al = new ArrayList();
		Contribute contribute;
		Map m;
		try{
			m = WSAccesBD.getObjectList("mer_contribucio",campsList,"id_rec = ?",lRec,"id_rec",c);
			
			SimpleDateFormat sDate = new SimpleDateFormat(APXTEC32.DATE_FORMAT);
			for (int i = 0; i< ((ArrayList)m.get("id_rol_cont")).size();i++){
				al.clear();
				contribute = new Contribute();

				al.add(((BigDecimal)(((ArrayList) m.get("id_rol_cont")).get(i))).toString());
				contribute.setRole(new SourceValue(lomSource(),WSAccesBD.executeQuery("select rol from mer_rol_contribucio where id_rol = ?",al,"rol",c),"role"));
				contribute.setEntity(new Entity((String) ((ArrayList) m.get("v_entitat")).get(i),"",APXTEC32.DEPT_EDUCACIO,""));
				if (contribute.getEntity().getUsername()!=null)
					contribute.getEntity().getUsername().trim();
				try{
					contribute.setDateTime(new edu.xtec.merli.ws.objects.Date(sDate.format((Date) ((ArrayList) m.get("d_data")).get(i))));
				}catch (ClassCastException e){
					contribute.setDateTime(new edu.xtec.merli.ws.objects.Date(sDate.format((Timestamp) ((ArrayList) m.get("d_data")).get(i))));					
				}
				//contribute.setDateTime(new edu.xtec.merli.ws.objects.Date(sDate.format((Timestamp) ((ArrayList) m.get("d_data")).get(i))));
				if ((contribute.getRole().getValue() != null && contribute.getRole().getValue().trim().length() > 0) && 
				    (contribute.getEntity().getUsername() != null && contribute.getEntity().getUsername().trim().length() > 0)){
					if (contribute.getRole().getValue().equals(APXTEC32.ROL_AUTHOR))
						lom.getLifeCycle().getContributeList().add(contribute);
					else if (contribute.getRole().getValue().equals(APXTEC32.ROL_EDITOR))
						lom.getLifeCycle().getContributeList().add(contribute);
					else{
						if (lomes) LOMES.mapejarContributionLOMES(contribute);
						lom.getMetaMetaData().getContributeList().add(contribute);
					}
				}
			}
		}catch(Exception e){
			logger.error("Error a l'obtenir les contribucions ->"+e);
			e.printStackTrace();
		}
	}

	/**
	 * Carrega els formats 4.1 Technical.Format
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarFormat(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		ArrayList laux;
		ArrayList al = new ArrayList();
		Map m;
		try{
			laux = new ArrayList();
			m = WSAccesBD.getObjectList("mer_rec_format",campsList,"id_rec = ?",lRec,"id_rec",c);
			for (int i = 0; i< ((ArrayList)m.get("id_format")).size();i++){
				al.clear();
				al.add(((BigDecimal)((ArrayList) m.get("id_format")).get(i)).toString());
				laux.add(WSAccesBD.executeQuery("select mime from mer_formats where id_format = ?",al,"mime",c));
			}
			lom.getTechnical().setFormat(laux);
		}catch(Exception e){
			logger.error("Error a l'obtenir els formats ->"+e);
			e.printStackTrace();
		}
	}
	public String getFormat(String mime) {
		ArrayList lcamps = new ArrayList();
		String result="";
		ArrayList lMimes = new ArrayList();
		ConnectionBean cb = null;
		Connection c = null;

		try{
			cb = connectBD();
			c=cb.getConnection();
			lcamps.add("id_format"); lMimes.add(mime);
			Map m =  WSAccesBD.getObjectList("mer_formats",lcamps,"mime = ?",lMimes,"",c);
			lcamps=((ArrayList) m.get("id_format"));
			if (lcamps.size()>0) result = String.valueOf(lcamps.get(0));
		}catch(Exception e){
			logger.error("Error a l'obtenir els formats ->"+e);
			e.printStackTrace();
		}finally{
			tancaConexio(c,cb);
		}
		return result;
	}
	public String getLlicencia(String llic) {
		ArrayList lcamps = new ArrayList();
		String result="";
		ArrayList lLlic = new ArrayList();
		ConnectionBean cb = null;
		Connection c = null;

		try{
			cb = connectBD();
			c=cb.getConnection();
			lcamps.add("id_llicencia"); lLlic.add(llic);
			Map m =  WSAccesBD.getObjectList("mer_llicencia",lcamps,"llicencia = ?",lLlic,"",c);
			lcamps=((ArrayList) m.get("id_llicencia"));
			if (lcamps.size()>0) result = String.valueOf(lcamps.get(0));
		}catch(Exception e){
			logger.error("Error a l'obtenir l'id de llicencia ->"+e);
			e.printStackTrace();
		}finally{
			tancaConexio(c,cb);
		}
		return result;
	}
	public String getDificultat(String dif) {
		ArrayList lcamps = new ArrayList();
		String result="";
		ArrayList lLlic = new ArrayList();
		ConnectionBean cb = null;
		Connection c = null;

		try{
			cb = connectBD();
			c=cb.getConnection();
			lcamps.add("id_dificultat"); lLlic.add(dif);
			Map m =  WSAccesBD.getObjectList("mer_dificultat",lcamps,"dificultat = ?",lLlic,"",c);
			lcamps=((ArrayList) m.get("id_dificultat"));
			if (lcamps.size()>0) result = String.valueOf(lcamps.get(0));
		}catch(Exception e){
			logger.error("Error a l'obtenir l'id de dificultat ->"+e);
			e.printStackTrace();
		}finally{
			tancaConexio(c,cb);
		}
		return result;
	}
	public String getContext(String context) {
		ArrayList lcamps = new ArrayList();
		String result="";
		ArrayList lLlic = new ArrayList();
		ConnectionBean cb = null;
		Connection c = null;

		try{
			cb = connectBD();
			c=cb.getConnection();
			lcamps.add("id_nivell_cat"); lLlic.add(context);
			Map m =  WSAccesBD.getObjectList("mer_nivell_edu_cat c, mer_nivell_educatiu e",lcamps,"e.id_nivell = c.id_nivell and e.nivell = ?",lLlic,"",c);
			lcamps=((ArrayList) m.get("id_nivell_cat"));
			if (lcamps.size()>0) result = String.valueOf(lcamps.get(0));
		}catch(Exception e){
			logger.error("Error a l'obtenir l'id de dificultat ->"+e);
			e.printStackTrace();
		}finally{
			tancaConexio(c,cb);
		}
		return result;
	}
	public String getRolUsuari(String rol) {
		ArrayList lcamps = new ArrayList();
		String result="";
		ArrayList lLlic = new ArrayList();
		ConnectionBean cb = null;
		Connection c = null;

		try{
			cb = connectBD();
			c=cb.getConnection();
			lcamps.add("id_rol_usuari"); lLlic.add(rol);
			Map m =  WSAccesBD.getObjectList("mer_rol_usuari",lcamps,"rol_usuari = ?",lLlic,"",c);
			lcamps=((ArrayList) m.get("id_rol_usuari"));
			if (lcamps.size()>0) result = String.valueOf(lcamps.get(0));
		}catch(Exception e){
			logger.error("Error a l'obtenir l'id de rol d'usuari ->"+e);
			e.printStackTrace();
		}finally{
			tancaConexio(c,cb);
		}
		return result;
	}
	public String getTipusRecurs(String tipusRecurs) {
		ArrayList lcamps = new ArrayList();
		String result="";
		ArrayList lLlic = new ArrayList();
		ConnectionBean cb = null;
		Connection c = null;

		try{
			cb = connectBD();
			c=cb.getConnection();
			lcamps.add("id_tipus_recurs"); lLlic.add(tipusRecurs);
			Map m =  WSAccesBD.getObjectList("mer_tipus_recurs",lcamps,"tipus_recurs = ?",lLlic,"",c);
			lcamps=((ArrayList) m.get("id_tipus_recurs"));
			if (lcamps.size()>0) result = String.valueOf(lcamps.get(0));
		}catch(Exception e){
			logger.error("Error a l'obtenir l'id de tipus d'usuari ->"+e);
			e.printStackTrace();
		}finally{
			tancaConexio(c,cb);
		}
		return result;
	}
	public String getAmbit(String ambit) {
		ArrayList lcamps = new ArrayList();
		String result="";
		ArrayList lAmbit = new ArrayList();
		ConnectionBean cb = null;
		Connection c = null;

		try{
			cb = connectBD();
			c=cb.getConnection();
			lcamps.add("id_ambit"); lAmbit.add(ambit);
			Map m =  WSAccesBD.getObjectList("mer_ambits",lcamps,"ambit = ?",lAmbit,"",c);
			lcamps=((ArrayList) m.get("id_ambit"));
			if (lcamps.size()>0) result = String.valueOf(lcamps.get(0));
		}catch(Exception e){
			logger.error("Error a l'obtenir l'id d'ambit ->"+e);
			e.printStackTrace();
		}finally{
			tancaConexio(c,cb);
		}
		return result;
	}
	
	/**
	 * Carrega els formats dels recursos físics.
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarFormatFisic(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		ArrayList laux;
		ArrayList al = new ArrayList();
		Map m;
		try{
			laux = new ArrayList();
			m = WSAccesBD.getObjectList("mer_rec_format_fisic",campsList,"id_rec = ?",lRec,"id_rec",c);
			for (int i = 0; i< ((ArrayList)m.get("id_format_fisic")).size();i++){
				al.clear();
				al.add(((BigDecimal)((ArrayList) m.get("id_format_fisic")).get(i)).toString());
				laux.add(WSAccesBD.executeQuery("select tipus from mer_formats_fisics where id_format_fisic = ?",al,"tipus",c));
			}
			lom.getDescripcioFisica().setFormat(laux);
		//	lom.getTechnical().setFormat(laux);
		}catch(Exception e){
			logger.error("Error a l'obtenir els formats fisics ->"+e);
			e.printStackTrace();
		}
	}

	/**
	 * Carrega els camps 2.2 LifeCycle.Status. 
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarStatus(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		Map m;
		try {
			m = WSAccesBD.getObject("mer_rec_info",campsList,"id_rec = ?",lRec,c);				
			
			if (((ArrayList) m.get("id_estat")).size() > 0){
				String estatLom = "";
				switch(((BigDecimal)((ArrayList) m.get("id_estat")).get(0)).intValue()){
					case -2: estatLom = APXTEC32.STX_DENEGAT;
						break;
					case -1:	estatLom = APXTEC32.STX_RETORNAT;
						break;
					case 0:	estatLom = APXTEC32.STX_ESBORRANY;
						break;
					case 1: estatLom = APXTEC32.STX_PER_REVISAR;
						break;
					case 2: estatLom = APXTEC32.STX_EN_REVISIO;
						break;
					case 3: estatLom = APXTEC32.STX_REVISANT;
						break;
					case 4: estatLom = APXTEC32.STX_ACCEPTAT;
						break;
					case 10:	estatLom = APXTEC32.STX_PENDENT;
						break;
				}
				lom.getLifeCycle().setStatus(new SourceValue(lomSource(),estatLom,"status"));
			}
//				contribute = new Contribute();
//				contribute.setRole(new SourceValue(lomSource(),LOM_CREATOR,"role"));
//				contribute.setEntity(new Entity((String) ((ArrayList) m.get("v_responsable")).get(0),"",APXTEC32.DEPT_EDUCACIO,""));
//				if (((ArrayList) m.get("d_data")) != null)
//				contribute.setDateTime(new edu.xtec.merli.ws.objects.Date((Timestamp) ((ArrayList) m.get("d_data")).get(0)));
//				
//				lom.getMetaMetaData().getContributeList().add(contribute);
		}catch(Exception e){
			logger.error("Error a l'obtenir la informació d'estat del recurs ->"+e);
			e.printStackTrace();
		}
	}

	/**
	 * Carrega el llistat d'idiomes del recurs a 1.3 General.Language
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarLanguageList(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		ArrayList laux = new ArrayList();
		Map m;
		try{	
			m = WSAccesBD.getObjectList("mer_rec_llengua", campsList, "id_rec = ?", lRec, "id_llengua desc", c);
			for (int i = 0; i< ((ArrayList)m.get("id_llengua")).size();i++){
				laux.add((String)((ArrayList) m.get("id_llengua")).get(i));
			}
			lom.getGeneral().setLanguageList(laux);
		}catch(Exception e){
			logger.error("Error a l'obtenir les llengües ->"+e);
			e.printStackTrace();
		}
		//Afegim CA pel 5.11
		laux = new ArrayList();
		laux.add("ca");
		lom.getEducational().setLanguageList(laux);
	}

	/**
	 * Carrega els camps que poden estar traduits. 1.2 General.Title, 1.4 General.Description i 6.3 Rigts.Description
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarTextosIdiomes(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		Map m;
		//Nomes catala
//		m = WSAccesBD.getObjectList("mer_rec_lang",rm.getFieldsList("mer_rec_lang"),"id_rec = ? AND lang = 'ca'",lRec,"id_rec",c);
//		if (((ArrayList) m.get("titol")).size() > 0)
//			lom.getGeneral().setTitle(new LangString((String) ((ArrayList) m.get("titol")).get(0),LANG));
//		if (((ArrayList) m.get("descripcio")).size() > 0)
//			lom.getGeneral().setDescription(new LangString((String) ((ArrayList) m.get("descripcio")).get(0),LANG));
//		if (((ArrayList) m.get("drets")).size() > 0)
//			lom.getRights().setDescription(new LangString((String) ((ArrayList) m.get("drets")).get(0),LANG));
		
		try
		{
			String lang = null;
			String estat = "0";
			ArrayList camps = new ArrayList();
			camps.addAll(campsList);
			m = WSAccesBD.getObjectList("mer_rec_lang",camps,"id_rec = ?",lRec,"id_rec",c);
			for (int i = 0; i< ((ArrayList)m.get("lang")).size();i++){
				try{
					lang = (String)((ArrayList) m.get("lang")).get(i);
					estat = ((BigDecimal)((ArrayList) m.get("estat")).get(i)).toString();
					if ("1".equals(estat)){
						if (((ArrayList) m.get("titol")).size() > 0)
							lom.getGeneral().setTitle(new LangString((String) ((ArrayList) m.get("titol")).get(i),lang));
						if (((ArrayList) m.get("descripcio")).size() > 0)
							lom.getGeneral().setDescription(new LangString((String) ((ArrayList) m.get("descripcio")).get(i),lang));
						if (((ArrayList) m.get("drets")).size() > 0)
							lom.getRights().setDescription(new LangString((String) ((ArrayList) m.get("drets")).get(i),lang));
					}					
				}catch(Exception e){
					logger.error("Error a l'obtenir idioma:"+lang+" ->"+e);
					e.printStackTrace();
				}
			}
			// SARA: Comentat perque aquesta llista es recupera de mer_rec_llengua: lom.getGeneral().setLanguageList(laux);
		}catch(Exception e){
			logger.error("Error a l'obtenir les llengües ->"+e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Carrega el camp 7 Relations
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarRelacions(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		ArrayList al = new ArrayList();
		Relacio relacio;
		Map m;
		try{
			m = WSAccesBD.getObjectList("mer_relacio_recursos",campsList,"recurs1 = ?",lRec,"recurs2",c);
			
			for (int i = 0; i< ((ArrayList)m.get("recurs2")).size();i++){
				al.clear();
				relacio = new Relacio();

				int idRec2=((BigDecimal) ((ArrayList) m.get("recurs2")).get(i)).intValue();
				relacio.setIdRecursRel(new Integer(idRec2));
				
				//enviem el titol del recurs relacionat en el camp descripcio
				String sDesc = WSAccesBD.executeQuery("select titol from mer_recurs where id_rec="+idRec2, "titol", c);
				relacio.setDescription(new LangString(sDesc, APXTEC32.DEFAULT_LANG));
				
				relacio.setKind((String) ((ArrayList) m.get("tipus")).get(i));
				
				//aixo hauria de ser un array d'strings: ArrayList descripcions = (ArrayList) ((ArrayList) m.get("descripcio")).get(i);
				//String sDesc= (String) ((ArrayList) m.get("descripcio")).get(i);
				//relacio.setDescription(new LangString((String) descripcions.get(0),APXTEC32.DEFAULT_LANG));

				lom.getRelacions().add(relacio);
			}
		}catch(Exception e){
			logger.error("Error a l'obtenir les relacions ->"+e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Carrega el camp Descripcio Fisica
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarDescripcioFisica(Lom lom, ArrayList campsList, ArrayList campsList2, ArrayList lRec, Connection c) {
	
		Map m;
		try{
			if (lom.getDescripcioFisica() == null) lom.setDescripcioFisica(new DescripcioFisica());
			//m = WSAccesBD.getObjectList("mer_recurs_fisic r, mer_unitats u",campsList,"r.id_recurs = ? and r.id_unitat_creadora=u.id_unitat",lRec,"r.id_recurs",c);
			m = WSAccesBD.getJoin(campsList, campsList2, "mer_recurs_fisic", "mer_unitats", "id_unitat_creadora", "id_unitat","id_recurs="+lRec.get(0), "", "id_recurs", "id_unitat", "ASC", c);
			//descripcioFisica = new DescripcioFisica();
			if(((ArrayList) m.get("id_unitat_creadora")).size()==1)
			{
				String sCaracteristiques="";
				if(((ArrayList) m.get("v_caracteristiques")).get(0)!=null)
					sCaracteristiques= (String) ((ArrayList) m.get("v_caracteristiques")).get(0).toString();
				//descripcioFisica.setCaracteristiques(new LangString(sCaracteristiques),APXTEC32.DEFAULT_LANG));
				lom.getDescripcioFisica().setCaracteristiques(sCaracteristiques);
				
				int idUnitat=((BigDecimal) ((ArrayList) m.get("id_unitat_creadora")).get(0)).intValue();
				String sUnitat = (String) ((ArrayList) m.get("v_nom")).get(0);
				lom.getDescripcioFisica().setUnitatCreadora(new Unitat(new Integer(idUnitat),sUnitat));
			}
			
		}catch(Exception e){
			logger.error("Error a l'obtenir la descripció física ->"+e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Carrega el camp identificador física de Descripcio Física
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarIdsFisics(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		IdResource id;
		LlistatGeneric aux= new LlistatGeneric(IdResource.class,"identificadors");
		Map m;
		try{
			m = WSAccesBD.getObjectList("mer_idfisic",campsList,"id_rec = ?",lRec,"v_tipus",c);
			
			for (int i = 0; i< ((ArrayList)m.get("v_tipus")).size();i++){
				id = new IdResource();
				id.setType((String) ((ArrayList) m.get("v_tipus")).get(i));
				id.setIdentifier((String) ((ArrayList) m.get("v_valor")).get(i));
				id.setLomEs(false);
				aux.getObjectList().add(id);
			}
			lom.getDescripcioFisica().setIdentificadorFisicList(aux);
		}catch(Exception e){
			logger.error("Error a l'obtenir els identificadors físics ->"+e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Carrega les unitats on esta disponible el recurs
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarDisponibleA(Lom lom, ArrayList campsList, ArrayList campsList2,ArrayList lRec, Connection c) {
		Unitat u;
		LlistatGeneric aux= new LlistatGeneric(Unitat.class,"disponibleA");
		Map m;
		try{
			m = WSAccesBD.getJoin(campsList, campsList2, "mer_rec_disp_uni", "mer_unitats", "id_unitat", "id_unitat", "id_rec = "+lRec.get(0), "", "id_unitat", "id_unitat", "ASC", c);
		//	m = WSAccesBD.getObjectList("mer_rec_disp_uni ru, mer_unitats u",campsList,"ru.id_rec = ? and ru.id_unitat=u.id_unitat",lRec,"u.id_unitat",c);
			
			for (int i = 0; i< ((ArrayList)m.get("v_nom")).size();i++){
				u = new Unitat();
				int idUni=((BigDecimal) ((ArrayList) m.get("id_unitat")).get(i*2)).intValue();//com que en les dos taules el nom del camp coincideix (id_unitat), es guarden els valors duplicats (per aixo fem %2) 
				u.setIdentifier(new Integer(idUni));
				u.setName((String) ((ArrayList) m.get("v_nom")).get(i));
				aux.getObjectList().add(u);
			}
			lom.getDescripcioFisica().setDisponibleA(aux);
		}catch(Exception e){
			logger.error("Error a l'obtenir els identificadors físics ->"+e);
			e.printStackTrace();
		}
	}
	

	/**
	 * Carrega dels camps amb un únic valor i més generals:
	 * 1.2 General.Titol, 1.4 General.Description, 2.1 LifeCycle.Version, 4.3 Technical.Location 4.7 Technical.Duration, 
	 * 5.8 Educational.Difficulty, 5.9 Educational.TypicalLearningTime, 5.7 TypicalAgeRange, 6.1 Rights.Cost, 
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 * @throws SQLException
	 * @throws MerliDBException
	 */
	private void carregaGeneralMeRLi(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) throws SQLException, MerliDBException {
		Map m;
		String aux;
		ArrayList al = new ArrayList();
		
		m= WSAccesBD.getObjectList("mer_recurs",campsList,"id_rec=?",lRec,"id_rec",c);
		if (((ArrayList) m.get("descripcio")).size() <= 0){
			//logger.error("Error, el recurs solicitat no existeix-> "+idResource);
			throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
		}
		if (((ArrayList) m.get("titol")).size() > 0)
			lom.getGeneral().setTitle(new LangString((String) ((ArrayList) m.get("titol")).get(0),APXTEC32.DEFAULT_LANG));
		
		if (((ArrayList) m.get("descripcio")).size() > 0)
			lom.getGeneral().setDescription(new LangString((String) ((ArrayList) m.get("descripcio")).get(0),APXTEC32.DEFAULT_LANG));
		
		try{
			if (((ArrayList) m.get("id_dificultat")).size() > 0){
				al.clear();
				if (((ArrayList) m.get("id_dificultat")).get(0) != null){
					al.add(((BigDecimal)((ArrayList) m.get("id_dificultat")).get(0)).toString());
					lom.getEducational().setDifficulty(new SourceValue(lomSource(),WSAccesBD.executeQuery("select dificultat from mer_dificultat where id_dificultat = ?",al,"dificultat",c),"dificultat"));
				}
			}
		}catch(Exception e){
			logger.error("Error a l'obtenir la dificultat ->"+e);
			e.printStackTrace();
		}	

		if (((ArrayList) m.get("drets")).size() > 0){
			if (((BigDecimal)((ArrayList) m.get("drets")).get(0)).toString().compareTo("0") == 0 )
				lom.getRights().setCost(new SourceValue(lomSource(),"no","cost"));
			else
				lom.getRights().setCost(new SourceValue(lomSource(),"yes","cost"));
		}
			
		if (((ArrayList) m.get("duracio")).size() > 0){
			Duration oDuration = new Duration((String) ((ArrayList) m.get("duracio")).get(0));
			lom.getEducational().setTypicalLearningTime(oDuration);
			lom.getTechnical().setDuration(new DurationElement(oDuration));
		}
		
		if (((ArrayList) m.get("edat_max")).size() > 0)
			aux = ((BigDecimal)((ArrayList) m.get("edat_max")).get(0)).toString();
		else
			aux = "U";
		
		if (((ArrayList) m.get("edat_min")).size() > 0)
			aux = ((BigDecimal)((ArrayList) m.get("edat_min")).get(0)).toString()+"-"+aux;
		else
			aux = "U-"+aux;
		
		lom.getEducational().setTypicalAgeRange(new LangString(aux,APXTEC32.DEFAULT_LANG));
		
		//21/10/2007 Canvis pels idiomes (JPT)
		//Es carrega desde MER_REC_LANG 
//			if (((ArrayList) m.get("titol")).size() > 0)
//				lom.getGeneral().setTitle(new LangString((String) ((ArrayList) m.get("titol")).get(0),LANG));			
		
		if (((ArrayList) m.get("url")).size() > 0)
			lom.getTechnical().setLocation((String) ((ArrayList) m.get("url")).get(0));
		
		if (((ArrayList) m.get("versio")).size() > 0)
			lom.getLifeCycle().setVersion(new LangString((String) ((ArrayList) m.get("versio")).get(0),APXTEC32.DEFAULT_LANG));
		
		if (((ArrayList) m.get("context")).size() > 0)
			lom.getGeneral().setCoverage(new LangString((String) ((ArrayList) m.get("context")).get(0),APXTEC32.DEFAULT_LANG));
		
	}
	
	/**
	 * Carrega l'Ambit o ambits del recurs com a LifeCycle.Contribution - "publisher"
	 * @param lom
	 * @param lRec
	 * @param c
	 */
	private void carregarAmbitToLifeCycleContribute(Lom lom, ArrayList lRec, Connection c) {
		Contribute contribute;
		ArrayList al = new ArrayList();
		Map m;
		try{
//				laux = new ArrayList();
//				if (lom.getClassificationList() == null)
//					lom.setClassificationList(new ArrayList());
//				classification = new Classification();
//				classification.setPurpose(new SourceValue(AMBIT,"competency",APXTEC32.PURPOSE));
//			
//				taxonPath = new TaxonPath();
//				taxonPath.setSource(new LangString(AMBIT,LANG));
//				al.clear();
//				al.add("id_ambit");
//				//Canviar al per rm.getFieldsList("mer_rec_ambits").
//				m = WSAccesBD.getObject("mer_rec_ambits",al,"id_rec = ?",lRec,c);
//				for (int i = 0; i< ((ArrayList)m.get("id_ambit")).size();i++){
//					al.clear();
//					al.add(((BigDecimal)((ArrayList) m.get("id_ambit")).get(i)).toString());	
//					laux.add(new Taxon(((BigDecimal)((ArrayList) m.get("id_ambit")).get(i)).toString(),new LangString(WSAccesBD.executeQuery("select ambit from mer_ambits where id_ambit = ?",al,"ambit",c),LANG)));					
//				}
//				taxonPath.setTaxonList(laux);
//				classification.setTaxonPath(taxonPath);
//				classification.setDescription(new LangString("Ambit del recurs",LANG));	
//				lom.getClassificationList().add(classification);
			

			al.clear();
			al.add("id_ambit");
			//Canviar al per rm.getFieldsList("mer_rec_ambits").
			m = WSAccesBD.getObject("mer_rec_ambits",al,"id_rec = ?",lRec,c);
			SimpleDateFormat sDate = new SimpleDateFormat(APXTEC32.DATE_FORMAT);
			for (int i = 0; i< ((ArrayList)m.get("id_ambit")).size();i++){
				
				contribute = new Contribute();
				
				contribute.setRole(new SourceValue(lomSource(), APXTEC32.ROL_PUBLISHER,APXTEC32.ROL));

				al.clear();
				al.add(((BigDecimal)((ArrayList) m.get("id_ambit")).get(i)).toString());	
				
				//contribute.setEntity(new Entity("","",WSAccesBD.executeQuery("select ambit from mer_ambits where id_ambit = ?",al,"ambit",c),""));
				String org = WSAccesBD.executeQuery("select ambit from mer_ambits where id_ambit = ?",al,"ambit",c);
				contribute.setEntity(new Entity(org, org, "", org, ""));					
				try{
					contribute.setDateTime(new edu.xtec.merli.ws.objects.Date(sDate.format(new Date(System.currentTimeMillis()))));
				}catch (Exception e){
					contribute.setDateTime(new edu.xtec.merli.ws.objects.Date(""));					
				}
				
				if (contribute.getEntity().getOrg() != null && contribute.getEntity().getOrg().trim().length() > 0)
					lom.getLifeCycle().getContributeList().add(contribute);
			}
			
			
		}catch(Exception e){
			logger.error("Error a l'obtenir els àmbits ->"+e);
			e.printStackTrace();
		}
	}

	/**
	 * Carregar dels tipus de recurs a 5.2 Educational.LearningResourceType
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarLearningResourceType(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		ArrayList laux;
		ArrayList al = new ArrayList();
		Map m;
		try{
			laux = new ArrayList();
			m = WSAccesBD.getObjectList("mer_rec_tipus_recurs",campsList,"id_rec = ?",lRec,"id_rec",c);
			for (int i = 0; i< ((ArrayList)m.get("id_tipus_recurs")).size();i++){
				if (lomes) al = LOMES.mapejarIdLearningResourceType(((BigDecimal)((ArrayList) m.get("id_tipus_recurs")).get(i)).toString());
				else{ 
					laux.clear();
					laux.add(((BigDecimal)((ArrayList) m.get("id_tipus_recurs")).get(i)).toString());	
					al.add(WSAccesBD.executeQuery("select tipus_recurs from mer_tipus_recurs where id_tipus_recurs = ?",laux,"tipus_recurs",c));
				}

				String st;
				for (int j=0; j < al.size(); j++){
					st = (String)al.get(j);
					lom.getEducational().getLearningResourceTypeList().add(new SourceValue(lre3Source(), st,"learningResourceType"));
				}
				al.clear();
			}
		}catch(Exception e){
			logger.error("Error a l'obtenir el tipus de recurs ->"+e);
			e.printStackTrace();
		}
	}

	/**
	 * Carrega del context (nivell educatiu) a 5.6 Educational.Context
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarContext(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		ArrayList laux;
		ArrayList al = new ArrayList();
		Map m;
		String aux;
		try{
			boolean afegir;
			laux = new ArrayList();
			m = WSAccesBD.getObjectList("mer_rec_nivell_educatiu",campsList,"id_rec = ?",lRec,"id_rec",c);
			for (int i = 0; i< ((ArrayList)m.get("id_nivell")).size();i++){
				afegir = true;
				al.clear();				
				al.add(((BigDecimal)((ArrayList) m.get("id_nivell")).get(i)).toString());
				aux = WSAccesBD.executeQuery("select merne.nivell from mer_nivell_edu_cat mern, mer_nivell_educatiu merne where mern.id_nivell = merne.ID_NIVELL and mern.id_nivell_cat = ?",al,"nivell",c);
				//Mapeig per a convertir a LOMes.
				if (lomes) aux=LOMES.mapejarContextLOMES(aux);
				for (int j=0; j<laux.size();j++){
					if (aux.equals(((SourceValue)laux.get(j)).getValue()))
						afegir = false;
				}
				if (afegir){
					String sSource = lre3Source();
					if ("higher education".equals(aux)) sSource = lomSource();
					laux.add(new SourceValue(sSource, aux, "context"));
				}
			}
			lom.getEducational().setContextList(laux);
		}catch(Exception e){
			logger.error("Error a l'obtenir el context ->"+e);
			e.printStackTrace();
		}
	}

	/**
	 * Carrega del tipus de destinatari a 5.5 Educational.IntendedEndUseRole
	 * @param lom
	 * @param campsList
	 * @param lRec
	 * @param c
	 */
	private void carregarIntendedEndUserRole(Lom lom, ArrayList campsList, ArrayList lRec, Connection c) {
		ArrayList laux;
		ArrayList al = new ArrayList();
		Map m;
		try{
			laux = new ArrayList();
			m = WSAccesBD.getObjectList("mer_rec_rol_usuari", campsList, "id_rec = ?", lRec, "id_rol_usuari desc", c);
			for (int i = 0; i< ((ArrayList)m.get("id_rol_usuari")).size();i++){
				al.clear();
				al.add(((BigDecimal)((ArrayList) m.get("id_rol_usuari")).get(i)).toString());
				String sValue = WSAccesBD.executeQuery("select rol_usuari from mer_rol_usuari where id_rol_usuari = ?",al,"rol_usuari",c);
				//Mapeig per a convertir a LOMes.
				if (lomes) sValue=LOMES.mapejarIntEndUserRolLOMES(sValue);
				String sSource = lomSource();
				if (APXTEC32.IEUR_COUNSELOR.equals(sValue) || APXTEC32.IEUR_PARENT.equals(sValue)) sSource = lre3Source();
				//Si és NULL //o OTHER no l'enviem.
				if (sValue != null)// && !APXTEC32.IEUR_OTHER.equals(sValue))
					laux.add((new SourceValue(sSource, sValue, "intendedEndUserRol")));
			}	
			lom.getEducational().setIntendedEndUserRoleList(laux);
		}catch(Exception e){
			logger.error("Error a l'obtenir el rol d'usuari ->"+e);
			e.printStackTrace();
		}
	}

	/**
	 * Carrega dels termes del thesaurus en els diversos idiomes disponibles a 9 Classification.
	 * purpose:discpline, source:LOMv1.0, taxonPath.source:ETB
	 * @param lom
	 * @param idRec
	 * @param c
	 */
	private void carregarClassificationTermesThesaurus(Lom lom,String idRec,Connection c) {
		ArrayList laux;
		ArrayList al = new ArrayList();
		TaxonPath taxonPath;
		Taxon t;
		Classification classification=null;
		Map m;
	//	RecursMerli rm;
		ArrayList lRec;	
	
		//ArrayList lKeyword = new ArrayList(); //KEYWORDS
		try{
		//	rm = new RecursMerli(0);
			lRec = new ArrayList();
			lRec.add(idRec);
			laux = new ArrayList();
			classification = new Classification();
			
			m = WSAccesBD.getObject("mer_rec_termes", getFieldsList("mer_rec_termes"),"id_rec = ?",lRec,c);
			int iSize = ((ArrayList)m.get("id_terme")).size();
			for (int i = 0; i< iSize;i++){			
				taxonPath = new TaxonPath();
				laux = new ArrayList();
				if (lomes){
					taxonPath.setSource(new LangString(APXTEC32.THESAURUSLOMes,APXTEC32.DEFAULT_LANG));
				}else{
					taxonPath.setSource(new LangString(APXTEC32.THESAURUS,APXTEC32.DEFAULT_LANG));
				}
				al.clear();
				al.add(((BigDecimal)((ArrayList) m.get("id_terme")).get(i)).toString());
				//Recupera el recurs pare del terme
				//t = getThesaurusPath(((BigDecimal)((ArrayList) m.get("id_terme")).get(i)).toString(), c);
				//if (t!= null) laux.add(t);
				t = getThesaurusTerm(((BigDecimal)((ArrayList) m.get("id_terme")).get(i)).toString(),c);
				laux.add(t);
				//Nomes agafem les 10 primeres paraules clau perque es el limit que imposa MELT //KEYWORDS
				//if (i<10) lKeyword.add(t.getEntryList()); //KEYWORDS
				taxonPath.setTaxonList(laux);	
				classification.addTaxonPath(taxonPath);	
			}
			}catch(Exception e){
			logger.error("Error a l'obtenir els termes del thesaurus ->"+e);
			e.printStackTrace();
		}
		//Carregar, de moment, la mateixa llista de paraules clau a General.Keyword (camp obligatori del MELT)
		//if (lKeyword.size()>0)
		//	lom.getGeneral().setKeywordList(lKeyword);
		
		//classification.setPurpose(new SourceValue(APXTEC32.THESAURUS,"discipline",APXTEC32.PURPOSE));
		if (lomes){
			classification.setPurpose(new SourceValue(APXTEC32.LOMes,APXTEC32.PURP_DISCPLINE,APXTEC32.PURPOSE));
		}else{
			classification.setPurpose(new SourceValue(APXTEC32.THESAURUS,APXTEC32.PURP_DISCPLINE,APXTEC32.PURPOSE));
		}
		classification.setDescription(APXTEC32.ETB_DESCRIPTION_CA);	
		classification.setDescription(APXTEC32.ETB_DESCRIPTION_ES);				
		classification.setDescription(APXTEC32.ETB_DESCRIPTION_EN);
				
		lom.getClassificationList().add(classification);
	}
	
	
	/**
	 * Carrega de les paraules obertes en els diversos idiomes disponibles a 9 Classification.
	 * @param lom
	 * @param idRec
	 * @param c
	 */
	private void carregarKeywords(Lom lom,String idRec,Connection c) {
		Map m;
		ArrayList lRec;	
		ArrayList lKeyword = new ArrayList();
		try{
			lRec = new ArrayList();
			lRec.add(idRec);
			ArrayList camps = new ArrayList();
			camps.add("v_paraula");
			//getFieldsList("mer_rec_paraules");
			//camps.addAll(getFieldsList("mer_paraules"));
			
			m = WSAccesBD.getObject("mer_rec_paraules rp, mer_paraules p", camps ,"rp.id_paraula=p.id_paraula and rp.id_rec = ?",lRec,c);
			int iSize = Math.min(((ArrayList)m.get("v_paraula")).size(), 10);
			for (int i = 0; i< iSize;i++)		
				lKeyword.add(((String)((ArrayList) m.get("v_paraula")).get(i)));
			
		}catch(Exception e){
			logger.error("Error a l'obtenir les paraules obertes ->"+e);
			e.printStackTrace();
		}
		if (lKeyword.size()>0)
			lom.getGeneral().setKeywordList(lKeyword);
	}

	/**
	 * Calcula el pare del terme donat i el retorna en un Taxon.
	 * @param idTerme
	 * @param connection
	 * @return
	 */
	private Taxon getThesaurusPath(String idTerme, Connection connection) {
		ArrayList camps = new ArrayList();
		ArrayList lParams = new ArrayList();
		String fromwhere;
		Map m;
		Taxon t = null;
		LangString lg;
		String lang = APXTEC32.DEFAULT_LANG;
		String relType = "MT";

		camps.add("t.id_terme as idterme");
		camps.add("t.TERME_CA as terme_ca");
		camps.add("t.TERME_ES as terme_es");
		camps.add("t.TERME_EN as terme_en");
		fromwhere = "the_termes t, the_terme_rel t2 where t.id_terme = t2.id_terme and t2.id_terme_rel = ? and t2.tipus_relacio=?";
		lParams.add(idTerme);
		lParams.add(relType);
		
		try {
			m = WSAccesBD.getQuery(fromwhere, camps, lParams, connection);
			if (((ArrayList)m.get("terme_ca")).size()>0){
				lg = new LangString((String)((ArrayList)m.get("terme_ca")).get(0),lang);
				t = new Taxon(((BigDecimal)((ArrayList)m.get("idterme")).get(0)).toString(), lg);
				lg = new LangString((String)((ArrayList)m.get("terme_es")).get(0),"es");
				t.setEntry(lg);//CONVERTINT TAXON.ENTRY A arrayList PER PODER ENGLOBAR DIVERSOS IDIOMES.
				lg = new LangString((String)((ArrayList)m.get("terme_en")).get(0),"en");
				t.setEntry(lg);//CONVERTINT TAXON.ENTRY A arrayList PER PODER ENGLOBAR DIVERSOS IDIOMES.
			}else{
				relType = "UF";
				lParams.remove(1);
				lParams.add(relType);
				m = WSAccesBD.getQuery(fromwhere, camps, lParams, connection);
				if (m.get("idterme")!=null && ((ArrayList)m.get("idterme")).size()>0){
					t = getThesaurusPath(((BigDecimal)((ArrayList)m.get("idterme")).get(0)).toString(),connection);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return t;
	}

	/**
	 * Retorna el taxon amb el terme amb l'id donat.
	 * @param idTerme
	 * @param connection
	 * @return
	 */
	private Taxon getThesaurusTerm(String idTerme, Connection connection) {
		ArrayList camps = new ArrayList();
		ArrayList lParams = new ArrayList();
		String fromwhere;
		Map m;
		Taxon t = null;
		LangString lg;
		String lang = APXTEC32.DEFAULT_LANG;

		camps.add("t.id_terme as idterme");
		camps.add("t.TERME_CA as terme_ca");
		camps.add("t.TERME_ES as terme_es");
		camps.add("t.TERME_EN as terme_en");
		fromwhere = "the_termes t where t.id_terme = ?";
		lParams.add(idTerme);
		
		try {
			m = WSAccesBD.getQuery(fromwhere, camps, lParams, connection);
			if (((ArrayList)m.get("terme_ca")).size()>0){
				lg = new LangString((String)((ArrayList)m.get("terme_ca")).get(0),lang);
				t = new Taxon(((BigDecimal)((ArrayList)m.get("idterme")).get(0)).toString(), lg);
				lg = new LangString((String)((ArrayList)m.get("terme_es")).get(0),"es");
				t.setEntry(lg);//CONVERTINT TAXON.ENTRY A arrayList PER PODER ENGLOBAR DIVERSOS IDIOMES.
				lg = new LangString((String)((ArrayList)m.get("terme_en")).get(0),"en");
				t.setEntry(lg);//CONVERTINT TAXON.ENTRY A arrayList PER PODER ENGLOBAR DIVERSOS IDIOMES.
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return t;
	}

	/**
	 * Carrega les classificacions que pengen del DUC: Termes DUC, termes ArbolCurricular(ArC) i Competències Bàsiques.
	 * @param lom
	 * @param lRec
	 * @param c
	 */
	private void carregarLlistatDUC(Lom lom, ArrayList lRec, Connection c) {
		ArrayList laux;
		ArrayList al;
		ArrayList taxonCompetenciesList;
		Classification classificationDUC;
		Classification classificationCB;
		Map m;
//		RecursMerli rm;
		try{
//			rm = new RecursMerli(0);
			laux = new ArrayList();
			al = new ArrayList();
			if (lom.getClassificationList() == null)
				lom.setClassificationList(new ArrayList());
			classificationDUC = new Classification();
			Classification classificationArc = new Classification();
		
			taxonCompetenciesList = new ArrayList();			
			
			m = WSAccesBD.getObject("mer_rec_curriculum",getFieldsList("mer_rec_curriculum"),"id_rec = ?",lRec,c);
			for (int i = 0; i< ((ArrayList)m.get("id_node")).size();i++){
				al.clear();
				if (APXTEC32.DUC_CONTENT.equals((String)((ArrayList) m.get("v_type")).get(i)))
					classificationDUC.addTaxonPath(getPathDUCContent((((ArrayList) m.get("id_node")).get(i)).toString(),c,taxonCompetenciesList));
				else if (APXTEC32.DUC_AREA.equals((String)((ArrayList) m.get("v_type")).get(i)))
					classificationDUC.addTaxonPath(getPathDUCArea((((ArrayList) m.get("id_node")).get(i)).toString(),c));
		
				getPathArC((((ArrayList) m.get("id_node")).get(i)).toString(),classificationArc,c);
			}
			

			//DUC
			if (!lomes){
				classificationDUC.setPurpose(new SourceValue(APXTEC32.DUC,"educational objective",APXTEC32.PURPOSE));
				classificationDUC.setDescription(APXTEC32.DUC_DESCRIPTION_CA);				
				classificationDUC.setDescription(APXTEC32.DUC_DESCRIPTION_ES);				
				classificationDUC.setDescription(APXTEC32.DUC_DESCRIPTION_EN);
				lom.getClassificationList().add(classificationDUC);
			}else{
				//Arbol Curricular		
				classificationArc.setPurpose(new SourceValue(APXTEC32.LOMes,"educational objective",APXTEC32.PURPOSE));
				classificationArc.setDescription(new LangString("Relaciones Arbol Curicural LOE 2006.MEC ",APXTEC32.DEFAULT_LANG));			
				lom.getClassificationList().add(classificationArc);
			}
			
			//Competències bàsiques.
			if (taxonCompetenciesList.size()>0){
				classificationCB = new Classification();
				classificationCB.setPurpose(new SourceValue(APXTEC32.COMPETENCIES_DUC,"competency",APXTEC32.PURPOSE));
				classificationCB.setTaxonPath(taxonCompetenciesList);
				classificationCB.setDescription(new LangString("Competències bàsiques",APXTEC32.DEFAULT_LANG));	
				lom.getClassificationList().add(classificationCB);
			}
		}catch(Exception e){
			logger.error("Error a l'obtenir els termes del DUC ->"+e);
			e.printStackTrace();
		}
	}	

	private TaxonPath getPathDUCContent(String idNode, Connection c, ArrayList taxonCompetenciesList) {

		ArrayList lTaxon = new ArrayList();
		LangString lSource = new LangString(APXTEC32.DUC,APXTEC32.DEFAULT_LANG);
		LangString lDesc = new LangString(APXTEC32.DUC,APXTEC32.DEFAULT_LANG);
		TaxonPath tp = null;

		try {
			//Si és competència bàsica l'afageix al llistat, sino el retorna per ser afegir posteriorment.
			if (getListPathDUCContent(idNode,c,lTaxon)){			
				TaxonPath taxonCompetencies = new TaxonPath();
				taxonCompetencies.setTaxonList(lTaxon);
				taxonCompetencies.setSource(new LangString(APXTEC32.DUC_COMPETENCIES,APXTEC32.DEFAULT_LANG));				
				
				taxonCompetenciesList.add(taxonCompetencies);
			}else{
				tp = new TaxonPath(lSource,lTaxon,lDesc);
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tp;
	}
	
	private TaxonPath getPathDUCArea(String idNode, Connection c) 
	{
		LangString lSource = new LangString(APXTEC32.DUC,APXTEC32.DEFAULT_LANG);
		LangString lDesc = new LangString(APXTEC32.DUC,APXTEC32.DEFAULT_LANG);
		ArrayList lTaxon = new ArrayList();
		ArrayList camps = new ArrayList();
		ArrayList lParams = new ArrayList();
		TaxonPath tp;
		String fromwhere;
		Map m;
		Taxon t;
		LangString lg;
		String lang = APXTEC32.DEFAULT_LANG;
		
		camps.add("ca.v_term as area");
		camps.add("ca.id_node as idarea");
		camps.add("cl.v_term as nivell");
		camps.add("ca.id_nodecur_level as idnivell");		
		lParams.add(idNode);
		
		fromwhere = "cur_area ca, cur_level cl where ca.id_node = ? and (ca.id_nodecur_level = cl.ID_NODE) Order by idnivell ASC";
		
		try {
			m = WSAccesBD.getQuery(fromwhere, camps, lParams, c);

			for (int k = 0; k< ((ArrayList)m.get("idnivell")).size();k++){
				//El Nivell
				lg = new LangString((String)((ArrayList)m.get("nivell")).get(k),lang);
				t = new Taxon(((BigDecimal)((ArrayList)m.get("idnivell")).get(k)).toString(), lg);
				lTaxon.add(t);
			}
			//L'area
			lg = new LangString((String)((ArrayList)m.get("area")).get(0),lang);
			t = new Taxon(((BigDecimal)((ArrayList)m.get("idarea")).get(0)).toString(), lg);
			lTaxon.add(t);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		tp = new TaxonPath(lSource,lTaxon,lDesc);
		
		return tp;
		
	}

	private void getPathArC(String idNode, Classification classificationArc, Connection c) {
		ArrayList camps = new ArrayList();
		ArrayList lParams = new ArrayList();
		ArrayList lTaxon;							
		TaxonPath taxonArC;
		
		String fromwhere;
		Map m;
		Map m2;
		Taxon t;
		LangString lg;
		String lang = APXTEC32.DEFAULT_LANG;
		String[] l = new String[10];
		String idPral;
		String idAux = null;
		
		camps.add("id_arc");		
		lParams.add(idNode);
		fromwhere = "cur_to_arc where id_node = ?";		
		
		try {
			m = WSAccesBD.getObjectList("CUR_TO_ARC", camps, "id_node=?", lParams,"id_arc", c);//fromwhere, camps, lParams, c);
			//Parsegem el resultat i posem en un llistat tots els elements del path. a no ser que l'element no existeixi o sigui NC.
			for ( int i=0;i<((ArrayList)m.get("id_arc")).size(); i++){
				idPral = (String) ((ArrayList)m.get("id_arc")).get(i);
				if (!"".equals(idPral) && !"NC".equals(idPral) && !pertanyClassificationTaxonList(idPral,classificationArc)){
					l= idPral.split("\\.");
					camps.clear();
					camps.add("terme");
					lTaxon = new ArrayList();
					//Recuparem cada un dels elements del Path.
					for (int j=0;j<l.length;j++){
						if (null==idAux)
							idAux = l[j];
						else
							idAux += "."+l[j];
						lParams.clear();
						lParams.add(idAux);
						m2 = WSAccesBD.getObjectList("CUR_ARC_TERMES", camps, "id_arc=?", lParams,"terme", c);						
						//El contingut
						if (0 <((ArrayList)m2.get("terme")).size()){
							lg = new LangString((String) ((ArrayList)m2.get("terme")).get(0),lang);
							t = new Taxon(idAux, lg);
							lTaxon.add(t);	
						}
					}
					idAux = null;
					//Creem un taxonPath per cada identificador de ArC relacionat amb un id del DUC.
					taxonArC = new TaxonPath();
					taxonArC.setSource(new LangString(APXTEC32.ARBOL_CURRICULAR,APXTEC32.DEFAULT_LANG));
					taxonArC.setTaxonList(lTaxon);
					taxonArC.setDescription(new LangString("element: "+idPral,APXTEC32.DEFAULT_LANG));
					classificationArc.addTaxonPath(taxonArC);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private boolean pertanyClassificationTaxonList(String idPral, Classification classificationArc) {
		String id = "";
		if (classificationArc.getTaxonPath() != null)
		for (int i=0; i<classificationArc.getTaxonPath().size();i++){
			try{
				id = classificationArc.getTaxonPath(i).getTaxonList(classificationArc.getTaxonPath(i).getTaxonList().size()-1).getId();
			}catch(Exception e){}
			if (idPral.equals(id))
				return true;
		}
		
		
		return false;
	}

	private boolean getListPathDUCContent(String idNode, Connection c, ArrayList lTaxon) {
		ArrayList camps = new ArrayList();
		ArrayList lParams = new ArrayList();
		String fromwhere;
		Map m;
		Taxon t;
		LangString lg;
		String lang = APXTEC32.DEFAULT_LANG;
		boolean isCP=false;
		
		camps.add("cc.v_term as content");
		camps.add("ca.v_term as area");
		camps.add("ca.id_node as idArea");
		camps.add("cl.v_term as nivell");
		camps.add("cl.id_node as idNivell");
		camps.add("cc.id_nodecur_content as idNivellPare");
		
		lParams.add(idNode);
		
		fromwhere = "cur_area ca, cur_content_area cca, cur_content cc, cur_level cl, cur_content_level ccl where cca.id_node = ? and (cca.id_nodecur_area = ca.ID_NODE) and (cca.id_node = cc.ID_NODE) and (ccl.id_nodecur_level = cl.ID_NODE) and (ccl.id_node = cc.ID_NODE) Order by idnivell ASC";
		
		try {
			m = WSAccesBD.getQuery(fromwhere, camps, lParams, c);
			isCP = isCompetenciesBasiques((String)((ArrayList)m.get("area")).get(0));
			
			if (((ArrayList)m.get("idNivellPare")).get(0) != null &&
					!"".equals(((ArrayList)m.get("idNivellPare")).get(0).toString())&&
					!"0".equals(((ArrayList)m.get("idNivellPare")).get(0).toString())){
				getListPathDUCContent(((BigDecimal)((ArrayList)m.get("idNivellPare")).get(0)).toString(),c,lTaxon);
			}else{
				for (int j = 0; j< ((ArrayList)m.get("idArea")).size();j++){
					for (int k = j; k< ((ArrayList)m.get("idNivell")).size();k++){
						//El Nivell
						if( ((BigDecimal)((ArrayList)m.get("idArea")).get(j)).equals((BigDecimal) ((ArrayList)m.get("idArea")).get(k)))
						{
						lg = new LangString((String)((ArrayList)m.get("nivell")).get(k),lang);
						t = new Taxon(((BigDecimal)((ArrayList)m.get("idNivell")).get(k)).toString(), lg);
						lTaxon.add(t);
						j=k;
						}
					}
					//L'àrea. Si és CP no posem l'àrea per no ser redundants.		
					if (!isCP){
						lg = new LangString((String)((ArrayList)m.get("area")).get(j),lang);
						t = new Taxon(((BigDecimal)((ArrayList)m.get("idArea")).get(j)).toString(), lg);
						lTaxon.add(t);
					}
				}
			}
			//El contingut
			lg = new LangString((String) ((ArrayList)m.get("content")).get(0),lang);
			t = new Taxon(idNode, lg);
			lTaxon.add(t);	


		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return isCP;
	}
	
	private boolean isCompetenciesBasiques(String area) {
		String txtCompetencies = "competencies basiques; competències bàsiques;".toUpperCase();
		return txtCompetencies.indexOf(area.toUpperCase().trim()+";")>=0;
	}
	
//	public ElementDUC getElementDUCold(IdElement idElement) throws MerliDBException, SQLException {
		
	/**
	 * set recurs
	 * @deprecated
	 * @param lom
	 * @return
	 * @throws MerliDBException
	 */
	public IdResource setRecursOriginal(Lom lom) throws MerliDBException {
		
		//ConnectionBean cb = connectBD();
		ConnectionBean cb = null;
		ArrayList al;
		ArrayList alcon = new ArrayList();
		ArrayList lRec = new ArrayList();
		ArrayList lIdForm = new ArrayList();
		
		String idRecurs = lom.getGeneral().getIdentifier().getIdEntry();
		lRec.add(new Integer(idRecurs));
		cb = connectBD();		
		Connection c = null;
		boolean esFisic = (lom.getDescripcioFisica()!=null && lom.getDescripcioFisica().getUnitatCreadora()!=null && lom.getDescripcioFisica().getUnitatCreadora().getIdentifier().intValue()!=0);
		carregarIdsLom(lom,cb.getConnection(),lIdForm, esFisic);
		try {
			c = cb.getConnection();
			c.setAutoCommit(true);
			
			//UPDATE recurs a MER_RECURS//
			//obligatori que funcioni correctament.
			WSAccesBD.executeUpdate("mer_recurs","id_rec=?",lRec,lom.getSetQuery("mer_recurs"),lom.getSetQueryFields("mer_recurs",APXTEC32.DEFAULT_LANG),c);
			
			//INSERT nous valors a MER_REC_LLENGUA//
			al = lom.getGeneral().getLanguageList();
			try{
				WSAccesBD.executeDelete("mer_rec_llengua","id_rec = ?",lRec,c);
				if (!al.isEmpty())
					for (int i = 0; i< al.size(); i++){
						alcon.clear();
						alcon.add(idRecurs);
						alcon.add(al.get(i));
						//AccesBD.executeInsert("mer_rec_llengua",idRecurs+",'"+al.get(i)+"'",c);
						WSAccesBD.executeInsert("mer_rec_llengua",alcon,c);
					}
			}catch (Exception e){
				logger.error("error a l'inserir Llengues ->"+e);
				e.printStackTrace();
			}
			
			/*INSERT nous valors a MER_REC_NIVELL_EDUCATIU*/
			al = lom.getEducational().getContextList();
			try{
				delContextList(idRecurs, c);
				if (!al.isEmpty()){
					for (int i = 0; i< al.size(); i++){	
						alcon.clear();
						alcon.add(idRecurs);
						alcon.add(((SourceValue)al.get(i)).getId());									
						//AccesBD.executeInsert("mer_rec_nivell_educatiu",idRecurs+","+al.get(i),c);
						WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);
					}
				}
			}catch (Exception e){
				logger.error("Error a l'inserir nivell educatiu ->"+e);
				e.printStackTrace();
			}

			/*INSERT nous valors a MER_REC_PARAULES_CLAU* /
			//Versió antiga. Cal carregar i desar la nova.
			al = recursMerli.getTaxon();
			AccesBD.executeDelete("mer_rec_paraules_clau","id_rec = ?",lRec,c);			
			if (!al.isEmpty()){
				for (int i = 0; i< al.size(); i++)
					AccesBD.executeInsert("mer_rec_paraules_clau",idRecurs+","+al.get(i)+",'ETB'",c);
			}*/
			
			for (int i=0;i<lom.getClassificationList().size();i++){	
				for (int l=0; l<((Classification)lom.getClassificationList().get(i)).getTaxonPath().size();l++){
					al = ((Classification)lom.getClassificationList().get(i)).getTaxonPath(l).getTaxonList();
					if ("ETB".equals(((Classification)lom.getClassificationList().get(i)).getTaxonPath(l).getSource().getString())){
						try{
							WSAccesBD.executeDelete("mer_rec_termes","id_rec = ?",lRec,c);			
							if (!al.isEmpty()){
								for (int j = 0; j< al.size();j++){
									alcon.clear();
									alcon.add(idRecurs);
									alcon.add(((Taxon)al.get(j)).getId());	
									alcon.add("ETB");
									WSAccesBD.executeInsert("mer_rec_termes",alcon,c);
									//AccesBD.executeInsert("mer_rec_termes",idRecurs+","+al.get(i)+",'ETB'",c);
								}
							}
						}catch(Exception e){
							logger.error("Error a l'inserir termes thesaurus ->"+e);
							e.printStackTrace();
						}
					}
				
					if("DUC".equals(((Classification)lom.getClassificationList().get(i)).getTaxonPath(l).getSource().getString())){
						String aux;
						try{
							WSAccesBD.executeDelete("mer_rec_curriculum","id_rec = ?",lRec,c);			
							if (!al.isEmpty()){
								for (int j = 0; j< al.size(); j++){
									aux  = ((Taxon) al.get(j)).getId();
									alcon = new ArrayList();
									alcon.add(idRecurs);
									if (aux != null && aux.length()>0 && aux.charAt(0) > '9'){
										for (int k = 0; k < aux.length(); k++){
											if (aux.charAt(k) <= '9'){
												alcon.add(aux.substring(0,k));
												alcon.add(aux.substring(k)); //idcur
												break;
											}
										}
									}
									else{
										alcon.add("content");
										alcon.add(aux);
									}
									WSAccesBD.executeInsert("mer_rec_curriculum","id_rec, v_type, id_node",alcon,c);					
								}
							}
						}catch(Exception e){
							logger.error("Error a l'inserir termes DUC ->"+e);
							e.printStackTrace();
						}
					}			
				}
			}


			/*INSERT nous valors a MER_REC_ROL_USUARI*/
			al = lom.getEducational().getIntendedEndUserRoleList();
			try{
				WSAccesBD.executeDelete("mer_rec_rol_usuari","id_rec = ?",lRec,c);			
				if (!al.isEmpty()){
					for (int i = 0; i< al.size(); i++){
						alcon.clear();
						alcon.add(idRecurs);
						alcon.add(((SourceValue)al.get(i)).getId());	
						WSAccesBD.executeInsert("mer_rec_rol_usuari",alcon,c);
					}
				}
			}catch(Exception e){
				logger.error("Error a l'inserir rol d'usuari ->"+e);
				e.printStackTrace();
			}
			
			/*INSERT nous valors a MER_REC_TIPS_RECURS*/
			al = lom.getEducational().getLearningResourceTypeList();
			try{
				WSAccesBD.executeDelete("mer_rec_tipus_recurs","id_rec = ?",lRec,c);			
				if (!al.isEmpty()){
					for (int i = 0; i< al.size(); i++){
						alcon.clear();
						alcon.add(idRecurs);
						alcon.add(((SourceValue)al.get(i)).getId());
						WSAccesBD.executeInsert("mer_rec_tipus_recurs",alcon,c);
					}
				}
			}catch(Exception e){
				logger.error("Error a l'inserir tipus de recurs ->"+e);
				e.printStackTrace();
			}
				
			
			/*INSERT nous valors a MER_REC_FORMAT*/
			try{
				if(!esFisic)
				{
					WSAccesBD.executeDelete("mer_rec_format","id_rec = ?",lRec,c);			
					if (!lIdForm.isEmpty()){
						for (int i = 0; i< lIdForm.size(); i++){
							alcon.clear();
							alcon.add(idRecurs);
							alcon.add(lIdForm.get(i));	
							WSAccesBD.executeInsert("mer_rec_format",alcon,c);
						}
					}
				}
				// no esta implementat per a recursos fisics
			}catch(Exception e){
				logger.error("Error a l'inserir el format ->"+e);
				e.printStackTrace();
			}

//			/*UPDATE taula de MER_REC_INFO*/
//			al = new ArrayList();
//			al.add(lom.getEstat());
//			al.add(lom.getResponsable());
//			AccesBD.executeUpdate("mer_rec_info","id_rec = ?",lRec," id_estat = ?, v_responsable = ?",al,c);
//			//(,idRecurs+",0,'"+recursMerli.getResponsable()+"'",c);
			
			/*INSERT taula de MER_DRETS*/
			al.clear();		
			
			if ("no".equals(lom.getRights().getCost().getValue()))
				al.add(new Integer(0));
			else 
				al.add(new Integer(1));
			//al.add(Utility.toParaulaDB(recursMerli.getRightsDesc()));
			
			if (lom.getRights().getDescription().getString() == null )
				lom.getRights().getDescription().setString("");
			al.add(lom.getRights().getDescription().getString());
			
			if (lom.getRights().getCopyRightAndOtherRestrictions().getValue() == null )
				lom.getRights().getCopyRightAndOtherRestrictions().setValue("");
			al.add(lom.getRights().getCopyRightAndOtherRestrictions().getId());
			try{
				if (0 == WSAccesBD.executeExist("mer_drets","id_rec",((Integer) lRec.get(0)).toString(),"",c)){
					alcon.clear();
					alcon.add(idRecurs);
					alcon.add(lom.getRights().getDescription().getString());	
					alcon.add(new Integer("-1"));
					alcon.add(lom.getRights().getCopyRightAndOtherRestrictions().getId());
					if ("no".equals(lom.getRights().getCost().getValue()))
						alcon.add(new Integer(0));
					else 
						alcon.add(new Integer(1));
					WSAccesBD.executeInsert("mer_drets",alcon,c);
					//AccesBD.executeInsert("mer_drets",idRecurs+",'"+
					//		recursMerli.getRightsDesc()+"',null,"+recursMerli.getLicense()+","+recursMerli.getCost(),c);
				}else{
					WSAccesBD.executeUpdate("mer_drets","id_rec = ?",lRec,"COST=?, DESCRIPCIO = ?, ID_LLICENCIA = ? ",al,c);
				}
			}catch(Exception e){
				logger.error("Error a l'inserir els drets ->"+e);
				e.printStackTrace();
			}
			
			/*INSERT valors a MER_CONTRIBUCIO*/
			//Si no funciona no s'inserta el lom.
			al = lom.getLifeCycle().getContributeList();

			String data = null;
			Date dDate = null;
			SimpleDateFormat sDate = new SimpleDateFormat(APXTEC32.DATE_FORMAT);
			//SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			for (int i = 0; i< al.size(); i++){
				try{
					lRec = new ArrayList();
					alcon =  new ArrayList();
					
					if ((Contribute)lom.getLifeCycle().getContributeList().get(i) != null){									
						try{
							data = ((Contribute)lom.getLifeCycle().getContributeList().get(i)).getDateTime().getDateTime();
							dDate = sDate.parse(data);
						} catch (Exception e){
							dDate = new Date();
						}
					}
					/*if ((data.compareTo("''")==0)||data=="'null'"|| data == null){
						data = sDate.format(new Timestamp(System.currentTimeMillis()));//"trunc(SYSDATE)";
					}*/				
					lRec.add(idRecurs);
					lRec.add(new Integer(((Contribute)lom.getLifeCycle().getContributeList().get(i)).getRole().getId()));
					alcon.add(((Contribute)lom.getLifeCycle().getContributeList().get(i)).getEntity().getUsername());
					alcon.add(dDate);
					alcon.add(((Contribute)lom.getLifeCycle().getContributeList().get(i)).getRole().getValue());
					WSAccesBD.executeUpdate("mer_contribucio","id_rec = ? AND id_rol_cont = ?",lRec," v_entitat = ?, d_data = ?, v_descripcio = ? ",alcon,c);
				}catch (Exception e){
					logger.error("Error al modificar una contribució ->"+e);
					e.printStackTrace();
				}
			}
			
			c.commit();
		}catch (Exception e) {
			e.printStackTrace();
			try {
				c.rollback();
				logger.debug("dades array="+alcon);
				logger.error("ws Error on setRecurs ->"+e.getMessage());
				throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}finally{
			tancaConexio(c,cb);
		}
		
		IdResource idResource = new IdResource();
		idResource.setIdentifier(idRecurs);
		idResource.setType("MERLI");
		
		return idResource;
	}

	public ElementDUC getElementDUC(IdElement idElement) throws MerliDBException, SQLException, SemanticException {
		
		
		ElementDUC elementDUC = new ElementDUC();	
		ElementDUC elDUC;
		DUCRelation ducRelation;
		ArrayList lRec = new ArrayList();	
		ArrayList laux = new ArrayList();
		lRec.add(new Integer(idElement.getIdentifier()));
//		Connection c = null;
//		ConnectionBean cb = null;
//		Map m = null;
		int id;
		Node node;
		SemanticInterface si = new SemanticInterface();
		
		try{
//			cb = connectBD();
//			c=cb.getConnection();
			id = Integer.parseInt(idElement.getIdentifier());
//			m= AccesBD.getObject("cur_content",elementDUC.getFieldsList("cur_content"),"id_node=?",lRec,c);
	
			node = si.getNode(id,"content");
			
			elementDUC.copyFromSemanticNet(node);
				
//			id_nodecur_content = String.valueOf(((ArrayList)m.get("id_nodecur_content")).get(0));
			Relation rt;
			lRec = si.getRelations(id,"content","RCC",RelationType.SOURCE);
			for (int i=0;i<lRec.size();i++){
				rt = (Relation) lRec.get(i);
				elDUC = new ElementDUC();
				elDUC.copyFromSemanticNet(si.getNode(rt.getIdDest(),rt.getDestType()));
				
				ducRelation = new DUCRelation();
				ducRelation.setDUCRelationType("RCC");
				ducRelation.setElementDUC(elDUC);
				laux.add(ducRelation);
			}
			
			lRec = si.getRelations(id,"content","RCL",RelationType.SOURCE);
			for (int i=0;i<lRec.size();i++){
				rt = (Relation) lRec.get(i);
				elDUC = new ElementDUC();
				elDUC.copyFromSemanticNet(si.getNode(rt.getIdDest(),rt.getDestType()));
				
				ducRelation = new DUCRelation();
				ducRelation.setDUCRelationType("RCL");
				ducRelation.setElementDUC(elDUC);
				laux.add(ducRelation);
			}
			
			lRec = si.getRelations(id,"content","RCA",RelationType.SOURCE);
			for (int i=0;i<lRec.size();i++){
				rt = (Relation) lRec.get(i);
				elDUC = new ElementDUC();
				elDUC.copyFromSemanticNet(si.getNode(rt.getIdDest(),rt.getDestType()));
				
				ducRelation = new DUCRelation();
				ducRelation.setDUCRelationType("RCA");
				ducRelation.setElementDUC(elDUC);
				
				laux.add(ducRelation);
			}
			
			elementDUC.setDUCRelationList(laux);		
			try{
				lRec = si.getRelations(id,"content","RET",RelationType.SOURCE);
				TaxonPath taxonPath = new TaxonPath();
				taxonPath.setSource(new LangString("ETB",APXTEC32.DEFAULT_LANG));
				taxonPath.setDescription(new LangString("Thesaurus de ELR",APXTEC32.DEFAULT_LANG));
				Taxon taxon;
				for (int i=0;i<lRec.size();i++){
					rt = (Relation) lRec.get(i);
					elDUC = new ElementDUC();
					elDUC.copyFromSemanticNet(si.getNode(rt.getIdDest(),rt.getDestType()));
					taxon = new Taxon();
					taxon.setId(String.valueOf(rt.getIdDest()));
					taxon.setEntry(new LangString(si.getNode(rt.getIdDest(),rt.getDestType()).getTerm(),APXTEC32.DEFAULT_LANG));
					taxonPath.getTaxonList().add(taxon);				
				}			
				
				elementDUC.setTaxonList(taxonPath);
			}catch(Exception e){
				logger.error("Error al carregar termes del thesaurus ->"+e);
				e.printStackTrace();
			}
	
		}catch (Exception e) {
			logger.error("ws Error on getRecurs ->"+e.getMessage());
			throw new SemanticException("Error carregant ElementDUC.");
		}
		
		return elementDUC;
	}
		
	/**
	 * Retorna el DUC complert. Es pot demanar excloure els continguts posant contents=false.
	 * @param contents es volen continguts o no
	 * @return
	 * @throws MerliDBException
	 * @throws SQLException
	 * @throws SOAPException 
	 */
	public ListDUC getAllElementDUC(boolean contents) throws MerliDBException, SQLException, SOAPException {

		ElementDUC elementDUC = new ElementDUC();	
		ListDUC listDUC = new ListDUC();

		ArrayList lRec = new ArrayList();	
		ArrayList lres = new ArrayList();
		
		try{

			SemanticInterface si = new SemanticInterface();
		
		
			lres = (ArrayList) si.snet.getNodesRelated(0,"level","RLL",RelationType.DEST);
			//m= AccesBD.getObject("cur_level",elementDUC.getFieldsList("cur_level"),CAMP_PARE+"=?",lRec,c);
			
			for (int i=0;i<lres.size();i++){
				int sId = ((Node)lres.get(i)).getIdNode();
				if (sId > 0){
					elementDUC= getFullElementDUC(si,sId, APXTEC32.TIPUS_LEVEL,contents);
					lRec.add(elementDUC);
//					listDUC.addElement(elementDUC);
				}
			}
			
		}catch (Exception e) {
			logger.error("ws Error on getRecurs ->"+e.getMessage());
			throw new MerliDBException(MerliDBException.ERROR_SQL);
		}
		listDUC = new ListDUC(lRec);
		return listDUC;
	}


	public ListDUC getLevelElementsDUC(IdElement idElement) throws SOAPException, MerliDBException {

		ElementDUC elementDUC = new ElementDUC();	
		ListDUC listDUC = new ListDUC();

		ArrayList lRec = new ArrayList();	
		ArrayList lres = new ArrayList();
		int idLevel;
		try{

			SemanticInterface si = new SemanticInterface();
			idLevel = (new Integer(idElement.getIdentifier())).intValue();
			lres = (ArrayList) si.snet.getNodesRelated(idLevel,"level","RLL",RelationType.DEST);
			//m= AccesBD.getObject("cur_level",elementDUC.getFieldsList("cur_level"),CAMP_PARE+"=?",lRec,c);
			logger.debug("lRes Size 1: " + lres.size());
			logger.debug(">> RLL START <<");
			/*for (String value : lres) {			    
			    logger.debug(value);
			}*/
			logger.debug(lres.toString());
			logger.debug(">> RLL END <<");
			/* NADIM - modificado para ejecutar en INT con menos datos de recursos. */
			int lResSize =lres.size();
			/*if(lres.size()>2){
				lResSize = 2;
			}else{
				lResSize = lres.size();
			}*/
			/* END NADIM */
			logger.debug("lRes Size 1-2: " + lResSize);
			for (int i=0;i<lResSize;i++){
				int sId = ((Node)lres.get(i)).getIdNode();
				if (sId > 0){
					elementDUC= getFullElementDUC(si,sId, APXTEC32.TIPUS_LEVEL,true);
					logger.debug("Element DUC (RLL): " + elementDUC.toXml().toString());
					lRec.add(elementDUC);
//					listDUC.addElement(elementDUC);
				}
			}
			lres = ((ArrayList) si.snet.getNodesRelated(idLevel,"level","RAL",RelationType.DEST));
			logger.debug("lRes Size 2: " + lres.size());
			logger.debug(">> RAL START <<");
			/*for (String value : lres) {			    
			    logger.debug(value);
			}*/
			logger.debug(lres.toString());
			logger.debug(">> RAL END <<");
			/* NADIM - modificado para ejecutar en INT con menos datos de recursos. */
			lResSize = lres.size();
			/*if(lres.size()>2){
				lResSize = 2;
			}else{
				lResSize = lres.size();
			}*/
			/* END NADIM */
			logger.debug("lRes Size 2-2: " + lResSize);
			for (int i=0;i<lResSize;i++){
				int sId = ((Node)lres.get(i)).getIdNode();
				if (sId > 0){
					elementDUC= getFullElementDUC(si,sId, APXTEC32.TIPUS_AREA,true);
					logger.debug("Element DUC (RAL): " + elementDUC.toXml().toString());
					lRec.add(elementDUC);
//					listDUC.addElement(elementDUC);
				}
			}
			
			lres = ((ArrayList) si.snet.getNodesRelated(idLevel,"level","RCL",RelationType.DEST));
			logger.debug("lRes Size 3: " + lres.size());
			logger.debug(">> RCL START <<");
			/*for (String value : lres) {			    
			    logger.debug(value);
			}*/
			logger.debug(lres.toString());
			logger.debug(">> RCL END <<");
			/* NADIM - modificado para ejecutar en INT con menos datos de recursos. */
			lResSize = lres.size();
			/*if(lres.size()>2){
				lResSize = 2;
			}else{
				lResSize = lres.size();
			}*/
			/* END NADIM */
			logger.debug("lRes Size 3-2: " + lResSize);
			for (int i=0;i<lResSize;i++){
				int sId = ((Node)lres.get(i)).getIdNode();
				if (sId > 0){
					elementDUC= getFullElementDUC(si,sId, APXTEC32.TIPUS_CONTENT,true);
					logger.debug("Element DUC (RCL): " + elementDUC.toXml().toString());
					lRec.add(elementDUC);
//					listDUC.addElement(elementDUC);
				}
			}
			
			logger.debug("lRec Size 4: " + lResSize);		
		}catch (Exception e) {
			logger.error("ws Error on getRecurs ->"+e.getMessage());
			throw new MerliDBException(MerliDBException.ERROR_SQL);
		}
		
		logger.debug("lRec Size : " + lRec.size());
		logger.debug("lRec : " + lRec);
		
		
		listDUC = new ListDUC(lRec);
		return listDUC;
	}
	
	
	/**
	 * 
	 * @param idElement id
	 * @param tipus tipus
	 * @param contents es volen continguts o no.
	 * @return
	 * @throws MerliDBException
	 * @throws SQLException
	 */
	public ElementDUC getFullElementDUC(SemanticInterface si, int idElement, int tipus, boolean contents) throws MerliDBException, SQLException {
		
		ElementDUC elementDUC = new ElementDUC();	
		ElementDUC elDUC;
		DUCRelation ducRelation;
		ArrayList lRec = new ArrayList();	
		ArrayList laux = new ArrayList();	
		Hashtable lnode = new Hashtable();//ArrayList();	
		
		lRec.add(new Integer(idElement));
		String aux;
		Node node;
		int intTipus;
		int direccio;
		int id;
		int lastPos;
		String tipusDesti;
		Relation rt;
		try{
			
			if (si == null)
				si = new SemanticInterface();
			
			//Recuperar dades del node donat.
			switch(tipus){
			case APXTEC32.TIPUS_LEVEL:
			
//				m= AccesBD.getObject("cur_level",elementDUC.getFieldsList("cur_level"),"id_node=?",lRec,c);
//				elementDUC.setCategory((String)((ArrayList)m.get("v_category")).get(0));
//				id_nodecur_level = String.valueOf(((ArrayList)m.get("id_nodecur_level")).get(0));
				aux = "level";
				break;
			case APXTEC32.TIPUS_AREA:
//				m= AccesBD.getObject("cur_area",elementDUC.getFieldsList("cur_area"),"id_node=?",lRec,c);
//				id_nodecur_level = String.valueOf(((ArrayList)m.get("id_nodecur_level")).get(0));
				aux = "area";
				break;
			case APXTEC32.TIPUS_CONTENT:
//				m= AccesBD.getObject("cur_content",elementDUC.getFieldsList("cur_content"),"id_node=?",lRec,c);
//				elementDUC.setCategory((String)((ArrayList)m.get("v_category")).get(0));
//				id_nodecur_content = String.valueOf(((ArrayList)m.get("id_nodecur_content")).get(0));
				aux = "content";
				break;
			default:
				aux = "error";
			}
			///String sId = String.valueOf(((ArrayList)m.get("id_node")).get(0));
			
			node = si.getNode(idElement,aux);			
			elementDUC.copyFromSemanticNet(node);
			IdElement oId = new IdElement(String.valueOf(idElement), aux);
				
			//Recupera termes del thesaurus relacionats.
//			try{
//				lRec = si.getRelations(idElement,aux,"RET",RelationType.SOURCE);
//				if (lRec != null && lRec.size()>0){
//					TaxonPath taxonPath = new TaxonPath();
//					taxonPath.setSource(new LangString("ETB",APXTEC32.DEFAULT_LANG));
//					taxonPath.setDescription(new LangString("Thesaurus de ELR",
//							APXTEC32.DEFAULT_LANG));
//					Taxon taxon;
//					for (int i=0;i<lRec.size();i++){
//						rt = (Relation) lRec.get(i);
//						elDUC = new ElementDUC();
//						elDUC.copyFromSemanticNet(si.getNode(rt.getIdDest(),rt.getDestType()));
//						taxon = new Taxon();
//						taxon.setId(String.valueOf(rt.getIdDest()));
//						taxon.setEntry(new LangString(si.getNode(rt.getIdDest(),rt.getDestType()).getTerm(),APXTEC32.DEFAULT_LANG));
//						taxonPath.getTaxonList().add(taxon);				
//					}			
//					
//					elementDUC.setTaxonList(taxonPath);
//				}
//			}catch(Exception e){
//				logger.error("Error al carregar termes del thesaurus ->"+e);
//				e.printStackTrace();
//			}
			
			
			
			//Llistat de relacions.
			laux = new ArrayList();
			if (tipus == APXTEC32.TIPUS_AREA)
				//relacio area/content	
				if (contents){
					laux.add("RCA");					
				}
			
			if (tipus == APXTEC32.TIPUS_CONTENT && contents){
				//relacio content/content	
				laux.add("RCC");
			}
			

			if (tipus == APXTEC32.TIPUS_LEVEL){
				//relacio level/level	
				laux.add("RLL");
				
				//relacio level/area	
				laux.add("RAL");


				//relacio level/content	
				if (contents){
					laux.add("RCL");
				}	
			}	
			lastPos = 0;
			
			for(int j = 0;j<laux.size();j++){
				
				direccio = RelationType.SOURCE;
//				if (laux.get(j) == "RLL" || laux.get(j) == "RCC" )
					direccio = RelationType.DEST;
				lRec = si.getRelations(idElement,aux,(String) laux.get(j),direccio);
				for (int i=0;i<lRec.size();i++){
					rt = (Relation) lRec.get(i);

					id = rt.getIdDest();
					tipusDesti = rt.getDestType();
//					if (laux.get(j) == "RLL" || laux.get(j) == "RCC"){
						id = rt.getIdSource();
						tipusDesti = rt.getSourceType();
//					}
					
					if (tipusDesti == "area" ) intTipus = APXTEC32.TIPUS_AREA;
					else if (tipusDesti == "content") intTipus = APXTEC32.TIPUS_CONTENT;
					else intTipus = APXTEC32.TIPUS_LEVEL;
					
					//elDUC = new ElementDUC();
					//elDUC.copyFromSemanticNet(si.getNode(rt.getIdDest(),rt.getDestType()));

					elDUC =getFullElementDUC(si,id,intTipus,contents);
					
					ducRelation = new DUCRelation();
					ducRelation.setDUCRelationType((String) laux.get(j));
					ducRelation.setElementDUC(elDUC);
					
					lnode.put(new Integer(lastPos++),ducRelation);
				}				
				//lnode.put(new Integer(j),ht);
			}							
			elementDUC.setDUCRelationList(lnode);
			
		}catch (Exception e) {
			logger.error("ws Error on getRecurs ->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_SQL);
		}
		
		return elementDUC;
	}

	public boolean isLomes() {
		return lomes;
	}

	public void setLomes(boolean lomes) {
		this.lomes = lomes;
	}


	public String lomSource(){
		if (lomes) return APXTEC32.LOMes;
		else return APXTEC32.LOM_VALUE;
	}
	public String lre3Source(){
		if (lomes) return APXTEC32.LOMes;
		else return APXTEC32.LRE3_VALUE;
	}
	
	private String getAgregaId(String idRecurs, Connection c){
		ArrayList al = new ArrayList();
		String idAgrega;
		
		al.add(idRecurs);
		
		try {
			idAgrega = WSAccesBD.executeQuery("select id_agrega from mer_rec_agrega where id_rec=?",al,"id_agrega",c);
		} catch (MerliDBException e) {
			idAgrega = null;
		}
		
		if (idAgrega == null || idAgrega.length()<5){
			idAgrega = generateAgregaId(idRecurs);
			saveAgregaId(idAgrega, idRecurs, c);
		}
		return idAgrega;
	}
	
	private void saveAgregaId(String idAgrega, String idRecurs, Connection c) {
		ArrayList alcon = new ArrayList();
		alcon.add(new Integer(idRecurs));
		alcon.add(idAgrega);
		alcon.add(new Integer(0));
		alcon.add(new Date(System.currentTimeMillis()));
		
		try {
			WSAccesBD.executeInsert("mer_rec_agrega",alcon,c);
		} catch (MerliDBException e) {
			logger.error("Error al salvar l'idAgrega ("+idAgrega+") del recurs: "+idRecurs+"->"+e.getMessage());
		}
	}

	private String generateAgregaId(String idRecurs) {
		String autCode = "es-ca";
		String lang = "2";
		String agrLevel ="2";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date d = new Date(System.currentTimeMillis());
		String date = sdf.format(d);
		return autCode+"_"+date+lang+agrLevel+"_XTEC_"+idRecurs;
	}
	
	public ArrayList getFieldsList(String tab) {
		return getFieldsList(tab,"");
	}
	/**
	 * retorna els noms dels camps de la taula tab.
	 * Si label no es buit ni null, afegeix davant de tots els camps el label, en el format:
	 * label.nom_camp
	 * @param tab
	 * @param label
	 * @return ArrayList que conte els noms dels camps de la taula tab
	 */
	public ArrayList getFieldsList(String tab, String label) {
		String table = tab.toLowerCase();
		ArrayList lcamps = new ArrayList();
		if(label==null) label="";
		if(!label.equals(""))label=label+".";
		
		if (table.compareTo("mer_recurs")==0){
			lcamps.add(label+"id_rec");
			lcamps.add(label+"titol");
			lcamps.add(label+"descripcio");
			lcamps.add(label+"url");
			lcamps.add(label+"versio");
			lcamps.add(label+"edat_min");
			lcamps.add(label+"edat_max");
			lcamps.add(label+"id_dificultat");
			lcamps.add(label+"duracio");
			lcamps.add(label+"drets");
			lcamps.add(label+"id_rol_usuari");
			lcamps.add(label+"confirmat");
			lcamps.add(label+"id_ambit");
			lcamps.add(label+"context");
		}
		if ("mer_rec_rol_usuari".compareTo(table)==0)
			lcamps.add(label+"id_rol_usuari");
		
		if (table.compareTo("mer_rec_nivell_educatiu")==0)
			lcamps.add(label+"id_nivell");

		if (table.compareTo("mer_rec_format")==0)
			lcamps.add(label+"id_format");

		if (table.compareTo("mer_rec_format_fisic")==0)
			lcamps.add(label+"id_format_fisic");

		if (table.compareTo("mer_rec_ambits")==0)
			lcamps.add(label+"id_ambit");

		if (table.compareTo("mer_contribucio")==0){
			lcamps.add(label+"id_rol_cont");
			lcamps.add(label+"v_entitat");
			lcamps.add(label+"v_descripcio");
			lcamps.add(label+"d_data");
		}
		if (table.compareTo("mer_rec_termes")==0){
			lcamps.add(label+"id_terme");
		}
		if (table.compareTo("mer_rec_curriculum")==0){
			lcamps.add(label+"id_node");
			lcamps.add(label+"v_type");
		}
		if (table.compareTo("mer_rec_llengua")==0){
			lcamps.add(label+"id_llengua");
		}
		if (table.compareTo("mer_rec_tipus_recurs")==0){
			lcamps.add(label+"id_tipus_recurs");
		}
		if (table.compareTo("mer_rec_info")==0){
			lcamps.add(label+"id_estat");
			lcamps.add(label+"v_responsable");
		}
		if (table.compareTo("mer_rec_lang")==0){
			lcamps.add(label+"titol");
			lcamps.add(label+"descripcio");
			lcamps.add(label+"drets");
			lcamps.add(label+"estat");
			lcamps.add(label+"lang");
			lcamps.add(label+"id_rec");
		}
		if (table.compareTo("mer_drets")==0){
			lcamps.add(label+"id_llicencia");
			lcamps.add(label+"descripcio");
			lcamps.add(label+"cost");
		}
		if (table.compareTo("mer_rec_agrega")==0){
			lcamps.add(label+"id_rec");
			lcamps.add(label+"id_agrega");
			lcamps.add(label+"send");
			lcamps.add(label+"date_agrega");
		}
		if (table.compareTo("the_termes")==0){
			lcamps.add(label+"terme_ca");
		}
		if (table.compareTo("mer_relacio_recursos")==0){
			lcamps.add(label+"recurs1");
			lcamps.add(label+"recurs2");
			lcamps.add(label+"tipus");
			lcamps.add(label+"descripcio");
		}
		if (table.compareTo("mer_recurs_fisic")==0){
			lcamps.add(label+"id_recurs");
			lcamps.add(label+"v_caracteristiques");
			lcamps.add(label+"id_unitat_creadora");
		}
		if (table.compareTo("mer_idfisic")==0){
			lcamps.add(label+"id_rec");
			lcamps.add(label+"v_tipus");
			lcamps.add(label+"v_valor");
		}
		if (table.compareTo("mer_unitats")==0){
			lcamps.add(label+"id_unitat");
			lcamps.add(label+"v_nom");
			lcamps.add(label+"v_url");
		}
		if (table.compareTo("mer_rec_disp_uni")==0){
			lcamps.add(label+"id_rec");
			lcamps.add(label+"id_unitat");
		}
		return lcamps;
	}

	public String getIdParaulaByParaula(String paraula) throws MerliDBException {
		ConnectionBean cb = connectBD();
		Connection c = null;
		String id="";
		ArrayList alcon = new ArrayList();
		ArrayList camps = new ArrayList();
		ArrayList res;
		alcon.add(paraula);
		camps.add("id_paraula");
		
		try {
			c=cb.getConnection();
			Map m = WSAccesBD.getObject("mer_paraules", camps, "v_paraula = ?", alcon, c);
			if(m!=null && m.get("id_paraula") !=null)
			{
				res=(ArrayList)m.get("id_paraula"); 
				if(res.size()>0) id=((BigDecimal)((ArrayList)m.get("id_paraula")).get(0)).toString();
			}
		} catch (MerliDBException e) {
			logger.error("Error al obtenir les paraules");
		}
		finally{
			tancaConexio(c,cb);
		}
		return id;
	}

	public boolean comprovaIp(String ip) throws MerliDBException {
		ConnectionBean cb = connectBD();
		Connection c = null;
		boolean b = false;
		
		try {
			c=cb.getConnection();
			b = WSAccesBD.executeExist("mer_adreces_permeses", "v_adreca",ip,"", c)!=0;
		} catch (MerliDBException e) {
			logger.error("Error al comprovar la ip");
		}
		finally{
			tancaConexio(c,cb);
		}
		return b;
	}
	
	public boolean existResource(String idResource) throws MerliDBException
	{
		ConnectionBean cb = connectBD();
		Connection c = null;
		boolean b = false;
		
		try {
			c=cb.getConnection();
			b = WSAccesBD.executeExist("mer_recurs", "id_rec", idResource, "", c)!=0;
		} catch (MerliDBException e) {
			logger.error("Error al comprovar la ip");
		}
		finally{
			tancaConexio(c,cb);
		}
		return b;
	}
	
	public IdResource getIdResourceByUrl(IdResource idResource) throws MerliDBException {
		IdResource idResourceRes;
		ConnectionBean cb = null;
		Connection c = null;
		Map rs;
		
		if(idResource.getIdentifier()==null || idResource.getIdentifier().length()<1) throw new MerliDBException(MerliDBException.CAMPS_OBLIGATORIS);
		
		try {
			cb = connectBD();
			c=cb.getConnection();
			rs = WSAccesBD.getObject("mer_recurs", Utility.toList("id_rec"), "url=?",Utility.toList(idResource.getIdentifier()), c);
		}
		catch (MerliDBException e) { 
			logger.warn("ws Error on getRecurs amb url "+idResource.getIdentifier()+" -> "+e.getMessage());
			throw new MerliDBException(MerliDBException.ERROR_SQL);
		}finally{
			tancaConexio(c,cb);
		}
	
		if(((ArrayList)rs.get("id_rec")).size()>0)			//si hi es retornem el primer
		{
			String idRecurs = Integer.toString(((BigDecimal)((ArrayList)rs.get("id_rec")).get(0)).intValue());
			idResourceRes = new IdResource(idRecurs, "MERLI");
		}
		else throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
		
		return idResourceRes;
	}
}
