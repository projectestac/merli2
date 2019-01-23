package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.voc.Copyright;


/**
 * Whether copyright or other restrictions apply
 */
public class CopyrightEditor extends ListBoxEditor<Copyright> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-CopyrightEditor";


    /**
     * Constructs a Copyright editor.
     */
    public CopyrightEditor() {
        super(Copyright.class);
        setStylePrimaryName(STYLE_NAME);
    }

}