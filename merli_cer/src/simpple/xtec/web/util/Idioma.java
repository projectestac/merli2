package simpple.xtec.web.util;

public class Idioma {
  String code = "";
  String name = "";

  public Idioma (String code, String name) {
	  this.code = code;
	  this.name = name;
      }
  
  public String getCode () {
	  return code;	  
      }

  public String getName () {
	  return name;	  
      }

}