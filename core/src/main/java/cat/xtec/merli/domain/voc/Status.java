package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;
import cat.xtec.merli.bind.*;


/**
 * The state or condition of a learning object (LOMv1.0).
 */
@XmlEnum
@XmlType(name = "status")
public enum Status implements EnumString<Status> {

    /** Draft */
    @XmlEnumValue("draft")
    DRAFT("draft"),

    /** Final */
    @XmlEnumValue("final")
    FINAL("final"),

    /** Revised */
    @XmlEnumValue("revised")
    REVISED("revised"),

    /** The resource is not available */
    @XmlEnumValue("unavailable")
    UNAVAILABLE("unavailable");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.LOM;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Status(String value) {
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
    @DucString
    public String value() {
        return value;
    }


    /**
     * {@inheritDoc}
     */
    @DucCreator()
    public static Status fromValue(String value) {
        return EnumString.from(Status.class, value);
    }

}
