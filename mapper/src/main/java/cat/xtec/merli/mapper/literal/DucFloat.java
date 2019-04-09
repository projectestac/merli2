package cat.xtec.merli.mapper.literal;


/**
 * Represents a literal whose type is {@code xsd:float}.
 */
public final class DucFloat extends DucNumber<Float> {

    /**
     * Constructs a new float literal.
     *
     * @param value     Literal value
     */
    public DucFloat(float value) {
        super(value, Datatype.FLOAT.value());
    }

}
