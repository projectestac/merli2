package cat.xtec.merli.domain.taxa;

import java.io.Serializable;
import javax.xml.bind.annotation.*;
import cat.xtec.merli.domain.Namespace;
import cat.xtec.merli.bind.*;


/**
 * A relationship between entities.
 */
@XmlType(name = "relation")
@XmlAccessorType(XmlAccessType.NONE)
public class Relation implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Target of this relationship */
    @DucTarget()
    @XmlElement(name = "target")
    protected Entity target;

    /** Type of the relationship */
    @DucPredicate()
    @XmlElement(name = "type", namespace = Namespace.DC)
    protected RelationType type;


    /**
     * Creates a new empty relation.
     */
    public Relation() {
        this(RelationType.ENTITY, null);
    }


    /**
     * Creates a new relation.
     *
     * @param target    Target entity
     */
    public Relation(Entity target) {
        this(RelationType.ENTITY, target);
    }


    /**
     * Creates a new relation.
     *
     * @param type      Relation type
     * @param target    Target entity
     */
    public Relation(RelationType type, Entity target) {
        setType(type);
        setTarget(target);
    }


    /**
     * Returns this object's target entity reference value.
     *
     * @return          Target entity reference value
     */
    public Entity getTarget() {
        return target;
    }


    /**
     * Sets this object's target entity reference value.
     *
     * @param value     Target entity reference value
     */
    public void setTarget(Entity value) {
        this.target = value;
    }


    /**
     * Returns this object's relation type value.
     *
     * @return          Entity relation type value
     */
    public RelationType getType() {
        return type;
    }


    /**
     * Sets this object's relation type value.
     *
     * @param value     Entity relation type value
     */
    public void setType(RelationType value) {
        this.type = (value == null) ?
            RelationType.ENTITY : value;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() +
               "(" + type + ", " + target + ")";
    }

}
