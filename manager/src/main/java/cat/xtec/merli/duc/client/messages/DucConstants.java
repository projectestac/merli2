package cat.xtec.merli.duc.client.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.i18n.client.Constants.DefaultStringValue;
import com.google.gwt.i18n.client.LocalizableResource.DefaultLocale;


/**
 * Translatable enumeration constants.
 *
 * {@inheritDoc}
 */
@DefaultLocale("en")
public interface DucConstants extends ConstantsWithLookup {

    /** Instance of this interface */
    static final DucConstants CONSTANTS = GWT.create(DucConstants.class);


    /**
     * Returns the value for the given method. If no method exists with
     * the given name, the method name is returned as-is.
     *
     * @param name      Method name
     * @return          String value
     */
    public static String getText(String name) {
        try {
            return CONSTANTS.getString(name);
        } catch (Exception e) {
            return name;
        }
    }


    /**
     * Returns the value for the given enumeration constant. The
     * enumeration name is used as the look-up method name.
     *
     * @param value     Enumeration constant
     * @return          String value
     */
    public static String getText(Enum value) {
        return DucConstants.getText(value.name());
    }


    /* Custom messages */

    @DefaultStringValue("Choose an option")
    String CHOOSE_AN_OPTION();

    /* Normal user of a learning object (LOMv1.0) */

    @DefaultStringValue("Author")
    String AUTHOR();

    @DefaultStringValue("Learner")
    String LEARNER();

    @DefaultStringValue("Manager")
    String MANAGER();

    @DefaultStringValue("Teacher")
    String TEACHER();

    /* Content knowledge type (DUCv3.0) */

    @DefaultStringValue("Unspecified")
    String UNSPECIFIED();

    @DefaultStringValue("Attitudinal")
    String ATTITUDINAL();

    @DefaultStringValue("Conceptual")
    String CONCEPTUAL();

    @DefaultStringValue("Procedural")
    String PROCEDURAL();

    /* Whether copyright or other restrictions apply (LOMv1.0) */

    @DefaultStringValue("Has restrictions")
    String HAS_RESTRICTIONS();

    @DefaultStringValue("Without restrictions")
    String WITHOUT_RESTRICTIONS();

    /* Content licenses (DUCv3.0) */

    @DefaultStringValue("Creative Commons: Attribution (CC BY)")
    String CREATIVE_COMMONS_BY();

    @DefaultStringValue("Creative Commons: Attribution-ShareAlike (CC BY-SA)")
    String CREATIVE_COMMONS_BYSA();

    @DefaultStringValue("Creative Commons: Attribution-NoDerivatives (CC BY-ND)")
    String CREATIVE_COMMONS_BYND();

    @DefaultStringValue("Creative Commons: Attribution-NonCommercial (CC BY-NC)")
    String CREATIVE_COMMONS_BYNC();

    @DefaultStringValue("Creative Commons: Attribution-NonCommercial-ShareAlike (CC BY-NC-SA)")
    String CREATIVE_COMMONS_BYNCSA();

    @DefaultStringValue("Creative Commons: Attribution-NonCommercial-NoDerivatives (CC BY-NC-ND)")
    String CREATIVE_COMMONS_BYNCND();

    @DefaultStringValue("Edu3 educational license")
    String EDU3_EDUCATIONAL();

    @DefaultStringValue("Other copyright licenses")
    String OTHER_COPYRIGHT();

    @DefaultStringValue("Public domain")
    String PUBLIC_DOMAIN();

    /* Lifecycle contributor roles (LOMv1.0) */

    @DefaultStringValue("Content provider")
    String CONTENT_PROVIDER();

    @DefaultStringValue("Creator")
    String CREATOR();

    @DefaultStringValue("Editor")
    String EDITOR();

    @DefaultStringValue("Educational validator")
    String EDUCATIONAL_VALIDATOR();

    @DefaultStringValue("Graphical designer")
    String GRAPHICAL_DESIGNER();

    @DefaultStringValue("Initiator")
    String INITIATOR();

    @DefaultStringValue("Instructional designer")
    String INSTRUCTIONAL_DESIGNER();

    @DefaultStringValue("Publisher")
    String PUBLISHER();

    @DefaultStringValue("Script writer")
    String SCRIPT_WRITER();

    @DefaultStringValue("Subject matter expert")
    String SUBJECT_MATTER_EXPERT();

