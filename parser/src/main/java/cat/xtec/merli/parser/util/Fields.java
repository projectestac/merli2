package cat.xtec.merli.parser.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import cat.xtec.merli.bind.*;


/**
 * Contains methods to extract binding information from class fields.
 */
public final class Fields {

    /**
     * Annotations that define a bindable field.
     */
    static final Class<?>[] TYPES = {
        DucIdentifier.class,
        DucAnnotation.class,
        DucAttribute.class,
        DucRelation.class
    };


    /** Prevent the instantiation */
    private Fields() {}


    /**
     * Checks if the field can be mapped to a type. That is, if it
     * is annotated with a bind type and it is mutable.
     *
     * @see                 #isMutable(Field)
     * @see                 #hasBindType(Field)
     * @param field         Field instance
     * @return              True if assignable
     */
    public static boolean isBindable(Field field) {
        return isMutable(field) && hasBindType(field);
    }


    /**
     * Checks if the field's value can be accessed. That is, if the
     * field is not transient and a getter method for it exists.
     *
     * @param field         Field instance
     * @return              True if accessible
     */
    public static boolean isAccessible(Field field) {
        return !isTransient(field) && hasAccessorMethod(field);
    }


    /**
     * Checks if the field's value can be mutated. That is, if the
     * field is not transient and a setter method for it exists.
     *
     * @param field         Field instance
     * @return              True if mutable
     */
    public static boolean isMutable(Field field) {
        return !isTransient(field) && hasMutatorMethod(field);
    }


    /**
     * Checks if the field's type is a collection.
     *
     * @param field         Field instance
     * @return              True if a collection
     */
    public static boolean isCollection(Field field) {
        return Collection.class.isAssignableFrom(field.getType());
    }


    /**
     * Checks if the field is transient. A field is transient if it
     * has a static modifier or it is annotated as transient. Note
     * that by definition, enumeration constants are always transient.
     *
     * @param field         Field instance
     * @return              True if transient
     */
    public static boolean isTransient(Field field) {
        return hasAnnotation(field, DucTransient.class) ||
               Modifier.isStatic(field.getModifiers());
    }


    /**
     * Checks if the field is an enumeration constant annotated
     * with a valid bind type of {@code DucConstant}.
     *
     * @param field         Field instance
     * @return              True if a constant
     */
    public static boolean isConstant(Field field) {
        return hasAnnotation(field, DucConstant.class) &&
               field.isEnumConstant();
    }


    /**
     * Checks if the given field is annotated as a predicate.
     *
     * @param field         Field instance
     * @return              True if a relation predicate
     */
    public static boolean isPredicate(Field field) {
        return hasAnnotation(field, DucPredicate.class);
    }


    /**
     * Checks if the given field is annotated as a target.
     *
     * @param field         Field instance
     * @return              True if a relation target
     */
    public static boolean isTarget(Field field) {
        return hasAnnotation(field, DucTarget.class);
    }


    /**
     * Checks if the field is annnotated with a valid bind type.
     * Notice that {@code DucContainer} is not a bind type.
     *
     * @see                 #getBindType(Field).
     * @param field         Field instance
     * @return              True if it has a valid annotation
     */
    public static boolean hasBindType(Field field) {
        return getBindType(field) != DucContainer.class;
    }


    /**
     * Checks if a public getter method exists for the field.
     *
     * @param field         Field instance
     * @return              If a getter exists
     */
    public static boolean hasAccessorMethod(Field field) {
        return getAccessorMethod(field) != null;
    }


    /**
     * Checks if a public setter method exists for the field.
     *
     * @param field         Field instance
     * @return              If a setter exists
     */
    public static boolean hasMutatorMethod(Field field) {
        return getMutatorMethod(field) != null;
    }


    /**
     * Obtains the type of the field's elements.
     *
     * That is, the type that can be assigned to the field. If the field
     * is a collection then the returned type is the generic type of the
     * field; otherwise, the field's type is returned as is.
     *
     * @param field         Field instance
     * @return              Element type for the field
     */
    public static Class<?> getDataType(Field field) {
        return isCollection(field) ? getGenericType(field) : field.getType();
    }


