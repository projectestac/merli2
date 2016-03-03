package edu.xtec.merli.ws;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.utils.Utility;

public class WSAccesBD {
	  private static int error;

		private static final Logger logger = Logger.getRootLogger();//("xtec.duc");
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

//	
//	  /**
//	   * Executa una inserció a la BD i retorna el codi d'error.
//	   * @param taula
//	   * @param query valors de la inserció a realitzar
//	   * @param connection
//	   * @return codi d'error
//	   * @throws Exception
//	   */
//	  public static int executeInsert(String taula, String valors,Connection connection) throws MerliDBException{
//		PreparedStatement pstInsert = null;		  		
//		try {
//			logger.warn("executeInsert-> "+"INSERT INTO "+taula+" VALUES ("+valors+")");
//	        pstInsert = connection.prepareStatement("INSERT INTO "+taula+" VALUES ("+valors+")");	       
//			pstInsert.execute();
//	        connection.commit();
//	        pstInsert.close();
//	      }
//	      catch (SQLException e) {
//		    try {
//				pstInsert.close();
//			} catch (SQLException e1) {
//				  logger.warn("Error connecting to DDBB:"+e1.getMessage());
//				  logger.error("SQL Insert-> "+"INSERT INTO "+taula+" VALUES ("+valors+")");
//				  e1.printStackTrace();
//				  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
//			}
//	        error = e.getErrorCode();
//	        if (e.getErrorCode() == 1){
//	          throw new MerliDBException(MerliDBException.OBJECTE_EXISTENT);
//	        }else{
//	          try {
//	            connection.rollback();
//	          }
//	          catch (SQLException sqle) {
//				  logger.warn("Error connecting to DDBB:"+sqle.getMessage());
//				  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
//	          }
//			  logger.warn("Error inserting element to DDBB:"+e.getMessage());
//			  throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
//	        }
//	      }
//		  return error;
//	  }
	  
