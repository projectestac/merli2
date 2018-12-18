package cat.xtec.merli.client.oailom.domain.oai;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A partial list of record headers with pagination. Were the headers
 * reference a record in the catalog.
 */
@XmlType(name = "ListIdentifiers")
@XmlAccessorType(XmlAccessType.NONE)
public class References implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** */
    @XmlElement(name = "resumptionToken")
    protected Pagination pagination;

    /** */
    @XmlElement(name = "header")
    protected List<Header> headers;


    /**
     * Returns this object's pagination object.
     *
     * @return          Pagination object
     */
    public Pagination getPagination() {
        return pagination;
    }


    /**
     * Sets this object's pagination object.
     *
     * @return          Pagination object
     */
    public void setPagination(Pagination value) {
        this.pagination = value;
    }


    /**
     * Returns this object's header list reference.
     *
     * @return          Header list reference
     */
    public List<Header> getHeaders() {
        if (headers == null) {
            headers = new ArrayList<Header>();
        }

        return this.headers;
    }

}
