package cat.xtec.merli.domain.lom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.domain.type.Contribution;


/**
 * History and current state of a learning object metadata.
 */
@XmlType(name = "metaMetadata")
@XmlAccessorType(XmlAccessType.NONE)
public class MetadataDetails implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Contributors to the metadata */
    @XmlElement(name = "contribute")
    protected List<Contribution> contributions;


    /**
     * Returns this object's contribution list reference.
     *
     * @return          Contribution list reference
     */
    public List<Contribution> getContributions() {
        if (contributions == null) {
            contributions = new ArrayList<Contribution>();
        }

        return this.contributions;
    }

}
