package cat.xtec.merli.parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import cat.xtec.merli.parser.util.*;
import cat.xtec.merli.bind.*;


/**
 * Methods to instantiate and obtain information from a type.
 */
public class DucFactory {

    /** No-args constructor */
    protected Constructor<?> constructor;

    /** Creator method (factory) */
    protected Method creator;

    /** Target mutator method */
    protected Method targetSetter;

    /** Predicate mutator method */
    protected Method predicateSetter;

    /** Getter for the identifier value */
    protected Method idGetter;

    /** Accessor for the locale tag */
    protected Method localeGetter;

    /** Accessor for the string value */
    protected Method stringGetter;

    /** Getter for the target of a relation */
    protected Method targetGetter;

    /** Map of predicates for a relation factory */
    private Map<DucVocabulary, Enum<?>> constants;


    /**
     * Class constructor
     */
    public DucFactory(Class<?> type) {
        this.constants = makeConstants(type);
        this.creator = Types.getCreatorMethod(type);
        this.constructor = Types.getConstructor(type);
        this.targetSetter = getTargetMutator(type);
        this.predicateSetter = getPredicateMutator(type);
        this.localeGetter = getLocaleAccessor(type);
        this.stringGetter = getStringAccessor(type);
        this.targetGetter = getTargetAccessor(type);
        this.idGetter = getIdentifierAccessor(type);
    }


    /**
     * Creates a new instance of the type encapsulated by this factory
     * by calling the constructor of the type.
     *
     * @return          New instance
     */
    public Object newInstance() throws Exception {
        return constructor.newInstance();
    }


    /**
     * Creates a new instance of the type encapsulated by this factory
     * by calling the factory method of the type.
     *
     * @param string    String representation
     * @return          New instance
     */
    public Object newInstance(String value) throws Exception {
        return creator.invoke(null, value);
    }


    /**
     * Creates a new instance of the type encapsulated by this factory
     * by calling the factory method of the type.
     *
     * @param string    String representation
     * @param string    Locale tag value
     *
     * @return          New instance
     */
    public Object newInstance(String value, String locale) throws Exception {
        return creator.invoke(null, value, locale);
    }


    /**
     * Creates a new instance of the relation type encapsulated by this
     * factory by calling the constructor of the type.
     *
     * @param value     Target of the relation
     * @param voc       Predicate of the relation
     *
     * @return          New instance
     */
    public Object newInstance(Object value, DucVocabulary voc) throws Exception {
        Object result = newInstance();
        Enum<?> constant = constants.get(voc);

        if (constant instanceof Enum) {
            targetSetter.invoke(result, value);
            predicateSetter.invoke(result, constant);
        }

        return result;
    }


    /**
     * Obtains the locale tag for the given object.
     *
     * That is, the value for the accessor annotated with {@code DucLocale}
     * or {@code null} if no such method exists on the type. The locale
     * tag is used to instantiate an object if a {@code DucCreator} for
     * the type is defined.
     *
     * @param object    Target object
     * @return          Locale tag or null
     */
    public String getLocale(Object object) {
        return (String) invoke(localeGetter, object);
    }


    /**
     * Obtains a string representation for the given object.
     *
     * The string representation is used to instantiate an object when
     * a {@code DucCreator} factory method exits for a type. If the type
     * is not annotated with {@code DucString} this method defaults to
     * invoking {@code String.valueOf}.
     *
     * @param object    Target object
     * @return          String representation
     */
    public String getString(Object object) {
        Object value = invoke(stringGetter, object);
        return String.valueOf(value != null ? value : object);
    }


    /**
     * Obtains the target object of an explicit relation.
     *
     * @param object    Target object
     * @return          Relation target value
     */
    public Object getTarget(Object object) {
        return invoke(targetGetter, object);
    }


