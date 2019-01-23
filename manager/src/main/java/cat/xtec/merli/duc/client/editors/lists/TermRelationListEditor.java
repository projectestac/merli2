package cat.xtec.merli.duc.client.editors.lists;

import cat.xtec.merli.domain.taxa.Term;


/**
 * Editor for a dynamic list of relations.
 */
public class TermRelationListEditor extends RelationListEditor {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-TermRelationListEditor";


    /**
     * Editor constructor.
     */
    public TermRelationListEditor() {
        super(Term.class);
        setStyleName(STYLE_NAME);
    }

}
