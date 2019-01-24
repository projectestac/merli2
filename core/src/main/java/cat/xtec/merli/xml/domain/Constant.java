package cat.xtec.merli.xml.domain;

import javax.xml.bind.annotation.*;

import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 * Encapulates an {@code EnumString} constant in a way that can
 * be marshaled by JAXB.
 *
 * This type is not intended to be used directly, but through the
 * enumeration adapter {@code EnumAdapter}. Each enumeration on the
 * {@code voc} package has an adapter that extends this class and
 * can be used with the {@code @XmlJavaTypeAdapter} annotation.
 *
 * Note that JAXB does not support marshaling interfaces and Java
 * does not support extending enums; because of that, we need to
 * store the enumeration constant as a string that can be later
 * unmarshaled knowing the enum type where it belongs.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Constant {

    /** Vocabulary value */
    @XmlElement(name = "value")
    protected String value;

    /** Source of the value */
    @XmlElement(name = "source")
    protected EnumSource source;

    /** Empty constructor */
    public Constant() {}


    /**
      * Encapsulates an {@code EnumString}.
      *
      * @param constant      Enumeration value
      */
    public Constant(EnumString constant) {
        this.value = constant.value();
        this.source = constant.source();
    }


    /**
     * Returns this object's value.
     *
     * @return          Value
     */
    public String getValue() {
        return value;
    }


    /**
     * Sets this object's value.
     *
     * @param value     Value
     */
    public void setValue(String value) {
        this.value = value;
    }


    /**
     * Returns this object's source value.
     *
     * @return          Source value
     */
    public EnumSource getSource() {
        return source;
    }


    /**
     * Sets this object's source value.
     *
     * @param value     Source value
     */
    public void setSource(EnumSource source) {
        this.source = source;
    }

}
