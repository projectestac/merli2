package cat.xtec.merli.client.zonaclic.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import cat.xtec.merli.domain.voc.Context;


/**
 * This enumeration maps project levels to the defined educational
 * contexts of the LOM domain.
 */
public enum Level {

    /** Kindergarten (3-6) */
    KINDERGARTEN("INF", Context.PRESCHOOL),

    /** Primary school (6-12) */
    PRIMARY_SCHOOL("PRI", Context.COMPULSORY_EDUCATION),

    /** Secondary school (12-16) */
    SECONDARY_SCHOOL("SEC", Context.COMPULSORY_EDUCATION),

    /** High school (16-18) */
    HIGH_SCHOOL("BTX", Context.HIGHER_EDUCATION);

    /** Enumeration value */
    private final String value;

    /** Educational context */
    private final Context context;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Level(String value, Context context) {
        this.value = value;
        this.context = context;
    }


    /**
     * Returns the educational context.
     *
     * @return      Educational context
     */
    public Context context() {
        return context;
    }


    /**
     * {@inheritDoc}
     */
    public String value() {
        return value;
    }


    /**
     * {@inheritDoc}
     */
    @JsonCreator
    public static Level fromValue(String value) {
        for (Level object : Level.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }

}
