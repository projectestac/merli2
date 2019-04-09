package cat.xtec.merli.mapper.util;

import java.net.URI;
import java.util.Date;
import java.lang.reflect.Method;

import cat.xtec.merli.mapper.literal.*;


/**
 * This factory creates literals from objects.
 */
public final class Literals {

    /** Supported types */
    private static final Class[] TYPES = {
        Boolean.class,
        Float.class,
        Integer.class,
        Double.class,
        Date.class,
        URI.class
    };


    /** Prevent the instantiation */
    private Literals() {}


    /**
     * Constructs a new literal for a boolean value.
     *
     * @param value     Boolean value
     * @return          New literal
     */
    public static DucBoolean from(Boolean value) {
        return new DucBoolean(value);
    }


    /**
     * Constructs a new literal for a float value.
     *
     * @param value     Float value
     * @return          New literal
     */
    public static DucFloat from(Float value) {
        return new DucFloat(value);
    }


    /**
     * Constructs a new literal for an integer value.
     *
     * @param value     Integer value
     * @return          New literal
     */
    public static DucInteger from(Integer value) {
        return new DucInteger(value);
    }


    /**
     * Constructs a new literal for a double value.
     *
     * @param value     Double value
     * @return          New literal
     */
    public static DucDouble from(Double value) {
        return new DucDouble(value);
    }


    /**
     * Constructs a new literal for a date value.
     *
     * @param value     Date object
     * @return          New literal
     */
    public static DucDate from(Date value) {
        return new DucDate(value);
    }


    /**
     * Constructs a new literal for an URI value.
     *
     * @param value     URI object
     * @return          New literal
     */
    public static DucURI from(URI value) {
        return new DucURI(value);
    }


    /**
     * Constructs a new literal for a string value.
     *
     * @param value     String object
     * @return          New literal
     */
    public static DucString from(String value) {
        return new DucString(value);
    }


    /**
     * Constructs a new string literal with a locale tag.
     *
     * @param value     Object instance
     * @param tag       Language tag
     * @return          New literal
     */
    public static DucString from(String value, String tag) {
        return new DucString(value, tag);
    }


    /**
     * Constructs a new string literal with a locale tag.
     *
     * @param value     An object
     * @param tag       Language tag
     * @return          New literal
     */
    public static DucString from(Object value, String tag) {
        return new DucString(String.valueOf(value), tag);
    }


    /**
     * Constructs a new string literal with a locale tag.
     *
     * @param value     An object
     * @param tag       Language tag
     * @return          New literal
     */
    public static DucString from(Object value, Object tag) {
        return new DucString(String.valueOf(value), String.valueOf(tag));
    }


    /**
     * Constructs a new literal for the given object.
     *
     * @param value     An object
     * @return          New literal
     */
    public static DucLiteral<?> from(Object value) {
        try {
            if (value instanceof Object) {
                Class<?> ctype = value.getClass();

                for (Class<?> type : TYPES) {
                    if (type.isAssignableFrom(ctype)) {
                        return from(value, type);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return from(String.valueOf(value));
    }


    /**
     * Invokes a factory method for the given type on a value.
     *
     * @param value     An object
     * @param type      Factory type
     * @return          New literal
     */
    private static DucLiteral<?> from(Object value, Class<?> type) throws Exception {
        Method method = Literals.class.getMethod("from", type);
        return (DucLiteral<?>) method.invoke(null, value);
    }

}
