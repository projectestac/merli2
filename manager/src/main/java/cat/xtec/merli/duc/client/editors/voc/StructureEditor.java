package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.voc.Structure;


/**
 * Underlying organizational structure of a learning object (LOMv1.0).
 */
public class StructureEditor extends ListBoxEditor<Structure> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-StructureEditor";


    /**
     * Constructs a Structure editor.
     */
    public StructureEditor() {
        super(Structure.class);
        setStylePrimaryName(STYLE_NAME);
    }

}