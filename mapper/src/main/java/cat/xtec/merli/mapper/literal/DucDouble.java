package cat.xtec.merli.mapper.literal;


/**
 * Represents a literal whose type is {@code xsd:double}.
 */
public final class DucDouble extends DucNumber<Double> {

    /**
     * Constructs a new double literal.
     *
     * @param value     Literal value
     */
    public DucDouble(double value) {
        super(value, Datatype.DOUBLE.value());
    }

}
