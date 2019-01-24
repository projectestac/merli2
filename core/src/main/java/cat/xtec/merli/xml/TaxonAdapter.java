package cat.xtec.merli.xml;

import javax.xml.bind.annotation.adapters.*;
import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.xml.domain.TaxonPath;


/**
 * Adapter to transform an entity into and from a taxon path.
 *
 * This JAXB adapter marshals an {@code Entity} instance as a taxon
 * path in a classification.
 *
 * For example, for an entity reference:
 *
 * &lt;taxonPath&gt;
 *   &lt;taxon&gt;
 *     &lt;id&gt;1234&lt;/id&gt;
 *     &lt;entry&gt;
 *       &lt;string language="ca"&gt;Àfrica&lt;/string&gt;
 *       &lt;string language="es"&gt;África&lt;/string&gt;
 *     &lt;/entry&gt;
 *   &lt;/taxon&gt;
 * &lt;/taxonPath&gt;
 */
public class TaxonAdapter extends XmlAdapter<TaxonPath, Entity> {

    /**
     * {@inheritDoc}
     */
    @Override
    public TaxonPath marshal(Entity value) throws Exception {
        return (value == null) ? null : new TaxonPath(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Entity unmarshal(TaxonPath value) throws Exception {
        return (value == null) ? null : value.getEntity();
    }

}
