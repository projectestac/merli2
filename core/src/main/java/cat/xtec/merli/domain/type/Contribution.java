package cat.xtec.merli.domain.type;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

import cat.xtec.merli.domain.voc.ContributorRole;
import cat.xtec.merli.bind.*;
import cat.xtec.merli.xml.*;


/**
 * Contribution to the state of a learning object.
 */
@XmlType(name = "contribute")
@XmlAccessorType(XmlAccessType.NONE)
public class Contribution implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Type of contribution */
    @XmlElement(name = "role", required = true)
    @XmlJavaTypeAdapter(ContributorRoleAdapter.class)
    protected ContributorRole role;

    /** List of contributors */
    @DucProperty(DucVocabulary.CONTRIBUTOR)
    @XmlElement(name = "entity")
    protected Contact entity;

    /** Date of the contribution */
    @DucProperty(DucVocabulary.DATE)
    @XmlElement(name = "date")
    protected TimePoint timePoint;


    /**
     * Returns this object's contributor role value.
     *
     * @return          Contributor role value
     */
    public ContributorRole getRole() {
        return role;
    }


    /**
     * Sets this object's contributor role value.
     *
     * @param value     Contributor role value
     */
    public void setRole(ContributorRole value) {
        this.role = value;
    }


    /**
     * Returns this object's entity value.
     *
     * @return          Entity reference
     */
    public Contact getEntity() {
        return entity;
    }


    /**
     * Sets this object's entity value.
     *
     * @param value     Entity reference
     */
    public void setEntity(Contact value) {
        this.entity = value;
    }


    /**
     * Sets this object's entity value.
     *
     * @param name      Entity name
     */
    public void setEntity(String name) {
        this.entity = new Contact(name, null);
    }


    /**
     * Returns this object's time point value.
     *
     * @return          Time point value
     */
    public TimePoint getTimePoint() {
        if (timePoint == null) {
            timePoint = new TimePoint();
        }

        return timePoint;
    }


    /**
     * Sets this object's time point value.
     *
     * @param value     Time point value
     */
    public void setTimePoint(TimePoint value) {
        this.timePoint = value;
    }


    /**
     * Sets this object's time point value.
     *
     * @param date      Date value
     */
    public void setTimePoint(Date date) {
        this.timePoint = new TimePoint();
        this.timePoint.setDate(date);
    }

}
