package cat.xtec.merli.parser;

import cat.xtec.merli.bind.DucVocabulary;


/**
 * Encapsulates a vocabulary predicate along with an object value
 * linked to the predicate.
 */
public final class DucFact {

    /** Assertion predicate */
    private DucVocabulary predicate;

    /** Assertion object value */
    private Object object;


    /**
     * Constructs a new entry object.
     *
     * @param predicate     A vocabulary token
     * @param object        Value for the predicate
     */
    public DucFact(DucVocabulary predicate, Object object) {
        this.predicate = predicate;
        this.object = object;
    }


    /**
     * Returns this object's predicate.
     *
     * @return          Vocabulary instance
     */
    public DucVocabulary getPredicate() {
        return predicate;
    }


    /**
     * Returns this object's value.
     *
     * @return          Value instance
     */
    public Object getObject() {
        return object;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("<%s, %s>", predicate, object);
    }

}