    @DefaultStringValue("Technical implementer")
    String TECHNICAL_IMPLEMENTER();

    @DefaultStringValue("Technical validator")
    String TECHNICAL_VALIDATOR();

    @DefaultStringValue("Terminator")
    String TERMINATOR();

    @DefaultStringValue("Unknown")
    String UNKNOWN();

    @DefaultStringValue("Validator")
    String VALIDATOR();

    /* The state or condition of a learning object (LOMv1.0) */

    @DefaultStringValue("Draft")
    String DRAFT();

    @DefaultStringValue("Final")
    String FINAL();

    @DefaultStringValue("Revised")
    String REVISED();

    @DefaultStringValue("Unavailable")
    String UNAVAILABLE();

    /* Physical medium of the resource (DUCv3.0) */

    @DefaultStringValue("Audio")
    String AUDIO();

    @DefaultStringValue("Data")
    String DATA();

    @DefaultStringValue("Image")
    String IMAGE();

    @DefaultStringValue("Text")
    String TEXT();

    @DefaultStringValue("Video")
    String VIDEO();

    @DefaultStringValue("Application")
    String APPLICATION();

    @DefaultStringValue("Educational game")
    String EDUCATIONAL_GAME();

    @DefaultStringValue("Textbook")
    String TEXTBOOK();

    @DefaultStringValue("Presentation")
    String PRESENTATION();

    @DefaultStringValue("Social media")
    String SOCIAL_MEDIA();

    @DefaultStringValue("Brochure")
    String BROCHURE();

    @DefaultStringValue("Dossier")
    String DOSSIER();

    @DefaultStringValue("Equipment")
    String EQUIPMENT();

    @DefaultStringValue("Interactive textbook")
    String INTERACTIVE_TEXTBOOK();

    @DefaultStringValue("Magazine")
    String MAGAZINE();

    @DefaultStringValue("Map")
    String MAP();

    @DefaultStringValue("Musical audio")
    String MUSICAL_AUDIO();

    @DefaultStringValue("Photograph")
    String PHOTOGRAPH();

    @DefaultStringValue("Printable")
    String PRINTABLE();

    @DefaultStringValue("Text document")
    String TEXT_DOCUMENT();

    @DefaultStringValue("Visual media")
    String VISUAL_MEDIA();

    @DefaultStringValue("Requires payment")
    String REQUIRES_PAYMENT();

    @DefaultStringValue("Free of charge")
    String WITHOUT_CHARGE();

    @DefaultStringValue("International Standard Book Number (ISBN)")
    String ISBN();

    @DefaultStringValue("International Standard Serial Number (ISSN)")
    String ISSN();

    @DefaultStringValue("Legal deposit (NBN)")
    String LEGAL_DEPOSIT();

    @DefaultStringValue("Internal reference")
    String MERLI();

    @DefaultStringValue("Another catalog")
    String OTHER_CATALOG();

    @DefaultStringValue("Atomic")
    String ATOMIC();

    @DefaultStringValue("Collection")
    String COLLECTION();

    @DefaultStringValue("Hierarchical")
    String HIERARCHICAL();

    @DefaultStringValue("Linear")
    String LINEAR();

    @DefaultStringValue("Networked")
    String NETWORKED();

    @DefaultStringValue("Compulsory education")
    String COMPULSORY_EDUCATION();

    @DefaultStringValue("Continuing education")
    String CONTINUING_EDUCATION();

    @DefaultStringValue("Distance education")
    String DISTANCE_EDUCATION();

    @DefaultStringValue("Educational administration")
    String EDUCATIONAL_ADMINISTRATION();

    @DefaultStringValue("Higher education")
    String HIGHER_EDUCATION();

    @DefaultStringValue("Library")
    String LIBRARY();

    @DefaultStringValue("Policy making")
    String POLICY_MAKING();

    @DefaultStringValue("Pre-school")
    String PRESCHOOL();

    @DefaultStringValue("Professional development")
    String PROFESSIONAL_DEVELOPMENT();

    @DefaultStringValue("Special education")
    String SPECIAL_EDUCATION();

    @DefaultStringValue("Vocational education")
    String VOCATIONAL_EDUCATION();

    @DefaultStringValue("Other")
    String OTHER();

    @DefaultStringValue("Accessibility restrictions")
    String ACCESSIBILITY_RESTRICTIONS();

