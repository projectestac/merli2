package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

// import cat.xtec.merli.xml.EnumAdapter;
import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;
import cat.xtec.merli.bind.*;


/**
 * Specific kind of resource (LREv3.0). This is only a subset of the
 * defined LRE Learning Resource Types.
 */
@XmlEnum
public enum ResourceType implements EnumString {

    /** The primary purpose is the evaluation of the learner */
    @XmlEnumValue("assessment")
    ASSESSMENT("assessment"),

    /** Exercises involving short sequences of practice */
    @XmlEnumValue("drill and practice")
    DRILL_AND_PRACTICE("drill and practice"),

    /** Encourages learners to explore and investigate */
    @XmlEnumValue("exploration")
    EXPLORATION("exploration"),

    /** Collection of specialised terms and their meanings */
    @XmlEnumValue("glossary")
    GLOSSARY("glossary"),

    /** Provides guidance on a particular topic */
    @XmlEnumValue("guide")
    GUIDE("guide"),

    /** Material for artistic projects and creative exercises */
    @XmlEnumValue("open activity")
    OPEN_ACTIVITY("open activity"),

    /** Editors or programs for producing something */
    @XmlEnumValue("tool")
    TOOL("tool"),

    /** A collection of web pages */
    @XmlEnumValue("web page")
    WEB_PAGE("web page");

    /** Enumeration value */
    private final String value;

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.LRE;


    /**
     * Enumeration constructor
     *
     * @param value     Type value
     */
    ResourceType(String value) {
        this.value = value;
    }


    /**
     * {@inheritDoc}
     */
    public EnumSource source() {
        return source;
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
    public static ResourceType fromValue(String value) {
        return EnumString.from(ResourceType.class, value);
    }


    /** Vocabulary XML adapter for this enumeration */
    // public static class Adapter extends EnumAdapter<ResourceType> {
    //     public Adapter() { super(ResourceType.class); }
    // }

}
