package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 * Classification purpose (LOMv1.0).
 */
@XmlEnum
@XmlType(name = "Purpose")
public enum Purpose implements EnumString {

    /** Accessibility restrictions */
    @XmlEnumValue("accessibility restrictions")
    ACCESSIBILITY_RESTRICTIONS("accessibility restrictions"),

    /** Competency */
    @XmlEnumValue("competency")
    COMPETENCY("competency"),

    /** Discipline */
    @XmlEnumValue("discipline")
    DISCIPLINE("discipline"),

    /** Educational level */
    @XmlEnumValue("educational level")
    EDUCATIONAL_LEVEL("educational level"),

    /** Educational objective */
    @XmlEnumValue("educational objective")
    EDUCATIONAL_OBJECTIVE("educational objective"),

    /** Idea */
    @XmlEnumValue("idea")
    IDEA("idea"),

    /** Prerequisite */
    @XmlEnumValue("prerequisite")
    PREREQUISITE("prerequisite"),

    /** Security level */
    @XmlEnumValue("security level")
    SECURITY_LEVEL("security level"),

    /** Skill level */
    @XmlEnumValue("skill level")
    SKILL_LEVEL("skill level"),

    /** Educational context of a resource */
    @XmlEnumValue("educational context")
    EDUCATIONAL_CONTEXT("educational context", EnumSource.MERLI_DUC),

    /** Main topic of a resource */
    @XmlEnumValue("subject descriptor")
    SUBJECT_DESCRIPTOR("subject descriptor", EnumSource.MERLI_ETB);

    /** Source of the vocabulary */
    private final EnumSource source;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value     String value
     */
    Purpose(String value) {
        this(value, EnumSource.LOM);
    }


    /**
     * Enumeration constructor
     *
     * @param value     String value
     * @param source    Source value
     */
    Purpose(String value, EnumSource source) {
        this.value = value;
        this.source = source;
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
    public static Purpose fromValue(String value) {
        return EnumString.from(Purpose.class, value);
    }

}
