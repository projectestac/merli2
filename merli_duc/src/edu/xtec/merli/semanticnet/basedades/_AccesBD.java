package edu.xtec.merli.semanticnet.basedades;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.xtec.merli.utils.Utility;



public class _AccesBD {
	  private static int error;
	
	  
	  /**
	   * Realitza un commit i torna a posar el contador de insercions a 0.
	   */
	  private static void realitzarCommit(Connection connection){
	    try{
	      connection.commit();
	      //numInsercions = 0;
	    }catch(SQLException sqle){
	      error = ErrorBD.ERROR_CONNEXIO;
	    }
	  }


	  /**Retorna el codi d'error donat en la última execució d'una funció.
	   *
	   * @return Codi d'error.
	   */
	  public static int getError(){
	    return error;
	  }

	
	  /**
	   * Executa una inserció a la BD i retorna el codi d'error.
	   * @param taula
	   * @param query valors de la inserció a realitzar
	   * @param connection
	   * @return codi d'error
	   * @throws Exception
	   */
	  public static int executeInsert(String taula, String valors,Connection connection) throws Exception{
		PreparedStatement pstInsert;		  		
		try {
	        //pstInsert = connection.prepareStatement("INSERT INTO "+taula+" VALUES ("+valors+")");	
			pstInsert = connection.prepareStatement("INSERT INTO ? VALUES (?)");
			pstInsert.setString(1,taula);
			pstInsert.setString(2,valors);
			pstInsert.execute();
	        connection.commit();
	        pstInsert.close();
	      }
	      catch (SQLException e) {
	        error = e.getErrorCode();
	        if (e.getErrorCode() == 1){
	          error = ErrorBD.OBJECTE_EXISTENT;
	        }else{
	          try {
	            connection.rollback();
	          }
	          catch (SQLException sqle) {
	            error = ErrorBD.ERROR_CONNEXIO;
	          }
	          error = ErrorBD.ERROR_INSERCIO;
	        }
	      }
		  return error;
	  }

	  /**
	   * Executa una consulta a la BD i en retorna el camp donat.
	   * En cas d'error aquest queda registrat a AccesBD.error
	   *
	   * @param query Consulta a realitzar
	   * @param camp Camp del resultat a retornar.
	   * @return Valor del camp consultat
	   */
	  public static String executeQuery(String query, String camp, Connection connection) throws Exception{
	    ResultSet rs;
	    String result ="0";
		Statement stmt;
		
		stmt=connection.createStatement();
		
	    error = ErrorBD.BUSQUEDA_BUIDA;
		try{
	      rs = stmt.executeQuery(query);		  
	      while (rs.next()) {
	        result = rs.getString(camp);
	        error = ErrorBD.OK;
	      }
		  stmt.close();
	    }catch(Exception e){
	      error = ErrorBD.ERROR_CONNEXIO;
	    }
	    return result;
	  }
	  
	  /**
	   * Retorna 0 si l'element "camp" de "taula" té el valor "valor".
	   * @param taula Taula on fer la cerca
	   * @param camp Camp sobre el que cercar
	   * @param valor Valor cercat.
	   * @param condicio Condició que han de complir les entrades.
	   * @return >1: existeix, 0: no existeix.
	   */
	  public static int executeExist(String taula, String camp, String valor, String condicio, Connection connection) throws Exception{
		  int error = 0;
		  int res = 0;
		  ResultSet rs;
		  Statement stmt;
		  stmt=connection.createStatement();
		  try{
			  rs = stmt.executeQuery("SELECT count(*) FROM "+taula+" WHERE "+camp+"='"+valor+"' "+condicio);
			  rs.next();
			  res = rs.getInt(1);
			  stmt.close();
		  }catch(Exception e){error = ErrorBD.ERROR_CONNEXIO; res = error; e.printStackTrace();}
		  
		  return res;
	  }


	/**
	 * Retorna un llistat complert de la "taula" donada amb els "camps" i ordenat segons "order".
	 * @param taula Taula sobre la que realitzar la consulta
	 * @param camps Camps que es retornaran.
	 * @param order Ordre de la consulta.
	 * @param connection Connexio a la BD
	 * @return Collection
	 */
	public static Map getFullLlistat(String taula, List camps, String order, Connection connection) throws Exception {
		String query;
		String sCamps = camps.toString().replace('[',' ').replace(']',' ');
		ArrayList res = new ArrayList();
		query = "SELECT "+sCamps+" FROM "+taula+" ORDER BY "+order;
		Statement stmt=connection.createStatement();
		ResultSet rs;
		rs = stmt.executeQuery(query);
		Map m = new LinkedHashMap();
		m = Utility.toMap(rs,camps);
		stmt.close();

		return m;
	}


	public static Map getObject(String taula, List camps, String condicio, Connection connection) throws Exception{
		String query;
		String sCamps = camps.toString().replace('[',' ').replace(']',' ');
		query =  "SELECT "+sCamps+" FROM "+taula+" WHERE "+condicio;

		Statement stmt = connection.createStatement();
		ResultSet rs;
		
		rs = stmt.executeQuery(query);
		Map m = new LinkedHashMap();
		m = Utility.toMap(rs,camps);
		stmt.close();
		
		return m;
	}


