package cat.xtec.merli.domain;

import java.util.Objects;
import java.io.Serializable;
import javax.xml.bind.annotation.*;
import cat.xtec.merli.bind.*;


/**
 * Unique resource identifier.
 *
 * This class represents an Internationalized Resource Identifier and
 * serves as an identifier for entities and other resources (which are
 * identified by an URI or an URN). It mimics the {@code java.net.URI}
 * in a way that is compatible with Google Web Toolkit (GWT).
 */
@XmlType(name = "uri")
@XmlAccessorType(XmlAccessType.NONE)
public class UID implements Serializable, Comparable<UID> {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Empty string value */
    protected static final String EMPTY_STRING = new String();

    /** DucNamespace separators */
    private static String[] SEPARATORS = {"#", "/", ":"};

    /** Value as a string */
    @XmlValue()
    protected String string;


    /**
     * Constructs a new empty URI.
     */
    public UID() {
        this(EMPTY_STRING);
    }


    /**
     * Constructs a URI for the given string.
     *
     * @param value       URI string
     */
    public UID(String string) {
        this.setString(string);
    }


    /**
     * Returns a new URI for the given object.
     *
     * @param value     Object to parse
     * @return          New URI instance
     */
    public static UID valueOf(Object value) {
        return valueOf(String.valueOf(value));
    }


    /**
     * Returns a new URI for the given string.
     *
     * @param value     String to parse
     * @return          New UID instance
     */
    @DucCreator()
    public static UID valueOf(String value) {
        return new UID(value);
    }


    /**
     * Returns this object's URI string.
     *
     * @return          URI string
     */
    public String getString() {
        return string;
    }


    /**
     * Sets this object's URI string.
     *
     * @param string    URI string
     */
    public void setString(String string) {
        this.string = (string != null) ?
            string : EMPTY_STRING;
    }


    /**
     * Returns the scheme part of this object's URI string.
     *
     * @return          Scheme string
     */
    public String getScheme() {
        return (string.contains(":")) ?
            string.split(":")[0] : EMPTY_STRING;
    }


    /**
     * Returns the namespace part of this object's URI string.
     *
     * @return          Namespace string
     */
    public String getNamespace() {
        for (String separator : SEPARATORS) {
            if (string.contains(separator)) {
                return string.split(separator, 1)[0];
            }
        }

        return EMPTY_STRING;
    }


    /**
     * Returns the localname part of this object's URI string.
     *
     * @return          Local name string
     */
    public String getLocalName() {
        for (String separator : SEPARATORS) {
            if (string.contains(separator)) {
                return string.split(separator, 1)[1];
            }
        }

        return EMPTY_STRING;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(UID o) {
        return string.compareTo(o.string);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (object instanceof UID) {
            UID uri = (UID) object;
            return string.equals(uri.string);
        }

        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(string);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.valueOf(string);
    }

}
