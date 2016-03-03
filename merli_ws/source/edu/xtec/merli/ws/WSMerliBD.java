package edu.xtec.merli.ws;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

import org.apache.log4j.Logger;

import edu.xtec.merli.MerliContribution;
import edu.xtec.merli.RecursMerli;
import edu.xtec.merli.ws.WSAccesBD;
import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.utils.Utility;
import edu.xtec.merli.ws.objects.Classification;
import edu.xtec.merli.ws.objects.Contribute;
import edu.xtec.merli.ws.objects.DUCRelation;
import edu.xtec.merli.ws.objects.Duration;
import edu.xtec.merli.ws.objects.ElementDUC;
import edu.xtec.merli.ws.objects.Entity;
import edu.xtec.merli.ws.objects.IdElement;
import edu.xtec.merli.ws.objects.IdResource;
import edu.xtec.merli.ws.objects.Identifier;
import edu.xtec.merli.ws.objects.LangString;
import edu.xtec.merli.ws.objects.ListDUC;
import edu.xtec.merli.ws.objects.Lom;
import edu.xtec.merli.ws.objects.SourceValue;
import edu.xtec.merli.ws.objects.Taxon;
import edu.xtec.merli.ws.objects.TaxonPath;
import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.semanticnet.SemanticException;
import edu.xtec.semanticnet.SemanticNet;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;


public class WSMerliBD {
	protected static ConnectionBeanProvider broker;
	
	public static final int IDENTIFIERS = -1;
	public static final int LABELS = -2;
	public static final int MAXIM = -3;
	public static final int MINIM = -4;	

	public static final int ESCOLAR = 1;
	public static final int ESPECIAL = 2;
	public static final int ADMINISTRATIU = 3;
	
	private static final Logger logger = Logger.getRootLogger();

	private static final String THESAURUS = "ETB";
	private static final String DUC = "DUC";
	private static final String AMBIT = "AMBIT";
	private static final String LOM_VALUE = "LOMv1.0";
	private static final String LANG = "ca";
	private static final String DUC_CONTENT = "content";
	private static final String DEPT_EDUCACIO = "Departament d'educació";
	private static final String CELEBRATE = "CELEBRATE";
	private static final String MERLI = "MERLI";
	private static final String LOM_CREATOR = "creator";

	private static final String DENEGAT = "unavailable";
	private static final String ESBORRANY = "draft";
	private static final String PER_REVISAR = "draft";
	private static final String EN_REVISIO = "draft";
	private static final String REVISANT = "revised";
	private static final String ACCEPTAT = "final";
/*
 * draft
final
revised
unavailable
 */
	private static final String ELN = "ELNv1.1";

private static final String CAMP_PARE = "id_nodecur_level";

private static final Object ID_CAMP_PARE = "0";

private static final int TIPUS_LEVEL = 0;
private static final int TIPUS_AREA = 1;
private static final int TIPUS_CONTENT =2;

private static final int ESTAT_DENEGAT = -2;
private static final int ESTAT_ESBORRAT = -2;


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
		if ("Denegat".equals(status))
			return -1;
		if ("Esborrany".equals(status))
			return 0;
		if ("Per revisar".equals(status))
			return 1;
		if ("En revisio".equals(status))
			return 2;
		if ("Revisat".equals(status))
			return 3;
		if ("Acceptat".equals(status))
			return 4;
		
