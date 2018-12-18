package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;


/**
 * Technical data type of the learing object (LREv3.0). Definied as
 * a list of MIME data types.
 */
@XmlEnum
@XmlType(name = "format")
public enum Format implements EnumString {

    /**  */
    @XmlEnumValue("application/base64")
    APPLICATION_BASE64("application/base64"),

    /** Binary Software */
    @XmlEnumValue("application/binary")
    APPLICATION_BINARY("application/binary"),

    /** Java Application */
    @XmlEnumValue("application/java")
    APPLICATION_JAVA("application/java"),

    /**  */
    @XmlEnumValue("application/macbinhex40")
    APPLICATION_MACBINHEX40("application/macbinhex40"),

    /** Microsoft Excel */
    @XmlEnumValue("application/msexcel")
    APPLICATION_MSEXCEL("application/msexcel"),

    /** Microsoft Word */
    @XmlEnumValue("application/msword")
    APPLICATION_MSWORD("application/msword"),

    /** Ogg */
    @XmlEnumValue("application/ogg")
    APPLICATION_OGG("application/ogg"),

    /** Adobe Portable Document Format */
    @XmlEnumValue("application/pdf")
    APPLICATION_PDF("application/pdf"),

    /** PostScript */
    @XmlEnumValue("application/postscript")
    APPLICATION_POSTSCRIPT("application/postscript"),

    /**  */
    @XmlEnumValue("application/ppt")
    APPLICATION_PPT("application/ppt"),

    /** Rich Text Format (RTF) */
    @XmlEnumValue("application/rtf")
    APPLICATION_RTF("application/rtf"),

    /** Unix-to-Unix encoding */
    @XmlEnumValue("application/uue")
    APPLICATION_UUE("application/uue"),

    /**  */
    @XmlEnumValue("application/x-compressed")
    APPLICATION_COMPRESSED("application/x-compressed"),

    /**  */
    @XmlEnumValue("application/x-gzip-compressed")
    APPLICATION_GZIP_COMPRESSED("application/x-gzip-compressed"),

    /**  */
    @XmlEnumValue("application/x-pn-realmedia")
    APPLICATION_REALMEDIA("application/x-pn-realmedia"),

    /** Adobe Flash */
    @XmlEnumValue("application/x-shockwave-flash")
    APPLICATION_SHOCKWAVE_FLASH("application/x-shockwave-flash"),

    /** Stuffit Archive */
    @XmlEnumValue("application/x-stuffit")
    APPLICATION_STUFFIT("application/x-stuffit"),

    /**  */
    @XmlEnumValue("application/x-zip-compressed")
    APPLICATION_ZIP_COMPRESSED("application/x-zip-compressed"),

    /** Zip Archive */
    @XmlEnumValue("application/zip")
    APPLICATION_ZIP("application/zip"),

    /** Au file format */
    @XmlEnumValue("audio/basic")
    AUDIO_BASIC("audio/basic"),

    /** Musical Instrument Digital Interface */
    @XmlEnumValue("audio/midi")
    AUDIO_MIDI("audio/midi"),

    /** MPEG Audio Layer */
    @XmlEnumValue("audio/mp3")
    AUDIO_MP3("audio/mp3"),

    /** MPEG Audio */
    @XmlEnumValue("audio/mpeg")
    AUDIO_MPEG("audio/mpeg"),

    /** Waveform Audio Format */
    @XmlEnumValue("audio/wav")
    AUDIO_WAV("audio/wav"),

    /** Real Audio Sound */
    @XmlEnumValue("audio/x-pn-realaudio")
    AUDIO_REALAUDIO("audio/x-pn-realaudio"),

    /** Real Audio Sound */
    @XmlEnumValue("audio/x-pn-realaudio-plugin")
    AUDIO_REALAUDIO_PLUGIN("audio/x-pn-realaudio-plugin"),

    /** Bitmap Image File */
    @XmlEnumValue("image/bmp")
    IMAGE_BMP("image/bmp"),

    /** Graphics Interchange Format */
    @XmlEnumValue("image/gif")
    IMAGE_GIF("image/gif"),

    /** JPEG Image */
    @XmlEnumValue("image/jpeg")
    IMAGE_JPEG("image/jpeg"),

    /** Portable Network Graphics (PNG) */
    @XmlEnumValue("image/png")
    IMAGE_PNG("image/png"),

    /** Tagged Image File Format */
    @XmlEnumValue("image/tiff")
    IMAGE_TIFF("image/tiff"),

    /** Windows Metafile (WMF) */
    @XmlEnumValue("image/x-wmf")
    IMAGE_WMF("image/x-wmf"),

    /** Virtual Reality Modeling Language */
    @XmlEnumValue("model/vrml")
    MODEL_VRML("model/vrml"),

    /** HyperText Markup Language (HTML) */
    @XmlEnumValue("text/html")
    TEXT_HTML("text/html"),

    /** Text File */
    @XmlEnumValue("text/plain")
    TEXT_PLAIN("text/plain"),

    /** Rich Text Format (RTF) */
    @XmlEnumValue("text/richtext")
    TEXT_RICHTEXT("text/richtext"),

    /** Extensible Markup Language (XML) */
    @XmlEnumValue("text/xml")
    TEXT_XML("text/xml"),

    /** Audio Video Interleave */
    @XmlEnumValue("video/avi")
    VIDEO_AVI("video/avi"),

    /** MPEG Video */
    @XmlEnumValue("video/mpeg")
    VIDEO_MPEG("video/mpeg"),

    /** Quicktime Video */
    @XmlEnumValue("video/quicktime")
    VIDEO_QUICKTIME("video/quicktime"),

    /** RealMedia Video */
    @XmlEnumValue("video/x-pn-realvideo")
    VIDEO_REALVIDEO("video/x-pn-realvideo"),

    /** RealMedia Video Plugin */
    @XmlEnumValue("video/x-pn-realvideo-plugin")
    VIDEO_REALVIDEO_PLUGIN("video/x-pn-realvideo-plugin");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.LRE;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Format(String value) {
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
    public static Format fromValue(String value) {
        for (Format object : Format.values()) {
            if (value.equals(object.value()))
                return object;
        }

        throw new IllegalArgumentException(value);
    }

}
