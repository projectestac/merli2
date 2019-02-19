package cat.xtec.merli.xml.domain;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.Relation;
import cat.xtec.merli.domain.taxa.RelationType;
import cat.xtec.merli.xml.*;


/**
 * A LOM relationship entry.
 *
 * This type is not intended to be used directly, but through the
 * relationship adapter {@code RelationAdapter}.
 */
@XmlType(name = "relationship")
@XmlAccessorType(XmlAccessType.NONE)
public class Relationship {

    /** Relation value */
    @XmlTransient
    protected Relation relation;

    /** Empty constructor */
    public Relationship() {}


    /**
     * Object constructor.
     *
     * @param relation       Relation value
     */
    public Relationship(Relation relation) {
        this.relation = relation;
    }


    /**
     * Returns this object's relation type value.
     *
     * @return              Relation type
     */
    @XmlElement(name = "kind")
    @XmlJavaTypeAdapter(RelationTypeAdapter.class)
    public RelationType getType() {
        return (relation == null) ?
            null : relation.getType();
    }


    /**
     * Returns this object's relation value.
     *
     * @return              Relation value
     */
    @XmlElement(name = "resource")
    @XmlJavaTypeAdapter(TargetAdapter.class)
    public Entity getTarget() {
        return (relation == null) ?
            null : relation.getTarget();
    }


    /**
     * Returns this object's relation value.
     *
     * @return              Relation value
     */
    public Relation getRelation() {
        return relation;
    }


    /**
     * Sets this object's relation type value.
     *
     * @param value         Relation type value
     */
    public void setType(RelationType value) {
        if (relation == null) {
            relation = new Relation();
        }

        relation.setType(value);
    }


    /**
     * Sets this object's relation target value.
     *
     * @param value         Target entity
     */
    public void setTarget(Entity value) {
        if (relation == null) {
            relation = new Relation();
        }

        relation.setTarget(value);
    }


    /**
     * Sets this object's relation value.
     *
     * @param value         Relation value
     */
    public void setRelation(Relation value) {
        this.relation = value;
    }

}
