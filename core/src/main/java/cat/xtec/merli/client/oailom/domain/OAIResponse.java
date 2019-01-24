package cat.xtec.merli.client.oailom.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.client.oailom.domain.oai.Record;
import cat.xtec.merli.client.oailom.domain.oai.References;


/**
 * Represents a response from an OAI repository.
 */
@XmlType(name = "oai-pmh")
@XmlRootElement(name = "OAI-PMH")
@XmlAccessorType(XmlAccessType.NONE)
public class OAIResponse implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** References of for a ListIdentifiers request */
    @XmlElement(name = "ListIdentifiers")
    protected References references;

    /** Records of a GetRecord request */
    @XmlElement(name = "record")
    @XmlElementWrapper(name = "GetRecord")
    protected List<Record> records;


    /**
     * Returns this object's references object.
     *
     * @return          References object
     */
    public References getReferences() {
        return references;
    }


    /**
     * Sets this object's references object.
     *
     * @param value     References object
     */
    public void setReferences(References value) {
        this.references = value;
    }


    /**
     * Returns this object's record list reference.
     *
     * @return          Record list reference
     */
    public List<Record> getRecords() {
        if (records == null) {
            records = new ArrayList<Record>();
        }

        return records;
    }

}