    @DefaultStringValue("Competency")
    String COMPETENCY();

    @DefaultStringValue("Discipline")
    String DISCIPLINE();

    @DefaultStringValue("Educational level")
    String EDUCATIONAL_LEVEL();

    @DefaultStringValue("Educational objective")
    String EDUCATIONAL_OBJECTIVE();

    @DefaultStringValue("Idea")
    String IDEA();

    @DefaultStringValue("Prerequisite")
    String PREREQUISITE();

    @DefaultStringValue("Security level")
    String SECURITY_LEVEL();

    @DefaultStringValue("Skill level")
    String SKILL_LEVEL();

    @DefaultStringValue("Educational context")
    String EDUCATIONAL_CONTEXT();

    @DefaultStringValue("Subject descriptor")
    String SUBJECT_DESCRIPTOR();

    @DefaultStringValue("Assessment")
    String ASSESSMENT();

    @DefaultStringValue("Drill and practice")
    String DRILL_AND_PRACTICE();

    @DefaultStringValue("Exploration")
    String EXPLORATION();

    @DefaultStringValue("Glossary")
    String GLOSSARY();

    @DefaultStringValue("Guide")
    String GUIDE();

    @DefaultStringValue("Open activity")
    String OPEN_ACTIVITY();

    @DefaultStringValue("Tool")
    String TOOL();

    @DefaultStringValue("Web page")
    String WEB_PAGE();

    @DefaultStringValue("Entity")
    String ENTITY();

    @DefaultStringValue("Term")
    String TERM();

    @DefaultStringValue("Resource")
    String RESOURCE();

    @DefaultStringValue("Top term")
    String TOP_TERM();

    @DefaultStringValue("Broader term")
    String BROADER_TERM();

    @DefaultStringValue("Narrower term")
    String NARROWER_TERM();

    @DefaultStringValue("Related term")
    String RELATED_TERM();

    @DefaultStringValue("Use")
    String USE();

    @DefaultStringValue("Use for")
    String USE_FOR();

    @DefaultStringValue("Requires")
    String REQUIRES();

    @DefaultStringValue("References")
    String REFERENCES();

    @DefaultStringValue("Has part")
    String HAS_PART();

    @DefaultStringValue("Has version")
    String HAS_VERSION();

    @DefaultStringValue("Has format")
    String HAS_FORMAT();

    @DefaultStringValue("Is required by")
    String IS_REQUIRED_BY();

    @DefaultStringValue("Is referenced by")
    String IS_REFERENCED_BY();

    @DefaultStringValue("Is part of")
    String IS_PART_OF();

    @DefaultStringValue("Is version of")
    String IS_VERSION_OF();

    @DefaultStringValue("Is format of")
    String IS_FORMAT_OF();

    @DefaultStringValue("Is based on")
    String IS_BASED_ON();

    @DefaultStringValue("Is basis for")
    String IS_BASIS_FOR();

    /* Entity types (DUCv3.0) */

    @DefaultStringValue("Root concept")
    String ROOT_CONCEPT();

    @DefaultStringValue("Contenct category")
    String CONTENT_CATEGORY();

    @DefaultStringValue("Vocabulary term")
    String VOCABULARY_TERM();

    @DefaultStringValue("Free term")
    String FREE_TERM();

    @DefaultStringValue("Learning object")
    String LEARNING_OBJECT();

    @DefaultStringValue("Education phase")
    String EDUCATION_PHASE();

    @DefaultStringValue("Education stage")
    String EDUCATION_STAGE();

    @DefaultStringValue("Academic year")
    String ACADEMIC_YEAR();

    @DefaultStringValue("Field of study")
    String FIELD_OF_STUDY();

    @DefaultStringValue("Programme of study")
    String PROGRAMME_OF_STUDY();

    @DefaultStringValue("Topic of study")
    String TOPIC_OF_STUDY();

    @DefaultStringValue("Facet indicator")
    String FACET_INDICATOR();

    @DefaultStringValue("Preferred term")
    String PREFERRED_TERM();

    @DefaultStringValue("Non-preferred term")
    String NON_PREFERRED_TERM();

    /* Technical data type of the learing object (LREv3.0) */

    @DefaultStringValue("Application/base64")
    String APPLICATION_BASE64();

    @DefaultStringValue("Binary Software")
    String APPLICATION_BINARY();

