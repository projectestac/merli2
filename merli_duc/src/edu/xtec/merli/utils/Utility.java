package edu.xtec.merli.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import edu.xtec.semanticnet.Node;

import org.apache.log4j.Logger;

public class Utility {

    private static final Logger logger = Logger.getRootLogger();//("xtec.duc");

    public static Properties loadProperties(String aPath, String aFile) throws Exception {
        Properties p = new Properties();
        try {
            p.load(Utility.class.getResourceAsStream(aPath + aFile));
            logger.info("PROPERTIES: " + p);
            File f = new File(System.getProperty("user.home"), aFile);
            if (f.exists()) {
                logger.info("FiLE_EXISTS: " + f.exists());
                FileInputStream is = new FileInputStream(f);                
                p.load(is);                
                is.close();
            }
        } catch (FileNotFoundException f) {
            logger.error(f);
        } catch (IOException e) {
            logger.error(e);
        } catch (NullPointerException e) {
            logger.error(e);
        }
        return p;
    }

    /**
     * Funció extreta de:
     * http://forum.java.sun.com/thread.jspa?threadID=484092&messageID=2262423
     * autor: duffymo Helper method that maps a ResultSet into a map of column
     * lists
     *
     * @param query ResultSet
     * @param list of columns names to include in the result map
     * @return map of lists, one per column, with column name as the key
     * @throws SQLException if the connection fails
     */
    public static final Map toMap(ResultSet rs, List wantedColumnNames) throws SQLException {
        Map columns = new LinkedHashMap();

        // Set up the map of columns
        int numWantedColumns = wantedColumnNames.size();
        for (int i = 0; i < numWantedColumns; ++i) {
            List columnValues = new ArrayList();
            columns.put(wantedColumnNames.get(i), columnValues);
        }

        while (rs.next()) {
            for (int i = 0; i < numWantedColumns; ++i) {
                String columnName = (String) wantedColumnNames.get(i);
                Object value = rs.getObject(columnName);
                List columnValues = (List) columns.get(columnName);
                columnValues.add(value);
                columns.put(columnName, columnValues);

            }
        }

        return columns;
    }

    public static String toParaula(String s) {
        return s.replace(']', ' ').replace('[', ' ').trim();
    }

    public static String toParaulaDB(String s) {
        return s.replaceAll("'", "''");
    }

    public static String aplanarText(String in) {
        return filter(in).toUpperCase();
    }

    public static String filter(String input) {
        String result = (input == null ? "" : input);
        if (input != null && input.length() > 0) {
            StringBuffer filtered = new StringBuffer(input.length());
            char c;
            for (int i = 0; i < input.length(); i++) {
                c = input.charAt(i);
                String s = null;
                switch (c) {
                    case ' ':
                        s = "_";
                        break;
                    case '<':
                        s = "";
                        break;
                    case '>':
                        s = "";
                        break;
                    case '"':
                        s = "";
                        break;
                    case '\'':
                        s = "";
                        break;
                    case '´':
                        s = "";
                        break;
                    case '`':
                        s = "";
                        break;
                    case '¨':
                        s = "";
                        break;
                    case '&':
                        s = "";
                        break;
                    case '\\':
                        s = "";
                        break;
                    case '/':
                        s = "";
                        break;
                    case ':':
                        s = "";
                        break;
                    case '*':
                        s = "";
                        break;
                    case '¿':
                        s = "";
                        break;
                    case '?':
                        s = "";
                        break;
                    case '!':
                        s = "";
                        break;
                    case '$':
                        s = "";
                        break;
                    case 'ç':
                        s = "c";
                        break;
                    case 'Ç':
                        s = "C";
                        break;
                    case 'á':
                        s = "a";
                        break;
                    case 'à':
                        s = "a";
                        break;
                    case 'ä':
                        s = "a";
                        break;
                    case 'é':
                        s = "e";
                        break;
                    case 'è':
                        s = "e";
                        break;
                    case 'ë':
                        s = "e";
                        break;
                    case 'í':
                        s = "i";
                        break;
                    case 'ì':
                        s = "i";
                        break;
                    case 'ï':
                        s = "i";
                        break;
                    case 'ó':
                        s = "o";
                        break;
                    case 'ò':
                        s = "o";
                        break;
                    case 'ö':
                        s = "o";
                        break;
                    case 'ú':
                        s = "u";
                        break;
                    case 'ù':
                        s = "u";
                        break;
                    case 'ü':
                        s = "u";
                        break;
                    case 'Á':
                        s = "A";
                        break;
                    case 'À':
                        s = "A";
                        break;
                    case 'Ä':
                        s = "A";
                        break;
                    case 'É':
                        s = "E";
                        break;
                    case 'È':
                        s = "E";
                        break;
                    case 'Ë':
                        s = "E";
                        break;
                    case 'Í':
                        s = "I";
                        break;
                    case 'Ì':
                        s = "I";
                        break;
                    case 'Ï':
                        s = "I";
                        break;
                    case 'Ó':
                        s = "O";
                        break;
                    case 'Ò':
                        s = "O";
                        break;
                    case 'Ö':
                        s = "O";
                        break;
                    case 'Ú':
                        s = "U";
                        break;
                    case 'Ù':
                        s = "U";
                        break;
                    case 'Ü':
                        s = "U";
                        break;
                }
                if (s != null) {
                    filtered.append(s);
                } else {
                    filtered.append(c);
                }
            }
            result = filtered.substring(0);
        }
        return result;
    }

