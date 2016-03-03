package edu.xtec.merli.basedades;


import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.xtec.merli.utils.Utility;

public class _AccesBD {
	  private static int error; 
	
	  
	  /** 
	   * Realitza un commit i torna a posar el contador de insercions a 0.
	 * @throws MerliDBException 
	   */
	  private static void realitzarCommit(Connection connection) throws MerliDBException{
	    try{
	      connection.commit();
	      //numInsercions = 0;
	    }catch(SQLException sqle){
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
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
	  public static int executeInsert(String taula, String valors,Connection connection) throws MerliDBException{
		PreparedStatement pstInsert;		  		
		try {
	        pstInsert = connection.prepareStatement("INSERT INTO "+taula+" VALUES ("+valors+")");	       
			pstInsert.execute();
	        connection.commit();
	        pstInsert.close();
	      }
	      catch (SQLException e) {
	        error = e.getErrorCode();
	        if (e.getErrorCode() == 1){
	          throw new MerliDBException(MerliDBException.OBJECTE_EXISTENT);
	        }else{
	          try {
	            connection.rollback();
	          }
	          catch (SQLException sqle) {
				  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
	          }
			  throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
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
	  public static String executeQuery(String query, String camp, Connection connection) throws MerliDBException{
	    ResultSet rs;
	    String result ="0";
		Statement stmt;
		

		try{
			stmt=connection.createStatement();
			rs = stmt.executeQuery(query);		  
			while (rs.next()) {
				result = rs.getString(camp);
			}
			stmt.close();
	    }catch(Exception e){
			  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
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
	  public static int executeExist(String taula, String camp, String valor, String condicio, Connection connection) throws MerliDBException{
		  int error = 0;
		  int res = 0;
		  ResultSet rs;
		  Statement stmt;
		  try{
			  stmt=connection.createStatement();
			  rs = stmt.executeQuery("SELECT count(*) FROM "+taula+" WHERE "+camp+"='"+valor+"' "+condicio);
			  rs.next();
			  res = rs.getInt(1);
			  stmt.close();
		  }catch(Exception e){
			  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		  }
		  
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


	public static int executeUpdate(String taula, String condicio, String query, Connection connection) throws MerliDBException {
		// TODO Auto-generated method stub
		PreparedStatement pstInsert;		  		
		try {
	        pstInsert = connection.prepareStatement("UPDATE "+taula+" SET "+query+" WHERE "+ condicio);		
			pstInsert.execute();
	        connection.commit();
	        pstInsert.close();
	      }
	      catch (SQLException e) {
	        error = e.getErrorCode();
			try {
	            connection.rollback();
			}catch(Exception ex){
				  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
	       
	      }
		  return error;
	}
	

	public static int executeDelete(String taula, String condicio, Connection connection) throws MerliDBException {
		// TODO Auto-generated method stub
		String query =  "delete from "+taula+" where "+condicio;
		int rs = 0;
		
		Statement stmt;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			throw new MerliDBException(MerliDBException.DELETEERROR);
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


	public static Map getObjectList(String taula, ArrayList camps, String condicio, String ordre, Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		String query;
		String sCamps = camps.toString().replace('[',' ').replace(']',' ');
		query =  "SELECT "+sCamps+" FROM "+taula+" WHERE "+condicio+" ORDER BY "+ordre;

		Statement stmt = connection.createStatement();
		ResultSet rs;
		
		rs = stmt.executeQuery(query);
		Map m = new LinkedHashMap();
		m = Utility.toMap(rs,camps);
		stmt.close();
		
		return m;
	}

	
	
	
}	  
