package cat.xtec.merli.domain.taxa;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;
import cat.xtec.merli.domain.lom.Resource;
import cat.xtec.merli.domain.taxa.Term;
import cat.xtec.merli.bind.*;


/**
 * Entity types (DUCv3.0).
 */
@XmlEnum
public enum EntityType implements EnumString {

    /* Hierarchy types (DUCv3.0) */

    /** Top hierarchy concept */
    @XmlEnumValue("root concept")
    ROOT_CONCEPT(Entity.class, "root concept"),

    /** Category concept */
    @XmlEnumValue("content category")
    CONTENT_CATEGORY(Entity.class, "content category"),

    /** Learning object */
    @XmlEnumValue("learning object")
    LEARNING_OBJECT(Entity.class, "learning object"),

    /** Thesaurus term */
    @XmlEnumValue("thesaurus term")
    VOCABULARY_TERM(Entity.class, "thesaurus term"),

    /** Term not from a thesaurus  */
    @XmlEnumValue("free term")
    FREE_TERM(Entity.class, "free term"),

    /* Category types (DUCv3.0) */

    /** Top educational level */
    @XmlEnumValue("education phase")
    EDUCATION_PHASE(Category.class, "education phase"),

    /** Stage level */
    @XmlEnumValue("education stage")
    EDUCATION_STAGE(Category.class, "education stage"),

    /** Academic year level */
    @XmlEnumValue("academic year")
    ACADEMIC_YEAR(Category.class, "academic year"),

    /** Discipline of a curriculum */
    @XmlEnumValue("field of study")
    FIELD_OF_STUDY(Category.class, "field of study"),

    /** Specific discipline of an educational level */
    @XmlEnumValue("programme of study")
    PROGRAMME_OF_STUDY(Category.class, "programme of study"),

    /** Topic of a programme study */
    @XmlEnumValue("topic of study")
    TOPIC_OF_STUDY(Category.class, "topic of study"),

    /* Thesaurus term types (DUCv3.0) */

    /** Node label (facet indicator) */
    @XmlEnumValue("facet indicator")
    FACET_INDICATOR(Term.class, "facet indicator"),

    /** Preferred term (descriptor) */
    @XmlEnumValue("preferred term")
    PREFERRED_TERM(Term.class, "preferred term"),

    /** Non-preferred term (non-descriptor) */
    @XmlEnumValue("non-preferred term")
    NON_PREFERRED_TERM(Term.class, "non-preferred term");

    /** Enumeration value */
    private final String value;

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.DUC;

    /** Enumeration group */
    private final Class<? extends Entity> group;


    /**
     * Enumeration constructor
     *
     * @param group     Type group
     * @param value     Type value
     */
    <E extends Entity> EntityType(Class<E> group, String value) {
        this.group = group;
        this.value = value;
    }


    /**
     * {@inheritDoc}
     */
    public EnumSource source() {
        return source;
    }


    /**
     * {@inheritDoc}
     */
    public String value() {
        return value;
    }


    /**
     * Returns this enumeration group.
     *
     * @return  String value
     */
    public Class<? extends Entity> group() {
        return group;
    }


    /**
     * {@inheritDoc}
     */
    public static EntityType fromValue(String value) {
        for (EntityType object : EntityType.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }

}
