package edu.xtec.merli.ws.objects;

public class APXTEC32 {
	public static final String DEFAULT_LANG = "ca";
	public static final String MS_APXTEC = "APXTECv3.2";
	public static final String LOMes = "LOM-ESv1.0";
	
	public static final String ACTION = "remoteplay";
	public static final String UNIVERSAL = "universal";
	public static final String MEC = "MEC";
	public static final String LOM = "LOMv1.0";	

	public static final String MERLI = "MERLI";
	public static final String CELEBRATE = "CELEBRATE";
	
	public static final String CONTENT = "content";
	public static final String AREA = "area";
	public static final String LEVEL = "level";
	
	public static final String SALT_LINIA = "\n";
	public static final int MAXPOSITION = 9999;
	public static final int LAST = -1;
	

	public static final String DATE_FORMAT = "yyyy-MM-dd";//"dd-MM-yyyy";

	public static final int IDENTIFIERS = -1;
	public static final int LABELS = -2;
	public static final int MAXIM = -3;
	public static final int MINIM = -4;	

	public static final int ESCOLAR = 1;
	public static final int ESPECIAL = 2;
	public static final int ADMINISTRATIU = 3;

	public static final String THESAURUS = "ETB";
	public static final String THESAURUSLOMes = "ETB-LRE MEC-CCAA V.1.0";
	public static final String DUC = "DUC";
	public static final String DUC_COMPETENCIES = "CB-DUC";
	public static final String AMBIT = "AMBIT";
	public static final String LOM_VALUE = "LOMv1.0";
	public static final String LRE3_VALUE = "LREv3.0";
	public static final String DUC_CONTENT = "content";
	public static final String DUC_AREA = "area";
	public static final String DEPT_EDUCACIO = "Departament d'Ensenyament";

	public static final String LOM_CREATOR = "creator";
	public static final LangString DUC_DESCRIPTION_CA = new LangString("Continguts relacionats del currículum","ca");
	public static final LangString DUC_DESCRIPTION_ES = new LangString("Contenidos relacionados del currículum","es");
	public static final LangString DUC_DESCRIPTION_EN = new LangString("Related curriculum contents","en");
	public static final LangString ETB_DESCRIPTION_CA = new LangString("Termes relacionats del tesaurus","ca");
	public static final LangString ETB_DESCRIPTION_ES = new LangString("Términos relacionados del tesauro","es");
	public static final LangString ETB_DESCRIPTION_EN = new LangString("Related thesaurus terms","en");
	
	public static final String ARBOL_CURRICULAR = "Árbol curricular LOE 2006";
	public static final String COMPETENCIES_DUC = "Competències bàsiques-DUC";

	public static final String STX_RETORNAT = "denied";
	public static final String STX_DENEGAT = "unavailable";
	public static final String STX_ESBORRANY = "draft";
	public static final String STX_PENDENT = "waiting";
	public static final String STX_PER_REVISAR = "draft";
	public static final String STX_EN_REVISIO = "revision";
	public static final String STX_REVISANT = "final";//C-LOMes
	public static final String STX_ACCEPTAT = "revised";//C-LOMes
	
	public static final String STX_CA_RETORNAT = "Retornat";
	public static final String STX_CA_DENEGAT = "Denegat";
	public static final String STX_CA_ESBORRANY = "Esborrany";
	public static final String STX_CA_PENDENT = "Pendent";
	public static final String STX_CA_PER_REVISAR = "Per revisar";
	public static final String STX_CA_EN_REVISIO = "En revisio";
	public static final String STX_CA_REVISANT = "Acceptat";//C-LOMes
	public static final String STX_CA_ACCEPTAT = "Revisat";//C-LOMes
	
	public static final int ST_ID_RETORNAT = -1;
	public static final int ST_ID_DENEGAT = -2;
	public static final int ST_ID_ESBORRANY = 0;
	public static final int ST_ID_PENDENT = 10;
	public static final int ST_ID_PER_REVISAR = 1;
	public static final int ST_ID_EN_REVISIO = 2;
	public static final int ST_ID_REVISANT = 3;//C-LOMes
	public static final int ST_ID_ACCEPTAT = 4;//C-LOMes
	
	
	public static final String ELN = "ELNv1.1";

	public static final String CAMP_PARE = "id_nodecur_level";
	
