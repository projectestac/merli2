package cat.xtec.merli.domain.taxa;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.domain.Namespace;
import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.voc.Knowledge;
import cat.xtec.merli.bind.*;


/**
 * An educational content category.
 */
@XmlType(name = "category")
@XmlRootElement(name = "category")
@XmlAccessorType(XmlAccessType.NONE)
@DucEntity(DucVocabulary.CLASS)
public class Category extends Entity {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Meaning and usage of the category */
    @DucAnnotation(DucVocabulary.COMMENT)
    @XmlElement(name = "description", namespace = Namespace.DC)
    protected String description;

    /** Observations */
    @DucAnnotation(DucVocabulary.OBSERVATION)
    @XmlElement(name = "observation")
    protected String observation;

    /** References */
    @DucAnnotation(DucVocabulary.REFERENCE)
    @XmlElement(name = "reference")
    protected String reference;

    /** Type of curricular knowledge  */
    @DucAttribute(DucVocabulary.KNOWLEDGE_TYPE)
    @XmlElement(name = "knowledge")
    protected Knowledge knowledge;

    /** Categories this object is a subclass-of */
    @DucRelation(DucVocabulary.PARENT)
    @XmlElement(name = "class")
    protected List<Entity> parents;

    /** Keywords that describe this node */
    @DucRelation(DucVocabulary.KEYWORD)
    @XmlElement(name = "keyword")
    protected List<Entity> keywords;


    /**
     * Object constructor.
     */
    public Category() {
        this(UID.valueOf(null));
    }


    /**
     * Object constructor.
     *
     * @param id    Unique identifier
     */
    public Category(UID id) {
        this.setId(id);
        this.setType(EntityType.CONTENT_CATEGORY);
        this.setKnowledge(Knowledge.UNSPECIFIED);
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
     * Sets this object's description string value.
     *
     * @param value     Description string value
     */
    public void setDescription(String value) {
        this.description = value;
    }


    /**
     * Returns this object's observations value.
     *
     * @return          Observations string value
     */
    public String getObservation() {
        return observation;
    }


    /**
     * Sets this object's observations value.
     *
     * @param value     Observations value
     */
    public void setObservation(String value) {
        this.observation = value;
    }


    /**
     * Returns this object's references value.
     *
     * @return          References value
     */
    public String getReference() {
        return reference;
    }


    /**
     * Sets this object's references string value.
     *
     * @param value     References string value
     */
    public void setReference(String value) {
        this.reference = value;
    }


    /**
     * Returns this object's knowledge type value.
     *
     * @return          Knowledge type value
     */
    public Knowledge getKnowledge() {
        return knowledge;
    }


    /**
     * Sets this object's knowledge type object.
     *
     * @param value     Knowledge type object
     */
    public void setKnowledge(Knowledge value) {
        this.knowledge = value;
    }


    /**
     * Returns this object's entities list reference.
     *
     * @return          Entities list reference
     */
    public List<Entity> getKeywords() {
        if (keywords == null) {
            keywords = new ArrayList<Entity>();
        }

        return this.keywords;
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

}
