package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 * Identifier catalogs types (RFC-8141).
 *
 * Note that «merli» is not a registered Uniform Resource Name (URN)
 * namespace. It is used here to provide a means to uniquely identify
 * a resource that may have been imported from an external source.
 *
 * A «merli» URN has the form {@code urn:merli:<host>:<scope>:<id>};
 * for example, {@code urn:merli:clic.xtec.cat:project:1234} or
 * {@code urn:merli:merli.xtec.cat:resource:5678}.
 */
@XmlEnum
@XmlType(name = "catalog")
public enum Catalog implements EnumString {

    /** International Standard Book Number */
    @XmlEnumValue("ISBN")
    ISBN("isbn"),

    /** International Standard Serial Number */
    @XmlEnumValue("ISSN")
    ISSN("issn"),

    /** National Bibliography Number */
    @XmlEnumValue("NBN")
    LEGAL_DEPOSIT("nbn"),

    /** Unique Merli Identifier */
    @XmlEnumValue("merli")
    MERLI("merli"),

    /** Unknown catalog */
    @XmlEnumValue("other")
    OTHER_CATALOG("example");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.RFC8141;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Catalog(String value) {
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
    public static Catalog fromValue(String value) {
        for (Catalog object : Catalog.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }

}
