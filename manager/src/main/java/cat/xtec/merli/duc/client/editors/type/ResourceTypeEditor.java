package cat.xtec.merli.duc.client.editors.type;

import cat.xtec.merli.duc.client.editors.CheckBoxEditor;
import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.domain.lom.Resource;


/**
 * Specific kind of resource (LOMv1.0).
 */
public class ResourceTypeEditor extends CheckBoxEditor<EntityType> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-ResourceTypeEditor";


    /**
     * Constructs a resourcetype editor.
     */
    public ResourceTypeEditor() {
        super(EntityType.valuesFor(Resource.class));
        setStylePrimaryName(STYLE_NAME);
    }

}
