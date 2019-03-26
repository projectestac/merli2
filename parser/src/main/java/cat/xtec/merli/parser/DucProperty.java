package cat.xtec.merli.parser;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

import cat.xtec.merli.parser.util.*;
import cat.xtec.merli.bind.*;


/**
 * Encapsulates a field of a binding type and provides methods to
 * obtain and set the field's values on intances of the type.
 */
public class DucProperty {

    /** Assignable data type */
    protected Class<?> dataType;

    /** Annotated bind type */
    protected Class<?> bindType;

    /** Annotated relation target type */
    protected Class<?> targetType;

    /** Getter for the property */
    protected Method accessor;

    /** Setter for the property */
    protected Method mutator;

    /** Getter for the predicate */
    protected Method predicator;

    /** Predicate value */
    protected DucVocabulary predicate;


    /**
     * Class constructor
     */
    public DucProperty(Field field) {
        this.dataType = Fields.getDataType(field);
        this.bindType = Fields.getBindType(field);
        this.targetType = Fields.getTargetType(field);
        this.predicate = Fields.getPredicate(field);
        this.accessor = Fields.getAccessorMethod(field);
        this.mutator = Fields.getMutatorMethod(field);
        this.predicator = getPredicateAccessor(dataType);
    }


    /**
     * Returns this object's data type.
     *
     * @see             Fields#getDataType
     * @return          Data type class
     */
    public Class<?> getDataType() {
        return dataType;
    }


    /**
     * Returns this object's bind type.
     *
     * @see             Fields#getBindType
     * @return          Bind type class
     */
    public Class<?> getBindType() {
        return bindType;
    }


    /**
     * Returns this object's relation target type.
     *
     * @see             Fields#getTargetType
     * @return          Target type class
     */
    public Class<?> getTargetType() {
        return targetType;
    }


    /**
     * Checks if this property binds an aggregate element. Which
     * is a bundle that contains a set of bindings.
     *
     * @return          If an aggregate element
     */
    public boolean isContainer() {
        return bindType == DucContainer.class;
    }


    /**
     * Checks if this property binds a relation element.
     *
     * @return          If a relation element
     */
    public boolean isRelation() {
        return bindType == DucRelation.class;
    }


    /**
     * Checks if this property binds an identifier.
     *
     * @return          If a relation element
     */
    public boolean isIdentifier() {
        return bindType == DucIdentifier.class;
    }


    /**
     * Checks if this property binds an annotation.
     *
     * @return          If a relation element
     */
    public boolean isAnnotation() {
        return bindType == DucAnnotation.class;
    }


    /**
     * Checks if this property binds an attribute.
     *
     * @return          If a relation element
     */
    public boolean isAttribute() {
        return bindType == DucAttribute.class;
    }


    /**
     * Checks if this property binds a collection element.
     *
     * @return          If a collection element
     */
    public boolean isCollection() {
        Class<?> type = accessor.getReturnType();
        return Collection.class.isAssignableFrom(type);
    }


    /**
     * Checks if this property binds a relation container. That
     * is, a relation whose data type has a target element.
     *
     * @return          If a relation container
     */
    public boolean isRelationContainer() {
        return isRelation() && targetType != null;
    }


    /**
     * Obtains the value of the field represented by this property
     * from the given object. Returns {@code null} if the value could
     * not been obtained.
     *
     * @param object    Target object
     * @return          A value
     */
    public Object getValue(Object object) {
        return invoke(accessor, object);
    }


    /**
     * Obtains the predicate for this property on the given target.
     * Returns {@code null} if the predicate could not been obtained.
     *
     * This is a convenience method that first checks if an explicit
     * predicate (a field annotated as {@code DucPredicate}) exists on
     * this property's data type and returns its predicate. If no
     * explicit predicate exists, this method returns the predicate
     * definied for this property's bind type.
     *
     * @param object    Target object
     * @return          A vocabulary value
     */
    public DucVocabulary getPredicate(Object object) {
        Enum<?> constant = (Enum<?>) invoke(predicator, object);
        DucVocabulary value = getEnumPredicate(constant);

        return (value != null) ? value : predicate;
    }


    /**
     * Sets the value of the field represented by this property.
     *
     * If this property is a collection, the value will be appended to
     * the collection obtained from the field; otherwise, the current
     * value on the object will be replaced.
     *
     * @param object    Target object
     * @param value     New value
     */
    public void setValue(Object object, Object value) {
        if (isCollection() == true) {
            appendValue(object, value);
        } else {
            replaceValue(object, value);
        }
    }


    /**
     * Replaces the value of the field represented by this property.
     *
     * @param object    Target object
     * @param value     New value
     */
    private void replaceValue(Object object, Object value) {
        invoke(mutator, object, value);
    }


    /**
     * Appends a value to the collection returned by the field which
     * this property represents. Does nothing if the field does not
     * return a collection instance.
     *
     * @param object    Target object
     * @param value     New value
     */
    private void appendValue(Object object, Object value) {
        try {
            Collection items = (Collection) getValue(object);
            items.add(value);
        } catch (Exception e) {}
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
     * Obtains the accessor method for the predicate of a relation.
     *
     * @param type          Relation type
     * @return              Class reference or null
     */
    private Method getPredicateAccessor(Class<?> type) {
        return Types.getAccessorMethod(type, DucPredicate.class);
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
     * Invokes a method with the given object as a parameter. If the
     * provided method is null this function returns null.
     *
     * @param method        Method to invoke
     * @param object        Target object
     * @param value         Method parameter
     *
     * @return              Result of the invokation
     */
    private Object invoke(Method method, Object object, Object value) {
        try {
            return method.invoke(object, value);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("%s(%s)",
            bindType.getSimpleName(),
            dataType
        );
    }

}
