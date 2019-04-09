package cat.xtec.merli.mapper.literal;

import java.util.Locale;


/**
 * Represents a literal whose type is {@code xsd:string}.
 */
public final class DucString extends DucLiteral<String> {

    /**
     * Constructs a new string literal.
     *
     * @param value     Literal value
     */
    public DucString(String value) {
        super(value, Datatype.STRING.value());
    }


    /**
     * Constructs a new string literal tagged with the given locale.
     *
     * @param value     Literal value
     * @param language  Language tag
     */
    public DucString(String value, String language) {
        super(value, language, Datatype.LANG_STRING.value());
    }


    /**
     * Constructs a new string literal tagged with the given locale.
     *
     * @param value     Literal value
     * @param locale    Locale instance
     */
    public DucString(String value, Locale locale) {
        super(value, locale.toLanguageTag(), Datatype.LANG_STRING.value());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLiteral() {
        return this.value;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean parseBoolean() {
        if (value.equals("true")) {
            return true;
        }

        if (value.equals("false")) {
            return false;
        }

        if (value.equals("1")) {
            return true;
        }

        if (value.equals("0")) {
            return false;
        }

        throw new NumberFormatException(
            "Not a valid boolean value");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public double parseDouble() {
        return Double.parseDouble(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public float parseFloat() {
        return Float.parseFloat(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int parseInteger() {
        return Integer.parseInt(value);
    }

}
