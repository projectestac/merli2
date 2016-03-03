package simpple.xtec.web.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;



public class TipusFitxer {
   
   public static final String ENLINIA = "enlinia";
   public static final String FISIC = "fisic";
   public static final String ALTRESFISICS = "altresfisics";
   public static final String ALTRESENLINIA = "altresenlinia";
   public static Hashtable allTipusFitxer = null;	
   public static Hashtable allTipusIds = null;	
   public static Hashtable allGrupRecursos = null;	
   public static Hashtable allGrupIcones = null;
   public static Hashtable allTipusIcones = null;	
	public static Hashtable labelsTipusFisics = null;
	public static Hashtable labelsTipusOnline = null;
   // logger
   static final Logger logger = Logger.getLogger(simpple.xtec.web.util.TipusFitxer.class);
   public static final String TIPUS_FITXER_GRUP = "tipusGrup";
   public static final String ID_GRUP = "idgrup";

   public static void carregaTipusFitxer () {
	      String grupTipus = "";
	      String mimeTypes = "";
	      String nomGrup = "";
	      String nomTipus = "";
	      String icona = "";
	      String idTipus ="";
	      String idGrup ="";
	      String iconaTipus ="";
	      ArrayList allMimeTypes = null;

		Connection myConnection = null;
		Statement stmt = null;
		ResultSet rs = null;
	      try {

	    	myConnection = UtilsCercador.getConnectionFromPool();
	    	stmt = myConnection.createStatement();
//	    	rs = stmt.executeQuery("SELECT * FROM tipus_fitxers");
	    	rs = stmt.executeQuery("SELECT tf.ID AS ID, tf.nomgrup AS nomTipus, tf.MIMETYPE AS mimeType, tf.icona as iconaTipus, gt.ID AS idgrup, gt.NOM AS nomGrup, gt.TIPUS_RECURS AS grupType, gt.ICONA as icona FROM TIPUS_FITXERS tf, GRUPS_TIPUS gt WHERE tf.GRUPTYPE=gt.ID");

	        allTipusFitxer = new Hashtable();  
	        allGrupRecursos = new Hashtable();
	        allGrupIcones = new Hashtable();
	        allTipusIds = new Hashtable();
	        allTipusIcones = new Hashtable();
			labelsTipusFisics = new Hashtable();
			labelsTipusOnline = new Hashtable();
	        //Inicialitzem el grup de tipus de recursos (fisics, enlinia) - grupTipus
	        allGrupRecursos.put(FISIC, new ArrayList());
	        allGrupRecursos.put(ENLINIA, new ArrayList());
	        allGrupRecursos.put(TIPUS_FITXER_GRUP, new Hashtable());
	        allGrupRecursos.put(ID_GRUP, new Hashtable());
	        
	        
	         while (rs.next()) {
	        	 //Carrega de cada registre de tipus_fitxers.
	        	 nomTipus = rs.getString("nomTipus");
	        	 mimeTypes = rs.getString("mimeType");
	        	 nomGrup = rs.getString("nomGrup");
	        	 grupTipus = rs.getString("grupType");
	         	 icona = rs.getString("icona");
	         	 idTipus = String.valueOf((rs.getBigDecimal("id")).intValue());
	         	 idGrup = String.valueOf((rs.getBigDecimal("idgrup")).intValue());
	         	 iconaTipus = rs.getString("iconaTipus");

	        	 if (mimeTypes == null) {mimeTypes = "";}
	        	 if (grupTipus == null) {grupTipus = "";}
	        	 if (nomGrup == null) {nomGrup = "";}
	        	 if (nomTipus == null) {nomTipus = "";}
	        	 if (icona == null) {icona = "";}
	        	 if (idTipus == null) {idTipus = "";}
	        	 if (idGrup == null) {idGrup = "";}
	        	 if (iconaTipus == null) {iconaTipus = "";}
	        	 //Fi carrega de registres.

	         	 allTipusIds.put(nomTipus, idTipus);
	         	 allTipusIds.put(idTipus, nomTipus);
	         	 allTipusIds.put(UtilsCercador.converteixString(nomTipus), idTipus);
	         	 ((Hashtable)allGrupRecursos.get(ID_GRUP)).put(idGrup, nomGrup);         	 
	         	 	         	
	         	//Us heredat de l'allTipusFitxer.
	            allMimeTypes = new ArrayList();
	            StringTokenizer myTokenizer2 = new StringTokenizer(mimeTypes," ");
	            while (myTokenizer2.hasMoreTokens()) {
	            	allMimeTypes.add((String)myTokenizer2.nextToken());
	                }
	            allTipusFitxer.put(idTipus,allMimeTypes);
            	if (!allGrupIcones.containsKey(idGrup)){
    	            allGrupIcones.put(idGrup, icona);
            	}
				if (!allTipusIcones.containsKey(idTipus)) {
					allTipusIcones.put(idTipus, iconaTipus);
            	}
	            //Fi - us heredat allTipusFitxer
	            
	            
	            if (allGrupRecursos.containsKey(grupTipus)){
					if (!((ArrayList) allGrupRecursos.get(grupTipus)).contains(idGrup)) {
						((ArrayList) allGrupRecursos.get(grupTipus)).add(idGrup);
					}
	            	
	            	if (!allGrupRecursos.containsKey(idGrup)){
	            		allGrupRecursos.put(idGrup, new ArrayList());
	            	}
	            	addStringIfDiferent(((ArrayList)allGrupRecursos.get(idGrup)),idTipus);
	            	((Hashtable)allGrupRecursos.get(TIPUS_FITXER_GRUP)).put(idTipus,idGrup);
	            }else{
	            	//Error de tipus_recurs a grups_tipus
	            }
	            
				if (grupTipus.equals(FISIC)) {
					labelsTipusFisics.put(idTipus, nomTipus);
				} else if (grupTipus.equals(ENLINIA)) {
					labelsTipusOnline.put(idTipus, nomTipus);
				}
	         	 
	            }

	        	
	        } catch (Exception e) {
	        logger.error(e);
	        } finally {
	        try {	
	          if (rs != null) {
	           rs.close();
	           }
	          if (stmt != null) {
	           stmt.close();
	           }
	          if (myConnection != null) {
	           myConnection.close();
	           }
	          } catch (Exception e) {
	          logger.error(e);	  
	          }
	        }
	      }
   
   
   private static void addStringIfDiferent(ArrayList original, String nou) {
	   String nous = original.toString();

	   if (nous.indexOf(nou)<0){
		   original.add(nou);
	   }
   }


public static void carregaTipusFitxer4 (String nameFile) {

      String line = "";
      String tipusFitxer = "";
      String mimeTypes = "";
      ArrayList allMimeTypes = null;
      BufferedReader in = null;
      try {
    	nameFile = nameFile.replaceAll("cercador_educacio", "filetypes");
      	in = new BufferedReader(new FileReader(nameFile));    	
    	logger.error("Seeking..." + nameFile);
    	allTipusFitxer = new Hashtable();  
        // while ((line = propertiesFile.readLine()) != null) {
        while ((line = in.readLine()) != null) {
            line = new String(line.getBytes(), "UTF-8");

            StringTokenizer myTokenizer = new StringTokenizer(line,"#");
            tipusFitxer = (String)myTokenizer.nextToken();
            mimeTypes = (String)myTokenizer.nextToken();
            allMimeTypes = new ArrayList();
            StringTokenizer myTokenizer2 = new StringTokenizer(mimeTypes," ");
            while (myTokenizer2.hasMoreTokens()) {
            	allMimeTypes.add((String)myTokenizer2.nextToken());
                }
            logger.debug("Tipus fitxer ----> " + tipusFitxer);
            allTipusFitxer.put(tipusFitxer,allMimeTypes);

            }
        	
        } catch (Exception e) {
        logger.error(e);
        }
      }
	
