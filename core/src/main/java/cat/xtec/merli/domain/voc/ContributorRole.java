package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

// import cat.xtec.merli.xml.EnumAdapter;
import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 * Lifecycle contributor roles (LOMv1.0).
 */
@XmlEnum
@XmlType(name = "contributorRole")
public enum ContributorRole implements EnumString {

    /** Who is responsible for making the content */
    @XmlEnumValue("author")
    AUTHOR("author"),

    /** Who is supplying the content */
    @XmlEnumValue("content provider")
    CONTENT_PROVIDER("content provider"),

    /** Who is responsible for making the metadata */
    @XmlEnumValue("creator")
    CREATOR("creator"),

    /** Who is responsible of a revision */
    @XmlEnumValue("editor")
    EDITOR("editor"),

    /** Who confirms the educational integrity */
    @XmlEnumValue("educational validator")
    EDUCATIONAL_VALIDATOR("educational validator"),

    /** Who constructed the visual elements */
    @XmlEnumValue("graphical designer")
    GRAPHICAL_DESIGNER("graphical designer"),

    /** Who initiated the development process */
    @XmlEnumValue("initiator")
    INITIATOR("initiator"),

    /** Who applyed research-based principles to the design */
    @XmlEnumValue("instructional designer")
    INSTRUCTIONAL_DESIGNER("instructional designer"),

    /** Who made the content available */
    @XmlEnumValue("publisher")
    PUBLISHER("publisher"),

    /** Who created the text or audio contents */
    @XmlEnumValue("script writer")
    SCRIPT_WRITER("script writer"),

    /** Who is the expert in the domain */
    @XmlEnumValue("subject matter expert")
    SUBJECT_MATTER_EXPERT("subject matter expert"),

    /** Who created the technical elements */
    @XmlEnumValue("technical implementer")
    TECHNICAL_IMPLEMENTER("technical implementer"),

    /** Who confirmed the technical integrity */
    @XmlEnumValue("technical validator")
    TECHNICAL_VALIDATOR("technical validator"),

    /** Who removed access to the resource */
    @XmlEnumValue("terminator")
    TERMINATOR("terminator"),

    /** The role as contributor is not known */
    @XmlEnumValue("unknown")
    UNKNOWN("unknown"),

    /** Who confirmed the overall integrity */
    @XmlEnumValue("validator")
    VALIDATOR("validator");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.LOM;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    ContributorRole(String value) {
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
    public static ContributorRole fromValue(String value) {
        return EnumString.from(ContributorRole.class, value);
    }


    /** Vocabulary XML adapter for this enumeration */
    // public static class Adapter extends EnumAdapter<ContributorRole> {
    //     public Adapter() { super(ContributorRole.class); }
    // }

}
