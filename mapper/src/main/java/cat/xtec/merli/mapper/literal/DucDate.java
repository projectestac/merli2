package cat.xtec.merli.mapper.literal;

import java.util.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


/**
 * Represents a literal whose type is {@code xsd:dateTime}.
 */
public final class DucDate extends DucLiteral<Date> {

    /**
     * Constructs a new boolean literal.
     *
     * @param value     Literal value
     */
    public DucDate(Date value) {
        super(value, Datatype.DATE_TIME.value());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLiteral() {
        return toString(value);
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


    /**
     * Converts a date object into a ISO-8601 string.
     *
     * @param value     Value to convert
     * @return          A string
     */
    private static String toString(Date value) {
        if (value instanceof Date == false) {
            return String.valueOf(value);
        }

        ZoneOffset zone = ZoneOffset.UTC;
        Instant instant = value.toInstant();
        LocalDateTime date = LocalDateTime.ofInstant(instant, zone);

        return String.valueOf(date);
    }

}
