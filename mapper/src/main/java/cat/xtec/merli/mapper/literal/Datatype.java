package cat.xtec.merli.mapper.literal;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.vocab.OWL2Datatype;
import uk.ac.manchester.cs.owl.owlapi.OWL2DatatypeImpl;


/**
 * Supported literal datatypes.
 */
public enum Datatype implements HasIRI {

    /** Boolean literal */
    BOOLEAN(OWL2Datatype.XSD_BOOLEAN),

    /** Date time literal */
    DATE_TIME(OWL2Datatype.XSD_DATE_TIME),

    /** Double number literal */
    DOUBLE(OWL2Datatype.XSD_DOUBLE),

    /** Float number literal */
    FLOAT(OWL2Datatype.XSD_FLOAT),

    /** Integer number literal */
    INTEGER(OWL2Datatype.XSD_INTEGER),

    /** Language tagged string literal */
    LANG_STRING(OWL2Datatype.RDF_PLAIN_LITERAL),

    /** String literal */
    STRING(OWL2Datatype.XSD_STRING),

    /** Uniform Resource Identifier literal */
    URI(OWL2Datatype.XSD_ANY_URI);

    /** Enumeration value */
    private final OWLDatatype value;


    /**
     * Enumeration constructor
     *
     * @param value
     */
    Datatype(OWL2Datatype type) {
        this.value = new OWL2DatatypeImpl(type);
    }


    /**
     * Returns this enumeration IRI.
     *
     * @return  IRI identifier
     */
    @Override
    public IRI getIRI() {
        return value.getIRI();
    }


    /**
     * Returns this enumeration value.
     *
     * @return  String value
     */
    public OWLDatatype value() {
        return value;
    }


    /**
     * Returns an enumeration object given a value.
     *
     * @param value     Enumeration value
     * @throws IllegalArgumentException
     */
    public static Datatype fromValue(OWLDatatype value) {
        for (Datatype object : Datatype.values()) {
            if (value.equals(object.value))
                return object;
        }

        throw new IllegalArgumentException();
    }

}
