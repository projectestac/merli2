package cat.xtec.merli.mapper;

import org.semanticweb.owlapi.model.*;

import cat.xtec.merli.bind.*;
import cat.xtec.merli.parser.*;
import cat.xtec.merli.mapper.util.*;
import static cat.xtec.merli.bind.DucVocabulary.*;


/**
 * Provides methods to convert {@code DucFact} instances to OWL axioms
 * using a predefined set of conversions.
 */
class AxiomBuilder {

    /** Domain context */
    private DucContext context;

    /** Root ontology data factory */
    private OWLDataFactory builder;


    /**
     * Builder constructor.
     *
     * @param store     Store instance
     */
    private AxiomBuilder(OWLStore store) {
        OWLOntology root = store.root;
        OWLOntologyManager manager = root.getOWLOntologyManager();
        this.context = store.context;
        this.builder = manager.getOWLDataFactory();
    }


    /**
     * Creates a new instance of the builder.
     *
     * @param store     Store instance
     */
    public static AxiomBuilder newInstance(OWLStore store) {
        return new AxiomBuilder(store);
    }


    /**
     * Creates a new OWL axiom that represents the given fact for
     * a domain entity type.
     *
     * @param id            Entity identifier
     * @param type          Domain type
     * @param fact          Fact instance
     *
     * @return              New fact instance
     */
    public OWLAxiom create(IRI id, Class<?> type, DucFact fact) {
        DucVocabulary kind = getEntityKind(type);
        DucVocabulary predicate = fact.getPredicate();
        DucProperty property = context.getProperty(type, predicate);

        Object value = fact.getObject();
        OWLAxiom axiom = null;

        if (property.isAnnotation()) {
            axiom = toAnnotation(id, property, value);
        } else if (property.isAttribute()) {
            axiom = toAttribute(id, property, value);
        } else if (property.isRelation()) {
            axiom = toRelation(id, property, kind, value);
        } else if (property.isIdentifier()) {
            axiom = toDeclaration(id, property, kind);
        }

        return axiom;
    }


    /**
     * Instantiates a new axiom for the given relation property.
     *
     * @param id            Predicate IRI identifier
     * @param property      Property instance
     * @param kind          INDIVIDUAL | CLASS
     * @param value         Value of the relation property
     *
     * @return              New axiom instance
     */
    public OWLAxiom toRelation(IRI id, DucProperty property, DucVocabulary kind, Object value) {
        IRI object = toIdentifier(property, value);
        IRI vocab = toPredicate(property, value);

        DucVocabulary predicate = property.getPredicate(value);
        OWLAxiom axiom = null;

        if (PARENT.equals(predicate)) {
            axiom = newParentAssertion(id, object);
        } else if (TYPE.equals(predicate)) {
            axiom = newTypeAssertion(id, object);
        } else if (CLASS.equals(kind)) {
            axiom = newClassRelation(id, vocab, object);
        } else {
            axiom = newInstanceRelation(id, vocab, object);
        }

        return axiom;
    }


    /**
     * Instantiates a new axiom for the given identifier property.
     *
     * @param id            Predicate IRI identifier
     * @param property      Property instance
     * @param kind          INDIVIDUAL | CLASS
     *
     * @return              New axiom instance
     */
    public OWLAxiom toDeclaration(IRI id, DucProperty property, DucVocabulary kind) {
        return INDIVIDUAL.equals(kind) ?
            newInstanceDeclaration(id) :
            newClassDeclaration(id);
    }


    /**
     * Instantiates a new axiom for the given annotation property.
     *
     * @param id            Predicate IRI identifier
     * @param property      Property instance
     * @param value         Value of the property
     *
     * @return              New axiom instance
     */
    public OWLAxiom toAnnotation(IRI id, DucProperty property, Object value) {
        Class<?> type = property.getDataType();
        DucFactory factory = context.getFactory(type);
        OWLLiteral literal = toLiteral(factory, value);
        IRI vocab = toPredicate(property, value);

        return newAnnotation(id, vocab, literal);
    }


    /**
     * Instantiates a new axiom for the given attribute property.
     *
     * @param id            Predicate IRI identifier
     * @param property      Property instance
     * @param value         Value of the property
     *
     * @return              New axiom instance
     */
    public OWLAxiom toAttribute(IRI id, DucProperty property, Object value) {
        Class<?> type = property.getDataType();
        DucFactory factory = context.getFactory(type);
        OWLLiteral literal = toLiteral(factory, value);
        IRI vocab = toPredicate(property, value);

        return newAttribute(id, vocab, literal);
    }


    /**
     * Instantiates a new entity IRI from the given value.
     *
     * @param property      Property instance
     * @param value         Value of the property
     *
     * @return              New IRI identifier
     */
    public IRI toIdentifier(DucProperty property, Object value) {
        Class<?> type = property.getDataType();
        DucFactory factory = context.getFactory(type);
        return IRI.create(factory.getIdentifier(value));
    }


