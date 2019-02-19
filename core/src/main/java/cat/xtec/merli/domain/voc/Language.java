package cat.xtec.merli.domain.voc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import cat.xtec.merli.domain.EnumSource;
import cat.xtec.merli.domain.EnumString;
import cat.xtec.merli.bind.*;


/**
 * ISO 639-1 languages (LREv3.0).
 */
@XmlEnum
@XmlType(name = "Language")
public enum Language implements EnumString<Language> {

    /** Afar */
    @XmlEnumValue("aa")
    AFAR("aa"),

    /** Abkhazian */
    @XmlEnumValue("ab")
    ABKHAZIAN("ab"),

    /** Avestan */
    @XmlEnumValue("ae")
    AVESTAN("ae"),

    /** Afrikaans */
    @XmlEnumValue("af")
    AFRIKAANS("af"),

    /** Akan */
    @XmlEnumValue("ak")
    AKAN("ak"),

    /** Amharic */
    @XmlEnumValue("am")
    AMHARIC("am"),

    /** Aragonese */
    @XmlEnumValue("an")
    ARAGONESE("an"),

    /** Arabic */
    @XmlEnumValue("ar")
    ARABIC("ar"),

    /** Assamese */
    @XmlEnumValue("as")
    ASSAMESE("as"),

    /** Avaric */
    @XmlEnumValue("av")
    AVARIC("av"),

    /** Aymara */
    @XmlEnumValue("ay")
    AYMARA("ay"),

    /** Azerbaijani */
    @XmlEnumValue("az")
    AZERBAIJANI("az"),

    /** Bashkir */
    @XmlEnumValue("ba")
    BASHKIR("ba"),

    /** Belarusian */
    @XmlEnumValue("be")
    BELARUSIAN("be"),

    /** Bulgarian */
    @XmlEnumValue("bg")
    BULGARIAN("bg"),

    /** Bihari languages */
    @XmlEnumValue("bh")
    BIHARI_LANGUAGES("bh"),

    /** Bislama */
    @XmlEnumValue("bi")
    BISLAMA("bi"),

    /** Bambara */
    @XmlEnumValue("bm")
    BAMBARA("bm"),

    /** Bengali */
    @XmlEnumValue("bn")
    BENGALI("bn"),

    /** Tibetan */
    @XmlEnumValue("bo")
    TIBETAN("bo"),

    /** Breton */
    @XmlEnumValue("br")
    BRETON("br"),

    /** Bosnian */
    @XmlEnumValue("bs")
    BOSNIAN("bs"),

    /** Catalan; Valencian */
    @XmlEnumValue("ca")
    CATALAN("ca"),

    /** Chechen */
    @XmlEnumValue("ce")
    CHECHEN("ce"),

    /** Chamorro */
    @XmlEnumValue("ch")
    CHAMORRO("ch"),

    /** Corsican */
    @XmlEnumValue("co")
    CORSICAN("co"),

    /** Cree */
    @XmlEnumValue("cr")
    CREE("cr"),

    /** Czech */
    @XmlEnumValue("cs")
    CZECH("cs"),

    /** Church Slavic; Old Slavonic */
    @XmlEnumValue("cu")
    CHURCH_SLAVIC("cu"),

    /** Chuvash */
    @XmlEnumValue("cv")
    CHUVASH("cv"),

    /** Welsh */
    @XmlEnumValue("cy")
    WELSH("cy"),

    /** Danish */
    @XmlEnumValue("da")
    DANISH("da"),

    /** German */
    @XmlEnumValue("de")
    GERMAN("de"),

    /** Divehi; Dhivehi; Maldivian */
    @XmlEnumValue("dv")
    DIVEHI("dv"),

    /** Dzongkha */
    @XmlEnumValue("dz")
    DZONGKHA("dz"),

    /** Ewe */
    @XmlEnumValue("ee")
    EWE("ee"),

    /** Greek, Modern (1453-) */
    @XmlEnumValue("el")
    MODERN_GREEK("el"),

    /** English */
    @XmlEnumValue("en")
    ENGLISH("en"),

    /** Esperanto */
    @XmlEnumValue("eo")
    ESPERANTO("eo"),

    /** Spanish; Castilian */
    @XmlEnumValue("es")
    SPANISH("es"),

    /** Estonian */
    @XmlEnumValue("et")
    ESTONIAN("et"),

    /** Basque */
    @XmlEnumValue("eu")
    BASQUE("eu"),

    /** Persian */
    @XmlEnumValue("fa")
    PERSIAN("fa"),

    /** Fulah */
    @XmlEnumValue("ff")
    FULAH("ff"),

