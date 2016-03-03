package edu.xtec.merli.segur.operations;

import java.util.ArrayList;

public class MagicsOperations implements IApplicationOperation{
	
	private static String name = "Magics";
	
	public static final int USERADD = 2001;
	public static final int USERSET = 2002;
	public static final int USERDEL = 2003;
	public static final int PERMADD = 2004;
	public static final int PERMSET = 2005;
	public static final int PERMDEL = 2006;
	
	/*
	 * Tots els identificadors de les operacions de la classe MAGICS 
	 * han de tenir la forma 20xx
	 */
	/*
	 * Cada cop q s'afageix una nova operació cal afegir-la als 2 metodes, al llistat 
	 * i a l'obtenció del nom de la operació.
	 */
	
	public ArrayList getListOperations(){
		ArrayList al = new ArrayList();
		al.add(new Integer(MagicsOperations.USERADD));
		al.add(new Integer(MagicsOperations.USERSET));
		al.add(new Integer(MagicsOperations.USERDEL));
		al.add(new Integer(MagicsOperations.PERMADD));
		al.add(new Integer(MagicsOperations.PERMSET));
		al.add(new Integer(MagicsOperations.PERMDEL));
		return al;
	}
	
	
	public String getOperationName(int idOp){
		switch (idOp){
			case 2001: return "ADD USER";
			case 2002: return "SET USER";
			case 2003: return "DEL USER";
			case 2004: return "ADD PERM";
			case 2005: return "SET PERM";
			case 2006: return "DEL PERM";
			
			default: return "noOperation";
		}
	}


	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
}
