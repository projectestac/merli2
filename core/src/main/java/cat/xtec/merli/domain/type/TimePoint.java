package cat.xtec.merli.domain.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * A point in time encoded as an ISO8601 date-time or as a textual
 * description of the point in time.
 */
@XmlType(name = "date")
@XmlAccessorType(XmlAccessType.NONE)
public final class TimePoint implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Numerical date-time value */
    @XmlElement(name = "dateTime")
    @XmlSchemaType(name = "dateTime")
    protected Date date;

    /** Textaul descriptions */
    @XmlElement(name = "string")
    @XmlElementWrapper(name = "description")
    protected List<LangString> descriptions;


    /**
     * Returns this object's date value.
     *
     * @return          Date value
     */
    public Date getDate() {
        return date;
    }


    /**
     * Sets this object's date value.
     *
     * @param value     Date value
     */
    public void setDate(Date value) {
        date = value;
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
