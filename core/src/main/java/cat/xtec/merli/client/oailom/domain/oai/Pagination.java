package cat.xtec.merli.client.oailom.domain.oai;

import java.io.Serializable;
import java.util.Date;
import java.math.BigInteger;
import javax.xml.bind.annotation.*;


/**
 * Information about a list of records request.
 */
@XmlType(name = "resumptionToken")
@XmlAccessorType(XmlAccessType.NONE)
public class Pagination implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Resumption token */
    @XmlValue
    protected String token;

    /** When the resumption token expires */
    @XmlAttribute(name = "expirationDate")
    @XmlSchemaType(name = "dateTime")
    protected Date expiration;

    /** Total number of records found */
    @XmlAttribute(name = "completeListSize")
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger size;

    /** Number of records returned so far */
    @XmlAttribute(name = "cursor")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger cursor;


    /**
     * Returns this object's token object.
     *
     * @return          Token object
     */
    public String getToken() {
        return token;
    }


    /**
     * Sets this object's token object.
     *
     * @return          Token object
     */
    public void setToken(String value) {
        this.token = value;
    }


    /**
     * Returns this object's expiration object.
     *
     * @return          Expiration object
     */
    public Date getExpiration() {
        return expiration;
    }


    /**
     * Sets this object's expiration object.
     *
     * @return          Expiration object
     */
    public void setExpiration(Date value) {
        this.expiration = value;
    }


    /**
     * Returns this object's size object.
     *
     * @return          Size object
     */
    public BigInteger getSize() {
        return size;
    }


    /**
     * Sets this object's size object.
     *
     * @return          Size object
     */
    public void setSize(BigInteger value) {
        this.size = value;
    }


    /**
     * Returns this object's cursor object.
     *
     * @return          Cursor object
     */
    public BigInteger getCursor() {
        return cursor;
    }


    /**
     * Sets this object's cursor object.
     *
     * @return          Cursor object
     */
    public void setCursor(BigInteger value) {
        this.cursor = value;
    }

}
