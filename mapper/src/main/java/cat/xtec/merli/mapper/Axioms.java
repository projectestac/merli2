package cat.xtec.merli.mapper;

import org.semanticweb.owlapi.model.*;

import cat.xtec.merli.bind.*;
import cat.xtec.merli.parser.*;
import cat.xtec.merli.mapper.util.*;
import cat.xtec.merli.mapper.OWLStore;


/**
 * TODO: REFACTOR, RENAME
 */
class Axioms {

    /** */
    private OWLStore store;

    /** Domain context */
    private DucContext context;


    /**
     *
     */
    private Axioms(OWLStore store) {
        this.store = store;
        this.context = store.context;
    }


    /**
     *
     */
    public static Axioms newInstance(OWLStore store) {
        return new Axioms(store);
    }


    /**
     * Converts an OWL axiom into a fact instance.
     *
     * @param type          Domain type
     * @param axiom         OWL axiom
     *
     * @return              New fact instance
     */
    public DucFact convert(Class<?> type, OWLAxiom axiom) throws Exception {
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


    private Object fetchObject(DucProperty property, Object value) {
        IRI iri = ((HasIRI) value).getIRI();
        return store.fetch(iri, property.getDataType());
    }


    private Object fetchTarget(DucProperty property, Object value) {
        IRI iri = ((HasIRI) value).getIRI();
        return store.fetch(iri, property.getTargetType());
    }


    private Object fromTarget(DucProperty property, DucVocabulary predicate, Object value) throws Exception {
        Class<?> type = property.getDataType();
        DucFactory factory = context.getFactory(type);
        Object target = fetchTarget(property, value);

        return factory.newInstance(target, predicate);
    }


    private Object fromObject(DucProperty property, Object value) throws Exception {
        Class<?> type = property.getDataType();
        DucFactory factory = context.getFactory(type);

        return (value instanceof OWLLiteral) ?
            fromLiteral(factory, (OWLLiteral) value) :
            factory.newInstance(String.valueOf(value));
    }


    private Object fromLiteral(DucFactory factory, OWLLiteral value) throws Exception {
        String string = value.getLiteral();
        String locale = value.getLang();

        return (value.hasLang() == true) ?
            factory.newInstance(string, locale) :
            factory.newInstance(string);
    }

}
