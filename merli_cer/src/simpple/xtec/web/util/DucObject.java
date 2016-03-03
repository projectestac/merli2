package simpple.xtec.web.util;

import java.util.ArrayList;

public class DucObject {
	public int id;
	public String term;
	public int parent_id;	
	public ArrayList children;
	public boolean hasChilds;

	public String termEs;
	public String termEn;
	public String termOc;
	
	public String getTerm(String lang){
		if (lang==null)
			return term;
		else if ("es".equals(lang.toLowerCase())){
			if (termEs != null && !"".equals(termEs))
				return termEs;
		}
		else if ("en".equals(lang.toLowerCase())){
			if (termEn != null && !"".equals(termEn))
				return termEn;
		}
		else if ("oc".equals(lang.toLowerCase())){
			if (termOc != null && !"".equals(termOc))
				return termOc;
		}

		return term;
	}
}