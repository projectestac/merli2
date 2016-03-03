package edu.xtec.gescurriculum.gestorcurriculum;

import org.apache.log4j.Logger;


public class DUCException extends Exception {


	
	
	private int codi;

	public static final int ERRORDELETENODE = 0;
	public static final int NONEXISTENTNODE = 1;
	public static final int ERRORSETNODE = 2;
	public static final int ERRORADDNODE = 3;
	public static final int ERRORMOVENODE = 4;

	private static final Logger logger = Logger.getRootLogger();//("xtec.duc");
	
	public DUCException (int i){
		codi = i;
	}
	
	public String getMessage(){
		String mess;
		
		switch (codi){
			case ERRORDELETENODE: 			mess = "ERROR_DELETE_NODE"; break;
			case ERRORSETNODE:				mess = "ERROR_SET_NODE"; break;
			case ERRORADDNODE: 				mess = "ERROR_ADD_NODE"; break;
			case ERRORMOVENODE: 			mess = "ERROR_MOVE_NODE"; break;
			default: 						mess = "SYSTEM_ERROR"; 
		}
		return mess;
	}
	
}
