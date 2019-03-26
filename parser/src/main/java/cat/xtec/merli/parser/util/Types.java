package cat.xtec.merli.parser.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cat.xtec.merli.bind.*;


/**
 * Contains methods to extract binding information from classes.
 */
public final class Types {

    /**
     * Annotations that define a bind type.
     */
    static final Class<?>[] TYPES = {
        DucEntity.class,
        DucContainer.class
    };


    /** Prevent the instantiation */
    private Types() {}


    /**
     * Checks if the class can be mapped to an OWL type. That is, if
     * it is annotated with a bind type and can be instantiated.
     *
     * @see                 #hasDefaultConstructor(Class)
     * @see                 #hasBindType(Class)
     * @param type          Class reference
     * @return              True if assignable
     */
    public static boolean isBindable(Class<?> type) {
        return hasDefaultConstructor(type) && hasBindType(type);
    }


    /**
     * Checks if the type is a container element. That is, if it
     * is annotated with {@code DucContainer}.
     *
     * @param type          Class reference
     * @return              True if it a container
     */
    public static boolean isContainer(Class<?> type) {
        return hasAnnotation(type, DucContainer.class);
    }


    /**
     * Checks if the class is annnotated with a valid bind type.
     *
     * @see                 #getBindType(Class).
     * @param type          Class reference
     * @return              True if it has a valid annotation
     */
    public static boolean hasBindType(Class<?> type) {
        return getBindType(type) != null;
    }


    /**
     * Checks if the class defines an explicit relationship. That is,
     * if it has a field annotated as {@code DucTarget}.
     *
     * @param type          Class reference
     * @return              True if it has a target field
     */
    public static boolean hasRelationTarget(Class<?> type) {
        return getAnnotatedField(type, DucTarget.class) != null;
    }


    /**
     * Checks if a public no-args constructor exists for the class.
     *
     * @param type          Class reference
     * @return              True if a constructor exitst
     */
    public static boolean hasDefaultConstructor(Class<?> type) {
        return getConstructor(type) != null;
    }


    /**
     * Checks if a factory method exists for the class.
     *
     * @param type          Class reference
     * @return              True if a factory exitst
     */
    public static boolean hasCreatorMethod(Class<?> type) {
        return getCreatorMethod(type) != null;
    }


    /**
     * Obtains the bind type of the class. The bind type is the
     * annotation class with which the type is annotated. That is,
     * the type of OWL entity to which the type must be mapped.
     *
     * @param type          Class reference
     * @return              Annotation class
     */
    public static Class<?> getBindType(Class<?> type) {
        for (Class<?> annotation : TYPES) {
            if (hasAnnotation(type, annotation)) {
                return annotation;
            }
        }

        return null;
    }


    /**
     * Obtains the data type of the given class. This convenience
     * method returns the provided type itself.
     *
     * @param type          Class reference
     * @return              Data type for class
     */
    public static Class<?> getDataType(Class<?> type) {
        return type;
    }


    /**
     * Obtains the public no-args constructor for the given type if
     * it exists. Otherwise, this method returns {@code null}.
     *
     * @param type          Class reference
     * @return              Constructor instance
     */
    public static Constructor<?> getConstructor(Class<?> type) {
        try {
            return type.getConstructor();
        } catch (Exception e) {}

        return null;
    }


    /**
     * Obtains the factory method for the given type if it exists.
     *
     * @param type          Class reference
     * @return              Factory method or {@code null}
     */
    public static Method getCreatorMethod(Class<?> type) {
        return getAnnotatedMethod(type, DucCreator.class);
    }


    /**
     * Obtains a list of fields declared by the given type or
     * any of its superclasses.
     *
     * @param type          Class reference
     * @return              Set of fields
     */
    public static List<Field> getTypeFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();

        for (Class<?> ctype : getTypeClasses(type)) {
            for (Field field : ctype.getDeclaredFields()) {
                fields.add(field);
            }
        }

