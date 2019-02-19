package cat.xtec.merli.domain.lom;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

import cat.xtec.merli.domain.Namespace;
import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.domain.voc.Copyright;
import cat.xtec.merli.domain.voc.Cost;
import cat.xtec.merli.domain.voc.License;
import cat.xtec.merli.bind.*;
import cat.xtec.merli.xml.*;


/**
 * Conditions of use of the resource
 */
@DucContainer()
@XmlType(name = "rights")
@XmlAccessorType(XmlAccessType.NONE)
public class RightsDetails implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Whether use of the resource requires payment */
    @DucAttribute(DucVocabulary.COST)
    @XmlElement(name = "cost")
    @XmlJavaTypeAdapter(CostAdapter.class)
    protected Cost cost;

    /** Whether copyright or other restrictions apply */
    @DucAttribute(DucVocabulary.COPYRIGHT)
    @XmlElement(name = "copyrightAndOtherRestrictions")
    @XmlJavaTypeAdapter(CopyrightAdapter.class)
    protected Copyright copyright;

    /** License of the content */
    @DucAttribute(DucVocabulary.LICENSE)
    @XmlElement(name = "license", namespace = Namespace.DUC)
    protected License license;

    /** Comments on the conditions of use */
    @DucAnnotation(DucVocabulary.CONDITIONS)
    @XmlElement(name = "string")
    @XmlElementWrapper(name = "description")
    protected List<LangString> descriptions;


    /**
     * Returns this object's cost value.
     *
     * @return          Cost value
     */
    public Cost getCost() {
        return cost;
    }


    /**
     * Sets this object's cost value.
     *
     * @param value     Cost value
     */
    public void setCost(Cost value) {
        this.cost = value;
    }


    /**
     * Returns this object's copyright value.
     *
     * @return          Copyright value
     */
    public Copyright getCopyright() {
        return copyright;
    }


    /**
     * Sets this object's copyright value.
     *
     * @param value     Copyright value
     */
    public void setCopyright(Copyright value) {
        this.copyright = value;
    }


    /**
     * Returns this object's license value.
     *
     * @return          License value
     */
    public License getLicense() {
        return license;
    }


    /**
     * Sets this object's license value.
     *
     * @param value     License value
     */
    public void setLicense(License value) {
        this.license = value;
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

}