    /**
     * Obtains the bind type of the field.
     *
     * The bind type is the class with which the field is annotated and
     * that describes the type of value to which it will be mapped. If
     * the field is not annotated, {@code DucContainer} is returned.
     *
     * @param field         Field instance
     * @return              Annotation class
     */
    public static Class<?> getBindType(Field field) {
        for (Class<?> annotation : TYPES) {
            if (hasAnnotation(field, annotation)) {
                return annotation;
            }
        }

        return DucContainer.class;
    }


    /**
     * Obtains the relation target type for the field.
     *
     * When the field's data type is an explicit relation annotated
     * with {@code DucTarget} this method return the type of the
     * target field.
     *
     * @param field         Field instance
     * @return              Target type or null
     */
    public static Class<?> getTargetType(Field field) {
        Class<?> type = Fields.getDataType(field);
        Field f = Types.getAnnotatedField(type, DucTarget.class);
        return (f != null) ? Fields.getDataType(f) : null;
    }


    /**
     * Casts an enum value to an enum constant field.
     *
     * @param value         Enumeration constant
     * @return              Field instance
     *
     * @throws IllegalArgumentException If not an enum constant
     */
    public static Field getEnumField(Enum<?> value) {
        try {
            Class<?> type = value.getClass();
            return type.getField(value.name());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }


    /**
     * Obtains the predicate of a bindable field.
     *
     * This method returns the value of the bind type annotation if
     * it exists, or {@code DucVocabulary.BOTTOM} if the field is not
     * annotated or the annotation does not have a value.
     *
     * @param field         Field instance
     * @return              Vocabulary value
     */
    public static DucVocabulary getPredicate(Field field) {
        try {
            Class type = field.isEnumConstant() ?
                DucConstant.class : getBindType(field);

            Method method = type.getMethod("value");
            Annotation annotation = field.getAnnotation(type);

            return (DucVocabulary) method.invoke(annotation);
        } catch (Exception e) {}

        return DucVocabulary.BOTTOM;
    }


    /**
     * Obtains the public getter method for a field if it exists.
     *
     * @param field         Field instance
     * @return              Getter method or null
     */
    public static Method getAccessorMethod(Field field) {
        try {
            String name = getMethodName("get", field);
            return field.getDeclaringClass().getMethod(name);
        } catch (Exception e) {}

        return null;
    }


    /**
     * Obtains the public setter method for a field if it exists.
     *
     * @param field         Field instance
     * @return              Setter method or null
     */
    public static Method getMutatorMethod(Field field) {
        try {
            Class<?> type = field.getType();
            String name = getMethodName("set", field);
            return field.getDeclaringClass().getMethod(name, type);
        } catch (Exception e) {}

        return null;
    }


    /**
     * Constructs a method name given a field and a method prefix.
     * The method name is constructed by converting the firs character
     * of the field's name to uppercase and prefixing it.
     *
     * @param prefix        Prefix for the method
     * @param field         Field instance
     * @return              Getter method or null
     */
    private static String getMethodName(String prefix, Field field) {
        return String.format("%s%C%s", prefix,
            field.getName().charAt(0),
            field.getName().substring(1)
        );
    }


    /**
     * Obtains the generic type of a field. Note that this method assumes
     * that the field has exactly one generic type and returns it.
     *
     * @param field         Field instance
     * @return              Generic type
     */
    private static Class<?> getGenericType(Field field) {
        ParameterizedType t = (ParameterizedType) field.getGenericType();
        return (Class<?>) t.getActualTypeArguments()[0];
    }


    /**
     * Returns if the field is annotated with the given class.
     *
     * @param field         Field instance
     * @param annon         Annotation class
     * @return              If the annotation is present
     */
    public static boolean hasAnnotation(Field field, Class annon) {
        return field != null && field.isAnnotationPresent(annon);
    }

}
