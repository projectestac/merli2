package cat.xtec.merli.parser;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;
import java.lang.reflect.Field;
import com.google.common.collect.Iterables;
import com.google.common.graph.Traverser;

import cat.xtec.merli.bind.*;
import cat.xtec.merli.parser.util.*;


/**
 * Encapsulates the binding domain and provides methods to interact
 * with domain objects.
 *
 * The context may be seen as a two-way dictionary of domain tokens and
 * their definitions. For each main class a map ({@code DucDomain}) is
 * created wich stores the vocabulary ({@code DucVocabulary}) used on that
 * class and the definition of each term ({@code DucProperty}).
 */
public final class DucContext {

    /** Maps predicates to properties */
    protected Map<Class, DucDomain> domains = new HashMap<>();

    /** Maps types to factories */
    protected Map<Class, DucFactory> factories = new HashMap<>();

    /** Maps fields to properties */
    protected Map<Field, DucProperty> properties = new HashMap<>();


    /**
     * Creates a new context instance.
     *
     * @param types         Types to be bound
     */
    private DucContext(Class<?>[] types) {
        for (Class<?> type : types) {
            initialize(type);
        }

        for (Class<?> type : domains.keySet()) {
            categorize(type);
        }
    }


    /**
     * Creates a new context instance.
     *
     * @param types         Type to recognize
     */
    public static DucContext newInstance(Class<?>[] types) {
        return new DucContext(types);
    }


    /**
     * Creates a new parser for this context.
     *
     * @return              New parser
     */
    public DucParser createParser() {
        return new DucParser(this);
    }


    /**
     * Creates a new scanner for this context.
     *
     * @return              New scanner
     */
    public DucScanner createScanner() {
        return new DucScanner(this);
    }


    /**
     * Obtains the domain associated with the given type.
     *
     * @param type          Domain type
     * @return              Domain instance
     */
    protected DucDomain getDomain(Class<?> type) {
        return domains.get(type);
    }


    /**
     * Obtains the factory associated with the provided type. Notice
     * that factories are only defined for leaf types on the domain.
     *
     * @param type          Class reference
     * @return              Factory instance or {@code null}
     */
    public DucFactory getFactory(Class<?> type) {
        return factories.get(type);
    }


    /**
     * Obtains the property associated with the given predicate
     * for the provided type.
     *
     * @param type          Domain type
     * @param voc           Vocabulary value
     *
     * @return              Property instance or {@code null}
     */
    public DucProperty getProperty(Class<?> type, DucVocabulary voc) {
        DucDomain domain = getDomain(type);
        DucProperty property = domain.get(voc);

        while (property != null && property.isContainer()) {
            domain = getDomain(property.getDataType());
            property = domain.get(voc);
        }

        return property;
    }


    /**
     * Inititalizes the context maps for the given type.
     *
     * @param type          Type to bind
     */
    private void initialize(Class<?> type) {
        if (Types.isBindable(type) && !domains.containsKey(type)) {
            domains.put(type, new DucDomain(type));

            for (Field field : Types.getTypeFields(type)) {
                if (!Fields.isAccessible(field)) {
                    continue;
                }

                DucProperty property = new DucProperty(field);
                Class<?> dataType = property.getDataType();

                if (!factories.containsKey(dataType)) {
                    DucFactory factory = new DucFactory(dataType);
                    factories.put(dataType, factory);
                }

                properties.put(field, property);
                initialize(dataType);
            }
        }
    }


    /**
     * Inititalizes the domain for the given type.
     *
     * This method extracts the predicates defined for the given type
     * and adds them to the type's domain object. This creates an index
     * where each leaf field is assigned to a unique predicate.
     *
     * @param type          Initialized type
     */
    private void categorize(Class<?> type) {
        DucDomain domain = domains.get(type);

        for (Field root : Types.getTypeFields(type)) {
            if (Fields.isAccessible(root)) {
                DucProperty property = properties.get(root);

                traverser.breadthFirst(root).forEach(field -> {
                    if (isDomainField(field) == true) {
                        DucVocabulary voc = Fields.getPredicate(field);
                        domain.put(voc, property);
                    }
                });
            }
        }
    }


    /**
     * Tree traverser of binding fields. Generates a tree of mapped
     * fields for an object, where the childs of a field are the
     * accessible properties of the field's data type.
     */
    private Traverser<Field> traverser = Traverser.forTree(field -> {
        final Class<?> type = Fields.getDataType(field);
        final List<Field> childs = Types.getTypeFields(type);

        if (isLeafField(field)) {
            return Collections.emptyList();
        }

        return Iterables.filter(childs, child -> {
            return Fields.isAccessible(child) ||
                   Fields.isConstant(child);
        });
    });


    /**
     * Checks if the field belongs to a domain. That is, if it has a
     * bind type or is a relation constant.
     *
     * @param field         Field instance
     * @return              If a domain field
     */
    private boolean isDomainField(Field field) {
        return Fields.isConstant(field) ||
               Fields.hasBindType(field);
    }


    /**
     * Checks if the field is terminal. That is, if the children of
     * the field must not be parsed.
     *
     * @param field         Field instance
     * @return              If a terminal field
     */
    private boolean isLeafField(Field field) {
        Class<?> type = Fields.getDataType(field);

        if (field.isEnumConstant() || Fields.isTarget(field)) {
            return true;
        }

        if (Fields.hasBindType(field)) {
            return !Types.hasRelationTarget(type);
        }

        if (!Types.hasBindType(type)) {
            return !Fields.isPredicate(field);
        }

        return false;
    }

}
