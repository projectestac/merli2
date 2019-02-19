package cat.xtec.merli.bind;


/**
 * DUC Ontology vocabulary
 */
public enum DucVocabulary {

    /* Predefined OWL vocabulary */

    /** Entity identifier */
    ABOUT(DucNamespace.RDF, "about"),

    /** The resource is a subclass of a class */
    PARENT(DucNamespace.RDFS, "subClassOf"),

    /** The resource is an instance of a class */
    TYPE(DucNamespace.RDF, "type"),

    /** Parent of all the classes */
    THING(DucNamespace.OWL, "Thing"),

    /** Subclass of all the classes */
    NOTHING(DucNamespace.OWL, "Nothing"),

    /** Empty data property */
    BOTTOM(DucNamespace.OWL, "BottomDataProperty"),

    /** The resource is a class */
    CLASS(DucNamespace.OWL, "Class"),

    /** The resource is a named individual */
    INDIVIDUAL(DucNamespace.OWL, "NamedIndividual"),

    /* Root concept classes */

    /** Root of the categories */
    DUC_CONTENT(DucNamespace.DUC, "Content"),

    /** Root of the vocabulary */
    DUC_CONCEPT(DucNamespace.DUC, "Concept"),

    /* Annotation properties */

    /** Description of the resource */
    COMMENT(DucNamespace.RDFS, "comment"),

    /** Human-readable name for the resource */
    LABEL(DucNamespace.RDFS, "label"),

    /** The nature or genre of the resource */
    KIND(DucNamespace.DCTERMS, "type"),

    /** Observations for the resource */
    OBSERVATION(DucNamespace.DUC, "observation"),

    /** References for the resource */
    REFERENCE(DucNamespace.DUC, "reference"),

    /** Type of curricular knowledge */
    KNOWLEDGE_TYPE(DucNamespace.DUC, "knowledgeType"),

    /* Datatype properties */

    /** Summary of the resource */
    ABSTRACT(DucNamespace.DCTERMS, "abstract"),

    /** Name given to the resource */
    TITLE(DucNamespace.DCTERMS, "title"),

    /** Entity responsible for a contribution */
    CONTRIBUTOR(DucNamespace.DCTERMS, "contributor"),

    /** A point of time */
    DATE(DucNamespace.DCTERMS, "date"),

    /** Description of the resource */
    DESCRIPTION(DucNamespace.DCTERMS, "description"),

    /** Physical characteristics of the resource */
    MEDIUM(DucNamespace.DCTERMS, "medium"),

    /** Media types of the resource */
    FORMAT(DucNamespace.DCTERMS, "format"),

    /** Language of the resource */
    RESOURCE_LANGUAGE(DucNamespace.DCTERMS, "language"),

    /** Comments on the conditions of use */
    CONDITIONS(DucNamespace.DCTERMS, "conditions"),

    /** Duration of the resource */
    DURATION(DucNamespace.DCTERMS, "extent"),

    /** Catalog entry for the resource */
    IDENTIFIER(DucNamespace.DCTERMS, "identifier"),

    /** Organizational structure of the resource */
    STRUCTURE(DucNamespace.LOM, "structure"),

    /** Edition of the resource */
    VERSION(DucNamespace.LOM, "version"),

    /** State or condition of the resource */
    STATUS(DucNamespace.LOM, "status"),

    /** URI of the resource */
    LOCATION(DucNamespace.LOM, "location"),

    /** Typical learning environment */
    CONTEXT(DucNamespace.LOM, "context"),

    /** How the resource is to be used */
    USAGE(DucNamespace.LOM, "usage"),

    /** Normal users of the resource */
    USER_ROLE(DucNamespace.LOM, "intendedEndUserRole"),

    /** Specific kinds of resources */
    LEARNING_TYPE(DucNamespace.LOM, "learningResourceType"),

    /** Whether the resource requires payment */
    COST(DucNamespace.LOM, "cost"),

    /** Whether copyright or other restrictions apply */
    COPYRIGHT(DucNamespace.LOM, "copyrightAndOtherRestrictions"),

    /** License of the content */
    LICENSE(DucNamespace.DUC, "license"),

    /** Natural language of the user */
    USER_LANGUAGE(DucNamespace.DUC, "userLanguage"),

    /* Object properties */

    /** A related vocabulary term */
    TERM_RELATION(DucNamespace.DUC, "termRelation"),

    /** A related learning object */
    LOM_RELATION(DucNamespace.DUC, "lomRelation"),

    /** A related resource */
    RELATION(DucNamespace.DCTERMS, "relation"),

    /** Topic of the resource */
    KEYWORD(DucNamespace.DCTERMS, "subject"),

    /** Resource from which a resource derives */
    SOURCE(DucNamespace.DCTERMS, "source"),

    /** Resource where this resource is included */
    IS_PART_OF(DucNamespace.DCTERMS, "isPartOf"),

    /** Resource that this resource includes */
    HAS_PART(DucNamespace.DCTERMS, "hasPart"),

    /** Resource that is adapted by this resource */
    IS_VERSION_OF(DucNamespace.DCTERMS, "isVersionOf"),

    /** Resource that adapts this resource */
    HAS_VERSION(DucNamespace.DCTERMS, "hasVersion"),

    /** Same resource in another format */
    IS_FORMAT_OF(DucNamespace.DCTERMS, "isFormatOf"),

    /** Same resource in another format */
    HAS_FORMAT(DucNamespace.DCTERMS, "hasFormat"),

    /** Resource that is pointed by this resource */
    REFERENCES(DucNamespace.DCTERMS, "references"),

    /** Resource that points to this resource */
    IS_REFERENCED_BY(DucNamespace.DCTERMS, "isReferencedBy"),

    /** Resource required to support its function */
    REQUIRES(DucNamespace.DCTERMS, "requires"),

    /** Requires this resource to support its function */
    IS_REQUIRED_BY(DucNamespace.DCTERMS, "isRequiredBy"),

    /** Resource that derives from this resource */
    IS_BASIS_FOR(DucNamespace.LOM, "isBasisFor"),

    /** Term that has a broader meaning */
    BROADER_TERM(DucNamespace.SKOS, "broader"),

    /** Term that has a narrower meaning */
    NARROWER_TERM(DucNamespace.SKOS, "narrower"),

    /** Related term */
    RELATED_TERM(DucNamespace.SKOS, "related"),

    /** Broadest class for the term */
    TOP_TERM(DucNamespace.SKOS, "hasTopConcept"),

    /** Preferred term  */
    USE(DucNamespace.DUC, "use"),

    /** Non-preferred synonym */
    USE_FOR(DucNamespace.DUC, "useFor");

    /** Enumeration namespace */
    private final DucNamespace ns;

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param ns        Namespace
     * @param path      Namespace path
     */
    DucVocabulary(DucNamespace ns, String path) {
        this.ns = ns;
        this.value = ns.value() + path;
    }


    /**
     * Returns this enumeration value.
     *
     * That is, the concatenation of the namespace and the path.
     * Thus, this method return the full IRI of a vocabulary.
     *
     * @return  String value
     */
    public String value() {
        return value;
    }


    /**
     * Returns this enumeration namespace.
     *
     * @return  Namespace value
     */
    public DucNamespace namespace() {
        return ns;
    }


    /**
     * Returns an enumeration object given a value.
     *
     * @param value      Enumeration value
     * @throws IllegalArgumentException
     */
    public static DucVocabulary fromValue(String value) {
        for (DucVocabulary object : DucVocabulary.values()) {
            if (value.equals(object.value))
                return object;
        }

        throw new IllegalArgumentException(value);
    }

}