	public static final Object ID_CAMP_PARE = "0";
	
	public static final int TIPUS_LEVEL = 0;
	public static final int TIPUS_AREA = 1;
	public static final int TIPUS_CONTENT =2;
	
	public static final int ESTAT_DENEGAT = -2;
	public static final int ESTAT_ESBORRAT = -2;
	
	public static final String DEFAULT_AGGREGATION_VALUE = "2";
	
	public static final String CTX_PRESCHOOL = "pre-school";
	public static final String CTX_COMPULSORY_EDU = "compulsory education";
	public static final String CTX_SPECIAL_EDU = "special education";
	public static final String CTX_VOCATIONAL_EDU = "vocational education";
	public static final String CTX_HIGHER_EDU = "higher education";
	public static final String CTX_DISTANCE_EDU = "adult / distance education";
	public static final String CTX_CONTINUING_EDU = "continuing education";
	public static final String CTX_PROFES_DEVELOP = "professional development";
	public static final String CTX_LIBRARY = "school libraries / documentation";
	public static final String CTX_EDU_ADMINISTR = "educational administration";
	public static final String CTX_POLICY_MAKING = "policy making";
	public static final String CTX_OTHER = "other";	
	public static final String CTX_SCHOOL = "school";
	public static final String CTX_TRAINING = "training";
	
	public static final String IEUR_AUTHOR = "author";
	public static final String IEUR_TEACHER = "teacher";
	public static final String IEUR_LEARNER = "learner";
	public static final String IEUR_COUNSELOR = "counselor";
	public static final String IEUR_PARENT = "parent";
	public static final String IEUR_MANAGER = "manager";
	public static final String IEUR_OTHER = "other";

	public static final String ROL="role";
	public static final String ROL_PUBLISHER="publisher";
	public static final String ROL_AUTHOR = "author";
	public static final String ROL_CREATOR = "creator";
	public static final String ROL_VALIDATOR = "validator";
	public static final String ROL_EDITOR = "editor";
	public static final int IROL_CREATOR = 3;
	
	public static final String PURPOSE ="purpose";
	public static final String PURP_DISCPLINE ="discipline";
	
	public static final String LRT_ID_ASSESSMENT = "1";
	public static final String LRT_ID_DRILL_PRACTICE = "2";
	public static final String LRT_ID_INFO_RESOURCE = "3";
	public static final String LRT_ID_GLOSSARY = "4";
	public static final String LRT_ID_GUIDE = "5";
	public static final String LRT_ID_EXPLORATION = "6";
	public static final String LRT_ID_OPEN_ACTIV = "7";
	public static final String LRT_ID_TOOL = "8";
	
	public static final String LRT_ASSESSMENT = "assessment";
	public static final String LRT_DRILL_PRACTICE = "drill and practice";
	public static final String LRT_INFO_RESOURCE = "information resource";
	public static final String LRT_GLOSSARY = "glossary";
	public static final String LRT_GUIDE = "guide";
	public static final String LRT_EXPLORATION = "exploration";
	public static final String LRT_OPEN_ACTIV = "open activity";
	public static final String LRT_TOOL = "tool";
	public static final String LRT_EXAM = "exam";
	public static final String LRT_SELF_ASSESSMENT = "self assessment";
	public static final String LRT_EXERCICE = "exercice";
	public static final String LRT_SLIDE = "slide";
	public static final String LRT_FIGURE = "figure";
	public static final String LRT_NARRATIVE_TEXT = "narrative text";
	public static final String LRT_LECTURE = "lecture";
	public static final String LRT_SIMULATION = "simulation";
	public static final String LRT_EXPERIMENT = "experiment";
	public static final String LRT_PROBLEM_STM = "problem statement";
	public static final String LRT_DIAGRAM = "diagram";
	public static final String LRT_GRAPH = "graph";
	public static final String LRT_INDEX = "index";
	public static final String LRT_TABLE = "table";

	public static final String PLATAFORMA_AGREGA = "Plataforma Agrega";
	public static final String DEFAULT_DURATION_NAME = "duration";
	public static final String DEFAULT_LEAR_RES_TYPE_VALUE = "database";
	public static final String DEFAULT_INTERACTIVITY_LEVEL_VALUE = "medium";
	
}
