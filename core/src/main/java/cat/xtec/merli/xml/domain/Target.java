package cat.xtec.merli.xml.domain;

import java.util.List;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.type.LangString;


/**
 * A LOM relationship target entity.
 *
 * This type is not intended to be used directly, but through the
 * relationship adapter {@code RelationAdapter}.
 */
@XmlType(name = "target")
@XmlAccessorType(XmlAccessType.NONE)
public class Target {

    /** This target entity */
    @XmlTransient
    protected Entity entity;

    /** Empty constructor */
    public Target() {}


    /**
     * Object constructor.
     *
     * @param entity        Entity value
     */
    public Target(Entity entity) {
        this.entity = entity;
    }


    /**
     * Returns this object's ID value.
     *
     * @return          ID value
     */
    @XmlElement(name = "identifier")
    public UID getUID() {
        return (entity == null) ?
            null : entity.getUID();
    }


    /**
     * Returns this object's labels value.
     *
     * @return          Labels value
     */
    @XmlElement(name = "string")
    @XmlElementWrapper(name = "description")
    public List<LangString> getLabels() {
        return (entity == null) ?
            null : entity.getLabels();
    }


    /**
     * Returns this object's entity value.
     *
     * @return          Entity value
     */
    public Entity getEntity() {
        return entity;
    }


    /**
     * Sets this object's entity value.
     *
     * @param value     Entity value
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }


    /**
     * Sets this object's ID value.
     *
     * @param value     ID value
     */
    public void setUID(UID id) {
        if (entity == null) {
            entity = new Entity();
        }

        entity.setUID(id);
    }

}
