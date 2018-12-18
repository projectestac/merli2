package cat.xtec.merli.domain.taxa;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;
import cat.xtec.merli.domain.lom.Resource;
import cat.xtec.merli.domain.taxa.Term;
import cat.xtec.merli.domain.xml.EnumAdapter;
import cat.xtec.merli.bind.*;



/**
 * Relationships types.
 */
@XmlEnum
public enum RelationType implements EnumString {

    /* Term relationships (DUCv3.0) */

    /** Broadest class for the term */
    @XmlEnumValue("top term")
    @DucRelation(DucVocabulary.TOP_TERM)
    TOP_TERM(Term.class, "top term"),

    /** Term that has a broader meaning */
    @XmlEnumValue("broader term")
    @DucRelation(DucVocabulary.BROADER_TERM)
    BROADER_TERM(Term.class, "broader term"),

    /** Term that has a narrower meaning */
    @XmlEnumValue("narrower term")
    @DucRelation(DucVocabulary.NARROWER_TERM)
    NARROWER_TERM(Term.class, "narrower term"),

    /** Related term */
    @XmlEnumValue("related term")
    @DucRelation(DucVocabulary.RELATED_TERM)
    RELATED_TERM(Term.class, "related term"),

    /** Preferred term  */
    @XmlEnumValue("use")
    @DucRelation(DucVocabulary.USE)
    USE(Term.class, "use"),

    /** Non-preferred synonym */
    @XmlEnumValue("use for")
    @DucRelation(DucVocabulary.USE_FOR)
    USE_FOR(Term.class, "use for"),

    /* Resource relationships (LOMv1.0) */

    /** Resource required to support its function */
    @XmlEnumValue("requires")
    @DucRelation(DucVocabulary.REQUIRES)
    REQUIRES(Resource.class, "requires"),

    /** Resource that is pointed by this resource */
    @XmlEnumValue("references")
    @DucRelation(DucVocabulary.REFERENCES)
    REFERENCES(Resource.class, "references"),

    /** Resource that this resource includes */
    @XmlEnumValue("haspart")
    @DucRelation(DucVocabulary.HAS_PART)
    HAS_PART(Resource.class, "has part"),

    /** Resource that adapts this resource */
    @XmlEnumValue("hasversion")
    @DucRelation(DucVocabulary.HAS_VERSION)
    HAS_VERSION(Resource.class, "has version"),

    /** Same resource in another format */
    @XmlEnumValue("hasformat")
    @DucRelation(DucVocabulary.HAS_FORMAT)
    HAS_FORMAT(Resource.class, "has format"),

    /** Requires this resource to support its function */
    @XmlEnumValue("isrequiredby")
    @DucRelation(DucVocabulary.IS_REQUIRED_BY)
    IS_REQUIRED_BY(Resource.class, "is required by"),

    /** Resource that points to this resource */
    @XmlEnumValue("isreferencedby")
    @DucRelation(DucVocabulary.IS_REFERENCED_BY)
    IS_REFERENCED_BY(Resource.class, "is referenced by"),

    /** Resource where this resource is included */
    @XmlEnumValue("ispartof")
    @DucRelation(DucVocabulary.IS_PART_OF)
    IS_PART_OF(Resource.class, "is part of"),

    /** Resource that is adapted by this resource */
    @XmlEnumValue("isversionof")
    @DucRelation(DucVocabulary.IS_VERSION_OF)
    IS_VERSION_OF(Resource.class, "is version of"),

    /** Same resource in another format */
    @XmlEnumValue("isformatof")
    @DucRelation(DucVocabulary.IS_FORMAT_OF)
    IS_FORMAT_OF(Resource.class, "is format of"),

    /** Resource from which a resource derives */
    @XmlEnumValue("isbasedon")
    @DucRelation(DucVocabulary.SOURCE)
    IS_BASED_ON(Resource.class, "is based on"),

    /** Resource that derives from this resource */
    @XmlEnumValue("isbasisfor")
    @DucRelation(DucVocabulary.IS_BASIS_FOR)
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
    public static RelationType fromValue(String value) {
        for (RelationType object : RelationType.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }


    /** Vocabulary XML adapter for this enumeration */
    public static class Adapter extends EnumAdapter<RelationType> {
        public Adapter() { super(RelationType.class); }
    }

}
