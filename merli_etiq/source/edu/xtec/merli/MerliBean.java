package edu.xtec.merli;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import cat.xtec.ws.proxies.correu.CorreuSender;
import cat.xtec.ws.proxies.correu.types.CorreuBody;
import cat.xtec.ws.proxies.correu.types.CorreuException;
import cat.xtec.ws.proxies.correu.types.CorreuResponse;
import cat.xtec.ws.proxies.correu.types.EnviamentResponse;
import edu.xtec.merli.agrega.AgregaInterface;
import edu.xtec.merli.basedades.AccesBD;
import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.basedades.RecursBD;
import edu.xtec.merli.gestorrec.InfoUsuari;
import edu.xtec.merli.mediateca.MediatecaBean;
import edu.xtec.merli.segur.User;
import edu.xtec.merli.segur.operations.MerliOperations;
import edu.xtec.merli.semanticnet.SemanticInterface;
import edu.xtec.merli.utils.Utility;
import edu.xtec.semanticnet.Node;
import edu.xtec.semanticnet.Relation;
import edu.xtec.semanticnet.RelationType;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

public class MerliBean {
	private static final String APPLICATION_RESOURCES = "ApplicationResources";

	protected static ConnectionBeanProvider broker;

	public static final int LLISTA_M_TOTS2 = 0;
	public static final int LLISTA_M_PROPIS = 1;
	public static final int LLISTA_M_PENDENTS = 2;
	public static final int LLISTA_M_REALITZATS = 3;
	public static final int LLISTA_M_REALITZATS_VALIDAR = 4;
	public static final int LLISTA_M_PUBLICATS = 5;
	public static final int LLISTA_M_EN_PROCES = 6;
	public static final int LLISTA_M_NO_ACCEPTAT = 7;
	public static final int LLISTA_M_TRADUCT_CA = 8;
	public static final int LLISTA_M_TRADUCT_ES = 9;
	public static final int LLISTA_M_TRADUCT_EN = 10;
	public static final int LLISTA_M_TRADUCT_OC = 11;
	public static final int LLISTA_M_CERCADOR = 12;
	public static final int LLISTA_M_AGREGA_PEND = 13;
	public static final int LLISTA_M_AGREGA_FET = 14;
	public static final int LLISTA_M_RETORNADES = 15;
	public static final int LLISTA_M_DENEGADES = 16;
		
	public static final int ESTAT_M_DENEGAT = -2;
	public static final int ESTAT_M_RETORNAT = -1;
	public static final int ESTAT_M_PENDENT = 10;
	public static final int ESTAT_M_EN_PROCES = 0;
	public static final int ESTAT_M_REALITZAT = 2;
	public static final int ESTAT_M_PUBLICAT = 4;
	
	public static final int RECURS_ONLINE = 1;
	public static final int RECURS_FISIC = 2;
	
	public static final int OPERACIO_CERCADOR = 1;
	public static final int OPERACIO_EXPORTACIO = 2;
	public static final int OPERACIO_COUNT = 3;
	
//	public static final int ESTAT_DENEGAT = -2;
//	public static final int ESTAT_RETORNAT = -1;
//	public static final int ESTAT_ESBORRANY = 0;

//	public static final int ESTAT_PER_ASSIGNAR = 1;
//	public static final int ESTAT_EN_REVISIO = 2;
//	public static final int ESTAT_PER_CORRECIO = 3;
//	public static final int ESTAT_ACCEPTAT = 4;
		
	public static final int RECURSOS_PAGINA=25;
	
	private static final Logger logger = Logger.getRootLogger();

	public Locale locale;
	public MessageResources messages;

	/* ADDED - Nadim --> Have to update when deploying new version --> 25/11/2015 */
    public static String versionControl = "v2.1.0";

