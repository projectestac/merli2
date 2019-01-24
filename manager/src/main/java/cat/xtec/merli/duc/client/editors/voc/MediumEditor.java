package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.voc.Medium;


/**
 * The state or condition of a learning object (LOMv1.0).
 */
public class MediumEditor extends ListBoxEditor<Medium> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-MediumEditor";


    /**
     * Constructs a Medium editor.
     */
    public MediumEditor() {
        super(Medium.class);
        setStylePrimaryName(STYLE_NAME);
    }

}
