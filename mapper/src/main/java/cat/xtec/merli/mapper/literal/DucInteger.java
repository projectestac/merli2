package cat.xtec.merli.mapper.literal;


/**
 * Represents a literal whose type is {@code xsd:integer}.
 */
public final class DucInteger extends DucNumber<Integer> {

    /**
     * Constructs a new integer literal.
     *
     * @param value     Literal value
     */
    public DucInteger(int value) {
        super(value, Datatype.INTEGER.value());
    }

}
