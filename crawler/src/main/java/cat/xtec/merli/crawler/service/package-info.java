/**
 * Merlí web service client.
 */
@XmlSchema(
 namespace = Namespace.MERLI,
 elementFormDefault = XmlNsForm.QUALIFIED,
 attributeFormDefault = XmlNsForm.QUALIFIED,
 xmlns = { @XmlNs(prefix = "mer", namespaceURI = Namespace.MERLI) }
)
package cat.xtec.merli.crawler.service;
import cat.xtec.merli.crawler.service.Namespace;
import javax.xml.bind.annotation.*;
