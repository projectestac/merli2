package cat.xtec.merli.duc.client.editors.lists;

import cat.xtec.merli.domain.taxa.Term;


/**
 * Editor for a dynamic list of keywords.
 */
public class TermListEditor extends EntityListEditor {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-TermListEditor";

    /**
     * Editor constructor.
     */
    public TermListEditor() {
        super(Term.class);
        addStyleName(STYLE_NAME);
    }

}
