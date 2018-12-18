package cat.xtec.merli.domain.xml;

import java.lang.reflect.Field;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import cat.xtec.merli.domain.EnumString;
import cat.xtec.merli.domain.type.Constant;


/**
 * Vocabulary XML adapter for an enumeration string.
 *
 * This JAXB adapter marshals an {@code EnumString} value along with
 * its source as specified in the LOM 1.0 standard. This facilitates
 * the use of controlled vocabularies from diferent sources. It also
 * ensures that the source matches the vocabulary during the unmarshal
 * operations.
 *
 * For example, the constant {@code Cost.REQUIRES_PAYMENT} will be
 * marshaled to and unmarshaled from the following XML:
 *
 * &lt;cost&gt;
 *   &lt;source&gtLOMv1.0&lt;/source&gt;
 *   &lt;value&gtyes&lt;/value&gt;
 * &lt;/cost&gt;
 */
public class EnumAdapter<E extends EnumString>
    extends XmlAdapter<Constant, E> {

    /** Specific enumeration class */
    private Class<E> type;


    /**
     * Creates a new adapter for an EnumString.
     *
     * @param type          EnumString class
     */
    public EnumAdapter(Class<E> type) {
        super();
        this.type = type;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Constant marshal(E value) throws Exception {
        return (value == null) ? null : new Constant(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public E unmarshal(Constant value) throws Exception {
        return (value == null) ? null : toEnumString(value);
    }


    /**
     * Returns the XML value for an enumeration constant.
     *
     * @param value             Enumeration value
     * @return                  XmlEnumValue object
     */
    private XmlEnumValue getEnumValue(E value) throws NoSuchFieldException {
        return getEnumField(value).getAnnotation(XmlEnumValue.class);
    }


    /**
     * Returns the class field for an enumeration constant.
     *
     * @param value             Enumeration constant
     * @return                  Enumeration field
     */
    private Field getEnumField(E value) throws NoSuchFieldException {
        return value.getClass().getField(value.name());
    }


    /**
     * Converts an encapsulated constant to an EnumString value.
     *
     * This method finds an enumeration constant for the defined type
     * that matches the source and the value found on the XML. Returns
     * {@code null} if none match.
     *
     * @param constant          Encapsulated constant
     * @return                  Enumeration value
     */
    private E toEnumString(Constant constant) throws Exception {
        for (E object : type.getEnumConstants()) {
            String value = getEnumValue(object).value();

            if (object.source().equals(constant.getSource())) {
                if (value.equals(constant.getValue())) {
                    return object;
                }
            }
        }

        return null;
    }

}
