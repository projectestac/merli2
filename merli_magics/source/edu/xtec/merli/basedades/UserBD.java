package edu.xtec.merli.basedades;


import java.math.BigDecimal;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;

import edu.xtec.merli.segur.Permission;
import edu.xtec.merli.segur.User;
import edu.xtec.merli.utils.Utility;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;

public class UserBD {

	protected static ConnectionBeanProvider broker;
	private static final Logger logger = Logger.getRootLogger();
	
	private static final String APPLICATION_RESOURCES = "MediatecaResources";	

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
			cb.getConnection().commit();
		broker.freeConnectionBean(cb);
		}catch(Exception e){
			logger.warn("Error switching off connection with DDBB->"+e);
			e.printStackTrace();throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);}
	}
	
	
	/**
	 * Tx per afegir un nou user a la BD.
	 * @param user User, informació del nou User a crear.
	 * @return
	 * @throws MerliDBException 
	 */
	public boolean addUser(User user) throws MerliDBException{
		boolean nPar;
		try{
			//Comprova si la paraula ja existeix a la BD
			nPar = existsUser(user.getUser());
			if( !nPar ){
								
				//Inserta la paraula en cas q no existeixi.
				if (user.getMail()==null || user.getMail().trim().length()==0) {
					user.setMail(" ");
				}
				
				if(user.getUs_merli().booleanValue())
				{
					user.setContrasenya(Utility.getEncoded(user.getContrasenya()));
//					MessageDigest md = MessageDigest.getInstance("SHA-1");
//					byte[] msg = user.getContrasenya().getBytes();
//					md.update(msg);
//					byte[] aMessageDigest = md.digest();
//					user.setContrasenya(new String(aMessageDigest));
				}
				insertUser(user);
			}else{
				//Si ja existeix llança l'error corresponent.
				return false;
			}
			
		}catch(MerliDBException me){
			logger.warn("Error on add new user ->"+me);
			me.printStackTrace();
			return false;
		} catch (NoSuchAlgorithmException e) {
			logger.warn("Error on add new user ->"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Inserta el user utilitzant el ConnectionBean.
	 * @param user, objecte User amb les dades del nou user.
	 * @param cb ConnectionBean connectat a la BD
	 * @return El resultat de la operació
	 * @throws Exception 
	 */
	public void insertUser(User user) throws MerliDBException{
		boolean connect = false;
		//Demana Connexió.
		ConnectionBean cb = connectBD();
		//Executa la inserció.
		try{
			//TODO: ull!!
			AccesBD.executeInsert("mer_users", "'"+user.getUser()+"','"+user.getMail()+"','"+user.getContrasenya()+"',"+user.getUnitat(), cb.getConnection());
			
		}catch(MerliDBException me){
			disconnectBD(cb);
			throw me;
		}catch(Exception e){
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		}
		//Finalitza la connexió a la BD. 
		disconnectBD(cb);
	}
		
	
	/**
	 * Comprova si la paraula amb id donada ja existeix.
	 * @param idParaula Id de la Paraula q es vol comprovar.
	 * @param cb ConnectionBean per utilitzar.
	 * @return El numero de paraules amb l'id. 0 no existeix. En cas d'error retornara <0.
	 * @throws Exception 
	 */
	public boolean existsUser(String username) throws MerliDBException{
		int res = 0;
		boolean connect = false;

		//Demana una connexió
		ConnectionBean cb = connectBD();
		try{
		res = AccesBD.executeExist("mer_users", "v_user",username,"", cb.getConnection());	
		}catch(MerliDBException me){
			disconnectBD(cb);
			throw me;
		}catch(Exception e){
			disconnectBD(cb);
			throw new MerliDBException(20);
		}
		//Finalitza la connexió a la BD. 
		disconnectBD(cb);			

		switch(res){
			case 0: return false;
			case -1: return true;
			default: return false;
		}		
	}


	public int setUser(User user) throws MerliDBException {
		ConnectionBean cb = connectBD();
		String condicio,query="";
		ArrayList al= new ArrayList();
		
		condicio = "v_user = '"+user.getUser()+"'";
		//al.add(0,user.getUser());		
		query += "v_mail = '"+user.getMail()+"'";//query = "v_mail = ?";
		if(user.getUnitat()!=null) query += ", id_unitat = '"+user.getUnitat()+"'";
		else query += ", id_unitat = null";
		if(user.getContrasenya()!=null) query += ", v_contrasenya = '"+user.getContrasenya()+"'";
		//al.add(1,user.getMail());
		//Executa la modificació.
		try{
		AccesBD.executeUpdate("mer_users", condicio, query, cb.getConnection());		
		this.insertPermission(user.getUser(),user.getPermissions());
		}catch(MerliDBException me){
			disconnectBD(cb);
			throw me;
		}catch(Exception e){
			disconnectBD(cb);
			throw new MerliDBException(20);
		}
		disconnectBD(cb);	
			
		return 0;//error;			
	}

	
	public int delUser(String user) throws MerliDBException {
		// TODO Auto-generated method stub
		int res = 0;

		ConnectionBean cb = connectBD();
		try{
		res = AccesBD.executeDelete("mer_users", "v_user = '"+user+"'", cb.getConnection());
		}catch(MerliDBException me){
			disconnectBD(cb);
			throw me;
		}catch(Exception e){
			disconnectBD(cb);
			throw new MerliDBException(20);
		}
		disconnectBD(cb);	
		
		return res;
	}

	public User getUser(String username) throws MerliDBException {
		Map m;
		User u = null;
		ArrayList l1, l2 = new ArrayList(),lres = new ArrayList();
		l2.add("v_user");
		l2.add("v_mail");
		l2.add("v_contrasenya");
		l2.add("id_unitat");
		ConnectionBean cb = connectBD();
		try{
			m = AccesBD.getObject("mer_users", l2, " v_user='"+username+"'", cb.getConnection());
			u = new User((String)((ArrayList)m.get("v_user")).get(0),(String)((ArrayList)m.get("v_mail")).get(0));
			if (m.get("id_unitat")!=null && ((ArrayList)m.get("id_unitat")).size()>0 && ((ArrayList)m.get("id_unitat")).get(0)!=null)
				u.setUnitat(new Integer(((BigDecimal)(((ArrayList)m.get("id_unitat")).get(0))).intValue()));
			if (m.get("v_contrasenya")!=null && ((ArrayList)m.get("v_contrasenya")).size()>0 && ((ArrayList)m.get("v_contrasenya")).get(0)!=null){
				u.setContrasenya((String)((ArrayList)m.get("v_contrasenya")).get(0));
				u.setUs_merli(new Boolean(true));
			}else{
				u.setUs_merli(new Boolean(false));
			}
					
			disconnectBD(cb);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Error on getting users list from DDBB->"+e);
			e.printStackTrace();			
		}
						
		return u;
	}
	

	public ArrayList getLlistatUsers() throws MerliDBException {
		// TODO Auto-generated method stub
		Map m;
		ArrayList l1, l2 = new ArrayList(),lres = new ArrayList();
		l2.add("v_user");
		l2.add("v_mail");
		ConnectionBean cb = connectBD();
		try{
			m = AccesBD.getFullLlistat("mer_users",l2,"v_user",cb.getConnection());
			l1 = (ArrayList) m.get("v_user");
			l2 = (ArrayList) m.get("v_mail");
			for (int i = 0;i<l1.size();i++){
				lres.add(new User((String)l1.get(i), (String)l2.get(i)));
			}			
			disconnectBD(cb);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Error on getting users list from DDBB->"+e);
			e.printStackTrace();			
		}
						
		return lres;
	}
	
	public ArrayList getLlistatUnitats() throws MerliDBException {
		Map m;
		ArrayList l1, l2 = new ArrayList(),lres = new ArrayList();
		l2.add("id_unitat");
		l2.add("v_nom");
		ConnectionBean cb = connectBD();
		try{
			String unitatDefecte = MessageResources.getMessageResources(APPLICATION_RESOURCES).getMessage("mediateca.unitat.creadora");

			//m = AccesBD.getFullLlistat("mer_unitats",l2,"upper(v_nom)",cb.getConnection());
			m = AccesBD.getObjectList("mer_unitats", l2, "id_unitat not in "+unitatDefecte , "upper(v_nom)", cb.getConnection());
			
			l1 = (ArrayList) m.get("id_unitat");
			l2 = (ArrayList) m.get("v_nom");
			for (int i = 0;i<l1.size();i++){
				lres.add(new User(l1.get(i).toString(), (String)l2.get(i).toString()));
			}			
			disconnectBD(cb);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Error on getting users list from DDBB->"+e);
			e.printStackTrace();			
		}
						
		return lres;
	}

	public ArrayList getLlistatPermisos() throws MerliDBException {
		// TODO Auto-generated method stub
		Map m;
		Permission p;
		ArrayList l1,l3, l2 = new ArrayList(),lres = new ArrayList();
		l2.add("id_permission");
		l2.add("v_permission");
		l2.add("v_description");
		
		ConnectionBean cb = connectBD();
		try{
			m = AccesBD.getFullLlistat("mer_permission",l2,"id_permission",cb.getConnection());
			l1 = (ArrayList) m.get("id_permission");
			l2 = (ArrayList) m.get("v_permission");
			l3 = (ArrayList) m.get("v_description");
			for (int i = 0;i<l1.size();i++){
				try{
					p = new Permission(((BigDecimal)l1.get(i)).intValue(), (String)l2.get(i));
					p.setDescription((String)l3.get(i));
					lres.add(p);
				}catch(Exception e){
					e.printStackTrace();
				}
			}			
			disconnectBD(cb);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Error on getting permissions list from DDBB->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
						
		return lres;
	}

	public boolean addPermission(Permission p) {
		// TODO Auto-generated method stub		
		int error = 0;
		boolean nPar;
		try{
			//Comprova si el permis ja existeix a la BD
			nPar = existsPermission(p.getPermission());
			if( !nPar ){
				//Inserta el permís en cas q no existeixi.
				if (!insertPermission(p))
					throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
			}else{
				//Si ja existeix llança l'error corresponent.
				throw new MerliDBException(MerliDBException.CODI_EXISTENT);
			}
			
		}catch(MerliDBException me){
			logger.warn("Error on add new permission to DDBB->"+me);
			me.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean setPermission(Permission perm) throws MerliDBException {
		// TODO Auto-generated method stub
		ConnectionBean cb = connectBD();
		String condicio,query="";
		ArrayList al= new ArrayList();
		condicio = "id_permission = "+perm.getIdPermission();
		query = "v_permission = '"+Utility.toParaulaDB(perm.getPermission())+"'";
		//al.add(0,user.getUser());		
		query += ", v_description = '"+Utility.toParaulaDB(perm.getDescription())+"'";//query = "v_mail = ?";
		//al.add(1,user.getMail());
		//Executa la modificació.
		try{
			AccesBD.executeUpdate("mer_permission", condicio, query, cb.getConnection());
			insertOperations(perm.getIdPermission(), perm.getOperations());
		}catch(MerliDBException me){
			disconnectBD(cb);
			throw me;
		}catch(Exception e){
			disconnectBD(cb);
			throw new MerliDBException(20);
		}
		
		disconnectBD(cb);	
			
		return true;//error;		
	}

	public boolean delPermission(int idPermission) throws MerliDBException{
		// TODO Auto-generated method stub
		ConnectionBean cb = connectBD();
		
		try {
			AccesBD.executeDelete("mer_user_perm", "id_permission = '"+idPermission+"'", cb.getConnection());
			AccesBD.executeDelete("mer_perm_oper", "id_permission = '"+idPermission+"'", cb.getConnection());
			AccesBD.executeDelete("mer_permission", "id_permission = '"+idPermission+"'", cb.getConnection());
		} catch (MerliDBException e) {
			// TODO Auto-generated catch block
			try {
				cb.getConnection().rollback();
				disconnectBD(cb);
				logger.warn("Error on deleting permission from DDBB->"+e);
				e.printStackTrace();
				throw new MerliDBException(MerliDBException.DELETEERROR);
			} catch (SQLException e1) {
				logger.warn("Error on connection with DDBB->"+e);
				e.printStackTrace();
				disconnectBD(cb);
				throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		}
		disconnectBD(cb);	
		
		return true;
	}

	public boolean insertPermission(Permission perm) throws MerliDBException{
		boolean connect = false;
		//Demana Connexió.
		ConnectionBean cb = connectBD();
		int idPermis=-1;
		//Executa la inserció.
		try {
			idPermis = AccesBD.getNext("seq_merli_permis", cb.getConnection());
		} catch (SQLException e) {
			disconnectBD(cb);
			logger.warn("Error on getting new idPermission from DDBB->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
		}
		try{
		//AccesBD.executeInsert("MER_PERMISSION",idPermis+",'"+perm.getPermission()+"','"+perm.getDescription()+"'",cb.getConnection());			
		AccesBD.executeInsert("MER_PERMISSION",idPermis+",'"+Utility.toParaulaDB(perm.getPermission())+"','"+Utility.toParaulaDB(perm.getDescription())+"'",cb.getConnection());			
		
		insertOperations(idPermis, perm.getOperations());
		}catch(MerliDBException me){
			disconnectBD(cb);
			throw me;
		}catch(Exception e){
			disconnectBD(cb);
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
		}
		//Finalitza la connexió a la BD. 
		disconnectBD(cb);
		return true;
	}
	
	public boolean existsPermission(String permission) throws MerliDBException{
		int res = 0;
		boolean connect = false;;

//		Demana una connexió
		ConnectionBean cb = connectBD();
		try{
		res = AccesBD.executeExist("mer_permission", "v_permission",permission,"", cb.getConnection());	
		}catch(MerliDBException me){
			disconnectBD(cb);
			throw me;
		}catch(Exception e){
			disconnectBD(cb);
			throw new MerliDBException(20);
		}
		//Finalitza la connexió a la BD. 
		disconnectBD(cb);			

		switch(res){
			case 0: return false;
			case -1: return true;
			default: return false;
		}		
	}

	public ArrayList getUserPermission(String username) throws MerliDBException {
		// TODO Auto-generated method stub
		Map m;
		ArrayList l = new ArrayList(),lres = new ArrayList();
		l.add("id_permission");
		ArrayList lParam = new ArrayList();
		lParam.add(username);

		ConnectionBean cb = connectBD();
		try{
			m = AccesBD.getObjectList("mer_user_perm",l,"v_username=?",lParam,"id_permission",cb.getConnection());
			//m = AccesBD.getObjectList("mer_user_perm",l,"v_username='"+username+"'","id_permission",cb.getConnection());
			l = (ArrayList) m.get("id_permission");
			for (int i = 0;i<l.size();i++){
				try{
					lres.add(new Integer(((BigDecimal)l.get(i)).intValue()));
				}catch(Exception e){
					e.printStackTrace();
				}
			}			
			disconnectBD(cb);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Error on getting user permissions from DDBB->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
						
		return lres;
	}

	public ArrayList getOperPermission(int idPermission) throws MerliDBException {
		// TODO Auto-generated method stub
		Map m;
		ArrayList l = new ArrayList(),lres = new ArrayList();
		l.add("id_operation");
		ArrayList lParam = new ArrayList();
		lParam.add(new Integer(idPermission));

		ConnectionBean cb = connectBD();
		try{
			m = AccesBD.getObjectList("mer_perm_oper",l,"id_permission = ?",lParam,"id_permission",cb.getConnection());
			//getObjectList("mer_perm_oper",l,"id_permission="+idPermission,"id_operation",cb.getConnection());
			l = (ArrayList) m.get("id_operation");
			for (int i = 0;i<l.size();i++){
				try{
					lres.add(new Integer(((BigDecimal)l.get(i)).intValue()));
				}catch(Exception e){
					e.printStackTrace();
				}
			}			
			disconnectBD(cb);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Error on getting operations permissions from DDBB->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
						
		return lres;
	}
	
	
	/**
	 * Inserta totes les operacions relacionades amb un permis.
	 * @param idPermission Permis al que se li assignen les operacions
	 * @param oper Llistat d'operacions del permis.
	 * @return True si s'han fet les modificacions correctament.
	 * @throws MerliDBException
	 */
	public boolean insertOperations(int idPermission, ArrayList oper) throws MerliDBException{
		ArrayList al = getOperPermission(idPermission);
		String delete = "";
		for (int i =0; i<al.size(); i++){
			if (oper.contains(al.get(i))){
				oper.remove(al.get(i));
			}else{
				if (delete.length()>2) delete += " OR ";
				delete += "id_operation="+al.get(i)+"";
			}
		}
		ConnectionBean cb = connectBD();
		try{
			if (delete.indexOf("=")>0){
				delete = "id_permission ="+idPermission+" AND ("+delete+")";
				AccesBD.executeDelete("mer_perm_oper", delete ,cb.getConnection());
			}
			for (int i = 0; i<oper.size();i++){
				AccesBD.executeInsert("mer_perm_oper",idPermission+","+oper.get(i),cb.getConnection());
			}
			disconnectBD(cb);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Error on adding permissions operations to DDBB->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		return true;		
	}
	
	
	/**
	 * Inserta els permisos d'un nou usuari. De moment assigna els permisos per tots els recursos.
	 * @param username Nom de l'usuari al que s'assignen els permisos
	 * @param perm Llistat de permisos que se li assignen.
	 * @return TRUE si s'han insertat correctament.
	 * @throws MerliDBException
	 */
	public boolean insertPermission(String username, ArrayList perm) throws MerliDBException{
		ArrayList al = getUserPermission(username);
		String delete = "";
		for (int i =0; i<al.size(); i++){
			if (perm.contains(al.get(i))){
				perm.remove(al.get(i));
			}else{
				if (delete.length()>2) delete += " OR ";
				delete += "id_permission="+al.get(i)+"";
			}
		}
		ConnectionBean cb = connectBD();
		try{
			if (delete.indexOf("=")>0){
				delete = "v_username ='"+username+"' AND ("+delete+")";
				AccesBD.executeDelete("mer_user_perm", delete ,cb.getConnection());
			}
			for (int i = 0; i<perm.size();i++){
				AccesBD.executeInsert("mer_user_perm","'"+username+"',"+perm.get(i)+",0",cb.getConnection());
			}
			disconnectBD(cb);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Error on adding user permissions to DDBB->"+e);
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		return true;		
	}

	public String getDefaultPermission() throws MerliDBException{
		ConnectionBean cb = connectBD();
		String defperm="";
		try{
			defperm = AccesBD.executeQuery("select v_value from mer_config where v_key='magics_default_perm'", "v_value", cb.getConnection());
				
			disconnectBD(cb);
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Error on getting users list from DDBB->"+e);
			e.printStackTrace();			
		}
		
		return defperm;
	}

	public boolean setDefaultPermission(Integer idPermis) throws MerliDBException {
		ConnectionBean cb = connectBD();
		int defperm;
		try{
			defperm = AccesBD.executeUpdate("mer_config", "v_key='magics_default_perm'", " v_value='"+idPermis.intValue()+"'", cb.getConnection());
			
			disconnectBD(cb);
			if (defperm==0)
				return true;
		} catch (Exception e) {
			disconnectBD(cb);
			logger.warn("Error on getting users list from DDBB->"+e);
			e.printStackTrace();			
		}
		
		return false;
	}
}