    public static String toListDB(ArrayList camps, String ntaula) {
        // TODO Auto-generated method stub.
        String list = camps.toString().replace('[', ' ').replace(']', ' ');
        list = ntaula + "." + list.trim();
        list = list.replaceAll(", ", ", " + ntaula + ".");

        return list;
    }

    public static ArrayList toList(String list) {
        // TODO Auto-generated method stub
        ArrayList l = new ArrayList();
        String aux;
        aux = list;
        while (aux.indexOf(',') > 0) {
            l.add(aux.substring(0, aux.indexOf(',')).trim());
            aux = aux.substring(aux.indexOf(',') + 1);
        }
        if (aux.trim().compareTo("") != 0) {
            l.add(aux.trim());
        }
        return l;
    }

    public static ArrayList toList(String[] list) {
        // TODO Auto-generated method stub
        ArrayList l = new ArrayList();

        for (int i = 0; i < list.length; i++) {
            l.add(list[i]);
        }

        return l;
    }

    public static ArrayList toList(String list, String sep) {
        // TODO Auto-generated method stub
        ArrayList l = new ArrayList();
        String aux;
        aux = list;
        while (aux.indexOf(sep) > 0) {
            l.add(aux.substring(0, aux.indexOf(sep)).trim());
            aux = aux.substring(aux.indexOf(sep) + 1);
        }
        if (aux.trim().compareTo("") != 0) {
            l.add(aux.trim());
        }
        return l;
    }

    public static ArrayList orderListByInt(ArrayList al, String element) {
			// TODO Auto-generated method stub
        //ArrayList l = new ArrayList();
        ArrayList lord = new ArrayList();
        int posI;
        int posJ;

        boolean trobat = false;
        if (al != null) {
            for (int i = 0; i < al.size(); i++) {
                int j = 0;
                while (!trobat && j < lord.size()) {
                    try {
                        posI = ((Integer) (((Hashtable) al.get(i)).get(element))).intValue();
                        posJ = ((Integer) (((Hashtable) lord.get(j)).get(element))).intValue();
                        if (posI < posJ) {
                            trobat = true;
                        } else {
                            j++;
                        }
                    } catch (Exception e) {
                        j++;
                    }
                }
                trobat = false;
                lord.add(j, al.get(i));
            }
        }
        return lord;
    }

    public static ArrayList orderListByPosition(ArrayList al) {
			// TODO Auto-generated method stub
        //ArrayList l = new ArrayList();
        ArrayList lord = new ArrayList();
        int posI;
        int posJ;

        boolean trobat = false;
        if (al != null) {
            for (int i = 0; i < al.size(); i++) {
                int j = 0;
                while (!trobat && j < lord.size()) {
                    try {
                        posI = ((Integer) ((Hashtable) ((Hashtable) al.get(i)).get("properties")).get("position")).intValue();
                        posJ = ((Integer) ((Hashtable) ((Hashtable) lord.get(j)).get("properties")).get("position")).intValue();
                        if (posI < posJ) {
                            trobat = true;
                        } else {
                            j++;
                        }
                    } catch (Exception e) {
                        j++;
                    }
                }
                trobat = false;
                lord.add(j, al.get(i));
            }
        }
        return lord;
    }

    public static ArrayList orderNodeListByPosition(ArrayList al) {
			// TODO Auto-generated method stub
        //ArrayList l = new ArrayList();
        ArrayList lord = new ArrayList();
        int posI;
        int posJ;

        boolean trobat = false;
        if (al != null) {
            for (int i = 0; i < al.size(); i++) {
                int j = 0;

                while (!trobat && j < lord.size()) {
                    try {

                        posI = Integer.parseInt((String) ((Hashtable) ((Node) al.get(i)).getProperties()).get("position"));
                        posJ = Integer.parseInt((String) ((Hashtable) ((Node) lord.get(j)).getProperties()).get("position"));
                        if (posI < posJ) {
                            trobat = true;
                        } else {
                            j++;
                        }
                    } catch (Exception e) {
                        j++;
                    }
                }
                trobat = false;
                lord.add(j, al.get(i));
            }
        }
        return lord;
    }