    /** Finnish */
    @XmlEnumValue("fi")
    FINNISH("fi"),

    /** Fijian */
    @XmlEnumValue("fj")
    FIJIAN("fj"),

    /** Faroese */
    @XmlEnumValue("fo")
    FAROESE("fo"),

    /** French */
    @XmlEnumValue("fr")
    FRENCH("fr"),

    /** Western Frisian */
    @XmlEnumValue("fy")
    WESTERN_FRISIAN("fy"),

    /** Irish */
    @XmlEnumValue("ga")
    IRISH("ga"),

    /** Gaelic; Scottish Gaelic */
    @XmlEnumValue("gd")
    GAELIC("gd"),

    /** Galician */
    @XmlEnumValue("gl")
    GALICIAN("gl"),

    /** Guarani */
    @XmlEnumValue("gn")
    GUARANI("gn"),

    /** Gujarati */
    @XmlEnumValue("gu")
    GUJARATI("gu"),

    /** Manx */
    @XmlEnumValue("gv")
    MANX("gv"),

    /** Hausa */
    @XmlEnumValue("ha")
    HAUSA("ha"),

    /** Hebrew */
    @XmlEnumValue("he")
    HEBREW("he"),

    /** Hindi */
    @XmlEnumValue("hi")
    HINDI("hi"),

    /** Hiri Motu */
    @XmlEnumValue("ho")
    HIRI_MOTU("ho"),

    /** Croatian */
    @XmlEnumValue("hr")
    CROATIAN("hr"),

    /** Haitian; Haitian Creole */
    @XmlEnumValue("ht")
    HAITIAN("ht"),

    /** Hungarian */
    @XmlEnumValue("hu")
    HUNGARIAN("hu"),

    /** Armenian */
    @XmlEnumValue("hy")
    ARMENIAN("hy"),

    /** Herero */
    @XmlEnumValue("hz")
    HERERO("hz"),

    /** Interlingua */
    @XmlEnumValue("ia")
    INTERLINGUA("ia"),

    /** Indonesian */
    @XmlEnumValue("id")
    INDONESIAN("id"),

    /** Interlingue; Occidental */
    @XmlEnumValue("ie")
    INTERLINGUE("ie"),

    /** Igbo */
    @XmlEnumValue("ig")
    IGBO("ig"),

    /** Sichuan Yi; Nuosu */
    @XmlEnumValue("ii")
    SICHUAN_YI("ii"),

    /** Inupiaq */
    @XmlEnumValue("ik")
    INUPIAQ("ik"),

    /** Ido */
    @XmlEnumValue("io")
    IDO("io"),

    /** Icelandic */
    @XmlEnumValue("is")
    ICELANDIC("is"),

    /** Italian */
    @XmlEnumValue("it")
    ITALIAN("it"),

    /** Inuktitut */
    @XmlEnumValue("iu")
    INUKTITUT("iu"),

    /** Japanese */
    @XmlEnumValue("ja")
    JAPANESE("ja"),

    /** Javanese */
    @XmlEnumValue("jv")
    JAVANESE("jv"),

    /** Georgian */
    @XmlEnumValue("ka")
    GEORGIAN("ka"),

    /** Kongo */
    @XmlEnumValue("kg")
    KONGO("kg"),

    /** Kikuyu; Gikuyu */
    @XmlEnumValue("ki")
    KIKUYU("ki"),

    /** Kuanyama; Kwanyama */
    @XmlEnumValue("kj")
    KUANYAMA("kj"),

    /** Kazakh */
    @XmlEnumValue("kk")
    KAZAKH("kk"),

    /** Kalaallisut; Greenlandic */
    @XmlEnumValue("kl")
    KALAALLISUT("kl"),

    /** Central Khmer */
    @XmlEnumValue("km")
    CENTRAL_KHMER("km"),

    /** Kannada */
    @XmlEnumValue("kn")
    KANNADA("kn"),

    /** Korean */
    @XmlEnumValue("ko")
    KOREAN("ko"),

    /** Kanuri */
    @XmlEnumValue("kr")
    KANURI("kr"),

    /** Kashmiri */
    @XmlEnumValue("ks")
    KASHMIRI("ks"),

    /** Kurdish */
    @XmlEnumValue("ku")
    KURDISH("ku"),

    /** Komi */
    @XmlEnumValue("kv")
    KOMI("kv"),

    /** Cornish */
    @XmlEnumValue("kw")
    CORNISH("kw"),

    /** Kirghiz; Kyrgyz */
    @XmlEnumValue("ky")
    KIRGHIZ("ky"),

    /** Latin */
    @XmlEnumValue("la")
    LATIN("la"),

