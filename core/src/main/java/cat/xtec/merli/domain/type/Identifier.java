package cat.xtec.merli.domain.type;

import javax.xml.bind.annotation.*;
import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.voc.Catalog;
import cat.xtec.merli.bind.*;


/**
 * An object identifier in a catalog.
 */
@XmlType(name = "identifier")
@XmlAccessorType(XmlAccessType.NONE)
public class Identifier extends UID {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Default catalog value */
    public static final Catalog DEFAULT_CATALOG = Catalog.OTHER_CATALOG;

    /** Name of the catalog */
    @XmlElement(name = "catalog", required = true)
    protected Catalog catalog;

    /** Value of the entry within the catalog */
    @XmlElement(name = "entry", required = true)
    protected String entry;


    /**
     * Constructs a new empty identifier.
     */
    public Identifier() {
        super();
    }


    /**
     * Constructs a new identifier from the given string..
     *
     * @param string       URN string
     */
    public Identifier(String string) {
        super(string);
    }


    /**
     * Constructs a new identifier from the given URN.
     *
     * @param string       URN identifier
     */
    public Identifier(UID urn) {
        setString(String.valueOf(urn));
    }


    /**
     * Returns a new identifier for the given object.
     *
     * @param value     Object to parse
     * @return          New identifier instance
     */
    public static Identifier valueOf(Object value) {
        return valueOf(String.valueOf(value));
    }


    /**
     * Returns a new identifier for the given string.
     *
     * @param value     String to parse
     * @return          New identifier instance
     */
    @DucCreator()
    public static Identifier valueOf(String value) {
        return new Identifier(value);
    }


    /**
     * Sets this object's URN string.
     *
     * @param string     URN string
     * @throws IllegalArgumentException
     */
    public void setString(String string) {
        if (string == null || string.isEmpty()) {
            this.catalog = null;
            this.entry = EMPTY_STRING;
            this.string = EMPTY_STRING;
            return;
        }

        String[] parts = string.split(":", 3);

        if (!"urn".equals(parts[0])) {
            throw new IllegalArgumentException(
                "Not a valid Uniform Resource Name");
        }

        this.catalog = Catalog.fromValue(parts[1]);
        this.entry = parts[2];
        this.string = string;
    }


    /**
     * Updates the URN of this identifier. This identifier's URN is a
     * string of the form "urn:<catalog>:<entry>".
     */
    private void updateString() {
        Catalog c = (catalog == null) ? DEFAULT_CATALOG : catalog;
        this.string = "urn:" + c.value() + ":" + entry;
    }


    /**
     * Returns this object's catalog value.
     *
     * @return          Catalog value
     */
    public Catalog getCatalog() {
        return catalog;
    }


    /**
     * Sets this object's catalog value.
     *
     * @param value     Catalog value
     */
    public void setCatalog(Catalog value) {
        this.catalog = value;
        this.updateString();
    }


    /**
     * Returns this object's entry value.
     *
     * @return          Entry value
     */
    public String getEntry() {
        return entry;
    }


    /**
     * Sets this object's entry value.
     *
     * @param value     Entry value
     */
    public void setEntry(String value) {
        this.entry = value;
        this.updateString();
    }

}
