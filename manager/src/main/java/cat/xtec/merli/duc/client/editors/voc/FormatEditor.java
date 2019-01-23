package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.voc.Format;


/**
 * Physical and media types. This includes the supported MIME types
 * and ISBD physical media types.
 */
public class FormatEditor extends ListBoxEditor<Format> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-FormatEditor";


    /**
     * Constructs a format editor.
     */
    public FormatEditor() {
        super(Format.class);
        setStylePrimaryName(STYLE_NAME);
    }

}