	  /**
	   * Executa una inserció a la BD i retorna el codi d'error.
	   * @param taula
	   * @param lVals valors de la inserció a realitzar
	   * @param connection
	   * @return codi d'error.
	   * @throws MerliDBException
	   */
	  public static int executeInsert(String taula, List lVals,Connection connection) throws MerliDBException{
			PreparedStatement pstInsert = null;		  	
			StringBuffer query;
			query = new StringBuffer("INSERT INTO ");
			query.append(taula).append(" VALUES (");
			if (connection == null) throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			for (int i=0;i<lVals.size()-1;i++){
				query.append("?,");
			}
			query.append("?)");
			try {
		        pstInsert = connection.prepareStatement(query.toString());
		       
				for (int i=0;i<lVals.size();i++){
					Object oTmp = lVals.get(i);
					if(oTmp==null) oTmp="";
					if (oTmp instanceof Date){
						pstInsert.setDate(i+1, new java.sql.Date((((Date)oTmp)).getTime()));
					}else if (oTmp instanceof Timestamp){
							pstInsert.setTimestamp(i+1, (Timestamp)oTmp);
					}else if (oTmp instanceof String){
						pstInsert.setString(i+1, (String)oTmp);						
					}else{
						pstInsert.setObject(i+1,lVals.get(i));
					}
				}
				pstInsert.execute();
		      }
		      catch (SQLException e) {
		    	  logger.error("SQL= "+query.toString()+" list="+lVals);
		    	  e.printStackTrace();			    
				  error = e.getErrorCode();
				  if (e.getErrorCode() == 1){
					  throw new MerliDBException(MerliDBException.OBJECTE_EXISTENT);
				  }else{
					  throw new MerliDBException(MerliDBException.ERROR_INSERCIO);
				  }
		      }finally{
					tancaStatement(pstInsert);
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
		    ResultSet rs = null;
		    String result ="0";
			Statement stmt = null;		

			try{
				stmt=connection.createStatement();
				rs = stmt.executeQuery(query);		  
				while (rs.next()) {
					result = rs.getString(camp);
				}
				rs.close();
				stmt.close();
		    }catch(Exception e){
			    try {
					rs.close();
					stmt.close();
				} catch (SQLException e1) {
					  logger.warn("Error connecting to DDBB:"+e1.getMessage());
					  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
				}
				  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
		    return result;
		  }
	  
	  /**
	   * Execute la Query donada. Els valors de la query venen donats en el llistat lCond. 
	   * @param query Consulta a realitzar. El valors queden preparats amb un ?.
	   * @param lCond Valors dins la consulta.
	   * @param camp Camp que es vol recuperar de la BBDD. retornat en format String.
	   * @param connection
	   * @return
	   * @throws MerliDBException
	   */
	  public static String executeQuery(String query, List lCond, String camp, Connection connection) throws MerliDBException{
		    ResultSet rs = null;
		    String result ="0";
			PreparedStatement pstmt = null;		
			if (connection == null) throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			try{
				pstmt=connection.prepareStatement(query);
				for (int i=0; i< lCond.size(); i++)
					pstmt.setObject(i+1,lCond.get(i));
				
				rs = pstmt.executeQuery();		  
				while (rs.next()) {
					result = rs.getString(camp);
				}
		    }catch(Exception e){
		    	e.printStackTrace();
		    	throw new MerliDBException(MerliDBException.ERROR_SQL);
			}finally{
				tancaStatement(rs,pstmt);
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
		  ResultSet rs = null;
		  Statement stmt = null;
		  if (connection == null) throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		  
		  try{
			  stmt=connection.createStatement();
			  rs = stmt.executeQuery("SELECT count(*) FROM "+taula+" WHERE "+camp+"='"+valor+"' "+condicio);
			  rs.next();
			  res = rs.getInt(1);
		  }catch(Exception e){
			  logger.warn("Error querying DDBB:"+e.getMessage());
			  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		  }finally{
			  tancaStatement(rs,stmt);
		  }
		  
		  return res;
	  }	  
	  
	  
	  private static void tancaStatement(ResultSet rs, Statement stmt) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error("Error al tancar un resultSet. ->"+e);
				e.printStackTrace();
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("Error al tancar un statement. ->"+e);
				e.printStackTrace();
			}
	  }


	/**
	   * Retorna 0 si la condicio donada a la "taula" es compleix com a minim un cop.
	   * @param taula Taula sobre la que s'avalua la condició
	   * @param condicio condició que s'ha de donar
	   * @param lCond Valors de la condicio
	   * @param connection 
	   * @return >1: existeix, 0: no existeix.
	   * @throws MerliDBException
	   */
	  public static int executeExist(String taula, String condicio, List lCond, Connection connection) throws MerliDBException{
		  int error = 0;
		  int res = 0;
		  StringBuffer query;
		  ResultSet rs = null;
		  PreparedStatement pstmt = null;
		  
		  query = new StringBuffer("SELECT count(*) FROM ");
		  query.append(taula);
		  query.append(" WHERE ");
		  //query.append(camp).append(" = ?");
		  query.append(condicio);
		  
		  try{
			  pstmt=connection.prepareStatement(query.toString());
			  for (int i=0; i<lCond.size(); i++)
				  pstmt.setObject(i+1,lCond.get(i));
			  rs = pstmt.executeQuery();
			  rs.next();
			  res = rs.getInt(1);
			  rs.close();
			  pstmt.close();
		  }catch(Exception e){
			  try {
					rs.close();
					pstmt.close();
				} catch (SQLException e1) {
					  logger.warn("Error connecting to DDBB:"+e1.getMessage());
					  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
				}
			  logger.warn("Error querying DDBB:"+e.getMessage());
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
	public static Map getFullLlistat(String taula, List camps, String order, Connection connection) throws MerliDBException {
		StringBuffer query;
		Map m;
		String sCamps = camps.toString().replace('[',' ').replace(']',' ');
		ArrayList res = new ArrayList();
		query = new StringBuffer("SELECT ");
		query.append(sCamps);
		query.append(" FROM ");
		query.append(taula);
		query.append(" ORDER BY ").append(order);
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		try{
			pstmt=connection.prepareStatement(query.toString());
			//pstmt.setString(1,order);
			rs =pstmt.executeQuery();
			m = new LinkedHashMap();
			m = Utility.toMap(rs,camps);
			rs.close();
			pstmt.close();
		}catch(Exception e){
			logger.warn("SQL ="+query.toString());
			e.printStackTrace();
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e1) {
				  logger.warn("Error connecting to DDBB:"+e1.getMessage());
				  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
			logger.warn("Error querying DDBB:"+e.getMessage());
			throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}
		return m;
	}



	public static Map getObject(String taula, List camps, String condicio, Connection connection) throws MerliDBException{
		String query;
		String sCamps = camps.toString().replace('[',' ').replace(']',' ');
		query =  "SELECT "+sCamps+" FROM "+taula+" WHERE "+condicio;

		Statement stmt = null;
		ResultSet rs = null;
		Map m = null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			m = new LinkedHashMap();
			m = Utility.toMap(rs,camps);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			try {
				rs.close();
				stmt.close();
			} catch (SQLException e1) {
				  logger.warn("Error connecting to DDBB:"+e1.getMessage());
				  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
		}
		
		return m;
	}
	
	
	public static Map getObject(String taula, List camps, String condicio, List lCond, Connection connection) throws MerliDBException{
		StringBuffer query;
		String sCamps = camps.toString().replace('[',' ').replace(']',' ');
		query =  new StringBuffer("SELECT ");
		query.append(sCamps).append(" FROM ").append(taula);
		query.append(" WHERE ").append(condicio);
		PreparedStatement pstmt = null;			
		ResultSet rs =null;
		Map m;
		
		if (connection == null) throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		
		try{
			pstmt = connection.prepareStatement(query.toString());
	
			for (int i=0; i<lCond.size(); i++)
				pstmt.setObject(i+1,lCond.get(i));
			
			rs = pstmt.executeQuery();
			m = new LinkedHashMap();
			m = Utility.toMap(rs,camps);
		} catch (SQLException e) {
			  logger.warn("Error connecting to DDBB:"+e.getMessage());
			  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		}finally{
			tancaStatement(rs,pstmt);
		}
		return m;
	}	
	
	


	public static int executeUpdate(String taula, String condicio, String query, Connection connection) throws MerliDBException {
		// TODO Auto-generated method stub
		PreparedStatement pstInsert = null;		  		
		try {
	        pstInsert = connection.prepareStatement("UPDATE "+taula+" SET "+query+" WHERE "+ condicio);		
			pstInsert.execute();
	        //connection.commit();
	        pstInsert.close();
	      }
	      catch (SQLException e) {
	    	  logger.warn("SQL= "+"UPDATE "+taula+" SET "+query+" WHERE "+ condicio);
	    	  e.printStackTrace();
			  try {
				  pstInsert.close();
				} catch (SQLException e1) {
					  logger.warn("Error connecting to DDBB:"+e1.getMessage());
					  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
				}
		        error = e.getErrorCode();
				try {
		            connection.rollback();
				}catch(Exception ex){
					  logger.warn("Error connecting DDBB:"+ex.getMessage());
					  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
				}
				logger.warn("Error modifying DDBB:"+e.getMessage());
				throw new MerliDBException(MerliDBException.ERROR_INSERCIO);	       
	      }
		  return error;
	}
	public static int executeUpdate(String taula, String condicio, List lCond, String update, List lParams, Connection connection) throws MerliDBException {
		// TODO Auto-generated method stub
		PreparedStatement pstInsert = null;	
		StringBuffer query;
		int i = 0;
		
		query = new StringBuffer("UPDATE ");
		query.append(taula).append(" SET ").append(update);
		query.append(" WHERE ").append(condicio);
		if (connection == null) throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		try {
	        pstInsert = connection.prepareStatement(query.toString());	
			for (i = 0; i<lParams.size();i++){
				Object oTmp = lParams.get(i);
				if (oTmp instanceof Date){
					pstInsert.setDate(i+1, new java.sql.Date((((Date)oTmp)).getTime()));
				}else if (oTmp instanceof Timestamp){
						pstInsert.setTimestamp(i+1, (Timestamp)oTmp);
				}else if (oTmp instanceof String){
					pstInsert.setString(i+1, (String)oTmp);							
				}else{
					pstInsert.setObject(i+1,oTmp);
				}				
			}
			for (i=i; i<lParams.size()+lCond.size();i++){				
				Object oTmp = lCond.get(i-lParams.size());
				if (oTmp instanceof Date){
					pstInsert.setDate(i+1, new java.sql.Date((((Date)oTmp)).getTime()));
				}else if (oTmp instanceof Timestamp){
						pstInsert.setTimestamp(i+1, (Timestamp)oTmp);
				}else if (oTmp instanceof String){
					pstInsert.setString(i+1, (String)oTmp);							
				}else{
					pstInsert.setObject(i+1,oTmp);
				}
				//pstInsert.setObject(i+1,lCond.get(i-lParams.size()));
			}
			pstInsert.execute();
	      }catch (SQLException e) {
	    	  logger.error("SQL: "+query.toString());
	    	  e.printStackTrace();
			  throw new MerliDBException(MerliDBException.ERROR_SQL);  
	      }finally{
			  tancaStatement(pstInsert);	
	      }
		return error;
	}
	

	private static void tancaStatement(PreparedStatement pst){
		if (pst != null)
			try {
				pst.close();
			} catch (SQLException e) {
				logger.error("Error al tancar un preparedStatement. ->"+e);
				e.printStackTrace();
			}
	}


	/**
	 * Executa un DELETE sobre la taula donada amb la condició donada.
	 * @param taula
	 * @param condicio
	 * @param connection
	 * @return
	 * @throws MerliDBException
	 */
	public static int executeDelete(String taula, String condicio, Connection connection) throws MerliDBException {
		// TODO Auto-generated method stub
		String query =  "delete from "+taula+" where "+condicio;
		int rs = 0;
		
		Statement stmt=null;
		try {
			stmt = connection.createStatement();
			rs = stmt.executeUpdate(query);
			stmt.close();
		} catch (SQLException e) {
			  try {
				  stmt.close();
			  } catch (SQLException e1) {
					  logger.warn("Error connecting to DDBB:"+e1.getMessage());
					  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			  }
			  logger.warn("Error deleting DDBB:"+e.getMessage());
			  throw new MerliDBException(MerliDBException.DELETEERROR);
		}
		return rs;
	}
	public static int executeDelete(String taula, String condicio, List lParams, Connection connection) throws MerliDBException {
		// TODO Auto-generated method stub
		StringBuffer query;
		int rs = 0;
		
		query = new StringBuffer("delete from ").append(taula).append(" where ").append(condicio);
		
		PreparedStatement pstmt = null;

		if (connection == null) throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		try {
			pstmt = connection.prepareStatement(query.toString());
			for (int i= 0; i<lParams.size();i++)
				pstmt.setObject(i+1,lParams.get(i));
			rs = pstmt.executeUpdate();
		} catch (SQLException e) {			  
			  logger.error("Error deleting DDBB:"+e.getMessage());
			  throw new MerliDBException(MerliDBException.DELETEERROR);
		} finally{
			tancaStatement(pstmt);
		}
		return rs;
	}


	/**
	 * 
	 * @param camp Llistat de camps de la taula que es volen.
	 * @param taula1 Taula 1 de la join.
	 * @param taula2 taula 2 de la join
	 * @param camp Camp sobre el q fer la join.
	 * @param condicio Condició aplicada a la 1a taula.
	 * @param c Connection.
	 * @return
	 * @throws MerliDBException 
	 * @throws SQLException
	 */
	public static Map getJoin(ArrayList camps, String taula1, String taula2, String camp, String condicio, Connection c) throws MerliDBException{
		// TODO Auto-generated method stub
		String query;
		String sCamps = Utility.toListDB(camps,"a");
		ArrayList res = new ArrayList();
		query = "SELECT "+sCamps+" FROM "+taula1+" a, "+taula2+
									" b WHERE a."+camp+" = b."+camp+" AND b."+condicio;
		Statement stmt=null;
		ResultSet rs=null;
		Map m = new LinkedHashMap();
		
		try{		
			stmt=c.createStatement();
			rs = stmt.executeQuery(query);
			m = Utility.toMap(rs,camps);
			rs.close();
			stmt.close();
		}catch (SQLException s){
			try{
				rs.close();
				stmt.close();
			}catch(SQLException e1){
				  logger.warn("Error connecting to DDBB:"+e1.getMessage());
				  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
			}
			throw new MerliDBException(MerliDBException.ERROR_SQL);
		}
		
		return m;
	}
	
	/**
	 * Fa una join de les dues taules donades pel camp "camp".
	 * @param camps1 Llistat de camps de la taula 1 que es volen.
	 * @param camps2 Llistat de camps de la taula 2 que es volen.
	 * @param taula1 Taula 1 de la join.
	 * @param taula2 taula 2 de la join
	 * @param camp Camp sobre el q fer la join
	 * @param condicio Condició aplicada a la 1a taula
	 * @param c Connection.
	 * @return Map
	 * @throws SQLException
	 * @throws MerliDBException 
	 */
	public static Map getJoin(ArrayList camps1,ArrayList camps2, String taula1, String taula2, String camp, String condicio, Connection c) throws SQLException{
		// TODO Auto-generated method stub
		String query;
		String sCamps = Utility.toListDB(camps1,"a");
		sCamps += " , "+Utility.toListDB(camps2,"b");
		ArrayList res = new ArrayList();
		query = "SELECT "+sCamps+" FROM "+taula1+" a, "+taula2+
									" b WHERE a."+camp+" = b."+camp;//+" AND b."+condicio;
		Statement stmt=null;
		ResultSet rs = null;		
		Map m = new LinkedHashMap();
		try{
			stmt=c.createStatement();
			rs = stmt.executeQuery(query);
			camps1.addAll(camps2);		
			m = Utility.toMap(rs,camps1);
			rs.close();
			stmt.close();
		}catch (SQLException s){
			try{
				rs.close();
				stmt.close();
			}catch(SQLException e1){
				  logger.warn("Error connecting to DDBB:"+e1.getMessage());
				  throw e1;
			}
			throw s;
		}
		
		return m;
	}

	/**
	 * Fa una join de les dues taules donades pel camp "camp".
	 * @param camps1 Llistat de camps de la taula 1 que es volen.
	 * @param camps2 Llistat de camps de la taula 2 que es volen.
	 * @param taula1 Taula 1 de la join.
	 * @param taula2 taula 2 de la join
	 * @param camp Camp sobre el q fer la join
	 * @param condicio1 Condició aplicada a la 1a taula
	 * @param condicio2 Condició aplicada a la 2a taula
	 * @param order1 Ordre, només accepta un sol camp de la taula 1, sense espais.
	 * @param order2 Ordre, només accepta un sol camp de la taula 2, sense espais.
	 * @param way Direcció, ASC o DESC.
	 * @param c Connection.
	 * @return Map
	 * @throws SQLException
	 * @throws MerliDBException 
	 */
	public static Map getJoin(ArrayList camps1,ArrayList camps2, String taula1, String taula2, String camp, String condicio1, String condicio2, String order1, String order2, String way, Connection c) throws SQLException {
		// TODO Auto-generated method stub
		String query;
		String sCamps = Utility.toListDB(camps1,"a");
		if (camps2.size() > 0)
			sCamps += " , "+Utility.toListDB(camps2,"b");
		ArrayList res = new ArrayList();
		query = "SELECT "+sCamps+" FROM "+taula1+" a, "+taula2+
									" b WHERE a."+camp+" = b."+camp;
		if (condicio1.length() > 2)
			query += " AND a."+condicio1;
		if (condicio2.length() > 2)
			query += " AND b."+condicio2;
		if (order1.indexOf(" ")<0 && (order1.length() > 2)){
			query += " Order by a."+order1;
			if (order2.indexOf(" ")<0 && (order2.length() > 2))
				query += " , b."+order2;
			query += " "+way;
		}else
			if (order2.indexOf(" ")<0 && (order2.length() > 2)){
				query += " Order by b."+order2;
				query += " "+way;
			}
		//logger.debug("getJoin-> query="+query);
		Statement stmt=null;
		ResultSet rs = null;	
		Map m = new LinkedHashMap();
		try{
			stmt=c.createStatement();
			rs = stmt.executeQuery(query);
			camps1.addAll(camps2);		
			m = Utility.toMap(rs,camps1);
			rs.close();
			stmt.close();
		}catch (SQLException s){
			try{
				if (rs!=null) rs.close();
				if (stmt!=null) stmt.close();
			}catch(SQLException e1){
				  logger.warn("Error connecting to DDBB:"+e1.getMessage());
				  throw e1;
			}
			throw s;
		}
		return m;
	}
	
	/**
	 * Fa una join de les dues taules donades pel camp "camp".
	 * @param camps1 Llistat de camps de la taula 1 que es volen.
	 * @param camps2 Llistat de camps de la taula 2 que es volen.
	 * @param taula1 Taula 1 de la join.
	 * @param taula2 taula 2 de la join
	 * @param camp1 Camp de la taula1 sobre el q fer la join
	 * @param camp2 Camp de la taula2 sobre el q fer la join
	 * @param condicio1 Condició aplicada a la 1a taula
	 * @param condicio2 Condició aplicada a la 2a taula
	 * @param order1 Ordre, només accepta un sol camp de la taula 1, sense espais.
	 * @param order2 Ordre, només accepta un sol camp de la taula 2, sense espais.
	 * @param way Direcció, ASC o DESC.
	 * @param c Connection.
	 * @return Map
	 * @throws SQLException
	 * @throws MerliDBException 
	 * @throws MerliDBException 
	 */
	public static Map getJoin(ArrayList camps1,ArrayList camps2, String taula1, String taula2, String camp1, String camp2, String condicio1, String condicio2, String order1, String order2, String way, Connection c) throws SQLException, MerliDBException {
		// TODO Auto-generated method stub
		String query;
		
		if (c == null) throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		
		String sCamps = Utility.toListDB(camps1,"a");
		if (camps2.size() > 0)
			sCamps += " , "+Utility.toListDB(camps2,"b");
		ArrayList res = new ArrayList();
		query = "SELECT "+sCamps+" FROM "+taula1+" a, "+taula2+
									" b WHERE a."+camp1+" = b."+camp2;
		if (condicio1.length() > 2)
			query += " AND a."+condicio1;
		if (condicio2.length() > 2)
			query += " AND b."+condicio2;
		if (order1.indexOf(" ")<0 && (order1.length() > 2)){
			query += " Order by a."+order1;
			if (order2.indexOf(" ")<0 && (order2.length() > 2))
				query += " , b."+order2;
			query += " "+way;
		}else
			if (order2.indexOf(" ")<0 && (order2.length() > 2)){
				query += " Order by b."+order2;
				query += " "+way;
			}
		//logger.debug("getJoin-> "+query);
		Statement stmt=null;
		ResultSet rs = null;	
		Map m = new LinkedHashMap();
		try{
			stmt=c.createStatement();
			rs = stmt.executeQuery(query);
			camps1.addAll(camps2);		
			m = Utility.toMap(rs,camps1);
		}catch (SQLException s){
			logger.warn("Error connecting to DDBB:"+s.getMessage());
			s.printStackTrace();
			throw s;
		}finally{
			tancaStatement(rs,stmt);
		}
		return m;
	}



	/*public static Map getObjectList(String taula, ArrayList camps, String condicio, String ordre, Connection connection) throws SQLException {
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
*/

	public static Map getObjectList(String taula, List camps, String condicio, String ordre, Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		String sCamps;
		String con;
		ArrayList cond;
		String aux;
		StringBuffer query;
		
		sCamps = camps.toString().replace('[',' ').replace(']',' ');
		
		query =  new StringBuffer("SELECT ");
		query.append(sCamps);
		query.append(" FROM ");
		query.append(taula);
		
		con = condicio;
		cond = new ArrayList();
		
		if (condicio.length() > 2){ 
			query.append(" WHERE ");
			/*while (con.length() > 12){
				con = con.substring(con.indexOf("=")+1);
				con.trim();
				
				//cond.add();
			}*/
			query.append(condicio);
		}
	
		if (ordre.length() > 2){ 
			query.append(" ORDER BY ");
			query.append(ordre);
		}

		PreparedStatement pstmt = null;
		ResultSet rs=null;
		Map m = new LinkedHashMap();
		try{
			pstmt = connection.prepareStatement(query.toString());
			rs = pstmt.executeQuery();
			m = Utility.toMap(rs,camps);
			rs.close();
			pstmt.close();
		}catch (SQLException s){
			try{
				rs.close();
				pstmt.close();
			}catch(SQLException e1){
				  logger.warn("Error connecting to DDBB:"+e1.getMessage());
				  throw e1;
			}
			throw s;
		}
		return m;
	}
	/**
	 * Retorna el llistat d'objectes que responen a la Query donada sobre la taula donada.
	 * @param taula
	 * @param camps
	 * @param condicio
	 * @param lParams
	 * @param ordre
	 * @param connection
	 * @return
	 * @throws SQLException
	 * @throws MerliDBException 
	 */
	public static Map getObjectList(String taula, List camps, String condicio, List lParams, String ordre, Connection connection) throws SQLException, MerliDBException {
		// TODO Auto-generated method stub
		String sCamps;
		String con;
		ArrayList cond;
		String aux;
		StringBuffer query;
		
		if (connection == null) throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		
		sCamps = camps.toString().replace('[',' ').replace(']',' ');
		
		query =  new StringBuffer("SELECT ");
		query.append(sCamps);
		query.append(" FROM ");
		query.append(taula);
		
		con = condicio;
		cond = new ArrayList();
		
		if (condicio.length() > 2 && !lParams.isEmpty()){ 
			query.append(" WHERE ");
			query.append(condicio);
		}
	
		if (ordre.length() > 2){ 
			query.append(" ORDER BY ");
			query.append(ordre);
		}

		PreparedStatement pstmt = null;

		ResultSet rs=null;
		Map m = new LinkedHashMap();
		try{	
			pstmt = connection.prepareStatement(query.toString());	
			if (condicio.length() > 2 && !lParams.isEmpty()){ 
				for (int i = 0; i<lParams.size();i++)
					pstmt.setObject(i+1,lParams.get(i));			
			}	
			rs = pstmt.executeQuery();
			m = Utility.toMap(rs,camps);
		}catch (SQLException e){
			logger.error("SQL="+query.toString());
			e.printStackTrace();
			throw e;
		}finally{
			tancaStatement(rs,pstmt);
		}

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
	 public static int executeInsert(String taula,  String camps, String valors,Connection connection) throws MerliDBException{
			PreparedStatement pstInsert=null;		  		
			try {
		        pstInsert = connection.prepareStatement("INSERT INTO "+taula+" ("+camps+") VALUES ("+valors+")");	       
			/*	pstInsert = connection.prepareStatement("INSERT INTO ? (?) VALUES (?)");
				pstInsert.setString(1,taula);
				pstInsert.setString(2,camps);
				pstInsert.setString(3,valors);
			*/	pstInsert.execute();
		        //connection.commit();
		        pstInsert.close();
		      }
		      catch (SQLException e) {
				  	try {
						pstInsert.close();
					} catch (SQLException e1) {
						  logger.warn("Error connecting to DDBB:"+e1.getMessage());
						  throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
					}
			        error = e.getErrorCode();
			        if (e.getErrorCode() == 1){
			          error = MerliDBException.OBJECTE_EXISTENT;
			        }else{
			          try {
			            connection.rollback();
			          }
			          catch (SQLException sqle) {
			            error = MerliDBException.ERROR_CONNEXIO;
			          }
			          error = MerliDBException.ERROR_INSERCIO;
			        }
		      }
			  return error;
		  } 
	 
	 public static int executeInsert(String taula,  String camps, List lParams, Connection connection) throws MerliDBException{
		PreparedStatement pstInsert = null;	
		StringBuffer query;
		if (connection == null) throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		query = new StringBuffer("INSERT INTO ");
		query.append(taula).append(" (").append(camps).append(") VALUES(");
		for (int i = 0; i< lParams.size()-1; i++)
			query.append("?,");
		query.append("?");
		query.append(")");
		try {
	        pstInsert = connection.prepareStatement(query.toString());	       
			for (int i = 0; i< lParams.size(); i++)
				pstInsert.setObject(i+1,lParams.get(i));
			pstInsert.execute();
		}catch (SQLException e) {
		  	logger.error("Error a l'insertar a la DDBB:"+e.getMessage());
			throw new MerliDBException(MerliDBException.ERROR_INSERCIO);				        
		}finally{
			tancaStatement(pstInsert);
		}
		
		return error;
	}


	public static int getNext(String type, Connection connection) throws SQLException, MerliDBException {
		// TODO Auto-generated method stub.
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		int ret;
		if (connection == null) throw new MerliDBException(MerliDBException.ERROR_CONNEXIO);
		
		try{
			pstmt = connection.prepareStatement("select "+type+".nextval from DUAL");
			rs = pstmt.executeQuery();
			rs.next();
			ret = rs.getInt("nextval");
		}catch(SQLException s){
			logger.error("Error al sol.licitar un nou id ->"+s);		
			throw s;
		}finally{
			tancaStatement(rs,pstmt);
		}
		
		return ret;
	}

	
	public static Map getQuery(String fromwhere, List camps, List lParams, Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		String sCamps;
		String con;
		ArrayList cond;
		String aux;
		StringBuffer query;
		List useCamps = new ArrayList(camps);
		sCamps = useCamps.toString().replace('[',' ').replace(']',' ');
		
		query =  new StringBuffer("SELECT ");
		query.append(sCamps);
		query.append(" FROM ");
		query.append(fromwhere);
		PreparedStatement pstmt  = null;
		
		prepararCampsRenombratsAS(useCamps);
		
		ResultSet rs=null;
		Map m = new LinkedHashMap();
		try{	
			pstmt = connection.prepareStatement(query.toString());		
			if (!lParams.isEmpty()){ 
				for (int i = 0; i<lParams.size();i++)
					pstmt.setObject(i+1,lParams.get(i));			
			}
			rs = pstmt.executeQuery();
			m = Utility.toMap(rs,useCamps);
			rs.close();
			pstmt.close();
		}catch(SQLException s){
			try {
				rs.close();
				pstmt.close();
			} catch (SQLException e1) {
				  logger.warn("Error connecting to DDBB:"+e1.getMessage());
				  throw e1;
			}
			throw s;
		}
		
		return m;
	}


	private static void prepararCampsRenombratsAS(List camps) {
		String aux;
		if (camps.toString().indexOf(" as ")>0){
			for(int i=0;i<camps.size();i++){
				aux = (String) camps.get(i);
				Pattern trafficPattern = Pattern.compile(".* as (.*)");
                Matcher trafficMatcher = trafficPattern.matcher(aux);
                if (trafficMatcher.find())
                    aux = trafficMatcher.group(1);
                    
				camps.set(i, aux);
			}
		}
	}
	
	
}	  
