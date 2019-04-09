package cat.xtec.merli.mapper.util;

import java.lang.reflect.Method;
import org.semanticweb.owlapi.model.*;


/**
 * Utility methods to extract values from axioms.
 */
public final class Values {

    /** Supported types in relevant order */
    private static final Class<?>[] TYPES = {
        OWLClass.class,
        OWLIndividual.class,
        OWLLiteral.class,
        OWLSubClassOfAxiom.class,
        OWLClassAssertionAxiom.class,
        OWLAnnotationAssertionAxiom.class,
        OWLDeclarationAxiom.class,
        HasFiller.class,
        HasObject.class,
        HasIRI.class
    };


    /** Prevent the instantiation */
    private Values() {}


    /**
     * Extracts a value from a class.
     *
     * @param value     An object
     * @return          Object value
     */
    public static Object from(OWLClass value) {
        return value;
    }


    /**
     * Extracts a value from an individual.
     *
     * @param value     An object
     * @return          Object value
     */
    public static Object from(OWLIndividual value) {
        return value;
    }


    /**
     * Extracts a value from an object that has an IRI.
     *
     * @param value     An object
     * @return          Object value
     */
    public static Object from(OWLLiteral value) {
        return value;
    }


    /**
     * Extracts a predicate from an object that has an IRI.
     *
     * @param value     An object
     * @return          Object value
     */
    public static Object from(HasIRI value) {
        return value.getIRI();
    }


    /**
     * Extracts a value from an object that has a property.
     *
     * @param value     An object
     * @return          Object value
     */
    public static Object from(HasObject<?> value) {
        return from(value.getObject());
    }


    /**
     * Extracts a value from an object that has a property.
     *
     * @param value     An object
     * @return          Object value
     */
    public static Object from(HasFiller<?> value) {
        return from(value.getFiller());
    }


    /**
     * Extracts a value from a subclass-of axiom.
     *
     * @param value     An object
     * @return          Object value
     */
    public static Object from(OWLAnnotationAssertionAxiom value) {
        return from(value.getValue());
    }


    /**
     * Extracts a value from a declaration axiom.
     *
     * @param value     An object
     * @return          Object value
     */
    public static Object from(OWLDeclarationAxiom value) {
        return from(value.getEntity());
    }


    /**
     * Extracts a value from a subclass-of axiom.
     *
     * @param value     An object
     * @return          Object value
     */
    public static Object from(OWLSubClassOfAxiom value) {
        return from(value.getSuperClass());
    }


    /**
     * Extracts a value from a class assertion axiom.
     *
     * @param value     An object
     * @return          Object value
     */
    public static Object from(OWLClassAssertionAxiom value) {
        return from(value.getClassExpression());
    }


    /**
     * Constructs a new value for the given object.
     *
     * @param value     An object
     * @return          Object value
     */
    public static Object from(Object value) {
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
     * @return          Object value
     */
    private static Object from(Object value, Class<?> type) throws Exception {
        Method method = Values.class.getMethod("from", type);
        return (Object) method.invoke(null, value);
    }

}
