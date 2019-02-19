package cat.xtec.merli.bind;


/**
 * DUC Ontology namespaces
 */
public enum DucNamespace {

    /** Disseny Unitari del Curriculum namespace */
    DUC("http://merli.xtec.cat/DUC#"),

    /** Dublin Core metadata terms namespace */
    DCTERMS("http://purl.org/dc/terms/"),

    /** Learning Object Metadata namespace */
    LOM("http://ltsc.ieee.org/rdf/lomv1p0/lom#"),

    /** Web Ontology Language namespace */
    OWL("http://www.w3.org/2002/07/owl#"),

    /** RDF namespace */
    RDF("http://www.w3.org/1999/02/22-rdf-syntax-ns#"),

    /** RDF Schema namespace */
    RDFS("http://www.w3.org/2000/01/rdf-schema#"),

    /** Simple Knowledge Organization System */
    SKOS("http://www.w3.org/2004/02/skos/core#"),

    /** XTHES thesaurus namespace */
    XTHES("http://www.xthes.org/elements/1.0/");

    /** Enumeration value */
    private final String value;


    /**
     * Enumeration constructor
     *
     * @param value     Property name
     */
    DucNamespace(String value) {
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
    public static DucNamespace fromValue(String value) {
        for (DucNamespace object : DucNamespace.values()) {
            if (value.equals(object.value))
                return object;
        }

        throw new IllegalArgumentException(value);
    }

}
