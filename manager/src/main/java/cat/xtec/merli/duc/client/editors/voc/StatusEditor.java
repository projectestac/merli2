package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.voc.Status;


/**
 * The state or condition of a learning object (LOMv1.0).
 */
public class StatusEditor extends ListBoxEditor<Status> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-StatusEditor";


    /**
     * Constructs a Status editor.
     */
    public StatusEditor() {
        super(Status.class);
        setStylePrimaryName(STYLE_NAME);
    }

}