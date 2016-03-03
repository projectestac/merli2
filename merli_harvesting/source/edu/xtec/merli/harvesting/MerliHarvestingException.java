package edu.xtec.merli.harvesting;

public class MerliHarvestingException extends Exception{
	
	public static String ERROR_BAD_VERB = "badVerb";
	public static String ERROR_BAD_ARGUMENT = "badArgument";
	public static String ERROR_BAD_RESUMPTION_TOKEN = "badResumptionToken";
	public static String ERROR_NO_SET_HIERARCHY = "noSetHierarchy";
	public static String ERROR_NO_RECORDS_MATCH = "noRecordsMatch";
	public static String ERROR_CANNOT_DISSEMINATE_FORMAT = "cannotDisseminateFormat";
	public static String ERROR_ID_DOES_NOT_EXIST = "idDoesNotExist";
	

	protected String sError;
	protected String sDescription;
	protected OAIElement eOAI;

	public MerliHarvestingException(String sError){
		this.sError=sError;
	}
	
	public MerliHarvestingException(String sError, OAIElement eOAI){
		this(sError, null, eOAI);
	}	
	
	public MerliHarvestingException(String sError, String sDescription){
		this(sError, sDescription, null);
	}
	
	public MerliHarvestingException(String sError, String sDescription, OAIElement eOAI){
		this.sError=sError;
		this.sDescription=sDescription;
		this.eOAI=eOAI;
	}
	
	public String getError(){
		return sError;
	}
	
	public String getDescription(){
		if (sDescription==null){
			if (ERROR_BAD_VERB.equals(sError)) sDescription="Illegal OAI verb";
			if (ERROR_BAD_ARGUMENT.equals(sError)) sDescription="Illegal argument/s or missing required arguments";
			if (ERROR_BAD_RESUMPTION_TOKEN.equals(sError)) sDescription="The value of the resumptionToken argument is invalid or expired";
			if (ERROR_NO_SET_HIERARCHY.equals(sError)) sDescription="The repositoy does not support sets";
			if (ERROR_NO_RECORDS_MATCH.equals(sError)) sDescription="The combination of the values of the from, until, set and metadataPrefix arguments results in an empty list";
			if (ERROR_CANNOT_DISSEMINATE_FORMAT.equals(sError)) sDescription="The value of the metadataPrefix argument is not supported by the repository";
			if (ERROR_ID_DOES_NOT_EXIST.equals(sError)) sDescription="The value of the identifier argument is unknown or illegal in this repository";
		}
		return sDescription;
	}
	
	public OAIElement getOAIElement(){
		return eOAI;
	}
	
	
	
	
}