    @DefaultStringValue("Java Application")
    String APPLICATION_JAVA();

    @DefaultStringValue("Application/macbinhex40")
    String APPLICATION_MACBINHEX40();

    @DefaultStringValue("Microsoft Excel")
    String APPLICATION_MSEXCEL();

    @DefaultStringValue("Microsoft Word")
    String APPLICATION_MSWORD();

    @DefaultStringValue("Ogg")
    String APPLICATION_OGG();

    @DefaultStringValue("Adobe Portable Document Format (PDF)")
    String APPLICATION_PDF();

    @DefaultStringValue("PostScript")
    String APPLICATION_POSTSCRIPT();

    @DefaultStringValue("Application/ppt")
    String APPLICATION_PPT();

    @DefaultStringValue("Rich Text Format (RTF)")
    String APPLICATION_RTF();

    @DefaultStringValue("Unix-to-Unix encoding")
    String APPLICATION_UUE();

    @DefaultStringValue("Application/x-compressed")
    String APPLICATION_COMPRESSED();

    @DefaultStringValue("Application/x-gzip-compressed")
    String APPLICATION_GZIP_COMPRESSED();

    @DefaultStringValue("Application/x-pn-realmedia")
    String APPLICATION_REALMEDIA();

    @DefaultStringValue("Adobe Flash")
    String APPLICATION_SHOCKWAVE_FLASH();

    @DefaultStringValue("Stuffit Archive")
    String APPLICATION_STUFFIT();

    @DefaultStringValue("Application/x-zip-compressed")
    String APPLICATION_ZIP_COMPRESSED();

    @DefaultStringValue("Zip Archive")
    String APPLICATION_ZIP();

    @DefaultStringValue("Au file format")
    String AUDIO_BASIC();

    @DefaultStringValue("Musical Instrument Digital Interface (MIDI)")
    String AUDIO_MIDI();

    @DefaultStringValue("MPEG Audio Layer (MP3)")
    String AUDIO_MP3();

    @DefaultStringValue("MPEG Audio")
    String AUDIO_MPEG();

    @DefaultStringValue("Waveform Audio Format (WAV)")
    String AUDIO_WAV();

    @DefaultStringValue("Real Audio Sound")
    String AUDIO_REALAUDIO();

    @DefaultStringValue("Real Audio Sound Plugin")
    String AUDIO_REALAUDIO_PLUGIN();

    @DefaultStringValue("Bitmap Image File (BMP)")
    String IMAGE_BMP();

    @DefaultStringValue("Graphics Interchange Format (GIF)")
    String IMAGE_GIF();

    @DefaultStringValue("JPEG Image (JPG)")
    String IMAGE_JPEG();

    @DefaultStringValue("Portable Network Graphics (PNG)")
    String IMAGE_PNG();

    @DefaultStringValue("Tagged Image File Format (TIFF)")
    String IMAGE_TIFF();

    @DefaultStringValue("Windows Metafile (WMF)")
    String IMAGE_WMF();

    @DefaultStringValue("Virtual Reality Modeling Language (VRML)")
    String MODEL_VRML();

    @DefaultStringValue("HyperText Markup Language (HTML)")
    String TEXT_HTML();

    @DefaultStringValue("Text File")
    String TEXT_PLAIN();

    @DefaultStringValue("Rich Text Format (RTF)")
    String TEXT_RICHTEXT();

    @DefaultStringValue("Extensible Markup Language (XML)")
    String TEXT_XML();

    @DefaultStringValue("Audio Video Interleave (AVI)")
    String VIDEO_AVI();

    @DefaultStringValue("MPEG Video")
    String VIDEO_MPEG();

    @DefaultStringValue("Quicktime Video")
    String VIDEO_QUICKTIME();

    @DefaultStringValue("RealMedia Video")
    String VIDEO_REALVIDEO();

    @DefaultStringValue("RealMedia Video Plugin")
    String VIDEO_REALVIDEO_PLUGIN();

    /* ISO 639-1 languages (LREv3.0) */

    @DefaultStringValue("Afar")
    String AFAR();

    @DefaultStringValue("Abkhazian")
    String ABKHAZIAN();

    @DefaultStringValue("Avestan")
    String AVESTAN();

    @DefaultStringValue("Afrikaans")
    String AFRIKAANS();

    @DefaultStringValue("Akan")
    String AKAN();