    public static String notes2note(Hashtable dtoNotes) {
        // TODO Auto-generated method stub
        String note = "";
        Hashtable hNote;
        ArrayList lnotes = (ArrayList) dtoNotes.get("list");
        Iterator iter = lnotes.iterator();
        while (iter.hasNext()) {
            hNote = (Hashtable) iter.next();
            try {
                note += "<ol>" + hNote.get("user") + "-" + ((Timestamp) hNote.get("date")).toString() + "<br/>";
            } catch (Exception e) {
                System.out.println((hNote.get("date")).getClass());
            }
            note += hNote.get("note") + "</ol>";
        }
        return note;
    }

    public static List orderNodeListByNote(ArrayList al) {
			// TODO Auto-generated method stub
//			 TODO Auto-generated method stub
        //ArrayList l = new ArrayList();
        ArrayList lord = new ArrayList();
        int posI;
        int posJ;

        boolean trobat = false;
        if (al != null) {
            for (int i = 0; i < al.size(); i++) {
                int j = 0;

                while (!trobat && j < lord.size()) {
                    try {
                        posI = Integer.parseInt((String) ((Hashtable) al.get(i)).get("history"));
                        posJ = Integer.parseInt((String) ((Hashtable) lord.get(j)).get("history"));
                        if (posI < posJ) {
                            trobat = true;
                        } else {
                            j++;
                        }
                    } catch (Exception e) {
                        j++;
                    }
                }
                trobat = false;
                lord.add(j, al.get(i));
            }
        }
        return lord;
    }

    public static final String FORBIDDEN_XML_CHARS = "\"'&<>";
    public static final String[] XML_REPLACING_ENTITIES = {"&quot;", "&#39;", "&amp;", "&lt;", "&gt;"};

    public static String xmlEncode(String txt) {
        String result = null;
        if (txt != null) {
            StringBuffer sb = new StringBuffer(txt.length() * 2);
            StringTokenizer st = new StringTokenizer(txt, FORBIDDEN_XML_CHARS, true);
            while (st.hasMoreTokens()) {
                String s = st.nextToken();
                int p = FORBIDDEN_XML_CHARS.indexOf(s.charAt(0));
                if (p >= 0) {
                    sb.append(XML_REPLACING_ENTITIES[p]);
                } else {
                    sb.append(s);
                }
            }
            result = sb.substring(0);
        }
        return result;
    }

    public static String createThesaurusList(ArrayList elems, String operation) {
        // TODO Auto-generated method stub
        Iterator it = elems.iterator();
        Hashtable h;
        String temp, result = "";
        String rt, bt;
        while (it.hasNext()) {
            h = (Hashtable) it.next();
            temp = "<a href=\"#\" onclick=\"swapDisplay('fSormThesaure');" + operation + "Key(" + h.get("idNode") + ");\">";//,'"+h.get("term")+"');\">";
            temp += "+</a>"; //<img src=\""+operation+"Key.png\"/>
            temp += "<a href=\"#\" title=\"" + Utility.xmlEncode((String) h.get("description")) + "\" ";
            temp += " id=\"" + h.get("nodeType") + h.get("idNode") + "\" ";

            temp += " onclick=\"navigateTo(" + h.get("idNode") + ")\">" + Utility.xmlEncode((String) h.get("term"));
            if (h.containsKey("relationType") && !((String) h.get("relationType")).equals("RT")) {
                temp += " (" + Utility.xmlEncode((String) h.get("relationType")) + ")";
            }
            temp += "</a>";
            if (h.containsKey("relationType") && ((String) h.get("relationType")).equals("RT")) {
                result = temp + "<br/>" + result;
            } else {
                result += "<br/>" + temp;
            }
        }

        return result;
    }

    public Timestamp date2Timestamp(Date date) {
        Timestamp ts = new Timestamp(0);

        java.util.Date dateU = (java.util.Date) date;

        ts = new Timestamp(dateU.getTime());

        return ts;
    }

    public static String controlarString(String text, int max) {
        String curt;
        curt = text;
        if (curt != null) {
            if (curt.length() > max) {
                curt = curt.substring(0, max - 6);
                curt += "[...]";
            }
        }
        return curt;
    }

    /**
     *
     * @param text
     * @return String
     * @throws NoSuchAlgorithmException
     */
    public static String getEncoded(String text) throws NoSuchAlgorithmException {
        String output;
        MessageDigest md;
        byte[] textBytes = text.getBytes();

        md = MessageDigest.getInstance("SHA1");
        md.update(textBytes);

        byte[] outputBytes = md.digest();

        // With Base64 encode we prevent from space bar character ending word
        output = new String(Base64Coder.encode(outputBytes));

        return output;
    }
}
