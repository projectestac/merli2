package cat.xtec.merli.duc.client.editors.taxa;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.domain.taxa.Term;


/**
 * Specific kind of resource (LOMv1.0).
 */
public class TermTypeEditor extends ListBoxEditor<EntityType> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-TermTypeEditor";


    /**
     * Constructs a term type editor.
     */
    public TermTypeEditor() {
        super(EntityType.valuesFor(Term.class));
        setStylePrimaryName(STYLE_NAME);
    }

}
