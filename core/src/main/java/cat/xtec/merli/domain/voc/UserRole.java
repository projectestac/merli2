package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;
import cat.xtec.merli.bind.*;


/**
 *  Normal user of a learning object (LOMv1.0).
 */
@XmlEnum
@XmlType(name = "UserRole")
public enum UserRole implements EnumString<UserRole> {

    /** Whoever creates or publishes a resource */
    @XmlEnumValue("author")
    AUTHOR("author"),

    /** Whoever works with the resource to learn something */
    @XmlEnumValue("learner")
    LEARNER("learner"),

    /** Whoever manages the delivery of the resource */
    @XmlEnumValue("manager")
    MANAGER("manager"),

    /** Whoever uses the reource to teach or instruct */
    @XmlEnumValue("teacher")
    TEACHER("teacher");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.LOM;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    UserRole(String value) {
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
    public static UserRole fromValue(String value) {
        return EnumString.from(UserRole.class, value);
    }

}
