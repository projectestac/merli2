package edu.xtec.merli.agrega;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.zip.ZipOutputStream;

import javax.xml.soap.SOAPElement;

import edu.xtec.merli.RecursMerli;
import edu.xtec.merli.agrega.objects.AgregaPiF;
import edu.xtec.merli.agrega.objects.AgregaResource;
import edu.xtec.merli.agrega.objects.IdResource;
import edu.xtec.merli.agrega.ws.AgregaMerliWS;
import edu.xtec.merli.agrega.ws.AgregaSQIWS;
import edu.xtec.merli.agrega.ws.AgregaServiceWS;
import edu.xtec.merli.agrega.ws.AgregaWS;

public class AgregaInterface {

	private static final int SERVIDOR = AgregaWS.LOCAL;
	

	public static boolean publicarRecurs(RecursMerli rm) {
		return publicarRecurs(rm, null);
	}
	public static boolean publicarRecurs(RecursMerli rm, String path) {
		//Recuperar LOM-es en XML del WS pròpi.
		AgregaMerliWS awm = new AgregaMerliWS();
		IdResource idResource = new IdResource(String.valueOf(rm.getIdRecurs()), "MERLI", true);
		System.out.println("Test Agrega Interface GetResource amb id="+rm.getIdRecurs());
		
		SOAPElement se = awm.getResource(idResource, SERVIDOR);//processOperation(AgregaWS.GETRESOURCE, idResource,SERVIDOR);
		
		ByteArrayOutputStream byteArrayOS = (ByteArrayOutputStream) AgregaUtils.printIMSManifest(se,rm);

		File pif = AgregaUtils.addFilesToPifZip(byteArrayOS, path);
		
		return presentarAlmacenar(pif,String.valueOf(rm.getIdRecurs()));		
	}
	
	
	public static boolean estasActivo(){

		AgregaSQIWS aws = new AgregaSQIWS();
		AgregaResource agResource = new AgregaResource();

		SOAPElement se = aws.estasActivo(agResource, SERVIDOR);
		
		
		if ("true".equals(se.getFirstChild().getNodeValue())) return true;
		else return false;
	}

	
	public static boolean presentarAlmacenar(File file, String idRecurs){
		String usuario="agrega";
		String clave="agrega";
		AgregaServiceWS aws = new AgregaServiceWS();
		AgregaPiF agPif = new AgregaPiF(usuario,clave,file, "es-ca-xtexMerli-"+idRecurs);

		return aws.presentarAlmacenar(agPif, SERVIDOR);		
		
	}
}
