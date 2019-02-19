package cat.xtec.merli.domain.type;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.voc.Purpose;
import cat.xtec.merli.xml.*;


/**
 * Classification of a resource on a taxonomy.
 *
 * This class encapsulates a list of entities from a taxonomy with
 * their classification purpose. Nested taxon paths are not supported.
 */
@XmlType(name = "classification")
@XmlAccessorType(XmlAccessType.NONE)
public class Classification implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Purpose of the classification */
    @XmlElement(name = "purpose")
    @XmlJavaTypeAdapter(PurposeAdapter.class)
    protected Purpose purpose;

    /** Root entities on the classification */
    @XmlElement(name = "taxonPath")
    @XmlJavaTypeAdapter(TaxonAdapter.class)
    protected List<Entity> entities;

    /** Empty constructor */
    public Classification() {}


    /**
     * Object constructor.
     *
     * @param purpose   Classification purpose
     */
    public Classification(Purpose purpose) {
        this.purpose = purpose;
    }


    /**
     * Returns this object's purpose value.
     *
     * @return          Purpose value
     */
    public Purpose getPurpose() {
        return purpose;
    }


    /**
     * Sets this object's purpose value.
     *
     * @param value     Purpose value
     */
    public void setPurpose(Purpose value) {
        this.purpose = value;
    }


    /**
     * Returns this object's root entities list reference.
     *
     * @return          Entities list reference
     */
    public List<Entity> getEntities() {
        if (entities == null) {
            entities = new ArrayList<Entity>();
        }

        return entities;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getPurpose() + ": " + getEntities();
    }

}
