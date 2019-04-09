package cat.xtec.merli.mapper.util;

import java.lang.reflect.Method;
import org.semanticweb.owlapi.model.*;
import cat.xtec.merli.bind.DucVocabulary;


/**
 * Utility methods to extract predicates from axioms.
 */
public final class Predicates {

    /** Supported types in relevant order */
    private static final Class<?>[] TYPES = {
        OWLClass.class,
        OWLIndividual.class,
        OWLSubClassOfAxiom.class,
        OWLClassAssertionAxiom.class,
        OWLDeclarationAxiom.class,
        HasProperty.class,
        HasIRI.class
    };


    /** Prevent the instantiation */
    private Predicates() {}


    /**
     * Extracts a predicate from a class value.
     *
     * @param value     An object
     * @return          Predicate value
     */
    public static DucVocabulary from(OWLEntity value) {
        return DucVocabulary.ABOUT;
    }


    /**
     * Extracts a predicate from a subclass-of axiom.
     *
     * @param value     An object
     * @return          Predicate value
     */
    public static DucVocabulary from(OWLSubClassOfAxiom value) {
        OWLClassExpression o = value.getSuperClass();
        return (o instanceof OWLClass) ? DucVocabulary.PARENT : from(o);
    }


    /**
     * Extracts a predicate from a class assertion axiom.
     *
     * @param value     An object
     * @return          Predicate value
     */
    public static DucVocabulary from(OWLClassAssertionAxiom value) {
        OWLClassExpression o = value.getClassExpression();
        return (o instanceof OWLClass) ? DucVocabulary.TYPE : from(o);
    }


    /**
     * Extracts a value from a declaration axiom.
     *
     * @param value     An object
     * @return          Object value
     */
    public static DucVocabulary from(OWLDeclarationAxiom value) {
        return from(value.getEntity());
    }


    /**
     * Extracts a predicate from an object that has an IRI.
     *
     * @param value     An object
     * @return          Predicate value
     */
    public static DucVocabulary from(HasIRI value) {
        String iri = String.valueOf(value.getIRI());
        return DucVocabulary.fromValue(iri);
    }


    /**
     * Extracts a predicate from an object that has a property.
     *
     * @param value     An object
     * @return          Predicate value
     */
    public static DucVocabulary from(HasProperty<?> value) {
        return from(value.getProperty());
    }


    /**
     * Extracts a predicate from the given object.
     *
     * @param value     An object
     * @return          Predicate value
     */
    public static DucVocabulary from(Object value) {
        try {
            Class<?> ctype = value.getClass();

            for (Class<?> type : TYPES) {
                if (type.isAssignableFrom(ctype)) {
                    return from(value, type);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;
    }


    /**
     * Invokes a factory method for the given type on a value.
     *
     * @param value     An object
     * @param type      Factory type
     * @return          Predicate value
     */
    private static DucVocabulary from(Object value, Class<?> type) throws Exception {
        Method method = Predicates.class.getMethod("from", type);
        return (DucVocabulary) method.invoke(null, value);
    }

}
