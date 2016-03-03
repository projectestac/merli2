package edu.xtec.merli.segur;

import java.util.ArrayList;

public class Permission {
	private int idPermission;
	private String permission;
	private String description;
	private ArrayList operations;
	
	
	
	public Permission(int idPermission, String permission) {
		super();
		// TODO Auto-generated constructor stub
		this.idPermission = idPermission;
		this.permission = permission;
	}



	public Permission(int idPermission, String permission, String description) {
		super();
		// TODO Auto-generated constructor stub
		this.idPermission = idPermission;
		this.permission = permission;
		this.description = description;
	}



	public String getDescription() {
		return description;
	}
	



	public void setDescription(String description) {
		this.description = description;
	}
	



	public int getIdPermission() {
		return idPermission;
	}
	



	public void setIdPermission(int idPermission) {
		this.idPermission = idPermission;
	}
	



	public String getPermission() {
		return permission;
	}
	



	public void setPermission(String permission) {
		this.permission = permission;
	}



	public ArrayList getOperations() {
		return operations;
	}
	



	public void setOperations(ArrayList operations) {
		this.operations = operations;
	}

	
	
}
