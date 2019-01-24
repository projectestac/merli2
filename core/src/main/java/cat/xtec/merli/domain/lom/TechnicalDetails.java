package cat.xtec.merli.domain.lom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.type.TimePeriod;
import cat.xtec.merli.domain.voc.Format;
import cat.xtec.merli.bind.*;


/**
 * Technical requirements and characteristics of a learning object.
 */
@XmlType(name = "technical")
@XmlAccessorType(XmlAccessType.NONE)
public class TechnicalDetails implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Duration of the learning object */
    @DucProperty(DucVocabulary.DURATION)
    @XmlElement(name = "duration")
    protected TimePeriod timePeriod;

    /** UID of the learning object */
    @DucProperty(DucVocabulary.LOCATION)
    @XmlElement(name = "location")
    @XmlSchemaType(name = "anyUID")
    protected UID location;

    /** Media types of the learning object */
    @DucProperty(DucVocabulary.FORMAT)
    @XmlElement(name = "format")
    protected List<Format> formats;


    /**
     * Returns this object's time period value.
     *
     * @return          Time period value
     */
    public TimePeriod getTimePeriod() {
        return timePeriod;
    }


    /**
     * Sets this object's time period value.
     *
     * @param value     Time period value
     */
    public void setTimePeriod(TimePeriod value) {
        this.timePeriod = value;
    }


    /**
     * Returns this object's location value.
     *
     * @return          Location reference
     */
    public UID getLocation() {
        return location;
    }


    /**
     * Sets this object's location value.
     *
     * @param value     Location reference
     */
    public void setLocation(UID value) {
        this.location = value;
    }


    /**
     * Returns this object's format list reference .
     *
     * @return          Format list reference
     */
    public List<Format> getFormats() {
        if (formats == null) {
            formats = new ArrayList<Format>();
        }

        return this.formats;
    }

}