    /** Luxembourgish; Letzeburgesch */
    @XmlEnumValue("lb")
    LUXEMBOURGISH("lb"),

    /** Ganda */
    @XmlEnumValue("lg")
    GANDA("lg"),

    /** Limburgan; Limburger; Limburgish */
    @XmlEnumValue("li")
    LIMBURGAN("li"),

    /** Lingala */
    @XmlEnumValue("ln")
    LINGALA("ln"),

    /** Lao */
    @XmlEnumValue("lo")
    LAO("lo"),

    /** Lithuanian */
    @XmlEnumValue("lt")
    LITHUANIAN("lt"),

    /** Luba-Katanga */
    @XmlEnumValue("lu")
    LUBA_KATANGA("lu"),

    /** Latvian */
    @XmlEnumValue("lv")
    LATVIAN("lv"),

    /** Malagasy */
    @XmlEnumValue("mg")
    MALAGASY("mg"),

    /** Marshallese */
    @XmlEnumValue("mh")
    MARSHALLESE("mh"),

    /** Maori */
    @XmlEnumValue("mi")
    MAORI("mi"),

    /** Macedonian */
    @XmlEnumValue("mk")
    MACEDONIAN("mk"),

    /** Malayalam */
    @XmlEnumValue("ml")
    MALAYALAM("ml"),

    /** Mongolian */
    @XmlEnumValue("mn")
    MONGOLIAN("mn"),

    /** Marathi */
    @XmlEnumValue("mr")
    MARATHI("mr"),

    /** Malay */
    @XmlEnumValue("ms")
    MALAY("ms"),

    /** Maltese */
    @XmlEnumValue("mt")
    MALTESE("mt"),

    /** Burmese */
    @XmlEnumValue("my")
    BURMESE("my"),

    /** Nauru */
    @XmlEnumValue("na")
    NAURU("na"),

    /** Bokmål, Norwegian; Norwegian Bokmål */
    @XmlEnumValue("nb")
    BOKMAL_NORWEGIAN("nb"),

    /** Ndebele, North; North Ndebele */
    @XmlEnumValue("nd")
    NORTH_NDEBELE("nd"),

    /** Nepali */
    @XmlEnumValue("ne")
    NEPALI("ne"),

    /** Ndonga */
    @XmlEnumValue("ng")
    NDONGA("ng"),

    /** Dutch; Flemish */
    @XmlEnumValue("nl")
    DUTCH("nl"),

    /** Norwegian Nynorsk; Nynorsk, Norwegian */
    @XmlEnumValue("nn")
    NYNORSK_NORWEGIAN("nn"),

    /** Norwegian */
    @XmlEnumValue("no")
    NORWEGIAN("no"),

    /** South Ndebele */
    @XmlEnumValue("nr")
    SOUTH_NDEBELE("nr"),

    /** Navajo; Navaho */
    @XmlEnumValue("nv")
    NAVAJO("nv"),

    /** Chichewa; Chewa; Nyanja */
    @XmlEnumValue("ny")
    CHICHEWA("ny"),

    /** Occitan */
    @XmlEnumValue("oc")
    OCCITAN("oc"),

    /** Ojibwa */
    @XmlEnumValue("oj")
    OJIBWA("oj"),

    /** Oromo */
    @XmlEnumValue("om")
    OROMO("om"),

    /** Oriya */
    @XmlEnumValue("or")
    ORIYA("or"),

    /** Ossetian; Ossetic */
    @XmlEnumValue("os")
    OSSETIAN("os"),

    /** Panjabi; Punjabi */
    @XmlEnumValue("pa")
    PANJABI("pa"),

    /** Pali */
    @XmlEnumValue("pi")
    PALI("pi"),

    /** Polish */
    @XmlEnumValue("pl")
    POLISH("pl"),

    /** Pushto; Pashto */
    @XmlEnumValue("ps")
    PUSHTO("ps"),

    /** Portuguese */
    @XmlEnumValue("pt")
    PORTUGUESE("pt"),

    /** Quechua */
    @XmlEnumValue("qu")
    QUECHUA("qu"),

    /** Romansh */
    @XmlEnumValue("rm")
    ROMANSH("rm"),

    /** Rundi */
    @XmlEnumValue("rn")
    RUNDI("rn"),

    /** Romanian; Moldavian; Moldovan */
    @XmlEnumValue("ro")
    ROMANIAN("ro"),

    /** Russian */
    @XmlEnumValue("ru")
    RUSSIAN("ru"),

    /** Kinyarwanda */
    @XmlEnumValue("rw")
    KINYARWANDA("rw"),

    /** Sanskrit */
    @XmlEnumValue("sa")
    SANSKRIT("sa"),

