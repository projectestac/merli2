package cat.xtec.merli.client.oailom.domain.oai;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.domain.lom.Resource;
import cat.xtec.merli.client.oailom.domain.Namespace;


/**
 * Information about a record. This includes the record header and
 * the LOM metadata of the record.
 */
@XmlType(name = "record")
@XmlAccessorType(XmlAccessType.NONE)
public class Record implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** */
    @XmlElement(name = "header", required = true)
    protected Header header;

    /** */
    @XmlElement(name = "lom", namespace = Namespace.LOM)
    @XmlElementWrapper(name = "metadata")
    protected List<Resource> resources;


    /**
     * Returns this object's header object.
     *
     * @return          Header object
     */
    public Header getHeader() {
        return header;
    }


    /**
     * Sets this object's header object.
     *
     * @return          Header object
     */
    public void setHeader(Header value) {
        this.header = value;
    }


    /**
     * Returns this object's resource list reference.
     *
     * @return          Resource list reference
     */
    public List<Resource> getResources() {
        if (resources == null) {
            resources = new ArrayList<Resource>();
        }

        return resources;
    }

}
