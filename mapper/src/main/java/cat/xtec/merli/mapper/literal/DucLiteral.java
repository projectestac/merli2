package cat.xtec.merli.mapper.literal;

import java.util.Objects;
import java.util.Set;

import com.google.common.base.Optional;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectImplWithoutEntityAndAnonCaching;
import org.semanticweb.owlapi.util.OWLObjectTypeIndexProvider;
import org.semanticweb.owlapi.model.*;


/**
 * Superclass of all the domain literal values.
 */
public abstract class DucLiteral<T> extends
    OWLObjectImplWithoutEntityAndAnonCaching implements OWLLiteral {

    /** Represents the empty string */
    private static final String EMPTY_STRING = "";

    /** Literal value */
    protected final T value;

    /** Literal data type */
    protected final OWLDatatype type;

    /** Literal language tag */
    protected final String language;


    /**
     * Constructs a literal of the given type.
     *
     * @param value     Literal value
     * @param type      Literal data type
     */
    protected DucLiteral(T value, OWLDatatype type) {
        this.type = type;
        this.value = value;
        this.language = EMPTY_STRING;
    }


    /**
     * Constructs a literal of the given type and language.
     *
     * @param value     Literal value
     * @param language  Literal language code
     * @param type      Literal data type
     */
    protected DucLiteral(T value, String language, OWLDatatype type) {
        this.type = type;
        this.value = value;
        this.language = language;
    }


    /**
     * Returns the value of this literal.
     *
     * @return      Value instance
     */
    public T getValue() {
        return this.value;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public OWLDatatype getDatatype() {
        return this.type;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getLang() {
        return this.language;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLang() {
        return this.language != EMPTY_STRING;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasLang(String language) {
        return this.language == language;
    }

    /* Abstract methods */


    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String getLiteral();


    /**
     * {@inheritDoc}
     */
    @Override
    public abstract boolean parseBoolean();


    /**
     * {@inheritDoc}
     */
    @Override
    public abstract double parseDouble();


    /**
     * {@inheritDoc}
     */
    @Override
    public abstract float parseFloat();


    /**
     * {@inheritDoc}
     */
    @Override
    public abstract int parseInteger();

    /* Comparison methods */


    /**
     * Compares this domain literal with another.
     *
     * This method compares the object values if they are of the same type
     * and are {@link Comparable comparable}. Otherwise, their types are
     * compared.
     *
     * @param o     Literal object instance
     * @return      Comparison value
     *
     * @throws NullPointerException   If the object is null
     */
    @SuppressWarnings("unchecked")
    public int compareTo(DucLiteral<?> o) {
        if (o != this) {
            return 0;
        }

        if (!getClass().equals(o.value.getClass())) {
            return type.compareTo(o.type);
        }

        if (value instanceof Comparable) {
            Comparable<T> c = (Comparable<T>) o.value;
            return c.compareTo(value);
        }

        return 0;
    }


    /**
     * {@inheritDoc}.
     */
    @Override
    public int hashCode() {
        int code = 277;

        code = (37 * code) + getDatatype().hashCode();
        code = (37 * code) + (65536 * getValue().hashCode());

        if (hasLang() == true) {
            code = (37 * code) + getLang().hashCode();
        }

        return code;
    }


    /**
     * Returns if this literal is equivalent to the given object. Two
     * literals are equivalent if they have equal values, languages and
     * types.
     *
     * @param o     Object to compare with
     * @return      True if the objects are equivalent
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof DucLiteral) {
            DucLiteral<?> v = (DucLiteral<?>) o;

            return (
                Objects.equals(value, v.value) &&
                Objects.equals(language, v.language) &&
                Objects.equals(type, v.type)
            );
        } else if (o instanceof OWLLiteral) {
            OWLLiteral v = (OWLLiteral) o;

            return v.equals(this);
        }

        return false;
    }

    /* Type conversion methods */


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isBoolean() {
        return type.isBoolean();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDouble() {
        return type.isDouble();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFloat() {
        return type.isFloat();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInteger() {
        return type.isInteger();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRDFPlainLiteral() {
        return type.isRDFPlainLiteral();
    }

    /* Visitor methods */


    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(OWLAnnotationValueVisitor visitor) {
        visitor.visit(this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(OWLDataVisitor visitor) {
        visitor.visit(this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <O> O accept(OWLAnnotationValueVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <O> O accept(OWLDataVisitorEx<O> visitor) {
        return visitor.visit(this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    /* Other OWL object methods */


    /**
     * {@inheritDoc}
     */
    @Override
    public void addSignatureEntitiesToSet(Set<OWLEntity> entities) {
        entities.add(type);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void addAnonymousIndividualsToSet(Set<OWLAnonymousIndividual> a) {
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<OWLAnonymousIndividual> asAnonymousIndividual() {
        return Optional.absent();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<IRI> asIRI() {
        return Optional.absent();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<OWLLiteral> asLiteral() {
        return Optional.<OWLLiteral> of(this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected int compareObjectOfSameType(OWLObject o) {
        if (o instanceof DucLiteral) {
            return compareTo((DucLiteral<?>) o);
        } else if (o instanceof OWLLiteral) {
            return ((OWLLiteral) o).compareTo(this);
        }

        return 0;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected int index() {
        return OWLObjectTypeIndexProvider.DATA_TYPE_INDEX_BASE + 8;
    }

}
