package edu.xtec.merli.harvesting.servlet;

/*
 * MerliServlet.java
 *
 * Created on 2007/01/10
 * @author Sara Arjona (sarjona@xtec.cat)
 * 
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import edu.xtec.merli.harvesting.db.MerliHarvestingDB;

public abstract class MerliServlet extends HttpServlet{
	
	protected static Logger logger = Logger.getRootLogger();
	
	private MerliHarvestingDB oHarvestingDB;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
    
	    
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		try{
			this.request=request;
			this.response=response;
			processRequest();
		} catch(ServletException e){
			throw e;
		} catch(IOException e){
			throw e;
		} finally{
			freeConnection();			
		}
	}
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		try{
			this.request=request;
			this.response=response;
			processRequest();
		} catch(ServletException e){
			throw e;
		} catch(IOException e){
			throw e;
		} finally{
			freeConnection();			
		}
	}
    
	protected abstract void processRequest()
		throws ServletException, IOException;

	protected void freeConnection() {
		if (getMerliHarvestingDB()!=null){
			getMerliHarvestingDB().freeConnection();
		}
	}
	
	protected MerliHarvestingDB getMerliHarvestingDB(){
		if (oHarvestingDB==null){
			oHarvestingDB = new MerliHarvestingDB();
		}
		return oHarvestingDB;		
	}

	

}
