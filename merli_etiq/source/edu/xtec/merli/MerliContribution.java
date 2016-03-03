package edu.xtec.merli;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import edu.xtec.merli.utils.Utility;

public class MerliContribution {
	private String entity;
	//private String date;
	private String rol;
	private int idRol;
	private String description;
	private String idRecurs;		
	
	private SimpleDateFormat sDate;		
	private Timestamp date;
	

	public static final int AUTOR = 1;
	public static final int VALIDADOR = 2;
	public static final int ETIQUETADOR = 3;
	public static final int CORRECTOR = 4;
	public static final int CATALOGADOR = 5;
	public static final int TRADUCTOR = 6;
	public static final int AGREGA = 7;
	public static final int EDITOR = 8;
	
	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {		
		this.date = date;
	}
	
	
	public MerliContribution(int rol, String entity, String date) {
		// TODO Auto-generated constructor stub
		this.entity = entity;
		if (date.length() < 12){
			SimpleDateFormat sDate = new SimpleDateFormat("dd-MM-yyyy");
			try {
				this.date = new Timestamp(sDate.parse(date).getTime());
			} catch (ParseException e) {
				sDate = new SimpleDateFormat("dd-M-yyyy");
				try {
					this.date = new Timestamp(sDate.parse(date).getTime());
				} catch (ParseException e1) {
					this.date = new Timestamp(System.currentTimeMillis());
				}
			}
		}else{
			SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				this.date = new Timestamp(sDate.parse(date).getTime());
			} catch (ParseException e) {
				sDate = new SimpleDateFormat("yyyy-MM-dd");
				try {
					this.date = new Timestamp(sDate.parse(date).getTime());
				} catch (ParseException e1) {
					this.date = new Timestamp(System.currentTimeMillis());
				}
			}
		}
		//this.date.setTime(sDate.getCalendar().getTimeInMillis());
		this.idRol = rol;
	}
	public MerliContribution(int rol, String entity, Timestamp date) {
		// TODO Auto-generated constructor stub
		this.entity = entity;
		this.date = date;
		//this.date.setTime(sDate.getCalendar().getTimeInMillis());
		this.idRol = rol;
	}
	public MerliContribution(int rol, String entity, Date date) {
		// TODO Auto-generated constructor stub
		this.entity = entity;
		this.date = date2Timestamp(date);
		//this.date.setTime(sDate.getCalendar().getTimeInMillis());
		this.idRol = rol;
	}
	
	public MerliContribution(int rol, String entity, java.util.Date date) {
		// TODO Auto-generated constructor stub
		this.entity = entity;
		this.date = new Timestamp(date.getTime());
		//this.date.setTime(sDate.getCalendar().getTimeInMillis());
		this.idRol = rol;
	}

	public MerliContribution() {
		// TODO Auto-generated constructor stub
	}
/*
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
*/
	public String getEntity() {
		return entity;
	}
	
	public void setEntity(String entity) {
		this.entity = entity;
	}
	
	public String getRol() {
		return rol;
	}
	
	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getDescription() {
		// TODO Auto-generated method stub
		return "";
	}

	
	
	public void setDescription(String description) {
		this.description = description;
	}
	

	public int getIdRol() {
		return idRol;
	}
	

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	public String getIdRecurs() {
		return idRecurs;
	}
	

	public void setIdRecurs(String idRecurs) {
		this.idRecurs = idRecurs;
	}

	public void setDate(Date date) {
		// TODO Auto-generated method stub
		try{  
		if (date!=null)
			  this.date = date2Timestamp(date);
		}catch (ClassCastException e){
			  this.date = new Timestamp(date.getTime());
		}
	}
	
	
	public Timestamp date2Timestamp(Date date){
		Timestamp ts = new Timestamp(0);
		
		java.util.Date dateU = (java.util.Date) date; 
		
		ts = new Timestamp(dateU.getTime());
		
		return ts;
	}
	
	
	
	
}
