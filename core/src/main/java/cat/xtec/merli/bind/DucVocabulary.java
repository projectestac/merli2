package cat.xtec.merli.bind;


/**
 * DUC Ontology vocabulary
 */
public enum DucVocabulary {

    /* Root concept classes */

    /** Root of the categories */
    DUC_CONTENT(DucNamespace.DUC, "Content"),

    /** Root of the vocabulary */
    DUC_CONCEPT(DucNamespace.DUC, "Concept"),

    /* Annotation properties */

    /** Alternative term identifier */
    XTHES_ID(DucNamespace.XTHES, "altIdentifier"),

    /** Description of the resource */
    COMMENT(DucNamespace.RDFS, "comment"),

    /** Human-readable name for the resource */
    LABEL(DucNamespace.RDFS, "label"),

    /** The nature or genre of the resource */
    TYPE(DucNamespace.DCTERMS, "type"),

    /** Observations for the resource */
    OBSERVATION(DucNamespace.DUC, "observation"),

    /** References for the resource */
    REFERENCE(DucNamespace.DUC, "reference"),

    /** Type of curricular knowledge */
    KNOWLEDGE_TYPE(DucNamespace.DUC, "knowledgeType"),

    /* Datatype properties */

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
    LANGUAGE(DucNamespace.DCTERMS, "language"),

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

    /* Object properties */

    /** The resource is a child of another resource */
    CLASS(DucNamespace.OWL, "Class"),

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
     * @param value
     */
    DucVocabulary(DucNamespace ns, String value) {
        this.ns = ns;
        this.value = value;
    }


    /**
     * Returns this enumeration value.
     *
     * @return  String value
     */
    public String value() {
        return value;
    }


    /**
     * Returns an enumeration object given a value.
     *
     * @param value     Enumeration value
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
