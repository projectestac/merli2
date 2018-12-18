package cat.xtec.merli.domain;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * Controlled vocabulary sources.
 *
 * References:
 *
 * - LOMv1.0 standard (IEEE Std 1484.12.1-2002)
 * - Vocabulary Bank of Education (VBE) (http://aspect.vocman.com/vbe/)
 * - IANA Protocol Registries (https://www.iana.org/protocols)
 */
@XmlEnum
@XmlType(name = "source")
public enum EnumSource {

    /** Unitary Curriculum Design */
    @XmlEnumValue("DUCv3.0")
    DUC("DUC 3.0"),

    /** Learning Object Metadata */
    @XmlEnumValue("LOMv1.0")
    LOM("LOM 1.0"),

    /** Learning Resource Exchange */
    @XmlEnumValue("LREv3.0")
    LRE("LRE 3.0"),

    /** Uniform Resource Names */
    @XmlEnumValue("RFC-8141")
    RFC8141("RFC 8141"),

    /** Disseny Unitari del Currículum */
    @XmlEnumValue("DUC")
    MERLI_DUC("DUC"),

    /** European Treasury Browser */
    @XmlEnumValue("ETB")
    MERLI_ETB("ETB");

    /** Enumeration value */
    private final String value;


    /**
     * {@inheritDoc}
     */
    EnumSource(String value) {
        this.value = value;
    }


    /**
     * Returns this enumeration value.
     *
     * @return  String value
     */
    public String value() {
        return value;
    }


    /**
     * {@inheritDoc}
     */
    public static EnumSource fromValue(String value) {
        for (EnumSource object : EnumSource.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }

}
