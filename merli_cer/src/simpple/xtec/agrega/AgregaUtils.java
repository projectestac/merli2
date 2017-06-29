package simpple.xtec.agrega;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.NodeList;

import simpple.xtec.agrega.ws.AgregaWS;
import simpple.xtec.web.util.Configuracio;
import simpple.xtec.web.util.DucObject;
import simpple.xtec.web.util.TipusFitxer;
import simpple.xtec.web.util.UtilsCercador;

public class AgregaUtils {
	static final Logger logger = Logger.getLogger(simpple.xtec.agrega.AgregaUtils.class);
	

 	/**
 	 * Funcionalitat per treure per pantalla el cos dell missatge soap.
 	 * @param msg Missatge a mostrar
 	 * @param titol Cap?alera de la impressi?
 	 * @throws SOAPException
 	 * @throws IOException
 	 */
  	public static void printSOAPMessage(SOAPMessage msg, String titol) throws SOAPException, IOException {
			ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
			msg.writeTo(byteArrayOS);
			System.out.println("*******************************");
			System.out.println(titol);
			System.out.println("-------------------------------");
			System.out.println(new String(byteArrayOS.toByteArray()));
			System.out.println("*******************************");
	} 
  	
  	
	private static void printTree(NodeList childNodes,String padding, ByteArrayOutputStream byteArrayOS) {
		 try{ 
				NodeList nodes = childNodes;
				for ( int i = 0; i < nodes.getLength(); i++ )
				{
					Node node = (Node) nodes.item(i);
					String name = node.getNodeName();
					if ( node.hasChildNodes() )
					{
						byteArrayOS.write(('\n'+padding+"<" + name + ">").getBytes());//'\n'+padding + 
						printTree(node.getChildNodes(), padding+" ", byteArrayOS);
						byteArrayOS.write(("</" + name + ">").getBytes());
					}
					else
					{
						byteArrayOS.write((new String((node.getNodeValue()))).getBytes());//(new String((node.getTextContent()).getBytes("ISO-8859-1"), "UTF-8")).toString().getBytes());
					}
				}
			}catch (Exception e){}
	}

	
	 
	 
	public static URL getUrl(int context, int server){
	  	URL url = null;
		try{
            switch (context){
            	case AgregaWS.MERLI: 
	    			switch (server){
	    				case AgregaWS.TEST: url = new URL("http://integracio.merli.xtec.cat/merli_ws2/merli"); break;
	    				case AgregaWS.LOCAL:	url = new URL("http://integracio.merli.xtec.cat/merli_ws2/merli");break;
	    				case AgregaWS.ACCEPT: url = new URL("http://preproduccio.merli.xtec.cat/merli_ws2/merli");break;
	    				case AgregaWS.PRODU: url = new URL("http://merli.xtec.cat/merli_ws2/merli");break;
	    				default: url = new URL("http://merli.xtec.cat/merli_ws2/merli");
	    			}
	    			break;
            	case AgregaWS.AGREGA: 
	    			switch (server){
	    				case AgregaWS.LOCAL: url = new URL("http://agrega-int.educacio.intranet/dri-1/services/SrvDRIService"); break;
	    				case AgregaWS.TEST:	url = new URL("http://ccaa.agrega.indra.es/dri-1/services/SrvDRIService"); break;
	    				case AgregaWS.ACCEPT: 
	    				case AgregaWS.PRODU: url = new URL("http://redes.agrega.indra.es/dri-1/services/SrvDRIService"); break;
	    				default: 	url = new URL("http://contenidos.proyectoagrega.es/dri-1/services/SrvDRIService");			
	    			}
	    			break;
            	case AgregaWS.AGREGA_SQI: 
	    			switch (server){
	    				case AgregaWS.LOCAL: url = new URL("http://agrega-int.educacio.intranet/dri-1/services/SrvDRIService"); break;
	    				case AgregaWS.TEST:	url = new URL("http://ccaa.agrega.indra.es/dri-1/services/SrvSQIService"); break;
	    				case AgregaWS.ACCEPT:
	    				case AgregaWS.PRODU:url = new URL("http://redes.agrega.indra.es/dri-1/services/SrvSQIService"); break;
	    				default: 	url = new URL("http://contenidos.proyectoagrega.es/dri-1/services/SrvSQIService");			
	    			}
	    			break;
            	case AgregaWS.AGREGA_SESSIONS: 
	    			switch (server){
	    				case AgregaWS.LOCAL:url = new URL("http://agrega-int.educacio.intranet/dri-1/services/SrvSesionesService"); break;
	    				case AgregaWS.TEST:	url = new URL("http://ccaa.agrega.indra.es/dri-1/services/SrvSesionesService"); break;
	    				case AgregaWS.ACCEPT:
	    				case AgregaWS.PRODU:url = new URL("http://redes.agrega.indra.es/dri-1/services/SrvSesionesService"); break;
	    				default: 	url = new URL("http://contenidos.proyectoagrega.es/dri-1/services/SrvSesionesService");			
	    			}
	    			break;
    		}
		}catch(Exception e){}
		
		return url;
	}


