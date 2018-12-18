package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import cat.xtec.merli.domain.xml.EnumAdapter;
import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 *  Normal user of a learning object (LOMv1.0).
 */
@XmlEnum
@XmlType(name = "UserRole")
public enum UserRole implements EnumString {

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
    public String value() {
        return value;
    }


    /**
     * {@inheritDoc}
     */
    public static UserRole fromValue(String value) {
        for (UserRole object : UserRole.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }


    /** Vocabulary XML adapter for this enumeration */
    public static class Adapter extends EnumAdapter<UserRole> {
        public Adapter() { super(UserRole.class); }
    }

}
