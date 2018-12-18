package cat.xtec.merli.client.zonaclic.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import cat.xtec.merli.domain.voc.Language;


/**
 * This class maps the locale codes to language instances. Encapsulates
 * LOM domain languages for deserialization purposes.
 */
public final class Locale {

    /** Enumeration value */
    private Language language = null;


    /**
     * Object constructor
     *
     * @param value     Language code
     */
    @JsonCreator
    public Locale(String value) {
        try {
            language = Language.fromValue(value);
        } catch (IllegalArgumentException e) {}
    }


    /**
     * {@inheritDoc}
     */
    public Language language() {
        return language;
    }

}
