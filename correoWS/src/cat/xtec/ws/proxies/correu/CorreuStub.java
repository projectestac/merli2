package cat.xtec.ws.proxies.correu;

import java.net.URL;
import java.util.Vector;
import oracle.soap.transport.http.OracleSOAPHTTPConnection;
import org.apache.soap.Body;
import org.apache.soap.Envelope;
import org.apache.soap.encoding.SOAPMappingRegistry;
import org.apache.soap.messaging.Message;
import org.w3c.dom.Element;

public class CorreuStub {

    private OracleSOAPHTTPConnection m_httpConnection = null;
    private SOAPMappingRegistry m_smr = null;
    private String _endpoint = "http://xtec-int.educacio.intranet:8080/enviaCorreu/CorreuHttpPort";

    public CorreuStub() {
        this.m_httpConnection = new OracleSOAPHTTPConnection();
        this.m_smr = new SOAPMappingRegistry();
    }

    public String getEndpoint() {
        return this._endpoint;
    }

    public void setEndpoint(String endpoint) {
        this._endpoint = endpoint;
    }

    public Vector disponibilitat(Element requestElem)
            throws Exception {
        URL endpointURL = new URL(this._endpoint);

        Envelope requestEnv = new Envelope();

        requestEnv.declareNamespace("cor", "http://www.gencat.cat/educacio/sscc/correu");
        Body requestBody = new Body();
        Vector requestBodyEntries = new Vector();

        requestBodyEntries.addElement(requestElem);
        requestBody.setBodyEntries(requestBodyEntries);
        requestEnv.setBody(requestBody);

        Message msg = new Message();
        msg.setSOAPTransport(this.m_httpConnection);
        msg.send(endpointURL, "disponibilitat", requestEnv);

        Envelope responseEnv = msg.receiveEnvelope();
        Body responseBody = responseEnv.getBody();

        return responseBody.getBodyEntries();
    }

    public Vector enviament(Element requestElem)
            throws Exception {
        URL endpointURL = new URL(this._endpoint);

        Envelope requestEnv = new Envelope();

        requestEnv.declareNamespace("cor", "http://www.gencat.cat/educacio/sscc/correu");

        Body requestBody = new Body();
        Vector requestBodyEntries = new Vector();

        requestBodyEntries.addElement(requestElem);
        requestBody.setBodyEntries(requestBodyEntries);
        requestEnv.setBody(requestBody);

        Message msg = new Message();
        msg.setSOAPTransport(this.m_httpConnection);
        msg.send(endpointURL, "enviament", requestEnv);

        Envelope responseEnv = msg.receiveEnvelope();
        Body responseBody = responseEnv.getBody();

        return responseBody.getBodyEntries();
    }
}
