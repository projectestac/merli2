package cat.xtec.merli.duc.client.editors.lists;

import cat.xtec.merli.domain.lom.Resource;


/**
 * Editor for a dynamic list of relations.
 */
public class ResourceRelationListEditor extends RelationListEditor {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-ResourceRelationListEditor";


    /**
     * Editor constructor.
     */
    public ResourceRelationListEditor() {
        super(Resource.class);
        setStyleName(STYLE_NAME);
    }

}
