package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 * Content licenses (DUCv3.0)
 */
@XmlEnum
@XmlType(name = "license")
public enum License implements EnumString {

    /** CreativeCommons Attribution */
    @XmlEnumValue("creative commons by")
    CREATIVE_COMMONS_BY("creative commons by"),

    /** CreativeCommons Attribution-ShareAlike */
    @XmlEnumValue("creative commons by-sa")
    CREATIVE_COMMONS_BYSA("creative commons by-sa"),

    /** CreativeCommons Attribution-NoDerivatives */
    @XmlEnumValue("creative commons by-nd")
    CREATIVE_COMMONS_BYND("creative commons by-nd"),

    /** CreativeCommons Attribution-NonCommercial */
    @XmlEnumValue("creative commons by-nc")
    CREATIVE_COMMONS_BYNC("creative commons by-nc"),

    /** CreativeCommons Attribution-NonCommercial-ShareAlike */
    @XmlEnumValue("creative commons by-nc-sa")
    CREATIVE_COMMONS_BYNCSA("creative commons by-nc-sa"),

    /** CreativeCommons Attribution-NonCommercial-NoDerivatives */
    @XmlEnumValue("creative commons by-nc-nd")
    CREATIVE_COMMONS_BYNCND("creative commons by-nc-nd"),

    /** Edu3 educational license */
    @XmlEnumValue("edu3 educational")
    EDU3_EDUCATIONAL("edu3 educational"),

    /** Other copyright licenses */
    @XmlEnumValue("other copyright")
    OTHER_COPYRIGHT("other copyright"),

    /** Public domain */
    @XmlEnumValue("public domain")
    PUBLIC_DOMAIN("public domain");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.DUC;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    License(String value) {
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
    public static License fromValue(String value) {
        return EnumString.from(License.class, value);
    }

}
