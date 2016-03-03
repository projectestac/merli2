package edu.xtec.merli.segur.operations;

import java.util.ArrayList;

public class DucOperations implements IApplicationOperation{
	/*
	 * Tots els identificadors de les operacions de la classe DUC han de tenir la forma 10xx
	 */

	public static final int SWAP = 1001;
	public static final int DEL = 1002;
	public static final int SELEC = 1003;
	public static final int ADD = 1004;
	public static final int SET = 1005;
	
	private static String name = "DUC";
	

	/*
	 * Cada cop q s'afageix una nova operació cal afegir-la als 2 metodes, al llistat 
	 * i a l'obtenció del nom de la operació.
	 */
	
	public ArrayList getListOperations(){
		ArrayList al = new ArrayList();
		al.add(new Integer(DucOperations.SWAP));
		al.add(new Integer(DucOperations.DEL));
		al.add(new Integer(DucOperations.SELEC));
		al.add(new Integer(DucOperations.ADD));
		al.add(new Integer(DucOperations.SET));
		return al;
	}
	
	
	public String getOperationName(int idOp){
		switch (idOp){
			case 1001: return "SWAP";
			case 1002: return "DEL";
			case 1003: return "SELEC";
			case 1004: return "ADD";
			case 1005: return "SET";
			
			default: return "noOperation";
		}
	}


	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
}
