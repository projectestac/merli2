package cat.xtec.merli.parser;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import cat.xtec.merli.bind.DucVocabulary;
import cat.xtec.merli.parser.util.*;


/**
 * A hash map that encapsulates the domain of an entity as a set
 * of predicate-property values.
 */
public class DucDomain extends HashMap<DucVocabulary, DucProperty> {

    /** Assignable data type */
    protected Class<?> dataType;

    /** Annotated bind type */
    protected Class<?> bindType;

    /** Holds the set of unique values */
    private volatile Set<DucProperty> valueSet;


    /**
     * Creates a new domain for the specified type.
     */
    public DucDomain(Class<?> type) {
        this.dataType = Types.getDataType(type);
        this.bindType = Types.getBindType(type);
    }


    /**
     * Returns this domain's data type.
     *
     * @see             Types#getDataType
     * @return          Data type class
     */
    public Class getDataType() {
        return dataType;
    }


    /**
     * Returns this domain's bind type.
     *
     * @see             Types#getBindType
     * @return          Bind type class
     */
    public Class getBindType() {
        return bindType;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DucProperty put(DucVocabulary key, DucProperty value) {
        if (valueSet instanceof Set) {
            valueSet.add(value);
        }

        return super.put(key, value);
    }


    /**
     * Returns a {@code Set} of the values in this map.
     *
     * @return      Unique values
     */
    public Set<DucProperty> valueSet() {
        if (!(valueSet instanceof Set)) {
            valueSet = new ValueSet(values());
        }

        return valueSet;
    }


    /**
     * Implements a value set backed by this map instance. Removal
     * operations on this set are propagated to the underlying domain
     * map. Addition operations are not supported.
     */
    class ValueSet<DucProperty> extends HashSet<DucProperty> {

        /**
         * Value set constructor.
         *
         * @param values        Collection of values
         */
        ValueSet(Collection<DucProperty> values) {
            super(Math.max(16, values.size()));

            for (DucProperty value : values) {
                super.add(value);
            }
        }


        /**
         * Removes the given element from this set and all the elements
         * from the underlying domain map that point to it.
         *
         * @param key           Element to remove
         * @return              True if an item was removed
         */
        public final boolean remove(Object key) {
            boolean removed = super.remove(key);

            if (removed == true) {
                DucDomain.this.entrySet().removeIf(e -> {
                    return (key != null) ?
                        key.equals(e.getValue()) :
                        key == e.getValue();
                });
            }

            return removed;
        }


        /** Clears this set and the domain map */
        public final void clear() {
            super.clear();
            DucDomain.this.clear();
        }


        /** This operation is not implemented */
        public boolean add(DucProperty v) {
            throw new UnsupportedOperationException();
        }


        /** This operation is not implemented */
        public boolean addAll(Collection<? extends DucProperty> c) {
            throw new UnsupportedOperationException();
        }
    }

}
