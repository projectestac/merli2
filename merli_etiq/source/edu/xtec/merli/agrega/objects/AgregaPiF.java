	package edu.xtec.merli.agrega.objects;

	import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Iterator;

import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import edu.xtec.merli.agrega.ws.AgregaWS;



	public class AgregaPiF extends ObjectMerli{

		private String usuario;
		private String clave;
		private String identificador;
		private File file;

		private AgregaSession agregaSession;
		
		public AgregaPiF() {
			super();

			this.usuario = "";
			this.clave = "";
		}

		public AgregaPiF(String usuario, String clave, String identificador) {
			super();

			this.usuario = usuario;
			this.clave = clave;
			this.identificador = identificador;
		}

		public AgregaPiF(String usuario, String clave, File file, String identificador) {
			super();

			this.usuario = usuario;
			this.clave = clave;
			this.file = file;
			this.identificador = identificador;
		}
		public AgregaPiF(String usuario, String clave, File file, String identificador, AgregaSession agregaSession) {
			super();

			this.usuario = usuario;
			this.clave = clave;
			this.file = file;
			this.identificador = identificador;
			this.agregaSession = agregaSession;
		}



		public AgregaPiF(SOAPElement sePiF) throws SOAPException {
			
			Iterator it = sePiF.getChildElements(soapFactory.createName("usuario"));
			usuario = ((SOAPElement) it.next()).getValue();
			
			it = sePiF.getChildElements(soapFactory.createName("clave"));
			clave = ((SOAPElement) it.next()).getValue();
			
		}


		public String getClave() {
			if (clave == null)
				if (agregaSession!= null)
					agregaSession.getPassword();
				else
					clave = "";
			return clave;
		}

		public void setClave(String clave) {
			this.clave = clave;
		}

		public String getFileString(){
			String fileString="";
			try {
				char[] cbuf = null;
				FileReader fr = new FileReader(file);
				fr.read(cbuf);
				fileString = new String(cbuf);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return fileString;
		}
		
		public File getFile() {
			return file;
		}

		public void setFile(File file) {
			this.file = file;
		}

		public String getIdentificador() {
			return identificador;
		}

		public void setIdentificador(String identificador) {
			this.identificador = identificador;
		}

		public String getUsuario() {
			if (usuario==null && agregaSession!=null)
				return agregaSession.getUserID();
			return usuario;
		}

		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}

		public AgregaSession getAgregaSession() {
			return agregaSession;
		}

		public void setAgregaSession(AgregaSession agregaSession) {
			this.agregaSession = agregaSession;
		}

		public SOAPElement toXml() throws SOAPException {
			return toXml("publicarAlmacenar");
		}
		
		public SOAPElement toXml(String operacion) throws SOAPException {
			// TODO Auto-generated method stub
			SOAPElement seOperacion=null;
			SOAPElement seUsuario;
			SOAPElement seClave;
			SOAPElement sePif;
		        
			//Crea l'element de la operacio
			seOperacion = soapFactory.createElement(soapFactory.createName(operacion));
			
			//Crea l'element usuario
			seUsuario = soapFactory.createElement(soapFactory.createName("usuario"));
			seUsuario.addTextNode(this.getUsuario());
			//Afageix l'element usuario a la operacio
			seOperacion.addChildElement(seUsuario);

			//Crea l'element clave
			seClave = soapFactory.createElement(soapFactory.createName("clave"));
			seClave.addTextNode(this.getClave());
			//Afageix l'element clave a la operacio
			seOperacion.addChildElement(seClave);

//			if (file != null){
//				sePif = soapFactory.createElement(soapFactory.createName("pif"));
//				sePif.addTextNode(getFileString());
//				//Afageix l'element type al IdResult
//				seOperacion.addChildElement(seClave);
//			}
			
			return seOperacion;
		}


		public SOAPElement toUsuarioXml() throws SOAPException {
			SOAPElement seUsuario;
			
			//Crea l'element usuario
			seUsuario = soapFactory.createElement(soapFactory.createName("usuario"));
			seUsuario.addTextNode(this.getUsuario());

			return seUsuario;
		}


		public SOAPElement toClaveXml() throws SOAPException {

			SOAPElement seClave; 
			
			//Crea l'element clave
			seClave = soapFactory.createElement(soapFactory.createName("clave"));
			seClave.addTextNode(this.getClave());
			
			return seClave;
		}
		

		public SOAPElement getMecXml() throws SOAPException {

			SOAPElement seClave; 
			
			//Crea l'element clave
			if (identificador == null) identificador = "";
			seClave = soapFactory.createElement(soapFactory.createName("mec"));
			seClave.addTextNode(identificador);
			
			return seClave;
		}
		
		public String toString(){
			return "IdResource[id="+getUsuario()+",type="+getClave()+"]";
		}
		

		public SOAPElement getResponse(SOAPMessage sm, String response) throws SOAPException {
			parseResponse(sm);
			Iterator it;
			SOAPElement se;

			switch(this.getOperation()){
			case AgregaWS.ESTASACTIVO:
				it = sm.getSOAPBody().getChildElements(soapFactory.createName("estasActivoResponse"));
				if (it!= null && it.hasNext()){
					se = (SOAPElement) it.next();
					return se;
				}
				return null;
			case AgregaWS.PRESENTARALMACENAR:
				return (SOAPElement) ((SOAPElement)sm.getSOAPBody().getChildElements(soapFactory.createName(response)).next()).getFirstChild();
			default: 
				return ((Node)((SOAPElement) sm.getSOAPBody().getChildElements(soapFactory.createName(response)).next()).getFirstChild().getFirstChild()).getParentElement();
			}
			}
	}