	public static ArrayList parseResults(String xmlResponse) {
		
		xmlResponse = "<results><lom><title>le primeri</title><description>sisi la descripcio del 1er</description><id>1</id><dataPublicacio>11/01/2010</dataPublicacio></lom><lom><title>le segoon</title><dataPublicacio>22/02/2010</dataPublicacio><description>sisi la descripcio del 2on</description><id>2</id></lom><lom><title>le tecxeri</title><description>sisi la descripcio del 3er</description><dataPublicacio>3/03/2010</dataPublicacio><id>3</id></lom><lom><title>le primeri</title><description>sisi la descripcio del 1er</description><id>1</id><dataPublicacio>11/01/2010</dataPublicacio></lom><lom><title>le segoon</title><dataPublicacio>22/02/2010</dataPublicacio><description>sisi la descripcio del 2on</description><id>2</id></lom><lom><title>le tecxeri</title><description>sisi la descripcio del 3er</description><dataPublicacio>3/03/2010</dataPublicacio><id>3</id></lom><lom><title>le primeri</title><description>sisi la descripcio del 1er</description><id>1</id><dataPublicacio>11/01/2010</dataPublicacio></lom><lom><title>le segoon</title><dataPublicacio>22/02/2010</dataPublicacio><description>sisi la descripcio del 2on</description><id>2</id></lom><lom><title>le tecxeri</title><description>sisi la descripcio del 3er</description><dataPublicacio>3/03/2010</dataPublicacio><id>3</id></lom><lom><title>le primeri</title><description>sisi la descripcio del 1er</description><id>1</id><dataPublicacio>11/01/2010</dataPublicacio></lom><lom><title>le segoon</title><dataPublicacio>22/02/2010</dataPublicacio><description>sisi la descripcio del 2on</description><id>2</id></lom><lom><title>le tecxeri</title><description>sisi la descripcio del 3er</description><dataPublicacio>3/03/2010</dataPublicacio><id>3</id></lom><lom><title>le primeri</title><description>sisi la descripcio del 1er</description><id>1</id><dataPublicacio>11/01/2010</dataPublicacio></lom><lom><title>le segoon</title><dataPublicacio>22/02/2010</dataPublicacio><description>sisi la descripcio del 2on</description><id>2</id></lom><lom><title>le tecxeri</title><description>sisi la descripcio del 3er</description><dataPublicacio>3/03/2010</dataPublicacio><id>3</id></lom></results>";

		ArrayList results = new ArrayList();
		Hashtable hLom;
		SAXBuilder builder = new SAXBuilder();
        try{
        Document doc = builder.build(new StringReader(xmlResponse));
        Element root = doc.getRootElement();
        
        Element mainElement = root;//.getChild("results");
 
        List allChildren = mainElement.getChildren("lom");
        Iterator myIterator = allChildren.iterator();
        Element lom;
        Element element;
        while (myIterator.hasNext()) {
        	hLom = new Hashtable();
            lom = (Element)myIterator.next();
            
            readValue("title","titol", hLom, lom);
            readValue("autor", hLom, lom);
            readValue("description","descripcio", hLom, lom);
            readValue("url", hLom, lom, true, "http://www.xtec.cat");
            readValue("idRecurs", hLom, lom);
            readValue("dataPublicacio", hLom, lom);
            readValue("numComentaris", hLom, lom);
            readValue("numVisites", hLom, lom);
            readValue("puntuacio", hLom, lom);
            readValue("format", hLom, lom);
        
            results.add(hLom);
        }
        	
        }catch(Exception e){
        	
        }
		return results;
	}




	private static void readValue(String name, Hashtable hLom, Element lom) {
		readValue(name,name,hLom,lom);
	}
	private static void readValue(String name, String nameTo, Hashtable hLom, Element lom) {
		readValue(name,nameTo,hLom,lom, false);
	}
	private static void readValue(String name, String nameTo, Hashtable hLom, Element lom, boolean mandatory) {
		readValue(name,nameTo,hLom,lom, false, "");
	}
	private static void readValue(String name,  Hashtable hLom, Element lom, boolean mandatory) {
		readValue(name, name, hLom,lom, false, "");
	}

	private static void readValue(String name, Hashtable hLom, Element lom, boolean mandatory, Object defaultValue) {
		readValue(name, name, hLom,lom, mandatory, defaultValue);
	}
	private static void readValue(String name, String nameTo, Hashtable hLom, Element lom, boolean mandatory, Object defaultValue) {
		Element element;
		element = lom.getChild(name);
		if (element != null){
			hLom.put(nameTo, element.getText());
			logger.debug(nameTo+"("+name+"): " + element.getText());
		}else{
			if (mandatory){
				hLom.put(nameTo, defaultValue);
			}
		}
		
	}




	public static Hashtable parseDUC(String[] ducContent) {
		// TODO Auto-generated method stub
		Hashtable results  = new Hashtable();
		Connection myConnection = null;
		try {		
			myConnection = UtilsCercador.getConnectionFromPool();
			
			results = UtilsCercador.parseContent2Arco (myConnection, ducContent);
			
		} catch (Exception e) {
			logger.error(e);
		} finally {
			try {
				myConnection.close();  
			} catch (Exception e) {
				logger.error(e);	
			}
		}  
		   
		return results;
	}


	
}
