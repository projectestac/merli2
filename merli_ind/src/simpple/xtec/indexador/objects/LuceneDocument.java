package simpple.xtec.indexador.objects;

import java.util.Hashtable;

import org.apache.log4j.Logger;

public class LuceneDocument {
  public Hashtable elementValues = null;
  public Hashtable attributeValues = null;
  public String contingut = "";
  public String titol = "";
  public String titolEs = "";
  public String titolEn = "";
  public String titolOc = "";
  public String autor = "";
  public String descripcio = "";
  public String descripcioEs = "";
  public String descripcioEn = "";
  public String descripcioOc = "";
  public String urlDoc = "";
  public String idRecurs = "";
  public int numComentaris = 0;
  public int numVisites = 0;    
  public float puntuacio = (float)0.0;
  public String comentaris = "";
  public int duracio = 0; // en minuts
  public String dataPublicacio = "";
  public String dataCatalogacio = "";  
  public String duc = "";
  public String ambit = "";  
  public String formatRecurs = "";
  public String idioma = "";
  
  public String unitatIds = "";
  public String unitatNoms = "";
  public String idfisics = "";
  public String coverage = "";
  public String carFisiques = "";
  public String editor = "";
  public String recurs = "enlinia";
  public String disponibleIds = "";
  public String disponibleNoms = "";
  public String catalogador = "";
  public String relacioIds = "";
  public String relacioDesc= "";
  public String relacioTipus = "";
  public String termsEs = "";
  public String termsOc = "";
  public String termsEn = "";
  public String termsCa = "";
  public String keysEs = "";
  public String keysOc = "";
  public String keysEn = "";
  public String keysCa = "";
  
  public String tipusRecurs = "";
  public String context = "";

  public String llicDesc="";
  public String llicId="";
  public String llicUrl="";
  
  static final Logger logger = Logger.getLogger("simpple.educacio.objects.LuceneDocument");  
  
  public LuceneDocument () {
    elementValues = new Hashtable();
    attributeValues = new Hashtable();
    }  
  
  public void addElement (String name, String value) {
    try {
      if (elementValues.containsKey (name)) {
         value += " " + (String)elementValues.get(name);
         }
      elementValues.put(name, value);  
      } catch (Exception e) {
      logger.error(e);
      }
    }

  public void addAttribute (String name, String value) {
    try {	
      attributeValues.put(name, value);
      } catch (Exception e) {
      logger.error(e);
      }
    }  

  
  }