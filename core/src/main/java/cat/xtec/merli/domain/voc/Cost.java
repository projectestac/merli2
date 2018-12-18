package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import cat.xtec.merli.domain.xml.EnumAdapter;
import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 * Whether use of the resource requires payment (LOMv1.0).
 */
@XmlEnum
@XmlType(name = "cost")
public enum Cost implements EnumString {

    /** Requires payment */
    @XmlEnumValue("yes")
    REQUIRES_PAYMENT("requires payment"),

    /** Available without charge */
    @XmlEnumValue("no")
    WITHOUT_CHARGE("without charge");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.LOM;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Cost(String value) {
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
    public static Cost fromValue(String value) {
        for (Cost object : Cost.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }


    /** Vocabulary XML adapter for this enumeration */
    public static class Adapter extends EnumAdapter<Cost> {
        public Adapter() { super(Cost.class); }
    }

}
