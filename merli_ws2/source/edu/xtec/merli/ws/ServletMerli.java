package edu.xtec.merli.ws;

import edu.xtec.merli.basedades.MerliDBException;
import edu.xtec.merli.utils.Utility;
import edu.xtec.merli.ws.objects.APXTEC32;
import edu.xtec.merli.ws.objects.Contribute;
import edu.xtec.merli.ws.objects.IdResource;
import edu.xtec.merli.ws.objects.Identifier;
import edu.xtec.merli.ws.objects.Lom;
import java.util.Iterator;
import java.util.Properties;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.w3c.dom.Node;

/**
 *
 * @author acanals5
 * @version 1.0.0
 */
public class ServletMerli extends ServletMain {

    public static final String GETRESOURCE = "getResource";
    public static final String DELRESOURCE = "delResource";
    public static final String UNPUBLISHRESOURCE = "unpublishResource";
    public static final String SETRESOURCE = "setResource";
    public static final String ADDRESOURCE = "addResource";
    public static final String EDITRESOURCE = "editResource";

    /**
     * Processa el SOAPMessage, identifica de quin típus és l'objecte enviat i
     * segons el típus executa la operació adequada. Retorna el missatge
     * retornat per la operació executada.
     */
    public SOAPMessage processMessage(SOAPMessage smRequest) {
        SOAPMessage smResponse = null;
        String sQuery = null;
        try {

            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();

            SOAPBodyElement sbeRequest = null;

            Iterator it = sbRequest.getChildElements();

            while (it.hasNext()) {
                try {
                    sbeRequest = (SOAPBodyElement) it.next();
                } catch (Exception e) {
                    logger.error(e);
                }
            }

            sQuery = sbeRequest.getElementName().getLocalName();//agafa la operacio a realitzar

            String[] j = smRequest.getMimeHeaders().getHeader("host");
            printLog(sQuery + "-INIT", j[0], "WSMerli");

            //AddResource
            if (ADDRESOURCE.equals(sQuery)) {
                smResponse = addResource((SOAPBodyElement) sbeRequest.getChildElements(sf.createName("lom")).next(), "ok");
            }

            //SetResource
            if (SETRESOURCE.equals(sQuery)) {
                smResponse = setResource((SOAPBodyElement) sbeRequest.getChildElements(sf.createName("lom")).next(), "ok");
            }

            //DelResource
            if (DELRESOURCE.equals(sQuery)) {
                smResponse = delResource((SOAPBodyElement) sbeRequest.getChildElements(sf.createName("idResource")).next(), "ok");
            }

            //unpublishResource
            if (UNPUBLISHRESOURCE.equals(sQuery)) {
                smResponse = unpublishResource((SOAPBodyElement) sbeRequest.getChildElements(sf.createName("idResource")).next());
            }

            //GetResource
            if (GETRESOURCE.equals(sQuery)) {
                smResponse = getResource((SOAPBodyElement) sbeRequest.getChildElements(sf.createName("idResource")).next());//,el);
            }

            //EditResource
            if (EDITRESOURCE.equals(sQuery)) {
                smResponse = editResource((SOAPBodyElement) sbeRequest.getChildElements(sf.createName("lom")).next(), "ok");//,el);
            }

            printLog(sQuery + "-END", "Server", "WSMerli");
        } catch (SOAPException se) {
            return internalError(se, sQuery);
        } catch (MerliDBException ioe) {
            return internalError(ioe, sQuery);
        } catch (Exception ioe) {
            return internalError(ioe, sQuery);
        }

        return smResponse;
    }

