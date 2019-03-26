package cat.xtec.merli.parser;

import java.util.Set;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import cat.xtec.merli.bind.DucVocabulary;


/**
 * Provides methods to extract properties from objects.
 *
 * The values for the properties may be given as a set of facts or
 * encapsulated in the domain object. Then a traversal of leaf nodes
 * is produced which gives meaning and context to each provided value.
 */
public class DucScanner {

    /** Context for this scanner */
    private DucContext context;


    /**
     * Creates a new scanner instance.
     *
     * @param context       Context instance
     */
    protected DucScanner(DucContext context) {
        this.context = context;
    }


    /**
     * Produces a traversal of leaf values on a target object. For each
     * non-null leaf value on the target object for wich a property exists
     * on the provided type's domain.
     *
     * @param target            An object from the domain
     * @param type              A domain type
     * @param consumer          Property consumer
     */
    public void forEach(Object target, Class<?> type, DucConsumer consumer) {
        DucDomain domain = context.getDomain(type);

        for (DucProperty property : domain.valueSet()) {
            Object values = property.getValue(target);

            if (values instanceof Object) {
                for (Object value : asCollection(values)) {
                    if (property.isContainer()) {
                        Class<?> subtype = property.getDataType();
                        forEach(value, subtype, consumer);
                    } else {
                        consumer.accept(property, target, value);
                    }
                }
            }
        }
    }


    /**
     * Produces a traversal of fact values on a target object. For each
     * value on the facts set for wich a property exists on the provided
     * domain and the target object.
     *
     * @param target            A domain object
     * @param facts             A set of facts to iterate
     * @param consumer          Property consumer
     */
    public void forEach(Object target, Set<DucFact> facts, DucConsumer consumer) {
        Class<?> type = target.getClass();
        DucDomain domain = context.getDomain(type);

        for (DucFact fact : facts) {
            if (fact == null) continue;
            Object value = fact.getObject();

            if (value instanceof Object) {
                DucVocabulary predicate = fact.getPredicate();
                DucProperty property = domain.get(predicate);
                Object subject = target;

                while (property != null && property.isContainer()) {
                    DucDomain subdomain = getSubdomain(property);
                    subject = property.getValue(subject);
                    property = subdomain.get(predicate);
                }

                if (property instanceof DucProperty) {
                    consumer.accept(property, subject, value);
                }
            }
        }
    }


    /**
     * Produces a traversal of properties for a domain type.
     *
     * @param type              Domain type
     * @param consumer          Property consumer
     */
    public void forEach(Class<?> type, Consumer<DucProperty> consumer) {
        DucDomain domain = context.getDomain(type);

        for (DucProperty property : domain.valueSet()) {
            if (property.isContainer()) {
                Class<?> ctype = property.getDataType();
                forEach(ctype, consumer);
            } else {
                consumer.accept(property);
            }
        }
    }


    /**
     * Obtains the domain of the given property's data type on the
     * current context.
     *
     * @param property      Property instance
     * @return              Domain instance or null
     */
    private DucDomain getSubdomain(DucProperty property) {
        return context.getDomain(property.getDataType());
    }


    /**
     * Returns the provided value as a collection. If the object is
     * already a collection it is returned as-is, otherwise, a new
     * array list with the value is constructed.
     *
     * @param value         An object instance
     * @return              Collection instance
     */
    private Collection asCollection(Object value) {
        return (value instanceof Collection) ?
            (Collection) value : Arrays.asList(value);
    }

}
