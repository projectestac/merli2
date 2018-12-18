package cat.xtec.merli.crawler.service;

import java.net.URI;
import java.net.URL;
import java.io.InputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.w3c.dom.Element;
import javax.xml.soap.*;

import cat.xtec.merli.Application;
import cat.xtec.merli.domain.lom.Resource;


/**
 * Client for the Merli 2.1 web service.
 */
public class MerliClient implements AutoCloseable {

    /** Endpoint to the service */
    private final URL ENDPOINT_URL;

    /** SOAP message factory */
    protected MessageFactory factory;

    /** Serializes objects to XML */
    protected Marshaller marshaller;

    /** XSLT transformer */
    protected Transformer transformer;

    /** SOAP connection intance */
    protected SOAPConnection connection;


    /**
     * Create a new client instance.
     *
     * @param endpoint      Service endpoint URI
     * @throws SerializerException  If the serializer cannot be created
     */
    public MerliClient(URI endpoint) throws Exception {
        ENDPOINT_URL = endpoint.toURL();
        marshaller = createMarshaller();
        transformer = createTransformer();
        connection = createConnection();
        factory = MessageFactory.newInstance();
    }


    /**
     * Creates a new resource on the remote server.
     *
     * @param resource          Resource to create
     * @throws SOAPException    If the post failed
     */
    public void post(Resource resource) throws Exception {
        SOAPMessage message = factory.createMessage();

        // Serializes the resource into a new result

        Element element = createElement(message, Operation.CREATE);
        DOMResult request = new DOMResult(element);

        marshaller.marshal(resource, request);

        // Transforms the serialized result and attaches the transformed
        // tree into the SOAP body. This transform removes the namespaces
        // from the serialized XML, so it can be understood by the service

        SOAPBody body = message.getSOAPBody();
        DOMResult result = new DOMResult(body);
        DOMSource source = new DOMSource(element);

        transformer.transform(source, result);

        // Sends the message to the server

        connection.call(message, ENDPOINT_URL);
    }


    /**
     * {@inheritDoc}
     */
    public void close() {
        try {
            connection.close();
        } catch (SOAPException e) {}
    }


    /**
     * Creates a new DOM element for the given message and operation.
     *
     * @param message           SOAP message
     * @param operation         Service opertation
     */
    protected Element createElement(SOAPMessage message, Operation operation) {
        return message.getSOAPPart().createElement(operation.value());
    }


    /**
     * Creates a new marshaller that can serialize LOM resources.
     *
     * @return              New marshaller instance
     */
    private final static Marshaller createMarshaller() throws Exception {
        JAXBContext context = JAXBContext.newInstance(Resource.class);
        Marshaller marshaller = context.createMarshaller();

        return marshaller;
    }


    /**
     * Creates a new SOAP connection instance.
     *
     * @return              New connection instance
     */
    private final static SOAPConnection createConnection() throws Exception {
        return SOAPConnectionFactory.newInstance().createConnection();
    }


    /**
     * Creates a new XSLT transformer to remove namespaces from an
     * XML tree. Note that this transformation is currently required
     * for compatibility with the Merli 2.1 SOAP messages which only
     * accepts LOM XML's without a defined namespace.
     *
     * @return              New transformer instance
     */
    private final static Transformer createTransformer() throws Exception {
        final String xslt = "/transform/remove-ns.xslt";
        final InputStream input = Application.getResource(xslt);
        final StreamSource source = new StreamSource(input);

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer(source);

        return transformer;
    }

}
