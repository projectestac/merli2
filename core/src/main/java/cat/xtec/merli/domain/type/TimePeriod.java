package cat.xtec.merli.domain.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.domain.Duration;
import cat.xtec.merli.bind.*;


/**
 * A period of time encoded as an ISO-8601 duration or as a textual
 * description of the period of time.
 */
@DucContainer()
@XmlType(name = "duration")
@XmlAccessorType(XmlAccessType.NONE)
public final class TimePeriod implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Numerical duration value */
    @DucAttribute(DucVocabulary.DURATION)
    @XmlElement(name = "duration")
    @XmlSchemaType(name = "dateTime")
    protected Duration duration;

    /** Textual descriptions */
    @XmlElement(name = "string")
    @XmlElementWrapper(name = "description")
    protected List<LangString> descriptions;


    /**
     * Returns this object's duration value.
     *
     * @return          Duration value
     */
    public Duration getDuration() {
        return duration;
    }


    /**
     * Sets this object's duration value.
     *
     * @param value     Duration value
     */
    public void setDuration(Duration value) {
        duration = value;
    }


    /**
     * Returns this object's description value.
     *
     * @return          Description value
     */
    public List<LangString> getDescriptions() {
        if (descriptions == null) {
            descriptions = new ArrayList<LangString>();
        }

        return descriptions;
    }


    /**
     * Sets this object's description value.
     *
     * @param value     Description value
     */
    public void setDescriptions(List<LangString> value) {
        this.descriptions = value;
    }

}
