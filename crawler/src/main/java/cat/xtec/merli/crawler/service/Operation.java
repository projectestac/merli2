package cat.xtec.merli.crawler.service;


/**
 * Available operations for the Merlí 2.1 service.
 */
public enum Operation {

    /** Create a resource */
    CREATE("addResource"),

    /** Obtain a resource */
    RETRIEVE("getResource"),

    /** Delete a resource */
    REMOVE("delResource"),

    /** Update a resource */
    UPDATE("setResource");

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Operation(String value) {
        this.value = value;
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
    public static Operation fromValue(String value) {
        for (Operation object : Operation.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }

}
