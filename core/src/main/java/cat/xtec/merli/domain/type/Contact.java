package cat.xtec.merli.domain.type;

import javax.xml.bind.annotation.*;


/**
 * A user business card.
 *
 * This object stores a contact as an vCard string, but note that no
 * validation or parsing is performed on the given strings. The vCard
 * is stored AS IS with no guarantees on its validity.
 */
@XmlType(name = "entity")
@XmlAccessorType(XmlAccessType.NONE)
public class Contact {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Card version string */
    private static String VERSION = "3.0";

    /** Newline character */
    private static String CRLF = "\r\n";

    /** Representation as an vCard */
    protected String string;


    /**
     * Constructs a new empty contact.
     */
    public Contact() {
        this.setString(toCardString("UNKNOWN", null));
    }


    /**
     * Constructs a new contact from a vCard string.
     *
     * @param string       String expression of a vCard
     */
    public Contact(String string) {
        this.setString(string);
    }


    /**
     * Constructs a new contact given a formatted name and
     * an organization string.
     *
     * @param name          Contact name
     * @param organization  Organization name
     *
     * @throws NullPointerException     If name is {@code null}
     */
    public Contact(String name, String organization) {
        this.setString(toCardString(name, organization));
    }


    /**
     * Parses the given vCard string to produce a new contact.
     *
     * @param value         Expression of a vCard
     * @return              New vCard object
     */
    public static Contact parse(String value) {
        return new Contact(value);
    }


    /**
     * Returns the string value of this object.
     *
     * @return              String value
     */
    protected String getString() {
        return string;
    }


    /**
     * Sets the string value of this object.
     *
     * @param value         Card expression
     */
    @XmlValue()
    protected void setString(String value) {
        this.string = value;
    }


    /**
     * Formats this object as an vCard string expression.
     *
     * @return          String expression
     */
    private String toCardString(String name, String organization) {
        StringBuilder sb = new StringBuilder();

        sb.append(asField("BEGIN", "VCARD"));
        sb.append(asField("VERSION", VERSION));
        sb.append(asField("FN", name));

        if (organization instanceof String) {
            sb.append(asField("ORG", organization));
        }

        sb.append(asField("END", "VCARD"));

        return sb.toString();
    }


    /**
     * Returns a field formatted for a vCard. That is returns the
     * string "name:value CRLF".
     *
     * @param name          Field name
     * @param value         Field value
     *
     * @return              A string value
     */
    private String asField(String name, String value) {
        return name + ":" + value + CRLF;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return string;
    }

}