	/**
	 * Sol¬∑licita un ConnectionBean al ConnectionBeanProvider, si aquest no est√† innicialitzat, ho fa.
	 * @return ConnectionBean, servit pel ConnectionBeanProvider.
	 * @throws MerliDBException
	 */
	private ConnectionBean connectBD() throws MerliDBException{
		//Inicialitza el CBP si no ho est√†.
		ConnectionBean bd = null;

		try{
			if (broker == null){
				//broker = ConnectionBeanProvider.getConnectionBeanProvider(true, Utility.loadProperties("../../../../","database.properties"));
				broker = ConnectionBeanProvider.getConnectionBeanProvider(true, Utility.loadProperties("/","database.properties"));
			}
			bd = broker.getConnectionBean();			
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
	
	public ArrayList executeOperation(String id, String property) throws MerliDBException {
		ArrayList l = new ArrayList();
		//l = this.getResult(property, id);
		return l;
	}

	private boolean calLlistar(int idRec, int tipus, Connection c) throws MerliDBException {
		//Connection c=null;
		ArrayList lParam = new ArrayList();
		int estaTraduit =0;
		try {
			lParam.add(new Integer(idRec));
			switch (tipus){
				case LLISTA_M_TRADUCT_CA: lParam.add("ca"); break;
				case LLISTA_M_TRADUCT_ES: lParam.add("es"); break;
				case LLISTA_M_TRADUCT_EN: lParam.add("en"); break;
				case LLISTA_M_TRADUCT_OC: lParam.add("oc"); break;
				default: return true;
			}
			
			lParam.add(new Integer(1));
			estaTraduit = AccesBD.executeCount("mer_rec_lang", "id_rec = ? AND lang= ? AND estat=?",lParam,c);			
		} catch (NullPointerException ne){
			logger.error("Can't count Recursos->"+ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			logger.error("Can't count Recursos->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		} finally{		
		}
		
		return (estaTraduit<1);
	}

	public int getNumRecursosInicial(int tipus,User user,int id, String cerca) throws MerliDBException 
	{
		int est,propis=3;
		
		if(user.hasPermission(MerliOperations.RECADD) || user.hasPermission(MerliOperations.RECSET)) est=MerliBean.LLISTA_M_EN_PROCES;
		else if(user.hasPermissionPublicarTots()) est=MerliBean.LLISTA_M_REALITZATS_VALIDAR;
		else est=MerliBean.LLISTA_M_PUBLICATS;
		if(user.hasPermission(MerliOperations.RECFISIC)&&user.getUnitat()!=null) propis=2;							//nomes mostrem els recursos de la unitat
		else if(user.hasPermission(MerliOperations.RECADD) || user.hasPermission(MerliOperations.RECSET)) propis=1;	//nomes mostrem els recursos de l'usuari

		return getNumRecursosAdmin( user, id, cerca, est, "", new Date(0), new Date(0), 0, "", "", propis, "");
	}
	
	public int getNumRecursosAdmin(User user,int id, String cerca, int estat, String estatsDisponibles, Date data_i, Date data_f, int fisic, String id_unitat, String id_catalogador, int propis, String descripcio) throws MerliDBException {	
		ConnectionBean cb = null;
		Connection c;
		int numRec=0;
		//String sCamps="count(distinct id_rec)";
		ArrayList lelem = new ArrayList();
		lelem.add("id_rec");
		lelem.add("titol");
		lelem.add("descripcio");
		lelem.add("url");
		//lelem.add("versio");lelem.add("edat_min");lelem.add("edat_max");lelem.add("id_dificultat");lelem.add("duracio");
		//lelem.add("drets");lelem.add("id_rol_usuari");lelem.add("confirmat");	lelem.add("id_ambit"); 
		lelem.add("data_etiq"); 
		lelem.add("id_estat");
		lelem.add("v_responsable");
		//lelem.add("etiquetat");	lelem.add("data_publi"); lelem.add("publicat");
		lelem.add("id_unitat_creadora");
		String sCamps = Utility.toListDB(lelem,"v");
		
		try {
			cb = connectBD();
			c = cb.getConnection();
			String query=construeixQuery(true,sCamps, user, 0, id, cerca, estat, estatsDisponibles, data_i, data_f, fisic, id_unitat, id_catalogador, propis, descripcio, "");
			logger.debug("obtenint nombre de registres per a paginar. Query: "+query);
			//System.out.println("num: "+query);
			
			Statement stmt=null;
			ResultSet rs = null;		
			try{
				stmt=c.createStatement();
				rs = stmt.executeQuery(query);
				while (rs.next()) {
					numRec = rs.getInt(1);
				}
				rs.close();
				stmt.close();
			}catch (SQLException s){
				try{
					rs.close();
					stmt.close();
				}catch(SQLException e1){
					  logger.warn("Error connecting to DDBB:"+e1.getMessage());
					  throw e1;
				}
				throw s;
			}
			c.close();
			disconnectBD(cb);
		}
		catch (Exception s){}
		
		return numRec;
	}
	
	public ArrayList getLlistatRecursosSemblants(String titol, String url, String ids, String idRecurs)
			throws MerliDBException {
		Map m;
		RecursMerli r = null;
		ArrayList lelem= new ArrayList(),lres = new ArrayList();
		ConnectionBean cb = null;
		Connection c;

		lelem.add("id_rec");
		lelem.add("titol");
		lelem.add("data_etiq"); 
		lelem.add("id_estat");
		lelem.add("v_responsable");
		String sCamps = Utility.toListDB(lelem,"v");
		
		try
		{
			cb = connectBD();
			c = cb.getConnection();

			String query = construeixQueryComprova(sCamps, titol, url, ids, idRecurs);
			logger.debug("creant llistat amb el resultat de la consulta. Query: "+query);
			//System.out.println("comprova: "+query);
			Statement stmt=null;
			ResultSet rs = null;
			m = new LinkedHashMap();
			try
			{
				stmt=c.createStatement();
				rs = stmt.executeQuery(query);		
				m = Utility.toMap(rs,lelem);
				rs.close();
				stmt.close();
			}
			catch (SQLException s)
			{
				try
				{
					rs.close();
					stmt.close();
				}
				catch(SQLException e1)
				{
					logger.warn("Error connecting to DDBB:"+e1.getMessage());
					throw e1;
				}
				throw s;
			} finally {
				try {
					c.close();
					disconnectBD(cb);
				} catch (Exception e) {
				}
			}
			
			for (int i = 0;i<((ArrayList)m.get("id_rec")).size();i++)
			{					
				try
				{
					String sTitol="", sEstat="", sResponsable="",sData=""; 
					if(((ArrayList)m.get("titol")).get(i)!=null) 		sTitol=((ArrayList)m.get("titol")).get(i).toString();
					if(((ArrayList)m.get("id_estat")).get(i)!=null) 	sEstat=((ArrayList)m.get("id_estat")).get(i).toString();
					if(((ArrayList)m.get("v_responsable")).get(i)!=null)sResponsable=((ArrayList)m.get("v_responsable")).get(i).toString();
					if(((ArrayList)m.get("data_etiq")).get(i)!=null)	sData=((ArrayList)m.get("data_etiq")).get(i).toString();
						
					r = new RecursMerli(((BigDecimal)((ArrayList)m.get("id_rec")).get(i)).intValue());
					r.setTitle(sTitol);
					r.setEstat(sEstat);
					r.setResponsable(sResponsable);	
					MerliContribution contr = new MerliContribution(MerliContribution.ETIQUETADOR,sResponsable,sData);
					ArrayList cs = new ArrayList();
					cs.add(contr);
					r.setContribution(cs);
					
					lres.add(r);
				}
				catch(Exception e){
					logger.error("Error al carrega un recurs del llistat: idRec="+r.getIdRecurs());
				}
			}
		}
		catch (NullPointerException ne){
			disconnectBD(cb);
			logger.warn("Can't create FullLlistat of Recursos->"+ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Can't create Recursos->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}

		return lres;

	}
	
	/**
	 * El llistat que es mostra inicalment depen dels permisos que te l'usuari
	 * en cas que un usuari tingui permisos de mes d'un rol, la jerarquia es catalogador, publicador, traductor, pendents i altres
	 * si un usuari es catalogador veura els seus esborranys
	 * si es publicador veura totes les fitxes realitzades (pendents de revisar per un publicador)
	 * si es traductor veura totes les fitxes publicades
	 * si pot editar pendents veura totes les fitxes pendents
	 * si no te cap d'aquests permisos veura totes les publicades
	 * si es modifica alguna cosa d'aquesta jerarquia cal modificar tambe el que surt seleccionat per defecte en els desplegables 'selestat' i 'despl' de gesrec.jsp
	 * @param tipus
	 * @param user
	 * @param pagina
	 * @param id
	 * @param cerca
	 * @return
	 * @throws MerliDBException
	 */
	public ArrayList getLlistatRecursosInicial(int tipus,User user,int pagina,int id,String cerca) throws MerliDBException
	{
		int est, propis=3;
		
		if(user.hasPermission(MerliOperations.RECADD) || user.hasPermission(MerliOperations.RECSET))
		{
			est=MerliBean.LLISTA_M_EN_PROCES;
			if(user.hasPermission(MerliOperations.RECFISIC)&&user.getUnitat()!=null) propis=2;	//nomes mostrem els recursos de la unitat
			else propis=1;																		//nomes mostrem els recursos de l'usuari
		}
		else if(user.hasPermissionPublicarTots()) est=MerliBean.LLISTA_M_REALITZATS_VALIDAR;
		else if(user.hasPermission(MerliOperations.TRADUCT))est=MerliBean.LLISTA_M_PUBLICATS;
		else if(user.hasPermission(MerliOperations.PENDEDIT)) est=MerliBean.ESTAT_M_PENDENT;
		else est=MerliBean.LLISTA_M_PUBLICATS;

		return getLlistatRecursosAdmin(tipus, user, pagina, id, cerca, est, "", new Date(0), new Date(0), 0, "", "", propis, "", "");
	}
	
	/**
	 * 
	 * @param tipus			--> tipus de llista (nomes util per traduccions); indefinit: qualsevol fora del rang 8-11
	 * @param user			--> usuari que fa la consulta; obligatori
	 * @param pagina		--> pagina que es vol visualitzar; indefinit: -1
	 * @param id			--> id del recurs a cercar, indefinit:0
	 * @param cerca			--> text a cercar en el titol; indefinit: ""
	 * @param estat			--> estat dels recursos; obligatori
	 * @param data_i		--> recursos catalogats anteriorment a la data; indefinit: new Date(0)
	 * @param data_f		--> recursos catalogats posteriorment a la data; indefinit: new Date(0)
	 * @param fisic			--> recursos online o fisics; indefinit: diferent a 1 (en_linia) i 2 (fisic)
	 * @param id_unitat		--> unitat creadora dels recursos; indefinit: ""
	 * @param id_catalogador--> id del catalogador del recurs; indefinit: ""
	 * @param propis		--> recursos propis o de la unitat; indefinit: diferent a 1 (propis) i 2 (de la unitat)
	 * @param descripcio	--> text a cercar en la descripcio; indefinit: ""
	 * @param ordenacio		--> criteri d'ordenacio "D" (data descendent), "A" (alfabËtic ascendent) o "I" (identificador ascendent); indefinit: diferent a "A", "D" i "I" (en aquest cas s'ordenara per data, de nou a vell)
	 * @return				--> un arraylist de recursos complerts
	 * @throws MerliDBException
	 */
	public ArrayList getLlistatRecursosAdmin(int tipus,User user,int pagina,int id,String cerca, int estat, String estatsDisponibles,Date data_i, Date data_f, int fisic, String id_unitat, String id_catalogador, int propis, String descripcio, String ordenacio) throws MerliDBException {
		Map m;
		ArrayList lelem= new ArrayList(),lres = new ArrayList();
		ConnectionBean cb = null;
		Connection c = null;

		lelem.add("id_rec");
		lelem.add("titol");
		lelem.add("descripcio");
		lelem.add("url");
		//lelem.add("versio");lelem.add("edat_min");lelem.add("edat_max");lelem.add("id_dificultat");lelem.add("duracio");
		//lelem.add("drets");lelem.add("id_rol_usuari");lelem.add("confirmat");	lelem.add("id_ambit"); 
		lelem.add("data_etiq"); 
		lelem.add("id_estat");
		lelem.add("etiquetat");
		lelem.add("validador");
		lelem.add("traductor");
		//lelem.add("etiquetat");	lelem.add("data_publi"); lelem.add("publicat");
		lelem.add("id_unitat_creadora");
		String sCamps = Utility.toListDB(lelem,"v");
		
		try {
			cb = connectBD();
			c = cb.getConnection();

			String query = construeixQuery(false, sCamps, user, pagina, id, cerca, estat, estatsDisponibles, data_i, data_f, fisic, id_unitat, id_catalogador, propis, descripcio, ordenacio);
			logger.debug("creant llistat amb el resultat de la consulta. Query: "+query);
			//System.out.println("query: "+query);
			Statement stmt=null;
			ResultSet rs = null;		
			m = new LinkedHashMap();
			try{
				stmt=c.createStatement();
				rs = stmt.executeQuery(query);		
				m = Utility.toMap(rs,lelem);
				rs.close();
				stmt.close();
			}catch (SQLException s){
				try{
					rs.close();
					stmt.close();
				}catch(SQLException e1){
					logger.warn("Error connecting to DDBB:"+e1.getMessage());
					throw e1;
				}
				throw s;
			}
			
			for (int i = 0;i<((ArrayList)m.get("id_rec")).size();i++)
			{
				//Si es una exportacio recupero tota la informaciÛ del recurs. Sino, recupero nomes la informacio necessaria
				if(tipus==OPERACIO_EXPORTACIO)
					lres.add(getRecurs(((BigDecimal)((ArrayList)m.get("id_rec")).get(i)).intValue()));
				else
					lres.add(getInfoMinima(m,i));
			}
		}catch (NullPointerException ne){
			disconnectBD(cb);
			logger.warn("Can't create FullLlistat of Recursos->"+ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Can't create Recursos->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		} finally {
			try {
				c.close();
				disconnectBD(cb);
			} catch (Exception e) {
			}
		}

		return lres;

	}
	
	public ArrayList getLlistatRecursosExportacio(int tipus,User user,int id,String cerca, int estat, String estatsDisponibles,Date data_i, Date data_f, int fisic, String id_unitat, String id_catalogador, int propis, String descripcio, String ordenacio) throws MerliDBException {
		Map m;
		ArrayList lelem= new ArrayList(),lres = new ArrayList();
		ConnectionBean cb = null;
		Connection c = null;
		
		lelem.add("id_rec");
		String sCamps = Utility.toListDB(lelem,"v");
		
		try {
			cb = connectBD();
			c = cb.getConnection();
			
			String query = construeixQueryExportacio(user,id,cerca, estat, estatsDisponibles, data_i, data_f, fisic, id_unitat, id_catalogador, propis, descripcio);
			logger.debug("creant llistat per l'exportacio. Query: "+query);
			//System.out.println("query exp: "+query);
			RecursBD rb= new RecursBD();
			lres = rb.getRecursosExportacio(query);
		}catch (NullPointerException ne){
			disconnectBD(cb);
			logger.warn("Can't create FullLlistat of Recursos->"+ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Can't create Recursos->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		} finally {
			try {
				c.close();
				disconnectBD(cb);
			} catch (Exception e) {
			}
		}

		return lres;

	}	


	private RecursMerli getInfoMinima(Map m, int i)
	{
		RecursMerli r = null;
		ArrayList l = new ArrayList();
		try{
			String sTitol="", sDescripcio="", sUrl="", sEstat="", sEtiquetador="", sValidador="", sTraductor="", sData="", sUnitat=""; 
			SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");						
			
			if(((ArrayList)m.get("titol")).get(i)!=null) 		sTitol=((ArrayList)m.get("titol")).get(i).toString();
			if(((ArrayList)m.get("descripcio")).get(i)!=null) 	sDescripcio=((ArrayList)m.get("descripcio")).get(i).toString();
			if(((ArrayList)m.get("url")).get(i)!=null) 			sUrl=((ArrayList)m.get("url")).get(i).toString();
			if(((ArrayList)m.get("id_estat")).get(i)!=null) 	sEstat=((ArrayList)m.get("id_estat")).get(i).toString();
			if(((ArrayList)m.get("etiquetat")).get(i)!=null)	sEtiquetador=((ArrayList)m.get("etiquetat")).get(i).toString();
			if(((ArrayList)m.get("data_etiq")).get(i)!=null)	sData = sDate.format(((ArrayList) m.get("data_etiq")).get(i));
			if(((ArrayList)m.get("validador")).get(i)!=null)	sValidador=((ArrayList)m.get("validador")).get(i).toString();
			if(((ArrayList)m.get("traductor")).get(i)!=null)	sTraductor=((ArrayList)m.get("traductor")).get(i).toString();
			if(((ArrayList)m.get("id_unitat_creadora")).get(i)!=null)sUnitat=((ArrayList)m.get("id_unitat_creadora")).get(i).toString();

			r = new RecursMerli(((BigDecimal)((ArrayList)m.get("id_rec")).get(i)).intValue());
			r.setTitle(sTitol);
			r.setDescription(sDescripcio);
			r.setUrl(sUrl);
			r.setEstat(sEstat);
			r.setResponsable(sEtiquetador);	
			r.setUnitatCreadora(sUnitat);
			
			l = new ArrayList();
			MerliContribution contr = new MerliContribution(MerliContribution.ETIQUETADOR,sEtiquetador,sData);
			l.add(contr);
			if(!sValidador.equals(""))
			{
				contr = new MerliContribution(MerliContribution.VALIDADOR,sValidador,new Date(1,1,1));
				l.add(contr);
			}
			if(!sTraductor.equals(""))
			{
				contr = new MerliContribution(MerliContribution.TRADUCTOR,sTraductor,new Date(1,1,1));
				l.add(contr);
			}
			r.setContribution(l);
		}catch(Exception e){
			logger.error("Error al carrega un recurs del llistat: idRec="+r.getIdRecurs());
		}
		return r;
	}

	/**
	 * construeix la part de la condicio de la query corresponent a l'id', el text 'cerca', les dates d'inici i fi, si el recurs es fisic o no, la unitat, el catalogador i la descripcio
	 * @param id
	 * @param cerca
	 * @param data_i
	 * @param data_f
	 * @param fisic
	 * @param id_unitat
	 * @param id_catalogador
	 * @param descripcio
	 * @return
	 */
	private String consAltres(int id, String cerca, Date data_i, Date data_f, int fisic, String id_unitat, String id_catalogador, int propis, String descripcio, User user) {
		String cons="";
		if (id != 0)	cons+="id_rec = "+id+" AND ";
		if (cerca != null && !cerca.equals(""))
			cons += "titol_pla like '" + Utility.aplanarText("%" + cerca + "%") + "' AND ";
		DateFormat formatter=new SimpleDateFormat("dd-MM-yyyy");
		if(!data_i.equals(new Date(0)))
		{
			try
			{
				String sData=formatter.format(data_i);					
				cons+="data_etiq >= to_date('"+sData+"','DD-MM-YYYY') AND ";
			}
			catch (Exception e)
			{
				logger.warn("Error converting initial date, format must be yyyy-mm-dd. "+e.getMessage());
			}
		}
		if(!data_f.equals(new Date(0)))
		{
			try
			{
				String sData=formatter.format(data_f);
				cons+="data_etiq <= to_date('"+sData+"','DD-MM-YYYY') AND ";
			}
			catch (Exception e)
			{
				logger.warn("Error converting final date, format must be yyyy-mm-dd. "+e.getMessage());
			}
		}
		if(fisic==MerliBean.RECURS_ONLINE)
			cons+="NOT EXISTS (SELECT * FROM mer_recurs_fisic f2 WHERE v.id_rec=f2.id_recurs) AND ";
		else if(fisic==MerliBean.RECURS_FISIC)
			cons+="EXISTS (SELECT * FROM mer_recurs_fisic f2 WHERE v.id_rec=f2.id_recurs) AND ";
		if(id_unitat!=null && !id_unitat.equals(""))
			cons+=" EXISTS (SELECT * FROM mer_rec_disp_uni d WHERE v.id_rec=d.id_rec AND d.id_unitat="+id_unitat+") AND ";
		if(id_catalogador!=null && !id_catalogador.equals(""))			
			cons+=" etiquetat = '"+id_catalogador+"' AND ";		
		if(id_unitat!=null && !id_unitat.equals(""))
			cons+=" EXISTS (SELECT * FROM mer_rec_disp_uni d WHERE v.id_rec=d.id_rec AND d.id_unitat="+id_unitat+") AND ";
		if(propis==1 && user.getUser()!=null)			
			cons+=" etiquetat = '"+user.getUser()+"' AND ";
		if(propis==2 && user.getUnitat()!=null)
			cons+=" EXISTS (SELECT * FROM mer_rec_disp_uni d WHERE v.id_rec=d.id_rec AND d.id_unitat="+user.getUnitat()+") AND ";
		if (descripcio != null && !descripcio.equals(""))
			cons += "desc_pla like '"+ Utility.aplanarText("%" + descripcio + "%") + "' AND ";
		
		return cons;
	}
	
	/**
	 * construeix la part de la condicio de la query en funcio de l'estat'
	 * @param estat
	 * @return
	 */
	private String consFiltre(int estat) {
		String cons="";
		switch (estat)
		{			
			case MerliBean.LLISTA_M_PENDENTS:
				cons+="id_estat = "+MerliBean.ESTAT_M_PENDENT+" AND "; break;
			case MerliBean.LLISTA_M_REALITZATS_VALIDAR:
				cons+="id_estat = "+MerliBean.ESTAT_M_REALITZAT+" AND "; break;
			case MerliBean.LLISTA_M_TRADUCT_CA:
			case MerliBean.LLISTA_M_TRADUCT_ES:
			case MerliBean.LLISTA_M_TRADUCT_OC:
			case MerliBean.LLISTA_M_TRADUCT_EN:
				String lleng=getLlengua(estat);
				cons+="id_estat = "+MerliBean.ESTAT_M_PUBLICAT
						+" AND NOT EXISTS(SELECT * FROM mer_rec_lang l WHERE v.id_rec=l.id_rec and lang='"+lleng+"' and estat=1) AND ";
				break;
			case MerliBean.LLISTA_M_AGREGA_PEND:
				cons+="id_rec IN (select id_rec from mer_rec_agrega where send=1 AND date_agrega is null) AND ";break;
			case MerliBean.LLISTA_M_AGREGA_FET:
				cons+="id_rec IN (select id_rec from mer_rec_agrega where send=1 AND date_agrega is not null) AND ";break;
			case MerliBean.LLISTA_M_REALITZATS:
				cons+="id_estat = "+MerliBean.ESTAT_M_REALITZAT+" AND "; break;			
			case MerliBean.LLISTA_M_PUBLICATS:
				cons+="id_estat = "+MerliBean.ESTAT_M_PUBLICAT+" AND "; break;
			case MerliBean.LLISTA_M_EN_PROCES:
				cons+="id_estat = "+MerliBean.ESTAT_M_EN_PROCES+" AND "; break;
			case MerliBean.LLISTA_M_NO_ACCEPTAT:
				cons+="(id_estat = "+MerliBean.ESTAT_M_DENEGAT+" OR id_estat = "+MerliBean.ESTAT_M_RETORNAT+") AND "; break;
			case MerliBean.LLISTA_M_RETORNADES:
				cons+="id_estat = "+MerliBean.ESTAT_M_RETORNAT+" AND "; break;
			case MerliBean.LLISTA_M_DENEGADES:
				cons+="id_estat = "+MerliBean.ESTAT_M_DENEGAT+" AND "; break;
			default:break;
		}
		return cons;
	}
	
	/**
	 * construeix la part de la condicio de la query corresponent al propietari
	 * en els estats 'en proces', 'retornat' i 'denegat' filtrem per usuari
	 * en l'estat 'realitzats' si no som publicadors filtrem per usuari
	 * en els estats 'publicats' i 'pendents' no filtrem
	 * si 'user' es superusuari tampoc filtrem
	 * @param user
	 * @return
	 */
	private String consPersonals(int estat, User user)
	{
		String cons="";
		
		//els estats 'en proces', 'retornat' i 'denegat' nomes tenen sentit com a catalogador
		//en aquests estats l'usuari nomes pot veure aquelles fitxes que li pertanyin
		//en el cas de fisics entenem que una fitxa pertany a un usuari quan es seva o ha estat creada a la seva propia unitat
		//en l'estat 'realitzats' els catalogadors poden veure nomes les que li pertanyen pero els publicadors poden veure-les totes
		//en l'estat 'publicats' tothom pot veure totes les fitxes
		//en l'estat 'pendents' tothom pot veure totes les fitxes
		//si es superusuari pot veure totes les fitxes
		
		if(!user.isSuperuser())
			switch (estat)
			{
				case MerliBean.LLISTA_M_RETORNADES:
				case MerliBean.LLISTA_M_DENEGADES:
				case MerliBean.LLISTA_M_EN_PROCES:
				case MerliBean.LLISTA_M_NO_ACCEPTAT:				//filtrem per usuari
					cons+=" (etiquetat = '"+user.getUser()+"' ";
					if(user.hasPermission(MerliOperations.RECFISIC) && user.getUnitat()!=null)
						cons+=" OR EXISTS (SELECT * FROM mer_recurs_fisic d WHERE v.id_rec=d.id_recurs AND d.id_unitat_creadora="+user.getUnitat()+") ";
					cons+=") AND ";
					break;
				case MerliBean.LLISTA_M_REALITZATS_VALIDAR:	
				case MerliBean.LLISTA_M_REALITZATS:					//si no som publicadors filtrem per usuari
					if(!user.hasPermissionPublicarTots())
					{
						cons+=" (etiquetat = '"+user.getUser()+"' ";
						if(user.hasPermission(MerliOperations.RECFISIC) && user.getUnitat()!=null)
							cons+=" OR EXISTS (SELECT * FROM mer_recurs_fisic d WHERE v.id_rec=d.id_recurs AND d.id_unitat_creadora="+user.getUnitat()+") ";
						cons+=") AND ";
					}
					break;
				case MerliBean.LLISTA_M_PUBLICATS:					//no filtrem
				case MerliBean.LLISTA_M_PENDENTS:
				case MerliBean.LLISTA_M_TRADUCT_CA:
				case MerliBean.LLISTA_M_TRADUCT_ES:
				case MerliBean.LLISTA_M_TRADUCT_OC:
				case MerliBean.LLISTA_M_TRADUCT_EN:
				case MerliBean.LLISTA_M_AGREGA_PEND:
				case MerliBean.LLISTA_M_AGREGA_FET:
				default:break;
			}
		
		return cons;
	}

	/**
	 * retorna l'identificador de la llengua a la que correspon l'estat, si no correspon a cap estat "per traduir", retorna ca
	 * @param estat
	 * @return
	 */
	private String getLlengua(int estat) {
		String lleng="ca";
		if(estat==MerliBean.LLISTA_M_TRADUCT_ES) lleng="es";
		else if(estat==MerliBean.LLISTA_M_TRADUCT_EN) lleng="en";
		else if(estat==MerliBean.LLISTA_M_TRADUCT_OC) lleng="oc";
		return lleng;
	}


	/**
	 * si no 'num', modifica la 'query' pq nomes retorni els elements que correspondrien a la 'pagina' (paginant de RECURSOS_PAGINA en RECURSOS_PAGINA)
	 * @param num
	 * @param pagina
	 * @param query
	 * @return
	 */
	private String paginacio(boolean num, int pagina, String query) {
		if(!num && pagina!=-1) //si es -1, es que estem fent una exportacio i els volem tots
		{
			int min_row = (pagina-1) * RECURSOS_PAGINA+1;
			int max_row = pagina * RECURSOS_PAGINA;
			String inipag="select *  from ( select a.*, ROWNUM rnum from (";
			String fipag=") a where ROWNUM <= "+max_row+") where rnum  >= "+min_row;
			query=inipag+query+fipag;
		}
		return query;
	}
	
	/**
	 * si no 'num', modifica la 'query' pq retorni els elements ordenats segons el criteri 'ordenacio'
	 * @param num
	 * @param ordenacio
	 * @param query
	 * @return
	 */
	private String ordenacio(boolean num, String ordenacio, String query) {
		if(!num)
		{
			String criteri;
			if(ordenacio.equals("A")) criteri="ltrim(v.titol) asc";
			else if(ordenacio.equals("I")) criteri="v.id_rec asc";
			else criteri="v.data_etiq desc";
			query=query+" order by "+criteri;
		}
		return query;
	}
	
	/**
	 * Construeix una query a la vista v_mer_list_etiq amb les restrccions donades pels parametres d'entrada
	 * @param num			--> indica si la query es per aconseguir el numero de registres o el llistat; obligatori
	 * @param sCamps		--> camps a consultar; obligatori
	 * @param user			--> usuari que fa la consulta; obligatori
	 * @param pagina		--> pagina que es vol visualitzar; indefinit: -1
	 * @param id			--> id del recurs a cercar, indefinit:0
	 * @param cerca			--> text a cercar en el titol; indefinit: ""
	 * @param estat			--> estat dels recursos; obligatori
	 * @param estatsDisponibles	--> llista d'estats que l'usuari te permisos per veure (nomes aplica si estat=TOTS); indefinit=""
	 * @param data_i		--> recursos catalogats anteriorment a la data; indefinit: new Date(0)
	 * @param data_f		--> recursos catalogats posteriorment a la data; indefinit: new Date(0)
	 * @param fisic			--> recursos online o fisics; indefinit: diferent a 1 (en_linia) i 2 (fisic)
	 * @param id_unitat		--> unitat creadors dels recursos; indefinit: ""
	 * @param id_catalogador--> id del catalogador del recurs; indefinit: ""
	 * @param propis		--> recursos propis o de la unitat; indefinit: diferent a 1 (propis) i 2 (de la unitat)
	 * @param descripcio	--> text a cercar en la descripcio; indefinit: ""
	 * @param ordenacio		--> criteri d'ordenacio; indefinit: "" (si es !num, s'ordenara per data descendent)
	 * @return
	 */
	private String construeixQuery(boolean num, String sCamps, User user,int pagina,int id,String cerca, int estat, String estatsDisponibles, Date data_i, Date data_f, int fisic, String id_unitat, String id_catalogador, int propis, String descripcio, String ordenacio)
	{
		String query, cons=" WHERE ";
		if(!user.hasPermission(MerliOperations.RECFISIC)) fisic=MerliBean.RECURS_ONLINE;//si l'usuari no te el permis fisics, limito la cerca als online
		
		if(estat==MerliBean.LLISTA_M_TOTS2)
		{
			String[] estats=estatsDisponibles.split(","); 
			cons+="(";
			for(int i=0;i<estats.length;i++)
			{
				try
				{
					int e=Integer.parseInt(estats[i]);
					if(e<MerliBean.LLISTA_M_TRADUCT_CA || e>MerliBean.LLISTA_M_TRADUCT_OC)	//evito els estats de traduccio pq estan inclosos a Publicades
					{
						cons+="(";
						cons+=consFiltre(e);
						cons+=consPersonals(e, user);
						cons=cons.substring(0, cons.length()-4);//li trec la ˙ltima AND
						cons+=")";
						cons+=" OR ";
					}
				}
				catch(Exception e){}
			}
			cons=cons.substring(0, cons.length()-4);//li trec la ˙ltima OR
			cons+=") AND ";
		}
		else
		{
			cons+=consFiltre(estat);
			cons+=consPersonals(estat, user);
		}
		cons+=consAltres(id, cerca, data_i,data_f,fisic,id_unitat,id_catalogador, propis, descripcio, user);
				
		query = "SELECT " + sCamps + " FROM v_mer_list_etiq v" + cons;
		
		if(cons.length()>7) query=query.substring(0, query.length()-4);	//trec l'ultim "AND "
		else query=query.substring(0, query.length()-7);				//no hem afegit cap condicio, per tant borrem el WHERE
		
		query=ordenacio(num, ordenacio, query);
		query=paginacio(num, pagina, query);
		query=comptar(num, query);
		
		return query;
	}
	
	private String comptar(boolean num, String query) {
		if(num)	query="select count(*) from ("+query+")";
		return query;
	}

	private String construeixQueryComprova(String sCamps, String titol, String url, String ids, String idRecurs)
	{
		String query, cons=" WHERE ";
		if (idRecurs != null && idRecurs.length() > 1)
			cons += "(ID_REC <> " + idRecurs + ") AND (";
		if(titol!=null && titol.length()>1)
			cons += "(titol_pla like '" + Utility.aplanarText("%" + titol + "%") + "') OR ";
		if(url!=null && url.length()>2)
		{
			url=url.replaceAll("index.html", "%");
			url=url.replaceAll("index.htm", "%");
			url=url.replaceAll("html", "%");
			url=url.replaceAll("htm", "%");
			url=url.replaceAll("http://", "%");
			if(url.charAt(url.length()-1)=='/') url.subSequence(0, url.length()-2);
			cons+="(upper(url) like '%"+url.toUpperCase()+"%') OR ";
		}
		if(ids!=null && !ids.equals(""))
		{
			String[] trocets=ids.split(";");
			for(int i=0;i<trocets.length;i+=2)
			{
				cons+="exists(select * from mer_idfisic i where v.ID_REC=i.ID_REC and i.V_TIPUS='"+trocets[i]+"' and ";
				cons+="(TRANSLATE(upper(i.V_VALOR),'¡¿¬ƒ»… ÀÕÃŒœ“”‘÷⁄Ÿ€‹« ','AAAAEEEEIIIIOOOOUUUUC_') like '"+Utility.aplanarText("%"+trocets[i+1]+"%")+"')) OR ";
			}
		}
				
		query = "SELECT " + sCamps + " FROM v_mer_list_etiq v" + cons;
		
		if(cons.length()>7)
		{
			query=query.substring(0, query.length()-3);	//trec l'ultim "OR "
			if (idRecurs != null && idRecurs.length() > 1)	query += ")";
		}
		else query=query.substring(0, query.length()-7);				//no hem afegit cap condicio, per tant borrem el WHERE
		
		return query;
	}
	
	private String construeixQueryExportacio(User user,int id,String cerca, int estat, String estatsDisponibles, Date data_i, Date data_f, int fisic, String id_unitat, String id_catalogador, int propis, String descripcio)
	{
		String query, cons=" WHERE ";
		
		if(estat==MerliBean.LLISTA_M_TOTS2)
		{
			String[] estats=estatsDisponibles.split(","); 
			cons+="(";
			for(int i=0;i<estats.length;i++)
			{
				try
				{
					int e=Integer.parseInt(estats[i]);
					if(e<MerliBean.LLISTA_M_TRADUCT_CA || e>MerliBean.LLISTA_M_TRADUCT_OC)	//evito els estats de traduccio pq estan inclosos a Publicades
					{
						cons+="(";
						cons+=consFiltre(e);
						cons+=consPersonals(e, user);
						cons=cons.substring(0, cons.length()-4);//li trec la ˙ltima AND
						cons+=")";
						cons+=" OR ";
					}
				}
				catch(Exception e){}
			}
			cons=cons.substring(0, cons.length()-4);//li trec la ˙ltima OR
			cons+=") AND ";
		}
		else
		{
			cons+=consFiltre(estat);
			cons+=consPersonals(estat, user);
		}
		cons+=consAltres(id, cerca, data_i,data_f,0,id_unitat,id_catalogador, propis, descripcio, user);
		
		query = "SELECT id_rec FROM v_mer_list_etiq_fisics v" + cons;
		
		if(cons.length()>7) query=query.substring(0, query.length()-4);	//trec l'ultim "AND "
		else query=query.substring(0, query.length()-7);				//no hem afegit cap condicio, per tant borrem el WHERE
		
		return query;
	}

	public void setEstat(int idRecurs, int estat, String usuari) throws MerliDBException {
		Connection c=null;
		ConnectionBean cb = null;
		String query;
		ArrayList lParam = new ArrayList();
		ArrayList lCond = new ArrayList();
		lCond.add(new Integer(idRecurs));
		cb = connectBD();
		try {
			switch (estat){
				case MerliBean.ESTAT_M_EN_PROCES:
				case MerliBean.ESTAT_M_REALITZAT:
					query = "id_estat = ?, v_responsable = ?";
					lParam.add(new Integer(estat));
					lParam.add(usuari);
					break;
				case MerliBean.ESTAT_M_PUBLICAT:
					setContribucio(idRecurs,usuari,MerliContribution.VALIDADOR);
					query = "id_estat = ?";
					lParam.add(new Integer(estat));
					break;
//				case MerliBean.ESTAT_PER_CORRECIO:
//					setContribucio(idRecurs,usuari,MerliContribution.VALIDADOR);
				case MerliBean.ESTAT_M_DENEGAT:
				case MerliBean.ESTAT_M_RETORNAT:
				case MerliBean.ESTAT_M_PENDENT:
				default:
					query = "id_estat = ?";
					lParam.add(new Integer(estat));
					break;
			}
			//cb = connectBD();
			c = cb.getConnection();
			
			AccesBD.executeUpdate("mer_rec_info","id_rec = ?",lCond,query,lParam,c);


		} catch (NullPointerException ne){
			logger.error("Can't create FullLlistat of Recursos->"+ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			logger.error("Can't creating Recursos.->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}finally{
			if(c!=null){
				try {
					c.close();
				} catch (SQLException e) {
					logger.error("Error tancant conexio: ",e);
					throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
				}
			}
			disconnectBD(cb);
		}
	}

	public void setContribucio(int idRecurs, String usuari, int rol) throws MerliDBException {
		setContribucio(idRecurs, usuari, rol, null);
	}
	public void setContribucio(int idRecurs, String usuari, int rol,String comentari) throws MerliDBException {
		ConnectionBean cb = null;
		ArrayList lParam = new ArrayList();
		ArrayList lCond = new ArrayList();
		lCond.add(new Integer(idRecurs));
		try {
			cb = connectBD();
			//c = ;
			
			lParam.add(new Integer(rol));
			lParam.add(usuari);
			lParam.add(new Date(System.currentTimeMillis()));
			if (comentari==null)
				switch (rol){
					case MerliContribution.AUTOR: lParam.add("Autor del recurs");
					break;
					case MerliContribution.CORRECTOR: lParam.add("Ha realitzat una correcciÛ del recurs");
					break;
					case MerliContribution.ETIQUETADOR: lParam.add("Etiquetador del recurs");
					break;
					case MerliContribution.VALIDADOR: lParam.add("Validador del recurs");
					break;
					case MerliContribution.TRADUCTOR: lParam.add("Traductor del recurs");
					break;
					case MerliContribution.AGREGA: lParam.add("Publicador a AGREGA");
					break;
				}
			else
				lParam.add(comentari);
			lCond.add(new Integer(rol));
			if (0 == AccesBD.executeExist("mer_contribucio","id_rec = ? AND id_rol_cont = ?",lCond,cb.getConnection())){
				lParam.add(new Integer(idRecurs));
				AccesBD.executeInsert("mer_contribucio",lParam, cb.getConnection());	
			}else{
				lParam.remove(0);
				AccesBD.executeUpdate("mer_contribucio","id_rec = ? AND id_rol_cont = ?",lCond," v_entitat = ?, d_data = ?, v_descripcio = ? ",lParam,cb.getConnection());
			}
		} catch (MerliDBException mbe){
			logger.warn("MerliBean.setContribution:Can't create contribution->"+mbe);
		} catch (NullPointerException ne){
			logger.warn("MerliBean.setContribution:Can't create contribution->",ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			logger.warn("MerliBean.setContribution:Error creating contribution.->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}finally{
			disconnectBD(cb);
		}
	}

	public void setMessage(RecursMerli recurs, String subject, String missatge, String email_origen, String email_desti) throws MessagingException,AddressException{
		String aplicacio="";
		String entorn="";
		String adrecaFrom="";
		ConnectionBean cb = null;
		try {
			cb = connectBD();
			aplicacio = AccesBD.executeQuery("select v_value from mer_config where v_key='merli_correu_aplicacio'", "v_value", cb.getConnection());
			entorn = AccesBD.executeQuery("select v_value from mer_config where v_key='merli_correu_url'", "v_value", cb.getConnection());
			adrecaFrom = AccesBD.executeQuery("select v_value from mer_config where v_key='merli_correu_from'", "v_value", cb.getConnection());
		} catch (Exception e) {
			logger.warn("No es pot accedir a les dades de mer_config ->"+e);
		}finally{
			try {
				disconnectBD(cb);
			} catch (MerliDBException e) {}
		}
		
		logger.debug(" mail subj:" + subject + " to:" + email_desti + " body:" + missatge + "");
			
		CorreuSender sender;
		try {
			sender = new CorreuSender(aplicacio,entorn);
			String resultDisponibilitat = sender.consultaDisponibilitat(adrecaFrom);
			if("OK".equals(resultDisponibilitat)) 
			{
				EnviamentResponse response = sender.enviaCorreu(adrecaFrom, email_desti, subject, missatge, CorreuBody.TXT);
				if(response.isOk())  logger.info("Correu enviat correctament");
				else 
				{
					logger.info("Resultat de l'enviament: " + response.getStatus());
					for(int i=0;i>response.unsendedMessages().size();i++)	//Llistar correus no enviats
					{
						CorreuResponse resposta = (CorreuResponse)response.unsendedMessages().get(i);
						logger.warn("El correu amb id: " + resposta.getCorreuId() + " no s'ha enviat. Causa: " + resposta.getErrorMessage());
					}
					logger.warn("El log generat per l'enviament Ès: \n" + sender.getLog());
				}
			} 	
		} catch (CorreuException e) {
			logger.error("Error al enviar el missatge. "+e.getMessage());
		}	
	}
	
	/**
	 * Crida fake al WS. Crea un SOAPElement, simulant la resposta del WS. Correcte indica l'status de la resposta i dels missatges
	 * @param smRequest
	 * @param correcte
	 * @return
	 */
	private SOAPMessage cridaFake(SOAPMessage smRequest, boolean correcte) {
		String sStatus,sMessage;
		if(correcte) {sStatus="OK";sMessage="Missatge processat correctament";}
		else {sStatus="KO";sMessage="No se han podido enviar correctamente todos los correos";}
		SOAPMessage smResponse;
		try {
			smResponse = MessageFactory.newInstance().createMessage();
			SOAPBody sbResponse = smResponse.getSOAPPart().getEnvelope().getBody();          
			Name n = SOAPFactory.newInstance().createName("ans1:RespostaEnviamen");             
			SOAPBodyElement sbeResponse = sbResponse.addBodyElement(n);
			sbeResponse.setAttribute("xmlns:ans1", "http://www.gencat.cat/educacio/sscc/correu");
				SOAPElement seStatus=SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("status"));
				seStatus.addTextNode(sStatus);
			sbeResponse.addChildElement(seStatus);
				SOAPElement seMessage=SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("message"));
				seMessage.addTextNode(sMessage);
			sbeResponse.addChildElement(seMessage);
				SOAPElement seResposta=SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("respostaCorreu"));
					SOAPElement seId=SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("id"));
					seId.addTextNode("162");
				seResposta.addChildElement(seId);
				seResposta.addChildElement(seStatus);
				seResposta.addChildElement(seMessage);
			sbeResponse.addChildElement(seResposta);	
			smResponse.saveChanges();
			return smResponse;
		} catch (SOAPException e) {
			e.printStackTrace();
			return null;
		}  
		
	}

	/**
	 * Crea el SOAPElement amb els par‡metres del missatge
	 * @param subject
	 * @param missatge
	 * @param user
	 * @param email
	 * @return
	 */
	private SOAPElement msgToXml(String subject, String missatge, String from, String email_origen, String email) {
		SOAPElement seCorreu = null;
		SOAPElement seFill;
		SOAPElement seNet;
		SOAPElement seDos;
	
		try {
			seCorreu = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("correu"));
			//from
				seFill = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("from"));
				seFill.addTextNode(from);
			seCorreu.addChildElement(seFill);
			//replyAdresses
				seFill = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("replyAddresses"));
				//bucle
					seNet = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("address"));
					seNet.addTextNode(email_origen);
				seFill.addChildElement(seNet);
				//fi bucle
			seCorreu.addChildElement(seFill);
			//destinationAddresses
				seFill = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("destinationAddresses"));
				seDos = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("destination"));
				//bucle
					seNet = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("address"));
					seNet.addTextNode(email);
				seDos.addChildElement(seNet);
					seNet = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("type"));
					seNet.addTextNode("TO");
				seDos.addChildElement(seNet);
			seFill.addChildElement(seDos);
				//fibucle
			seCorreu.addChildElement(seFill);
			//subject
				seFill = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("subject"));
				seFill.addTextNode(subject);
			seCorreu.addChildElement(seFill);
			//mailBody
				seFill = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("mailBody"));
					seNet = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("bodyType"));
					seNet.addTextNode("text/plain");
				seFill.addChildElement(seNet);
					seNet = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("bodyContent"));
					seNet.addTextNode(missatge);
				seFill.addChildElement(seNet);
			seCorreu.addChildElement(seFill);
			//attachments
			//	seFill = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("attachments"));
				//bucle -- de moment no es poden adjuntar arxius
				//fill=attachments;	//Net=attachment*;	//Besnet=filename, filePath, attachmentContent;	//Rebesnet=mimeTye, fileContent
				//SOAPElement seBesnet;
				//SOAPElement seRebesnet;