	public static int executeUpdate(String taula, String condicio, String query, Connection connection) throws Exception {
		// TODO Auto-generated method stub
		PreparedStatement pstInsert;	
		error =0;
		try {
	        pstInsert = connection.prepareStatement("UPDATE "+taula+" SET "+query+" WHERE "+ condicio);		
			pstInsert.execute();
	        connection.commit();
	        pstInsert.close();
	      }
	      catch (SQLException e) {
	        error = e.getErrorCode();
			e.printStackTrace();
	        try {
	            connection.rollback();
	          }
	          catch (SQLException sqle) {
	            error = ErrorBD.ERROR_CONNEXIO;
	          }
	        error = ErrorBD.ERROR_INSERCIO;
	       
	      }
		  return error;
	}


	public static int executeDelete(String taula, String condicio, Connection connection) {
		// TODO Auto-generated method stub
		String query =  "delete from "+taula+" where "+condicio;
		int rs = 0;
		
		Statement stmt;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}


	public static Map getJoin(ArrayList camps, String taula1, String taula2, String camp, String condicio, Connection c) throws SQLException {
		// TODO Auto-generated method stub
		String query;
		String sCamps = Utility.toListDB(camps,"a");
		ArrayList res = new ArrayList();
		query = "SELECT "+sCamps+" FROM "+taula1+" a, "+taula2+
									" b WHERE a."+camp+" = b."+camp+" AND b."+condicio;
		Statement stmt=c.createStatement();
		ResultSet rs;		
		rs = stmt.executeQuery(query);
		Map m = new LinkedHashMap();
		m = Utility.toMap(rs,camps);
		stmt.close();
		return m;
	}
	
	public static Map getJoin(ArrayList camps1,ArrayList camps2, String taula1, String taula2, String camp, String condicio, Connection c) throws SQLException {
		// TODO Auto-generated method stub
		String query;
		String sCamps = Utility.toListDB(camps1,"a");
		sCamps += " , "+Utility.toListDB(camps2,"b");
		ArrayList res = new ArrayList();
		query = "SELECT "+sCamps+" FROM "+taula1+" a, "+taula2+
									" b WHERE a."+camp+" = b."+camp;//+" AND b."+condicio;
		Statement stmt=c.createStatement();
		ResultSet rs;		
		rs = stmt.executeQuery(query);
		Map m = new LinkedHashMap();
		camps1.addAll(camps2);		
		m = Utility.toMap(rs,camps1);
		stmt.close();
		return m;
	}


	public static Map getObjectList(String taula, List camps, String condicio, String ordre, Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		String query;
		String sCamps = camps.toString().replace('[',' ').replace(']',' ');
		query =  "SELECT "+sCamps+" FROM "+taula;
		
		if (condicio.length() > 2){ 
			query +=" WHERE "+condicio;
		}
	
		if (ordre.length() > 2){ 
			query += " ORDER BY "+ordre;
		}

		Statement stmt = connection.createStatement();
		ResultSet rs;
		
		rs = stmt.executeQuery(query);
		Map m = new LinkedHashMap();
		m = Utility.toMap(rs,camps);
		stmt.close();
		
		return m;
	}


	  /**
	   * Executa una inserció a la BD i retorna el codi d'error.
	   * @param taula
	   * @param camps camps donats per insertar.
	   * @param valors valors de la inserció a realitzar
	   * @param connection
	   * @return codi d'error
	   * @throws Exception
	   */
	  public static int executeInsert(String taula,  String camps, String valors,Connection connection) throws Exception{
		PreparedStatement pstInsert;		  		
		try {
	        pstInsert = connection.prepareStatement("INSERT INTO "+taula+" ("+camps+") VALUES ("+valors+")");	       
		/*	pstInsert = connection.prepareStatement("INSERT INTO ? (?) VALUES (?)");
			pstInsert.setString(1,taula);
			pstInsert.setString(2,camps);
			pstInsert.setString(3,valors);
		*/	pstInsert.execute();
	        connection.commit();
	        pstInsert.close();
	      }
	      catch (SQLException e) {
	        error = e.getErrorCode();
	        if (e.getErrorCode() == 1){
	          error = ErrorBD.OBJECTE_EXISTENT;
	        }else{
	          try {
	            connection.rollback();
	          }
	          catch (SQLException sqle) {
	            error = ErrorBD.ERROR_CONNEXIO;
	          }
	          error = ErrorBD.ERROR_INSERCIO;
	        }
	      }
		  return error;
	  }


	public static int getNext(String type, Connection connection) throws SQLException {
		// TODO Auto-generated method stub.
		Statement stmt = connection.createStatement();
		ResultSet rs;
		rs = stmt.executeQuery("select cur_nodes.nextval from DUAL");
		rs.next();
		
		return rs.getInt("nextval");
	}

	
	
	
}	  