    /**
     * Processa el SOAPMessage, identifica de quin típus és l'objecte enviat i
     * segons el típus executa la operació adequada. Retorna el missatge
     * retornat per la operació executada.
     */
    public SOAPMessage processMessage(SOAPMessage smRequest, String ip) {
        SOAPMessage smResponse = null;
        String sQuery = null;
        try {

            SOAPBody sbRequest = smRequest.getSOAPPart().getEnvelope().getBody();

            SOAPBodyElement sbeRequest = null;

            Iterator it = sbRequest.getChildElements();

            while (it.hasNext()) {
                try {
                    sbeRequest = (SOAPBodyElement) it.next();
                } catch (Exception e) {
                    logger.error(e);
                }
            }

            sQuery = sbeRequest.getElementName().getLocalName();//agafa la operacio a realitzar

            String[] j = smRequest.getMimeHeaders().getHeader("host");
            printLog(sQuery + "-INIT", j[0], "WSMerli");

            //AddResource
            if (ADDRESOURCE.equals(sQuery)) {
                smResponse = addResource(getChildByLocalName(sbeRequest, "lom"), ip);
            }

            //SetResource
            if (SETRESOURCE.equals(sQuery)) {
                smResponse = setResource(getChildByLocalName(sbeRequest, "lom"), ip);
            }

            //DelResource
            if (DELRESOURCE.equals(sQuery)) {
                smResponse = delResource(getChildByLocalName(sbeRequest, "idResource"), ip);
            }

            //unpublishResource
            if (UNPUBLISHRESOURCE.equals(sQuery)) {
                smResponse = unpublishResource(getChildByLocalName(sbeRequest, "idResource"));
            }

            //GetResource
            if (GETRESOURCE.equals(sQuery)) {
                smResponse = getResource(getChildByLocalNameGetResource(sbeRequest, "idResource"));// ,el);
            }

            //EditResource
            if (EDITRESOURCE.equals(sQuery)) {
                smResponse = editResource(getChildByLocalName(sbeRequest, "lom"), ip);// ,el);
            }

            printLog(sQuery + "-END", "Server", "WSMerli");
        } catch (SOAPException se) {
            return internalError(se, sQuery);
        } catch (MerliDBException ioe) {
            return internalError(ioe, sQuery);
        } catch (Exception ioe) {
            return internalError(ioe, sQuery);
        }

        return smResponse;
    }
/* NADIM ADDED GET RESOURCE*/
    private SOAPElement getChildByLocalNameGetResource(SOAPBodyElement sbeRequest, String localName) {
        Node element = null;
        //SOAPBodyElement element = null;
        Iterator it = sbeRequest.getChildElements();
        while (it.hasNext()) {
            try {
                element = (SOAPElement) it.next();
                if (element.getLocalName() != null) {
                    if (element.getLocalName().equals(localName)) {
                        return (SOAPElement) element;
                    }
                }
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
        return null;
    }
/* END NADIM ADDED GET RESOURCE*/    
    private SOAPBodyElement getChildByLocalName(SOAPBodyElement sbeRequest, String localName) {
        Node element = null;
        Iterator it = sbeRequest.getChildElements();
        while (it.hasNext()) {
            try {
                element = (Node) it.next();
                if (element.getLocalName() != null) {
                    if (element.getLocalName().equals(localName)) {
                        return (SOAPBodyElement) element;
                    }
                }
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
        return null;
    }

    /**
     * Rep el SOAPBodyElement enviat pel client i el converteix a l'objecte
     * adequat dins el domini. En aquest cas un Lom
     *
     * Executa la operació setResource passant com a parametre el Lom.
     *
     * Retorna un Result indicant la operació i que l'execució ha estat correcte
     * i amb un objecte IdResource amb l'id del recurs modificat.
     *
     * @param sbeRequest SOAPBodyElement amb un Lom
     * @return
     * @throws MerliDBException
     * @throws SOAPException
     */
    private SOAPMessage setResource(SOAPBodyElement sbeRequest, String ip) throws MerliDBException, SOAPException {
        WSMerliBD wsmbd = new WSMerliBD();
        Lom lom;

        if (sbeRequest != null) {
            lom = new Lom(sbeRequest);
        } else {
            throw new MerliDBException(MerliDBException.PARAM_INCORRECTE);
        }

        SOAPMessage smResponse = null;
        if (comprovarIp(ip)) {
            if (esIdentificadorValid(lom.getGeneral().getIdentifier())) {
                if (wsmbd.existResource(lom.getGeneral().getIdentifier().getIdEntry())) {
                    IdResource idResource;
                    printLog("setResource-" + lom.getGeneral().getIdentifier().getIdEntry(), "SERVER", "wsMerli");

                    if (comprovarCampsObligatoris(lom)) {
                        idResource = wsmbd.setRecurs(lom);	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!							
                    } else {
                        throw new SOAPException("Camps obligatoris buits");
                    }

                    smResponse = createResponse(SETRESOURCE, idResource);
                    //		smResponse= internalError(new MerliDBException(MerliDBException.OBJECTE_INEXISTENT),SETRESOURCE);
                } else {
                    logger.info("[SetResource] El recurs " + lom.getGeneral().getIdentifier().getIdEntry() + " no existeix");
                    throw new MerliDBException(MerliDBException.OBJECTE_INEXISTENT);
                }
            } else {
                logger.info("[SetResource] El recurs " + lom.getGeneral().getIdentifier().getIdEntry() + " no existeix");
                throw new MerliDBException(MerliDBException.CAMPS_OBLIGATORIS);
            }
        } else {
            logger.info("[SetResource] Adreça no permesa -->" + ip);
            throw new SOAPException("Permís denegat");
        }
        return smResponse;
    }

    private boolean esIdentificadorValid(Identifier i) {
        return i != null && i.getIdEntry() != null && !i.getIdEntry().equals("");
    }

    /*
     * retorna cert si els seguents camps del lom venen plens:
     * titol
     * descripcio (resum)
     * url
     * autor
     * catalogador (nomes es comprova en cas que l'estat sigui diferent de 'Pendent' (10) o l'estat no estigui especificat)
     * publicador (nomes es comprova en cas que l'estat sigui 'Publicat' (4))
     */
    private boolean comprovarCampsObligatoris(Lom lom) {
        boolean bgeneral = true;
        boolean bautor = false;
        boolean bcatalogador = false;
        boolean bpublicador = true;
        try {
            if (lom.getGeneral().getTitle() == null || lom.getGeneral().getTitle().getListString().size() <= 0
                    || lom.getGeneral().getDescription() == null || lom.getGeneral().getDescription().getListString().size() <= 0
                    || lom.getTechnical().getLocation() == null || "".equals(lom.getTechnical().getLocation().trim())) {
                bgeneral = false;
            }

            if (lom.getLifeCycle().getStatus() != null) {
                String estat = lom.getLifeCycle().getStatus().getValue();
                if (estat.equals(APXTEC32.STX_PENDENT)) {
                    bcatalogador = true;
                }
                if (estat.equals(APXTEC32.STX_ACCEPTAT)) {
                    bpublicador = false;
                }
            }

            for (int i = 0; i < lom.getLifeCycle().getContributeList().size(); i++) {
                Contribute c = (Contribute) lom.getLifeCycle().getContributeList().get(i);
                if (APXTEC32.ROL_AUTHOR.equals(c.getRole().getValue()) && c.getEntity().getUsername().trim().length() > 0) {
                    bautor = true;
                }
            }
            for (int i = 0; i < lom.getMetaMetaData().getContributeList().size(); i++) {
                Contribute c = (Contribute) lom.getMetaMetaData().getContributeList().get(i);
                if (!bcatalogador && APXTEC32.ROL_CREATOR.equals(c.getRole().getValue()) && c.getEntity().getUsername().trim().length() > 0) {
                    bcatalogador = true;
                }
                if (!bpublicador && APXTEC32.ROL_VALIDATOR.equals(c.getRole().getValue()) && c.getEntity().getUsername().trim().length() > 0) {
                    bpublicador = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bgeneral && bautor && bcatalogador && bpublicador;
    }

    /**
     * Rep el SOAPBodyElement enviat pel client i el converteix a l'objecte
     * adequat dins el domini. En aquest cas un IdResource.
     *
     * Executa la operació delResource passant com a parametre l'IdResource.
     *
     * Retorna un Result indicant la operació i que l'execució ha estat
     * correcte.
     *
     * @param sbeRequest SOAPBodyElement amb un IdResource
     * @return
     * @throws SOAPException
     * @throws MerliDBException
     */
    public SOAPMessage delResource(SOAPBodyElement sbeRequest, String ip) throws SOAPException, MerliDBException {

        WSMerliBD wsmbd = new WSMerliBD();

        SOAPMessage smResponse = null;

        if (comprovarIp(ip)) {
            IdResource idResource;
            if (sbeRequest != null) {
                idResource = new IdResource(sbeRequest);
            } else {
                throw new MerliDBException(MerliDBException.PARAM_INCORRECTE);
            }
            printLog("delResource-" + idResource.getIdentifier(), "SERVER", "wsMerli");
            wsmbd.delRecurs(idResource);

            smResponse = createResponse(DELRESOURCE, idResource);
            //		smResponse= internalError(new MerliDBException(MerliDBException.OBJECTE_INEXISTENT),DELRESOURCE);
        } else {
            logger.info("[DelResource] Adreça no permesa -->" + ip);
            throw new SOAPException("Permís denegat");
        }

        return smResponse;
    }

    /**
     * Rep el SOAPBodyElement enviat pel client i el converteix a l'objecte
     * adequat dins el domini. En aquest cas un IdResource.
     *
     * Executa la operació unpublishRecurs passant com a parametre l'IdResource.
     *
     * Retorna un Result indicant la operació i que l'execució ha estat
     * correcte.
     *
     * @param sbeRequest SOAPBodyElement amb un IdResource
     * @return
     * @throws SOAPException
     * @throws MerliDBException
     */
    public SOAPMessage unpublishResource(SOAPBodyElement sbeRequest) throws SOAPException, MerliDBException {

        WSMerliBD wsmbd = new WSMerliBD();

        SOAPMessage smResponse = null;

        IdResource idResource = new IdResource(sbeRequest);
        printLog("unpublishRecurs-" + idResource.getIdentifier(), "SERVER", "wsMerli");
        wsmbd.unpublishRecurs(idResource);

        smResponse = createResponse(UNPUBLISHRESOURCE, idResource);
//		smResponse= internalError(new MerliDBException(MerliDBException.OBJECTE_INEXISTENT),UNPUBLISHRESOURCE);

        return smResponse;
    }

    /**
     * Rep el SOAPBodyElement enviat pel client i el converteix a l'objecte
     * adequat dins el domini. En aquest cas un Lom
     *
     * Executa la operació addResource passant com a parametre el Lom.
     *
     * Retorna un Result indicant la operació i que l'execució ha estat correcte
     * i amb un objecte IdResource amb l'id del recurs creat.
     *
     * @param sbeRequest SOAPBodyElement amb un Lom
     * @return
     * @throws SOAPException
     * @throws MerliDBException
     */
    public SOAPMessage addResource(SOAPBodyElement sbeRequest, String ip) throws SOAPException, MerliDBException {

        WSMerliBD wsmbd = new WSMerliBD();
        wsmbd.setLomes(false);
        Lom lom;

        if (sbeRequest != null) {
            lom = new Lom(sbeRequest);
        } else {
            throw new MerliDBException(MerliDBException.PARAM_INCORRECTE);
        }

        SOAPMessage smResponse = null;

        IdResource idResource = new IdResource();

        if (comprovarIp(ip)) {
            if (comprovarCampsObligatoris(lom)) {
                idResource = wsmbd.addRecurs(lom);	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!							
            } else {
                throw new SOAPException("Camps obligatoris buits");
            }
            smResponse = createResponse(ADDRESOURCE, idResource);
            printLog("addResource-creat:" + idResource.getIdentifier(), "SERVER", "wsMerli");
            //smResponse= internalError(new MerliDBException(MerliDBException.OBJECTE_INEXISTENT),ADDRESOURCE);
        } else {
            logger.info("[AddResource] Adreça no permesa -->" + ip);
            throw new SOAPException("Permís denegat");
        }

        return smResponse;
    }

    private boolean comprovarIp(String ip) throws MerliDBException {
        WSMerliBD wsmbd = new WSMerliBD();
        boolean b = true;
        if (!ip.equals("ok")) {
            b = wsmbd.comprovaIp(ip);
        }
        return b;
    }

    /**
     * Rep el SOAPBodyElement enviat pel client i el converteix a l'objecte
     * adequat dins el domini. En aquest cas un IdResource.
     *
     * Executa la operació getResource passant com a parametre l'IdResource. Se
     * li afageix un atribut "lomEs", si te valor "true" es retorna el recurs en
     * format LOM-ESv1.0
     *
     * <getResource>
     * <idResource lomEs="true">
     * <identifier>8934</identifier>
     * <type>MERLI</type>
     * </idResource>
     * </getResource>
     * Retorna un Result indicant la operació i que l'execució ha estat correcte
     * i amb l'objecte sol·licitat en un Lom.
     *
     * @param node SOAPBodyElement amb un IdResource
     * @return
     * @throws SOAPException
     * @throws MerliDBException
     */
    public SOAPMessage getResource(SOAPElement sbRequest/*,SOAPBodyElement sbLomes*/) throws SOAPException, MerliDBException {

        WSMerliBD wsmbd = new WSMerliBD();
        String lomes = "";
        IdResource idResource;

        if (sbRequest != null) {
            idResource = new IdResource(sbRequest);
        } else {
            throw new MerliDBException(MerliDBException.PARAM_INCORRECTE);
        }

        //Es passa el valor de l'atribut lomEs. Per defecte és "false".
        wsmbd.setLomes(idResource.isLomEs());
        SOAPMessage smResponse = null;
        Lom lom = null;

        if (idResource.getType().equals("url")) {
            idResource = wsmbd.getIdResourceByUrl(idResource);
        }

        lom = wsmbd.getResource(idResource);

        smResponse = createResponse(GETRESOURCE, lom);

        return smResponse;
    }

    public SOAPMessage editResource(SOAPBodyElement sbeRequest, String ip) throws SOAPException, MerliDBException {
        SOAPMessage smResponse = null;
        IdResource url = new IdResource();
        Properties p;
        Lom lom;
        String baseurl, context, sUrl;

        if (comprovarIp(ip)) {
            if (sbeRequest != null) {
                lom = new Lom(sbeRequest);
            } else {
                throw new MerliDBException(MerliDBException.PARAM_INCORRECTE);
            }

            try {
                p = Utility.loadProperties("/", "ws.properties");
                baseurl = p.getProperty("baseurl");
                context = p.getProperty("context");
                sUrl = baseurl + "/" + context + "/etiquetar.do?operation=editarWS2&idRec=0&" + lom.toUrl();
            } catch (Exception e) {
                throw new SOAPException("Error al accedir al fitxer de propietats");
            }
            url.setIdentifier(sUrl);
            url.setType("url");

            smResponse = createResponse(EDITRESOURCE, url);
            printLog("editResource-url creada: " + sUrl + "\n", "SERVER", "wsMerli");
        } else {
            //smResponse= internalError(new MerliDBException(MerliDBException.ADRECA_NO_PERMESA),EDITRESOURCE);
            logger.info("[EditResource] Adreça no permesa -->" + ip);
            throw new SOAPException("Permís denegat");
        }
        return smResponse;
    }

}
