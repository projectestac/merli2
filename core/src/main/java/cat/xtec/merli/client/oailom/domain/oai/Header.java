package cat.xtec.merli.client.oailom.domain.oai;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.domain.UID;
import cat.xtec.merli.client.oailom.domain.voc.Status;


/**
 * Information about a record.
 */
@XmlType(name = "header")
@XmlAccessorType(XmlAccessType.NONE)
public class Header implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Unique identifier of the record */
    @XmlElement(name = "identifier", required = true)
    @XmlSchemaType(name = "anyUID")
    protected UID identifier;

    /** Last update date of the record */
    @XmlElement(name = "datestamp" , required = true)
    protected Date updateDate;

    /** If the record was deleted */
    @XmlAttribute(name = "status")
    protected Status status;


    /**
     * Returns this object's identifier object.
     *
     * @return          Identifier object
     */
    public UID getIdentifier() {
        return identifier;
    }


    /**
     * Sets this object's identifier object.
     *
     * @return          Identifier object
     */
    public void setIdentifier(UID value) {
        this.identifier = value;
    }


    /**
     * Returns this object's update date object.
     *
     * @return          Update date object
     */
    public Date getUpdateDate() {
        return updateDate;
    }


    /**
     * Sets this object's update date object.
     *
     * @return          Update date object
     */
    public void setUpdateDate(Date value) {
        this.updateDate = value;
    }


    /**
     * Returns this object's status object.
     *
     * @return          Status object
     */
    public Status getStatus() {
        return status;
    }


    /**
     * Sets this object's status object.
     *
     * @return          Status object
     */
    public void setStatus(Status value) {
        this.status = value;
    }

}
