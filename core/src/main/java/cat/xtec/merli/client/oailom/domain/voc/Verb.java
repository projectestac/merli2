package cat.xtec.merli.client.oailom.domain.voc;

import javax.xml.bind.annotation.*;


/**
 * Supported action verbs for a request.
 */
@XmlEnum
@XmlType(name = "verb")
public enum Verb {

    /** Retrieve the headers of the records */
    @XmlEnumValue("ListIdentifiers")
    LIST_IDENTIFIERS("ListIdentifiers"),

    /** Retrieve an individual record */
    @XmlEnumValue("GetRecord")
    GET_RECORD("GetRecord");

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Verb(String value) {
        this.value = value;
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
    public static Verb fromValue(String value) {
        for (Verb object : Verb.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }

}
