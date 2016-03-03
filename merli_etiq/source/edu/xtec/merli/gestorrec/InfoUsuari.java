package edu.xtec.merli.gestorrec;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class InfoUsuari{
	private String username;
	private String email;
	
	private int rol;
	private int numContribucions;
	private int numPendents;
	private int numRecursos;
	
	private Timestamp ultimaContribucio;

	
	
	public InfoUsuari() {
		super();
	}


	public InfoUsuari(String username, String email, int rol) {
		this.username = username;
		this.email = email;
		this.rol = rol;
	}


	public InfoUsuari(String username, int rol) {
		this.username = username;
		this.rol = rol;
	}


	public String getEmail() {
		return email;
	}
	

	public void setEmail(String email) {
		this.email = email;
	}
	

	public Timestamp getUltimaContribucio() {
		return ultimaContribucio;
	}
	

	public void setUltimaContribucio(Timestamp lastContribucio) {
		this.ultimaContribucio = lastContribucio;
	}

	public String getDataUltContribucio(){	
		if (ultimaContribucio == null)
			return "";
		SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");				  				  
		return (sDate.format(ultimaContribucio));				  		 
	}
	
	public String getDataUltContribucio(String format){	
		SimpleDateFormat sDate = new SimpleDateFormat(format);				  				  
		return (sDate.format(ultimaContribucio));				  		 
	}

	public int getNumContribucions() {
		return numContribucions;
	}
	

	public void setNumContribucions(int numContribucions) {
		this.numContribucions = numContribucions;
	}
	

	public int getNumPendents() {
		return numPendents;
	}
	

	public void setNumPendents(int numPendents) {
		this.numPendents = numPendents;
	}
	

	public int getNumRecursos() {
		return numRecursos;
	}
	

	public void setNumRecursos(int numRecursos) {
		this.numRecursos = numRecursos;
	}
	

	public int getRol() {
		return rol;
	}
	

	public void setRol(int rol) {
		this.rol = rol;
	}
	

	public String getUsername() {
		return username;
	}
	

	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}
