package simpple.xtec.web.util;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

/**
 * Miscellaneous methods
 *
 * @author descuer
 *
 */
public class UtilsCercador {

    // logger
    static final Logger logger = Logger.getLogger(simpple.xtec.web.util.UtilsCercador.class);

    /**
     * A partir d'un objecte Calendar retorna un String amb la data
     */
    public static String calculaDataActual(Calendar rightNow) {
        int dia_actual = 0;
        int mes_actual = 0;
        int any_actual = 0;
        // 09/03/07 12:49:33,717047000 +01:00 		
        String dia_actual_string = "";
        String mes_actual_string = "";
        String data_actual_string = "";
        try {
            dia_actual = rightNow.get(Calendar.DAY_OF_MONTH);
            dia_actual_string = "" + dia_actual;
            if (dia_actual < 10) {
                dia_actual_string = "0" + dia_actual_string;
            }
            logger.debug("Dia actual: " + dia_actual);
            logger.debug("Dia actual string: " + dia_actual_string);
            mes_actual = rightNow.get(Calendar.MONTH);
            mes_actual++;
            mes_actual_string = "" + mes_actual;
            if (mes_actual < 10) {
                mes_actual_string = "0" + mes_actual_string;
            }
            logger.debug("Mes actual: " + mes_actual);
            logger.debug("Mes actual string: " + mes_actual_string);

            any_actual = rightNow.get(Calendar.YEAR);
            // 09/03/07 12:49:33,717047000 +01:00 		
            data_actual_string = any_actual + "-" + mes_actual_string + "-" + dia_actual_string;
        } catch (Exception e) {
            logger.error(e);
        }
        //String data_actual_string = "01/03/07";
        return data_actual_string;
    }

    /**
     * A partir d'un objecte Calendar retorna un String amb l'hora
     */
    public static String calculaHoraActual(Calendar rightNow) {
        int hora_actual = 0;
        int minut_actual = 0;
        String minut_actual_string = "";
        int segon_actual = 0;
        String segon_actual_string = "";
        String hora_actual_string = "";
        String hora_actual_return = "";
        try {
            hora_actual = rightNow.get(Calendar.HOUR_OF_DAY);
            hora_actual_string = "" + hora_actual;
            if (hora_actual < 10) {
                hora_actual_string = "0" + hora_actual_string;
            }
            minut_actual = rightNow.get(Calendar.MINUTE);
            minut_actual_string = "" + minut_actual;
            if (minut_actual < 10) {
                minut_actual_string = "0" + minut_actual_string;
            }
            segon_actual = rightNow.get(Calendar.SECOND);
            segon_actual_string = "" + segon_actual;
            if (segon_actual < 10) {
                segon_actual_string = "0" + segon_actual_string;
            }
            hora_actual_return = hora_actual_string + ":" + minut_actual_string + ":" + segon_actual_string;
        } catch (Exception e) {
            logger.error(e);
        }
        return hora_actual_return;
    }

