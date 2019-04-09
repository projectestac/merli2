package cat.xtec.merli.mapper.literal;

import java.net.URI;


/**
 * Represents a literal whose type is {@code xsd:anyURI}.
 */
public final class DucURI extends DucLiteral<URI> {

    /**
     * Constructs a new URI literal.
     *
     * @param value     Literal value
     */
    public DucURI(URI value) {
        super(value, Datatype.URI.value());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLiteral() {
        return String.valueOf(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean parseBoolean() {
        throw new NumberFormatException(
            "Not a valid boolean value");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public double parseDouble() {
        throw new NumberFormatException(
            "Not a valid double value");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public float parseFloat() {
        throw new NumberFormatException(
            "Not a valid float value");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int parseInteger() {
        throw new NumberFormatException(
            "Not a valid integer value");
    }

}
