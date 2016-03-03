package edu.xtec.merli.htmltag;



/**
 *
 * <p>Títol: HTMLSelectTag</p>
 * <p>Descripció: Component d'&agrave;rea de text, que genera codi HTML, per enriquir qualsevol text<p/>
 * <p>Departament d'Educació - XTEC</p>
 * @version 1.0
 *
 *
 */

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.taglib.html.MultiboxTag;
import org.apache.struts.util.LabelValueBean;

import edu.xtec.merli.etiquetador.EtiqBean;
import edu.xtec.merli.etiquetador.EtiqForm;
import edu.xtec.merli.utils.Utility;

/**
 * @jsp:tag name="Select" body-content="JSP"
 */
public class HTMLMultiboxMerliTag extends HTMLMultiboxTag implements Cloneable{
	
	
	  protected String[] getPossibleValues() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
			Object bean = pageContext.findAttribute(name);
			String[] res = BeanUtils.getArrayProperty(bean, possibleValues);	
			//((EtiqForm)bean).getPosTipRec();//
			return res;
	  }
	  
	  protected String[] getLabelsList() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
			Object bean = pageContext.findAttribute(name);
			String[] res = BeanUtils.getArrayProperty(bean, label);	
			//((EtiqForm)bean).getLabTipRec();//
			return res;  
	  }
	  
	  protected String[] getValuesList() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{	
		/* Object bean = pageContext.findAttribute("etiqForm");
		 int idRec =  Integer.parseInt(BeanUtils.getProperty(bean, "idRecurs"));
		 EtiqBean bean2 = (EtiqBean)pageContext.findAttribute("etiqBean");
		 ArrayList l = bean2.getTipusRecursCheck(idRec);
		 String[] res = new String[l.size()];
		 for (int i = 0; i< l.size();i++){
			 res[i] = (String) l.get(i);
		 }
		 
		 * String[] res = new String[lis.length];
		 for (int i = 0; i< lis.length;i++){
			 if (((String) lis[i]).equals("true"));
			 res[i] = (String) lis[i];
		 }
	 
		 return lis;
		 */
		 Object bean = pageContext.findAttribute(name);
		 return BeanUtils.getArrayProperty(bean, property); 
		
	  }	



}

