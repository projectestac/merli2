package cat.xtec.merli.mapper.util;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;
import org.semanticweb.owlapi.model.parameters.Imports;
import org.semanticweb.owlapi.model.*;


/**
 * This class defines utility methods to work with ontologies.
 */
public final class Ontologies {

    /** Prevent the instantiation */
    private Ontologies() {}


    /**
     * Returns a stream of ontologies referenced by the given ontology
     * instance. That is, the ontology itself plus all its imports.
     *
     * @param root      Root ontology
     * @return          Ontologies stream
     */
    public static Stream<OWLOntology> ontologies(OWLOntology root) {
        return root.getImportsClosure().stream();
    }


    /**
     * Returns a stream of classes declared on the given ontology or
     * any of its imported ontologies.
     *
     * @param root      Root ontology
     * @return          Entities stream
     */
    public static Stream<OWLEntity> classes(OWLOntology root) {
        return root.getClassesInSignature(Imports.INCLUDED)
                   .stream().map(o -> (OWLEntity) o);
    }


    /**
     * Returns a stream of named individuals declared on the given
     * ontology or any of its imported ontologies.
     *
     * @param root      Root ontology
     * @return          Entities stream
     */
    public static Stream<OWLEntity> individuals(OWLOntology root) {
        return root.getIndividualsInSignature(Imports.INCLUDED)
                   .stream().map(o -> (OWLEntity) o);
    }


    /**
     * Returns a stream of named subclasses for the given superclass.
     *
     * @param root      Root ontology
     * @param entity    Class instance
     * @return          A stream of entities
     */
    public static Stream<OWLEntity> subclasses(OWLOntology root, OWLClass entity) {
        return ontologies(root).map(o -> o.getSubClassAxiomsForSuperClass(entity))
            .flatMap(Collection::stream).filter(e -> e.isGCI() == false)
            .map(e -> e.getSubClass().asOWLClass());
    }


    /**
     * Returns a stream of named individuals for the given class.
     *
     * @param root      Root ontology
     * @param entity    Class instance
     * @return          A stream of entities
     */
    public static Stream<OWLEntity> instances(OWLOntology root, OWLClass entity) {
        return ontologies(root).map(o -> o.getClassAssertionAxioms(entity))
            .flatMap(Collection::stream).map(e -> e.getIndividual())
            .filter(o -> o.isNamed()).map(o -> (OWLEntity) o);
    }


    /**
     * Returns a stream of the axioms and annotations defined on the
     * provided ontology, or any of its imported ontologies, that descrive
     * the given entity. Only classes and individuals are supported.
     *
     * @param root      Root ontology
     * @param entity    Entity instance
     * @return          Axioms stream
     */
    public static Stream<OWLAxiom> assertions(OWLOntology root, OWLEntity entity) {
        return Stream.concat(axioms(root, entity), annotations(root, entity));
    }


    /**
     * Returns a stream of the axioms defined on the provided ontology,
     * or any of its imported ontologies, that descrive the given entity.
     * Only classes and individuals are supported by this method.
     *
     * @param root      Root ontology
     * @param entity    Entity instance
     * @return          Axioms stream
     */
    public static Stream<OWLAxiom> axioms(OWLOntology root, OWLEntity entity) {
        return (entity.isOWLClass() == false) ?
            axioms(root, (OWLIndividual) entity) :
            axioms(root, (OWLClass) entity);
    }


    /**
     * Returns a stream of the axioms defined on the provided ontology,
     * or any of its imported ontologies, that descrive the given entity.
     * Notice that this method does not return any annotation axioms.
     *
     * @param root      Root ontology
     * @param entity    Class instance
     * @return          Axioms stream
     */
    public static Stream<OWLAxiom> axioms(OWLOntology root, OWLClass entity) {
        Set<OWLClassAxiom> axioms = root.getAxioms(entity, Imports.INCLUDED);
        Stream<OWLAxiom> about = declarations(root, entity);
        Stream<OWLAxiom> stream = axioms.stream().map(o -> (OWLAxiom) o);

        return Stream.concat(about, stream);
    }


    /**
     * Returns a stream of the axioms defined on the provided ontology,
     * or any of its imported ontologies, that descrive the given entity.
     * Notice that this method does not return any annotation axioms.
     *
     * @param root      Root ontology
     * @param entity    Class instance
     * @return          Axioms stream
     */
    public static Stream<OWLAxiom> axioms(OWLOntology root, OWLIndividual entity) {
        Set<OWLIndividualAxiom> axioms = root.getAxioms(entity, Imports.INCLUDED);
        Stream<OWLAxiom> about = declarations(root, entity);
        Stream<OWLAxiom> stream = axioms.stream().map(o -> (OWLAxiom) o);

        return Stream.concat(about, stream);
    }


    /**
     * Returns a stream of the axioms defined on the provided ontology,
     * or any of its imported ontologies, that annotate the given entity.
     *
     * @param root      Root ontology
     * @param entity    Entity instance
     * @return          Axioms stream
     */
    public static Stream<OWLAxiom> annotations(OWLOntology root, OWLEntity entity) {
        return annotations(root, entity.getIRI());
    }


    /**
     * Returns a stream of the axioms defined on the provided ontology,
     * or any of its imported ontologies, that annotate the given entity.
     *
     * @param root      Root ontology
     * @param iri       Entity identifier
     * @return          Axioms stream
     */
    public static Stream<OWLAxiom> annotations(OWLOntology root, IRI iri) {
        return ontologies(root).map(o -> o.getAnnotationAssertionAxioms(iri))
              .flatMap(Collection::stream).map(o -> (OWLAxiom) o);
    }


    /**
     * Returns a stream of entities that have the given identifier on
     * the provided ontology or any of its imported ontologies.
     *
     * Notice that, do to punning, for a given unique IRI this method may
     * return more than one object. For example, a class and an individual,
     * which means that an entity acts both as a class and as an individual.
     *
     * @param root      Root ontology
     * @param iri       Entity identifier
     * @return          A stream of entities
     */
    public static Stream<OWLEntity> entities(OWLOntology root, IRI iri) {
        return root.getEntitiesInSignature(iri, Imports.INCLUDED).stream();
    }


    /**
     * Returns a stream of classes that have the given identifier on
     * the provided ontology or any of its imported ontologies.
     *
     * @param root      Root ontology
     * @param iri       Entity identifier
     * @return          A stream of entities
     */
    public static Stream<OWLEntity> classes(OWLOntology root, IRI iri) {
        return entities(root, iri).filter(o -> o.isOWLClass());
    }


    /**
     * Returns a stream of named individuals that have the given identifier
     * on the provided ontology or any of its imported ontologies.
     *
     * @param root      Root ontology
     * @param iri       Entity identifier
     * @return          A stream of entities
     */
    public static Stream<OWLEntity> individuals(OWLOntology root, IRI iri) {
        return entities(root, iri).filter(o -> o.isOWLNamedIndividual());
    }


    /**
     * Returns the declaration axioms for the given entity. Notice that
     * declaration axioms are implicit, thus, this convenience method
     * simply returns a new declaration axiom for the given entity type.
     *
     * @param root      Root ontology
     * @param entity    Entity instance
     * @return          Axioms stream
     */
    private static Stream<OWLAxiom> declarations(OWLOntology root, OWLObject entity) {
        OWLOntologyManager manager = root.getOWLOntologyManager();
        OWLDataFactory factory = manager.getOWLDataFactory();
        return Stream.of(factory.getOWLDeclarationAxiom((OWLEntity) entity));
    }

}
