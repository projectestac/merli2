package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.CheckBoxEditor;
import cat.xtec.merli.domain.voc.Context;


/**
 * Typical learning environment where use of a learning object is intended
 * to take place (LOMv1.0).
 */
public class ContextEditor extends CheckBoxEditor<Context> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-ContextEditor";


    /**
     * Constructs a context editor.
     */
    public ContextEditor() {
        super(Context.class);
        setStylePrimaryName(STYLE_NAME);
    }

}