        return fields;
    }


    /**
     * Obtains a list of methods declared by the given type or
     * any of its superclasses.
     *
     * @param type          Class reference
     * @return              Set of methods
     */
    public static List<Method> getTypeMethods(Class<?> type) {
        List<Method> methods = new ArrayList<>();

        for (Class<?> ctype : getTypeClasses(type)) {
            for (Method method : ctype.getDeclaredMethods()) {
                methods.add(method);
            }
        }

        return methods;
    }


    /**
     * Obtains the list of classes to which the given type pertains.
     * That is, the set of all its superclasses plus itself.
     *
     * @param type          Class reference
     * @return              Set of class objects
     */
    public static List<Class<?>> getTypeClasses(Class<?> type) {
        List<Class<?>> types = new ArrayList<>();

        while (type instanceof Object) {
            types.add(type);
            type = type.getSuperclass();
        }

        return types;
    }


    /**
     * Obtains the accessor method for the given annotation.
     *
     * If the annotation marks a method it is returned; otherwise, the
     * getter method for the first field annotated with the given class
     * will be returned if it exists.
     *
     * @param type          Class reference
     * @param annon         Annotation class
     *
     * @return              Accessor method or {@code null}
     */
    public static Method getAccessorMethod(Class<?> type, Class annon) {
        Method method = getAnnotatedMethod(type, annon);

        if (method instanceof Method == false) {
            Field field = getAnnotatedField(type, annon);
            method = Fields.getAccessorMethod(field);
        }

        return method;
    }


    /**
     * Obtains the mutator method for the given annotation.
     *
     * If the annotation marks a method it is returned; otherwise, the
     * setter method for the first field annotated with the given class
     * will be returned if it exists.
     *
     * @param type          Class reference
     * @param annon         Annotation class
     *
     * @return              Accessor method or {@code null}
     */
    public static Method getMutatorMethod(Class<?> type, Class annon) {
        Method method = getAnnotatedMethod(type, annon);

        if (method instanceof Method == false) {
            Field field = getAnnotatedField(type, annon);
            method = Fields.getMutatorMethod(field);
        }

        return method;
    }


    /**
     * Obtains the first public method annotated with the given class if
     * it exitst. Otherwise, this method returns {@code null}.
     *
     * @param type          Class reference
     * @param annon         Annotation class
     * @return              If the annotation is present
     */
    public static Method getAnnotatedMethod(Class<?> type, Class annon) {
        for (Method method : getTypeMethods(type)) {
            if (hasAnnotation(method, annon)) {
                return method;
            }
        }

        return null;
    }


    /**
     * Obtains the first declared field annotated with the given class
     * if it exitst. Otherwise, this method returns {@code null}.
     *
     * @param type          Class reference
     * @param annon         Annotation class
     * @return              If the annotation is present
     */
    public static Field getAnnotatedField(Class<?> type, Class annon) {
        for (Field field : getTypeFields(type)) {
            if (hasAnnotation(field, annon)) {
                return field;
            }
        }

        return null;
    }


    /**
     * Returns if the type is annotated with the given class.
     *
     * @param type          Class reference
     * @param annon         Annotation class
     * @return              If the annotation is present
     */
    public static boolean hasAnnotation(Class<?> type, Class annon) {
        return type != null && type.isAnnotationPresent(annon);
    }


    /**
     * Returns if the method is annotated with the given class.
     *
     * @param method        Method reference
     * @param annon         Annotation class
     * @return              If the annotation is present
     */
    private static boolean hasAnnotation(Method method, Class annon) {
        return method != null && method.isAnnotationPresent(annon);
    }


    /**
     * Returns if the field is annotated with the given class.
     *
     * @param field         Field instance
     * @param annon         Annotation class
     * @return              If the annotation is present
     */
    private static boolean hasAnnotation(Field field, Class annon) {
        return field != null && field.isAnnotationPresent(annon);
    }

}