		if ("unavailable".equals(status))
			return -1;
		if ("draft".equals(status))
			return 1;
		if ("revised".equals(status))
			return 3;
		if ("final".equals(status))
			return 4;
		return 0;
	}

	private void carregarIdsLom(Lom lom, Connection c, ArrayList lIdForm) throws MerliDBException {
		ArrayList al = new ArrayList();
		//		Preparar valors amb id de la BBDD
		al .clear();
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
		for (int i=0;i<lom.getTechnical().getFormat().size();i++){
			al.clear();
			al.add((String)lom.getTechnical().getFormat().get(i));
			lIdForm.add(WSAccesBD.executeQuery("select id_format from mer_formats where mime = ?",al,"id_format",c));
		}
		for (int i=0;i<lom.getLifeCycle().getContributeList().size();i++){
			al.clear();
			al.add(((Contribute)lom.getLifeCycle().getContributeList().get(i)).getRole().getValue());
			((Contribute)lom.getLifeCycle().getContributeList().get(i)).getRole().setId(WSAccesBD.executeQuery("select id_rol from mer_rol_contribucio r where r.ROL= ?",al,"id_rol",c));
		}
		for (int i=0;i<lom.getMetaMetaData().getContributeList().size();i++){
			al.clear();
			al.add(((Contribute)lom.getMetaMetaData().getContributeList().get(i)).getRole().getValue());
			((Contribute)lom.getMetaMetaData().getContributeList().get(i)).getRole().setId(WSAccesBD.executeQuery("select id_rol from mer_rol_contribucio r where r.ROL= ?",al,"id_rol",c));
		}
		al.clear();
		if (((SourceValue)lom.getRights().getCopyRightAndOtherRestrictions()).getValue() == null)
			((SourceValue)lom.getRights().getCopyRightAndOtherRestrictions()).setValue("");
		al.add(((SourceValue)lom.getRights().getCopyRightAndOtherRestrictions()).getValue());
		((SourceValue)lom.getRights().getCopyRightAndOtherRestrictions()).setId(WSAccesBD.executeQuery("select id_llicencia from mer_llicencia where llicencia = ?",al,"id_llicencia",c));
		
		
		for (int i=0; i < lom.getClassificationList().size();i++){
			if ("AMBIT".equals(((Classification)lom.getClassificationList().get(i)).getTaxonPath().getSource().getString())){
				//idAmbit = new Integer(((Taxon)((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList().get(0)).getId());
				for (int j=0; j <((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList().size();j++){
					al.clear();
					al.add(((Taxon)((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList().get(j)).getEntry().getString());
					((Taxon)((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList().get(j)).setId(WSAccesBD.executeQuery("select id_ambit from mer_ambits r where r.AMBIT= ?",al,"id_ambit",c));			
				}				
			}
		}
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
			ArrayList al = new ArrayList();
			ArrayList alcon = new ArrayList();
			ArrayList lIdForm = new ArrayList();
			int idLom = 0;
			String aux;
			
			try{
				//DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
				c = cb.getConnection();

				c.setAutoCommit(false);
				//Si no retorna un id acaba l'operacio.
				idLom = WSAccesBD.getNext("seq_merli", c);
				
				try{
					carregarIdsLom(lom,c,lIdForm);
				}catch(Exception e){
					logger.error("Error al carregar id's ->"+e);
					e.printStackTrace();
				}
				lom.getGeneral().getIdentifier().setIdLom(idLom);	
				
				/*INSERT recurs a MER_RECURS*/
				//Si no funciona l'insert del recurs no segueix amb la insercio.
				WSAccesBD.executeInsert("mer_recurs",lom.getAddQuery(),c);
				WSAccesBD.executeInsert("mer_rec_lang",lom.getAddLang(),c);
				
				/*INSERT valors a MER_REC_LLENGUA*/
				try{
					al.clear();
					al = lom.getGeneral().getLanguageList();
					if (!al.isEmpty())
						for (int i = 0; i< al.size(); i++){
							alcon.clear();
							alcon.add(new Integer (idLom));
							alcon.add(al.get(i));
							WSAccesBD.executeInsert("mer_rec_llengua",alcon,c);//idRecurs+",'"+al.get(i)+"'"
						}
				}catch(Exception e){
					logger.error("Error a l'inserir llengües ->"+e);
					e.printStackTrace();
				}
				/*INSERT valors a MER_REC_NIVELL_EDUCATIU*/
				try{
					al.clear();
					al = lom.getEducational().getContextList();
					if (!al.isEmpty())
						for (int i = 0; i< al.size(); i++){
							try{
								//En cas de Compulsory-education parsegem l'edat per saber si correspon
								//a Ed. Primaria o a Ed. Secundaria, a ambdos o a cap.
								if ("2".equals(((SourceValue)al.get(i)).getId())){
									aux = lom.getEducational().getTypicalAgeRangeMin();
									if ( aux != null && !"".equals(aux) && 12>=Integer.parseInt(aux)){
										alcon.clear();
										alcon.add(new Integer(idLom));
										alcon.add("3");
										WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
									}
									aux = lom.getEducational().getTypicalAgeRangeMax();
									if (aux != null && !"".equals(aux) && 12<=Integer.parseInt(aux)){
										alcon.clear();
										alcon.add(new Integer(idLom));
										alcon.add("4");
										WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
									}
								}else{
									alcon.clear();
									alcon.add(((SourceValue)al.get(i)).getId());
									aux = WSAccesBD.executeQuery("select id_nivell_cat from mer_nivell_edu_cat where id_nivell = ?",alcon,"id_nivell_cat",c);
									alcon.clear();
									alcon.add(new Integer(idLom));
									alcon.add(aux);
									WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
								}
							}catch(Exception e){
								logger.error("Error al afegir contextos ->"+e);
								e.printStackTrace();
							}
						}
				}catch(Exception e){
					logger.error("Error a l'inserir nivells educatius ->"+e);
					e.printStackTrace();
				}
				/*INSERT valors del Thesaurus ETB
				 * i del DUC.
				 */
				
				for (int i=0;i<lom.getClassificationList().size();i++){
					try{
						if (THESAURUS.equals(((Classification)lom.getClassificationList().get(i)).getPurpose().getSource())){
							alcon = ((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList();
							for (int j=0;j<alcon.size();j++){
								al.clear();
								al.add(new Integer(idLom));
								al.add(((Taxon)alcon.get(j)).getId());
								al.add("ETB");
								WSAccesBD.executeInsert("mer_rec_termes",al,c);
							}
						}
					}catch(Exception e){
						logger.error("Error a l'inserir termes thesaurus ->"+e);
						e.printStackTrace();
					}
					try{
						if (AMBIT.equals(((Classification)lom.getClassificationList().get(i)).getPurpose().getSource())){
							alcon = ((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList();
							for (int j=0;j<alcon.size();j++){
								al.clear();
								al.add(new Integer(idLom));
								al.add(((Taxon)alcon.get(j)).getId());
								WSAccesBD.executeInsert("mer_rec_ambits",al,c);
							}
						}
					}catch(Exception e){
						logger.error("Error a l'inserir ambits ->"+e);
						e.printStackTrace();
					}
					try{
						if (DUC.equals(((Classification)lom.getClassificationList().get(i)).getPurpose().getSource())){
							alcon = ((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList();
							for (int j=0;j<alcon.size();j++){
								al.clear();
								al.add(new Integer(idLom));
								al.add(((Taxon)alcon.get(j)).getId());
								al.add("content");
								WSAccesBD.executeInsert("mer_rec_curriculum",al,c);
							}
						}
					}catch(Exception e){
						logger.error("Error a l'inserir termes DUC ->"+e);
						e.printStackTrace();
					}					
				}
				
				
				/*INSERT valors a MER_REC_ROL_USUARI*/		
				al = lom.getEducational().getIntendedEndUserRoleList();
				for (int i = 0; i< al.size(); i++){	
					try{
						alcon.clear();
						alcon.add(new Integer(idLom));
						alcon.add(((SourceValue)al.get(i)).getId());
						WSAccesBD.executeInsert("mer_rec_rol_usuari",alcon,c);
					}catch(Exception e){
						logger.error("Error a l'inserir rol d'usuari ->"+e);
						e.printStackTrace();
					}
				}
				
				/*INSERT valors a MER_REC_TIPUS_RECURS*/			
				al = lom.getEducational().getLearningResourceTypeList();
				for (int i = 0; i< al.size(); i++){
					try{
						alcon.clear();
						alcon.add(new Integer(idLom));
						alcon.add(((SourceValue)al.get(i)).getId());
						WSAccesBD.executeInsert("mer_rec_tipus_recurs",alcon,c);
					}catch(Exception e){
						logger.error("Error a l'inserir tipus de recurs ->"+e);
						e.printStackTrace();
					}
				}
				
				/*INSERT valors a MER_REC_FORMAT*/
				for (int i = 0; i< lIdForm.size(); i++){
					try{
						alcon.clear();
						alcon.add(new Integer(idLom));
						alcon.add(lIdForm.get(i));
						WSAccesBD.executeInsert("mer_rec_format",alcon,c);
					}catch(Exception e){
						logger.error("Error a l'inserir formats ->"+e);
						e.printStackTrace();
					}
				}
	
				/*INSERT taula de MER_REC_INFO*/
				//La inserció correcte d'aquest camp és obligatòria per la correcte
				//inserció del recurs.
				alcon.clear();
				alcon.add(new Integer(idLom));
				alcon.add(new Integer(getIdStatus(lom.getLifeCycle().getStatus().getValue())));
				
				for (int i=0;i<lom.getMetaMetaData().getContributeList().size();i++){
					if ("creator".equals(((Contribute)lom.getMetaMetaData().getContributeList().get(i)).getRole().getValue()))
						alcon.add(((Contribute)lom.getMetaMetaData().getContributeList().get(i)).getEntity().getUsername());
				}
//				if (alcon.size() < 3)
//					throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
				WSAccesBD.executeInsert("mer_rec_info",alcon,c);
				
				/*INSERT valors a MER_CONTRIBUCIO*/
				al = lom.getLifeCycle().getContributeList();
				al.addAll(lom.getMetaMetaData().getContributeList());
				
				String data = null;
				SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");
				for (int i = 0; i< al.size(); i++){
					try{
						Date dData = null;
						try{
							data = ((Contribute)al.get(i)).getDateTime().getDateTime();
							dData = sDate.parse(data);
						} catch (Exception e){
							dData = new Date();
						}
						
						if (data != null)
							data = sDate.format(sDate.parse(data));
						else data = sDate.format(new Timestamp(System.currentTimeMillis()));//"trunc(SYSDATE)";
						if ((data.compareTo("''")==0)||data=="'null'")
							data = sDate.format(new Timestamp(System.currentTimeMillis()));//"trunc(SYSDATE)";				
							
						alcon.clear();
						alcon.add(new Integer(((Contribute)al.get(i)).getRole().getId()));
						alcon.add(((Contribute)al.get(i)).getEntity().getUsername());
						//alcon.add(data);
						alcon.add(dData);
						alcon.add(((Contribute)al.get(i)).getRole().getValue());
						alcon.add(new Integer(idLom));
						WSAccesBD.executeInsert("mer_contribucio",alcon,c);
					}
					catch (Exception e){
						logger.error("Error a l'inserir una contribució ->"+e);
						e.printStackTrace();
					}
				}
				
				/*INSERT taula de MER_DRETS*/
				try{
					alcon.clear();
					alcon.add(new Integer(idLom));
					if (lom.getRights().getDescription().getString() == null )
						lom.getRights().getDescription().setString("");
					alcon.add(lom.getRights().getDescription().getString());
					alcon.add(new Integer(-1));
					if (lom.getRights().getCopyRightAndOtherRestrictions().getId() == null || lom.getRights().getCopyRightAndOtherRestrictions().getId() == "0")
						alcon.add("");
					else
						alcon.add(lom.getRights().getCopyRightAndOtherRestrictions().getId());
					
					if ("no".equals(lom.getRights().getCost()))
						alcon.add(new Integer(0));
					else
						alcon.add(new Integer(1));
					WSAccesBD.executeInsert("mer_drets",alcon,c);
				}catch(Exception e){
					logger.error("Error a l'inserir els drets ->"+e);
					e.printStackTrace();
				}
				
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
			carregarIdsLom(lom,cb.getConnection(),lIdForm);
			try {
				c = cb.getConnection();
				c.setAutoCommit(true);
				
				//UPDATE recurs a MER_RECURS//
				//obligatori que funcioni correctament.
				WSAccesBD.executeUpdate("mer_recurs","id_rec=?",lRec,lom.getSetQuery("mer_recurs"),lom.getSetQueryFields("mer_recurs"),c);
				
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
					WSAccesBD.executeDelete("mer_rec_nivell_educatiu","id_rec = ?",lRec,c);
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
					al = ((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList();
					if ("ETB".equals(((Classification)lom.getClassificationList().get(i)).getTaxonPath().getSource().getString())){
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
					if("DUC".equals(((Classification)lom.getClassificationList().get(i)).getTaxonPath().getSource().getString())){
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
					WSAccesBD.executeDelete("mer_rec_format","id_rec = ?",lRec,c);			
					if (!lIdForm.isEmpty()){
						for (int i = 0; i< lIdForm.size(); i++){
							alcon.clear();
							alcon.add(idRecurs);
							alcon.add(lIdForm.get(i));	
							WSAccesBD.executeInsert("mer_rec_format",alcon,c);
						}
					}
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
				try{
					al.add(new Integer(lom.getRights().getCost()));
				}catch(Exception e){
					al.add("");
				}
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
						if ("no".equals(lom.getRights().getCost()))
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
				SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");
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
		String aux;
		
		String idRecurs = lom.getGeneral().getIdentifier().getIdEntry();
		lRec.add(new Integer(idRecurs));
		cb = connectBD();		
		Connection c = null;
		carregarIdsLom(lom,cb.getConnection(),lIdForm);
		try {
			c = cb.getConnection();
			c.setAutoCommit(true);
			/******************************************************************
			//COMPROVAR QUE L'USUARI SIGUI EL jpanzano. RESTRICCIÓ TEMPORAL!!!!
			*******************************************************************/
			al = new ArrayList();
			al.add(new Integer(3));
			al.add(new Integer(idRecurs));
			String aux2 = WSAccesBD.executeQuery("select v_entitat from mer_contribucio WHERE id_rol_cont=? AND id_rec=?",al,"v_entitat",c);
			if (!"jpanzano".equals(aux2)){
				logger.error("Intent de modificació d'un recurs que no es propietat de JPANZANO.");
				throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
			}
				
			al.clear();
			
			//UPDATE recurs a MER_RECURS//
			//obligatori que funcioni correctament.
			WSAccesBD.executeUpdate("mer_recurs","id_rec=?",lRec,lom.getSetQuery("mer_recurs"),lom.getSetQueryFields("mer_recurs"),c);
			al.add(new Integer(idRecurs));
			al.add("ca");
			WSAccesBD.executeUpdate("mer_rec_lang","id_rec=? AND lang=?",al,lom.getSetQuery("mer_rec_lang"),lom.getSetQueryFields("mer_rec_lang"),c);
			
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
				WSAccesBD.executeDelete("mer_rec_nivell_educatiu","id_rec = ?",lRec,c);
				if (!al.isEmpty())
					for (int i = 0; i< al.size(); i++){
						try{
							//En cas de Compulsory-education parsegem l'edat per saber si correspon
							//a Ed. Primaria o a Ed. Secundaria, a ambdos o a cap.
							if ("2".equals(((SourceValue)al.get(i)).getId())){
								aux = lom.getEducational().getTypicalAgeRangeMin();
								if ( aux != null && !"".equals(aux) && 12>=Integer.parseInt(aux)){
									alcon.clear();
									alcon.add(idRecurs);
									alcon.add("3");
									WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
								}
								aux = lom.getEducational().getTypicalAgeRangeMax();
								if (aux != null && !"".equals(aux) && 12<=Integer.parseInt(aux)){
									alcon.clear();
									alcon.add(idRecurs);
									alcon.add("4");
									WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
								}
							}else{
								//En cas de pre-school parsegem l'edat per saber si correspon
								//a Llar d'infants o a Parvulari, a ambdos o a cap.
								if ("1".equals(((SourceValue)al.get(i)).getId())){
									aux = lom.getEducational().getTypicalAgeRangeMin();
									if ( aux != null && !"".equals(aux) && 3>=Integer.parseInt(aux)){
										alcon.clear();
										alcon.add(idRecurs);
										alcon.add("1");
										WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
									}
									aux = lom.getEducational().getTypicalAgeRangeMax();
									if (aux != null && !"".equals(aux) && 3<=Integer.parseInt(aux)){
										alcon.clear();
										alcon.add(idRecurs);
										alcon.add("2");
										WSAccesBD.executeInsert("mer_rec_nivell_educatiu",alcon,c);//idRecurs+",'"+al.get(i)+"'"
									}
								}else{
									alcon.clear();
									alcon.add(((SourceValue)al.get(i)).getId());
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

			/*INSERT nous valors a MER_REC_PARAULES_CLAU* /
			//Versió antiga. Cal carregar i desar la nova.
			al = recursMerli.getTaxon();
			AccesBD.executeDelete("mer_rec_paraules_clau","id_rec = ?",lRec,c);			
			if (!al.isEmpty()){
				for (int i = 0; i< al.size(); i++)
					AccesBD.executeInsert("mer_rec_paraules_clau",idRecurs+","+al.get(i)+",'ETB'",c);
			}*/
			
			for (int i=0;i<lom.getClassificationList().size();i++){	
				al = ((Classification)lom.getClassificationList().get(i)).getTaxonPath().getTaxonList();
				if (THESAURUS.equals(((Classification)lom.getClassificationList().get(i)).getTaxonPath().getSource().getString())){
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
				if (AMBIT.equals(((Classification)lom.getClassificationList().get(i)).getTaxonPath().getSource().getString())){
					try{
						WSAccesBD.executeDelete("mer_rec_ambits","id_rec = ?",lRec,c);			
						if (!al.isEmpty()){
							for (int j = 0; j< al.size();j++){
								alcon.clear();
								alcon.add(idRecurs);
								alcon.add(((Taxon)al.get(j)).getId());	
								WSAccesBD.executeInsert("mer_rec_ambits",alcon,c);
								//AccesBD.executeInsert("mer_rec_termes",idRecurs+","+al.get(i)+",'ETB'",c);
							}
						}
					}catch(Exception e){
						logger.error("Error a l'inserir ambits ->"+e);
						e.printStackTrace();
					}
				}
				if(DUC.equals(((Classification)lom.getClassificationList().get(i)).getTaxonPath().getSource().getString())){
//					String aux;
//					try{
//						AccesBD.executeDelete("mer_rec_curriculum","id_rec = ?",lRec,c);			
//						if (!al.isEmpty()){
//							for (int j = 0; j< al.size(); j++){
//								aux  = ((Taxon) al.get(j)).getId();
//								alcon = new ArrayList();
//								alcon.add(idRecurs);
//								if (aux != null && aux.length()>0 && aux.charAt(0) > '9'){
//									for (int k = 0; k < aux.length(); k++){
//										if (aux.charAt(k) <= '9'){
//											alcon.add(aux.substring(0,k));
//											alcon.add(aux.substring(k)); //idcur
//											break;
//										}
//									}
//								}
//								else{
//									alcon.add("content");
//									alcon.add(aux);
//								}
//								AccesBD.executeInsert("mer_rec_curriculum","id_rec, v_type, id_node",alcon,c);					
//							}
//						}
//					}catch(Exception e){
//						logger.error("Error a l'inserir termes DUC ->"+e);
//						e.printStackTrace();
//					}
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
				WSAccesBD.executeDelete("mer_rec_format","id_rec = ?",lRec,c);			
				if (!lIdForm.isEmpty()){
					for (int i = 0; i< lIdForm.size(); i++){
						alcon.clear();
						alcon.add(idRecurs);
						alcon.add(lIdForm.get(i));	
						WSAccesBD.executeInsert("mer_rec_format",alcon,c);
					}
				}
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
			try{
				if (0 == WSAccesBD.executeExist("mer_drets","id_rec",((Integer) lRec.get(0)).toString(),"",c)){
					alcon.clear();
					alcon.add(idRecurs);
					alcon.add(lom.getRights().getDescription().getString());	
					alcon.add(new Integer("-1"));
					alcon.add(lom.getRights().getCopyRightAndOtherRestrictions().getId());					
					if ("no".equals(lom.getRights().getCost()))
						alcon.add(new Integer(0));
					else 
						alcon.add(new Integer(1));
					WSAccesBD.executeInsert("mer_drets",alcon,c);
					//AccesBD.executeInsert("mer_drets",idRecurs+",'"+
					//		recursMerli.getRightsDesc()+"',null,"+recursMerli.getLicense()+","+recursMerli.getCost(),c);
				}else{
					al.clear();			
					if ("no".equals(lom.getRights().getCost()))
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
					WSAccesBD.executeUpdate("mer_drets","id_rec = ?",lRec,"COST=?, DESCRIPCIO = ?, ID_LLICENCIA = ? ",al,c);
				}
			}catch(Exception e){
				logger.error("Error a l'inserir els drets ->"+e);
				e.printStackTrace();
			}
			
			/*INSERT valors a MER_CONTRIBUCIO*/
			//Si no funciona no s'inserta el lom.
			al = lom.getLifeCycle().getContributeList();
			al.addAll(lom.getMetaMetaData().getContributeList());

			String data = null;
			Date dDate = null;
			SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");
			//SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			for (int i = 0; i< al.size(); i++){
				try{
					lRec = new ArrayList();
					alcon =  new ArrayList();
					
					if ((Contribute)lom.getLifeCycle().getContributeList().get(i) != null){									
						try{
							data = ((Contribute)al.get(i)).getDateTime().getDateTime();
							dDate = sDate.parse(data);
						} catch (Exception e){
							dDate = new Date();
						}
					}
					/*if ((data.compareTo("''")==0)||data=="'null'"|| data == null){
						data = sDate.format(new Timestamp(System.currentTimeMillis()));//"trunc(SYSDATE)";
					}*/				
					lRec.add(idRecurs);
					lRec.add(new Integer(((Contribute)al.get(i)).getRole().getId()));
					alcon.add(((Contribute)al.get(i)).getEntity().getUsername());
					alcon.add(dDate);
					alcon.add(((Contribute)al.get(i)).getRole().getValue());
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
			lParam.add(new Integer(ESTAT_DENEGAT));
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
		
		ArrayList l = new ArrayList();
		l.add(new Integer(idResource.getIdentifier()));
		ConnectionBean cb = null;
		Connection c = null;
		String query;
		ArrayList lParam = new ArrayList();
		ArrayList lCond = new ArrayList();
		
		try {
			cb = connectBD();
			c=cb.getConnection();
			
			WSAccesBD.executeDelete("mer_rec_llengua","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_nivell_educatiu","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_format","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_termes","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_ambits","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_rol_usuari","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_tipus_recurs","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_contribucio","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_info","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_curriculum","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_rec_lang","id_rec = ?",l,c);
			WSAccesBD.executeDelete("mer_recurs","id_rec = ?",l,c);
						
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

	
	
	public Lom getResource(IdResource idResource) throws MerliDBException {	
		RecursMerli rm = new RecursMerli(Integer.parseInt(idResource.getIdentifier()));
		Lom lom = new Lom(); 

		ArrayList al = new ArrayList();
		ArrayList laux = new ArrayList();
		ArrayList laux2 = new ArrayList();
		ArrayList lRec = new ArrayList();
		lRec.add(new Integer(idResource.getIdentifier()));
		TaxonPath taxonPath = null;
		Contribute contribute;

		Connection c = null;
		ConnectionBean cb = null;
		Classification classification;
		Map m = null;
		String aux;
	
		try{
			cb = connectBD();
			c=cb.getConnection();	
			
			m= WSAccesBD.getObjectList("mer_recurs",rm.getFieldsList("mer_recurs"),"id_rec=?",lRec,"id_rec",c);
			if (((ArrayList) m.get("descripcio")).size() <= 0){
				//logger.error("Error, el recurs solicitat no existeix-> "+idResource);
				throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
			}
			lom.getGeneral().getIdentifier().setEntry("MERLI"+idResource.getIdentifier());
			//pendent de localitzar
			
//			try{
//				if (((ArrayList) m.get("id_ambit")).size() > 0){
//					//rm.setAmbit(((BigDecimal)((ArrayList) m.get("id_ambit")).get(0)).toString());
//					classification = new Classification();
//					classification.setPurpose(new SourceValue(AMBIT,"competency","purpose"));
//						
//					taxonPath = new TaxonPath();
//					taxonPath.setSource(new LangString(AMBIT,LANG));
//					
//					al.clear();
//					al.add(((BigDecimal)((ArrayList) m.get("id_ambit")).get(0)).toString());
//					laux.add(new Taxon(((BigDecimal)((ArrayList) m.get("id_ambit")).get(0)).toString(),new LangString(WSAccesBD.executeQuery("select ambit from mer_ambits where id_ambit = ?",al,"ambit",c),LANG)));
//					
//					
//					taxonPath.setTaxonList(laux);				
//					classification.setTaxonPath(taxonPath);
//					classification.setDescription(new LangString("Ambit del recurs",LANG));	
//					lom.getClassificationList().add(classification);
//				}
//			}catch(Exception e){
//				logger.error("Error a l'obtenir l'ambit ->"+e);
//				e.printStackTrace();
//			}
			
//			if (((ArrayList) m.get("descripcio")).size() > 0)
//				lom.getGeneral().setDescription(new LangString((String) ((ArrayList) m.get("descripcio")).get(0),LANG));
			
			try{
				if (((ArrayList) m.get("id_dificultat")).size() > 0){
					al.clear();
					if (((ArrayList) m.get("id_dificultat")).get(0) != null){
						al.add(((BigDecimal)((ArrayList) m.get("id_dificultat")).get(0)).toString());
						lom.getEducational().setDifficulty(new SourceValue(LOM_VALUE,WSAccesBD.executeQuery("select dificultat from mer_dificultat where id_dificultat = ?",al,"dificultat",c),"dificultat"));
					}
				}
			}catch(Exception e){
				logger.error("Error a l'obtenir la dificultat ->"+e);
				e.printStackTrace();
			}	

			if (((ArrayList) m.get("drets")).size() > 0){
				if (((BigDecimal)((ArrayList) m.get("drets")).get(0)).toString().compareTo("0") == 0 )
					lom.getRights().setCost("no");
				else
					lom.getRights().setCost("yes");
			}
				
			if (((ArrayList) m.get("duracio")).size() > 0)
				lom.getEducational().setTypicalLearningTime(new Duration((String) ((ArrayList) m.get("duracio")).get(0)));
			
			if (((ArrayList) m.get("edat_max")).size() > 0)
				aux = ((BigDecimal)((ArrayList) m.get("edat_max")).get(0)).toString();
			else
				aux = "U";
			
			if (((ArrayList) m.get("edat_min")).size() > 0)
				aux = ((BigDecimal)((ArrayList) m.get("edat_min")).get(0)).toString()+"-"+aux;
			else
				aux = "U-"+aux;
			
			lom.getEducational().setTypicalAgeRange(aux);

			//Es carrega desde MER_REC_LANG
//			if (((ArrayList) m.get("titol")).size() > 0)
//				lom.getGeneral().setTitle(new LangString((String) ((ArrayList) m.get("titol")).get(0),LANG));			
			
			if (((ArrayList) m.get("url")).size() > 0)
				lom.getTechnical().setLocation((String) ((ArrayList) m.get("url")).get(0));
			
			if (((ArrayList) m.get("versio")).size() > 0)
				lom.getLifeCycle().setVersion(new LangString((String) ((ArrayList) m.get("versio")).get(0),LANG));

						
			//Carrega dels camps textuals desde mer_rec_lang.
			m = WSAccesBD.getObjectList("mer_rec_lang",rm.getFieldsList("mer_rec_lang"),"lang='ca' AND id_rec = ?",lRec,"id_rec",c);
			
			if (((ArrayList) m.get("titol")).size() > 0)
				lom.getGeneral().setTitle(new LangString((String) ((ArrayList) m.get("titol")).get(0),LANG));	
			if (((ArrayList) m.get("descripcio")).size() > 0)
				lom.getGeneral().setDescription(new LangString((String) ((ArrayList) m.get("descripcio")).get(0),LANG));
			if (((ArrayList) m.get("drets")).size() > 0)
				lom.getRights().setDescription(new LangString((String) ((ArrayList) m.get("drets")).get(0),LANG));	
			
			//Carrega Tipus de Destinatari endUserRol
			try{
				laux = new ArrayList();
				m = WSAccesBD.getObjectList("mer_rec_rol_usuari",rm.getFieldsList("mer_rec_rol_usuari"),"id_rec = ?",lRec,"id_rec",c);
				for (int i = 0; i< ((ArrayList)m.get("id_rol_usuari")).size();i++){
					al.clear();
					al.add(((BigDecimal)((ArrayList) m.get("id_rol_usuari")).get(i)).toString());
					laux.add((new SourceValue(ELN,WSAccesBD.executeQuery("select rol_usuari from mer_rol_usuari where id_rol_usuari = ?",al,"rol_usuari",c),"intendedEndUserRol")));
				}	
				lom.getEducational().setIntendedEndUserRoleList(laux);
			}catch(Exception e){
				logger.error("Error a l'obtenir el rol d'usuari ->"+e);
				e.printStackTrace();
			}	
			
			//Carrega dades del context.
			try{
				boolean afegir;
				laux = new ArrayList();
				m = WSAccesBD.getObjectList("mer_rec_nivell_educatiu",rm.getFieldsList("mer_rec_nivell_educatiu"),"id_rec = ?",lRec,"id_rec",c);
				for (int i = 0; i< ((ArrayList)m.get("id_nivell")).size();i++){
					afegir = true;
					al.clear();				
					al.add(((BigDecimal)((ArrayList) m.get("id_nivell")).get(i)).toString());
					aux = WSAccesBD.executeQuery("select merne.nivell from mer_nivell_edu_cat mern, mer_nivell_educatiu merne where mern.id_nivell = merne.ID_NIVELL and mern.id_nivell_cat = ?",al,"nivell",c);
					for (int j=0; j<laux.size();j++){
						if (aux.equals(((SourceValue)laux.get(j)).getValue()))
							afegir = false;
					}
					if (afegir)
						laux.add(new SourceValue(ELN,aux,"context"));
				}
				lom.getEducational().setContextList(laux);
			}catch(Exception e){
				logger.error("Error a l'obtenir el context ->"+e);
				e.printStackTrace();
			}	
				
			//Carrega Tipus de recurs.
			try{
				laux = new ArrayList();
				m = WSAccesBD.getObjectList("mer_rec_tipus_recurs",rm.getFieldsList("mer_rec_tipus_recurs"),"id_rec = ?",lRec,"id_rec",c);
				for (int i = 0; i< ((ArrayList)m.get("id_tipus_recurs")).size();i++){
					al.clear();
					al.add(((BigDecimal)((ArrayList) m.get("id_tipus_recurs")).get(i)).toString());
					laux.add(new SourceValue(ELN,WSAccesBD.executeQuery("select tipus_recurs from mer_tipus_recurs where id_tipus_recurs = ?",al,"tipus_recurs",c),"learningResourceType"));
				}
				lom.getEducational().setLearningResourceTypeList(laux);
			}catch(Exception e){
				logger.error("Error a l'obtenir el tipus de recurs ->"+e);
				e.printStackTrace();
			}	
				
			
			/* Carregar AMBITS */
			try{
				laux = new ArrayList();
				if (lom.getClassificationList() == null)
					lom.setClassificationList(new ArrayList());
				classification = new Classification();
				classification.setPurpose(new SourceValue(AMBIT,"competency","purpose"));
			
				taxonPath = new TaxonPath();
				taxonPath.setSource(new LangString(AMBIT,LANG));
				al.clear();
				al.add("id_ambit");
				//Canviar al per rm.getFieldsList("mer_rec_ambits").
				m = WSAccesBD.getObject("mer_rec_ambits",al,"id_rec = ?",lRec,c);
				for (int i = 0; i< ((ArrayList)m.get("id_ambit")).size();i++){
					al.clear();
					al.add(((BigDecimal)((ArrayList) m.get("id_ambit")).get(i)).toString());	
					laux.add(new Taxon(((BigDecimal)((ArrayList) m.get("id_ambit")).get(i)).toString(),new LangString(WSAccesBD.executeQuery("select ambit from mer_ambits where id_ambit = ?",al,"ambit",c),LANG)));					
				}
				taxonPath.setTaxonList(laux);
				classification.setTaxonPath(taxonPath);
				classification.setDescription(new LangString("Ambit del recurs",LANG));	
				lom.getClassificationList().add(classification);
			}catch(Exception e){
				logger.error("Error a l'obtenir els termes del DUC ->"+e);
				e.printStackTrace();
			}	
			
			
			/*Carrega llistat de DUC.*/
			try{
				laux = new ArrayList();
				if (lom.getClassificationList() == null)
					lom.setClassificationList(new ArrayList());
				classification = new Classification();
				classification.setPurpose(new SourceValue("DUC","educational objective","purpose"));
			
				taxonPath = new TaxonPath();
				taxonPath.setSource(new LangString("DUC",LANG));
				
				m = WSAccesBD.getObject("mer_rec_curriculum",rm.getFieldsList("mer_rec_curriculum"),"id_rec = ?",lRec,c);
				for (int i = 0; i< ((ArrayList)m.get("id_node")).size();i++){
					al.clear();
					if (DUC_CONTENT.equals((String)((ArrayList) m.get("v_type")).get(i))){
						al.add(((BigDecimal)((ArrayList) m.get("id_node")).get(i)).toString());	
						laux.add(new Taxon(((BigDecimal)((ArrayList) m.get("id_node")).get(i)).toString(),new LangString(WSAccesBD.executeQuery("select v_term from cur_content where id_node = ?",al,"v_term",c),LANG)));
					}
				}
				taxonPath.setTaxonList(laux);
				classification.setTaxonPath(taxonPath);
				classification.setDescription(new LangString("Continguts del curriculum relacionat",LANG));	
				lom.getClassificationList().add(classification);
			}catch(Exception e){
				logger.error("Error a l'obtenir els termes del DUC ->"+e);
				e.printStackTrace();
			}												
	
			//Carrega llistat de paraules clau.
			try{
				laux = new ArrayList();
				classification = new Classification();
				classification.setPurpose(new SourceValue("ETB","discipline","purpose"));
			
				taxonPath = new TaxonPath();
				taxonPath.setSource(new LangString("Thesaurus de ELR",LANG));
	
				m = WSAccesBD.getObject("mer_rec_termes",rm.getFieldsList("mer_rec_termes"),"id_rec = ?",lRec,c);
				for (int i = 0; i< ((ArrayList)m.get("id_terme")).size();i++){
					al.clear();
					al.add(((BigDecimal)((ArrayList) m.get("id_terme")).get(i)).toString());
					laux.add(new Taxon(((BigDecimal)((ArrayList) m.get("id_terme")).get(i)).toString(),new LangString(WSAccesBD.executeQuery("select terme_ca from the_termes where id_terme = ?",al,"terme_ca",c),LANG)));
				}
				taxonPath.setTaxonList(laux);
				classification.setTaxonPath(taxonPath);
				classification.setDescription(new LangString("Termes seleccionats del thesaurus de ETB",LANG));	
				lom.getClassificationList().add(classification);
			}catch(Exception e){
				logger.error("Error a l'obtenir els termes del thesaurus ->"+e);
				e.printStackTrace();
			}	
							
			//Carrega llistat de llengues.
			try{
				laux = new ArrayList();	
				m = WSAccesBD.getObject("mer_rec_llengua",rm.getFieldsList("mer_rec_llengua"),"id_rec = ?",lRec,c);		
				for (int i = 0; i< ((ArrayList)m.get("id_llengua")).size();i++){
					laux.add((String)((ArrayList) m.get("id_llengua")).get(i));
				}
				lom.getGeneral().setLanguageList(laux);
			}catch(Exception e){
				logger.error("Error a l'obtenir les llengües ->"+e);
				e.printStackTrace();
			}	
				
			//Carregar dades del format.
			try{
				laux = new ArrayList();
				m = WSAccesBD.getObjectList("mer_rec_format",rm.getFieldsList("mer_rec_format"),"id_rec = ?",lRec,"id_rec",c);
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
							
			//Carregar dades de Contribucions.
			try{
				laux = new ArrayList();
				laux2 = new ArrayList();
				m = WSAccesBD.getObjectList("mer_contribucio",rm.getFieldsList("mer_contribucio"),"id_rec = ?",lRec,"id_rec",c);
				MerliContribution mc;
				SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");
				for (int i = 0; i< ((ArrayList)m.get("id_rol_cont")).size();i++){
					al.clear();
					contribute = new Contribute();
	
					al.add(((BigDecimal)(((ArrayList) m.get("id_rol_cont")).get(i))).toString());
					contribute.setRole(new SourceValue(LOM_VALUE,WSAccesBD.executeQuery("select rol from mer_rol_contribucio where id_rol = ?",al,"rol",c),"role"));
					contribute.setEntity(new Entity((String) ((ArrayList) m.get("v_entitat")).get(i),"",DEPT_EDUCACIO,""));
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
						if (contribute.getRole().getValue().equals("author"))
							lom.getLifeCycle().getContributeList().add(contribute);
						else
							lom.getMetaMetaData().getContributeList().add(contribute);
					}
				}
				//lom.getLifeCycle().setContributeList(laux2);
				//lom.getMetaMetaData().setContributeList(laux);
			}catch(Exception e){
				logger.error("Error a l'obtenir les contribucions ->"+e);
				e.printStackTrace();
			}	
	
			//Carregar informació de drets: mer_drets
			try {
				m = WSAccesBD.getObject("mer_drets",rm.getFieldsList("mer_drets"),"id_rec = ?",lRec,c);				
				if (((ArrayList) m.get("id_llicencia")).size() > 0 && ((ArrayList) m.get("id_llicencia")).get(0) != null){
					al.clear();					
					al.add(((BigDecimal)((ArrayList) m.get("id_llicencia")).get(0)).toString());
					lom.getRights().setCopyRightAndOtherRestrictions(new SourceValue(LOM_VALUE,WSAccesBD.executeQuery("select llicencia from mer_llicencia where id_llicencia = ?",al,"llicencia",c),"copyRightAndOtherRestrictions"));
				}
				//Es carrega desde el MER_REC_LANG.
//				if (((ArrayList) m.get("descripcio")).size() > 0 && ((ArrayList) m.get("descripcio")).get(0) != null)
//					lom.getRights().setDescription(new LangString((String) ((ArrayList) m.get("descripcio")).get(0),LANG));	
				if (((ArrayList) m.get("cost")).size() > 0 && ((ArrayList) m.get("cost")).get(0) != null){
					if("0".equals(((BigDecimal) ((ArrayList) m.get("cost")).get(0)).toString()))
						lom.getRights().setCost("no");
					else
						lom.getRights().setCost("yes");
				}				
			}catch(Exception e){
				logger.error("Error a l'obtenir els drets ->"+e);
				e.printStackTrace();
			}
				
			//Carregar informació d'estat del recurs: mer_rec_info
			try {
				m = WSAccesBD.getObject("mer_rec_info",rm.getFieldsList("mer_rec_info"),"id_rec = ?",lRec,c);				
				lom.getMetaMetaData().setIdentifier(new Identifier(CELEBRATE,MERLI+idResource.getIdentifier()));
				
				if (((ArrayList) m.get("id_estat")).size() > 0){
					String estatLom = "";
					switch(((BigDecimal)((ArrayList) m.get("id_estat")).get(0)).intValue()){
						case -1: estatLom = DENEGAT;
							break;
						case 0:	estatLom = ESBORRANY;
							break;
						case 1: estatLom = PER_REVISAR;
							break;
						case 2: estatLom = EN_REVISIO;
							break;
						case 3: estatLom = REVISANT;
							break;
						case 4: estatLom = ACCEPTAT;
							break;
					}
					lom.getLifeCycle().setStatus(new SourceValue(LOM_VALUE,estatLom,"status"));
				}
//				contribute = new Contribute();
//				contribute.setRole(new SourceValue(LOM_VALUE,LOM_CREATOR,"role"));
//				contribute.setEntity(new Entity((String) ((ArrayList) m.get("v_responsable")).get(0),"",DEPT_EDUCACIO,""));
//				if (((ArrayList) m.get("d_data")) != null)
//				contribute.setDateTime(new edu.xtec.merli.ws.objects.Date((Timestamp) ((ArrayList) m.get("d_data")).get(0)));
//				
//				lom.getMetaMetaData().getContributeList().add(contribute);
			}catch(Exception e){
				logger.error("Error a l'obtenir la informació d'estat del recurs ->"+e);
				e.printStackTrace();
			}	
		} catch (Exception e) {
			logger.error("Error on getResource ("+idResource+")"+" ->"+e.getMessage());
			throw new MerliDBException(MerliDBException.ERROR_SQL);
		}finally{
			tancaConexio(c,cb);
		}

		return lom;
	}

	public ElementDUC getElementDUCold(IdElement idElement) throws MerliDBException, SQLException {
		
		
		ElementDUC elementDUC = new ElementDUC();	
		ElementDUC elDUC;
		DUCRelation ducRelation;
		ArrayList lRec = new ArrayList();	
		ArrayList laux = new ArrayList();
		lRec.add(new Integer(idElement.getIdentifier()));
		String id_nodecur_content;
		Connection c = null;
		ConnectionBean cb = null;
		Map m = null;
		String aux;
		String query;
		
		try{
			cb = connectBD();
			c=cb.getConnection();
		
			m= WSAccesBD.getObject("cur_content",elementDUC.getFieldsList("cur_content"),"id_node=?",lRec,c);
	
			elementDUC.setTerm((String)((ArrayList)m.get("v_term")).get(0));
			elementDUC.setCategory((String)((ArrayList)m.get("v_category")).get(0));
			elementDUC.setDescription((String)((ArrayList)m.get("v_description")).get(0));
			elementDUC.setReferences((String)((ArrayList)m.get("v_references")).get(0));
			elementDUC.setIdElement(idElement);			
			id_nodecur_content = String.valueOf(((ArrayList)m.get("id_nodecur_content")).get(0));
		
			try{
				query = "id_node = "+idElement.getIdentifier()+" and v_type = '"+idElement.getTypeElement()+"'";
				//m = AccesBD.getJoin(elementDUC.getFieldsList("cur_thesaurus_etb"),new ArrayList(),"THE_TERMES","CUR_THESAURUS_ETB","id_terme","id_terme","",query,"","","",c);
				m = WSAccesBD.getJoin(elementDUC.getFieldsList("cur_thesaurus_etb"),new ArrayList(),"THE_TERMES","CUR_THESAURUS","id_terme","id_paraula","",query,"","","",c);
				TaxonPath taxonPath = new TaxonPath();
				taxonPath.setSource(new LangString("ETB",LANG));
				taxonPath.setDescription(new LangString("Thesaurus de ELR",LANG));
			
				for (int i=0;i<((ArrayList)m.get("id_terme")).size();i++){
					Taxon taxon = new Taxon();
					taxon.setId(String.valueOf(((ArrayList)m.get("id_terme")).get(i)));
					taxon.setEntry(new LangString((String) ((ArrayList)m.get("terme_ca")).get(i),LANG));
					taxonPath.getTaxonList().add(taxon);
				}
			
				elementDUC.setTaxonList(taxonPath);
			}catch(Exception e){
				logger.error("Error al carregar termes del thesaurus ->"+e);
				e.printStackTrace();
			}
			
			
			try{
				lRec.clear();
				lRec.add(new Integer(idElement.getIdentifier()));
				laux.clear();
		
				//relacio content/area			
				query ="ID_NODE = "+ idElement.getIdentifier();//"cur_area ca, cur_content_area cca WHERE cca.ID_NODE = ? AND cca.ID_NODECUR_AREA = ca.ID_NODE";
				//m = AccesBD.getQuery(query,elementDUC.getFieldsList("cur_area","ca"),lRec,c);
				m = WSAccesBD.getJoin(elementDUC.getFieldsList("cur_level_area_content"),new ArrayList(),"cur_area","cur_content_area","ID_NODE","ID_NODECUR_AREA","",query,"","","",c);
				
				if (m != null && ((ArrayList)m.get("v_term")).size() > 0){
					elDUC = new ElementDUC();
					
					if (((ArrayList) m.get("v_term")) == null || ((ArrayList) m.get("v_term")).size() == 0)
						throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
					elDUC.setTerm((String)((ArrayList)m.get("v_term")).get(0));
					
					if (((ArrayList) m.get("v_category")) != null && ((ArrayList) m.get("v_category")).size() > 0)
						elDUC.setCategory((String)((ArrayList)m.get("v_category")).get(0));
	
					if (((ArrayList) m.get("v_description")) != null && ((ArrayList) m.get("v_description")).size() > 0)
						elDUC.setDescription((String)((ArrayList)m.get("v_description")).get(0));
	
					if (((ArrayList) m.get("v_references")) != null && ((ArrayList) m.get("v_references")).size() > 0)
						elDUC.setReferences((String)((ArrayList)m.get("v_references")).get(0));
					
					IdElement oId = idElement;
					String sId = String.valueOf(((ArrayList)m.get("id_node")).get(0));
					oId = new IdElement(sId, "area");

					elDUC.setIdElement(oId);	
		
					ducRelation = new DUCRelation();
					ducRelation.setDUCRelationType("RCA");
					ducRelation.setElementDUC(elDUC);
					laux.add(ducRelation);
				}
			}catch(Exception e){
				logger.error("Error al carregar les relacions content/area ->"+e);
				e.printStackTrace();
			}
			
			
			//relacio content/level
			try{
				query ="ID_NODE = "+idElement.getIdentifier();// "cur_level cl, cur_content_level ccl WHERE ccl.ID_NODE = ? AND ccl.ID_NODECUR_LEVEL = cl.ID_NODE";
				m = WSAccesBD.getJoin(elementDUC.getFieldsList("cur_level_area_content"),new ArrayList(),"cur_level","cur_content_level","ID_NODE","ID_NODECUR_LEVEL","",query,"","","",c);
				//m = AccesBD.getQuery(query,elementDUC.getFieldsList("cur_level","cl"),lRec,c);
				
				if (m != null && ((ArrayList)m.get("v_term")).size() > 0){
					elDUC = new ElementDUC();
					if (((ArrayList) m.get("v_term")) == null || ((ArrayList) m.get("v_term")).size() == 0)
						throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
					elDUC.setTerm((String)((ArrayList)m.get("v_term")).get(0));
					
					if (((ArrayList) m.get("v_category")) != null && ((ArrayList) m.get("v_category")).size() > 0)
						elDUC.setCategory((String)((ArrayList)m.get("v_category")).get(0));
	
					if (((ArrayList) m.get("v_description")) != null && ((ArrayList) m.get("v_description")).size() > 0)
						elDUC.setDescription((String)((ArrayList)m.get("v_description")).get(0));
	
					if (((ArrayList) m.get("v_references")) != null && ((ArrayList) m.get("v_references")).size() > 0)
						elDUC.setReferences((String)((ArrayList)m.get("v_references")).get(0));
					
					IdElement oId = idElement;
					String sId = String.valueOf(((ArrayList)m.get("id_node")).get(0));
					oId = new IdElement(sId, "level");
					
					elDUC.setIdElement(oId);	
					ducRelation = new DUCRelation();
					ducRelation.setDUCRelationType("RCL");
					ducRelation.setElementDUC(elDUC);
					laux.add(ducRelation);
				}
			}catch(Exception e){
				logger.error("Error al carregar les relacions content/level ->"+e);
				e.printStackTrace();
			}
			
			//relacio content/content
			try{
				lRec.clear();
				if (Integer.parseInt(id_nodecur_content) > 0){
				
					lRec.add(id_nodecur_content);
					m= WSAccesBD.getObject("cur_content",elementDUC.getFieldsList("cur_content"),"id_node=?",lRec,c);
					
					elDUC = new ElementDUC();
					if (((ArrayList) m.get("v_term")) == null || ((ArrayList) m.get("v_term")).size() == 0)
						throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
					elDUC.setTerm((String)((ArrayList)m.get("v_term")).get(0));
					
					if (((ArrayList) m.get("v_category")) != null && ((ArrayList) m.get("v_category")).size() > 0)
						elDUC.setCategory((String)((ArrayList)m.get("v_category")).get(0));
	
					if (((ArrayList) m.get("v_description")) != null && ((ArrayList) m.get("v_description")).size() > 0)
						elDUC.setDescription((String)((ArrayList)m.get("v_description")).get(0));
	
					if (((ArrayList) m.get("v_references")) != null && ((ArrayList) m.get("v_references")).size() > 0)
						elDUC.setReferences((String)((ArrayList)m.get("v_references")).get(0));
					
					IdElement oId = idElement;
					String sId = String.valueOf(((ArrayList)m.get("id_node")).get(0));
					oId = new IdElement(sId, "content");
					elDUC.setIdElement(oId);	
					
					ducRelation = new DUCRelation();
					ducRelation.setDUCRelationType("RCC");
					ducRelation.setElementDUC(elDUC);
					laux.add(ducRelation);
				}
			}catch(Exception e){
				logger.error("Error al carregar les relacions content/content ->"+e);
				e.printStackTrace();
			}
			
			elementDUC.setDUCRelationList(laux);
			
		}catch (Exception e) {
			logger.error("ws Error on getRecurs ->"+e.getMessage());
			throw new MerliDBException(MerliDBException.ERROR_SQL);
		}finally{
			tancaConexio(c,cb);
		}
		
		return elementDUC;
	}
	

	/**
	 * Retorna el DUC complert. Es pot demanar excloure els continguts posant contents=false.
	 * @param contents es volen continguts o no
	 * @return
	 * @throws MerliDBException
	 * @throws SQLException
	 */
	public ListDUC getAllElementDUCold(boolean contents) throws MerliDBException, SQLException {

		ElementDUC elementDUC = new ElementDUC();	
		ListDUC listDUC;

		ArrayList lRec = new ArrayList();	
		ArrayList lres = new ArrayList();
		lRec.add(ID_CAMP_PARE);

		Connection c = null;
		ConnectionBean cb = null;
		Map m = null;

		try{
			cb = connectBD();
			c=cb.getConnection();
		
			m= WSAccesBD.getObject("cur_level",elementDUC.getFieldsList("cur_level"),CAMP_PARE+"=?",lRec,c);
			
			for (int i=0;i<((ArrayList)m.get("id_node")).size();i++){
				int sId = Integer.parseInt(((ArrayList)m.get("id_node")).get(i).toString());
				elementDUC= getFullElementDUCold(c, sId, WSMerliBD.TIPUS_LEVEL,contents);
				lres.add(elementDUC);
			}
			
		}catch (Exception e) {
			logger.error("ws Error on getRecurs ->"+e.getMessage());
			throw new MerliDBException(MerliDBException.ERROR_SQL);
		}finally{
			tancaConexio(c,cb);
		}
		listDUC = new ListDUC(lres);
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
	public ElementDUC getFullElementDUCold(Connection con, int idElement, int tipus, boolean contents) throws MerliDBException, SQLException {
		// TODO Auto-generated method stub
		
		ElementDUC elementDUC = new ElementDUC();	
		ElementDUC elDUC;
		DUCRelation ducRelation;
		ArrayList lRec = new ArrayList();	
		ArrayList laux = new ArrayList();	
		lRec.add(new Integer(idElement));
		String id_nodecur_content;
		String id_nodecur_level;
		ConnectionBean cb = null;
		Connection c = null;
		Map m = null;
		String aux;
		String query;
		IdElement oId;
		try{
			if (con==null){
				cb = connectBD();
				try{
					c=cb.getConnection();
				}catch(Exception e){
					logger.error("Error al crear la connexió.");
					throw e;
				}
			}else{
				c = con;
			}
			//Recuperar dades del node donat.
			switch(tipus){
			case TIPUS_LEVEL:
				m= WSAccesBD.getObject("cur_level",elementDUC.getFieldsList("cur_level"),"id_node=?",lRec,c);
				elementDUC.setCategory((String)((ArrayList)m.get("v_category")).get(0));
				id_nodecur_level = String.valueOf(((ArrayList)m.get("id_nodecur_level")).get(0));
				aux = "level";
				break;
			case TIPUS_AREA:
				m= WSAccesBD.getObject("cur_area",elementDUC.getFieldsList("cur_area"),"id_node=?",lRec,c);
				id_nodecur_level = String.valueOf(((ArrayList)m.get("id_nodecur_level")).get(0));
				aux = "area";
				break;
			case TIPUS_CONTENT:
				m= WSAccesBD.getObject("cur_content",elementDUC.getFieldsList("cur_content"),"id_node=?",lRec,c);
				elementDUC.setCategory((String)((ArrayList)m.get("v_category")).get(0));
				id_nodecur_content = String.valueOf(((ArrayList)m.get("id_nodecur_content")).get(0));
				aux = "content";
				break;
			default:
				aux = "error";
			}
			String sId = String.valueOf(((ArrayList)m.get("id_node")).get(0));
			oId = new IdElement(sId, aux);

			elementDUC.setIdElement(oId);
			elementDUC.setTerm((String)((ArrayList)m.get("v_term")).get(0));
			elementDUC.setDescription((String)((ArrayList)m.get("v_description")).get(0));
			elementDUC.setReferences((String)((ArrayList)m.get("v_references")).get(0));
			
			//Recupera termes del thesaurus relacionats.
			try{
				query = "id_node = "+oId.getIdentifier()+" and v_type = '"+oId.getTypeElement()+"'";
				//m = AccesBD.getJoin(elementDUC.getFieldsList("cur_thesaurus_etb"),new ArrayList(),"THE_TERMES","CUR_THESAURUS_ETB","id_terme","id_terme","",query,"","","",c);
				m = WSAccesBD.getJoin(elementDUC.getFieldsList("cur_thesaurus_etb"),new ArrayList(),"THE_TERMES","CUR_THESAURUS","id_terme","id_paraula","",query,"","","",c);
				TaxonPath taxonPath = new TaxonPath();
				taxonPath.setSource(new LangString("ETB",LANG));
				taxonPath.setDescription(new LangString("Thesaurus de ELR",LANG));
			
				for (int i=0;i<((ArrayList)m.get("id_terme")).size();i++){
					Taxon taxon = new Taxon();
					taxon.setId(String.valueOf(((ArrayList)m.get("id_terme")).get(i)));
					taxon.setEntry(new LangString((String) ((ArrayList)m.get("terme_ca")).get(i),LANG));
					taxonPath.getTaxonList().add(taxon);
				}
			
				elementDUC.setTaxonList(taxonPath);
			}catch(Exception e){
				logger.error("Error al carregar termes del thesaurus ->"+e);
				e.printStackTrace();
			}
			
			
			//Llistat de relacions.
			laux = new ArrayList();
			if (tipus == WSMerliBD.TIPUS_AREA){
				//relacio area/content	
				if (contents){
					try{
						lRec.clear();
						lRec.add(new Integer(oId.getIdentifier()));
						//laux.clear();		
						query ="ID_NODECUR_AREA = "+ oId.getIdentifier();
						m = WSAccesBD.getJoin(elementDUC.getFieldsList("id"),new ArrayList(),"cur_content","cur_content_area","ID_NODE","ID_NODE","",query,"","","",c);
						
						for (int i=0;i<((ArrayList)m.get("id_node")).size();i++){						
							ducRelation = new DUCRelation();
							ducRelation.setDUCRelationType("RAC");
							ducRelation.setElementDUC(getFullElementDUCold(c, Integer.parseInt(((ArrayList)m.get("id_node")).get(i).toString()),WSMerliBD.TIPUS_CONTENT, contents));
							laux.add(ducRelation);
						}
					}catch(Exception e){
						logger.error("Error al carregar les relacions area/content ->"+e);
						e.printStackTrace();
					}	
				}
			}
			
			if (tipus == WSMerliBD.TIPUS_CONTENT && contents){
				//relacio content/content	
				try{
					lRec.clear();
					lRec.add(new Integer(oId.getIdentifier()));
					//laux.clear();		
					m= WSAccesBD.getObject("cur_content",elementDUC.getFieldsList("id"),"id_nodecur_content=?",lRec,c);
					
					for (int i=0;i<((ArrayList)m.get("id_node")).size();i++){						
						ducRelation = new DUCRelation();
						ducRelation.setDUCRelationType("RCC");
						ducRelation.setElementDUC(getFullElementDUCold(c, Integer.parseInt(((ArrayList)m.get("id_node")).get(i).toString()),WSMerliBD.TIPUS_CONTENT, contents));
						laux.add(ducRelation);
					}
				}catch(Exception e){
					logger.error("Error al carregar les relacions content/content ->"+e);
					e.printStackTrace();
				}	
			}
			

			if (tipus == WSMerliBD.TIPUS_LEVEL){
				//relacio level/level	
				try{
					lRec.clear();
					lRec.add(new Integer(oId.getIdentifier()));
					//laux.clear();		
					m= WSAccesBD.getObject("cur_level",elementDUC.getFieldsList("id"),"id_nodecur_level=?",lRec,c);
					
					for (int i=0;i<((ArrayList)m.get("id_node")).size();i++){						
						ducRelation = new DUCRelation();
						ducRelation.setDUCRelationType("RLL");
						ducRelation.setElementDUC(getFullElementDUCold(c, Integer.parseInt(((ArrayList)m.get("id_node")).get(i).toString()),WSMerliBD.TIPUS_LEVEL,contents));
						laux.add(ducRelation);
					}
				}catch(Exception e){
					logger.error("Error al carregar les relacions level/level ->"+e);
					e.printStackTrace();
				}	
				
				//relacio level/area	
				try{
					lRec.clear();
					lRec.add(new Integer(oId.getIdentifier()));
					//laux.clear();		
					m= WSAccesBD.getObject("cur_area",elementDUC.getFieldsList("id"),"id_nodecur_level=?",lRec,c);
					
					for (int i=0;i<((ArrayList)m.get("id_node")).size();i++){						
						ducRelation = new DUCRelation();
						ducRelation.setDUCRelationType("RLA");
						ducRelation.setElementDUC(getFullElementDUCold(c, Integer.parseInt(((ArrayList)m.get("id_node")).get(i).toString()),WSMerliBD.TIPUS_AREA,contents));
						laux.add(ducRelation);
					}
				}catch(Exception e){
					logger.error("Error al carregar les relacions level/area ->"+e);
					e.printStackTrace();
				}

				//relacio level/content	
				if (contents){
				try{
					lRec.clear();
					lRec.add(new Integer(oId.getIdentifier()));
					//laux.clear();		
					query ="ID_NODECUR_LEVEL = "+ oId.getIdentifier();
					m = WSAccesBD.getJoin(elementDUC.getFieldsList("id"),new ArrayList(),"cur_content","cur_content_level","ID_NODE","ID_NODE","",query,"","","",c);
					
					for (int i=0;i<((ArrayList)m.get("id_node")).size();i++){						
						ducRelation = new DUCRelation();
						ducRelation.setDUCRelationType("RLC");
						ducRelation.setElementDUC(getFullElementDUCold(c, Integer.parseInt(((ArrayList)m.get("id_node")).get(i).toString()),WSMerliBD.TIPUS_CONTENT,contents));
						laux.add(ducRelation);
					}
				}catch(Exception e){
					logger.error("Error al carregar les relacions level/content ->"+e);
					e.printStackTrace();
				}
				}	
			}		
			
			elementDUC.setDUCRelationList(laux);
			
		}catch (Exception e) {
			logger.error("ws Error on getRecurs ->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_SQL);
		}finally{
			if (con==null){
				tancaConexio(c,cb);
			}
		}
		
		return elementDUC;
	}
	
	
	
	
	
	
	
	
	
	public ElementDUC getElementDUC(IdElement idElement) throws MerliDBException, SQLException, SemanticException {
		
		
		ElementDUC elementDUC = new ElementDUC();	
		ElementDUC elDUC;
		DUCRelation ducRelation;
		ArrayList lRec = new ArrayList();	
		ArrayList laux = new ArrayList();
		lRec.add(new Integer(idElement.getIdentifier()));
		String id_nodecur_content;
//		Connection c = null;
//		ConnectionBean cb = null;
//		Map m = null;
		String aux;
		String query;
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
				taxonPath.setSource(new LangString("ETB",LANG));
				taxonPath.setDescription(new LangString("Thesaurus de ELR",LANG));
				Taxon taxon;
				for (int i=0;i<lRec.size();i++){
					rt = (Relation) lRec.get(i);
					elDUC = new ElementDUC();
					elDUC.copyFromSemanticNet(si.getNode(rt.getIdDest(),rt.getDestType()));
					taxon = new Taxon();
					taxon.setId(String.valueOf(rt.getIdDest()));
					taxon.setEntry(new LangString(si.getNode(rt.getIdDest(),rt.getDestType()).getTerm(),LANG));
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
					elementDUC= getFullElementDUC(si,sId, WSMerliBD.TIPUS_LEVEL,contents);
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
		// TODO Auto-generated method stub
		
		ElementDUC elementDUC = new ElementDUC();	
		ElementDUC elDUC;
		DUCRelation ducRelation;
		ArrayList lRec = new ArrayList();	
		ArrayList laux = new ArrayList();	
		Hashtable lnode = new Hashtable();//ArrayList();	
		
		lRec.add(new Integer(idElement));
		String id_nodecur_content;
		String id_nodecur_level;
		String aux;
		String query;
		Node node;
		int intTipus;
		int direccio;
		int id;
		int lastPos;
		String tipusDesti;
		IdElement oId;
		Relation rt;
		try{
			
			if (si == null)
				si = new SemanticInterface();
			
			//Recuperar dades del node donat.
			switch(tipus){
			case TIPUS_LEVEL:
			
//				m= AccesBD.getObject("cur_level",elementDUC.getFieldsList("cur_level"),"id_node=?",lRec,c);
//				elementDUC.setCategory((String)((ArrayList)m.get("v_category")).get(0));
//				id_nodecur_level = String.valueOf(((ArrayList)m.get("id_nodecur_level")).get(0));
				aux = "level";
				break;
			case TIPUS_AREA:
//				m= AccesBD.getObject("cur_area",elementDUC.getFieldsList("cur_area"),"id_node=?",lRec,c);
//				id_nodecur_level = String.valueOf(((ArrayList)m.get("id_nodecur_level")).get(0));
				aux = "area";
				break;
			case TIPUS_CONTENT:
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
			oId = new IdElement(String.valueOf(idElement), aux);
				
			//Recupera termes del thesaurus relacionats.

				
			try{
				lRec = si.getRelations(idElement,aux,"RET",RelationType.SOURCE);
				TaxonPath taxonPath = new TaxonPath();
				taxonPath.setSource(new LangString("ETB",LANG));
				taxonPath.setDescription(new LangString("Thesaurus de ELR",LANG));
				Taxon taxon;
				for (int i=0;i<lRec.size();i++){
					rt = (Relation) lRec.get(i);
					elDUC = new ElementDUC();
					elDUC.copyFromSemanticNet(si.getNode(rt.getIdDest(),rt.getDestType()));
					taxon = new Taxon();
					taxon.setId(String.valueOf(rt.getIdDest()));
					taxon.setEntry(new LangString(si.getNode(rt.getIdDest(),rt.getDestType()).getTerm(),LANG));
					taxonPath.getTaxonList().add(taxon);				
				}			
				
				elementDUC.setTaxonList(taxonPath);
			}catch(Exception e){
				logger.error("Error al carregar termes del thesaurus ->"+e);
				e.printStackTrace();
			}
			
			
			
			//Llistat de relacions.
			laux = new ArrayList();
			if (tipus == WSMerliBD.TIPUS_AREA)
				//relacio area/content	
				if (contents){
					laux.add("RCA");					
				}
			
			if (tipus == WSMerliBD.TIPUS_CONTENT && contents){
				//relacio content/content	
				laux.add("RCC");
			}
			

			if (tipus == WSMerliBD.TIPUS_LEVEL){
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
			
			Hashtable ht;
			for(int j = 0;j<laux.size();j++){
				
				direccio = RelationType.SOURCE;
//				if (laux.get(j) == "RLL" || laux.get(j) == "RCC" )
					direccio = RelationType.DEST;
				ht = new Hashtable();
				lRec = si.getRelations(idElement,aux,(String) laux.get(j),direccio);
				for (int i=0;i<lRec.size();i++){
					rt = (Relation) lRec.get(i);

					id = rt.getIdDest();
					tipusDesti = rt.getDestType();
//					if (laux.get(j) == "RLL" || laux.get(j) == "RCC"){
						id = rt.getIdSource();
						tipusDesti = rt.getSourceType();
//					}
					
					if (tipusDesti == "area" ) intTipus = WSMerliBD.TIPUS_AREA;
					else if (tipusDesti == "content") intTipus = WSMerliBD.TIPUS_CONTENT;
					else intTipus = WSMerliBD.TIPUS_LEVEL;
					
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

	

	
	
	
	
	
	
	

}
