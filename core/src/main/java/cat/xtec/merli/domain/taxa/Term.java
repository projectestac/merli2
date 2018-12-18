package cat.xtec.merli.domain.taxa;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.domain.Namespace;
import cat.xtec.merli.domain.taxa.Relation;
import cat.xtec.merli.domain.UID;
import cat.xtec.merli.bind.DucVocabulary;
import cat.xtec.merli.bind.*;


/**
 * A vocabulary item.
 */
@DucClass()
@XmlType(name = "term")
@XmlRootElement(name = "term")
@XmlAccessorType(XmlAccessType.NONE)
public class Term extends Entity {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Meaning of the term and its usage */
    @DucAnnotation(DucVocabulary.COMMENT)
    @XmlElement(name = "description", namespace = Namespace.DC)
    protected String description;

    /** Terms this object is a subclass-of */
    @DucClass(DucVocabulary.CLASS)
    @XmlElement(name = "class")
    protected List<Entity> parents;

    /** Relationships originating from this term */
    @DucRelation(DucVocabulary.TERM_RELATION)
    @XmlElement(name = "relation")
    protected List<Relation> relations;


    /**
     * Object constructor.
     */
    public Term() {
        this(null);
    }


    /**
     * Object constructor.
     *
     * @param id    Unique identifier
     */
    public Term(UID id) {
        this.setUID(id);
        this.setType(EntityType.VOCABULARY_TERM);
    }


    /**
     * Returns this object's description value.
     *
     * @return          Description value
     */
    public String getDescription() {
        return description;
    }


    /**
     * Sets this object's description value.
     *
     * @param value     Description value
     */
    public void setDescription(String value) {
        this.description = value;
    }


    /**
     * Returns this object's parents list reference.
     *
     * @return          Entities list reference
     */
    public List<Entity> getParents() {
        if (parents == null) {
            parents = new ArrayList<Entity>();
        }

        return this.parents;
    }


    /**
     * Returns this object's relationships list reference.
     *
     * @return          Term relations list reference
     */
    public List<Relation> getRelations() {
        if (relations == null) {
            relations = new ArrayList<Relation>();
        }

        return this.relations;
    }

}
