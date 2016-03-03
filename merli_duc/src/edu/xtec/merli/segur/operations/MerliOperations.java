package edu.xtec.merli.segur.operations;

import java.util.ArrayList;

public class MerliOperations implements IApplicationOperation {
	
	private String name = "MeRLí";
	/*
	 * Tots els identificadors de les operacions de la classe MeRLi han de tenir la forma 30xx.
	 */
	public static final int RECADD  = 3001;
	public static final int RECSET  = 3002;
	public static final int RECDEL  = 3003;
	public static final int TRADUCT = 3007;
	public static final int AGREGA = 3008;
	public static final int PUBLICAR = 3009;
	public static final int PENDEDIT = 3010;
	public static final int PENDVIEW = 3011;
	public static final int RECFISIC = 3012;
	
	public static final int PERMIS_AUTOADMIN  = 40075;

	public ArrayList getListOperations() {
		// TODO Auto-generated method stub
		ArrayList al = new ArrayList();
		al.add(new Integer(MerliOperations.RECADD));
		al.add(new Integer(MerliOperations.RECSET));
		al.add(new Integer(MerliOperations.RECDEL));
		al.add(new Integer(MerliOperations.TRADUCT));
		al.add(new Integer(MerliOperations.AGREGA));
		al.add(new Integer(MerliOperations.PUBLICAR));
		al.add(new Integer(MerliOperations.PENDEDIT));
		al.add(new Integer(MerliOperations.PENDVIEW));
		al.add(new Integer(MerliOperations.RECFISIC));
		
		return al;
	}

	public String getOperationName(int idOp) {
		// TODO Auto-generated method stub
		switch (idOp){
		case 3001: return "ADD RESOURCE";
		case 3002: return "SET RESOURCE";
		case 3003: return "DEL RESOURCE";
		case 3007: return "TRADUÏR";
		case 3008: return "ASIGN. AGREGA";
		case 3009: return "PUBLICAR";
		case 3010: return "EDIT PENDENTS";
		case 3011: return "VIEW PENDENTS";
		case 3012: return "FISICS";
		default: return "noOperation";
		}
		
	}

	public String getName() {
		return name;
	}
}
