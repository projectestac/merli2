package cat.xtec.merli.domain.lom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.domain.Namespace;
import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.domain.voc.Format;
import cat.xtec.merli.domain.voc.Medium;
import cat.xtec.merli.bind.DucVocabulary;
import cat.xtec.merli.bind.*;


/**
 * Physical characteristics of a learning object.
 *
 * Note that this is not part of the LOM standard but an extension
 * of the format introduced by DUC.
 */
@XmlType(name = "physical")
@XmlAccessorType(XmlAccessType.NONE)
public class PhysicalDetails implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Physical characteristics of the learning object */
    @DucProperty(DucVocabulary.MEDIUM)
    @XmlElement(name = "medium", namespace = Namespace.DC)
    protected List<LangString> descriptions;

    /** Physical types of the the learning object */
    @DucProperty(DucVocabulary.FORMAT)
    @XmlElement(name = "format", namespace = Namespace.DC)
    protected List<Medium> mediums;


    /**
     * Returns this object's descriptions list reference .
     *
     * @return          Description list reference
     */
    public List<LangString> getDescriptions() {
        if (descriptions == null) {
            descriptions = new ArrayList<LangString>();
        }

        return this.descriptions;
    }


    /**
     * Returns this object's mediums list reference .
     *
     * @return          Medium list reference
     */
    public List<Medium> getMediums() {
        if (mediums == null) {
            mediums = new ArrayList<Medium>();
        }

        return this.mediums;
    }

}
