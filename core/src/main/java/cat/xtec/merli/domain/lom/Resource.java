package cat.xtec.merli.domain.lom;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

import cat.xtec.merli.domain.Namespace;
import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.domain.taxa.Relation;
import cat.xtec.merli.domain.type.Classification;
import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.bind.*;
import cat.xtec.merli.xml.*;


/**
 * Learning object. Encapsulates all the details and classifications of
 * a single learning object.
 */
@DucIndividual()
@XmlType(name = "lom")
@XmlRootElement(name = "lom")
@XmlAccessorType(XmlAccessType.NONE)
public class Resource extends Entity {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** General details titles reference */
    @DucAnnotation(DucVocabulary.LABEL)
    @XmlTransient
    protected List<LangString> titles;

    /** General information */
    @DucBundle()
    @XmlElement(name = "general")
    protected GeneralDetails generalDetails;

    /** History and current state */
    @DucBundle()
    @XmlElement(name = "lifeCycle")
    protected LifeCycleDetails lifeCycleDetails;

    /** Technical characteristics */
    @DucBundle()
    @XmlElement(name = "technical")
    protected TechnicalDetails technicalDetails;

    /** Pedagogical and educational characteristics */
    @DucBundle()
    @XmlElement(name = "educational")
    protected EducationalDetails educationalDetails;

    /** Conditions of use */
    @DucBundle()
    @XmlElement(name = "rights")
    protected RightsDetails rightsDetails;

    /** Description of this metadata */
    @DucBundle()
    @XmlElement(name = "metaMetadata")
    protected MetadataDetails metadataDetails;

    /** Physical characteristics */
    @DucBundle()
    @XmlElement(name = "physical", namespace = Namespace.DUC)
    protected PhysicalDetails physicalDetails;

    /** Categories this object is a subclass-of */
    @DucClass(DucVocabulary.CLASS)
    protected List<Entity> parents;

    /** Relations with other resources */
    @DucRelation(DucVocabulary.LOM_RELATION)
    @XmlElement(name = "relation")
    @XmlJavaTypeAdapter(RelationAdapter.class)
    protected List<Relation> relations;

    /** Classifications for this object */
    @XmlElement(name = "classification")
    protected List<Classification> classes;


    /**
     * Object constructor.
     */
    public Resource() {
        this(null);
    }


    /**
     * Object constructor.
     *
     * @param id    Unique identifier
     */
    public Resource(UID id) {
        this.setUID(id);
        this.setType(EntityType.LEARNING_OBJECT);
        this.titles = super.labels;
    }


    /**
     * Returns this object's general details object.
     *
     * @return          General details object
     */
    public GeneralDetails getGeneralDetails() {
        if (generalDetails == null) {
            generalDetails = new GeneralDetails();
        }

        return generalDetails;
    }


    /**
     * Returns this object's life cycle details object.
     *
     * @return          Life cycle details object
     */
    public LifeCycleDetails getLifeCycleDetails() {
        if (lifeCycleDetails == null) {
            lifeCycleDetails = new LifeCycleDetails();
        }

        return lifeCycleDetails;
    }


    /**
     * Returns this object's technical details object.
     *
     * @return          Technical details object
     */
    public TechnicalDetails getTechnicalDetails() {
        if (technicalDetails == null) {
            technicalDetails = new TechnicalDetails();
        }

        return technicalDetails;
    }


    /**
     * Returns this object's educational details object.
     *
     * @return          Educational details object
     */
    public EducationalDetails getEducationalDetails() {
        if (educationalDetails == null) {
            educationalDetails = new EducationalDetails();
        }

        return educationalDetails;
    }


    /**
     * Returns this object's rights details object.
     *
     * @return          Rights details object
     */
    public RightsDetails getRightsDetails() {
        if (rightsDetails == null) {
            rightsDetails = new RightsDetails();
        }

        return rightsDetails;
    }


    /**
     * Returns this object's metadata details object.
     *
     * @return          Metadata details object
     */
    public MetadataDetails getMetadataDetails() {
        if (metadataDetails == null) {
            metadataDetails = new MetadataDetails();
        }

        return metadataDetails;
    }


    /**
     * Returns this object's physical details object.
     *
     * @return          Metadata details object
     */
    public PhysicalDetails getPhysicalDetails() {
        if (physicalDetails == null) {
            physicalDetails = new PhysicalDetails();
        }

        return physicalDetails;
    }


    /**
     * Returns this object's labels value. This method makes a call
     * to {@see GeneralDetails#getTitles} to get the actual labels.
     *
     * @return          Labels value
     */
    public List<LangString> getLabels() {
        if (labels == null) {
            labels = getGeneralDetails().getTitles();
            titles = super.labels;
        }

        return labels;
    }


    /**
     * Returns this object's relation list reference.
     *
     * @return          Relation list reference
     */
    public List<Relation> getRelations() {
        if (relations == null) {
            relations = new ArrayList<Relation>();
        }

        return relations;
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

        return parents;
    }


    /**
     * Returns this object's classes list reference.
     *
     * @return          Entities list reference
     */
    public List<Classification> getClasses() {
        if (classes == null) {
            classes = new ArrayList<Classification>();
        }

        return classes;
    }

}
