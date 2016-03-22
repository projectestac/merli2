package edu.xtec.merli;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.basedades.UserBD;
import edu.xtec.merli.segur.Permission;
import edu.xtec.merli.segur.User;
import edu.xtec.merli.segur.operations.ApplicationOperations;
import edu.xtec.merli.segur.operations.IApplicationOperation;
import edu.xtec.merli.segur.operations.Operations;
import edu.xtec.merli.utils.Utility;

public class MagicBean {

	public static final int ADDUSER = 1;
	public static final int SETUSER = 2;
	public static final int DELUSER = 3;
	public static final int ADDPERMISSION = 4;
	public static final int SETPERMISSION = 5;
	public static final int DELPERMISSION = 6; 
	public static String DEFAULT_PERM="defaultPermission";
	
	private int operation;
	private static final Logger logger = Logger.getRootLogger();//("xtec.duc");
	/* ADDED - Nadim --> Have to update when deploying new version --> 25/11/2015 */
	public static String versionControl = "v2.1.0";
	
	public ArrayList getElementList(String tipus){
		ArrayList list = new ArrayList();
		ArrayList lRes = new ArrayList();
		Iterator iter, it;
		Hashtable hOper;
		Hashtable haux;
		UserBD ubd = new UserBD();
		try {
			if (tipus.compareTo("user")==0){
				list = ubd.getLlistatUsers();
				iter = list.iterator();
				User u;
				while (iter.hasNext()){
					u = (User) iter.next();
					hOper = new Hashtable();
					hOper.put("id",u.getUser());
					hOper.put("name",u.getUser());
					try{
						hOper.put("attribute",u.getMail());
					}catch(Exception e){}
					lRes.add(hOper);
				}
			}
			if (tipus.compareTo("unitat")==0){
				lRes = ubd.getLlistatUnitats();
			}
			if (tipus.compareTo("permission")==0){
				list = ubd.getLlistatPermisos();
				iter = list.iterator();
				Permission p;
				while (iter.hasNext()){
					p = (Permission) iter.next();
					hOper = new Hashtable();
					hOper.put("id",new Integer(p.getIdPermission()));
					hOper.put("name",p.getPermission());
					try{
						hOper.put("description",p.getDescription());
					}catch(Exception e){}
					lRes.add(hOper);
				}
			}			
		} catch (MerliDBException e) {
			e.printStackTrace();
		}
		
		return lRes;
	}

	public boolean addUser(String username, String email, Integer unitat, String contrasenya, Boolean us_merli){
		UserBD ubd = new UserBD();
		try {
			ubd.addUser(new User(username,email,unitat, contrasenya, us_merli));
		} catch (MerliDBException e) {
			return false;
		}
		return true;
	}

	public boolean setUser(String username, String email){
		UserBD ubd = new UserBD();
		try {
			ubd.setUser(new User(username,email));
		} catch (MerliDBException e) {
			return false;
		}
		return true;
	}
	
	public boolean delUser(String username){
		UserBD ubd = new UserBD();
		try {
			ubd.delUser(username);
		} catch (MerliDBException e) {
			return false;
		}
		return true;
	}

	public ArrayList executeOperation(String id, String property) {
		// TODO Auto-generated method stub
		ArrayList l = new ArrayList();
		Hashtable h = new Hashtable();
		
		if (property.compareTo("operation")==0){
			return this.getOperationList(id);			
		}
		if (property.compareTo("list")==0){
			return this.getElementList(id);
		}
		
		h.put("id","");
		h.put("name","No s'han trobat resultats");
		h.put("description","torni a realitzar la consulta.");
		l.add(h);
		
		return l;
	}


	private ArrayList getOperationList(String id) {
		ArrayList lRes = new ArrayList();
		Iterator iter;
		Hashtable hOper;
		UserBD ubd = new UserBD();
		if (id.compareTo("user")==0){
			hOper = new Hashtable();
			hOper.put("id","nouUser");
			hOper.put("name","Nou Usuari");
			lRes.add(hOper);
			hOper = new Hashtable();
			hOper.put("id","modificarUser");
			hOper.put("name","Modificar Usuari");
			lRes.add(hOper);
			hOper = new Hashtable();
			hOper.put("id","esborrarUser");
			hOper.put("name","Esborrar Usuari");
			lRes.add(hOper);
			hOper = new Hashtable();
			hOper.put("id","permisosUser");
			hOper.put("name","Permisos Usuari");
			lRes.add(hOper);
		}
		if (id.compareTo("permission")==0){
			hOper = new Hashtable();
			hOper.put("id","nouPermis");
			hOper.put("name","Nou Permis");
			lRes.add(hOper);
			hOper = new Hashtable();
			hOper.put("id","modificarPermis");
			hOper.put("name","Modificar Permis");
			lRes.add(hOper);
			hOper = new Hashtable();
			hOper.put("id","esborrarPermis");
			hOper.put("name","Esborrar Permis");
			lRes.add(hOper);
		}
		return lRes;
	}


