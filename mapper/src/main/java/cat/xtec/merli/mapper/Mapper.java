package cat.xtec.merli.mapper;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;

import cat.xtec.merli.Application;
import cat.xtec.merli.bind.DucVocabulary;
import cat.xtec.merli.mapper.util.Predicates;
import cat.xtec.merli.mapper.util.Ontologies;
import cat.xtec.merli.mapper.util.Values;


/**
 * Main class of the ontology binder module.
 */
public final class Mapper {

    /** Root ontology file path */
    private static String ROOT_PATH = "root-ontology.owl";

    /** Application logger reference */
    private static Logger logger = Application.getLogger();

    /** Prevent the instantiation */
    private Mapper() {}


    /**
     * Returns a reference to the application logger.
     *
     * @return              Logger instance
     */
    public static Logger getLogger() {
        return logger;
    }


    /**
     * Main method of the class.
     *
     * @param args          Command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: binder <zip_path>");
            System.exit(1);
        }

        OWLOntology root = openOntologies(args[0]);

        try (Scanner input = new Scanner(System.in)) {
            while (input.hasNextLine()) {
                String iri = input.nextLine();
                print(root, IRI.create(iri));
            }
        }
    }


    /**
     * Loads the ontologies from the provided ZIP file.
     *
     * @param path      Ontologies ZIP file path
     * @return          Root ontology reference or null
     */
    protected static OWLOntology openOntologies(String path) throws Exception {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology root = null;

        try (ZipFile zip = new ZipFile(path)) {
            for (ZipEntry entry : getZipEntries(zip)) {
                InputStream in = zip.getInputStream(entry);
                root = manager.loadOntologyFromOntologyDocument(in);
            }
        }

        return root;
    }


    /**
     * Returns a list of the entries from a ZIP. This method always sorts
     * the entry named {@code ROOT_PATH} las in the list.
     *
     * @param zip           ZIP file
     * @return              Entries list
     */
    private static List<ZipEntry> getZipEntries(ZipFile zip) {
        List<ZipEntry> entries = (List) Collections.list(zip.entries());
        Collections.sort(entries, (a, b) -> cmpZipEntry(a, b));

        return entries;
    }


    /**
     * Compartes ZIP entries by name. This is a convenience comparator
     * to always sort the entries with a name of {@code ROOT_PATH} last
     * in a given collection.
     *
     * @param a             First ZIP entry
     * @param b             Second ZIP entry
     * @return              Comparision value
     */
    private static int cmpZipEntry(ZipEntry a, ZipEntry b) {
        return (ROOT_PATH.equals(a.getName())) ?  1 :
               (ROOT_PATH.equals(b.getName())) ? -1 : 0;
    }


    /**
     * Prints the entities with the given IRI and all of their
     * axioms.
     *
     * @param root         Root ontology
     * @param iri          Entity IRI
     */
    private static void print(final OWLOntology root, IRI iri) {
        Ontologies.entities(root, iri).forEach(entity -> {
            print(entity);

            Ontologies.assertions(root, entity)
                .forEach(a -> print(a));
        });
    }


    /**
     * Prints the given entity as a string.
     *
     * @param entity       Entity to print
     */
    private static void print(OWLEntity entity) {
        String value = toString(entity);
        System.out.printf("\n%s\n", value);
    }


    /**
     * Prints the given axiom as a predicate-string pair.
     *
     * @param axiom        Axiom to parse and print
     */
    private static void print(OWLAxiom axiom) {
        Object value = Values.from(axiom);
        DucVocabulary predicate = Predicates.from(axiom);

        if (predicate == null) {
            return;
        }

        if (value instanceof OWLLiteral) {
            value = toString((OWLLiteral) value);
        }

        if (value instanceof OWLEntity) {
            value = toString((OWLEntity) value);
        }

        System.out.printf("  %-15s %s\n", predicate, value);
    }


    /**
     * Converts an enity into a string representation.
     *
     * @param entity        OWL entity
     * @return              String representation
     */
    private static String toString(OWLEntity entity) {
        EntityType<?> type = entity.getEntityType();
        IRI iri = entity.getIRI();

        return String.format("%s(%s)", type, iri);
    }


    /**
     * Converts a literal into a string representation. This is used
     * to unifiy the string representation of literals.
     *
     * @param literal       OWL literal
     * @return              String representation
     */
    private static String toString(OWLLiteral literal) {
        String value = literal.getLiteral();
        String lang = literal.getLang();

        if (value instanceof String) {
            value = value.replaceAll("\"", "\\\"");
        }

        return (literal.hasLang() == true) ?
            String.format("\"%s\"@%s", value, lang) :
            String.format("\"%s\"", value);
    }

}
