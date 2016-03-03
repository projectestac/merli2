package edu.xtec.merli.segur.login;

import edu.xtec.merli.segur.User;
import edu.xtec.merli.utils.Utility;
import edu.xtec.util.db.ConnectionBean;
import edu.xtec.util.db.ConnectionBeanProvider;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Hashtable;
import org.apache.log4j.Logger;

/**
 * @author Aleix Canals.
 *
 * Basat en codi de: Mike Robinson
 *
 */
public class LoginBean {

    private static final Logger logger = Logger.getRootLogger();//("xtec.duc");

    protected static ConnectionBeanProvider broker;
    private HashMap validUsers = new HashMap();

    /**
     * Search for this user in the system.
     *
     * @param userId
     * @return users profile. If user valid returns profil > 0, 0 otherwise.
     */
    public boolean validateUserYY(String userId, String password) {
        // TODO Auto-generated method stub		
        String result;
        //logger.info("Looking for user information on the system.");

        try {
            ConnectionBean cb = this.connectBD();
            Statement stmt = cb.getConnection().createStatement();
            ResultSet rs;

            try {
                rs = stmt.executeQuery("SELECT v_user FROM mer_users WHERE v_user = '" + userId + "'");
                while (rs.next()) {
                    result = rs.getString("v_user");
                    if (result.compareTo(userId) == 0) {
                        //logger.info("user found in the system");
                        return true;
                    }
                }
                stmt.close();
            } catch (Exception e) {
                logger.warn("Error trying to validate user.");
            }
            //c.close();
            this.disconnectBD(cb);
        } catch (SQLException e) {
            logger.warn("Error trying to validate user.");
            e.printStackTrace();
        } catch (Exception e) {
            logger.warn("Error trying to validate user.");
            e.printStackTrace();
        }

        return false;
    }

    public boolean validateUser(String userId, String password) {
        boolean bResult = false;
        ConnectionBean cb = null;
        //logger.info("Looking for user information on the system.");
        try {

            cb = this.connectBD();
            //CallableStatement oFunction = cb.getConnection().prepareCall("{ ? = call admxtec.FN_E13_MERLI_PASSWD_OK(?,?) }");
            CallableStatement oFunction = cb.getConnection().prepareCall("{ call admxtec.PKG_XTEC.AUTENTICACIO(?,?,?) }");
            /*oFunction.registerOutParameter(1, Types.INTEGER);
            oFunction.setString(2, userId);
            oFunction.setString(3, password);*/
            oFunction.setString(1, userId);
            oFunction.setString(2, password);
            oFunction.registerOutParameter(3, Types.INTEGER);
            oFunction.execute();
            //String sResult = oFunction.getString(1);
            String sResult = oFunction.getString(3);

            oFunction.close();
            if ("1".equals(sResult)) {
                bResult = true;
            }
            //logger.info("Return from DDBB FN_E13_MERL_PASSWD_OK [" + userId + "]: " + sResult);
            logger.info("Return from DDBB admxtec.PKG_XTEC.AUTENTICACIO [" + userId + "]: " + sResult);
            //if (!sResult.equals("0")){
            //.getConnection().createStatement();
            //rs = stmt.executeQuery("SELECT v_user FROM mer_users WHERE v_user = '"+userId+"'");	
//				MessageDigest md = MessageDigest.getInstance("SHA-1");
//				byte[] msg = password.getBytes();
//				md.update(msg);
//				byte[] aMessageDigest = md.digest();
//				password=new String(aMessageDigest);

//NADIM - Commented to activate the CallableStatement 21/01/2015
          /*password = Utility.getEncoded(password);

             String sResult;
             PreparedStatement pstmt = cb.getPreparedStatement("SELECT v_user FROM mer_users WHERE v_user = ? AND v_contrasenya = ?");
             pstmt.setString(1, userId);
             pstmt.setString(2, password);
             ResultSet rs = pstmt.executeQuery();
             while (rs.next() && !bResult) {
             sResult = rs.getString("v_user");
             bResult = sResult.compareTo(userId) == 0;
             }
             pstmt.close();*/
            //}
        } catch (SQLException e) {
            logger.error("Error trying to validate user " + userId + "--- " + e);
            e.printStackTrace();
        } catch (Exception e) {
            logger.error("Error trying to validate user " + userId);
            e.printStackTrace();
        } finally {
            this.disconnectBD(cb);
        }
        return bResult;
    }

