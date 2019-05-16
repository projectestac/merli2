package cat.xtec.merli.mapper;

import org.semanticweb.owlapi.model.*;

import cat.xtec.merli.bind.*;
import cat.xtec.merli.parser.*;
import cat.xtec.merli.mapper.util.*;
import cat.xtec.merli.mapper.OWLStore;


/**
 * Provides methods to convert {@code OWLAxiom} instances to {@code DucFact}
 * objects using a predefined set of conversions.
 */
class FactBuilder {

    /** */
    private OWLStore store;

    /** Domain context */
    private DucContext context;


    /**
     * Builder constructor.
     *
     * @param store     Store instance
     */
    private FactBuilder(OWLStore store) {
        this.store = store;
        this.context = store.context;
    }


    /**
     * Creates a new instance of the builder.
     *
     * @param store     Store instance
     */
    public static FactBuilder newInstance(OWLStore store) {
        return new FactBuilder(store);
    }


    /**
     * Creates a new fact that represents the given OWL axiom for
     * a domain entity type.
     *
     * @param type          Domain type
     * @param axiom         OWL axiom
     *
     * @return              New fact instance
     */
    public DucFact create(Class<?> type, OWLAxiom axiom) throws Exception {
        DucVocabulary predicate = Predicates.from(axiom);
        DucProperty property = context.getProperty(type, predicate);

        Object value = Values.from(axiom);
        Object object = null;

        if (property.isRelationContainer()) {
            object = fromTarget(property, predicate, value);
        } else if (property.isRelation()) {
            object = fetchObject(property, value);
        } else {
            object = fromObject(property, value);
        }

        return new DucFact(predicate, object);
    }


    /**
     * Fetch a new object for the provided value's IRI identifier
     * and the property data type.
     *
     * @param property      Property instance
     * @param value         Value of the property
     *
     * @return              New object instance
     */
    private Object fetchObject(DucProperty property, Object value) {
        IRI iri = ((HasIRI) value).getIRI();
        return store.fetch(iri, property.getDataType());
    }


    /**
     * Fetch a new object for the provided value's IRI identifier
     * and the property's relation target type.
     *
     * @param property      Property instance
     * @param value         Value of the property
     *
     * @return              New object instance
     */
    private Object fetchTarget(DucProperty property, Object value) {
        IRI iri = ((HasIRI) value).getIRI();
        return store.fetch(iri, property.getTargetType());
    }


    /**
     * Instantiates a new object from the given explicit relation.
     *
     * @param property      Property instance
     * @param predicate     Predicate of the relation
     * @param value         Value of the property
     *
     * @return              New object instance
     */
    private Object fromTarget(DucProperty property, DucVocabulary predicate, Object value) throws Exception {
        Class<?> type = property.getDataType();
        DucFactory factory = context.getFactory(type);
        Object target = fetchTarget(property, value);

        return factory.newInstance(target, predicate);
    }


    /**
     * Instantiates a new object from the given axiom value.
     *
     * @param property      Property instance
     * @param value         Value of the property
     *
     * @return              New object instance
     */
    private Object fromObject(DucProperty property, Object value) throws Exception {
        Class<?> type = property.getDataType();
        DucFactory factory = context.getFactory(type);

        return (value instanceof OWLLiteral) ?
            fromLiteral(factory, (OWLLiteral) value) :
            factory.newInstance(String.valueOf(value));
    }


    /**
     * Instantiates a new object from the given literal value.
     *
     * @param factory       Factory instance
     * @param value         A literal value
     *
     * @return              New object instance
     */
    private Object fromLiteral(DucFactory factory, OWLLiteral value) throws Exception {
        String string = value.getLiteral();
        String locale = value.getLang();

        return (value.hasLang() == true) ?
            factory.newInstance(string, locale) :
            factory.newInstance(string);
    }

}
