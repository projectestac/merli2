package cat.xtec.merli.xml;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

import cat.xtec.merli.domain.taxa.Relation;
import cat.xtec.merli.domain.type.Relationship;


/**
 * Adapter to transform a relation into a LOM relationship.
 *
 * This JAXB adapter marshals a {@code Relation} instance as a
 * relationship to a resource.
 *
 * For example, for a relation reference:
 *
 * &lt;relation&gt;
 *   &lt;kind&gt;
 *     &lt;source&gt;LOMv1.0&lt;/source&gt;
 *     &lt;value&gt;ispartof&lt;/value&gt;
 *   &lt;/kind&gt;
 *   &lt;resource&gt;
 *     &lt;identifier&gt;1234&lt;/identifier&gt;
 *     &lt;description&gt;
 *       &lt;string language="ca"&gt;Àfrica&lt;/string&gt;
 *       &lt;string language="es"&gt;África&lt;/string&gt;
 *     &lt;/description&gt;
 *   &lt;/resource&gt;
 * &lt;/relation&gt;
 */
public class RelationAdapter extends XmlAdapter<Relationship, Relation> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Relationship marshal(Relation value) throws Exception {
        return (value == null) ? null : new Relationship(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Relation unmarshal(Relationship value) throws Exception {
        return (value == null) ? null : value.getRelation();
    }

}
