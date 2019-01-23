package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

// import cat.xtec.merli.xml.EnumAdapter;
import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 * Underlying organizational structure of a learning object (LOMv1.0).
 */
@XmlEnum
@XmlType(name = "structure")
public enum Structure implements EnumString {

    /** Indivisible resource */
    @XmlEnumValue("atomic")
    ATOMIC("atomic"),

    /** Set of unlinked resources */
    @XmlEnumValue("collection")
    COLLECTION("collection"),

    /** Set of resources linked as a tree */
    @XmlEnumValue("hierarchical")
    HIERARCHICAL("hierarchical"),

    /** Set of resources linked as a path */
    @XmlEnumValue("linear")
    LINEAR("linear"),

    /** Set of resources linked as a graph */
    @XmlEnumValue("networked")
    NETWORKED("networked");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.LOM;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Structure(String value) {
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
    public static Structure fromValue(String value) {
        return EnumString.from(Structure.class, value);
    }


    /** Vocabulary XML adapter for this enumeration */
    // public static class Adapter extends EnumAdapter<Structure> {
    //     public Adapter() { super(Structure.class); }
    // }

}
