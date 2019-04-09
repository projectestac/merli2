package cat.xtec.merli.mapper.literal;

import org.semanticweb.owlapi.model.OWLDatatype;


/**
 * Superclass of all the numeric literal values.
 */
public abstract class DucNumber<T extends Number> extends DucLiteral<T> {

    /**
     * {@inheritDoc}
     */
    protected DucNumber(T literal, OWLDatatype datatype) {
        super(literal, datatype);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLiteral() {
        return this.value.toString();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean parseBoolean() {
        if (value.equals(1)) {
            return true;
        }

        if (value.equals(0)) {
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
        return value.doubleValue();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public float parseFloat() {
        return value.floatValue();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int parseInteger() {
        return value.intValue();
    }

}