    public static void carregarDriver() {
        try {
            /*	  if (Configuracio.isVoid()) {
             Configuracio.carregaConfiguracio(); 
             }
             Class.forName(Configuracio.nomDriverBD).newInstance();
             */
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    public static void carregarDriverOracle() {
        try {
            /*		  if (Configuracio.isVoid()) {
             Configuracio.carregaConfiguracio(); 
             }
             Class.forName(Configuracio.nomDriverBDOracle).newInstance();
             */
        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    public static String getType(String typeDoc) {
        String type = "";
        try {
            if (typeDoc.equals("0")) {
                type = "HTML";
            }
            if (typeDoc.equals("1")) {
                type = "PDF";
            }
            if (typeDoc.equals("2")) {
                type = "Word";
            }
            if (typeDoc.equals("3")) {
                type = "Excel";
            }
            if (typeDoc.equals("4")) {
                type = "Powerpoint";
            }
            if (typeDoc.equals("5")) {
                type = "SXI";
            }
            if (typeDoc.equals("6")) {
                type = "SXC";
            }
            if (typeDoc.equals("7")) {
                type = "SXW";
            }
            if (typeDoc.equals("100")) {
                type = "SQL";
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return type;
    }

    public static boolean esGirada(String myDate) {
        boolean esGirada = false;
        StringTokenizer myTokenizer = null;
        try {
            myTokenizer = new StringTokenizer(myDate, "-");
            String any = myTokenizer.nextToken();
            if (any.length() == 4) {
                esGirada = true;
            }
        } catch (Exception e) {
        }
        return esGirada;
    }

    public static String girarDataDMYguio(String myDate) {
        boolean esGirada = UtilsCercador.esGirada(myDate);

        String dataGirada = "";
        StringTokenizer myTokenizer = null;
        try {
            myTokenizer = new StringTokenizer(myDate, "-");

            String any = myTokenizer.nextToken();
            String mes = myTokenizer.nextToken();
            String dia = myTokenizer.nextToken();
            if (esGirada) {
                dataGirada = dia + "/" + mes + "/" + any;
            } else {
                dataGirada = any + "/" + mes + "/" + dia;
            }
        } catch (Exception e) {
        }
        return dataGirada;
    }

    public static String girarData(String myDate) {
        String dataGirada = "";
        StringTokenizer myTokenizer = null;
        try {
            myTokenizer = new StringTokenizer(myDate, "/");
            String dia = myTokenizer.nextToken();
            String mes = myTokenizer.nextToken();
            String any = myTokenizer.nextToken();
            dataGirada = any + "-" + mes + "-" + dia;
        } catch (Exception e) {
        }
        return dataGirada;
    }

    // 2007-03-13 11:53:52
    public static String girarDataGuio(String myDate) {
        String dataGirada = "";
        StringTokenizer myTokenizer = null;
        try {
            myDate = myDate.replace(' ', '-');
            myTokenizer = new StringTokenizer(myDate, "-");
            logger.debug("My date: " + myDate);
            String any = myTokenizer.nextToken();
            logger.debug("Any: " + any);
            String mes = myTokenizer.nextToken();
            logger.debug("Mes: " + mes);
            String dia = myTokenizer.nextToken();
            logger.debug("Dia: " + dia);
            String hora = myTokenizer.nextToken();
            dataGirada = dia + "-" + mes + "-" + any + " " + hora;
        } catch (Exception e) {
        }
        return dataGirada;
    }

    public static String getDia(String dataCerca) {
        String dia = "";
        int indexOf = 0;
        try {
            indexOf = dataCerca.indexOf(" ");
            dia = dataCerca.substring(0, indexOf);
            dia = dia.replace('-', '/');
            dia = girarData(dia);
            dia = dia.replace('-', '/');
        } catch (Exception e) {
            logger.error(e);
        }
        return dia;
    }

    public static String getHora(String dataCerca) {
        String hora = "";
        int indexOf = 0;
        try {
            indexOf = dataCerca.indexOf(" ");
            hora = dataCerca.substring(indexOf + 1, dataCerca.length());
            // hora = hora.substring(0, hora.length() -2);
        } catch (Exception e) {
            logger.error(e);
        }
        return hora;
    }

    public static String getFNfromVCardOld(String autorVCard) {
        String fn = "";
        try {
            StringTokenizer myTokenizer = new StringTokenizer(autorVCard, "\n");
            while (myTokenizer.hasMoreTokens()) {
                String nextToken = myTokenizer.nextToken();
                nextToken = nextToken.trim();
                if (nextToken.startsWith("FN:")) {
                    fn = nextToken.substring(3, nextToken.length());
                }

            }
        } catch (Exception e) {
            logger.error(e);
        }
        return fn;
    }

    public static String getFNfromVCard(String autorVCard) {
        String fn = "";
        int indexInicial = -1;
        int indexFinal = -1;
        if (autorVCard != null) {
            try {
                fn = autorVCard;
                indexInicial = fn.indexOf("FN:") + 3;
                indexFinal = fn.indexOf("EMAIL");
                if (indexFinal > 0 && indexFinal > indexInicial && indexInicial < fn.length()) {
                    fn = fn.substring(indexInicial, indexFinal);
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return fn;
    }

    public static String getLlistaHoresChart() {
        String llistaHores = "";
        Calendar rightNow = null;
        int hora_actual = 0;
        int numHores = 0;
        try {
            rightNow = Calendar.getInstance();
            rightNow.add(Calendar.HOUR, -23);
            hora_actual = rightNow.get(Calendar.HOUR_OF_DAY);
            while (numHores < 24) {
                llistaHores += "'" + hora_actual + "'";
                numHores++;
                hora_actual++;
                if (hora_actual == 24) {
                    hora_actual = 0;
                }
                if (numHores < 24) {
                    llistaHores += ",";
                }
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return llistaHores;
    }

    /**
     *
     * M?tode: dataToString
     *
     * Transforma una data en format Calendar en un String format MySql
     *
     * @param myCalendar
     *
     */
    private static String dataToString(Calendar myCalendar) {

        String data_retorn = "";

        try {

            int dia_actual = myCalendar.get(Calendar.DAY_OF_MONTH);
            int mes_actual = myCalendar.get(Calendar.MONTH);
            mes_actual++;
            int any_actual = myCalendar.get(Calendar.YEAR);
            int hora_actual = myCalendar.get(Calendar.HOUR_OF_DAY);
            String hora_actual_string = "" + hora_actual;
            String mes_actual_string = "" + mes_actual;
            if (hora_actual < 10) {
                hora_actual_string = "0" + hora_actual_string;
            }
            if (mes_actual < 10) {
                mes_actual_string = "0" + mes_actual_string;
            }

            data_retorn = any_actual + "-" + mes_actual_string + "-" + dia_actual + " " + hora_actual_string + ":00:00";

        } catch (Exception e) {
            logger.error(e);
        }

        return data_retorn;

    }

    public static String getLlistaValorsChart() {
        String llistaValors = "";
        Calendar rightNow = null;
        int hora_actual = 0;
        int numHores = 0;
        String data_actual_string = "";
        String data_anterior_string = "";
        String data_busqueda_string = "";
        String sqlQuery = "";
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            // carregarDriver();
            carregarDriverOracle();
            /*  Context initContext = new InitialContext();
             DataSource ds = (DataSource)initContext.lookup("java:/jdbc/pool/MerliConnectionPoolDS"); */
            myConnection = getConnectionFromPool();

            // myConnection = DriverManager.getConnection(Configuracio.cadenaConnexioBD);
            stmt = myConnection.createStatement();
            rightNow = Calendar.getInstance();
            data_actual_string = dataToString(rightNow);
            rightNow.add(Calendar.HOUR, -23);
            data_anterior_string = dataToString(rightNow);
            hora_actual = rightNow.get(Calendar.HOUR_OF_DAY);
            while (numHores < 24) {
                rightNow.add(Calendar.HOUR, 1);
                data_busqueda_string = dataToString(rightNow);
                sqlQuery = "SELECT count(*) from log_cerques WHERE data_cerca BETWEEN '" + data_anterior_string + "' AND '" + data_busqueda_string + "'";

                logger.debug(sqlQuery);
                rs = stmt.executeQuery(sqlQuery);
                rs.next();

                llistaValors += "" + rs.getInt(1);
                numHores++;
                if (numHores < 24) {
                    llistaValors += ",";
                }
                data_anterior_string = data_busqueda_string;
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return llistaValors;
    }

    public static ArrayList getContentsFromDUC(Connection myConnection, int idDUC, int idLevel, String tipus) {
        String sqlQuery = "";
        //  Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;

        ArrayList allAreas = null;
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            allAreas = new ArrayList();
            carregarDriverOracle();
            // Context initContext = new InitialContext();
            //  DataSource ds = (DataSource)initContext.lookup("java:/jdbc/pool/MerliConnectionPoolDS");    	      	
            //  myConnection = ds.getConnection();

            logger.debug("Create connexio");
            stmt = myConnection.createStatement();
            //   stmt2 = myConnection.createStatement(); 	      
            logger.debug("connexio created");
            if (tipus.equals("area")) {
                sqlQuery = "SELECT * FROM duc_info WHERE (relationType='RCA' or relationType='RCA') AND parent_id=" + idDUC + " and id NOT IN (SELECT id FROM duc_info where relationType='RCC')" + " and id IN (SELECT id FROM duc_info where relationType='RCL' and parent_id=" + idLevel + ")";
            } else {
                sqlQuery = "SELECT * FROM duc_info WHERE (relationType='RCC' or relationType='RCC') AND parent_id=" + idDUC;
            }
            logger.debug("SQL: " + sqlQuery);
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                DucObject ducObject = new DucObject();
                ducObject.id = rs.getInt("id");
                ducObject.term = rs.getString("term");
                ducObject.termEs = rs.getString("term_es");
                ducObject.termEn = rs.getString("term_en");
                ducObject.termOc = rs.getString("term_oc");
                // XTEC: Added this check to hide Competency area
                //if (!ducObject.term.startsWith("Compet?ncies")){
                ducObject.children = getChildrenDUC(myConnection, ducObject.id);
                if (ducObject.children.size() > 0) {
                    ducObject.hasChilds = true;
                } else {
                    ducObject.hasChilds = false;
                }
                allAreas.add(ducObject);
                //}
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                /* if (myConnection != null) {
                 myConnection.close();
                 } */
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return allAreas;
    }

    public static Hashtable getAllCicles(Connection myConnection, ArrayList allLevels) {
        Hashtable allCicles = null;
        try {
            allCicles = new Hashtable();
            int i = 0;
            while (i < allLevels.size()) {
                DucObject ducLevel = (DucObject) allLevels.get(i);
                ArrayList allCiclesLevel = getCiclesFromLevel(myConnection, ducLevel.id);
                if (allCiclesLevel.size() > 0) {
                    logger.debug("DUC: " + ducLevel.id + "__" + allCiclesLevel.size());
                    allCicles.put(new Integer(ducLevel.id), allCiclesLevel);
                }
                i++;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return allCicles;
    }

    public static Hashtable getAllAreas(Connection myConnection, ArrayList allLevels) {
        Hashtable allAreas = null;
        try {
            allAreas = new Hashtable();
            int i = 0;
            while (i < allLevels.size()) {
                DucObject ducLevel = (DucObject) allLevels.get(i);
                ArrayList allAreasLevel = getAreasFromLevel(myConnection, ducLevel.id);
                if (allAreasLevel.size() > 0) {
                    logger.debug("DUC: " + ducLevel.id + "__" + allAreasLevel.size());
                    allAreas.put(new Integer(ducLevel.id), allAreasLevel);
                }
                i++;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return allAreas;
    }

    public static ArrayList getCiclesFromLevel(Connection myConnection, int idLevel) {
        String sqlQuery = "";
        //  Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList allCicles = null;
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            allCicles = new ArrayList();
            carregarDriverOracle();
            // Context initContext = new InitialContext();
            //  DataSource ds = (DataSource)initContext.lookup("java:/jdbc/pool/MerliConnectionPoolDS");    	      	
            //  myConnection = ds.getConnection();

            stmt = myConnection.createStatement();
            sqlQuery = "SELECT * FROM duc_info WHERE relationType='RLL' AND parent_id=" + idLevel;
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                DucObject ducObject = new DucObject();
                ducObject.id = rs.getInt("id");
                ducObject.term = rs.getString("term");
                ducObject.termEs = rs.getString("term_es");
                ducObject.termEn = rs.getString("term_en");
                ducObject.termOc = rs.getString("term_oc");
//             ducObject.children = getChildrenDUC(myConnection, ducObject.id);
                allCicles.add(ducObject);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                /* if (myConnection != null) {
                 myConnection.close();
                 } */
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return allCicles;
    }

    public static ArrayList getAreasFromLevel(Connection myConnection, int idLevel) {
        String sqlQuery = "";
        //  Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList allAreas = null;

        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            allAreas = new ArrayList();
            carregarDriverOracle();

            stmt = myConnection.createStatement();
            sqlQuery = "SELECT * FROM duc_info WHERE relationType='RAL' AND parent_id=" + idLevel + " ORDER BY term ASC";
            logger.debug(sqlQuery);
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                DucObject ducObject = new DucObject();
                ducObject.id = rs.getInt("id");
                ducObject.term = rs.getString("term");
                ducObject.termEs = rs.getString("term_es");
                ducObject.termEn = rs.getString("term_en");
                ducObject.termOc = rs.getString("term_oc");
                // XTEC: Added this check to hide Competency area
                //if (!ducObject.term.startsWith("Compet?ncies")){
                allAreas.add(ducObject);
                //}
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                /* if (myConnection != null) {
                 myConnection.close();
                 } */
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return allAreas;
    }

    /*
     public static ArrayList getLevelFromArea (Connection myConnection, int idArea) {
     String sqlQuery = "";
     //  Connection myConnection = null;
     Statement stmt = null;
     ResultSet rs = null;
     ArrayList allAreas = null;
     try {
     if (Configuracio.isVoid()) {
     Configuracio.carregaConfiguracio();  
     }
     allAreas = new ArrayList();
     carregarDriverOracle();

     stmt = myConnection.createStatement();
     sqlQuery = "SELECT * FROM duc_info WHERE relationType='RAL' AND parent_id=" + idLevel;

     rs = stmt.executeQuery (sqlQuery);
     while (rs.next()) {
     DucObject ducObject = new DucObject();
     ducObject.id = rs.getInt("id");
     ducObject.term = rs.getString("term");

     allAreas.add(ducObject);
     }
     } catch (Exception e) {
     logger.error(e);	
     } finally {
     try {
     if (rs != null) {
     rs.close();
     }
     if (stmt != null) {
     stmt.close();
     }
     } catch (Exception e) {
     logger.error(e);	            	
     }
     }
     return allAreas;
     }
    
     */
    public static ArrayList getChildrenDUC(Connection myConnection, int ducId) {
        String sqlQuery = "";
        // Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList allChildren = null;
        try {
            logger.debug("getChildrenDUC");
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }

            allChildren = new ArrayList();
            stmt = myConnection.createStatement();
            sqlQuery = "SELECT * FROM duc_info WHERE parent_id=" + ducId;
            logger.debug(sqlQuery);
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                DucObject ducObject = new DucObject();
                ducObject.id = rs.getInt("id");
                ducObject.term = rs.getString("term");
                ducObject.termEs = rs.getString("term_es");
                ducObject.termEn = rs.getString("term_en");
                ducObject.termOc = rs.getString("term_oc");
                /*       ducObject.children = getChildrenDUC(myConnection, ducObject.id);
                 if (ducObject.children.size() > 0) {
                 ducObject.hasChilds = true;
                 } else {
                 ducObject.hasChilds = false;
                 }*/
                allChildren.add(ducObject);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return allChildren;
    }

    public static boolean hasChildsDUC(Connection myConnection, int ducId) {
        String sqlQuery = "";
        // Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }

            stmt = myConnection.createStatement();
            sqlQuery = "SELECT * FROM duc_info WHERE parent_id=" + ducId;
            logger.debug(sqlQuery);
            rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                logger.debug("Return true");
                return true;
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return false;
    }

    public static String getTermDuc(Connection myConnection, String idDuc, String lang) {
        String sqlQuery = "";
        Statement stmt = null;
        ResultSet rs = null;
        String term = "";
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }

            term = "term";
            if (lang != null && (" es; en; oc;".indexOf(" " + lang.toLowerCase() + ";") >= 0)) {
                term = "term_" + lang.toUpperCase();
            }

            stmt = myConnection.createStatement();
            sqlQuery = "SELECT term, " + term + " FROM duc_info WHERE id=" + idDuc;
            logger.debug("getTermDuc: " + sqlQuery);
            rs = stmt.executeQuery(sqlQuery);
            /*v2.2 java.sql.SQLException: Exhausted Resultset*/
            if(rs.next()){
                term = rs.getString(term);
                if (term == null || "".equals(term)) {
                    term = rs.getString("term");
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return term;
    }

    public static boolean hasChildLevels(Connection myConnection, String idDuc) {
        String sqlQuery = "";
        Statement stmt = null;
        ResultSet rs = null;
        boolean b = false;
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }

            stmt = myConnection.createStatement();
            sqlQuery = "SELECT * FROM duc_info WHERE parent_id=" + idDuc + " AND relationtype='RLL'";
            logger.debug(sqlQuery);
            rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                b = true;
            }

        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return b;
    }

    public static List getContextMapping(String ducId) {
        String sqlQuery = "";
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        List context = new ArrayList();
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            myConnection = getConnectionFromPool();

            stmt = myConnection.createStatement();
            sqlQuery = "SELECT * FROM duc_to_context WHERE duc_id=" + ducId;
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                context.add(rs.getString("context_id"));
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return context;
    }

    public static ArrayList getAllLevels(Connection myConnection) {
        String sqlQuery = "";
        // Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList allLevels = null;
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }

            allLevels = new ArrayList();
            stmt = myConnection.createStatement();
            sqlQuery = "SELECT * FROM duc_info WHERE parent_id=-1 order by id";
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                DucObject ducObject = new DucObject();
                ducObject.id = rs.getInt("id");
                ducObject.term = rs.getString("term");
                ducObject.termEs = rs.getString("term_es");
                ducObject.termEn = rs.getString("term_en");
                ducObject.termOc = rs.getString("term_oc");
                //  ducObject.children = getChildrenDUC(myConnection, ducObject.id);

                ducObject.hasChilds = hasChildsDUC(myConnection, ducObject.id);
                allLevels.add(ducObject);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                /*        	if (myConnection != null) {
                 myConnection.close();
                 }*/
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return allLevels;
    }

    public static Hashtable parseContent2Arco(Connection myConnection, String[] ducContent) {
        String sqlQuery = "";
        //  Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSet rsArc = null;
        String idArc;
        String terme;
        String ducIds = "";

        Hashtable result = null;
        //Sino hi ha contingut retorna una hash buida.
        if (ducContent == null || ducContent.length == 0) {
            return new Hashtable();
        }

        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            result = new Hashtable();
            UtilsCercador.carregarDriverOracle();

            logger.debug("Create connexio");
            stmt = myConnection.createStatement();
            //   stmt2 = myConnection.createStatement();
            logger.debug("connexio created");
            for (int i = 0; i < ducContent.length; i++) {
                if (i > 0) {
                    ducIds += ",";
                }
                ducIds += ducContent[i];
            }
            sqlQuery = "select id_arc from cur_to_arc where id_node in (" + ducIds + ")";

            logger.debug("SQL: " + sqlQuery);
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                idArc = rs.getString("id_arc");
                UtilsCercador.getAllArcoTerms(myConnection, idArc, result);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                /* if (myConnection != null) {
                 myConnection.close();
                 } */
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return result;
    }

    private static void getArcoTerm(Connection myConnection, String idArc, Hashtable result) {
        String sqlQuery;
        ResultSet rsArc = null;
        PreparedStatement stmt = null;
        String terme;
        String idArcElem;
        String path;
        sqlQuery = "select terme, id_arc from cur_arc_termes where id_arc=?";

        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            UtilsCercador.carregarDriverOracle();

            logger.debug("Create connexio");
            stmt = myConnection.prepareStatement(sqlQuery);
            stmt.setString(1, idArc);

            logger.debug("SQL: " + sqlQuery);

            String[] l;
            stmt.execute();
            rsArc = stmt.getResultSet();
            if (rsArc.next()) {
                terme = rsArc.getString("terme");
                idArcElem = rsArc.getString("id_arc");

                result.put(idArcElem, terme);
            }
        } catch (Exception e) {
        } finally {
            try {
                rsArc.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    private static void getAllArcoTerms(Connection myConnection, String idArc, Hashtable result) {
        String sqlQuery;
        String terme;
        String idArcElem;
        String path;
        String[] l;

        l = idArc.split("\\.");
        path = "";
        //Recuparem cada un dels elements del Path.
        for (int j = 0; j < l.length; j++) {
            if (j > 0) {
                path += ".";
            }
            path += l[j];
            getArcoTerm(myConnection, path, result);
        }

    }

    public static boolean isUserInRole(String usuari) {
        String sqlQuery = "";
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        boolean isInRole = false;
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            myConnection = getConnectionFromPool();
            stmt = myConnection.createStatement();
            if (usuari != null) {
                usuari = usuari.replaceAll("'", "''");
            }
            sqlQuery = "SELECT * FROM admin_users WHERE lower(xtec_username)=lower('" + usuari + "')";
            logger.debug(sqlQuery);
            rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                isInRole = true;
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        logger.debug("IsInRole: " + isInRole);
        return isInRole;
    }

    public static int getMaxCerquesChart(String valors) {
        int maxCerques = 0;
        StringTokenizer myTokenizer = null;
        try {
            myTokenizer = new StringTokenizer(valors, ",");
            while (myTokenizer.hasMoreTokens()) {
                String tempValue = (String) myTokenizer.nextToken();
                int tempValueInt = (new Integer(tempValue)).intValue();
                if (tempValueInt > maxCerques) {
                    maxCerques = tempValueInt;
                }
            }
            maxCerques = maxCerques + ((maxCerques * 25) / 100);
        } catch (Exception e) {
            logger.error(e);
        }
        return maxCerques;
    }

    public static Connection getConnectionFromPool() {
        Connection myConnection = null;
        try {
            Context initContext = new InitialContext();
            DataSource ds = null;
            //ds = (DataSource)initContext.lookup("jdbc/pool/CercadorConnectionPoolDS");
            logger.info("Weblogic");
            ds = (DataSource) initContext.lookup("jdbc/pool/CercadorConnectionPoolDS");
            logger.debug("Getting connection....");
            // DataSource ds = (DataSource)initContext.lookup("jdbc/pool/MerliConnectionPoolDS");
            myConnection = ds.getConnection();
            logger.debug("Connection get....");
        } catch (Exception e) {
            logger.error(e);
        }
        return myConnection;
    }
    /*NADIM*/
    public static Connection getConnectionFromPool(String connectionPool) {
        Connection myConnection = null;
        try {
            Context initContext = new InitialContext();
            DataSource ds = null;
            //ds = (DataSource)initContext.lookup("jdbc/pool/CercadorConnectionPoolDS");
            logger.info("Weblogic");
            ds = (DataSource) initContext.lookup(connectionPool);
            logger.debug("Getting connection....");
            // DataSource ds = (DataSource)initContext.lookup("jdbc/pool/MerliConnectionPoolDS");
            myConnection = ds.getConnection();
            logger.debug("Connection get....");
        } catch (Exception e) {
            logger.error(e);
        }
        return myConnection;
    }
    /*END NADIM*/
    public static ArrayList getComentarisSuspesosUsuari(String usuari) {
        String sqlQuery = "";
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList allComentaris = null;
        try {
            if (Configuracio.isVoid()) {
                Configuracio.carregaConfiguracio();
            }
            allComentaris = new ArrayList();

            myConnection = getConnectionFromPool();
            stmt = myConnection.createStatement();
            sqlQuery = "SELECT * FROM comentaris WHERE xtec_username='" + usuari + "' AND suspens=1";
            rs = stmt.executeQuery(sqlQuery);
            while (rs.next()) {
                ComentariObject comentariObject = new ComentariObject();
                comentariObject.id = (long) rs.getLong("id");
                comentariObject.setComentari((String) rs.getString("comentari"), 135);
                comentariObject.titol = (String) rs.getString("titol");
                comentariObject.puntuacio = (int) rs.getInt("puntuacio");
                comentariObject.autor = (String) rs.getString("xtec_username");
                comentariObject.dataEdicio = (String) rs.getString("data_edicio");
                comentariObject.idRecurs = (String) rs.getString("recurs_id");
                allComentaris.add(comentariObject);
            }

        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return allComentaris;
    }

    public static int getComentarisSuspesos(String login) {
        int comentarisSuspesos = 0;
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            myConnection = getConnectionFromPool();
            stmt = myConnection.createStatement();
            sql = "SELECT count(*) FROM comentaris WHERE xtec_username='" + login + "' and suspens=1";
            logger.debug(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                comentarisSuspesos = rs.getInt(1);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }

            } catch (Exception e) {
            }
        }
        return comentarisSuspesos;
    }

    public static String getNomComplet(String login) {
        String nomComplet = login;
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            myConnection = UtilsCercador.getConnectionFromPool("jdbc/pool/MerliConnectionPoolDS");
            /*stmt = myConnection.createStatement();
            sql = "SELECT nom, cognom1 FROM ADMXTEC.V_SSO_USUARIS_PERSONALS_XTEC WHERE lower(name)=lower('" + login + "')";
            //   	   sql = "SELECT nom, cognom1 FROM V_SSO_USUARIS_PERSONALS_XTEC WHERE lower(name)=lower('" + login + "')";
            logger.debug(sql);
            rs = stmt.executeQuery(sql);*/
            CallableStatement oFunction = myConnection.prepareCall("{ call admxtec.PKG_XTEC.DADESPERSONALS(?,?,?,?,?,?,?,?) }");
            logger.info("Connecting to: admxtec.PKG_XTEC.DADESPERSONALS(?,?,?,?,?,?,?,?)");
            oFunction.setString(1, login);
            oFunction.setString(2, "1");
            oFunction.registerOutParameter(3, Types.VARCHAR);
            oFunction.registerOutParameter(4, Types.VARCHAR);
            oFunction.registerOutParameter(5, Types.VARCHAR);
            oFunction.registerOutParameter(6, Types.VARCHAR);
            oFunction.registerOutParameter(7, Types.VARCHAR);
            oFunction.registerOutParameter(8, Types.VARCHAR);
            oFunction.execute();
            String sResult = oFunction.getString(3);

            logger.info("sResult: " + sResult);

            if ("1".equals(sResult)) {
                String nom = oFunction.getString(6);
                String cognom1 = oFunction.getString(7);
                nomComplet = nom + " " + cognom1;
                logger.info("nomComplete: " + nomComplet);
            }
            oFunction.close();
            /*if (rs.next()) {
                nomComplet = rs.getString("nom") + " " + rs.getString("cognom1");
            }*/
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                /*if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }*/
                if (myConnection != null) {
                    myConnection.close();
                }

            } catch (Exception e) {
                logger.error(e);
            }
        }
        return nomComplet;
    }

    public static Connection getConnectionFromPoolVistaUsuari() {
        Connection myConnection = null;
        try {
            Context initContext = new InitialContext();
            DataSource ds = null;
            try {
                // Jboss
                ds = (DataSource) initContext.lookup("java:/jdbc/pool/VistaUsuariConnectionPoolDS");
            } catch (NameNotFoundException e) {
                // Oracle
                ds = (DataSource) initContext.lookup("jdbc/pool/VistaUsuariConnectionPoolDS");
            }
            logger.debug("Getting connection....");
            // DataSource ds = (DataSource)initContext.lookup("jdbc/pool/MerliConnectionPoolDS");
            myConnection = ds.getConnection();
            logger.debug("Connection get....");
        } catch (Exception e) {
            logger.error(e);
        }
        return myConnection;
    }

    public static String getImageFormat(String format) {
        String imgUrl = "";
        try {

            imgUrl = (String) ((Hashtable) TipusFitxer.allTipusIcones).get(format);
            if (imgUrl == null || "".equals(imgUrl)) {
                imgUrl = (String) ((Hashtable) TipusFitxer.allTipusIcones).get(converteixString(format));
            }
            if (imgUrl != null && !"".equals(imgUrl)) {
                return imgUrl;
            }

            if (format == null) {
                return "";
            }
            if (format.trim().equalsIgnoreCase("Aplicació") || format.trim().equalsIgnoreCase("Aplicació")) {
                imgUrl = "aplicacio.gif";
            }
            if (format.trim().equalsIgnoreCase("Vídeo") || format.trim().equalsIgnoreCase("Vìdeo")) {
                imgUrl = "video.gif";
            }
            if (format.trim().equalsIgnoreCase("Interactiu Flash")) {
                imgUrl = "flash.gif";
            }
            if (format.trim().equalsIgnoreCase("Pàgina web") || format.trim().equalsIgnoreCase("P?gina web")) {
                imgUrl = "web.gif";
            }
            if (format.trim().equalsIgnoreCase("Imatge")) {
                imgUrl = "imatges.gif";
            }
            if (format.trim().equalsIgnoreCase("PDF")) {
                imgUrl = "pdf.gif";
            }
            if (format.trim().equalsIgnoreCase("Àudio") || format.trim().equalsIgnoreCase("Àudio")) {
                imgUrl = "audio.gif";
            }
            if (format.trim().equalsIgnoreCase("Fitxer comprimit")) {
                imgUrl = "compressio.gif";
            }
            if (format.trim().equalsIgnoreCase("Full de càlcul") || format.trim().equalsIgnoreCase("Full de c?lcul")) {
                imgUrl = "calcul.gif";
            }
            if (format.trim().equalsIgnoreCase("Presentació") || format.trim().equalsIgnoreCase("Presentaci?")) {
                imgUrl = "presentacion.gif";
            }
            if (format.trim().equalsIgnoreCase("Document de text")) {
                imgUrl = "text.gif";
            }
            if ("".equals(imgUrl)) {
                format = format.trim();
                if (converteixString(format, true).equals(converteixString("Aplicació", true))) {
                    imgUrl = "aplicacio.gif";
                }
                if (converteixString(format, true).equals(converteixString("Vídeo", true))) {
                    imgUrl = "video.gif";
                }
                if (converteixString(format, true).equals(converteixString("Interactiu Flash", true))) {
                    imgUrl = "flash.gif";
                }
                if (converteixString(format, true).equals(converteixString("Pàgina web", true))) {
                    imgUrl = "web.gif";
                }
                if (converteixString(format, true).equals(converteixString("Imatge", true))) {
                    imgUrl = "imatges.gif";
                }
                if (converteixString(format, true).equals(converteixString("PDF", true))) {
                    imgUrl = "pdf.gif";
                }
                if (converteixString(format, true).equals(converteixString("Àudio", true))) {
                    imgUrl = "audio.gif";
                }
                if (converteixString(format, true).equals(converteixString("Fitxer comprimit", true))) {
                    imgUrl = "compressio.gif";
                }
                if (converteixString(format, true).equals(converteixString("Full de càlcul", true))) {
                    imgUrl = "calcul.gif";
                }
                if (converteixString(format, true).equals(converteixString("Presentació", true))) {
                    imgUrl = "presentacion.gif";
                }
                if (converteixString(format, true).equals(converteixString("Document de text", true))) {
                    imgUrl = "text.gif";
                }

            }
        } catch (Exception e) {
            logger.error(e);
        }
        return "imatges/icones/" + imgUrl;
    }

    public static String getIndexActualFromDB() {
        String indexActualDB = "";
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            myConnection = getConnectionFromPool();
            stmt = myConnection.createStatement();
            sql = "SELECT current_index FROM config_indexacio";
            logger.debug(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                indexActualDB = rs.getString("current_index");
            }

        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }

            } catch (Exception e) {
            }
        }
        return indexActualDB;
    }

    public static void setIndexActualDB(String indexActual) {

        Connection myConnection = null;
        PreparedStatement prepStmt = null;
        String sql = "";
        try {
            myConnection = getConnectionFromPool();

            sql = "UPDATE config_indexacio SET current_index = ?";
            prepStmt = myConnection.prepareStatement(sql);
            prepStmt.setString(1, indexActual);
            logger.debug(sql);
            prepStmt.executeUpdate();

        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (prepStmt != null) {
                    prepStmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }

            } catch (Exception e) {
            }
        }

    }

    public static String getIndexActual(String index1, String index2) {
        File dir1 = null;
        File dir2 = null;
        int numFiles1 = 0;
        int numFiles2 = 0;
        File segments1 = null;
        File segments2 = null;
        File writeLock1 = null;
        File writeLock2 = null;
        String indexSelected = "";

        try {
            String indexActualDb = getIndexActualFromDB();
            if ((indexActualDb == null) || indexActualDb.equals("")) {

                dir1 = new File(index1);
                dir2 = new File(index2);
                if (!dir1.isDirectory()) {
                    indexSelected = index2;
                }
                if (!dir2.isDirectory()) {
                    indexSelected = index1;
                }
                numFiles1 = dir1.list().length;
                logger.debug("numFiles1: " + numFiles1);
                numFiles2 = dir2.list().length;
                logger.debug("numFiles2: " + numFiles2);
                if ((numFiles1 > 4) || (numFiles1 < 3)) {
                    indexSelected = index2;

                }
                if ((numFiles2 > 4) || (numFiles2 < 3)) {
                    indexSelected = index1;
                }
                if ((numFiles1 > 0) && (numFiles2 > 0)) {
                    segments1 = new File(index1 + "/" + dir1.list()[0]);
                    segments2 = new File(index2 + "/" + dir2.list()[0]);
                    Date date1 = new Date(segments1.lastModified());
                    logger.debug("Date 1: " + date1);
                    Date date2 = new Date(segments2.lastModified());
                    logger.debug("Date 2: " + date2);
                    if (date2.after(date1)) {
                        indexSelected = index2;
                    } else {
                        indexSelected = index1;
                    }
                }
                setIndexActualDB(indexSelected);
            } else {
                indexSelected = indexActualDb;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return indexSelected;
    }

    public boolean isValidUSer(String usuari) {
        boolean isValid = true;
        try {
            if ((usuari == null) && Configuracio.sso.equals("si")) {
                isValid = false;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return isValid;
    }

    public static String getLastUrl(HttpServletRequest request) {
        String lastUrl = "";
        int numParams = 0;
        try {
            //  lastUrl = new String(HttpUtils.getRequestURL(request));
            lastUrl = request.getRequestURI();
            Enumeration allParameters = request.getParameterNames();
            while (allParameters.hasMoreElements()) {
                String nameParam = (String) allParameters.nextElement();
                String valueParam = (String) request.getParameter(nameParam);
                if (numParams == 0) {
                    lastUrl += "?";
                } else {
                    lastUrl += "&";
                }
                numParams++;
                lastUrl += nameParam + "=" + valueParam;

            }
        } catch (Exception e) {
            logger.error(e);
        }
        logger.info("LastURL: "+ lastUrl);
        return lastUrl;
    }

    public static String fromAcute(String original) {
        String modificada = "";
        try {
            modificada = original;
            modificada = modificada.replaceAll("&iacute;", "í");
            modificada = modificada.replaceAll("&oacute;", "ó");
            modificada = modificada.replaceAll("&aacute;", "á");
            modificada = modificada.replaceAll("&eacute;", "é");
            modificada = modificada.replaceAll("&uacute;", "ú");
            modificada = modificada.replaceAll("&Oacute;", "Ó");
            modificada = modificada.replaceAll("&Eacute;", "É");
            modificada = modificada.replaceAll("&Iacute;", "Í");
            modificada = modificada.replaceAll("&Aacute;", "Á");
            modificada = modificada.replaceAll("&Uacute;", "Ú");
            modificada = modificada.replaceAll("&ograve;", "ò");
            modificada = modificada.replaceAll("&agrave;", "à");
            modificada = modificada.replaceAll("&egrave;", "è");
            modificada = modificada.replaceAll("&Ograve;", "Ò");
            modificada = modificada.replaceAll("&Agrave;", "À");
            modificada = modificada.replaceAll("&Egrave;", "È");
            modificada = modificada.replaceAll("&ccedil;", "ç");
            modificada = modificada.replaceAll("&Ccedil;", "Ç");

        } catch (Exception e) {
            logger.error(e);
        }
        return modificada;
    }

    public static String converteixString(String input) {
        return converteixString(input, true);
    }

    public static String converteixString(String input, boolean matches) {
        if (input == null) {
            return "";
        }
        input = input.toLowerCase().trim().replaceAll("[^a-zA-Z]", "%");
        if (matches) {
            input = input.replaceAll("[%]+", "%");
        }
        return input;
    }

    public static String toAcute(String original) {
        String modificada = "";
        try {
            modificada = original;
            modificada = modificada.replaceAll("í", "&iacute;");
            modificada = modificada.replaceAll("ó", "&oacute;");
            modificada = modificada.replaceAll("á", "&aacute;");
            modificada = modificada.replaceAll("é", "&eacute;");
            modificada = modificada.replaceAll("ú", "&uacute;");
            modificada = modificada.replaceAll("Ó", "&Oacute;");
            modificada = modificada.replaceAll("É", "&Eacute;");
            modificada = modificada.replaceAll("Í", "&Iacute;");
            modificada = modificada.replaceAll("Á", "&Aacute;");
            modificada = modificada.replaceAll("Ú", "&Uacute;");
            modificada = modificada.replaceAll("ò", "&ograve;");
            modificada = modificada.replaceAll("à", "&agrave;");
            modificada = modificada.replaceAll("è", "&egrave;");
            modificada = modificada.replaceAll("Ò", "&Ograve;");
            modificada = modificada.replaceAll("À", "&Agrave;");
            modificada = modificada.replaceAll("È", "&Egrave;");
            modificada = modificada.replaceAll("ç", "&ccedil;");
            modificada = modificada.replaceAll("Ç", "&Ccedil;");

        } catch (Exception e) {
            logger.error(e);
        }
        return modificada;
    }

    public static String treureAccents(String original) {
        String modificada = "";
        if (original != null) {
            try {
                modificada = original.toLowerCase();
                modificada = modificada.replaceAll("á", "a");
                modificada = modificada.replaceAll("à", "a");
                modificada = modificada.replaceAll("í", "i");
                modificada = modificada.replaceAll("ú", "u");
                modificada = modificada.replaceAll("é", "e");
                modificada = modificada.replaceAll("è", "e");
                modificada = modificada.replaceAll("ó", "o");
                modificada = modificada.replaceAll("ò", "o");
                modificada = modificada.replaceAll("ï", "i");
                modificada = modificada.replaceAll("ü", "u");
            } catch (Exception e) {
                logger.error(e);
            }
        }
        return modificada;
    }

    /*
     public static boolean indexIsOk (String indexDir) {
     IndexSearcher searcher = null;
     FSDirectory indexDirectory = null;
     int maxDoc = 0;
     boolean isOk = false;
     try {
     logger.debug("indexIsOk -> in");
     indexDirectory = FSDirectory.getDirectory(indexDir);
     logger.debug("Checking... " + indexDir);
     if (IndexReader.isLocked(indexDirectory)) {
     logger.debug("Index locked...."  + indexDirectory);
     IndexReader.unlock(indexDirectory);
     }
     if (!IndexReader.indexExists(indexDirectory)) {
     logger.debug("Index no existeix...." + indexDirectory);
     isOk = false;
     }
     logger.debug("Creating searcher...");
     searcher = new IndexSearcher(indexDirectory);
     maxDoc = searcher.maxDoc();
     logger.debug("Max doc..." + maxDoc);
     // if (maxDoc <= 1000) {
     if (maxDoc <= 1000) {
     isOk = false;
     } else {
     isOk = true;
     }
     logger.debug("indexIsOk -> out");
     } catch (Exception e) {
     logger.error(e);
     isOk = false;
     } finally {
     try {
     searcher.close();
     } catch  (Exception e) {
     }
     }
     return isOk;
     }
     */
    public static ArrayList getAllLanguages() {
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        ArrayList allLanguages = null;
        ArrayList allCodes = null;
        int pos = 0;
        boolean found = false;
        try {
            allLanguages = new ArrayList();
            allCodes = new ArrayList();
            myConnection = getConnectionFromPool();
            stmt = myConnection.createStatement();
            sql = "SELECT distinct(idioma) FROM idioma_recurs";
            logger.info(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String code = rs.getString(1);
                StringTokenizer myTokenizer = new StringTokenizer(code);
                if (myTokenizer != null) {
                    while (myTokenizer.hasMoreTokens()) {
                        String myCode = (String) myTokenizer.nextToken();
                        logger.info("code -> " + myCode);
                        String name = getLanguageName(myCode);
                        Idioma myIdioma = new Idioma(myCode, name);
                        if (!allCodes.contains(myCode)) {
                            pos = 0;
                            found = false;
                            while (!found && pos < allCodes.size()) {
                                if (name.compareTo(((Idioma) allLanguages.get(pos)).getName()) < 0) {
                                    found = true;
                                } else {
                                    pos++;
                                }
                            }
                            allLanguages.add(pos, myIdioma);
                            allCodes.add(pos, myCode);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }

            } catch (Exception e) {
            }
        }
        return allLanguages;

    }

    public static String getLanguageName(String code) {
        String language = "";
        String llengues = "";
        try {
            String[] llengs = code.split(" ");
            for (int i = 0; i < llengs.length; i++) {
                code = llengs[i].trim();
                language = code;

                if (code.equalsIgnoreCase("aa")) {
                    language = "Afar";
                }
                if (code.equalsIgnoreCase("ab")) {
                    language = "Abkhazian";
                }
                if (code.equalsIgnoreCase("af")) {
                    language = "Afrikaans";
                }
                if (code.equalsIgnoreCase("am")) {
                    language = "Amharic";
                }
                if (code.equalsIgnoreCase("ar")) {
                    language = "&Agrave;rab";
                }
                if (code.equalsIgnoreCase("as")) {
                    language = "Assamese";
                }
                if (code.equalsIgnoreCase("ay")) {
                    language = "Aymara";
                }
                if (code.equalsIgnoreCase("az")) {
                    language = "Azerbaijani";
                }
                if (code.equalsIgnoreCase("ba")) {
                    language = "Bashkir";
                }
                if (code.equalsIgnoreCase("be")) {
                    language = "Byelorussian";
                }
                if (code.equalsIgnoreCase("bg")) {
                    language = "B&uacute;lgar";
                }
                if (code.equalsIgnoreCase("bh")) {
                    language = "Bihari";
                }
                if (code.equalsIgnoreCase("bi")) {
                    language = "Bislama";
                }
                if (code.equalsIgnoreCase("bn")) {
                    language = "Bengal&iacute;";
                }
                if (code.equalsIgnoreCase("bo")) {
                    language = "Tibet&agrave;";
                }
                if (code.equalsIgnoreCase("br")) {
                    language = "Bret&oacute;";
                }
                if (code.equalsIgnoreCase("ca")) {
                    language = "Catal&agrave;";
                }
                if (code.equalsIgnoreCase("co")) {
                    language = "Corsican";
                }
                if (code.equalsIgnoreCase("cs")) {
                    language = "Txec";
                }
                if (code.equalsIgnoreCase("cy")) {
                    language = "Welsh";
                }
                if (code.equalsIgnoreCase("da")) {
                    language = "Dan&egrave;s";
                }
                if (code.equalsIgnoreCase("de")) {
                    language = "Alemany";
                }
                if (code.equalsIgnoreCase("dz")) {
                    language = "Bhutani";
                }
                if (code.equalsIgnoreCase("el")) {
                    language = "Grec";
                }
                if (code.equalsIgnoreCase("en")) {
                    language = "Angl&egrave;s";
                }
                if (code.equalsIgnoreCase("eo")) {
                    language = "Esperanto";
                }
                if (code.equalsIgnoreCase("es")) {
                    language = "Castell&agrave;";
                }
                if (code.equalsIgnoreCase("et")) {
                    language = "Estoni&agrave;";
                }
                if (code.equalsIgnoreCase("eu")) {
                    language = "&Egrave;uscar";
                }
                if (code.equalsIgnoreCase("fa")) {
                    language = "Persian";
                }
                if (code.equalsIgnoreCase("fi")) {
                    language = "Fin&egrave;s";
                }
                if (code.equalsIgnoreCase("fj")) {
                    language = "Fiji";
                }
                if (code.equalsIgnoreCase("fo")) {
                    language = "Faroese";
                }
                if (code.equalsIgnoreCase("fr")) {
                    language = "Franc&egrave;s";
                }
                if (code.equalsIgnoreCase("fy")) {
                    language = "Frisian";
                }
                if (code.equalsIgnoreCase("ga")) {
                    language = "Ga&egrave;lic irland&egrave;s";
                }
                if (code.equalsIgnoreCase("gd")) {
                    language = "Scots";
                }
                if (code.equalsIgnoreCase("gl")) {
                    language = "Gallec";
                }
                if (code.equalsIgnoreCase("gn")) {
                    language = "Guaran&iacute;";
                }
                if (code.equalsIgnoreCase("gu")) {
                    language = "Gujarati";
                }
                if (code.equalsIgnoreCase("ha")) {
                    language = "Hausa";
                }
                if (code.equalsIgnoreCase("he")) {
                    language = "Hebrew";
                }
                if (code.equalsIgnoreCase("hi")) {
                    language = "Hindi-urd&uacute;";
                }
                if (code.equalsIgnoreCase("hr")) {
                    language = "Croatian";
                }
                if (code.equalsIgnoreCase("hu")) {
                    language = "Hongar&egrave;s";
                }
                if (code.equalsIgnoreCase("hy")) {
                    language = "Armeni";
                }
                if (code.equalsIgnoreCase("ia")) {
                    language = "Interlingua";
                }
                if (code.equalsIgnoreCase("id")) {
                    language = "Indonesian";
                }
                if (code.equalsIgnoreCase("ie")) {
                    language = "Interlingue";
                }
                if (code.equalsIgnoreCase("ik")) {
                    language = "Inupiak";
                }
                if (code.equalsIgnoreCase("is")) {
                    language = "Icelandic";
                }
                if (code.equalsIgnoreCase("it")) {
                    language = "Itali&agrave;";
                }
                if (code.equalsIgnoreCase("iu")) {
                    language = "Inuktitut";
                }
                if (code.equalsIgnoreCase("ja")) {
                    language = "Japon&egrave;s";
                }
                if (code.equalsIgnoreCase("jw")) {
                    language = "Javanese";
                }
                if (code.equalsIgnoreCase("ka")) {
                    language = "Georgi&agrave;";
                }
                if (code.equalsIgnoreCase("kk")) {
                    language = "Kazakh";
                }
                if (code.equalsIgnoreCase("kl")) {
                    language = "Greenlandic";
                }
                if (code.equalsIgnoreCase("km")) {
                    language = "Cambodian";
                }
                if (code.equalsIgnoreCase("kn")) {
                    language = "Kannada";
                }
                if (code.equalsIgnoreCase("ko")) {
                    language = "Core&agrave;";
                }
                if (code.equalsIgnoreCase("ks")) {
                    language = "Kashmiri";
                }
                if (code.equalsIgnoreCase("ku")) {
                    language = "Kurdish";
                }
                if (code.equalsIgnoreCase("ky")) {
                    language = "Kirghiz";
                }
                if (code.equalsIgnoreCase("la")) {
                    language = "Llat&iacute;";
                }
                if (code.equalsIgnoreCase("ln")) {
                    language = "Lingala";
                }
                if (code.equalsIgnoreCase("lo")) {
                    language = "Laothian";
                }
                if (code.equalsIgnoreCase("lt")) {
                    language = "Litu&agrave;";
                }
                if (code.equalsIgnoreCase("lv")) {
                    language = "Let&oacute;";
                }
                if (code.equalsIgnoreCase("mg")) {
                    language = "Malagasy";
                }
                if (code.equalsIgnoreCase("mi")) {
                    language = "Maori";
                }
                if (code.equalsIgnoreCase("mk")) {
                    language = "Macedonian";
                }
                if (code.equalsIgnoreCase("ml")) {
                    language = "Malayalam";
                }
                if (code.equalsIgnoreCase("mn")) {
                    language = "Mongolian";
                }
                if (code.equalsIgnoreCase("mo")) {
                    language = "Moldavian";
                }
                if (code.equalsIgnoreCase("mr")) {
                    language = "Marathi";
                }
                if (code.equalsIgnoreCase("ms")) {
                    language = "Malay";
                }
                if (code.equalsIgnoreCase("mt")) {
                    language = "Maltese";
                }
                if (code.equalsIgnoreCase("my")) {
                    language = "Burmese";
                }
                if (code.equalsIgnoreCase("na")) {
                    language = "Nauru";
                }
                if (code.equalsIgnoreCase("ne")) {
                    language = "Nepali";
                }
                if (code.equalsIgnoreCase("nl")) {
                    language = "Neerland&egrave;s";
                }
                if (code.equalsIgnoreCase("no")) {
                    language = "Noruec";
                }
                if (code.equalsIgnoreCase("oc")) {
                    language = "Occit&agrave;";
                }
                if (code.equalsIgnoreCase("om")) {
                    language = "Afan";
                }
                if (code.equalsIgnoreCase("or")) {
                    language = "Oriya";
                }
                if (code.equalsIgnoreCase("pa")) {
                    language = "Panjabi";
                }
                if (code.equalsIgnoreCase("pl")) {
                    language = "Polon&egrave;s";
                }
                if (code.equalsIgnoreCase("ps")) {
                    language = "Pashto,";
                }
                if (code.equalsIgnoreCase("pt")) {
                    language = "Portugu&egrave;s";
                }
                if (code.equalsIgnoreCase("qu")) {
                    language = "Qu&iacute;txua";
                }
                if (code.equalsIgnoreCase("rm")) {
                    language = "Rhaeto-Romance";
                }
                if (code.equalsIgnoreCase("rn")) {
                    language = "Kirundi";
                }
                if (code.equalsIgnoreCase("ro")) {
                    language = "Ruman&egrave;s";
                }
                if (code.equalsIgnoreCase("ru")) {
                    language = "Rus";
                }
                if (code.equalsIgnoreCase("rw")) {
                    language = "Kinyarwanda";
                }
                if (code.equalsIgnoreCase("sa")) {
                    language = "S&agrave;nscrit";
                }
                if (code.equalsIgnoreCase("sd")) {
                    language = "Sindhi";
                }
                if (code.equalsIgnoreCase("sg")) {
                    language = "Sangho";
                }
                if (code.equalsIgnoreCase("sh")) {
                    language = "Serbocroat";
                }
                if (code.equalsIgnoreCase("si")) {
                    language = "Sinhalese";
                }
                if (code.equalsIgnoreCase("sk")) {
                    language = "Eslovac";
                }
                if (code.equalsIgnoreCase("sl")) {
                    language = "Eslov&egrave;";
                }
                if (code.equalsIgnoreCase("sm")) {
                    language = "Samoan";
                }
                if (code.equalsIgnoreCase("sn")) {
                    language = "Shona";
                }
                if (code.equalsIgnoreCase("so")) {
                    language = "Somali";
                }
                if (code.equalsIgnoreCase("sq")) {
                    language = "Alban&egrave;s";
                }
                if (code.equalsIgnoreCase("sr")) {
                    language = "Serbian";
                }
                if (code.equalsIgnoreCase("ss")) {
                    language = "Siswati";
                }
                if (code.equalsIgnoreCase("st")) {
                    language = "Sesotho";
                }
                if (code.equalsIgnoreCase("su")) {
                    language = "Sundanese";
                }
                if (code.equalsIgnoreCase("sv")) {
                    language = "Suec";
                }
                if (code.equalsIgnoreCase("sw")) {
                    language = "Swahili";
                }
                if (code.equalsIgnoreCase("ta")) {
                    language = "Tamil";
                }
                if (code.equalsIgnoreCase("te")) {
                    language = "Telugu";
                }
                if (code.equalsIgnoreCase("tg")) {
                    language = "Tajik";
                }
                if (code.equalsIgnoreCase("th")) {
                    language = "Thai";
                }
                if (code.equalsIgnoreCase("ti")) {
                    language = "Tigrinya";
                }
                if (code.equalsIgnoreCase("tk")) {
                    language = "Turkmen";
                }
                if (code.equalsIgnoreCase("tl")) {
                    language = "Tag&agrave;log o Filip&iacute;";
                }
                if (code.equalsIgnoreCase("tn")) {
                    language = "Setswana";
                }
                if (code.equalsIgnoreCase("to")) {
                    language = "Tonga";
                }
                if (code.equalsIgnoreCase("tr")) {
                    language = "Turkish";
                }
                if (code.equalsIgnoreCase("ts")) {
                    language = "Tsonga";
                }
                if (code.equalsIgnoreCase("tt")) {
                    language = "Tatar";
                }
                if (code.equalsIgnoreCase("tw")) {
                    language = "Twi";
                }
                if (code.equalsIgnoreCase("ug")) {
                    language = "Uighur";
                }
                if (code.equalsIgnoreCase("uk")) {
                    language = "Ucra&iuml;n&egrave;s";
                }
                if (code.equalsIgnoreCase("ur")) {
                    language = "Hindi-urd&uacute;";
                }
                if (code.equalsIgnoreCase("uz")) {
                    language = "Uzbek";
                }
                if (code.equalsIgnoreCase("vi")) {
                    language = "Vietnamese";
                }
                if (code.equalsIgnoreCase("vo")) {
                    language = "Volapuk";
                }
                if (code.equalsIgnoreCase("wo")) {
                    language = "W&ograve;lof";
                }
                if (code.equalsIgnoreCase("xh")) {
                    language = "Xhosa";
                }
                if (code.equalsIgnoreCase("yi")) {
                    language = "Yiddish";
                }
                if (code.equalsIgnoreCase("yo")) {
                    language = "Ioruba";
                }
                if (code.equalsIgnoreCase("za")) {
                    language = "Zhuang";
                }
                if (code.equalsIgnoreCase("zh")) {
                    language = "Xin&egrave;s";
                }
                if (code.equalsIgnoreCase("zu")) {
                    language = "Zulu";
                }
                if (code.equalsIgnoreCase("an")) {
                    language = "Aragon&egrave;s";
                }
                if (code.equalsIgnoreCase("bm")) {
                    language = "B&agrave;mbara";
                }
                if (code.equalsIgnoreCase("ff")) {
                    language = "Ful";
                }

                if (i > 0) {
                    llengues += "; " + language;
                } else {
                    llengues += language;
                }
            }

        } catch (Exception e) {
            logger.error(e);
        }
        return llengues;
    }

    public static ArrayList getUnitats() {
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        ArrayList allUnits = null;
        Idioma unitat;
        try {
            allUnits = new ArrayList();
            myConnection = getConnectionFromPool();
            stmt = myConnection.createStatement();
            sql = "SELECT id_unitat, v_nom FROM mer_unitats order by upper(v_nom)";
            logger.info(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                unitat = new Idioma(rs.getString(1), rs.getString(2));
                allUnits.add(unitat);
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }

            } catch (Exception e) {
            }
        }
        return allUnits;
    }

    public static String getUnitatUrl(String idUnitat) {
        Connection myConnection = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";
        String url = null;
        try {
            myConnection = getConnectionFromPool();
            sql = "SELECT v_url FROM mer_unitats WHERE id_unitat='" + idUnitat + "'";
            logger.info(sql);
            stmt = myConnection.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                url = rs.getString("v_url");
            }
        } catch (Exception e) {
            logger.error(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (myConnection != null) {
                    myConnection.close();
                }

            } catch (Exception e) {
            }
        }
        return url;
    }

    public static void clearDir(String rootDir) {
        File f = null;
        try {
            f = new File(rootDir);
            String[] allFiles = f.list();
            int i = 0;
            while (i < allFiles.length) {
                String fileToDeleteString = rootDir + "/" + allFiles[i];
                logger.info("Deleting file: " + fileToDeleteString);
                File fileToDelete = new File(fileToDeleteString);
                if (!fileToDelete.isDirectory()) {
                    if (fileToDelete.delete()) {
                        logger.warn("Delete Ok!");
                    }
                }
                i++;
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static Long copyDir(String rootDir, String targetDir) {
        File f = null;
        Long result = null;
        try {
            f = new File(rootDir);
            String[] allFiles = f.list();
            int i = 0;
            while (i < allFiles.length) {
                String fileRootString = rootDir + "/" + allFiles[i];
                File fileRoot = new File(fileRootString);
                String fileTargetString = targetDir + "/" + allFiles[i];
                File fileTarget = new File(fileTargetString);
                logger.info("File root: " + fileRootString);
                logger.info("File target: " + fileTargetString);
                try {
                    if (!fileRoot.isDirectory()) {
                        Long temp = CopyFile.copyFile(fileRoot, fileTarget);
                        if (temp == null) {
                            return null;
                        } else {
                            result = temp;
                        }
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
                i++;
            }
        } catch (Exception e) {
            logger.error(e);
        }
        return result;
    }

    public static boolean indexIsOk(String indexDir) {
        IndexSearcher searcher = null;
        FSDirectory indexDirectory = null;
        int maxDoc = 0;
        boolean isOk = false;
        try {
            logger.debug("indexIsOk -> in");
            indexDirectory = FSDirectory.getDirectory(indexDir);
            logger.debug("Checking... " + indexDir);
            if (IndexReader.isLocked(indexDirectory)) {
                logger.debug("Index locked...." + indexDirectory);
                IndexReader.unlock(indexDirectory);
            }
            if (!IndexReader.indexExists(indexDirectory)) {
                logger.debug("Index no existeix...." + indexDirectory);
                isOk = false;
            }
            logger.debug("Creating searcher...");
            searcher = new IndexSearcher(indexDirectory);
            maxDoc = searcher.maxDoc();
            logger.debug("Max doc..." + maxDoc);
            //     if (maxDoc <= 500) {
            if (maxDoc <= 10) {
                isOk = false;
            } else {
                isOk = true;
            }
            logger.debug("indexIsOk -> out");
        } catch (Exception e) {
            logger.error(e);
            isOk = false;
        } finally {
            try {
                searcher.close();
            } catch (Exception e) {
            }
        }
        logger.info("Index ok -> " + isOk);
        return isOk;
    }

    public synchronized static void recoverFromBackup(String indexDir) {
        String sourceDir = indexDir + "/tmpBackup";
        try {
            logger.info("recoverFromBackup -> in");
            logger.info("clearing Dir -> " + indexDir);
            UtilsCercador.clearDir(indexDir);
            logger.info("copying Dir From -> " + sourceDir);
            logger.info("copying Dir To -> " + indexDir);
            UtilsCercador.copyDir(sourceDir, indexDir);
            logger.info("recoverFromBackup -> out");
        } catch (Exception e) {
            logger.error(e);
            logger.error("copying Dir From -> " + sourceDir);
            logger.error("copying Dir To -> " + indexDir);
        }
    }

    public synchronized static void recoverFromTmp(String indexDir) {
        String sourceDir = indexDir + "/tmp";
        try {
            logger.info("recoverFromTmp -> in");
            logger.info("clearing Dir -> " + indexDir);
            UtilsCercador.clearDir(indexDir);
            logger.info("copying Dir From -> " + sourceDir);
            logger.info("copying Dir To -> " + indexDir);
            UtilsCercador.copyDir(sourceDir, indexDir);
            logger.error("recoverFromTmp -> out");
        } catch (Exception e) {
            logger.error(e);
            logger.error("copying Dir From -> " + sourceDir);
            logger.error("copying Dir To -> " + indexDir);
        }
    }

    public static String parseDurationString(String duration) {
        int time = Integer.parseInt(duration);
        String durationLom = "";
        int aux;
        //minuts
        aux = time % 60;
        time = time / 60;
        if (aux > 0) {
            durationLom = aux + "M";
        }
        if (time > 0) {
            //hores
            aux = time % 24;
            time = time / 24;
            if (aux > 0) {
                durationLom = "T" + aux + "H" + durationLom;
            }
            if (time > 0) {
                //dies
                aux = time % 31;
                time = time / 31;
                if (aux > 0) {
                    durationLom = aux + "D" + durationLom;
                }
                if (time > 0) {
                    //Mesos
                    aux = time % 12;
                    time = time / 12;
                    if (aux > 0) {
                        durationLom = aux + "M" + durationLom;
                    }
                    if (time > 0) {
                        //anys
                        durationLom = time + "Y" + durationLom;
                    }
                }
            }
        } else {
            durationLom = "T" + durationLom;
        }

        durationLom = "P" + durationLom;

        return durationLom;
    }

    public static ArrayList parseFormatValues(String format) {
        ArrayList response = null;
        TipusFitxer.carregaTipusFitxer();
        Hashtable allTipusFitxer = TipusFitxer.allTipusFitxer;
        Enumeration allKeys = allTipusFitxer.keys();
        String actKey;
        while (response == null && allKeys.hasMoreElements()) {
            actKey = (String) allKeys.nextElement();
            if (format.equals(UtilsCercador.treureAccents(actKey))) {
                response = (ArrayList) allTipusFitxer.get(actKey);
            }
        }

        return response;
    }

    public static String getLoggedIn(){

        return "";
    }
}