   public static boolean isVoid () {
	   if (allTipusFitxer == null) {
		   return true; 
	       }
	   return false;
       }


	public static String getNomGrupOfTipus(String format) {
		String nomGrup = "";
		
		nomGrup = (String)((Hashtable) TipusFitxer.allGrupRecursos.get(TipusFitxer.TIPUS_FITXER_GRUP)).get(format);

		return nomGrup;
	}
	public static String getNomTipusOfGrup(String grup) {
		String nomGrup = "";
		String afegit="";
		ArrayList tipus = new ArrayList();
		if (TipusFitxer.FISIC.equals(grup) || TipusFitxer.ENLINIA.equals(grup)){
			for (int i=0;i<((ArrayList)allGrupRecursos.get(grup)).size();i++){
				if (afegit.indexOf("#"+(String)((ArrayList)allGrupRecursos.get(grup)).get(i))<0){
					afegit +="#"+((ArrayList)allGrupRecursos.get(grup)).get(i);
					tipus.addAll((ArrayList)allGrupRecursos.get(((ArrayList)allGrupRecursos.get(grup)).get(i)));
				}				
			}			
		}else if (TipusFitxer.ALTRESENLINIA.equals(grup)){
			for (int i=0;i<((ArrayList)allGrupRecursos.get(TipusFitxer.ENLINIA)).size();i++){
				if (afegit.indexOf("#"+(String)((ArrayList)allGrupRecursos.get(TipusFitxer.ENLINIA)).get(i))<0){
					afegit +="#"+((ArrayList)allGrupRecursos.get(TipusFitxer.ENLINIA)).get(i);
					tipus.addAll((ArrayList)allGrupRecursos.get(((ArrayList)allGrupRecursos.get(TipusFitxer.ENLINIA)).get(i)));
				}				
			}			
		}else if (TipusFitxer.ALTRESFISICS.equals(grup)){
			for (int i=0;i<((ArrayList)allGrupRecursos.get(TipusFitxer.FISIC)).size();i++){
				if (afegit.indexOf("#"+(String)((ArrayList)allGrupRecursos.get(TipusFitxer.FISIC)).get(i))<0){
					afegit +="#"+((ArrayList)allGrupRecursos.get(TipusFitxer.FISIC)).get(i);
					tipus.addAll((ArrayList)allGrupRecursos.get(((ArrayList)allGrupRecursos.get(TipusFitxer.FISIC)).get(i)));
				}				
			}			
		}else{
			tipus = (ArrayList)allGrupRecursos.get(grup);
		}		
		
		if (tipus!=null)
		for (int i=0;i<tipus.size();i++){
			if (!"".equals(nomGrup)){
				nomGrup += " or ";
			}
				nomGrup += tipus.get(i);
		}
		
		return " ( "+nomGrup + " ) ";
	}
}