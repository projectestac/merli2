package cat.xtec.merli.domain.taxa;


/**
 * An entity can be marked with a number of this flags to assert a
 * boolean property about it.
 *
 * This is a convenience enumeration that can be used to store extra
 * information on an entity. Notice thought, that the absence of a flag
 * on an entity does not necessarily indicate that the property does not
 * hold true for it, maybe only that it wasn't checked.
 */
public enum EntityFlag {

    /** Represents a class */
    IS_CLASS,

    /** Represents an individual */
    IS_INDIVIDUAL,

    /** Non terminal node */
    HAS_CHILDREN;

}