    @DefaultStringValue("Amharic")
    String AMHARIC();

    @DefaultStringValue("Aragonese")
    String ARAGONESE();

    @DefaultStringValue("Arabic")
    String ARABIC();

    @DefaultStringValue("Assamese")
    String ASSAMESE();

    @DefaultStringValue("Avaric")
    String AVARIC();

    @DefaultStringValue("Aymara")
    String AYMARA();

    @DefaultStringValue("Azerbaijani")
    String AZERBAIJANI();

    @DefaultStringValue("Bashkir")
    String BASHKIR();

    @DefaultStringValue("Belarusian")
    String BELARUSIAN();

    @DefaultStringValue("Bulgarian")
    String BULGARIAN();

    @DefaultStringValue("Bihari languages")
    String BIHARI_LANGUAGES();

    @DefaultStringValue("Bislama")
    String BISLAMA();

    @DefaultStringValue("Bambara")
    String BAMBARA();

    @DefaultStringValue("Bengali")
    String BENGALI();

    @DefaultStringValue("Tibetan")
    String TIBETAN();

    @DefaultStringValue("Breton")
    String BRETON();

    @DefaultStringValue("Bosnian")
    String BOSNIAN();

    @DefaultStringValue("Catalan; Valencian")
    String CATALAN();

    @DefaultStringValue("Chechen")
    String CHECHEN();

    @DefaultStringValue("Chamorro")
    String CHAMORRO();

    @DefaultStringValue("Corsican")
    String CORSICAN();

    @DefaultStringValue("Cree")
    String CREE();

    @DefaultStringValue("Czech")
    String CZECH();

    @DefaultStringValue("Church Slavonic; Old Bulgarian")
    String CHURCH_SLAVIC();

    @DefaultStringValue("Chuvash")
    String CHUVASH();

    @DefaultStringValue("Welsh")
    String WELSH();

    @DefaultStringValue("Danish")
    String DANISH();

    @DefaultStringValue("German")
    String GERMAN();

    @DefaultStringValue("Divehi; Dhivehi; Maldivian")
    String DIVEHI();

    @DefaultStringValue("Dzongkha")
    String DZONGKHA();

    @DefaultStringValue("Ewe")
    String EWE();

    @DefaultStringValue("Greek, Modern (1453-)")
    String MODERN_GREEK();

    @DefaultStringValue("English")
    String ENGLISH();

    @DefaultStringValue("Esperanto")
    String ESPERANTO();

    @DefaultStringValue("Spanish; Castilian")
    String SPANISH();

    @DefaultStringValue("Estonian")
    String ESTONIAN();

    @DefaultStringValue("Basque")
    String BASQUE();

    @DefaultStringValue("Persian")
    String PERSIAN();

    @DefaultStringValue("Fulah")
    String FULAH();

    @DefaultStringValue("Finnish")
    String FINNISH();

    @DefaultStringValue("Fijian")
    String FIJIAN();

    @DefaultStringValue("Faroese")
    String FAROESE();

    @DefaultStringValue("French")
    String FRENCH();

    @DefaultStringValue("Western Frisian")
    String WESTERN_FRISIAN();

    @DefaultStringValue("Irish")
    String IRISH();

    @DefaultStringValue("Gaelic; Scottish Gaelic")
    String GAELIC();

    @DefaultStringValue("Galician")
    String GALICIAN();

    @DefaultStringValue("Guarani")
    String GUARANI();

    @DefaultStringValue("Gujarati")
    String GUJARATI();

    @DefaultStringValue("Manx")
    String MANX();

    @DefaultStringValue("Hausa")
    String HAUSA();

    @DefaultStringValue("Hebrew")
    String HEBREW();

    @DefaultStringValue("Hindi")
    String HINDI();

    @DefaultStringValue("Hiri Motu")
    String HIRI_MOTU();

    @DefaultStringValue("Croatian")
    String CROATIAN();

    @DefaultStringValue("Haitian; Haitian Creole")
    String HAITIAN();

    @DefaultStringValue("Hungarian")
    String HUNGARIAN();

    @DefaultStringValue("Armenian")
    String ARMENIAN();

    @DefaultStringValue("Herero")
    String HERERO();

    @DefaultStringValue("Interlingua")
    String INTERLINGUA();

    @DefaultStringValue("Indonesian")
    String INDONESIAN();