    /**
     * Search for this user in the system.
     *
     * @param userId
     * @return users profile. If user valid returns profil > 0, 0 otherwise.
     * @throws
     */
    public boolean validateUserXX(String userId, String password) {
        // TODO Auto-generated method stub		

//		 declaration section:
        Socket smtpSocket = null;
        DataOutputStream os = null;
        DataInputStream is = null;
        int resp = 0;

//		 Initialization section:
//		 Try to open a socket on port 110
//		 Try to open input and output streams
        try {
            smtpSocket = new Socket("www.xtec.net", 110);
            os = new DataOutputStream(smtpSocket.getOutputStream());
            is = new DataInputStream(smtpSocket.getInputStream());
        } catch (UnknownHostException e) {
            logger.warn("Don't know about host: hostname->" + e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.warn("Couldn't get I/O for the connection to: hostname->" + e);
            e.printStackTrace();
        }

//		 If everything has been initialized then we want to write some data
//		 to the socket we have opened a connection to on port 25
        if (smtpSocket != null && os != null && is != null) {
            try {

                //		 send information to validate username.
                os.writeBytes("USER " + userId + "\n");
                os.writeBytes("PASS " + password + "\n");

                //		 keep on reading from/to the socket till we 've recived the first 
                //three lines, to validate comunication, username and last password.
                //if there is ERR the user is not validated.
                String responseLine;
                while ((resp < 3) && ((responseLine = is.readLine()) != null)) {
                    if (responseLine.indexOf("ERR") != -1) {
                        os.close();
                        is.close();
                        smtpSocket.close();
                        return false;
                    }
                    resp++;
                }
                //		 clean up:
                //		 close the output stream
                //		 close the input stream
                //		 close the socket
                os.close();
                is.close();
                smtpSocket.close();
            } catch (UnknownHostException e) {
                logger.warn("Trying to connect to unknown host: " + e);
                e.printStackTrace();
            } catch (IOException e) {
                logger.warn("IOException:  " + e);
                e.printStackTrace();
            }
            if (resp == 3) {
                return true;
            }
        }

        return false;
    }

    /**
     * Sol·licita un ConnectionBean al ConnectionBeanProvider, si aquest no està
     * innicialitzat, ho fa.
     *
     * @return ConnectionBean, servit pel ConnectionBeanProvider.
     * @throws ThesaurusException
     */
    private ConnectionBean connectBD() throws Exception {
        //Inicialitza el CBP si no ho està.
        ConnectionBean bd = null;
        Hashtable propDoc;
        //String folderProp = "../../../../";
        String folderProp = "/";
        String pathProp = "database.properties";
        try {
            if (broker == null) {
                broker = ConnectionBeanProvider.getConnectionBeanProvider(true, Utility.loadProperties(folderProp, pathProp));
            }
            bd = broker.getConnectionBean();
            if (bd == null) {
                throw new Exception("ConnectionBean is null-> properties=" + Utility.loadProperties(folderProp, pathProp));
            }
        } catch (Exception e) {
            logger.error("Error connecting to take on user information->" + e);
            e.printStackTrace();
            Exception the = new Exception("DataBaseConnectionError");
            the.setStackTrace(e.getStackTrace());
            throw the;
        }//throw new Exception("Error en la connexió amb la base de dades.");}		
        //Retorna un ConnectionBean.
        return bd;
    }

    /**
     * Es desconnecta de la BD.
     */
    private void disconnectBD(ConnectionBean cb) {
        //Allibera el connectionBean utilitzat.
        try {
            broker.freeConnectionBean(cb);
        } catch (Exception e) {
            logger.warn("Error disconnecting from DDBB->" + e);
            e.printStackTrace();
        }//missError ="error.desconnexio.bd";}
    }

    /**
     * Recupera la informació sobre l'usuari.
     *
     * @param userId identificador de l'usuari.
     * @return Objecte usuari amb tota la informació necessaria.
     */
    public User getUser(String username) {
        if (username == null || "".equals(username)) {
            return null;
        }
        String userId = username.toLowerCase();
        User u = new User(userId);
        ConnectionBean cb = null;
        try {
            cb = this.connectBD();
            //Agafa la informació de l'usuari
            PreparedStatement pstmt = cb.getPreparedStatement("SELECT v_mail,id_unitat FROM mer_users WHERE v_user=? ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);;
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) //si l'usuari no existeix al sistema se l'insereix
            {
                PreparedStatement pstmtInsert = cb.getPreparedStatement("insert into mer_users (v_user,v_mail) values (?,?)");
                pstmtInsert.setString(1, userId);
                pstmtInsert.setString(2, userId + "@xtec.cat");
                pstmtInsert.execute();
                pstmtInsert.close();
                cb.closeStatement(pstmtInsert, true);

                rs = pstmt.executeQuery();	//tornem a llençar la query de consulta
            } else {
                rs.beforeFirst();
            }

            while (rs.next()) {
                String sMail = rs.getString("v_mail");
                u.setMail(sMail);
                String sUnitat = rs.getString("id_unitat");
                if (sUnitat != null && !sUnitat.equals("")) {
                    try {
                        u.setUnitat(Integer.valueOf(sUnitat));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            rs.close();
            pstmt.close();

            //Agafa la informació sobre els permisos de l'usuari.
            pstmt = cb.getPreparedStatement("SELECT id_permission, id_rec FROM mer_user_perm WHERE v_username=? ", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            pstmt.setString(1, userId);

            rs = pstmt.executeQuery();
            if (!rs.next()) //si no te ningun permis assignat li assignem el permis per defecte
            {
                pstmt = cb.getPreparedStatement("SELECT v_value as id_permission, 0 as id_rec FROM mer_config WHERE v_key='magics_default_perm'");
                rs = pstmt.executeQuery();
            } else {
                rs.beforeFirst();
            }

            PreparedStatement pstmt2 = cb.getPreparedStatement("SELECT id_operation FROM mer_perm_oper WHERE id_permission=? ");
            while (rs.next()) {
                int perm = Integer.parseInt(rs.getString("id_permission"));
                int rec = Integer.parseInt(rs.getString("id_rec"));
                u.setPermission(perm, rec);
                pstmt2.setInt(1, perm);
                ResultSet rs2 = pstmt2.executeQuery();
                while (rs2.next()) {
                    perm = Integer.parseInt(rs2.getString("id_operation"));
                    u.setPermission(perm, rec);
                }
                rs2.close();
            }
            rs.close();
            cb.closeStatement(pstmt2, true);
            cb.closeStatement(pstmt, true);
        } catch (Exception e) {
            logger.error("Error trying to take on user information ");
            e.printStackTrace();
        } finally {
            disconnectBD(cb);
        }

        return u;
    }

}
