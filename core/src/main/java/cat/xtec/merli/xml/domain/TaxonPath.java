package cat.xtec.merli.xml.domain;

import javax.xml.bind.annotation.*;
import cat.xtec.merli.domain.taxa.Entity;


/**
 * A taxonomic path in a specific classification.
 *
 * This type is not intended to be used directly, but through the
 * classification adapter {@code TaxonAdapter}. Note also that nested
 * taxon paths are not supported.
 */
@XmlType(name = "taxonPath")
@XmlAccessorType(XmlAccessType.NONE)
public class TaxonPath {

    /** Taxonomic entry */
    @XmlElement(name = "taxon", required = true)
    protected Taxon taxon;

    /** Empty constructor */
    public TaxonPath() {}


    /**
     * Object constructor.
     *
     * @param entity        Root entity value
     */
    public TaxonPath(Entity entity) {
        this.taxon = new Taxon(entity);
    }


    /**
     * Returns this object's root entity value.
     *
     * @return          Taxon entity or {@code null}
     */
    @XmlTransient
    public Entity getEntity() {
        return (taxon == null) ? null : taxon.getEntity();
    }


    /**
     * Returns this object's taxon value.
     *
     * @return          Taxon value
     */
    public Taxon getTaxon() {
        return taxon;
    }


    /**
     * Sets this object's taxon value.
     *
     * @param value     Taxon value
     */
    public void setTaxon(Taxon taxon) {
        this.taxon = taxon;
    }

}