    @DefaultStringValue("Interlingue; Occidental")
    String INTERLINGUE();

    @DefaultStringValue("Igbo")
    String IGBO();

    @DefaultStringValue("Sichuan Yi; Nuosu")
    String SICHUAN_YI();

    @DefaultStringValue("Inupiaq")
    String INUPIAQ();

    @DefaultStringValue("Ido")
    String IDO();

    @DefaultStringValue("Icelandic")
    String ICELANDIC();

    @DefaultStringValue("Italian")
    String ITALIAN();

    @DefaultStringValue("Inuktitut")
    String INUKTITUT();

    @DefaultStringValue("Japanese")
    String JAPANESE();

    @DefaultStringValue("Javanese")
    String JAVANESE();

    @DefaultStringValue("Georgian")
    String GEORGIAN();

    @DefaultStringValue("Kongo")
    String KONGO();

    @DefaultStringValue("Kikuyu; Gikuyu")
    String KIKUYU();

    @DefaultStringValue("Kuanyama; Kwanyama")
    String KUANYAMA();

    @DefaultStringValue("Kazakh")
    String KAZAKH();

    @DefaultStringValue("Kalaallisut; Greenlandic")
    String KALAALLISUT();

    @DefaultStringValue("Central Khmer")
    String CENTRAL_KHMER();

    @DefaultStringValue("Kannada")
    String KANNADA();

    @DefaultStringValue("Korean")
    String KOREAN();

    @DefaultStringValue("Kanuri")
    String KANURI();

    @DefaultStringValue("Kashmiri")
    String KASHMIRI();

    @DefaultStringValue("Kurdish")
    String KURDISH();

    @DefaultStringValue("Komi")
    String KOMI();

    @DefaultStringValue("Cornish")
    String CORNISH();

    @DefaultStringValue("Kirghiz; Kyrgyz")
    String KIRGHIZ();

    @DefaultStringValue("Latin")
    String LATIN();

    @DefaultStringValue("Luxembourgish; Letzeburgesch")
    String LUXEMBOURGISH();

    @DefaultStringValue("Ganda")
    String GANDA();

    @DefaultStringValue("Limburgan; Limburger; Limburgish")
    String LIMBURGAN();

    @DefaultStringValue("Lingala")
    String LINGALA();

    @DefaultStringValue("Lao")
    String LAO();

    @DefaultStringValue("Lithuanian")
    String LITHUANIAN();

    @DefaultStringValue("Luba-Katanga")
    String LUBA_KATANGA();

    @DefaultStringValue("Latvian")
    String LATVIAN();

    @DefaultStringValue("Malagasy")
    String MALAGASY();

    @DefaultStringValue("Marshallese")
    String MARSHALLESE();

    @DefaultStringValue("Maori")
    String MAORI();

    @DefaultStringValue("Macedonian")
    String MACEDONIAN();

    @DefaultStringValue("Malayalam")
    String MALAYALAM();

    @DefaultStringValue("Mongolian")
    String MONGOLIAN();

    @DefaultStringValue("Marathi")
    String MARATHI();

    @DefaultStringValue("Malay")
    String MALAY();

    @DefaultStringValue("Maltese")
    String MALTESE();

    @DefaultStringValue("Burmese")
    String BURMESE();

    @DefaultStringValue("Nauru")
    String NAURU();

    @DefaultStringValue("Bokmål, Norwegian; Norwegian Bokmål")
    String BOKMAL_NORWEGIAN();

    @DefaultStringValue("Ndebele, North; North Ndebele")
    String NORTH_NDEBELE();

    @DefaultStringValue("Nepali")
    String NEPALI();

    @DefaultStringValue("Ndonga")
    String NDONGA();

    @DefaultStringValue("Dutch; Flemish")
    String DUTCH();

    @DefaultStringValue("Norwegian Nynorsk; Nynorsk, Norwegian")
    String NYNORSK_NORWEGIAN();

    @DefaultStringValue("Norwegian")
    String NORWEGIAN();

    @DefaultStringValue("South Ndebele")
    String SOUTH_NDEBELE();

    @DefaultStringValue("Navajo; Navaho")
    String NAVAJO();

    @DefaultStringValue("Chichewa; Chewa; Nyanja")
    String CHICHEWA();

    @DefaultStringValue("Occitan")
    String OCCITAN();

