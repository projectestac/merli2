package cat.xtec.merli.client.zonaclic.domain;

import com.fasterxml.jackson.annotation.JsonCreator;


/**
 * Project classification areas.
 */
public enum Area {

    /** Social sciences */
    SOCIAL_SCIENCES("soc"),

    /** Physical education */
    PHYSICAL_EDUCATION("ef"),

    /** Mathematics */
    MATHEMATICS("mat"),

    /** Music */
    MUSIC("mus"),

    /** Languages */
    LANGUAGES("lleng"),

    /** Design & technology */
    DESIGN_AND_TECHNOLOGY("tec"),

    /** Art & design */
    ART_AND_DESIGN("vip"),

    /** Experimental sciences */
    EXPERIMENTAL_SCIENCES("exp"),

    /** Miscellaneous */
    MISCELLANEOUS("div");

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Area(String value) {
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
    @JsonCreator
    public static Area fromValue(String value) {
        for (Area object : Area.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }

}
