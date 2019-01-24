package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 * Educational contexts. Typical learning environment where use of a
 * learning object is intended to take place (LREv3.0).
 */
@XmlEnum
@XmlType(name = "context")
public enum Context implements EnumString {

    /** Education after kindergarten and before higher education */
    @XmlEnumValue("compulsory education")
    COMPULSORY_EDUCATION("compulsory education"),

    /** Further education of those over ordinary school age */
    @XmlEnumValue("continuing education")
    CONTINUING_EDUCATION("continuing education"),

    /** Education no requiring to be physically present */
    @XmlEnumValue("distance education")
    DISTANCE_EDUCATION("distance education"),

    /** Management of educational and training institutions */
    @XmlEnumValue("educational administration")
    EDUCATIONAL_ADMINISTRATION("educational administration"),

    /** Education provided by a college or university */
    @XmlEnumValue("higher education")
    HIGHER_EDUCATION("higher education"),

    /** School libraries/documentation centers */
    @XmlEnumValue("library")
    LIBRARY("library"),

    /** Makers of policy decisions */
    @XmlEnumValue("policy making")
    POLICY_MAKING("policy making"),

    /** Kindergarten or nursery school */
    @XmlEnumValue("pre-school")
    PRESCHOOL("pre-school"),

    /** Related to improving professional skills */
    @XmlEnumValue("professional development")
    PROFESSIONAL_DEVELOPMENT("professional development"),

    /** For persons who have special educational needs */
    @XmlEnumValue("special education")
    SPECIAL_EDUCATION("special education"),

    /** Relating to a vocation or occupation */
    @XmlEnumValue("vocational education")
    VOCATIONAL_EDUCATION("vocational education"),

    /** Other contexts */
    @XmlEnumValue("other")
    OTHER("other");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.LRE;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Context(String value) {
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
     * {@inheritDoc}
     */
    public static Context fromValue(String value) {
        return EnumString.from(Context.class, value);
    }

}