    @DefaultStringValue("Ojibwa")
    String OJIBWA();

    @DefaultStringValue("Oromo")
    String OROMO();

    @DefaultStringValue("Oriya")
    String ORIYA();

    @DefaultStringValue("Ossetian; Ossetic")
    String OSSETIAN();

    @DefaultStringValue("Panjabi; Punjabi")
    String PANJABI();

    @DefaultStringValue("Pali")
    String PALI();

    @DefaultStringValue("Polish")
    String POLISH();

    @DefaultStringValue("Pushto; Pashto")
    String PUSHTO();

    @DefaultStringValue("Portuguese")
    String PORTUGUESE();

    @DefaultStringValue("Quechua")
    String QUECHUA();

    @DefaultStringValue("Romansh")
    String ROMANSH();

    @DefaultStringValue("Rundi")
    String RUNDI();

    @DefaultStringValue("Romanian; Moldavian; Moldovan")
    String ROMANIAN();

    @DefaultStringValue("Russian")
    String RUSSIAN();

    @DefaultStringValue("Kinyarwanda")
    String KINYARWANDA();

    @DefaultStringValue("Sanskrit")
    String SANSKRIT();

    @DefaultStringValue("Sardinian")
    String SARDINIAN();

    @DefaultStringValue("Sindhi")
    String SINDHI();

    @DefaultStringValue("Northern Sami")
    String NORTHERN_SAMI();

    @DefaultStringValue("Sango")
    String SANGO();

    @DefaultStringValue("Sinhala; Sinhalese")
    String SINHALA();

    @DefaultStringValue("Slovak")
    String SLOVAK();

    @DefaultStringValue("Slovenian")
    String SLOVENIAN();

    @DefaultStringValue("Samoan")
    String SAMOAN();

    @DefaultStringValue("Shona")
    String SHONA();

    @DefaultStringValue("Somali")
    String SOMALI();

    @DefaultStringValue("Albanian")
    String ALBANIAN();

    @DefaultStringValue("Serbian")
    String SERBIAN();

    @DefaultStringValue("Swati")
    String SWATI();

    @DefaultStringValue("Sotho, Southern")
    String SOTHO();

    @DefaultStringValue("Sundanese")
    String SUNDANESE();

    @DefaultStringValue("Swedish")
    String SWEDISH();

    @DefaultStringValue("Swahili")
    String SWAHILI();

    @DefaultStringValue("Tamil")
    String TAMIL();

    @DefaultStringValue("Telugu")
    String TELUGU();

    @DefaultStringValue("Tajik")
    String TAJIK();

    @DefaultStringValue("Thai")
    String THAI();

    @DefaultStringValue("Tigrinya")
    String TIGRINYA();

    @DefaultStringValue("Turkmen")
    String TURKMEN();

    @DefaultStringValue("Tagalog")
    String TAGALOG();

    @DefaultStringValue("Tswana")
    String TSWANA();

    @DefaultStringValue("Tonga (Tonga Islands)")
    String TONGA();

    @DefaultStringValue("Turkish")
    String TURKISH();

    @DefaultStringValue("Tsonga")
    String TSONGA();

    @DefaultStringValue("Tatar")
    String TATAR();

    @DefaultStringValue("Twi")
    String TWI();

    @DefaultStringValue("Tahitian")
    String TAHITIAN();

    @DefaultStringValue("Uighur; Uyghur")
    String UIGHUR();

    @DefaultStringValue("Ukrainian")
    String UKRAINIAN();

    @DefaultStringValue("Urdu")
    String URDU();

    @DefaultStringValue("Uzbek")
    String UZBEK();

    @DefaultStringValue("Venda")
    String VENDA();

    @DefaultStringValue("Vietnamese")
    String VIETNAMESE();

    @DefaultStringValue("Volapük")
    String VOLAPUK();

    @DefaultStringValue("Walloon")
    String WALLOON();

    @DefaultStringValue("Wolof")
    String WOLOF();

    @DefaultStringValue("Xhosa")
    String XHOSA();

    @DefaultStringValue("Yiddish")
    String YIDDISH();

    @DefaultStringValue("Yoruba")
    String YORUBA();

    @DefaultStringValue("Zhuang; Chuang")
    String ZHUANG();

    @DefaultStringValue("Chinese")
    String CHINESE();

    @DefaultStringValue("Zulu")
    String ZULU();

}
