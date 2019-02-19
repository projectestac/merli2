package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;
import cat.xtec.merli.bind.*;


/**
 * Whether use of the resource requires payment (LOMv1.0).
 */
@XmlEnum
@XmlType(name = "cost")
public enum Cost implements EnumString<Cost> {

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
    @DucString
    public String value() {
        return value;
    }


    /**
     * {@inheritDoc}
     */
    @DucCreator()
    public static Cost fromValue(String value) {
        return EnumString.from(Cost.class, value);
    }

}
