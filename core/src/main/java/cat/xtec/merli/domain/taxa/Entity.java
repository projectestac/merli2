package cat.xtec.merli.domain.taxa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.Serializable;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.bind.DucVocabulary;
import cat.xtec.merli.domain.Namespace;
import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.domain.voc.Language;
import cat.xtec.merli.bind.*;


/**
 * Represents an entity pertaining to an OWL ontology.
 *
 * This class is a minimal representation of a domain entity intended
 * to be used only to reference an object in the domain. It should be
 * used to represent the target of a relation.
 *
 * An ontology entity is represented by a unique IRI identifier.
 * Its name may be a human readable label for the entity or any other
 * textual representation.
 */
@DucClass()
@XmlType(name = "entity")
@XmlRootElement(name = "entity")
@XmlAccessorType(XmlAccessType.NONE)
public class Entity implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Global unique identifier */
    @DucIRI()
    @XmlElement(name = "id", namespace = Namespace.DUC)
    protected UID id;

    /** Entity type */
    @DucAnnotation(DucVocabulary.TYPE)
    @XmlElement(name = "type", namespace = Namespace.DC)
    protected EntityType type;

    /** Multilingual labels */
    @DucAnnotation(DucVocabulary.LABEL)
    @XmlElement(name = "title", namespace = Namespace.DC)
    protected List<LangString> labels;


    /**
     * Object constructor.
     */
    public Entity() {
        this(null);
    }


    /**
     * Object constructor.
     *
     * @param id    Unique identifier
     */
    public Entity(UID id) {
        this.setUID(id);
    }


    /**
     * Returns the unique identifier of the entity.
     *
     * @return          Id value
     */
    public UID getUID() {
        return id;
    }


    /**
     * Sets the unique identifier for the entity.
     *
     * @param value     IRI identifier
     */
    public void setUID(UID value) {
        this.id = value;
    }


    /**
     * Returns this object's type value.
     *
     * @return          Type value
     */
    public EntityType getType() {
        return type;
    }


    /**
     * Sets this object's type value.
     *
     * @param value     Type value
     */
    public void setType(EntityType value) {
        this.type = value;
    }


    /**
     * Returns a label suitable for the given language.
     *
     * If no label is defined for the specified language this method
     * returns either a label for the default languagage if it exists
     * or the first label found. May return null if the entity has
     * no labels defined.
     *
     * @see LangString#DEFAULT_LANGUAGE
     * @param language  Requested language tag
     * @return          A language string or {@code null}
     */
    public LangString getLabel(Language language) {
        if (labels == null || labels.isEmpty()) {
            return null;
        }

        LangString choice = getLabels().get(0);
        Language fallback = LangString.DEFAULT_LANGUAGE;

        for (LangString label : getLabels()) {
            if (language.equals(label.getLanguage())) {
                choice = label;
                break;
            } else if (language.equals(fallback)) {
                choice = label;
            }
        }

        return choice;
    }


    /**
     * Returns a label suitable for the given language code.
     * @see #getLabel(Language)
     *
     * @param code      ISO 639-1 language code
     * @return          A language string or {@code null}
     */
    public LangString getLabel(String code) {
        Language lang = null;

        try {
            lang = Language.fromValue(code);
        } catch (IllegalArgumentException e) {
            lang = LangString.DEFAULT_LANGUAGE;
        }

        return getLabel(lang);
    }


    /**
     * Returns this object's labels value.
     *
     * @return          Labels value
     */
    public List<LangString> getLabels() {
        if (labels == null) {
            labels = new ArrayList<LangString>();
        }

        return labels;
    }


    /**
     * Whether the provided object is equal to this one.
     *
     * The objects are considered to be equal if they are two intances of
     * an {@code Entity} with the same unique identifier.
     *
     * @param o         An object instance
     * @return          True if the objects are equivalent
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;

        if (o instanceof Entity) {
            Entity entity = (Entity) o;
            return Objects.equals(id, entity.id);
        }

        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    /**
     * Returns a string representation of the entity.
     *
     * @return          A textual representation
     */
    @Override
    public String toString() {
        return String.valueOf(getUID());
    }

}
