package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import cat.xtec.merli.domain.xml.EnumAdapter;
import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 * Whether copyright or other restrictions apply (LOMv1.0).
 */
@XmlEnum
@XmlType(name = "copyright")
public enum Copyright implements EnumString {

    /** Requires payment */
    @XmlEnumValue("yes")
    HAS_RESTRICTIONS("has restrictions"),

    /** Available without charge */
    @XmlEnumValue("no")
    WITHOUT_RESTRICTIONS("without restrictions");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.LOM;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Copyright(String value) {
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
    public static Copyright fromValue(String value) {
        for (Copyright object : Copyright.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }


    /** Vocabulary XML adapter for this enumeration */
    public static class Adapter extends EnumAdapter<Copyright> {
        public Adapter() { super(Copyright.class); }
    }

}