    /**
     * Obtains the entity identifier for the given object.
     *
     * If this is a factory for an explicit relation the identifier
     * for the relation's target is returned, otherwise this method
     * returns the identifier for this factory's type.
     *
     * @param object    Target object
     * @return          A string value
     */
    public String getIdentifier(Object object) {
        return (targetGetter instanceof Method) ?
            String.valueOf(invoke(idGetter, getTarget(object))) :
            String.valueOf(invoke(idGetter, object));
    }


    /**
     * Invokes a method with the given object as a parameter. If the
     * provided method is null this function returns null.
     *
     * @param method        Method to invoke
     * @param object        Target object
     *
     * @return              Result of the invokation
     */
    private Object invoke(Method method, Object object) {
        try {
            return method.invoke(object);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Creates a map of predicate constants for a relation type. If the
     * type is not an explicit relation (it does not have a field annotated
     * with {@code DucPredicate}) this method returns null.
     *
     * @param type          Relation type
     * @return              Constants map or null
     */
    private Map<DucVocabulary, Enum<?>> makeConstants(Class<?> type) {
        Class<?> ctype = getPredicateType(type);

        if (ctype instanceof Class == false) {
            return null;
        }

        Map<DucVocabulary, Enum<?>> constants = new HashMap<>();

        for (Enum<?> constant : (Enum[]) ctype.getEnumConstants()) {
            DucVocabulary predicate = getEnumPredicate(constant);

            if (predicate instanceof DucVocabulary) {
                constants.put(predicate, constant);
            }
        }

        return constants;
    }


    /**
     * Obtain the predicate data type for a relation.
     *
     * @param type          Relation type
     * @return              Class reference or null
     */
    private Class<?> getPredicateType(Class<?> type) {
        Field field = Types.getAnnotatedField(type, DucPredicate.class);
        return (field != null) ? field.getType() : null;
    }


    /**
     * Obtain the predicate for the given enumeration constant. Returns
     * {@code null} if the constant does not map to a predicate.
     *
     * @param constant      Enumeration constant
     * @return              Vocabulary value or null
     */
    private DucVocabulary getEnumPredicate(Enum<?> constant) {
        try {
            Field field = Fields.getEnumField(constant);
            return field.getAnnotation(DucConstant.class).value();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Obtains the mutator method for the predicate of a relation.
     *
     * @param type          Relation type
     * @return              A method or null
     */
    private Method getPredicateMutator(Class<?> type) {
        return Types.getMutatorMethod(type, DucPredicate.class);
    }


    /**
     * Obtains the mutator method for the target of a relation.
     *
     * @param type          Relation type
     * @return              A method or null
     */
    private Method getTargetMutator(Class<?> type) {
        return Types.getMutatorMethod(type, DucTarget.class);
    }


    /**
     * Obtains the accessor method for the locale tag.
     *
     * @param type          Domain type
     * @return              A method or null
     */
    private Method getLocaleAccessor(Class<?> type) {
        return Types.getAccessorMethod(type, DucLocale.class);
    }


    /**
     * Obtains the accessor method for the string representation.
     *
     * @param type          Domain type
     * @return              A method or null
     */
    private Method getStringAccessor(Class<?> type) {
        return Types.getAccessorMethod(type, DucString.class);
    }


    /**
     * Obtains the accessor method for the relation target.
     *
     * @param type          Domain type
     * @return              A method or null
     */
    private Method getTargetAccessor(Class<?> type) {
        return Types.getAccessorMethod(type, DucTarget.class);
    }


    /**
     * Obtains the accessor method for the entity identifier.
     *
     * If this factory's domain type is an explicit relation, the
     * accessor for the {@code DucIdentifier} of the relation target
     * type is returned. Otherwise, this method returns the accessor
     * for the field annotated with {@code DucIdentifier} on this
     * type or {@code null} if non exits.
     *
     * @param type          Domain type
     * @return              A method or null
     */
    private Method getIdentifierAccessor(Class<?> type) {
        Field field = Types.getAnnotatedField(type, DucTarget.class);
        Class<?> ctype = (field == null) ? type : field.getType();
        return Types.getAccessorMethod(ctype, DucIdentifier.class);
    }

}
