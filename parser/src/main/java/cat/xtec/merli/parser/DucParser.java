package cat.xtec.merli.parser;

import java.util.Set;
import java.util.HashSet;
import cat.xtec.merli.bind.DucVocabulary;


/**
 * Parses an composes domain objects.
 *
 * This class is used to process an object hierarchy into a plain set
 * of facts and the other way around. Notice that each assertion predicate,
 * which is a vocabulary value, uniquely identifies a field on the
 * underlying class. The context for the parser is used to map predicates
 * to the fields of the class.
 */
public class DucParser {

    /** Context for this parser */
    private DucContext context;

    /** Traverses object properties */
    private DucScanner scanner;


    /**
     * Creates a new parser instance.
     *
     * @param context       Marshalling context
     * @param ontology      Root ontology
     */
    protected DucParser(DucContext context) {
        this.context = context;
        this.scanner = context.createScanner();
    }


    /**
     * Parses a domain object into a set of facts.
     *
     * Invalid an {@code null} values are ignored. If the provided type
     * is a superclass of the given object, only the properties found
     * on the superclass will be parsed into facts.
     *
     * @param object        Domain object
     * @param type          Domain type
     * @return              A new set of facts
     *
     * @throws DucException If the given type is not from the domain
     */
    public <T> Set<DucFact> parse(T object, Class<T> type) throws DucException {
        Set<DucFact> facts = new HashSet<>();

        if (context.getDomain(type) == null) {
            throw new DucException("Not a domain type: " + type);
        }

        scanner.forEach(object, type, (property, target, value) -> {
            try {
                DucVocabulary token = property.getPredicate(value);
                facts.add(new DucFact(token, value));
            } catch (Exception e) {}
        });

        return facts;
    }


    /**
     * Composes a domain object from a set of facts.
     *
     * Invalid an {@code null} values are ignored. This method does not
     * create intermediate objects (lists or containers) which must be
     * provided by the domain type.
     *
     * @param object        Set of facts
     * @param type          Domain type
     * @return              A new object instance
     *
     * @throws DucException If the object could not be created
     */
    public <T> T compose(Set<DucFact> facts, Class<T> type) throws DucException {
        T result = null;

        try {
            result = type.newInstance();
        } catch (Exception e) {
            throw new DucException(e);
        }

        scanner.forEach(result, facts, (property, target, value) -> {
            try {
                property.setValue(target, value);
            } catch (Exception e) {}
        });

        return result;
    }

}
