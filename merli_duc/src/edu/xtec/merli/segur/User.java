package edu.xtec.merli.segur;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import edu.xtec.merli.segur.operations.MerliOperations;

public class User {
	private String user;
	private String mail;
	private Integer unitat;
	private Boolean us_merli=Boolean.FALSE;
	private String contrasenya;
	
	// Conte una hash amb totes les operacions sobre les q te dret l'usuari. Si l'usuari te un dret
	// total sobre tots els recursos en el hash nomes hi tindra un llista amb l'element 0.
	// Altrement contindra una llista amb tots els recursos sobre els q hi te dret.
	private Hashtable operCol=new Hashtable();	
	
	public String getMail() {
		return mail;
	}
	
	public void setMail(String mail) {
		if (mail==null || "".equals(mail)){
			if (this.user!=null){
				this.mail = this.user+"@xtec.cat";
			}
		}else{
			this.mail = mail;
		}
	}
	
	public Integer getUnitat() {
		return unitat;
	}

	public void setUnitat(Integer unitat) {
		this.unitat = unitat;
	}
	
	public User(String user, String mail, Integer unitat) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.mail = mail;
		this.unitat = unitat;
	}
	
	public User(String user, String mail, Integer unitat, String contrasenya) {
		this.user = user;
		this.mail = mail;
		this.unitat = unitat;
		this.contrasenya = contrasenya;
		us_merli=Boolean.TRUE; //si te contrasenya es merli 
	}
	
	public User(String user, String mail, Integer unitat, String contrasenya, Boolean us_merli) {
		this.user = user;
		this.mail = mail;
		this.unitat = unitat;
		this.contrasenya = contrasenya;
		this.us_merli=us_merli;
	}

	public User(String user, String mail) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.mail = mail;
	}
	
	
	public User(String user) {
		// TODO Auto-generated constructor stub
		this.user = user;
		this.mail = user+"@xtec.cat";
	}
	

	public String getUser() {
		return user;
	}
	

	public void setUser(String user) {
		this.user = user;
	}


	public String getContrasenya() {
		return contrasenya;
	}

	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}

	/**
	 * Comprova si l'usuari te el permis 'idOperation' sobre tots els recursos.
	 * @param idPermission
	 * @return true si té el permís sobre tots els recursos.
	 */
	public boolean hasPermission(int idOperation){
		if (this.isSuperuser()) return true;
		if (operCol.containsKey(new Integer(idOperation)))
			return ((ArrayList)operCol.get(new Integer(idOperation))).contains(new Integer(0));
		return false;
	}
	
	/**
	 * Comprova si l'usuari te el permis per publicar tots els recursos o nomes els propis (es autoadmin)
	 * @return true si té el permís sobre tots els recursos.
	 */
	public boolean hasPermissionPublicarTots(){
		if (this.isSuperuser()) return true;
		return (this.hasPermission(MerliOperations.PUBLICAR)&&!this.isAutoadmin());
	}
	
	/**
	 * Dona a l'usuari el permis 'idOperation' sobre tots els recursos.
	 * @param idPermission
	 */
	public void setPermission(int idOperation){
		ArrayList l = new ArrayList();
		l.add(new Integer(0));
		operCol.put(new Integer(idOperation),l);
	}
	
	/**
	 * Comprova si l'usuari te el permís 'idOperation' sobre el recurs 'idRec'.
	 * @param idPermission Permís que es consulta
	 * @param idRec Recurs sobre el que es vol tenir permís
	 * @return True si té permis sobre el recurs.
	 */
	public boolean hasPermission(int idOperation, int idRec){
		if (this.isSuperuser()) return true;
		Integer idOper = new Integer(idOperation);
		if (operCol.containsKey(idOper)){
			return (((ArrayList)operCol.get(idOper)).contains(new Integer(idRec)) || ((ArrayList)operCol.get(idOper)).contains(new Integer(0)));
		}
		return false;
	}	


	public void setPermission(int idOperation, int idRec){
		Integer idOper = new Integer(idOperation);
		ArrayList l = new ArrayList();
		l.add(new Integer(idRec));
		if (operCol.containsKey(idOper)){
			((ArrayList)operCol.get(idOper)).add(new Integer(idRec));
		}else{
			operCol.put(idOper,l);
		}
	}

	
	public boolean isSuperuser() {
		if (operCol.containsKey(new Integer(0))) return true;
		return false;
	}
	
	public boolean isAutoadmin() {
		if (operCol.containsKey(new Integer(MerliOperations.PERMIS_AUTOADMIN))) return true;
		return false;
	}
	
	
	/**
	 * Retorna els identificadors del Permisos (no operacions) que té l'usuari.
	 * @return
	 */
	public ArrayList getPermissions(){
		ArrayList l1;
		Enumeration l2;
		Integer permis;
		l1= new ArrayList();
		if (operCol.containsKey(new Integer(0))){
			l1.add(new Integer(0));
		}else{
			l2 = (Enumeration) operCol.keys();
			while (l2.hasMoreElements()){
				permis = (Integer) l2.nextElement();
				if (permis.intValue()>=40000)
					l1.add(permis);
			}
		}
		return l1;
	}

	public Boolean getUs_merli() {
		return us_merli;
	}

	public void setUs_merli(Boolean us_merli) {
		this.us_merli = us_merli;
	}
}