//					seNet = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("attachment"));
//						seBesnet = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("fileName"));
//						seBesnet.addTextNode("hola.txt");
//					seNet.addChildElement(seBesnet);
//						seBesnet = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("filePath"));
//						seBesnet.addTextNode("/j2ee/e13_jira/");
//					seNet.addChildElement(seBesnet);
//					
//						seBesnet = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("attachmentContent"));
//							seRebesnet = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("mimeType"));
//							// afegir el text del mimeTye
//						seBesnet.addChildElement(seRebesnet);
//							seRebesnet = SOAPFactory.newInstance().createElement(SOAPFactory.newInstance().createName("fileContent"));
//							// afegir el text del fileContent
//						seBesnet.addChildElement(seRebesnet);					
//					seNet.addChildElement(seBesnet);
//				//fiBucle
//				seFill.addChildElement(seNet);
		//	seCorreu.addChildElement(seFill);
		} catch (SOAPException e) {
			e.printStackTrace();
		}
logger.info("Subject mail:"+subject);
logger.info("Missatge mail:"+missatge);
logger.info("*****"+seCorreu.toString()+"******");
		return seCorreu;
	}

	public void corregir(int idRecurs, String title, String description, String rightsDesc) throws MerliDBException {
		Connection c;
		ConnectionBean cb = null;
		String query;
		ArrayList lParam = new ArrayList();
		ArrayList lCond = new ArrayList();
		lCond.add(new Integer(idRecurs));
		try {
			salvarTextos(idRecurs,"ca",title,description,rightsDesc,true);
			
			
			cb = connectBD();
			c = cb.getConnection();
			query= "titol = ?, titol_pla=?, descripcio = ?, desc_pla = ?";
			lParam.add(title);
			lParam.add(Utility.aplanarText(title));
			lParam.add(description);
			lParam.add(Utility.aplanarText(description));
			AccesBD.executeUpdate("mer_recurs","id_rec = ?",lCond,query,lParam,c);
			
			query= "descripcio = ?";
			lParam.clear();
			lParam.add(rightsDesc);
			AccesBD.executeUpdate("mer_drets","id_rec = ?",lCond,query,lParam,c);
			
			c.close();
			disconnectBD(cb);
		} catch (NullPointerException ne){
			disconnectBD(cb);
			logger.warn("Error setting language correction:>"+ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Error setting language correction:->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
	}

	public RecursMerli getRecurs(int idRecurs) throws MerliDBException {
		RecursBD rb= new RecursBD();
		return rb.getRecurs(idRecurs);
	}

	public Collection getUserList(int rol) throws MerliDBException {
		ArrayList lRes = new ArrayList();

		InfoUsuari iu = new InfoUsuari();

		Map m, m2;
		ArrayList lparm, lelem= new ArrayList();
		ConnectionBean cb = null;
		Connection c;
		String query;
		
		try {
			cb = connectBD();
			c = cb.getConnection();
			//m =  AccesBD.getJoin(lelem,l1,"mer_recurs","mer_rec_info","id_rec","","","id_rec","","DESC",c);
			//m = AccesBD.getFullLlistat("mer_recurs",lelem,"id_rec DESC",cb.getConnection());			

			lparm = new ArrayList();
			lelem = new ArrayList();
			lelem.add("v_username"); 
			
			query="mer_perm_oper mpo, mer_user_perm mup Where mpo.id_operation = ? AND mpo.id_permission = mup.id_permission";					

			switch (rol){
				case MerliContribution.AUTOR: lparm.add(new Integer(MerliOperations.PUBLICAR));
				break;
				case MerliContribution.CORRECTOR: lparm.add(new Integer(MerliOperations.PUBLICAR));
				break;
				case MerliContribution.ETIQUETADOR: lparm.add(new Integer(MerliOperations.RECADD));
				break;
				case MerliContribution.VALIDADOR: lparm.add(new Integer(MerliOperations.PUBLICAR));
				break;
			}
			
			m = AccesBD.getQuery(query,lelem, lparm,c);

			for (int i = 0; i< ((ArrayList)m.get("v_username")).size();i++){
				iu = new InfoUsuari();
				iu.setUsername(((ArrayList)m.get("v_username")).get(i).toString());

				lelem.clear();
				lelem.add("d_data");
				lparm.clear();
				lparm.add(iu.getUsername());
				lparm.add(new Integer(rol));
				query="v_entitat =? AND id_rol_cont =?";				
				m2 = AccesBD.getObjectList("mer_contribucio",lelem,query,lparm,"d_data DESC",c);
				//iu.setNumContribucions(((BigDecimal)((ArrayList) m2.get("count(*)")).get(0)).toString());
				if (!((ArrayList) m2.get("d_data")).isEmpty()){
					try{
						iu.setUltimaContribucio(((Timestamp) ((ArrayList) m2.get("d_data")).get(0)));
					}catch (ClassCastException e){
						iu.setUltimaContribucio(new Timestamp(((Date) ((ArrayList) m2.get("d_data")).get(0)).getTime()));
					}
					iu.setNumContribucions(((ArrayList)m2.get("d_data")).size());
				}
				
				lelem.clear();
				lelem.add("count(*)");
				lparm.clear();
				lparm.add(iu.getUsername());
				switch (rol){
					case MerliContribution.AUTOR: lparm.add(new Integer(MerliBean.ESTAT_M_PUBLICAT));
					break;
					case MerliContribution.CORRECTOR: lparm.add(new Integer(MerliBean.ESTAT_M_REALITZAT));
					break;
					case MerliContribution.ETIQUETADOR: lparm.add(new Integer(MerliBean.ESTAT_M_EN_PROCES));
					break;
					case MerliContribution.VALIDADOR: lparm.add(new Integer(MerliBean.ESTAT_M_EN_PROCES));
					break;
				}
				
				query="v_responsable=?  AND id_estat = ?";				
				m2 = AccesBD.getObject("mer_rec_info",lelem,query,lparm,c);
				if (!((ArrayList) m2.get("count(*)")).isEmpty())			
					iu.setNumPendents(((BigDecimal)((ArrayList) m2.get("count(*)")).get(0)).intValue());
				lRes.add(iu);
			}
			c.close();
			disconnectBD(cb);
		} catch (NullPointerException ne){
			disconnectBD(cb);
			logger.warn("Can't create users list->"+ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Can't create users list.->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}	
		
		return lRes;
	}
	
	public ArrayList getCurriculumElements(ArrayList curriculum){
		Hashtable ht = new Hashtable();
		ArrayList alist = new ArrayList();
		ArrayList curri = curriculum;		
		Relation rel;
		int idCont = 0;
		SemanticInterface sn = new SemanticInterface();
		
		for (int i = 0; i < curri.size();i++){
			//Es controla un IndexOutOfBounds per si el getRelations retorna una llista buida
			//posem el nivell
			try{
				if(((String)curri.get(i)).indexOf("content")>=0)		//curri conte strings de l'estil contentXXX o areaXXX
				{
					idCont = Integer.parseInt(((String) curri.get(i)).substring(7));		//em quedo nomes amb l'id
					rel = (Relation) sn.getRelations(idCont,"content","RCL",RelationType.SOURCE).get(0);
				}
				else		//area
				{
					idCont = Integer.parseInt(((String) curri.get(i)).substring(4));//curri es un string de l'estil areaXXX, agafo l'id
					rel = (Relation) sn.getRelations(idCont,"area","RAL",RelationType.SOURCE).get(0);
				}
				ht = new Hashtable();
				try{		
					ht.put("level",sn.getNode(rel.getIdDest(),"level").getTerm());
					ht.put("idLevel",new Integer(rel.getIdDest()));
				}catch (Exception e){
					ht.put("level","");
					ht.put("idLevel",new Integer(0));
				}
			}catch (Exception e){
				logger.error("Error carregant relacions DUC-RCL del recurs:"+idCont);
				ht.put("level","");
				ht.put("idLevel",new Integer(0));
			}
			//posem l'area
			try{
				if(((String)curri.get(i)).indexOf("content")>=0)
				{
					rel = (Relation) sn.getRelations(idCont,"content","RCA",RelationType.SOURCE).get(0);
					try{
						ht.put("area",sn.getNode(rel.getIdDest(),"area").getTerm());
						ht.put("idArea",new Integer(rel.getIdDest()));
					}catch (Exception e){
						ht.put("area","");
						ht.put("idArea",new Integer(0));
					}
				}
				else
				{
					ht.put("area",sn.getNode(idCont,"area").getTerm());
					ht.put("idArea",new Integer(idCont));
					alist.add(ht);
				}
			}catch (Exception e){
				logger.error("Error carregant relacions DUC-RCA del recurs:"+idCont);
				ht.put("area","");
				ht.put("idArea",new Integer(0));
			}
			//En cas de no existir el contingut no es llistara
			try{
				if(((String)curri.get(i)).indexOf("content")>=0)
				{
					ht.put("content",sn.getNode(idCont,"content").getTerm());
					ht.put("idContent",new Integer(idCont));
					alist.add(ht);
				}
			}catch (Exception e){
				ht.put("content","content"+idCont);
			}
		}
		
		return alist;
	}

	/**
	 * Torna el text donat en el seguent format:
	 * <element title="TITLE">ELEMENT</element>
	 * @param element
	 * @param title
	 * @return
	 */
	public String printTitleElement(String element, String title){
		StringBuffer curri = new StringBuffer();
		curri.append("<element ");
		curri.append("title=\"");
		curri.append(title);
		curri.append("\">");
		if (element.length()>30){
			curri.append(element.substring(0,25));
			curri.append("...");
		}else{
			curri.append(element);
		}
		curri.append("</element>");
		return curri.toString();
	}
	
	/**
	 * Torna l'string donat en el seguent format
	 * <element title="ELEMENT">ELEMENT</element>
	 * si ELEMENT t√© m√©s de 30 car√†cters retorna:
	 * <element title="ELEMENT">ELE...</element>
	 * @param element
	 * @return
	 */
	public String printTitleElement(String element){
		return printTitleElement(element,Math.min(30,element.length()));
	}
	
	/**
	 * Torna l'string donat en el seguent format
	 * <element title="ELEMENT">ELEMENT</element>
	 * si ELEMENT t√© m√©s de SIZE car√†cters retorna:
	 * <element title="ELEMENT">ELE...</element>
	 * @param element
	 * @param size
	 * @return
	 */
	public String printTitleElement(String element, int size){
		StringBuffer curri = new StringBuffer();
		if (element.length()>size){
			curri.append("<element ");
			curri.append("title=\"");
			curri.append(element);
			curri.append("\">");
			curri.append(element.substring(0,size-1));
			curri.append("...");
			curri.append("</element>");
		}else{
			curri.append(element);
		}
		return curri.toString();
	}

	public String getUserMail(String etiquetador) {
		ArrayList lcamp,lCond;
		ConnectionBean cb = null;
		String res = "";
		lcamp = new ArrayList();
		lcamp.add("v_mail");
		lCond = new ArrayList();
		lCond.add(etiquetador);
		try {
			cb = connectBD();
			Map m = AccesBD.getObject("mer_users",lcamp,"v_user = ?",lCond,cb.getConnection());
			res = (String) ((ArrayList)m.get("v_mail")).get(0);			
		}catch(Exception e){
			return etiquetador+"@xtec.cat";
		}finally{
			try {
				disconnectBD(cb);
			} catch (MerliDBException e) {
			}
		}
		return res;
	}
	
	public boolean getUser(String usuari) {
		ArrayList lcamp,lCond;
		ConnectionBean cb = null;
		boolean res = false;
		lcamp = new ArrayList();
		lcamp.add("v_user");
		lCond = new ArrayList();
		lCond.add(usuari);
		try {
			cb = connectBD();
			Map m = AccesBD.getObject("mer_users",lcamp,"v_user = ?",lCond,cb.getConnection());
			if (((ArrayList)m.get("v_user")).size() > 0)
				res = true;			
		}catch(Exception e){
			logger.error("Error al comprovar l'usuari ->"+e);
		}finally{
			try {
				disconnectBD(cb);
			} catch (MerliDBException e) {
			}
		}
		return res;
	}
	
	public String llistaRec2HTML(ArrayList al, User user, int llista, int pagina){
		 StringBuffer text = new StringBuffer();
		 MerliBean mb = new MerliBean();
		 String etiq = "";
		 String resp = "";
		 String uni = "";
		 Iterator it = al.iterator();//.subList((Math.min(al.size(),(pagina-1)*MerliBean.RECURSOS_PAGINA)),Math.min(al.size(),pagina*MerliBean.RECURSOS_PAGINA)).iterator();
		 RecursMerli r;
		 int i=0;
		  	
		 while (it.hasNext()){
			 String imatge;
			 boolean esFisic=false;
			  i++;
		 	  r = (RecursMerli)it.next();
		 	 try {esFisic= comprovaEsRecFisic(r.getIdRecurs());} 
		 	 catch (MerliDBException e1) {	e1.printStackTrace();}
			 catch (SQLException e1) {	e1.printStackTrace();}
			  /**
			   * Paragraf per cada un dels recursos
			   */
			  //text.append("<p id=\"").append(r.getIdRecurs()).append("\">");
			  text.append("<div class=\"list\" id=\"").append(r.getIdRecurs()).append("\">");
			  /**
			   * Radiobutton per seleccionar el recurs i realitzar-hi operacions.
			   */
			  //text.append("<input name=\"recurs\"");
			  //text.append(" value=\"").append(r.getIdRecurs()).append("\"");
			  //text.append(" type=\"radio\" ");
			  //text.append(" onclick=\"\"/>");
			  text.append("<div class=\"listSelecButton\">");
			  if(esFisic)	imatge="fisic.png";
			  else			imatge="enlinia.png";
			  text.append("<img src=\"web/images/"+imatge+"\" onclick=\"loadInfoRec(").append(r.getIdRecurs()).append(",this)\"/>");
			  text.append("</div>");
			  
			  String estat = "alert";
//			  String esTitol = "title";
			  
			  if (r.getEstat() != null){// && llista == MerliBean.LLISTA_PROPIS ){
				  switch (Integer.parseInt(r.getEstat())){
				  	case MerliBean.ESTAT_M_DENEGAT:
				  	case MerliBean.ESTAT_M_RETORNAT:
						estat = "alert";
//						esTitol = "titAlert";
						break;
					case MerliBean.ESTAT_M_PENDENT:
				  	case MerliBean.ESTAT_M_EN_PROCES:
						estat = "message";
//						esTitol = "titMis";
						break;
				  	case MerliBean.ESTAT_M_REALITZAT:	
						estat = "notice";
//						esTitol = "titNoti";
								break;
				  	case MerliBean.ESTAT_M_PUBLICAT:		
					default:					
						estat = "inform";
//						esTitol = "titOk";
						break;		
				  }
			  }
			  text.append("<div class=\"listItem").append(estat).append("\">");
			  /**
			   * Nom del recurs amb la descripci√≥ com a 'title' i URL com a HREF
			   */
			  text.append("<span class=\"title\" ");
			  if (r.getDescription() != null){
				  text.append("title=\"");
				  text.append(messages.getMessage(locale,"merli.recurs"));
				  text.append(" ").append(r.getIdRecurs()).append(": ");
				  text.append(miniAplanarText(r.getDescription()));
				  text.append("\"");
			  }
			  text.append(">");
				  /**
				   * link al recurs sobre el nom.
				   */
			  text.append("<div style=\"width:60px;float:left;\">");
			  text.append(r.getIdRecurs());
			  text.append(". </div>");
			  text.append("<div style=\"color: #AD2114;float:left;\">");
			  text.append(mb.printTitleElement(r.getTitle(),80));
			  text.append("</div>");
			  /*if (r.getUrl() != null){
				  text.append("<a href=\"").append(r.getUrl()).append("\">");
				  text.append(mb.printTitleElement(r.getTitle(),20));
				  text.append("</a>");
			  }*/
			  text.append("</span>");
			  /**
			   * URL del recurs
			   */			
			  /*text.append("<span class=\"url\">");
			  text.append(mb.printTitleElement(r.getUrl()));
			  text.append("</span>");*/

			  /**
			   * informaci√≥ de l'Estat en q es troba cada recurs
			   */
			  if (r.getEstat() != null ){//&&
					  	//(llista == MerliBean.LLISTA_M_CERCADOR || llista == MerliBean.LLISTA_M_PROPIS || llista == MerliBean.LLISTA_M_NO_ACCEPTAT)){				
				  text.append("<span class=\"estat\">");
				  try{
				  text.append(messages.getMessage(locale,"merli.estat.user."+r.getEstat()));
				  }catch (Exception e){
					  e.printStackTrace();
				  }
				  text.append("</span>");				  
			  }
			  /**
			   * Usuari etiquetador del recurs
			   */		
			  //if (llista == MerliBean.LLISTA_ASSIGN ||
				//	  llista == MerliBean.LLISTA_VALIDAR || user.isSuperuser()){				
				 if(Integer.parseInt(r.getEstat()) != MerliBean.ESTAT_M_PENDENT)
				 {
				      text.append(" <span class=\"user\">(");
					  text.append("<text title=\"Catalogador/a\">");
					  text.append(r.getContribution(MerliContribution.ETIQUETADOR).getEntity());
					  text.append(" </text>/ ");
					  if (Integer.parseInt(r.getEstat()) == MerliBean.ESTAT_M_PUBLICAT && r.getContribution(MerliContribution.VALIDADOR) != null){
						  text.append("<text title=\"Validador/a\">");
						  text.append(r.getContribution(MerliContribution.VALIDADOR).getEntity());
						  text.append(" </text>/ ");
					  }
					  if (Integer.parseInt(r.getEstat()) == MerliBean.ESTAT_M_PUBLICAT && r.getContribution(MerliContribution.TRADUCTOR) != null){
						  text.append("<text title=\"Traductor/a\">");
						  text.append(r.getContribution(MerliContribution.TRADUCTOR).getEntity());
						  text.append(" </text>/ ");
					  }
						  
					  SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");				  				  
					  text.append(sDate.format(r.getContribution(MerliContribution.ETIQUETADOR).getDate()));
					  text.append(")</span>");
				 }
			  text.append("&nbsp;&nbsp;&nbsp;");
				  
			  if (r.getContribution(MerliContribution.ETIQUETADOR) != null)
				  etiq = r.getContribution(MerliContribution.ETIQUETADOR).getEntity();
			  if (r.getResponsable() != null)
				  resp = r.getResponsable();
			  if(r.getUnitatCreadora()!=null)
				  uni = r.getUnitatCreadora();
			  text.append(carregaOperacions(user,r.getIdRecurs(),r.getEstat(),etiq,resp,uni,llista, esFisic,r.getTitle()));
				  
			  //}
			  text.append("<div id=\"item").append(r.getIdRecurs()).append("\" class=\"itemInfo\">");
			  text.append("<table><tr><td style=\"border-left: 1px solid #AD2114; padding-left: 13px;\">");
			  if (r.getDescription()!=null && r.getDescription().trim().length()>0){
				//  text.append("<img src=\"web/images/punter2.png\" onclick=\"\"/>"); 
				  text.append("<span class=\"merli\">");
				  //text.append("<b>Identificador ").append(r.getIdRecurs()).append(": </b>");
				  text.append(r.getDescription());
				  text.append("</span>  ");
			  }
			  if (r.getUrl()!=null && !"http://".equalsIgnoreCase(r.getUrl())){
				  text.append("<a href=\"").append(r.getUrl()).append("\" target=\"_blank\">");
				  text.append(new MerliBean().printTitleElement(r.getUrl(),70));
				  text.append("</a>");
			  }
			  text.append("<br/><br/>");
			  text.append("</td></tr></table>");
				  //text.append("<br/><b>");
				  //text.append(Utility.toParaula(r.getTaxonTerm().toString()));
				  //text.append("</b><br/>");				  
				  //text.append(messages.getMessage(locale,"merli.publishedby")).append("<b>").append(r.getContribution(1).getEntity());
				  //text.append(" (");				  
				  //SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");				  				  
				  //text.append(sDate.format(r.getContribution(MerliContribution.AUTOR).getDate()));				  
				  //text.append(")");
				  //text.append("</b><br/>");
				 // text.append("<div class=\"itemOperacions\">");


//				  if (r.getContribution(MerliContribution.ETIQUETADOR) != null)
//					  etiq = r.getContribution(MerliContribution.ETIQUETADOR).getEntity();
//				  if (r.getResponsable() != null)
//					  resp = r.getResponsable();
//				  text.append(carregaOperacions(user,r.getIdRecurs(),r.getEstat(),etiq,resp,llista, esFisic));
				  
				  text.append("</div>");
			  text.append("</div>");
			 text.append("</div>");
			text.append("</div>");
		  }
		  
		 if (al.size() < 0){
			  text = new StringBuffer("<span class=\"alert\">");
			  text.append(messages.getMessage(locale,"error.merli.llistat.recursos"));
			  text.append("</span>");
		 }else if (al.size()==0){
			  text = new StringBuffer("<span>");
			  text.append(messages.getMessage(locale,"merli.list.buida"));
			  text.append("</span>");
		 }
		  return text.toString();
	 }
	
	public String llistaRec2HTMLComprova(ArrayList al, boolean err, String urlMessage) {
		 StringBuffer text = new StringBuffer();
		 Iterator it = al.iterator();
		 RecursMerli r;
		 int i=0;
		  	
		 if(!urlMessage.equals(""))
		 {
			String label = messages.getMessage(locale, "etiq.comprovacioUrl");
			text.append("<span id=\"validateUrl\">").append(label).append(urlMessage).append("</span><br><br><hr/>");
		 }

		 while (it.hasNext()){
			 i++;
		 	 r = (RecursMerli)it.next();
			  /**
			   * Paragraf per cada un dels recursos
			   */
			  text.append("<div class=\"list\" id=\"").append(r.getIdRecurs()).append("\">");
				  
			  String estat = "alert";
			  
			  if (r.getEstat() != null){
				  switch (Integer.parseInt(r.getEstat())){
				  	case MerliBean.ESTAT_M_DENEGAT:
				  	case MerliBean.ESTAT_M_RETORNAT:
						estat = "alert";
						break;
					case MerliBean.ESTAT_M_PENDENT:
				  	case MerliBean.ESTAT_M_EN_PROCES:
						estat = "message";
						break;
				  	case MerliBean.ESTAT_M_REALITZAT:	
						estat = "notice";
						break;
				  	case MerliBean.ESTAT_M_PUBLICAT:		
					default:					
						estat = "inform";
						break;		
				  }
			  }
			  text.append("<div class=\"listItem").append(estat).append("\">");
			  /**
			   * Nom del recurs amb la descripci√≥ com a 'title' i URL com a HREF
			   */
			  text.append("<span class=\"title\" >");
				  /**
				   * link al recurs sobre el nom.
				   */
			  text.append("<div style=\"width:60px;float:left;\">");
			  text.append(r.getIdRecurs());
			  text.append(". </div>");
			  text.append("<div style=\"color: #AD2114;float:left;\"><a href='gesrecurs.do?idRecurs="+r.getIdRecurs()+"&llistat=1&operation=veureRec2' target='_blank'>");
			  text.append(printTitleElement(r.getTitle(),30));
			  text.append("</a></div>");
			  text.append("</span>");

			  /**
			   * informaci√≥ de l'Estat en q es troba cada recurs
			   */
			  if (r.getEstat() != null ){			
				  text.append("<span class=\"estat\">");
				  try{
					  text.append(messages.getMessage(locale,"merli.estat.user."+r.getEstat()));
				  }catch (Exception e){
					  e.printStackTrace();
				  }
				  text.append("</span>");				  
			  }
			  /**
			   * Usuari etiquetador del recurs i data
			   */		
			  if(Integer.parseInt(r.getEstat()) != MerliBean.ESTAT_M_PENDENT)
			  {
				  text.append(" <span class=\"user\">(");
				  text.append("<text title=\"Catalogador/a\">");
				  text.append(r.getContribution(MerliContribution.ETIQUETADOR).getEntity());
				  text.append(" </text>/ ");
				  SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");				  				  
				  text.append(sDate.format(r.getContribution(MerliContribution.ETIQUETADOR).getDate()));
				  text.append(")</span>");
			  }
			  text.append("&nbsp;&nbsp;&nbsp;");
				  
			  text.append("</div>");
		      text.append("</div>");
			 
		  }
		 
		 String amagaRB="", selRB=""; 
		 if(err)
		 {
			 selRB="checked='true'";
			 amagaRB="style='display:none;'";
			 text = new StringBuffer("<span>");
			  text.append(messages.getMessage(locale,"error.merli.llistat.compr"));
			  text.append("</span>");
		 }
		 else if (al.size() < 0){
			  selRB="checked='true'";
			  amagaRB="style='display:none;'";
			  text = new StringBuffer("<span>");
			  text.append(messages.getMessage(locale,"error.merli.llistat.recursos"));
			  text.append("</span>");
		 }else if (al.size()==0)
		 {
			  selRB="checked='true'";
			  amagaRB="style='display:none;'";
			  text = new StringBuffer("<span>");
			  text.append(messages.getMessage(locale,"merli.list.comprova.buida"));
			  text.append("</span>");
		 }
		 
		 /* Botonets final */
		 	 text.append("<br><br>");
			 text.append("<div>");
			 text.append("<span "+amagaRB+">");
			 text.append("<input type='radio' name='scom' value='continua' id='compr_continua' "+selRB+" style='height:10px;'> Vull continuar amb la catalogaciÛ perquË considero que aquest recurs encara no est‡ catalogat <br>");
			 text.append("<input type='radio' name='scom' value='cancela' id='compr_cancela' style='height:10px;'> Vull cancel∑lar la catalogaciÛ perquË aquest recurs ja est‡ catalogat <br>");
			 text.append("</span>");
		text
				.append("<button type='button' class='butoMerli small red sortir' onClick='sortirComprova();' style='top: 30px; left: 520px;'>Accepta</button>");
			 text.append("</div>");
			 
		 return text.toString();
	 }
	
	public String miniAplanarText(String input){
        String result=(input==null ? "" : input);
        if(input!=null && input.length()>0){
            StringBuffer filtered = new StringBuffer(input.length());
            char c;
            for(int i=0; i<input.length(); i++) {
                c = input.charAt(i);
                String s=null;
                switch(c){
                    case '"': s="\'";    break;
                    default: break;
                }
                if(s!=null)
                    filtered.append(s);
                else
                    filtered.append(c);
            }
            result=filtered.substring(0);
        }
        return result;
    }
	

	public String carregaOperacions(User user,int idRecurs, String estat, int llista, boolean esFisic, String titol) {
		 return carregaOperacions(user,idRecurs,estat,"","","",llista, esFisic, titol);
	 }
	public String carregaOperacions(User user,int idRecurs, String estat,String etiquetador, String responsable, String unitat, int llista, boolean esFisic, String titol) {
			StringBuffer text = new StringBuffer();
			int estatId = Integer.parseInt(estat);
			
			insertOperacioVeure(idRecurs, llista, text);
			
			//Operacions Afegeix i Elimina Disponibilitat
			try {
				if(esFisic && user.getUnitat()!=null)
				{
					if(!comprovaDisponible(idRecurs, user.getUser())){
						if (estatId!=MerliBean.ESTAT_M_DENEGAT &&
								estatId!=MerliBean.ESTAT_M_RETORNAT){
							insertOperacioDisponible(idRecurs, llista, text);
						}
					}else insertOperacioEliminaDisp(idRecurs, llista, text);
				}
			} catch (MerliDBException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			switch (estatId){
			case MerliBean.ESTAT_M_DENEGAT:
				if (user.isSuperuser()){
					insertOperacioModificar(idRecurs, llista, text);
					insertOperacioEsborrar(idRecurs, text, titol);
				}else if (esRecursRelacionat(user,etiquetador,responsable,unitat,estat)){		
					insertOperacioEsborrar(idRecurs, text, titol);	
				}
				break;
		  	case MerliBean.ESTAT_M_RETORNAT:
				if (esRecursRelacionat(user,etiquetador,responsable,unitat,estat) || user.isSuperuser()){					
					insertOperacioModificar(idRecurs, llista, text);				
					insertOperacioEsborrar(idRecurs, text, titol);	
				}
				break;
			case MerliBean.ESTAT_M_EN_PROCES:
				if (esRecursRelacionat(user,etiquetador,responsable,unitat,estat) || user.isSuperuser()){
					insertOperacioModificar(idRecurs, llista, text);
					insertOperacioEsborrar(idRecurs, text, titol);
				}
				break;
		  	case MerliBean.ESTAT_M_PENDENT:
				if (esRecursRelacionat(user,etiquetador,responsable,unitat,estat) || user.isSuperuser()){
					insertOperacioModificar(idRecurs, llista, text);
					insertOperacioEsborrar(idRecurs, text, titol);
				}
				else if ((user.hasPermission(MerliOperations.PENDEDIT,idRecurs) || user.hasPermission(MerliOperations.PENDVIEW,idRecurs))){		
					insertOperacioModificar(idRecurs, llista, text);		
					if (user.hasPermission(MerliOperations.PENDEDIT,idRecurs)) insertOperacioEsborrar(idRecurs, text, titol);
				}
				
				break;
		  	case MerliBean.ESTAT_M_REALITZAT:	
				if (esRecursRelacionat(user,etiquetador,responsable,unitat,estat) ){
					insertOperacioModificar(idRecurs, llista, text);
				//	insertOperacioEsborrar(idRecurs, text);
				}
			//	if (user.hasPermission(MerliOperations.PUBLICAR,idRecurs) || user.isSuperuser()){
				if (user.hasPermissionPublicarTots() || user.isSuperuser()){
					insertOperacioPublicar(idRecurs, llista, text);
				}
				break;
		  	case MerliBean.ESTAT_M_PUBLICAT:	
//				if ((esRecursRelacionat(user,etiquetador,responsable,unitat,estat) && user.hasPermission(MerliOperations.RECSET)) 
//						|| user.hasPermission(MerliOperations.PUBLICAR,idRecurs) || user.isSuperuser()) insertOperacioModificar(idRecurs, llista, text);
				if ((esRecursRelacionat(user,etiquetador,responsable,unitat,estat) && user.hasPermission(MerliOperations.RECSET)) 
						|| user.hasPermissionPublicarTots() || user.isSuperuser()) insertOperacioModificar(idRecurs, llista, text);
				if (user.hasPermission(MerliOperations.TRADUCT,idRecurs)) insertOperacioTraduir(idRecurs, llista, text);				
				break;
			default:					
				break;		
			}
			
			
//			if (llista == MerliBean.LLISTA_M_PUBLICATS){
//				text.append("<a href=\"#\" onclick=\"javascript:veure(").append(idRecurs).append(",").append(llista).append(");\">");			
//				text.append("<img class=\"operacio\" src=\"web/images/esborrar.png\" alt=\"E\"/>");			
//				text.append(messages.getMessage(locale,"merli.op.veure"));			
//				text.append("</a>");			
//			}
		
			//Afegir la opciÛ de reenviar un recurs directament desde el llistat.
			if (llista == MerliBean.LLISTA_M_AGREGA_PEND){
				insertOperacioEnviarAgrega(idRecurs, llista, text);			
			}
			//Si estem en el llistat de AGREGA i no hi ha cap operaciÛ disponible afegim la operaciÛ "veure" 
			if (llista == MerliBean.LLISTA_M_AGREGA_PEND || llista == MerliBean.LLISTA_M_AGREGA_FET){
				//if (text.indexOf("onclick=\"javascript:")<0)
					//insertOperacioVeure(idRecurs, llista, text);			
			}
			
			return text.toString();
		}

	/**
	 * Un usuari esta relacionat amb un recurs si n'es el responsable/etiquetador
	 * si la seva unitat n'es el responsable/etiquetador
	 * si es va crear a la seva propia unitat
	 * @param user
	 * @param etiquetador
	 * @param responsable
	 * @param unitatCreadora
	 * @param estat
	 * @return
	 */
	private boolean esRecursRelacionat(User user, String etiquetador, String responsable, String unitatCreadora, String estat) {
		String usuari = "";
		String unitatConv = "";
		String unitat = "no";
		if (user.getUnitat()!=null)
			unitatConv = MediatecaBean.extractUserUnitat(String.valueOf(user.getUnitat()));
		if (user.getUnitat()!=null)
			unitat = String.valueOf(user.getUnitat());
		if (user.getUser()!=null)
			usuari = user.getUser();
	
		if (usuari.equals(etiquetador) || usuari.equals(responsable) ||
				unitatConv.equals(etiquetador) || unitatConv.equals(responsable) || unitat.equals(unitatCreadora)){
			return true;
		}		
		
		return false;
	}

	private void insertOperacioAssignar(int idRecurs, int llista, StringBuffer text) {
		text.append("<a href=\"#\" onclick=\"javascript:assignar(").append(idRecurs).append(",").append(llista).append(");\">");			
		text.append("<img class=\"operacio\" src=\"web/images/elimina.png\" alt=\"E\"/>");			
		text.append(messages.getMessage(locale,"merli.op.assignar"));			
		text.append("</a>&nbsp;&nbsp;");
	}

	private void insertOperacioModificar(int idRecurs, int llista, StringBuffer text) {
		text.append("<a href=\"#\" onclick=\"javascript:modificar(").append(idRecurs).append(",").append(llista).append(");\">");			  
		text.append("<img class=\"operacio\" src=\"web/images/editar.png\" alt=\"M\" title=\"").append(messages.getMessage(locale,"merli.op.modificar")).append("\" align=\"top\"/>");		
		//text.append(messages.getMessage(locale,"merli.op.modificar"));			
		text.append("</a>&nbsp;&nbsp;");
	}

	private void insertOperacioEsborrar(int idRecurs, StringBuffer text, String titol) {
		text.append("<a href=\"#\" onclick=\"javascript:esborrar('").append(titol.replaceAll("'", "\\\\'"))
				.append("',")
				.append(idRecurs).append(");\">");
		text.append("<img class=\"operacio\" src=\"web/images/eliminar.png\" alt=\"E\" title=\"").append(messages.getMessage(locale,"merli.op.esborrar")).append("\" align=\"top\"/>");	
		//text.append(messages.getMessage(locale,"merli.op.esborrar"));			
		text.append("</a>&nbsp;&nbsp;");
	}

	private void insertOperacioSolModif(int idRecurs, int llista, StringBuffer text) {
		text.append("<a href=\"#\" onclick=\"javascript:solmodif(").append(idRecurs).append(",").append(llista).append(");\">");			  
		text.append("<img class=\"operacio\" src=\"web/images/editar.png\" alt=\"M\"/>");
		text.append(messages.getMessage(locale,"merli.op.solmodif"));			
		text.append("</a>&nbsp;&nbsp;");
	}

	private void insertOperacioTraduir(int idRecurs, int llista, StringBuffer text) {
		text.append("<a href=\"#\" onclick=\"javascript:traduir(").append(idRecurs).append(",").append(llista).append(");\">");			
		text.append("<img class=\"operacio\" src=\"web/images/traduir.png\" alt=\"E\" title=\"").append(messages.getMessage(locale,"merli.op.traduir")).append("\" align=\"top\"/>");	
		//text.append(messages.getMessage(locale,"merli.op.traduir"));			
		text.append("</a>&nbsp;&nbsp;");
	}

	private void insertOperacioCorregir(int idRecurs, int llista, StringBuffer text) {
		text.append("<a href=\"#\" onclick=\"javascript:corregir(").append(idRecurs).append(",").append(llista).append(");\">");			
		text.append("<img class=\"operacio\" src=\"web/images/editar.png\" alt=\"E\"/>");
		text.append(messages.getMessage(locale,"merli.op.corregir"));			
		text.append("</a>&nbsp;&nbsp;");
	}

	private void insertOperacioCorrect(int idRecurs, int llista, StringBuffer text) {
		text.append("<a href=\"#\" onclick=\"javascript:correct(").append(idRecurs).append(",").append(llista).append(");\">");			  
		text.append("<img class=\"operacio\" src=\"web/images/editar.png\" alt=\"M\"/>");
		text.append(messages.getMessage(locale,"merli.op.solmodif"));			
		text.append("</a>&nbsp;&nbsp;");
	}

	private void insertOperacioVeure(int idRecurs, int llista, StringBuffer text) {
		text.append("<a href=\"#\" onclick=\"javascript:veure(").append(idRecurs).append(",").append(llista).append(");\">");			
		text.append("<img class=\"operacio\" src=\"web/images/veure.png\" alt=\"E\" title=\"").append(messages.getMessage(locale,"merli.op.veure")).append("\" align=\"top\"/>");	
		//text.append(messages.getMessage(locale,"merli.op.veure"));			
		text.append("</a>&nbsp;&nbsp;");
	}
	private void insertOperacioPublicar(int idRecurs, int llista, StringBuffer text) {
		text.append("<a href=\"#\" onclick=\"javascript:publicar(").append(idRecurs).append(",").append(llista).append(");\">");			
		text.append("<img class=\"operacio\" src=\"web/images/publicar.png\" alt=\"E\" title=\"").append(messages.getMessage(locale,"merli.op.publicar")).append("\" align=\"top\"/>");			
		//text.append(messages.getMessage(locale,"merli.op.publicar"));			
		text.append("</a>&nbsp;&nbsp;");
	}

	private void insertOperacioEnviarAgrega(int idRecurs, int llista, StringBuffer text) {
		text.append("<a href=\"#\" onclick=\"javascript:enviarAgrega(").append(idRecurs).append(",").append(llista).append(");\">");			
		text.append("<img class=\"operacio\" src=\"web/images/agrega.png\" alt=\"A\" title=\"").append(messages.getMessage(locale,"merli.op.enviarAgrega")).append("\" align=\"top\"/>");				
		//text.append(messages.getMessage(locale,"merli.op.enviarAgrega"));			
		text.append("</a>&nbsp;&nbsp;");
	}
	
	private void insertOperacioDisponible(int idRecurs, int llista, StringBuffer text) {
		text.append("<a id=\"disp\" href=\"#\" onclick=\"javascript:disponible(").append(idRecurs).append(");\">");			
		text.append("<img class=\"operacio\" src=\"web/images/afegir_disponibilitat.png\" alt=\"E\" title=\"").append(messages.getMessage(locale,"merli.op.disponible")).append("\" align=\"top\"/>");		
		//text.append(messages.getMessage(locale,"merli.op.disponible"));			
		text.append("</a>&nbsp;&nbsp;");
	}
	
	private void insertOperacioEliminaDisp(int idRecurs, int llista, StringBuffer text) {
		text.append("<a id=\"disp\" href=\"#\" onclick=\"javascript:no_disponible(").append(idRecurs).append(");\">");			
		text.append("<img class=\"operacio\" src=\"web/images/treure_disponibilitat.png\" alt=\"E\" title=\"").append(messages.getMessage(locale,"merli.op.nodisponible")).append("\" align=\"top\"/>");		
	//	text.append(messages.getMessage(locale,"merli.op.nodisponible"));			
		text.append("</a>&nbsp;&nbsp;");
	}

	public String paginacioHTML(int size,int pagAct) {
		return paginacioHTML(size,pagAct,0);
	}
	public String paginacioHTML(int size,int pagAct,int list) {
		
		String pagina = "";
//			Double max2 = new Double(Math.ceil(size / MerliBean.RECURSOS_PAGINA));
//			int size3 = max2.intValue();
		int max;
		
		if (size%MerliBean.RECURSOS_PAGINA>0) max = 1;
		else max=0;
		max += size/MerliBean.RECURSOS_PAGINA;
		
		if (max>1)
		{	
			String dis="style=\"color:#AD2114; font-weight:bold;\"";
			if(pagAct == 1) dis="disabled=true; style=\"color:#AAA;\""; 
			
			pagina += "<input type=\"button\" value=\"<<\" onclick=\"setPagina("+(1)+",this,"+(list==MerliBean.OPERACIO_CERCADOR)+","+max+");\" "+dis+" /> ";
			pagina += "<input type=\"button\" value=\"<\" onclick=\"setPagina("+(pagAct-1)+",this,"+(list==MerliBean.OPERACIO_CERCADOR)+","+max+");\" "+dis+" /> ";

			pagina+= "<span>&nbsp;"+messages.getMessage(locale,"merli.paginacio.pagina");
			pagina += "<input maxlength=\"5\" type=\"text\" value=\"" + pagAct
					+ "\" onkeydown=\"if (event.keyCode == 13) setPagina(this.value,this,"
					+ (list == MerliBean.OPERACIO_CERCADOR) + "," + max
					+ "); return noLetters(event);\" style=\"width:40px; text-align:right;\" /> ";
			pagina+= messages.getMessage(locale,"merli.paginacio.de")+max+"&nbsp;</span>";
			
			//pagina+= "<span>&nbsp;"+messages.getMessage(locale,"merli.paginacio", new String[] {String.valueOf(pagAct),String.valueOf(max)})+"&nbsp;</span>";
			/*pagina+="<select onchange=\"setPagina(this.value, this,"+(list==MerliBean.OPERACIO_CERCADOR)+");\" >";
			for (int i=1;i<=max;i++){
				int iStart =((i-1)*MerliBean.RECURSOS_PAGINA+1);
				int iEnd = (i==max?((i-1)*MerliBean.RECURSOS_PAGINA+(size-1)%MerliBean.RECURSOS_PAGINA)+1:(i*MerliBean.RECURSOS_PAGINA));
				pagina +="<option value=\""+i+"\""+(i==pagAct?"selected":"")+" >"+iStart+" - "+iEnd+"</option>";
			}
			pagina+="</select>";*/
			
			dis="style=\"color:#AD2114; font-weight:bold;\"";
			if(pagAct == max) dis="disabled=true; style=\"color:#AAA;\""; 
			pagina += "<input type=\"button\" value=\">\" onclick=\"setPagina("+(pagAct+1)+",this,"+(list==MerliBean.OPERACIO_CERCADOR)+","+max+");\" "+dis+"/> ";
			pagina += "<input type=\"button\" value=\">>\" onclick=\"setPagina("+(max)+",this,"+(list==MerliBean.OPERACIO_CERCADOR)+","+max+");\" "+dis+"/> ";

			pagina+= "<span>&nbsp;&nbsp;&nbsp;"+messages.getMessage(locale,"merli.paginacio.recurs", new String[] {String.valueOf((pagAct-1)*RECURSOS_PAGINA+1),String.valueOf(Math.min(pagAct*RECURSOS_PAGINA, size)),String.valueOf(size)})+"&nbsp;</span>";

			pagina+="<br>";
			
			// Aleix
			/*if (pagAct > 1)
					pagina += "<a href=\"#\" onclick=\"setPagina("+(pagAct-1)+",this);\"><-- </a>";
				for (int i=Math.max(1,pagAct-1);i<=Math.min(pagAct+1,max);i++){

					if (pagAct == i)
						pagina += "<strong>";
					else
						pagina += "<a href=\"#\" onclick=\"setPagina("+i+",this);\">";
					pagina += i;
					if (pagAct == i)
						pagina += "</strong> ";
					else
						pagina += "</a> ";
					if (i != Math.min(pagAct+1,max))
						pagina += "| ";
					
				}
				if ((pagAct+1) <= max)
					pagina += "<a href=\"#\" onclick=\"setPagina("+(pagAct+1)+",this);\">--></a>";
*/				
		}
		
		//pagina += "<br/><br/><span class=\"estat\">Nombre total de recursos: "+size+"</span>";
			
		return pagina;
	}

	public void salvarTextos(int idRecurs, String lang, String title, String description, String rightsDesc, boolean status) throws MerliDBException, SQLException {
		Connection c=null;
		ConnectionBean cb = null;
		String query;
		ArrayList lParam = new ArrayList();
		ArrayList lCond = new ArrayList();
		lCond.add(new Integer(idRecurs));
		lCond.add(lang);
		try {
			cb = connectBD();
			c = cb.getConnection();
			query= "titol = ?,  descripcio = ?, drets =?, estat=?";
			lParam.add((title));
			lParam.add((description));
			lParam.add((rightsDesc));
			if (status)
				lParam.add(new Integer(1));
			else
				lParam.add(new Integer(0));
				
			if (AccesBD.executeExist("mer_rec_lang","id_rec=? AND lang=?",lCond,c)>0){					 
				AccesBD.executeUpdate("mer_rec_lang","id_rec = ? AND lang=?",lCond,query,lParam,c);
			}else{
				query = "titol,  descripcio, drets, estat, id_rec, lang";
				lParam.add(new Integer(idRecurs));
				lParam.add(lang);
				AccesBD.executeInsert("mer_rec_lang",query,lParam,c);
			}
			
		} catch (NullPointerException ne){
			logger.warn("Error setting language correction:>"+ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			logger.warn("Error setting language correction:->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}finally{
			c.close();
			disconnectBD(cb);
		}
	}
	
	private boolean comprovaEsRecFisic(int idRecurs) throws MerliDBException, SQLException{
		Connection c=null;
		ConnectionBean cb = null;
		ArrayList lCond = new ArrayList();
		boolean res=false;
		try {
			cb = connectBD();
			c = cb.getConnection();
			lCond.add(new Integer(idRecurs));
			res=(AccesBD.executeExist("mer_recurs_fisic","id_recurs=?",lCond,c)>0);
		} catch (Exception e) {
				logger.warn("Error:->"+e);
				e.printStackTrace();
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}finally{
				c.close();
				disconnectBD(cb);
		}
		return res;
	}
	
	private boolean comprovaDisponible(int idRecurs, String sUsuari) throws MerliDBException, SQLException{
		Connection c=null;
		ConnectionBean cb = null;
		ArrayList lCond = new ArrayList();
		boolean res=false;
		try {
			cb = connectBD();
			c = cb.getConnection();
			lCond.add(new Integer(idRecurs));
			lCond.add(sUsuari);
			res=(AccesBD.executeExist("mer_rec_disp_uni r, mer_users u","r.id_rec=? and u.id_unitat=r.id_unitat and u.v_user=?",lCond,c)>0);
		} catch (Exception e) {
				logger.warn("Error:->"+e);
				e.printStackTrace();
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}finally{
				c.close();
				disconnectBD(cb);
		}
		return res;
	}

	public void setSendAgrega(int idRecurs, boolean send, String user, Connection c) throws MerliDBException {
		setSendAgrega(idRecurs, send, user, false, c);
	}

	public void setSendAgrega(int idRecurs, boolean send, String user) throws MerliDBException {
		setSendAgrega(idRecurs, send, user, false);
	}

	public void setSendAgrega(int idRecurs, boolean send, String user, boolean date) throws MerliDBException {
		Connection c = null;
		ConnectionBean cb = null;

		try {
			cb = connectBD();
			c = cb.getConnection();
			
			setSendAgrega(idRecurs, send, user, date, c);
		} catch (Exception e) {
			logger.warn("Error:->" + e);
		} finally {
			try {
				c.close();
			} catch (SQLException e) {
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
			try {
				disconnectBD(cb);
			} catch (MerliDBException e) {
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}
	}

	public void setSendAgrega(int idRecurs, boolean send, String user, boolean date, Connection c)
			throws MerliDBException {
		String query;
		ArrayList lParam = new ArrayList();
		ArrayList lCond = new ArrayList();
		lCond.add(new Integer(idRecurs));
		
		String valSend ="0";
		if (send)
			valSend ="1";
		try {
			if (0==AccesBD.executeExist("mer_rec_agrega", "id_rec=?", lCond, c)){
				//Crear-lo de nou si cal enviar-lo sino, no fer res.
				if (send){
					lParam.add(String.valueOf(idRecurs));
					lParam.add(generateAgregaId(idRecurs));
					lParam.add(valSend);
					if (date)
						lParam.add(new Date(System.currentTimeMillis()));
					else
						lParam.add("");
					setContribucio(idRecurs,user,MerliContribution.AGREGA);
					AccesBD.executeInsert("mer_rec_agrega", lParam, c);
				}
			}else{
				//Edit.
				query = "send = ?";
				if (send){
					lParam.add("1");
				}else
					lParam.add("0");

				if (date){
					query += ", date_agrega=?";
					lParam.add(new Date(System.currentTimeMillis()));
				}
				setContribucio(idRecurs,user,MerliContribution.AGREGA);
				AccesBD.executeUpdate("mer_rec_agrega","id_rec = ?",lCond,query,lParam,c);
			}
		} catch (NullPointerException ne){
			logger.error("Can not save resource:"+idRecurs+" AGREGA send state="+send+" by user:"+user+"->"+ne);
			throw new MerliDBException(MerliDBException.CONNEXIO_TANCADA);
		} catch (Exception e) {
			logger.error("Saving Agrega intregation state.->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
	}

	private String generateAgregaId(int idRecurs) {
		String autCode = "es-ca";
		String lang = "2";
		String agrLevel ="2";
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date d = new Date(System.currentTimeMillis());
		String date = sdf.format(d);
		return autCode+date+lang+agrLevel+"_XTEC_"+idRecurs;
	}

	public boolean publicarRecursAgrega(RecursMerli rm) {
		return AgregaInterface.publicarRecurs(rm);				
	}

	public boolean calPublicarAgrega(boolean istradEs, boolean isAgrega, Timestamp date) {
		return (istradEs && isAgrega && (null==date));
	}
	
	public static String getMessage(Locale locale, String missatge){
	 return MessageResources.getMessageResources(APPLICATION_RESOURCES).getMessage(locale,missatge);
	}
	
	public int migracioPendent() {
		MediatecaBean mb = new MediatecaBean();
		return mb.getNumeroPendents();				
	}

	public String getUnitatById(String idUnitat)
	{
		ConnectionBean cb = null;
		ArrayList lparam = new ArrayList();
		ArrayList lcamps = new ArrayList();
		String sUnitat="";
		try {
			cb = connectBD();
			lparam.add(idUnitat);
			lcamps.add("v_nom");
			Map m = AccesBD.getObject("mer_unitats",lcamps,"id_unitat = ?",lparam,cb.getConnection());
			sUnitat=(String) ((ArrayList)m.get("v_nom")).get(0);
		}
		catch(Exception e){}
		finally{
			try 
			{
				disconnectBD(cb);
			}
			catch (MerliDBException e) {}
		}
		return sUnitat;
	}
	
	public boolean existsRecurs(String idRecurs) {
				ConnectionBean cb = null;
				int numRec;
				ArrayList lparam = new ArrayList();
				try {
					cb = connectBD();
					lparam.add(idRecurs);
					numRec = AccesBD.executeCount("mer_recurs","id_rec = ?",lparam,cb.getConnection());
				}catch(Exception e){
				numRec=0;	
				}finally{
					try {
						disconnectBD(cb);
					} catch (MerliDBException e) {
					}
				}
				return numRec>0;
			}

	public ArrayList searchParaulaOberta(String valor) {

		ArrayList lcamp,lCond, lr;
		ConnectionBean cb = null;
		lcamp = new ArrayList();
		lcamp.add("id_paraula");
		lcamp.add("v_paraula");
		lCond = new ArrayList();
		lCond.add("%"+Utility.aplanarText(valor)+"%");
		lr = new ArrayList();
		Node n;
		try {
			cb = connectBD();
			Map m = AccesBD.getObjectList("mer_paraules",lcamp,"' '||v_paraula_pla||' ' like ?",lCond, "v_paraula", cb.getConnection());
			
			for (int i =0; i < ((ArrayList)m.get("v_paraula")).size(); i++){
				n = new Node(((BigDecimal) ((ArrayList)m.get("id_paraula")).get(i)).intValue(), "", null);
				n.setTerm( (String) ((ArrayList)m.get("v_paraula")).get(i));
				lr.add(n);
			}
		}catch(Exception e){
			return null;
		}finally{
			try {
				disconnectBD(cb);
			} catch (MerliDBException e) {
				
			}
		}
		return lr;
	}
	
	public String getVersionControl(){
		return versionControl;
	}
}
