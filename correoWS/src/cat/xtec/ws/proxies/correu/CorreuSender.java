package cat.xtec.ws.proxies.correu;

import cat.xtec.ws.proxies.correu.types.CorreuAttachment;
import cat.xtec.ws.proxies.correu.types.CorreuBody;
import cat.xtec.ws.proxies.correu.types.CorreuException;
import cat.xtec.ws.proxies.correu.types.CorreuInfo;
import cat.xtec.ws.proxies.correu.types.CorreuResponse;
import cat.xtec.ws.proxies.correu.types.EnviamentResponse;
import cat.xtec.ws.proxies.correu.utils.LogStringWriter;
import cat.xtec.ws.proxies.correu.utils.ServiceLocator;
import java.io.PrintStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.soap.encoding.soapenc.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CorreuSender
{
  public static final String CORREU_XTEC = "correus_aplicacions.educacio@xtec.cat";
  public static final String CORREU_GENCAT = "correus_aplicacions.educacio@gencat.cat";
  public static final String CORREU_EDUCACIO = "apligest@correueducacio.xtec.cat";
  private String idApp = null;
  private String entorn = null;
  private boolean isDebug = false;
  private Document doc = null;
  private CorreuStub caller = null;
  private Transformer identityTransformer = null;
  private LogStringWriter log = new LogStringWriter();
  
  public CorreuSender(String idApp, String entorn)
    throws CorreuException
  {
    this.idApp = idApp;
    this.entorn = entorn;
    this.caller = new CorreuStub();
    try
    {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      this.doc = docBuilder.newDocument();
    }
    catch (ParserConfigurationException e)
    {
      this.log.error("Hi ha problemes de configuració del processador XML. Revisar llibreries");
      e.printStackTrace();
      throw new CorreuException("Error de configuració del processament XML", e);
    }
  }
  
  public CorreuSender(String idApp, String entorn, boolean debugMode)
    throws CorreuException
  {
    this(idApp, entorn);
    this.isDebug = debugMode;
    this.log.info("DebugMode is set to true");
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    try
    {
      this.identityTransformer = transformerFactory.newTransformer();
      this.identityTransformer.setOutputProperty("omit-xml-declaration", "yes");
    }
    catch (TransformerConfigurationException e)
    {
      e.printStackTrace();
      this.log.error("Hi ha problemes de configuració del processador XML. Revisar llibreries");
      throw new CorreuException("Error de configuració del processament XML", e);
    }
    if (this.isDebug)
    {
      this.log.println("[DEBUG] Id Aplicació: " + idApp);
      this.log.println("[DEBUG] Entorn d'execució: " + entorn);
    }
  }
  
  public String getLog()
  {
    return this.log.toString();
  }
  
  public void cleanLog()
  {
    this.log = new LogStringWriter();
  }
  
  public void setDebugMode(boolean debugMode)
  {
    this.isDebug = debugMode;
    this.log.info("DebugMode is set to " + debugMode);
  }
  
  public String consultaDisponibilitat()
  {
    String result = consultaDisponibilitat(null);
    
    return result;
  }
  
  public String consultaDisponibilitat(String from)
  {
    this.log.info("Inici Consulta de disponibilitat del servei " + (from != null ? "amb compte origen : " + from : ""));
    if (this.isDebug) {
      this.log.println("[DEBUG] Es crida al servei amb url: " + ServiceLocator.getInstance().getUrl(this.entorn));
    }
    this.caller.setEndpoint(ServiceLocator.getInstance().getUrl(this.entorn));
    Element peticioDisponibilitat = buildPeticioDisponibilitat(from);
    if (this.isDebug)
    {
      this.log.println("[DEBUG] Petició de disponibilitat:");
      this.log.println(getStringFromElement(peticioDisponibilitat));
      this.log.println("[DEBUG] Fi petició de disponibilitat");
    }
    Vector result = null;
    try
    {
      result = this.caller.disponibilitat(peticioDisponibilitat);
    }
    catch (Exception e)
    {
      this.log.error("S'ha produit un error en la crida a disponibilitat");
      this.log.printStackTrace(e);
    }
    if (result != null)
    {
      Element response = (Element)result.elementAt(0);
      if (this.isDebug)
      {
        this.log.println("[DEBUG] Resposta de disponibilitat:");
        this.log.println(getStringFromElement(response));
        this.log.println("[DEBUG] Fi resposta de disponibilitat");
      }
      if (response.getNodeName().endsWith("RespostaDisponibilitat"))
      {
        Element status = (Element)response.getElementsByTagName("status").item(0);
        String statusValue = status.getFirstChild().getNodeValue();
        if ("OK".equals(statusValue))
        {
          this.log.info("Servei disponible");
          this.log.info("Fi Consulta de disponibilitat del servei");
          

















          return statusValue;
        }
        Element message = (Element)response.getElementsByTagName("message").item(0);
        this.log.error("Error controlat del servei: " + message.getFirstChild().getNodeValue());
        this.log.info("Fi Consulta de disponibilitat del servei");
        











        return message.getFirstChild().getNodeValue();
      }
      this.log.error("S'ha produit algun error en el servei ja que no es troba l'estructura de missatge prevista");
      this.log.info("Fi Consulta de disponibilitat del servei");
      





      return "KO";
    }
    this.log.error("S'ha produit algun error en el servei ja que no es troba l'estructura de missatge prevista");
    this.log.info("Fi Consulta de disponibilitat del servei");
    
    return "KO";
  }
  
  public EnviamentResponse enviaCorreu(String from, String tos, String subject, String bodyContent, int bodyType)
    throws CorreuException
  {
    return enviaCorreu(from, tos, null, null, subject, bodyContent, bodyType);
  }
  
  public EnviamentResponse enviaCorreu(String from, String tos, String ccs, String bccs, String subject, String bodyContent, int bodyType)
    throws CorreuException
  {
    CorreuInfo correu = new CorreuInfo();
    correu.setFrom(from);
    correu.addTo(tos);
    correu.addCc(ccs);
    correu.addBcc(bccs);
    correu.setSubject(subject);
    correu.setBodyInfo(bodyType, bodyContent);
    

    return enviaCorreus(new CorreuInfo[] { correu });
  }
  
  public EnviamentResponse enviaCorreuAmbAdjunt(String from, String tos, String subject, String bodyContent, int bodyType, byte[] attachment, String attachmentName, String attachmentType)
    throws CorreuException
  {
    return enviaCorreuAmbAdjunt(from, tos, null, null, subject, bodyContent, bodyType, attachment, attachmentName, attachmentType);
  }
  
  public EnviamentResponse enviaCorreuAmbAdjunt(String from, String tos, String ccs, String bccs, String subject, String bodyContent, int bodyType, byte[] attachment, String attachmentName, String attachmentType)
    throws CorreuException
  {
    CorreuInfo correu = new CorreuInfo();
    correu.setFrom(from);
    correu.addTo(tos);
    correu.addCc(ccs);
    correu.addBcc(bccs);
    correu.setSubject(subject);
    correu.setBodyInfo(bodyType, bodyContent);
    correu.addAttachment(attachment, attachmentName, attachmentType);
    
    return enviaCorreus(new CorreuInfo[] { correu });
  }
  
  public EnviamentResponse enviaCorreuAmbAdjunts(String from, String tos, String ccs, String bccs, String subject, String bodyContent, int bodyType, CorreuAttachment[] attachments)
    throws CorreuException
  {
    CorreuInfo correu = new CorreuInfo();
    correu.setFrom(from);
    correu.addTo(tos);
    correu.addCc(ccs);
    correu.addBcc(bccs);
    correu.setSubject(subject);
    correu.setBodyInfo(bodyType, bodyContent);
    for (int i = 0; i < attachments.length; i++) {
      correu.getAttachments().add(attachments[i]);
    }
    return enviaCorreus(new CorreuInfo[] { correu });
  }
  
  public EnviamentResponse enviaCorreus(CorreuInfo[] correus)
    throws CorreuException
  {
    this.log.info("Inici Enviament");
    
    EnviamentResponse result = new EnviamentResponse();
    if ((correus == null) || (correus.length == 0))
    {
      this.log.error("No s'ha enviat cap correu");
      this.log.info("Fi Enviament");
      throw new CorreuException("S'ha d'enviar com a mínim un correu");
    }
    if (this.isDebug) {
      this.log.println("[DEBUG] Es crida al servei amb url: " + ServiceLocator.getInstance().getUrl(this.entorn));
    }
    this.caller.setEndpoint(ServiceLocator.getInstance().getUrl(this.entorn));
    Element peticioEnviament = buildPeticioEnviament(correus);
    if (this.isDebug)
    {
      this.log.println("[DEBUG] Petició d'enviament:");
      System.out.println(getStringFromElement(peticioEnviament));
      this.log.println("[DEBUG] Fi petició d'enviament");
    }
    Vector resultCrida = null;
    try
    {
      resultCrida = this.caller.enviament(peticioEnviament);
    }
    catch (Exception e)
    {
      result.setStatus("KO");
      result.setMessage("ERROR: " + e.getMessage());
      e.printStackTrace();
    }
    if (resultCrida != null)
    {
      Element response = (Element)resultCrida.elementAt(0);
      if (this.isDebug)
      {
        this.log.println("[DEBUG] Resposta d'enviament:");
        System.out.println(getStringFromElement(response));
        this.log.println("[DEBUG] Fi resposta d'enviament");
      }
      if (response.getNodeName().endsWith("RespostaEnviament"))
      {
        Element status = (Element)response.getElementsByTagName("status").item(0);
        result.setStatus(status.getFirstChild().getNodeValue());
        NodeList nodes = response.getElementsByTagName("message");
        if (nodes.getLength() > 0)
        {
          Element message = (Element)nodes.item(0);
          result.setMessage(message.getFirstChild().getNodeValue());
        }
        nodes = response.getElementsByTagName("respostaCorreu");
        if (nodes.getLength() > 0) {
          for (int i = 0; i < nodes.getLength(); i++)
          {
            CorreuResponse cResp = buildResponseCorreu((Element)nodes.item(i), correus[i]);
            result.addCorreuResponse(cResp);
          }
        }
        this.log.info("Resultat enviament: " + result.getStatus());
        this.log.info("Fi Enviament");
        

















        return result;
      }
      result.setStatus("KO");
      this.log.info("Format de resposta no previst");
      this.log.info("Fi Enviament");
      










      return result;
    }
    result.setStatus("KO");
    result.setMessage("ERROR: No es té la resposta del servei");
    this.log.error("No es té la resposta del servei");
    this.log.info("Fi Enviament");
    

    return result;
  }
  
  private String getStringFromElement(Element theElement)
  {
    Source domSource = new DOMSource(theElement);
    StringWriter result = new StringWriter();
    StreamResult streamResult = new StreamResult(result);
    try
    {
      this.identityTransformer.transform(domSource, streamResult);
    }
    catch (TransformerException e)
    {
      this.log.println("[DEBUG], Error en la impressió del missatge XML");
      this.log.printStackTrace(e);
    }
    return result.toString();
  }
  
  private Element buildPeticioDisponibilitat(String from)
  {
    Element result = this.doc.createElementNS("http://www.gencat.cat/educacio/sscc/correu", "cor:PeticioDisponibilitat");
    if ((from != null) && (!from.equals("")))
    {
      Element fromNode = this.doc.createElement("from");
      fromNode.appendChild(this.doc.createTextNode(from));
      result.appendChild(fromNode);
    }
    return result;
  }
  
  private Element buildPeticioEnviament(CorreuInfo[] correus)
  {
    Element result = this.doc.createElementNS("http://www.gencat.cat/educacio/sscc/correu", "cor:PeticioEnviament");
    
    Element app = this.doc.createElement("idApp");
    app.appendChild(this.doc.createTextNode(this.idApp));
    result.appendChild(app);
    for (int i = 0; i < correus.length; i++)
    {
      Element correu = buildXmlCorreu(correus[i]);
      result.appendChild(correu);
    }
    return result;
  }
  
  private Element buildXmlCorreu(CorreuInfo correu)
  {
    Element result = this.doc.createElement("correu");
    if ((correu.getFrom() != null) && (!correu.getFrom().equals("")))
    {
      Element fromNode = this.doc.createElement("from");
      fromNode.appendChild(this.doc.createTextNode(correu.getFrom()));
      result.appendChild(fromNode);
    }
    if (correu.getReplyAddresses().size() > 0)
    {
      ArrayList replyAddrs = correu.getReplyAddresses();
      Element replyAddresses = this.doc.createElement("replyAddresses");
      for (int i = 0; i < replyAddrs.size(); i++)
      {
        Element address = this.doc.createElement("address");
        address.appendChild(this.doc.createTextNode((String)replyAddrs.get(i)));
        replyAddresses.appendChild(address);
      }
      result.appendChild(replyAddresses);
    }
    if ((correu.getTos().size() > 0) || (correu.getCcs().size() > 0) || (correu.getBccs().size() > 0))
    {
      Element destinations = this.doc.createElement("destinationAddresses");
      
      ArrayList tos = correu.getTos();
      for (int i = 0; i < tos.size(); i++)
      {
        Element destination = this.doc.createElement("destination");
        Element address = this.doc.createElement("address");
        address.appendChild(this.doc.createTextNode((String)tos.get(i)));
        Element type = this.doc.createElement("type");
        type.appendChild(this.doc.createTextNode("TO"));
        destination.appendChild(address);
        destination.appendChild(type);
        
        destinations.appendChild(destination);
      }
      ArrayList ccs = correu.getCcs();
      for (int i = 0; i < ccs.size(); i++)
      {
        Element destination = this.doc.createElement("destination");
        Element address = this.doc.createElement("address");
        address.appendChild(this.doc.createTextNode((String)ccs.get(i)));
        Element type = this.doc.createElement("type");
        type.appendChild(this.doc.createTextNode("CC"));
        destination.appendChild(address);
        destination.appendChild(type);
        
        destinations.appendChild(destination);
      }
      ArrayList bccs = correu.getBccs();
      for (int i = 0; i < bccs.size(); i++)
      {
        Element destination = this.doc.createElement("destination");
        Element address = this.doc.createElement("address");
        address.appendChild(this.doc.createTextNode((String)bccs.get(i)));
        Element type = this.doc.createElement("type");
        type.appendChild(this.doc.createTextNode("BCC"));
        destination.appendChild(address);
        destination.appendChild(type);
        
        destinations.appendChild(destination);
      }
      result.appendChild(destinations);
    }
    if ((correu.getSubject() != null) && (!correu.getSubject().equals("")))
    {
      Element subject = this.doc.createElement("subject");
      subject.appendChild(this.doc.createCDATASection(correu.getSubject()));
      
      result.appendChild(subject);
    }
    Element mailBody = this.doc.createElement("mailBody");
    Element bodyType = this.doc.createElement("bodyType");
    bodyType.appendChild(this.doc.createCDATASection(correu.getBody().getStringBodyType()));
    mailBody.appendChild(bodyType);
    Element bodyContent = this.doc.createElement("bodyContent");
    bodyContent.appendChild(this.doc.createCDATASection(correu.getBody().getContent()));
    mailBody.appendChild(bodyContent);
    result.appendChild(mailBody);
    if (correu.getAttachments().size() > 0)
    {
      ArrayList attachments = correu.getAttachments();
      
      Element attachs = this.doc.createElement("attachments");
      for (int i = 0; i < attachments.size(); i++)
      {
        CorreuAttachment att = (CorreuAttachment)attachments.get(i);
        Element attachment = this.doc.createElement("attachment");
        Element filename = this.doc.createElement("fileName");
        filename.appendChild(this.doc.createCDATASection(att.getFileName()));
        attachment.appendChild(filename);
        Element attachmentContent = this.doc.createElement("attachmentContent");
        Element fileContent = this.doc.createElement("fileContent");
        fileContent.appendChild(this.doc.createTextNode(Base64.encode(att.getContent())));
        Element mimeType = this.doc.createElement("mimeType");
        mimeType.appendChild(this.doc.createTextNode(att.getFileType()));
        attachmentContent.appendChild(fileContent);
        attachmentContent.appendChild(mimeType);
        attachment.appendChild(attachmentContent);
        attachs.appendChild(attachment);
      }
      result.appendChild(attachs);
    }
    return result;
  }
  
  private CorreuResponse buildResponseCorreu(Element correuResponse, CorreuInfo correu)
  {
    CorreuResponse result = new CorreuResponse();
    result.setCorreu(correu);
    Element status = (Element)correuResponse.getElementsByTagName("status").item(0);
    result.setStatus(status.getFirstChild().getNodeValue());
    NodeList nodes = correuResponse.getElementsByTagName("message");
    if (nodes.getLength() > 0)
    {
      Element message = (Element)nodes.item(0);
      result.setErrorMessage(message.getFirstChild().getNodeValue());
    }
    return result;
  }
}
