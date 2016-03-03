package edu.xtec.merli.basedades;

/**
* <p>Title: Errors BD Vocabulari</p>
* <p>Description:
* Codis d'error. Tots els codis d'error es troben aqui, i poden ser cridats
* de manera static.
* </p>
* <p>Copyright: Copyright (c) 2004</p>
* <p>Company: pie</p>
* @author acanals5
* @version 1.0
*/

public class MerliDBException extends Exception{
	
	
	private int codi;
	
 public static final int MAX_INT = 25;
 public static final int OK = 0;
 public static final int OBJECTE_EXISTENT = 1;
 public static final int OBJECTE_INEXISTENT = 2;
 public static final int CODI_INEXISTENT = 3;
 public static final int CODI_EXISTENT = 4;
 public static final int CAMPS_OBLIGATORIS = 7;
 public static final int FITXER_INEXISTENT = 12;
 public static final int DRIVER_INEXISTENT = 13;
 public static final int ERROR_CONNEXIO = 14;
 public static final int CONNEXIO_OBERTA = 15;
 public static final int CONNEXIO_TANCADA = 16;
 public static final int BUSQUEDA_BUIDA = 17;
 public static final int ERROR_INSERCIO = 18;
 public static final int ERROR_FITXER = 19;
 public static final int ERROR_SQL = 20;
 public static final int ADRECA_NO_PERMESA = 21;
 public static final int PARAM_INCORRECTE = 22;

 public static final int DELETEERROR = 26;
 
 
 
 public MerliDBException(int i){
		codi = i;
	}

 /**
  * Retorna l'explicació de l'error donat.
  * @param i Identificador de l'error
  * @return Explicació de l'error
  */
 public String getMessage(){
		String mess;
		
   switch (codi){
     case 0: return "Ok";
     case 1: return "L'objecte existeix.";
     case 2: return "L'objecte donat no existeix.";
     case 3: return "El codi donat no existeix";
     case 4: return "El codi donat ja existeix";
     case 5: return "La definició donada no existeix";
     case 6: return "La definició donada ja existeix";
     case 7: return "Camps obligatoris buits";
     case 12: return "Fitxer inexistent";
     case 13: return "Driver inexistent";
     case 14: return "Error en la connexio";
     case 15: return "La connexió ja està oberta";
     case 16: return "La connexió ja està tancada";
     case 17: return "La consulta realitzada no retorna resultats";
     case 18: return "Error a l'insertar valors a la BBDD";
     case 19: return "Error al tractar el Fitxer";
     case 20: return "Error valors consulta a BBDD";
	 case 21: return "Adreça ip no permesa";
	 case 22: return "Els paràmetres enviats no són del tipus correcte";
	 
     case 26: return "DeleteObjectException";

     default: return "UnknownError";
   }

 }
}