    /**
     * Instantiates a new predicate IRI from the given value.
     *
     * @param property      Property instance
     * @param value         Value of the property
     *
     * @return              New IRI identifier
     */
    public IRI toPredicate(DucProperty property, Object value) {
        return IRI.create(property.getPredicate(value).value());
    }


    /**
     * Instantiates a new OWL literal from the given value.
     *
     * @param factory       Factory instance
     * @param value         An object value
     *
     * @return              New literal instance
     */
    public OWLLiteral toLiteral(DucFactory factory, Object value) {
        String string = factory.getString(value);
        String locale = factory.getLocale(value);

        return (locale instanceof String) ?
            Literals.from(string, locale) :
            Literals.from(string);
    }


    /**
     * Obtains the OWL type of a domain class. Thtat is, the predicate
     * which is annotated with {@code DucEntity} on the class.
     *
     * @param type          Domian entity type
     * @return              Vocabulary value
     */
    public DucVocabulary getEntityKind(Class<?> type) {
        return type.getAnnotation(DucEntity.class).value();
    }


    /**
     * Creates a new declaration axiom for a class.
     *
     * @param subject       Subject identifier
     * @return              New axiom instance
     */
    private OWLAxiom newClassDeclaration(IRI subject) {
        OWLEntity entity = builder.getOWLClass(subject);
        return builder.getOWLDeclarationAxiom(entity);
    }


    /**
     * Creates a new declaration axiom for an individual.
     *
     * @param subject       Subject identifier
     * @return              New axiom instance
     */
    private OWLAxiom newInstanceDeclaration(IRI subject) {
        OWLEntity entity = builder.getOWLNamedIndividual(subject);
        return builder.getOWLDeclarationAxiom(entity);
    }


    /**
     * Creates a new subclass-of assertion axiom for a class.
     *
     * @param subject       Subject identifier
     * @param object        Object identifier
     * @return              New axiom instance
     */
    private OWLAxiom newParentAssertion(IRI subject, IRI object) {
        return builder.getOWLSubClassOfAxiom(
            builder.getOWLClass(subject),
            builder.getOWLClass(object)
        );
    }


    /**
     * Creates a new class assertion axiom for an individual.
     *
     * @param subject       Subject identifier
     * @param object        Object identifier
     * @return              New axiom instance
     */
    private OWLAxiom newTypeAssertion(IRI subject, IRI object) {
        return builder.getOWLClassAssertionAxiom(
            builder.getOWLClass(object),
            builder.getOWLNamedIndividual(subject)
        );
    }


    /**
     * Creates a new class assertion axiom for a relation.
     *
     * @param subject       Subject identifier
     * @param predicate     Predicate identifier
     * @param object        Object identifier
     * @return              New axiom instance
     */
    private OWLAxiom newInstanceRelation(IRI subject, IRI predicate, IRI object) {
        return builder.getOWLClassAssertionAxiom(
            builder.getOWLObjectSomeValuesFrom(
                builder.getOWLObjectProperty(predicate),
                builder.getOWLClass(object)
            ),
            builder.getOWLNamedIndividual(subject)
        );
    }


    /**
     * Creates a new subclass-of assertion axiom for a relation.
     *
     * @param subject       Subject identifier
     * @param predicate     Predicate identifier
     * @param object        Object identifier
     * @return              New axiom instance
     */
    private OWLAxiom newClassRelation(IRI subject, IRI predicate, IRI object) {
        return builder.getOWLSubClassOfAxiom(
            builder.getOWLClass(subject),
            builder.getOWLObjectSomeValuesFrom(
                builder.getOWLObjectProperty(predicate),
                builder.getOWLClass(object)
            )
        );
    }


    /**
     * Creates a new annotation assertion axiom.
     *
     * @param subject       Subject identifier
     * @param predicate     Predicate identifier
     * @param value         Literal value
     * @return              New axiom instance
     */
    private OWLAxiom newAnnotation(IRI subject, IRI predicate, OWLLiteral value) {
        return builder.getOWLAnnotationAssertionAxiom(
            builder.getOWLAnnotationProperty(predicate),
            subject,
            value
        );
    }


    /**
     * Creates a new data property assertion axiom.
     *
     * @param subject       Subject identifier
     * @param predicate     Predicate identifier
     * @param value         Literal value
     * @return              New axiom instance
     */
    private OWLAxiom newAttribute(IRI subject, IRI predicate, OWLLiteral value) {
        return builder.getOWLDataPropertyAssertionAxiom(
            builder.getOWLDataProperty(predicate),
            builder.getOWLNamedIndividual(subject),
            value
        );
    }

}
