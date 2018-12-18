/**
 * Educational ontology domain objects.
 *
 * The classes contained on this package are annotated both for the Java
 * Architecture for XML Binding (JAXB) and the DUC ontology. Thus allowing
 * the conversion of the domain object to the different formats used on the
 * DUC application.
 *
 * Note also that the classes on the domain are self-contained to permit
 * their cross-compilation to JavaScript using the Google Web Toolkit (GWT).
 */
@XmlSchema(
  xmlns = {
    @XmlNs(prefix = "dc", namespaceURI = Namespace.DC),
    @XmlNs(prefix = "duc", namespaceURI = Namespace.DUC),
    @XmlNs(prefix = "lom", namespaceURI = Namespace.LOM)
  },
  elementFormDefault = XmlNsForm.QUALIFIED,
  attributeFormDefault = XmlNsForm.QUALIFIED
)
package cat.xtec.merli.domain;
import javax.xml.bind.annotation.*;
