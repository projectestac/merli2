package cat.xtec.merli.mapper;

import org.semanticweb.owlapi.model.*;

import cat.xtec.merli.bind.*;
import cat.xtec.merli.parser.*;
import cat.xtec.merli.mapper.util.*;
import static cat.xtec.merli.bind.DucVocabulary.*;


/**
 * TODO: REFACTOR, RENAME, IMPLEMENTAR FACTORIES!
 */
class Facts {

    /** Domain context */
    private DucContext context;

    /** Root ontology data factory */
    private OWLDataFactory builder;


    private Facts(OWLStore store) {
        OWLOntology root = store.root;
        OWLOntologyManager manager = root.getOWLOntologyManager();
        this.context = store.context;
        this.builder = manager.getOWLDataFactory();
    }


    /**
     *
     */
    public static Facts newInstance(OWLStore store) {
        return new Facts(store);
    }


    /**
     * Converts...
     *
     * @param id            Entity identifier
     * @param type          Domain type
     * @param fact          Fact instance
     *
     * @return              New fact instance
     */
    public OWLAxiom convert(IRI id, Class<?> type, DucFact fact) {
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


    public OWLAxiom toDeclaration(IRI id, DucProperty property, DucVocabulary kind) {
        return INDIVIDUAL.equals(kind) ?
            newInstanceDeclaration(id) :
            newClassDeclaration(id);
    }


    public OWLAxiom toAnnotation(IRI id, DucProperty property, Object value) {
        Class<?> type = property.getDataType();
        DucFactory factory = context.getFactory(type);
        OWLLiteral literal = toLiteral(factory, value);
        IRI vocab = toPredicate(property, value);

        return newAnnotation(id, vocab, literal);
    }


    public OWLAxiom toAttribute(IRI id, DucProperty property, Object value) {
        Class<?> type = property.getDataType();
        DucFactory factory = context.getFactory(type);
        OWLLiteral literal = toLiteral(factory, value);
        IRI vocab = toPredicate(property, value);

        return newAttribute(id, vocab, literal);
    }


    public IRI toIdentifier(DucProperty property, Object value) {
        Class<?> type = property.getDataType();
        DucFactory factory = context.getFactory(type);
        return IRI.create(factory.getIdentifier(value));
    }


    public IRI toPredicate(DucProperty property, Object value) {
        return IRI.create(property.getPredicate(value).value());
    }


    public DucVocabulary getEntityKind(Class<?> type) {
        return type.getAnnotation(DucEntity.class).value();
    }


    public OWLLiteral toLiteral(DucFactory factory, Object value) {
        String string = factory.getString(value);
        String locale = factory.getLocale(value);

        return (locale instanceof String) ?
            Literals.from(string, locale) :
            Literals.from(string);
    }


    /**
     *
     *
     * @param subject       Subject identifier
     * @return              New axiom instance
     */
    private OWLAxiom newClassDeclaration(IRI subject) {
        OWLEntity entity = builder.getOWLClass(subject);
        return builder.getOWLDeclarationAxiom(entity);
    }


    /**
     *
     *
     * @param subject       Subject identifier
     * @return              New axiom instance
     */
    private OWLAxiom newInstanceDeclaration(IRI subject) {
        OWLEntity entity = builder.getOWLNamedIndividual(subject);
        return builder.getOWLDeclarationAxiom(entity);
    }


    /**
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
