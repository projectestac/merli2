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

    /* Types for 'EntityType' */

    @DefaultStringValue("Root concept")
    String ROOT_CONCEPT();

    /* Types for 'CategoryType' */

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

    /* Types for 'context' */

    @DefaultStringValue("Higher education")
    String HIGHER_EDUCATION();

    @DefaultStringValue("Other")
    String OTHER();

    @DefaultStringValue("School")
    String SCHOOL();

    @DefaultStringValue("Training")
    String TRAINING();

    /* Types for 'contributorRole' */

    @DefaultStringValue("Author")
    String AUTHOR();

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

    /* Types for 'Cost' */

    @DefaultStringValue("Requires payment")
    String REQUIRES_PAYMENT();

    @DefaultStringValue("Free of charge")
    String WITHOUT_CHARGE();

    /* Types for 'Copyright' */

    @DefaultStringValue("Creative Commons: Attribution (CC BY)")
    String CC_BY();

    @DefaultStringValue("Creative Commons: Attribution-ShareAlike (CC BY-SA)")
    String CC_BY_SA();

    @DefaultStringValue("Creative Commons: Attribution-NoDerivatives (CC BY-ND)")
    String CC_BY_ND();

    @DefaultStringValue("Creative Commons: Attribution-NonCommercial (CC BY-NC)")
    String CC_BY_NC();

    @DefaultStringValue("Creative Commons: Attribution-NonCommercial-ShareAlike (CC BY-NC-SA)")
    String CC_BY_NC_SA();

    @DefaultStringValue("Creative Commons: Attribution-NonCommercial-NoDerivatives (CC BY-NC-ND)")
    String CC_BY_NC_ND();

    @DefaultStringValue("Edu3")
    String EDU3();

    @DefaultStringValue("Other copyright licenses")
    String COPYRIGHT();

    @DefaultStringValue("Public domain")
    String PUBLIC_DOMAIN();

    /* Types for 'Knowledge' */

    @DefaultStringValue("All types")
    String EITHER();

    @DefaultStringValue("Attitudinal")
    String ATTITUDINAL();

    @DefaultStringValue("Conceptual")
    String CONCEPTUAL();

    @DefaultStringValue("Procedural")
    String PROCEDURAL();

    /* Types for 'kind' */

    @DefaultStringValue("Is part of")
    String IS_PART_OF();

    @DefaultStringValue("Has part")
    String HAS_PART();

    @DefaultStringValue("Is version of")
    String IS_VERSION_OF();

    @DefaultStringValue("Has version")
    String HAS_VERSION();

    @DefaultStringValue("Is format of")
    String IS_FORMAT_OF();

    @DefaultStringValue("Has format")
    String HAS_FORMAT();

    @DefaultStringValue("References")
    String REFERENCES();

    @DefaultStringValue("Is referenced by")
    String IS_REFERENCED_BY();

    @DefaultStringValue("Is based on")
    String IS_BASED_ON();

    @DefaultStringValue("Is basis for")
    String IS_BASIS_FOR();

    @DefaultStringValue("Requires")
    String REQUIRES();

    @DefaultStringValue("Is required by")
    String IS_REQUIRED_BY();

    /* Types for 'TermType' */

    @DefaultStringValue("Facet indicator")
    String FACET_INDICATOR();

    @DefaultStringValue("Preferred term")
    String PREFERRED_TERM();

    @DefaultStringValue("Non-preferred term")
    String NON_PREFERRED_TERM();

    /* Types for 'learningResourceType' */

    @DefaultStringValue("Exercise")
    String EXERCISE();

    @DefaultStringValue("Simulation")
    String SIMULATION();

    @DefaultStringValue("Questionnaire")
    String QUESTIONNAIRE();

    @DefaultStringValue("Diagram")
    String DIAGRAM();

    @DefaultStringValue("Figure")
    String FIGURE();

    @DefaultStringValue("Graph")
    String GRAPH();

    @DefaultStringValue("Index")
    String INDEX();

    @DefaultStringValue("Lecture")
    String LECTURE();

    @DefaultStringValue("Slide")
    String SLIDE();

    @DefaultStringValue("Table")
    String TABLE();

    @DefaultStringValue("Narrative text")
    String NARRATIVE_TEXT();

    @DefaultStringValue("Exam")
    String EXAM();

    @DefaultStringValue("Experiment")
    String EXPERIMENT();

    @DefaultStringValue("Problem statement")
    String PROBLEM_STATEMENT();

    @DefaultStringValue("Self assessment")
    String SELF_ASSESMENT();

    /* Types for 'status' */

    @DefaultStringValue("Draft")
    String DRAFT();

    @DefaultStringValue("Final")
    String FINAL();

    @DefaultStringValue("Revised")
    String REVISED();

    @DefaultStringValue("Unavailable")
    String UNAVAILABLE();

    /* Types for 'structure' */

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

    /* Types for 'termRelation' */

    @DefaultStringValue("Use")
    String USE();

    @DefaultStringValue("Use for")
    String USE_FOR();

    @DefaultStringValue("Top term")
    String TOP_TERM();

    @DefaultStringValue("Broader term")
    String BROADER_TERM();

    @DefaultStringValue("Narrower term")
    String NARROWER_TERM();

    @DefaultStringValue("Related term")
    String RELATED_TERM();

    /* Types for 'UserRole' */

    @DefaultStringValue("Learner")
    String LEARNER();

    @DefaultStringValue("Manager")
    String MANAGER();

    @DefaultStringValue("Teacher")
    String TEACHER();

    /* Identifier catalogs */

    @DefaultStringValue("ISBN")
    String ISBN();

    @DefaultStringValue("ISSN")
    String ISSN();

    @DefaultStringValue("Legal deposit")
    String LEGAL_DEPOSIT();

    @DefaultStringValue("Another catalog")
    String OTHER_CATALOG();

    /* Language names */

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