    /** Sardinian */
    @XmlEnumValue("sc")
    SARDINIAN("sc"),

    /** Sindhi */
    @XmlEnumValue("sd")
    SINDHI("sd"),

    /** Northern Sami */
    @XmlEnumValue("se")
    NORTHERN_SAMI("se"),

    /** Sango */
    @XmlEnumValue("sg")
    SANGO("sg"),

    /** Sinhala; Sinhalese */
    @XmlEnumValue("si")
    SINHALA("si"),

    /** Slovak */
    @XmlEnumValue("sk")
    SLOVAK("sk"),

    /** Slovenian */
    @XmlEnumValue("sl")
    SLOVENIAN("sl"),

    /** Samoan */
    @XmlEnumValue("sm")
    SAMOAN("sm"),

    /** Shona */
    @XmlEnumValue("sn")
    SHONA("sn"),

    /** Somali */
    @XmlEnumValue("so")
    SOMALI("so"),

    /** Albanian */
    @XmlEnumValue("sq")
    ALBANIAN("sq"),

    /** Serbian */
    @XmlEnumValue("sr")
    SERBIAN("sr"),

    /** Swati */
    @XmlEnumValue("ss")
    SWATI("ss"),

    /** Sotho, Southern */
    @XmlEnumValue("st")
    SOTHO("st"),

    /** Sundanese */
    @XmlEnumValue("su")
    SUNDANESE("su"),

    /** Swedish */
    @XmlEnumValue("sv")
    SWEDISH("sv"),

    /** Swahili */
    @XmlEnumValue("sw")
    SWAHILI("sw"),

    /** Tamil */
    @XmlEnumValue("ta")
    TAMIL("ta"),

    /** Telugu */
    @XmlEnumValue("te")
    TELUGU("te"),

    /** Tajik */
    @XmlEnumValue("tg")
    TAJIK("tg"),

    /** Thai */
    @XmlEnumValue("th")
    THAI("th"),

    /** Tigrinya */
    @XmlEnumValue("ti")
    TIGRINYA("ti"),

    /** Turkmen */
    @XmlEnumValue("tk")
    TURKMEN("tk"),

    /** Tagalog */
    @XmlEnumValue("tl")
    TAGALOG("tl"),

    /** Tswana */
    @XmlEnumValue("tn")
    TSWANA("tn"),

    /** Tonga (Tonga Islands) */
    @XmlEnumValue("to")
    TONGA("to"),

    /** Turkish */
    @XmlEnumValue("tr")
    TURKISH("tr"),

    /** Tsonga */
    @XmlEnumValue("ts")
    TSONGA("ts"),

    /** Tatar */
    @XmlEnumValue("tt")
    TATAR("tt"),

    /** Twi */
    @XmlEnumValue("tw")
    TWI("tw"),

    /** Tahitian */
    @XmlEnumValue("ty")
    TAHITIAN("ty"),

    /** Uighur; Uyghur */
    @XmlEnumValue("ug")
    UIGHUR("ug"),

    /** Ukrainian */
    @XmlEnumValue("uk")
    UKRAINIAN("uk"),

    /** Urdu */
    @XmlEnumValue("ur")
    URDU("ur"),

    /** Uzbek */
    @XmlEnumValue("uz")
    UZBEK("uz"),

    /** Venda */
    @XmlEnumValue("ve")
    VENDA("ve"),

    /** Vietnamese */
    @XmlEnumValue("vi")
    VIETNAMESE("vi"),

    /** Volapük */
    @XmlEnumValue("vo")
    VOLAPUK("vo"),

    /** Walloon */
    @XmlEnumValue("wa")
    WALLOON("wa"),

    /** Wolof */
    @XmlEnumValue("wo")
    WOLOF("wo"),

    /** Xhosa */
    @XmlEnumValue("xh")
    XHOSA("xh"),

    /** Yiddish */
    @XmlEnumValue("yi")
    YIDDISH("yi"),

    /** Yoruba */
    @XmlEnumValue("yo")
    YORUBA("yo"),

    /** Zhuang; Chuang */
    @XmlEnumValue("za")
    ZHUANG("za"),

    /** Chinese */
    @XmlEnumValue("zh")
    CHINESE("zh"),

    /** Zulu */
    @XmlEnumValue("zu")
    ZULU("zu");

    /** Source of the vocabulary */
    private final EnumSource source = EnumSource.LRE;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Language(String value) {
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
    @DucString
    public String value() {
        return value;
    }


    /**
     * {@inheritDoc}
     */
    @DucCreator()
    public static Language fromValue(String value) {
        return EnumString.from(Language.class, value);
    }

}
