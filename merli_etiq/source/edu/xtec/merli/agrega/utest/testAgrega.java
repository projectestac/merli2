package edu.xtec.merli.agrega.utest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.soap.SOAPElement;

import junit.framework.TestCase;
import edu.xtec.merli.MerliBean;
import edu.xtec.merli.agrega.AgregaInterface;
import edu.xtec.merli.agrega.AgregaUtils;
import edu.xtec.merli.agrega.objects.AgregaPiF;
import edu.xtec.merli.agrega.objects.AgregaResource;
import edu.xtec.merli.agrega.objects.AgregaSession;
import edu.xtec.merli.agrega.objects.SQIQuery;
import edu.xtec.merli.agrega.ws.AgregaSQIWS;
import edu.xtec.merli.agrega.ws.AgregaServiceWS;
import edu.xtec.merli.agrega.ws.AgregaSessionsWS;
import edu.xtec.merli.agrega.ws.AgregaWS;
import edu.xtec.merli.basedades.MerliDBException;

public class testAgrega  extends TestCase {

	public static void main(String[] args) {
		junit.swingui.TestRunner.run(testAgrega.class);
	}


	public final void testGetResource() throws MerliDBException{

		int sId = 8936;//3690";
		//MerliBean mb = new MerliBean();
		
		//AgregaInterface.publicarRecurs(mb.getRecurs(sId),null);
		String txt="<?xml version=\"1.0\" encoding=\"UTF-8\"?><manifest xmlns:lomes=\"http://ltsc.ieee.org/xsd/LOM\" xmlns:adlcp=\"http://www.adlnet.org/xsd/adlcp_v1p3\" xmlns:imsss=\"http://www.imsglobal.org/xsd/imsss\" xmlns:adlseq=\"http://www.adlnet.org/xsd/adlseq_v1p3\" xmlns:adlnav=\"http://www.adlnet.org/xsd/adlnav_v1p3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.imsglobal.org/xsd/imscp_v1p1\" xsi:schemaLocation=\"http://www.imsglobal.org/xsd/imscp_v1p1 imscp_v1p1.xsd http://ltsc.ieee.org/xsd/LOM lom.xsd http://www.adlnet.org/xsd/adlcp_v1p3 adlcp_v1p3.xsd http://www.imsglobal.org/xsd/imsss imsss_v1p0.xsd http://www.adlnet.org/xsd/adlseq_v1p3 adlseq_v1p3.xsd http://www.adlnet.org/xsd/adlnav_v1p3 adlnav_v1p3.xsd\" identifier=\"ODE-55baf070-ba85-3a14-ab22-fd201a42847c\"> <metadata> <schema>ADL SCORM</schema><schemaversion>2004 3rd Edition</schemaversion><?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><lom><general><identifier><catalog>Plataforma Agrega</catalog><entry>es-20080808_XTEC_8894</entry></identifier><title><string language=\"ca\">A Testejar pub AGREGA</string><string language=\"es\">A testejar la pub a AGREGA.</string></title><language>ca</language><description><string language=\"ca\">des de l'etiq.</string></description><keyword><string language=\"ca\">joc</string><string language=\"en\">play</string><string language=\"es\">juego</string></keyword><keyword><string language=\"ca\">activitats socioculturals</string><string language=\"en\">socio-cultural activities</string><string language=\"es\">actividades socio-culturales</string></keyword><aggregationLevel><source>LOM-ESv1.0</source><value>2</value></aggregationLevel></general><lifeCycle><status><source>LOM-ESv1.0</source><value>revised</value></status><contribute><role><source>LOM-ESv1.0</source><value>publisher</value></role><entity>BEGIN:VCARD";
		System.out.println(txt);
		txt = txt.replaceAll("xml", "DSL");
		System.out.println(txt);
		txt = txt.replaceAll("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><lom>", "<lom  xmlns=\"http://ltsc.ieee.org/xsd/LOM\">");
		System.out.println(txt);
		
		System.out.println("-------------------------------");
		System.out.println("FI testGetResource");
		System.out.println("###############################");
	}


//	public final void testCreateSession() throws MerliDBException{
//
//		System.out.println("###############################");
//		System.out.println("testCreateSession");
//		System.out.println("-------------------------------");
//		
//		AgregaWS aws = new AgregaWS();
//		AgregaSession agSession = new AgregaSession("acanals5","ac4263");
//
//		SOAPElement se = aws.processOperation(AgregaWS.CREATESESSION, agSession, AgregaWS.LOCAL);
//		
//		AgregaUtils.printSOAPElement(se,"createSessionResponse");
//	}
	


//	public final void testEstasActivo() throws MerliDBException{
//
//		System.out.println("###############################");
//		System.out.println("testEstasActivo");
//		System.out.println("-------------------------------");
//		
//		System.out.println("::"+AgregaInterface.estasActivo());
//		
//		System.out.println("-------------------------------");
//		System.out.println("###############################");
//	}
	
	


//	public final void testSqiQuery() throws MerliDBException{
//
//		System.out.println("###############################");
//		System.out.println("testSqiQuery");
//		System.out.println("-------------------------------");
//
//		AgregaSQIWS awsqi = new AgregaSQIWS();
//		AgregaSessionsWS awsession = new AgregaSessionsWS();
//		
//		AgregaSession as = new AgregaSession("acanals","acanals");
//		as.setSessionID(awsession.createSession(as, AgregaWS.TEST));	
//		
//		System.out.println("SessionID: " + as.getSessionID());
//		
//		SQIQuery sqi = new SQIQuery(as.getSessionID(),"<simpleQuery><term>lengua</term></simpleQuery>",1,"LQS");
//
//		awsqi.setQueryLanguage(sqi, AgregaWS.TEST);
//		SOAPElement se = awsqi.agregaSQIQuery(sqi, AgregaWS.TEST);
//		
//		
//		AgregaUtils.printSOAPElement(se, "SQI QUERY");
//				
//		System.out.println("-------------------------------");
//		
//		se = awsession.destroySession(as, AgregaWS.TEST);		
//		AgregaUtils.printSOAPElement(se, "SQI DESTROY");
//		
//		System.out.println("###############################");
//	}

//	public final void testPresentarAlmacenar() throws MerliDBException{
//
//		System.out.println("###############################");
//		System.out.println("testPresentarAlmacenar");
//		System.out.println("-------------------------------");
//		
//		AgregaServiceWS aws = new AgregaServiceWS();
//		File tempFile = null;
//		try {
////			tempFile = File.createTempFile("temp_agrega_text_", ".temp.zip");
////			FileOutputStream fo = new FileOutputStream(tempFile);
////			fo.write("hola com va aixo?".getBytes());
////			fo.close();
////			
////			System.out.println(tempFile.toString());
//
//			tempFile = new File("C:/projectes/XTEC/WorkspaceXTEC/merli_metiq/web/test-lom.zip");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		AgregaPiF agPif = new AgregaPiF("agrega","agrega",tempFile,"es-ca-xtexMerli-438834");
//
//		SOAPElement se = aws.processOperation(AgregaServiceWS.PRESENTARALMACENAR, agPif, AgregaWS.LOCAL);
//		
//		AgregaUtils.printSOAPElement(se,"presentarAlmacenarResponse");
//
//		System.out.println("-------------------------------");
//		System.out.println("FI testPresentarAlmacenar");
//		System.out.println("###############################");
//	}
	


//	public final void testDestroySession() throws MerliDBException{
//
//		System.out.println("###############################");
//		System.out.println("testDestroySession");
//		System.out.println("-------------------------------");
//		
//		AgregaWS aws = new AgregaWS();
//		AgregaSession agSession = new AgregaSession("acanals","acanals","sessionId349585");
//
//		SOAPElement se = aws.processOperation(AgregaWS.DESTROYSESSION, agSession, AgregaWS.LOCAL);
//		
//		ByteArrayOutputStream byteArrayOS = (ByteArrayOutputStream) AgregaUtils.printSOAPElement(se,"createSessionResponse");
//
//		System.out.println("::::::::::::::::::::::::::::::::");
//		System.out.println(new String(byteArrayOS.toByteArray()));
//		System.out.println("*******************************");
//		
//	}
//	
//	

//	public final void testSolicitarEntregarSesion() throws MerliDBException{
//		int server = AgregaSessionsWS.LOCAL;
//		
//		System.out.println("###############################");
//		System.out.println("testSolicitarEntregarSesion");
//		System.out.println("-------------------------------");
//		
//		AgregaSessionsWS ags = new AgregaSessionsWS();
//		AgregaSession aSession = new AgregaSession("agrega","agrega");
//		ags.createAnonymousSession(aSession, server);
//		String mec = "ODE-742b1072-86e4-3d37-8043-9ae3c273cb66";
//		
//		System.out.println("session:"+aSession.getSessionID());
//		System.out.println("get ODE:"+mec);
//		System.out.println("-------------------------------");
//		
//		AgregaServiceWS asws= new AgregaServiceWS();
//		AgregaPiF aPif = new AgregaPiF("agrega","agrega", null, mec, aSession);
//		asws.solicitarEntregarSesion(aPif, server);
//
//		ags.destroySession(aSession, server);
//	}
}
