package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 * Physical medium of the resource (DUCv3.0).
 *
 * This vocabulary is an atempt to align the physical formats used
 * by Merlí (2.0) with those defined by LRE.
 */
@XmlEnum
@XmlType(name = "medium")
public enum Medium implements EnumString {

    /* LRE Learning Resource Types (learning asset) */

    /** Recorded sound */
    @XmlEnumValue("audio")
    AUDIO("audio"),

    /** Visual, factual, or numerical information */
    @XmlEnumValue("data")
    DATA("data"),

    /** Still image or graphical representation */
    @XmlEnumValue("image")
    IMAGE("image"),

    /** A unit of writing */
    @XmlEnumValue("text")
    TEXT("text"),

    /** Any moving image including animation */
    @XmlEnumValue("video")
    VIDEO("video"),

    /* LRE Learning Resource Types (learning resource) */

    /** Computer software */
    @XmlEnumValue("application")
    APPLICATION("application"),

    /** Resource that is both fun and educational */
    @XmlEnumValue("educational game")
    EDUCATIONAL_GAME("educational game"),

    /** Book or book component */
    @XmlEnumValue("textbook")
    TEXTBOOK("textbook"),

    /** Information delivered by an instructor */
    @XmlEnumValue("presentation")
    PRESENTATION("presentation"),

    /** Resource available online */
    @XmlEnumValue("social media")
    SOCIAL_MEDIA("social media"),

    /* Custom types */

    /** Information about a product or service */
    @XmlEnumValue("brochure")
    BROCHURE("brochure"),

    /** Collection of documents about a subject */
    @XmlEnumValue("dossier")
    DOSSIER("dossier"),

    /** Objects or tools used for learning */
    @XmlEnumValue("equipment")
    EQUIPMENT("equipment"),

    /** Book accompanied by an interactive content */
    @XmlEnumValue("interactive textbook")
    INTERACTIVE_TEXTBOOK("interactive textbook"),

    /** A periodical publication */
    @XmlEnumValue("magazine")
    MAGAZINE("magazine"),

    /** Diagrammatic representation */
    @XmlEnumValue("map")
    MAP("map"),

    /** Recorded music sound */
    @XmlEnumValue("musical audio")
    MUSICAL_AUDIO("musical audio"),

    /** A picture made using a camera */
    @XmlEnumValue("photograph")
    PHOTOGRAPH("photograph"),

    /** Resource designed for printing */
    @XmlEnumValue("printable")
    PRINTABLE("printable"),

    /** A physical unit of writing */
    @XmlEnumValue("text document")
    TEXT_DOCUMENT("text document"),

    /** Any visual material */
    @XmlEnumValue("visual media")
    VISUAL_MEDIA("visual media");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.DUC;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Medium(String value) {
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
    public static Medium fromValue(String value) {
        return EnumString.from(Medium.class, value);
    }

}
