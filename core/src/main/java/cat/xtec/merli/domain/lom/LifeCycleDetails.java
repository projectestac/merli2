package cat.xtec.merli.domain.lom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

import cat.xtec.merli.domain.type.Contribution;
import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.domain.voc.Status;
import cat.xtec.merli.bind.DucVocabulary;
import cat.xtec.merli.bind.*;


/**
 * Features related to the history and current state of a learning object
 * and those who have affected it during its evolution.
 */
@XmlType(name = "lifeCycle")
@XmlAccessorType(XmlAccessType.NONE)
public class LifeCycleDetails implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** State or condition of the learning object */
    @DucProperty(DucVocabulary.STATUS)
    @XmlElement(name = "status")
    @XmlJavaTypeAdapter(Status.Adapter.class)
    protected Status status;

    /** Edition of the learning object */
    @DucProperty(DucVocabulary.VERSION)
    @XmlElement(name = "string")
    @XmlElementWrapper(name = "version")
    protected List<LangString> versions;

    /** Contributors to the learning object */
    @XmlElement(name = "contribute")
    protected List<Contribution> contributions;


    /**
     * Returns this object's status value.
     *
     * @return          Status value
     */
    public Status getStatus() {
        return status;
    }


    /**
     * Sets this object's status value.
     *
     * @param value     Status value
     */
    public void setStatus(Status value) {
        this.status = value;
    }


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


    /**
     * Returns this object's version value.
     *
     * @return          Version value
     */
    public List<LangString> getVersions() {
        if (versions == null) {
            versions = new ArrayList<LangString>();
        }

        return versions;
    }

}
