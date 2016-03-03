package simpple.xtec.web.util;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class ComentariObject {
	public long id;
	public String titol;	
	public String comentari;
	public String autor;	
	public String idRecurs;
	public String nomUsuari;
	public int puntuacio;
	public int suspens;
	public String dataEdicio;
	
	 static final Logger logger = Logger.getLogger(simpple.xtec.web.util.ComentariObject.class);
// 135	 
	 public String cutString (String token, int tokenLength) {
	   String fixed = token;
       try {
    	   if (token.length() >= tokenLength) {
    		  fixed = token.substring(0, (tokenLength - 1)) + " " + cutString(token.substring((tokenLength - 1), token.length()), tokenLength);
    	      }
          } catch (Exception e) {
          logger.error(e);	  
          }
       return fixed;   
	}
	
	public void setComentari (String comentari, int tokenLength) {
		StringTokenizer myTokenizer = new StringTokenizer(comentari);
		String nextToken = "";
		String comentariFixed = "";
		try {
			while (myTokenizer.hasMoreTokens()) {
				nextToken = (String)myTokenizer.nextToken();
			    nextToken = cutString(nextToken, tokenLength);
				comentariFixed += nextToken + " ";
			   }
			
	      } catch (Exception e) {
	      logger.error(e);			
		  }
		this.comentari = comentariFixed;		
	    }
}