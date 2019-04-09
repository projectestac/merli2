package cat.xtec.merli.mapper;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import com.google.common.graph.Traverser;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.*;

import cat.xtec.merli.parser.*;
import cat.xtec.merli.mapper.util.*;


/**
 * Provides methods to obtain domain objects from ontologies and
 * sets of ontology changes from domain objects.
 */
public class OWLStore {

    /** Domain type parser */
    protected DucParser parser;

    /** Domain context */
    protected DucContext context;

    /** Root ontology */
    protected OWLOntology root;

    /** Ontology imports closure */
    protected Set<OWLOntology> ontologies;

    /** Converts facts to axioms */
    protected Axioms axiomConverter;

    /** Converts axioms to facts */
    protected Facts factConverter;

    /** Application logger reference */
    private static Logger logger = Mapper.getLogger();


    /**
     * Creates a new OWL store instance
     *
     * @param root          Root ontology
     * @param types         Supported domain types
     */
    protected OWLStore(OWLOntology root, Class<?>[] types) {
        this.root = root;
        this.ontologies = root.getImportsClosure();
        this.context = DucContext.newInstance(types);
        this.axiomConverter = Axioms.newInstance(this);
        this.factConverter = Facts.newInstance(this);
        this.parser = context.createParser();
    }


    /**
     * Creates a new OWL store for the provided ontologies.
     *
     * @param root          Root ontology
     * @param types         Supported domain types
     *
     * @return              New OWL store
     */
    public static OWLStore newInstance(OWLOntology root, Class<?>[] types) {
        return new OWLStore(root, types);
    }


    /**
     * Fetches a single object from the ontology and serializes it
     * to the given domain type.
     *
     * @param id            Entity identifier
     * @param type          Domain type
     *
     * @return              An instance of type or null
     */
    public <T> T fetch(IRI id, Class<T> type) {
        HashSet<DucFact> facts = new HashSet<>();
        Stream<OWLAxiom> axioms = getAssertions(root, id);

        // Collect facts from relevant axioms. The axioms considered
        // relevant will depend on the requested domain type.

        axioms.forEach(axiom -> {
            try {
                facts.add(fromAxiom(type, axiom));
            } catch (Exception e) {
                logger.fine(e.getMessage());
            }
        });

        // Compose the result from the collected facts. Returns null
        // if no facts were collected (the entity does not exist).

        T result = null;

        try {
            if (facts.isEmpty() == false) {
                result = parser.compose(facts, type);
            }
        } catch (DucException e) {
            logger.severe(e.getMessage());
        }

        return result;
    }


    /**
     * Obtains the set of changes required to store the given object
     * into the ontology as the given domain type.
     *
     * @param id            Entity identifier
     * @param object        Object to store
     * @param type          Domain type
     *
     * @return              List of changes
     *
     * @throws NullPointerException     If any of the parameters are null
     */
    public <T> OWLChanges persist(IRI id, T object, Class<T> type) {
        Set<DucFact> facts = null;

        // Extracts the relevant facts from the provided object. The
        // extracted facts will depend on the specified domain type.

        try {
            facts = parser.parse(object, type);
        } catch (DucException e) {
            logger.severe(e.getMessage());
        }

        if (facts == null || facts.isEmpty()) {
            return OWLChanges.empty();
        }

        // Collects a set of relevant axioms from the extracted facts.
        // The generated axioms are dependent of the domain type.

        Set<OWLAxiom> axioms = new HashSet<>();

        facts.forEach(fact -> {
            try {
                axioms.add(fromFact(id, type, fact));
            } catch (Exception e) {
                logger.fine(e.getMessage());
            }
        });

        // Convert the collected axioms into a list of changes. Notice
        // that new axioms are always appended to the root ontology.

        List<OWLOntologyChange> changes = new ArrayList<>();

        getAssertions(root, id).forEach(axiom -> {
            if (axioms.contains(axiom) == false) {
                OWLOntology o = getOntologyForAxiom(axiom);
                RemoveAxiom change = new RemoveAxiom(o, axiom);
                changes.add(change);
            } else {
                axioms.remove(axiom);
            }
        });

        axioms.forEach(axiom -> {
            AddAxiom change = new AddAxiom(root, axiom);
            changes.add(change);
        });

        return new OWLChanges(changes);
    }


