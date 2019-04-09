package cat.xtec.merli.mapper.literal;


/**
 * Represents a literal whose type is {@code xsd:boolean}.
 */
public final class DucBoolean extends DucLiteral<Boolean> {

    /**
     * Constructs a new boolean literal.
     *
     * @param value     Literal value
     */
    public DucBoolean(boolean value) {
        super(value, Datatype.BOOLEAN.value());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLiteral() {
        return value.toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean parseBoolean() {
        return value;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public double parseDouble() {
        return parseInteger();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public float parseFloat() {
        return parseInteger();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int parseInteger() {
        return value ? 1 : 0;
    }

}
