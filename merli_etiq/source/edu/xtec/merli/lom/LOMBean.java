package edu.xtec.merli.lom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.xtec.merli.RecursMerli;
import edu.xtec.merli.basedades.RecursBD;

public class LOMBean {
	protected static Logger logger = Logger.getRootLogger();
	public static String P_RESOURCE_ID = "resource_id";
	
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	protected RecursMerli oResource = null;
	
	public boolean init(HttpServletRequest request, HttpServletResponse response){
		this.request=request;
		this.response=response;
		return start();
	}
	
	protected boolean start(){
		boolean bOk = false;
		try{
			getResource();
			bOk = true;
		}catch (Exception e){
			e.printStackTrace();
		}
		return bOk;
	}
	
	public RecursMerli getResource(){
		if (oResource==null){
			if (request.getParameter(P_RESOURCE_ID)!=null){
				int iId = iId = Integer.parseInt(request.getParameter(P_RESOURCE_ID));
				oResource = getResource(iId);
			}
		}
		return oResource;
	}

	
	/**
	 *  
	 * @param iId
	 * @return 
	 */
	public RecursMerli getResource(int iId){
		try{
			if (oResource==null){
				RecursBD rbd =new RecursBD();
				oResource = rbd.getRecurs(iId);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return oResource;
	}
	
}
