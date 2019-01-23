package cat.xtec.merli.xml;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.type.Target;


/**
 * Adapter to transform an entity into a relation resource.
 *
 * This JAXB adapter marshals an {@code Entity} instance as a
 * resource in a LOM relationship.
 *
 * For example, for an entity reference:
 *
 * &lt;resource&gt;
 *   &lt;identifier&gt;1234&lt;/identifier&gt;
 *   &lt;description&gt;
 *     &lt;string language="ca"&gt;Àfrica&lt;/string&gt;
 *     &lt;string language="es"&gt;África&lt;/string&gt;
 *   &lt;/description&gt;
 * &lt;/resource&gt;
 */
public class TargetAdapter extends XmlAdapter<Target, Entity> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Target marshal(Entity value) throws Exception {
        return (value == null) ? null : new Target(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Entity unmarshal(Target value) throws Exception {
        return (value == null) ? null : value.getEntity();
    }

}
