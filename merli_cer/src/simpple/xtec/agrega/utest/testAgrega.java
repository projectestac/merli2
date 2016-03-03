//package simpple.xtec.agrega.utest;
//
//
//
//import javax.xml.soap.SOAPElement;
//
//import junit.framework.TestCase;
//import simpple.xtec.agrega.AgregaInterface;
//import simpple.xtec.agrega.AgregaUtils;
//import simpple.xtec.agrega.objects.AgregaSession;
//import simpple.xtec.agrega.objects.SQIQuery;
//import simpple.xtec.agrega.ws.AgregaSQIWS;
//import simpple.xtec.agrega.ws.AgregaSessionsWS;
//import simpple.xtec.agrega.ws.AgregaWS;
//import simpple.xtec.web.util.UtilsCercador;
//
//public class testAgrega  extends TestCase {
//
//	public static void main(String[] args) {
//		junit.swingui.TestRunner.run(testAgrega.class);
//	}
//
//
////	public final void testGetResource() throws MerliDBException{
////
////		int sId = 8936;//3690";
////		AgregaInterface.publicarRecurs(sId);
////	}
//
//
////	public final void testCreateSession() throws MerliDBException{
////
////		System.out.println("###############################");
////		System.out.println("testCreateSession");
////		System.out.println("-------------------------------");
////		
////		AgregaWS aws = new AgregaWS();
////		AgregaSession agSession = new AgregaSession("acanals5","ac4263");
////
////		SOAPElement se = aws.processOperation(AgregaWS.CREATESESSION, agSession, AgregaWS.LOCAL);
////		
////		AgregaUtils.printSOAPElement(se,"createSessionResponse");
////	}
//	
//
//
//	public final void testEstasActivo() throws Exception{
//
//		System.out.println("###############################");
//		System.out.println("testEstasActivo");
//		System.out.println("-------------------------------");
//		
//		//System.out.println("::"+AgregaInterface.estasActivo());
//		System.out.println("-------------------------------");
//		UtilsCercador.parseDurationString("45");
//		UtilsCercador.parseDurationString("145");
//		UtilsCercador.parseDurationString("1450");
//		UtilsCercador.parseDurationString("46665");
//		UtilsCercador.parseDurationString("866650");
//		
//		System.out.println("-------------------------------");
//		System.out.println("###############################");
//	}
//	
//	
//
//
//	public final void testSqiQuery() throws Exception{
//
//		System.out.println("###############################");
//		System.out.println("testSqiQuery");
//		System.out.println("-------------------------------");
//
//		AgregaUtils.parseResults("");
//		
//		AgregaSQIWS awsqi = new AgregaSQIWS();
//		AgregaSessionsWS awsession = new AgregaSessionsWS();
//		
//		AgregaSession as = new AgregaSession("acanals","acanals");
//		
//		as.setSessionID(awsession.createSession(as, AgregaWS.TEST));
//		
//		System.out.println("SessionID: " + as.getSessionID());
//		
//		SQIQuery sqi = new SQIQuery(as.getSessionID(),"lengua",1,"VSQI");
//
//		awsqi.setQueryLanguage(sqi, AgregaWS.TEST);
//		String result = awsqi.agregaSQIQuery(sqi, AgregaWS.TEST);
//		
//		
//
//		System.out.println(result);
//		System.out.println("-------------------------------");
//		
//		SOAPElement se = awsession.destroySession(as, AgregaWS.TEST);		
//		
//		System.out.println("###############################");
//	}
//
////	public final void testPresentarAlmacenar() throws MerliDBException{
////
////		System.out.println("###############################");
////		System.out.println("testPresentarAlmacenar");
////		System.out.println("-------------------------------");
////		
////		AgregaServiceWS aws = new AgregaServiceWS();
////		File tempFile = null;
////		try {
//////			tempFile = File.createTempFile("temp_agrega_text_", ".temp.zip");
//////			FileOutputStream fo = new FileOutputStream(tempFile);
//////			fo.write("hola com va aixo?".getBytes());
//////			fo.close();
//////			
//////			System.out.println(tempFile.toString());
////
////			tempFile = new File("c:/projectes/XTEC/temp/lomes2.zip");
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
////
////		AgregaPiF agPif = new AgregaPiF("acanals33","acanals",tempFile,"es-ca-xtexMerli-438834");
////
////		SOAPElement se = aws.processOperation(AgregaServiceWS.PRESENTARALMACENAR, agPif, AgregaWS.TEST);
////		
////		AgregaUtils.printSOAPElement(se,"presentarAlmacenarResponse");
////	}
//	
//
//
////	public final void testDestroySession() throws MerliDBException{
////
////		System.out.println("###############################");
////		System.out.println("testDestroySession");
////		System.out.println("-------------------------------");
////		
////		AgregaWS aws = new AgregaWS();
////		AgregaSession agSession = new AgregaSession("acanals","acanals","sessionId349585");
////
////		SOAPElement se = aws.processOperation(AgregaWS.DESTROYSESSION, agSession, AgregaWS.LOCAL);
////		
////		ByteArrayOutputStream byteArrayOS = (ByteArrayOutputStream) AgregaUtils.printSOAPElement(se,"createSessionResponse");
////
////		System.out.println("::::::::::::::::::::::::::::::::");
////		System.out.println(new String(byteArrayOS.toByteArray()));
////		System.out.println("*******************************");
////		
////	}
////	
////	
//}
