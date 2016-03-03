package edu.xtec.merli.ws.objects;

import java.util.ArrayList;

public class LOMES {
	private static final String RIGHTS_COPYRIGHT_VALUE = "creative commons: attribution - share alike";
	private static final String RIGHTS_COST_VALUE = "no";
	public static final String CONTRIBUTION_DESCRIPTION_CATALOG = "Fecha de catalogación";
	public static final String CONTRIBUTION_DESCRIPTION_PUBLIC = "Fecha de publicación en Agrega";
	private static final String RIGHTS_DESCRIPTION = "Los derechos no se aplican solo a España. Podría ser algo así: La utilización de estos contenidos es universal, gratuita y abierta, siempre y cuando se trate de un uso educativo no comercial. Las acciones, productos y utilidades derivadas de su utilización no podrán, en consecuencia, generar ningún tipo de lucro. Asimismo, es obligada la referencia a la fuente.";
	public static final String RIGHTS_ACCES_DESCRIPTION = "No existen restricciones"; 
	

	public static void mapejarContributionLOMES(Contribute contribute) {
		if (!APXTEC32.ROL_CREATOR.equals(contribute.getRole().getValue())){
			contribute.getRole().setValue(APXTEC32.ROL_VALIDATOR);	
			contribute.getDateTime().setDescription(LOMES.CONTRIBUTION_DESCRIPTION_CATALOG);
		}else{
			contribute.getDateTime().setDescription(LOMES.CONTRIBUTION_DESCRIPTION_PUBLIC);
		}
	}

	/**
	 * Mapeig per la integració al LOMes del camp 5.5 Educational.IntendedEndUserRole
	 * @param ieur
	 * @return
	 */
	public static String mapejarIntEndUserRolLOMES(String ieur) {
		if (APXTEC32.IEUR_AUTHOR.equals(ieur.toLowerCase())) return APXTEC32.IEUR_AUTHOR;
		if (APXTEC32.IEUR_COUNSELOR.equals(ieur.toLowerCase())) return APXTEC32.IEUR_TEACHER;
		if (APXTEC32.IEUR_LEARNER.equals(ieur.toLowerCase())) return APXTEC32.IEUR_LEARNER;
		if (APXTEC32.IEUR_MANAGER.equals(ieur.toLowerCase())) return APXTEC32.IEUR_MANAGER;
		if (APXTEC32.IEUR_PARENT.equals(ieur.toLowerCase())) return APXTEC32.IEUR_TEACHER;
		if (APXTEC32.IEUR_TEACHER.equals(ieur.toLowerCase())) return APXTEC32.IEUR_TEACHER;
		//if (APXTEC32.IEUR_OTHER.equals(ieur.toLowerCase())) 
		return null;
	}

	/**
	 * Mapeig per la integració al LOMes del camp 5.2 Educational.LearningResourceType
	 * @param lrt
	 * @return
	 */
	public static ArrayList mapejarIdLearningResourceType(String lrt) {
		ArrayList lrtList = new ArrayList();
		if (APXTEC32.LRT_ID_ASSESSMENT.equals(lrt.toLowerCase())){
			lrtList.add(APXTEC32.LRT_EXAM);
			lrtList.add(APXTEC32.LRT_SELF_ASSESSMENT);
		}else if (APXTEC32.LRT_ID_DRILL_PRACTICE.equals(lrt.toLowerCase())){
			lrtList.add(APXTEC32.LRT_EXERCICE);
		}else if (APXTEC32.LRT_ID_INFO_RESOURCE.equals(lrt.toLowerCase())){
			lrtList.add(APXTEC32.LRT_SLIDE);
			lrtList.add(APXTEC32.LRT_FIGURE);
		}else if (APXTEC32.LRT_ID_GLOSSARY.equals(lrt.toLowerCase())){
			lrtList.add(APXTEC32.LRT_NARRATIVE_TEXT);
		}else if (APXTEC32.LRT_ID_GUIDE.equals(lrt.toLowerCase())){
			lrtList.add(APXTEC32.LRT_LECTURE);
		}else if (APXTEC32.LRT_ID_EXPLORATION.equals(lrt.toLowerCase())){
			lrtList.add(APXTEC32.LRT_SIMULATION);
			lrtList.add(APXTEC32.LRT_EXPERIMENT);
		}else if (APXTEC32.LRT_ID_OPEN_ACTIV.equals(lrt.toLowerCase())){
			lrtList.add(APXTEC32.LRT_PROBLEM_STM);
		}else if (APXTEC32.LRT_ID_TOOL.equals(lrt.toLowerCase())){
			lrtList.add(APXTEC32.LRT_DIAGRAM);
			lrtList.add(APXTEC32.LRT_GRAPH);
			lrtList.add(APXTEC32.LRT_INDEX);
			lrtList.add(APXTEC32.LRT_SLIDE);
			lrtList.add(APXTEC32.LRT_TABLE);
		}
		return lrtList;
	}
	

	/**
	 * Mapeig per la integració al LOMes del camp 5.6 Educational.Context
	 * @param context
	 * @return
	 */
	public static String mapejarContextLOMES(String context) {
		if (APXTEC32.CTX_PRESCHOOL.equals(context.toLowerCase()))
			return APXTEC32.CTX_SCHOOL;
		if (APXTEC32.CTX_COMPULSORY_EDU.equals(context.toLowerCase()))
			return APXTEC32.CTX_SCHOOL;
		if (APXTEC32.CTX_HIGHER_EDU.equals(context.toLowerCase()))
			return APXTEC32.CTX_HIGHER_EDU;
		if (APXTEC32.CTX_PROFES_DEVELOP.equals(context.toLowerCase()))
			return APXTEC32.CTX_TRAINING;
//		if (APXTEC32.CTX_SPECIAL_EDU.equals(context.toLowerCase()))
//			return APXTEC32.CTX_OTHER;
//		if (APXTEC32.CTX_VOCATIONAL_EDU.equals(context))
//			return APXTEC32.CTX_OTHER;
//		if (APXTEC32.CTX_DISTANCE_EDU.equals(context))
//			return APXTEC32.CTX_OTHER;
//		if (APXTEC32.CTX_CONTINUING_EDU.equals(context))
//			return APXTEC32.CTX_OTHER;
//		if (APXTEC32.CTX_LIBRARY.equals(context))
//			return APXTEC32.CTX_OTHER;
//		if (APXTEC32.CTX_EDU_ADMINISTR.equals(context))
//			return APXTEC32.CTX_OTHER;
//		if (APXTEC32.CTX_POLICY_MAKING.equals(context))
//			return APXTEC32.CTX_OTHER;
//		if (APXTEC32.CTX_OTHER.equals(context))
//			return APXTEC32.CTX_OTHER;
		
		
		return APXTEC32.CTX_OTHER;
	}

	public static void mapejarRights(Rights rights) {
		rights.setLomEs(true);
		rights.getDescription().setString(LOMES.RIGHTS_DESCRIPTION);
		rights.getCost().setValue(LOMES.RIGHTS_COST_VALUE);
		rights.getCopyRightAndOtherRestrictions().setValue(LOMES.RIGHTS_COPYRIGHT_VALUE);
	}

	
}
