package cat.xtec.merli.domain.taxa;

import java.util.Arrays;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;
import cat.xtec.merli.domain.lom.Resource;
import cat.xtec.merli.bind.*;


/**
 * Relationships types.
 */
@XmlEnum()
public enum RelationType implements EnumString<RelationType> {

    /* Generic relationships (DUCv3.0) */

    /** A related entity */
    @XmlEnumValue("entity")
    @DucConstant(DucVocabulary.RELATION)
    ENTITY(Entity.class, "term"),

    /** A related vocabulary term */
    @XmlEnumValue("term")
    @DucConstant(DucVocabulary.TERM_RELATION)
    TERM(Entity.class, "term"),

    /** A related learning object */
    @XmlEnumValue("resource")
    @DucConstant(DucVocabulary.LOM_RELATION)
    RESOURCE(Entity.class, "resource"),

    /* Term relationships (DUCv3.0) */

    /** Broadest class for the term */
    @XmlEnumValue("top term")
    @DucConstant(DucVocabulary.TOP_TERM)
    TOP_TERM(Term.class, "top term"),

    /** Term that has a broader meaning */
    @XmlEnumValue("broader term")
    @DucConstant(DucVocabulary.BROADER_TERM)
    BROADER_TERM(Term.class, "broader term"),

    /** Term that has a narrower meaning */
    @XmlEnumValue("narrower term")
    @DucConstant(DucVocabulary.NARROWER_TERM)
    NARROWER_TERM(Term.class, "narrower term"),

    /** Related term */
    @XmlEnumValue("related term")
    @DucConstant(DucVocabulary.RELATED_TERM)
    RELATED_TERM(Term.class, "related term"),

    /** Preferred term  */
    @XmlEnumValue("use")
    @DucConstant(DucVocabulary.USE)
    USE(Term.class, "use"),

    /** Non-preferred synonym */
    @XmlEnumValue("use for")
    @DucConstant(DucVocabulary.USE_FOR)
    USE_FOR(Term.class, "use for"),

    /* Resource relationships (LOMv1.0) */

    /** Resource required to support its function */
    @XmlEnumValue("requires")
    @DucConstant(DucVocabulary.REQUIRES)
    REQUIRES(Resource.class, "requires"),

    /** Resource that is pointed by this resource */
    @XmlEnumValue("references")
    @DucConstant(DucVocabulary.REFERENCES)
    REFERENCES(Resource.class, "references"),

    /** Resource that this resource includes */
    @XmlEnumValue("haspart")
    @DucConstant(DucVocabulary.HAS_PART)
    HAS_PART(Resource.class, "has part"),

    /** Resource that adapts this resource */
    @XmlEnumValue("hasversion")
    @DucConstant(DucVocabulary.HAS_VERSION)
    HAS_VERSION(Resource.class, "has version"),

    /** Same resource in another format */
    @XmlEnumValue("hasformat")
    @DucConstant(DucVocabulary.HAS_FORMAT)
    HAS_FORMAT(Resource.class, "has format"),

    /** Requires this resource to support its function */
    @XmlEnumValue("isrequiredby")
    @DucConstant(DucVocabulary.IS_REQUIRED_BY)
    IS_REQUIRED_BY(Resource.class, "is required by"),

    /** Resource that points to this resource */
    @XmlEnumValue("isreferencedby")
    @DucConstant(DucVocabulary.IS_REFERENCED_BY)
    IS_REFERENCED_BY(Resource.class, "is referenced by"),

    /** Resource where this resource is included */
    @XmlEnumValue("ispartof")
    @DucConstant(DucVocabulary.IS_PART_OF)
    IS_PART_OF(Resource.class, "is part of"),

    /** Resource that is adapted by this resource */
    @XmlEnumValue("isversionof")
    @DucConstant(DucVocabulary.IS_VERSION_OF)
    IS_VERSION_OF(Resource.class, "is version of"),

    /** Same resource in another format */
    @XmlEnumValue("isformatof")
    @DucConstant(DucVocabulary.IS_FORMAT_OF)
    IS_FORMAT_OF(Resource.class, "is format of"),

    /** Resource from which a resource derives */
    @XmlEnumValue("isbasedon")
    @DucConstant(DucVocabulary.SOURCE)
    IS_BASED_ON(Resource.class, "is based on"),

    /** Resource that derives from this resource */
    @XmlEnumValue("isbasisfor")
    @DucConstant(DucVocabulary.IS_BASIS_FOR)
    IS_BASIS_FOR(Resource.class, "is basis for");

    /** Enumeration value */
    private final String value;

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.LOM;

    /** Enumeration group */
    private final Class<? extends Entity> group;


    /**
     * Enumeration constructor
     *
     * @param group     Type group
     * @param value     Type value
     */
    <E extends Entity> RelationType(Class<E> group, String value) {
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
    public String value() {
        return value;
    }


    /**
     * {@inheritDoc}
     */
    public static RelationType fromValue(String value) {
        return EnumString.from(RelationType.class, value);
    }


    /**
     * Returns the enumeration constants for the given group.
     *
     * @param group     Enumeration group class
     * @return          Enumeration constants array
     */
    public static <T extends Entity> RelationType[] valuesFor(Class<T> group) {
        return Arrays.stream(RelationType.values())
            .filter(t -> group.equals(t.group()))
            .toArray(RelationType[]::new);
    }

}