	public ArrayList getPermissionList(String username) {
		// TODO Auto-generated method stub
		UserBD ubd = new UserBD();
		try {
			ArrayList operPerm = ubd.getUserPermission(username);
			return operPerm;
		} catch (MerliDBException e) {
			// TODO Auto-generated catch block
			logger.warn("Can't load permission rights of the user:"+username);
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList getOperationsList(int idPermis) {
		// TODO Auto-generated method stub
		ArrayList al = new ArrayList();
		UserBD ubd = new UserBD();
		try {
			ArrayList operPerm = ubd.getOperPermission(idPermis);
			return operPerm;
		} catch (MerliDBException e) {
			// TODO Auto-generated catch block
			logger.warn("Can't load operations rights of the permission:"+idPermis);
			e.printStackTrace();
			return null;
		}
	}

	public String createOperationList(ArrayList operationsList, int idPermis) {
		// TODO Auto-generated method stub
		String html = "";
		IApplicationOperation ao;
		String aux;
		ArrayList lop, lapl = Operations.getApplications();
		for (int iap=0; iap<lapl.size(); iap++){
			ao = (IApplicationOperation) lapl.get(iap);
			lop = ao.getListOperations();
			html +="<div id=\""+Utility.xmlEncode(ao.getName())+"Op\" class=\"operPerm\">";
			html +="<div class=\"operTitle\">Operacions "+Utility.xmlEncode(ao.getName())+"</div>";
			html +="<div class=\"opers\" onchange=\"operModified("+idPermis+");\">";
			for (int iop = 0; iop < lop.size(); iop++){
				if (operationsList.contains(lop.get(iop))) 
					aux = "checked=\"checked\"";
				else aux ="";
				html += "<input type=\"checkbox\" value=\""+lop.get(iop)+"\" name=\"operation"+idPermis+"\" "+aux+"/>"+Utility.xmlEncode(ao.getOperationName(((Integer)lop.get(iop)).intValue()));
			}
			html += "</div>";
			html += "</div>";			
		}
		
		return html;
	}

	public String createPermissionList(ArrayList permissionList, String username) throws MerliDBException {
		// TODO Auto-generated method stub
		String html = "";
		IApplicationOperation ao;
		String aux;
		Permission perm;
		UserBD ubd = new UserBD();
		ArrayList lper = ubd.getLlistatPermisos();
		html +="<span>Permisos:</span><br/>";
		html +="<div id=\""+Utility.xmlEncode(username)+"Perm\" class=\"operPerm\" onchange=\"operModified('"+username+"');\">";
		for (int iper=0; iper<lper.size(); iper++){
			perm = (Permission) lper.get(iper);
			if (permissionList.contains(new Integer(perm.getIdPermission()))) 
				aux = "checked=\"checked\"";
			else 
				aux ="";
			html+="<div style=\"float:left;width:150px\">";
			html += "<input type=\"checkbox\" value=\""+perm.getIdPermission()+"\" name=\"perm"+Utility.xmlEncode(username)+"\" "+aux+" title=\""+Utility.xmlEncode(perm.getDescription())+"\"/>"+Utility.xmlEncode(perm.getPermission())+"</div>";
		}
		html += "</div>";	
		
		return html;
	}

	public User getUser(String username) {
		UserBD ubd = new UserBD();
		try {
			return ubd.getUser(username);
		} catch (MerliDBException e) {
			return null;
		}
	}


	public String getDefaultPermission() {
		UserBD ubd = new UserBD();
		try {
			return ubd.getDefaultPermission();
		} catch (MerliDBException e) {
			return null;
		}
	}

	public boolean setDefaultPermission(Integer idPermis) {
		UserBD ubd = new UserBD();
		try {
			return ubd.setDefaultPermission(idPermis);
		} catch (MerliDBException e) {
			return false;
		}
	}


	public String getVersionControl(){
		return versionControl;
	}
	

}
