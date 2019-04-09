package cat.xtec.merli.mapper;

import java.util.List;
import java.util.Collections;
import org.semanticweb.owlapi.model.*;


/**
 * Encapsulates a list of changes on an ontology.
 */
public class OWLChanges {

    /** Empty changes instance */
    static final OWLChanges EMPTY_CHANGES = new OWLChanges();

    /** Ontology change entries */
    private List<OWLOntologyChange> entries;


    /**
     * Creates a new empty changes instance.
     */
    private OWLChanges() {
        this.entries = Collections.emptyList();
    }


    /**
     * Creates a new OWL changes instance from the given list
     * of ontology changes.
     *
     * @param entries       Ontology changes list
     */
    public OWLChanges(List<OWLOntologyChange> entries) {
        this.entries = entries;
    }


    /**
     * Applies this changes into the provided ontology.
     *
     * @param root          Root ontology
     */
    public void commit(OWLOntology root) {
        commit(root.getOWLOntologyManager());
    }


    /**
     * Applies this changes into the provided manager.
     *
     * @param manager       Changes manager
     */
    public void commit(HasApplyChanges manager) {
        manager.applyChanges(entries);
    }


    /**
     * Returns an immutable instance of this class with no entries.
     * This method efectively returns {@code EMPTY_CHANGES}.
     *
     * @return          Changes instance
     */
    public static OWLChanges empty() {
        return EMPTY_CHANGES;
    }


    /**
     * {@inheritDoc}
     */
    public String toString() {
        return String.format("%s(%s)",
            getClass().getSimpleName(),
            String.valueOf(entries)
        );
    }

}
