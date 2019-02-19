package cat.xtec.merli.domain.type;

import java.util.Objects;
import java.io.Serializable;
import javax.xml.bind.annotation.*;

import cat.xtec.merli.domain.voc.Language;
import cat.xtec.merli.bind.*;


/**
 * A string tagged with a language. By default the string is tagged
 * with {@code Language.ENGLISH}.
 */
@XmlType(name = "string")
@XmlAccessorType(XmlAccessType.NONE)
public final class LangString implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Default language tag */
    public static final Language DEFAULT_LANGUAGE = Language.ENGLISH;

    /** Empty text value */
    public static final String EMPTY_TEXT = new String();

    /** Language tag */
    @XmlAttribute(name = "language")
    protected Language language;

    /** String text */
    @XmlValue()
    protected String text;


    /**
     * Constructs a new empty LangString.
     */
    public LangString() {
        this(EMPTY_TEXT, DEFAULT_LANGUAGE);
    }


    /**
     * Constructs a new LangString.
     */
    public LangString(String text) {
        this(text, DEFAULT_LANGUAGE);
    }


    /**
     * Constructs a tagged LangString.
     *
     * @param text        Text value
     * @param language    Language tag
     */
    public LangString(String text, Language language) {
        this.setText(text);
        this.setLanguage(language);
    }


    /**
     * Returns a new LangString for the given value and tag.
     *
     * @param text        Text value
     * @return            Language string
     *
     * @throws IllegalArgumentException   If tag is not a
     *      valid language identifier
     */
    @DucCreator()
    public static LangString valueOf(String text, String tag) {
        Language language = Language.fromValue(tag);
        return new LangString(text, language);
    }


    /**
     * Returns a copy of the given value.
     *
     * @param string      Language string
     * @return            A new language string
     *
     * @throws NullPointerException
     */
    public static LangString valueOf(LangString string) {
        return new LangString(string.text, string.language);
    }


    /**
     * Returns a new LangString for the given value.
     *
     * @param value       Text value
     * @return            Untagged language string
     */
    public static LangString valueOf(String text) {
        return new LangString(text);
    }


    /**
     * Returns if the text on this string is empty. The text is considered
     * empty if its length is zero.
     *
     * @return            If the text is empty
     */
    public boolean isEmpty() {
        return text.isEmpty();
    }


    /**
     * Returns the language tag.
     *
     * @return            Language tag
     */
    public Language getLanguage() {
        return language;
    }


    /**
     * Returns the locale code of this string.
     *
     * @see               Language#value
     * @return            Locale code
     */
    @DucLocale
    public String getLocaleCode() {
        return language.value();
    }


    /**
     * Returns the text value.
     *
     * @return            Text value
     */
    @DucString
    public String getText() {
        return text;
    }


    /**
     * Sets the language tag value.
     *
     * @param language     Language tag
     */
    public void setLanguage(Language language) {
        this.language = (language != null) ? language : DEFAULT_LANGUAGE;
    }


    /**
     * Sets the text value.
     *
     * @param value       Text value
     */
    public void setText(String text) {
        this.text = (text != null) ? text : EMPTY_TEXT;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof LangString) {
            LangString s = (LangString) o;

            return (
                language.equals(s.language) &&
                text.equals(s.text)
            );
        }

        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(text, language);
    }


    /**
     * {@inheritDoc}.
     */
    @Override
    public String toString() {
        return quote(text) + "@" + getLocaleCode();
    }


    /**
     * Adds surrounding quotes to the given string.
     *
     * @param value     String instance
     * @return          Quoted string
     */
    private String quote(String value) {
        return "\"" + value.replaceAll("\"", "\\\"") + "\"";
    }

}
