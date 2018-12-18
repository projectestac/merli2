package cat.xtec.merli.client.oailom.domain.voc;

import javax.xml.bind.annotation.*;


/**
 * Status of a record.
 */
@XmlEnum
@XmlType(name = "status")
public enum Status {

    /** The record was deleted */
    @XmlEnumValue("deleted")
    DELETED("deleted");

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
    public String value() {
        return value;
    }


    /**
     * Returns an enumeration object given a value.
     *
     * @param value     Enumeration value
     * @throws IllegalArgumentException
     */
    public static Status fromValue(String value) {
        for (Status object : Status.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }

}
