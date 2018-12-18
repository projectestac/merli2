/**
 * Learning Object Metadata resource. This is a simplified representation
 * of the LOMv1.0 standard (IEEE Std 1484.12.1-2002).
 */
@XmlSchema(
 namespace = Namespace.LOM,
 elementFormDefault = XmlNsForm.QUALIFIED,
 attributeFormDefault = XmlNsForm.QUALIFIED
)
package cat.xtec.merli.domain.lom;
import cat.xtec.merli.domain.Namespace;
import javax.xml.bind.annotation.*;
