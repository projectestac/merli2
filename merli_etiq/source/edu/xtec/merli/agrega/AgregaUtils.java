package edu.xtec.merli.agrega;

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
import java.net.URL;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.serialize.DOMSerializer;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.Serializer;
import org.w3c.dom.NodeList;

import edu.xtec.merli.RecursMerli;
import edu.xtec.merli.agrega.ws.AgregaWS;

public class AgregaUtils {

	private static final String BASE_PIF_ZIP_PATH = "C:\\projectes\\XTEC\\WorkspaceXTEC\\merli_metiq\\web\\base-lom-xtec.zip";//"\\web\\base-lom-xtec.zip";
	private static final String TEST_PIF_ZIP_PATH = "C:\\projectes\\XTEC\\WorkspaceXTEC\\merli_metiq\\web\\test-lom-xtec.zip";


	public static OutputStream printSOAPElement(SOAPElement se, String titol) {
		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		
		try {
			/*****************element serialized**************/
			byteArrayOS.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
			byteArrayOS.write("<manifest xmlns:lomes=\"http://ltsc.ieee.org/xsd/LOM\"".getBytes());
			byteArrayOS.write(" xmlns:adlcp=\"http://www.adlnet.org/xsd/adlcp_v1p3\"".getBytes());
			byteArrayOS.write(" xmlns:imsss=\"http://www.imsglobal.org/xsd/imsss\"".getBytes());
			byteArrayOS.write(" xmlns:adlseq=\"http://www.adlnet.org/xsd/adlseq_v1p3\"".getBytes());
			byteArrayOS.write(" xmlns:adlnav=\"http://www.adlnet.org/xsd/adlnav_v1p3\"".getBytes());
			byteArrayOS.write(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"".getBytes());
			byteArrayOS.write(" xmlns=\"http://www.imsglobal.org/xsd/imscp_v1p1\"".getBytes());
			byteArrayOS.write(" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imscp_v1p1 imscp_v1p1.xsd http://ltsc.ieee.org/xsd/LOM lom.xsd http://www.adlnet.org/xsd/adlcp_v1p3 adlcp_v1p3.xsd http://www.imsglobal.org/xsd/imsss imsss_v1p0.xsd http://www.adlnet.org/xsd/adlseq_v1p3 adlseq_v1p3.xsd http://www.adlnet.org/xsd/adlnav_v1p3 adlnav_v1p3.xsd\" identifier=\"ODE-55baf070-ba85-3a14-ab22-fd201a42847c\">".getBytes());
			byteArrayOS.write(" <metadata>".getBytes());
			byteArrayOS.write(" <schema>ADL SCORM</schema>".getBytes());
			byteArrayOS.write("<schemaversion>2004 3rd Edition</schemaversion>".getBytes()); 

			printNodeToConsole(se, byteArrayOS);
			
			byteArrayOS.write("</metadata>".getBytes());
			byteArrayOS.write("<organizations default=\"ORG-69912705-ae19-3811-81d5-1975fac5f661\">".getBytes());
			byteArrayOS.write("<organization".getBytes());
			byteArrayOS.write(" identifier=\"ORG-69912705-ae19-3811-81d5-1975fac5f661\" structure=\"hierarchical\">".getBytes());
			byteArrayOS.write("<title>LOM - MeRLí</title>".getBytes());
			byteArrayOS.write("<item identifier=\"ITEM-28fc7184-035d-38d0-acf2-920cde846932\"".getBytes());
			byteArrayOS.write(" identifierref=\"RES-3fa0db3c-0d76-3158-aaf1-1ea419249975\" isvisible=\"true\">".getBytes());
			byteArrayOS.write("<title>Contingut</title>".getBytes());
			byteArrayOS.write("</item>".getBytes());
			byteArrayOS.write("</organization>".getBytes());
			byteArrayOS.write("</organizations>".getBytes());
			byteArrayOS.write("<resources>".getBytes());
			byteArrayOS.write("<resource identifier=\"RES-3fa0db3c-0d76-3158-aaf1-1ea419249975\"".getBytes());
			byteArrayOS.write(" type=\"webcontent\" adlcp:scormType=\"asset\" href=\"http://keith-wood.name/timeEntry.html\"/>".getBytes());
			byteArrayOS.write("</resources>".getBytes());
			byteArrayOS.write("</manifest>".getBytes());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return byteArrayOS;
	} 
 	
	 public static void printNodeToConsole(Node n, ByteArrayOutputStream byteArrayOS) {
	        try {
	            TransformerFactory factory = TransformerFactory.newInstance();
	            Transformer transformer = factory.newTransformer();
	            Properties oform = new Properties();
				oform.setProperty("encoding", "ISO-8859-1");
				
				//oform.setOmitXMLDeclaration(true);
	            transformer.setOutputProperties(oform);
	            // also print to console
	            transformer.transform(new DOMSource(n), new StreamResult(byteArrayOS));
	        } catch (Exception e) {
	            System.out.println(e);
	        }
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

	

		public static OutputStream printIMSManifest(SOAPElement se, RecursMerli rm) {
			ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
			
			try {
				/*****************element serialized**************/
				byteArrayOS.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes());
				byteArrayOS.write("<manifest xmlns:lomes=\"http://ltsc.ieee.org/xsd/LOM\"".getBytes());
				byteArrayOS.write(" xmlns:adlcp=\"http://www.adlnet.org/xsd/adlcp_v1p3\"".getBytes());
				byteArrayOS.write(" xmlns:imsss=\"http://www.imsglobal.org/xsd/imsss\"".getBytes());
				byteArrayOS.write(" xmlns:adlseq=\"http://www.adlnet.org/xsd/adlseq_v1p3\"".getBytes());
				byteArrayOS.write(" xmlns:adlnav=\"http://www.adlnet.org/xsd/adlnav_v1p3\"".getBytes());
				byteArrayOS.write(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"".getBytes());
				byteArrayOS.write(" xmlns=\"http://www.imsglobal.org/xsd/imscp_v1p1\"".getBytes());
				byteArrayOS.write(" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imscp_v1p1 imscp_v1p1.xsd http://ltsc.ieee.org/xsd/LOM lom.xsd http://www.adlnet.org/xsd/adlcp_v1p3 adlcp_v1p3.xsd http://www.imsglobal.org/xsd/imsss imsss_v1p0.xsd http://www.adlnet.org/xsd/adlseq_v1p3 adlseq_v1p3.xsd http://www.adlnet.org/xsd/adlnav_v1p3 adlnav_v1p3.xsd\" identifier=\"ODE-55baf070-ba85-3a14-ab22-fd201a42847c\">".getBytes());
				byteArrayOS.write(" <metadata>".getBytes());
				byteArrayOS.write(" <schema>ADL SCORM</schema>".getBytes());
				byteArrayOS.write("<schemaversion>2004 3rd Edition</schemaversion>".getBytes()); 

				printNodeToConsole(se, byteArrayOS);
				
				byteArrayOS.write("</metadata>".getBytes());
				byteArrayOS.write(("<organizations default=\"ORG-"+String.valueOf(rm.getAgregaId())+"\">").getBytes());
				byteArrayOS.write("<organization".getBytes());
				byteArrayOS.write((" identifier=\"ORG-"+String.valueOf(rm.getAgregaId())+"\" structure=\"hierarchical\">").getBytes());
				byteArrayOS.write("<title></title>".getBytes());
				byteArrayOS.write(("<item identifier=\"ITEM-"+String.valueOf(rm.getAgregaId())+"\"").getBytes());
				byteArrayOS.write((" identifierref=\"RES-"+String.valueOf(rm.getAgregaId())+"\" isvisible=\"true\">").getBytes());
				byteArrayOS.write("<title>Contingut</title>".getBytes());
				byteArrayOS.write("</item>".getBytes());
				byteArrayOS.write("</organization>".getBytes());
				byteArrayOS.write("</organizations>".getBytes());
				byteArrayOS.write("<resources>".getBytes());
				byteArrayOS.write(("<resource identifier=\"RES-"+String.valueOf(rm.getAgregaId())+"\"").getBytes());
				byteArrayOS.write((" type=\"webcontent\" adlcp:scormType=\"asset\" href=\""+rm.getUrl()+"\">").getBytes());
				byteArrayOS.write("<metadata>".getBytes());
				
				printNodeToConsole(se, byteArrayOS);
				
				byteArrayOS.write("</metadata>".getBytes());
				byteArrayOS.write("</resource>".getBytes());
				byteArrayOS.write("</resources>".getBytes());
				byteArrayOS.write("</manifest>".getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			System.out.println("**************************************");
			System.out.println(byteArrayOS);
			System.out.println("**************************************");
			return byteArrayOS;
		} 
	 
	public static File addFilesToExistingZip(File zipFile,
			 File xmlfile) throws IOException {
		// get a temp file
		File tempFile = File.createTempFile("temp_agrega_merli_", ".temp.zip");
		tempFile.deleteOnExit();
		byte[] buf = new byte[1024];


		ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(tempFile));
		
		//Copia la base del fitxer al fitxer temporal.
		ZipEntry entry = zin.getNextEntry();
		while (entry != null) {
			String name = entry.getName();
			boolean notInFiles = true;
			// Add ZIP entry to output stream.
			out.putNextEntry(new ZipEntry(name));
			// Transfer bytes from the ZIP file to the output file
			int len;
			while ((len = zin.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			
			entry = zin.getNextEntry();
		}
		// Close the streams		
		zin.close();
		
		
		// Copia i comprimeix el fitxer XML dins el .ZIP temporal.
		InputStream in = new FileInputStream(xmlfile);
		// Add ZIP entry to output stream.
		out.putNextEntry(new ZipEntry("imsmanifest.xml"));//files[i].getName()));
		// Transfer bytes from the file to the ZIP file
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		// Complete the entry
		out.closeEntry();
		in.close();

		// Complete the ZIP file
		out.close();

		return tempFile;
	}



	public static File addFilesToPifZip(ByteArrayOutputStream byteArrayOS, String path) {
		 try {
				File imsXML = File.createTempFile("temp", ".xml.tmp");
			    imsXML.deleteOnExit();
				
				 // Create file imsmanifest.xml
			    FileWriter fstream = new FileWriter(imsXML);
			   // BufferedWriter out = new BufferedWriter(fstream);
			    
			    FileOutputStream fos = new FileOutputStream(imsXML);
		        OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
			    String replaced = byteArrayOS.toString();
			    //replaced = replaced.replaceAll(oldChar, newChar)
			    replaced = replaced.replaceAll("<lom>", "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\">");
			    
			    int indexml = 5;
			    indexml = replaced.indexOf("<?",indexml);
			    while (indexml>0){
			    	replaced = replaced.substring(0,indexml) + 
			    				replaced.substring(replaced.indexOf(">",indexml)+1, replaced.length());
			    	
			    	indexml = replaced.indexOf("<?",indexml);
			    }
			    
			    out.write(replaced);
			    
			    //Close the output stream
			    out.close();
			    fos.close();
				if (path == null){
					path ="";
				}

	            File zipFile = new File(path+TEST_PIF_ZIP_PATH);
			    return zipFile;
//	            File zipFile = new File(path+BASE_PIF_ZIP_PATH);
//			                
//	            return addFilesToExistingZip(zipFile,imsXML);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
	
	
	public static URL getUrl(int context, int server){
	  	URL url = null;
		try{
            switch (context){
            	case AgregaWS.MERLI: 
	    			switch (server){
	    				case AgregaWS.LOCAL: url = new URL("http://localhost:8090/merli_ws_melt/merli"); break;
	    				case AgregaWS.TEST:	url = new URL("http://integracio.merli.xtec.cat/merli_ws2/merli");break;
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
	    				case AgregaWS.LOCAL: url = new URL("http://agrega-int.educacio.intranet/dri-1/services/SrvSQIService"); break;
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
	
	
	 

 	/**
 	 * Funcionalitat per treure per pantalla el cos dell missatge soap.
 	 * @param msg Missatge a mostrar
 	 * @param titol Capçalera de la impressió
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
}
