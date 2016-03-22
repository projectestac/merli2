package edu.xtec.semanticnet;

public class SemanticException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3977017357631828278L;
	private int code;
	private String description;
	private Object object;
	
	public static final int NODYNAMIC = 0;
	public static final int NONEXISTENTNODE = 1;
	public static final int EXISTENTNODE = 2;
	public static final int NONEXISTENTRELATIONTYPE = 3;
	public static final int DATASOURCEMISSED = 4;
	public static final int DATASOURCEINSTANTIATION = 5;
	public static final int DATASOURCEILLEGALACCES = 6;
	public static final int DATASOURCENOTFOUND = 7;
	public static final int XMLERROR = 8;
	public static final int XMLNOTLOADED = 9;
	public static final int DATABASECONNECTIONERROR = 10;
	public static final int EXISTDIFERENTNODE = 11;
	public static final int NONEXISTENTNODETYPE = 12;
	public static final int NONDELETEDRELATION = 13;
	public static final int XMLPARSERERROR = 14;

	public SemanticException(String string) {
		super(string);
	}

	public int getCode() {
		return code;
	}
	

	public void setCode(int code) {
		this.code = code;
		switch (code){
			case SemanticException.NODYNAMIC: 
				this.setDescription("Cal carregar el camp 'initNode' a les propietats del SemanticNet.");break;
			case SemanticException.NONEXISTENTNODE: 
				this.setDescription("El node sol·licitat no existeix.");break;
			case SemanticException.EXISTENTNODE: 
				this.setDescription("El node sol·licitat ja existeix.");break;
			case SemanticException.NONEXISTENTRELATIONTYPE: 
				this.setDescription("El tipus de relació trobat no existeix."); break; 
			case SemanticException.DATASOURCEMISSED: 
				this.setDescription("Cal carregar el camp 'class' a les propietats del SemanticNet."); break; 
			case SemanticException.DATASOURCEINSTANTIATION: 
				this.setDescription("Error a l'instanciar la classe donada pel Repositori de dades."); break; 
			case SemanticException.DATASOURCEILLEGALACCES: 
				this.setDescription("Accès il·legal al repositori de dades donat."); break;  
			case SemanticException.DATASOURCENOTFOUND: 
				this.setDescription("El RepositoriDades donat no es pot localitzar."); break; 
			case SemanticException.XMLERROR: 
				this.setDescription("L'XML donat no ès vàlid, no pot ser validat per l'esquema 'semanticNet.xsd'."); break;
			case SemanticException.XMLNOTLOADED: 
				this.setDescription("L'XML donat no s'ha pogut carregar. Cal comprovar que la direcció sigui correcta."); break;
			case SemanticException.DATABASECONNECTIONERROR:
				this.setDescription("Error en la connexió amb la base de dades.");
			case SemanticException.EXISTDIFERENTNODE:
				this.setDescription("Existeix un node amb el mateix identificador ja carregat.");
			case SemanticException.NONEXISTENTNODETYPE:
				this.setDescription("El tipus de node sol·licitat no existeix.");		
			case SemanticException.NONDELETEDRELATION:
				this.setDescription("La relació sol·licitada s'ha pogut eliminar.");	
			case SemanticException.XMLPARSERERROR:
				this.setDescription("No s'ha pogut carregar el parser correctament.");				
		}
	}
	

	public String getDescription() {
		return description;
	}
	

	private void setDescription(String description) {
		this.description = description;
	}

	public Object getObject() {
		return object;
	}
	

	public void setObject(Object object) {
		this.object = object;
	}
	
	
}