    /**
     * Generates the changes required to create the entity with the
     * provided IRI and domain type on the ontology.
     *
     * @param id            Entity identifier
     * @param type          Domain type
     * @return              List of changes
     *
     * @throws IllegalArgumentException     The given IRI exists or
     *      the object cannot be instantiated
     */
    public <T> OWLChanges create(IRI id, Class<T> type) {
        T object = null;

        try {
            failOnExistence(id);
            object = type.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        return persist(id, object, type);
    }


    /**
     * Generates the changes required to remove the entity with the
     * provided IRI from the ontology.
     *
     * @param id            Entity identifier
     * @return              List of changes
     */
    @SuppressWarnings("unchecked")
    public OWLChanges remove(IRI id) {
        OWLEntityRemover remover = new OWLEntityRemover(ontologies);
        getEntities(root, id).forEach(e -> e.accept(remover));
        List<?> changes = remover.getChanges();
        return new OWLChanges((List<OWLOntologyChange>) changes);
    }


    /**
     * Streams the direct descendants of a class entity.
     *
     * @param id            Entity identifier
     * @return              Stream of identifiers
     */
    public Stream<IRI> children(IRI id) {
        Stream<OWLEntity> stream = getClassChildren(root, id);
        return stream.map(e -> e.getIRI());
    }


    /**
     * Streams the individuals that are direct instances of a class.
     *
     * @param id            Entity identifier
     * @return              Stream of identifiers
     */
    public Stream<IRI> instances(IRI id) {
        Stream<OWLEntity> stream = getClassInstances(root, id);
        return stream.map(e -> e.getIRI());
    }


    /**
     * Streams a breadth-first traversal of the descendants of the
     * class entity with the provided identifier.
     *
     * @param id            Entity identifier
     * @return              Stream of identifiers
     */
    public Stream<IRI> traverse(IRI id) {
        OWLEntity entity = getClassEntity(id);

        Iterable<OWLEntity> iterable = traverser.breadthFirst(entity);
        Spliterator<OWLEntity> iterator = iterable.spliterator();
        Stream<OWLEntity> stream = StreamSupport.stream(iterator, false);

        return stream.map(e -> e.getIRI());
    }


    /**
     * Checks if at least an entity with the given identifier
     * exists on the ontology.
     *
     * @param id            Entity identifier
     * @return              True if an entiy was found
     */
    protected boolean entityExists(IRI id) {
        return getEntities(root, id).findAny().isPresent();
    }


    /**
     * Returns a stream of entities found on the ontology with the
     * given identifier. Notice that due to punning, there may be
     * multiple entities with the given IRI.
     *
     * @param root      Root ontology
     * @param id        Entity identifier
     * @return          Stream of entities
     */
    protected Stream<OWLEntity> getEntities(OWLOntology root, IRI id) {
        return Ontologies.entities(root, id);
    }


    /**
     * Returns a stream of axioms found on the ontology for entities
     * with the given identifier. Notice that due to punning, there
     * may be multiple entities with the given IRI.
     *
     * @param root      Root ontology
     * @param id        Entity identifier
     * @return          Stream of axioms
     */
    protected Stream<OWLAxiom> getAssertions(OWLOntology root, IRI id) {
        return getEntities(root, id).flatMap(entity -> {
            return Ontologies.assertions(root, entity);
        });
    }


    /**
     * Returns a stream of entities found on the ontology that are
     * children of the class with the given identifier.
     *
     * @param root      Root ontology
     * @param id        Entity identifier
     * @return          Stream of entities
     */
    protected Stream<OWLEntity> getClassChildren(OWLOntology root, IRI id) {
        return Ontologies.subclasses(root, getClassEntity(id));
    }


    /**
     * Returns a stream of entities found on the ontology that are
     * instances of the class with the given identifier.
     *
     * @param root      Root ontology
     * @param id        Entity identifier
     * @return          Stream of entities
     */
    protected Stream<OWLEntity> getClassInstances(OWLOntology root, IRI id) {
        return Ontologies.instances(root, getClassEntity(id));
    }


    /**
     * Obtains the ontology that contains the given axiom or null
     * if the axiom is not found on the imports closure.
     *
     * If the axiom cannot be found on any of the ontologies, the root
     * ontology is returned. This is to account for any implicit axioms
     * that may be queried (i.e. declaration axioms).
     *
     * @param axiom     An axiom instance
     * @return          Ontology instance
     */
    protected OWLOntology getOntologyForAxiom(OWLAxiom axiom) {
        for (OWLOntology ontology : ontologies) {
            if (ontology.containsAxiom(axiom))
                return ontology;
        }

        return root;
    }


    /**
     * Obtains a new OWL class instance with the given identifier.
     *
     * @param id        Class identifier
     * @return          New OWL class instance
     */
    protected OWLClass getClassEntity(IRI id) {
        OWLOntologyManager manager = root.getOWLOntologyManager();
        OWLDataFactory builder = manager.getOWLDataFactory();
        return builder.getOWLClass(id);
    }


    /**
     * This convenience method throws an exception if an entity with
     * the provided IRI identifier already exists on the ontology.
     *
     * @param id            Entity identifier
     */
    protected void failOnExistence(IRI id) throws IllegalArgumentException {
        if (entityExists(id) == true) {
            throw new IllegalArgumentException(
                "An entity exists with the IRI: " + id);
        }
    }


    /**
     * Converts an OWL axiom into a fact instance.
     *
     * @param type          Domain type
     * @param axiom         OWL axiom
     * @return              New fact instance
     */
    private DucFact fromAxiom(Class<?> type, OWLAxiom axiom) throws Exception {
        return axiomConverter.convert(type, axiom);
    }


    /**
     * Converts a fact instance into an OWL axiom.
     *
     * @param type          Domain type
     * @param fact          Fact instance
     * @return              New OWL axiom
     */
    private OWLAxiom fromFact(IRI id, Class<?> type, DucFact fact) throws Exception {
        return factConverter.convert(id, type, fact);
    }


    /**
     * Graph traverser for descendent classes of a class.
     */
    private Traverser<OWLEntity> traverser = Traverser.forGraph(c -> {
        return Ontologies.subclasses(root, c.asOWLClass())::iterator;
    });

}
