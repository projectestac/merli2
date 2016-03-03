package edu.xtec.merli.semanticnet.basedades;

import org.apache.log4j.Logger;

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

public class ErrorBD{
 public static int MAX_INT = 25;
 public static int OK = 0;
 public static int OBJECTE_EXISTENT = -1;
 public static int OBJECTE_INEXISTENT = -2;
 public static int CODI_INEXISTENT = -3;
 public static int CODI_EXISTENT = -4;
 public static int DEFINICIO_INEXISTENT = -5;
 public static int DEFINICIO_EXISTENT = -6;
 public static int SINONIMIA_INEXISTENT = -7;
 public static int SINONIMIA_EXISTENT = -8;
 public static int MORFOLOGIA_INEXISTENT = -9;
 public static int MORFOLOGIA_EXISTENT = -10;
 public static int ERROR_SINONIM = -11;
 public static int FITXER_INEXISTENT = -12;
 public static int DRIVER_INEXISTENT = -13;
 public static int ERROR_CONNEXIO = -14;
 public static int CONNEXIO_OBERTA = -15;
 public static int CONNEXIO_TANCADA = -16;
 public static int BUSQUEDA_BUIDA = -17;
 public static int ERROR_INSERCIO = -18;
 public static int ERROR_FITXER = -19;
 public static int ERROR_CREA_PARAULA = -20;
 public static int ERROR_CREA_MORFOLOGIA = -21;
 public static int SINONIM_INEXISTENT = -22;
 public static int SINONIM_EXISTENT = -23;
 public static int HIPERHIPO_EXISTENT = -24;
 public static int HIPERHIPO_INEXISTENT = -25;

	private static final Logger logger = Logger.getRootLogger();//("xtec.duc");
 public ErrorBD() {
 }

 /**
  * Retorna l'explicació de l'error donat.
  * @param i Identificador de l'error
  * @return Explicació de l'error
  */
 public static String getError(int i){
	 logger.warn("Database error:"+i);
   switch (i){
     case 0: return "Ok";
     case 1: return "L'objecte.existeix.";
     case 2: return "L'objecte donat no existeix.";
     case 3: return "El codi donat no existeix";
     case 4: return "El codi donat ja existeix";
     case 5: return "La definició donada no existeix";
     case 6: return "La definició donada ja existeix";
     case 7: return "La sinonimia donada no existeix";
     case 8: return "La sinonimia donada ja existeix";
     case 9: return "La morfologia donada no existeix";
     case 10: return "La morfologia donada ja existeix";
     case 11: return "Error de sinonim";
     case 12: return "Fitxer inexistent";
     case 13: return "Driver inexistent";
     case 14: return "Error en la connexio";
     case 15: return "La connexió ja està oberta";
     case 16: return "La connexió ja està tancada";
     case 17: return "La consulta realitzada no retorna resultats";
     case 18: return "Error a l'insertar valors a la BBDD";
     case 19: return "Error al tractar el Fitxer";
     case 20: return "Error a l'insertar una paraula a la BBDD";
     case 21: return "Error a l'insertar una morfologia a la BBDD";
     case 22: return "El sinonim donat no existeix";
     case 23: return "El sinonim donat ja existeix";
     case 24: return "La ralació Hiper-Hipo donada ja existeix";
     case 25: return "La ralació Hiper-Hipo donada no existeix";




     default: return "Error Desconegut";
   }

 }
}
