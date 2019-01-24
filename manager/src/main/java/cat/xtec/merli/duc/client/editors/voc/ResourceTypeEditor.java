package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.CheckBoxEditor;
import cat.xtec.merli.domain.voc.ResourceType;


/**
 * Specific kind of resource (LOMv1.0).
 */
public class ResourceTypeEditor extends CheckBoxEditor<ResourceType> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-ResourceTypeEditor";


    /**
     * Constructs a resourcetype editor.
     */
    public ResourceTypeEditor() {
        super(ResourceType.class);
        setStylePrimaryName(STYLE_NAME);
    }

}
