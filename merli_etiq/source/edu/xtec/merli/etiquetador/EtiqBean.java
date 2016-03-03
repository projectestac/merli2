package edu.xtec.merli.etiquetador;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.basedades.RecursBD;


public class EtiqBean {
	
	

	private static final Logger logger = Logger.getRootLogger();
	
	/**
	 * 
	 */
	public EtiqBean(){

	}

	

	public ArrayList getTipusRecurs(){
		ArrayList recs = new ArrayList();
		
		RecursBD rbd =new RecursBD();
		
		try {
			recs = rbd.getResourceType(RecursBD.IDENTIFIERS);
		} catch (MerliDBException e) {
			e.printStackTrace();
		}
		return recs;		
	}
	
	public ArrayList getTipusRecursLabel(){
		ArrayList labs = new ArrayList();
		RecursBD rbd =new RecursBD();
		
		try {
			labs = rbd.getResourceType(RecursBD.LABELS);
		} catch (MerliDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return labs;	
	}
	
	public ArrayList getTipusRecursCheck(int idResource){
		ArrayList labs = new ArrayList();
		RecursBD rbd =new RecursBD();
		
		try {
			labs = rbd.getResourceType(idResource);
		} catch (MerliDBException e) {
			e.printStackTrace();
		}
		return labs;	
	}
	
	
	
	
	
	/**
	 * Retorna els valors en un llistat {valor, text} del camp property donat.
	 * 
	 * @param property Camp select que es vol crear.
	 * @return llistat de label-value [{value, label},{v,l}]
	 */
	public ArrayList executeVaLaOperation(String property) {
		// TODO Auto-generated method stub
		ArrayList al = new ArrayList();
		
		if (property.compareTo("llengua")==0) {
			return this.getVaLaLlengua();
		}
		if (property.compareTo("rolUsuari")==0) {
			return this.getVaLaRolUsuari();
		}
		if (property.compareTo("dificultat")==0) {
			return this.getVaLaDificultat();
		}
		if (property.compareTo("format")==0) {
			return this.getVaLaDificultat();
		}
		if (property.compareTo("llicencia")==0) {
			return this.getVaLaLlicencia();
		}
		if (property.compareTo("ambit")==0) {
			return this.getVaLaAmbit();
		}
		if (property.compareTo("llengua")==0) {
			return this.getVaLaLlengua();
		}
		if (property.compareTo("llengua")==0) {
			return this.getVaLaLlengua();
		}if (property.compareTo("llengua")==0) {
			return this.getVaLaLlengua();
		}
		
		
		
		return al;
	}

	
	private ArrayList getVaLaAmbit() {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList getVaLaLlicencia() {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList getVaLaDificultat() {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList getVaLaRolUsuari() {
		// TODO Auto-generated method stub
		return null;
	}

	private ArrayList getVaLaLlengua() {
		// TODO Auto-generated method stub
		ArrayList al = new ArrayList();
		ArrayList l = new ArrayList();
		l.add("x-none");
		l.add("");
		al.add(l);
		l = new ArrayList();
		l.add("ca");
		l.add("Català");
		al.add(l);
		l = new ArrayList();
		l.add("es");
		l.add("Castellà");
		al.add(l);
		l = new ArrayList();
		l.add("en");
		l.add("Anglès");
		al.add(l);
		l = new ArrayList();
		l.add("fr");
		l.add("Francès");
		al.add(l);
		l = new ArrayList();
		
		return al;
	}



	public String checkUrl(String url, String idRecurs) {
		// TODO Auto-generated method stub
		RecursBD rbd =new RecursBD();
		
		boolean exists = false;
		try {
			exists = rbd.existsUrl(url,idRecurs);			
		} catch (MerliDBException e) {
			// TODO Auto-generated catch block
			logger.error("Error while getting data to check URL. ->"+e);
			e.printStackTrace();
			return "error.etiq.url.validate";
		}catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Unexpected error. ->"+e);
			e.printStackTrace();
			return "etiq.url.cantvalidate";
		}	
		if (exists)
			return "etiq.url.existent";
		
		return "etiq.url.ok";
	}



	public String getTermesCurriculum(String valor) {
		// TODO Auto-generated method stub
		RecursBD rbd = new RecursBD();
		String result="";
		try{
			ArrayList aterms;
			Hashtable relt;
			aterms = rbd.getTermesCurriculum(valor);
			for (int i = 0;i<aterms.size();i++){
				relt = (Hashtable) aterms.get(i);
				if (relt.containsKey("idTerme") &&
						relt.containsKey("terme")){
					result += "{"+relt.get("idTerme");
					result += ","+relt.get("terme")+"}";
				}
			}
			
		}catch(Exception e){
			result="";
		}
						
		return result;
	}
	
	
	
}
