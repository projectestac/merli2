package edu.xtec.merli.segur.operations;

import java.util.ArrayList;

import edu.xtec.merli.segur.operations.DucOperations;
import edu.xtec.merli.segur.operations.MagicsOperations;
import edu.xtec.merli.segur.operations.MerliOperations;

public class Operations{

	public static final DucOperations DUC = new DucOperations();
	public static final MagicsOperations MAGICS = new MagicsOperations();
	public static final MerliOperations MERLI = new MerliOperations();	
	
	
	public static ArrayList getApplications(){
		ArrayList al = new ArrayList();
		al.add(DUC);
		al.add(MAGICS);
		al.add(MERLI);
		
		return al;
	}
	
}


